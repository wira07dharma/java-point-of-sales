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

public class FrmMatDistribution extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatDistribution matDistribution;
    
    public static final String FRM_NAME_DISTRIBUTION_MATERIAL		=  "FRM_NAME_DISTRIBUTION_MATERIAL" ;
    
    public static final int FRM_FIELD_DISTRIBUTION_MATERIAL_ID		=  0 ;
    public static final int FRM_FIELD_LOCATION_ID			=  1 ;
    public static final int FRM_FIELD_LOCATION_TYPE			=  2 ;
    public static final int FRM_FIELD_DISTRIBUTION_DATE			=  3 ;
    public static final int FRM_FIELD_DISTRIBUTION_CODE			=  4 ;
    public static final int FRM_FIELD_DISTRIBUTION_CODE_COUNTER		=  5 ;
    public static final int FRM_FIELD_DISTRIBUTION_STATUS		=  6 ;
    public static final int FRM_FIELD_REMARK                            =  7 ;
    public static final int FRM_FIELD_INVOICE_SUPPLIER                  =  8 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_DISTRIBUTION_MATERIAL_ID",  "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_LOCATION_TYPE",  "FRM_FIELD_DISTRIBUTION_DATE",
        "FRM_FIELD_DISTRIBUTION_CODE", "FRM_FIELD_DISTRIBUTION_CODE_COUNTER",
        "FRM_FIELD_DISTRIBUTION_STATUS", "FRM_FIELD_REMARK",
        "FRM_FIELD_INVOICE_SUPPLIER"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,  TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_DATE + ENTRY_REQUIRED,TYPE_STRING,
        TYPE_INT,  TYPE_INT,
        TYPE_STRING, TYPE_STRING + ENTRY_REQUIRED
    } ;
    
    public FrmMatDistribution() {
    }
    
    public FrmMatDistribution(MatDistribution matDistribution) {
        this.matDistribution = matDistribution;
    }
    
    public FrmMatDistribution(HttpServletRequest request, MatDistribution matDistribution) {
        super(new FrmMatDistribution(matDistribution), request);
        this.matDistribution = matDistribution;
    }
    
    public String getFormName() { return FRM_NAME_DISTRIBUTION_MATERIAL; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MatDistribution getEntityObject(){ return matDistribution; }
    
    public void requestEntityObject(MatDistribution matDistribution) {
        try {
            this.requestParam();
            matDistribution.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            matDistribution.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
            matDistribution.setDistributionDate(getDate(FRM_FIELD_DISTRIBUTION_DATE));
            matDistribution.setDistributionCode(getString(FRM_FIELD_DISTRIBUTION_CODE));
            matDistribution.setDistributionStatus(getInt(FRM_FIELD_DISTRIBUTION_STATUS));
            matDistribution.setRemark(getString(FRM_FIELD_REMARK));
            matDistribution.setInvoiceSupplier(getString(FRM_FIELD_INVOICE_SUPPLIER));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
