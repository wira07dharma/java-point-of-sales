package com.dimata.posbo.form.masterdata;

import java.util.*;
import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;

import com.dimata.posbo.entity.masterdata.*;

public class CtrlCodeRange extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Code Material MatCurrency sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Material MatCurrency Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private CodeRange codeRange;
    private PstCodeRange pstCodeRange;
    private FrmCodeRange frmCodeRange;
    int language = LANGUAGE_FOREIGN;

    public CtrlCodeRange(HttpServletRequest request) {
        msgString = "";
        codeRange = new CodeRange();
        try {
            pstCodeRange = new PstCodeRange(0);
        } catch (Exception e) {
            ;
        }
        frmCodeRange = new FrmCodeRange(request, codeRange);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCodeRange.addError(FrmCodeRange.FRM_FIELD_FROM_CODE, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public CodeRange getCodeRange() {
        return codeRange;
    }

    public FrmCodeRange getForm() {
        return frmCodeRange;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidCodeRange) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidCodeRange != 0) {
                    try {
                        codeRange = PstCodeRange.fetchExc(oidCodeRange);
                    } catch (Exception exc) {
                    }
                }

                frmCodeRange.requestEntityObject(codeRange);

                if (frmCodeRange.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (codeRange.getOID() == 0) {
                    try {
                        long oid = pstCodeRange.insertExc(this.codeRange);
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
                        long oid = pstCodeRange.updateExc(this.codeRange);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidCodeRange != 0) {
                    try {
                        codeRange = PstCodeRange.fetchExc(oidCodeRange);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidCodeRange != 0) {
                    try {
                        codeRange = PstCodeRange.fetchExc(oidCodeRange);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidCodeRange != 0) {
                    try {
                        long oid = PstCodeRange.deleteExc(oidCodeRange);
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
            default :
        }
        return rsCode;
    }
}
