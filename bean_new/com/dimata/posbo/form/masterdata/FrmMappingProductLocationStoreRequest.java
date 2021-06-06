/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.masterdata;

/**
 *
 * @author Witar
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.masterdata.*;

public class FrmMappingProductLocationStoreRequest extends FRMHandler implements I_FRMInterface, I_FRMType{
    private MappingProductLocationStoreRequest mappingProductLocationStoreRequest;
    public static final String FRM_MAPPING_PRODUCT_LOCATION_STORE_REQUEST = "FRM_MAPPING_PRODUCT_LOCATION_STORE_REQUEST";
    public static final int FRM_FIELD_POS_LOCATION_MAPPING_STORE_REQUEST_ID = 0;
    public static final int FRM_FIELD_MATERIAL_ID = 1;
    public static final int FRM_FIELD_LOCATION_ID = 2;
    public static String[] fieldNames ={
        "FRM_FIELD_POS_LOCATION_MAPPING_STORE_REQUEST_ID", 
        "FRM_FIELD_MATERIAL_ID", 
        "FRM_FIELD_LOCATION_ID"
    };
    
    public static int[] fieldTypes ={
        TYPE_LONG, 
        TYPE_LONG, 
        TYPE_STRING
    };
    
    public FrmMappingProductLocationStoreRequest() {
    }

    public FrmMappingProductLocationStoreRequest(MappingProductLocationStoreRequest mappingProductLocationStoreRequest) {
        this.mappingProductLocationStoreRequest = mappingProductLocationStoreRequest;
    }

    public FrmMappingProductLocationStoreRequest(HttpServletRequest request, MappingProductLocationStoreRequest mappingProductLocationStoreRequest) {
        super(new FrmMappingProductLocationStoreRequest(mappingProductLocationStoreRequest), request);


        this.mappingProductLocationStoreRequest = mappingProductLocationStoreRequest;
    }

    public String getFormName() {
        return FRM_MAPPING_PRODUCT_LOCATION_STORE_REQUEST;
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

    public MappingProductLocationStoreRequest getEntityObject() {
        return mappingProductLocationStoreRequest;
    }
    
    public void requestEntityObject(MappingProductLocationStoreRequest mappingProductLocationStoreRequest) {
        try {
            this.requestParam();
            mappingProductLocationStoreRequest.setOID(getLong(FRM_FIELD_POS_LOCATION_MAPPING_STORE_REQUEST_ID));
            mappingProductLocationStoreRequest.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            mappingProductLocationStoreRequest.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
