/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.db.DBException;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.posbo.entity.warehouse.MaterialStockCode;
import com.dimata.posbo.entity.warehouse.PriceProtection;
import com.dimata.posbo.entity.warehouse.PriceProtectionItem;
import com.dimata.posbo.entity.warehouse.PstMaterialStockCode;
import com.dimata.posbo.entity.warehouse.PstPriceProtection;
import com.dimata.posbo.entity.warehouse.PstPriceProtectionItem;
import com.dimata.posbo.session.warehouse.SessPriceProtection;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dimata005
 */
public class CtrlPriceProtection extends Control implements I_Language {
    
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
    private PriceProtection priceProtection;
    private PriceProtection prevPriceProtection;
    private PstPriceProtection pstPriceProtection;
    private FrmPriceProtection frmPriceProtection;
    private HttpServletRequest req;
    Date dateLog = new  Date();
    int language = LANGUAGE_DEFAULT;

    public CtrlPriceProtection() {
    }
    
    public CtrlPriceProtection(HttpServletRequest request) {
        msgString = "";
        priceProtection = new PriceProtection();
        try {
            pstPriceProtection = new PstPriceProtection(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmPriceProtection = new FrmPriceProtection(request, priceProtection);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmPriceProtection.addError(frmPriceProtection.FRM_FLD_POS_PRICE_PROTECTION_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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
    
    public PriceProtection getPriceProtection() {
        return priceProtection;
    }
    
    public FrmPriceProtection getForm() {
        return frmPriceProtection;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
    public int action(int cmd, long oidPosPrice, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD :
                 break;
            case Command.SAVE :
                if(oidPosPrice != 0){
                        try{
                                priceProtection = PstPriceProtection.fetchExc(oidPosPrice);
                                prevPriceProtection = PstPriceProtection.fetchExc(oidPosPrice);
                        }catch(Exception exc){
                        }
                }

                frmPriceProtection.requestEntityObject(priceProtection);
                Date date = ControlDate.getDateTime(FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_CREATE_DATE], req);
                priceProtection.setDateCreated(date);
                
                if(frmPriceProtection.errorSize()>0) {
                        msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                        return RSLT_FORM_INCOMPLETE ;
                }

                if(priceProtection.getOID()==0){
                        try{
                                //buat count dan no dokument
                                SessPriceProtection sessPriceProtection = new SessPriceProtection();
                                int maxCounter = sessPriceProtection.getMaxPriceProtectionCounter(priceProtection.getDateCreated(), priceProtection);
                                maxCounter = maxCounter + 1;
                                priceProtection.setPpCounter(maxCounter);
                                priceProtection.setNumberPP(sessPriceProtection.generateCostingCode(priceProtection));
                                long oid = pstPriceProtection.insertExc(this.priceProtection);
                                
                               
                        }catch (Exception exc){
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                        }

                }else{
                        try {
                                //cek apakah location nya sama? jika tidak sama maka create no invoice baru
                                if(prevPriceProtection.getLocationId()!=priceProtection.getLocationId()){
                                    SessPriceProtection sessPriceProtection = new SessPriceProtection();
                                    int maxCounter = sessPriceProtection.getMaxPriceProtectionCounter(priceProtection.getDateCreated(), priceProtection);
                                    maxCounter = maxCounter + 1;
                                    priceProtection.setPpCounter(maxCounter);
                                    priceProtection.setNumberPP(sessPriceProtection.generateCostingCode(priceProtection));
                                }
                                
                                long oid = pstPriceProtection.updateExc(this.priceProtection);
                        }catch (Exception exc){
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
                        }

                }
                break;
            case Command.EDIT :
                if (oidPosPrice != 0) {
                        try {
                                priceProtection = pstPriceProtection.fetchExc(oidPosPrice);
                        } catch (Exception exc){ 
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                }
                break;
                
            case Command.ASK :
                if (oidPosPrice != 0) {
                        try {
                                priceProtection = pstPriceProtection.fetchExc(oidPosPrice);
                        } catch (Exception exc){ 
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                }
                break;

            case Command.DELETE :
                if (oidPosPrice != 0){
                        try{
                                long oid = pstPriceProtection.deleteExc(oidPosPrice);
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
                
            case Command.POSTING :
                
                if(oidPosPrice != 0){
                        try{
                                priceProtection = PstPriceProtection.fetchExc(oidPosPrice);
                                prevPriceProtection = PstPriceProtection.fetchExc(oidPosPrice);
                        }catch(Exception exc){
                        }
                }

                frmPriceProtection.requestEntityObject(priceProtection);
                Date datex = ControlDate.getDateTime(FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_CREATE_DATE], req);
                priceProtection.setDateCreated(datex);
                
                if(frmPriceProtection.errorSize()>0) {
                        msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                        return RSLT_FORM_INCOMPLETE ;
                }

                if(priceProtection.getOID()==0){
                        try{
                                //buat count dan no dokument
                                SessPriceProtection sessPriceProtection = new SessPriceProtection();
                                int maxCounter = sessPriceProtection.getMaxPriceProtectionCounter(priceProtection.getDateCreated(), priceProtection);
                                maxCounter = maxCounter + 1;
                                priceProtection.setPpCounter(maxCounter);
                                priceProtection.setNumberPP(sessPriceProtection.generateCostingCode(priceProtection));
                                long oid = pstPriceProtection.insertExc(this.priceProtection);
                                
                               
                        }catch (Exception exc){
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                        }

                }else{
                    
                        try {
                                //cek apakah location nya sama? jika tidak sama maka create no invoice baru
                                if(prevPriceProtection.getLocationId()!=priceProtection.getLocationId()){
                                    SessPriceProtection sessPriceProtection = new SessPriceProtection();
                                    int maxCounter = sessPriceProtection.getMaxPriceProtectionCounter(priceProtection.getDateCreated(), priceProtection);
                                    maxCounter = maxCounter + 1;
                                    priceProtection.setPpCounter(maxCounter);
                                    priceProtection.setNumberPP(sessPriceProtection.generateCostingCode(priceProtection));
                                }
                                
                                long oid = pstPriceProtection.updateExc(this.priceProtection);
                                
                                //disini cek jika statusnya dokument final maka, price protection sudah mempengaruhi 
                                String whereClouse=PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID] + " = " + oidPosPrice;
                                Vector listMaterialItem = PstPriceProtectionItem.list(whereClouse, "");
                                if(listMaterialItem.size()>0){
                                     for (int k = 0; k <listMaterialItem.size(); k++) {
                                         PriceProtectionItem priceProtectionItem = (PriceProtectionItem)listMaterialItem.get(k);
                                         
                                         //cari serial number nya dan update
                                         String whereClouseCode=PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID] + " = " + priceProtectionItem.getMaterialId();
                                         int qty = (int) priceProtectionItem.getStockOnHand();
                                         Vector listStockCode = PstMaterialStockCode.list(0,qty, whereClouseCode, PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_DATE]+" ASC ");
                                         
                                         if(listStockCode.size()>0){
                                             for (int i = 0; i <listStockCode.size(); i++) {
                                                 MaterialStockCode materialStockCode = (MaterialStockCode) listStockCode.get(i);
                                                 //proses update
                                                 try{
                                                     PstMaterialStockCode.updateAmountAfterGetPP(materialStockCode.getOID(), priceProtectionItem.getAmount());
                                                 }catch(Exception ex){
                                                     System.out.println("salah oey CtrlPriceProtection > command posting > update harga");
                                                 }    
                                             }
                                         }
                                         
                                     }
                                }
                                
                        }catch (Exception exc){
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
                        }

                }
                break;
                
            default :
        }
        return rsCode;
    }
    
}
