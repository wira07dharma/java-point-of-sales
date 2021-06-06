/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: May 1, 2006
 * Time: 1:44:05 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.pos.session.sale;

import com.dimata.pos.cashier.CashierMainApp;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.CashReturn;
import com.dimata.pos.entity.payment.PstCashReturn;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.masterCashier.CashMaster;
import com.dimata.pos.entity.masterCashier.PstCashMaster;
import com.dimata.pos.session.billing.makeInvoiceNo;
import com.dimata.pos.session.billing.SessBilling;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.session.sales.SessSaleCommision;
import com.dimata.posbo.session.masterdata.SessMaterial;
import com.dimata.posbo.report.sale.SaleReportItem;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.common.entity.payment.*;
import com.dimata.common.entity.custom.DataCustom;
import com.dimata.common.entity.system.PstSystemProperty;

import javax.servlet.http.HttpServletRequest;
import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

public class SessSellingProcess {

    private Sales currentSales = null;
    public Sales getCurrentSales() {
        if (currentSales == null) {
            currentSales = new Sales();
        }
        return currentSales;
    }

    /**
     * gadnyana
     * proses pencarian cash master
     * yang akan digunakan untuk
     * melengkapi daat cash bill main
     */
    public static CashMaster getCashMaster(long locationOID){
        DBResultSet dbrs = null;
        CashMaster cashMaster = new CashMaster();
        try{
            String sql = "select * from "+ PstCashMaster.TBL_CASH_MASTER+
                    " where "+PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID]+"="+locationOID;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                cashMaster.setOID(rs.getLong(PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]));
                cashMaster.setCashierNumber(rs.getInt(PstCashMaster.fieldNames[PstCashMaster.FLD_CASHIER_NUMBER]));
                cashMaster.setLocationId(rs.getLong(PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID]));
                cashMaster.setPriceType(rs.getString(PstCashMaster.fieldNames[PstCashMaster.FLD_PRICE_TYPE]));
                cashMaster.setCashService(rs.getDouble(PstCashMaster.fieldNames[PstCashMaster.FLD_SERVICE]));
                cashMaster.setCashTax(rs.getDouble(PstCashMaster.fieldNames[PstCashMaster.FLD_TAX]));
            }
            rs.close();
        }catch(Exception e){
            System.out.println("**** err ."+e.toString());
            cashMaster = new CashMaster();
        }finally{
            DBResultSet.close(dbrs);
        }
        return cashMaster;
    }

    public static CashCashier getCashCashier(long masterOID){
        DBResultSet dbrs = null;
        CashCashier cashCashier = new CashCashier();
        try{
            String sql = "select * from "+ PstCashCashier.TBL_CASH_CASHIER+
                    " where "+PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID]+"="+masterOID+
                    " and "+PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID]+"=1";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                PstCashCashier.resultToObject(rs,cashCashier);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("**** err ."+e.toString());
            cashCashier = new CashCashier();
        }finally{
            DBResultSet.close(dbrs);
        }
        return cashCashier;
    }

    public static MemberGroup getMemberGroup(int memberType){
        DBResultSet dbrs = null;
        MemberGroup memberGroup = new MemberGroup();
        try{
            String sql = "select * from "+PstMemberGroup.TBL_MEMBER_GROUP+
                    " where "+PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE]+"="+memberType;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                PstMemberGroup.resultToObject(rs,memberGroup);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("**** err ."+e.toString());
            memberGroup = new MemberGroup();
        }finally{
            DBResultSet.close(dbrs);
        }
        return memberGroup;
    }

    public static MemberReg getNonMember(){
        DBResultSet dbrs = null;
        MemberReg memberReg = new MemberReg();
        try{
            String sql = "select * from "+ PstMemberReg.TBL_CONTACT_LIST+
                    " where "+PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID]+"="+getMemberGroup(PstMemberGroup.UMUM).getOID();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                PstMemberReg.resultToObject(rs,memberReg);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("**** err ."+e.toString());
            memberReg = new MemberReg();
        }finally{
            DBResultSet.close(dbrs);
        }
        return memberReg;
    }

    public static MemberReg getMember(long memberOID){
        DBResultSet dbrs = null;
        MemberReg memberReg = new MemberReg();
        try{
            String sql = "select * from "+ PstMemberReg.TBL_CONTACT_LIST+
                    " where "+PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID]+"="+getMemberGroup(PstMemberGroup.UMUM).getOID();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                PstMemberReg.resultToObject(rs,memberReg);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("**** err ."+e.toString());
            memberReg = new MemberReg();
        }finally{
            DBResultSet.close(dbrs);
        }
        return memberReg;
    }

    public static StandartRate getLatestRate(String currencyId) {
        StandartRate standartRate = null;
        String standWhereLatestRate = PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + "='" + currencyId + "'" +
                " AND " + PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS] + "=" + PstStandartRate.ACTIVE;
        String standOrderLatestRate = PstStandartRate.fieldNames[PstStandartRate.FLD_END_DATE] + " DESC ";
        Vector vctStandartRate = PstStandartRate.list(0, 0, standWhereLatestRate, standOrderLatestRate);
        int sizeSt = vctStandartRate.size();
        CurrencyType type = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(currencyId));
        if (sizeSt > 0) {
            standartRate = (StandartRate) vctStandartRate.get(0);
        }
        return standartRate;
    }

    public static CurrencyType getCurrencyType(String code) {
        DBResultSet dbrs = null;
        CurrencyType currencyType = new CurrencyType();
        try{
            String sql = "select * from "+ PstCurrencyType.TBL_POS_CURRENCY_TYPE+
                    " where "+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]+"='"+code+"'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                PstCurrencyType.resultToObject(rs,currencyType);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("**** err ."+e.toString());
            currencyType = new CurrencyType();
        }finally{
            DBResultSet.close(dbrs);
        }
        return currencyType;
    }

    public BillMain getBillMain(HttpServletRequest request, int docType, String currCode){
        BillMain billMain = new BillMain(); // defaultSaleModel.getMainSale();
        try {
            long locationOid = FRMQueryString.requestLong(request, "location_name");
            int status = FRMQueryString.requestInt(request, "trans_status");
            CurrencyType currType = getCurrencyType(currCode); // defaultSaleModel.getCurrencyTypeUsed();
            StandartRate standartRate = getLatestRate(String.valueOf(currType.getOID())); //defaultSaleModel.getRateUsed();
            billMain.setDocType(docType);
            int cashierNo = 1; // CashierMainApp.getCashMaster().getCashierNumber();
            CashCashier cashCashier = getCashCashier(getCashMaster(locationOid).getOID());
            long shiftOid = 0;//CashierMainApp.getShift().getOID();
            billMain.setInvoiceCounter(PstBillMain.getCounterTransaction(locationOid, cashierNo, docType));
            billMain.setInvoiceNumber(PstBillMain.generateNumberInvoice(new Date(),locationOid, cashierNo, docType));
            billMain.setInvoiceNo(makeInvoiceNo.setInvoiceNumber());
            billMain.setCashCashierId(cashCashier.getOID());
            billMain.setLocationId(locationOid);
            billMain.setShiftId(shiftOid);
            billMain.setCurrencyId(currType.getOID());
            billMain.setRate(standartRate.getSellingRate());
        }catch(Exception e){}
        return billMain;
    }

    public static long prosesInsertDataSale(Vector listItem, HttpServletRequest request,
                                            int docType,
                                            AppUser appUser,String currCode, long memberOid) {
        long oid = 0;
        try {
            double payment = FRMQueryString.requestDouble(request, "txtpayment");
            double change = FRMQueryString.requestDouble(request, "txtchange");
            long locationOid = FRMQueryString.requestLong(request, "location_name");
            long salesOid = FRMQueryString.requestLong(request, "sales_name");
            int status = FRMQueryString.requestInt(request, "trans_status");
            //long memberOID = FRMQueryString.requestLong(request, "member_oid");

            try{
                locationOid = Long.parseLong((String) PstSystemProperty.getValueByName("INTERNAL_LOCATION"));
            }catch(Exception e){
                locationOid = 0;
            }

            System.out.println("status : " + status);
            System.out.println("PstBillMain.TRANS_STATUS_CLOSE : " + PstBillMain.TRANS_STATUS_CLOSE);
            System.out.println("PstBillMain.TRANS_STATUS_OPEN  : " + PstBillMain.TRANS_STATUS_OPEN);

            //************ proses set data for main ******************
            CurrencyType currType = getCurrencyType(currCode); // defaultSaleModel.getCurrencyTypeUsed();
            StandartRate standartRate = getLatestRate(String.valueOf(currType.getOID())); //defaultSaleModel.getRateUsed();

            BillMain billMain = new BillMain(); // defaultSaleModel.getMainSale();
            billMain.setDocType(docType);

            //long locationOID = 0; // CashierMainApp.getCashMaster().getLocationId();
            CashCashier cashCashier = getCashCashier(getCashMaster(locationOid).getOID());
            int cashierNo = 1;
            try{
                CashMaster cashMaster = PstCashMaster.fetchExc(cashCashier.getCashMasterId());
                cashierNo = cashMaster.getCashierNumber();
            }catch(Exception e){}
             // CashierMainApp.getCashMaster().getCashierNumber();
            long shiftOid = CashierMainApp.getShift().getOID();

            MemberReg member = new MemberReg(); // CashSaleController.getCustomerNonMember();
            if(memberOid==0){
                member = getNonMember();
            }else{
                member = PstMemberReg.fetchExc(memberOid);
            }

            billMain.setInvoiceCounter(PstBillMain.getCounterTransaction(locationOid, cashierNo, docType));
            billMain.setInvoiceNumber(PstBillMain.generateNumberInvoice(new Date(),locationOid, cashierNo, docType));
            billMain.setInvoiceNo(billMain.getInvoiceNumber()); // makeInvoiceNo.setInvoiceNumber());
            billMain.setAppUserId(appUser.getOID());
            billMain.setCashCashierId(cashCashier.getOID());
            billMain.setLocationId(locationOid);
            billMain.setShiftId(shiftOid);
            billMain.setCurrencyId(currType.getOID());
            billMain.setRate(standartRate.getSellingRate());

            //System.out.println("defaultSaleModel : " + defaultSaleModel);
            System.out.println("locationOid : " + locationOid + " listItem" + listItem);
            //System.out.println("defaultSdsgsdfgdfgdfgsdfaleModel : " + defaultSaleModel);

            Sales sales = new Sales();
            try{
                sales = PstSales.fetchExc(salesOid);
            }catch(Exception e){}
            billMain.setSalesCode(sales.getCode());
            billMain.setTransactionStatus(status); // PstBillMain.TRANS_STATUS_CLOSE);
            if (status == PstBillMain.TRANS_STATUS_CLOSE) {
                billMain.setTransctionType(PstBillMain.TRANS_TYPE_CASH);
            } else {
                billMain.setTransctionType(PstBillMain.TRANS_TYPE_CASH);
            }
            try {
                billMain.setAppUserId(appUser.getOID()); // CashierMainApp.getCashCashier().getAppUserId());
                billMain.setCustomerId(member.getOID());
                // billMain.setCashCashierId(CashierMainApp.getCashCashier().getOID());
                billMain.setShiftId(0); //CashierMainApp.getShift().getOID());
            } catch (Exception e) {
            }

            billMain.setLocationId(locationOid);
            billMain.setBillDate(new Date());

            oid = PstBillMain.insertExc(billMain);
            /**
             * gadnyana
             * proses insert data discount data cost ppn
             * ini sesuai dengan permintaan dari client Surft Travel Online
             */
            double discOther = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER]);
            double discCargo = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO]);
            double discSupp = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER]);
            double costPpn = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN]);

            double discOutlet = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.DISC_OUTLET]);
            double commOutlet = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COMM_OUTLET]);
            double savingPc = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);


            Vector list = new Vector();
            DataCustom dataCustom = new DataCustom();
            // disc other
            dataCustom.setOwnerId(oid);
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER]);
            dataCustom.setDataValue(String.valueOf(discOther));
            list.add(dataCustom);
            // disc cargo
            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO]);
            dataCustom.setDataValue(String.valueOf(discCargo));
            list.add(dataCustom);
            // disc supp
            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER]);
            dataCustom.setDataValue(String.valueOf(discSupp));
            list.add(dataCustom);
            // cost ppn
            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN]);
            dataCustom.setDataValue(String.valueOf(costPpn));
            list.add(dataCustom);

            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.DISC_OUTLET]);
            dataCustom.setDataValue(String.valueOf(discOutlet));
            list.add(dataCustom);

            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COMM_OUTLET]);
            dataCustom.setDataValue(String.valueOf(commOutlet));
            list.add(dataCustom);

            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);
            dataCustom.setDataValue(String.valueOf(savingPc));
            list.add(dataCustom);

            SessSaleCommision.saveDataCommision(list);

            double totalitem = 0;
            if (oid != 0) {
                for (int k = 0; k < listItem.size(); k++) {
                    Billdetail billdetail = (Billdetail) listItem.get(k);
                    try {
                        Material mat = PstMaterial.fetchExc(billdetail.getMaterialId());
                        billdetail.setBillMainId(oid);
                        billdetail.setUnitId(mat.getDefaultStockUnitId());
                        long oidx = PstBillDetail.insertExc(billdetail);
                        totalitem = totalitem + billdetail.getTotalPrice();
                    } catch (Exception e) {
                    }
                }

                CashPayments cashPayments = new CashPayments();
                cashPayments.setAmount(totalitem);
                cashPayments.setBillMainId(oid);
                cashPayments.setCurrencyId(billMain.getCurrencyId());
                cashPayments.setPayDateTime(billMain.getBillDate());
                cashPayments.setPaymentStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                cashPayments.setPaymentType(PstCashPayment.CASH);
                cashPayments.setRate(billMain.getRate());
                cashPayments.setUpdateStatus(PstCashPayment.UPDATE_STATUS_INSERTED);
                PstCashPayment.insertExc(cashPayments);

                if (change > 0) {
                    CashReturn cashReturn = new CashReturn();
                    cashReturn.setAmount(payment - totalitem);
                    cashReturn.setBillMainId(oid);
                    cashReturn.setCurrencyId(billMain.getCurrencyId());
                    cashReturn.setPaymentStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                    cashReturn.setRate(billMain.getRate());
                    PstCashReturn.insertExc(cashReturn);
                }
            }
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        }
        return oid;
    }

    public static void prosesUpdateDataSale(Vector listItem, HttpServletRequest request,long oidMain, AppUser appUser) {
        try {

            double payment = FRMQueryString.requestDouble(request, "txtpayment");
            double change = FRMQueryString.requestDouble(request, "txtchange");
            long locationOid = FRMQueryString.requestLong(request, "location_name");
            int status = FRMQueryString.requestInt(request, "trans_status");

            System.out.println("status : " + status);
            System.out.println("PstBillMain.TRANS_STATUS_CLOSE : " + PstBillMain.TRANS_STATUS_CLOSE);
            System.out.println("PstBillMain.TRANS_STATUS_OPEN : " + PstBillMain.TRANS_STATUS_OPEN);
            BillMain billMain = PstBillMain.fetchExc(oidMain);
            billMain.setTransactionStatus(status);
            if (status == PstBillMain.TRANS_STATUS_CLOSE) {
                billMain.setTransctionType(PstBillMain.TRANS_TYPE_CASH);
            } else {
                billMain.setTransctionType(PstBillMain.TRANS_TYPE_CASH);
            }
            try {
                billMain.setAppUserId(appUser.getOID());
                billMain.setShiftId(0);
            } catch (Exception e) {}


            /**
             * gadnyana
             * proses insert data discount data cost ppn
             * ini sesuai dengan permintaan dari client Surft Travel Online
             */
            double discOther = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER]);
            double discCargo = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO]);
            double discSupp = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER]);
            double costPpn = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN]);

            double discOutlet = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.DISC_OUTLET]);
            double commOutlet = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COMM_OUTLET]);
            double savingPc = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);

            Vector list = list = SessSaleCommision.getDataCommision(oidMain);
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    DataCustom dataCustom = (DataCustom)list.get(i);
                    try{
                        if(dataCustom.getDataName().equals(SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER])){
                            dataCustom.setDataValue(String.valueOf(discOther));
                            list.setElementAt(dataCustom,i);
                        }
                        if(dataCustom.getDataName().equals(SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO])){
                            dataCustom.setDataValue(String.valueOf(discCargo));
                            list.setElementAt(dataCustom,i);
                        }
                        if(dataCustom.getDataName().equals(SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER])){
                            dataCustom.setDataValue(String.valueOf(discSupp));
                            list.setElementAt(dataCustom,i);
                        }
                        if(dataCustom.getDataName().equals(SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN])){
                            dataCustom.setDataValue(String.valueOf(costPpn));
                            list.setElementAt(dataCustom,i);
                        }
                        if(dataCustom.getDataName().equals(SessSaleCommision.strNameCommision[SessSaleCommision.DISC_OUTLET])){
                            dataCustom.setDataValue(String.valueOf(discOutlet));
                            list.setElementAt(dataCustom,i);
                        }
                        if(dataCustom.getDataName().equals(SessSaleCommision.strNameCommision[SessSaleCommision.COMM_OUTLET])){
                            dataCustom.setDataValue(String.valueOf(commOutlet));
                            list.setElementAt(dataCustom,i);
                        }
                        if(dataCustom.getDataName().equals(SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC])){
                            dataCustom.setDataValue(String.valueOf(savingPc));
                            list.setElementAt(dataCustom,i);
                        }
                    }catch(Exception e){}
                }
                // update value commision if exist
                SessSaleCommision.saveDataCommision(list);
            }

            billMain.setLocationId(locationOid);
            PstBillMain.updateExc(billMain);
            SessBilling.deleteBillDetail(billMain.getOID());
            PstCashPayment.deleteBillPayments(billMain.getOID());
            PstCashReturn.deleteBillReturn(billMain.getOID());

            double totalitem = 0;
            if (billMain.getOID() != 0) {
                for (int k = 0; k < listItem.size(); k++) {
                    Billdetail billdetail = (Billdetail) listItem.get(k);
                    try {
                        Material mat = PstMaterial.fetchExc(billdetail.getMaterialId());
                        billdetail.setBillMainId(billMain.getOID());
                        billdetail.setUnitId(mat.getDefaultStockUnitId());
                        long oidx = PstBillDetail.insertExc(billdetail);
                        totalitem = totalitem + billdetail.getTotalPrice();
                    } catch (Exception e) {
                    }
                }

                CashPayments cashPayments = new CashPayments();
                cashPayments.setAmount(totalitem);
                cashPayments.setBillMainId(billMain.getOID());
                cashPayments.setCurrencyId(billMain.getCurrencyId());
                cashPayments.setPayDateTime(billMain.getBillDate());
                cashPayments.setPaymentStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                cashPayments.setPaymentType(PstCashPayment.CASH);
                cashPayments.setRate(billMain.getRate());
                cashPayments.setUpdateStatus(PstCashPayment.UPDATE_STATUS_INSERTED);
                PstCashPayment.insertExc(cashPayments);

                if (change > 0) {
                    CashReturn cashReturn = new CashReturn();
                    cashReturn.setAmount(payment - totalitem);
                    cashReturn.setBillMainId(billMain.getOID());
                    cashReturn.setCurrencyId(billMain.getCurrencyId());
                    cashReturn.setPaymentStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                    cashReturn.setRate(billMain.getRate());
                    PstCashReturn.insertExc(cashReturn);
                }
            }
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        }
    }

    /**
     * gadnyana
     * untuk pengambilan data bill man
     */
    public BillMain getBillMain(int docType, String currCode, long locationOid){
        BillMain billMain = new BillMain(); // defaultSaleModel.getMainSale();
        try {
            try{
                locationOid = Long.parseLong((String) PstSystemProperty.getValueByName("INTERNAL_LOCATION"));
            }catch(Exception e){
                locationOid = 0;
            }
            CurrencyType currType = getCurrencyType(currCode); // defaultSaleModel.getCurrencyTypeUsed();
            StandartRate standartRate = getLatestRate(String.valueOf(currType.getOID())); //defaultSaleModel.getRateUsed();
            billMain.setDocType(docType);
            CashCashier cashCashier = getCashCashier(getCashMaster(locationOid).getOID());
            int cashierNo = 1; // CashierMainApp.getCashMaster().getCashierNumber();
            try{
                CashMaster cashMaster = PstCashMaster.fetchExc(cashCashier.getCashMasterId());
                cashierNo = cashMaster.getCashierNumber();
            }catch(Exception e){}
            long shiftOid = 0;//CashierMainApp.getShift().getOID();
            billMain.setInvoiceCounter(PstBillMain.getCounterTransaction(docType));
            billMain.setInvoiceNumber(PstBillMain.generateNumberInvoice(new Date(), docType));
            billMain.setInvoiceNo(makeInvoiceNo.setInvoiceNumber());
            billMain.setCashCashierId(cashCashier.getOID());
            billMain.setLocationId(locationOid);
            billMain.setShiftId(shiftOid);
            billMain.setCurrencyId(currType.getOID());
            billMain.setRate(standartRate.getSellingRate());
        }catch(Exception e){}
        return billMain;
    }

    /**
     * gadnyana
     */
    public Vector setGetDataSellingInvioceInternal(Vector list, long locationOid,
                                                   int docType, String currCode, long memberOid){
        Vector listMain = new Vector();
        try{
            if(list!=null && list.size()>0){
                double totalDiscount = 0.0;
                Vector listItem = new Vector();
                BillMain billMain = getBillMain(docType,currCode,locationOid);
                for(int k=0;k<list.size();k++){
                    Material material = new Material();
                    SaleReportItem saleItem = (SaleReportItem) list.get(k);
                    try{
                        material = PstMaterial.fetchExc(saleItem.getItemId());
                    }catch(Exception e){
                        System.out.println("err. "+e.toString());
                    }
                    Billdetail billDetail = new Billdetail();
                    billDetail.setSku(saleItem.getItemCode());
                    billDetail.setQty(saleItem.getTotalQty());
                    billDetail.setItemName(saleItem.getItemName());
                    double priceSales = SessMaterial.getPriceSale(material);
                    billDetail.setItemPrice(priceSales);
                    billDetail.setMaterialId(saleItem.getItemId());
                    billDetail.setMaterialType(material.getMaterialType());
                    billDetail.setUnitId(material.getDefaultStockUnitId());
                    billDetail.setDiscType(PstBillDetail.TYPE_DISC_PCT);

                    /*PersonalDiscount personalDiscount = new PersonalDiscount();
                    try{
                        personalDiscount = getDiscKhususProduct(memberOid,saleItem.getItemId());
                    }catch(Exception e){}
                    double totalDiscItem = 0.0;
                    if(personalDiscount.getPersDiscPct()!=0){
                        totalDiscItem = (priceSales * personalDiscount.getPersDiscPct())/100;
                        billDetail.setDiscPct(personalDiscount.getPersDiscPct());
                    }else if(personalDiscount.getPersDiscVal()!=0){
                        totalDiscItem = priceSales - personalDiscount.getPersDiscVal();
                    }*/

                    billDetail.setDisc(saleItem.getDiscount());
                    double totalDiscItem = saleItem.getDiscount();
                    totalDiscount = totalDiscount + totalDiscItem;
                    billDetail.setTotalPrice(saleItem.getTotalSold()); // (priceSales-totalDiscItem) * billDetail.getQty());
                   // billDetail.setDisc(totalDiscItem * billDetail.getQty());
                    // add data ke vector
                    // yang akan di proses selajutnya
                    listItem.add(billDetail);
                }

                // add hasil dari
                // list item ke vector main
                listMain.add(billMain);
                listMain.add(listItem);
                listMain.add(String.valueOf(totalDiscount));
            }
        }catch(Exception e){
            System.out.println("=>>> err. "+e.toString());
        }finally{
            if(listMain.size()==0){
                listMain.add(new BillMain());
                listMain.add(new Vector());
            }
        }
        return listMain;
    }

    /**
     * gadnyana
     * ini untuk pencarian data disc
     * berdasarkan location/member
     */
    public PersonalDiscount getDiscKhususProduct(long contactOid, long materialOid){
        DBResultSet dbrs = null;
        PersonalDiscount personalDiscount = new PersonalDiscount();
        try{
            String where = PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID]+"="+contactOid+
                    " and "+PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_MATERIAL_ID]+"="+materialOid;
            Vector list = PstPersonalDiscount.list(0,0,where,"");
            if(list!=null && list.size()>0){
                personalDiscount = (PersonalDiscount)list.get(0);
            }
        }catch(Exception e){
            System.out.println("err."+e.toString());
        }
        return personalDiscount;
    }
}
