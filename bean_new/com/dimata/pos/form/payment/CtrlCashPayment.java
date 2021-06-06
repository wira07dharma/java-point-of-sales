/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.pos.form.payment;

/**
 *
 * @author Wiweka
 */
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

public class   CtrlCashPayment extends Control implements I_Language    {

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
    private CashPayments1 cashPayment;
    private BillMain billMain;
    private CashCreditCard cashCreditCard = new CashCreditCard();
    private PaymentInfo paymentInfo = new PaymentInfo();
    private PstCashPayment1 pstCashPayment;
    private PstBillMain pstBillMain;
    private PstCashCreditCard pstCashCreditCard;
    private FrmCashPayment frmCashPayment;
    private FrmCashCreditCard frmCashCreditCard;
     private CashReturn cashReturn;
    private PstCashReturn pstCashReturn;
    int language = LANGUAGE_FOREIGN;
    //private PaymentInfo paymentInfo;
    private PstPaymentInfo pstPaymentInfo;
    private FrmPaymentInfo frmPaymentInfo;
    private FrmPaymentInfo frmCashCreditPaymentInfo;
    
    public CtrlCashPayment(HttpServletRequest request) {
        msgString = "";
        cashPayment = new CashPayments1();
        paymentInfo = new PaymentInfo();
        try {
            pstCashPayment = new PstCashPayment1(0);
        } catch (Exception e) {
            ;
        }
        frmCashPayment = new FrmCashPayment(request, cashPayment);
        frmCashCreditCard = new FrmCashCreditCard(request,cashCreditCard);
        frmPaymentInfo = new FrmPaymentInfo(request, paymentInfo);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCashPayment.addError(frmCashPayment.FRM_FIELD_CASH_PAYMENT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public CashPayments1 getCashPayment() {
        return cashPayment;
    }

    public FrmCashPayment getForm() {
        return frmCashPayment;
    }

    public String getMessage()  {
        return msgString;
    }

    public int getStart() {
        return start;
    }
    public PaymentInfo getPaymentInfo() {
        return this.paymentInfo;
    }

    public int action(int cmd, long getCashPayment, long oidBillMain, int transactionStatus) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        int counter = 0;
        boolean incrementAllPrType = true;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (getCashPayment != 0) {
                    try {
                        cashPayment = PstCashPayment1.fetchExc(getCashPayment);
                    } catch (Exception exc) {
                    }
                }

                frmCashPayment.requestEntityObject(cashPayment);
                cashPayment.setBillMainId(oidBillMain);
                cashPayment.setPayDateTime(new Date());

                if (frmCashPayment.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (cashPayment.getOID() == 0) {
                    try {
                        long oid = pstCashPayment.insertExc(this.cashPayment);
                        if (oid != 0) {
                            //Update Transaction Status
                            int transStatus = pstBillMain.updateTransStatus(oidBillMain, transactionStatus);

                            //Update Invoice Number                            
                            Date billdate = new Date();
                            billMain = PstBillMain.fetchExc(oidBillMain);
                            int invoiceCounter = SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType);
                            String invoiceNo = SessBilling.getCodeOrderMaterial(billMain);
                            String InvoiceNo = pstBillMain.updateInvoiceNo(oidBillMain, invoiceCounter, invoiceNo);

                            //UNTUK INSERT CASH CREDIT CARD
                            frmCashCreditCard.requestEntityObject(cashCreditCard);
                            cashCreditCard.setPaymentId(cashPayment.getOID());
                            cashCreditCard.setAmount(cashPayment.getAmount());
                            cashCreditCard.setCurrencyId(cashPayment.getCurrencyId());
                            cashCreditCard.setRate(cashPayment.getRate());
                            long oidCashCreditCard = pstCashCreditCard.insertExc(this.cashCreditCard);

                            //proses insert return payment
                            CashReturn cashReturn = new CashReturn();
                            cashReturn.setBillMainId(oidBillMain);
                            cashReturn.setCurrencyId(billMain.getCurrencyId());
                            cashReturn.setRate(billMain.getRate());
                            cashReturn.setAmount(cashPayment.getAmountReturn());

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
                        long oid = pstCashPayment.updateExc(this.cashPayment);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (getCashPayment != 0) {
                    try {
                        cashPayment = PstCashPayment1.fetchExc(getCashPayment);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (getCashPayment != 0) {
                    try {
                        cashPayment = PstCashPayment1.fetchExc(getCashPayment);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (getCashPayment != 0) {
                    try {
                        long oid = PstCashPayment1.deleteExc(getCashPayment);
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

     public int requestPayment(HttpServletRequest req, long oidCustomer, double amountTotal, long oidCashCashier, long shiftID, long locationId, long userID, long oidCashBillMain){
        int strError=0;
        try{
                    CashPayments1 xCashPayment = new CashPayments1();
                    frmCashPayment.requestEntityObject(xCashPayment);
                    xCashPayment.setBillMainId(oidCashBillMain);
                    xCashPayment.setPayDateTime(new Date());

                    long oid = pstCashPayment.insertExc(xCashPayment);
                        if (oid != 0) {
                            try{
                                //Update Transaction Status
                                int transStatus = pstBillMain.updateTransStatus(oidCashBillMain, 0);

                                //Update Invoice Number
                                Date billdate = new Date();
                                billMain = PstBillMain.fetchExc(oidCashBillMain);
                                boolean incrementAllPrType = true;
                                int invoiceCounter = SessBilling.getIntCode(billMain, billdate, oidCashBillMain, 0, incrementAllPrType);
                                String invoiceNo = SessBilling.getCodeOrderMaterial(billMain);
                                String InvoiceNo = pstBillMain.updateInvoiceNo(oidCashBillMain, invoiceCounter, invoiceNo);

                                //UNTUK INSERT CASH CREDIT CARD
                                frmCashCreditCard.requestEntityObject(cashCreditCard);
                                cashCreditCard.setPaymentId(xCashPayment.getOID());
                                cashCreditCard.setAmount(xCashPayment.getAmount());
                                cashCreditCard.setCurrencyId(xCashPayment.getCurrencyId());
                                cashCreditCard.setRate(xCashPayment.getRate());
                                long oidCashCreditCard = pstCashCreditCard.insertExc(this.cashCreditCard);

                                double balance = amountTotal-xCashPayment.getAmount();
                                
                                    //proses insert return payment
                                    CashReturn cashReturn = new CashReturn();
                                    cashReturn.setBillMainId(oidCashBillMain);
                                    cashReturn.setCurrencyId(billMain.getCurrencyId());
                                    cashReturn.setRate(billMain.getRate());
                                    cashReturn.setAmount(balance);

                                    oid = pstCashReturn.insertExc(cashReturn);

                            }catch(Exception e){

                            }
                        }
        }catch(Exception e){

        }
        return strError;
    }
}
