/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.admin;

import com.dimata.posbo.entity.admin.FingerPatern;
import com.dimata.posbo.entity.admin.PstFingerPatern;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

public class CtrlFingerPatern
        extends Control
        implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText = {
        {"Berhasil", "Tidak Bisa Di Prosess", "kode Sudah Ada", "Data Belum Lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };

    private int start;
    private String msgString;
    private FingerPatern fingerPatern;
    private PstFingerPatern pstFingerPatern;
    private FrmFingerPatern frmFingerPatern;
    int language = LANGUAGE_DEFAULT;

    public CtrlFingerPatern(HttpServletRequest request) {
        msgString = "";
        fingerPatern = new FingerPatern();
        try {
            pstFingerPatern = new PstFingerPatern(0);
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        frmFingerPatern = new FrmFingerPatern(request, fingerPatern);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmFingerPatern.addError(frmFingerPatern.FRM_EMPLOYEE_ID,
                        resultText[language][RSLT_EST_CODE_EXIST]);
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

    public FingerPatern getFingerPatern() {
        return fingerPatern;
    }

    public FrmFingerPatern getForm() {
        return frmFingerPatern;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidFingerPatern, long oidEmployee) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                if (oidFingerPatern != 0) {
                    try {
                        fingerPatern = pstFingerPatern.fetchExc(oidFingerPatern);
                    } catch (Exception exc) {
                    }
                }
                frmFingerPatern.requestEntityObject(fingerPatern);
                if (frmFingerPatern.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                if (fingerPatern.getOID() == 0) {
                    try {
                        long oid = pstFingerPatern.insertExc(this.fingerPatern);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        long oid = pstFingerPatern.updateExc(this.fingerPatern);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.EDIT:
                if (oidFingerPatern != 0) {
                    try {
                        fingerPatern = pstFingerPatern.fetchExc(oidFingerPatern);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.ASK:

                if (oidFingerPatern != 0) {
                    try {
                        fingerPatern = PstFingerPatern.fetchExc(oidFingerPatern);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }

                break;
            case Command.DELETE:
                if (oidFingerPatern != 0) {
                    try {
                        long oid = pstFingerPatern.deleteExc(oidFingerPatern);
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

    public int action(int cmd, FingerPatern fingerPatern) {
        int rsCode = RSLT_OK;
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        switch (cmd) {
            case Command.SAVE:
                //cek dulu apakah sudah ada data yang dikirimkan
                String whereClause = "";
                whereClause = " " + PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID] + "=" + fingerPatern.getEmployeeId() + "";
                whereClause += " and " + PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_TYPE] + "=" + fingerPatern.getFingerType() + "";

                Vector listFingerPatern = PstFingerPatern.list(0, 0, whereClause, "");
                //jika belum ada 
                if (listFingerPatern.size() == 0) {
                    try {
                        long oid = pstFingerPatern.insertExc(fingerPatern);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    //jika sudah ada
                    FingerPatern fingerPatern1 = new FingerPatern();
                    fingerPatern1 = (FingerPatern) listFingerPatern.get(0);
                    fingerPatern.setOID(fingerPatern1.getOID());
                    try {
                        long oid = pstFingerPatern.updateExc(fingerPatern);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                break;

            case Command.DELETE:
                if (fingerPatern.getOID() != 0) {
                    try {
                        long oid = pstFingerPatern.deleteExc(fingerPatern);
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

            default:
        }

        return rsCode;
    }

}
