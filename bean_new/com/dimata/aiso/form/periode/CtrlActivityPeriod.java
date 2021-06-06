/*
 * CtrlActivityPeriod.java
 *
 * Created on January 5, 2007, 9:22 AM
 */

package com.dimata.aiso.form.periode;

/* import package java*/
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/* import package qdep*/
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.periode.ActivityPeriod;
import com.dimata.aiso.entity.periode.PstActivityPeriod;

/* import package dimata*/
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

/**
 *
 * @author  dwi
 */
public class CtrlActivityPeriod implements I_Language {
    
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_MULTIPLE_DATE = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    
    public static String[][] resultText = {
        {"Berhasil !", "Tidak dapat diproses", "Tanggal awal sama","Data tidak lengkap"},
        {"Success !", "Can not process", "Duplicate start date", "Data incomplete"}
    };
    
    public static int NO_ERR = 0;
    public static int ERR_START_DATE = 1;
    public static int ERR_DUE_DATE = 2;   
    public static int ERR_POSTED = 3;
    
    public static String[][] errorText = {
        {" ","Tanggal awal tidak sesuai","Tanggal akhir tidak sesuai","Periode terakhir belum terposting"},
        {" ","Invalid Start Date","Invalid End Date","Last period hasn't posted"}
    };
    
    public static int MSG_PER_OK = 0;
    public static int MSG_PER_LAST = 1;
    public static int MSG_PER_LAST_DUE = 2;
    public static int MSG_PER_DUE = 3;
    public static int MSG_PER_ERR = 4;
    public static int MSG_NO_PER = 5;
    public static int MSG_NO_TODAY = 6;
    
    public static String[][] msgPeriodeText = {
        {"","Hari ini adalah tanggal akhir entry data ...","Hari ini adalah tanggal akhir entry data dan akhir periode ...",
         "Hari ini adalah tanggal akhir periode ...","Periode ini harus ditutup ...",
         "Tidak ada periode yang aktif, silahkan setup new period ...","Tutup buku tidak bisa dilakukan, bukan akhir periode ..."},
         {"","Last entry date ...","Last entry date for this period ...",
          "End date for this period ...","This period has to close ...",
          "There are no active period, please setup new period ...","Closing Period can not process. Not end date of period ..."}
    };
    
    private int start = 0;
    private String msgString = "";
    private int iErrCode = 0;
    private ActivityPeriod actPeriod;
    private PstActivityPeriod pstActPeriode;
    private FrmActivityPeriod frmActPeriod; 
    private int language= LANGUAGE_DEFAULT;
    private String messageErr = "";
    
    
    /** Creates a new instance of CtrlActivityPeriod */
    public CtrlActivityPeriod(HttpServletRequest request) {
        msgString = "";
        actPeriod = new ActivityPeriod();
        try{
            pstActPeriode = new PstActivityPeriod(0);            
        }catch(Exception e){}
        frmActPeriod = new FrmActivityPeriod(request, actPeriod);
    }
    
    public int getStatusPeriod()
    {
        Vector currDate = PstActivityPeriod.getCurrPeriod();
        if((currDate!=null) && (currDate.size()>0))
        {
            ActivityPeriod actPeriod = (ActivityPeriod) currDate.get(0);
            Date tempDate = new Date();
            Date toDayDate = new Date(tempDate.getYear(),tempDate.getMonth(),tempDate.getDate());

            if(actPeriod.getEndDate().compareTo(toDayDate) == 0)
            {
                return MSG_PER_LAST;
            }
            
            if(actPeriod.getEndDate().before(toDayDate))
            {
                return MSG_PER_ERR;
            }
        }
        else
        {
            return MSG_NO_PER;
        }
        return MSG_PER_OK;
    }  
    
