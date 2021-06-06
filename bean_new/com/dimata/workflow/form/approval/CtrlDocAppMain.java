/**
 * Created on 	: 3:00 PM
 *
 * @author	: gedhy
 * @version	: 01
 */
/**
 * *****************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 ******************************************************************
 */
package com.dimata.workflow.form.approval;

/* java package */
import javax.servlet.http.*;

/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;

/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.workflow.entity.approval.*;

public class CtrlDocAppMain extends Control implements I_Language {

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
    private DocAppMain docAppMain;
    private PstDocAppMain pstDocAppMain;
    private FrmDocAppMain frmDocAppMain;
    int language = LANGUAGE_DEFAULT;

    public CtrlDocAppMain(HttpServletRequest request) {
        msgString = "";
        docAppMain = new DocAppMain();
        try {
            pstDocAppMain = new PstDocAppMain(0);
        } catch (Exception e) {;
        }
        frmDocAppMain = new FrmDocAppMain(request, docAppMain);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmDocAppMain.addError(frmDocAppMain.FRM_FIELD_DOCAPP_MAIN_OID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public DocAppMain getDocAppMain() {
        return docAppMain;
    }

    public FrmDocAppMain getForm() {
        return frmDocAppMain;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidDocAppMain) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidDocAppMain != 0) {
                    try {
                        docAppMain = PstDocAppMain.fetchExc(oidDocAppMain);
                    } catch (Exception exc) {
                    }
                }

                frmDocAppMain.requestEntityObject(docAppMain);

                if (frmDocAppMain.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (docAppMain.getOID() == 0) {
                    try {
                        long oid = pstDocAppMain.insertExc(this.docAppMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstDocAppMain.updateExc(this.docAppMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidDocAppMain != 0) {
                    try {
                        docAppMain = PstDocAppMain.fetchExc(oidDocAppMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidDocAppMain != 0) {
                    try {
                        docAppMain = PstDocAppMain.fetchExc(oidDocAppMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidDocAppMain != 0) {
                    try {
                        long oid = PstDocAppMain.deleteExc(oidDocAppMain);
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
