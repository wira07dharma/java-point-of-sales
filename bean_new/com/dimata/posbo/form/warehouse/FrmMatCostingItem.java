package com.dimata.posbo.form.warehouse;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;

import com.dimata.posbo.entity.warehouse.MatCostingItem;

import javax.servlet.http.HttpServletRequest;

public class FrmMatCostingItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    private MatCostingItem matCostingItem;
    
    public static final String FRM_NAME_MATCOSTINGITEM = "FRM_NAME_MATDISPATCHITEM";
    
    public static final int FRM_FIELD_COSTING_MATERIAL_ITEM_ID = 0;
    public static final int FRM_FIELD_COSTING_MATERIAL_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_UNIT_ID = 3;
    public static final int FRM_FIELD_QTY = 4;
    public static final int FRM_FIELD_HPP = 5;
    public static final int FRM_FIELD_RESIDUE_QTY = 6;
    public static final int FRM_FIELD_BALANCE_QTY = 7;
    public static final int FRM_FIELD_WEIGHT = 8;
    public static final int FRM_FIELD_COST = 9;
    
    public static String[] fieldNames = {
        "FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID", "FRM_FIELD_DISPATCH_MATERIAL_ID",
        "FRM_FIELD_MATERIAL_ID", "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_QTY", "FRM_FIELD_HPP", "FRM_RESIDUE_QTY","FRM_BALANCE_QTY",
        "FRM_FIELD_WEIGHT", "FRM_FIELD_COST"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED, TYPE_LONG,
        TYPE_FLOAT, TYPE_FLOAT, TYPE_FLOAT, TYPE_FLOAT,
        TYPE_FLOAT, TYPE_FLOAT
    };
    
    public FrmMatCostingItem() {
    }
    
    public FrmMatCostingItem(MatCostingItem matCostingItem) {
        this.matCostingItem = matCostingItem;
    }
    
    public FrmMatCostingItem(HttpServletRequest request, MatCostingItem matCostingItem) {
        super(new FrmMatCostingItem(matCostingItem), request);
        this.matCostingItem = matCostingItem;
    }
    
    public String getFormName() {
        return FRM_NAME_MATCOSTINGITEM;
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
    
    public MatCostingItem getEntityObject() {
        return matCostingItem;
    }
    
    public void requestEntityObject(MatCostingItem matCostingItem) {
        try {
            this.requestParam();
            matCostingItem.setCostingMaterialId(getLong(FRM_FIELD_COSTING_MATERIAL_ID));
            matCostingItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            matCostingItem.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            matCostingItem.setQty(getDouble(FRM_FIELD_QTY));
            matCostingItem.setHpp(getDouble(FRM_FIELD_HPP));
            //adding stock balance and stok fisik
            matCostingItem.setResidueQty(getDouble(FRM_FIELD_RESIDUE_QTY));
            matCostingItem.setBalanceQty(getDouble(FRM_FIELD_BALANCE_QTY));
            matCostingItem.setWeight(getDouble(FRM_FIELD_WEIGHT));
            matCostingItem.setCost(getDouble(FRM_FIELD_COST));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
