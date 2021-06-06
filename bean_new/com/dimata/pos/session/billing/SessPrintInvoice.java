/*
 * SessPrintInvoice.java
 *
 * Created on January 10, 2005, 9:27 AM
 */

package com.dimata.pos.session.billing;

import com.dimata.pos.cashier.CreditPaymentModel;
import com.dimata.pos.cashier.DefaultSaleModel;
import com.dimata.pos.cashier.GiftModel;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;

import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;
/**
 *
 * @author  yogi
 */
import com.dimata.pos.printAPI.PrintInvoiceObj;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.posbo.entity.masterdata.PstMemberPoin;
import com.dimata.posbo.entity.masterdata.PstMemberReg;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

public class SessPrintInvoice {
    
    /** Creates a new instance of SessPrintInvoice */
    public SessPrintInvoice() {
    }
    
    public static PrintInvoiceObj getData(DefaultSaleModel saleModel){
        BillMain billMain = saleModel.getMainSale();
        PrintInvoiceObj obj = getData(billMain);   
        obj.setDiscount(saleModel.getDiscAmount());       
        obj.setService(saleModel.getSvcAmount());
        obj.setTotalTransaction(saleModel.getNetTrans());
        obj.setTax(saleModel.getTaxAmount());
        obj.setTotalPaid(saleModel.getTotalPayments());
        obj.setSubTotal(saleModel.getTotalTrans());
        obj.setMemberPoint(saleModel.getMemberPoin().getCredit());
        obj.setTotLastPayment(saleModel.getLastPayment());
        return obj;
    }
    
    public static PrintInvoiceObj getData(BillMain billMain){
        PrintInvoiceObj obj = new PrintInvoiceObj();
        long oidBillMain = 0;
        if(billMain!=null){
            oidBillMain = oidBillMain(billMain);
        }
        
        obj.setNoInvoice(billMain.getInvoiceNumber());
        obj.setDateInvoice(billMain.getBillDate());
        try{
            AppUser appUser = PstAppUser.fetch(billMain.getAppUserId());
            obj.setCashier(appUser.getFullName());
        }catch(Exception e){
        }
        Vector listBillDetail = getBillDetail(oidBillMain);
        obj.setListBillDetail(listBillDetail);
        obj.setListOtherCost(listOtherCost(oidBillMain));
        
        Vector listPayment = getListPayment(oidBillMain);
        obj.setListPayment(listPayment);
        obj.setTotCardCost(sumCardCost(oidBillMain));
        obj.setTotOtherCost(sumOtherCost(oidBillMain));
        
        obj = getMember(obj,billMain.getCustomerId(),oidBillMain);
     
        double totalreturn = sumReturn(oidBillMain);
    
        obj.setTotalReturn(totalreturn);
      
        return obj;
    }
    
    
    
