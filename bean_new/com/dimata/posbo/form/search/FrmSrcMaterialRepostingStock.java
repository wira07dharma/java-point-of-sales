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

/**
 *
 * @author Mirah
 * 06062012
 */
public class FrmSrcMaterialRepostingStock extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcMaterialRepostingStock srcMaterialRepostingStock;

    public static final String FRM_NAME_SRCMATERIAL_REPOSTINGSTOCK = "FRM_NAME_SRCMATERIAL_REPOSTINGSTOCK";

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
    
    //adding search for reposting stock 06/06/2012 by Mirahu
    public static final int FRM_FIELD_PREV_DAYS = 23;
    

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
                TYPE_LONG

            };

    public FrmSrcMaterialRepostingStock() {
    }

    public FrmSrcMaterialRepostingStock(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        this.srcMaterialRepostingStock = srcMaterialRepostingStock;
    }

    public FrmSrcMaterialRepostingStock(HttpServletRequest request, SrcMaterialRepostingStock srcMaterialRepostingStock) {
        super(new FrmSrcMaterialRepostingStock(srcMaterialRepostingStock), request);
        this.srcMaterialRepostingStock = srcMaterialRepostingStock;
    }

    public String getFormName() {
        return FRM_NAME_SRCMATERIAL_REPOSTINGSTOCK;
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

    public SrcMaterialRepostingStock getEntityObject() {
        return srcMaterialRepostingStock;
    }

    public void requestEntityObject(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        try {
            this.requestParam();
            srcMaterialRepostingStock.setMatcode(getString(FRM_FIELD_MATCODE));
            srcMaterialRepostingStock.setMatname(getString(FRM_FIELD_MATNAME));
            srcMaterialRepostingStock.setSupplierId(getLong(FRM_FIELD_SUPPLIERID));
            srcMaterialRepostingStock.setCategoryId(getLong(FRM_FIELD_CATEGORYID));
            srcMaterialRepostingStock.setSubCategoryId(getLong(FRM_FIELD_SUBCATEGORYID));
            srcMaterialRepostingStock.setSortby(getInt(FRM_FIELD_SORTBY));
            srcMaterialRepostingStock.setDescription(getString(FRM_FIELD_DESCRIPTION));
            srcMaterialRepostingStock.setLocationId(getLong(FRM_FIELD_LOCATIONID));
            srcMaterialRepostingStock.setTypeItem(getInt(FRM_FIELD_TYPE_ITEM));
            srcMaterialRepostingStock.setMerkId(getLong(FRM_FIELD_MERK_ID));
            srcMaterialRepostingStock.setShowImage(getInt(FRM_SHOW_IMAGE));
            srcMaterialRepostingStock.setOidCodeRange(getLong(FRM_FIELD_CODE_RANGE_ID));
            //for show update catalog
            srcMaterialRepostingStock.setShowUpdateCatalog(getInt(FRM_FIELD_SHOW_UPDATE_CATALOG));
            srcMaterialRepostingStock.setDateFrom(getDate(FRM_FIELD_DATE_FROM));
            srcMaterialRepostingStock.setDateTo(getDate(FRM_FIELD_DATE_TO));
            //for show print discount qty
            srcMaterialRepostingStock.setShowDiscountQty(getInt(FRM_FIELD_SHOW_DISCOUNT_QTY));
            //for show Stock nol
            srcMaterialRepostingStock.setShowStokNol(getInt(FRM_FIELD_SHOW_STOCK_NOL));
            //for show hpp
            srcMaterialRepostingStock.setShowHpp(getInt(FRM_FIELD_SHOW_HPP));
            //for show list currency
            srcMaterialRepostingStock.setShowCurrency(getInt(FRM_FIELD_SHOW_CURRENCY));
            //for group item
            srcMaterialRepostingStock.setGroupItem(getInt(FRM_FIELD_GROUP_ITEM));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

    
}
