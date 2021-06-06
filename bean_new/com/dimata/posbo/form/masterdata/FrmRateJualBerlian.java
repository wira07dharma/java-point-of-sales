/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author PC
 */
import com.dimata.posbo.entity.masterdata.RateJualBerlian;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

public class FrmRateJualBerlian extends FRMHandler implements I_FRMInterface, I_FRMType {

    private RateJualBerlian entRateJualBerlian;
    public static final String FRM_NAME_RATE_JUAL_BERLIAN = "FRM_NAME_RATE_JUAL_BERLIAN";
    public static final int FRM_FIELD_RATE_JUAL_BERLIAN_ID = 0;
    public static final int FRM_FIELD_CODE = 1;
    public static final int FRM_FIELD_NAME = 2;
    public static final int FRM_FIELD_DESCRIPTION = 3;
    public static final int FRM_FIELD_RATE = 4;
    public static final int FRM_FIELD_UPDATE_DATE = 5;

    public static String[] fieldNames = {
        "FRM_FIELD_RATE_JUAL_BERLIAN_ID",
        "FRM_FIELD_CODE",
        "FRM_FIELD_NAME",
        "FRM_FIELD_DESCRIPTION",
        "FRM_FIELD_RATE",
        "FRM_FIELD_UPDATE_DATE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_DATE
    };

    public static String[] fieldQuestion = {
        "",
        "Isilah kolom ini dengan kode rate jual (format huruf)",
        "Isilah kolom ini dengan nama rate jual (format huruf)",
        "Isilah kolom ini dengan keterangan (format huruf)",
        "Isilah kolom ini dengan nilai rate jual (format angka)",
        "Isilah kolom ini dengan data tanggal (format tahun-bulan-tanggal)"
    };    
    
    public static String[] fieldError = {
        "",
        "Kode rate jual salah (harus berupa huruf)",
        "Nama rate jual salah (harus berupa huruf)",
        "Keterangan salah (harus berupa huruf)",
        "Nilai rate jual salah (harus berupa angka)",
        "Tangga update salah"
    };
    
    public FrmRateJualBerlian() {
    }

    public FrmRateJualBerlian(RateJualBerlian entRateJualBerlian) {
        this.entRateJualBerlian = entRateJualBerlian;
    }

    public FrmRateJualBerlian(HttpServletRequest request, RateJualBerlian entRateJualBerlian) {
        super(new FrmRateJualBerlian(entRateJualBerlian), request);
        this.entRateJualBerlian = entRateJualBerlian;
    }

    public String getFormName() {
        return FRM_NAME_RATE_JUAL_BERLIAN;
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

    public RateJualBerlian getEntityObject() {
        return entRateJualBerlian;
    }

    public void requestEntityObject(RateJualBerlian entRateJualBerlian) {
        try {
            this.requestParam();
            entRateJualBerlian.setCode(getString(FRM_FIELD_CODE));
            entRateJualBerlian.setName(getString(FRM_FIELD_NAME));
            entRateJualBerlian.setDescription(getString(FRM_FIELD_DESCRIPTION));
            entRateJualBerlian.setRate(getFloat(FRM_FIELD_RATE));
            entRateJualBerlian.setUpdateDate(new Date());
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
