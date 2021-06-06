/*
 * FrmSrcSaleReport.java
 *
 * Created on February 19, 2005, 11:07 AM
 */

package com.dimata.posbo.form.search;

import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.search.*;
/**
 *
 * @author  wpradnyana
 */
public class FrmSrcSaleReport extends FRMHandler implements I_FRMInterface, I_FRMType {
    SrcSaleReport srcSaleReport;
    
    public static String FRM_SRC_SALE_REPORT = "FRM_SRC_SALE_REPORT";
    public static int FRM_FLD_FROM_DATE=0;
    public static int FRM_FLD_TO_DATE=1;
    public static int FRM_FLD_SHIFT_ID=2;
    public static int FRM_FLD_CATEGORY_ID=3;
    public static int FRM_FLD_SUB_CATEGORY_ID=4;
    public static int FRM_FLD_ITEM_NAME=5;
    public static int FRM_FLD_MARK_ID=6;
    public static int FRM_FLD_LOCATION_ID=7;
    public static int FRM_FLD_SUPPLIER_ID=8;
    public static int FRM_FLD_SALES_PERSON_ID=9;
    public static int FRM_FLD_TIPE_TRANSAKSI=10;
    public static int FRM_FLD_GROUP_BY=11;
    public static int FRM_FLD_ITEM_ID=12;
    public static int FRM_FLD_DETAIL_GROUP=13;
    public static int FRM_FLD_DESC_SORT=14;
    public static int FRM_FLD_CURRENCY=15;
    public static int FRM_FLD_CUST_CODE=16;
    public static int FRM_FLD_CUST_NAME=17;
    public static int FRM_FLD_TRANS_STATUS=18;
    public static int FRM_FLD_USE_DATE = 19;
    public static int FRM_FLD_VIEW_PHOTO = 20;
    //cash master id
    public static int FRM_FLD_CASH_MASTER_ID = 21;
    
    public static int FRM_FLD_STATUS = 22;
    public static int FRM_FLD_INVOICE_NUMBER = 23;
    public static int FRM_FLD_NOTE = 24;
     
    //add opie-eyek 20160803
    public static int FRM_FLD_VIEW_REPORT = 25;
    public static int FRM_FLD_ORDER_BY = 26;
    public static int FRM_FLD_VIEW_STOCK = 27;
    public static int FRM_FIELD_BILLDATESTATUS = 28;
    
    public static String[] fieldNames= {
        
        "FRM_FLD_FROM_DATE",
        "FRM_FLD_TO_DATE",
        "FRM_FLD_SHIFT_ID",
        "FRM_FLD_CATEGORY_ID",
        "FRM_FLD_SUB_CATEGORY_ID",
        "FRM_FLD_ITEM_NAME",
        "FRM_FLD_MARK_ID",
        "FRM_FLD_LOCATION_ID",
        "FRM_FLD_SUPPLIER_ID",
        "FRM_FLD_SALES_PERSON_ID",
        "FRM_FLD_TIPE_TRANSAKSI",
        "FRM_FLD_GROUP_BY",
        "FRM_FLD_ITEM_ID",
        "FRM_FLD_DETAIL_GROUP",
        "FRM_FLD_DESC_SORT",
        "FRM_FLD_CURRENCY",
        "FRM_FLD_CUST_CODE",
        "FRM_FLD_CUST_NAME",
        "FRM_FLD_TRANS_STATUS",
        "FRM_FLD_USE_DATE",
        "FRM_FLD_VIEW_PHOTO",
        "FRM_FLD_CASHIER_MASTER_ID",
        "FRM_FLD_STATUS",
        "FRM_FLD_INVOICE_NUMBER",
        "FRM_FLD_NOTE",
        "FRM_FLD_VIEW_REPORT",
        "FRM_FLD_ORDER_BY",
        "FRM_FLD_VIEW_STOCK",
        "FRM_FIELD_BILLDATESTATUS"
    };
    public static int fieldTypes[]={
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
         TYPE_STRING,
        TYPE_STRING, 
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };
    
