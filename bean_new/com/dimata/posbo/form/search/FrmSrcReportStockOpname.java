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

public class FrmSrcReportStockOpname extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcReportStockOpname srcReportStockOpname;

	public static final String FRM_NAME_SRCREPORTSTOCKOPNAME    =  "FRM_NAME_SRCREPORTSTOCKOPNAME" ;

	public static final int FRM_FIELD_LOCATION_ID		=  0 ;
	public static final int FRM_FIELD_SUPPLIER_ID		=  1 ;
	public static final int FRM_FIELD_CATEGORY_ID		=  2 ;
	public static final int FRM_FIELD_SUB_CATEGORY_ID	=  3 ;
	public static final int FRM_FIELD_DATE_FROM		=  4 ;
	public static final int FRM_FIELD_DATE_TO		=  5 ;
	public static final int FRM_FIELD_SORT_BY		=  6 ;

	public static String[] fieldNames = 
        {
            "FRM_FIELD_LOCATION_ID",
            "FRM_FIELD_SUPPLIER_ID",
            "FRM_FIELD_CATEGORY_ID",
            "FRM_FIELD_SUB_CATEGORY_ID",
            "FRM_FIELD_DATE_FROM",
            "FRM_FIELD_DATE_TO",
            "FRM_FIELD_SORTBY"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_DATE,
            TYPE_DATE,
            TYPE_INT
	} ;

	public FrmSrcReportStockOpname()
        {
	}
	
        public FrmSrcReportStockOpname(SrcReportStockOpname srcReportStockOpname)
        {
            this.srcReportStockOpname = srcReportStockOpname;
	}

	public FrmSrcReportStockOpname(HttpServletRequest request, SrcReportStockOpname srcReportStockOpname)
        {
            super(new FrmSrcReportStockOpname(srcReportStockOpname), request);
            this.srcReportStockOpname = srcReportStockOpname;
	}

	public String getFormName() { return FRM_NAME_SRCREPORTSTOCKOPNAME; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcReportStockOpname getEntityObject(){ return srcReportStockOpname; }

	public void requestEntityObject(SrcReportStockOpname srcReportStockOpname) 
        {
            try
            {
                this.requestParam();
                srcReportStockOpname.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
                srcReportStockOpname.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
                srcReportStockOpname.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
                srcReportStockOpname.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
                srcReportStockOpname.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
                srcReportStockOpname.setDateTo(getDate(FRM_FIELD_DATE_TO));
                srcReportStockOpname.setSortBy(getInt(FRM_FIELD_SORT_BY));
            }
            catch(Exception e)
            {
                    System.out.println("Error on requestEntityObject : "+e.toString());
            }
	}
}
