/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.masterdata;

/**
 *
 * @author user
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import javax.servlet.http.*;

import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.masterdata.*;
/**
 *
 * @author user
 */
public class CtrlTransferToServer extends Control implements I_Language {
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
    private TransferToServer transferServer;
    private PstConnection pstconn;
    private FrmTransferToServer frmTransferServer;
    int language = LANGUAGE_DEFAULT;

    public CtrlTransferToServer(HttpServletRequest request) {
        msgString = "";
        transferServer = new TransferToServer();
        try {
            //pstconn = new PstConnection(0);
        } catch (Exception e) {
            ;
        }
        frmTransferServer = new FrmTransferToServer(request, transferServer);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                //this.frmLocation.addError(frmLocation.FRM_FIELD_LOCATION_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

    public TransferToServer getTransferToServer() {
        return transferServer;
    }

    public FrmTransferToServer getForm() {
        return frmTransferServer;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }



}

