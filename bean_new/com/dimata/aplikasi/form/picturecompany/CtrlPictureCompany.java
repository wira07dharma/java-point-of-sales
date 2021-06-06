/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.form.picturecompany;

import com.dimata.aplikasi.entity.picturecompany.PstPictureCompany;
import com.dimata.aplikasi.entity.picturecompany.PictureCompany;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
public class CtrlPictureCompany extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText = {
        {"Berhasil", "Tidak Bisa Di Proses", "Kode Sudah Ada", "Data Belum Lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };

    private int start;
    private String msgString;
    private PictureCompany pictureCompany;
    private PictureCompany prevPictureCompany;
    private PstPictureCompany pstPictureCompany;
    private FrmPictureCompany frmPictureCompany;
    private int LANGUAGE_DEFAULT;
    int language = LANGUAGE_DEFAULT;

    public CtrlPictureCompany(HttpServletRequest request) {
        msgString = "";
        pictureCompany = new PictureCompany();
        try {
            pstPictureCompany = new PstPictureCompany();
        } catch (Exception ex) {;
        }
        frmPictureCompany = new FrmPictureCompany();
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPictureCompany.addError(FrmPictureCompany.FRM_FLD_PICTURE_COMPANY_ID, resultText[language][RSLT_EST_CODE_EXIST]);
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_EST_CODE_EXIST];
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

    public PictureCompany getPictureCompany() {
        return pictureCompany;
    }

    public FrmPictureCompany getForm() {
        return frmPictureCompany;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidPictureCompany, PictureCompany objPictureCompany, String pathImg) {
        msgString = "";

        int exCode = I_DBExceptionInfo.NO_EXCEPTION;

        int rsCode = RSLT_OK;

        switch (cmd) {
            case Command.ADD:
                break;
            case Command.SAVE:

                if (oidPictureCompany != 0) {
                    try {
                        pictureCompany = PstPictureCompany.fetchExc(oidPictureCompany);
                        prevPictureCompany = PstPictureCompany.fetchExc(oidPictureCompany);
                    } catch (Exception ex) {

                    }
                }

                // frmPictureCompany.requetEntityObject(pictureCompany);
                pictureCompany.setNamaPicture(objPictureCompany.getNamaPicture());

                if (pictureCompany.getOID() == 0) {
                    try {
                        long oid = PstPictureCompany.insertExc(this.pictureCompany);
                    } catch (Exception dbexc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    if (prevPictureCompany != null && prevPictureCompany.getOID() != 0 && prevPictureCompany.getNamaPicture() != null && prevPictureCompany.getNamaPicture().length() > 0) {
                        PstPictureCompany.deletePictureCompany(prevPictureCompany, pathImg);

                    }
                    try {
                        long oid = PstPictureCompany.updateExc(this.pictureCompany);
                    } catch (DBException ex) {
                        Logger.getLogger(CtrlPictureCompany.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case Command.EDIT:
                if (oidPictureCompany != 0) {
                    try {
                        pictureCompany = PstPictureCompany.fetchExc(oidPictureCompany);
                    } catch (DBException ex) {
                        Logger.getLogger(CtrlPictureCompany.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            case Command.ASK:
                if (oidPictureCompany != 0) {
                    try {
                        pictureCompany = PstPictureCompany.fetchExc(oidPictureCompany);
                    } catch (Exception ex) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidPictureCompany != 0) {
                    try {
                        long oid = PstPictureCompany.deleteExc(oidPictureCompany);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            exCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            exCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (Exception ex) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            default:
        }
        return rsCode;
    }
}
