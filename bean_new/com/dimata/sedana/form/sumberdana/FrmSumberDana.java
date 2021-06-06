/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.sumberdana;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.sedana.entity.sumberdana.SumberDana;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

public class FrmSumberDana extends FRMHandler implements I_FRMInterface, I_FRMType {

    private SumberDana entSumberDana;
    public static final String FRM_NAME_SUMBERDANA = "FRM_NAME_SUMBERDANA";
    public static final int FRM_FIELD_SUMBER_DANA_ID = 0;
    public static final int FRM_FIELD_CONTACT_ID = 1;
    public static final int FRM_FIELD_JENIS_SUMBER_DANA = 2;
    public static final int FRM_FIELD_KODE_SUMBER_DANA = 3;
    public static final int FRM_FIELD_JUDUL_SUMBER_DANA = 4;
    public static final int FRM_FIELD_TARGET_PENDANAAN = 5;
    public static final int FRM_FIELD_PRIORITAS_PENGGUNAAN = 6;
    public static final int FRM_FIELD_TOTAL_KETERSEDIAAN_DANA = 7;
    public static final int FRM_FIELD_BIAYA_BUNGA_KE_KREDITUR = 8;
    public static final int FRM_FIELD_TIPE_BUNGA_KE_KREDITUR = 9;
    public static final int FRM_FIELD_TANGGAL_DANA_MASUK = 10;
    public static final int FRM_FIELD_TANGGAL_LUNAS_KE_KREDITUR = 11;
    public static final int FRM_FIELD_TANGGAL_DANA_MULAI_TERSEDIA = 12;
    public static final int FRM_FIELD_TANGGAL_AKHIR_TERSEDIA = 13;
    public static final int FRM_FIELD_MINIMUM_PINJAMAN_KE_DEBITUR = 14;
    public static final int FRM_FIELD_MAXIMUM_PINJAMAN_KE_DEBITUR = 15;

    public static String[] fieldNames = {
        "FRM_FIELD_SUMBER_DANA_ID",
        "FRM_FIELD_CONTACT_ID",
        "FRM_FIELD_JENIS_SUMBER_DANA",
        "FRM_FIELD_KODE_SUMBER_DANA",
        "FRM_FIELD_JUDUL_SUMBER_DANA",
        "FRM_FIELD_TARGET_PENDANAAN",
        "FRM_FIELD_PRIORITAS_PENGGUNAAN",
        "FRM_FIELD_TOTAL_KETERSEDIAAN_DANA",
        "FRM_FIELD_BIAYA_BUNGA_KE_KREDITUR",
        "FRM_FIELD_TIPE_BUNGA_KE_KREDITUR",
        "FRM_FIELD_TANGGAL_DANA_MASUK",
        "FRM_FIELD_TANGGAL_LUNAS_KE_KREDITUR",
        "FRM_FIELD_TANGGAL_DANA_MULAI_TERSEDIA",
        "FRM_FIELD_TANGGAL_AKHIR_TERSEDIA",
        "FRM_FIELD_MINIMUM_PINJAMAN_KE_DEBITUR",
        "FRM_FIELD_MAXIMUM_PINJAMAN_KE_DEBITUR"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public FrmSumberDana() {
    }

    public FrmSumberDana(SumberDana entSumberDana) {
        this.entSumberDana = entSumberDana;
    }

    public FrmSumberDana(HttpServletRequest request, SumberDana entSumberDana) {
        super(new FrmSumberDana(entSumberDana), request);
        this.entSumberDana = entSumberDana;
    }

    public String getFormName() {
        return FRM_NAME_SUMBERDANA;
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

    public SumberDana getEntityObject() {
        return entSumberDana;
    }

    public void requestEntityObject(SumberDana entSumberDana) {
        try {
            this.requestParam();
            entSumberDana.setContactId(getLong(FRM_FIELD_CONTACT_ID));
            entSumberDana.setJenisSumberDana(getInt(FRM_FIELD_JENIS_SUMBER_DANA));
            entSumberDana.setKodeSumberDana(getString(FRM_FIELD_KODE_SUMBER_DANA));
            entSumberDana.setJudulSumberDana(getString(FRM_FIELD_JUDUL_SUMBER_DANA));
            entSumberDana.setTargetPendanaan(getString(FRM_FIELD_TARGET_PENDANAAN));
            entSumberDana.setPrioritasPenggunaan(getInt(FRM_FIELD_PRIORITAS_PENGGUNAAN));
            entSumberDana.setTotalKetersediaanDana(getDouble(FRM_FIELD_TOTAL_KETERSEDIAAN_DANA));
            entSumberDana.setBiayaBungaKeKreditur(getFloat(FRM_FIELD_BIAYA_BUNGA_KE_KREDITUR));
            entSumberDana.setTipeBungaKeKreditur(getInt(FRM_FIELD_TIPE_BUNGA_KE_KREDITUR));
            
            //FORMAT DATE
            String strTanggalLunasKeKreditur = getString(FRM_FIELD_TANGGAL_LUNAS_KE_KREDITUR);
            String strTanggalDanaMulaiTersedia = getString(FRM_FIELD_TANGGAL_DANA_MULAI_TERSEDIA);
            String strTanggalDanaMasuk = getString(FRM_FIELD_TANGGAL_DANA_MASUK);
            String strTanggalAkhirTersedia = getString(FRM_FIELD_TANGGAL_AKHIR_TERSEDIA);
            
            if(strTanggalLunasKeKreditur.length() > 0){
                entSumberDana.setTanggalLunasKeKreditur(Formater.formatDate(strTanggalLunasKeKreditur, "yyyy-MM-dd"));
            }
            
            if(strTanggalDanaMulaiTersedia.length() > 0){
                entSumberDana.setTanggalDanaMulaiTersedia(Formater.formatDate(strTanggalDanaMulaiTersedia, "yyyy-MM-dd"));
            }
            
            if(strTanggalDanaMasuk.length() > 0){
                entSumberDana.setTanggalDanaMasuk(Formater.formatDate(strTanggalDanaMasuk, "yyyy-MM-dd"));
            }
            
            if(strTanggalAkhirTersedia.length() > 0){
                entSumberDana.setTanggalAkhirTersedia(Formater.formatDate(strTanggalAkhirTersedia, "yyyy-MM-dd"));
            }
            entSumberDana.setMinimumPinjamanKeDebitur(getDouble(FRM_FIELD_MINIMUM_PINJAMAN_KE_DEBITUR));
            entSumberDana.setMaximumPinjamanKeDebitur(getDouble(FRM_FIELD_MAXIMUM_PINJAMAN_KE_DEBITUR));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
