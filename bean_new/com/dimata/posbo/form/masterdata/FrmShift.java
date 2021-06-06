package com.dimata.posbo.form.masterdata;

/* java package */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.gui.jsp.ControlDate;

public class FrmShift extends FRMHandler implements I_FRMInterface, I_FRMType {
    private Shift shift;

    public static final String FRM_NAME_SHIFT = "FRM_NAME_SHIFT";

    public static final int FRM_FIELD_SHIFT_ID = 0;
    public static final int FRM_FIELD_NAME = 1;
    public static final int FRM_FIELD_REMARK = 2;
    public static final int FRM_FIELD_START_TIME = 3;
    public static final int FRM_FIELD_END_TIME = 4;

    public static String[] fieldNames =
            {
                "FRM_FIELD_SHIFT_ID", "FRM_FIELD_NAME",
                "FRM_FIELD_REMARK", "FRM_FIELD_START_TIME",
                "FRM_FIELD_END_TIME"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG, TYPE_STRING + ENTRY_REQUIRED,
                TYPE_STRING + ENTRY_REQUIRED, TYPE_DATE,
                TYPE_DATE
            };

    public FrmShift() {
    }

    public FrmShift(Shift shift) {
        this.shift = shift;
    }

    public FrmShift(HttpServletRequest request, Shift shift) {
        super(new FrmShift(shift), request);
        this.shift = shift;
    }

    public String getFormName() {
        return FRM_NAME_SHIFT;
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

    public Shift getEntityObject() {
        return shift;
    }

    public void requestEntityObject(Shift shift) {
        try {
            this.requestParam();
            shift.setName(getString(FRM_FIELD_NAME));
            shift.setRemark(getString(FRM_FIELD_REMARK));
            shift.setStartTime(getDate(FRM_FIELD_START_TIME));
            shift.setEndTime(getDate(FRM_FIELD_END_TIME));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
