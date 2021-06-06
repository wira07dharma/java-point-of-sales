package com.dimata.posbo.form.masterdata;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.masterdata.MatMappLocation;

import javax.servlet.http.HttpServletRequest;

public class FrmMatMappLocation extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatMappLocation matMappLocation;

    public static final String FRM_NAME_MAT_MAPP_LOCATION = "FRM_NAME_MAT_MAPP_LOCATION";

    public static final int FRM_FIELD_MATERIAL_ID = 0;
    public static final int FRM_FIELD_LOCATION_ID = 1;

    public static String[] fieldNames =
            {
                "FRM_FIELD_MATERIAL_ID",
                "FRM_FIELD_LOCATION_ID"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG,  TYPE_LONG
            };

    public FrmMatMappLocation() {
    }

    public FrmMatMappLocation(MatMappLocation matMappLocation) {
        this.matMappLocation = matMappLocation;
    }

    public FrmMatMappLocation(HttpServletRequest request, MatMappLocation matMappLocation) {
        super(new FrmMatMappLocation(matMappLocation), request);
        this.matMappLocation = matMappLocation;
    }

    public String getFormName() {
        return FRM_NAME_MAT_MAPP_LOCATION;
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

    public MatMappLocation getEntityObject() {
        return matMappLocation;
    }

    public void requestEntityObject(MatMappLocation matMappLocation) {
        try {
            this.requestParam();
            //matMappLocation.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            //matMappLocation.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
