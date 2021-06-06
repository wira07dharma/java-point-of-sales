/*
 * Form Name  	:  FrmMaterialUnit.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  	:  [authorName]
 * @version  	:  [version]
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

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

public class FrmMaterialStock extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MaterialStock materialStock;
    
    public static final String FRM_NAME_MATERIALSTOCK	=  "FRM_NAME_MATERIALSTOCK" ;
    
    public static final int FRM_FIELD_MATERIAL_STOCK_ID =  0 ;
    public static final int FRM_FIELD_PERIODE_ID        =  1 ;
    public static final int FRM_FIELD_MATERIAL_UNIT_ID  =  2 ;
    public static final int FRM_FIELD_LOCATION_ID       =  3 ;
    public static final int FRM_FIELD_QTY               =  4 ;
    public static final int FRM_FIELD_QTY_MIN           =  5 ;
    public static final int FRM_FIELD_QTY_MAX           =  6 ;
    public static final int FRM_FIELD_QTY_IN            =  7 ;
    public static final int FRM_FIELD_QTY_OUT           =  8 ;
    public static final int FRM_FIELD_OPENING_QTY       =  9 ;
    public static final int FRM_FIELD_CLOSING_QTY       =  10 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_STOCK_ID",
        "FRM_FIELD_PERIODE_ID",
        "FRM_FIELD_MATERIAL_UNIT_ID",
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_QTY",
        "FRM_FIELD_QTY_MIN",
        "FRM_FIELD_QTY_MAX",
        "FRM_FIELD_QTY_IN",
        "FRM_FIELD_QTY_OUT",
        "FRM_FIELD_OPENING_QTY",
        "FRM_FIELD_CLOSING_QTY"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    } ;
    
    public FrmMaterialStock() {
    }
    
    public FrmMaterialStock(MaterialStock materialStock) {
        this.materialStock = materialStock;
    }
    
    public FrmMaterialStock(HttpServletRequest request, MaterialStock materialStock) {
        super(new FrmMaterialStock(materialStock), request);
        this.materialStock = materialStock;
    }
    
    public String getFormName() { return FRM_NAME_MATERIALSTOCK; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MaterialStock getEntityObject(){ return materialStock; }
    
    public void requestEntityObject(MaterialStock materialStock) {
        try {
            this.requestParam();
            materialStock.setPeriodeId(getLong(FRM_FIELD_PERIODE_ID));
            materialStock.setMaterialUnitId(getLong(FRM_FIELD_MATERIAL_UNIT_ID));
            materialStock.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            materialStock.setQty(getDouble(FRM_FIELD_QTY));
            materialStock.setQtyMin(getDouble(FRM_FIELD_QTY_MIN));
            materialStock.setQtyMax(getDouble(FRM_FIELD_QTY_MAX));
            materialStock.setQtyIn(getDouble(FRM_FIELD_QTY_IN));
            materialStock.setQtyOut(getDouble(FRM_FIELD_QTY_OUT));
            materialStock.setOpeningQty(getDouble(FRM_FIELD_OPENING_QTY));
            materialStock.setClosingQty(getDouble(FRM_FIELD_CLOSING_QTY));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
