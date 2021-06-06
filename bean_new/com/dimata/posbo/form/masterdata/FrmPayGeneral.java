/*
 * FrmPayGeneral.java
 *
 * Created on March 29, 2007, 5:35 PM
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
 * @author yunny
 */
public class FrmPayGeneral extends FRMHandler implements I_FRMInterface, I_FRMType {

    private PayGeneral payGeneral;

    public static final String FRM_PAY_GENERAL = "FRM_PAY_GENERAL";

    public static final int FRM_FIELD_GEN_ID = 0;
    public static final int FRM_FIELD_COMPANY_NAME = 1;
    public static final int FRM_FIELD_COMP_ADDRESS = 2;
    public static final int FRM_FIELD_CITY = 3;
    public static final int FRM_FIELD_ZIP_CODE = 4;
    public static final int FRM_FIELD_BUSSINESS_TYPE = 5;
    public static final int FRM_FIELD_TAX_OFFICE = 6;
    public static final int FRM_FIELD_REG_TAX_NR = 7;
    public static final int FRM_FIELD_REG_TAX_BUS_NR = 8;
    public static final int FRM_FIELD_REG_TAX_DATE = 9;
    public static final int FRM_FIELD_TEL = 10;
    public static final int FRM_FIELD_FAX = 11;
    public static final int FRM_FIELD_LEADER_NAME = 12;
    public static final int FRM_FIELD_LEADER_POSITION = 13;
    public static final int FRM_FIELD_TAX_REP_LOCATION = 14;
    public static final int FRM_FIELD_TAX_YEAR = 15;
    public static final int FRM_FIELD_TAX_MONTH = 16;
    public static final int FRM_FIELD_TAX_REP_DATE = 17;
    public static final int FRM_FIELD_TAX_PAID_PCT = 18;
    public static final int FRM_FIELD_TAX_POS_COST_PCT = 19;
    public static final int FRM_FIELD_TAX_POS_COST_MAX = 20;
    public static final int FRM_FIELD_TAX_ROUND1000 = 21;
    public static final int FRM_FIELD_TAX_CALC_MTD = 22;
    public static final int FRM_FIELD_NON_TAX_INCOME = 23;
    public static final int FRM_FIELD_NON_TAX_WIFE = 24;
    public static final int FRM_FIELD_NON_TAX_DEPNT = 25;
    public static final int FRM_FIELD_TAX_FORM_SIGN_BY = 26;
    public static final int FRM_FIELD_LOCAL_CUR_CODE = 27;
    public static final int FRM_FIELD_WORK_DAYS = 28;

