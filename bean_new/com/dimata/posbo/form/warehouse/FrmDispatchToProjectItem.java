/**
 * User: wardana
 * Date: Mar 23, 2004
 * Time: 4:06:26 PM
 * Version: 1.0 
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.warehouse.DispatchToProjectItem;

import javax.servlet.http.HttpServletRequest;

public class FrmDispatchToProjectItem extends FRMHandler implements I_FRMInterface, I_FRMType {

    private DispatchToProjectItem objDspToProjectItem;

    public static final String FRM_NAME_DISPATCH_TO_PROJECT_ITEM = "DISPATCH_TO_PROJECT_ITEM";

    public static final int FRM_FLD_DISPATCH_MATERIAL_ITEM_ID = 0;
    public static final int FRM_FLD_DISPATCH_MATERIAL_ID = 1;
    public static final int FRM_FLD_MATERIAL_ID = 2;
    public static final int FRM_FLD_UNIT_ID = 3;
    public static final int FRM_FLD_QTY = 4;
    public static final int FRM_FLD_COST = 5;

    public static final String[] stFieldNames = {
        "FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID",
        "FRM_FIELD_DISPATCH_MATERIAL_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_QTY",
        "FRM_FIELD_COST"
    };

    public static final int[] iFieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public FrmDispatchToProjectItem() {
    }

    public FrmDispatchToProjectItem(DispatchToProjectItem objDspToProjectItem) {
        this.objDspToProjectItem = objDspToProjectItem;
    }

    public FrmDispatchToProjectItem(HttpServletRequest request, DispatchToProjectItem objDspToProjectItem) {
        super(new FrmDispatchToProjectItem(objDspToProjectItem), request);
        this.objDspToProjectItem = objDspToProjectItem;
    }

    public int getFieldSize() {
        return stFieldNames.length;
    }

    public String getFormName() {
        return FRM_NAME_DISPATCH_TO_PROJECT_ITEM;
    }

    public String[] getFieldNames() {
        return stFieldNames;
    }

    public int[] getFieldTypes() {
        return iFieldTypes;
    }

    public DispatchToProjectItem getEntityObject(){
        return objDspToProjectItem;
    }

    public void requestEntityObject(DispatchToProjectItem objDspToProjectItem){
        try{
            this.requestParam();
            objDspToProjectItem.setlDispatchMaterialId(getLong(FRM_FLD_DISPATCH_MATERIAL_ID));
            objDspToProjectItem.setlMaterialId(getLong(FRM_FLD_MATERIAL_ID));
            objDspToProjectItem.setlUnitId(getLong(FRM_FLD_UNIT_ID));
            objDspToProjectItem.setiQty(getDouble(FRM_FLD_QTY));
            objDspToProjectItem.setdCost(getDouble(FRM_FLD_COST));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
