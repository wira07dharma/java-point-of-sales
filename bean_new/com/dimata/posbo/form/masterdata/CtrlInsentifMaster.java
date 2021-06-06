/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.posbo.entity.masterdata.*;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class CtrlInsentifMaster extends Control implements I_Language {

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
    private InsentifMaster entInsentifMaster;
    private PstInsentifMaster pstInsentifMaster;
    private FrmInsentifMaster frmInsentifMaster;
    int language = LANGUAGE_DEFAULT;

    public CtrlInsentifMaster(HttpServletRequest request) {
        msgString = "";
        entInsentifMaster = new InsentifMaster();
        try {
            pstInsentifMaster = new PstInsentifMaster(0);
        } catch (Exception e) {;
        }
        frmInsentifMaster = new FrmInsentifMaster(request, entInsentifMaster);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmInsentifMaster.addError(frmInsentifMaster.FRM_FIELD_INSENTIF_MASTER_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public InsentifMaster getInsentifMaster() {
        return entInsentifMaster;
    }

    public FrmInsentifMaster getForm() {
        return frmInsentifMaster;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidInsentifMaster, long userId, String userName) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidInsentifMaster != 0) {
                    try {
                        entInsentifMaster = PstInsentifMaster.fetchExc(oidInsentifMaster);
                    } catch (Exception exc) {
                    }
                }                                

                frmInsentifMaster.requestEntityObject(entInsentifMaster);
                
                if (frmInsentifMaster.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }                                

                //cek periode
                if (entInsentifMaster.getPeriodeForever() == 0) {
                    if (entInsentifMaster.getPeriodeStart() == null || entInsentifMaster.getPeriodeEnd() == null) {
                        msgString = "Tanggal harus diisi jika periode tidak berlaku selamanya";
                        return RSLT_FORM_INCOMPLETE;
                    }
                    //cek range tanggal sama
                    String startPeriode = Formater.formatDate(entInsentifMaster.getPeriodeStart(), "yyyy-MM-dd");
                    String endPeriode = Formater.formatDate(entInsentifMaster.getPeriodeEnd(), "yyyy-MM-dd");
                    String whereDate = PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INSENTIF_MASTER_ID] + " <> " + entInsentifMaster.getOID()
                            + " AND " + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_FOREVER] + " = " + PstInsentifMaster.PERIODE_RANGE
                            + " AND ("
                            + " DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_START] + ") = '" + startPeriode + "'"
                            + " OR DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_END] + ") = '" + startPeriode + "'"
                            + " OR DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_START] + ") = '" + endPeriode + "'"
                            + " OR DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_END] + ") = '" + endPeriode + "'"
                            + " OR (DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_START] + ") BETWEEN '" + startPeriode + "' AND '" + endPeriode + "')"
                            + " OR (DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_END] + ") BETWEEN '" + startPeriode + "' AND '" + endPeriode + "')"
                            + " OR ('" + startPeriode + "' BETWEEN DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_START] + ")"
                            + " AND DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_END] + "))"
                            + " OR ('" + endPeriode + "' BETWEEN DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_START] + ")"
                            + " AND DATE(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_END] + "))"
                            + ")"
                            + "";
                    Vector listInsentifMaster = PstInsentifMaster.list(0, 0, whereDate, "");
                    if (!listInsentifMaster.isEmpty()) {
                        msgString = "Range untuk periode ini sudah ada";
                        return 1;
                    }
                }
                
                if (entInsentifMaster.getOID() == 0) {
                    try {
                        long oid = pstInsentifMaster.insertExc(this.entInsentifMaster);
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
                    //cek apakah master insentif sudah ada yg pakai atau belum
                    Vector listInsentifData = PstInsentifData.list(0, 0, "" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INSENTIF_MASTER_ID] + " = " + oidInsentifMaster, "");
                    if (listInsentifData.size() > 0) {
                        msgString = "Data tidak dapat diubah karena sudah digunakan untuk perhitungan insentif.";
                        return 1;
                    }
                    
                    try {
                        long oid = pstInsentifMaster.updateExc(this.entInsentifMaster);
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
                if (oidInsentifMaster != 0) {
                    try {
                        entInsentifMaster = PstInsentifMaster.fetchExc(oidInsentifMaster);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidInsentifMaster != 0) {
                    try {
                        entInsentifMaster = PstInsentifMaster.fetchExc(oidInsentifMaster);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidInsentifMaster != 0) {
                    try {
                        long oid = PstInsentifMaster.deleteExc(oidInsentifMaster);
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