    //set obj for invoice return
    //updated by widi
    public static PrintInvoiceObj getDataReturn(DefaultSaleModel saleModel){
        BillMain billMain = saleModel.getMainSale (); 
        PrintInvoiceObj obj = new PrintInvoiceObj();
        long oidBillMain = 0;
        if(billMain!=null){
            oidBillMain = oidBillMain(billMain);
        }
        
        obj.setNoInvoice(billMain.getInvoiceNumber());
        obj.setNoRefInvoice(billMain.getRefInvoiceNum());
        obj.setDateInvoice(billMain.getBillDate());
        try{
            AppUser appUser = PstAppUser.fetch(billMain.getAppUserId());
            obj.setCashier(appUser.getFullName());
        }catch(Exception e){
        }
        Vector listBillDetail = getBillDetail(oidBillMain);
        obj.setListBillDetail(listBillDetail);
        
   
        obj.setDiscount(saleModel.getDiscAmount ());
        obj.setService(saleModel.getSvcAmount ());
        obj.setTax(saleModel.getTaxAmount ());
        obj.setSubTotal(saleModel.getTotalTrans ());
        
        obj.setTotalTransaction(saleModel.getNetTrans ());
        
        return obj;
    }
    //set obj for invoice return
    public static PrintInvoiceObj getDataReturn(BillMain billMain){
        PrintInvoiceObj obj = new PrintInvoiceObj();
        long oidBillMain = 0;
        if(billMain!=null){
            oidBillMain = oidBillMain(billMain);
        }
        
        obj.setNoInvoice(billMain.getInvoiceNumber());
        obj.setNoRefInvoice(billMain.getRefInvoiceNum());
        obj.setDateInvoice(billMain.getBillDate());
        try{
            AppUser appUser = PstAppUser.fetch(billMain.getAppUserId());
            obj.setCashier(appUser.getFullName());
        }catch(Exception e){
        }
        Vector listBillDetail = getBillDetail(oidBillMain);
        obj.setListBillDetail(listBillDetail);
        
        //Vector listPayment = getListPayment(oidBillMain);
        //obj.setListPayment(listPayment);
        
        //obj = getMember(obj,billMain.getCustomerId());
        double subTotalTrans = sumSubTotalTransaction(oidBillMain);
        double discount = 0;
        if(billMain.getDiscType()==PstBillMain.DISC_TYPE_PCT){
            discount = (billMain.getDiscount() * subTotalTrans)/100;
        }else{
            discount = billMain.getDiscount();
        }
        obj.setDiscount(discount);
        obj.setService(billMain.getServiceValue());
        obj.setTax(billMain.getTaxValue());
        obj.setSubTotal(subTotalTrans);
        
        double totalTransaction = subTotalTrans - discount + billMain.getServiceValue() + billMain.getTaxValue();//sumTransaction(oidBillMain);
        //double totalreturn = sumReturn(oidBillMain);
        //double totalPaid = sumTransaction(oidBillMain);//totalTransaction;// + totalreturn;
        obj.setTotalTransaction(totalTransaction);
        //obj.setTotalReturn(totalreturn);
        //obj.setTotalPaid(totalPaid);
        
        return obj;
    }
    
    //set obj for credit invoice
    
    public static PrintInvoiceObj getDataCreditInvoice(BillMain billMain){
        PrintInvoiceObj obj = new PrintInvoiceObj();
        long oidBillMain = 0;
        if(billMain!=null){
            oidBillMain = oidBillMain(billMain);
        }
        
        obj.setNoInvoice(billMain.getInvoiceNumber());
        obj.setNoRefInvoice(billMain.getCoverNumber());
        obj.setDateInvoice(billMain.getBillDate());
        try{
            AppUser appUser = PstAppUser.fetch(billMain.getAppUserId());
            obj.setCashier(appUser.getFullName());
        }catch(Exception e){
        }
        Vector listBillDetail = getBillDetail(oidBillMain);
        obj.setListBillDetail(listBillDetail);
        
        //Vector listPayment = getListPayment(oidBillMain);
        //obj.setListPayment(listPayment);
        
        obj = getMember(obj,billMain.getCustomerId(),oidBillMain);
        double subTotalTrans = sumSubTotalTransaction(oidBillMain);
        double discount = 0;
        if(billMain.getDiscType()==PstBillMain.DISC_TYPE_PCT){
            discount = (billMain.getDiscount() * subTotalTrans)/100;
        }else{
            discount = billMain.getDiscount();
        }
        obj.setDiscount(discount);
        obj.setService(billMain.getServiceValue());
        obj.setTax(billMain.getTaxValue());
        obj.setSubTotal(subTotalTrans);
        
        double totalTransaction = subTotalTrans - discount + billMain.getServiceValue() + billMain.getTaxValue();//sumTransaction(oidBillMain);
        //double totalreturn = sumReturn(oidBillMain);
        //double totalPaid = sumTransaction(oidBillMain);//totalTransaction;// + totalreturn;
        obj.setTotalTransaction(totalTransaction);
        //obj.setTotalReturn(totalreturn);
        //obj.setTotalPaid(totalPaid);
        
        return obj;
    }
    
    //set obj for credit invoice
    
