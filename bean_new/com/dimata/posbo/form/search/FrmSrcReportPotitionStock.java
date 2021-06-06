/*
 * FrmSrcReportPotitionStock.java
 *
 * Created on February 18, 2008, 10:05 AM
 */

package com.dimata.posbo.form.search;

import com.dimata.posbo.entity.search.SrcReportPotitionStock;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author gwawan
 */
public class FrmSrcReportPotitionStock extends FRMHandler implements I_FRMInterface, I_FRMType { 
    
    private SrcReportPotitionStock srcReportPotitionStock;
    
    public static final String FRM_NAME_SRCREPORTPOTITIONSTOCK = "FRM_NAME_SRCREPORTPOTITIONSTOCK";
    
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
    public static final int FRM_FIELD_STATUSDATE = 14;
    public static final int FRM_FIELD_STANDART_ID = 15;
    
    public static final int FRM_FIELD_GROUP_BY = 16;
    public static final int FRM_FIELD_PRICE_TYPE_ID=17;
    public static final int FRM_FIELD_VIEW_DEAD_STOK=18;
    public static final int FRM_FIELD_INCLUDE_WAREHOUSE = 19;
    public static final int FRM_FIELD_SHOW_QTY_ZERO = 20;
    
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
        "FRM_FIELD_STATUSDATE",
        "FRM_FIELD_STANDART",
        "FRM_FIELD_GROUP_BY",
        "FRM_FIELD_PRICE_TYPE_ID",
        "FRM_FIELD_VIEW_DEAD_STOK",
        "FRM_FIELD_INCLUDE_WAREHOUSE",
        "FRM_FIELD_SHOW_QTY_ZERO"
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
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };
    
    /** Creates a new instance of FrmSrcReportPotitionStock */
    public FrmSrcReportPotitionStock() {
    }
    
    public FrmSrcReportPotitionStock(SrcReportPotitionStock srcReportPotitionStock) {
        this.srcReportPotitionStock = srcReportPotitionStock;
    }
    
    public FrmSrcReportPotitionStock(HttpServletRequest request, SrcReportPotitionStock srcReportPotitionStock) {
        super(new FrmSrcReportPotitionStock(srcReportPotitionStock), request);
        this.srcReportPotitionStock = srcReportPotitionStock;
    }
    
    public String getFormName() {
        return FRM_NAME_SRCREPORTPOTITIONSTOCK;
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
    
    public SrcReportPotitionStock getEntityObject() {
        return srcReportPotitionStock;
    }
    
    public void requestEntityObject(SrcReportPotitionStock srcReportPotitionStock) {
        try {
            this.requestParam();
            srcReportPotitionStock.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            srcReportPotitionStock.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
            srcReportPotitionStock.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
            srcReportPotitionStock.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
            srcReportPotitionStock.setPeriodeId(getLong(FRM_FIELD_PERIODE_ID));
            srcReportPotitionStock.setSku(getString(FRM_FIELD_SKU));
            srcReportPotitionStock.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
            srcReportPotitionStock.setDateTo(getDate(FRM_FIELD_DATE_TO));
            srcReportPotitionStock.setType(getInt(FRM_FIELD_TYPE));
            srcReportPotitionStock.setMerkId(getLong(FRM_FIELD_MERK_ID));
            srcReportPotitionStock.setStockValueBy(getInt(FRM_FIELD_STOCK_VALUE_BY));
            srcReportPotitionStock.setInfoShowed(getInt(FRM_FIELD_INFO_SHOWED));
            srcReportPotitionStock.setUserId(getLong(FRM_FIELD_USER_ID));
            srcReportPotitionStock.setGeneratereport(getBoolean(FRM_FIELD_GENERATE_REPORT));
            srcReportPotitionStock.setStatusDate(getInt(FRM_FIELD_STATUSDATE));
            srcReportPotitionStock.setStandartId(getLong(FRM_FIELD_STANDART_ID));
            srcReportPotitionStock.setStockValueSale(getLong(FRM_FIELD_STOCK_VALUE_BY));
            
            srcReportPotitionStock.setGroupBy(getInt(FRM_FIELD_GROUP_BY));
            srcReportPotitionStock.setPriceTypeId(getLong(FRM_FIELD_PRICE_TYPE_ID));
            srcReportPotitionStock.setViedDeadStok(getInt(FRM_FIELD_VIEW_DEAD_STOK));
            srcReportPotitionStock.setIncWH(getInt(FRM_FIELD_INCLUDE_WAREHOUSE));
            srcReportPotitionStock.setShowQtyZero(getInt(FRM_FIELD_SHOW_QTY_ZERO));
            
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
