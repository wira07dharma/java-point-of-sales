/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.materialreqfacility;


import com.dimata.hanoman.entity.outletrsv.materialreqfacility.MaterialReqFacility;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMaterialReqFacility extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MaterialReqFacility entMaterialReqFacility;
    public static final String FRM_NAME_MATERIAL_REQ_FACILITY = "FRM_NAME_MATERIAL_REQ_FACILITY";
    public static final int FRM_FIELD_MATERIAL_REQ_FACILITY_ID = 0;
    public static final int FRM_FIELD_MATERIAL_REQ_LOCATION_ID = 1;
    public static final int FRM_FIELD_AKTIVA_ID = 2;
    public static final int FRM_FIELD_MATERIAL_ID = 3;
    public static final int FRM_FIELD_NUMBER = 4;
    public static final int FRM_FIELD_NOTE = 5;
    public static final int FRM_FIELD_ORDER_INDEX = 6;
    public static final int FRM_FIELD_DURATION = 7;

    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_REQ_FACILITY_ID",
        "FRM_FIELD_MATERIAL_REQ_LOCATION_ID",
        "FRM_FIELD_AKTIVA_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_NUMBER",
        "FRM_FIELD_NOTE",
        "FRM_FIELD_ORDER_INDEX",
        "FRM_FIELD_DURATION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT
    };

    public FrmMaterialReqFacility() {
    }

    public FrmMaterialReqFacility(MaterialReqFacility entMaterialReqFacility) {
        this.entMaterialReqFacility = entMaterialReqFacility;
    }

    public FrmMaterialReqFacility(HttpServletRequest request, MaterialReqFacility entMaterialReqFacility) {
        super(new FrmMaterialReqFacility(entMaterialReqFacility), request);
        this.entMaterialReqFacility = entMaterialReqFacility;
    }

    public String getFormName() {
        return FRM_NAME_MATERIAL_REQ_FACILITY;
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

    public MaterialReqFacility getEntityObject() {
        return entMaterialReqFacility;
    }

    public void requestEntityObject(MaterialReqFacility entMaterialReqFacility) {
        try {
            this.requestParam();
//            entMaterialReqFacility.setMaterialReqFacilityId(getLong(FRM_FIELD_MATERIAL_REQ_FACILITY_ID));
            entMaterialReqFacility.setMaterialReqLocationId(getLong(FRM_FIELD_MATERIAL_REQ_LOCATION_ID));
            entMaterialReqFacility.setAktivaId(getLong(FRM_FIELD_AKTIVA_ID));
            entMaterialReqFacility.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entMaterialReqFacility.setNumber(getFloat(FRM_FIELD_NUMBER));
            entMaterialReqFacility.setNote(getString(FRM_FIELD_NOTE));
            entMaterialReqFacility.setOrderIndex(getInt(FRM_FIELD_ORDER_INDEX));
            entMaterialReqFacility.setDuration(getFloat(FRM_FIELD_DURATION));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
