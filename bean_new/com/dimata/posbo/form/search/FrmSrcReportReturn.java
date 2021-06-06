package com.dimata.posbo.form.search;

/* java package */ 
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.posbo.entity.search.*;

public class FrmSrcReportReturn extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcReportReturn srcReportReturn;

	public static final String FRM_NAME_SRCREPORTRETURN    =  "FRM_NAME_SRCREPORTRETURN" ;

	public static final int FRM_FIELD_LOCATION_ID       =  0 ;
	public static final int FRM_FIELD_SHIFT_ID          =  1 ;
	public static final int FRM_FIELD_OPERATOR_ID       =  2 ;
	public static final int FRM_FIELD_SUPPLIER_ID       =  3 ;
	public static final int FRM_FIELD_CATEGORY_ID       =  4 ;
	public static final int FRM_FIELD_SUB_CATEGORY_ID   =  5 ;
	public static final int FRM_FIELD_DATE_FROM         =  6 ;
	public static final int FRM_FIELD_DATE_TO           =  7 ;
	public static final int FRM_FIELD_SORT_BY           =  8 ;
	public static final int FRM_FIELD_RETURN_SOURCE     =  9 ;
	public static final int FRM_FIELD_RETURN_TO         =  10 ;
	public static final int FRM_FIELD_RETURN_REASON     =  11;
        public static final int FRM_FIELD_STATUS_DATE     =  12;

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
            "FRM_FIELD_RETURN_SOURCE",
            "FRM_FIELD_RETURN_TO",
            "FRM_FIELD_RETURN_REASON",
            "FRM_FIELD_STATUS_DATE"
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
            TYPE_INT,
            TYPE_LONG,
            TYPE_INT,
            TYPE_INT
	} ;

	public FrmSrcReportReturn()
        {
	}
	
        public FrmSrcReportReturn(SrcReportReturn srcReportReturn)
        {
            this.srcReportReturn = srcReportReturn;
	}

	public FrmSrcReportReturn(HttpServletRequest request, SrcReportReturn srcReportReturn)
        {
            super(new FrmSrcReportReturn(srcReportReturn), request);
            this.srcReportReturn = srcReportReturn;
	}

	public String getFormName() { return FRM_NAME_SRCREPORTRETURN; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcReportReturn getEntityObject(){ return srcReportReturn; }

	public void requestEntityObject(SrcReportReturn srcReportReturn) 
        {
            try
            {
                this.requestParam();
                srcReportReturn.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                srcReportReturn.setShiftId(getLong(FRM_FIELD_SHIFT_ID));
                srcReportReturn.setOperatorId(getLong(FRM_FIELD_OPERATOR_ID));
                srcReportReturn.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
                srcReportReturn.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
                srcReportReturn.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
                srcReportReturn.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
                srcReportReturn.setDateTo(getDate(FRM_FIELD_DATE_TO));
                srcReportReturn.setSortBy(getInt(FRM_FIELD_SORT_BY));
                srcReportReturn.setReturnSource(getInt(FRM_FIELD_RETURN_SOURCE));
                srcReportReturn.setReturnTo(getLong(FRM_FIELD_RETURN_TO));
                srcReportReturn.setReturnReason(getInt(FRM_FIELD_RETURN_REASON));
                srcReportReturn.setStatusDate(getInt(FRM_FIELD_STATUS_DATE));
            }
            catch(Exception e)
            {
                    System.out.println("Error on requestEntityObject : "+e.toString());
            }
	}
}
