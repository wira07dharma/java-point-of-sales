/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.CalcCogsCost;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmCalcCogsCost extends FRMHandler implements I_FRMInterface, I_FRMType {

    private CalcCogsCost entCalcCogsCost;
    public static final String FRM_NAME_CALC_COGS_COST = "FRM_NAME_CALC_COGS_COST";
    public static final int FRM_FIELD_CALC_COGS_COST_ID = 0;
    public static final int FRM_FIELD_CALC_COGS_MAIN_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_UNIT_ID = 3;
    public static final int FRM_FIELD_QTY_ITEM = 4;
    public static final int FRM_FIELD_STOCK_LEFT = 5;
    public static final int FRM_FIELD_SUB_TOTAL_COST = 6;
    public static final int FRM_FIELD_STOK_AWAL = 6;

    public static String[] fieldNames = {
        "FRM_FIELD_CALC_COGS_COST_ID",
        "FRM_FIELD_CALC_COGS_MAIN_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_QTY_ITEM",
        "FRM_FIELD_STOCK_LEFT",
        "FRM_FIELD_SUB_TOTAL_COST",
        "FRM_FIELD_STOK_AWAL"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public FrmCalcCogsCost() {
    }

    public FrmCalcCogsCost(CalcCogsCost entCalcCogsCost) {
        this.entCalcCogsCost = entCalcCogsCost;
    }

    public FrmCalcCogsCost(HttpServletRequest request, CalcCogsCost entCalcCogsCost) {
        super(new FrmCalcCogsCost(entCalcCogsCost), request);
        this.entCalcCogsCost = entCalcCogsCost;
    }

    public String getFormName() {
        return FRM_NAME_CALC_COGS_COST;
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

    public CalcCogsCost getEntityObject() {
        return entCalcCogsCost;
    }

    public void requestEntityObject(CalcCogsCost entCalcCogsCost) {
        try {
            this.requestParam();
//            entCalcCogsCost.setCalcCogsCostId(getLong(FRM_FIELD_CALC_COGS_COST_ID));
            entCalcCogsCost.setCalcCogsMainId(getLong(FRM_FIELD_CALC_COGS_MAIN_ID));
            entCalcCogsCost.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entCalcCogsCost.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            entCalcCogsCost.setQtyItem(getDouble(FRM_FIELD_QTY_ITEM));
            entCalcCogsCost.setStockLeft(getDouble(FRM_FIELD_STOCK_LEFT));
            entCalcCogsCost.setSubTotalCost(getDouble(FRM_FIELD_SUB_TOTAL_COST));
            entCalcCogsCost.setStokAwal(getDouble(FRM_FIELD_STOK_AWAL));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