    public static PrintInvoiceObj getDataCreditInvoice(DefaultSaleModel saleModel){
        PrintInvoiceObj obj = new PrintInvoiceObj();
        BillMain billMain = saleModel.getMainSale ();   
        long oidBillMain = 0;
        if(billMain!=null){
            oidBillMain = oidBillMain(billMain); 
        }
        
        obj.setNoInvoice(billMain.getInvoiceNumber());
        obj.setNoRefInvoice(billMain.getCoverNumber());
        obj.setDateInvoice(billMain.getBillDate());
        try{
            AppUser appUser = PstAppUser.fetch(billMain.getAppUserId());
            obj.setCashier(appUser.getFullName());
        }catch(Exception e){
        }
        Vector listBillDetail = getBillDetail(oidBillMain);
        obj.setListBillDetail(listBillDetail);
        
        //Vector listPayment = getListPayment(oidBillMain);
        //obj.setListPayment(listPayment);
        
        obj = getMember(obj,billMain.getCustomerId(),oidBillMain);
//        double subTotalTrans = sumSubTotalTransaction(oidBillMain);
//        double discount = 0;
//        if(billMain.getDiscType()==PstBillMain.DISC_TYPE_PCT){
//            discount = (billMain.getDiscount() * subTotalTrans)/100;
//        }else{
//            discount = billMain.getDiscount();
//        }
        obj.setDiscount(saleModel.getDiscAmount ());
        obj.setService(saleModel.getSvcAmount ());
        obj.setTax(saleModel.getTaxAmount ());
        obj.setSubTotal(saleModel.getTotalTrans ());
        
        //double totalTransaction = subTotalTrans - discount + billMain.getServiceValue() + billMain.getTaxValue();//sumTransaction(oidBillMain);
        //double totalreturn = sumReturn(oidBillMain);
        //double totalPaid = sumTransaction(oidBillMain);//totalTransaction;// + totalreturn;
        obj.setTotalTransaction(saleModel.getNetTrans ());
        obj.setMemberPoint(saleModel.getMemberPoin().getCredit());
        //obj.setTotalReturn(totalreturn);
        //obj.setTotalPaid(totalPaid);
        
        return obj;
    }
    public static PrintInvoiceObj getDataGiftTaking(GiftModel giftModel){
        PrintInvoiceObj obj = new PrintInvoiceObj();
        BillMain billMain = giftModel.getGiftTrans();
        
        obj.setNoInvoice(billMain.getInvoiceNumber());
        obj.setNoRefInvoice(billMain.getCoverNumber());
        obj.setDateInvoice(billMain.getBillDate());
        try{
            AppUser appUser = PstAppUser.fetch(billMain.getAppUserId());
            obj.setCashier(appUser.getFullName());
        }catch(Exception e){
        }
        
        obj.setListBillDetail(giftModel.getListGift());
        obj.setMemberName(giftModel.getCustomerServed().getPersonName());
        obj.setMemberBarcode(giftModel.getCustomerServed().getMemberBarcode());
        obj.setPrevPoint(giftModel.getAvailablePoint());
        obj.setTotalPoint(giftModel.getRequestedPoint());
        obj.setBalancePoint(giftModel.getRestPoint());
        
        return obj;
    }
    
    //set obj for credit payment invoice
    //   public static PrintInvoiceObj getDataPaymentCreditInvoice(BillMain billMain){
    //        PrintInvoiceObj obj = new PrintInvoiceObj();
    //        long oidBillMain = 0;
    //        if(billMain!=null){
    //            oidBillMain = oidBillMain(billMain);
    //        }
    //
    //        obj.setNoInvoice(billMain.getInvoiceNumber());
    //        obj.setNoRefInvoice(billMain.getCoverNumber());
    //        obj.setDateInvoice(billMain.getBillDate());
    //        try{
    //            AppUser appUser = PstAppUser.fetch(billMain.getAppUserId());
    //            obj.setCashier(appUser.getFullName());
    //        }catch(Exception e){
    //        }
    //
    //        obj.setTotLastPayment(0);
    //        Vector listPayment = getListPayment(oidBillMain);
    //        obj.setListPayment(listPayment);
    //
    //        obj = getMember(obj,billMain.getCustomerId(),oidBillMain);
    //
    //        double totalPaid = sumTransaction(oidBillMain);
    //        obj.setTotalPaid(totalPaid);
    //        obj.setTotBalance(0);
    //
    //        return obj;
    //   }
    
