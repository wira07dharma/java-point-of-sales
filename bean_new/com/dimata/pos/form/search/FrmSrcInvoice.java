/*
 * FrmSrcInvoice.java
 *
 * Created on February 25, 2005, 5:57 PM
 */

package com.dimata.pos.form.search;

/* java package */ 
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
import com.dimata.pos.entity.search.*; 
import static com.dimata.qdep.form.I_FRMType.TYPE_INT;
/* project package */



//public class FrmSrcPendingOrder  
/**
 *
 * @author  wpradnyana
 */
public class FrmSrcInvoice extends FRMHandler implements I_FRMInterface, I_FRMType
{
    
    public static final String FRM_NAME_SRC_INVOICE="FRM_NAME_SRC_INVOICE";
    
    SrcInvoice srcInvoice;
    public static final int FRM_FLD_CUSTOMER_NAME=0;    
      public static final int FRM_FLD_INVOICE_DATE=1;
      public static final int FRM_FLD_INVOICE_DATE_TO=2;
      public static final int FRM_FLD_INVOICE_ID=3;
      public static final int FRM_FLD_INVOICE_NUMBER=4;
      public static final int FRM_FLD_MEMBER_ID=5;
      public static final int FRM_FLD_MEMBER_NAME=6;
      public static final int FRM_FLD_TRANSACTION_POIN=7;
      public static final int FRM_FLD_TRANSACTION_STATUS=8;
      public static final int FRM_FLD_SALES_PERSON=9;
      public static final int FRM_FLD_SALES_ID=10;
      public static final int FRM_FLD_SALES_NAME=11;
      public static final int FRM_FLD_MATERIAL_ID=12;
      public static final int FRM_FLD_MATERIAL_NAME=13;
      public static final int FRM_FLD_SKU=14;
      public static final int FRM_FLD_CURRENCY_ID=15;
      public static final int FRM_FLD_STATUS_DATE=16;
      public static final int FRM_FLD_PRMSTATUS=17;
      
      /**
       * Ari wiweka
       * 20130724
       *
       */
      public static final int FRM_FLD_DOC_TYPE=18;
      public static final int FRM_FLD_TRANSACTION_TYPE=19;
      public static final int FRM_FLD_SORTBY=20;
      public static final int FRM_FLD_SALES_CODE=21;
         // update by mchen | 2014-12-09
      public static final int FRM_FLD_DRAFF = 22;
      public static final int FRM_FLD_ONPROCESS = 23;
      public static final int FRM_FLD_DONE = 24;
    

    public static String[] fieldNames={
      "FRM_FLD_CUSTOMER_NAME",
      "FRM_FLD_INVOICE_DATE",
      "FRM_FLD_INVOICE_DATE_TO",
      "FRM_FLD_INVOICE_ID",
      "FRM_FLD_INVOICE_NUMBER",
      "FRM_FLD_MEMBER_ID",
      "FRM_FLD_MEMBER_NAME",
      "FRM_FLD_TRANSACTION_POIN",
      "FRM_FLD_TRANSACTION_STATUS",
      "FRM_FLD_SALES_PERSON",
      "FRM_FLD_SALES_ID",
      "FRM_FLD_SALES_NAME",
      "FRM_FLD_MATERIAL_ID",
      "FRM_FLD_MATERIAL_NAME",
      "FRM_FLD_SKU",
      "FRM_FLD_CURRENCY_ID",
      "FRM_FLD_STATUS_DATE",
      "FRM_FLD_PRMSTATUS",
      
      "FRM_FLD_DOC_TYPE",
      "FRM_FLD_TRANSACTION_TYPE",
      "FRM_FLD_SORTBY",
      "FRM_FLD_SALES_CODE",
      // update by mchen | 2014-12-09
      "FRM_FLD_DRAFF",
      "FRM_FLD_ONPROCESS",
      "FRM_FLD_DONE"
      

    };
    public static int[] fieldTypes={
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        // update by mchen | 2014-12-09
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };
    
    /** Creates a new instance of FrmSrcInvoice */
    public FrmSrcInvoice ()
    {
    }
    
     public FrmSrcInvoice (SrcInvoice srcInvoice) 
        {
            this.srcInvoice = srcInvoice; 
	}

	public FrmSrcInvoice (HttpServletRequest request, SrcInvoice srcInvoice)
        {
            super(new FrmSrcInvoice(srcInvoice), request);
            this.srcInvoice = srcInvoice;
	}
    public String[] getFieldNames ()
    {
        return fieldNames;
        //throw new UnsupportedOperationException ();
    }
    
    public int getFieldSize ()
    {
        return fieldNames.length;
        //throw new UnsupportedOperationException ();
    }
    
    public int[] getFieldTypes ()
    {
        return fieldTypes;
        //throw new UnsupportedOperationException ();
    }
    
    public String getFormName ()
    {
        return FRM_NAME_SRC_INVOICE; 
        //;throw new UnsupportedOperationException ();
    }
    
    public void requestEntityObject(SrcInvoice srcInvoice){
        try{
            this.requestParam ();
             srcInvoice.setCustomerName (getString (FRM_FLD_CUSTOMER_NAME)); 
            srcInvoice.setInvoiceDate (getDate(FRM_FLD_INVOICE_DATE));
            srcInvoice.setInvoiceDateTo (getDate(FRM_FLD_INVOICE_DATE_TO));
            srcInvoice.setInvoiceId (getLong (FRM_FLD_INVOICE_ID));
            srcInvoice.setInvoiceNumber (getString (FRM_FLD_INVOICE_NUMBER));
            srcInvoice.setMemberId (getLong (FRM_FLD_MEMBER_ID));
            srcInvoice.setMemberName (getString(FRM_FLD_MEMBER_NAME));
            srcInvoice.setTransactionPoin (getInt(FRM_FLD_TRANSACTION_POIN));
            srcInvoice.setTransStatus(getInt(FRM_FLD_TRANSACTION_STATUS)); 
            srcInvoice.setSalesPerson(getString(FRM_FLD_SALES_PERSON));
            srcInvoice.setSalesId(getLong(FRM_FLD_SALES_ID));
            srcInvoice.setSalesName(getString(FRM_FLD_SALES_NAME));
            srcInvoice.setMaterialId(getLong(FRM_FLD_MATERIAL_ID));
            srcInvoice.setMatName(getString(FRM_FLD_MATERIAL_NAME));
            srcInvoice.setSku(getString(FRM_FLD_SKU));
            srcInvoice.setCurrId(getLong(FRM_FLD_CURRENCY_ID));
            srcInvoice.setStatusDate(getInt(FRM_FLD_STATUS_DATE));
            
            srcInvoice.setDocType(getInt(FRM_FLD_DOC_TYPE));
            srcInvoice.setTransType(getInt(FRM_FLD_TRANSACTION_TYPE));
            srcInvoice.setSortBy(getInt(FRM_FLD_SORTBY));
            srcInvoice.setSalesCode(getString(FRM_FLD_SALES_CODE));
            // update by mchen | 2014-12-09
            srcInvoice.setStatusDraff(getInt(FRM_FLD_DRAFF));
            srcInvoice.setStatusOnProcess(getInt(FRM_FLD_ONPROCESS));
            srcInvoice.setStatusDone(getInt(FRM_FLD_DONE));
        }catch(Exception e){
            e.printStackTrace(); 
        }
    }
}
