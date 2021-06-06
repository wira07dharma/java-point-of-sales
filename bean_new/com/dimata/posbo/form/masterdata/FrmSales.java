package com.dimata.posbo.form.masterdata;

/* java package */

import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmSales extends FRMHandler implements I_FRMInterface, I_FRMType {
    private Sales sales;

    public static final String FRM_NAME_SHIFT = "FRM_NAME_SHIFT";

    public static final int FRM_FIELD_SALES_ID = 0;
    public static final int FRM_FIELD_CODE = 1;
    public static final int FRM_FIELD_NAME = 2;
    public static final int FRM_FIELD_REMARK = 3;
    public static final int FRM_FIELD_COMMISION = 4;
    //adding login id & password by mirahu 20120514
    public static final int FRM_FIELD_LOGIN_ID = 5;
    public static final int FRM_FIELD_PASSWORD = 6;
    public static final int FRM_FIELD_LOCATION_ID = 7;
    public static final int FRM_FIELD_CURRENCY_ID=8;
    //added by dewok
    public static final int FRM_FIELD_EMPLOYEE_ID=9;
    public static final int FRM_FIELD_STATUS=10;
    public static final int FRM_FIELD_POSITION_ID=11;

    public static String[] fieldNames =
            {
                    "FRM_FIELD_SALES_ID", "FRM_FIELD_CODE",
                    "FRM_FIELD_NAME", "FRM_FIELD_REMARK",
                    "FRM_FIELD_COMMISION",
                    //adding login id & password by mirahu 20120514
                    "FRM_FIELD_LOGIN_ID", "FRM_FIELD_PASSWORD","FRM_FIELD_LOCATION_ID","FRM_FIELD_CURRENCY_ID",
                    "EMPLOYEE_ID","FRM_FIELD_STATUS","FRM_FIELD_POSITION_ID"
            };

    public static int[] fieldTypes =
            {
                    TYPE_LONG, TYPE_STRING + ENTRY_REQUIRED,
                    TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING,
                    TYPE_FLOAT,
                    //adding login id & password by mirahu 20120514
                    TYPE_STRING, TYPE_STRING,TYPE_LONG,TYPE_LONG,
                    TYPE_LONG, TYPE_INT,TYPE_LONG
            };

    public FrmSales() {
    }

    public FrmSales(Sales sales) {
        this.sales = sales;
    }

    public FrmSales(HttpServletRequest request, Sales sales) {
        super(new FrmSales(sales), request);
        this.sales = sales;
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

    public Sales getEntityObject() {
        return sales;
    }

    public void requestEntityObject(Sales sales) {
        try {
            this.requestParam();
            sales.setCode(getString(FRM_FIELD_CODE));
            sales.setName(getString(FRM_FIELD_NAME));
            sales.setRemark(getString(FRM_FIELD_REMARK));
            sales.setCommision(getDouble(FRM_FIELD_COMMISION));
            //adding login id & password by mirahu 20120514
            sales.setLoginId(getString(FRM_FIELD_LOGIN_ID));
            sales.setPassword(getString(FRM_FIELD_PASSWORD));
            sales.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            sales.setDefaultCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
            sales.setEmployeeId(getLong(FRM_FIELD_EMPLOYEE_ID));
            sales.setStatus(getInt(FRM_FIELD_STATUS));
            sales.setPositionId(getLong(FRM_FIELD_POSITION_ID));
        }
        catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
