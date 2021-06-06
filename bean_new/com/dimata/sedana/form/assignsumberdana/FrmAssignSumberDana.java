/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.assignsumberdana;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.sedana.entity.assignsumberdana.AssignSumberDana;
import javax.servlet.http.HttpServletRequest;

public class FrmAssignSumberDana extends FRMHandler implements I_FRMInterface, I_FRMType {

    private AssignSumberDana entAssignSumberDana;
    public static final String FRM_NAME_ASSIGNSUMBERDANA = "FRM_NAME_ASSIGNSUMBERDANA";
    public static final int FRM_FIELD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID = 0;
    public static final int FRM_FIELD_SUMBER_DANA_ID = 1;
    public static final int FRM_FIELD_TYPE_KREDIT_ID = 2;
    public static final int FRM_FIELD_MAX_PRESENTASE_PENGGUNAAN = 3;

    public static String[] fieldNames = {
        "FRM_FIELD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID",
        "FRM_FIELD_SUMBER_DANA_ID",
        "FRM_FIELD_TYPE_KREDIT_ID",
        "FRM_FIELD_MAX_PRESENTASE_PENGGUNAAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT
    };

    public FrmAssignSumberDana() {
    }

    public FrmAssignSumberDana(AssignSumberDana entAssignSumberDana) {
        this.entAssignSumberDana = entAssignSumberDana;
    }

    public FrmAssignSumberDana(HttpServletRequest request, AssignSumberDana entAssignSumberDana) {
        super(new FrmAssignSumberDana(entAssignSumberDana), request);
        this.entAssignSumberDana = entAssignSumberDana;
    }

    public String getFormName() {
        return FRM_NAME_ASSIGNSUMBERDANA;
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

    public AssignSumberDana getEntityObject() {
        return entAssignSumberDana;
    }

    public void requestEntityObject(AssignSumberDana entAssignSumberDana) {
        try {
            this.requestParam();
            entAssignSumberDana.setSumberDanaId(getLong(FRM_FIELD_SUMBER_DANA_ID));
            entAssignSumberDana.setTypeKreditId(getLong(FRM_FIELD_TYPE_KREDIT_ID));
            entAssignSumberDana.setMaxPresentasePenggunaan(getFloat(FRM_FIELD_MAX_PRESENTASE_PENGGUNAAN));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
