package com.dimata.posbo.form.masterdata;

/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmMinMaxStock extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MinMaxStock minMaxStock;
    
    public static final String FRM_NAME_MINMAXSTOCK = "FRM_NAME_MINMAXSTOCK" ;
    
    public static final int FRM_FIELD_MINMAXSTOCK_ID    =  0 ;
    public static final int FRM_FIELD_MATERIAL_ID       =  1 ;
    public static final int FRM_FIELD_LOCATION_ID       =  2 ;
    public static final int FRM_FIELD_QTY_MIN           =  3 ;
    public static final int FRM_FIELD_QTY_MAX           =  4 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_MINMAXSTOCK_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_QTY_MIN",
        "FRM_FIELD_QTY_MAX"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_FLOAT,
        TYPE_FLOAT
    } ;
    
    public FrmMinMaxStock() {
    }
    
    public FrmMinMaxStock(MinMaxStock minMaxStock) {
        this.minMaxStock = minMaxStock;
    }
    
    public FrmMinMaxStock(HttpServletRequest request, MinMaxStock minMaxStock) {
        super(new FrmMinMaxStock(minMaxStock), request);
        this.minMaxStock = minMaxStock;
    }
    
    public String getFormName() { return FRM_NAME_MINMAXSTOCK; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MinMaxStock getEntityObject(){ return minMaxStock; }
    
    public void requestEntityObject(MinMaxStock minMaxStock) {
        try {
            this.requestParam();
            minMaxStock.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            minMaxStock.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            minMaxStock.setQtyMin(getDouble(FRM_FIELD_QTY_MIN));
            minMaxStock.setQtyMax(getDouble(FRM_FIELD_QTY_MAX));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
