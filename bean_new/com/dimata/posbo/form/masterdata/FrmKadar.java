/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.Kadar;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import static com.dimata.qdep.form.I_FRMType.TYPE_LONG;
import static com.dimata.qdep.form.I_FRMType.TYPE_STRING;
import static java.awt.image.DataBuffer.TYPE_DOUBLE;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Yudi
 */
public class FrmKadar extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Kadar entKadar;
    public static final String FRM_NAME_KADAR = "FRM_NAME_KADAR";
    public static final int FRM_FIELD_KADAR_ID = 0;
    public static final int FRM_FIELD_KODE_KADAR = 1;
    public static final int FRM_FIELD_KADAR = 2;
    public static final int FRM_FIELD_KARAT = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_KADAR_ID",
        "FRM_FIELD_KODE_KADAR",
        "FRM_FIELD_KADAR",
        "FRM_FIELD_KARAT"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DOUBLE,
        TYPE_INT
    };
    
    public static String[] fieldQuestion = {
        "",
        "Kolom ini harus di isi kode kadar",
        "Kolom ini harus kadar",
        "Kolom ini harus karat"
    };
    
    public static String[] fieldError = {
        "",
        "Kolom ini harus di isi kode kadar",
        "Kolom ini harus kadar",
        "Kolom ini harus karat"
    };
    
    
    public FrmKadar() {
    }

    public FrmKadar(Kadar entKadar) {
        this.entKadar = entKadar;
    }

    public FrmKadar(HttpServletRequest request, Kadar entKadar) {
        super(new FrmKadar(entKadar), request);
        this.entKadar = entKadar;
    }

    public String getFormName() {
        return FRM_NAME_KADAR;
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

    public Kadar getEntityObject() {
        return entKadar;
    }

    public void requestEntityObject(Kadar entKadar) {
        try {
            this.requestParam();
            entKadar.setKodeKadar(getString(FRM_FIELD_KODE_KADAR));
            entKadar.setKadar(getDouble(FRM_FIELD_KADAR));
            entKadar.setKarat(getInt(FRM_FIELD_KARAT));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
