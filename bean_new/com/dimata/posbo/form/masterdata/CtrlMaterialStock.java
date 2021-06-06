package com.dimata.posbo.form.masterdata;

/* java package */
import java.util.*;
import javax.servlet.*;
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

public class CtrlMaterialStock extends Control implements I_Language {
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
    private MaterialStock materialStock;
    private PstMaterialStock pstMaterialStock;
    private FrmMaterialStock frmMaterialStock;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlMaterialStock(HttpServletRequest request){
        msgString = "";
        materialStock = new MaterialStock();
        try{
            pstMaterialStock = new PstMaterialStock(0);
        }catch(Exception e){;}
        frmMaterialStock = new FrmMaterialStock(request, materialStock);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmMaterialStock.addError(frmMaterialStock.FRM_FIELD_MATERIAL_STOCK_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public MaterialStock getMaterialStock() { return materialStock; }
    
    public FrmMaterialStock getForm() { return frmMaterialStock; }
    
    public String getMessage(){ return msgString; }
    
    public int getStart() { return start; }
    
    public int action(int cmd , long oidMaterialStock) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd) {
            case Command.ADD :
                break;
                
            case Command.SAVE :
                if(oidMaterialStock != 0) {
                    try {
                        materialStock = PstMaterialStock.fetchExc(oidMaterialStock);
                    }
                    catch(Exception exc) {
                    }
                }
                
                frmMaterialStock.requestEntityObject(materialStock);
                
                if(frmMaterialStock.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                if(materialStock.getOID()==0) {
                    try {
                        long oid = pstMaterialStock.insertExc(this.materialStock);
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
                        long oid = pstMaterialStock.updateExc(this.materialStock);
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
                if (oidMaterialStock != 0) {
                    try {
                        materialStock = PstMaterialStock.fetchExc(oidMaterialStock);
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
                if (oidMaterialStock != 0) {
                    try {
                        materialStock = PstMaterialStock.fetchExc(oidMaterialStock);
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
                if (oidMaterialStock != 0) {
                    try {
                        long oid = PstMaterialStock.deleteExc(oidMaterialStock);
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
