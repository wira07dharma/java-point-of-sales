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

public class FrmSrcMatDispatch extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcMatDispatch srcMaterialDispatchExc;

	public static final String FRM_NAME_SRCMATERIALDISPATCHEXC	=  "FRM_NAME_SRCMATERIALDISPATCHEXC" ;

	public static final int FRM_FIELD_DISPATCH_CODE			=  0 ;
	public static final int FRM_FIELD_DISPATCH_DATE_FROM		=  1 ;
	public static final int FRM_FIELD_DISPATCH_DATE_TO		=  2 ;
	public static final int FRM_FIELD_IGNORE_DATE			=  3 ;
	public static final int FRM_FIELD_DISPATCH_FROM			=  4 ;
	public static final int FRM_FIELD_DISPATCH_TO			=  5 ;
	public static final int FRM_FIELD_STATUS			=  6 ;
	public static final int FRM_FIELD_SORT_BY			=  7 ;
        public static final int FRM_FIELD_TEST_CHECK			=  8 ;
        public static final int FRM_FIELD_LOCATION_TYPE			=  9 ;

	public static String[] fieldNames = 
        {
            "FRM_FIELD_DISPATCH_CODE", "FRM_FIELD_DISPATCH_DATE_FROM",  
            "FRM_FIELD_DISPATCH_DATE_TO", "FRM_FIELD_IGNORE_DATE", 
            "FRM_FIELD_DISPATCH_FROM", "FRM_FIELD_DISPATCH_TO", 
            "FRM_FIELD_STATUS", "FRM_FIELD_SORT_BY","FRM_FIELD_TEST_CHECK",
            "LOCATION_TYPE"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_STRING, TYPE_DATE,  
            TYPE_DATE, TYPE_BOOL,  
            TYPE_LONG, TYPE_LONG,   
            TYPE_INT, TYPE_INT, TYPE_INT,
            TYPE_INT
	} ;

	public FrmSrcMatDispatch()
        {
	}
	
        public FrmSrcMatDispatch(SrcMatDispatch srcMaterialDispatchExc)
        {
            this.srcMaterialDispatchExc = srcMaterialDispatchExc;
	}

	public FrmSrcMatDispatch(HttpServletRequest request, SrcMatDispatch srcMaterialDispatchExc)
        {
            super(new FrmSrcMatDispatch(srcMaterialDispatchExc), request);
            this.srcMaterialDispatchExc = srcMaterialDispatchExc;
	}

	public String getFormName() { return FRM_NAME_SRCMATERIALDISPATCHEXC; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcMatDispatch getEntityObject(){ return srcMaterialDispatchExc; }

	public void requestEntityObject(SrcMatDispatch srcMaterialDispatchExc) 
        {
            try
            {
		this.requestParam();
		srcMaterialDispatchExc.setDispatchCode(getString(FRM_FIELD_DISPATCH_CODE));
		srcMaterialDispatchExc.setDispatchDateFrom(getDate(FRM_FIELD_DISPATCH_DATE_FROM));
		srcMaterialDispatchExc.setDispatchDateTo(getDate(FRM_FIELD_DISPATCH_DATE_TO));
		srcMaterialDispatchExc.setIgnoreDate(getBoolean(FRM_FIELD_IGNORE_DATE));
		srcMaterialDispatchExc.setDispatchFrom(getLong(FRM_FIELD_DISPATCH_FROM));
		srcMaterialDispatchExc.setDispatchTo(getLong(FRM_FIELD_DISPATCH_TO));
		srcMaterialDispatchExc.setStatus(getInt(FRM_FIELD_STATUS));
		srcMaterialDispatchExc.setSortBy(getInt(FRM_FIELD_SORT_BY));
                srcMaterialDispatchExc.setTestCheck(getInt(FRM_FIELD_TEST_CHECK));
                srcMaterialDispatchExc.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
            }
            catch(Exception e)
            {
		System.out.println("Error on requestEntityObject : "+e.toString());
            }
	}
}
