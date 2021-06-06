/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.materialreqperson;

import com.dimata.hanoman.entity.outletrsv.materialreqperson.MaterialReqPerson;
import com.dimata.hanoman.entity.outletrsv.materialreqperson.PstMaterialReqPerson;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;

/*
 Description : Controll MaterialReqPerson
 Date : Tue Feb 21 2017
 Author : Dewa
 */
public class CtrlMaterialReqPerson extends Control implements I_Language {

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
    private MaterialReqPerson entMaterialReqPerson;
    private PstMaterialReqPerson pstMaterialReqPerson;
    private FrmMaterialReqPerson frmMaterialReqPerson;
    private long MaterialReqPersonId = 0;
    int language = LANGUAGE_DEFAULT;

    public CtrlMaterialReqPerson(HttpServletRequest request) {
        msgString = "";
        entMaterialReqPerson = new MaterialReqPerson();
        try {
            pstMaterialReqPerson = new PstMaterialReqPerson(0);
        } catch (Exception e) {;
        }
        frmMaterialReqPerson = new FrmMaterialReqPerson(request, entMaterialReqPerson);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMaterialReqPerson.addError(frmMaterialReqPerson.FRM_FIELD_MATERIAL_REQ_PERSON_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MaterialReqPerson getMaterialReqPerson() {
        return entMaterialReqPerson;
    }

    public FrmMaterialReqPerson getForm() {
        return frmMaterialReqPerson;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMaterialReqPerson) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMaterialReqPerson != 0) {
                    try {
                        entMaterialReqPerson = PstMaterialReqPerson.fetchExc(oidMaterialReqPerson);
                    } catch (Exception exc) {
                    }
                }

                frmMaterialReqPerson.requestEntityObject(entMaterialReqPerson);

                if (frmMaterialReqPerson.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMaterialReqPerson.getOID() == 0) {
                    try {
                        long oid = pstMaterialReqPerson.insertExc(this.entMaterialReqPerson);
                        setMaterialReqPersonId(oid);
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
                        long oid = pstMaterialReqPerson.updateExc(this.entMaterialReqPerson);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMaterialReqPerson != 0) {
                    try {
                        entMaterialReqPerson = PstMaterialReqPerson.fetchExc(oidMaterialReqPerson);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMaterialReqPerson != 0) {
                    try {
                        entMaterialReqPerson = PstMaterialReqPerson.fetchExc(oidMaterialReqPerson);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMaterialReqPerson != 0) {
                    try {
                        long oid = PstMaterialReqPerson.deleteExc(oidMaterialReqPerson);
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
   * @return the MaterialReqPersonId
   */
  public long getMaterialReqPersonId() {
    return MaterialReqPersonId;
  }

  /**
   * @param MaterialReqPersonId the MaterialReqPersonId to set
   */
  public void setMaterialReqPersonId(long MaterialReqPersonId) {
    this.MaterialReqPersonId = MaterialReqPersonId;
  }
}
