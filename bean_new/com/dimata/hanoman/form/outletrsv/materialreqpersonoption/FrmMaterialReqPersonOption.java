/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.materialreqpersonoption;

import com.dimata.hanoman.entity.outletrsv.materialreqpersonoption.MaterialReqPersonOption;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMaterialReqPersonOption extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MaterialReqPersonOption entMaterialReqPersonOption;
    public static final String FRM_NAME_MATERIAL_REQ_PERSON_OPTION = "FRM_NAME_MATERIAL_REQ_PERSON_OPTION";
    public static final int FRM_FIELD_MATERIAL_REQ_PERSON_OPTION_ID = 0;
    public static final int FRM_FIELD_MATERIAL_REQ_PERSON_ID = 1;
    public static final int FRM_FIELD_DEPARTMENT_ID = 2;
    public static final int FRM_FIELD_SECTION_ID = 3;
    public static final int FRM_FIELD_POSITION_ID = 4;
    public static final int FRM_FIELD_COMPETENCY_ID = 5;
    public static final int FRM_FIELD_LEVEL_ID = 6;
    public static final int FRM_FIELD_PRIORITY_INDEX = 7;

    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_REQ_PERSON_OPTION_ID",
        "FRM_FIELD_MATERIAL_REQ_PERSON_ID",
        "FRM_FIELD_DEPARTMENT_ID",
        "FRM_FIELD_SECTION_ID",
        "FRM_FIELD_POSITION_ID",
        "FRM_FIELD_COMPETENCY_ID",
        "FRM_FIELD_LEVEL_ID",
        "FRM_FIELD_PRIORITY_INDEX"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT
    };

    public FrmMaterialReqPersonOption() {
    }

    public FrmMaterialReqPersonOption(MaterialReqPersonOption entMaterialReqPersonOption) {
        this.entMaterialReqPersonOption = entMaterialReqPersonOption;
    }

    public FrmMaterialReqPersonOption(HttpServletRequest request, MaterialReqPersonOption entMaterialReqPersonOption) {
        super(new FrmMaterialReqPersonOption(entMaterialReqPersonOption), request);
        this.entMaterialReqPersonOption = entMaterialReqPersonOption;
    }

    public String getFormName() {
        return FRM_NAME_MATERIAL_REQ_PERSON_OPTION;
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

    public MaterialReqPersonOption getEntityObject() {
        return entMaterialReqPersonOption;
    }

    public void requestEntityObject(MaterialReqPersonOption entMaterialReqPersonOption) {
        try {
            this.requestParam();
//            entMaterialReqPersonOption.setMaterialReqPersonOptionId(getLong(FRM_FIELD_MATERIAL_REQ_PERSON_OPTION_ID));
            entMaterialReqPersonOption.setMaterialReqPersonId(getLong(FRM_FIELD_MATERIAL_REQ_PERSON_ID));
            entMaterialReqPersonOption.setDepartmentId(getLong(FRM_FIELD_DEPARTMENT_ID));
            entMaterialReqPersonOption.setSectionId(getLong(FRM_FIELD_SECTION_ID));
            entMaterialReqPersonOption.setPositionId(getLong(FRM_FIELD_POSITION_ID));
            entMaterialReqPersonOption.setCompetencyId(getLong(FRM_FIELD_COMPETENCY_ID));
            entMaterialReqPersonOption.setLevelId(getLong(FRM_FIELD_LEVEL_ID));
            entMaterialReqPersonOption.setPriorityIndex(getInt(FRM_FIELD_PRIORITY_INDEX));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
