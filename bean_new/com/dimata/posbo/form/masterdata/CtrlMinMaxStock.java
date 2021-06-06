package com.dimata.posbo.form.masterdata;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;

import javax.servlet.http.HttpServletRequest;

public class CtrlMinMaxStock extends Control implements I_Language {
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
    private MinMaxStock minMaxStock;
    private PstMinMaxStock pstMinMaxStock;
    private FrmMinMaxStock frmMinMaxStock;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlMinMaxStock(HttpServletRequest request){
        msgString = "";
        minMaxStock = new MinMaxStock();
        try{
            pstMinMaxStock = new PstMinMaxStock(0);
        }catch(Exception e){;}
        frmMinMaxStock = new FrmMinMaxStock(request, minMaxStock);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmMinMaxStock.addError(frmMinMaxStock.FRM_FIELD_MINMAXSTOCK_ID, resultText[language][RSLT_EST_CODE_EXIST] );
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }
    
    public int getLanguage(){ return language; }
    
    public void setLanguage(int language){ this.language = language; }
    
    public MinMaxStock getMinMaxStock() { return minMaxStock; }
    
    public FrmMinMaxStock getForm() { return frmMinMaxStock; }
    
    public String getMessage(){ return msgString; }
    
    public int getStart() { return start; }
    
    public int action(int cmd , long oidMinMaxStock) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd) {
            case Command.ADD :
                break;
                
            case Command.SAVE :
                if(oidMinMaxStock != 0) {
                    try {
                        minMaxStock = PstMinMaxStock.fetchExc(oidMinMaxStock);
                    }
                    catch(Exception exc) {
                    }
                }
                
                frmMinMaxStock.requestEntityObject(minMaxStock);
                
                if(frmMinMaxStock.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                if(minMaxStock.getOID()==0) {
                    try {
                        long oid = pstMinMaxStock.insertExc(this.minMaxStock);
                    }
                    catch(DBException dbexc) {
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
                        long oid = pstMinMaxStock.updateExc(this.minMaxStock);
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
                
            case Command.EDIT :
                if (oidMinMaxStock != 0) {
                    try {
                        minMaxStock = PstMinMaxStock.fetchExc(oidMinMaxStock);
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
                
            case Command.ASK :
                if (oidMinMaxStock != 0) {
                    try {
                        minMaxStock = PstMinMaxStock.fetchExc(oidMinMaxStock);
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
                
            case Command.DELETE :
                if (oidMinMaxStock != 0) {
                    //System.out.println("OID <> 0" + oidMinMaxStock);
                    try {
                        long oid = PstMinMaxStock.deleteExc(oidMinMaxStock);
                        if(oid!=0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }
                        else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }
                    catch(DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch(Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            default :
                
        }
        return rsCode;
    }
}
