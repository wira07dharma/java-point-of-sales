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

public class FrmMaterialComposit extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MaterialComposit materialComposit;
    
    public static final String FRM_NAME_MATERIALCOMPOSIT = "FRM_NAME_MATERIALCOMPOSIT" ;
    //update opie 28-06-2012
    public static final int FRM_FIELD_MATERIAL_COMPOSIT_ID  =  0 ;
    public static final int FRM_FIELD_MATERIAL_ID           =  1 ;
    public static final int FRM_FIELD_MATERIAL_COMPOSER_ID  =  2 ;
    public static final int FRM_FIELD_UNIT_ID               =  3 ;
    public static final int FRM_FIELD_QTY                   =  4 ;
    public static final int FRM_FIELD_QTY_INPUT             =  5 ;
    public static final int FRM_FIELD_UNIT_ID_KONVERSI     =  6 ;
	public static final int FRM_FIELD_SEQUENCE_IDX			=  7 ;
	public static final int FRM_FIELD_AUTO_FILL				=  8 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_COMPOSIT_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_MATERIAL_COMPOSER_ID",
        "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_QTY",
        "FRM_FIELD_QTY_INPUT",
        "FRM_FIELD_UNIT_ID_KONVERSI",
		"FRM_FIELD_SEQUENCE_IDX",
		"FRM_FIELD_AUTO_FILL"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
		TYPE_INT,
		TYPE_INT
    } ;
    
    public FrmMaterialComposit() {
    }
    
    public FrmMaterialComposit(MaterialComposit materialComposit) {
        this.materialComposit = materialComposit;
    }
    
    public FrmMaterialComposit(HttpServletRequest request, MaterialComposit materialComposit) {
        super(new FrmMaterialComposit(materialComposit), request);
        this.materialComposit = materialComposit;
    }
    
    public String getFormName() { return FRM_NAME_MATERIALCOMPOSIT; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MaterialComposit getEntityObject(){ return materialComposit; }
    
    public void requestEntityObject(MaterialComposit materialComposit) {
        try {
            this.requestParam();
            materialComposit.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            materialComposit.setMaterialComposerId(getLong(FRM_FIELD_MATERIAL_COMPOSER_ID));
            materialComposit.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            materialComposit.setQty(getDouble(FRM_FIELD_QTY));
			materialComposit.setSequenceIdx(getInt(FRM_FIELD_SEQUENCE_IDX));
			materialComposit.setAutoFill(getInt(FRM_FIELD_AUTO_FILL));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
