/*
 * CtrlSpecialJournalDetailAssignt.java
 *
 * Created on February 9, 2007, 1:36 PM
 */

package com.dimata.aiso.form.specialJournal;

/* import package java util */
import java.util.*;

/* import package java sql */
import java.sql.*;

/* import javax servlet http */
import javax.servlet.http.HttpServletRequest;

/* import package aiso */
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.specialJournal.*;
import com.dimata.aiso.form.specialJournal.*;
import com.dimata.aiso.session.specialJournal.*;

/* import package qdep */
import com.dimata.qdep.form.Control;
import com.dimata.qdep.system.*;

/* import package dimata util */
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

/**
 *
 * @author  dwi
 */
public class CtrlSpecialJournalDetailAssignt extends Control implements I_Language{
    
    public static int RSLT_OK = 0;
    
    public static int INT_AMOUNT_BLANK = 0;
    public static int INT_AMOUNT_SHARE_BLANK = 1;
    public static String[][] strJurnalBalance = 
    {
        {"Kolom amount belum diisi ...","Kolom share procentage kosong ..."},
        {"Amount Column Empty ...","Share Procentage Column Empty ..."}
    };
    
    private int start;
    private String msgString;
    public static String msgBalance;
    private SpecialJournalDetailAssignt objSpecialJournalDetailAssignt;
    private PstSpecialJournalDetailAssignt pstSpecialJournalDetailAssignt;
    private FrmSpecialJournalDetailAssignt frmSpecialJournalDetailAssignt;
    private int language= LANGUAGE_DEFAULT;
    
    public CtrlSpecialJournalDetailAssignt(HttpServletRequest request) 
    {
        msgString = "";
        objSpecialJournalDetailAssignt = new SpecialJournalDetailAssignt();
        try{
            pstSpecialJournalDetailAssignt = new PstSpecialJournalDetailAssignt(0);
        }catch(Exception e){}
        frmSpecialJournalDetailAssignt = new FrmSpecialJournalDetailAssignt(request, objSpecialJournalDetailAssignt);
    }
    
    public int getLanguage(){ return language; }
    
    public void setLanguage(int language){ this.language = language; }
    
    public SpecialJournalDetailAssignt getJurnalDetail() {
        return objSpecialJournalDetailAssignt;
    }
    
    public FrmSpecialJournalDetailAssignt getForm() {
        return frmSpecialJournalDetailAssignt;
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
        if(cmd==Command.SUBMIT || cmd==Command.ADD){
            frmSpecialJournalDetailAssignt.requestEntityObject(objSpecialJournalDetailAssignt);
        }
        return RSLT_OK;
    }
    
    /** 
     * this method used to posted journal detail
     * this method will called by "CtrlJurnalUmum.journalPosted" method()
     */
    public static int postedJournalDetailActivity(SpecialJournalDetailAssignt objSpecialJournalDetailAssignt)
    {
        long oid = 0;
        switch(objSpecialJournalDetailAssignt.getDataStatus())
        {
            case PstSpecialJournalDetailAssignt.DATASTATUS_CLEAN :                
                break;
                
            case PstSpecialJournalDetailAssignt.DATASTATUS_ADD :                
                try
                {
                    oid = PstSpecialJournalDetailAssignt.insertExc(objSpecialJournalDetailAssignt);                    
                }
                catch(DBException dbexc)
                {
                    System.out.println("DBException on CtrlJourrnalDetailAssignt.postedJournalDetailActivity.DATASTATUS_ADD : " + dbexc.toString());
                }
                catch(Exception exc)
                {
                    System.out.println("Exception on CtrlJourrnalDetailAssignt.postedJournalDetailActivity.DATASTATUS_ADD : " + exc.toString());
                }
                break;
                
            case PstSpecialJournalDetailAssignt.DATASTATUS_UPDATE :                
                try
                {                   
                    oid = PstSpecialJournalDetailAssignt.updateExc(objSpecialJournalDetailAssignt);
                }
                catch(DBException dbexc)
                {
                    System.out.println("DBException on CtrlJourrnalDetailAssignt.postedJournalDetailActivity.DATASTATUS_UPDATE : " + dbexc.toString());
                }
                catch(Exception exc)
                {
                    System.out.println("Exception on CtrlJourrnalDetailAssignt.postedJournalDetailActivity.DATASTATUS_UPDATE : " + exc.toString());
                }
                break;
                
            case PstSpecialJournalDetailAssignt.DATASTATUS_DELETE :                
                try
                {                    
                    oid = PstSpecialJournalDetailAssignt.deleteExc(objSpecialJournalDetailAssignt.getOID());
                }
                catch(DBException dbexc)
                {
                    System.out.println("DBException on CtrlJourrnalDetailAssignt.postedJournalDetailActivity.DATASTATUS_DELETE : " + dbexc.toString());
                }
                catch(Exception exc)
                {
                    System.out.println("Exception on CtrlJourrnalDetailAssignt.postedJournalDetailActivity.DATASTATUS_DELETE : " + exc.toString());
                }
                break;
        }
        return 0;
    }  
    
}
