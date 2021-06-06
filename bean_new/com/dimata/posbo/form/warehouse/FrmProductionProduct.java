/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.ProductionProduct;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmProductionProduct extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ProductionProduct entProductionProduct;
    public static final String FRM_NAME_PRODUCTION_PRODUCT = "FRM_NAME_PRODUCTION_PRODUCT";
    public static final int FRM_FIELD_PRODUCTION_PRODUCT_ID = 0;
    public static final int FRM_FIELD_PRODUCTION_GROUP_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_MATERIAL_TYPE = 3;
    public static final int FRM_FIELD_COST_PCT = 4;
    public static final int FRM_FIELD_COST_TYPE = 5;
    public static final int FRM_FIELD_STOCK_QTY = 6;
    public static final int FRM_FIELD_COST_VALUE = 7;
    public static final int FRM_FIELD_SALES_VALUE = 8;
    public static final int FRM_FIELD_PERIOD_DISTRIBUTION = 9;

    public static String[] fieldNames = {
        "FRM_FIELD_PRODUCTION_PRODUCT_ID",
        "FRM_FIELD_PRODUCTION_GROUP_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_MATERIAL_TYPE",
        "FRM_FIELD_COST_PCT",
        "FRM_FIELD_COST_TYPE",
        "FRM_FIELD_STOCK_QTY",
        "FRM_FIELD_COST_VALUE",
        "FRM_FIELD_SALES_VALUE",
        "FRM_FIELD_PERIOD_DISTRIBUTION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT
    };

    public FrmProductionProduct() {
    }

    public FrmProductionProduct(ProductionProduct entProductionProduct) {
        this.entProductionProduct = entProductionProduct;
    }

    public FrmProductionProduct(HttpServletRequest request, ProductionProduct entProductionProduct) {
        super(new FrmProductionProduct(entProductionProduct), request);
        this.entProductionProduct = entProductionProduct;
    }

    public String getFormName() {
        return FRM_NAME_PRODUCTION_PRODUCT;
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

    public ProductionProduct getEntityObject() {
        return entProductionProduct;
    }

    public void requestEntityObject(ProductionProduct entProductionProduct) {
        try {
            this.requestParam();
            entProductionProduct.setOID(getLong(FRM_FIELD_PRODUCTION_PRODUCT_ID));
            entProductionProduct.setProductionGroupId(getLong(FRM_FIELD_PRODUCTION_GROUP_ID));
            entProductionProduct.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entProductionProduct.setMaterialType(getInt(FRM_FIELD_MATERIAL_TYPE));
            entProductionProduct.setCostPct(getDouble(FRM_FIELD_COST_PCT));
            entProductionProduct.setCostType(getInt(FRM_FIELD_COST_TYPE));
            entProductionProduct.setStockQty(getDouble(FRM_FIELD_STOCK_QTY));
            entProductionProduct.setCostValue(getDouble(FRM_FIELD_COST_VALUE));
            entProductionProduct.setSalesValue(getDouble(FRM_FIELD_SALES_VALUE));
            entProductionProduct.setPeriodDistribution(getInt(FRM_FIELD_PERIOD_DISTRIBUTION));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
