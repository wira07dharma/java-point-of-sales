package com.dimata.pos.form.search;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
import com.dimata.pos.entity.search.*; 
import com.dimata.pos.entity.billing.*;
/* project package */

public class FrmSrcPendingOrder extends FRMHandler implements I_FRMInterface, I_FRMType
{
	private SrcPendingOrder srcPendingOrder;

        public static final String FRM_NAME_SRC_PENDING_ORDER = "FRM_NAME_SRC_PENDING_ORDER";
	
	public static final int FRM_FIELD_BOX_ORDER_CODE		=  0 ;
	public static final int FRM_FIELD_CUSTOMER_NAME		=  1 ;
	public static final int FRM_FIELD_DATE_CREATED_FROM		=  2 ;
	public static final int FRM_FIELD_DATE_CREATED_TO		=  3 ;
	public static final int FRM_FIELD_DATE_EXPIRED_FROM		=  4 ;
	public static final int FRM_FIELD_DATE_EXPIRED_TO	=  5 ;
	public static final int FRM_FIELD_DATE_FINISHED_FROM		=  6 ;
	public static final int FRM_FIELD_DATE_FINISHED_TO		=  7 ;
	public static final int FRM_FIELD_DOWN_PAYMENT_VALUE		=  8 ;
	public static final int FRM_FIELD_LOCATION_ID    		=  9 ;
	public static final int FRM_FIELD_OPERATOR_ID		=  10;
        public static final int FRM_FIELD_SALES_NAME		=  11;
        public static final int FRM_FIELD_SHIFT_ID		=  12;
        public static final int FRM_FIELD_SORT_METHOD		=  13;
        
        public static final int FRM_FIELD_LOCATION_NAME    		=  14 ;
        public static final int FRM_FIELD_OPERATOR_NAME		=  15;
        public static final int FRM_FIELD_SALES_ID		=  16;
        public static final int FRM_FIELD_SHIFT_NAME		=  17;
        
        public static final int FRM_FIELD_MEMBER_ID		=  18;
        public static final int FRM_FIELD_MEMBER_NAME		=  19;
        
        public static final int FRM_FIELD_PAID_INV_NUMBER		=  19;
        public static final int FRM_FIELD_PAID_INV_ID		=  20;
        
        
        public static final int SORT_BY_DATE_CREATED=0;
        public static final int SORT_BY_DATE_EXPIRED=1;
        public static final int SORT_BY_DATE_FINISHED=2;
        public static final int SORT_BY_CUSTOMER_NAME=3;
        public static final int SORT_BY_BOX_ORDER_CODE=4;
    
    public static final String sortTitle[][]={ 
        {"DATE CREATED","DATE EXPIRED","DATE FINISHED","CUSTOMER NAME","ORDER NUMBER"},
        {"TANGGAL PEMBUATAN","TANGGAL HANGUS","TANGGAL SELESAI","NAMA CUSTOMER","NOMOR ORDER"} 
    };
    
    public static final String sortMap[]={
        PstPendingOrder.fieldNames[PstPendingOrder.FLD_CREATION_DATE],
        PstPendingOrder.fieldNames[PstPendingOrder.FLD_EXPIRED_DATE], 
        PstPendingOrder.fieldNames[PstPendingOrder.FLD_PLAN_TAKEN_DATE], 
        PstPendingOrder.fieldNames[PstPendingOrder.FLD_NAME], 
        PstPendingOrder.fieldNames[PstPendingOrder.FLD_ORDER_NUMBER]
    }; 
    
    public static final String reportType[][]={
        {"OPEN","LUNAS","EXPIRED","HANGUS"},
        {"OPEN","CLOSE","EXPIRED","DELETED"}
    };
    
	public static String[] fieldNames = 
        {
            "FRM_FIELD_BOX_ORDER_CODE",
            "FRM_FIELD_CUSTOMER_NAME",
            "FRM_FIELD_DATE_CREATED_FROM",
            "FRM_FIELD_DATE_CREATED_TO",
            "FRM_FIELD_DATE_EXPIRED_FROM",
            "FRM_FIELD_DATE_EXPIRED_TO",
            "FRM_FIELD_DATE_FINISHED_FROM",
            "FRM_FIELD_DATE_FINISHED_TO",
            "FRM_FIELD_DOWN_PAYMENT_VALUE",
            "FRM_FIELD_LOCATION_ID",
            "FRM_FIELD_OPERATOR_ID",
            "FRM_FIELD_SALES_NAME",
            "FRM_FIELD_SHIFT_ID",
            "FRM_FIELD_SORT_METHOD",
            
            "FRM_FIELD_LOCATION_NAME",
            "FRM_FIELD_OPERATOR_NAME",
            "FRM_FIELD_SALES_ID",
            "FRM_FIELD_SHIFT_NAME",
            
            "FRM_FIELD_MEMBER_ID",
            "FRM_FIELD_MEMBER_NAME",
            "FRM_FIELD_PAID_INV_ID",
            "FRM_FIELD_PAID_INV_NUMBER"
            
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_STRING,
            TYPE_STRING,
            TYPE_DATE,
            TYPE_DATE,
            TYPE_DATE,
            TYPE_DATE,
            TYPE_DATE,
            TYPE_DATE,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_STRING,
            TYPE_LONG,
            TYPE_INT,
            
            TYPE_STRING,
            TYPE_STRING,
            TYPE_LONG,
            TYPE_STRING,
            
            TYPE_LONG,
            TYPE_STRING,
            
            TYPE_LONG,
            TYPE_STRING
	} ;

