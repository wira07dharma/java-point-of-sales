/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.PerhitunganPoin;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmPerhitunganPoin extends FRMHandler implements I_FRMInterface, I_FRMType {

    private PerhitunganPoin entPerhitunganPoin;
    public static final String FRM_NAME_PERHITUNGAN_POIN = "FRM_NAME_PERHITUNGAN_POIN";
    public static final int FRM_FIELD_PERHITUNGAN_POIN_ID = 0;
    public static final int FRM_FIELD_MATERIAL_JENIS_TYPE = 1;
    public static final int FRM_FIELD_SELL_VALUE = 2;
    public static final int FRM_FIELD_POIN_REWARD = 3;
    public static final int FRM_FIELD_UPDATE_DATE = 4;
    public static final int FRM_FIELD_STATUS_AKTIF = 5;

    public static String[] fieldNames = {
        "FRM_FIELD_PERHITUNGAN_POIN_ID",
        "FRM_FIELD_MATERIAL_JENIS_TYPE",
        "FRM_FIELD_SELL_VALUE",
        "FRM_FIELD_POIN_REWARD",
        "FRM_FIELD_UPDATE_DATE",
        "FRM_FIELD_STATUS_AKTIF"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_INT
    };

    public FrmPerhitunganPoin() {
    }

    public FrmPerhitunganPoin(PerhitunganPoin entPerhitunganPoin) {
        this.entPerhitunganPoin = entPerhitunganPoin;
    }

    public FrmPerhitunganPoin(HttpServletRequest request, PerhitunganPoin entPerhitunganPoin) {
        super(new FrmPerhitunganPoin(entPerhitunganPoin), request);
        this.entPerhitunganPoin = entPerhitunganPoin;
    }

    public String getFormName() {
        return FRM_NAME_PERHITUNGAN_POIN;
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

    public PerhitunganPoin getEntityObject() {
        return entPerhitunganPoin;
    }

    public void requestEntityObject(PerhitunganPoin entPerhitunganPoin) {
        try {
            this.requestParam();
            entPerhitunganPoin.setMaterialJenisType(getInt(FRM_FIELD_MATERIAL_JENIS_TYPE));
            entPerhitunganPoin.setSellValue(getDouble(FRM_FIELD_SELL_VALUE));
            entPerhitunganPoin.setPoinReward(getInt(FRM_FIELD_POIN_REWARD));
            entPerhitunganPoin.setUpdateDate(getDate(FRM_FIELD_UPDATE_DATE));
            entPerhitunganPoin.setStatusAktif(getInt(FRM_FIELD_STATUS_AKTIF));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
