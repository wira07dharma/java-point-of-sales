package com.dimata.posbo.form.warehouse;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.warehouse.MatConReturn;

public class FrmMatConReturn extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private MatConReturn matReturn;

	public static final String FRM_NAME_MATRETURN		=  "FRM_NAME_MATRETURN" ;

	public static final int FRM_FIELD_RETURN_MATERIAL_ID	=  0 ;
	public static final int FRM_FIELD_LOCATION_ID		=  1 ;
	public static final int FRM_FIELD_LOCATION_TYPE		=  2 ;
	public static final int FRM_FIELD_RETURN_TO		=  3 ;
	public static final int FRM_FIELD_RETURN_DATE		=  4 ;
	public static final int FRM_FIELD_RET_CODE		=  5 ;
	public static final int FRM_FIELD_RET_CODE_CNT		=  6 ;
	public static final int FRM_FIELD_RETURN_STATUS		=  7 ;
	public static final int FRM_FIELD_RETURN_SOURCE		=  8 ;
	public static final int FRM_FIELD_SUPPLIER_ID		=  9 ;
	public static final int FRM_FIELD_PURCHASE_ORDER_ID	=  10 ;
	public static final int FRM_FIELD_RECEIVE_MATERIAL_ID	=  11 ;
	public static final int FRM_FIELD_REMARK		=  12 ;
	public static final int FRM_FIELD_RETURN_REASON		=  13 ;
        public static final int FRM_FIELD_INVOICE_SUPPLIER      =  14 ;
        
	public static String[] fieldNames = 
        {
            "FRM_FIELD_RETURN_MATERIAL_ID",
            "FRM_FIELD_LOCATION_ID",
            "FRM_FIELD_LOCATION_TYPE",
            "FRM_FIELD_RETURN_TO",
            "FRM_FIELD_RETURN_DATE",
            "FRM_FIELD_RET_CODE",
            "FRM_FIELD_RET_CODE_CNT",
            "FRM_FIELD_RETURN_STATUS",
            "FRM_FIELD_RETURN_SOURCE",
            "FRM_FIELD_SUPPLIER_ID",
            "FRM_FIELD_PURCHASE_ORDER_ID",
            "FRM_FIELD_RECEIVE_MATERIAL_ID",
            "FRM_FIELD_REMARK",
            "FRM_FIELD_RETURN_REASON",
            "FRM_FIELD_INVOICE_SUPPLIER"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_LONG,
            TYPE_LONG,
            TYPE_INT,
            TYPE_LONG,
            TYPE_DATE,
            TYPE_STRING,
            TYPE_INT,
            TYPE_INT,
            TYPE_INT,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_STRING,
            TYPE_INT,
            TYPE_STRING
	} ;

	public FrmMatConReturn()
        {
	}
	
        public FrmMatConReturn(MatConReturn matReturn)
        {
            this.matReturn = matReturn;
	}

	public FrmMatConReturn(HttpServletRequest request, MatConReturn matReturn)
        {
            super(new FrmMatConReturn(matReturn), request);
            this.matReturn = matReturn;
	}

	public String getFormName() { return FRM_NAME_MATRETURN; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public MatConReturn getEntityObject(){ return matReturn; }

	public void requestEntityObject(MatConReturn matReturn) 
        {
            try
            {
		this.requestParam();
		matReturn.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
		matReturn.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
		matReturn.setReturnTo(getLong(FRM_FIELD_RETURN_TO));
		matReturn.setReturnDate(getDate(FRM_FIELD_RETURN_DATE));
		matReturn.setRetCode(getString(FRM_FIELD_RET_CODE));
		matReturn.setRetCodeCnt(getInt(FRM_FIELD_RET_CODE_CNT));
		matReturn.setReturnStatus(getInt(FRM_FIELD_RETURN_STATUS));
		matReturn.setReturnSource(getInt(FRM_FIELD_RETURN_SOURCE));
		matReturn.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
		matReturn.setPurchaseOrderId(getLong(FRM_FIELD_PURCHASE_ORDER_ID));
		matReturn.setReceiveMaterialId(getLong(FRM_FIELD_RECEIVE_MATERIAL_ID));
		matReturn.setRemark(getString(FRM_FIELD_REMARK));
		matReturn.setReturnReason(getInt(FRM_FIELD_RETURN_REASON));
                matReturn.setInvoiceSupplier(getString(FRM_FIELD_INVOICE_SUPPLIER));
            }
            catch(Exception e)
            {
		System.out.println("Error on requestEntityObject : "+e.toString());
            }
	}
}
