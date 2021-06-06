/*
 * CtrlForwarderInfo.java
 *
 * Created on June 4, 2007, 10:44 AM
 */

package com.dimata.posbo.form.warehouse;

/** java package */
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;

/** qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;

/** project package */
import com.dimata.posbo.entity.warehouse.ForwarderInfo;
import com.dimata.posbo.entity.warehouse.PstForwarderInfo;
import com.dimata.posbo.session.warehouse.SessForwarderInfo;

/**
 *
 * @author  gwawan
 */
public class CtrlForwarderInfo extends Control implements I_Language {
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
    private ForwarderInfo forwarderInfo;
    private PstForwarderInfo pstForwarderInfo;
    private FrmForwarderInfo frmForwarderInfo;
    int language = LANGUAGE_FOREIGN;
    
    /** Creates a new instance of CtrlForwarderInfo */
    public CtrlForwarderInfo() {
    }
    
    public CtrlForwarderInfo(HttpServletRequest request) {
        msgString = "";
        this.forwarderInfo = new ForwarderInfo();
        try {
            this.pstForwarderInfo = new PstForwarderInfo(0);
        }
        catch(Exception e) {
            ;
        }
        this.frmForwarderInfo = new FrmForwarderInfo(request, forwarderInfo);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmForwarderInfo.addError(this.frmForwarderInfo.FRM_FIELD_FORWARDER_INFO_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ForwarderInfo getUnit() {
        return this.forwarderInfo;
    }

    public FrmForwarderInfo getForm() {
        return this.frmForwarderInfo;
    }

    public String getMessage() {
        return this.msgString;
    }

    public int getStart() {
        return this.start;
    }
    
    public ForwarderInfo getForwarderInfo() {
        return this.forwarderInfo;
    }

    public int action(int cmd, long oidForwarderInfo) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                int counter = 0;
                Date date = new Date();
                if (oidForwarderInfo != 0) {
                    try {
                        forwarderInfo = PstForwarderInfo.fetchExc(oidForwarderInfo);
                        date = forwarderInfo.getDocDate();
                        counter = forwarderInfo.getCounter();
                    }
                    catch (Exception exc) {
                    }
                }
                
                frmForwarderInfo.requestEntityObject(this.forwarderInfo);
                System.out.println("NOTES >>> "+forwarderInfo.getNotes());
                
                if (frmForwarderInfo.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                if (forwarderInfo.getOID() == 0) {
                    try {
                        forwarderInfo.setCounter(SessForwarderInfo.getMaxCounter(forwarderInfo, date, oidForwarderInfo, counter));
                        forwarderInfo.setDocNumber(SessForwarderInfo.getDocCode(forwarderInfo));
                        long oid = pstForwarderInfo.insertExc(forwarderInfo);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }
                else {
                    try {
                        long oid = pstForwarderInfo.updateExc(this.forwarderInfo);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidForwarderInfo != 0) {
                    try {
                        forwarderInfo = PstForwarderInfo.fetchExc(oidForwarderInfo);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidForwarderInfo != 0) {
                    try {
                        forwarderInfo = PstForwarderInfo.fetchExc(oidForwarderInfo);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidForwarderInfo != 0) {
                    try {
                        long oid = 0;
                        if (oidForwarderInfo != 0) {
                            oid = PstForwarderInfo.deleteExc(oidForwarderInfo);
                        }
                        
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }
                        else {
                            forwarderInfo = PstForwarderInfo.fetchExc(oidForwarderInfo);
                            frmForwarderInfo.addError(FrmForwarderInfo.FRM_FIELD_FORWARDER_INFO_ID, "");
                            FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default :

        }
        return excCode;
    }
    
    public int action2(int cmd, long oidForwarderInfo,HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                int counter = 0;
                Date date = new Date();
                if (oidForwarderInfo != 0) {
                    try {
                        forwarderInfo = PstForwarderInfo.fetchExc(oidForwarderInfo);
                        date = forwarderInfo.getDocDate();
                        counter = forwarderInfo.getCounter();
                    }
                    catch (Exception exc) {
                    }
                }
                
                frmForwarderInfo.requestEntityObject(this.forwarderInfo);
                if (this.forwarderInfo.getDocDate()==null){
                    Date tgl = FRMQueryString.requestDate(request, ""+FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_DOC_DATE]+"");
                    this.forwarderInfo.setDocDate(tgl);
                }
                System.out.println("NOTES >>> "+forwarderInfo.getNotes());
                
                if (frmForwarderInfo.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                if (forwarderInfo.getOID() == 0) {
                    try {
                        forwarderInfo.setCounter(SessForwarderInfo.getMaxCounter(forwarderInfo, date, oidForwarderInfo, counter));
                        forwarderInfo.setDocNumber(SessForwarderInfo.getDocCode(forwarderInfo));
                        long oid = pstForwarderInfo.insertExc(forwarderInfo);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }
                else {
                    try {
                        long oid = pstForwarderInfo.updateExc(this.forwarderInfo);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidForwarderInfo != 0) {
                    try {
                        forwarderInfo = PstForwarderInfo.fetchExc(oidForwarderInfo);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidForwarderInfo != 0) {
                    try {
                        forwarderInfo = PstForwarderInfo.fetchExc(oidForwarderInfo);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidForwarderInfo != 0) {
                    try {
                        long oid = 0;
                        if (oidForwarderInfo != 0) {
                            oid = PstForwarderInfo.deleteExc(oidForwarderInfo);
                        }
                        
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }
                        else {
                            forwarderInfo = PstForwarderInfo.fetchExc(oidForwarderInfo);
                            frmForwarderInfo.addError(FrmForwarderInfo.FRM_FIELD_FORWARDER_INFO_ID, "");
                            FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default :

        }
        return excCode;
    }
    
}
 