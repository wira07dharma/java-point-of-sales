package com.dimata.posbo.form.warehouse;

/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.warehouse.MatDistributionDetail;

public class FrmMatDistributionDetail extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    private MatDistributionDetail matDistributionDetail;
    
    public static final String FRM_NAME_MAT_DISTRIBUTION_DETAIL	=  "FRM_NAME_MAT_DISTRIBUTION_DETAIL" ;
    
    public static final int FRM_FIELD_DISPATCH_MATERIAL_ID      =  0 ;
    public static final int FRM_FIELD_DSTRIBUTION_MATERIAL_ID   =  1 ;
    public static final int FRM_FIELD_MATERIAL_ID               =  2 ;
    public static final int FRM_FIELD_LOCATION_ID               =  3 ;
    public static final int FRM_FIELD_QTY                       =  4 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_DISPATCH_MATERIAL_ID",  "FRM_FIELD_DSTRIBUTION_MATERIAL_ID",
        "FRM_FIELD_MATERIAL_ID",  "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_QTY"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,  TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,  TYPE_LONG + ENTRY_REQUIRED,
        TYPE_FLOAT
    } ;
    
    public FrmMatDistributionDetail() {
    }
    public FrmMatDistributionDetail(MatDistributionDetail matDistributionDetail) {
        this.matDistributionDetail = matDistributionDetail;
    }
    
    public FrmMatDistributionDetail(HttpServletRequest request, MatDistributionDetail matDistributionDetail) {
        super(new FrmMatDistributionDetail(matDistributionDetail), request);
        this.matDistributionDetail = matDistributionDetail;
    }
    
    public String getFormName() { return FRM_NAME_MAT_DISTRIBUTION_DETAIL; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MatDistributionDetail getEntityObject(){ return matDistributionDetail; }
    
    public void requestEntityObject(MatDistributionDetail matDistributionDetail) {
        try {
            this.requestParam();
            matDistributionDetail.setDispatchMaterialId(getLong(FRM_FIELD_DISPATCH_MATERIAL_ID));
            matDistributionDetail.setDistributionMaterialId(getLong(FRM_FIELD_DSTRIBUTION_MATERIAL_ID));
            matDistributionDetail.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            matDistributionDetail.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            matDistributionDetail.setQty(getDouble(FRM_FIELD_QTY));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
