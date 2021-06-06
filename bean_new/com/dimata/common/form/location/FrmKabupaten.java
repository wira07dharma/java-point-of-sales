/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.form.location;

import com.dimata.common.entity.location.Kabupaten;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Acer
 */
public class FrmKabupaten extends FRMHandler implements I_FRMInterface, I_FRMType {
    
     private Kabupaten kabupaten;
    public static final String FRM_NAME_KABUPATEN = "FRM_NAME_KABUPATEN";
    public static final int FRM_FIELD_ID_KABUPATEN = 0;
    public static final int FRM_FIELD_KD_KABUPATEN = 1;
    public static final int FRM_FIELD_NM_KABUPATEN = 2;
    public static final int FRM_FIELD_ID_PROPINSI = 3;
    public static final int FRM_FIELD_ID_NEGARA = 4;
    public static String[] fieldNames = {
        "FRM_FIELD_ID_KABUPATEN", "FRM_FIELD_KD_KABUPATEN",
        "FRM_FIELD_NM_KABUPATEN", "FRM_FIELD_ID_PROPINSI","FRM_FIELD_ID_NEGARA"
    };
    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_STRING,
        TYPE_STRING , TYPE_LONG + ENTRY_REQUIRED, TYPE_LONG + ENTRY_REQUIRED
    };

    public FrmKabupaten() {
    }

    public FrmKabupaten(Kabupaten kabupaten) {
        this.kabupaten = kabupaten;
    }

    public FrmKabupaten(HttpServletRequest request, Kabupaten kabupaten) {
        super(new FrmKabupaten(kabupaten), request);
        this.kabupaten = kabupaten;
    }

    public String getFormName() {
        return FRM_NAME_KABUPATEN;
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

    public Kabupaten getEntityObject() {
        return kabupaten;
    }

    public void requestEntityObject(Kabupaten kabupaten) {
        try {
            this.requestParam();
            kabupaten.setKdKabupaten(getString(FRM_FIELD_KD_KABUPATEN));
            kabupaten.setNmKabupaten(getString(FRM_FIELD_NM_KABUPATEN));
            kabupaten.setIdPropinsi(getLong(FRM_FIELD_ID_PROPINSI));
            kabupaten.setIdNegara(getLong(FRM_FIELD_ID_NEGARA));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
}
