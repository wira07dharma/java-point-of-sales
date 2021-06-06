/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.form.uploadpicture;

import com.dimata.aplikasi.entity.uploadpicture.PictureBackground;
import com.dimata.aplikasi.entity.uploadpicture.PstPictureBackground;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author arin
 */
public class CtrlPictureBackground extends Control implements I_Language {

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
    private PictureBackground pictureBackground;
    private PictureBackground prevPictureBackground;
    private PstPictureBackground pstPictureBackground;
    private FrmPictureBackground frmPictureBackground;
    private int LANGUAGE_DEFAULT;
    int language = LANGUAGE_DEFAULT;

    public CtrlPictureBackground(HttpServletRequest request) {

        msgString = "";

        pictureBackground = new PictureBackground();

        try {

            //?
            pstPictureBackground = new PstPictureBackground();
        } catch (Exception ex) {;
        }
        frmPictureBackground = new FrmPictureBackground();
    }

    private String getSystemMessage(int msgCode) {

        switch (msgCode) {

            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPictureBackground.addError(frmPictureBackground.FRM_FLD_NAMA_PICTURE, resultText[language][RSLT_EST_CODE_EXIST]);

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

    public PictureBackground getPictureBackground() {
        return pictureBackground;
    }

    public FrmPictureBackground getForm() {
        return frmPictureBackground;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidPictureBackground, PictureBackground objPictureBackground, String pathImg) {

        msgString = "";

        int exCode = I_DBExceptionInfo.NO_EXCEPTION;

        int rsCode = RSLT_OK;

        switch (cmd) {

            case Command.ADD:

                break;
            case Command.SAVE:

                if (oidPictureBackground != 0) {
                    try {

                        pictureBackground = PstPictureBackground.fetchExc(oidPictureBackground);
                        prevPictureBackground = PstPictureBackground.fetchExc(oidPictureBackground);
                    } catch (Exception ex) {

                    }
                }
                pictureBackground.setKeterangan(objPictureBackground.getKeterangan());
                pictureBackground.setNamaPicture(objPictureBackground.getNamaPicture());
                pictureBackground.setUploadPicture(objPictureBackground.getUploadPicture());
                pictureBackground.setLoginId(objPictureBackground.getLoginId());
                pictureBackground.setChangePicture(objPictureBackground.isChangePicture());
//                frmPictureBackground.requetEntityObject(pictureBackground);
//                
//                if (frmPictureBackground.errorSize()>0){
//                
//                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
//                    
//                    return RSLT_FORM_INCOMPLETE; }                                

                if (pictureBackground.getOID() == 0) {
                    try {

                        PictureBackground oid = PstPictureBackground.insertExc(this.pictureBackground);

                    } catch (Exception dbexc) {

                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {

                    if (prevPictureBackground != null && prevPictureBackground.getOID() != 0 && prevPictureBackground.getNamaPicture() != null && prevPictureBackground.getNamaPicture().length() > 0) {
                        if (pictureBackground != null && pictureBackground.isChangePicture()) {
                            PstPictureBackground.deletePictureBackground(prevPictureBackground, pathImg);
                        } else {
                            pictureBackground.setUploadPicture(prevPictureBackground.getUploadPicture());
                        }

                    }
                    try {
                        long oid = PstPictureBackground.updateExc(this.pictureBackground);
                    } catch (DBException ex) {
                        Logger.getLogger(CtrlPictureBackground.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            case Command.EDIT:

                if (oidPictureBackground != 0) {

                    try {
                        pictureBackground = PstPictureBackground.fetchExc(oidPictureBackground);
                    } catch (DBException ex) {
                        Logger.getLogger(CtrlPictureBackground.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;

            case Command.ASK:

                if (oidPictureBackground != 0) {

                    try {

                        pictureBackground = PstPictureBackground.fetchExc(oidPictureBackground);

                    } catch (Exception ex) {

                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:

                if (oidPictureBackground != 0) {
                    try {
                        prevPictureBackground = PstPictureBackground.fetchExc(oidPictureBackground);
                    } catch (Exception ex) {

                    }

                    try {

                        PstPictureBackground.deletePictureBackground(prevPictureBackground, pathImg);
                        PstPictureBackground.deleteExc(oidPictureBackground);
                    } catch (Exception exc) {

                        msgString = "can't delete " + exc;
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default:
        }
        return rsCode;
    }
}
