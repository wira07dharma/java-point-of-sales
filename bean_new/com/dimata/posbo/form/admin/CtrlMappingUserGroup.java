/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.admin;

import com.dimata.posbo.entity.admin.MappingUserGroup;
import com.dimata.posbo.entity.admin.PstMappingUserGroup;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;

/*
Description : Controll MappingUserGroup
Date : Fri Dec 27 2019
Author : WiraDharma
 */
public class CtrlMappingUserGroup extends Control implements I_Language {

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
  private MappingUserGroup entMappingUserGroup;
  private PstMappingUserGroup pstMappingUserGroup;
  private FrmMappingUserGroup frmMappingUserGroup;
  int language = LANGUAGE_DEFAULT;

  public CtrlMappingUserGroup(HttpServletRequest request) {
    msgString = "";
    entMappingUserGroup = new MappingUserGroup();
    try {
      pstMappingUserGroup = new PstMappingUserGroup(0);
    } catch (Exception e) {;
    }
    frmMappingUserGroup = new FrmMappingUserGroup(request, entMappingUserGroup);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmMappingUserGroup.addError(frmMappingUserGroup.FRM_FIELD_MAPPING_USER_GROUP_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public MappingUserGroup getMappingUserGroup() {
    return entMappingUserGroup;
  }

  public FrmMappingUserGroup getForm() {
    return frmMappingUserGroup;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidMappingUserGroup) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidMappingUserGroup != 0) {
          try {
            entMappingUserGroup = pstMappingUserGroup.fetchExc(oidMappingUserGroup);
          } catch (Exception exc) {
          }
        }

        frmMappingUserGroup.requestEntityObject(entMappingUserGroup);

        if (frmMappingUserGroup.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entMappingUserGroup.getOID() == 0) {
          try {
            long oid = pstMappingUserGroup.insertExc(this.entMappingUserGroup);
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
            long oid = pstMappingUserGroup.updateExc(this.entMappingUserGroup);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidMappingUserGroup != 0) {
          try {
            entMappingUserGroup = PstMappingUserGroup.fetchExc(oidMappingUserGroup);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidMappingUserGroup != 0) {
          try {
            entMappingUserGroup = PstMappingUserGroup.fetchExc(oidMappingUserGroup);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidMappingUserGroup != 0) {
          try {
            long oid = PstMappingUserGroup.deleteExc(oidMappingUserGroup);
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