     private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmActPeriod.addError(frmActPeriod.FRM_START_DATE, resultText[language][RSLT_MULTIPLE_DATE] );
                return resultText[language][RSLT_MULTIPLE_DATE];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_MULTIPLE_DATE;
            default:
                return RSLT_UNKNOWN_ERROR; 
        }
    }
    
    public ActivityPeriod getPeriode() {
        return actPeriod;
    }
    
    
    public FrmActivityPeriod getForm() {
        return frmActPeriod;
    }
    
    public String getMessage(){
        return msgString;
    }
    
    public int getErrCode(){
        return iErrCode;
    }
    
    public int getStart() {
        return start;
    }
    
    public int action(int cmd, long IDPeriode, int monthInterval, int lastEntryDuration) 
    {
        this.start = start;
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;   
        int rsCode = RSLT_OK; 
        
        switch (cmd) 
        {
            case Command.ADD : 
                    break;
            
            case Command.SAVE :
                frmActPeriod.requestEntityObject(actPeriod);
                
                Date newStartDate = actPeriod.getStartDate();
                Date newDueDate = actPeriod.getEndDate();                
                
                if (newStartDate == null || newDueDate == null) {
                    newStartDate = PstActivityPeriod.getFirstDateOfNewPeriod();
                    int year = newStartDate.getYear();
                    int month = newStartDate.getMonth() + monthInterval - 1;
                    int date = newStartDate.getDate();
                    GregorianCalendar gregCal = new GregorianCalendar(year, month, date);
                    date = gregCal.getActualMaximum(gregCal.DAY_OF_MONTH);
                    newDueDate = new Date(year, month, date);
                    actPeriod.setStartDate(newStartDate);
                    actPeriod.setEndDate(newDueDate);
                }
                
                if (frmActPeriod.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                //int validPeriod = isValidPeriod(IDPeriode, newStartDate, newDueDate, newLastEntry, monthInterval, lastEntryDuration);
                int validPeriod = NO_ERR;
                if (validPeriod == NO_ERR) {
                    // set data that didn't got from form
                    actPeriod.setOID(IDPeriode);
                    
                    if (IDPeriode > 0) {                        
                        ActivityPeriod actPeriod = new ActivityPeriod();                         
                        actPeriod.setPosted(PstActivityPeriod.PERIOD_PREPARE_OPEN);                        
                        try {
                            actPeriod = PstActivityPeriod.fetchExc(IDPeriode);
                            actPeriod.setPosted(actPeriod.getPosted());
                        } catch (Exception error) {
                            System.out.println("err get Entiti A T P"+error.toString());
                        }
                    } else if (PstActivityPeriod.getCount("") > 0)
                        actPeriod.setPosted(PstActivityPeriod.PERIOD_PREPARE_OPEN);
                    else
                        actPeriod.setPosted(PstActivityPeriod.PERIOD_OPEN);
                    
                    long oid = 0;
                    if (actPeriod.getOID() == 0) {
                        try {
                            oid = pstActPeriode.insertExc(this.actPeriod);
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_SAVED);
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
                            oid = pstActPeriode.updateExc(this.actPeriod);
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_UPDATED);
                        } catch (DBException dbexc) {
                            excCode = dbexc.getErrorCode();
                            msgString = getSystemMessage(excCode);
                        } catch (Exception exc) {
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                    }
                } else {
                    rsCode = FRMMessage.ERR_SAVED;
                    msgString = errorText[language][validPeriod];
                }
                break;
            case Command.EDIT :
                if (IDPeriode != 0){
                    try {
                        actPeriod = (ActivityPeriod)pstActPeriode.fetchExc(IDPeriode);
                    } catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString=getSystemMessage(excCode);
                    } catch (Exception exc){
                        msgString=getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK :
                if ((IDPeriode > 0)||(IDPeriode < 0)){
                    try {
                        actPeriod = (ActivityPeriod) pstActPeriode.fetchExc(IDPeriode);
                        msgString = FRMMessage.getMsg(FRMMessage.MSG_ASKDEL);
                    } catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString=getSystemMessage(excCode);
                    } catch (Exception exc){
                        msgString=getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
            case Command.DELETE :
                try {
                    PstActivityPeriod.deleteExc(IDPeriode);
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_DELETED);
                } catch (DBException dbExc) {
                    excCode = dbExc.getErrorCode();
                    msgString = getSystemMessage(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                }
                break;
                
            default:
        }
        return rsCode;
    }
}
