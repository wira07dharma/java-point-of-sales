/*
 * FrmCashMaster.java
 *
 * Created on January 8, 2004, 9:53 AM
 */
package com.dimata.pos.form.masterCashier;

/**
 *
 * @author gedhy
 */
// java package 
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// qdep package 
import com.dimata.qdep.form.*;

// project package 
import com.dimata.pos.entity.masterCashier.*;

public class FrmCashMaster extends FRMHandler implements I_FRMInterface, I_FRMType {

    private CashMaster cashMaster;

    public static final String FRM_NAME_CASH_MASTER = "FRM_NAME_CASH_MASTER";

    public static final int FRM_FIELD_CASH_MASTER_ID = 0;
    public static final int FRM_FIELD_LOCATION_ID = 1;
    public static final int FRM_FIELD_CASHIER_NUMBER = 2;
    public static final int FRM_FIELD_TAX = 3;
    public static final int FRM_FIELD_SERVICE = 4;
    public static final int FRM_FIELD_PRICE_TYPE = 5;
    //added by dewok 2018-06-22
    public static final int FRM_FIELD_CASHIER_DATABASE_MODE = 6;
    public static final int FRM_FIELD_CASHIER_STOCK_MODE = 7;

    public static String[] fieldNames = {
        "FRM_FIELD_CASH_MASTER_ID",
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_CASHIER_NUMBER",
        "FRM_FIELD_TAX",
        "FRM_FIELD_SERVICE",
        "FRM_FIELD_PRICE_TYPE",
        //added by dewok 2018-06-22
        "FRM_FIELD_CASHIER_DATABASE_MODE",
        "FRM_FIELD_CASHIER_STOCK_MODE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        //added by dewok 2018-06-22
        TYPE_INT,
        TYPE_INT
    };

    public FrmCashMaster() {
    }

    public FrmCashMaster(CashMaster cashMaster) {
        this.cashMaster = cashMaster;
    }

    public FrmCashMaster(HttpServletRequest request, CashMaster cashMaster) {
        super(new FrmCashMaster(cashMaster), request);
        this.cashMaster = cashMaster;
    }

    public String getFormName() {
        return FRM_NAME_CASH_MASTER;
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

    public CashMaster getEntityObject() {
        return cashMaster;
    }

    public void requestEntityObject(CashMaster cashMaster) {
        try {
            this.requestParam();
            cashMaster.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            cashMaster.setCashierNumber(getInt(FRM_FIELD_CASHIER_NUMBER));
            cashMaster.setCashTax(getDouble(FRM_FIELD_TAX));
            cashMaster.setCashService(getDouble(FRM_FIELD_SERVICE));
            cashMaster.setPriceType(getString(FRM_FIELD_PRICE_TYPE));
            //added by dewok 2018-06-22
            cashMaster.setCashierDatabaseMode(getInt(FRM_FIELD_CASHIER_DATABASE_MODE));
            cashMaster.setCashierStockMode(getInt(FRM_FIELD_CASHIER_STOCK_MODE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
