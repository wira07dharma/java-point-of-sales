/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.ChainAddCost;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author IanRizky
 */
public class FrmChainAddCost extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ChainAddCost entChainAddCost;
    public static final String FRM_NAME_CHAIN_ADD_COST = "FRM_NAME_CHAIN_ADD_COST";
    public static final int FRM_FIELD_CHAIN_ADD_COST_ID = 0;
    public static final int FRM_FIELD_CHAIN_PERIOD_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_STOCK_QTY = 3;
    public static final int FRM_FIELD_STOCK_VALUE = 4;

    public static String[] fieldNames = {
        "FRM_FIELD_CHAIN_ADD_COST_ID",
        "FRM_FIELD_CHAIN_PERIOD_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_STOCK_QTY",
        "FRM_FIELD_STOCK_VALUE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT
    };

    public FrmChainAddCost() {
    }

    public FrmChainAddCost(ChainAddCost entChainAddCost) {
        this.entChainAddCost = entChainAddCost;
    }

    public FrmChainAddCost(HttpServletRequest request, ChainAddCost entChainAddCost) {
        super(new FrmChainAddCost(entChainAddCost), request);
        this.entChainAddCost = entChainAddCost;
    }

    public String getFormName() {
        return FRM_NAME_CHAIN_ADD_COST;
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

    public ChainAddCost getEntityObject() {
        return entChainAddCost;
    }

    public void requestEntityObject(ChainAddCost entChainAddCost) {
        try {
            this.requestParam();
            entChainAddCost.setChainPeriodId(getLong(FRM_FIELD_CHAIN_PERIOD_ID));
            entChainAddCost.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entChainAddCost.setStockQty(getInt(FRM_FIELD_STOCK_QTY));
            entChainAddCost.setStockValue(getDouble(FRM_FIELD_STOCK_VALUE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
