/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.ProductionGroup;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmProductionGroup extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ProductionGroup entProductionGroup;
    public static final String FRM_NAME_PRODUCTION_GROUP = "FRM_NAME_PRODUCTION_GROUP";
    public static final int FRM_FIELD_PRODUCTION_GROUP_ID = 0;
    public static final int FRM_FIELD_PRODUCTION_ID = 1;
    public static final int FRM_FIELD_BATCH_NUMBER = 2;
    public static final int FRM_FIELD_CHAIN_PERIOD_ID = 3;
    public static final int FRM_FIELD_DATE_START = 4;
    public static final int FRM_FIELD_DATE_END = 5;
    public static final int FRM_FIELD_PRODUCTION_GROUP_PARENT_ID = 6;
    public static final int FRM_FIELD_INDEX = 7;

    public static String[] fieldNames = {
        "FRM_FIELD_PRODUCTION_GROUP_ID",
        "FRM_FIELD_PRODUCTION_ID",
        "FRM_FIELD_BATCH_NUMBER",
        "FRM_FIELD_CHAIN_PERIOD_ID",
        "FRM_FIELD_DATE_START",
        "FRM_FIELD_DATE_END",
        "FRM_FIELD_PRODUCTION_GROUP_PARENT_ID",
        "INDEX"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT
    };

    public FrmProductionGroup() {
    }

    public FrmProductionGroup(ProductionGroup entProductionGroup) {
        this.entProductionGroup = entProductionGroup;
    }

    public FrmProductionGroup(HttpServletRequest request, ProductionGroup entProductionGroup) {
        super(new FrmProductionGroup(entProductionGroup), request);
        this.entProductionGroup = entProductionGroup;
    }

    public String getFormName() {
        return FRM_NAME_PRODUCTION_GROUP;
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

    public ProductionGroup getEntityObject() {
        return entProductionGroup;
    }

    public void requestEntityObject(ProductionGroup entProductionGroup) {
        try {
            this.requestParam();
            entProductionGroup.setOID(getLong(FRM_FIELD_PRODUCTION_GROUP_ID));
            entProductionGroup.setProductionId(getLong(FRM_FIELD_PRODUCTION_ID));
            entProductionGroup.setBatchNumber(getString(FRM_FIELD_BATCH_NUMBER));
            entProductionGroup.setChainPeriodId(getLong(FRM_FIELD_CHAIN_PERIOD_ID));
            entProductionGroup.setDateStart(Formater.formatDate(getString(FRM_FIELD_DATE_START), "yyyy-MM-dd HH:mm:ss"));
            entProductionGroup.setDateEnd(Formater.formatDate(getString(FRM_FIELD_DATE_END), "yyyy-MM-dd HH:mm:ss"));
            entProductionGroup.setProductionGroupParentId(getLong(FRM_FIELD_PRODUCTION_GROUP_PARENT_ID));
            entProductionGroup.setIndex(getInt(FRM_FIELD_INDEX));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
