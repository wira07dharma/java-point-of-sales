/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.rsvcustpackrecurring;

import com.dimata.hanoman.entity.outletrsv.rsvcustpackrecurring.PstResvCustomPackRecurring;
import com.dimata.hanoman.entity.outletrsv.rsvcustpackrecurring.ResvCustomPackRecurring;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;

/*
 Description : Controll ResvCustomPackRecurring
 Date : Thu Feb 16 2017
 Author : Dewa
 */
public class CtrlResvCustomPackRecurring extends Control implements I_Language {

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
    private ResvCustomPackRecurring entResvCustomPackRecurring;
    private PstResvCustomPackRecurring pstResvCustomPackRecurring;
    private FrmResvCustomPackRecurring frmResvCustomPackRecurring;
    int language = LANGUAGE_DEFAULT;

    public CtrlResvCustomPackRecurring(HttpServletRequest request) {
        msgString = "";
        entResvCustomPackRecurring = new ResvCustomPackRecurring();
        try {
            pstResvCustomPackRecurring = new PstResvCustomPackRecurring(0);
        } catch (Exception e) {;
        }
        frmResvCustomPackRecurring = new FrmResvCustomPackRecurring(request, entResvCustomPackRecurring);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmResvCustomPackRecurring.addError(frmResvCustomPackRecurring.FRM_FIELD_RSV_CUSTOME_PACK_RECUR_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ResvCustomPackRecurring getResvCustomPackRecurring() {
        return entResvCustomPackRecurring;
    }

    public FrmResvCustomPackRecurring getForm() {
        return frmResvCustomPackRecurring;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidResvCustomPackRecurring, long oidPackBilling) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidResvCustomPackRecurring != 0) {
                    try {
                        entResvCustomPackRecurring = PstResvCustomPackRecurring.fetchExc(oidResvCustomPackRecurring);
                    } catch (Exception exc) {
                    }
                }

                frmResvCustomPackRecurring.requestEntityObject(entResvCustomPackRecurring, oidPackBilling);

                if (frmResvCustomPackRecurring.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entResvCustomPackRecurring.getOID() == 0) {
                    try {
                        long oid = pstResvCustomPackRecurring.insertExc(this.entResvCustomPackRecurring);
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
                        long oid = pstResvCustomPackRecurring.updateExc(this.entResvCustomPackRecurring);
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
                if (oidResvCustomPackRecurring != 0) {
                    try {
                        entResvCustomPackRecurring = PstResvCustomPackRecurring.fetchExc(oidResvCustomPackRecurring);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidResvCustomPackRecurring != 0) {
                    try {
                        entResvCustomPackRecurring = PstResvCustomPackRecurring.fetchExc(oidResvCustomPackRecurring);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidResvCustomPackRecurring != 0) {
                    try {
                        long oid = PstResvCustomPackRecurring.deleteExc(oidResvCustomPackRecurring);
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
