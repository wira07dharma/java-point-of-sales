/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/* java package */

import java.util.*;
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
public class CtrlSalesType extends Control implements I_Language {
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
    private SalesType salesType;
    private PstSalesType pstSalesType;
    private FrmSalesType frmSalesType;
    int language = LANGUAGE_FOREIGN;

    public CtrlSalesType(HttpServletRequest request) {
        msgString = "";
        salesType = new SalesType();
        try {
            pstSalesType = new PstSalesType(0);
        } catch (Exception e) {
            ;
        }
        frmSalesType = new FrmSalesType(request, salesType);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmSalesType.addError(frmSalesType.FRM_FIELD_TYPE_SALES_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public SalesType getCosting() {
        return salesType;
    }

    public FrmSalesType getForm() {
        return frmSalesType;
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
                        salesType = PstSalesType.fetchExc(oidCosting);
                    } catch (Exception exc) {
                    }
                }

                frmSalesType.requestEntityObject(salesType);
                
                if (frmSalesType.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                String whereClause = "( " + PstSalesType.fieldNames[PstSalesType.FLD_TYPE_SALES_NAME] + " = '" + salesType.getName() +
                        "') AND " + PstSalesType.fieldNames[PstSalesType.FLD_TYPE_SALES_ID] + " <> " + salesType.getOID();
                Vector isExist = PstSalesType.list(0, 1, whereClause, "");
                if (isExist != null && isExist.size() > 0) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }


                if (salesType.getOID() == 0) {
                    try {
                        long oid = pstSalesType.insertExc(this.salesType);
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
                        long oid = pstSalesType.updateExc(this.salesType);
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
                        salesType = PstSalesType.fetchExc(oidCosting);
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
                        salesType = PstSalesType.fetchExc(oidCosting);
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
                            oid = PstSalesType.deleteExc(oidCosting);
                         }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            frmSalesType.addError(FrmSalesType.FRM_FIELD_TYPE_SALES_ID, "");
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
