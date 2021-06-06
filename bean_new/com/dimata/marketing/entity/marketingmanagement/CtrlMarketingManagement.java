/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingmanagement;

import static com.dimata.marketing.form.marketingpromotion.CtrlMarketingPromotion.RSLT_UNKNOWN_ERROR;
import com.dimata.posbo.entity.marketing.MarketingManagement;
import com.dimata.posbo.entity.marketing.PstMarketingManagement;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Sunima
 */
public class CtrlMarketingManagement extends Control implements I_Language{
    
    public static int RSLT_OK = 0;
    public static int RSLT_UNKOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
};
     private int start;
    private String msgString;
    private MarketingManagement entMarketingManagement;
    private PstMarketingManagement pstMarketingManagement;
    private FrmMarketingManagement frmMarketingManagement;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlMarketingManagement(HttpServletRequest request){
        msgString = "";
        entMarketingManagement = new MarketingManagement();
        try{
            pstMarketingManagement = new PstMarketingManagement(0);
            
        }catch(Exception e){
        
        }
        frmMarketingManagement = new FrmMarketingManagement(request, entMarketingManagement);
    }
    
    private String getSystemMessage(int msgCode){
        switch(msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMarketingManagement.addError(frmMarketingManagement.FRM_FIELD_MARKETING_MANAGEMENT_ID, resultText[language][RSLT_EST_CODE_EXIST]);
                return resultText[language][RSLT_EST_CODE_EXIST];
                
            default:
                return resultText[language][RSLT_UNKOWN_ERROR];
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
    
    public MarketingManagement getMarketingManagement() {
        return entMarketingManagement;
    }
    
    public FrmMarketingManagement getForm() {
        return frmMarketingManagement;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
 public int action(int cmd, long oidMarketingManagement, String oidDelete) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMarketingManagement != 0) {
                    try {
                        entMarketingManagement = PstMarketingManagement.fetchExc(oidMarketingManagement);
                    } catch (Exception exc) {
                    }
                }

                frmMarketingManagement.requestEntityObject(entMarketingManagement);

                if (frmMarketingManagement.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entMarketingManagement.getOID() == 0) {
                    try {
                        long oid = pstMarketingManagement.insertExc(this.entMarketingManagement);
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
                        long oid = pstMarketingManagement.updateExc(this.entMarketingManagement);
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
                if (oidMarketingManagement != 0) {
                    try {
                        entMarketingManagement = PstMarketingManagement.fetchExc(oidMarketingManagement);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMarketingManagement != 0) {
                    try {
                        entMarketingManagement = PstMarketingManagement.fetchExc(oidMarketingManagement);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMarketingManagement != 0) {
                    try {
                        long oid = PstMarketingManagement.deleteExc(oidMarketingManagement);
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
    
