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
import com.dimata.posbo.session.masterdata.SessMaterialUnit;

public class CtrlUnit extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Code Material Unit sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Material Unit Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private Unit unit;
    private PstUnit pstUnit;
    private FrmUnit frmUnit;
    int language = LANGUAGE_FOREIGN;

    public CtrlUnit(HttpServletRequest request) {
        msgString = "";
        unit = new Unit();
        try {
            pstUnit = new PstUnit(0);
        } catch (Exception e) {
            ;
        }
        frmUnit = new FrmUnit(request, unit);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmUnit.addError(frmUnit.FRM_FIELD_UNIT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Unit getUnit() {
        return unit;
    }

    public FrmUnit getForm() {
        return frmUnit;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidUnit) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidUnit != 0) {
                    try {
                        unit = PstUnit.fetchExc(oidUnit);
                    } catch (Exception exc) {
                    }
                }

                frmUnit.requestEntityObject(unit);

                if (frmUnit.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                String whereClause = "( " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " = '" + unit.getCode() +
                        "') AND " + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + " <> " + unit.getOID();
                Vector isExist = PstUnit.list(0, 1, whereClause, "");
                if (isExist != null && isExist.size() > 0) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }
                
                if (unit.getOID() == 0) {
                    try {
                        long oid = pstUnit.insertExc(this.unit);
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
                        long oid = pstUnit.updateExc(this.unit);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidUnit != 0) {
                    try {
                        unit = PstUnit.fetchExc(oidUnit);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidUnit != 0) {
                    try {
                        unit = PstUnit.fetchExc(oidUnit);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidUnit != 0) {
                    try {
                        long oid = 0;
                        if (SessMaterialUnit.readyDataToDelete(oidUnit)) {
                            oid = PstUnit.deleteExc(oidUnit);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            unit = PstUnit.fetchExc(oidUnit);
                            frmUnit.addError(FrmUnit.FRM_FIELD_UNIT_ID, "");
                            msgString = "Hapus data gagal,data masih digunakan oleh data lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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
