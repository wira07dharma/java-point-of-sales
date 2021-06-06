/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.ChainMainMaterial;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author IanRizky
 */
public class FrmChainMainMaterial extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ChainMainMaterial entChainMainMaterial;
    public static final String FRM_NAME_CHAIN_MAIN_MATERIAL = "FRM_NAME_CHAIN_MAIN_MATERIAL";
    public static final int FRM_FIELD_CHAIN_MAIN_MATERIAL_ID = 0;
    public static final int FRM_FIELD_CHAIN_PERIOD_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_MATERIAL_TYPE = 3;
    public static final int FRM_FIELD_COST_PCT = 4;
    public static final int FRM_FIELD_COST_TYPE = 5;
    public static final int FRM_FIELD_STOCK_QTY = 6;
    public static final int FRM_FIELD_COST_VALUE = 7;
    public static final int FRM_FIELD_SALES_VALUE = 8;
    public static final int FRM_FIELD_PERIOD_DISTRIBUTION = 9;

    public static String[] fieldNames = {
        "FRM_FIELD_CHAIN_MAIN_MATERIAL_ID",
        "FRM_FIELD_CHAIN_PERIOD_ID",
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
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT
    };

    public FrmChainMainMaterial() {
    }

    public FrmChainMainMaterial(ChainMainMaterial entChainMainMaterial) {
        this.entChainMainMaterial = entChainMainMaterial;
    }

    public FrmChainMainMaterial(HttpServletRequest request, ChainMainMaterial entChainMainMaterial) {
        super(new FrmChainMainMaterial(entChainMainMaterial), request);
        this.entChainMainMaterial = entChainMainMaterial;
    }

    public String getFormName() {
        return FRM_NAME_CHAIN_MAIN_MATERIAL;
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

    public ChainMainMaterial getEntityObject() {
        return entChainMainMaterial;
    }

    public void requestEntityObject(ChainMainMaterial entChainMainMaterial) {
        try {
            this.requestParam();
            entChainMainMaterial.setChainPeriodId(getLong(FRM_FIELD_CHAIN_PERIOD_ID));
            entChainMainMaterial.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entChainMainMaterial.setMaterialType(getInt(FRM_FIELD_MATERIAL_TYPE));
            entChainMainMaterial.setCostPct(getDouble(FRM_FIELD_COST_PCT));
            entChainMainMaterial.setCostType(getInt(FRM_FIELD_COST_TYPE));
            entChainMainMaterial.setStockQty(getInt(FRM_FIELD_STOCK_QTY));
            entChainMainMaterial.setCostValue(getDouble(FRM_FIELD_COST_VALUE));
            entChainMainMaterial.setSalesValue(getDouble(FRM_FIELD_SALES_VALUE));
            entChainMainMaterial.setPeriodDistribution(getInt(FRM_FIELD_PERIOD_DISTRIBUTION));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
