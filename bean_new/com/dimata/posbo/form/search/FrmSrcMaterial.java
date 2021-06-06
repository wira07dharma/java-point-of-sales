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

public class FrmSrcMaterial extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcMaterial srcMaterial;

    public static final String FRM_NAME_SRCMMATERIAL = "FRM_NAME_SRCMMATERIAL";

    public static final int FRM_FIELD_MATCODE = 0;
    public static final int FRM_FIELD_MATNAME = 1;
    public static final int FRM_FIELD_SUPPLIERID = 2;
    public static final int FRM_FIELD_CATEGORYID = 3;
    public static final int FRM_FIELD_SUBCATEGORYID = 4;
    public static final int FRM_FIELD_SORTBY = 5;
    public static final int FRM_FIELD_DESCRIPTION = 6;
    public static final int FRM_FIELD_LOCATIONID = 7;
    public static final int FRM_FIELD_TYPE_ITEM = 8;
    public static final int FRM_FIELD_MERK_ID = 9;
    public static final int FRM_SHOW_IMAGE = 10;
    public static final int FRM_FIELD_CODE_RANGE_ID = 11;
    //for show Update Catalog
    public static final int FRM_FIELD_SHOW_UPDATE_CATALOG = 12;
    public static final int FRM_FIELD_DATE_FROM = 13;
    public static final int FRM_FIELD_DATE_TO = 14;

    //for show Print Discount Qty
    public static final int FRM_FIELD_SHOW_DISCOUNT_QTY = 15;

    //for show Stok Nol
    public static final int FRM_FIELD_SHOW_STOCK_NOL = 16;

    //for currency type
    public static final int FRM_FIELD_CURRENCY_TYPE_ID =  17;

    //for member type
    public static final int FRM_FIELD_MEMBER_TYPE_ID =  18;

    //for show hpp
    public static final int FRM_FIELD_SHOW_HPP = 19;

    //for show currency
    public static final int FRM_FIELD_SHOW_CURRENCY = 20;

    //for show GroupItem(barang, jasa, composit)
    public static final int FRM_FIELD_GROUP_ITEM = 21;
    
    //for priceType di price tag
    public static final int FRM_FIELD_PRICE_TYPE_ID = 22;

    //show serial number
    public static final int FRM_FIELD_REQUIRED_SERIAL_NUMBER=23;
    
    public static final int FRM_FIELD_EDIT_MATERIAL=24;
    
    public static final int FRM_FIELD_SELL_LOCATION=25;
    
    public static final int FRM_FIELD_GONDOLA_CODE=26;
    
    public static final int FRM_VIEW_PRICE_HPP=27;
    
    public static String[] fieldNames =
            {
                "FRM_FIELD_MATCODE", "FRM_FIELD_MATNAME",
                "FRM_FIELD_SUPPLIERID", "FRM_FIELD_CATEGORYID",
                "FRM_FIELD_SUBCATEGORYID", "FRM_FIELD_SORTBY",
                "FRM_FIELD_DESCRIPTION", "FRM_FIELD_LOCATIONID",
                "FRM_FIELD_TYPE_ITEM", "FRM_FIELD_MERK_ID",
                "FRM_SHOW_IMAGE","FRM_FIELD_CODE_RANGE_ID",
                //for show update catalog
                "FRM_FIELD_SHOW_UPDATE_CATALOG",
                "FRM_FIELD_DATE_FROM",
                "FRM_FIELD_DATE_TO",
                //for show Print Discount Qty
                "FRM_FIELD_SHOW_DISCOUNT_QTY",
                //for show Stock nol
                "FRM_FIELD_SHOW_STOCK_NOL",
                //for currency type
                "FRM_FIELD_CURRENCY_TYPE_ID",
                //for member type
                "FRM_FIELD_MEMBER_TYPE_ID",
                //for show hpp
                "FRM_FIELD_SHOW_HPP",
                //for show list currency
                "FRM_FIELD_SHOW_CURRENCY",
                //for group item list
                "FRM_FIELD_GROUP_ITEM",
                //for priceType
                "FRM_FIELD_PRICE_TYPE_ID",
                "FRM_FIELD_REQUIRED_SERIAL_NUMBER",
                "FRM_FIELD_EDIT_MATERIAL",
                "FRM_FIELD_SELL_LOCATION",
                "FRM_FIELD_GONDOLA_CODE",
                "FRM_VIEW_PRICE_HPP"
            };

    public static int[] fieldTypes =
            {
                TYPE_STRING, TYPE_STRING,
                TYPE_LONG, TYPE_LONG,
                TYPE_LONG, TYPE_INT,
                TYPE_STRING, TYPE_LONG,
                TYPE_INT, TYPE_LONG,
                TYPE_INT, TYPE_LONG,
                //for show update catalog
                TYPE_INT,
                TYPE_DATE,
                TYPE_DATE,
                //for show print discount qty
                TYPE_INT,
               //for show Stock nol
                TYPE_INT,
               //for currency type
                TYPE_LONG,
              //for member type
                TYPE_LONG,
              //for show hpp
                TYPE_INT,
              //for show list currency
                TYPE_INT,
              //for group item
                TYPE_INT,
              //for price type id
                TYPE_LONG,
                TYPE_INT,
                TYPE_INT,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT

            };

    public FrmSrcMaterial() {
    }

    public FrmSrcMaterial(SrcMaterial srcMaterial) {
        this.srcMaterial = srcMaterial;
    }

    public FrmSrcMaterial (HttpServletRequest request, SrcMaterial srcMaterial) {
        super(new FrmSrcMaterial(srcMaterial), request);
        this.srcMaterial = srcMaterial;
    }

    public String getFormName() {
        return FRM_NAME_SRCMMATERIAL;
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

    public SrcMaterial getEntityObject() {
        return srcMaterial;
    }

    public void requestEntityObject(SrcMaterial srcMaterial) {
        try {
            this.requestParam();
            srcMaterial.setMatcode(getString(FRM_FIELD_MATCODE));
            srcMaterial.setMatname(getString(FRM_FIELD_MATNAME));
            srcMaterial.setSupplierId(getLong(FRM_FIELD_SUPPLIERID));
            srcMaterial.setCategoryId(getLong(FRM_FIELD_CATEGORYID));
            srcMaterial.setSubCategoryId(getLong(FRM_FIELD_SUBCATEGORYID));
            srcMaterial.setSortby(getInt(FRM_FIELD_SORTBY));
            srcMaterial.setDescription(getString(FRM_FIELD_DESCRIPTION));
            srcMaterial.setLocationId(getLong(FRM_FIELD_LOCATIONID));
            srcMaterial.setTypeItem(getInt(FRM_FIELD_TYPE_ITEM));
            srcMaterial.setMerkId(getLong(FRM_FIELD_MERK_ID));
            srcMaterial.setShowImage(getInt(FRM_SHOW_IMAGE));
            srcMaterial.setOidCodeRange(getLong(FRM_FIELD_CODE_RANGE_ID));
            //for show update catalog
            srcMaterial.setShowUpdateCatalog(getInt(FRM_FIELD_SHOW_UPDATE_CATALOG));
            srcMaterial.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
            srcMaterial.setDateTo(getDate(FRM_FIELD_DATE_TO));
            //for show print discount qty
            srcMaterial.setShowDiscountQty(getInt(FRM_FIELD_SHOW_DISCOUNT_QTY));
            //for show Stock nol
            srcMaterial.setShowStokNol(getInt(FRM_FIELD_SHOW_STOCK_NOL));
            //for show hpp
            srcMaterial.setShowHpp(getInt(FRM_FIELD_SHOW_HPP));
            //for show list currency
            srcMaterial.setShowCurrency(getInt(FRM_FIELD_SHOW_CURRENCY));
            //for group item
            srcMaterial.setGroupItem(getInt(FRM_FIELD_GROUP_ITEM));
            //for select price type id
            //update opie-eyek 20130805
            srcMaterial.setSelectPriceTypeId(getLong(FRM_FIELD_PRICE_TYPE_ID));
            //for currency type id
            //srcMaterial.setSelectCurrencyTypeId(getLong(FRM_FIELD_CURRENCY_TYPE_ID));
            srcMaterial.setUseSerialNumber(getInt(FRM_FIELD_REQUIRED_SERIAL_NUMBER));
            
            srcMaterial.setStatusMaterial(getInt(FRM_FIELD_EDIT_MATERIAL));
            
            //srcMaterial.setSellLocation(getLong(FRM_FIELD_SELL_LOCATION));
            srcMaterial.setGondolaId(getLong(FRM_FIELD_GONDOLA_CODE));
            srcMaterial.setViewHppvsPrice(getInt(FRM_VIEW_PRICE_HPP));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
