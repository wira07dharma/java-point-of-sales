/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.pos.form.payment;

/**
 *
 * @author Wiweka
 */
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PaymentInfo;
import com.dimata.common.entity.payment.PstPaymentInfo;
import com.dimata.common.form.payment.FrmPaymentInfo;
import java.util.*;
import javax.servlet.http.*;

// dimata package
import com.dimata.util.*;
import com.dimata.util.lang.*;

// qdep package
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
//import com.dimata.qdep.db.*;

// project package
import com.dimata.pos.entity.payment.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.session.billing.*;
import com.dimata.posbo.db.DBException;

import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.masterdata.MemberReg;
import com.dimata.posbo.session.sales.SessAr;

public class CtrlCashCreditPaymentMain extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Code Material Sales sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Material Sales Code already exist", "Data incomplete"}
    };
    private int start;
    private String msgString;
    private CreditPaymentMain creditPaymentMain;
    private BillMain billMain;
    private PstBillMain pstBillMain;
    private CashCreditPaymentsDinamis cashCreditPaymentsDinamis = new CashCreditPaymentsDinamis();
    private CashCreditPaymentInfo cashCreditPaymentInfo = new CashCreditPaymentInfo();
    private PstCashCreditPaymentDinamis pstCashCreditPaymentDinamis;
    private PstCashCreditPaymentInfo pstCashCreditPaymentInfo;
    private PstCreditPaymentMain pstCreditPaymentMain;
    private FrmCashCreditPaymentMain frmCashCreditPaymentMain;
    private FrmCashCreditPayment frmCashCreditPayment;
    private FrmCashCreditPaymentInfo frmCashCreditPaymentInfo;
    private CashReturn cashReturn;
    private PstCashReturn pstCashReturn;
    int language = LANGUAGE_FOREIGN;
    private PaymentInfo paymentInfo;
    private PstPaymentInfo pstPaymentInfo;
    private FrmPaymentInfo frmPaymentInfo;

    public CtrlCashCreditPaymentMain(HttpServletRequest request) {
        msgString = "";
        creditPaymentMain = new CreditPaymentMain();
        paymentInfo = new PaymentInfo();
        try {
            pstCreditPaymentMain = new PstCreditPaymentMain(0);
        } catch (Exception e) {
            ;
        }
        frmCashCreditPaymentMain = new FrmCashCreditPaymentMain(request, creditPaymentMain);
        frmCashCreditPayment = new FrmCashCreditPayment(request,cashCreditPaymentsDinamis);
        frmCashCreditPaymentInfo = new FrmCashCreditPaymentInfo(request,cashCreditPaymentInfo);
        frmPaymentInfo = new FrmPaymentInfo(request, paymentInfo);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCashCreditPaymentMain.addError(frmCashCreditPaymentMain.FRM_FIELD_CREDIT_PAYMENT_MAIN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public CreditPaymentMain getCreditPaymentMain() {
        return creditPaymentMain;
    }

    public FrmCashCreditPaymentMain getForm() {
        return frmCashCreditPaymentMain;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public PaymentInfo getPaymentInfo() {
        return this.paymentInfo;
    }

      public CashCreditPaymentInfo getCashCreditPaymentInfo() {
        return this.cashCreditPaymentInfo;
    }

    public int action(int cmd, long getCreditPaymentMain, long oidBillMain, double amount) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        int counter = 0;
        boolean incrementAllPrType = true;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (getCreditPaymentMain != 0) {
                    try {
                        creditPaymentMain = PstCreditPaymentMain.fetchExc(getCreditPaymentMain);
                    } catch (Exception exc) {
                    }
                }

                frmCashCreditPaymentMain.requestEntityObject(creditPaymentMain);
                billMain = new BillMain();
                try {
                    billMain = PstBillMain.fetchExc(oidBillMain);
                } catch (Exception e) {
                    System.out.println("Customer not found ...");
                }
                creditPaymentMain.setBillMainId(oidBillMain);
                creditPaymentMain.setInvoiceCounter(billMain.getInvoiceCounter());
                creditPaymentMain.setDocType(billMain.getDocType());
                creditPaymentMain.setInvoiceNumber(billMain.getInvoiceNumber());
                creditPaymentMain.setBillDate(billMain.getBillDate());
                creditPaymentMain.setBillStatus(billMain.getBillStatus());

                if (frmCashCreditPaymentMain.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (creditPaymentMain.getOID() == 0) {
                    try {
                        long oid = pstCreditPaymentMain.insertExc(this.creditPaymentMain);

                        if (oid != 0) {

                            //insert credit payment
                            frmCashCreditPayment.requestEntityObject(cashCreditPaymentsDinamis);
                            cashCreditPaymentsDinamis.setAmount(creditPaymentMain.getPayAmountCredit());
                            cashCreditPaymentsDinamis.setCreditMainId(creditPaymentMain.getOID());
                            long oidPayment = pstCashCreditPaymentDinamis.insertExc(this.cashCreditPaymentsDinamis);

                            double payAmount = PstCreditPaymentMain.getSumPayment("CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + oidBillMain + "'");
                            if(payAmount >= amount){
                            //Update Transaction Status
                            int transactionStatus = 0;
                            
                            //Update Invoice Number
                            Date billdate = new Date();
                            billMain = PstBillMain.fetchExc(oidBillMain);
                            int invoiceCounter = SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType);
                            billMain.setInvoiceCounter(invoiceCounter);
                            String invoiceNo = SessBilling.getCodeOrderMaterial(billMain);
                           // String InvoiceNo = pstBillMain.updateInvoiceNo(oidBillMain, invoiceCounter, invoiceNo);
                            }

                            //insert payment info      
                            frmCashCreditPaymentInfo.requestEntityObject(cashCreditPaymentInfo);
                            cashCreditPaymentInfo.setPaymentId(cashCreditPaymentsDinamis.getOID());
                            cashCreditPaymentInfo.setAmount(cashCreditPaymentsDinamis.getAmount());
                            long oidPaymentInfo = pstCashCreditPaymentInfo.insertExc(this.cashCreditPaymentInfo);


                        } else {
                            msgString = getSystemMessage(1);
                        }

                        if (oid != 0) {
                            //proses insert return payment
                            CashReturn cashReturn = new CashReturn();
                            cashReturn.setBillMainId(oidBillMain);
                            cashReturn.setCurrencyId(billMain.getCurrencyId());
                            cashReturn.setRate(billMain.getRate());
                            cashReturn.setAmount(creditPaymentMain.getAmountReturn());

                            oid = pstCashReturn.insertExc(cashReturn);
                        } else {
                            msgString = getSystemMessage(1);
                        }


                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstCreditPaymentMain.updateExc(this.creditPaymentMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (getCreditPaymentMain != 0) {
                    try {
                        creditPaymentMain = PstCreditPaymentMain.fetchExc(getCreditPaymentMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (getCreditPaymentMain != 0) {
                    try {
                        creditPaymentMain = PstCreditPaymentMain.fetchExc(getCreditPaymentMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (getCreditPaymentMain != 0) {
                    try {
                        long oid = PstCreditPaymentMain.deleteExc(getCreditPaymentMain);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default:

        }
        return rsCode;
    }

     public int requestPayment(HttpServletRequest req, long oidCustomer, double amountTotal, long oidCashCashier, long shiftID, long locationId, long userID){
        int strError=0;
        BillMain bMain = new BillMain();
        try{
            Date DueDate = FRMQueryString.requestDate(req, FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_DUE_DATE]);
            Date TanggalPencairan = FRMQueryString.requestDate(req, FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_TANGGAL_PENCAIRAN]);
            String whereClauseBillMain = PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = '0' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = '1' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = '1' AND " + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] +" = '"+oidCustomer+"'";
            String orderClauseBillMain = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
            Vector records = PstBillMain.listInvoiceToPayment(0, 0, whereClauseBillMain, orderClauseBillMain);
            double sisaPaid=amountTotal;
            //double sisaAmount=0.0;
            if(records!=null && records.size()>0){
                for(int i=0; i<records.size(); i++){
                    Vector vt = (Vector) records.get(i);
                    BillMain billMainx = (BillMain) vt.get(0);
                    MemberReg memberReg = (MemberReg) vt.get(1);
                    CurrencyType currencyTypeX = (CurrencyType) vt.get(2);
                    AppUser appUser = (AppUser) vt.get(3);
                    double paid=0.0;
                    double totalPaymentPaid= SessAr.getSumPaymentForArInvoice(billMainx.getOID());
                    double totalBill = billMainx.getAmount()+billMainx.getServiceValue()+billMainx.getTaxValue()-billMainx.getDiscount();
                    double balanceAr = totalBill-totalPaymentPaid;
                    if(FRMQueryString.requestInt(req, "invoice_"+billMainx.getOID())==1){
                            CreditPaymentMain creditPaymentMainSplit = new CreditPaymentMain();
                            creditPaymentMainSplit.setBillMainId(billMainx.getOID());
                            creditPaymentMainSplit.setInvoiceCounter(billMainx.getInvoiceCounter());
                            creditPaymentMainSplit.setDocType(billMainx.getDocType());
                            creditPaymentMainSplit.setInvoiceNumber(billMainx.getInvoiceNumber());
                            creditPaymentMainSplit.setBillDate(billMainx.getBillDate());
                            creditPaymentMainSplit.setBillStatus(billMainx.getBillStatus());
                            creditPaymentMainSplit.setLocationId(locationId);
                            creditPaymentMainSplit.setBillDate(billMainx.getBillDate());
                            creditPaymentMainSplit.setAppUserId(userID);
                            creditPaymentMainSplit.setAppUserSalesId(billMainx.getAppUserSalesId());
//                            creditPaymentMainSplit.setSalesCode(billMainx.getSalesCode());
                            creditPaymentMainSplit.setInvoiceCounter(billMainx.getInvoiceCounter());
                            creditPaymentMainSplit.setDocType(billMainx.getDocType());
                            creditPaymentMainSplit.setShiftId(shiftID);
                            creditPaymentMainSplit.setCashCashierId(oidCashCashier);
                            
                            //insert di cash_credit_payment_main
                            long oid = pstCreditPaymentMain.insertExc(creditPaymentMainSplit);

                             if (oid != 0) {
                                //insert credit payment
                                //cek sisa pembayaran
                                if(sisaPaid>=balanceAr){
                                    paid=balanceAr;
                                    CashCreditPaymentsDinamis cashCreditPaymentsDinamis = new CashCreditPaymentsDinamis();
                                    frmCashCreditPayment.requestEntityObject(cashCreditPaymentsDinamis);
                                    cashCreditPaymentsDinamis.setAmount(paid);
                                    cashCreditPaymentsDinamis.setCreditMainId(oid);
                                     //insert cash_credit_payment
                                    long oidPayment = pstCashCreditPaymentDinamis.insertExc(cashCreditPaymentsDinamis);
                                        if(oidPayment!=0){
                                            //insert payment info
                                            CashCreditPaymentInfo cashCreditPaymentInfoMultiPayment = new CashCreditPaymentInfo();
                                            frmCashCreditPaymentInfo.requestEntityObject(cashCreditPaymentInfoMultiPayment);

                                            cashCreditPaymentInfoMultiPayment.setPaymentId(cashCreditPaymentsDinamis.getOID());
                                            cashCreditPaymentInfoMultiPayment.setAmount(amountTotal);
                                            cashCreditPaymentInfoMultiPayment.setDueDate(DueDate);
                                            cashCreditPaymentInfoMultiPayment.setChequeDueDate(DueDate);
                                            cashCreditPaymentInfoMultiPayment.setTanggalPencairan(TanggalPencairan);
                                            long oidPaymentInfo = pstCashCreditPaymentInfo.insertExc(cashCreditPaymentInfoMultiPayment);
                                            
                                            if(oidPaymentInfo!=0){
                                                //update invoice menjadi Transaksi kredit  sudah lunas
                                                pstBillMain.updateTransStatus(billMainx.getOID(), 0);
                                            }
                                        }
                                    
                                }else{ //sisa pembayaran jika ada
                                    paid=sisaPaid;
                                    CashCreditPaymentsDinamis cashCreditPaymentsDinamis = new CashCreditPaymentsDinamis();
                                    frmCashCreditPayment.requestEntityObject(cashCreditPaymentsDinamis);
                                    cashCreditPaymentsDinamis.setAmount(sisaPaid);
                                    cashCreditPaymentsDinamis.setCreditMainId(oid);

                                     //insert cash_credit_payment
                                    long oidPayment = pstCashCreditPaymentDinamis.insertExc(cashCreditPaymentsDinamis);

                                    //insert payment info
                                    CashCreditPaymentInfo cashCreditPaymentInfoMultiPayment = new CashCreditPaymentInfo();
                                    frmCashCreditPaymentInfo.requestEntityObject(cashCreditPaymentInfoMultiPayment);

                                    cashCreditPaymentInfoMultiPayment.setPaymentId(cashCreditPaymentsDinamis.getOID());
                                    cashCreditPaymentInfoMultiPayment.setAmount(sisaPaid);
                                    cashCreditPaymentInfoMultiPayment.setDueDate(DueDate);
                                    cashCreditPaymentInfoMultiPayment.setChequeDueDate(DueDate);
                                    cashCreditPaymentInfoMultiPayment.setTanggalPencairan(TanggalPencairan);
                                    long oidPaymentInfo = pstCashCreditPaymentInfo.insertExc(cashCreditPaymentInfoMultiPayment);
                                }
                                
                        } else {
                            paid=0;
                            msgString = getSystemMessage(1);
                        }
                    }else{
                        paid=0;
                    }
                    sisaPaid=sisaPaid-paid;
                }
            }
        }catch(Exception e){

        }
        return strError;
    }
    
}
