/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.materialreqlocation;

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.hanoman.entity.outletrsv.materialreqlocation.MaterialReqLocation;
import com.dimata.hanoman.entity.outletrsv.materialreqlocation.PstMaterialReqLocation;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 Description : Controll MaterialReqLocation
 Date : Tue Feb 21 2017
 Author : Dewa
 */
public class CtrlMaterialReqLocation extends Control implements I_Language {

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
  private MaterialReqLocation entMaterialReqLocation;
  private MaterialReqLocation tmpMaterialReqLocation = new MaterialReqLocation();
  private PstMaterialReqLocation pstMaterialReqLocation;
  private FrmMaterialReqLocation frmMaterialReqLocation;
  private long MaterialReqLocationId = 0;
  int language = LANGUAGE_DEFAULT;

  public CtrlMaterialReqLocation(HttpServletRequest request) {
    msgString = "";
    entMaterialReqLocation = new MaterialReqLocation();
    try {
      pstMaterialReqLocation = new PstMaterialReqLocation(0);
    } catch (Exception e) {;
    }
    frmMaterialReqLocation = new FrmMaterialReqLocation(request, entMaterialReqLocation);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmMaterialReqLocation.addError(frmMaterialReqLocation.FRM_FIELD_MATERIAL_REQ_LOCATION_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public MaterialReqLocation getMaterialReqLocation() {
    return entMaterialReqLocation;
  }

  public FrmMaterialReqLocation getForm() {
    return frmMaterialReqLocation;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidMaterialReqLocation) {
    if (oidMaterialReqLocation != 0) {
      try {
        tmpMaterialReqLocation = PstMaterialReqLocation.fetchExc(oidMaterialReqLocation);
      } catch (DBException ex) {
        Logger.getLogger(CtrlMaterialReqLocation.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidMaterialReqLocation != 0) {
          try {
            entMaterialReqLocation = PstMaterialReqLocation.fetchExc(oidMaterialReqLocation);
          } catch (Exception exc) {
          }
        }

        frmMaterialReqLocation.requestEntityObject(entMaterialReqLocation);

        if (frmMaterialReqLocation.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entMaterialReqLocation.getOID() == 0) {
          try {
            long oid = pstMaterialReqLocation.insertExc(this.entMaterialReqLocation);
            this.logDocId = oid;
            this.logUserAction = "Save";
            this.logDetails = MaterialReqLocation.getLog(entMaterialReqLocation);
            setMaterialReqLocationId(oid);
          } catch (DBException dbexc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
            return getControlMsgId(excCode);
          } catch (Exception exc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
            return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
          }

        } else {
          try {
            long oid = pstMaterialReqLocation.updateExc(this.entMaterialReqLocation);
            this.logDocId = oid;
            this.logUserAction = "Update";
            this.logDetails = MaterialReqLocation.getLog(tmpMaterialReqLocation, entMaterialReqLocation);
          } catch (DBException dbexc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidMaterialReqLocation != 0) {
          try {
            entMaterialReqLocation = PstMaterialReqLocation.fetchExc(oidMaterialReqLocation);
          } catch (DBException dbexc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidMaterialReqLocation != 0) {
          try {
            entMaterialReqLocation = PstMaterialReqLocation.fetchExc(oidMaterialReqLocation);
          } catch (DBException dbexc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidMaterialReqLocation != 0) {
          try {
            this.logDetails = MaterialReqLocation.getLog(tmpMaterialReqLocation);
            long oid = PstMaterialReqLocation.deleteExc(oidMaterialReqLocation);
            this.logDocId = oid;
            this.logUserAction = "Delete";
            if (oid != 0) {
              msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
              excCode = RSLT_OK;
            } else {
              msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
              excCode = RSLT_FORM_INCOMPLETE;
            }
          } catch (DBException dbexc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            rsCode = RSLT_UNKNOWN_ERROR;
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      default:

    }
    return rsCode;
  }

  /**
   * @return the MaterialReqLocationId
   */
  public long getMaterialReqLocationId() {
    return MaterialReqLocationId;
  }

  /**
   * @param MaterialReqLocationId the MaterialReqLocationId to set
   */
  public void setMaterialReqLocationId(long MaterialReLocationId) {
    this.MaterialReqLocationId = MaterialReLocationId;
  }

  public long logUserId = 0;
  public String logUsername = "";
  public String logApp = "Prochain"; //
  public String logUrl = ""; //
  public String logDocType = "Master Required Location"; //
  public String logUserAction = ""; //
  public String logDocNumber = "01"; //
  public long logDocId = 0; //
  public String logDetails = ""; //

  public void setLog() {
    try {
      Date dateH = new Date();
      LogSysHistory logSysHistory = new LogSysHistory();
      logSysHistory.setLogUserId(this.logUserId);
      logSysHistory.setLogLoginName(this.logUsername);
      logSysHistory.setLogApplication(this.logApp);
      logSysHistory.setLogOpenUrl(this.logUrl);
      logSysHistory.setLogUpdateDate(dateH);
      logSysHistory.setLogDocumentType(this.logDocType);
      logSysHistory.setLogUserAction(this.logUserAction);
      logSysHistory.setLogDocumentNumber(this.logDocNumber);
      logSysHistory.setLogDocumentId(this.logDocId);
      logSysHistory.setLogDetail(this.logDetails);
      PstLogSysHistory.insertLog(logSysHistory);
    } catch (DBException ex) {
      Logger.getLogger(CtrlMaterialReqLocation.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
