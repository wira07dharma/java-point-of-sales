/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/* java package */

import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

/**
 *
 * @author PT. Dimata
 */
public class FrmSalesType extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SalesType salesType;

    public static final String FRM_SALES_TYPE = "FRM_SALES_TYPE";

    public static final int FRM_FIELD_TYPE_SALES_ID= 0;
    public static final int FRM_FIELD_TYPE_SALES_NAME = 1;
    public static final int FRM_FIELD_TYPE_USE_SALES = 2;

    public static String[] fieldNames =
            {
                "FRM_FIELD_COSTING_ID", 
                "FRM_FIELD_NAME",
                "FRM_FIELD_DESCRIPTION"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG, 
                TYPE_STRING + ENTRY_REQUIRED,
                TYPE_INT,

            };

    public FrmSalesType() {
    }

    public FrmSalesType(SalesType salesType) {
        this.salesType = salesType;
    }

    public FrmSalesType(HttpServletRequest request, SalesType salesType) {
        super(new FrmSalesType(salesType), request);
        this.salesType = salesType;
    }

    public String getFormName() {
        return FRM_SALES_TYPE;
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

    public SalesType getEntityObject() {
        return salesType;
    }

    public void requestEntityObject(SalesType salesType) {
        try {
            
            this.requestParam();
            
            salesType.setName(getString(FRM_FIELD_TYPE_SALES_NAME));
            salesType.setTypeSales(getInt(FRM_FIELD_TYPE_USE_SALES));

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
