/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.pos.form.payment;

/**
 *
 * @author Wiweka
 */
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
import com.dimata.posbo.db.DBException;


public class CtrlCashCreditPayment extends Control implements I_Language    {

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
    private CashCreditPaymentsDinamis cashCreditPaymentsDinamis;
    private PstCashCreditPaymentDinamis pstCashCreditPaymentDinamis;
    private FrmCashCreditPayment frmCashCreditPayment;
    int language = LANGUAGE_FOREIGN;

    public CtrlCashCreditPayment(HttpServletRequest request) {
        msgString = "";
        cashCreditPaymentsDinamis = new CashCreditPaymentsDinamis();
        try {
            pstCashCreditPaymentDinamis = new PstCashCreditPaymentDinamis(0);
        } catch (Exception e) {
            ;
        }
        frmCashCreditPayment = new FrmCashCreditPayment(request, cashCreditPaymentsDinamis);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCashCreditPayment.addError(frmCashCreditPayment.FRM_FIELD_CASH_CREDIT_PAYMENT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public CashCreditPaymentsDinamis getCashCreditPayment() {
        return cashCreditPaymentsDinamis;
    }

    public FrmCashCreditPayment getForm() {
        return frmCashCreditPayment;
    }

    public String getMessage()  {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long getCashCreditPayment) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (getCashCreditPayment != 0) {
                    try {
                        cashCreditPaymentsDinamis = PstCashCreditPaymentDinamis.fetchExc(getCashCreditPayment);
                    } catch (Exception exc) {
                    }
                }

                frmCashCreditPayment.requestEntityObject(cashCreditPaymentsDinamis);

                if (frmCashCreditPayment.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (cashCreditPaymentsDinamis.getOID() == 0) {
                    try {
                        long oid = pstCashCreditPaymentDinamis.insertExc(this.cashCreditPaymentsDinamis);


                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstCashCreditPaymentDinamis.updateExc(this.cashCreditPaymentsDinamis);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (getCashCreditPayment != 0) {
                    try {
                        cashCreditPaymentsDinamis = PstCashCreditPaymentDinamis.fetchExc(getCashCreditPayment);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (getCashCreditPayment != 0) {
                    try {
                        cashCreditPaymentsDinamis = PstCashCreditPaymentDinamis.fetchExc(getCashCreditPayment);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (getCashCreditPayment != 0) {
                    try {
                        long oid = PstCashCreditPaymentDinamis.deleteExc(getCashCreditPayment);
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
}
