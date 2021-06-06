package com.dimata.posbo.form.masterdata;

import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.posbo.db.DBException;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.posbo.entity.masterdata.MatMappLocation;
import com.dimata.posbo.entity.masterdata.PstMatMappLocation;

import javax.servlet.http.HttpServletRequest;

public class CtrlMatMappLocation extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_DELETE_RESTRICT = 4;

    public static String[][] resultText = {
            {"Berhasil", "Tidak dapat diproses", "Kategori sudah ada", "Data tidak lengkap", "Kategori tidak bisa dihapus, masih dipakai modul lain ..."},
            {"Succes", "Can not process", "Category code already exist", "Data incomplete", "Cannot delete, category still used by another module"}
    };

    private int start;
    private String msgString;
    private MatMappLocation matMappLocation;
    private PstMatMappLocation pstMatMappLocation;
    private FrmMatMappLocation frmMatMappLocation;
    int language = LANGUAGE_FOREIGN;

    public CtrlMatMappLocation(HttpServletRequest request) {
        msgString = "";
        matMappLocation = new MatMappLocation();
        try {
            pstMatMappLocation = new PstMatMappLocation(0);
        } catch (Exception e) {
            ;
        }
        frmMatMappLocation = new FrmMatMappLocation(request, matMappLocation);
    }

    private String getSystemMessage(int msgCode) {
        //System.out.println("=========>msgCode : " + msgCode);
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return resultText[language][RSLT_EST_CODE_EXIST];
            case I_DBExceptionInfo.DEL_RESTRICTED:
                return resultText[language][RSLT_DELETE_RESTRICT];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_EST_CODE_EXIST;
            case I_DBExceptionInfo.DEL_RESTRICTED:
                return RSLT_DELETE_RESTRICT;
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

    public MatMappLocation getMatMappLocation() {
        return matMappLocation;
    }

    public FrmMatMappLocation getForm() {
        return frmMatMappLocation;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long locationOid, long productOid) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                boolean checkOIDs = PstMatMappLocation.checkOID(locationOid, productOid);
                if (checkOIDs) {
                    try {
                        matMappLocation = PstMatMappLocation.fetchExc(locationOid, productOid);
                    } catch (Exception exc) {
                    }
                }

                frmMatMappLocation.requestEntityObject(matMappLocation);

                if (frmMatMappLocation.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (checkOIDs == false) {
                    try {
                        long oid = pstMatMappLocation.insertExc(this.matMappLocation);
                    } catch (DBException dbexc) {
                        System.out.println("Masuk ke DBException");
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        System.out.println("Masuk ke Exception");
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        long oid = pstMatMappLocation.updateExc(this.matMappLocation);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                break;

            case Command.EDIT:
                try {
                    matMappLocation = PstMatMappLocation.fetchExc(locationOid, productOid);
                } catch (DBException dbexc) {
                    excCode = dbexc.getErrorCode();
                    msgString = getSystemMessage(excCode);
                    return getControlMsgId(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                }
                break;

            case Command.ASK:
                try {
                    matMappLocation = PstMatMappLocation.fetchExc(locationOid, productOid);
                } catch (DBException dbexc) {
                    excCode = dbexc.getErrorCode();
                    msgString = getSystemMessage(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                }
                break;

            case Command.DELETE:
                try {
                    long oid = PstMatMappLocation.deleteExc(locationOid, productOid);
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
                    return getControlMsgId(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                }
                break;

            default :

        }
        return rsCode;
    }
}
