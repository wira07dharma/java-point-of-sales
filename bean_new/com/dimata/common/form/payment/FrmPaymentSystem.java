/*
 * Form Name  	:  FrmPaymentSystem.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 ******************************************************************
 */
package com.dimata.common.form.payment;

/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.common.entity.payment.PaymentSystem;

public class FrmPaymentSystem extends FRMHandler implements I_FRMInterface, I_FRMType {

    private PaymentSystem paymentSystem;

    public static final String FRM_NAME_PAYMENTSYSTEM = "FRM_NAME_PAYMENTSYSTEM";

    public static final int FRM_FIELD_PAYMENT_SYSTEM_ID = 0;
    public static final int FRM_FIELD_PAYMENT_SYSTEM = 1;
    public static final int FRM_FIELD_DESCRIPTION = 2;
    public static final int FRM_FIELD_BANK_INFO_OUT = 3;

    public static final int FRM_FIELD_CARD_INFO = 4;

    public static final int FRM_FIELD_DUE_DATE_INFO = 5;

    public static final int FRM_FIELD_DAYS = 6;

    public static final int FRM_FIELD_CHECK_BG_INFO = 7;

    public static final int FRM_FIELD_BANK_INFO_IN = 8;

    public static final int FRM_FIELD_BANK_NAME = 9;

    public static final int FRM_FIELD_BANK_ADDRESS = 10;

    public static final int FRM_FIELD_SWIFT_CODE = 11;

    public static final int FRM_FIELD_ACCOUNT_NAME = 12;

    public static final int FRM_FIELD_ACCOUNT_NUMBER = 13;

    public static final int FRM_FIELD_INFO_STATUS = 14;

    public static final int FRM_FIELD_FIXED = 15;

    public static final int FRM_FIELD_CLEARED_REF_ID = 16;

    //add Frm Payment Type
    public static final int FRM_FIELD_PAYMENT_TYPE = 17;

    //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
    public static final int FRM_FIELD_CHARGE_TO_CUSTOMER_PERCENT = 18;
    public static final int FRM_FIELD_BANK_COST_PERCENT = 19;

    public static final int FRM_FIELD_COSTING_TO = 20;

    //added by dewok 20190309 for payment type return
    public static final int FRM_FIELD_ENABLE_FOR_RETURN = 21;

    public static String[] fieldNames = {
        "FRM_FIELD_PAYMENT_SYSTEM_ID", "FRM_FIELD_PAYMENT_SYSTEM",
        "FRM_FIELD_DESCRIPTION", "FRM_FIELD_BANK_INFO_OUT",
        "FRM_FIELD_CARD_INFO", "FRM_FIELD_DUE_DATE_INFO",
        "FRM_FIELD_DAYS",
        "FRM_FIELD_CHECK_BG_INFO",
        "FRM_FIELD_BANK_INFO_IN",
        "FRM_FIELD_BANK_NAME",
        "FRM_FIELD_BANK_ADDRESS",
        "FRM_FIELD_SWIFT_CODE",
        "FRM_FIELD_ACCOUNT_NAME",
        "FRM_FIELD_ACCOUNT_NUMBER",
        "FRM_FIELD_INFO_STATUS",
        "FRM_FIELD_FIXED",
        "FRM_FIELD_CLEARED_REF_ID",
        //add Frm Payment Type
        "FRM_FIELD_PAYMENT_TYPE",
        //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
        "FRM_FIELD_CHARGE_TO_CUSTOMER_PERCENT",
        "FRM_FIELD_BANK_COST_PERCENT",
        "FRM_FIELD_COSTING_TO",
        "FRM_FIELD_ENABLE_FOR_RETURN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_STRING,
        TYPE_STRING, TYPE_BOOL,
        TYPE_BOOL, TYPE_BOOL,
        TYPE_INT,
        TYPE_BOOL,
        TYPE_BOOL,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT, TYPE_BOOL,
        TYPE_LONG,
        //add type payment type
        TYPE_INT,
        //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_INT
    };

    public FrmPaymentSystem() {
    }

    public FrmPaymentSystem(PaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public FrmPaymentSystem(HttpServletRequest request, PaymentSystem paymentSystem) {
        super(new FrmPaymentSystem(paymentSystem), request);
        this.paymentSystem = paymentSystem;
    }

    public String getFormName() {
        return FRM_NAME_PAYMENTSYSTEM;
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

    public PaymentSystem getEntityObject() {
        return paymentSystem;
    }

    public void requestEntityObject(PaymentSystem paymentSystem) {
        try {
            this.requestParam();
            paymentSystem.setPaymentSystem(getString(FRM_FIELD_PAYMENT_SYSTEM));
            paymentSystem.setDescription(getString(FRM_FIELD_DESCRIPTION));
            paymentSystem.setAccountName(getString(FRM_FIELD_ACCOUNT_NAME));
            paymentSystem.setAccountNumber(getString(FRM_FIELD_ACCOUNT_NUMBER));
            paymentSystem.setBankAddress(getString(FRM_FIELD_BANK_ADDRESS));
            paymentSystem.setBankInfoIn(getBoolean(FRM_FIELD_BANK_INFO_IN));
            paymentSystem.setBankInfoOut(getBoolean(FRM_FIELD_BANK_INFO_OUT));
            paymentSystem.setBankName(getString(FRM_FIELD_BANK_NAME));
            paymentSystem.setCardInfo(getBoolean(FRM_FIELD_CARD_INFO));
            paymentSystem.setCheckBGInfo(getBoolean(FRM_FIELD_CHECK_BG_INFO));
            paymentSystem.setDays(getInt(FRM_FIELD_DAYS));
            paymentSystem.setDueDateInfo(getBoolean(FRM_FIELD_DUE_DATE_INFO));
            paymentSystem.setSwiftCode(getString(FRM_FIELD_SWIFT_CODE));
            paymentSystem.setInfoStatus(getInt(FRM_FIELD_INFO_STATUS));
            paymentSystem.setFixed(getBoolean(FRM_FIELD_FIXED));
            paymentSystem.setClearedRefId(getLong(FRM_FIELD_CLEARED_REF_ID));
            //add payment type
            paymentSystem.setPaymentType(getInt(FRM_FIELD_PAYMENT_TYPE));
            //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
            paymentSystem.setChargeToCustomerPercent(getDouble(FRM_FIELD_CHARGE_TO_CUSTOMER_PERCENT));
            paymentSystem.setBankCostPercent(getDouble(FRM_FIELD_BANK_COST_PERCENT));
            //add opie-eyek 20150820
            paymentSystem.setCostingTo(getLong(FRM_FIELD_COSTING_TO));
            
            //added by dewok 20190309 for payment type return
            paymentSystem.setEnableForReturn(getInt(FRM_FIELD_ENABLE_FOR_RETURN));

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
