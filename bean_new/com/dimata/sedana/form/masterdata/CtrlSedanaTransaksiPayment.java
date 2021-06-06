/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.harisma.entity.masterdata.*; 
import com.dimata.qdep.db.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.sedana.entity.masterdata.PstTransaksiPayment;
import com.dimata.sedana.entity.masterdata.TransaksiPayment;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import javax.servlet.http.*;

public class CtrlSedanaTransaksiPayment extends Control implements I_Language {

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
  private TransaksiPayment entSedanaTransaksiPayment;
  private PstTransaksiPayment pstSedanaTransaksiPayment;
  private FrmSedanaTransaksiPayment frmSedanaTransaksiPayment;
  int language = LANGUAGE_DEFAULT;

  public CtrlSedanaTransaksiPayment(HttpServletRequest request) {
    msgString = "";
    entSedanaTransaksiPayment = new TransaksiPayment();
    try {
      pstSedanaTransaksiPayment = new PstTransaksiPayment(0);
    } catch (Exception e) {;
    }
    frmSedanaTransaksiPayment = new FrmSedanaTransaksiPayment(request, entSedanaTransaksiPayment);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmSedanaTransaksiPayment.addError(frmSedanaTransaksiPayment.FRM_FIELD_TRANSAKSI_PAYMENT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public TransaksiPayment getSedanaTransaksiPayment() {
    return entSedanaTransaksiPayment;
  }

  public FrmSedanaTransaksiPayment getForm() {
    return frmSedanaTransaksiPayment;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidSedanaTransaksiPayment) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidSedanaTransaksiPayment != 0) {
          try {
            entSedanaTransaksiPayment = PstTransaksiPayment.fetchExc(oidSedanaTransaksiPayment);
          } catch (Exception exc) {
          }
        }

        frmSedanaTransaksiPayment.requestEntityObject(entSedanaTransaksiPayment);

        if (frmSedanaTransaksiPayment.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entSedanaTransaksiPayment.getOID() == 0) {
          try {
            long oid = pstSedanaTransaksiPayment.insertExc(this.entSedanaTransaksiPayment);
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
            long oid = pstSedanaTransaksiPayment.updateExc(this.entSedanaTransaksiPayment);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidSedanaTransaksiPayment != 0) {
          try {
            entSedanaTransaksiPayment = PstTransaksiPayment.fetchExc(oidSedanaTransaksiPayment);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidSedanaTransaksiPayment != 0) {
          try {
            entSedanaTransaksiPayment = PstTransaksiPayment.fetchExc(oidSedanaTransaksiPayment);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidSedanaTransaksiPayment != 0) {
          try {
            long oid = PstTransaksiPayment.deleteExc(oidSedanaTransaksiPayment);
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
