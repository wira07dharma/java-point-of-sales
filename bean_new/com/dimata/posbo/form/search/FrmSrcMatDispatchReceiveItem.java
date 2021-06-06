/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

public class FrmSrcMatDispatchReceiveItem extends FRMHandler implements I_FRMInterface, I_FRMType
{
	private SrcMatDispatchReceiveItem srcMatDispatchReceiveItemExc;

	public static final String FRM_NAME_SRCMATDISPATCHRECEIVEEXC	=  "FRM_NAME_SRCMATDISPATCHRECEIVEEXC" ;

	public static final int FRM_FIELD_DISPATCH_CODE			=  0 ;
	public static final int FRM_FIELD_DISPATCH_DATE_FROM		=  1 ;
	public static final int FRM_FIELD_DISPATCH_DATE_TO		=  2 ;
	public static final int FRM_FIELD_IGNORE_DATE			=  3 ;
	public static final int FRM_FIELD_DISPATCH_FROM			=  4 ;
	public static final int FRM_FIELD_DISPATCH_TO			=  5 ;
	public static final int FRM_FIELD_STATUS			=  6 ;
	public static final int FRM_FIELD_SORT_BY			=  7 ;

	public static String[] fieldNames =
        {
            "FRM_FIELD_DISPATCH_CODE", "FRM_FIELD_DISPATCH_DATE_FROM",
            "FRM_FIELD_DISPATCH_DATE_TO", "FRM_FIELD_IGNORE_DATE",
            "FRM_FIELD_DISPATCH_FROM", "FRM_FIELD_DISPATCH_TO",
            "FRM_FIELD_STATUS", "FRM_FIELD_SORT_BY"
	} ;

	public static int[] fieldTypes =
        {
            TYPE_STRING, TYPE_DATE,
            TYPE_DATE, TYPE_BOOL,
            TYPE_LONG, TYPE_LONG,
            TYPE_INT, TYPE_INT
	} ;

	public FrmSrcMatDispatchReceiveItem()
        {
	}

        public FrmSrcMatDispatchReceiveItem(SrcMatDispatchReceiveItem srcMatDispatchReceiveItemExc)
        {
            this.srcMatDispatchReceiveItemExc = srcMatDispatchReceiveItemExc;
	}

	public FrmSrcMatDispatchReceiveItem(HttpServletRequest request, SrcMatDispatchReceiveItem srcMatDispatchReceiveItemExc)
        {
            super(new FrmSrcMatDispatchReceiveItem(srcMatDispatchReceiveItemExc), request);
            this.srcMatDispatchReceiveItemExc = srcMatDispatchReceiveItemExc;
	}

	public String getFormName() { return FRM_NAME_SRCMATDISPATCHRECEIVEEXC; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public SrcMatDispatchReceiveItem getEntityObject(){ return srcMatDispatchReceiveItemExc; }

	public void requestEntityObject(SrcMatDispatch srcMaterialDispatchExc)
        {
            try
            {
		this.requestParam();
		srcMatDispatchReceiveItemExc.setDispatchCode(getString(FRM_FIELD_DISPATCH_CODE));
		srcMatDispatchReceiveItemExc.setDispatchDateFrom(getDate(FRM_FIELD_DISPATCH_DATE_FROM));
		srcMatDispatchReceiveItemExc.setDispatchDateTo(getDate(FRM_FIELD_DISPATCH_DATE_TO));
		srcMatDispatchReceiveItemExc.setIgnoreDate(getBoolean(FRM_FIELD_IGNORE_DATE));
		srcMatDispatchReceiveItemExc.setDispatchFrom(getLong(FRM_FIELD_DISPATCH_FROM));
		srcMatDispatchReceiveItemExc.setDispatchTo(getLong(FRM_FIELD_DISPATCH_TO));
		srcMatDispatchReceiveItemExc.setStatus(getInt(FRM_FIELD_STATUS));
		srcMatDispatchReceiveItemExc.setSortBy(getInt(FRM_FIELD_SORT_BY));
            }
            catch(Exception e)
            {
		System.out.println("Error on requestEntityObject : "+e.toString());
            }
	}
}


