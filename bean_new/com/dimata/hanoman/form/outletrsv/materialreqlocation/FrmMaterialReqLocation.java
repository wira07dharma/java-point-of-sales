/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.materialreqlocation;

import com.dimata.hanoman.entity.outletrsv.materialreqlocation.MaterialReqLocation;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMaterialReqLocation extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MaterialReqLocation entMaterialReqLocation;
    public static final String FRM_NAME_MATERIAL_REQ_LOCATION = "FRM_NAME_MATERIAL_REQ_LOCATION";
    public static final int FRM_FIELD_MATERIAL_REQ_LOCATION_ID = 0;
    public static final int FRM_FIELD_MATERIAL_ID = 1;
    public static final int FRM_FIELD_POS_ROOM_CLASS_ID = 2;
    public static final int FRM_FIELD_DURATION = 3;
    public static final int FRM_FIELD_ORDER_INDEX = 4;
    public static final int FRM_FIELD_IGNORE_PIC = 5;

    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_REQ_LOCATION_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_POS_ROOM_CLASS_ID",
        "FRM_FIELD_DURATION",
        "FRM_FIELD_ORDER_INDEX",
        "FRM_FIELD_IGNORE_PIC"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT
    };

    public FrmMaterialReqLocation() {
    }

    public FrmMaterialReqLocation(MaterialReqLocation entMaterialReqLocation) {
        this.entMaterialReqLocation = entMaterialReqLocation;
    }

    public FrmMaterialReqLocation(HttpServletRequest request, MaterialReqLocation entMaterialReqLocation) {
        super(new FrmMaterialReqLocation(entMaterialReqLocation), request);
        this.entMaterialReqLocation = entMaterialReqLocation;
    }

    public String getFormName() {
        return FRM_NAME_MATERIAL_REQ_LOCATION;
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

    public MaterialReqLocation getEntityObject() {
        return entMaterialReqLocation;
    }

    public void requestEntityObject(MaterialReqLocation entMaterialReqLocation) {
        try {
            this.requestParam();
//            entMaterialReqLocation.setMaterialReqLocationId(getLong(FRM_FIELD_MATERIAL_REQ_LOCATION_ID));
            entMaterialReqLocation.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entMaterialReqLocation.setPosRoomClassId(getLong(FRM_FIELD_POS_ROOM_CLASS_ID));
            entMaterialReqLocation.setDuration(getFloat(FRM_FIELD_DURATION));
            entMaterialReqLocation.setOrderIndex(getInt(FRM_FIELD_ORDER_INDEX));
            entMaterialReqLocation.setIgnorePIC(getInt(FRM_FIELD_IGNORE_PIC));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
