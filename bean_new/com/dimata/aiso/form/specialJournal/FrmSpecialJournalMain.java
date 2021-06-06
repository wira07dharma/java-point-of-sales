/*
 * FrmSpecialJournalMain.java
 *
 * Created on February 7, 2007, 11:41 AM
 */

package com.dimata.aiso.form.specialJournal;

/* import package aiso */
import com.dimata.aiso.entity.specialJournal.*;

/* import package javax HttpServletRequest */
import javax.servlet.http.HttpServletRequest;

/* import package qdep form */
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import java.util.StringTokenizer;

/**
 *
 * @author  dwi
 */
public class FrmSpecialJournalMain extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    public static final String FRM_SPECIAL_JOURNAL_MAIN = "FRM_SPECIAL_JOURNAL_MAIN";
    
    public static int FRM_ENTRY_DATE = 0;
    public static int FRM_TRANS_DATE = 1;
    public static int FRM_VOUCHER_NUMBER = 2;
    public static int FRM_VOUCHER_COUNTER = 3;
    public static int FRM_JOURNAL_NUMBER = 4;
    public static int FRM_REFERENCE = 5;
    public static int FRM_BOOK_TYPE = 6;
    public static int FRM_AMOUNT = 7;
    public static int FRM_AMOUNT_STATUS = 8;
    public static int FRM_JOURNAL_STATUS = 9;
    public static int FRM_BILYET_NUMBER = 10;
    public static int FRM_BILYET_DUE_DATE = 11;
    public static int FRM_ID_PERKIRAAN = 12;
    public static int FRM_DEPARTMENT_ID = 13;
    public static int FRM_PERIODE_ID = 14;
    public static int FRM_USER_ID = 15;
    public static int FRM_DESCRIPTION = 16;
    public static int FRM_CONTACT_ID = 17;
    public static int FRM_JOURNAL_TYPE = 18;
    public static int FRM_CURRENCY_TYPE_ID = 19;
    public static int FRM_STANDAR_RATE = 20;
    public static int FRM_FR_CONTACT_ID = 21;
    
    public static String[] fieldNames = {
        "ENTRY_DATE",
        "TRANS_DATE",
        "VOUCHER_NUMBER",
        "VOUCHER_COUNTER",
        "JOURNAL_NUMBER",
        "REFERENCE",
        "BOOK_TYPE",
        "AMOUNT",
        "AMOUNT_STATUS",
        "JOURNAL_STATUS",
        "BILYET_NUMBER",
        "BILYET_DUE_DATE",
        "ID_PERKIRAAN",
        "DEPARTMENT_ID",
        "PERIODE_ID",
        "USER_ID",
        "DESCRIPTION",
        "CONTACT_ID",
        "JOURNAL_TYPE",
        "CURRENCY_TYPE_ID",
        "STANDAR_RATE",
        "FR_CONTACT_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_DATE,
        TYPE_DATE + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG
    };
    
    private SpecialJournalMain objSpecialJournalMain;
    
    /** Creates a new instance of FrmSpecialJournalMain */
    public FrmSpecialJournalMain(SpecialJournalMain objSpecialJournalMain) {
        this.objSpecialJournalMain = objSpecialJournalMain;
    }
    
     public FrmSpecialJournalMain(HttpServletRequest request, SpecialJournalMain objSpecialJournalMain) {
         super(new FrmSpecialJournalMain(objSpecialJournalMain), request);
         this.objSpecialJournalMain = objSpecialJournalMain;
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
        return FRM_SPECIAL_JOURNAL_MAIN;
    }
    
    public SpecialJournalMain getEntityObject(){
        return objSpecialJournalMain;
    }
    
    public void requestEntityObject(SpecialJournalMain objSpecialJournalMain){
        try{
            this.requestParam();
            try{
           // objSpecialJournalMain.setEntryDate(this.getDate(FRM_ENTRY_DATE));
            objSpecialJournalMain.setTransDate(this.getDate(FRM_TRANS_DATE));
            //objSpecialJournalMain.setVoucherNumber(this.getString(FRM_VOUCHER_NUMBER));
            //objSpecialJournalMain.setVoucherCounter(this.getInt(FRM_VOUCHER_COUNTER));
            objSpecialJournalMain.setJournalNumber(this.getString(FRM_JOURNAL_NUMBER));
            objSpecialJournalMain.setReference(this.getString(FRM_REFERENCE));
            objSpecialJournalMain.setBookType(this.getInt(FRM_BOOK_TYPE));
            objSpecialJournalMain.setAmount(this.getDouble(FRM_AMOUNT));
            objSpecialJournalMain.setAmountStatus(this.getInt(FRM_AMOUNT_STATUS));
            objSpecialJournalMain.setBilyetNumber(this.getString(FRM_BILYET_NUMBER));
            objSpecialJournalMain.setBilyetDueDate(this.getDate(FRM_BILYET_DUE_DATE));
            objSpecialJournalMain.setIdPerkiraan(this.getLong(FRM_ID_PERKIRAAN));
            objSpecialJournalMain.setDepartmentId(this.getLong(FRM_DEPARTMENT_ID));
            //objSpecialJournalMain.setPeriodeId(this.getLong(FRM_PERIODE_ID));
           // objSpecialJournalMain.setUserId(this.getLong(FRM_USER_ID));
            objSpecialJournalMain.setDescription(this.getString(FRM_DESCRIPTION));
            objSpecialJournalMain.setContactId(this.getLong(FRM_CONTACT_ID));
            objSpecialJournalMain.setJournalType(this.getInt(FRM_JOURNAL_TYPE));
            objSpecialJournalMain.setCurrencyTypeId(this.getLong(FRM_CURRENCY_TYPE_ID));
            objSpecialJournalMain.setStandarRate(this.getDouble(FRM_STANDAR_RATE));
            objSpecialJournalMain.setFrcontactId(this.getLong(FRM_FR_CONTACT_ID));
            }catch(Exception eee){
                System.out.println("Err requestEntityObject : "+eee.toString());
            }
            this.objSpecialJournalMain = objSpecialJournalMain;
        }catch(Exception e){
            objSpecialJournalMain = new SpecialJournalMain();
        }    
    }
    
    public static void main(String[] arg){
    String x = "1,000,000.00";
    String q = "";
    StringTokenizer z = new StringTokenizer(x,",");
    while(z.hasMoreTokens()){
        q = q + z.nextToken();
    }
    System.out.println(q);
    }
}
