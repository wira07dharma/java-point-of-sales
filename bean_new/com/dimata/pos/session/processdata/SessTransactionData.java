/*
 * SessTransactionData.java
 *
 * Created on December 27, 2004, 11:17 AM
 */

package com.dimata.pos.session.processdata;

import com.dimata.ObjLink.BOCashier.*;
import com.dimata.interfaces.BOCashier.I_Guest;
import com.dimata.common.entity.payment.*;
import com.dimata.interfaces.BOCashier.I_Billing;
import com.dimata.pos.cashier.*;
import com.dimata.posbo.db.DBException;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.qdep.entity.I_IJGeneral;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 *
 * @author  yogi
 * @edited by wpulantara
 */
/* package java */


/* package posbo */

public class SessTransactionData {
    
    /** Creates a new instance of SessTransactionData */
    
    public SessTransactionData() {
    }
    
    public static void putCreditPaymentData(CreditPaymentModel saleModel) throws DBException{
        CreditPaymentMain creditPaymentMain= saleModel.getMainPayment();
        Enumeration enumNewPayments = saleModel.getListNewPayment().elements();
        Enumeration enumNewPaymentsInfo = saleModel.getListNewPaymentInfo().elements();
        saleModel.getMainPayment().setBillMainId(saleModel.getSaleRef().getOID());
        long creditMainId = 0;
        try{
            creditMainId = PstCreditPaymentMain.insertExc(creditPaymentMain);
        }catch(DBException dbe){
            
        }
        if(creditMainId!=0){
            while(enumNewPayments.hasMoreElements()){
                CashCreditPayments payments = (CashCreditPayments)enumNewPayments.nextElement();
                CashCreditPaymentInfo paymentInfos = (CashCreditPaymentInfo)enumNewPaymentsInfo.nextElement();
                payments.setCreditMainId(creditMainId);
                payments.setPaymentStatus(I_IJGeneral.PAYMENT_STATUS_NOT_POSTED);
                long paymentId = 0;
                paymentId = PstCashCreditPayment.insertExc(payments);
                if(paymentId!=0){
                    paymentInfos.setPaymentId(paymentId);
                    if(payments.getPaymentType()!=PstCashCreditPayment.CASH){
                        long paymentInfo = 0;
                        
                        try{
                            paymentInfo = PstCashCreditPaymentInfo.insertExc(paymentInfos);
                        }catch(Exception dbe){
                            System.out.println("Err on putCreditPaymentData : "+dbe.toString());
                        }
                    }
                }
            }
            
            if(saleModel.getChange()>0 && saleModel.getCreditPaymentChange()!=null){
                CashCreditPayments payChange = saleModel.getCreditPaymentChange();
                payChange.setCreditMainId(creditMainId);
                payChange.setPaymentStatus(I_IJGeneral.PAYMENT_STATUS_NOT_POSTED);
                try{
                    PstCashCreditPayment.insertExc(payChange);
                }
                catch(Exception dbe){
                    System.out.println("Err on putCreditPaymentData : "+dbe.toString());
                }
            }
            
            double newBalance = saleModel.getPaymentBalanceTotal();
            if(newBalance<=0){
                saleModel.getSaleRef().setTransactionStatus(PstBillMain.TRANS_STATUS_CLOSE);
                try{
                    PstBillMain.updateExc(saleModel.getSaleRef());
                }catch(Exception dbe){
                    System.out.println("Err on putCreditPaymentData : "+dbe.toString());
                }
            }
            
        }
    }
    public static synchronized void putData(DefaultSaleModel defaultSaleModel){
        //----------------------------------------
        // objects for publish data;
        SaleModel saleModelLink = new SaleModel();
        CustomerLink customerLink= new CustomerLink();
        DetailBillLink detailBillLink = new DetailBillLink();
        Vector vctDetailBill = new Vector();
        MainBillLink mainBillLink = new MainBillLink();
        Vector paymentLink = new Vector();
        saleModelLink.setCustomer(customerLink);
        saleModelLink.setDetailBill(vctDetailBill);
        saleModelLink.setMainBill(mainBillLink);
        saleModelLink.setPaymentBill(paymentLink);
        
        //
        //----------------------------------------
        
        MemberReg customerServed = defaultSaleModel.getCustomerServed();
        BillMain mainSale = defaultSaleModel.getMainSale();
        Hashtable hashBillDetail = defaultSaleModel.getHashBillDetail();
        Hashtable hashBillDetailCode = defaultSaleModel.getHashBillDetailCode();
        Vector vectShorter = defaultSaleModel.getVectBillDetail();
        Hashtable hashOtherCost = defaultSaleModel.getHashOtherCost();
        Hashtable hashPersonalDisc = defaultSaleModel.getHashPersonalDisc();
        Hashtable cashPayments = defaultSaleModel.getCashPayments();
        Hashtable cashPaymentInfo = defaultSaleModel.getCashPaymentInfo();
        Hashtable creditPayment = defaultSaleModel.getCreditPayment();
        Hashtable creditPaymentInfo = defaultSaleModel.getCreditPaymentInfo();
        Hashtable cashReturns = defaultSaleModel.getCashReturn();
        MemberPoin memberPoin = defaultSaleModel.getMemberPoin();
        StandartRate rateUsed = defaultSaleModel.getRateUsed();
        CurrencyType currencyTypeUsed = defaultSaleModel.getCurrencyTypeUsed();
        
        
        /* begin to insert to bill main */
        Vector vectBillDetail =  hash2vector(hashBillDetail,vectShorter);
        Vector vectBillDetailCode =  hash2vector(hashBillDetailCode,vectShorter);
        Vector vectCashPayments = hash2vector(cashPayments);
        Vector vectCreditPayment = hash2vector(creditPayment);
        Vector vectCashPaymentInfo = hash2vector(cashPaymentInfo);
        Vector vectCreditPaymentInfo = hash2vector(creditPaymentInfo);
        Vector vectCashReturn = hash2vector(cashReturns);
        Vector vectOtherCost = hash2vector(hashOtherCost);
        
        try{
            mainSale.setCustomerId(customerServed.getOID());
            mainSale.setCurrencyId(defaultSaleModel.getRateUsed().getCurrencyTypeId());
            mainSale.setRate(defaultSaleModel.getRateUsed().getSellingRate());
            if(defaultSaleModel.getCustomerLink()!=null){
                mainSale.setSpecialId(defaultSaleModel.getCustomerLink().getReservationId());
            }
            long oidBillMain = mainSale.getOID();
            if(oidBillMain==0){
                oidBillMain = PstBillMain.insertExc(mainSale);
            }else{
                oidBillMain = PstBillMain.updateExc(mainSale);
            }
            
            /* to bill detail */
            if(vectBillDetail!=null&&vectBillDetail.size()>0){
                for(int i=0;i<vectBillDetail.size();i++){
                    Billdetail billDetail = (Billdetail)vectBillDetail.get(i);
                    billDetail.setBillMainId(oidBillMain);
                    long oidBilldetail = billDetail.getOID();
                    
                    if(oidBilldetail>0 && billDetail.getUpdateStatus()!=PstBillDetail.UPDATE_STATUS_DELETED && mainSale.getDocType()!=PstBillMain.TYPE_RETUR){
                        billDetail.setUpdateStatus(PstBillDetail.UPDATE_STATUS_UPDATED);
                    }
                    if(billDetail.getUpdateStatus()==PstBillDetail.UPDATE_STATUS_INSERTED){
                        oidBilldetail = PstBillDetail.insertExc(billDetail);
                    }else if(billDetail.getUpdateStatus()==PstBillDetail.UPDATE_STATUS_UPDATED){
                        oidBilldetail = PstBillDetail.updateExc(billDetail);
                    }else if(billDetail.getUpdateStatus()==PstBillDetail.UPDATE_STATUS_DELETED){
                        if(oidBilldetail>0){
                            oidBilldetail = PstBillDetail.deleteExc(oidBilldetail);
                        }
                    }
                    
                    long oidMaterial = billDetail.getMaterialId();
                    Material material = PstMaterial.fetchExc(oidMaterial);
                    if(material.getRequiredSerialNumber()==PstMaterial.REQUIRED || CashierMainApp.isUsingProductColor()){
                        BillDetailCode billDetailCode = (BillDetailCode)vectBillDetailCode.get(i);
                        billDetailCode.setSaleItemId(oidBilldetail);
                        long oidBillDetailCode = billDetailCode.getOID();
                        if(oidBillDetailCode>0 && billDetailCode.getUpdateStatus()!=PstBillDetailCode.UPDATE_STATUS_DELETED){
                            billDetailCode.setUpdateStatus(PstBillDetailCode.UPDATE_STATUS_UPDATED);
                        }
                        if(billDetailCode.getUpdateStatus()==PstBillDetailCode.UPDATE_STATUS_INSERTED){
                            oidBillDetailCode = PstBillDetailCode.insertExc(billDetailCode);
                        }else if(billDetailCode.getUpdateStatus()==PstBillDetailCode.UPDATE_STATUS_UPDATED){
                            oidBillDetailCode = PstBillDetailCode.updateExc(billDetailCode);
                        }else if(billDetailCode.getUpdateStatus()==PstBillDetailCode.UPDATE_STATUS_DELETED){
                            if(oidBillDetailCode>0){
                                oidBillDetailCode = PstBillDetailCode.deleteExc(oidBillDetailCode);
                            }
                        }
                    }
                }
            }
            
            /** insert other cost */
            if(vectOtherCost!=null && vectOtherCost.size()>0){
                for(int i = 0; i < vectOtherCost.size(); i++){
                    OtherCost otherCost = (OtherCost) vectOtherCost.get(i);
                    otherCost.setBillMainId(oidBillMain);
                    long oidOtherCost = otherCost.getOID();
                    if(oidOtherCost>0 && otherCost.getUpdateStatus()!=PstOtherCost.UPDATE_STATUS_DELETED){
                        otherCost.setUpdateStatus(PstOtherCost.UPDATE_STATUS_UPDATED);
                    }
                    switch(otherCost.getUpdateStatus()){
                        case PstOtherCost.UPDATE_STATUS_INSERTED:
                            oidOtherCost = PstOtherCost.insertExc(otherCost);
                            break;
                        case PstOtherCost.UPDATE_STATUS_UPDATED:
                            oidOtherCost = PstOtherCost.updateExc(otherCost);
                            break;
                        case PstOtherCost.UPDATE_STATUS_DELETED:
                            if(oidOtherCost>0){
                                oidOtherCost = PstOtherCost.deleteExc(oidOtherCost);
                            }
                            break;
                    }
                }
            }
            
            /* insert to payment */
            if(vectCashPayments!=null&&vectCashPayments.size()>0){
                for(int j=0;j<vectCashPayments.size();j++){
                    CashPayments cashpayment = (CashPayments)vectCashPayments.get(j);
                    cashpayment.setBillMainId(oidBillMain);
                    cashpayment.setPaymentStatus(I_IJGeneral.PAYMENT_STATUS_NOT_POSTED);
                    long oidCashPayment = cashpayment.getOID();
                    if(oidCashPayment>0)
                        cashpayment.setUpdateStatus(PstCashPayment.UPDATE_STATUS_UPDATED);
                    if(cashpayment.getUpdateStatus()==PstCashPayment.UPDATE_STATUS_INSERTED&&cashpayment.getAmount()>0){
                        oidCashPayment = PstCashPayment.insertExc(cashpayment);
                    }else if(cashpayment.getUpdateStatus()==PstCashPayment.UPDATE_STATUS_UPDATED&&cashpayment.getAmount()>0){
                        oidCashPayment = PstCashPayment.updateExc(cashpayment);
                    }else if(cashpayment.getUpdateStatus()==PstCashPayment.UPDATE_STATUS_DELETED&&cashpayment.getAmount()>0){
                        if(oidCashPayment>0){
                            oidCashPayment = PstCashPayment.deleteExc(oidCashPayment);
                        }
                    }
                    /* insert to credit card if only payment type not cash type */
                    CashCreditCard cashCreditCard = new CashCreditCard();
                    if(cashpayment.getPaymentType()!=PstCashPayment.CASH){
                        cashCreditCard = (CashCreditCard)vectCashPaymentInfo.get(j);
                        long oidCashCreditCard =  cashCreditCard.getOID();
                        if(oidCashCreditCard>0)
                            cashCreditCard.setUpdateStatus(PstCashCreditCard.UPDATE_STATUS_UPDATED);
                        cashCreditCard.setPaymentId(oidCashPayment);
                        if(cashCreditCard.getUpdateStatus()==PstCashCreditCard.UPDATE_STATUS_INSERTED){
                            oidCashCreditCard = PstCashCreditCard.insertExc(cashCreditCard);
                        }else if(cashCreditCard.getUpdateStatus()==PstCashCreditCard.UPDATE_STATUS_UPDATED){
                            oidCashCreditCard = PstCashCreditCard.updateExc(cashCreditCard);
                        }else if(cashCreditCard.getUpdateStatus()==PstCashCreditCard.UPDATE_STATUS_DELETED){
                            if(oidCashCreditCard>0){
                                oidCashCreditCard = PstCashCreditCard.deleteExc(oidCashCreditCard);
                            }
                        }
                        
                    }
                }
            }
            
            if(vectCashReturn.size()>0){
                
                for(int i=0;i<vectCashReturn.size();i++){
                    CashReturn cashReturn = (CashReturn)vectCashReturn.get(i);
                    
                    cashReturn.setBillMainId(oidBillMain);
                    cashReturn.setPaymentStatus(I_IJGeneral.PAYMENT_STATUS_NOT_POSTED);
                    
                    if(cashReturn.getOID()==0)
                        PstCashReturn.insertExc(cashReturn);
                    else
                        PstCashReturn.updateExc(cashReturn);
                }
            }
            
            // member point insert
            memberPoin.setMemberId(customerServed.getOID());
            memberPoin.setCashBillMainId(oidBillMain);
            if(memberPoin.getOID()==0)
                PstMemberPoin.insertExc(memberPoin);
            else
                PstMemberPoin.updateExc(memberPoin);
            
            // update pending order if exist
            long pendingOrderId = mainSale.getCashPendingOrderId();
            if(pendingOrderId>0){
                try{
                    PendingOrder po = PstPendingOrder.fetchExc(pendingOrderId);
                    po.setPendingOrderStatus(PstPendingOrder.STATUS_CLOSED);
                    PstPendingOrder.updateExc(po);
                }
                catch(Exception e){
                    System.out.println("Err in put Data : "+e.toString());
                }
            }
            
            // create credit payment if needed
            if(defaultSaleModel.getNetCreditPay()>0 && defaultSaleModel.getCreditPay()!=null){
                try{
                    putCreditPaymentData(defaultSaleModel.getCreditPay());
                }
                catch(Exception e){
                    System.out.println("Err in put Data : "+e.toString());
                }
                
                
            }
            
            try{
                if(CashierMainApp.getTransactionPublished()==CashierMainApp.PUBLISH_MULTICAST){
                    if(mainSale.getTransctionType()==PstBillMain.TRANS_TYPE_CASH && mainSale.getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN){
                        // do nothing for now
                        System.out.println("DO NOTHING ON PUBLISH");
                    }
                    else{
                        if(CashierMainApp.getIntegrationWith()==CashierMainApp.INTEGRATE_WITH_HANOMAN)
                            SessTransactionData.publishSaleData(SessTransactionData.translateSaleModel(saleModelLink,defaultSaleModel));
                        else if(CashierMainApp.getIntegrationWith()==CashierMainApp.INTEGRATE_WITH_MANUFACTURE){
                            //com.dimata.prochain.session.marketing.SessGenerateInvoice sess = new com.dimata.prochain.session.marketing.SessGenerateInvoice();
                            if(mainSale.getTransctionType()==PstBillMain.TRANS_TYPE_CREDIT && mainSale.getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN){
                                try{
                                    //sess.generateDirectCreditInvoice();
                                }
                                catch(Exception e){
                                    System.out.println("err on generateDirectCreditInvoice() = "+e.toString());
                                }
                            }
                            else{
                                try{
                                   // sess.generateDirectInvoice();
                                }
                                catch(Exception e){
                                    System.out.println("err on generateDirectInvoice() = "+e.toString());
                                }
                            }
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }catch(Exception e){
            System.out.println("err in process put data : "+e.toString());
            e.printStackTrace();
        }
        
    }
    
    
    /* convert hash table to vector */
    public static Vector hash2vector(Hashtable hashdata){
        Vector result = new Vector(1,1);
        if(hashdata!=null&&hashdata.size()>0){
            result = new Vector(hashdata.values());
        }
        return result;
    }
    
    /* convert hash table to vector with shorter*/
    public static Vector hash2vector(Hashtable hashdata,Vector shorter){
        Vector result = new Vector(1,1);
        int size = shorter.size();
        if(size>0 && size==hashdata.size()){
            for(int i = 0; i < size; i++){
                String key = (String) shorter.get(i);
                result.add(hashdata.get(key));
            }
        }
        else{
            result = hash2vector(hashdata);
        }
        return result;
    }
    
    /* to get data from open bill */
    public static DefaultSaleModel getData(BillMain billmain){
        DefaultSaleModel defaultSaleModel = new DefaultSaleModel();
        try{
            
            BillMain mainSale = PstBillMain.fetchExc(billmain.getOID());
            if(mainSale!=null){
                
                defaultSaleModel.setMainSale(mainSale);
                String whereDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
                " = "+mainSale.getOID();
                String orderDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID];
                Vector vectBillDetail = PstBillDetail.list(0,0,whereDetail,orderDetail);
                
                MemberReg customerServed = PstMemberReg.fetchExc(mainSale.getCustomerId());
                defaultSaleModel.setCustomerServed(customerServed);
                defaultSaleModel.setIsOpenBillPayment(defaultSaleModel.getMainSale().getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN);
                if(mainSale.getCurrencyId()>0){
                    try{
                        CurrencyType currType = PstCurrencyType.fetchExc(mainSale.getCurrencyId());
                        defaultSaleModel.setCurrencyTypeUsed(currType);
                        defaultSaleModel.setRateUsed(CashSaleController.getLatestRate(String.valueOf(currType.getOID())));
                        defaultSaleModel.getRateUsed().setSellingRate(mainSale.getRate());
                    }
                    catch(Exception e){
                        System.out.println("err on get currType: "+e.toString());
                    }
                }
                
                if(CashierMainApp.getIntegrationWith()==CashierMainApp.INTEGRATE_WITH_HANOMAN&&mainSale.getSpecialId()>0){
                    try{
                        CustomerLink cusLink = new CustomerLink();
                        cusLink.setReservationId(mainSale.getSpecialId());
                        System.out.println("mainSale.getSpecialId()="+mainSale.getSpecialId());
                        I_Guest guestHelper = null;
                        try{
                            guestHelper = (I_Guest) Class.forName(I_Guest.strCustomerLinkHanoman).newInstance();
                            System.out.println("MASUK 1 ");
                        }
                        catch(Exception e){
                            System.out.println("err on getGuestInstant: "+e.toString());
                        }
                        System.out.println("MASUK 2 ");
                        if(guestHelper!=null){
                            System.out.println("MASUK 3 ");
                            Vector listGuest = guestHelper.listCustomers(cusLink, 0,0);
                            if(listGuest!=null&&listGuest.size()>0){
                                System.out.println("MASUK 4 ");
                                cusLink = (CustomerLink)listGuest.get(0);
                                defaultSaleModel.setCustomerLink(cusLink);
                                System.out.println("cusLink="+cusLink.getCustomerName());
                            }
                        }
                    }
                    catch(Exception e){
                        System.out.println("err on getCustomerLink: "+e.toString());
                    }
                }
                
                try{
                    PendingOrder pendingOrder = new PendingOrder();
                    long pendingOrderId = mainSale.getCashPendingOrderId();
                    if(pendingOrderId>0){
                        pendingOrder = PstPendingOrder.fetchExc(pendingOrderId);
                        if(pendingOrder.getOID()>=0){
                            defaultSaleModel.setLastPayment(pendingOrder.getDownPayment());
                        }
                    }
                    else{
                        defaultSaleModel.setLastPayment(0);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    defaultSaleModel.setLastPayment(0);
                }
                
                
                Hashtable hashBillDetail = new Hashtable();
                Hashtable hashBillDetailCode = new Hashtable();
                Vector vectShorter = new Vector();
                if(vectBillDetail!=null&&vectBillDetail.size()>0){
                    for(int i=0;i<vectBillDetail.size();i++){
                        Billdetail billdetail = (Billdetail)vectBillDetail.get(i);
                        
                        String whereBillDetailCode = PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID]+
                        " = "+billdetail.getOID();
                        Vector vectBillDetailCode = PstBillDetailCode.list(0,0, whereBillDetailCode,"");
                        
                        BillDetailCode billDetailCode = new BillDetailCode();
                        
                        double qtyReturned = CashSaleController.getQtyAlreadyReturned(billmain.getOID(),billdetail.getMaterialId(),"");
                        if(vectBillDetailCode!=null&&vectBillDetailCode.size()>0){
                            billDetailCode = (BillDetailCode)vectBillDetailCode.get(0);
                            qtyReturned = CashSaleController.getQtyAlreadyReturned(mainSale.getOID(),billdetail.getMaterialId(),billDetailCode.getStockCode());
                        }
                        else{
                            qtyReturned = CashSaleController.getQtyAlreadyReturned(mainSale.getOID(),billdetail.getMaterialId(),"");
                        }
                        hashBillDetailCode.put(billdetail.getSku()+"-"+billDetailCode.getStockCode(), billDetailCode);
                        defaultSaleModel.setHashBillDetailCode(hashBillDetailCode);
                        
                        billdetail.setAmountAvailForReturn(billdetail.getQty()-qtyReturned);
                        hashBillDetail.put(billdetail.getSku()+"-"+billDetailCode.getStockCode(), billdetail);
                        vectShorter.add(billdetail.getSku()+"-"+billDetailCode.getStockCode());
                    }
                    defaultSaleModel.setHashBillDetail(hashBillDetail);
                    defaultSaleModel.setVectBillDetail(vectShorter);
                }
                
                // start other cost //
                String whereOtherCost = PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID]+
                " = " + mainSale.getOID();
                String orderOtherCost = PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_OTHER_COST_ID];
                Vector vectOtherCost = PstOtherCost.list(0,0,whereOtherCost,orderOtherCost);
                Hashtable hashOtherCost = new Hashtable();
                int sizeOtherCost = vectOtherCost.size();
                double totOtherCost = 0;
                for(int i = 0; i < sizeOtherCost; i++){
                    OtherCost otherCost = (OtherCost) vectOtherCost.get(i);
                    totOtherCost = totOtherCost + otherCost.getAmount();
                    hashOtherCost.put(otherCost.getName(), otherCost);
                }
                defaultSaleModel.setHashOtherCost(hashOtherCost);
                defaultSaleModel.setTotOtherCost(totOtherCost);
                // end other cost //
                
                //start payment //
                String whereCashPayment = PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
                " = "+mainSale.getOID();
                Vector vectCashPayment = PstCashPayment.list(0,0, whereCashPayment, "");
                System.out.println("paV"+vectCashPayment.size());
                Hashtable hashCashPayment = new Hashtable();
                Hashtable hashCashPaymentInfo = new Hashtable();
                double totCardCost = 0;
                if(vectCashPayment!=null&&vectCashPayment.size()>0){
                    for(int z=0;z<vectCashPayment.size();z++){
                        System.out.println("pay"+z);
                        CashPayments cashPayment = (CashPayments)vectCashPayment.get(z);
                        
                        hashCashPayment.put(cashPayment.getPayDateTime(), cashPayment);
                        
                        String whereCashPaymentInfo = PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]+
                        " = "+cashPayment.getOID();
                        Vector vectCashPaymentInfo = PstCashCreditCard.list(0,0,whereCashPaymentInfo,"");
                        if(vectCashPaymentInfo!=null&&vectCashPaymentInfo.size()==1){
                            CashCreditCard cashCreditCard = (CashCreditCard)vectCashPaymentInfo.get(0);
                            totCardCost = totCardCost + cashCreditCard.getAmount();
                            hashCashPaymentInfo.put(cashPayment.getPayDateTime(), cashCreditCard);
                            defaultSaleModel.setCashPaymentInfo(hashCashPaymentInfo);
                            System.out.println("paI"+hashCashPaymentInfo.size());
                        }
                    }
                    System.out.println("pay"+hashCashPayment.size());
                    defaultSaleModel.setCashPayments(hashCashPayment);
                    defaultSaleModel.setTotCardCost(totCardCost);
                }
                //end payment //
                
                //start member point //
                String whereMemberPoint = PstMemberPoin.fieldNames[PstMemberPoin.FLD_CASH_BILL_MAIN_ID]+
                " = "+mainSale.getOID();
                Vector vectMemberPoint = PstMemberPoin.list(0,0, whereMemberPoint, "");
                if(vectMemberPoint!=null&&vectMemberPoint.size()==1){
                    MemberPoin memberPoin = (MemberPoin)vectMemberPoint.get(0);
                    defaultSaleModel.setMemberPoin(memberPoin);
                }
                //end member point //
                
                //set personal diskon //
                String wherePersonalDiskon = PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID]+
                " = "+customerServed.getOID();
                Vector vectPersonalDiskon = PstPersonalDiscount.list(0,0, wherePersonalDiskon,"");
                Hashtable hashPersonalDiscount = new Hashtable();
                if(vectPersonalDiskon!=null&&vectPersonalDiskon.size()>0){
                    for(int i=0;i<vectPersonalDiskon.size();i++){
                        PersonalDiscount personalDiscount = (PersonalDiscount)vectPersonalDiskon.get(i);
                        hashPersonalDiscount.put(new Long(personalDiscount.getMaterialId()), personalDiscount); //this right key i don't know???
                    }
                    defaultSaleModel.setHashPersonalDisc(hashPersonalDiscount);
                }
                //end personal diskon //
                
                //set sales person//
                if(CashierMainApp.isEnableSaleEntry()){
                    String whereSales = PstSales.fieldNames[PstSales.FLD_CODE]+" = '"+mainSale.getSalesCode()+"' ";
                    Vector vctSales = null;
                    try{
                        vctSales = PstSales.list(0,0, whereSales, "");
                        
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(vctSales!=null&&vctSales.size()>0){
                        Sales salesPerson = (Sales)vctSales.get(0);
                        defaultSaleModel.setSalesPerson(salesPerson);
                    }
                }
                else{
                    defaultSaleModel.setSalesPerson(new Sales());
                }
                //end sales person//
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("err di getdata : "+e.toString());
        }
        return defaultSaleModel;
    }
    
    public static DefaultSaleModel getDataHalfInvoice(BillMain billmain, double pct){
        DefaultSaleModel defaultSaleModel = new DefaultSaleModel();
        try{
            
            BillMain mainSale = PstBillMain.fetchExc(billmain.getOID());
            mainSale.setDiscount(mainSale.getDiscount()*pct/100);
            mainSale.setTaxValue(mainSale.getTaxValue()*pct/100);
            if(mainSale!=null){                
                defaultSaleModel.setMainSale(mainSale);
                String whereDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+
                " = "+mainSale.getOID();
                String orderDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID];
                Vector vectBillDetail = PstBillDetail.list(0,0,whereDetail,orderDetail);
                
                MemberReg customerServed = PstMemberReg.fetchExc(mainSale.getCustomerId());
                defaultSaleModel.setCustomerServed(customerServed);
                defaultSaleModel.setIsOpenBillPayment(defaultSaleModel.getMainSale().getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN);
                if(mainSale.getCurrencyId()>0){
                    try{
                        CurrencyType currType = PstCurrencyType.fetchExc(mainSale.getCurrencyId());
                        defaultSaleModel.setCurrencyTypeUsed(currType);
                        defaultSaleModel.setRateUsed(CashSaleController.getLatestRate(String.valueOf(currType.getOID())));
                        defaultSaleModel.getRateUsed().setSellingRate(mainSale.getRate());
                    }
                    catch(Exception e){
                        System.out.println("err on get currType: "+e.toString());
                    }
                }
                
                if(CashierMainApp.getIntegrationWith()==CashierMainApp.INTEGRATE_WITH_HANOMAN&&mainSale.getSpecialId()>0){
                    try{
                        CustomerLink cusLink = new CustomerLink();
                        cusLink.setReservationId(mainSale.getSpecialId());
                        System.out.println("mainSale.getSpecialId()="+mainSale.getSpecialId());
                        I_Guest guestHelper = null;
                        try{
                            guestHelper = (I_Guest) Class.forName(I_Guest.strCustomerLinkHanoman).newInstance();
                            System.out.println("MASUK 1 ");
                        }
                        catch(Exception e){
                            System.out.println("err on getGuestInstant: "+e.toString());
                        }
                        System.out.println("MASUK 2 ");
                        if(guestHelper!=null){
                            System.out.println("MASUK 3 ");
                            Vector listGuest = guestHelper.listCustomers(cusLink, 0,0);
                            if(listGuest!=null&&listGuest.size()>0){
                                System.out.println("MASUK 4 ");
                                cusLink = (CustomerLink)listGuest.get(0);
                                defaultSaleModel.setCustomerLink(cusLink);
                                System.out.println("cusLink="+cusLink.getCustomerName());
                            }
                        }
                    }
                    catch(Exception e){
                        System.out.println("err on getCustomerLink: "+e.toString());
                    }
                }
                
                try{
                    PendingOrder pendingOrder = new PendingOrder();
                    long pendingOrderId = mainSale.getCashPendingOrderId();
                    if(pendingOrderId>0){
                        pendingOrder = PstPendingOrder.fetchExc(pendingOrderId);
                        if(pendingOrder.getOID()>=0){
                            defaultSaleModel.setLastPayment(pendingOrder.getDownPayment()*pct/100);
                        }
                    }
                    else{
                        defaultSaleModel.setLastPayment(0);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    defaultSaleModel.setLastPayment(0);
                }
                
                
                Hashtable hashBillDetail = new Hashtable();
                Hashtable hashBillDetailCode = new Hashtable();
                Vector vectShorter = new Vector();
                if(vectBillDetail!=null&&vectBillDetail.size()>0){
                    for(int i=0;i<vectBillDetail.size();i++){
                        Billdetail billdetail = (Billdetail)vectBillDetail.get(i);
                        
                        String whereBillDetailCode = PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID]+
                        " = "+billdetail.getOID();
                        Vector vectBillDetailCode = PstBillDetailCode.list(0,0, whereBillDetailCode,"");
                        
                        BillDetailCode billDetailCode = new BillDetailCode();
                        
                        double qtyReturned = CashSaleController.getQtyAlreadyReturned(billmain.getOID(),billdetail.getMaterialId(),"");
                        if(vectBillDetailCode!=null&&vectBillDetailCode.size()>0){
                            billDetailCode = (BillDetailCode)vectBillDetailCode.get(0);
                            qtyReturned = CashSaleController.getQtyAlreadyReturned(mainSale.getOID(),billdetail.getMaterialId(),billDetailCode.getStockCode());
                        }
                        else{
                            qtyReturned = CashSaleController.getQtyAlreadyReturned(mainSale.getOID(),billdetail.getMaterialId(),"");
                        }
                        hashBillDetailCode.put(billdetail.getSku()+"-"+billDetailCode.getStockCode(), billDetailCode);
                        defaultSaleModel.setHashBillDetailCode(hashBillDetailCode);
                        
                        billdetail.setDisc(billdetail.getDisc()*pct/100);
                        billdetail.setItemPrice(billdetail.getItemPrice()*pct/100);
                        billdetail.setTotalPrice(billdetail.getTotalPrice()*pct/100);
                        billdetail.setAmountAvailForReturn(billdetail.getQty()-qtyReturned);
                        hashBillDetail.put(billdetail.getSku()+"-"+billDetailCode.getStockCode(), billdetail);
                        vectShorter.add(billdetail.getSku()+"-"+billDetailCode.getStockCode());
                    }
                    defaultSaleModel.setHashBillDetail(hashBillDetail);
                    defaultSaleModel.setVectBillDetail(vectShorter);
                }
                
                // start other cost //
                String whereOtherCost = PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID]+
                " = " + mainSale.getOID();
                String orderOtherCost = PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_OTHER_COST_ID];
                Vector vectOtherCost = PstOtherCost.list(0,0,whereOtherCost,orderOtherCost);
                Hashtable hashOtherCost = new Hashtable();
                int sizeOtherCost = vectOtherCost.size();
                double totOtherCost = 0;
                for(int i = 0; i < sizeOtherCost; i++){
                    OtherCost otherCost = (OtherCost) vectOtherCost.get(i);
                    otherCost.setAmount(otherCost.getAmount()*pct/100);
                    totOtherCost = totOtherCost + otherCost.getAmount();
                    hashOtherCost.put(otherCost.getName(), otherCost);
                }
                defaultSaleModel.setHashOtherCost(hashOtherCost);
                defaultSaleModel.setTotOtherCost(totOtherCost);
                // end other cost //
                
                //start payment //
                String whereCashPayment = PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
                " = "+mainSale.getOID();
                Vector vectCashPayment = PstCashPayment.list(0,0, whereCashPayment, "");
                System.out.println("paV"+vectCashPayment.size());
                Hashtable hashCashPayment = new Hashtable();
                Hashtable hashCashPaymentInfo = new Hashtable();
                double totCardCost = 0;
                if(vectCashPayment!=null&&vectCashPayment.size()>0){
                    for(int z=0;z<vectCashPayment.size();z++){
                        System.out.println("pay"+z);
                        CashPayments cashPayment = (CashPayments)vectCashPayment.get(z);
                        
                        cashPayment.setAmount(cashPayment.getAmount()*pct/100);
                        hashCashPayment.put(cashPayment.getPayDateTime(), cashPayment);
                        
                        String whereCashPaymentInfo = PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]+
                        " = "+cashPayment.getOID();
                        Vector vectCashPaymentInfo = PstCashCreditCard.list(0,0,whereCashPaymentInfo,"");
                        if(vectCashPaymentInfo!=null&&vectCashPaymentInfo.size()==1){
                            CashCreditCard cashCreditCard = (CashCreditCard)vectCashPaymentInfo.get(0);
                            cashCreditCard.setAmount(cashCreditCard.getAmount()*pct/100);
                            totCardCost = totCardCost + cashCreditCard.getAmount();
                            hashCashPaymentInfo.put(cashPayment.getPayDateTime(), cashCreditCard);
                            defaultSaleModel.setCashPaymentInfo(hashCashPaymentInfo);
                            System.out.println("paI"+hashCashPaymentInfo.size());
                        }
                    }
                    System.out.println("pay"+hashCashPayment.size());
                    defaultSaleModel.setCashPayments(hashCashPayment);
                    defaultSaleModel.setTotCardCost(totCardCost);
                }
                //end payment //
                
