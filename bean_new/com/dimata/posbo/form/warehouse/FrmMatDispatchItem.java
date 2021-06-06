package com.dimata.posbo.form.warehouse;

import javax.servlet.http.*;

import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.warehouse.MatDispatchItem;

public class FrmMatDispatchItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatDispatchItem matDispatchItem;
    
    public static final String FRM_NAME_MATDISPATCHITEM = "FRM_NAME_MATDISPATCHITEM";
    
    public static final int FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID = 0;
    public static final int FRM_FIELD_DISPATCH_MATERIAL_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_UNIT_ID = 3;
    public static final int FRM_FIELD_QTY = 4;
    public static final int FRM_FIELD_HPP = 5;
    public static final int FRM_FIELD_HPP_TOTAL = 6;
    public static final int FRM_FIELD_BERAT_LAST = 7;
    public static final int FRM_FIELD_BERAT_CURRENT = 8;
    public static final int FRM_FIELD_BERAT_SELISIH = 9;
    public static final int FRM_FIELD_GONDOLA_ID = 10;
    public static final int FRM_FIELD_GONDOLA_TO_ID = 11;
    public static final int FRM_FIELD_HPP_AWAL = 12;
    public static final int FRM_FIELD_ONGKOS = 13;
    public static final int FRM_FIELD_ONGKOS_AWAL = 14;
    public static final int FRM_FIELD_HPP_TOTAL_AWAL = 15;
    
    public static String[] fieldNames = {
        "FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID",
        "FRM_FIELD_DISPATCH_MATERIAL_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_QTY",
        "FRM_FIELD_HPP",
        "FRM_FIELD_HPP_TOTAL",
        "FRM_FIELD_BERAT_LAST",
        "FRM_FIELD_BERAT_CURRENT",
        "FRM_FIELD_BERAT_SELISIH",
        "FRM_FIELD_GONDOLA_ID",
        "FRM_FIELD_GONDOLA_TO_ID",
        "FRM_FIELD_HPP_AWAL",
        "FRM_FIELD_ONGKOS",
        "FRM_FIELD_ONGKOS_AWAL",
        "FRM_FIELD_HPP_TOTAL_AWAL"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public FrmMatDispatchItem() {
    }
    
    public FrmMatDispatchItem(MatDispatchItem matDispatchItem) {
        this.matDispatchItem = matDispatchItem;
    }
    
    public FrmMatDispatchItem(HttpServletRequest request, MatDispatchItem matDispatchItem) {
        super(new FrmMatDispatchItem(matDispatchItem), request);
        this.matDispatchItem = matDispatchItem;
    }
    
    public String getFormName() {
        return FRM_NAME_MATDISPATCHITEM;
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
    
    public MatDispatchItem getEntityObject() {
        return matDispatchItem;
    }
    
    public void requestEntityObject(MatDispatchItem matDispatchItem) {
        try {
            this.requestParam();
            matDispatchItem.setDispatchMaterialId(getLong(FRM_FIELD_DISPATCH_MATERIAL_ID));
            matDispatchItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            matDispatchItem.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            matDispatchItem.setQty(getDouble(FRM_FIELD_QTY));
            matDispatchItem.setHpp(getDouble(FRM_FIELD_HPP));
            matDispatchItem.setHppTotal(getDouble(FRM_FIELD_HPP_TOTAL));
            matDispatchItem.setBeratLast(getDouble(FRM_FIELD_BERAT_LAST));
            matDispatchItem.setBeratCurrent(getDouble(FRM_FIELD_BERAT_CURRENT));
            matDispatchItem.setBeratSelisih(getDouble(FRM_FIELD_BERAT_SELISIH));
            matDispatchItem.setGondolaId(getLong(FRM_FIELD_GONDOLA_ID));
            matDispatchItem.setGondolaToId(getLong(FRM_FIELD_GONDOLA_TO_ID));
            matDispatchItem.setHppAwal(getDouble(FRM_FIELD_HPP_AWAL));
            matDispatchItem.setOngkos(getDouble(FRM_FIELD_ONGKOS));
            matDispatchItem.setOngkosAwal(getDouble(FRM_FIELD_ONGKOS_AWAL));
            matDispatchItem.setHppTotalAwal(getDouble(FRM_FIELD_HPP_TOTAL_AWAL));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
