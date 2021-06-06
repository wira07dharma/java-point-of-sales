/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.posbo.entity.masterdata.*;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class CtrlPerhitunganPoin extends Control implements I_Language {

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
    private PerhitunganPoin entPerhitunganPoin;
    private PstPerhitunganPoin pstPerhitunganPoin;
    private FrmPerhitunganPoin frmPerhitunganPoin;
    int language = LANGUAGE_DEFAULT;

    private PerhitunganPoin prevPerhitunganPoin = null;

    public CtrlPerhitunganPoin(HttpServletRequest request) {
        msgString = "";
        entPerhitunganPoin = new PerhitunganPoin();
        try {
            pstPerhitunganPoin = new PstPerhitunganPoin(0);
        } catch (Exception e) {;
        }
        frmPerhitunganPoin = new FrmPerhitunganPoin(request, entPerhitunganPoin);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPerhitunganPoin.addError(frmPerhitunganPoin.FRM_FIELD_PERHITUNGAN_POIN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public PerhitunganPoin getPerhitunganPoin() {
        return entPerhitunganPoin;
    }

    public FrmPerhitunganPoin getForm() {
        return frmPerhitunganPoin;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidPerhitunganPoin, long userId, String userName) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidPerhitunganPoin != 0) {
                    try {
                        entPerhitunganPoin = PstPerhitunganPoin.fetchExc(oidPerhitunganPoin);
                        prevPerhitunganPoin = PstPerhitunganPoin.fetchExc(oidPerhitunganPoin);
                    } catch (Exception exc) {
                    }
                }

                frmPerhitunganPoin.requestEntityObject(entPerhitunganPoin);
                entPerhitunganPoin.setUpdateDate(new Date());

                if (frmPerhitunganPoin.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entPerhitunganPoin.getOID() == 0) {
                    try {
                        long oid = pstPerhitunganPoin.insertExc(this.entPerhitunganPoin);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                        if (oid != 0) {
                            insertHistoryMaterial(userId, userName, cmd, oid);
                        }
                        //update status aktif data lama
                        String wherePoin = "" + PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_PERHITUNGAN_POIN_ID] + " <> " + oid
                                + " AND " + PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_MATERIAL_JENIS_TYPE] + " = " + this.entPerhitunganPoin.getMaterialJenisType()
                                + "";
                        Vector listPoin = PstPerhitunganPoin.list(0, 0, wherePoin, "");
                        for (int i = 0; i < listPoin.size(); i++) {
                            PerhitunganPoin pp = (PerhitunganPoin) listPoin.get(i);
                            pp.setStatusAktif(0);
                            PstPerhitunganPoin.updateExc(pp);
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
                        long oid = pstPerhitunganPoin.updateExc(this.entPerhitunganPoin);
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
                if (oidPerhitunganPoin != 0) {
                    try {
                        entPerhitunganPoin = PstPerhitunganPoin.fetchExc(oidPerhitunganPoin);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPerhitunganPoin != 0) {
                    try {
                        entPerhitunganPoin = PstPerhitunganPoin.fetchExc(oidPerhitunganPoin);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidPerhitunganPoin != 0) {                    
                    try {
                        entPerhitunganPoin = PstPerhitunganPoin.fetchExc(oidPerhitunganPoin);
                        long oid = PstPerhitunganPoin.deleteExc(oidPerhitunganPoin);
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
            logSysHistory.setLogOpenUrl("masterdata/perhitungan_poin.jsp");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType("Pos material nilai tukar emas");
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber("" + entPerhitunganPoin.getOID());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(this.entPerhitunganPoin.getLogDetail(prevPerhitunganPoin));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oidHistory = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
}
