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
/* project package */

import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.masterdata.SessSales;

public class CtrlSales extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Kode Sales sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Sales Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private Sales sales;
    private PstSales pstSales;
    private FrmSales frmSales;
    int language = LANGUAGE_FOREIGN;

    public CtrlSales(HttpServletRequest request) {
        msgString = "";
        sales = new Sales();
        try {
            pstSales = new PstSales(0);
        } catch (Exception e) {
            ;
        }
        frmSales = new FrmSales(request, sales);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmSales.addError(frmSales.FRM_FIELD_SALES_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Sales getSales() {
        return sales;
    }

    public FrmSales getForm() {
        return frmSales;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidSales) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidSales != 0) {
                    try {
                        sales = PstSales.fetchExc(oidSales);
                    } catch (Exception exc) {
                    }
                }

                frmSales.requestEntityObject(sales);

                if (frmSales.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                /*String whereClause = "( " + PstSales.fieldNames[PstSales.FLD_NAME] + " = '" + sales.getName() +
                        "') AND " + PstSales.fieldNames[PstSales.FLD_SALES_ID] + " <> " + sales.getOID();
                Vector isExist = PstSales.list(0, 1, whereClause, "");
                if (isExist != null && isExist.size() > 0) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }*/
                    

                if (sales.getOID() == 0) {
                    try {
                        long oid = pstSales.insertExc(this.sales);
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
                        long oid = pstSales.updateExc(this.sales);
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
                if (oidSales != 0) {
                    try {
                        sales = PstSales.fetchExc(oidSales);
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

            case Command.ASK:
                if (oidSales != 0) {
                    try {
                        sales = PstSales.fetchExc(oidSales);
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

            case Command.DELETE:
                if (oidSales != 0) {
                    try {
                        sales = PstSales.fetchExc(oidSales);
                        long oid = 0;
                        if (SessSales.readyDataToDelete(oidSales, sales.getCode())) {
                            oid = PstSales.deleteExc(oidSales);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            frmSales.addError(FrmSales.FRM_FIELD_SALES_ID, "");
                            msgString = "Hapus data gagal, data masih di gunakan oleh modul lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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
                }
                break;

            default :

        }
        return excCode;
    }
}
