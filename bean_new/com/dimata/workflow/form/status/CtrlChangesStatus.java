/* 
 * Ctrl Name  		:  CtrlChangesStatus.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */
/**
 * *****************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 ******************************************************************
 */
package com.dimata.workflow.form.status;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.workflow.entity.status.*;

public class CtrlChangesStatus extends Control implements I_Language {

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
    private ChangesStatus changesStatus;
    private PstChangesStatus pstChangesStatus;
    private FrmChangesStatus frmChangesStatus;
    int language = LANGUAGE_DEFAULT;

    public CtrlChangesStatus(HttpServletRequest request) {
        msgString = "";
        changesStatus = new ChangesStatus();
        try {
            pstChangesStatus = new PstChangesStatus(0);
        } catch (Exception e) {;
        }
        frmChangesStatus = new FrmChangesStatus(request, changesStatus);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmChangesStatus.addError(frmChangesStatus.FRM_FIELD_APP_MAPPING_OID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ChangesStatus getChangesStatus() {
        return changesStatus;
    }

    public FrmChangesStatus getForm() {
        return frmChangesStatus;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidAppMappingOid, long oidDocTypeStatusOid) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                boolean checkOIDs = PstChangesStatus.checkOID(oidAppMappingOid, oidDocTypeStatusOid);
                if (checkOIDs) {
                    try {
                        changesStatus = PstChangesStatus.fetchExc(oidAppMappingOid, oidDocTypeStatusOid);
                    } catch (Exception exc) {
                    }
                }

                frmChangesStatus.requestEntityObject(changesStatus);
                changesStatus.setAppMappingOid(oidAppMappingOid);
                changesStatus.setDocStatusOid(oidDocTypeStatusOid);

                if (frmChangesStatus.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (checkOIDs == false) {
                    try {
                        long oid = pstChangesStatus.insertExc(this.changesStatus);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_SAVED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstChangesStatus.updateExc(this.changesStatus);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_SAVED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                try {
                    changesStatus = PstChangesStatus.fetchExc(oidAppMappingOid, oidDocTypeStatusOid);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                }
                break;

            case Command.ASK:
                try {
                    changesStatus = PstChangesStatus.fetchExc(oidAppMappingOid, oidDocTypeStatusOid);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                }
                break;

            case Command.DELETE:
                try {
                    long oid = PstChangesStatus.deleteExc(oidAppMappingOid, oidDocTypeStatusOid);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                }
                break;

            default:

        }
        return rsCode;
    }
}
