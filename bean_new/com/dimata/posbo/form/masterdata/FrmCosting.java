/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

/**
 *
 * @author PT. Dimata
 */
public class FrmCosting extends FRMHandler implements I_FRMInterface, I_FRMType {
    private Costing costing;

    public static final String FRM_NAME_COSTING = "FRM_NAME_COSTING";

    public static final int FRM_FIELD_COSTING_ID = 0;
    public static final int FRM_FIELD_NAME = 1;
    public static final int FRM_FIELD_DESCRIPTION = 2;

    public static String[] fieldNames =
            {
                "FRM_FIELD_COSTING_ID", "FRM_FIELD_NAME",
                "FRM_FIELD_DESCRIPTION"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG, TYPE_STRING + ENTRY_REQUIRED,
                TYPE_STRING,

            };

    public FrmCosting() {
    }

    public FrmCosting(Costing costing) {
        this.costing = costing;
    }

    public FrmCosting(HttpServletRequest request, Costing costing) {
        super(new FrmCosting(costing), request);
        this.costing = costing;
    }

    public String getFormName() {
        return FRM_NAME_COSTING;
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

    public Costing getEntityObject() {
        return costing;
    }

    public void requestEntityObject(Costing costing) {
        try {
            this.requestParam();
            costing.setName(getString(FRM_FIELD_NAME));
            costing.setDescription(getString(FRM_FIELD_DESCRIPTION));

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
