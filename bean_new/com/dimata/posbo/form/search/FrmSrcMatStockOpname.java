package com.dimata.posbo.form.search;

import javax.servlet.http.*;

import com.dimata.posbo.entity.search.SrcMatStockOpname;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;

public class FrmSrcMatStockOpname extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcMatStockOpname srcMatStockOpname;

    public static final String FRM_NAME_SRCMATSTOCKOPNAME = "FRM_NAME_SRCMATSTOCKOPNAME";

    public static final int FRM_FIELD_LOCATION_ID = 0;
    public static final int FRM_FIELD_STATUS_DATE = 1;
    public static final int FRM_FIELD_FROM_DATE = 2;
    public static final int FRM_FIELD_TO_DATE = 3;
    public static final int FRM_FIELD_SORTBY = 4;
    public static final int FRM_FIELD_STATUS = 5;
    public static final int FRM_FIELD_SUPPLIER_ID = 6;
    public static final int FRM_FIELD_CATEGORY_ID = 7;
    public static final int FRM_FIELD_SUB_CATEGORY_ID = 8;
    public static final int FRM_FIELD_PRMNUMBER = 9;
    public static final int FRM_FIELD_GROUP_BY = 10;
    //added by dewok 2018 for jewelry
    public static final int FRM_FIELD_ETALASE_ID = 11;
    public static final int FRM_FIELD_OPNAME_ITEM_TYPE = 12;
    
    public static String[] fieldNames =
            {
                "FRM_FIELD_LOCATIONID", "FRM_FIELD_STATUS_DATE",
                "FRM_FIELD_FROM_DATE", "FRM_FIELD_TO_DATE",
                "FRM_FIELD_SORTBY", "FRM_FIELD_STATUS",
                "FRM_FIELD_SUPPLIER_ID", "FRM_FIELD_CATEGORY_ID",
                "FRM_FIELD_SUB_CATEGORY_ID",
                "FRM_FIELD_PRMNUMBER","FRM_FIELD_GROUP_BY",
                "FRM_FIELD_ETALASE_ID","FRM_FIELD_OPNAME_ITEM_TYPE"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG, TYPE_INT,
                TYPE_DATE, TYPE_DATE,
                TYPE_INT, TYPE_INT,
                TYPE_LONG, TYPE_LONG,
                TYPE_LONG,
                TYPE_STRING,TYPE_INT,
                TYPE_LONG, TYPE_INT
            };

    public FrmSrcMatStockOpname() {
    }

    public FrmSrcMatStockOpname(SrcMatStockOpname srcMatStockOpname) {
        this.srcMatStockOpname = srcMatStockOpname;
    }

    public FrmSrcMatStockOpname(HttpServletRequest request, SrcMatStockOpname srcMatStockOpname) {
        super(new FrmSrcMatStockOpname(srcMatStockOpname), request);
        this.srcMatStockOpname = srcMatStockOpname;
    }

    public String getFormName() {
        return FRM_NAME_SRCMATSTOCKOPNAME;
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

    public SrcMatStockOpname getEntityObject() {
        return srcMatStockOpname;
    }

    public void requestEntityObject(SrcMatStockOpname srcMatStockOpname) {
        try {
            this.requestParam();
            srcMatStockOpname.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            srcMatStockOpname.setStatusDate(getInt(FRM_FIELD_STATUS_DATE));
            srcMatStockOpname.setFromDate(getDate(FRM_FIELD_FROM_DATE));
            srcMatStockOpname.setToDate(getDate(FRM_FIELD_TO_DATE));
            srcMatStockOpname.setSortBy(getInt(FRM_FIELD_SORTBY));
            srcMatStockOpname.setStatus(getInt(FRM_FIELD_STATUS));
            srcMatStockOpname.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
            srcMatStockOpname.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
            srcMatStockOpname.setSubCategoryId(getLong(FRM_FIELD_SUB_CATEGORY_ID));
            srcMatStockOpname.setOpnameNumber(getString(FRM_FIELD_PRMNUMBER));
            srcMatStockOpname.setGroupBy(getInt(FRM_FIELD_GROUP_BY));
            srcMatStockOpname.setEtalaseId(getLong(FRM_FIELD_ETALASE_ID));
            srcMatStockOpname.setOpnameItemType(getInt(FRM_FIELD_OPNAME_ITEM_TYPE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
