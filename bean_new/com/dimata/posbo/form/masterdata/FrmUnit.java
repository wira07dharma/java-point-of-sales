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

public class FrmUnit extends FRMHandler implements I_FRMInterface, I_FRMType {
    private Unit unit;
    
    public static final String FRM_NAME_UNIT = "FRM_NAME_UNIT";
    
    public static final int FRM_FIELD_UNIT_ID = 0;
    public static final int FRM_FIELD_CODE = 1;
    public static final int FRM_FIELD_NAME = 2;
    public static final int FRM_FIELD_QTY_PER_BASE_UNIT = 3;
    public static final int FRM_FIELD_BASE_UNIT_ID = 4;
    
    public static String[] fieldNames = {
        "FRM_FIELD_UNIT_ID", "FRM_FIELD_CODE",
        "FRM_FIELD_NAME", "FRM_FIELD_QTY_PER_BASE_UNIT",
        "FRM_FIELD_BASE_UNIT_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED, TYPE_FLOAT,
        TYPE_LONG
    };
    
    public FrmUnit() {
    }
    
    public FrmUnit(Unit unit) {
        this.unit = unit;
    }
    
    public FrmUnit(HttpServletRequest request, Unit unit) {
        super(new FrmUnit(unit), request);
        this.unit = unit;
    }
    
    public String getFormName() {
        return FRM_NAME_UNIT;
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
    
    public Unit getEntityObject() {
        return unit;
    }
    
    public void requestEntityObject(Unit unit) {
        try {
            this.requestParam();
            unit.setCode(getString(FRM_FIELD_CODE));
            unit.setName(getString(FRM_FIELD_NAME));
            unit.setQtyPerBaseUnit(getDouble(FRM_FIELD_QTY_PER_BASE_UNIT));
            unit.setBaseUnitId(getLong(FRM_FIELD_BASE_UNIT_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
