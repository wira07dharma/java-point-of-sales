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

public class FrmSrcReportReceive extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcReportReceive srcReportReceive;
    
    public static final String FRM_NAME_SRCREPORTRECEIVE    =  "FRM_NAME_SRCREPORTRECEIVE";
    
    public static final int FRM_FIELD_LOCATION_ID	= 0;
    public static final int FRM_FIELD_SHIFT_ID		= 1;
    public static final int FRM_FIELD_OPERATOR_ID	= 2;
    public static final int FRM_FIELD_SUPPLIER_ID	= 3;
    public static final int FRM_FIELD_CATEGORY_ID	= 4;
    public static final int FRM_FIELD_SUB_CATEGORY_ID	= 5;
    public static final int FRM_FIELD_DATE_FROM		= 6;
    public static final int FRM_FIELD_DATE_TO		= 7;
    public static final int FRM_FIELD_SORT_BY		= 8;
    public static final int FRM_FIELD_RECEIVE_SOURCE	= 9;
    public static final int FRM_FIELD_RECEIVE_FROM      = 10;
    public static final int FRM_FIELD_CURRENCY_ID       = 11;
    public static final int FRM_FIELD_STATUSDATE	= 12;
    public static final int FRM_FIELD_PRICE_TYPE_ID     = 13;
    
    public static String[] fieldNames = {
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_SHIFT_ID",
        "FRM_FIELD_OPERATOR_ID",
        "FRM_FIELD_SUPPLIER_ID",
        "FRM_FIELD_CATEGORY_ID",
        "FRM_FIELD_SUB_CATEGORY_ID",
        "FRM_FIELD_DATE_FROM",
        "FRM_FIELD_DATE_TO",
        "FRM_FIELD_SORTBY",
        "FRM_FIELD_RECEIVE_SOURCE",
        "FRM_FIELD_RECEIVE_FROM",
        "FRM_FIELD_CURRENCY_ID",
        "FRM_FIELD_STATUSDATE",
        "FRM_FIELD_PRICE_TYPE_ID"
    } ;
    
    public static int[] fieldTypes = {
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
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG
    } ;
    
    public FrmSrcReportReceive() {
    }
    
    public FrmSrcReportReceive(SrcReportReceive srcReportReceive) {
        this.srcReportReceive = srcReportReceive;
    }
    
    public FrmSrcReportReceive(HttpServletRequest request, SrcReportReceive srcReportReceive) {
        super(new FrmSrcReportReceive(srcReportReceive), request);
        this.srcReportReceive = srcReportReceive;
    }
    
    public String getFormName() { return FRM_NAME_SRCREPORTRECEIVE; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public SrcReportReceive getEntityObject(){ return srcReportReceive; }
    
    public void requestEntityObject(SrcReportReceive srcReportReceive) {
        try {
            
            this.requestParam();
            srcReportReceive.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            srcReportReceive.setShiftId(getLong(FRM_FIELD_SHIFT_ID));
            srcReportReceive.setOperatorId(getLong(FRM_FIELD_OPERATOR_ID));
            srcReportReceive.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
            srcReportReceive.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
            srcReportReceive.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
            srcReportReceive.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
            srcReportReceive.setDateTo(getDate(FRM_FIELD_DATE_TO));
            srcReportReceive.setSortBy(getInt(FRM_FIELD_SORT_BY));
            srcReportReceive.setReceiveSource(getInt(FRM_FIELD_RECEIVE_SOURCE));
            srcReportReceive.setReceiveFrom(getLong(FRM_FIELD_RECEIVE_FROM));
            srcReportReceive.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
            srcReportReceive.setStatusdate(getInt(FRM_FIELD_STATUSDATE));
            srcReportReceive.setPriceTypeId(getLong(FRM_FIELD_PRICE_TYPE_ID));
            
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
