/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.materialreqfacility;

/**
 *
 * @author Dewa
 */
import com.dimata.hanoman.entity.outletrsv.materialreqfacility.MaterialReqFacility;
import com.dimata.hanoman.entity.outletrsv.materialreqfacility.PstMaterialReqFacility;
import com.dimata.qdep.db.DBException;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;

/*
 Description : Controll MaterialReqFacility
 Date : Tue Feb 21 2017
 Author : Dewa
 */
public class CtrlMaterialReqFacility extends Control implements I_Language {

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
    private MaterialReqFacility entMaterialReqFacility;
    private PstMaterialReqFacility pstMaterialReqFacility;
    private FrmMaterialReqFacility frmMaterialReqFacility;
    private long MaterialReqFacilityId = 0;
    int language = LANGUAGE_DEFAULT;

    public CtrlMaterialReqFacility(HttpServletRequest request) {
        msgString = "";
        entMaterialReqFacility = new MaterialReqFacility();
        try {
            pstMaterialReqFacility = new PstMaterialReqFacility(0);
        } catch (Exception e) {;
        }
        frmMaterialReqFacility = new FrmMaterialReqFacility(request, entMaterialReqFacility);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMaterialReqFacility.addError(frmMaterialReqFacility.FRM_FIELD_MATERIAL_REQ_FACILITY_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MaterialReqFacility getMaterialReqFacility() {
        return entMaterialReqFacility;
    }

    public FrmMaterialReqFacility getForm() {
        return frmMaterialReqFacility;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMaterialReqFacility) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMaterialReqFacility != 0) {
                    try {
                        entMaterialReqFacility = PstMaterialReqFacility.fetchExc(oidMaterialReqFacility);
                    } catch (Exception exc) {
                    }
                }

                frmMaterialReqFacility.requestEntityObject(entMaterialReqFacility);

                if (frmMaterialReqFacility.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMaterialReqFacility.getOID() == 0) {
                    try {
                        long oid = pstMaterialReqFacility.insertExc(this.entMaterialReqFacility);
                        setMaterialReqFacilityId(oid);
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
                        long oid = pstMaterialReqFacility.updateExc(this.entMaterialReqFacility);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMaterialReqFacility != 0) {
                    try {
                        entMaterialReqFacility = PstMaterialReqFacility.fetchExc(oidMaterialReqFacility);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMaterialReqFacility != 0) {
                    try {
                        entMaterialReqFacility = PstMaterialReqFacility.fetchExc(oidMaterialReqFacility);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMaterialReqFacility != 0) {
                    try {
                        long oid = PstMaterialReqFacility.deleteExc(oidMaterialReqFacility);
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
   * @return the MaterialReqFacilityId
   */
  public long getMaterialReqFacilityId() {
    return MaterialReqFacilityId;
  }

  /**
   * @param MaterialReqFacilityId the MaterialReqFacilityId to set
   */
  public void setMaterialReqFacilityId(long MaterialReqFacilityId) {
    this.MaterialReqFacilityId = MaterialReqFacilityId;
  }
}
