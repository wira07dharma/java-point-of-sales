/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.anggota;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.sedana.entity.anggota.PengurusKelompok;
import com.dimata.sedana.entity.anggota.PstPengurusKelompok;

/*
 Description : Controll PengurusKelompok
 Date : Sat Aug 26 2017
 Author : dewa
 */
public class CtrlPengurusKelompok extends Control implements I_Language {

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
    private PengurusKelompok entPengurusKelompok;
    private PstPengurusKelompok pstPengurusKelompok;
    private FrmPengurusKelompok frmPengurusKelompok;
    int language = LANGUAGE_DEFAULT;

    public CtrlPengurusKelompok(HttpServletRequest request) {
        msgString = "";
        entPengurusKelompok = new PengurusKelompok();
        try {
            pstPengurusKelompok = new PstPengurusKelompok(0);
        } catch (Exception e) {;
        }
        frmPengurusKelompok = new FrmPengurusKelompok(request, entPengurusKelompok);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPengurusKelompok.addError(frmPengurusKelompok.FRM_FIELD_ID_PENGURUS, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public PengurusKelompok getPengurusKelompok() {
        return entPengurusKelompok;
    }

    public FrmPengurusKelompok getForm() {
        return frmPengurusKelompok;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidPengurusKelompok) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidPengurusKelompok != 0) {
                    try {
                        entPengurusKelompok = PstPengurusKelompok.fetchExc(oidPengurusKelompok);
                    } catch (Exception exc) {
                    }
                }

                frmPengurusKelompok.requestEntityObject(entPengurusKelompok);

                if (frmPengurusKelompok.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entPengurusKelompok.getOID() == 0) {
                    try {
                        long oid = pstPengurusKelompok.insertExc(this.entPengurusKelompok);
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
                        long oid = pstPengurusKelompok.updateExc(this.entPengurusKelompok);
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
                if (oidPengurusKelompok != 0) {
                    try {
                        entPengurusKelompok = PstPengurusKelompok.fetchExc(oidPengurusKelompok);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPengurusKelompok != 0) {
                    try {
                        entPengurusKelompok = PstPengurusKelompok.fetchExc(oidPengurusKelompok);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidPengurusKelompok != 0) {
                    try {
                        long oid = PstPengurusKelompok.deleteExc(oidPengurusKelompok);
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
