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

public class FrmSrcReportDispatch extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcReportDispatch srcReportDispatch;

	public static final String FRM_NAME_SRCREPORTDISPATCH    =  "FRM_NAME_SRCREPORTDISPATCH" ;

	public static final int FRM_FIELD_LOCATION_ID		=  0 ;
	public static final int FRM_FIELD_SUPPLIER_ID		=  1 ;
	public static final int FRM_FIELD_CATEGORY_ID		=  2 ;
	public static final int FRM_FIELD_SUB_CATEGORY_ID	=  3 ;
	public static final int FRM_FIELD_DATE_FROM		=  4 ;
	public static final int FRM_FIELD_DATE_TO		=  5 ;
	public static final int FRM_FIELD_SORT_BY		=  6 ;
	public static final int FRM_FIELD_DISPATCH_TO           =  7 ;
        //add frm costing_Id
        public static final int FRM_FIELD_COSTING_ID            =  8 ;
        // add By Fitra 06-06-2014
         public static final int FRM_FIELD_STATUSDATE            =  9 ;

	public static String[] fieldNames = 
        {
            "FRM_FIELD_LOCATION_ID",
            "FRM_FIELD_SUPPLIER_ID",
            "FRM_FIELD_CATEGORY_ID",
            "FRM_FIELD_SUB_CATEGORY_ID",
            "FRM_FIELD_DATE_FROM",
            "FRM_FIELD_DATE_TO",
            "FRM_FIELD_SORTBY",
            "FRM_FIELD_DISPATCH_TO",
            //add field costing id
            "FRM_FIELD_COSTING_ID",
            "FRM_FIELD_STATUSDATE"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_DATE,
            TYPE_DATE,
            TYPE_INT,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_INT
	} ;

	public FrmSrcReportDispatch()
        {
	}
	
        public FrmSrcReportDispatch(SrcReportDispatch srcReportDispatch)
        {
            this.srcReportDispatch = srcReportDispatch;
	}

	public FrmSrcReportDispatch(HttpServletRequest request, SrcReportDispatch srcReportDispatch)
        {
            super(new FrmSrcReportDispatch(srcReportDispatch), request);
            this.srcReportDispatch = srcReportDispatch;
	}

	public String getFormName() { return FRM_NAME_SRCREPORTDISPATCH; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcReportDispatch getEntityObject(){ return srcReportDispatch; }

	public void requestEntityObject(SrcReportDispatch srcReportDispatch) 
        {
            try
            {
                this.requestParam();
                srcReportDispatch.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                srcReportDispatch.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
                srcReportDispatch.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
                srcReportDispatch.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
                srcReportDispatch.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
                srcReportDispatch.setDateTo(getDate(FRM_FIELD_DATE_TO));
                srcReportDispatch.setSortBy(getInt(FRM_FIELD_SORT_BY));
                srcReportDispatch.setDispatchTo(getLong(FRM_FIELD_DISPATCH_TO));
                srcReportDispatch.setStatusDate(getInt(FRM_FIELD_STATUSDATE));
                //srcReportDispatch.setCostingId(getLong(FRM_FIELD_COSTING_ID));
            }
            catch(Exception e)
            {
                    System.out.println("Error on requestEntityObject : "+e.toString());
            }
	}
}
