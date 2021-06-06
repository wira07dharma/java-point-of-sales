/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.form.email;

import com.dimata.common.entity.email.PstSettingTemplateInfo;
import com.dimata.common.entity.email.SettingTemplateInfo;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;

/**
 *
 * @author Dimata 007
 */
public class CtrlSettingTemplateInfo extends Control implements I_Language {

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
    private SettingTemplateInfo entSettingTemplateInfo;
    private PstSettingTemplateInfo pstSettingTemplateInfo;
    private FrmSettingTemplateInfo frmSettingTemplateInfo;
    int language = LANGUAGE_DEFAULT;

    public CtrlSettingTemplateInfo(HttpServletRequest request) {
        msgString = "";
        entSettingTemplateInfo = new SettingTemplateInfo();
        try {
            pstSettingTemplateInfo = new PstSettingTemplateInfo(0);
        } catch (Exception e) {;
        }
        frmSettingTemplateInfo = new FrmSettingTemplateInfo(request, entSettingTemplateInfo);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmSettingTemplateInfo.addError(frmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public SettingTemplateInfo getSettingTemplateInfo() {
        return entSettingTemplateInfo;
    }

    public FrmSettingTemplateInfo getForm() {
        return frmSettingTemplateInfo;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidSettingTemplateInfo) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidSettingTemplateInfo != 0) {
                    try {
                        entSettingTemplateInfo = PstSettingTemplateInfo.fetchExc(oidSettingTemplateInfo);
                    } catch (Exception exc) {
                    }
                }

                frmSettingTemplateInfo.requestEntityObject(entSettingTemplateInfo);

                if (frmSettingTemplateInfo.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entSettingTemplateInfo.getOID() == 0) {
                    try {
                        long oid = pstSettingTemplateInfo.insertExc(this.entSettingTemplateInfo);
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
                        long oid = pstSettingTemplateInfo.updateExc(this.entSettingTemplateInfo);
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
                if (oidSettingTemplateInfo != 0) {
                    try {
                        entSettingTemplateInfo = PstSettingTemplateInfo.fetchExc(oidSettingTemplateInfo);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidSettingTemplateInfo != 0) {
                    try {
                        entSettingTemplateInfo = PstSettingTemplateInfo.fetchExc(oidSettingTemplateInfo);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidSettingTemplateInfo != 0) {
                    try {
                        long oid = PstSettingTemplateInfo.deleteExc(oidSettingTemplateInfo);
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
        return rsCode;
    }
}
