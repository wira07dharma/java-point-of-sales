/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.tabungan;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.sedana.entity.tabungan.JenisTransaksi;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmJenisTransaksi extends FRMHandler implements I_FRMInterface, I_FRMType {

    private JenisTransaksi entJenisTransaksi;
    public static final String FRM_NAME_JENIS_TRANSAKSI = "FRM_NAME_JENIS_TRANSAKSI";
    public static final int FRM_FIELD_JENIS_TRANSAKSI_ID = 0;
    public static final int FRM_FIELD_JENIS_TRANSAKSI = 1;
    public static final int FRM_FIELD_AFLIASI_ID = 2;
    public static final int FRM_FIELD_TIPE_ARUS_KAS = 3;
    public static final int FRM_FIELD_TYPE_PROSEDUR = 4;
    public static final int FRM_FIELD_PROSEDURE_UNTUK = 5;
    public static final int FRM_FIELD_PROSENTASE_PERHITUNGAN = 6;
    public static final int FRM_FIELD_TYPE_TRANSAKSI = 7;
    public static final int FRM_FIELD_VALUE_STANDAR_TRANSAKSI = 8;
    public static final int FRM_FIELD_STATUS_AKTIF = 9;
    public static final int FRM_FIELD_TIPE_DOC = 10;
    public static final int FRM_FIELD_INPUT_OPTION = 11;

    public static String[] fieldNames = {
        "FRM_FIELD_JENIS_TRANSAKSI_ID",
        "FRM_FIELD_JENIS_TRANSAKSI",
        "FRM_FIELD_AFLIASI_ID",
        "FRM_FIELD_TIPE_ARUS_KAS",
        "FRM_FIELD_TYPE_PROSEDUR",
        "FRM_FIELD_PROSEDURE_UNTUK",
        "FRM_FIELD_PROSENTASE_PERHITUNGAN",
        "FRM_FIELD_TYPE_TRANSAKSI",
        "VALUE_STANDAR_TRANSAKSI",
        "STATUS_AKTIF",
        "TIPE_DOC",
        "INPUT_OPTION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };

    public FrmJenisTransaksi() {
    }

    public FrmJenisTransaksi(JenisTransaksi entJenisTransaksi) {
        this.entJenisTransaksi = entJenisTransaksi;
    }

    public FrmJenisTransaksi(HttpServletRequest request, JenisTransaksi entJenisTransaksi) {
        super(new FrmJenisTransaksi(entJenisTransaksi), request);
        this.entJenisTransaksi = entJenisTransaksi;
    }

    public String getFormName() {
        return FRM_NAME_JENIS_TRANSAKSI;
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

    public JenisTransaksi getEntityObject() {
        return entJenisTransaksi;
    }

    public void requestEntityObject(JenisTransaksi entJenisTransaksi) {
        try {
            this.requestParam();
//            entJenisTransaksi.setJenisTransaksiId(getLong(FRM_FIELD_JENIS_TRANSAKSI_ID));
            entJenisTransaksi.setJenisTransaksi(getString(FRM_FIELD_JENIS_TRANSAKSI));
            entJenisTransaksi.setAfliasiId(getLong(FRM_FIELD_AFLIASI_ID));
            entJenisTransaksi.setTipeArusKas(getInt(FRM_FIELD_TIPE_ARUS_KAS));
            entJenisTransaksi.setTypeProsedur(getInt(FRM_FIELD_TYPE_PROSEDUR));
            entJenisTransaksi.setProsedureUntuk(getInt(FRM_FIELD_PROSEDURE_UNTUK));
            entJenisTransaksi.setProsentasePerhitungan(getDouble(FRM_FIELD_PROSENTASE_PERHITUNGAN));
            entJenisTransaksi.setTypeTransaksi(getInt(FRM_FIELD_TYPE_TRANSAKSI));
            entJenisTransaksi.setValueStandarTransaksi(getDouble(FRM_FIELD_VALUE_STANDAR_TRANSAKSI));
            entJenisTransaksi.setStatusAktif(getInt(FRM_FIELD_STATUS_AKTIF));
            entJenisTransaksi.setTipeDoc(getInt(FRM_FIELD_TIPE_DOC));
            entJenisTransaksi.setInputOption(getInt(FRM_FIELD_INPUT_OPTION));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
