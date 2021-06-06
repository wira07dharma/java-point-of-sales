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
import java.util.*;
import javax.servlet.http.*;

/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;

/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.workflow.entity.approval.*;

public class CtrlAppMapping extends Control implements I_Language {

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
    private AppMapping appMapping;
    private PstAppMapping pstAppMapping;
    private FrmAppMapping frmAppMapping;
    int language = LANGUAGE_DEFAULT;

    public CtrlAppMapping(HttpServletRequest request) {
        msgString = "";
        appMapping = new AppMapping();
        try {
            pstAppMapping = new PstAppMapping(0);
        } catch (Exception e) {;
        }
        frmAppMapping = new FrmAppMapping(request, appMapping);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmAppMapping.addError(frmAppMapping.FRM_FIELD_APP_MAPPING_OID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public AppMapping getAppMapping() {
        return appMapping;
    }

    public FrmAppMapping getForm() {
        return frmAppMapping;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidAppMapping) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidAppMapping != 0) {
                    try {
                        appMapping = PstAppMapping.fetchExc(oidAppMapping);
                    } catch (Exception exc) {
                    }
                }

                frmAppMapping.requestEntityObject(appMapping);

                if (frmAppMapping.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (appMapping.getOID() == 0) {
                    try {
                        long oid = pstAppMapping.insertExc(this.appMapping);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstAppMapping.updateExc(this.appMapping);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }

                // process Process Status
                Vector vectProcStatus = this.frmAppMapping.getProcessStatus();
                if (PstAppMapping.setActionStatus(appMapping.getOID(), vectProcStatus)) {
                    msgString = FRMMessage.getMsg(FRMMessage.ERR_SAVED);
                } else {
                    msgString = FRMMessage.getErr(FRMMessage.ERR_UNKNOWN);
                }
                break;

            case Command.EDIT:
                if (oidAppMapping != 0) {
                    try {
                        appMapping = PstAppMapping.fetchExc(oidAppMapping);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidAppMapping != 0) {
                    try {
                        appMapping = PstAppMapping.fetchExc(oidAppMapping);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidAppMapping != 0) {
                    try {
                        long oid = PstAppMapping.deleteExc(oidAppMapping);
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
