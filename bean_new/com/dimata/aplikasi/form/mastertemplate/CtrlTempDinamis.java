/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.form.mastertemplate;

import com.dimata.aplikasi.entity.mastertemplate.PstTempDinamis;
import com.dimata.aplikasi.entity.mastertemplate.TempDinamis;
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
 * @author arin
 */
public class CtrlTempDinamis extends Control implements I_Language {

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
    private TempDinamis tempDinamis;
    private TempDinamis prevTempDinamis;
    private PstTempDinamis pstTempDinamis;
    private FrmTempDinamis frmTempDinamis;
    private int LANGUAGE_DEFAULT;
    int language = LANGUAGE_DEFAULT;

    public CtrlTempDinamis(HttpServletRequest request) {

        msgString = "";

        tempDinamis = new TempDinamis();

        try {

            //?
            pstTempDinamis = new PstTempDinamis(0);
        } catch (Exception ex) {;
        }
        frmTempDinamis = new FrmTempDinamis(request, tempDinamis);
    }

    private String getSystemMessage(int msgCode) {

        switch (msgCode) {

            case I_DBExceptionInfo.MULTIPLE_ID:
                //this.frmTempDinamis.addError(frmTempDinamis.FRM_FLD_TEMP_DINAMIS_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public TempDinamis getTempDinamis() {
        return tempDinamis;
    }

    public FrmTempDinamis getForm() {
        return frmTempDinamis;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidTempDinamis) {

        msgString = "";

        int exCode = I_DBExceptionInfo.NO_EXCEPTION;

        int rsCode = RSLT_OK;

        switch (cmd) {

            case Command.ADD:

                break;
            case Command.SAVE:

                if (oidTempDinamis != 0) {

                    try {

                        tempDinamis = PstTempDinamis.fetchExc(oidTempDinamis);
                        prevTempDinamis = PstTempDinamis.fetchExc(oidTempDinamis);
                    } catch (Exception ex) {

                    }

                }

                frmTempDinamis.requetEntityObject(tempDinamis);
                if (prevTempDinamis != null && prevTempDinamis.getOID() != 0) {
                    tempDinamis.setOID(prevTempDinamis.getOID());
                }
                if (frmTempDinamis.errorSize() > 0) {

                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

                    return RSLT_FORM_INCOMPLETE;
                }
                if (tempDinamis.getOID() == 0) {

                    try {

                        long oid = PstTempDinamis.insertExc(this.tempDinamis);

                    } catch (Exception dbexc) {

                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {

                    try {
                        long oid = PstTempDinamis.updateExc(this.tempDinamis);
                    } catch (DBException ex) {
                        Logger.getLogger(CtrlTempDinamis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            case Command.EDIT:

                if (oidTempDinamis != 0) {

                    try {
                        tempDinamis = PstTempDinamis.fetchExc(oidTempDinamis);
                    } catch (DBException ex) {
                        Logger.getLogger(CtrlTempDinamis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;

            case Command.ASK:

                if (oidTempDinamis != 0) {

                    try {

                        tempDinamis = PstTempDinamis.fetchExc(oidTempDinamis);

                    } catch (Exception ex) {

                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:

                if (oidTempDinamis != 0) {

                    try {

                        long oid = PstTempDinamis.deleteExc(oidTempDinamis);

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
