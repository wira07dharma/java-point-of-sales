package com.dimata.posbo.form.warehouse;

/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.posbo.entity.warehouse.MatConStockOpname;

public class FrmMatConStockOpname extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatConStockOpname matStockOpname;
    
    public static final String FRM_NAME_MATSTOCKOPNAME  =  "FRM_NAME_MATSTOCKOPNAME";
    
    public static final int FRM_FIELD_STOCK_OPNAME_ID       =  0 ;
    public static final int FRM_FIELD_LOCATION_ID           =  1 ;
    public static final int FRM_FIELD_STOCK_OPNAME_DATE     =  2 ;
    public static final int FRM_FIELD_STOCK_OPNAME_TIME     =  3 ;
    public static final int FRM_FIELD_STOCK_OPNAME_NUMBER   =  4 ;
    public static final int FRM_FIELD_STOCK_OPNAME_STATUS   =  5 ;
    public static final int FRM_FIELD_SUPPLIER_ID           =  6 ;
    public static final int FRM_FIELD_CATEGORY_ID           =  7 ;
    public static final int FRM_FIELD_SUB_CATEGORY_ID       =  8 ;
    public static final int FRM_FIELD_REMARK                =  9 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_STOCK_OPNAME_ID",
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_STOCK_OPNAME_DATE",
        "FRM_FIELD_STOCK_OPNAME_TIME",
        "FRM_FIELD_STOCK_OPNAME_NUMBER",
        "FRM_FIELD_STOCK_OPNAME_STATUS",
        "FRM_FIELD_SUPPLIER_ID",
        "FRM_FIELD_CATEGORY_ID",
        "FRM_FIELD_SUB_CATEGORY_ID",
        "FRM_FIELD_REMARK"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,  TYPE_LONG,
        TYPE_DATE,  TYPE_STRING,
        TYPE_STRING, TYPE_INT,
        TYPE_LONG, TYPE_LONG,
        TYPE_LONG, TYPE_STRING
    } ;
    
    public FrmMatConStockOpname(){
    }
    public FrmMatConStockOpname(MatConStockOpname matStockOpname){
        this.matStockOpname = matStockOpname;
    }
    
    public FrmMatConStockOpname(HttpServletRequest request, MatConStockOpname matStockOpname){
        super(new FrmMatConStockOpname(matStockOpname), request);
        this.matStockOpname = matStockOpname;
    }
    
    public String getFormName() { return FRM_NAME_MATSTOCKOPNAME; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MatConStockOpname getEntityObject(){ return matStockOpname; }
    
    public void requestEntityObject(MatConStockOpname matStockOpname) {
        try {
            this.requestParam();
            matStockOpname.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            matStockOpname.setStockOpnameDate(getDate(FRM_FIELD_STOCK_OPNAME_DATE));
            matStockOpname.setStockOpnameNumber(getString(FRM_FIELD_STOCK_OPNAME_NUMBER));
            matStockOpname.setStockOpnameTime(getString(FRM_FIELD_STOCK_OPNAME_TIME));
            matStockOpname.setStockOpnameStatus(getInt(FRM_FIELD_STOCK_OPNAME_STATUS));
            matStockOpname.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
            matStockOpname.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
            matStockOpname.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
            matStockOpname.setRemark(getString(FRM_FIELD_REMARK));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
