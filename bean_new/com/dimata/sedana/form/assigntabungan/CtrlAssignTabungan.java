/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.assigntabungan;

/**
 *
 * @author Regen
 */
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.qdep.db.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.sedana.entity.assigntabungan.AssignTabungan;
import com.dimata.sedana.entity.assigntabungan.PstAssignTabungan;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import javax.servlet.http.*;

public class CtrlAssignTabungan extends Control implements I_Language {

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
  private AssignTabungan entAssignTabungan;
  private PstAssignTabungan pstAssignTabungan;
  private FrmAssignTabungan frmAssignTabungan;
  int language = LANGUAGE_DEFAULT;

  public CtrlAssignTabungan(HttpServletRequest request) {
    msgString = "";
    entAssignTabungan = new AssignTabungan();
    try {
      pstAssignTabungan = new PstAssignTabungan(0);
    } catch (Exception e) {;
    }
    frmAssignTabungan = new FrmAssignTabungan(request, entAssignTabungan);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmAssignTabungan.addError(frmAssignTabungan.FRM_FIELD_ASSIGNMASTERTABUNGANID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public AssignTabungan getAssignTabungan() {
    return entAssignTabungan;
  }

  public FrmAssignTabungan getForm() {
    return frmAssignTabungan;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidAssignTabungan) {
    return action(cmd, oidAssignTabungan, 0, false);
  }

  public int action(int cmd, long oidAssignTabungan, long oidMasterTabungan, boolean multipleJenisSimpanan) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidAssignTabungan != 0) {
          try {
            entAssignTabungan = PstAssignTabungan.fetchExc(oidAssignTabungan);
          } catch (Exception exc) {
          }
        }

        frmAssignTabungan.requestEntityObject(entAssignTabungan);
        if(oidMasterTabungan != 0) {
          entAssignTabungan.setMasterTabungan(oidMasterTabungan);
        }

        if (frmAssignTabungan.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entAssignTabungan.getOID() == 0) {
          try {
            long oid = pstAssignTabungan.insertExc(this.entAssignTabungan);
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
            long oid = pstAssignTabungan.updateExc(this.entAssignTabungan);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidAssignTabungan != 0) {
          try {
            entAssignTabungan = PstAssignTabungan.fetchExc(oidAssignTabungan);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidAssignTabungan != 0) {
          try {
            entAssignTabungan = PstAssignTabungan.fetchExc(oidAssignTabungan);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidAssignTabungan != 0) {
          try {
            long oid = PstAssignTabungan.deleteExc(oidAssignTabungan);
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
