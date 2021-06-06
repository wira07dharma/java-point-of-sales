package com.dimata.posbo.form.warehouse;

/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */

import com.dimata.posbo.entity.warehouse.MatStockOpnameItem;

public class FrmMatStockOpnameItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatStockOpnameItem stockOpnameItem;
    
    public static final String FRM_NAME_STOCKOPNAMEITEM	=  "FRM_NAME_STOCKOPNAMEITEM" ;
    
    public static final int FRM_FIELD_STOCK_OPNAME_ITEM_ID  =  0 ;
    public static final int FRM_FIELD_STOCK_OPNAME_ID       =  1 ;
    public static final int FRM_FIELD_MATERIAL_ID           =  2 ;
    public static final int FRM_FIELD_UNIT_ID               =  3 ;
    public static final int FRM_FIELD_QTY_OPNAME            =  4 ;
    public static final int FRM_FIELD_QTY_SOLD              =  5 ;
    public static final int FRM_FIELD_QTY_SYSTEM            =  6 ;
    public static final int FRM_FIELD_COST                  =  7 ;
    public static final int FRM_FIELD_PRICE                 =  8 ;
    //added by dewok 2018-02-26
    public static final int FRM_FIELD_KADAR_ID              =  9 ;
    public static final int FRM_FIELD_KADAR_OPNAME_ID       =  10 ;
    public static final int FRM_FIELD_BERAT                 =  11 ;
    public static final int FRM_FIELD_BERAT_OPNAME          =  12 ;
    public static final int FRM_FIELD_REMARK                =  13 ;
    public static final int FRM_FIELD_BERAT_SELISIH         =  14 ;
    public static final int FRM_FIELD_QTY_ITEM              =  15 ;
    public static final int FRM_FIELD_QTY_SELISIH           =  16 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_STOCK_OPNAME_ITEM_ID",  "FRM_FIELD_STOCK_OPNAME_ID",
        "FRM_FIELD_MATERIAL_ID", "FRM_FIELD_UNIT_ID"
        ,"FRM_FIELD_QTY_OPNAME", "FRM_FIELD_QTY_SOLD"
        , "FRM_FIELD_QTY_SYSTEM", "FRM_FIELD_COST"
        , "FRM_FIELD_PRICE",
        "FRM_FIELD_KADAR_ID",
        "FRM_FIELD_KADAR_OPNAME_ID",
        "FRM_FIELD_BERAT",
        "FRM_FIELD_BERAT_OPNAME",
        "FRM_FIELD_REMARK",
        "FRM_FIELD_BERAT_SELISIH",
        "FRM_FIELD_QTY_ITEM",
        "FRM_FIELD_QTY_SELISIH"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,  TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,  TYPE_LONG,
        TYPE_FLOAT, TYPE_FLOAT,
        TYPE_FLOAT, TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,  TYPE_LONG,
        TYPE_FLOAT, TYPE_FLOAT,
        TYPE_STRING, TYPE_FLOAT,
        TYPE_FLOAT, TYPE_FLOAT
    } ;
    
    public FrmMatStockOpnameItem() {
    }
    public FrmMatStockOpnameItem(MatStockOpnameItem stockOpnameItem) {
        this.stockOpnameItem = stockOpnameItem;
    }
    
    public FrmMatStockOpnameItem(HttpServletRequest request, MatStockOpnameItem stockOpnameItem) {
        super(new FrmMatStockOpnameItem(stockOpnameItem), request);
        this.stockOpnameItem = stockOpnameItem;
    }
    
    public String getFormName() { return FRM_NAME_STOCKOPNAMEITEM; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MatStockOpnameItem getEntityObject(){ return stockOpnameItem; }
    
    public void requestEntityObject(MatStockOpnameItem stockOpnameItem) {
        try {
            this.requestParam();
            stockOpnameItem.setStockOpnameId(getLong(FRM_FIELD_STOCK_OPNAME_ID));
            stockOpnameItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            stockOpnameItem.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            stockOpnameItem.setQtyOpname(getDouble(FRM_FIELD_QTY_OPNAME));
            stockOpnameItem.setQtySold(getDouble(FRM_FIELD_QTY_SOLD));
            stockOpnameItem.setQtySystem(getDouble(FRM_FIELD_QTY_SYSTEM));
            stockOpnameItem.setCost(getDouble(FRM_FIELD_COST));
            stockOpnameItem.setPrice(getDouble(FRM_FIELD_PRICE));
            
            stockOpnameItem.setKadarId(getLong(FRM_FIELD_KADAR_ID));
            stockOpnameItem.setKadarOpnameId(getLong(FRM_FIELD_KADAR_OPNAME_ID));
            stockOpnameItem.setBerat(getDouble(FRM_FIELD_BERAT));
            stockOpnameItem.setBeratOpname(getDouble(FRM_FIELD_BERAT_OPNAME));
            stockOpnameItem.setRemark(getString(FRM_FIELD_REMARK));
            stockOpnameItem.setBeratSelisih(getDouble(FRM_FIELD_BERAT_SELISIH));
            stockOpnameItem.setQtyItem(getDouble(FRM_FIELD_QTY_ITEM));
            stockOpnameItem.setQtySelisih(getDouble(FRM_FIELD_QTY_SELISIH));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
