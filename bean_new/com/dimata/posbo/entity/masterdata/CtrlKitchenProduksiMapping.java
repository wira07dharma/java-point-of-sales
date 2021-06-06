/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;

/*
Description : Controll KitchenProduksiMapping
Date : Sun Aug 07 2016
Author : opie-eyek 20160807
 */
public class CtrlKitchenProduksiMapping extends Control implements I_Language {

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
    private KitchenProduksiMapping entKitchenProduksiMapping;
    private PstKitchenProduksiMapping pstKitchenProduksiMapping;
    private FrmKitchenProduksiMapping frmKitchenProduksiMapping;
    int language = LANGUAGE_DEFAULT;

    public CtrlKitchenProduksiMapping(HttpServletRequest request) {
        msgString = "";
        entKitchenProduksiMapping = new KitchenProduksiMapping();
        try {
            pstKitchenProduksiMapping = new PstKitchenProduksiMapping(0);
        } catch (Exception e) {;
        }
        frmKitchenProduksiMapping = new FrmKitchenProduksiMapping(request, entKitchenProduksiMapping);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmKitchenProduksiMapping.addError(frmKitchenProduksiMapping.FRM_FIELD_POSMAPPINGPRODUKSIID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public KitchenProduksiMapping getKitchenProduksiMapping() {
        return entKitchenProduksiMapping;
    }

    public FrmKitchenProduksiMapping getForm() {
        return frmKitchenProduksiMapping;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidKitchenProduksiMapping) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidKitchenProduksiMapping != 0) {
                    try {
                        entKitchenProduksiMapping = PstKitchenProduksiMapping.fetchExc(oidKitchenProduksiMapping);
                    } catch (Exception exc) {
                    }
                }

                frmKitchenProduksiMapping.requestEntityObject(entKitchenProduksiMapping);

                if (frmKitchenProduksiMapping.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entKitchenProduksiMapping.getOID() == 0) {
                    try {
                        long oid = pstKitchenProduksiMapping.insertExc(this.entKitchenProduksiMapping);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstKitchenProduksiMapping.updateExc(this.entKitchenProduksiMapping);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidKitchenProduksiMapping != 0) {
                    try {
                        entKitchenProduksiMapping = PstKitchenProduksiMapping.fetchExc(oidKitchenProduksiMapping);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidKitchenProduksiMapping != 0) {
                    try {
                        entKitchenProduksiMapping = PstKitchenProduksiMapping.fetchExc(oidKitchenProduksiMapping);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidKitchenProduksiMapping != 0) {
                    try {
                        long oid = PstKitchenProduksiMapping.deleteExc(oidKitchenProduksiMapping);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
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
