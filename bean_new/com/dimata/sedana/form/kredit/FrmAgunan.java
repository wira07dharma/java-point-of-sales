/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.kredit;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.sedana.entity.kredit.Agunan;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmAgunan extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Agunan entAgunan;
    public static final String FRM_NAME_AGUNAN = "FRM_NAME_AGUNAN";
    public static final int FRM_FIELD_AGUNAN_ID = 0;
    public static final int FRM_FIELD_KODE_JENIS_AGUNAN = 1;
    public static final int FRM_FIELD_CONTACT_ID = 2;
    public static final int FRM_FIELD_KODE_KAB_LOKASI_AGUNAN = 3;
    public static final int FRM_FIELD_ALAMAT_AGUNAN = 4;
    public static final int FRM_FIELD_NILAI_AGUNAN_NJOP = 5;
    public static final int FRM_FIELD_BUKTI_KEPEMILIKAN = 6;
    public static final int FRM_FIELD_TIPE_AGUNAN = 7;
    public static final int FRM_FIELD_NOTE_TIPE_AGUNAN = 8;
    public static final int FRM_FIELD_NILAI_EKONOMIS = 9;
    public static final int FRM_FIELD_NILAI_JAMINAN_AGUNAN =  10;

    public static String[] fieldNames = {
        "FRM_FIELD_AGUNAN_ID",
        "FRM_FIELD_KODE_JENIS_AGUNAN",
        "FRM_FIELD_CONTACT_ID",
        "FRM_FIELD_KODE_KAB_LOKASI_AGUNAN",
        "FRM_FIELD_ALAMAT_AGUNAN",
        "FRM_FIELD_NILAI_AGUNAN_NJOP",
        "FRM_FIELD_BUKTI_KEPEMILIKAN",
        "FRM_FIELD_TIPE_AGUNAN",
        "FRM_FIELD_NOTE",
        "FRM_FIELD_NILAI_EKONOMIS",
        "FRM_FIELD_NILAI_JAMINAN_AGUNAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public FrmAgunan() {
    }

    public FrmAgunan(Agunan entAgunan) {
        this.entAgunan = entAgunan;
    }

    public FrmAgunan(HttpServletRequest request, Agunan entAgunan) {
        super(new FrmAgunan(entAgunan), request);
        this.entAgunan = entAgunan;
    }

    public String getFormName() {
        return FRM_NAME_AGUNAN;
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

    public Agunan getEntityObject() {
        return entAgunan;
    }

    public void requestEntityObject(Agunan entAgunan) {
        try {
            this.requestParam();
//            entAgunan.setAgunanId(getLong(FRM_FIELD_AGUNAN_ID)); 
            entAgunan.setKodeJenisAgunan(entAgunan.getKodeJenisAgunan());
            entAgunan.setContactId(getLong(FRM_FIELD_CONTACT_ID));
            entAgunan.setKodeKabLokasiAgunan(getLong(FRM_FIELD_KODE_KAB_LOKASI_AGUNAN));
            entAgunan.setAlamatAgunan(getString(FRM_FIELD_ALAMAT_AGUNAN));
            entAgunan.setNilaiAgunanNjop(getDouble(FRM_FIELD_NILAI_AGUNAN_NJOP));
            entAgunan.setBuktiKepemilikan(getString(FRM_FIELD_BUKTI_KEPEMILIKAN));
            entAgunan.setTipeAgunan(getInt(FRM_FIELD_TIPE_AGUNAN));
            entAgunan.setNoteTipeAgunan(getString(FRM_FIELD_NOTE_TIPE_AGUNAN));
            entAgunan.setNilaiEkonomis(getDouble(FRM_FIELD_NILAI_EKONOMIS));
            entAgunan.setNilaiJaminanAgunan(getDouble(FRM_FIELD_NILAI_JAMINAN_AGUNAN));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString()); 
        }
    }

}
