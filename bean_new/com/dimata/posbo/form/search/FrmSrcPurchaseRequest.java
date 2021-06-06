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

public class FrmSrcPurchaseRequest extends FRMHandler implements I_FRMInterface, I_FRMType
{
	private SrcPurchaseRequest srcPurchaseRequest;

	public static final String FRM_NAME_SRCORDERMATERIAL    =  "FRM_NAME_SRCREQUESTMATERIAL" ;

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

	public FrmSrcPurchaseRequest()
        {
	}

        public FrmSrcPurchaseRequest(SrcPurchaseRequest srcPurchaseRequest)
        {
            this.srcPurchaseRequest = srcPurchaseRequest;
	}

	public FrmSrcPurchaseRequest(HttpServletRequest request, SrcPurchaseRequest srcPurchaseRequest)
        {
            super(new FrmSrcPurchaseRequest(srcPurchaseRequest), request);
            this.srcPurchaseRequest = srcPurchaseRequest;
	}

	public String getFormName() { return FRM_NAME_SRCORDERMATERIAL; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; }

	public int getFieldSize() { return fieldNames.length; }

	public SrcPurchaseRequest getEntityObject(){ return srcPurchaseRequest; }

	public void requestEntityObject(SrcPurchaseRequest srcPurchaseRequest)
        {
		try
                {
			this.requestParam();
			srcPurchaseRequest.setPrmnumber(getString(FRM_FIELD_PRMNUMBER));
			srcPurchaseRequest.setPrmdatefrom(getDate(FRM_FIELD_PRMDATEFROM));
			srcPurchaseRequest.setPrmdateto(getDate(FRM_FIELD_PRMDATETO));
			srcPurchaseRequest.setLocation(getLong(FRM_FIELD_LOCATION));
			srcPurchaseRequest.setStatusdate(getInt(FRM_FIELD_STATUSDATE));
			srcPurchaseRequest.setSortby(getInt(FRM_FIELD_SORTBY));
			srcPurchaseRequest.setSortByType(getInt(FRM_FIELD_SORTBY_TYPE));
			srcPurchaseRequest.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
		}
                catch(Exception e)
                {
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
