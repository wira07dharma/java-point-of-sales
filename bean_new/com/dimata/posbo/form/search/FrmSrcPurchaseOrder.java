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

public class FrmSrcPurchaseOrder extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcPurchaseOrder srcPurchaseOrder;

	public static final String FRM_NAME_SRCORDERMATERIAL    =  "FRM_NAME_SRCORDERMATERIAL" ;

	public static final int FRM_FIELD_PRMSTATUS		=  0 ;
	public static final int FRM_FIELD_PRMNUMBER		=  1 ;
	public static final int FRM_FIELD_VENDORNAME		=  2 ;
	public static final int FRM_FIELD_PRMDATEFROM		=  3 ;
	public static final int FRM_FIELD_PRMDATETO		=  4 ;
	public static final int FRM_FIELD_LOCATION		=  5 ;
	public static final int FRM_FIELD_STATUSDATE		=  6 ;
	public static final int FRM_FIELD_SORTBY		=  7 ;
	public static final int FRM_FIELD_SORTBY_TYPE		=  8 ;
	public static final int FRM_FIELD_LOCATION_TYPE		=  9 ;

	public static String[] fieldNames = 
        {
            "FRM_FIELD_PRMSTATUS",
            "FRM_FIELD_PRMNUMBER",
            "FRM_FIELD_VENDORNAME",
            "FRM_FIELD_PRMDATEFROM",
            "FRM_FIELD_PRMDATETO",
            "FRM_FIELD_LOCATION",
            "FRM_FIELD_STATUSDATE",
            "FRM_FIELD_SORTBY",
            "FRM_FIELD_SORTBY_TYPE",
            "FRM_FIELD_LOCATION_TYPE"
	} ;

	public static int[] fieldTypes = 
        {
            TYPE_LONG,
            TYPE_STRING,
            TYPE_STRING,
            TYPE_DATE,
            TYPE_DATE,
            TYPE_LONG,
            TYPE_INT,
            TYPE_INT,
            TYPE_INT,
            TYPE_INT
	} ;

	public FrmSrcPurchaseOrder()
        {
	}
	
        public FrmSrcPurchaseOrder(SrcPurchaseOrder srcPurchaseOrder)
        {
            this.srcPurchaseOrder = srcPurchaseOrder;
	}

	public FrmSrcPurchaseOrder(HttpServletRequest request, SrcPurchaseOrder srcPurchaseOrder)
        {
            super(new FrmSrcPurchaseOrder(srcPurchaseOrder), request);
            this.srcPurchaseOrder = srcPurchaseOrder;
	}

	public String getFormName() { return FRM_NAME_SRCORDERMATERIAL; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcPurchaseOrder getEntityObject(){ return srcPurchaseOrder; }

	public void requestEntityObject(SrcPurchaseOrder srcPurchaseOrder) 
        {
		try
                {
			this.requestParam();
			srcPurchaseOrder.setPrmnumber(getString(FRM_FIELD_PRMNUMBER));
			srcPurchaseOrder.setVendorname(getString(FRM_FIELD_VENDORNAME));
			srcPurchaseOrder.setPrmdatefrom(getDate(FRM_FIELD_PRMDATEFROM));
			srcPurchaseOrder.setPrmdateto(getDate(FRM_FIELD_PRMDATETO));
			srcPurchaseOrder.setLocation(getLong(FRM_FIELD_LOCATION));
			srcPurchaseOrder.setStatusdate(getInt(FRM_FIELD_STATUSDATE));
			srcPurchaseOrder.setSortby(getInt(FRM_FIELD_SORTBY));
			srcPurchaseOrder.setSortByType(getInt(FRM_FIELD_SORTBY_TYPE));
			srcPurchaseOrder.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
