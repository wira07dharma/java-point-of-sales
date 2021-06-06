/*
 * CtrlAccPayableDetail.java
 *
 * Created on May 5, 2007, 12:56 PM
 */

package com.dimata.posbo.form.arap;

/* java package */
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
/* project package */
import com.dimata.posbo.entity.arap.AccPayableDetail;
import com.dimata.posbo.entity.arap.PstAccPayableDetail;
/**
 *
 * @author  gwawan
 */
public class CtrlAccPayableDetail extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Code Material Unit sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Material Unit Code already exist", "Data incomplete"}
    };
    
    private int start;
    private String msgString;
    private AccPayableDetail accPayableDetail;
    private PstAccPayableDetail pstAccPayableDetail;
    private FrmAccPayableDetail frmAccPayableDetail;
    int language = LANGUAGE_FOREIGN;
    
    /** Creates a new instance of CtrlAccPayableDetail */
    public CtrlAccPayableDetail() {
    }
    
    public CtrlAccPayableDetail(HttpServletRequest request) {
        msgString = "";
        accPayableDetail = new AccPayableDetail();
        try{
            pstAccPayableDetail = new PstAccPayableDetail(0);
        } catch(Exception e) {
            ;
        }
        frmAccPayableDetail = new FrmAccPayableDetail(request, accPayableDetail);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmAccPayableDetail.addError(frmAccPayableDetail.FRM_ACC_PAYABLE_DETAIL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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
        return this.language;
    }
    
    public void setLanguage(int language) {
        this.language = language;
    }
    
    public AccPayableDetail getAccPayableDetail() {
        return this.accPayableDetail;
    }
    
    public void setAccPayableDetail(AccPayableDetail accPayableDetail) {
        this.accPayableDetail = accPayableDetail;
    }
    
    public FrmAccPayableDetail getForm() {
        return this.frmAccPayableDetail;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
    public int action(int cmd, long oidObj) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                if(oidObj != 0) {
                    try {
                        this.accPayableDetail = PstAccPayableDetail.fetchExc(oidObj);
                    } catch(Exception e) {
                    }
                }
                long oidAP = accPayableDetail.getAccPayableId();
                frmAccPayableDetail.requestEntityObject(accPayableDetail);
                if(frmAccPayableDetail.errorSize() > 0) {
                    msgString = FRMMessage.getMessage(FRMMessage.MSG_INCOMPLATE);
                    //return RSLT_FORM_INCOMPLETE;
                }
                System.out.println(">>>oidAP: "+oidAP);
                if(accPayableDetail.getOID() == 0) {
                    try {
                        this.accPayableDetail.setAccPayableId(oidAP);
                        long oid = pstAccPayableDetail.insertExc(this.accPayableDetail);
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
                        long oid = pstAccPayableDetail.updateExc(this.accPayableDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.EDIT:
                if (oidObj != 0) {
                    try {
                        accPayableDetail = PstAccPayableDetail.fetchExc(oidObj);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.ASK:
                if (oidObj != 0) {
                    try {
                        accPayableDetail = PstAccPayableDetail.fetchExc(oidObj);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.DELETE:
                if (oidObj != 0) {
                    try {
                        long oid = PstAccPayableDetail.deleteExc(oidObj);
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
        }
        return rsCode;
    }
    
}
