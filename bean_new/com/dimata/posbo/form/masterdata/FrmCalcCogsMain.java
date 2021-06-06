/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.CalcCogsMain;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmCalcCogsMain extends FRMHandler implements I_FRMInterface, I_FRMType {

    private CalcCogsMain entCalcCogsMain;
    public static final String FRM_NAME_CALC_COGS_MAIN = "FRM_NAME_CALC_COGS_MAIN";
    public static final int FRM_FIELD_CALC_COGS_MAIN_ID = 0;
    public static final int FRM_FIELD_COST_DATE_START = 1;
    public static final int FRM_FIELD_COST_DATE_END = 2;
    public static final int FRM_FIELD_SALES_DATE_START = 3;
    public static final int FRM_FIELD_SALES_DATE_END = 4;
    public static final int FRM_FIELD_STATUS = 5;
    public static final int FRM_FIELD_NOTE = 6;

    public static String[] fieldNames = {
        "FRM_FIELD_CALC_COGS_MAIN_ID",
        "FRM_FIELD_COST_DATE_START",
        "FRM_FIELD_COST_DATE_END",
        "FRM_FIELD_SALES_DATE_START",
        "FRM_FIELD_SALES_DATE_END",
        "FRM_FIELD_STATUS",
        "FRM_FIELD_NOTE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING
    };

    public FrmCalcCogsMain() {
    }

    public FrmCalcCogsMain(CalcCogsMain entCalcCogsMain) {
        this.entCalcCogsMain = entCalcCogsMain;
    }

    public FrmCalcCogsMain(HttpServletRequest request, CalcCogsMain entCalcCogsMain) {
        super(new FrmCalcCogsMain(entCalcCogsMain), request);
        this.entCalcCogsMain = entCalcCogsMain;
    }

    public String getFormName() {
        return FRM_NAME_CALC_COGS_MAIN;
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

    public CalcCogsMain getEntityObject() {
        return entCalcCogsMain;
    }

    public void requestEntityObject(CalcCogsMain entCalcCogsMain) {
        try {
            this.requestParam();
            String costStart = getString(FRM_FIELD_COST_DATE_START);
            String costEnd = getString(FRM_FIELD_COST_DATE_END);
            String salesStart = getString(FRM_FIELD_SALES_DATE_START);
            String salesEnd = getString(FRM_FIELD_SALES_DATE_END);
            
            Date dateCostStart = Formater.formatDate(costStart, "yyyy-MM-dd");
            Date dateCostEnd = Formater.formatDate(costEnd, "yyyy-MM-dd");
            Date dateSalesStart = Formater.formatDate(salesStart, "yyyy-MM-dd");
            Date dateSalesEnd = Formater.formatDate(salesEnd, "yyyy-MM-dd");
            
//            entCalcCogsMain.setCalcCogsMainId(getLong(FRM_FIELD_CALC_COGS_MAIN_ID));
            entCalcCogsMain.setCostDateStart(dateCostStart);
            entCalcCogsMain.setCostDateEnd(dateCostEnd);
            entCalcCogsMain.setSalesDateStart(dateSalesStart);
            entCalcCogsMain.setSalesDateEnd(dateSalesEnd);
            entCalcCogsMain.setStatus(getInt(FRM_FIELD_STATUS));
            entCalcCogsMain.setNote(getString(FRM_FIELD_NOTE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