    public static String[] fieldNames = {
        "FRM_FIELD_GEN_ID",
        "FRM_FIELD_COMPANY_NAME",
        "FRM_FIELD_COMP_ADDRESS",
        "FRM_FIELD_CITY",
        "FRM_FIELD_ZIP_CODE",
        "FRM_FIELD_BUSSINESS_TYPE",
        "FRM_FIELD_TAX_OFFICE",
        "FRM_FIELD_REG_TAX_NR",
        "FRM_FIELD_REG_TAX_BUS_NR",
        "FRM_FIELD_REG_TAX_DATE",
        "FRM_FIELD_TEL",
        "FRM_FIELD_FAX",
        "FRM_FIELD_LEADER_NAME",
        "FRM_FIELD_LEADER_POSITION",
        "FRM_FIELD_TAX_REP_LOCATION",
        "FRM_FIELD_TAX_YEAR",
        "FRM_FIELD_TAX_MONTH",
        "FRM_FIELD_TAX_REP_DATE",
        "FRM_FIELD_TAX_PAID_PCT",
        "FRM_FIELD_TAX_POS_COST_PCT",
        "FRM_FIELD_TAX_POS_COST_MAX",
        "FRM_FIELD_TAX_ROUND1000",
        "FRM_FIELD_TAX_CALC_MTD",
        "FRM_FIELD_NON_TAX_INCOME",
        "FRM_FIELD_NON_TAX_WIFE",
        "FRM_FIELD_NON_TAX_DEPNT",
        "FRM_FIELD_TAX_FORM_SIGN_BY",
        "FRM_FIELD_LOCAL_CUR_CODE",
        "FRM_FIELD_WORK_DAYS",};

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_INT,};

    /**
     * Creates a new instance of FrmPayGeneral
     */
    public FrmPayGeneral() {
    }

    public FrmPayGeneral(PayGeneral payGeneral) {
        this.payGeneral = payGeneral;
    }

    public FrmPayGeneral(HttpServletRequest request, PayGeneral payGeneral) {
        super(new FrmPayGeneral(payGeneral), request);
        this.payGeneral = payGeneral;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getFormName() {

        return FRM_PAY_GENERAL;
    }

    public PayGeneral getEntityObject() {
        return payGeneral;
    }

    public void requestEntityObject(PayGeneral payGeneral) {
        try {
            this.requestParam();
            //employee.setPositionId(getLong(FRM_FIELD_POSITION_ID));
            payGeneral.setCompanyName(getString(FRM_FIELD_COMPANY_NAME));
            payGeneral.setCompAddress(getString(FRM_FIELD_COMP_ADDRESS));
            payGeneral.setCity(getString(FRM_FIELD_CITY));
            payGeneral.setZipCode(getString(FRM_FIELD_ZIP_CODE));
            payGeneral.setBussinessType(getString(FRM_FIELD_BUSSINESS_TYPE));
            payGeneral.setTaxOffice(getString(FRM_FIELD_TAX_OFFICE));
            payGeneral.setRegTaxNr(getString(FRM_FIELD_REG_TAX_NR));
            payGeneral.setRegTaxBusNr(getString(FRM_FIELD_REG_TAX_BUS_NR));
            payGeneral.setRegTaxDate(getDate(FRM_FIELD_REG_TAX_DATE));
            payGeneral.setTel(getString(FRM_FIELD_TEL));
            payGeneral.setFax(getString(FRM_FIELD_FAX));
            payGeneral.setLeaderName(getString(FRM_FIELD_LEADER_NAME));
            payGeneral.setLeaderPosition(getString(FRM_FIELD_LEADER_POSITION));
            payGeneral.setTaxRepLocation(getString(FRM_FIELD_TAX_REP_LOCATION));
            payGeneral.setTaxYear(getInt(FRM_FIELD_TAX_YEAR));
            payGeneral.setTaxMonth(getInt(FRM_FIELD_TAX_MONTH));
            payGeneral.setTaxRepDate(getInt(FRM_FIELD_TAX_REP_DATE));
            payGeneral.setTaxPaidPct(getDouble(FRM_FIELD_TAX_PAID_PCT));
            payGeneral.setTaxPosCostPct(getDouble(FRM_FIELD_TAX_POS_COST_PCT));
            payGeneral.setTaxPosCostMax(getDouble(FRM_FIELD_TAX_POS_COST_MAX));
            payGeneral.setTaxRound1000(getInt(FRM_FIELD_TAX_ROUND1000));
            payGeneral.setTaxCalcMtd(getInt(FRM_FIELD_TAX_CALC_MTD));
            payGeneral.setNonTaxIncome(getDouble(FRM_FIELD_NON_TAX_INCOME));
            payGeneral.setNonTaxWife(getInt(FRM_FIELD_NON_TAX_WIFE));
            payGeneral.setNonTaxDepnt(getInt(FRM_FIELD_NON_TAX_DEPNT));
            payGeneral.setTaxFormSignBy(getInt(FRM_FIELD_TAX_FORM_SIGN_BY));
            payGeneral.setLocalCurCode(getString(FRM_FIELD_LOCAL_CUR_CODE));
            payGeneral.setWorkDays(getInt(FRM_FIELD_WORK_DAYS));

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
