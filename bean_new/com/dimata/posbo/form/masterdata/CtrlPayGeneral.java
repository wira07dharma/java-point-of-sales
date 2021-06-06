/*
 * CtrlPayGeneral.java
 *
 * Created on March 30, 2007, 8:27 AM
 */
package com.dimata.posbo.form.masterdata;


/* java package */
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
/* project package */
//import com.dimata.harisma.db.*;
import com.dimata.posbo.entity.masterdata.*;
//import com.dimata.harisma.entity.locker.*;
//import com.dimata.harisma.form.locker.*;
//import com.dimata.harisma.entity.attendance.*;

/**
 *
 * @author yunny
 */
public class CtrlPayGeneral extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };

    private int start;
    private String msgString;
    private PayGeneral payGeneral;
    private PstPayGeneral pstPayGeneral;
    private FrmPayGeneral frmPayGeneral;

    int language = LANGUAGE_DEFAULT;

    /**
     * Creates a new instance of CtrlPayGeneral
     */
    public CtrlPayGeneral(HttpServletRequest request) {
        msgString = "";
        payGeneral = new PayGeneral();
        //locker = new Locker();
        try {
            pstPayGeneral = new PstPayGeneral(0);
            //pstLocker = new PstLocker(0);
        } catch (Exception e) {;
        }

        frmPayGeneral = new FrmPayGeneral(request, payGeneral);
        //frmLocker = new FrmLocker(request, locker);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPayGeneral.addError(frmPayGeneral.FRM_FIELD_GEN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public PayGeneral getPayGeneral() {
        return payGeneral;
    }

    public FrmPayGeneral getForm() {
        return frmPayGeneral;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidPayGeneral) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidPayGeneral != 0) {
                    try {
                        payGeneral = PstPayGeneral.fetchExc(oidPayGeneral);
                    } catch (Exception exc) {
                    }
                }

                frmPayGeneral.requestEntityObject(payGeneral);

                if (frmPayGeneral.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (payGeneral.getOID() == 0) {
                    try {
                        long oid = pstPayGeneral.insertExc(this.payGeneral);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        long oid = pstPayGeneral.updateExc(this.payGeneral);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidPayGeneral != 0) {
                    try {
                        payGeneral = PstPayGeneral.fetchExc(oidPayGeneral);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPayGeneral != 0) {
                    try {
                        payGeneral = PstPayGeneral.fetchExc(oidPayGeneral);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                System.out.println("oidPayGeneral " + oidPayGeneral);
                if (oidPayGeneral != 0) {
                    try {
                        long oid = PstPayGeneral.deleteExc(oidPayGeneral);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default:

        }
        return excCode;
    }

}
