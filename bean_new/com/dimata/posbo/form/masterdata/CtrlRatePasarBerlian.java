/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.PstRatePasarBerlian;
import com.dimata.posbo.entity.masterdata.RatePasarBerlian;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Regen
 */
public class CtrlRatePasarBerlian extends Control implements I_Language {

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
    private RatePasarBerlian entRatePasarBerlian;
    private PstRatePasarBerlian pstRatePasarBerlian;
    private FrmRatePasarBerlian frmRatePasarBerlian;
    int language = LANGUAGE_DEFAULT;
    
    private RatePasarBerlian prevRatePasarBerlian;

    public CtrlRatePasarBerlian(HttpServletRequest request) {
        msgString = "";
        entRatePasarBerlian = new RatePasarBerlian();
        try {
            pstRatePasarBerlian = new PstRatePasarBerlian(0);
        } catch (Exception e) {;
        }
        frmRatePasarBerlian = new FrmRatePasarBerlian(request, entRatePasarBerlian);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmRatePasarBerlian.addError(frmRatePasarBerlian.FRM_FIELD_RATEPASARID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public RatePasarBerlian getRatePasarBerlian() {
        return entRatePasarBerlian;
    }

    public FrmRatePasarBerlian getForm() {
        return frmRatePasarBerlian;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidRatePasarBerlian, long userId, String userName) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidRatePasarBerlian != 0) {
                    try {
                        entRatePasarBerlian = PstRatePasarBerlian.fetchExc(oidRatePasarBerlian);
                        prevRatePasarBerlian = PstRatePasarBerlian.fetchExc(oidRatePasarBerlian);
                    } catch (Exception exc) {
                    }
                }

                frmRatePasarBerlian.requestEntityObject(entRatePasarBerlian);

                if (frmRatePasarBerlian.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entRatePasarBerlian.getOID() == 0) {
                    try {
                        long oid = pstRatePasarBerlian.insertExc(this.entRatePasarBerlian);
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
                        long oid = pstRatePasarBerlian.updateExc(this.entRatePasarBerlian);
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
                if (oidRatePasarBerlian != 0) {
                    try {
                        entRatePasarBerlian = PstRatePasarBerlian.fetchExc(oidRatePasarBerlian);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidRatePasarBerlian != 0) {
                    try {
                        entRatePasarBerlian = PstRatePasarBerlian.fetchExc(oidRatePasarBerlian);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidRatePasarBerlian != 0) {
                    try {
                        entRatePasarBerlian = PstRatePasarBerlian.fetchExc(oidRatePasarBerlian);
                        long oid = PstRatePasarBerlian.deleteExc(oidRatePasarBerlian);
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
            logSysHistory.setLogOpenUrl("masterdata/rate_pasar_berlian.jsp");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType("Pos material nilai tukar emas");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber("" + entRatePasarBerlian.getOID());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(this.entRatePasarBerlian.getLogDetail(prevRatePasarBerlian));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oidHistory = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
}
