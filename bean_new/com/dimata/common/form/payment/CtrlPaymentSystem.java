/*
 * Ctrl Name  		:  CtrlPaymentSystem.java
 * Created on           :  [date] [time] AM/PM
 *
 * @author  		: karya
 * @version  		: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.form.payment;

/* java package */
import java.util.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.DBException;
import com.dimata.common.entity.payment.PaymentSystem;
import com.dimata.common.entity.payment.PstPaymentSystem;
/* project package */
//import com.dimata.services.objsynchxnodes.*;


public class CtrlPaymentSystem extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Payment System sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Payment System already exist", "Data incomplete"}
    };
    
    private int start;
    private String msgString;
    private PaymentSystem paymentSystem;
    private PstPaymentSystem pstPaymentSystem;
    private FrmPaymentSystem frmPaymentSystem;
    int language = LANGUAGE_FOREIGN;
    
    public CtrlPaymentSystem(HttpServletRequest request){
        msgString = "";
        paymentSystem = new PaymentSystem();
        try{
            pstPaymentSystem = new PstPaymentSystem(0);
        }catch(Exception e){;}
        frmPaymentSystem = new FrmPaymentSystem(request, paymentSystem);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmPaymentSystem.addError(frmPaymentSystem.FRM_FIELD_PAYMENT_SYSTEM_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public PaymentSystem getPaymentSystem() { return paymentSystem; }
    
    public FrmPaymentSystem getForm() { return frmPaymentSystem; }
    
    public String getMessage(){ return msgString; }
    
    public int getStart() { return start; }
    
    public int action(int cmd , long oidPaymentSystem){
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd){
            case Command.ADD :
                break;
                
            case Command.SAVE :
                if(oidPaymentSystem != 0){
                    try{
                        paymentSystem = PstPaymentSystem.fetchExc(oidPaymentSystem);
                    }catch(Exception exc){
                    }
                }
                
                frmPaymentSystem.requestEntityObject(paymentSystem);
                
                if(frmPaymentSystem.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                paymentSystem = PstPaymentSystem.setInfoPayment(paymentSystem);
                
                if(paymentSystem.getOID()==0){
                    String whereClause = PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM]+ " = '"+paymentSystem.getPaymentSystem()+"'";
                    Vector list = PstPaymentSystem.list(0,0,whereClause,"");
                    if(list !=null && list.size()>0){
                        msgString =  resultText[language][RSLT_EST_CODE_EXIST];
                        return RSLT_EST_CODE_EXIST ;
                    }
                    try{
                        long oid = pstPaymentSystem.insertExc(this.paymentSystem);
                        if(oid!=0){
                            //DSJ_ObjSynch.addObjToSynch(paymentSystem.getOID(), paymentSystem.getClass().getName(), Command.ADD);
                        }
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }else{
                    try {
                        long oid = pstPaymentSystem.updateExc(this.paymentSystem);
                        //eka
                        if(oid!=0){
                            //DSJ_ObjSynch.addObjToSynch(paymentSystem.getOID(), paymentSystem.getClass().getName(), Command.UPDATE);
                        }
                    }catch (Exception exc){
                        excCode = I_DBExceptionInfo.UNKNOWN;
                        msgString = getSystemMessage(excCode);
                    }
                    
                }
                break;
                
            case Command.EDIT :
                if (oidPaymentSystem != 0) {
                    
                    try {
                        paymentSystem = PstPaymentSystem.fetchExc(oidPaymentSystem);
                        
                    } catch (Exception exc){
                        excCode = I_DBExceptionInfo.UNKNOWN;
                        msgString = getSystemMessage(excCode);
                        exc.printStackTrace();
                    }
                }
                break;
                
            case Command.ASK :
                if (oidPaymentSystem != 0) {
                    try {
                        paymentSystem = PstPaymentSystem.fetchExc(oidPaymentSystem);
                    } catch (Exception exc){
                        excCode = I_DBExceptionInfo.UNKNOWN;
                        msgString = getSystemMessage(excCode);
                    }
                }
                break;
                
            case Command.DELETE :
                if (oidPaymentSystem != 0){
                    try{
                        long oid = PstPaymentSystem.deleteExc(oidPaymentSystem);
                        if(oid!=0){
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            
                            //DSJ_ObjSynch.addObjToSynch(oidPaymentSystem, paymentSystem.getClass().getName(), Command.DELETE);
                            
                            excCode = RSLT_OK;
                        }else{
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }catch(Exception exc){
                        excCode = I_DBExceptionInfo.UNKNOWN;
                        msgString = getSystemMessage(excCode);
                    }
                }
                break;
                
            default :
                
        }
        return excCode;
    }
}
