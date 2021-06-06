package com.dimata.posbo.form.search;

/* java package */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.posbo.entity.search.SrcMatConReturn;

public class FrmSrcMatConReturn extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcMatConReturn srcMatConReturn;
    
    public static final String FRM_NAME_SRCMATRETURN = "FRM_NAME_SRCMATRETURN";
    
    public static final int FRM_FIELD_RETURNSTATUS = 0;
    public static final int FRM_FIELD_RETURNNUMBER = 1;
    public static final int FRM_FIELD_VENDORNAME = 2;
    public static final int FRM_FIELD_RETURNFROMDATE = 3;
    public static final int FRM_FIELD_RETURNTODATE = 4;
    public static final int FRM_FIELD_RETURNDATESTATUS = 5;
    public static final int FRM_FIELD_RETURNSORTBY = 6;
    public static final int FRM_FIELD_RETURNSORTBY_TYPE = 7;
    public static final int FRM_FIELD_LOCATION_TYPE = 8;
    public static final int FRM_FIELD_LOCATION_FROM = 9;
    public static final int FRM_FIELD_RETURN_REASON = 10;
    
    public static String[] fieldNames = {
        "FRM_FIELD_RETURNSTATUS",
        "FRM_FIELD_RETURNNUMBER",
        "FRM_FIELD_VENDORNAME",
        "FRM_FIELD_RETURNFROMDATE",
        "FRM_FIELD_RETURNTODATE",
        "FRM_FIELD_RETURNDATESTATUS",
        "FRM_FIELD_RETURNSORTBY",
        "FRM_FIELD_RETURNSORTBY_TYPE",
        "FRM_FIELD_LOCATION_TYPE",
        "FRM_FIELD_LOCATION_FROM",
        "FRM_FIELD_RETURN_REASON"
    };
    
    public static int[] fieldTypes = {
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT
    };
    
    public FrmSrcMatConReturn() {
    }
    
    public FrmSrcMatConReturn(SrcMatConReturn srcMatConReturn) {
        this.srcMatConReturn = srcMatConReturn;
    }
    
    public FrmSrcMatConReturn(HttpServletRequest request, SrcMatConReturn srcMatConReturn) {
        super(new FrmSrcMatConReturn(srcMatConReturn), request);
        this.srcMatConReturn = srcMatConReturn;
    }
    
    public String getFormName() {
        return FRM_NAME_SRCMATRETURN;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public SrcMatConReturn getEntityObject() {
        return srcMatConReturn;
    }
    
    public void requestEntityObject(SrcMatConReturn srcMatConReturn) {
        try {
            this.requestParam();
            srcMatConReturn.setReturnnumber(getString(FRM_FIELD_RETURNNUMBER));
            srcMatConReturn.setVendorname(getString(FRM_FIELD_VENDORNAME));
            srcMatConReturn.setReturnfromdate(getDate(FRM_FIELD_RETURNFROMDATE));
            srcMatConReturn.setReturntodate(getDate(FRM_FIELD_RETURNTODATE));
            srcMatConReturn.setReturndatestatus(getInt(FRM_FIELD_RETURNDATESTATUS));
            srcMatConReturn.setReturnsortby(getInt(FRM_FIELD_RETURNSORTBY));
            srcMatConReturn.setReturnSortByType(getInt(FRM_FIELD_RETURNSORTBY_TYPE));
            srcMatConReturn.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
            srcMatConReturn.setLocationFrom(getLong(FRM_FIELD_LOCATION_FROM));
            srcMatConReturn.setReturnReason(getInt(FRM_FIELD_RETURN_REASON));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
