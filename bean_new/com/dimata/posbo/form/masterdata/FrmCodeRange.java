package com.dimata.posbo.form.masterdata;

import javax.servlet.http.*;

import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.masterdata.*;

public class FrmCodeRange extends FRMHandler implements I_FRMInterface, I_FRMType {
    private CodeRange codeRange;

    public static final String FRM_NAME_CODE_RANGE = "FRM_NAME_CODE_RANGE";

    public static final int FRM_FIELD_FROM_CODE = 0;
    public static final int FRM_FIELD_TO_CODE = 1;

    public static String[] fieldNames =
            {
                "FRM_FIELD_FROM_CODE", "FRM_FIELD_TO_CODE"
            };

    public static int[] fieldTypes =
            {
                TYPE_STRING + ENTRY_REQUIRED,
                TYPE_STRING + ENTRY_REQUIRED
            };

    public FrmCodeRange() {
    }

    public FrmCodeRange(CodeRange codeRange) {
        this.codeRange = codeRange;
    }

    public FrmCodeRange(HttpServletRequest request, CodeRange codeRange) {
        super(new FrmCodeRange(codeRange), request);
        this.codeRange = codeRange;
    }

    public String getFormName() {
        return FRM_NAME_CODE_RANGE;
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

    public CodeRange getEntityObject() {
        return codeRange;
    }

    public void requestEntityObject(CodeRange codeRange) {
        try {
            this.requestParam();
            codeRange.setFromRangeCode(getString(FRM_FIELD_FROM_CODE));
            codeRange.setToRangeCode(getString(FRM_FIELD_TO_CODE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