    public static PrintInvoiceObj getDataPaymentCreditInvoice(CreditPaymentModel creditPaymentModel){
        PrintInvoiceObj obj = new PrintInvoiceObj();
        
        CreditPaymentMain creditPayMain = creditPaymentModel.getMainPayment();
        obj.setNoInvoice(creditPayMain.getInvoiceNumber());
        obj.setNoRefInvoice(creditPaymentModel.getSaleRef().getInvoiceNumber());
        obj.setDateInvoice(creditPayMain.getBillDate());
        try{
            AppUser appUser = PstAppUser.fetch(creditPayMain.getAppUserId());
            obj.setCashier(appUser.getFullName());
        }catch(Exception e){
        }
        
        obj.setTotLastPayment(creditPaymentModel.getLastPaymentTotal());
        long oidPaymentMain = oidPaymentMain(creditPayMain);
        Vector listPayment = getListPaymentCredit(oidPaymentMain);
        obj.setListPayment(listPayment);
        obj.setTotalTransaction (creditPaymentModel.getLastTransTotal ()); 
        obj = getMember(obj,creditPaymentModel.getSaleRef().getCustomerId(),creditPaymentModel.getSaleRef().getOID());
        obj.setTotCardCostCredit(sumCardCostCredit(oidPaymentMain));
        double totalPaid = creditPaymentModel.getCurrentPaymentTotal();
        obj.setTotalPaid(totalPaid);
        obj.setTotalReturn(creditPaymentModel.getChange());
        obj.setTotBalance(creditPaymentModel.getPaymentBalanceTotal());
        
        return obj;
    }
    
