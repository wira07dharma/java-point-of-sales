/*
 * FrmSpecialJournalDetail.java
 *
 * Created on February 7, 2007, 1:07 PM
 */

package com.dimata.aiso.form.specialJournal;

/* import package aiso */
import com.dimata.aiso.entity.specialJournal.*;

/* import package javax servlet */
import javax.servlet.http.*;

/* import package qdep form */
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;


/**
 *
 * @author  dwi
 */
public class FrmSpecialJournalDetail extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    public static final String FRM_SPECIAL_JOURNAL_DETAIL = "FRM_SPECIAL_JOURNAL_DETAIL";
    
    public static final int FRM_DESCRIPTION = 0;
    public static final int FRM_AMOUNT = 1;
    public static final int FRM_AMOUNT_STATUS = 2;
    public static final int FRM_CURRENCY_RATE = 3;
    public static final int FRM_JOURNAL_MAIN_ID = 4;
    public static final int FRM_ID_PERKIRAAN = 5;
    public static final int FRM_CONTACT_ID = 6;
    public static final int FRM_CURRENCY_TYPE_ID = 7;
    public static final int FRM_DEPARTMENT_ID = 8;
    public static final int FRM_BUDGET_BALANCE = 9;
    
    public static String[] fieldNames = {
        "DESCRIPTION",
        "AMOUNT",
        "AMOUNT_STATUS",
        "CURRENCY_RATE",
        "JOURNAL_MAIN_ID",
        "ID_PERKIRAAN",
        "CONTACT_ID",
        "CURRENCY_TYPE_ID",
        "DEPARTMENT_ID",
        "BUDGET_BALANCE"
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_FLOAT
    };
    
    private SpecialJournalDetail objSpecialJournalDetail;
    
    /** Creates a new instance of FrmSpecialJournalDetail */
    public FrmSpecialJournalDetail(SpecialJournalDetail objSpecialJournalDetail) {
        this.objSpecialJournalDetail = objSpecialJournalDetail;
    }
    
    public FrmSpecialJournalDetail(HttpServletRequest request, SpecialJournalDetail objSpecialJournalDetail) {
        super(new FrmSpecialJournalDetail(objSpecialJournalDetail), request);
        this.objSpecialJournalDetail = objSpecialJournalDetail;
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
        return FRM_SPECIAL_JOURNAL_DETAIL;
    }
    
    public SpecialJournalDetail getEntityObject(){
        return objSpecialJournalDetail;
    }
    
    public void requestEntityObject(SpecialJournalDetail objSpecialJournalDetail){
        try{
            this.requestParam();
            
            objSpecialJournalDetail.setDescription(this.getString(FRM_DESCRIPTION));
            objSpecialJournalDetail.setAmount(this.getDouble(FRM_AMOUNT));
            objSpecialJournalDetail.setAmountStatus(this.getInt(FRM_AMOUNT_STATUS));
            objSpecialJournalDetail.setCurrencyRate(this.getDouble(FRM_CURRENCY_RATE));
            objSpecialJournalDetail.setJournalMainId(this.getLong(FRM_JOURNAL_MAIN_ID));
            objSpecialJournalDetail.setIdPerkiraan(this.getLong(FRM_ID_PERKIRAAN));
            objSpecialJournalDetail.setContactId(this.getLong(FRM_CONTACT_ID));
            objSpecialJournalDetail.setCurrencyTypeId(this.getLong(FRM_CURRENCY_TYPE_ID));
            objSpecialJournalDetail.setDepartmentId(this.getLong(FRM_DEPARTMENT_ID));
            objSpecialJournalDetail.setBudgetBalance(this.getDouble(FRM_BUDGET_BALANCE));
            
            this.objSpecialJournalDetail = objSpecialJournalDetail;
        }catch(Exception e){
            System.out.println("Exception pd request entity object ===> "+e.toString());
        }    
    }
}
