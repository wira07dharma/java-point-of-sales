/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.kredit;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.sedana.entity.kredit.Angsuran;
import com.dimata.util.Formater;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmAngsuran extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Angsuran entAngsuran;
    public static final String FRM_NAME_ANGSURAN = "FRM_NAME_ANGSURAN";
    public static final int FRM_FIELD_ID_ANGSURAN = 0;
    public static final int FRM_FIELD_JUMLAH_ANGSURAN = 1;
    public static final int FRM_FIELD_JADWAL_ANGSURAN_ID = 2;
    public static final int FRM_FIELD_TRANSAKSI_ID = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_ID_ANGSURAN",
        "FRM_FIELD_JUMLAH_ANGSURAN",
        "FRM_FIELD_JADWAL_ANGSURAN_ID",
        "TRANSAKSI_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG
    };

    public FrmAngsuran() {
    }

    public FrmAngsuran(Angsuran entAngsuran) {
        this.entAngsuran = entAngsuran;
    }

    public FrmAngsuran(HttpServletRequest request, Angsuran entAngsuran) {
        super(new FrmAngsuran(entAngsuran), request);
        this.entAngsuran = entAngsuran;
    }

    public String getFormName() {
        return FRM_NAME_ANGSURAN;
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

    public Angsuran getEntityObject() {
        return entAngsuran;
    }

    public void requestEntityObject(Angsuran entAngsuran) {
        try {
            this.requestParam();          
//            entAngsuran.setIdAngsuran(getLong(FRM_FIELD_ID_ANGSURAN));
            entAngsuran.setJumlahAngsuran(getDouble(FRM_FIELD_JUMLAH_ANGSURAN));
            entAngsuran.setJadwalAngsuranId(getLong(FRM_FIELD_JADWAL_ANGSURAN_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