    // to get oid from bill main
    public static long oidBillMain(BillMain billMain) {
        DBResultSet dbrs = null;
        long oid = 0;
        try {
            String sql = "SELECT "+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" FROM " + PstBillMain.TBL_CASH_BILL_MAIN +
            " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
            " = " + billMain.getInvoiceNumber()+
            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+billMain.getDocType()+
            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+
            " = "+billMain.getLocationId();
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                oid = rs.getLong(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return oid;
    }
    
    //get oidpaymentmain
    public static long oidPaymentMain(CreditPaymentMain billMain) {
        DBResultSet dbrs = null;
        long oid = 0;
        try {
            String sql = "SELECT "+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+" FROM " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN +
            " WHERE " + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_INVOICE_NUMBER] +
            " = " + billMain.getInvoiceNumber();
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                oid = rs.getLong(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return oid;
    }
    
    public static Vector getBillDetail(long oidBillMain) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        try {
            String sql = "SELECT DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME]+
            ", DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_SKU]+
            ", DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]+
            ", DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+
            ", DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_DISC_PCT]+
            ", DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+
            ", DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]+
            ", CODE."+PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_CODE]+
            " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL+" AS DTL "+
            " LEFT JOIN "+PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE+" AS CODE "+
            " ON DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+
            " = CODE."+PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID]+
            " WHERE DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
            " = " + oidBillMain+
            " ORDER BY DTL."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]; 
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                Billdetail billDetail = new Billdetail();
                billDetail.setItemName(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME]));
                billDetail.setSku(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_SKU]));
                double price = rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]);
                double qty = rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]);
                billDetail.setItemPrice(price);
                double discount = rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]);
                double discPct = rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_DISC_PCT]);
                double amount = rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]);
                if(discount==0&&discPct!=0){
                    discount = (discPct * qty * price)/100;
                    billDetail.setDisc(discount);
                }else if(discount!=0&&discPct==0){
                    billDetail.setDisc(discount);
                }
                billDetail.setTotalPrice(amount);
                billDetail.setQty(qty);
                temp.add(billDetail);
                
                BillDetailCode billDetailCode = new BillDetailCode();
                billDetailCode.setStockCode(rs.getString(PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_CODE]));
                temp.add(billDetailCode);
                result.add(temp);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    //get list payment
    public static Vector getListPayment(long oidBillMain) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        try {
         
            String sql = "SELECT PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+
            ", PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+
            ", PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+
            ", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
            ", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NAME]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NUMBER]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_ACCOUNT_NAME]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_BANK]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_DUE_DATE]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_BANK_NAME]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_CARD_NAME]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_EXPIRED_DATE]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_HOLDER_NAME]+
            ", DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_AMOUNT]+
            " AS CARD_COST "+
            " FROM " + PstCashPayment.TBL_PAYMENT+" AS PAY "+
            " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS CURR "+
            " ON PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            " = CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
            " LEFT JOIN "+PstCashCreditCard.TBL_CREDIT_CARD+" AS DTL "+
            " ON PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID]+
            " = DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]+
            " WHERE PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
            " = " + oidBillMain;
            
            //System.out.println("SQL>>>>>>>>>>>> : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                CashPayments cashPayment = new CashPayments();
                cashPayment.setAmount(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));
                cashPayment.setRate(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]));
                cashPayment.setPaymentType(rs.getInt(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]));
                cashPayment.setPayDateTime(rs.getDate(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]));
                temp.add(cashPayment);
                
                CurrencyType currType = new CurrencyType();
                currType.setCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                currType.setName(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]));
                temp.add(currType);
                
                CashCreditCard cashCredit = new CashCreditCard();
                cashCredit.setCcName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NAME]));
                cashCredit.setCcNumber(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NUMBER]));
                cashCredit.setChequeAccountName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_ACCOUNT_NAME]));
                cashCredit.setChequeBank(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_BANK]));
                cashCredit.setChequeDueDate(rs.getDate(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_DUE_DATE]));
                cashCredit.setDebitBankName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_BANK_NAME]));
                cashCredit.setDebitCardName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_CARD_NAME]));
                cashCredit.setHolderName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_HOLDER_NAME]));
                cashCredit.setExpiredDate(rs.getDate(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_EXPIRED_DATE]));
                cashCredit.setAmount(rs.getDouble("CARD_COST"));
                temp.add(cashCredit);
                result.add(temp);
                
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
         }
         return result;
    }
    
    public static Vector getListPaymentCredit(long oidBillMain) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        try {
            String sql = "SELECT (PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]+
            ") AS "+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_AMOUNT]+
            ", PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_RATE]+
            ", PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAY_TYPE]+
            ", PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAY_DATETIME]+
            ", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+
            ", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CC_NAME]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CC_NUMBER]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_ACCOUNT_NAME]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_BANK]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_DUE_DATE]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_DEBIT_BANK_NAME]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_DEBIT_CARD_NAME]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_EXPIRED_DATE]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_HOLDER_NAME]+
            ", DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_AMOUNT]+
            " AS CARD_COST "+
            " FROM " + PstCashCreditPayment.TBL_PAYMENT+" AS PAY "+
            " INNER JOIN "+PstCurrencyType.TBL_POS_CURRENCY_TYPE+" AS CURR "+
            " ON PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CURRENCY_ID]+
            " = CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+
            " LEFT JOIN "+PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO+" AS DTL "+
            " ON PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_ID]+
            " = DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID]+
            " WHERE PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]+
            " = " + oidBillMain;
            
            //System.out.println("SQL>>>>>>>>>>>> : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                CashPayments cashPayment = new CashPayments();
                cashPayment.setAmount(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));
                cashPayment.setRate(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]));
                cashPayment.setPaymentType(rs.getInt(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]));
                cashPayment.setPayDateTime(rs.getDate(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]));
                temp.add(cashPayment);
                
                CurrencyType currType = new CurrencyType();
                currType.setCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                currType.setName(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]));
                temp.add(currType);
                
                CashCreditCard cashCredit = new CashCreditCard();
                cashCredit.setCcName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NAME]));
                cashCredit.setCcNumber(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NUMBER]));
                cashCredit.setChequeAccountName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_ACCOUNT_NAME]));
                cashCredit.setChequeBank(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_BANK]));
                cashCredit.setChequeDueDate(rs.getDate(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_DUE_DATE]));
                cashCredit.setDebitBankName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_BANK_NAME]));
                cashCredit.setDebitCardName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_CARD_NAME]));
                cashCredit.setHolderName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_HOLDER_NAME]));
                cashCredit.setExpiredDate(rs.getDate(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_EXPIRED_DATE]));
                cashCredit.setAmount(rs.getDouble("CARD_COST"));
                temp.add(cashCredit);
                result.add(temp);
                
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    //sum total transaction
    public static double sumTransaction(long oidBillMain) {
        DBResultSet dbrs = null;
        double result = 0;
        try {
            //            String sql = "SELECT SUM(PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+
            //                         " * PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+
            String sql = "SELECT SUM(PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+            
            ") AS SUMOFPAY "+
            " FROM " + PstCashPayment.TBL_PAYMENT+" AS PAY "+
            " WHERE PAY."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
            " = " + oidBillMain;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err sum of transaction: "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
           
        }
         return result;
    }
    
    //sum sub total transaction
    public static double sumSubTotalTransaction(long oidBillMain) {
        DBResultSet dbrs = null;
        double result = 0;
        try {
            String sql = "SELECT SUM(PAY."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]+
            ") AS SUMOFPAY "+
            " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL+" AS PAY "+
            " WHERE PAY."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
            " = " + oidBillMain;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err sum of transaction: "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    //sum total return
    public static double sumReturn(long oidBillMain) {
        DBResultSet dbrs = null;
        double result = 0;
        try {
            String sql = "SELECT SUM(PAY."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+
            ") AS SUMOFPAY "+
            " FROM " + PstCashReturn.TBL_RETURN+" AS PAY "+
            " WHERE PAY."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+
            " = " + oidBillMain;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err sum of return: "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    //sum total card cost for invoice
    public static double sumCardCost(long oidBillMain) {
        DBResultSet dbrs = null;
        double result = 0;
        try {
            String sql = "SELECT SUM("+
            " DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_AMOUNT]+") "+
            " FROM "+PstCashPayment.TBL_PAYMENT+" AS MAIN "+
            " INNER JOIN "+PstCashCreditCard.TBL_CREDIT_CARD+" AS DTL "+
            " ON MAIN."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID]+
            " = DTL."+PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]+
            " WHERE MAIN."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
            " = "+oidBillMain;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            if(rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err sum of card cost: "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    //sum total card cost for credit
    public static double sumCardCostCredit(long oidCreditMain) {
        DBResultSet dbrs = null;
        double result = 0;
        try {
            String sql = "SELECT SUM("+
            " DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_AMOUNT]+") "+
            " FROM "+PstCashCreditPayment.TBL_PAYMENT+" AS PAY "+            
            " INNER JOIN "+PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO+" AS DTL "+
            " ON PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_PAYMENT_ID]+
            " = DTL."+PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID]+
            " WHERE PAY."+PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID]+
            " = "+oidCreditMain;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            if(rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err sum of card cost: "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    //sum total card cost for credit
    public static double sumOtherCost(long oidBillMain) {
        DBResultSet dbrs = null;
        double result = 0;
        try {
            String sql = " SELECT SUM("+PstOtherCost.fieldNames[PstOtherCost.FLD_AMOUNT]+")"+
                         " FROM "+PstOtherCost.TBL_CASH_OTHER_COST+
                         " WHERE "+PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID]+
                         " = "+oidBillMain;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            if(rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err sum of card cost: "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    // list card cost for credit
    public static Vector listOtherCost(long oidBillMain) {
        Vector result = new Vector();
        String where = PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID]+
        " = "+oidBillMain;
        try{
            result = PstOtherCost.list(0,0, where, "");
        }
        catch(Exception e){
            System.out.println("err in listOtherCost: "+e.toString());
        }
        return result;
    }
    
    //to get member data
    public static PrintInvoiceObj getMember(PrintInvoiceObj obj, long oidMember,long oidBillmain){
        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT MEM."+PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]+
            ", MEM."+PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE]+
            ", POIN."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_DEBET]+
            " FROM " + PstMemberReg.TBL_CONTACT_LIST+" AS MEM "+
            " LEFT JOIN "+PstMemberPoin.TBL_POS_MEMBER_POIN+" AS POIN "+
            " ON MEM."+PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]+
            " = POIN."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_MEMBER_ID]+
            " WHERE MEM."+PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]+
            " = " + oidMember+
            " AND POIN."+PstMemberPoin.fieldNames[PstMemberPoin.FLD_CASH_BILL_MAIN_ID]+
            " = "+oidBillmain;
                         /*
                         " GROUP BY MEM."+PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]+
                         ", MEM."+PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];
                          */
            
            //System.out.println("SQL >>>>>>>>>> : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                obj.setMemberName(rs.getString(1));
                obj.setMemberBarcode(rs.getString(2));
                obj.setMemberPoint(rs.getInt(3));
                
            }
            rs.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("err at member : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        return obj;
        
    }
    
    //convert hashtable to vector
    public static Vector hash2Vector(Hashtable hashData){
        Vector result = new Vector();
        if(hashData!=null&&hashData.size()>0){
            result =  new Vector(hashData.values());
        }
        return result;
    }
    
    
    public static void main(String args[]){
        BillMain billmain = new BillMain();
        String noInvoice = "041200002";
        billmain.setInvoiceNumber(noInvoice);
        long oid = oidBillMain(billmain);
        try{
            billmain = PstBillMain.fetchExc(oid);
        }catch(Exception e){
        }
        PrintInvoiceObj obj = getData(billmain);
        System.out.println("obj time>>>>>>>>>>> : "+Formater.formatDate(obj.getDateInvoice(),"hh:mm:ss"));
    }
}
