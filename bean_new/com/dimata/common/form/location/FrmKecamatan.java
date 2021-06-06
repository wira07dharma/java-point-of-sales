/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.form.location;

import com.dimata.common.entity.location.Kecamatan;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Acer
 */
public class FrmKecamatan extends FRMHandler implements I_FRMInterface, I_FRMType  {
    
    
     private Kecamatan kecamatan;
    public static final String FRM_NAME_KECAMATAN = "FRM_NAME_KECAMATAN";
    public static final int FRM_FIELD_ID_KECAMATAN = 0;
    public static final int FRM_FIELD_KD_KECAMATAN = 1;
    public static final int FRM_FIELD_NM_KECAMATAN = 2;
    public static final int FRM_FIELD_ID_KABUPATEN = 3;
    public static final int FRM_FIELD_ID_PROPINSI = 4;
    public static final int FRM_FIELD_ID_NEGARA = 5;
    public static String[] fieldNames = {
        "FRM_FIELD_ID_KECAMATAN", "FRM_FIELD_KD_KECAMATAN",
        "FRM_FIELD_NM_KECAMATAN", "FRM_FIELD_ID_KABUPATEN",
        "FRM_FIELD_ID_PROPINSI", "FRM_FIELD_ID_NEGARA"
    };
    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_STRING,
        TYPE_STRING + ENTRY_REQUIRED, TYPE_LONG + ENTRY_REQUIRED, TYPE_LONG + ENTRY_REQUIRED, TYPE_LONG + ENTRY_REQUIRED
    };

    public FrmKecamatan() {
    }

    public FrmKecamatan(Kecamatan kecamatan) {
        this.kecamatan = kecamatan;
    }

    public FrmKecamatan(HttpServletRequest request, Kecamatan kecamatan) {
        super(new FrmKecamatan(kecamatan), request);
        this.kecamatan = kecamatan;
    }

    public String getFormName() {
        return FRM_NAME_KECAMATAN;
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

    public Kecamatan getEntityObject() {
        return kecamatan;
    }

    public void requestEntityObject(Kecamatan kecamatan) {
        try {
            this.requestParam();
            kecamatan.setKdKecamatan(getString(FRM_FIELD_KD_KECAMATAN));
            kecamatan.setNmKecamatan(getString(FRM_FIELD_NM_KECAMATAN));
            kecamatan.setIdKabupaten(getLong(FRM_FIELD_ID_KABUPATEN));
            kecamatan.setIdPropinsi(getLong(FRM_FIELD_ID_PROPINSI));
            kecamatan.setIdNegara(getLong(FRM_FIELD_ID_NEGARA));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
