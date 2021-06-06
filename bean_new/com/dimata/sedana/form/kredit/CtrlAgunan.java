/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.kredit;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.sedana.entity.kredit.Agunan;
import com.dimata.sedana.entity.kredit.AgunanMapping;
import com.dimata.sedana.entity.kredit.PstAgunan;
import com.dimata.sedana.entity.kredit.PstAgunanMapping;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
/*
 Description : Controll Agunan
 Date : Wed Jul 12 2017
 Author : dewa
 */
public class CtrlAgunan extends Control implements I_Language {

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
    private Agunan entAgunan;
    private PstAgunan pstAgunan;
    private FrmAgunan frmAgunan;
    int language = LANGUAGE_DEFAULT;

    public CtrlAgunan(HttpServletRequest request) {
        msgString = "";
        entAgunan = new Agunan();
        try {
            pstAgunan = new PstAgunan(0);
        } catch (Exception e) {;
        }
        frmAgunan = new FrmAgunan(request, entAgunan);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmAgunan.addError(frmAgunan.FRM_FIELD_AGUNAN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Agunan getAgunan() {
        return entAgunan;
    }

    public FrmAgunan getForm() {
        return frmAgunan;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidAgunan, long oidAnggota, long oidPinjaman, double prosentasePinjaman) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidAgunan != 0) {
                    try {
                        entAgunan = PstAgunan.fetchExc(oidAgunan);
                    } catch (Exception exc) {
                    }
                } else {
                    String nomorAgunan = "";
                    String kodeAgunan = "A";
                    String today = Formater.formatDate(new Date(), "yyyyMMdd");
                    int last = PstAgunan.getCount("");
                    last += 1;
                    String newLast = "" + last;
                    if (newLast.length() == 1) {
                        newLast = "000" + last;
                    } else if (newLast.length() == 2) {
                        newLast = "00" + last;
                    } else if (newLast.length() == 3) {
                        newLast = "0" + last;
                    } else if (newLast.length() == 4) {
                        newLast = "" + last;
                    }
                    nomorAgunan = kodeAgunan + today + "-" + newLast;
                    int check = 1;
                    while (check > 0) {
                        Vector listAgunan2 = PstAgunan.list(0, 0, "" + PstAgunan.fieldNames[PstAgunan.FLD_KODE_JENIS_AGUNAN] + " = '" + nomorAgunan + "'", "");
                        if (listAgunan2.isEmpty()) {
                            check = 0;
                        } else {
                            last += 1;
                            newLast = "" + last;
                            if (newLast.length() == 1) {
                                newLast = "000" + last;
                            } else if (newLast.length() == 2) {
                                newLast = "00" + last;
                            } else if (newLast.length() == 3) {
                                newLast = "0" + last;
                            } else if (newLast.length() == 4) {
                                newLast = "" + last;
                            }
                            nomorAgunan = kodeAgunan + today + "-" + newLast;
                        }
                    }
                    entAgunan.setKodeJenisAgunan(nomorAgunan);
                }
                
                frmAgunan.requestEntityObject(entAgunan);
                entAgunan.setContactId(oidAnggota);

                if (frmAgunan.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entAgunan.getOID() == 0) {
                    try {
                        long oid = pstAgunan.insertExc(this.entAgunan);
                        AgunanMapping agunanMapping = new AgunanMapping();
                        agunanMapping.setAgunanId(oid);
                        agunanMapping.setPinjamanId(oidPinjaman);
                        agunanMapping.setProsentasePinjaman(prosentasePinjaman);
                        PstAgunanMapping.insertExc(agunanMapping);
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
                        long oid = pstAgunan.updateExc(this.entAgunan);
                        Vector listMapping = PstAgunanMapping.list(0, 0, "" + PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_AGUNAN_ID] + " = '" + oid + "'"
                                + " AND " + PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_PINJAMAN_ID] + " = '" + oidPinjaman + "'", "");
                        AgunanMapping agunanMapping = new AgunanMapping();
                        if (!listMapping.isEmpty()) {
                            AgunanMapping am = (AgunanMapping) listMapping.get(0);
                            agunanMapping = PstAgunanMapping.fetchExc(am.getOID());
                            agunanMapping.setProsentasePinjaman(prosentasePinjaman);
                            PstAgunanMapping.updateExc(agunanMapping);
                        } else {

                        }
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
                if (oidAgunan != 0) {
                    try {
                        entAgunan = PstAgunan.fetchExc(oidAgunan);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidAgunan != 0) {
                    try {
                        entAgunan = PstAgunan.fetchExc(oidAgunan);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidAgunan != 0) {
                    try {
                        long oid = PstAgunan.deleteExc(oidAgunan);
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
