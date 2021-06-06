/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author dimata005
 */
import com.dimata.posbo.entity.masterdata.EmasLantakan;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;
import com.dimata.util.Formater;

public class FrmEmasLantakan extends FRMHandler implements I_FRMInterface, I_FRMType {

    private EmasLantakan entEmasLantakan;
    public static final String FRM_NAME_EMASLANTAKAN = "FRM_NAME_EMASLANTAKAN";
    public static final int FRM_FIELD_EMAS_LANTAKAN_ID = 0;
    public static final int FRM_FIELD_HARGA_BELI = 1;
    public static final int FRM_FIELD_HARGA_JUAL = 2;
    public static final int FRM_FIELD_HARGA_TENGAH = 3;
    public static final int FRM_FIELD_START_DATE = 4;
    public static final int FRM_FIELD_END_DATE = 5;

    public static String[] fieldNames = {
        "FRM_FIELD_EMAS_LANTAKAN_ID",
        "FRM_FIELD_HARGA_BELI",
        "FRM_FIELD_HARGA_JUAL",
        "FRM_FIELD_HARGA_TENGAH",
        "FRM_FIELD_START_DATE",
        "FRM_FIELD_END_DATE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_STRING
    };
    
    public static String[] fieldQuestion = {
        "",
        "Isilah kolom ini dengan harga beli (format angka)",
        "Isilah kolom ini dengan harga jual (format angka)",
        "Isilah kolom ini dengan harga tengah (format angka)",
        "Isilah kolom ini dengan tanggal awal",
        "Isilah kolom ini dengan tanggal akhir"
    };
    
    public static String[] fieldError = {
        "",
        "Harga beli error (harus berupa angka)",
        "Harga jual error (harus berupa angka)",
        "Harga tengah error (harus berupa angka)",
        "Tangga awal error",
        "Tangga akhir error"
    };

    public FrmEmasLantakan() {
    }

    public FrmEmasLantakan(EmasLantakan entEmasLantakan) {
        this.entEmasLantakan = entEmasLantakan;
    }

    public FrmEmasLantakan(HttpServletRequest request, EmasLantakan entEmasLantakan) {
        super(new FrmEmasLantakan(entEmasLantakan), request);
        this.entEmasLantakan = entEmasLantakan;
    }

    public String getFormName() {
        return FRM_NAME_EMASLANTAKAN;
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

    public EmasLantakan getEntityObject() {
        return entEmasLantakan;
    }

    public void requestEntityObject(EmasLantakan entEmasLantakan) {
        try {
            this.requestParam();
            entEmasLantakan.setHargaBeli(getFloat(FRM_FIELD_HARGA_BELI));
            entEmasLantakan.setHargaJual(getFloat(FRM_FIELD_HARGA_JUAL));
            entEmasLantakan.setHargaTengah(getFloat(FRM_FIELD_HARGA_TENGAH));
            entEmasLantakan.setStartDate(Formater.formatDate(getString(FRM_FIELD_START_DATE), "yyyy-MM-dd"));
            entEmasLantakan.setEndDate(Formater.formatDate(getString(FRM_FIELD_END_DATE), "yyyy-MM-dd"));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
