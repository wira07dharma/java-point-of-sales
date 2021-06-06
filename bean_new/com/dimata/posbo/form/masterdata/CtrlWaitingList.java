/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/* java package */

import com.dimata.gui.jsp.ControlDate;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
/* project package */

import com.dimata.posbo.entity.masterdata.*;
import java.util.Date;

public class CtrlWaitingList extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "Code Material WaitingList sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Material WaitingList Code already exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private WaitingList waitingList;
    private PstWaitingList PstWaitingList;
    private FrmWaitingList frmWaitingList;
    private HttpServletRequest req;
    int language = LANGUAGE_FOREIGN;

    public CtrlWaitingList(HttpServletRequest request) {
        msgString = "";
        waitingList = new WaitingList();
        try {
            PstWaitingList = new PstWaitingList(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmWaitingList = new FrmWaitingList(request, waitingList);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmWaitingList.addError(frmWaitingList.FRM_CUSTOMER_WAITING_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public WaitingList getWaitingList() {
        return waitingList;
    }

    public FrmWaitingList getForm() {
        return frmWaitingList;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidCustomer) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
            case Command.UPDATE:
                if (oidCustomer != 0) {
                    try {
                        waitingList = PstWaitingList.fetchExc(oidCustomer);
                        Date now = new Date();
                        waitingList.setRealTime(now);
                        waitingList.setStatus(1);
                        try {
                            long oid = PstWaitingList.updateExc(this.waitingList);
                        } catch (DBException dbexc) {
                            excCode = dbexc.getErrorCode();
                            msgString = getSystemMessage(excCode);
                        } catch (Exception exc) {
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                    } catch (Exception exc) {
                    }
                }
                break;
            case Command.SAVE:
                String merkOldName = "";
                if (oidCustomer != 0) {
                    try {
                        waitingList = PstWaitingList.fetchExc(oidCustomer);
                        merkOldName = waitingList.getCustomerName();
                    } catch (Exception exc) {
                    }
                }

                frmWaitingList.requestEntityObject(waitingList);
                Date dateStart = ControlDate.getDateTime(frmWaitingList.fieldNames[FrmWaitingList.FRM_START_TIME], req);
                waitingList.setStartTime(dateStart);
                
                Date dateEnd = ControlDate.getDateTime(frmWaitingList.fieldNames[FrmWaitingList.FRM_END_TIME], req);
                waitingList.setEndTime(dateEnd);
                
                if (frmWaitingList.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (waitingList.getOID() == 0) {
                    try {
                        long oid = PstWaitingList.insertExc(this.waitingList);
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
                        long oid = PstWaitingList.updateExc(this.waitingList);


                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidCustomer != 0) {
                    try {
                        waitingList = PstWaitingList.fetchExc(oidCustomer);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidCustomer != 0) {
                    try {
                        waitingList = PstWaitingList.fetchExc(oidCustomer);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidCustomer != 0) {
                    try {
                        long oid = 0;
                        oid = PstWaitingList.deleteExc(oidCustomer);
//                        if(SessMerk.readyDataToDelete(oidMerk)){
//                            oid = PstWaitingList.deleteExc(oidMerk);
//                        }
//                        if (oid != 0) {
//                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
//                            excCode = RSLT_OK;
//                        } else {
//                            waitingList = PstWaitingList.fetchExc(oidMerk);
//                            frmWaitingList.addError(FrmWaitingList.FRM_FIELD_MERK_ID,"");
//                            msgString = "Hapus data gagal,data masih digunakan oleh data lain."; // FRMMessage.getMessage(FRMMessage.ERR_DELETED);
//                            excCode = RSLT_FORM_INCOMPLETE;
//                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default :

        }
        return excCode;
    }
}

