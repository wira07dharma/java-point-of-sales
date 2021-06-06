/*
 * CtrlActivityPeriodLink.java
 *
 * Created on January 16, 2007, 6:42 PM
 */

package com.dimata.aiso.form.masterdata;

/* import package servlet Http */
import javax.servlet.http.HttpServletRequest;

/* import package dimata util */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

/* import package aiso */
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.form.masterdata.FrmActivityPeriodLink;

/* import package qdep */
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;

/* import package java util */
import java.util.*;


/**
 *
 * @author  dwi
 */
public class CtrlActivityPeriodLink extends Control implements I_Language {
    
   public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_LEVEL_POSTED = 4;
    public static int RSLT_NOT_POSTABLE_LINK = 5;
    
    public static String[][] resultText = 
    {
        {
            "Berhasil ...", 
            "Tidak dapat diproses ...", 
            "Link sudah ada ...", 
            "Data tidak lengkap ...",             
            "Activity yang bersifat header tidak dapat dipakai sebagai acuan ..."
        },
        {
            "Success ...", 
            "Can not process ...", 
            "Link Activity exist ...", 
            "Data incomplete ...",             
            "Cannot use a header activity as a general activity link ..."
        }
    };
    
    private int start;
    private String msgString;
    private ActivityPeriodLink objActivityPeriodLink;
    private PstActivityPeriodLink pstActivityPeriodLink;
    private FrmActivityPeriodLink frmActivityPeriodLink;
    private Vector listActivityPeriodLink = new Vector(1,1);
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
    public CtrlActivityPeriodLink(HttpServletRequest request) {
        msgString = "";
        objActivityPeriodLink = new ActivityPeriodLink();
        try
        {
            pstActivityPeriodLink = new PstActivityPeriodLink(0);
        }
        catch(Exception e)
        {
        }
        frmActivityPeriodLink = new FrmActivityPeriodLink(request, objActivityPeriodLink);
    }
    
