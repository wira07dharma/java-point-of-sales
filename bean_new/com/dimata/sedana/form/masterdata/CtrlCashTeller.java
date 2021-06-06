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
import com.dimata.sedana.entity.masterdata.CashTeller;
import com.dimata.sedana.entity.masterdata.CashTellerBalance;
import com.dimata.sedana.entity.masterdata.PstCashTeller;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import java.util.Date;
import javax.servlet.http.*;

public class CtrlCashTeller extends Control implements I_Language {

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
  private CashTeller entCashTeller;
  private PstCashTeller pstCashTeller;
  private FrmCashTeller frmCashTeller;
  int language = LANGUAGE_DEFAULT;

  public CtrlCashTeller(HttpServletRequest request) {
    msgString = "";
    entCashTeller = new CashTeller();
    try {
      pstCashTeller = new PstCashTeller(0);
    } catch (Exception e) {;
    }
    frmCashTeller = new FrmCashTeller(request, entCashTeller);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmCashTeller.addError(frmCashTeller.FRM_FIELD_TELLER_SHIFT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public CashTeller getCashTeller() {
    return entCashTeller;
  }

  public FrmCashTeller getForm() {
    return frmCashTeller;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidCashTeller) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidCashTeller != 0) {
          try {
            entCashTeller = PstCashTeller.fetchExc(oidCashTeller);
          } catch (Exception exc) {
          }
        }

        frmCashTeller.requestEntityObject(entCashTeller);

        if (frmCashTeller.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entCashTeller.getOID() == 0) {
          try {
            long oid = pstCashTeller.insertExc(this.entCashTeller);            
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
            return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
          }
        } else {
          try {
            long oid = pstCashTeller.updateExc(this.entCashTeller);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidCashTeller != 0) {
          try {
            entCashTeller = PstCashTeller.fetchExc(oidCashTeller);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidCashTeller != 0) {
          try {
            entCashTeller = PstCashTeller.fetchExc(oidCashTeller);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidCashTeller != 0) {
          try {
            long oid = PstCashTeller.deleteExc(oidCashTeller);
            if (oid != 0) {
              msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
              excCode = RSLT_OK;
            } else {
              msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
              excCode = RSLT_FORM_INCOMPLETE;
            }
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
