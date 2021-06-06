/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.CalcCogsSales;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmCalcCogsSales extends FRMHandler implements I_FRMInterface, I_FRMType {

    private CalcCogsSales entCalcCogsSales;
    public static final String FRM_NAME_CALC_COGS_SALES = "FRM_NAME_CALC_COGS_SALES";
    public static final int FRM_FIELD_CALC_COGS_SALES_ID = 0;
    public static final int FRM_FIELD_CALC_COGS_MAIN_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_UNIT_ID = 3;
    public static final int FRM_FIELD_QTY_ITEM = 4;
    public static final int FRM_FIELD_STOCK_LEFT = 5;
    public static final int FRM_FIELD_SUB_TOTAL_SALES = 6;
    public static final int FRM_FIELD_STOK_AWAL = 7;

    public static String[] fieldNames = {
        "FRM_FIELD_CALC_COGS_SALES_ID",
        "FRM_FIELD_CALC_COGS_MAIN_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_QTY_ITEM",
        "FRM_FIELD_STOCK_LEFT",
        "FRM_FIELD_SUB_TOTAL_SALES",
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

    public FrmCalcCogsSales() {
    }

    public FrmCalcCogsSales(CalcCogsSales entCalcCogsSales) {
        this.entCalcCogsSales = entCalcCogsSales;
    }

    public FrmCalcCogsSales(HttpServletRequest request, CalcCogsSales entCalcCogsSales) {
        super(new FrmCalcCogsSales(entCalcCogsSales), request);
        this.entCalcCogsSales = entCalcCogsSales;
    }

    public String getFormName() {
        return FRM_NAME_CALC_COGS_SALES;
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

    public CalcCogsSales getEntityObject() {
        return entCalcCogsSales;
    }

    public void requestEntityObject(CalcCogsSales entCalcCogsSales) {
        try {
            this.requestParam();
//            entCalcCogsSales.setCalcCogsSalesId(getLong(FRM_FIELD_CALC_COGS_SALES_ID));
            entCalcCogsSales.setCalcCogsMainId(getLong(FRM_FIELD_CALC_COGS_MAIN_ID));
            entCalcCogsSales.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entCalcCogsSales.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            entCalcCogsSales.setQtyItem(getDouble(FRM_FIELD_QTY_ITEM));
            entCalcCogsSales.setStockLeft(getDouble(FRM_FIELD_STOCK_LEFT));
            entCalcCogsSales.setSubTotalSales(getDouble(FRM_FIELD_SUB_TOTAL_SALES));
            entCalcCogsSales.setStokAwal(getDouble(FRM_FIELD_STOK_AWAL));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
