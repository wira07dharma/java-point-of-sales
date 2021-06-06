/*
 * FrmSpecialJournalDetailAssignt.java
 *
 * Created on February 9, 2007, 1:07 PM
 */

package com.dimata.aiso.form.specialJournal;

/* import package aiso special journal */
import com.dimata.aiso.entity.specialJournal.*;

/* import package javax servlet http */
import javax.servlet.http.HttpServletRequest;

/* import package qdep form */
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface; 
import com.dimata.qdep.form.I_FRMType;

/**
 *
 * @author  dwi
 */
public class FrmSpecialJournalDetailAssignt extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    public static final String FRM_JDETAIL_ASSIGNT = "JDETAIL_ASSIGNT";
    
    public static final int FRM_AMOUNT = 0;
    public static final int FRM_SHARE_PROCENTAGE = 1;
    public static final int FRM_JOURNAL_DETAIL_ID = 2;
    public static final int FRM_ACTIVITY_ID = 3;
    
    public static String[] fieldNames = {
        "AMOUNT",
        "SHARE_PROCENTAGE",
        "JOURNAL_DETAIL_ID",
        "ACTIVITY_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_FLOAT + ENTRY_REQUIRED,
        TYPE_FLOAT + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED
    };
    
    private SpecialJournalDetailAssignt objSpecialJournalDetailAssignt;
    
    /** Creates a new instance of FrmSpecialJournalDetailAssignt */
    public FrmSpecialJournalDetailAssignt(SpecialJournalDetailAssignt objSpecialJournalDetailAssignt) {
        this.objSpecialJournalDetailAssignt = objSpecialJournalDetailAssignt;
    }
    
    public FrmSpecialJournalDetailAssignt(HttpServletRequest request, SpecialJournalDetailAssignt objSpecialJournalDetailAssignt) {
        super(new FrmSpecialJournalDetailAssignt(objSpecialJournalDetailAssignt), request);
        this.objSpecialJournalDetailAssignt = objSpecialJournalDetailAssignt;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getFormName() {
        return FRM_JDETAIL_ASSIGNT;
    }
    
    public SpecialJournalDetailAssignt getEntityObject(){
        return objSpecialJournalDetailAssignt;
    }
    
    public void requestEntityObject(SpecialJournalDetailAssignt objSpecialJournalDetailAssignt){
    	try{
            this.requestParam();
            
            objSpecialJournalDetailAssignt.setAmount(this.getDouble(FRM_AMOUNT));
            objSpecialJournalDetailAssignt.setShareProcentage(this.getFloat(FRM_SHARE_PROCENTAGE));
            objSpecialJournalDetailAssignt.setJournalDetailId(this.getLong(FRM_JOURNAL_DETAIL_ID));
            objSpecialJournalDetailAssignt.setActivityId(this.getLong(FRM_ACTIVITY_ID));           
            
            this.objSpecialJournalDetailAssignt = objSpecialJournalDetailAssignt;
        }catch(Exception e){
            System.out.println("Exception on request entity object ::: "+e.toString());
        }
    }
}
