/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.Production;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmProduction extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Production entProduction;
    public static final String FRM_NAME_PRODUCTION = "FRM_NAME_PRODUCTION";
    public static final int FRM_FIELD_PRODUCTION_ID = 0;
    public static final int FRM_FIELD_PRODUCTION_NUMBER = 1;
    public static final int FRM_FIELD_PRODUCTION_DATE = 2;
    public static final int FRM_FIELD_PRODUCTION_STATUS = 3;
    public static final int FRM_FIELD_LOCATION_FROM_ID = 4;
    public static final int FRM_FIELD_LOCATION_TO_ID = 5;
    public static final int FRM_FIELD_REMARK = 6;

    public static String[] fieldNames = {
        "FRM_FIELD_PRODUCTION_ID",
        "FRM_FIELD_PRODUCTION_NUMBER",
        "FRM_FIELD_PRODUCTION_DATE",
        "FRM_FIELD_PRODUCTION_STATUS",
        "FRM_FIELD_LOCATION_FROM_ID",
        "FRM_FIELD_LOCATION_TO_ID",
        "FRM_FIELD_REMARK"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING
    };

    public FrmProduction() {
    }

    public FrmProduction(Production entProduction) {
        this.entProduction = entProduction;
    }

    public FrmProduction(HttpServletRequest request, Production entProduction) {
        super(new FrmProduction(entProduction), request);
        this.entProduction = entProduction;
    }

    public String getFormName() {
        return FRM_NAME_PRODUCTION;
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

    public Production getEntityObject() {
        return entProduction;
    }

    public void requestEntityObject(Production entProduction) {
        try {
            this.requestParam();
            entProduction.setOID(getLong(FRM_FIELD_PRODUCTION_ID));
            entProduction.setProductionNumber(getString(FRM_FIELD_PRODUCTION_NUMBER));
            entProduction.setProductionDate(Formater.formatDate(getString(FRM_FIELD_PRODUCTION_DATE), "yyyy-MM-dd HH:mm:ss"));
            entProduction.setProductionStatus(getInt(FRM_FIELD_PRODUCTION_STATUS));
            entProduction.setLocationFromId(getLong(FRM_FIELD_LOCATION_FROM_ID));
            entProduction.setLocationToId(getLong(FRM_FIELD_LOCATION_TO_ID));
            entProduction.setRemark(getString(FRM_FIELD_REMARK));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
