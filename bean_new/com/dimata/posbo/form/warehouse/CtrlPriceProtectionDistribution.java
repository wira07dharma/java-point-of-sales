/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;


import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.warehouse.PriceProtectionDistribution;
import com.dimata.posbo.entity.warehouse.PstPriceProtection;
import com.dimata.posbo.entity.warehouse.PstPriceProtectionDistribution;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dimata005
 */
public class CtrlPriceProtectionDistribution extends Control implements I_Language {
    
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
    private PriceProtectionDistribution priceProtectionDistribution;
    private PstPriceProtectionDistribution pstPriceProtectionDistribution;
    private FrmPriceProtectionDistribution frmPriceProtectionDistribution;
    private HttpServletRequest req;
    Date dateLog = new  Date();
    int language = LANGUAGE_DEFAULT;

    public CtrlPriceProtectionDistribution() {
    }
    
    public CtrlPriceProtectionDistribution(HttpServletRequest request) {
        msgString = "";
        priceProtectionDistribution = new PriceProtectionDistribution();
        try {
            pstPriceProtectionDistribution = new PstPriceProtectionDistribution(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmPriceProtectionDistribution = new FrmPriceProtectionDistribution(request, priceProtectionDistribution);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPriceProtectionDistribution.addError(frmPriceProtectionDistribution.FRM_FLD_POS_PRICE_PROTECTION_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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
    
    public PriceProtectionDistribution getPriceProtectionItem() {
        return priceProtectionDistribution;
    }
    
    public FrmPriceProtectionDistribution getForm() {
        return frmPriceProtectionDistribution;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
    public int action(int cmd, long oidPriceProtectionItem, long oidPriceProtection, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD :
                 break;
            case Command.SAVE :
                if(oidPriceProtectionItem != 0){
                    try{
                            priceProtectionDistribution = PstPriceProtectionDistribution.fetchExc(oidPriceProtectionItem);
                    }catch(Exception exc){
                    }
                }

                frmPriceProtectionDistribution.requestEntityObject(priceProtectionDistribution);

                if(frmPriceProtectionDistribution.errorSize()>0) {
                        msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                        return RSLT_FORM_INCOMPLETE ;
                }

                if(priceProtectionDistribution.getOID()==0){
                        try{
                                long oid = pstPriceProtectionDistribution.insertExc(this.priceProtectionDistribution);
                                
                                 //update amount totalnya
                                //cek sum
                                String whereClause=pstPriceProtectionDistribution.fieldNames[pstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID]+"="+priceProtectionDistribution.getPriceProtectionId();
                                double sumTotal= pstPriceProtectionDistribution.getSum(whereClause);
                                try{
                                    PstPriceProtection.updateTotAmount(priceProtectionDistribution.getPriceProtectionId(), sumTotal);
                                }catch(Exception ex){
                                
                                }       
                        }catch (Exception exc){
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                        }

                }else{
                        try {
                                long oid = pstPriceProtectionDistribution.updateExc(this.priceProtectionDistribution);
                                String whereClause=pstPriceProtectionDistribution.fieldNames[pstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID]+"="+priceProtectionDistribution.getPriceProtectionId();
                                double sumTotal= pstPriceProtectionDistribution.getSum(whereClause);
                                try{
                                    PstPriceProtection.updateTotAmount(priceProtectionDistribution.getPriceProtectionId(), sumTotal);
                                }catch(Exception ex){
                                
                                }
                        }catch (Exception exc){
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
                        }

                }
                break;
            case Command.EDIT :
                if (oidPriceProtectionItem != 0) {
                        try {
                                priceProtectionDistribution = pstPriceProtectionDistribution.fetchExc(oidPriceProtectionItem);
                        } catch (Exception exc){ 
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                }
                break;
                
            case Command.ASK :
                if (oidPriceProtectionItem != 0) {
                        try {
                                priceProtectionDistribution = pstPriceProtectionDistribution.fetchExc(oidPriceProtectionItem);
                        } catch (Exception exc){ 
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                }
                break;

            case Command.DELETE :
                if (oidPriceProtectionItem != 0){
                        try{
                                long oid = pstPriceProtectionDistribution.deleteExc(oidPriceProtectionItem);
                                String whereClause = pstPriceProtectionDistribution.fieldNames[pstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID]+"="+oidPriceProtection;
                                double sumTotal = pstPriceProtectionDistribution.getSum(whereClause);
                                try{
                                    PstPriceProtection.updateTotAmount(oidPriceProtection, sumTotal);
                                }catch(Exception ex){
                                
                                }
                                if(oid!=0){
                                        msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                        excCode = RSLT_OK;
                                }else{
                                        msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                        excCode = RSLT_FORM_INCOMPLETE;
                                }
                        }catch(Exception exc){	
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                }
                break;

            default :
        }
        return rsCode;
    }
    
}
