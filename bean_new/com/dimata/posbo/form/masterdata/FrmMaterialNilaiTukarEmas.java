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
import com.dimata.posbo.entity.masterdata.MaterialNilaiTukarEmas;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmMaterialNilaiTukarEmas extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MaterialNilaiTukarEmas entMaterialNilaiTukarEmas;
    public static final String FRM_NAME_MATERIALNILAITUKAREMAS = "FRM_NAME_MATERIALNILAITUKAREMAS";
    public static final int FRM_FIELD_NILAI_TUKAR_EMAS_ID = 0;
    public static final int FRM_FIELD_KADAR_ID = 1;
    public static final int FRM_FIELD_COLOR_ID = 2;
    public static final int FRM_FIELD_LOKAL = 3;
    public static final int FRM_FIELD_RETURN_LOKAL = 4;
    public static final int FRM_FIELD_RETURN_LOKAL_RUSAK = 5;
    public static final int FRM_FIELD_ASING = 6;
    public static final int FRM_FIELD_RETURN_ASING = 7;
    public static final int FRM_FIELD_RETURN_ASING_RUSAK = 8;
    public static final int FRM_FIELD_PESANAN = 9;
    public static final int FRM_FIELD_TARIF_RETUR = 10;
    public static final int FRM_FIELD_TARIF_RETUR_DIATAS_SETAHUN = 11;

    public static String[] fieldNames = {
        "FRM_FIELD_NILAI_TUKAR_EMAS_ID",
        "FRM_FIELD_KADAR_ID",
        "FRM_FIELD_COLOR_ID",
        "FRM_FIELD_LOKAL",
        "FRM_FIELD_RETURN_LOKAL",
        "FRM_FIELD_RETURN_LOKAL_RUSAK",
        "FRM_FIELD_ASING",
        "FRM_FIELD_RETURN_ASING",
        "FRM_FIELD_RETURN_ASING_RUSAK",
        "FRM_FIELD_PESANAN",
        "FRM_FIELD_TARIF_RETUR",
        "FRM_FIELD_TARIF_RETUR_DIATAS_SETAHUN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
     public static String[] fieldQuestion = {
        "",
        "Pilih kadar",
        "Pilih warna",
        "Isi data lokal",
        "Isi data retur lokal",
        "Isi data retur lokal rusak",
        "Isi data asing",
        "Isi data retur asing",
        "Isi data retur asing rusak",
        "Isi data pesanan",
        "Isi data tarif retur",
        "Isi data tarif retur di atas setahun"
    };
     
      public static String[] fieldError = {
        "",
        "FRM_FIELD_KADAR_ID",
        "FRM_FIELD_COLOR_ID",
        "FRM_FIELD_LOKAL",
        "FRM_FIELD_RETURN_LOKAL",
        "FRM_FIELD_RETURN_LOKAL_RUSAK",
        "FRM_FIELD_ASING",
        "FRM_FIELD_RETURN_ASING",
        "FRM_FIELD_RETURN_ASING_RUSAK",
        "FRM_FIELD_PESANAN",        
        "FRM_FIELD_TARIF_RETUR",
        "FRM_FIELD_TARIF_RETUR_DIATAS_SETAHUN"
    };
    
    public FrmMaterialNilaiTukarEmas() {
    }

    public FrmMaterialNilaiTukarEmas(MaterialNilaiTukarEmas entMaterialNilaiTukarEmas) {
        this.entMaterialNilaiTukarEmas = entMaterialNilaiTukarEmas;
    }

    public FrmMaterialNilaiTukarEmas(HttpServletRequest request, MaterialNilaiTukarEmas entMaterialNilaiTukarEmas) {
        super(new FrmMaterialNilaiTukarEmas(entMaterialNilaiTukarEmas), request);
        this.entMaterialNilaiTukarEmas = entMaterialNilaiTukarEmas;
    }

    public String getFormName() {
        return FRM_NAME_MATERIALNILAITUKAREMAS;
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

    public MaterialNilaiTukarEmas getEntityObject() {
        return entMaterialNilaiTukarEmas;
    }

    public void requestEntityObject(MaterialNilaiTukarEmas entMaterialNilaiTukarEmas) {
        try {
            this.requestParam();
            entMaterialNilaiTukarEmas.setKadarId(getLong(FRM_FIELD_KADAR_ID));
            entMaterialNilaiTukarEmas.setColorId(getLong(FRM_FIELD_COLOR_ID));
            entMaterialNilaiTukarEmas.setLokal(getDouble(FRM_FIELD_LOKAL));
            entMaterialNilaiTukarEmas.setReturnLokal(getDouble(FRM_FIELD_RETURN_LOKAL));
            entMaterialNilaiTukarEmas.setReturnLokalRusak(getDouble(FRM_FIELD_RETURN_LOKAL_RUSAK));
            entMaterialNilaiTukarEmas.setAsing(getDouble(FRM_FIELD_ASING));
            entMaterialNilaiTukarEmas.setReturnAsing(getDouble(FRM_FIELD_RETURN_ASING));
            entMaterialNilaiTukarEmas.setReturnAsingRusak(getDouble(FRM_FIELD_RETURN_ASING_RUSAK));
            entMaterialNilaiTukarEmas.setPesanan(getDouble(FRM_FIELD_PESANAN));
            entMaterialNilaiTukarEmas.setTarifRetur(getDouble(FRM_FIELD_TARIF_RETUR));
            entMaterialNilaiTukarEmas.setTarifReturDiatasSetahun(getDouble(FRM_FIELD_TARIF_RETUR_DIATAS_SETAHUN));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
