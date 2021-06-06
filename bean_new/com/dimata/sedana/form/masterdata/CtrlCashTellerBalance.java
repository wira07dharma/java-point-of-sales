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
import com.dimata.sedana.entity.masterdata.CashTellerBalance;
import com.dimata.sedana.entity.masterdata.PstCashTellerBalance;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import javax.servlet.http.*;

/*
 Description : Controll CashTellerBalance
 Date : Fri Aug 18 2017
 Author : Regen
 */
public class CtrlCashTellerBalance extends Control implements I_Language {

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
  private CashTellerBalance entCashTellerBalance;
  private PstCashTellerBalance pstCashTellerBalance;
  private FrmCashTellerBalance frmCashTellerBalance;
  int language = LANGUAGE_DEFAULT;

  public CtrlCashTellerBalance(HttpServletRequest request) {
    msgString = "";
    entCashTellerBalance = new CashTellerBalance();
    try {
      pstCashTellerBalance = new PstCashTellerBalance(0);
    } catch (Exception e) {;
    }
    frmCashTellerBalance = new FrmCashTellerBalance(request, entCashTellerBalance);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmCashTellerBalance.addError(frmCashTellerBalance.FRM_FIELD_CASHBALANCEID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public CashTellerBalance getCashTellerBalance() {
    return entCashTellerBalance;
  }

  public FrmCashTellerBalance getForm() {
    return frmCashTellerBalance;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidCashTellerBalance) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidCashTellerBalance != 0) {
          try {
            entCashTellerBalance = PstCashTellerBalance.fetchExc(oidCashTellerBalance);
          } catch (Exception exc) {
          }
        }

        frmCashTellerBalance.requestEntityObject(entCashTellerBalance);

        if (frmCashTellerBalance.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entCashTellerBalance.getOID() == 0) {
          try {
            long oid = pstCashTellerBalance.insertExc(this.entCashTellerBalance);
            msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
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
            long oid = pstCashTellerBalance.updateExc(this.entCashTellerBalance);
            msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidCashTellerBalance != 0) {
          try {
            entCashTellerBalance = PstCashTellerBalance.fetchExc(oidCashTellerBalance);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidCashTellerBalance != 0) {
          try {
            entCashTellerBalance = PstCashTellerBalance.fetchExc(oidCashTellerBalance);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidCashTellerBalance != 0) {
          try {
            long oid = PstCashTellerBalance.deleteExc(oidCashTellerBalance);
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
