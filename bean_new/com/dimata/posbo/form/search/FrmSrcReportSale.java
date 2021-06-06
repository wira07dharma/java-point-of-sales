package com.dimata.posbo.form.search;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.posbo.entity.search.*;

public class FrmSrcReportSale extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcReportSale srcReportSale;

	public static final String FRM_NAME_SRCREPORTSALE    =  "FRM_NAME_SRCREPORTSALE" ;

	public static final int FRM_FIELD_LOCATION_ID		=  0 ;
	public static final int FRM_FIELD_SHIFT_ID		=  1 ;
	public static final int FRM_FIELD_OPERATOR_ID		=  2 ;
	public static final int FRM_FIELD_SUPPLIER_ID		=  3 ;
	public static final int FRM_FIELD_CATEGORY_ID		=  4 ;
	public static final int FRM_FIELD_SUB_CATEGORY_ID	=  5 ;
	public static final int FRM_FIELD_DATE_FROM		=  6 ;
	public static final int FRM_FIELD_DATE_TO		=  7 ;
	public static final int FRM_FIELD_SORT_BY		=  8 ;
	public static final int FRM_FIELD_SALES_CODE		=  9 ;
	public static final int FRM_FIELD_CURRENCY_ID		=  10;
        public static final int FRM_FIELD_SUPPLIER_TYPE		=  11;
        public static final int FRM_FIELD_DOC_TYPE		=  12;
	public static String[] fieldNames = 
        {
            "FRM_FIELD_LOCATION_ID",
            "FRM_FIELD_SHIFT_ID",
            "FRM_FIELD_OPERATOR_ID",
            "FRM_FIELD_SUPPLIER_ID",
            "FRM_FIELD_CATEGORY_ID",
            "FRM_FIELD_SUB_CATEGORY_ID",
            "FRM_FIELD_DATE_FROM",
            "FRM_FIELD_DATE_TO",
            "FRM_FIELD_SORTBY",
            "FRM_FIELD_SALES_CODE",
            "FRM_FIELD_CURRENCY_ID",
            "FRM_FIELD_SUPPLIER_TYPE",
            "FRM_FIELD_DOC_TYPE"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_DATE,
            TYPE_DATE,
            TYPE_INT,
            TYPE_STRING,
            TYPE_LONG,
            TYPE_INT,
            TYPE_INT
	} ;

	public FrmSrcReportSale()
        {
	}
	
        public FrmSrcReportSale(SrcReportSale srcReportSale)
        {
            this.srcReportSale = srcReportSale;
	}

	public FrmSrcReportSale(HttpServletRequest request, SrcReportSale srcReportSale)
        {
            super(new FrmSrcReportSale(srcReportSale), request);
            this.srcReportSale = srcReportSale;
	}

	public String getFormName() { return FRM_NAME_SRCREPORTSALE; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcReportSale getEntityObject(){ return srcReportSale; }

	public void requestEntityObject(SrcReportSale srcReportSale) 
        {
            try
            {
                this.requestParam();
                srcReportSale.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                srcReportSale.setShiftId(getLong(FRM_FIELD_SHIFT_ID));
                srcReportSale.setOperatorId(getLong(FRM_FIELD_OPERATOR_ID));
                srcReportSale.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
                srcReportSale.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
                srcReportSale.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
                srcReportSale.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
                srcReportSale.setDateTo(getDate(FRM_FIELD_DATE_TO));
                srcReportSale.setSortBy(getInt(FRM_FIELD_SORT_BY));
                srcReportSale.setSalesCode(getString(FRM_FIELD_SALES_CODE));
                srcReportSale.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
                srcReportSale.setSupplierType(getInt(FRM_FIELD_SUPPLIER_TYPE));   
                srcReportSale.setDocType (getInt(FRM_FIELD_DOC_TYPE));   
            }
            catch(Exception e)
            {
                    System.out.println("Error on requestEntityObject : "+e.toString()); 
            }
	}
}
