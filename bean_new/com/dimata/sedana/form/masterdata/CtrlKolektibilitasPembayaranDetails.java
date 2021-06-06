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
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.sedana.entity.masterdata.KolektibilitasPembayaranDetails;
import com.dimata.sedana.entity.masterdata.PstKolektibilitasPembayaranDetails;

public class CtrlKolektibilitasPembayaranDetails extends Control implements I_Language {

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
  private KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails;
  private PstKolektibilitasPembayaranDetails pstKolektibilitasPembayaranDetails;
  private FrmKolektibilitasPembayaranDetails frmKolektibilitasPembayaranDetails;
  int language = LANGUAGE_DEFAULT;

  public CtrlKolektibilitasPembayaranDetails(HttpServletRequest request) {
    msgString = "";
    entKolektibilitasPembayaranDetails = new KolektibilitasPembayaranDetails();
    try {
      pstKolektibilitasPembayaranDetails = new PstKolektibilitasPembayaranDetails(0);
    } catch (Exception e) {
    }
    frmKolektibilitasPembayaranDetails = new FrmKolektibilitasPembayaranDetails(request, entKolektibilitasPembayaranDetails);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmKolektibilitasPembayaranDetails.addError(frmKolektibilitasPembayaranDetails.FRM_FIELD_KOLEKTIBILITASDETAILID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public KolektibilitasPembayaranDetails getKolektibilitasPembayaranDetails() {
    return entKolektibilitasPembayaranDetails;
  }

  public FrmKolektibilitasPembayaranDetails getForm() {
    return frmKolektibilitasPembayaranDetails;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidKolektibilitasPembayaranDetails) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidKolektibilitasPembayaranDetails != 0) {
          try {
            entKolektibilitasPembayaranDetails = PstKolektibilitasPembayaranDetails.fetchExc(oidKolektibilitasPembayaranDetails);
          } catch (Exception exc) {
          }
        }

        frmKolektibilitasPembayaranDetails.requestEntityObject(entKolektibilitasPembayaranDetails);

        if (frmKolektibilitasPembayaranDetails.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entKolektibilitasPembayaranDetails.getOID() == 0) {
          try {
            long oid = pstKolektibilitasPembayaranDetails.insertExc(this.entKolektibilitasPembayaranDetails);
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
            long oid = pstKolektibilitasPembayaranDetails.updateExc(this.entKolektibilitasPembayaranDetails);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidKolektibilitasPembayaranDetails != 0) {
          try {
            entKolektibilitasPembayaranDetails = PstKolektibilitasPembayaranDetails.fetchExc(oidKolektibilitasPembayaranDetails);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidKolektibilitasPembayaranDetails != 0) {
          try {
            entKolektibilitasPembayaranDetails = PstKolektibilitasPembayaranDetails.fetchExc(oidKolektibilitasPembayaranDetails);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidKolektibilitasPembayaranDetails != 0) {
          try {
            long oid = PstKolektibilitasPembayaranDetails.deleteExc(oidKolektibilitasPembayaranDetails);
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
