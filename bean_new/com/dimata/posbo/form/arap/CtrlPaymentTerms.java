/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.arap;

/* java package */
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
/* project package */
import com.dimata.posbo.entity.arap.PaymentTerms;
import com.dimata.posbo.entity.arap.PstPaymentTerms;
import com.dimata.common.entity.payment.PaymentInfo;
import com.dimata.common.entity.payment.PstPaymentInfo;
import com.dimata.common.form.payment.FrmPaymentInfo;
/**
 *
 * @author mirahu
 */
public class CtrlPaymentTerms extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Code Material Unit sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Material Unit Code already exist", "Data incomplete"}
    };

    private int start;
    private String msgString;
    private PaymentTerms paymentTerms;
    private PstPaymentTerms pstPaymentTerms;
    private FrmPaymentTerms frmPaymentTerms;
    int language = LANGUAGE_FOREIGN;

    //private long oidAccPayable;
    //private long oidAccPayableDetail;

    private PaymentInfo paymentInfo;
    private PstPaymentInfo pstPaymentInfo;
    private FrmPaymentInfo frmPaymentInfo;
    long oid = 0;

    /** Creates a new instance of CtrlPaymentTerms */
    public CtrlPaymentTerms(HttpServletRequest request) {
        msgString = "";
        paymentTerms = new PaymentTerms();
        paymentInfo = new PaymentInfo();
        try{
            pstPaymentTerms = new PstPaymentTerms(0);
            pstPaymentInfo = new PstPaymentInfo(0);
        } catch(Exception e) {
            ;
        }
        frmPaymentTerms = new FrmPaymentTerms(request, paymentTerms);
        frmPaymentInfo = new FrmPaymentInfo(request, paymentInfo);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPaymentTerms.addError(frmPaymentTerms.FRM_PAYMENT_TERMS_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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
        return this.language;
    }

    public PaymentTerms getPaymentTerms() {
        return paymentTerms;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public FrmPaymentTerms getForm() {
        return this.frmPaymentTerms;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidPaymentTerms, long oidReceiveMaterial, HttpServletRequest request, int size) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidPaymentTerms != 0) {
                    try {
                        paymentTerms = PstPaymentTerms.fetchExc(oidPaymentTerms);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                }

                frmPaymentTerms.requestEntityObject(paymentTerms);


                if (oidPaymentTerms == 0) {
                    try {

                        this.oid = pstPaymentTerms.insertExc(this.paymentTerms);

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {

                         this.oid = pstPaymentTerms.updateExc(this.paymentTerms);

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidPaymentTerms != 0) {
                    try {
                        paymentTerms = PstPaymentTerms.fetchExc(oidPaymentTerms);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPaymentTerms != 0) {
                    try {
                        paymentTerms = PstPaymentTerms.fetchExc(oidPaymentTerms);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE :
                if(oidPaymentTerms != 0) {

                    try{
                        long oid = PstPaymentTerms.deleteExc(oidPaymentTerms);
			if(oid!=0){
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
			}else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                               }
                        }catch(DBException dbexc){
				excCode = dbexc.getErrorCode();
				msgString = getSystemMessage(excCode);
			}catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
			}
		}
                break;
        }
        return rsCode;
    }


}
