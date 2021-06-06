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
import com.dimata.posbo.session.masterdata.SessShift;
import com.dimata.gui.jsp.ControlDate;

public class CtrlShift extends Control implements I_Language {
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
    private Shift shift;
    private PstShift pstShift;
    private FrmShift frmShift;
    int language = LANGUAGE_FOREIGN;

    public CtrlShift(HttpServletRequest request) {
        msgString = "";
        shift = new Shift();
        try {
            pstShift = new PstShift(0);
        } catch (Exception e) {
            ;
        }
        frmShift = new FrmShift(request, shift);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmShift.addError(frmShift.FRM_FIELD_SHIFT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Shift getShift() {
        return shift;
    }

    public FrmShift getForm() {
        return frmShift;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidShift, HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidShift != 0) {
                    try {
                        shift = PstShift.fetchExc(oidShift);
                    } catch (Exception exc) {
                    }
                }

                frmShift.requestEntityObject(shift);
                Date startTime = ControlDate.getTime(frmShift.fieldNames[FrmShift.FRM_FIELD_START_TIME], request);
                Date endTime = ControlDate.getTime(frmShift.fieldNames[FrmShift.FRM_FIELD_END_TIME], request);
                shift.setStartTime(startTime);
                shift.setEndTime(endTime);

                if (frmShift.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                String whereClause = "( " + PstShift.fieldNames[PstShift.FLD_NAME] + " = '" + shift.getName() +
                        "') AND " + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + " <> " + shift.getOID();
                Vector isExist = PstShift.list(0, 1, whereClause, "");
                if (isExist != null && isExist.size() > 0) {
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }


                if (shift.getOID() == 0) {
                    try {
                        long oid = pstShift.insertExc(this.shift);
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
                        long oid = pstShift.updateExc(this.shift);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidShift != 0) {
                    try {
                        shift = PstShift.fetchExc(oidShift);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidShift != 0) {
                    try {
                        shift = PstShift.fetchExc(oidShift);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidShift != 0) {
                    try {
                        long oid = 0;
                        if (SessShift.readyDataToDelete(oidShift)) {
                            oid = PstShift.deleteExc(oidShift);
                        }
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            frmShift.addError(FrmShift.FRM_FIELD_SHIFT_ID, "");
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
