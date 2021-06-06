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
import com.dimata.common.entity.system.*;

public class CtrlMaterialComposit extends Control implements I_Language {
    /*public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;*/

    public static final int RSLT_OK = 0;
    public static final int RSLT_UNKNOWN_ERROR = 1;
    public static final int RSLT_MATERIAL_EXIST = 2;
    public static final int RSLT_FORM_INCOMPLETE = 3;
    public static final int RSLT_QTY_NULL = 4;
    
   /** public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };**/

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Material sudah ada", "Data tidak lengkap", "Jumlah material tidak boleh nol ..."},
        {"Succes", "Can not process", "Material already exist ...", "Data incomplete", "Quantity may not zero ..."}
    };
    
    private int start;
    private String msgString;
    private MaterialComposit materialComposit;
    private PstMaterialComposit pstMaterialComposit;
    private FrmMaterialComposit frmMaterialComposit;
    int language = LANGUAGE_DEFAULT;

    long oidMaterial =0;
    
    public CtrlMaterialComposit(HttpServletRequest request){
        msgString = "";
        materialComposit = new MaterialComposit();
        try{
            pstMaterialComposit = new PstMaterialComposit(0);
        }catch(Exception e){;}
        frmMaterialComposit = new FrmMaterialComposit(request, materialComposit);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            //case I_DBExceptionInfo.MULTIPLE_ID :
                //this.frmMaterialComposit.addError(frmMaterialComposit.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST] );
                //return resultText[language][RSLT_EST_CODE_EXIST];
            case RSLT_MATERIAL_EXIST:
                this.frmMaterialComposit.addError(frmMaterialComposit.FRM_FIELD_MATERIAL_COMPOSER_ID, resultText[language][RSLT_MATERIAL_EXIST]);
                return resultText[language][RSLT_MATERIAL_EXIST];
            case RSLT_QTY_NULL:
                this.frmMaterialComposit.addError(frmMaterialComposit.FRM_FIELD_QTY, resultText[language][RSLT_QTY_NULL]);
                return resultText[language][RSLT_QTY_NULL];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode){
        switch (msgCode){
            //case I_DBExceptionInfo.MULTIPLE_ID :
                //return RSLT_EST_CODE_EXIST;
            case RSLT_MATERIAL_EXIST:
                return RSLT_MATERIAL_EXIST;
            case RSLT_QTY_NULL:
                return RSLT_QTY_NULL;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }
    
    public int getLanguage(){ return language; }
    
    public void setLanguage(int language){ this.language = language; }
    
    public MaterialComposit getMaterialComposit() { return materialComposit; }
    
    public FrmMaterialComposit getForm() { return frmMaterialComposit; }
    
    public String getMessage(){ return msgString; }
    
    public int getStart() { return start; }
    
    public int action(int cmd , long oidMaterialComposit) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd) {
            case Command.ADD :
                break;
                
            case Command.SAVE :
                if(oidMaterialComposit != 0) {
                    try {
                        materialComposit = PstMaterialComposit.fetchExc(oidMaterialComposit);
                    }
                    catch(Exception exc) {
                    }
                }
                
                frmMaterialComposit.requestEntityObject(materialComposit);

                 // check if current material already exist in orderMaterial
                if (materialComposit.getOID() == 0 && PstMaterialComposit.materialExist(materialComposit.getMaterialComposerId(), materialComposit.getMaterialId())) {
                   msgString = getSystemMessage(RSLT_MATERIAL_EXIST);
                    return getControlMsgId(RSLT_MATERIAL_EXIST);
                }

                //check if qty =0
               if (materialComposit.getQty() == 0) {
                    msgString = getSystemMessage(RSLT_QTY_NULL);
                    return getControlMsgId(RSLT_QTY_NULL);
               }


                if(frmMaterialComposit.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                if(materialComposit.getOID()==0) {
                    try {
                        long oid = pstMaterialComposit.insertExc(this.materialComposit);
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
                        long oid = pstMaterialComposit.updateExc(this.materialComposit);
                    }
                    catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }

                /*oidMaterial = materialComposit.getMaterialId();
                        Vector grandTotalComposit = PstMaterialComposit.grandTotalComposit(oidMaterial);

                        for(int i=0;i<grandTotalComposit.size();i++){
                            Vector temp = (Vector)grandTotalComposit.get(i);
                               Double sumStockValue = (Double)temp.get(0);
                               Double sumCostValue = (Double)temp.get(1);
                               boolean isOK = false;

                               Double sumCostNoPpnValue = sumCostValue/1.1;
                               Double lastVat = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

                              isOK = PstMaterialComposit.updateCostAndStockValueMaster(oidMaterial, sumStockValue, sumCostValue, sumCostNoPpnValue, lastVat);
                              System.out.println("=============== update stockValue and lastCost : "+sumStockValue);
                              System.out.println("=============== update lastCost : "+sumCostValue);
                     }*/
                
               

                break;
                
            case Command.EDIT :
                if (oidMaterialComposit != 0) {
                    try {
                        materialComposit = PstMaterialComposit.fetchExc(oidMaterialComposit);
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
                if (oidMaterialComposit != 0) {
                    try {
                        materialComposit = PstMaterialComposit.fetchExc(oidMaterialComposit);
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
                if (oidMaterialComposit != 0) {

                    try {
                        long oid = PstMaterialComposit.deleteExc(oidMaterialComposit);
                       
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
