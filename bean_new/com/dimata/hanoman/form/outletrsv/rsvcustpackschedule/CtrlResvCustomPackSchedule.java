/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.rsvcustpackschedule;

import com.dimata.hanoman.entity.outletrsv.rsvcustpackschedule.PstResvCustomPackSchedule;
import com.dimata.hanoman.entity.outletrsv.rsvcustpackschedule.ResvCustomPackSchedule;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import java.util.Date;

/*
 Description : Controll ResvCustomPackSchedule
 Date : Thu Feb 16 2017
 Author : Dewa
 */
public class CtrlResvCustomPackSchedule extends Control implements I_Language {

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
    private ResvCustomPackSchedule entResvCustomPackSchedule;
    private PstResvCustomPackSchedule pstResvCustomPackSchedule;
    private FrmResvCustomPackSchedule frmResvCustomPackSchedule;
    int language = LANGUAGE_DEFAULT;

    public CtrlResvCustomPackSchedule(HttpServletRequest request) {
        msgString = "";
        entResvCustomPackSchedule = new ResvCustomPackSchedule();
        try {
            pstResvCustomPackSchedule = new PstResvCustomPackSchedule(0);
        } catch (Exception e) {;
        }
        frmResvCustomPackSchedule = new FrmResvCustomPackSchedule(request, entResvCustomPackSchedule);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmResvCustomPackSchedule.addError(frmResvCustomPackSchedule.FRM_FIELD_CUSTOM_SCHEDULE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ResvCustomPackSchedule getResvCustomPackSchedule() {
        return entResvCustomPackSchedule;
    }

    public FrmResvCustomPackSchedule getForm() {
        return frmResvCustomPackSchedule;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidResvCustomPackSchedule, long oidPackBilling) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidResvCustomPackSchedule != 0) {
                    try {
                        entResvCustomPackSchedule = PstResvCustomPackSchedule.fetchExc(oidResvCustomPackSchedule);
                    } catch (Exception exc) {
                    }
                }

                frmResvCustomPackSchedule.requestEntityObject(entResvCustomPackSchedule, oidPackBilling);

                if (frmResvCustomPackSchedule.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entResvCustomPackSchedule.getOID() == 0) {
                    try {
                        long oid = pstResvCustomPackSchedule.insertExc(this.entResvCustomPackSchedule);
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
                        long oid = pstResvCustomPackSchedule.updateExc(this.entResvCustomPackSchedule);
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
                if (oidResvCustomPackSchedule != 0) {
                    try {
                        entResvCustomPackSchedule = PstResvCustomPackSchedule.fetchExc(oidResvCustomPackSchedule);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidResvCustomPackSchedule != 0) {
                    try {
                        entResvCustomPackSchedule = PstResvCustomPackSchedule.fetchExc(oidResvCustomPackSchedule);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidResvCustomPackSchedule != 0) {
                    try {
                        long oid = PstResvCustomPackSchedule.deleteExc(oidResvCustomPackSchedule);
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

    public int actionOnRecurring(int cmd, long oidResvCustomPackSchedule, long oidPackBilling, Date startDate, Date endDate) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidResvCustomPackSchedule != 0) {
                    try {
                        entResvCustomPackSchedule = PstResvCustomPackSchedule.fetchExc(oidResvCustomPackSchedule);
                    } catch (Exception exc) {
                        
                    }
                }

                frmResvCustomPackSchedule.requestEntityObjectRecurring(entResvCustomPackSchedule, oidPackBilling, startDate, endDate);

                if (frmResvCustomPackSchedule.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entResvCustomPackSchedule.getOID() == 0) {
                    try {
                        long oid = pstResvCustomPackSchedule.insertExc(this.entResvCustomPackSchedule);
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
                        long oid = pstResvCustomPackSchedule.updateExc(this.entResvCustomPackSchedule);
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
                if (oidResvCustomPackSchedule != 0) {
                    try {
                        entResvCustomPackSchedule = PstResvCustomPackSchedule.fetchExc(oidResvCustomPackSchedule);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidResvCustomPackSchedule != 0) {
                    try {
                        entResvCustomPackSchedule = PstResvCustomPackSchedule.fetchExc(oidResvCustomPackSchedule);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidResvCustomPackSchedule != 0) {
                    try {
                        long oid = PstResvCustomPackSchedule.deleteExc(oidResvCustomPackSchedule);
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
