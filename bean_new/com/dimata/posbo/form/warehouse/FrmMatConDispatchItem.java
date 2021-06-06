package com.dimata.posbo.form.warehouse;

import javax.servlet.http.*;

import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.warehouse.MatConDispatchItem;

public class FrmMatConDispatchItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatConDispatchItem matDispatchItem;
    
    public static final String FRM_NAME_MATDISPATCHITEM = "FRM_NAME_MATDISPATCHITEM";
    
    public static final int FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID = 0;
    public static final int FRM_FIELD_DISPATCH_MATERIAL_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_UNIT_ID = 3;
    public static final int FRM_FIELD_QTY = 4;
    public static final int FRM_FIELD_HPP = 5;
    public static final int FRM_FIELD_HPP_TOTAL = 6;
    
    public static String[] fieldNames = {
        "FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID", "FRM_FIELD_DISPATCH_MATERIAL_ID",
        "FRM_FIELD_MATERIAL_ID", "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_QTY", "FRM_FIELD_HPP",
        "FRM_FIELD_HPP_TOTAL"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED, TYPE_LONG,
        TYPE_FLOAT, TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public FrmMatConDispatchItem() {
    }
    
    public FrmMatConDispatchItem(MatConDispatchItem matDispatchItem) {
        this.matDispatchItem = matDispatchItem;
    }
    
    public FrmMatConDispatchItem(HttpServletRequest request, MatConDispatchItem matDispatchItem) {
        super(new FrmMatConDispatchItem(matDispatchItem), request);
        this.matDispatchItem = matDispatchItem;
    }
    
    public String getFormName() {
        return FRM_NAME_MATDISPATCHITEM;
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
    
    public MatConDispatchItem getEntityObject() {
        return matDispatchItem;
    }
    
    public void requestEntityObject(MatConDispatchItem matDispatchItem) {
        try {
            this.requestParam();
            matDispatchItem.setDispatchMaterialId(getLong(FRM_FIELD_DISPATCH_MATERIAL_ID));
            matDispatchItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            matDispatchItem.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            matDispatchItem.setQty(getDouble(FRM_FIELD_QTY));
            matDispatchItem.setHpp(getDouble(FRM_FIELD_HPP));
            matDispatchItem.setHppTotal(getDouble(FRM_FIELD_HPP_TOTAL));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
