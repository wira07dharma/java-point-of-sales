package com.dimata.posbo.form.warehouse;

/* java package */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.warehouse.MatConReceiveItem;

public class FrmMatConReceiveItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatConReceiveItem matReceiveItem;
    
    public static final String FRM_NAME_MATRECEIVEITEM = "FRM_NAME_MATRECEIVEITEM";
    
    public static final int FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID = 0;
    public static final int FRM_FIELD_RECEIVE_MATERIAL_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_EXPIRED_DATE = 3;
    public static final int FRM_FIELD_UNIT_ID = 4;
    public static final int FRM_FIELD_COST = 5;
    public static final int FRM_FIELD_CURRENCY_ID = 6;
    public static final int FRM_FIELD_QTY = 7;
    public static final int FRM_FIELD_DISCOUNT = 8;
    public static final int FRM_FIELD_TOTAL = 9;
    public static final int FRM_FIELD_DISCOUNT2 = 10;
    public static final int FRM_FIELD_DISC_NOMINAL = 11;
    public static final int FRM_FIELD_CURR_BUYING_PRICE = 12;
    public static final int FRM_FIELD_FORWARDER_COST = 13;
    
    public static String[] fieldNames = {
        "FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID",
        "FRM_FIELD_RECEIVE_MATERIAL_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_EXPIRED_DATE",
        "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_COST",
        "FRM_FIELD_CURRENCY_ID",
        "FRM_FIELD_QTY",
        "FRM_FIELD_DISCOUNT",
        "FRM_FIELD_TOTAL",
        "FRM_FIELD_DISCOUNT2",
        "FRM_FIELD_DISC_NOMINAL",
        "FRM_FIELD_CURR_BUYING_PRICE",
        "FRM_FIELD_FORWARDER_COST"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public FrmMatConReceiveItem() {
    }
    
    public FrmMatConReceiveItem(MatConReceiveItem matReceiveItem) {
        this.matReceiveItem = matReceiveItem;
    }
    
    public FrmMatConReceiveItem(HttpServletRequest request, MatConReceiveItem matReceiveItem) {
        super(new FrmMatConReceiveItem(matReceiveItem), request);
        this.matReceiveItem = matReceiveItem;
    }
    
    public String getFormName() {
        return FRM_NAME_MATRECEIVEITEM;
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
    
    public MatConReceiveItem getEntityObject() {
        return matReceiveItem;
    }
    
    public void requestEntityObject(MatConReceiveItem matReceiveItem) {
        try {
            this.requestParam();
            matReceiveItem.setReceiveMaterialId(getLong(FRM_FIELD_RECEIVE_MATERIAL_ID));
            matReceiveItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            matReceiveItem.setExpiredDate(getDate(FRM_FIELD_EXPIRED_DATE));
            matReceiveItem.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            matReceiveItem.setCost(getDouble(FRM_FIELD_COST));
            matReceiveItem.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
            matReceiveItem.setQty(getDouble(FRM_FIELD_QTY));
            matReceiveItem.setDiscount(getDouble(FRM_FIELD_DISCOUNT));
            matReceiveItem.setTotal(getDouble(FRM_FIELD_TOTAL));
            matReceiveItem.setDiscount2(getDouble(FRM_FIELD_DISCOUNT2));
            matReceiveItem.setDiscNominal(getDouble(FRM_FIELD_DISC_NOMINAL));
            matReceiveItem.setCurrBuyingPrice(getDouble(FRM_FIELD_CURR_BUYING_PRICE));
            matReceiveItem.setForwarderCost(getDouble(FRM_FIELD_FORWARDER_COST));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
}