    public void requestEntity(SrcSaleReport srcSaleReport){
        try{
            this.requestParam();
            srcSaleReport.setCategoryId(getLong(FRM_FLD_CATEGORY_ID));
            srcSaleReport.setDateFrom(getDate(FRM_FLD_FROM_DATE));
            srcSaleReport.setDateTo(getDate(FRM_FLD_TO_DATE));
            srcSaleReport.setItemName(getString(FRM_FLD_ITEM_NAME));
            srcSaleReport.setLocationId(getLong(FRM_FLD_LOCATION_ID));
            srcSaleReport.setMarkId(getLong(FRM_FLD_MARK_ID));
            srcSaleReport.setSalesPersonId(getLong(FRM_FLD_SALES_PERSON_ID));
            srcSaleReport.setShiftId(getLong(FRM_FLD_SHIFT_ID));
            srcSaleReport.setSubCategoryId(getLong(FRM_FLD_SUB_CATEGORY_ID));
            srcSaleReport.setSupplierId(getLong(FRM_FLD_SUPPLIER_ID));
            srcSaleReport.setTransType(getInt(FRM_FLD_TIPE_TRANSAKSI));
            srcSaleReport.setGroupBy(getInt(FRM_FLD_GROUP_BY));
            srcSaleReport.setItemId(getLong(FRM_FLD_ITEM_ID));
            srcSaleReport.setDetailGroup(getInt(FRM_FLD_DETAIL_GROUP));
            srcSaleReport.setDescSort(getInt(FRM_FLD_DESC_SORT));
            srcSaleReport.setCurrencyOid(getLong(FRM_FLD_CURRENCY));
            srcSaleReport.setCustCode(getString(FRM_FLD_CUST_CODE));
            srcSaleReport.setCustName(getString(FRM_FLD_CUST_NAME));
            srcSaleReport.setTransStatus(getInt(FRM_FLD_TRANS_STATUS));
            srcSaleReport.setUseDate(getInt(FRM_FLD_USE_DATE));
            srcSaleReport.setImageView(getInt(FRM_FLD_VIEW_PHOTO));
            //cashMasterId
            srcSaleReport.setCashMasterId(getLong(FRM_FLD_CASH_MASTER_ID));
            srcSaleReport.setInvoiceNumber(getString(FRM_FLD_INVOICE_NUMBER));
            srcSaleReport.setNote(getString(FRM_FLD_NOTE));
            
            //opie-eyek 20160803
            srcSaleReport.setViewReport(getInt(FRM_FLD_VIEW_REPORT));
            srcSaleReport.setSortBy(getInt(FRM_FLD_ORDER_BY));
            srcSaleReport.setViewStock(getInt(FRM_FLD_VIEW_STOCK));
            srcSaleReport.setBillDateStatus(getInt(FRM_FIELD_BILLDATESTATUS));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Creates a new instance of FrmSrcSaleReport */
    public FrmSrcSaleReport() {
    }
    public FrmSrcSaleReport(SrcSaleReport srcSaleReport) {
        this.srcSaleReport = srcSaleReport;
    }
    
    public FrmSrcSaleReport(HttpServletRequest request,SrcSaleReport srcSaleReport) {
        super(new FrmSrcSaleReport(srcSaleReport),request);
        this.srcSaleReport = srcSaleReport;
    }
    
    public String[] getFieldNames() {
        return this.fieldNames;
        //throw new UnsupportedOperationException ();
    }
    
    public int getFieldSize() {
        return this.fieldNames.length;
        //throw new UnsupportedOperationException ();
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
        //throw new UnsupportedOperationException ();
    }
    
    public String getFormName() {
        return FRM_SRC_SALE_REPORT;
        //throw new UnsupportedOperationException ();
    }
    
}
