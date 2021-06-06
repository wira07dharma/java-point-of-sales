/*
 * FrmIjJournal.java
 *
 * Created on February 21, 2005, 8:35 AM
 */

package com.dimata.ij.form.search;

// package java
import java.util.*;

//  package javax
import javax.servlet.*;
import javax.servlet.http.*;

// package dimata
import com.dimata.util.*;

// import qdep
import com.dimata.qdep.form.*;

// import ij
import com.dimata.ij.entity.search.*;

/**
 *
 * @author  gedhy
 */
public class FrmSrcIjJournal extends FRMHandler implements I_FRMInterface, I_FRMType 
{    

    public static final String FRM_SEARCH_IJ_JOURNAL	= "FRM_SEARCH_IJ_JOURNAL";
    
    public static final int FRM_SELECTED_TRANS_DATE	= 0;
    public static final int FRM_TRANS_START_DATE	= 1;
    public static final int FRM_TRANS_END_DATE		= 2;
    public static final int FRM_BILL_NUMBER		= 3;
    public static final int FRM_CONTACT_NAME		= 4;
    public static final int FRM_TRANS_CURRENCY		= 5;
    public static final int FRM_JOURNAL_STATUS		= 6;
    public static final int FRM_ORDER_BY		= 7;
    
    public static String[] fieldNames = {
       "FRM_SELECTED_TRANS_DATE",
       "FRM_TRANS_START_DATE",
       "FRM_TRANS_END_DATE",
       "FRM_BILL_NUMBER",
       "FRM_CONTACT_NAME",
       "FRM_TRANS_CURRENCY",
       "FRM_JOURNAL_STATUS",
       "FRM_ORDER_BY"
    } ;

    public static int[] fieldTypes = {
       TYPE_INT,
       TYPE_DATE,
       TYPE_DATE,
       TYPE_STRING,   
       TYPE_STRING,
       TYPE_LONG,
       TYPE_INT,
       TYPE_INT
    };

    public static final int SELECTED_ALL_DATE = 0 ;    
    public static final int SELECTED_USER_DATE = 1 ;    
    
    public static final int SORT_BY_TRANS_DATE      = 0;
    public static final int SORT_BY_BILL_NUMBER     = 1;        
    public static final int SORT_BY_CURRENCY        = 2;
    public static final int SORT_BY_JOURNAL_STATUS  = 3;
    public static String[][] sortFieldNames = 
    {
        {"Tanggal Transaksi","Nomor Bill","Mata Uang","Status Jurnal"},
        {"Transaction Date","Bill Number","Transaction Currency","Journal Status"}
    };

    private SrcIjJournal srcIjJournal = new SrcIjJournal();

    public FrmSrcIjJournal()
    { 
    }
    
    public FrmSrcIjJournal(HttpServletRequest request) {
        super(new FrmSrcIjJournal(), request);
    }

    public String getFormName() {
        return this.FRM_SEARCH_IJ_JOURNAL;
    }    
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }    
    
    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public SrcIjJournal getEntityObject()
    {
        return srcIjJournal;
    }

    public void requestEntityObject(SrcIjJournal srcIjJournal)
    {        
        try 
        {
            this.requestParam();

            srcIjJournal.setSelectedTransDate(getInt(FRM_SELECTED_TRANS_DATE));
            srcIjJournal.setTransStartDate(getDate(FRM_TRANS_START_DATE));
            srcIjJournal.setTransEndDate(getDate(FRM_TRANS_END_DATE));
            srcIjJournal.setBillNumber(getString(FRM_BILL_NUMBER));
            srcIjJournal.setContactName(getString(FRM_CONTACT_NAME));
            srcIjJournal.setTransCurrency(getLong(FRM_TRANS_CURRENCY));
            srcIjJournal.setJournalStatus(getInt(FRM_JOURNAL_STATUS));
            srcIjJournal.setSortBy(getInt(FRM_ORDER_BY));

            this.srcIjJournal = srcIjJournal;
        }
        catch(Exception e) 
        {
            System.out.println("EXC...");
            srcIjJournal = new SrcIjJournal();
        }       
    }
    
}
