/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.pos.form.payment;

/**
 *
 * @author Wiweka
 * 20130711
 */
import com.dimata.pos.form.billing.*;
import com.dimata.pos.form.payment.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// qdep package
import com.dimata.qdep.form.*;

// project package
import com.dimata.pos.entity.masterCashier.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;

public class FrmCashCreditPaymentMain extends FRMHandler implements I_FRMInterface, I_FRMType {

    private CreditPaymentMain creditPaymentMain;
    public static final String FRM_NAME_CREDIT_PAYMENT_MAIN = "FRM_NAME_CREDIT_PAYMENT_MAIN";
    public static final int FRM_FIELD_CREDIT_PAYMENT_MAIN_ID = 0;
    public static final int FRM_FIELD_BILL_MAIN_ID = 1;
    public static final int FRM_FIELD_CASH_CASHIER_ID = 2;
    public static final int FRM_FIELD_LOCATION_ID = 3;
    public static final int FRM_FIELD_BILL_DATE = 4;
    public static final int FRM_FIELD_APPUSER_ID = 5;
    public static final int FRM_FIELD_SHIFT_ID = 6;
    public static final int FRM_FIELD_BILL_STATUS = 7;
    public static final int FRM_FIELD_SALES_CODE = 8;
    public static final int FRM_FIELD_INVOICE_NUMBER = 9;
    public static final int FRM_FIELD_INVOICE_COUNTER = 10;
    public static final int FRM_FIELD_DOC_TYPE = 11;

    //untuk retur payment
    public static final int FRM_FIELD_AMOUNT_RETURN_CREDIT = 12;
    public static final int FRM_FIELD_PAY_AMOUNT_CREDIT = 13;
    public static final int FRM_FIELD_APP_USER_SALES_ID = 14;
    
    public static String[] fieldNames = {
        "FRM_FIELD_CREDIT_PAYMENT_MAIN_ID",
        "FRM_FIELD_BILL_MAIN_ID",
        "FRM_FIELD_CASH_CASHIER_ID ",
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_BILL_DATE",
        "FRM_FIELD_APPUSER_ID",
        "FRM_FIELD_SHIFT_ID",
        "FRM_FIELD_BILL_STATUS",
        "FRM_FIELD_SALES_CODE",
        "FRM_FIELD_INVOICE_NUMBER",
        "FRM_FIELD_INVOICE_COUNTER",
        "FRM_FIELD_DOC_TYPE",
        "FRM_FIELD_AMOUNT_RETURN_CREDIT",
        "FRM_FIELD_PAY_AMOUNT_CREDIT",
        "FRM_FIELD_APP_USER_SALES_ID"

    };
    public static int[] fieldTypes = {
        TYPE_LONG, //creditpaymentmain
        TYPE_LONG, //billmainId
        TYPE_LONG, //cashierId
        TYPE_LONG, //locationId
        TYPE_DATE, //billdate
        TYPE_LONG, //appuserId
        TYPE_LONG, //shiftId
        TYPE_INT, //billstatus
        TYPE_STRING, //salesCode

        TYPE_STRING, //invNumbre
        TYPE_INT, //invCounter
        TYPE_INT, //docType

        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG
    };

    public FrmCashCreditPaymentMain() {
    }

    public FrmCashCreditPaymentMain(CreditPaymentMain creditPaymentMain) {
        this.creditPaymentMain = creditPaymentMain;
    }

    public FrmCashCreditPaymentMain(HttpServletRequest request, CreditPaymentMain creditPaymentMain) {
        super(new FrmCashCreditPaymentMain(creditPaymentMain), request);
        this.creditPaymentMain = creditPaymentMain;
    }

    public String getFormName() {
        return FRM_NAME_CREDIT_PAYMENT_MAIN;
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

    public CreditPaymentMain getEntityObject() {
        return creditPaymentMain;
    }

    public void requestEntityObject(CreditPaymentMain creditPaymentMain) {
        try {
            this.requestParam();
            //creditPaymentMain.setOID(getLong(FRM_FIELD_CASH_BILL_MAIN_ID));
            creditPaymentMain.setCashCashierId(getLong(FRM_FIELD_CASH_CASHIER_ID));
            creditPaymentMain.setBillStatus(getInt(FRM_FIELD_BILL_STATUS));
            creditPaymentMain.setBillMainId(getLong(FRM_FIELD_BILL_MAIN_ID));
            creditPaymentMain.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            creditPaymentMain.setBillDate(getDate(FRM_FIELD_BILL_DATE));
            creditPaymentMain.setShiftId(getLong(FRM_FIELD_SHIFT_ID));
            creditPaymentMain.setAppUserId(getLong(FRM_FIELD_APPUSER_ID));
            creditPaymentMain.setDocType(getInt(FRM_FIELD_DOC_TYPE));
            creditPaymentMain.setInvoiceCounter(getInt(FRM_FIELD_INVOICE_COUNTER));
            creditPaymentMain.setInvoiceNumber(getString(FRM_FIELD_INVOICE_NUMBER));
            creditPaymentMain.setSalesCode(getString(FRM_FIELD_SALES_CODE));

            creditPaymentMain.setAmountReturn(getDouble(FRM_FIELD_AMOUNT_RETURN_CREDIT));
            creditPaymentMain.setPayAmountCredit(getDouble(FRM_FIELD_PAY_AMOUNT_CREDIT));
            creditPaymentMain.setAppUserSalesId(getLong(FRM_FIELD_APP_USER_SALES_ID));

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
