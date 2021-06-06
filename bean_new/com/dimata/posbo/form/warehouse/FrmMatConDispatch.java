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

public class FrmMatConDispatch extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatConDispatch matDispatch;
    
    public static final String FRM_NAME_DISPATCH_MATERIAL		=  "FRM_NAME_DISPATCH_MATERIAL" ;
    
    public static final int FRM_FIELD_DISPATCH_MATERIAL_ID		=  0 ;
    public static final int FRM_FIELD_LOCATION_ID			=  1 ;
    public static final int FRM_FIELD_DISPATCH_TO			=  2 ;
    public static final int FRM_FIELD_LOCATION_TYPE			=  3 ;
    public static final int FRM_FIELD_DISPATCH_DATE			=  4 ;
    public static final int FRM_FIELD_DISPATCH_CODE			=  5 ;
    public static final int FRM_FIELD_DISPATCH_CODE_COUNTER		=  6 ;
    public static final int FRM_FIELD_DISPATCH_STATUS		=  7 ;
    public static final int FRM_FIELD_REMARK			=  8 ;
    public static final int FRM_FIELD_INVOICE_SUPPLIER              =  9 ;
    
    public static String[] fieldNames = {
        "FRM_FIELD_DISPATCH_MATERIAL_ID",  "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_DISPATCH_TO",  "FRM_FIELD_LOCATION_TYPE",
        "FRM_FIELD_DISPATCH_DATE", "FRM_FIELD_DISPATCH_CODE",
        "FRM_FIELD_DISPATCH_CODE_COUNTER", "FRM_FIELD_DF_STATUS",
        "FRM_FIELD_REMARK", "FRM_FIELD_INVOICE_SUPPLIER"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_LONG,  TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG,  TYPE_INT,
        TYPE_DATE + ENTRY_REQUIRED, TYPE_STRING,
        TYPE_INT,  TYPE_INT,
        TYPE_STRING, TYPE_STRING
    } ;
    
    public FrmMatConDispatch() {
    }
    
    public FrmMatConDispatch(MatConDispatch matDispatch) {
        this.matDispatch = matDispatch;
    }
    
    public FrmMatConDispatch(HttpServletRequest request, MatConDispatch matDispatch) {
        super(new FrmMatConDispatch(matDispatch), request);
        this.matDispatch = matDispatch;
    }
    
    public String getFormName() { return FRM_NAME_DISPATCH_MATERIAL; }
    
    public int[] getFieldTypes() { return fieldTypes; }
    
    public String[] getFieldNames() { return fieldNames; }
    
    public int getFieldSize() { return fieldNames.length; }
    
    public MatConDispatch getEntityObject(){ return matDispatch; }
    
    public void requestEntityObject(MatConDispatch matDispatch) {
        try {
            this.requestParam();
            matDispatch.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            matDispatch.setDispatchTo(getLong(FRM_FIELD_DISPATCH_TO));
            matDispatch.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
            matDispatch.setDispatchDate(getDate(FRM_FIELD_DISPATCH_DATE));
            matDispatch.setDispatchCode(getString(FRM_FIELD_DISPATCH_CODE));
            matDispatch.setDispatchStatus(getInt(FRM_FIELD_DISPATCH_STATUS));
            matDispatch.setRemark(getString(FRM_FIELD_REMARK));
            matDispatch.setInvoiceSupplier(getString(FRM_FIELD_INVOICE_SUPPLIER));
        }
        catch(Exception e) {
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
}
