/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author Regen
 */
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.posbo.entity.masterdata.MasterGroupMapping;
import com.dimata.posbo.entity.masterdata.PstMasterGroupMapping;

/*
Description : Controll MasterGroupMapping
Date : Wed Nov 20 2019
Author : WiraDharma
 */
public class CtrlMasterGroupMapping extends Control implements I_Language {

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
  private MasterGroupMapping entMasterGroupMapping;
  private PstMasterGroupMapping pstMasterGroupMapping;
  private FrmMasterGroupMapping frmMasterGroupMapping;
  int language = LANGUAGE_DEFAULT;

  public CtrlMasterGroupMapping(HttpServletRequest request) {
    msgString = "";
    entMasterGroupMapping = new MasterGroupMapping();
    try {
      pstMasterGroupMapping = new PstMasterGroupMapping(0);
    } catch (Exception e) {;
    }
    frmMasterGroupMapping = new FrmMasterGroupMapping(request, entMasterGroupMapping);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmMasterGroupMapping.addError(frmMasterGroupMapping.FRM_FIELD_MASTER_GROUP_MAPPING_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public MasterGroupMapping getMasterGroupMapping() {
    return entMasterGroupMapping;
  }

  public FrmMasterGroupMapping getForm() {
    return frmMasterGroupMapping;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidMasterGroupMapping) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidMasterGroupMapping != 0) {
          try {
            entMasterGroupMapping = PstMasterGroupMapping.fetchExc(oidMasterGroupMapping);
          } catch (Exception exc) {
          }
        }

        frmMasterGroupMapping.requestEntityObject(entMasterGroupMapping);

        if (frmMasterGroupMapping.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entMasterGroupMapping.getOID() == 0) {
          try {
            long oid = pstMasterGroupMapping.insertExc(this.entMasterGroupMapping);
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
            long oid = pstMasterGroupMapping.updateExc(this.entMasterGroupMapping);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidMasterGroupMapping != 0) {
          try {
            entMasterGroupMapping = PstMasterGroupMapping.fetchExc(oidMasterGroupMapping);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidMasterGroupMapping != 0) {
          try {
            entMasterGroupMapping = PstMasterGroupMapping.fetchExc(oidMasterGroupMapping);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidMasterGroupMapping != 0) {
          try {
            long oid = PstMasterGroupMapping.deleteExc(oidMasterGroupMapping);
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
