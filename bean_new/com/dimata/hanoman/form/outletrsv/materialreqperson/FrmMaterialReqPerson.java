/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.materialreqperson;

import com.dimata.hanoman.entity.outletrsv.materialreqperson.MaterialReqPerson;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMaterialReqPerson extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MaterialReqPerson entMaterialReqPerson;
    public static final String FRM_NAME_MATERIAL_REQ_PERSON = "FRM_NAME_MATERIAL_REQ_PERSON";
    public static final int FRM_FIELD_MATERIAL_REQ_PERSON_ID = 0;
    public static final int FRM_FIELD_MATERIAL_REQ_LOCATION_ID = 1;
    public static final int FRM_FIELD_NUMBER_OF_PERSON = 2;
    public static final int FRM_FIELD_JOBDESC = 3;
    public static final int FRM_FIELD_JOB_WEIGHT = 4;

    public static String[] fieldNames = {
        "FRM_FIELD_MATERIAL_REQ_PERSON_ID",
        "FRM_FIELD_MATERIAL_REQ_LOCATION_ID",
        "FRM_FIELD_NUMBER_OF_PERSON",
        "FRM_FIELD_JOBDESC",
        "FRM_FIELD_JOB_WEIGHT"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_FLOAT
    };

    public FrmMaterialReqPerson() {
    }

    public FrmMaterialReqPerson(MaterialReqPerson entMaterialReqPerson) {
        this.entMaterialReqPerson = entMaterialReqPerson;
    }

    public FrmMaterialReqPerson(HttpServletRequest request, MaterialReqPerson entMaterialReqPerson) {
        super(new FrmMaterialReqPerson(entMaterialReqPerson), request);
        this.entMaterialReqPerson = entMaterialReqPerson;
    }

    public String getFormName() {
        return FRM_NAME_MATERIAL_REQ_PERSON;
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

    public MaterialReqPerson getEntityObject() {
        return entMaterialReqPerson;
    }

    public void requestEntityObject(MaterialReqPerson entMaterialReqPerson) {
        try {
            this.requestParam();
//            entMaterialReqPerson.setMaterialReqPersonId(getLong(FRM_FIELD_MATERIAL_REQ_PERSON_ID));
            entMaterialReqPerson.setMaterialReqLocationId(getLong(FRM_FIELD_MATERIAL_REQ_LOCATION_ID));
            entMaterialReqPerson.setNumberOfPerson(getInt(FRM_FIELD_NUMBER_OF_PERSON));
            entMaterialReqPerson.setJobdesc(getString(FRM_FIELD_JOBDESC));
            entMaterialReqPerson.setJobWeight(getFloat(FRM_FIELD_JOB_WEIGHT));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