	public FrmSrcPendingOrder ()
        {
	}
	
        public FrmSrcPendingOrder (SrcPendingOrder srcPendingOrder)
        {
            this.srcPendingOrder = srcPendingOrder;
	}

	public FrmSrcPendingOrder (HttpServletRequest request, SrcPendingOrder srcPendingOrder)
        {
            super(new FrmSrcPendingOrder(srcPendingOrder), request);
            this.srcPendingOrder = srcPendingOrder;
	}

	public String getFormName() { return FRM_NAME_SRC_PENDING_ORDER; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcPendingOrder getEntityObject(){ return srcPendingOrder; }

	public void requestEntityObject(SrcPendingOrder srcPendingOrder) 
        {
            try
            {
                this.requestParam();
                srcPendingOrder.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                srcPendingOrder.setShiftId(getLong(FRM_FIELD_SHIFT_ID));
                srcPendingOrder.setOperatorId(getLong(FRM_FIELD_OPERATOR_ID));
                
                srcPendingOrder.setBoxOrderCode (getString(FRM_FIELD_BOX_ORDER_CODE));
                srcPendingOrder.setCustomerName(getString(FRM_FIELD_CUSTOMER_NAME));
                srcPendingOrder.setDateCreatedFrom (getDate(FRM_FIELD_DATE_CREATED_FROM));
                srcPendingOrder.setDateCreatedTo(getDate(FRM_FIELD_DATE_CREATED_TO));
                srcPendingOrder.setDateExpiredFrom (getDate(FRM_FIELD_DATE_EXPIRED_FROM));
                srcPendingOrder.setDateExpiredTo (getDate(FRM_FIELD_DATE_EXPIRED_TO));
                srcPendingOrder.setDateFinishedFrom (getDate(FRM_FIELD_DATE_FINISHED_FROM));
                srcPendingOrder.setDateFinishedTo(getDate(FRM_FIELD_DATE_FINISHED_TO)); 
                srcPendingOrder.setDownaPaymentValue (getDouble(FRM_FIELD_DOWN_PAYMENT_VALUE));     
                srcPendingOrder.setSalesName(getString(FRM_FIELD_SALES_NAME));   
                srcPendingOrder.setSortMethod (getInt(FRM_FIELD_SORT_METHOD));   
                
                srcPendingOrder.setOperatorName (getString(FRM_FIELD_OPERATOR_NAME)); 
                srcPendingOrder.setLocationName (getString(FRM_FIELD_LOCATION_NAME));     
                srcPendingOrder.setSalesId (getLong(FRM_FIELD_SALES_ID));   
                srcPendingOrder.setShiftName (getString(FRM_FIELD_SHIFT_NAME));  
                
                srcPendingOrder.setMemberId (getLong(FRM_FIELD_MEMBER_ID));   
                srcPendingOrder.setMemberName (getString(FRM_FIELD_MEMBER_NAME));  
                
                srcPendingOrder.setPaidInvoiceId (getLong(FRM_FIELD_PAID_INV_ID));   
                srcPendingOrder.setPaidInvoiceNumber (getString(FRM_FIELD_PAID_INV_NUMBER));  
                
                
            }
            catch(Exception e)
            {
                    System.out.println("Error on requestEntityObject : "+e.toString()); 
            }
	}
        
        public static String toString(SrcPendingOrder srcPendingOrder){
        StringBuffer strBuff = new StringBuffer();
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_BOX_ORDER_CODE]+" : "+srcPendingOrder.getBoxOrderCode ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_CUSTOMER_NAME]+" : "+srcPendingOrder.getCustomerName ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_CREATED_FROM]+" : "+srcPendingOrder.getDateCreatedFrom ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_CREATED_TO]+" : "+srcPendingOrder.getDateCreatedTo ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_EXPIRED_FROM]+" : "+srcPendingOrder.getDateExpiredFrom ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_EXPIRED_TO]+" : "+srcPendingOrder.getDateExpiredTo ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_FINISHED_FROM]+" : "+srcPendingOrder.getDateFinishedFrom ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DATE_FINISHED_FROM]+" : "+srcPendingOrder.getDateFinishedTo ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_DOWN_PAYMENT_VALUE]+" : "+srcPendingOrder.getDownaPaymentValue ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_LOCATION_ID]+" : "+srcPendingOrder.getLocationId ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_LOCATION_NAME]+" : "+srcPendingOrder.getLocationName ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_MEMBER_ID]+" : "+srcPendingOrder.getMemberId ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_MEMBER_NAME]+" : "+srcPendingOrder.getMemberName ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_OPERATOR_ID]+" : "+srcPendingOrder.getOperatorId ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_OPERATOR_NAME]+" : "+srcPendingOrder.getOperatorName ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SALES_ID]+" : "+srcPendingOrder.getSalesId ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SALES_NAME]+" : "+srcPendingOrder.getSalesName ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SHIFT_ID]+" : "+srcPendingOrder.getShiftId ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SHIFT_NAME]+" : "+srcPendingOrder.getShiftName ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_SORT_METHOD]+" : "+srcPendingOrder.getSortMethod ()+"\n");
        
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_PAID_INV_ID]+" : "+srcPendingOrder.getPaidInvoiceId ()+"\n");
        strBuff.append (FrmSrcPendingOrder.fieldNames[FrmSrcPendingOrder.FRM_FIELD_PAID_INV_NUMBER]+" : "+srcPendingOrder.getPaidInvoiceNumber ()+"\n");
        
        String result = new String(strBuff);
        return result; 
        
        
    }
}
