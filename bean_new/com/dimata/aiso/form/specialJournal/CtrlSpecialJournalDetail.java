/*
 * CtrlSpecialJournalDetail.java
 *
 * Created on February 8, 2007, 5:28 PM
 */

package com.dimata.aiso.form.specialJournal;

/* import package java util */
import java.util.*;
import java.sql.*;

/* import package javax servlet http */
import javax.servlet.http.HttpServletRequest;

/* import package dimata util */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

/* import package qdep system */
import com.dimata.qdep.system.*;

/* import package qdep form */
import com.dimata.qdep.form.Control;

/* import package aiso */
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.specialJournal.*;
import com.dimata.aiso.form.specialJournal.*;
import com.dimata.aiso.session.specialJournal.SessSpecialJurnal;
import com.dimata.interfaces.journal.I_JournalType;

/**
 *
 * @author  dwi
 */
public class CtrlSpecialJournalDetail extends Control implements I_Language {
    
    public static int RSLT_OK = 0;
    public static int RSLT_CONTACT = 1;
    public static int RSLT_ACTIVITY = 2;
    public static int RSLT_DESCRIPTION = 3; 
    public static int RSLT_AMOUNT = 4; 
    public static String[][] strJurnalBalance = 
    {
        {"Data sudah tersimpan","Data contact belum dientry","Data acitivity belum di entry","Memo belum dientry","Nilai transaksi belum dientry"},
        {"Data is saved","Contact is required.Please entry","Activity is required. Please entry","Memo is required. Please entry","Amount is required. Please entry"}
    };
    
    private int start;
    private String msgString;
    public static String msgBalance;
    private SpecialJournalDetail objSpecialJournalDetail;
    private PstSpecialJournalDetail pstSpecialJournalDetail;
    private FrmSpecialJournalDetail frmSpecialJournalDetail;
    private int language= LANGUAGE_DEFAULT;
    
    public CtrlSpecialJournalDetail(HttpServletRequest request) 
    {
        msgString = "";
        objSpecialJournalDetail = new SpecialJournalDetail();
        try{
            pstSpecialJournalDetail = new PstSpecialJournalDetail(0);
        }catch(Exception e){}
        frmSpecialJournalDetail = new FrmSpecialJournalDetail(request, objSpecialJournalDetail);
    }
    
    public int getLanguage(){ return language; }
    
    public void setLanguage(int language){ this.language = language; }
    
    public SpecialJournalDetail getJurnalDetail() {
        return objSpecialJournalDetail;
    }
    
    public FrmSpecialJournalDetail getForm() {
        return frmSpecialJournalDetail;
    }
    
    public String getMessage(){
        return msgString;
    }
    
    public String getMsgBalance(){
        return msgBalance;
    }
    
    public int getStart() {
        return start;
    }
    
    public int action(int cmd) {
        if(cmd==Command.SAVE || cmd==Command.ASK){
            frmSpecialJournalDetail.requestEntityObject(objSpecialJournalDetail);
             if(objSpecialJournalDetail.getDescription().length()==0 ){
                    msgString = strJurnalBalance[language][RSLT_DESCRIPTION];
                    return RSLT_DESCRIPTION;
                }
             if(objSpecialJournalDetail.getIdPerkiraan() == 0 ){
                    msgString = strJurnalBalance[language][RSLT_ACTIVITY];
                    return RSLT_ACTIVITY;
                }
            if(objSpecialJournalDetail.getAmount() == 0 ){
                    msgString = strJurnalBalance[language][RSLT_AMOUNT];
                    return RSLT_AMOUNT;
                }
            
                if(objSpecialJournalDetail.getContactId() == 0 ){
                        msgString = strJurnalBalance[language][RSLT_CONTACT];
                        return RSLT_CONTACT;
                  }
        }
        return RSLT_OK;
    }
    
    /** 
     * this method used to posted journal detail
     * this method will called by "CtrlJurnalUmum.journalPosted" method()
     */
    public static int postedJournalDetail(SpecialJournalDetail objSpecialJournalDetail)
    {
        long oid = 0;
        switch(objSpecialJournalDetail.getDataStatus())
        {
            case PstSpecialJournalDetail.DATASTATUS_CLEAN :                
                break;
                
            case PstSpecialJournalDetail.DATASTATUS_ADD :                
                try
                {
                    oid = PstSpecialJournalDetail.insertExc(objSpecialJournalDetail);
                    /*int size = jurnaldetail.getDetailLinks().size();
                    if(size>0){
                        jurnaldetail.setGeneralDetailLink(oid);
                        for(int i = 0; i < size; i++){
                            JurnalDetail dLink = jurnaldetail.getDetailLink(i);
                            dLink.setGeneralDetailLink(oid);
                            dLink.setJurnalIndex(jurnaldetail.getJurnalIndex());
                            PstJurnalDetail.insertExc(dLink);
                        }
                        oid = PstJurnalDetail.updateExc(jurnaldetail);
                    }*/
                }
                catch(DBException dbexc)
                {
                    System.out.println("DBException on CtrlJurnalDetail.postedJournalDetail.DATASTATUS_ADD : " + dbexc.toString());
                }
                catch(Exception exc)
                {
                    System.out.println("Exception on CtrlJurnalDetail.postedJournalDetail.DATASTATUS_ADD : " + exc.toString());
                }
                break;
                
            case PstSpecialJournalDetail.DATASTATUS_UPDATE :                
                try
                {
                    /*PstJurnalDetail.deleteByGeneralDetail(jurnaldetail.getOID());
                    int size = jurnaldetail.getDetailLinks().size();
                    if(size>0){
                        jurnaldetail.setGeneralDetailLink(jurnaldetail.getOID());
                        for(int i = 0; i < size; i++){
                            JurnalDetail dLink = jurnaldetail.getDetailLink(i);
                            dLink.setGeneralDetailLink(jurnaldetail.getOID());
                            dLink.setJurnalIndex(jurnaldetail.getJurnalIndex());
                            PstJurnalDetail.insertExc(dLink);
                        }
                    }*/
                    oid = PstSpecialJournalDetail.updateExc(objSpecialJournalDetail);
                }
                catch(DBException dbexc)
                {
                    System.out.println("DBException on CtrlJurnalDetail.postedJournalDetail.DATASTATUS_UPDATE : " + dbexc.toString());
                }
                catch(Exception exc)
                {
                    System.out.println("Exception on CtrlJurnalDetail.postedJournalDetail.DATASTATUS_UPDATE : " + exc.toString());
                }
                break;
                
            case PstSpecialJournalDetail.DATASTATUS_DELETE :                
                try
                {
                    //PstSpecialJournalDetail.deleteByGeneralDetail(objSpecialJournalDetail.getOID());
                    oid = PstSpecialJournalDetail.deleteExc(objSpecialJournalDetail.getOID());
                }
                catch(DBException dbexc)
                {
                    System.out.println("DBException on CtrlJurnalDetail.postedJournalDetail.DATASTATUS_DELETE : " + dbexc.toString());
                }
                catch(Exception exc)
                {
                    System.out.println("Exception on CtrlJurnalDetail.postedJournalDetail.DATASTATUS_DELETE : " + exc.toString());
                }
                break;
        }
        return 0;
    }    
    
}