     private String getSystemMessage(int msgCode)
    {
        switch (msgCode)
        {
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmActivityPeriodLink.addError(frmActivityPeriodLink.FRM_ACT_PERIOD_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public ActivityPeriodLink getActivityPeriodLink() 
    {
        return objActivityPeriodLink;
    }
    
    
    public FrmActivityPeriodLink getForm()   
    {
        return frmActivityPeriodLink;
    }
    
    public String getMessage()
    {
        return msgString;
    }
    
    public int getStart() 
    {
        return start;
    }    
    
    public void setListActivityPeriodLink(Vector result) {
        this.listActivityPeriodLink = result;
    }

    public Vector getListActivityPeriodLink() {
        return this.listActivityPeriodLink;
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
                        objActivityPeriodLink = PstActivityPeriodLink.fetchExc(Oid);
                        long lOid = PstActivityPeriodLink.updateExc(objActivityPeriodLink);                        
                    }catch(Exception e){
                        System.out.println("Exception on CtrlActivityPeriodLink.update :::: "+e.toString());
                    }
                }
                
                frmActivityPeriodLink.requestEntityObject(objActivityPeriodLink);
                objActivityPeriodLink.setOID(Oid);  
                
                if(frmActivityPeriodLink.errorSize() > 0)  
                {
                    
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }               
              
                
                if (objActivityPeriodLink.getOID() == 0)
                    
                {
                    try
                    {
                        long  oid = pstActivityPeriodLink.insertExc(this.objActivityPeriodLink);                      
                                               
                    }
                    catch (DBException dbexc) 
                    {
                        System.out.println("Error pd saat insertExc ==> "+dbexc.toString());
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
                        long oid = pstActivityPeriodLink.updateExc(this.objActivityPeriodLink);                                               
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
                        objActivityPeriodLink = (ActivityPeriodLink)pstActivityPeriodLink.fetchExc(Oid);                        
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
                        objActivityPeriodLink = (ActivityPeriodLink) pstActivityPeriodLink.fetchExc(Oid);
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
                    PstActivityPeriodLink pstActivityPeriodLink = new PstActivityPeriodLink();
                    try
                    {
                        long oid = pstActivityPeriodLink.deleteExc(Oid);
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
    
     public int action(int cmd, long Oid, Vector vectActivityBudgetPeriod) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (Oid != 0) {
                    try {
                        objActivityPeriodLink = PstActivityPeriodLink.fetchExc(Oid);
                    } catch (Exception exc) {
                    }
                }

                frmActivityPeriodLink.requestEntityObject(objActivityPeriodLink);                
                objActivityPeriodLink.setOID(Oid);  
               

                /* manupalate data activity period link */
               // if (Oid != 0) {
                Vector acPeLink = PstActivityPeriodLink.list(0,0,PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACTIVITY_ID]+"="+objActivityPeriodLink.getActivityId(),"");                
                PstActivityPeriodLink.checkForDelete(acPeLink,vectActivityBudgetPeriod);
                    
                    if (vectActivityBudgetPeriod != null && vectActivityBudgetPeriod.size() > 0) {
                        for (int i = 0; i < vectActivityBudgetPeriod.size(); i++) {
                            Vector vect = (Vector) vectActivityBudgetPeriod.get(i);
                            long oidActivityPeriod = 0;
                            long oidActivity = 0;
                            float fBudget = 0;
                            if(vect != null && vect.size() > 0){
                                try{
                                oidActivityPeriod = Long.parseLong((String) vect.get(0)); 
                                oidActivity = Long.parseLong((String) vect.get(1));
                                fBudget = Float.parseFloat((String) vect.get(2));
                                }catch(Exception e){}
                            }
                            
                            ActivityPeriodLink objActivityPeriodLink = new ActivityPeriodLink();
                            objActivityPeriodLink.setActivityPeriodId(oidActivityPeriod);
                            objActivityPeriodLink.setActivityId(oidActivity);
                            objActivityPeriodLink.setBudget(fBudget);                                
                            
                            System.out.println("objActivityPeriodLink.getActivityPeriodId() :::: "+objActivityPeriodLink.getActivityPeriodId());
                            System.out.println("objActivityPeriodLink.getActivityId() :::: "+objActivityPeriodLink.getActivityId());
                            ActivityPeriodLink activityPeriodLink = PstActivityPeriodLink.checkOID(objActivityPeriodLink.getActivityPeriodId(),objActivityPeriodLink.getActivityId());
                            if (activityPeriodLink.getOID()==0) {
                                try {
                                    PstActivityPeriodLink.insertExc(objActivityPeriodLink);
                                    System.out.println("On Save ::::::::: ");
                                } catch (Exception err) {
                                    err.printStackTrace();
                                    System.out.println("err di insert to activity period link ===>  " + err.toString());
                                }
                            } else {
                                try {
                                    objActivityPeriodLink.setOID(activityPeriodLink.getOID());
                                    PstActivityPeriodLink.updateExc(objActivityPeriodLink);
                                    System.out.println("On Update ::::::::: ");
                                } catch (Exception err) {
                                    err.printStackTrace();
                                    System.out.println("err di update to activity period link " + err.toString());
                                }
                            }
                        }
                    }
                    setListActivityPeriodLink(Oid);
                //}
                break;

            case Command.EDIT:
                if (Oid != 0) {
                    try {
                        objActivityPeriodLink = PstActivityPeriodLink.fetchExc(Oid);                        
                        setListActivityPeriodLink(Oid);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (Oid != 0) {
                    try {
                        objActivityPeriodLink = PstActivityPeriodLink.fetchExc(Oid);
                        setListActivityPeriodLink(Oid);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (Oid != 0) {
                    try {
                         
                        objActivityPeriodLink = PstActivityPeriodLink.fetchExc(Oid);
                                                   
                        long oid = PstActivityPeriodLink.deleteExc(Oid);                                                
                            
                      
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            setListActivityPeriodLink(Oid);                            
                            //frmActivityPeriodLink.addError(FrmMaterial.FRM_FIELD_MATERIAL_ID, "");
                            msgString = "Hapus data gagal, Data sudah pernah di pakai untuk transaksi."; //FRMMessage.getMessage(FRMMessage.ERR_DELETED);
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

            default :
                setListActivityPeriodLink(Oid);                
                break;

        }
        return excCode;
    }
     
     public void setListActivityPeriodLink(long Oid) {
        String wh = PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACT_LINK_PERIOD_ID] + " = " + Oid;
        listActivityPeriodLink = PstActivityPeriodLink.list(0, 0, wh, "");
        setListActivityPeriodLink(listActivityPeriodLink);
    }

}
