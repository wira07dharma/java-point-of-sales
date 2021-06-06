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
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.masterdata.MaterialNilaiTukarEmas;
import com.dimata.posbo.entity.masterdata.PstMaterialNilaiTukarEmas;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import java.util.Date;

/*
Description : Controll MaterialNilaiTukarEmas
Date : Tue Oct 24 2017
Author : opie-eyek 20171024
 */
public class CtrlMaterialNilaiTukarEmas extends Control implements I_Language {

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
    private MaterialNilaiTukarEmas entMaterialNilaiTukarEmas;
    private PstMaterialNilaiTukarEmas pstMaterialNilaiTukarEmas;
    private FrmMaterialNilaiTukarEmas frmMaterialNilaiTukarEmas;
    int language = LANGUAGE_DEFAULT;
    
    
    MaterialNilaiTukarEmas prevMaterialNilaiTukarEmas = null;
    private HttpServletRequest req;
    Date dateLog = new  Date();

    public CtrlMaterialNilaiTukarEmas(HttpServletRequest request) {
        msgString = "";
        entMaterialNilaiTukarEmas = new MaterialNilaiTukarEmas();
        try {
            pstMaterialNilaiTukarEmas = new PstMaterialNilaiTukarEmas(0);
        } catch (Exception e) {;
        }
        frmMaterialNilaiTukarEmas = new FrmMaterialNilaiTukarEmas(request, entMaterialNilaiTukarEmas);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMaterialNilaiTukarEmas.addError(frmMaterialNilaiTukarEmas.FRM_FIELD_NILAI_TUKAR_EMAS_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MaterialNilaiTukarEmas getMaterialNilaiTukarEmas() {
        return entMaterialNilaiTukarEmas;
    }

    public FrmMaterialNilaiTukarEmas getForm() {
        return frmMaterialNilaiTukarEmas;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }
    
    public int action(int cmd, long oidMaterialNilaiTukarEmas) throws Exception{
        AppUser appUser = new AppUser();
             return action(cmd, oidMaterialNilaiTukarEmas,0,"");
        }
    

    public int action(int cmd, long oidMaterialNilaiTukarEmas, long userId, String nameUser) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMaterialNilaiTukarEmas != 0) {
                    try {
                        entMaterialNilaiTukarEmas = PstMaterialNilaiTukarEmas.fetchExc(oidMaterialNilaiTukarEmas);
                        prevMaterialNilaiTukarEmas = PstMaterialNilaiTukarEmas.fetchExc(oidMaterialNilaiTukarEmas);
                    } catch (Exception exc) {
                    }
                }

                frmMaterialNilaiTukarEmas.requestEntityObject(entMaterialNilaiTukarEmas);
                entMaterialNilaiTukarEmas.setLastUpdate(new Date());

                if (frmMaterialNilaiTukarEmas.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMaterialNilaiTukarEmas.getOID() == 0) {
                    try {
                        long oid = pstMaterialNilaiTukarEmas.insertExc(this.entMaterialNilaiTukarEmas);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                        if (oid != 0){
                            insertHistoryMaterial(userId, nameUser, cmd, oid);
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
                        int cmdHistory = Command.UPDATE;
                        long oid = pstMaterialNilaiTukarEmas.updateExc(this.entMaterialNilaiTukarEmas);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                        if (oid != 0){
                            insertHistoryMaterial(userId, nameUser, cmdHistory, oid);
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
                if (oidMaterialNilaiTukarEmas != 0) {
                    try {
                        entMaterialNilaiTukarEmas = PstMaterialNilaiTukarEmas.fetchExc(oidMaterialNilaiTukarEmas);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMaterialNilaiTukarEmas != 0) {
                    try {
                        entMaterialNilaiTukarEmas = PstMaterialNilaiTukarEmas.fetchExc(oidMaterialNilaiTukarEmas);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMaterialNilaiTukarEmas != 0) {
                    try {
                        long oid = PstMaterialNilaiTukarEmas.deleteExc(oidMaterialNilaiTukarEmas);
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
    
    public  void insertHistoryMaterial(long userID, String nameUser, int cmd, long oid)
    {
        try
        {
           LogSysHistory logSysHistory = new LogSysHistory();
           logSysHistory.setLogUserId(userID);
           logSysHistory.setLogLoginName(nameUser);
           logSysHistory.setLogApplication("Prochain");
           logSysHistory.setLogOpenUrl("master/material/nilai_tukar_emas.jsp");
           logSysHistory.setLogUpdateDate(dateLog);
           logSysHistory.setLogDocumentType("Pos material nilai tukar emas");
           logSysHistory.setLogUserAction(Command.commandString[cmd]);
           logSysHistory.setLogDocumentNumber(""+entMaterialNilaiTukarEmas.getOID());
           logSysHistory.setLogDocumentId(oid);
           logSysHistory.setLogDetail(this.entMaterialNilaiTukarEmas.getLogDetail(prevMaterialNilaiTukarEmas));

           if(!logSysHistory.getLogDetail().equals("") || cmd==Command.DELETE)
           {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
           }
      }
      catch(Exception e)
      {

      }
    }
}
