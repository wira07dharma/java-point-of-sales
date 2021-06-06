/*
 * CtrlActivityAccountLink.java
 *
 * Created on January 13, 2007, 11:39 AM
 */

package com.dimata.aiso.form.masterdata;

/* import package http servlet */
import javax.servlet.http.HttpServletRequest;

/* import package dimata util */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

/* import package aiso */
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.form.masterdata.FrmActivityAccountLink;

/* import package qdep */
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*; 



/**
 *
 * @author  dwi
 */
public class CtrlActivityAccountLink extends Control implements I_Language {
    
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
            "Link sudah ada. Silahkan pilih perkiraan lain", 
            "Data tidak lengkap",             
            "Activity yang bersifat header tidak dapat dipakai sebagai acuan"
        },
        {
            "Success", 
            "Unknown Error. System can not process your data", 
            "Chart of account has linked to activity this.Please select another one", 
            "Data incomplete. Please check and try entry again",             
            "Cannot use a header activity as a general activity link"
        }
    };
    
    private int start;
    private String msgString;
    private ActivityAccountLink objActivityAccountLink;
    private PstActivityAccountLink pstActivityAccountLink;
    private FrmActivityAccountLink frmActivityAccountLink;
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
    public CtrlActivityAccountLink(HttpServletRequest request) {
        msgString = "";
        objActivityAccountLink = new ActivityAccountLink();
        try
        {
            pstActivityAccountLink = new PstActivityAccountLink(0);
        }
        catch(Exception e)
        {
        }
        frmActivityAccountLink = new FrmActivityAccountLink(request, objActivityAccountLink);
    }
    
     private String getSystemMessage(int msgCode)
    {
        switch (msgCode)
        {
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmActivityAccountLink.addError(frmActivityAccountLink.FRM_ACCOUNT_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public ActivityAccountLink getActivityAccountLink() 
    {
        return objActivityAccountLink;
    }
    
    
    public FrmActivityAccountLink getForm()  
    {
        return frmActivityAccountLink;
    }
    
    public String getMessage()
    {
        return msgString;
    }
    
    public int getStart() 
    {
        return start;
    }
    
     public int action(int cmd, long Oid, long idActivity) 
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
                        objActivityAccountLink = PstActivityAccountLink.fetchExc(Oid);                        
                    }catch(Exception e){}
                }
                
                frmActivityAccountLink.requestEntityObject(objActivityAccountLink);
                objActivityAccountLink.setOID(Oid);  
                boolean bCheckCoA = false;
                if(idActivity != 0)
                bCheckCoA = PstActivityAccountLink.checkCoAId(objActivityAccountLink.getIdPerkiraan(), idActivity);
                
                if(frmActivityAccountLink.errorSize() > 0)  
                {
                    msgString = resultText[language][RSLT_FORM_INCOMPLETE];
                    return RSLT_FORM_INCOMPLETE ;
                }               
              
                if(bCheckCoA){
                    msgString = resultText[language][RSLT_EST_CODE_EXIST];
                    return RSLT_EST_CODE_EXIST;
                }
                
                if (objActivityAccountLink.getOID() == 0 && !bCheckCoA)
                    
                {
                    try
                    {
                        long  oid = pstActivityAccountLink.insertExc(this.objActivityAccountLink);                      
                                               
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
                        long oid = pstActivityAccountLink.updateExc(this.objActivityAccountLink);                                               
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
                        objActivityAccountLink = (ActivityAccountLink)pstActivityAccountLink.fetchExc(Oid);                        
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
                        objActivityAccountLink = (ActivityAccountLink) pstActivityAccountLink.fetchExc(Oid);
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
                    PstActivityAccountLink pstActivityAccountLink = new PstActivityAccountLink();
                    try
                    {
                        long oid = pstActivityAccountLink.deleteExc(Oid);
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
