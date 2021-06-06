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

public class FrmSrcReportStock extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcReportStock srcReportStock;
    
    public static final String FRM_NAME_SRCREPORTSTOCK = "FRM_NAME_SRCREPORTSTOCK";
    
    public static final int FRM_FIELD_LOCATION_ID = 0;
    public static final int FRM_FIELD_SUPPLIER_ID = 1;
    public static final int FRM_FIELD_CATEGORY_ID = 2;
    public static final int FRM_FIELD_SUB_CATEGORY_ID = 3;
    public static final int FRM_FIELD_PERIODE_ID = 4;
    public static final int FRM_FIELD_SKU = 5;
    public static final int FRM_FIELD_DATE_FROM = 6;
    public static final int FRM_FIELD_DATE_TO = 7;
    public static final int FRM_FIELD_TYPE = 8;
    public static final int FRM_FIELD_MERK_ID = 9;
    public static final int FRM_FIELD_STOCK_VALUE_BY = 10;
    public static final int FRM_FIELD_INFO_SHOWED = 11;
    public static final int FRM_FIELD_USER_ID = 12;
    public static final int FRM_FIELD_GENERATE_REPORT = 13;
    public static final int FRM_FIELD_MATERIAL_NAME = 14;
    public static final int FRM_FIELD_KSG = 15;
    public static final int FRM_FIELD_SORT_BY = 16;
    public static final int FRM_FIELD_USER_DOC = 17;
    public static String[] fieldNames = {
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_SUPPLIER_ID",
        "FRM_FIELD_CATEGORY_ID",
        "FRM_FIELD_SUB_CATEGORY_ID",
        "FRM_FIELD_PERIODE_ID",
        "FRM_FIELD_SKU",
        "FRM_FIELD_DATE_FROM",
        "FRM_FIELD_DATE_TO",
        "FRM_FIELD_TYPE",
        "FRM_FIELD_MERK_ID",
        "FRM_FIELD_STOCK_VALUE_BY",
        "FRM_FIELD_INFO_SHOWED",
        "FRM_FIELD_USER_ID",
        "FRM_FIELD_GENERATE_REPORT",
        "FRM_FIELD_MATERIAL_NAME",
        "FRM_FIELD_KSG",
        "FRM_FIELD_SORT_BY",
        "FRM_FIELD_USER_DOC"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_BOOL,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG
    };
    
    public FrmSrcReportStock() {
    }
    
    public FrmSrcReportStock(SrcReportStock srcReportStock) {
        this.srcReportStock = srcReportStock;
    }
    
    public FrmSrcReportStock(HttpServletRequest request, SrcReportStock srcReportStock) {
        super(new FrmSrcReportStock(srcReportStock), request);
        this.srcReportStock = srcReportStock;
    }
    
    public String getFormName() {
        return FRM_NAME_SRCREPORTSTOCK;
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
    
    public SrcReportStock getEntityObject() {
        return srcReportStock;
    }
    
    public void requestEntityObject(SrcReportStock srcReportStock) {
        try {
            this.requestParam();
            srcReportStock.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            srcReportStock.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
            srcReportStock.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
            srcReportStock.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
            srcReportStock.setPeriodeId(getLong(FRM_FIELD_PERIODE_ID));
            srcReportStock.setSku(getString(FRM_FIELD_SKU));
            srcReportStock.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
            srcReportStock.setDateTo(getDate(FRM_FIELD_DATE_TO));
            srcReportStock.setType(getInt(FRM_FIELD_TYPE));
            srcReportStock.setMerkId(getLong(FRM_FIELD_MERK_ID));
            srcReportStock.setStockValueBy(getInt(FRM_FIELD_STOCK_VALUE_BY));
            srcReportStock.setInfoShowed(getInt(FRM_FIELD_INFO_SHOWED));
            srcReportStock.setUserId(getLong(FRM_FIELD_USER_ID));
            srcReportStock.setGeneratereport(getBoolean(FRM_FIELD_GENERATE_REPORT));
            srcReportStock.setMaterialName(getString(FRM_FIELD_MATERIAL_NAME));
            srcReportStock.setKsg(getString(FRM_FIELD_KSG));
            srcReportStock.setSortBy(getInt(FRM_FIELD_SORT_BY));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
