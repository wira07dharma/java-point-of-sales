/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.ChainPeriod;
import com.dimata.posbo.entity.masterdata.PstChainPeriod;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;

/*
 Description : Controll ChainPeriod
 Date : Tue Jul 09 2019
 Author :
 */
public class CtrlChainPeriod extends Control implements I_Language {

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
    private ChainPeriod entChainPeriod;
    private PstChainPeriod pstChainPeriod;
    private FrmChainPeriod frmChainPeriod;
    int language = LANGUAGE_DEFAULT;

    public CtrlChainPeriod(HttpServletRequest request) {
        msgString = "";
        entChainPeriod = new ChainPeriod();
        try {
            pstChainPeriod = new PstChainPeriod(0);
        } catch (Exception e) {;
        }
        frmChainPeriod = new FrmChainPeriod(request, entChainPeriod);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmChainPeriod.addError(frmChainPeriod.FRM_FIELD_CHAIN_PERIOD_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ChainPeriod getChainPeriod() {
        return entChainPeriod;
    }

    public FrmChainPeriod getForm() {
        return frmChainPeriod;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidChainPeriod) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidChainPeriod != 0) {
                    try {
                        entChainPeriod = PstChainPeriod.fetchExc(oidChainPeriod);
                    } catch (Exception exc) {
                    }
                }

                frmChainPeriod.requestEntityObject(entChainPeriod);

                if (frmChainPeriod.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                //CHECK INDEX EXIST
                if (entChainPeriod.getOID() == 0 && PstChainPeriod.getCount(PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID] + " = " + entChainPeriod.getChainMainId() + " AND " + PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX] + " = " + entChainPeriod.getIndex()) > 0) {
                    msgString = "Index " + entChainPeriod.getIndex() + " sudah ada !";
                    return RSLT_EST_CODE_EXIST;
                }

                if (entChainPeriod.getOID() == 0) {
                    try {
                        long oid = pstChainPeriod.insertExc(this.entChainPeriod);
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
                        long oid = pstChainPeriod.updateExc(this.entChainPeriod);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidChainPeriod != 0) {
                    try {
                        entChainPeriod = PstChainPeriod.fetchExc(oidChainPeriod);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidChainPeriod != 0) {
                    try {
                        entChainPeriod = PstChainPeriod.fetchExc(oidChainPeriod);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidChainPeriod != 0) {
                    try {
                        long oid = PstChainPeriod.deleteExc(oidChainPeriod);
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
