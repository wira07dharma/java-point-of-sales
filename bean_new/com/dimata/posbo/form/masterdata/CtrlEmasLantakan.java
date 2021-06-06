/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author dimata005
 */
import com.dimata.posbo.entity.masterdata.EmasLantakan;
import com.dimata.posbo.entity.masterdata.PstEmasLantakan;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import java.util.Date;

/*
Description : Controll EmasLantakan
Date : Tue Oct 24 2017
Author : opie-eyek 20171024
 */
public class CtrlEmasLantakan extends Control implements I_Language {

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
    private EmasLantakan entEmasLantakan;
    private EmasLantakan prevEntEmasLantakan;
    private PstEmasLantakan pstEmasLantakan;
    private FrmEmasLantakan frmEmasLantakan;
    int language = LANGUAGE_DEFAULT;

    public CtrlEmasLantakan(HttpServletRequest request) {
        msgString = "";
        entEmasLantakan = new EmasLantakan();
        try {
            pstEmasLantakan = new PstEmasLantakan(0);
        } catch (Exception e) {;
        }
        frmEmasLantakan = new FrmEmasLantakan(request, entEmasLantakan);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmEmasLantakan.addError(frmEmasLantakan.FRM_FIELD_EMAS_LANTAKAN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public EmasLantakan getEmasLantakan() {
        return entEmasLantakan;
    }

    public FrmEmasLantakan getForm() {
        return frmEmasLantakan;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidEmasLantakan, long userID, String nameUser) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidEmasLantakan != 0) {
                    try {
                        entEmasLantakan = PstEmasLantakan.fetchExc(oidEmasLantakan);
                        prevEntEmasLantakan = PstEmasLantakan.fetchExc(oidEmasLantakan);
                    } catch (Exception exc) {
                    }
                }

                frmEmasLantakan.requestEntityObject(entEmasLantakan);

                if (frmEmasLantakan.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entEmasLantakan.getOID() == 0) {
                    try {
                        entEmasLantakan.setStatusAktif(1);
                        long oid = pstEmasLantakan.insertExc(this.entEmasLantakan);
                        if(oid !=0) {
                            insertHistoryEmasLantakan(userID, nameUser, Command.SAVE, oid);
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
                        long oid = pstEmasLantakan.updateExc(this.entEmasLantakan);
                        if(oid !=0) {
                            insertHistoryEmasLantakan(userID, nameUser, Command.UPDATE, oid);
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
                if (oidEmasLantakan != 0) {
                    try {
                        entEmasLantakan = PstEmasLantakan.fetchExc(oidEmasLantakan);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidEmasLantakan != 0) {
                    try {
                        entEmasLantakan = PstEmasLantakan.fetchExc(oidEmasLantakan);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidEmasLantakan != 0) {
                    try {
                        long oid = PstEmasLantakan.deleteExc(oidEmasLantakan);
                        try {
                            prevEntEmasLantakan = PstEmasLantakan.fetchExc(oidEmasLantakan);
                        } catch (Exception exc) {
                        }
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
    
    public void insertHistoryEmasLantakan(long userID, String nameUser, int cmd, long oid) {
        try {
           LogSysHistory logSysHistory = new LogSysHistory();
           logSysHistory.setLogUserId(userID);
           logSysHistory.setLogLoginName(nameUser);
           logSysHistory.setLogApplication("Prochain");
           logSysHistory.setLogOpenUrl("master/material/emas_lantakan.jsp");
           logSysHistory.setLogUpdateDate(new Date());
           logSysHistory.setLogDocumentType("Pos emas lantakan");
           logSysHistory.setLogUserAction(Command.commandString[cmd]);
           logSysHistory.setLogDocumentNumber("");
           logSysHistory.setLogDocumentId(oid);
           logSysHistory.setLogDetail(entEmasLantakan.getLogDetail(prevEntEmasLantakan));

           if(!logSysHistory.getLogDetail().equals("") || cmd==Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
           }
      } catch(Exception e) {

      }
    }
}
