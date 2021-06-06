/**
 * Created on 	: 3:00 PM
 *
 * @author	: gedhy
 * @version	: 01
 */
/**
 * *****************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 ******************************************************************
 */
package com.dimata.workflow.form.approval;

/* java package */
import javax.servlet.http.*;

/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;

/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.workflow.entity.approval.*;

public class CtrlDocType extends Control implements I_Language {

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
    private DocType docType;
    private PstDocType pstDocType;
    private FrmDocType frmDocType;
    int language = LANGUAGE_DEFAULT;

    public CtrlDocType(HttpServletRequest request) {
        msgString = "";
        docType = new DocType();
        try {
            pstDocType = new PstDocType(0);
        } catch (Exception e) {;
        }
        frmDocType = new FrmDocType(request, docType);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmDocType.addError(frmDocType.FRM_FIELD_DOCTYPE_OID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public DocType getDocType() {
        return docType;
    }

    public FrmDocType getForm() {
        return frmDocType;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidDocType) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidDocType != 0) {
                    try {
                        docType = PstDocType.fetchExc(oidDocType);
                    } catch (Exception exc) {
                    }
                }

                frmDocType.requestEntityObject(docType);

                boolean bool = PstDocType.cekDocType(docType.getType(), oidDocType);
                if (bool) {
                    frmDocType.addError(frmDocType.FRM_FIELD_DOCUMENT_TYPE, " Document type is already defined in system");
                }

                if (frmDocType.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (docType.getOID() == 0) {
                    try {
                        long oid = pstDocType.insertExc(this.docType);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstDocType.updateExc(this.docType);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }

                // process Document Status
                /*Vector vectDocStatus = this.frmDocType.getDocumentStatus(this.docType.getType());
                if(PstDocType.setDocumentStatus(this.docType.getType(),vectDocStatus)){
                    msgString = FRMMessage.getMsg(FRMMessage.ERR_SAVED);
                }else{
                    msgString = FRMMessage.getErr(FRMMessage.ERR_UNKNOWN);
                } */
                break;

            case Command.EDIT:
                if (oidDocType != 0) {
                    try {
                        docType = PstDocType.fetchExc(oidDocType);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidDocType != 0) {
                    try {
                        docType = PstDocType.fetchExc(oidDocType);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidDocType != 0) {
                    try {
                        long oid = PstDocType.deleteExc(oidDocType);
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
