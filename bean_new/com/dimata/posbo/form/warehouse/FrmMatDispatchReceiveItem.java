/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.warehouse;

import javax.servlet.http.*;

import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.warehouse.MatDispatchReceiveItem;
//import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.warehouse.MatDispatchItem;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.masterdata.*;


public class FrmMatDispatchReceiveItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatDispatchReceiveItem matDispatchReceiveItem;
    



    public static final String FRM_NAME_MATDISPATCHRECEIVEITEM = "FRM_NAME_MATDISPATCHRECEIVEITEM";

    public static final int FRM_FIELD_DF_REC_ITEM_ID = 0;
    public static final int FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID = 1;
    public static final int FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID = 2;
    public static final int FRM_FIELD_DF_REC_GROUP_ID = 3;
    public static final int FRM_FIELD_DISPATCH_MATERIAL_ID = 4;
    public static final int FRM_FIELD_MATERIAL_SOURCE_ID = 5;
    public static final int FRM_FIELD_UNIT_SOURCE_ID = 6;
    public static final int FRM_FIELD_QTY_SOURCE = 7;
    public static final int FRM_FIELD_RECEIVE_MATERIAL_ID = 8;
    public static final int FRM_FIELD_MATERIAL_TARGET_ID = 9;
    public static final int FRM_FIELD_UNIT_TARGET_ID = 10;
    public static final int FRM_FIELD_QTY_TARGET = 11;
    public static final int FRM_FIELD_SKU_SOURCE = 12;
    public static final int FRM_FIELD_NAME_SOURCE = 13;
    public static final int FRM_FIELD_SKU_TARGET = 14;
    public static final int FRM_FIELD_NAME_TARGET = 15;
    public static final int FRM_FIELD_CODE_SOURCE = 16;
    public static final int FRM_FIELD_CODE_TARGET = 17;
    //for nilai stok
    public static final int FRM_FIELD_HPP_SOURCE = 18;
    public static final int FRM_FIELD_HPP_TOTAL_SOURCE = 19;
    public static final int FRM_FIELD_COST_TARGET = 20;
    public static final int FRM_FIELD_TOTAL_TARGET = 21;



    public static String[] fieldNames = {
        "FRM_FIELD_DF_REC_ITEM_ID", "FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID",
        "FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID", "FRM_FIELD_DF_REC_GROUP_ID",
        "FRM_FIELD_DISPATCH_MATERIAL_ID", "FRM_FIELD_MATERIAL_SOURCE_ID",
        "FRM_FIELD_UNIT_SOURCE_ID", "FRM_FIELD_QTY_SOURCE",
        "FRM_FIELD_RECEIVE_MATERIAL_ID","FRM_FIELD_MATERIAL_TARGET_ID",
        "FRM_FIELD_UNIT_TARGET_ID", "FRM_FIELD_QTY_TARGET",
        "FRM_FIELD_SKU_SOURCE", "FRM_FIELD_NAME_SOURCE",
        "FRM_FIELD_SKU_TARGET", "FRM_FIELD_NAME_TARGET",
        "FRM_FIELD_CODE_SOURCE", "FRM_FIELD_CODE_TARGET",
        "FRM_FIELD_HPP_SOURCE", "FRM_FIELD_HPP_TOTAL_SOURCE",
        "FRM_FIELD_COST_TARGET", "FRM_FIELD_TOTAL_TARGET"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_LONG,
        TYPE_LONG, TYPE_LONG,
        TYPE_LONG, TYPE_LONG,
        TYPE_LONG, TYPE_FLOAT,
        TYPE_LONG, TYPE_LONG,
        TYPE_LONG, TYPE_FLOAT,
        TYPE_STRING, TYPE_STRING,
        TYPE_STRING, TYPE_STRING,
        TYPE_STRING, TYPE_STRING,
        TYPE_FLOAT, TYPE_FLOAT,
        TYPE_FLOAT, TYPE_FLOAT
    };

    public FrmMatDispatchReceiveItem() {
    }

    public FrmMatDispatchReceiveItem(MatDispatchReceiveItem matDispatchReceiveItem) {
        this.matDispatchReceiveItem = matDispatchReceiveItem;
    }

    public FrmMatDispatchReceiveItem(HttpServletRequest request, MatDispatchReceiveItem matDispatchReceiveItem) {
        super(new FrmMatDispatchReceiveItem(matDispatchReceiveItem), request);
        this.matDispatchReceiveItem = matDispatchReceiveItem;
    }

    public String getFormName() {
        return FRM_NAME_MATDISPATCHRECEIVEITEM;
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

    public MatDispatchReceiveItem getEntityObject() {
        return matDispatchReceiveItem;
    }

    public void requestEntityObject(MatDispatchReceiveItem matDispatchReceiveItem) {
        try {
            this.requestParam();

           

            matDispatchReceiveItem.getTargetItem().setOID(getLong(FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID));
            matDispatchReceiveItem.getSourceItem().setOID(getLong(FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID));
            matDispatchReceiveItem.setDfRecGroupId(getLong(FRM_FIELD_DF_REC_GROUP_ID));
            matDispatchReceiveItem.setDispatchMaterialId(getLong(FRM_FIELD_DISPATCH_MATERIAL_ID));

            MatDispatchItem matDispatchItem = matDispatchReceiveItem.getSourceItem();
            MatReceiveItem matReceiveItem = matDispatchReceiveItem.getTargetItem();
            Material materialSource = matDispatchItem.getMaterialSource();
            Unit unitSource = matDispatchItem.getUnitSource();
            Material materialTarget = matReceiveItem.getMaterialTarget();
            Unit unitTarget = matReceiveItem.getUnitTarget();

            matDispatchItem.setDispatchMaterialId(getLong(FRM_FIELD_DISPATCH_MATERIAL_ID));
            matDispatchItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_SOURCE_ID));
            matDispatchItem.setUnitId(getLong(FRM_FIELD_UNIT_SOURCE_ID));
            matDispatchItem.setQty(getDouble(FRM_FIELD_QTY_SOURCE));
            matDispatchItem.setHpp(getDouble(FRM_FIELD_HPP_SOURCE));
            matDispatchItem.setHppTotal(getDouble(FRM_FIELD_HPP_TOTAL_SOURCE));
            //for oidReceiveMaterial
            matReceiveItem.setReceiveMaterialId(getLong(FRM_FIELD_RECEIVE_MATERIAL_ID));
            matReceiveItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_TARGET_ID));
            matReceiveItem.setUnitId(getLong(FRM_FIELD_UNIT_TARGET_ID));
            matReceiveItem.setQty(getDouble(FRM_FIELD_QTY_TARGET));


            matReceiveItem.setCost(getDouble(FRM_FIELD_COST_TARGET));
            matReceiveItem.setTotal(getDouble(FRM_FIELD_TOTAL_TARGET));
            materialSource.setSku(getString(FRM_FIELD_SKU_SOURCE));
            materialSource.setName(getString(FRM_FIELD_NAME_SOURCE));
            materialTarget.setSku(getString(FRM_FIELD_SKU_TARGET));
            materialTarget.setName(getString(FRM_FIELD_NAME_TARGET));
            unitSource.setCode(getString(FRM_FIELD_CODE_SOURCE));
            unitTarget.setCode(getString(FRM_FIELD_CODE_TARGET));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
