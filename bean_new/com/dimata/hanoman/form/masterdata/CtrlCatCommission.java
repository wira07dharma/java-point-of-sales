package com.dimata.hanoman.form.masterdata;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.dimata.hanoman.form.masterdata.FrmCatCommission;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.CaCommission;
import com.dimata.posbo.entity.masterdata.PstCatCommission;

public class CtrlCatCommission extends Control implements I_Language {

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
    private CaCommission entCatCommission;
    private PstCatCommission pstCatCommission;
    private FrmCatCommission frmCatCommission;
    private long catCommissionId = 0;
    int language = LANGUAGE_DEFAULT;

    public CtrlCatCommission(HttpServletRequest request) {
        msgString = "";
        entCatCommission = new CaCommission();
        try {
          pstCatCommission = new PstCatCommission(0);
        } catch (Exception e) {
        }
        frmCatCommission = new FrmCatCommission(request, entCatCommission);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCatCommission.addError(frmCatCommission.FRM_FIELD_CAT_COMMISSION_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public CaCommission getCatCommission() {
        return entCatCommission;
    }

    public FrmCatCommission getForm() {
        return frmCatCommission;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidCatCommission) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidCatCommission != 0) {
                    try {
                        entCatCommission = PstCatCommission.fetchExc(oidCatCommission);
                    } catch (Exception exc) {
                    }
                }

                frmCatCommission.requestEntityObject(entCatCommission);

                if (frmCatCommission.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entCatCommission.getOID() == 0) {
                    try {
                        long oid = pstCatCommission.insertExc(this.entCatCommission);
                        setcatCommissionId(oid);
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
                        long oid = pstCatCommission.updateExc(this.entCatCommission);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidCatCommission != 0) {
                    try {
                        entCatCommission = PstCatCommission.fetchExc(oidCatCommission);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidCatCommission != 0) {
                    try {
                        entCatCommission = PstCatCommission.fetchExc(oidCatCommission);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidCatCommission != 0) {
                    try {
                        long oid = PstCatCommission.deleteExc(oidCatCommission);
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

  /**
   * @return the catCommissionId
   */
  public long getcatCommissionId() {
    return catCommissionId;
  }

  /**
   * @param catCommissionId the catCommissionId to set
   */
  public void setcatCommissionId(long MaterialReLocationId) {
    this.catCommissionId = MaterialReLocationId;
  }
  
}
