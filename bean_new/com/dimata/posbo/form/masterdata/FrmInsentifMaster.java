/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.InsentifMaster;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmInsentifMaster extends FRMHandler implements I_FRMInterface, I_FRMType {

    private InsentifMaster entInsentifMaster;
    public static final String FRM_NAME_INSENTIF_MASTER = "FRM_NAME_INSENTIF_MASTER";
    public static final int FRM_FIELD_INSENTIF_MASTER_ID = 0;
    public static final int FRM_FIELD_TITLE = 1;
    public static final int FRM_FIELD_PERIODE_START = 2;
    public static final int FRM_FIELD_PERIODE_END = 3;
    public static final int FRM_FIELD_PERIODE_FOREVER = 4;
    public static final int FRM_FIELD_MATERIAL_MAIN = 5;
    public static final int FRM_FIELD_INCLUDE_SALES_PROFIT = 6;
    public static final int FRM_FIELD_INCLUDE_COST_OF_SALES = 7;
    public static final int FRM_FIELD_DIVISION_POINT = 8;
    public static final int FRM_FIELD_FAKTOR_NOMINAL_INSENTIF = 9;
    public static final int FRM_FIELD_DEPEND_ON_POSITION = 10;
    public static final int FRM_FIELD_STATUS = 11;
    public static final int FRM_FIELD_CATEGORY_ID = 12;

    public static String[] fieldNames = {
        "FRM_FIELD_INSENTIF_MASTER_ID",
        "FRM_FIELD_TITLE",
        "FRM_FIELD_PERIODE_START",
        "FRM_FIELD_PERIODE_END",
        "FRM_FIELD_PERIODE_FOREVER",
        "FRM_FIELD_MATERIAL_MAIN",
        "FRM_FIELD_INCLUDE_SALES_PROFIT",
        "FRM_FIELD_INCLUDE_COST_OF_SALES",
        "FRM_FIELD_DIVISION_POINT",
        "FRM_FIELD_FAKTOR_NOMINAL_INSENTIF",
        "FRM_FIELD_DEPEND_ON_POSITION",
        "FRM_FIELD_STATUS",
        "FRM_FIELD_CATEGORY_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG
    };

    public FrmInsentifMaster() {
    }

    public FrmInsentifMaster(InsentifMaster entInsentifMaster) {
        this.entInsentifMaster = entInsentifMaster;
    }

    public FrmInsentifMaster(HttpServletRequest request, InsentifMaster entInsentifMaster) {
        super(new FrmInsentifMaster(entInsentifMaster), request);
        this.entInsentifMaster = entInsentifMaster;
    }

    public String getFormName() {
        return FRM_NAME_INSENTIF_MASTER;
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

    public InsentifMaster getEntityObject() {
        return entInsentifMaster;
    }

    public void requestEntityObject(InsentifMaster entInsentifMaster) {
        try {
            this.requestParam();
//            entInsentifMaster.setInsentifMasterId(getLong(FRM_FIELD_INSENTIF_MASTER_ID));
            entInsentifMaster.setTitle(getString(FRM_FIELD_TITLE));
            entInsentifMaster.setPeriodeStart(Formater.formatDate(getString(FRM_FIELD_PERIODE_START), "yyyy-MM-dd"));
            entInsentifMaster.setPeriodeEnd(Formater.formatDate(getString(FRM_FIELD_PERIODE_END), "yyyy-MM-dd"));
            entInsentifMaster.setPeriodeForever(getInt(FRM_FIELD_PERIODE_FOREVER));
            entInsentifMaster.setMaterialMain(getInt(FRM_FIELD_MATERIAL_MAIN));
            entInsentifMaster.setIncludeSalesProfit(getInt(FRM_FIELD_INCLUDE_SALES_PROFIT));
            entInsentifMaster.setIncludeCostOfSales(getInt(FRM_FIELD_INCLUDE_COST_OF_SALES));
            entInsentifMaster.setDivisionPoint(getDouble(FRM_FIELD_DIVISION_POINT));
            entInsentifMaster.setFaktorNominalInsentif(getDouble(FRM_FIELD_FAKTOR_NOMINAL_INSENTIF));
            entInsentifMaster.setDependOnPosition(getInt(FRM_FIELD_DEPEND_ON_POSITION));
            entInsentifMaster.setStatus(getInt(FRM_FIELD_STATUS));
            entInsentifMaster.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
