/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.AssignDiscount;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmAssignDiscount extends FRMHandler implements I_FRMInterface, I_FRMType {

    private AssignDiscount entAssignDiscount;
    public static final String FRM_NAME_ASSIGN_DISCOUNT = "FRM_NAME_ASSIGN_DISCOUNT";
    public static final int FRM_FIELD_ASSIGN_DISC_ID = 0;
    public static final int FRM_FIELD_EMPLOYEE_ID = 1;
    public static final int FRM_FIELD_MAX_DISC = 2;

    public static String[] fieldNames = {
        "FRM_FIELD_ASSIGN_DISC_ID",
        "FRM_FIELD_EMPLOYEE_ID",
        "FRM_FIELD_MAX_DISC"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT
    };

    public FrmAssignDiscount() {
    }

    public FrmAssignDiscount(AssignDiscount entAssignDiscount) {
        this.entAssignDiscount = entAssignDiscount;
    }

    public FrmAssignDiscount(HttpServletRequest request, AssignDiscount entAssignDiscount) {
        super(new FrmAssignDiscount(entAssignDiscount), request);
        this.entAssignDiscount = entAssignDiscount;
    }

    public String getFormName() {
        return FRM_NAME_ASSIGN_DISCOUNT;
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

    public AssignDiscount getEntityObject() {
        return entAssignDiscount;
    }

    public void requestEntityObject(AssignDiscount entAssignDiscount) {
        try {
            this.requestParam();
//            entAssignDiscount.setAssignDiscountId(getLong(FRM_FIELD_ASSIGN_DISC_ID));
            entAssignDiscount.setEmployeeId(getLong(FRM_FIELD_EMPLOYEE_ID));
            entAssignDiscount.setMaxDisc(getFloat(FRM_FIELD_MAX_DISC));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
