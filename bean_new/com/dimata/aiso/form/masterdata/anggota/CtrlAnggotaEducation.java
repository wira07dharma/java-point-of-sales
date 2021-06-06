/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.anggota;

import com.dimata.aiso.entity.masterdata.anggota.AnggotaEducation;
import com.dimata.aiso.entity.masterdata.anggota.PstAnggotaEducation;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author HaddyPuutraa (PKL) Created Kamis, 21 Pebruari 2013
 */
public class CtrlAnggotaEducation extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_RECORD_NOT_FOUND = 4;

    public static String[][] resultText = {
        {"Proses Berhasil", "Tidak Dapat Diproses", "Data Tersebut Sudah Ada", "Data Tidak Lengkap", "Data tidak ditemukan karena telah diubah oleh user lain"},
        {"Success", "Can not process", "Data already exist", "Data Incomplete", "Data not found cause another user changed it"}
    };

    private int start;
    private String msgString;
    private AnggotaEducation anggotaEducation;
    private PstAnggotaEducation pstAnggotaEducation;
    private FrmAnggotaEducation frmAnggotaEducation;
    int language = LANGUAGE_DEFAULT;

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public CtrlAnggotaEducation(HttpServletRequest request) {
        msgString = "";
        anggotaEducation = new AnggotaEducation();
        try {
            pstAnggotaEducation = new PstAnggotaEducation(0);
        } catch (Exception e) {

        }
        frmAnggotaEducation = new FrmAnggotaEducation(request, anggotaEducation);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmAnggotaEducation.addError(frmAnggotaEducation.FRM_EDUCATION_DETAIL, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public AnggotaEducation getAnggotaEducation() {
        return anggotaEducation;
    }

    public FrmAnggotaEducation getForm() {
        return frmAnggotaEducation;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int actionList(int listCmd, int start, int vectSize, int recordToGet) {
        msgString = "";

        switch (listCmd) {
            case Command.FIRST:
                this.start = 0;
                break;

            case Command.PREV:
                this.start = start - recordToGet;
                if (start < 0) {
                    this.start = 0;
                }
                break;

            case Command.NEXT:
                this.start = start + recordToGet;
                if (start >= vectSize) {
                    this.start = start - recordToGet;
                }
                break;

            case Command.LAST:
                int mdl = vectSize % recordToGet;
                if (mdl > 0) {
                    this.start = vectSize - mdl;
                } else {
                    this.start = vectSize - recordToGet;
                }

                break;

            default:
                this.start = start;
                if (vectSize < 1) {
                    this.start = 0;
                }

                if (start > vectSize) {
                    // set to last
                    mdl = vectSize % recordToGet;
                    if (mdl > 0) {
                        this.start = vectSize - mdl;
                    } else {
                        this.start = vectSize - recordToGet;
                    }
                }
                break;
        } //end switch
        return this.start;
    }

    public int action(int cmd, long anggotaOid, long educationOid) throws DBException {
        int errCode = -1;
        int excCode = 0;
        int rsCode = 0;

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (educationOid != 0) {
                    try {
                        anggotaEducation = pstAnggotaEducation.fetchExc(anggotaOid, educationOid);
                    } catch (Exception exc) {

                    }
                }

                frmAnggotaEducation.requestEntityObject(anggotaEducation);
                if (frmAnggotaEducation.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (educationOid == 0) {
                    try {
                        //long oid = pstAnggotaEducation.insertExc(this.anggotaEducation);
                        int result = pstAnggotaEducation.insertAnggotaEducation(anggotaEducation);
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
                        //long oid = pstAnggotaEducation.updateExc(anggotaEducation, educationOid);
                        int result = pstAnggotaEducation.updateAnggotaEducation(anggotaEducation, educationOid);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (anggotaOid != 0 && educationOid != 0) {
                    try {
                        anggotaEducation = pstAnggotaEducation.fetchExc(anggotaOid, educationOid);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (anggotaOid != 0 && educationOid != 0) {
                    try {
                        anggotaEducation = pstAnggotaEducation.fetchExc(anggotaOid, educationOid);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (anggotaOid != 0 && educationOid != 0) {
                    try {
                        //long oid = pstAnggotaEducation.deleteExc(anggotaOid, educationOid);
                        anggotaEducation = pstAnggotaEducation.fetchExc(anggotaOid, educationOid);
                        int result = pstAnggotaEducation.deleteAnggotaEducation(anggotaEducation);
                        if (result != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_UNKNOWN_ERROR;
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

        }//end switch

        return excCode;
    }
}
