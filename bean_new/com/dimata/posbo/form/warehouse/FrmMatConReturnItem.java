package com.dimata.posbo.form.warehouse;

/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.warehouse.*;

public class FrmMatConReturnItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatConReturnItem matReturnItem;
    
    public static final String FRM_NAME_MATRETURNITEM           =  "FRM_NAME_MATRETURNITEM" ;
    
    public static final int FRM_FIELD_RETURN_MATERIAL_ITEM_ID   =  0 ;
    public static final int FRM_FIELD_RETURN_MATERIAL_ID        =  1 ;
    public static final int FRM_FIELD_MATERIAL_ID               =  2 ;
    public static final int FRM_FIELD_UNIT_ID                   =  3 ;
    public static final int FRM_FIELD_COST                      =  4 ;
    public static final int FRM_FIELD_CURRENCY_ID               =  5 ;
    public static final int FRM_FIELD_QTY                       =  6 ;
    public static final int FRM_FIELD_TOTAL                     =  7 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_RETURN_MATERIAL_ITEM_ID",
        "FRM_FIELD_RETURN_MATERIAL_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_COST",
        "FRM_FIELD_CURRENCY_ID",
        "FRM_FIELD_QTY",
        "FRM_FIELD_TOTAL"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    } ;
    
    public FrmMatConReturnItem() {
    }
    
    public FrmMatConReturnItem(MatConReturnItem matReturnItem) {
        this.matReturnItem = matReturnItem;
    }
    
    public FrmMatConReturnItem(HttpServletRequest request, MatConReturnItem matReturnItem){
        super(new FrmMatConReturnItem(matReturnItem), request);
        this.matReturnItem = matReturnItem;
    }
    
    public String getFormName() { return FRM_NAME_MATRETURNITEM; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MatConReturnItem getEntityObject(){ return matReturnItem; }
    
    public void requestEntityObject(MatConReturnItem matReturnItem) {
        try {
            this.requestParam();
            matReturnItem.setReturnMaterialId(getLong(FRM_FIELD_RETURN_MATERIAL_ID));
            matReturnItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            matReturnItem.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            matReturnItem.setCost(getDouble(FRM_FIELD_COST));
            matReturnItem.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
            matReturnItem.setQty(getDouble(FRM_FIELD_QTY));
            matReturnItem.setTotal(getDouble(FRM_FIELD_TOTAL));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
