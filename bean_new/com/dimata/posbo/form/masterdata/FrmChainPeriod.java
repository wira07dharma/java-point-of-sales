/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.ChainPeriod;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author IanRizky
 */
public class FrmChainPeriod extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ChainPeriod entChainPeriod;
    public static final String FRM_NAME_CHAIN_PERIOD = "FRM_NAME_CHAIN_PERIOD";
    public static final int FRM_FIELD_CHAIN_PERIOD_ID = 0;
    public static final int FRM_FIELD_CHAIN_MAIN_ID = 1;
    public static final int FRM_FIELD_TITLE = 2;
    public static final int FRM_FIELD_INDEX = 3;
    public static final int FRM_FIELD_DURATION = 4;

    public static String[] fieldNames = {
        "FRM_FIELD_CHAIN_PERIOD_ID",
        "FRM_FIELD_CHAIN_MAIN_ID",
        "FRM_FIELD_TITLE",
        "FRM_FIELD_INDEX",
        "FRM_FIELD_DURATION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG
    };

    public FrmChainPeriod() {
    }

    public FrmChainPeriod(ChainPeriod entChainPeriod) {
        this.entChainPeriod = entChainPeriod;
    }

    public FrmChainPeriod(HttpServletRequest request, ChainPeriod entChainPeriod) {
        super(new FrmChainPeriod(entChainPeriod), request);
        this.entChainPeriod = entChainPeriod;
    }

    public String getFormName() {
        return FRM_NAME_CHAIN_PERIOD;
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

    public ChainPeriod getEntityObject() {
        return entChainPeriod;
    }

    public void requestEntityObject(ChainPeriod entChainPeriod) {
        try {
            this.requestParam();
            entChainPeriod.setChainMainId(getLong(FRM_FIELD_CHAIN_MAIN_ID));
            entChainPeriod.setTitle(getString(FRM_FIELD_TITLE));
            entChainPeriod.setIndex(getInt(FRM_FIELD_INDEX));
            entChainPeriod.setDuration(getLong(FRM_FIELD_DURATION));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
