/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author PC
 */
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.posbo.entity.masterdata.PstRateJualBerlian;
import com.dimata.posbo.entity.masterdata.RateJualBerlian;
import java.util.Date;

/*
 Description : Controll RateJualBerlian
 Date : Sat Apr 21 2018
 Author : eyek 20180421
 */
public class CtrlRateJualBerlian extends Control implements I_Language {

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
    private RateJualBerlian entRateJualBerlian;
    private PstRateJualBerlian pstRateJualBerlian;
    private FrmRateJualBerlian frmRateJualBerlian;
    int language = LANGUAGE_DEFAULT;

    private RateJualBerlian prevRateJualBerlian;

    public CtrlRateJualBerlian(HttpServletRequest request) {
        msgString = "";
        entRateJualBerlian = new RateJualBerlian();
        try {
            pstRateJualBerlian = new PstRateJualBerlian(0);
        } catch (Exception e) {;
        }
        frmRateJualBerlian = new FrmRateJualBerlian(request, entRateJualBerlian);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmRateJualBerlian.addError(frmRateJualBerlian.FRM_FIELD_RATE_JUAL_BERLIAN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public RateJualBerlian getRateJualBerlian() {
        return entRateJualBerlian;
    }

    public FrmRateJualBerlian getForm() {
        return frmRateJualBerlian;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidRateJualBerlian, long userId, String userName) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidRateJualBerlian != 0) {
                    try {
                        entRateJualBerlian = PstRateJualBerlian.fetchExc(oidRateJualBerlian);
                        prevRateJualBerlian = PstRateJualBerlian.fetchExc(oidRateJualBerlian);
                    } catch (Exception exc) {
                    }
                }

                frmRateJualBerlian.requestEntityObject(entRateJualBerlian);

                if (frmRateJualBerlian.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entRateJualBerlian.getOID() == 0) {
                    try {
                        entRateJualBerlian.setStatusAktif(1);
                        long oid = pstRateJualBerlian.insertExc(this.entRateJualBerlian);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                        if (oid != 0) {
                            insertHistoryMaterial(userId, userName, cmd, oid);
                        }
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
                        long oid = pstRateJualBerlian.updateExc(this.entRateJualBerlian);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                        if (oid != 0) {
                            insertHistoryMaterial(userId, userName, cmd, oid);
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidRateJualBerlian != 0) {
                    try {
                        entRateJualBerlian = PstRateJualBerlian.fetchExc(oidRateJualBerlian);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidRateJualBerlian != 0) {
                    try {
                        entRateJualBerlian = PstRateJualBerlian.fetchExc(oidRateJualBerlian);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidRateJualBerlian != 0) {
                    try {
                        entRateJualBerlian = PstRateJualBerlian.fetchExc(oidRateJualBerlian);
                        long oid = PstRateJualBerlian.deleteExc(oidRateJualBerlian);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                        if (oid != 0) {
                            insertHistoryMaterial(userId, userName, cmd, oid);
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

    public void insertHistoryMaterial(long userID, String userName, int cmd, long oid) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(userName);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("masterdata/rate_jual_berlian.jsp");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType("Pos material nilai tukar emas");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber("" + entRateJualBerlian.getOID());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(this.entRateJualBerlian.getLogDetail(prevRateJualBerlian));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oidHistory = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
}
