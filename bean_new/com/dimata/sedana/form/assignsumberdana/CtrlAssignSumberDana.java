/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.assignsumberdana;

import com.dimata.harisma.entity.masterdata.*;
import com.dimata.qdep.db.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.sedana.entity.assignsumberdana.AssignSumberDana;
import com.dimata.sedana.entity.assignsumberdana.PstAssignSumberDana;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import javax.servlet.http.*;

/*
 Description : Controll AssignSumberDana
 Date : Mon Jun 19 2017
 Author : Ari Ardiadi
 */
public class CtrlAssignSumberDana extends Control implements I_Language {

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
    private AssignSumberDana entAssignSumberDana;
    private PstAssignSumberDana pstAssignSumberDana;
    private FrmAssignSumberDana frmAssignSumberDana;
    int language = LANGUAGE_DEFAULT;

    public CtrlAssignSumberDana(HttpServletRequest request) {
        msgString = "";
        entAssignSumberDana = new AssignSumberDana();
        try {
            pstAssignSumberDana = new PstAssignSumberDana(0);
        } catch (Exception e) {;
        }
        frmAssignSumberDana = new FrmAssignSumberDana(request, entAssignSumberDana);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmAssignSumberDana.addError(frmAssignSumberDana.FRM_FIELD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public AssignSumberDana getAssignSumberDana() {
        return entAssignSumberDana;
    }

    public FrmAssignSumberDana getForm() {
        return frmAssignSumberDana;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidAssignSumberDana) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidAssignSumberDana != 0) {
                    try {
                        entAssignSumberDana = PstAssignSumberDana.fetchExc(oidAssignSumberDana);
                    } catch (Exception exc) {
                    }
                }

                frmAssignSumberDana.requestEntityObject(entAssignSumberDana);

                if (frmAssignSumberDana.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entAssignSumberDana.getOID() == 0) {
                    try {
                        long oid = pstAssignSumberDana.insertExc(this.entAssignSumberDana);
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
                        long oid = pstAssignSumberDana.updateExc(this.entAssignSumberDana);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidAssignSumberDana != 0) {
                    try {
                        entAssignSumberDana = PstAssignSumberDana.fetchExc(oidAssignSumberDana);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidAssignSumberDana != 0) {
                    try {
                        entAssignSumberDana = PstAssignSumberDana.fetchExc(oidAssignSumberDana);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidAssignSumberDana != 0) {
                    try {
                        long oid = PstAssignSumberDana.deleteExc(oidAssignSumberDana);
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
