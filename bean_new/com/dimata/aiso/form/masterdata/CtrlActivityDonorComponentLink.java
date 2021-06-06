/*
 * CtrlActivityDonorComponentLink.java
 *
 * Created on January 23, 2007, 6:01 PM
 */

package com.dimata.aiso.form.masterdata; 

/* import package javax servlet */
import javax.servlet.http.HttpServletRequest;

/* import package dimata util */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

/* import package aiso */
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.form.masterdata.FrmActivityDonorComponentLink;

/* import package qdep */
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;

/**
 *
 * @author  dwi
 */
public class CtrlActivityDonorComponentLink extends Control implements I_Language {
    
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_LEVEL_POSTED = 4;
    public static int RSLT_NOT_POSTABLE_LINK = 5;
    
    public static String[][] resultText = 
    {
        {
            "Berhasil", 
            "Tidak dapat diproses", 
            "Link sudah ada", 
            "Data tidak lengkap",             
            "Activity yang bersifat header tidak dapat dipakai sebagai acuan"
        },
        {
            "Success", 
            "Can not process", 
            "Donor component has linked with this activity.Please select another one", 
            "Data incomplete",             
            "Cannot use a header activity as a general activity link"
        }
    };
    
    private int start;
    private String msgString;
    private ActivityDonorComponentLink objActivityDonorComponentLink;
    private PstActivityDonorComponentLink pstActivityDonorComponentLink;
    private FrmActivityDonorComponentLink frmActivityDonorComponentLink;
    int language = LANGUAGE_DEFAULT;
    
     public int getLanguage()
    { 
        return language; 
    }
    
    public void setLanguage(int language)
    { 
        this.language = language; 
    }
    
    /** Creates a new instance of CtrlActivity */
    public CtrlActivityDonorComponentLink(HttpServletRequest request) {
        msgString = "";
        objActivityDonorComponentLink = new ActivityDonorComponentLink();
        try
        {
            pstActivityDonorComponentLink = new PstActivityDonorComponentLink(0);
        }
        catch(Exception e)
        {
        }
        frmActivityDonorComponentLink = new FrmActivityDonorComponentLink(request, objActivityDonorComponentLink);
    }
    
     private String getSystemMessage(int msgCode)
    {
        switch (msgCode)
        {
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmActivityDonorComponentLink.addError(frmActivityDonorComponentLink.FRM_DONOR_COMPONENT_ID, resultText[language][RSLT_EST_CODE_EXIST] );
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode)
    {
        switch (msgCode)
        {
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }
    
    public ActivityDonorComponentLink getActivityDonorComponentLink() 
    {
        return objActivityDonorComponentLink;
    }
    
    
    public FrmActivityDonorComponentLink getForm()  
    {
        return frmActivityDonorComponentLink;
    }
    
    public String getMessage()
    {
        return msgString;
    }
    
    public int getStart() 
    {
        return start;
    }
    
     public int action(int cmd, long Oid) 
    {        
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd)
        {
            case Command.ADD :
                break;
                
            case Command.SAVE :
                if(Oid!=0){
                    try{
                        objActivityDonorComponentLink = PstActivityDonorComponentLink.fetchExc(Oid);                       
                    }catch(Exception e){
                        objActivityDonorComponentLink = new ActivityDonorComponentLink(); 
                    } 
                }
                
                frmActivityDonorComponentLink.requestEntityObject(objActivityDonorComponentLink);
                objActivityDonorComponentLink.setOID(Oid);  
                
                if(frmActivityDonorComponentLink.errorSize() > 0)  
                {                    
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }  
                
                boolean checkCode = false;
                if(objActivityDonorComponentLink.getDonorComponentId() != 0 && objActivityDonorComponentLink.getActivityPeriodId() != 0 && objActivityDonorComponentLink.getActivityId() != 0 && objActivityDonorComponentLink.getOID() == 0){
                    checkCode = PstActivityDonorComponentLink.checkDonorComp(objActivityDonorComponentLink.getDonorComponentId(),objActivityDonorComponentLink.getActivityPeriodId(),objActivityDonorComponentLink.getActivityId()); 
                }
                
                
                if(checkCode){
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }
                
                if (objActivityDonorComponentLink.getOID() == 0)
                //if (Oid == 0)                    
                {
                    try
                    {                        
                        long  oid = pstActivityDonorComponentLink.insertExc(this.objActivityDonorComponentLink);                      
                                               
                    }
                    catch (DBException dbexc) 
                    {                        
                        excCode = dbexc.getErrorCode();
                        msgString=getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }
                    catch (Exception exc) 
                    {
                        msgString=getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                else 
                {
                    try 
                    {                        
                        long oid = pstActivityDonorComponentLink.updateExc(this.objActivityDonorComponentLink);                                               
                    }
                    catch (DBException dbexc)
                    {                        
                        excCode = dbexc.getErrorCode();
                        msgString=getSystemMessage(excCode);
                    }
                    catch (Exception exc)
                    {
                        msgString=getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.EDIT :
                if (Oid != 0) 
                {
                    try 
                    {                        
                        objActivityDonorComponentLink = (ActivityDonorComponentLink)pstActivityDonorComponentLink.fetchExc(Oid);                        
                    }
                    catch (DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString=getSystemMessage(excCode);
                    }
                    catch (Exception exc)
                    {
                        msgString=getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK :
                if (Oid != 0)
                {
                    try 
                    {
                        objActivityDonorComponentLink = (ActivityDonorComponentLink) pstActivityDonorComponentLink.fetchExc(Oid);
                        msgString = FRMMessage.getErr(FRMMessage.MSG_ASKDEL);
                    }
                    catch (DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString=getSystemMessage(excCode);
                    }
                    catch (Exception exc)
                    {
                        msgString=getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
                
            case Command.DELETE :
                if (Oid != 0)
                {
                    PstActivityDonorComponentLink pstActivityDonorComponentLink = new PstActivityDonorComponentLink();
                    try
                    { 
                        long oid = pstActivityDonorComponentLink.deleteExc(Oid);  
                        this.start = 0;
                        
                    }
                    catch (DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString=getSystemMessage(excCode);
                    }
                    catch (Exception exc)
                    {
                        msgString=getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            default:
                
        }
        return rsCode;
    }
    
}
