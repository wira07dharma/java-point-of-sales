package com.dimata.posbo.form.search;

/* java package */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.posbo.entity.search.SrcMatReturn;

public class FrmSrcMatReturn extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcMatReturn srcMatReturn;

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

    public static String[] fieldNames =
            {
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

    public static int[] fieldTypes =
            {
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

    public FrmSrcMatReturn() {
    }

    public FrmSrcMatReturn(SrcMatReturn srcMatReturn) {
        this.srcMatReturn = srcMatReturn;
    }

    public FrmSrcMatReturn(HttpServletRequest request, SrcMatReturn srcMatReturn) {
        super(new FrmSrcMatReturn(srcMatReturn), request);
        this.srcMatReturn = srcMatReturn;
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

    public SrcMatReturn getEntityObject() {
        return srcMatReturn;
    }

    public void requestEntityObject(SrcMatReturn srcMatReturn) {
        try {
            this.requestParam();
            srcMatReturn.setReturnnumber(getString(FRM_FIELD_RETURNNUMBER));
            srcMatReturn.setVendorname(getString(FRM_FIELD_VENDORNAME));
            srcMatReturn.setReturnfromdate(getDate(FRM_FIELD_RETURNFROMDATE));
            srcMatReturn.setReturntodate(getDate(FRM_FIELD_RETURNTODATE));
            srcMatReturn.setReturndatestatus(getInt(FRM_FIELD_RETURNDATESTATUS));
            srcMatReturn.setReturnsortby(getInt(FRM_FIELD_RETURNSORTBY));
            srcMatReturn.setReturnSortByType(getInt(FRM_FIELD_RETURNSORTBY_TYPE));
            srcMatReturn.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
            srcMatReturn.setLocationFrom(getLong(FRM_FIELD_LOCATION_FROM));
            srcMatReturn.setReturnReason(getInt(FRM_FIELD_RETURN_REASON));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
