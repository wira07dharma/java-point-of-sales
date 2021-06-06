package com.dimata.aiso.form.masterdata;

// package java
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// import dimata
import com.dimata.util.*;
import com.dimata.util.lang.*;

// import qdep
import com.dimata.aiso.db.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;

// import aiso
import com.dimata.aiso.entity.masterdata.*;

public class CtrlPerkiraan extends Control implements I_Language  
{    
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
            "No Perkiraan sudah ada", 
            "Data tidak lengkap", 
            "Level rekening tidak sesuai dengan acuannya", 
            "Perkiraan yang bersifat header tidak dapat dipakai sebagai perkiraan acuan"
        },
        {
            "Ok", 
            "Can not process", 
            "Account number exist", 
            "Data incomplete", 
            "Account's level not appropriate to its reference", 
            "Cannot use a header account as a general account link"
        }
    };
    
    private int start;
    private String msgString;
    private Perkiraan perkiraan;
    private PstPerkiraan pstPerkiraan;
    private FrmPerkiraan frmPerkiraan;
    int language = LANGUAGE_DEFAULT;
    
    public int getLanguage()
    { 
        return language; 
    }
    
    public void setLanguage(int language)
    { 
        this.language = language; 
    }
    
    public CtrlPerkiraan(HttpServletRequest request) 
    {
        msgString = "";
        perkiraan = new Perkiraan();
        try
        {
            pstPerkiraan = new PstPerkiraan(0);
        }
        catch(Exception e)
        {
        }
        frmPerkiraan = new FrmPerkiraan(request, perkiraan);
    }
    
    private String getSystemMessage(int msgCode)
    {
        switch (msgCode)
        {
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmPerkiraan.addError(frmPerkiraan.FRM_NOPERKIRAAN, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public Perkiraan getPerkiraan() 
    {
        return perkiraan;
    }
    
    
    public FrmPerkiraan getForm() 
    {
        return frmPerkiraan;
    }
    
    public String getMessage()
    {
        return msgString;
    }
    
    public int getStart() 
    {
        return start;
    }
    
    //public int action(int cmd, long IDPerkiraan, int start,  int recordToGet, String whereClause) 
    public int action(int cmd, long IDPerkiraan) 
    {        
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd)
        {
            case Command.ADD :
                break;
                
            case Command.SAVE :
                frmPerkiraan.requestEntityObject(perkiraan);
                perkiraan.setOID(IDPerkiraan);
                
                if(frmPerkiraan.errorSize() > 0)  
                {
                    msgString = resultText[language][RSLT_FORM_INCOMPLETE];
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                if (perkiraan.getGeneralAccountLink() < 0) 
                {
                    msgString = resultText[language][RSLT_NOT_POSTABLE_LINK];
                    return RSLT_NOT_POSTABLE_LINK;
                }
                
                if(!PstPerkiraan.checkAccountlevelPostable(perkiraan.getIdParent(),perkiraan.getLevel()))
                {
                    msgString = resultText[language][RSLT_LEVEL_POSTED];
                    return RSLT_LEVEL_POSTED ;
                }
                
                if (perkiraan.getOID() == 0)
                {
                    try
                    {
                        long  oid = pstPerkiraan.insertExc(this.perkiraan); 
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
                        long oid = pstPerkiraan.updateExc(this.perkiraan);                                               
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
                if (IDPerkiraan != 0) 
                {
                    try 
                    {                        
                        perkiraan = (Perkiraan)pstPerkiraan.fetchExc(IDPerkiraan);
                        System.out.println("CtrlPerkiraan perkiraan.getPostable() : " + perkiraan.getPostable());
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
                if (IDPerkiraan != 0)
                {
                    try 
                    {
                        perkiraan = (Perkiraan) pstPerkiraan.fetchExc(IDPerkiraan);
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
                if (IDPerkiraan != 0)
                {
                    PstPerkiraan pstPerkiraan = new PstPerkiraan();
                    try
                    {
                        long oid = pstPerkiraan.deleteExc(IDPerkiraan);
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
