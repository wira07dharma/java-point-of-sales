/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.anggota;

import com.dimata.sedana.entity.anggota.PengurusKelompok;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmPengurusKelompok extends FRMHandler implements I_FRMInterface, I_FRMType {

    private PengurusKelompok entPengurusKelompok;
    public static final String FRM_NAME_PENGURUS_KELOMPOK = "FRM_NAME_PENGURUS_KELOMPOK";
    public static final int FRM_FIELD_ID_PENGURUS = 0;
    public static final int FRM_FIELD_NAMA_PENGURUS = 1;
    public static final int FRM_FIELD_JENIS_KELAMIN = 2;
    public static final int FRM_FIELD_STATUS_KEPEMILIKAN = 3;
    public static final int FRM_FIELD_JABATAN = 4;
    public static final int FRM_FIELD_TELEPON = 5;
    public static final int FRM_FIELD_EMAIL = 6;
    public static final int FRM_FIELD_ALAMAT = 7;
    public static final int FRM_FIELD_PROSENTASE_KEPENGURUSAN = 8;
    public static final int FRM_FIELD_ID_KELOMPOK = 9;

    public static String[] fieldNames = {
        "FRM_FIELD_ID_PENGURUS",
        "FRM_FIELD_NAMA_PENGURUS",
        "FRM_FIELD_JENIS_KELAMIN",
        "FRM_FIELD_STATUS_KEPEMILIKAN",
        "FRM_FIELD_JABATAN",
        "FRM_FIELD_TELEPON",
        "FRM_FIELD_EMAIL",
        "FRM_FIELD_ALAMAT",
        "FRM_FIELD_PROSENTASE_KEPENGURUSAN",
        "FRM_ID_KELOMPOK"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_LONG
    };

    public FrmPengurusKelompok() {
    }

    public FrmPengurusKelompok(PengurusKelompok entPengurusKelompok) {
        this.entPengurusKelompok = entPengurusKelompok;
    }

    public FrmPengurusKelompok(HttpServletRequest request, PengurusKelompok entPengurusKelompok) {
        super(new FrmPengurusKelompok(entPengurusKelompok), request);
        this.entPengurusKelompok = entPengurusKelompok;
    }

    public String getFormName() {
        return FRM_NAME_PENGURUS_KELOMPOK;
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

    public PengurusKelompok getEntityObject() {
        return entPengurusKelompok;
    }

    public void requestEntityObject(PengurusKelompok entPengurusKelompok) {
        try {
            this.requestParam();
//            entPengurusKelompok.setIdPengurus(getLong(FRM_FIELD_ID_PENGURUS));
            entPengurusKelompok.setNamaPengurus(getString(FRM_FIELD_NAMA_PENGURUS));
            entPengurusKelompok.setJenisKelamin(getInt(FRM_FIELD_JENIS_KELAMIN));
            entPengurusKelompok.setStatusKepemilikan(getInt(FRM_FIELD_STATUS_KEPEMILIKAN));
            entPengurusKelompok.setJabatan(getLong(FRM_FIELD_JABATAN));
            entPengurusKelompok.setTelepon(getString(FRM_FIELD_TELEPON));
            entPengurusKelompok.setEmail(getString(FRM_FIELD_EMAIL));
            entPengurusKelompok.setAlamat(getString(FRM_FIELD_ALAMAT));
            entPengurusKelompok.setProsentaseKepengurusan(getDouble(FRM_FIELD_PROSENTASE_KEPENGURUSAN));
            entPengurusKelompok.setIdKelompok(getLong(FRM_FIELD_ID_KELOMPOK));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
