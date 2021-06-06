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
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.sedana.entity.masterdata.*;

/*
Description : Controll JangkaWaktu
Date : Mon Nov 11 2019
Author : WiraDharma
 */
public class CtrlJangkaWaktu extends Control implements I_Language {

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
  private JangkaWaktu entJangkaWaktu;
  private PstJangkaWaktu pstJangkaWaktu;
  private FrmJangkaWaktu frmJangkaWaktu;
  int language = LANGUAGE_DEFAULT;

  public CtrlJangkaWaktu(HttpServletRequest request) {
    msgString = "";
    entJangkaWaktu = new JangkaWaktu();
    try {
      pstJangkaWaktu = new PstJangkaWaktu(0);
    } catch (Exception e) {;
    }
    frmJangkaWaktu = new FrmJangkaWaktu(request, entJangkaWaktu);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmJangkaWaktu.addError(frmJangkaWaktu.FRM_FIELD_JANGKA_WAKTU_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public JangkaWaktu getJangkaWaktu() {
    return entJangkaWaktu;
  }

  public FrmJangkaWaktu getForm() {
    return frmJangkaWaktu;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidJangkaWaktu) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidJangkaWaktu != 0) {
          try {
            entJangkaWaktu = PstJangkaWaktu.fetchExc(oidJangkaWaktu);
          } catch (Exception exc) {
          }
        }

        frmJangkaWaktu.requestEntityObject(entJangkaWaktu);

        if (frmJangkaWaktu.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entJangkaWaktu.getOID() == 0) {
          try {
            long oid = pstJangkaWaktu.insertExc(this.entJangkaWaktu);
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
            long oid = pstJangkaWaktu.updateExc(this.entJangkaWaktu);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidJangkaWaktu != 0) {
          try {
            entJangkaWaktu = PstJangkaWaktu.fetchExc(oidJangkaWaktu);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidJangkaWaktu != 0) {
          try {
            entJangkaWaktu = PstJangkaWaktu.fetchExc(oidJangkaWaktu);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidJangkaWaktu != 0) {
          try {
            long oid = PstJangkaWaktu.deleteExc(oidJangkaWaktu);
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
