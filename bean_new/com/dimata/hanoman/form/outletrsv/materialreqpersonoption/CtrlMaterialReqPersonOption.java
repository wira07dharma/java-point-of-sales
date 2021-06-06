/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.materialreqpersonoption;

import com.dimata.hanoman.entity.outletrsv.materialreqpersonoption.MaterialReqPersonOption;
import com.dimata.hanoman.entity.outletrsv.materialreqpersonoption.PstMaterialReqPersonOption;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;

/*
 Description : Controll MaterialReqPersonOption
 Date : Tue Feb 21 2017
 Author : Dewa
 */
public class CtrlMaterialReqPersonOption extends Control implements I_Language {

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
    private MaterialReqPersonOption entMaterialReqPersonOption;
    private PstMaterialReqPersonOption pstMaterialReqPersonOption;
    private FrmMaterialReqPersonOption frmMaterialReqPersonOption;
    private long MaterialReqPersonOptionId = 0;
    int language = LANGUAGE_DEFAULT;

    public CtrlMaterialReqPersonOption(HttpServletRequest request) {
        msgString = "";
        entMaterialReqPersonOption = new MaterialReqPersonOption();
        try {
            pstMaterialReqPersonOption = new PstMaterialReqPersonOption(0);
        } catch (Exception e) {;
        }
        frmMaterialReqPersonOption = new FrmMaterialReqPersonOption(request, entMaterialReqPersonOption);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMaterialReqPersonOption.addError(frmMaterialReqPersonOption.FRM_FIELD_MATERIAL_REQ_PERSON_OPTION_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MaterialReqPersonOption getMaterialReqPersonOption() {
        return entMaterialReqPersonOption;
    }

    public FrmMaterialReqPersonOption getForm() {
        return frmMaterialReqPersonOption;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMaterialReqPersonOption) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMaterialReqPersonOption != 0) {
                    try {
                        entMaterialReqPersonOption = PstMaterialReqPersonOption.fetchExc(oidMaterialReqPersonOption);
                    } catch (Exception exc) {
                    }
                }

                frmMaterialReqPersonOption.requestEntityObject(entMaterialReqPersonOption);

                if (frmMaterialReqPersonOption.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMaterialReqPersonOption.getOID() == 0) {
                    try {
                        long oid = pstMaterialReqPersonOption.insertExc(this.entMaterialReqPersonOption);
                        setMaterialReqPersonOptionId(oid);
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
                        long oid = pstMaterialReqPersonOption.updateExc(this.entMaterialReqPersonOption);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMaterialReqPersonOption != 0) {
                    try {
                        entMaterialReqPersonOption = PstMaterialReqPersonOption.fetchExc(oidMaterialReqPersonOption);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMaterialReqPersonOption != 0) {
                    try {
                        entMaterialReqPersonOption = PstMaterialReqPersonOption.fetchExc(oidMaterialReqPersonOption);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMaterialReqPersonOption != 0) {
                    try {
                        long oid = PstMaterialReqPersonOption.deleteExc(oidMaterialReqPersonOption);
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
   * @return the MaterialReqPersonOptionId
   */
  public long getMaterialReqPersonOptionId() {
    return MaterialReqPersonOptionId;
  }

  /**
   * @param MaterialReqPersonOptionId the MaterialReqPersonOptionId to set
   */
  public void setMaterialReqPersonOptionId(long MaterialReqPersonOptionId) {
    this.MaterialReqPersonOptionId = MaterialReqPersonOptionId;
  }
}
