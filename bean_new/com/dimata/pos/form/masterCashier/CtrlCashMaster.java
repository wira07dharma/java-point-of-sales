/*
 * CtrlCashMaster.java
 *
 * Created on January 8, 2004, 9:53 AM
 */

package com.dimata.pos.form.masterCashier;

/**
 *
 * @author  gedhy
 */
// java package

import javax.servlet.http.*;

// dimata package
import com.dimata.util.*;
import com.dimata.util.lang.*;

// qdep package
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
//import com.dimata.qdep.db.*;

// project package
import com.dimata.pos.entity.masterCashier.*;
import com.dimata.posbo.db.DBException;

import com.dimata.pos.session.masterCashier.SessMasterCashier;

public class CtrlCashMaster extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Code Material Sales sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Material Sales Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private CashMaster cashMaster;
    private PstCashMaster pstCashMaster;
    private FrmCashMaster frmCashMaster;
    int language = LANGUAGE_FOREIGN;

    public CtrlCashMaster(HttpServletRequest request) {
        msgString = "";
        cashMaster = new CashMaster();
        try {
            pstCashMaster = new PstCashMaster(0);
        } catch (Exception e) {
            ;
        }
        frmCashMaster = new FrmCashMaster(request, cashMaster);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCashMaster.addError(frmCashMaster.FRM_FIELD_CASH_MASTER_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public CashMaster getCashMaster() {
        return cashMaster;
    }

    public FrmCashMaster getForm() {
        return frmCashMaster;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidCashMaster) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidCashMaster != 0) {
                    try {
                        cashMaster = PstCashMaster.fetchExc(oidCashMaster);
                    } catch (Exception exc) {
                    }
                }

                frmCashMaster.requestEntityObject(cashMaster);

                if (frmCashMaster.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (cashMaster.getOID() == 0) {
                    try {
                        long oid = pstCashMaster.insertExc(this.cashMaster);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstCashMaster.updateExc(this.cashMaster);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidCashMaster != 0) {
                    try {
                        cashMaster = PstCashMaster.fetchExc(oidCashMaster);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidCashMaster != 0) {
                    try {
                        cashMaster = PstCashMaster.fetchExc(oidCashMaster);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidCashMaster != 0) {
                    try {
                        long oid = 0;
                        if(SessMasterCashier.readyDataToDelete(oidCashMaster)){
                            oid = PstCashMaster.deleteExc(oidCashMaster);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            cashMaster = PstCashMaster.fetchExc(oidCashMaster);
                            frmCashMaster.addError(FrmCashMaster.FRM_FIELD_CASH_MASTER_ID, "");
                            msgString = "Hapus data gagal, data sudah terpakai di modul lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default :

        }
        return excCode;
    }

}
