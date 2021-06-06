/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.masterdata;

import com.dimata.sedana.entity.masterdata.TingkatanBunga;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmTingkatanBunga extends FRMHandler implements I_FRMInterface, I_FRMType {

    private TingkatanBunga entTingkatanBunga;
    public static final String FRM_NAME_TINGKATAN_BUNGA = "FRM_NAME_TINGKATAN_BUNGA";
    public static final int FRM_FIELD_ID_TINGKATAN_BUNGA = 0;
    public static final int FRM_FIELD_ID_JENIS_SIMPANAN = 1;
    public static final int FRM_FIELD_NOMINAL_SALDO_MIN = 2;
    public static final int FRM_FIELD_PERSENTASE_BUNGA = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_ID_TINGKATAN_BUNGA",
        "FRM_FIELD_ID_JENIS_SIMPANAN",
        "FRM_FIELD_NOMINAL_SALDO_MIN",
        "FRM_FIELD_PERSENTASE_BUNGA"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public FrmTingkatanBunga() {
    }

    public FrmTingkatanBunga(TingkatanBunga entTingkatanBunga) {
        this.entTingkatanBunga = entTingkatanBunga;
    }

    public FrmTingkatanBunga(HttpServletRequest request, TingkatanBunga entTingkatanBunga) {
        super(new FrmTingkatanBunga(entTingkatanBunga), request);
        this.entTingkatanBunga = entTingkatanBunga;
    }

    public String getFormName() {
        return FRM_NAME_TINGKATAN_BUNGA;
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

    public TingkatanBunga getEntityObject() {
        return entTingkatanBunga;
    }

    public void requestEntityObject(TingkatanBunga entTingkatanBunga) {
        try {
            this.requestParam();
//            entTingkatanBunga.setIdTingkatanBunga(getLong(FRM_FIELD_ID_TINGKATAN_BUNGA));
            entTingkatanBunga.setIdJenisSimpanan(getLong(FRM_FIELD_ID_JENIS_SIMPANAN));
            entTingkatanBunga.setNominalSaldoMin(getDouble(FRM_FIELD_NOMINAL_SALDO_MIN));
            entTingkatanBunga.setPersentaseBunga(getDouble(FRM_FIELD_PERSENTASE_BUNGA));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
