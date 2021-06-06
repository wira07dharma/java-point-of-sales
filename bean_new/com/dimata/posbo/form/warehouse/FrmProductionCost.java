/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.ProductionCost;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmProductionCost extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ProductionCost entProductionCost;
    public static final String FRM_NAME_PRODUCTION_COST = "FRM_NAME_PRODUCTION_COST";
    public static final int FRM_FIELD_PRODUCTION_COST_ID = 0;
    public static final int FRM_FIELD_PRODUCTION_GROUP_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_STOCK_QTY = 3;
    public static final int FRM_FIELD_STOCK_VALUE = 4;
    public static final int FRM_FIELD_COST_TYPE = 5;
    public static final int FRM_FIELD_PRODUCT_DISTRIBUTION_ID = 6;

    public static String[] fieldNames = {
        "FRM_FIELD_PRODUCTION_COST_ID",
        "FRM_FIELD_PRODUCTION_GROUP_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_STOCK_QTY",
        "FRM_FIELD_STOCK_VALUE",
        "FRM_FIELD_COST_TYPE",
        "FRM_FIELD_PRODUCT_DISTRIBUTION_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG
    };

    public FrmProductionCost() {
    }

    public FrmProductionCost(ProductionCost entProductionCost) {
        this.entProductionCost = entProductionCost;
    }

    public FrmProductionCost(HttpServletRequest request, ProductionCost entProductionCost) {
        super(new FrmProductionCost(entProductionCost), request);
        this.entProductionCost = entProductionCost;
    }

    public String getFormName() {
        return FRM_NAME_PRODUCTION_COST;
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

    public ProductionCost getEntityObject() {
        return entProductionCost;
    }

    public void requestEntityObject(ProductionCost entProductionCost) {
        try {
            this.requestParam();
            entProductionCost.setOID(getLong(FRM_FIELD_PRODUCTION_COST_ID));
            entProductionCost.setProductionGroupId(getLong(FRM_FIELD_PRODUCTION_GROUP_ID));
            entProductionCost.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entProductionCost.setStockQty(getDouble(FRM_FIELD_STOCK_QTY));
            entProductionCost.setStockValue(getDouble(FRM_FIELD_STOCK_VALUE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
