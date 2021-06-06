/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.Color;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import static com.dimata.qdep.form.I_FRMType.TYPE_INT;
import static com.dimata.qdep.form.I_FRMType.TYPE_LONG;
import static com.dimata.qdep.form.I_FRMType.TYPE_STRING;
import static java.awt.image.DataBuffer.TYPE_DOUBLE;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmColor extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Color entColor;
    public static final String FRM_NAME_COLOR = "FRM_NAME_COLOR";
    public static final int FRM_FIELD_COLOR_ID = 0;
    public static final int FRM_FIELD_COLOR_CODE = 1;
    public static final int FRM_FIELD_COLOR_NAME= 2;
   
    public static String[] fieldNames = {
        "FRM_FIELD_COLOR_ID",
        "FRM_FIELD_COLOR_CODE",
        "FRM_FIELD_COLOR_NAME",
   
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
     
    };
    
    public static String[] fieldQuestion = {
        "",
        "Kolom ini harus di isi color code",
        "Kolom ini harus di isi color name"
    };
    
    public static String[] fieldError = {
        "",
        "Kolom ini harus di isi color code",
        "Kolom ini harus di isi color name"

    };
    
    
    public FrmColor() {
    }

    public FrmColor(Color entColor) {
        this.entColor = entColor;
    }

    public FrmColor(HttpServletRequest request, Color entColor) {
        super(new FrmColor(entColor), request);
        this.entColor = entColor;
    }

    public String getFormName() {
        return FRM_NAME_COLOR;
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

    public Color getEntityObject() {
        return entColor;
    }

    public void requestEntityObject(Color entColor) {
        try {
            this.requestParam();
            entColor.setColorCode(getString(FRM_FIELD_COLOR_CODE));
            entColor.setColorName(getString(FRM_FIELD_COLOR_NAME));
            
            } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }   
    }

}
