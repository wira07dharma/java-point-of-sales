/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

/**
 *
 * @author Regen
 */
import com.dimata.posbo.entity.warehouse.BillImageMapping;
import com.dimata.posbo.entity.warehouse.PstBillImageMapping;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;

/*
Description : Controll BillImageMapping
Date : Fri Feb 07 2020
Author : Wiradarma
 */
public class CtrlBillImageMapping extends Control implements I_Language {

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
  private BillImageMapping entBillImageMapping;
  private PstBillImageMapping pstBillImageMapping;
  private FrmBillImageMapping frmBillImageMapping;
  int language = LANGUAGE_DEFAULT;

  public CtrlBillImageMapping(HttpServletRequest request) {
    msgString = "";
    entBillImageMapping = new BillImageMapping();
    try {
      pstBillImageMapping = new PstBillImageMapping(0);
    } catch (Exception e) {;
    }
    frmBillImageMapping = new FrmBillImageMapping(request, entBillImageMapping);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmBillImageMapping.addError(frmBillImageMapping.FRM_FIELD_IMAGE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public BillImageMapping getBillImageMapping() {
    return entBillImageMapping;
  }

  public FrmBillImageMapping getForm() {
    return frmBillImageMapping;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidBillImageMapping) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidBillImageMapping != 0) {
          try {
            entBillImageMapping = PstBillImageMapping.fetchExc(oidBillImageMapping);
          } catch (Exception exc) {
          }
        }

        frmBillImageMapping.requestEntityObject(entBillImageMapping);

        if (frmBillImageMapping.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entBillImageMapping.getOID() == 0) {
          try {
            long oid = pstBillImageMapping.insertExc(this.entBillImageMapping);
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
            long oid = pstBillImageMapping.updateExc(this.entBillImageMapping);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidBillImageMapping != 0) {
          try {
            entBillImageMapping = PstBillImageMapping.fetchExc(oidBillImageMapping);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidBillImageMapping != 0) {
          try {
            entBillImageMapping = PstBillImageMapping.fetchExc(oidBillImageMapping);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidBillImageMapping != 0) {
          try {
            long oid = PstBillImageMapping.deleteExc(oidBillImageMapping);
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
