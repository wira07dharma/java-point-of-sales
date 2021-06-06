/*
 * CtrlActivity.java
 *
 * Created on January 10, 2007, 5:53 PM
 */

package com.dimata.aiso.form.masterdata;

/* import package servlet */
import javax.servlet.http.HttpServletRequest;

/* import package dimata util */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

/* import package aiso */
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.form.masterdata.FrmActivity;

/* import package qdep */
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;

/**
 *
 * @author  dwi
 */
public class CtrlActivity extends Control implements I_Language {
    
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
            "Proses gagal. Data yg diproses digunakan oleh proses lain atau ulang proses untuk memastikan", 
            "Code activity sudah ada", 
            "Data tidak lengkap", 
            "Level activity tidak sesuai dengan acuannya", 
            "Activity yang bersifat header tidak dapat dipakai sebagai acuan"
        },
        {
            "Success", 
            "Process Failed. Data may be use by other process. To make sure please repeat process", 
            "Activity Code exist", 
            "Data incomplete", 
            "Activity's level not appropriate to its reference", 
            "Cannot use a header activity as a general activity link"
        }
    };
    
    private int start;
    private String msgString;
    private Activity objActivity;
    private PstActivity pstActivity;
    private FrmActivity frmActivity;
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
    public CtrlActivity(HttpServletRequest request) {
        msgString = "";
        objActivity = new Activity();
        try
        {
            pstActivity = new PstActivity(0);
        }
        catch(Exception e)
        {
        }
        frmActivity = new FrmActivity(request, objActivity);
    }
    
     private String getSystemMessage(int msgCode)
    {
        switch (msgCode)
        {
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmActivity.addError(frmActivity.FRM_CODE, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public Activity getActivity() 
    {
        return objActivity;
    }
    
    
    public FrmActivity getForm()  
    {
        return frmActivity;
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
                        objActivity = PstActivity.fetchExc(Oid);
                        System.out.println("objActivity ======> "+objActivity);
                    }catch(Exception e){}
                }
                
                frmActivity.requestEntityObject(objActivity);
                objActivity.setOID(Oid);  
                
                if(frmActivity.errorSize() > 0)  
                {
                    
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }               
              
                
                if (objActivity.getOID() == 0)
                    
                {
                    try
                    {
                        long  oid = pstActivity.insertExc(this.objActivity);                      
                                               
                    }
                    catch (DBException dbexc) 
                    {
                        System.out.println("asdsad"+dbexc.toString());
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
                        long oid = pstActivity.updateExc(this.objActivity);                                               
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
                        objActivity = (Activity)pstActivity.fetchExc(Oid);
                        System.out.println("CtrlActivity objActivity.getPosted() : " + objActivity.getPosted());
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
                        objActivity = (Activity) pstActivity.fetchExc(Oid);
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
                    PstActivity pstActivity = new PstActivity();
                    try
                    {
                        long oid = pstActivity.deleteExc(Oid);
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
