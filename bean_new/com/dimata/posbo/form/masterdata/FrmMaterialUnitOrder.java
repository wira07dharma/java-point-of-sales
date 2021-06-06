/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author dimata005
 */
/* java package */
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmMaterialUnitOrder extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MaterialUnitOrder matMaterialUnitOrder;
    public static final String FRM_NAME_MATERIAL_UNIT_ORDER = "FRM_NAME_MATERIAL_UNIT_ORDER";
    public static final int FRM_FIELD_MATERIAL_UNIT_BUY_ID = 0;
    public static final int FRM_FIELD_MATERIAL_ID = 1;
    public static final int FRM_UNIT_ID = 2;
    //adding status ditampilkan atau tidak by mirahu 20120511
    public static final int FRM_FIELD_MINIMUM_QTY_ORDER = 3;
    public static String[] fieldNames =
            {
        "FRM_FIELD_MATERIAL_UNIT_BUY_ID", "FRM_FIELD_MATERIAL_ID", "FRM_UNIT_ID", "FRM_FIELD_MINIMUM_QTY_ORDER"
    };
    public static int[] fieldTypes =
            {
        TYPE_LONG, TYPE_LONG, TYPE_LONG , TYPE_FLOAT + ENTRY_REQUIRED
    };

    public FrmMaterialUnitOrder() {
    }

    public FrmMaterialUnitOrder(MaterialUnitOrder matMaterialUnitOrder) {
        this.matMaterialUnitOrder = matMaterialUnitOrder;
    }

    public FrmMaterialUnitOrder(HttpServletRequest request, MaterialUnitOrder matMaterialUnitOrder) {
        super(new FrmMaterialUnitOrder(matMaterialUnitOrder), request);


        this.matMaterialUnitOrder = matMaterialUnitOrder;
    }

    public String getFormName() {
        return FRM_NAME_MATERIAL_UNIT_ORDER;
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

    public MaterialUnitOrder getEntityObject() {
        return matMaterialUnitOrder;
    }

    public void requestEntityObject(MaterialUnitOrder matMaterialUnitOrder) {
        try {
            this.requestParam();
            matMaterialUnitOrder.setMaterialID(getLong(FRM_FIELD_MATERIAL_ID));
            matMaterialUnitOrder.setUnitID(getLong(FRM_UNIT_ID));
            //adding status ditampilkan atau tidak by mirahu 20120511
            matMaterialUnitOrder.setMinimumQtyOrder(getDouble(FRM_FIELD_MINIMUM_QTY_ORDER));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}