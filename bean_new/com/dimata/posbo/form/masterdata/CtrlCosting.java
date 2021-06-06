/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.masterdata;

/* java package */

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;

import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.masterdata.SessCosting;

/**
 *
 * @author PT. Dimata
 */
public class CtrlCosting extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Code Material Shift sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Material Shift Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private Costing costing;
    private PstCosting pstCosting;
    private FrmCosting frmCosting;
    int language = LANGUAGE_FOREIGN;

    public CtrlCosting(HttpServletRequest request) {
        msgString = "";
        costing = new Costing();
        try {
            pstCosting = new PstCosting(0);
        } catch (Exception e) {
            ;
        }
        frmCosting = new FrmCosting(request, costing);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCosting.addError(frmCosting.FRM_FIELD_COSTING_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Costing getCosting() {
        return costing;
    }

    public FrmCosting getForm() {
        return frmCosting;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidCosting, HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidCosting != 0) {
                    try {
                        costing = PstCosting.fetchExc(oidCosting);
                    } catch (Exception exc) {
                    }
                }

                frmCosting.requestEntityObject(costing);
                
                if (frmCosting.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                String whereClause = "( " + PstCosting.fieldNames[PstCosting.FLD_NAME] + " = '" + costing.getName() +
                        "') AND " + PstCosting.fieldNames[PstCosting.FLD_COSTING_ID] + " <> " + costing.getOID();
                Vector isExist = PstCosting.list(0, 1, whereClause, "");
                if (isExist != null && isExist.size() > 0) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }


                if (costing.getOID() == 0) {
                    try {
                        long oid = pstCosting.insertExc(this.costing);
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
                        long oid = pstCosting.updateExc(this.costing);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidCosting != 0) {
                    try {
                        costing = PstCosting.fetchExc(oidCosting);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidCosting != 0) {
                    try {
                        costing = PstCosting.fetchExc(oidCosting);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidCosting != 0) {
                    try {
                        long oid = 0;
                        if (SessCosting.readyDataToDelete(oidCosting)) {
                            oid = PstCosting.deleteExc(oidCosting);
                         }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            frmCosting.addError(FrmCosting.FRM_FIELD_COSTING_ID, "");
                            msgString = "Hapus data gagal, Data masih di gunakan oleh modul lain.";
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
        return excCode;
    }

}