                //start member point //
                String whereMemberPoint = PstMemberPoin.fieldNames[PstMemberPoin.FLD_CASH_BILL_MAIN_ID]+
                " = "+mainSale.getOID();
                Vector vectMemberPoint = PstMemberPoin.list(0,0, whereMemberPoint, "");
                if(vectMemberPoint!=null&&vectMemberPoint.size()==1){
                    MemberPoin memberPoin = (MemberPoin)vectMemberPoint.get(0);
                    defaultSaleModel.setMemberPoin(memberPoin);
                }
                //end member point //
                
                //set personal diskon //
                String wherePersonalDiskon = PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID]+
                " = "+customerServed.getOID();
                Vector vectPersonalDiskon = PstPersonalDiscount.list(0,0, wherePersonalDiskon,"");
                Hashtable hashPersonalDiscount = new Hashtable();
                if(vectPersonalDiskon!=null&&vectPersonalDiskon.size()>0){
                    for(int i=0;i<vectPersonalDiskon.size();i++){
                        PersonalDiscount personalDiscount = (PersonalDiscount)vectPersonalDiskon.get(i);
                        hashPersonalDiscount.put(new Long(personalDiscount.getMaterialId()), personalDiscount); //this right key i don't know???
                    }
                    defaultSaleModel.setHashPersonalDisc(hashPersonalDiscount);
                }
                //end personal diskon //
                
                //set sales person//
                if(CashierMainApp.isEnableSaleEntry()){
                    String whereSales = PstSales.fieldNames[PstSales.FLD_CODE]+" = '"+mainSale.getSalesCode()+"' ";
                    Vector vctSales = null;
                    try{
                        vctSales = PstSales.list(0,0, whereSales, "");
                        
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(vctSales!=null&&vctSales.size()>0){
                        Sales salesPerson = (Sales)vctSales.get(0);
                        defaultSaleModel.setSalesPerson(salesPerson);
                    }
                }
                else{
                    defaultSaleModel.setSalesPerson(new Sales());
                }
                //end sales person//
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("err di getdata : "+e.toString());
        }
        return defaultSaleModel;
    }
    
    public static void main(String args[]){
        
        //==================================
        //(Start)testing for inserting sale data
        //==================================
        BillMain billMain = new BillMain();
        billMain.setInvoiceNumber("050100002");
        //billMain.setInvoiceNo ("050100002");
        DefaultSaleModel defaultSaleModel = getData(billMain);//new DefaultSaleModel();
        
        MemberReg customerServed = defaultSaleModel.getCustomerServed();
        
        BillMain mainSale = defaultSaleModel.getMainSale();
        Hashtable hashBillDetail = defaultSaleModel.getHashBillDetail();
        Hashtable hashPayments = defaultSaleModel.getCashPayments();
        MemberPoin memberPoin = defaultSaleModel.getMemberPoin();
        
        System.out.println("mainSale oid >>>>>>>>>>> "+mainSale.getOID());
        System.out.println("hashBillDetail >>>>>>>>>>> "+hashBillDetail.size());
        System.out.println("payments >>>>>>>>>>> "+hashPayments.size());
        System.out.println("memberPoin >>>>>>>>>>> "+memberPoin.getOID());
        
        //==================================
        //(Start)testing for fetching sale data
        //==================================
    }
    public static void putPendingOrderData(PendingOrderModel pendingOrderModel){
        
        PendingOrder pendingOrder = pendingOrderModel.getPendingOrder();
        pendingOrder.setPaymentStatus(I_IJGeneral.PAYMENT_STATUS_NOT_POSTED);
        try{
            PstPendingOrder.insertExc(pendingOrder);
        }catch(Exception dbe){
            dbe.printStackTrace();
        }
    }
    
    public static void publishSaleData(SaleModel objSaleModel){
        
        I_Billing billingHelper = null;
        try{
            billingHelper = (I_Billing)Class.forName(I_Billing.classNameHanoman).newInstance();
            /*
            Enumeration enDetail = objSaleModel.getDetailBill().elements();
            while(enDetail.hasMoreElements()){
                DetailBillLink det = (DetailBillLink)enDetail.nextElement();
                //logger.debug("publishSaleData() Detail values = "+ det.getTotalSold());
            }
            Enumeration enPayment = objSaleModel.getPaymentBill().elements();
            while(enPayment.hasMoreElements()){
                PaymentLink paid = (PaymentLink)enPayment.nextElement();
                //logger.debug("publishSaleData() pay values = "+ paid.getAmount());
            }
            //
             */
            
            billingHelper.insertSale(objSaleModel);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
   /* public static SaleModel translateSaleModel(SaleModel saleModelLink, Billdetail billDetail){
    
                if(saleModelLink==null){
                    saleModelLink = new SaleModel();
                }
                DetailBillLink detailLink = new DetailBillLink();
                detailLink.setSaleDetailId (billDetail.getOID());
                detailLink.setMainBillId (saleModelLink.getMainBill ().getBillMainId ());
                detailLink.setCost (billDetail.getCost ());
                detailLink.setDisc (billDetail.getDisc ());
                detailLink.setDiscPct (billDetail.getDiscPct ());
                detailLink.setItemCode (billDetail.getSku ());
                detailLink.setItemId (billDetail.getMaterialId ());
                detailLink.setItemName (billDetail.getItemName ());
                detailLink.setItemPrice (billDetail.getItemPrice ());
                detailLink.setLocationId (saleModelLink.getMainBill().getLocationId ());
                detailLink.setSoldQty (billDetail.getQty ());
                detailLink.setTotalSold (billDetail.getTotalPrice ());
                detailLink.setUnitId (billDetail.getUnitId ());
                saleModelLink.getDetailBill ().add (detailLink);
                return saleModelLink;
    }
    */
    /*public static SaleModel translateSaleModel(SaleModel saleModelLink, CashPayments cashPayment,CashCreditCard cashCreditCard){
     
                PaymentLink paymentLink = new PaymentLink();
                double amount = cashPayment.getAmount ();
                paymentLink.setAmount(amount);
                String bankName = "";
                String accountName="";
                String accountNum = "";
                Date dueDate = new Date();
                switch(cashPayment.getPaymentType ()){
                    case PstCashPayment.CARD:
                        paymentLink.setPaymentType (PaymentLink.PAYMENT_TYPE_CC);
                        accountName = cashCreditCard.getHolderName ();
                        accountNum = cashCreditCard.getCcNumber ();
                        dueDate = cashCreditCard.getExpiredDate ();
                        break;
                    case PstCashPayment.CASH:
                        paymentLink.setPaymentType (PaymentLink.PAYMENT_TYPE_CASH);
                        //accountName = cashCreditCard.getCcName ();
                        //accountNum = cashCreditCard.getCcNumber ();
                        //dueDate = cashCreditCard.getExpiredDate ();
                        break;
                    case PstCashPayment.CHEQUE:
                        paymentLink.setPaymentType (PaymentLink.PAYMENT_TYPE_CHEQUE);
                        bankName = cashCreditCard.getChequeBank ();
                        accountName = cashCreditCard.getChequeAccountName ();
                        //accountNum = cashCreditCard.get
                        dueDate = cashCreditCard.getChequeDueDate ();
                        break;
                    case PstCashPayment.DEBIT:
                        paymentLink.setPaymentType (PaymentLink.PAYMENT_TYPE_DEBET_CARD);
                        bankName = cashCreditCard.getDebitBankName ();
                        accountName = cashCreditCard.getDebitCardName ();
     
                        dueDate = cashCreditCard.getChequeDueDate ();
                        break;
                    default:
                        paymentLink.setPaymentType (PaymentLink.PAYMENT_TYPE_CASH);
                        break;
                }
                paymentLink.setBankName (bankName);
                paymentLink.setCcName (cashCreditCard.getCcName());
                paymentLink.setCcNumber (accountNum);
                paymentLink.setExpDate (dueDate);
                paymentLink.setHolderName (accountName);
                double rate = cashPayment.getRate ();
                paymentLink.setRateUsed (rate);
                paymentLink.setBasicAmount (amount/rate);
                paymentLink.setBillMainId (saleModelLink.getMainBill ().getBillMainId ());
                paymentLink.setPayDateTime (cashPayment.getPayDateTime ());
     
                paymentLink.setPaymentId (cashPayment.getOID ());
                paymentLink.setPaymentCurrency (saleModelLink.getCustomer ().getCurrency ());
                saleModelLink.getPaymentBill ().add(paymentLink);
                return saleModelLink;
     
     
     
    }*/
    public static SaleModel translateSaleModel(SaleModel saleModelLink, DefaultSaleModel defaultSaleModel){
        
        BillMain mainSale = defaultSaleModel.getMainSale();
        CustomerLink customer = defaultSaleModel.getCustomerLink();
        StandartRate rateUsed = defaultSaleModel.getRateUsed();
        StandartRate defaultRateUsed = new StandartRate();
        CurrencyType type = (CurrencyType)CashierMainApp.getHashCurrencyType().get(CashierMainApp.getDSJ_CashierXML().getConfig(0).defaultCurrencyCode);
        defaultRateUsed = CashSaleController.getStandartRate(""+type.getOID());
        saleModelLink.setCustomer(customer);
        saleModelLink.getMainBill().setBillDate(mainSale.getBillDate());
        saleModelLink.getMainBill().setBillMainId(mainSale.getOID());
        saleModelLink.getMainBill().setCustomerId(mainSale.getCustomerId());
        saleModelLink.getMainBill().setDisc(mainSale.getDiscount()/rateUsed.getSellingRate());
        saleModelLink.getMainBill().setDiscType(mainSale.getDiscType());
        saleModelLink.getMainBill().setDocType(mainSale.getDocType());
        saleModelLink.getMainBill().setInvoiceNumber(mainSale.getInvoiceNumber());
        saleModelLink.getMainBill().setLocationId(mainSale.getLocationId());
        saleModelLink.getMainBill().setReservationId(customer.getReservationId());
        //saleModelLink.getMainBill().setReservationId(mainSale.getSpecialId());
        saleModelLink.getMainBill().setSalesCode(mainSale.getSalesCode());
        saleModelLink.getMainBill().setServicePct(mainSale.getServicePct());
        saleModelLink.getMainBill().setServiceValue(mainSale.getServiceValue()/rateUsed.getSellingRate());
        saleModelLink.getMainBill().setShiftId(mainSale.getShiftId());
        saleModelLink.getMainBill().setSoldCurrency(customer.getCurrency());
        //saleModelLink.getMainBill ().setSoldRate (defaultSaleModel.getRateUsed().getSellingRate());
        saleModelLink.getMainBill().setSoldRate(defaultRateUsed.getSellingRate());
        saleModelLink.getMainBill().setTaxPct(mainSale.getTaxPercentage());
        saleModelLink.getMainBill().setTaxValue(mainSale.getTaxValue()/rateUsed.getSellingRate());
        saleModelLink.getMainBill().setTotalAmount(defaultSaleModel.getTotalTrans()/rateUsed.getSellingRate());
        saleModelLink.getMainBill().setTransStatus(mainSale.getTransactionStatus());
        saleModelLink.getMainBill().setTransType(mainSale.getTransctionType());
        saleModelLink.getMainBill().setUserId(mainSale.getAppUserId());
        
        Enumeration enDetail = defaultSaleModel.getHashBillDetail().elements();
        while(enDetail.hasMoreElements()){
            Billdetail billDetail = (Billdetail)enDetail.nextElement();
            DetailBillLink detailLink = new DetailBillLink();
            detailLink.setSaleDetailId(billDetail.getOID());
            detailLink.setMainBillId(saleModelLink.getMainBill().getBillMainId());
            detailLink.setCost(billDetail.getCost()/rateUsed.getSellingRate());
            detailLink.setDisc(billDetail.getDisc()/rateUsed.getSellingRate());
            detailLink.setDiscPct(billDetail.getDiscPct());
            detailLink.setItemCode(billDetail.getSku());
            detailLink.setItemId(billDetail.getMaterialId());
            detailLink.setItemName(billDetail.getItemName());
            detailLink.setItemPrice(billDetail.getItemPrice()/rateUsed.getSellingRate());
            detailLink.setLocationId(saleModelLink.getMainBill().getLocationId());
            detailLink.setSoldQty(billDetail.getQty());
            detailLink.setTotalSold(billDetail.getTotalPrice()/rateUsed.getSellingRate());
            detailLink.setUnitId(billDetail.getUnitId());
            saleModelLink.getDetailBill().add(detailLink);
        }
        
        Enumeration enPayment = defaultSaleModel.getCashPayments().elements();
        Enumeration enPaymentInfo = defaultSaleModel.getCashPaymentInfo().elements();
        while(enPayment.hasMoreElements()){
            CashPayments cashPayment = (CashPayments)enPayment.nextElement();
            CashCreditCard cashCreditCard = new CashCreditCard();
            try{
                if(enPaymentInfo.hasMoreElements())
                    cashCreditCard = (CashCreditCard )enPaymentInfo.nextElement();
            }catch(Exception e){
                System.out.println("Err on translateSaleModel : "+e.toString());
            }
            
            PaymentLink paymentLink = new PaymentLink();
            double amount = cashPayment.getAmount();
            //double amountRate = cashPayment.getRate ();
            double amountRate = rateUsed.getSellingRate();
            paymentLink.setAmount(amount/amountRate);
            String bankName = "";
            String accountName="";
            String accountNum = "";
            Date dueDate = new Date();
            try{
                switch(cashPayment.getPaymentType()){
                    case PstCashPayment.CARD:
                        paymentLink.setPaymentType(PaymentLink.PAYMENT_TYPE_CC);
                        accountName = cashCreditCard.getHolderName();
                        accountNum = cashCreditCard.getCcNumber();
                        dueDate = cashCreditCard.getExpiredDate();
                        break;
                    case PstCashPayment.CASH:
                        paymentLink.setPaymentType(PaymentLink.PAYMENT_TYPE_CASH);
                        //accountName = cashCreditCard.getCcName ();
                        //accountNum = cashCreditCard.getCcNumber ();
                        //dueDate = cashCreditCard.getExpiredDate ();
                        break;
                    case PstCashPayment.CHEQUE:
                        paymentLink.setPaymentType(PaymentLink.PAYMENT_TYPE_CHEQUE);
                        bankName = cashCreditCard.getChequeBank();
                        accountName = cashCreditCard.getChequeAccountName();
                        //accountNum = cashCreditCard.get
                        dueDate = cashCreditCard.getChequeDueDate();
                        break;
                    case PstCashPayment.DEBIT:
                        paymentLink.setPaymentType(PaymentLink.PAYMENT_TYPE_DEBET_CARD);
                        bankName = cashCreditCard.getDebitBankName();
                        accountName = cashCreditCard.getDebitCardName();
                        
                        dueDate = cashCreditCard.getChequeDueDate();
                        break;
                    default:
                        paymentLink.setPaymentType(PaymentLink.PAYMENT_TYPE_CASH);
                        break;
                }
                
                paymentLink.setBankName(bankName);
                paymentLink.setCcName(cashCreditCard.getCcName());
                paymentLink.setCcNumber(accountNum);
                paymentLink.setExpDate(dueDate);
                paymentLink.setHolderName(accountName);
                //double rate = cashPayment.getRate ();
                paymentLink.setRateUsed(amountRate);
                paymentLink.setBasicAmount(amount/amountRate);
                paymentLink.setBillMainId(saleModelLink.getMainBill().getBillMainId());
                paymentLink.setPayDateTime(cashPayment.getPayDateTime());
                
                paymentLink.setPaymentId(cashPayment.getOID());
                paymentLink.setPaymentCurrency(saleModelLink.getCustomer().getCurrency());
                saleModelLink.getPaymentBill().add(paymentLink);
            }
            catch(Exception e){
                System.out.println("Err on translateSaleModel : "+e.toString());
            }
        }
        return saleModelLink;
    }
    /*public static SaleModel translateSaleModel(SaleModel saleModelLink, BillMain mainSale){
     
                if(saleModelLink==null){
                    saleModelLink = new SaleModel();
                }
     
                saleModelLink.getMainBill ().setBillDate (mainSale.getBillDate ());
                saleModelLink.getMainBill ().setBillMainId (mainSale.getOID ());
                saleModelLink.getMainBill ().setCustomerId (mainSale.getCustomerId ());
                saleModelLink.getMainBill ().setDisc (mainSale.getDiscount ());
                saleModelLink.getMainBill ().setDiscType (mainSale.getDiscType ());
                saleModelLink.getMainBill ().setDocType (mainSale.getDocType ());
                saleModelLink.getMainBill ().setInvoiceNumber (mainSale.getInvoiceNumber ());
                saleModelLink.getMainBill ().setLocationId (mainSale.getLocationId ());
                //saleModelLink.getMainBill ().setReservationId (mainSale.);
                saleModelLink.getMainBill ().setSalesCode (mainSale.getSalesCode ());
                saleModelLink.getMainBill ().setServicePct (mainSale.getServicePct ());
                saleModelLink.getMainBill ().setServiceValue (mainSale.getServiceValue ());
                saleModelLink.getMainBill ().setShiftId (mainSale.getShiftId ());
                //saleModelLink.getMainBill ().setSoldCurrency (mainSale.get);
                //saleModelLink.getMainBill ().getSoldRate ();
                saleModelLink.getMainBill ().setTaxPct (mainSale.getTaxPercentage ());
                saleModelLink.getMainBill ().setTaxValue (mainSale.getTaxValue ());
                //saleModelLink.getMainBill ().setTotalAmount (mainSale.get);
                saleModelLink.getMainBill ().setTransStatus (mainSale.getTransactionStatus ());
                saleModelLink.getMainBill ().setTransType (mainSale.getTransctionType ());
                saleModelLink.getMainBill ().setUserId (mainSale.getAppUserId ());
                return saleModelLink;
    }*/
}
