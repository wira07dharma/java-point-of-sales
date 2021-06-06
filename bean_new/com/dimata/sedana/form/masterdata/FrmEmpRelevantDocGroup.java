/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.form.masterdata;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.sedana.entity.masterdata.EmpRelevantDocGroup;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dedy_blinda
 */
public class FrmEmpRelevantDocGroup extends FRMHandler implements I_FRMInterface, I_FRMType {
    private EmpRelevantDocGroup entEmpRelevantDocGroup;
    public static final String FRM_NAME_EMP_RELVT_DOC_GRP = "FRM_NAME_EMP_RELVT_DOC_GRP";
    public static final int FRM_FIELD_EMP_RELVT_DOC_GRP_ID = 0;
    public static final int FRM_FIELD_DOC_GROUP = 1;
    public static final int FRM_FIELD_DOC_GROUP_DESC = 2;
   /* public static final int FRM_FIELD_SYSTEM_NAME = 3;
    public static final int FRM_FIELD_MODUL = 4;
    public static final int FRM_FIELD_DOC_GROUP_TIPE = 5;*/
    
    public static String[] fieldNames = {
        "FRM_FIELD_EMP_RELVT_DOC_GRP_ID",
        "FRM_FIELD_DOC_GROUP",
        "FRM_FIELD_DOC_GROUP_DESC",
       /* "FRM_FIELD_SYSTEM_NAME",
        "FRM_FIELD_MODUL",
        "FRM_FIELD_DOC_GROUP_TIPE"*/
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
       /* TYPE_INT,
        TYPE_INT,
        TYPE_INT*/
    };

    public FrmEmpRelevantDocGroup() {
    }

    public FrmEmpRelevantDocGroup(EmpRelevantDocGroup entEmpRelevantDocGroup) {
        this.entEmpRelevantDocGroup = entEmpRelevantDocGroup;
    }

    public FrmEmpRelevantDocGroup(HttpServletRequest request, EmpRelevantDocGroup entEmpRelevantDocGroup) {
        super(new FrmEmpRelevantDocGroup(entEmpRelevantDocGroup), request);
        this.entEmpRelevantDocGroup = entEmpRelevantDocGroup;
    }

    public String getFormName() {
        return FRM_NAME_EMP_RELVT_DOC_GRP;
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

    public EmpRelevantDocGroup getEntityObject() {
        return entEmpRelevantDocGroup;
    }

    public void requestEntityObject(EmpRelevantDocGroup entEmpRelevantDocGroup) {
        try {
            this.requestParam();
            entEmpRelevantDocGroup.setDocGroup(getString(FRM_FIELD_DOC_GROUP));
            entEmpRelevantDocGroup.setDocGroupDesc(getString(FRM_FIELD_DOC_GROUP_DESC));
            /*
            entEmpRelevantDocGroup.setSystemName(getInt(FRM_FIELD_SYSTEM_NAME));
            entEmpRelevantDocGroup.setModul(getInt(FRM_FIELD_MODUL));
            entEmpRelevantDocGroup.setDocGroupTipe(getInt(FRM_FIELD_DOC_GROUP_TIPE));*/
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
