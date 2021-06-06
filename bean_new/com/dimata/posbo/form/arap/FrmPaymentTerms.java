/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.arap;
/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.arap.PaymentTerms;

/**
 *
 * @author Mirahu
 */
public class FrmPaymentTerms extends FRMHandler implements I_FRMInterface, I_FRMType{
    private PaymentTerms paymentTerms;

    public static final String FRM_PAYMENT_TERMS = "FRM_PAYMENT_TERMS";
    public static final int FRM_PAYMENT_TERMS_ID = 0;
    public static final int FRM_PURCHASE_ORDER_ID = 1;
    public static final int FRM_RECEIVE_MATERIAL_ID = 2;
    public static final int FRM_DUE_DATE = 3;
    public static final int FRM_PAYMENT_SYSTEM_ID = 4;
    public static final int FRM_CURRENCY_TYPE_ID = 5;
    public static final int FRM_RATE = 6;
    public static final int FRM_AMOUNT = 7;
    public static final int FRM_NOTE = 8;

    public static final String[] fieldNames = {
        "FRM_PAYMENT_TERMS_ID",
        "FRM_PURCHASE_ORDER_ID",
        "FRM_RECEIVE_MATERIAL_ID",
        "FRM_DUE_DATE",
        "FRM_PAYMENT_SYSTEM_ID",
        "FRM_CURRENCY_TYPE_ID",
        "FRM_RATE",
        "FRM_AMOUNT",
        "FRM_NOTE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING
    };

    /** Creates a new instance of FrmAccPayableDetail */
    public FrmPaymentTerms() {
    }

    public FrmPaymentTerms(PaymentTerms paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public FrmPaymentTerms(HttpServletRequest request, PaymentTerms paymentTerms) {
        super(new FrmPaymentTerms(paymentTerms), request);
    }

    public String[] getFieldNames() {
        return this.fieldNames;
    }

    public int getFieldSize() {
        return this.fieldNames.length;
    }

    public int[] getFieldTypes() {
        return this.fieldTypes;
    }

    public String getFormName() {
        return this.FRM_PAYMENT_TERMS;
    }

    public void requestEntityObject(PaymentTerms paymentTerms) {
        try {
            this.requestParam();
            paymentTerms.setOID(this.getLong(FRM_PAYMENT_TERMS_ID));
            paymentTerms.setPurchaseOrderId(this.getLong(FRM_PURCHASE_ORDER_ID));
            paymentTerms.setReceiveMaterialId(this.getLong(FRM_RECEIVE_MATERIAL_ID));
            paymentTerms.setDueDate(this.getDate(FRM_DUE_DATE));
            paymentTerms.setPaymentSystemId(this.getLong(FRM_PAYMENT_SYSTEM_ID));
            paymentTerms.setCurrencyTypeId(this.getLong(FRM_CURRENCY_TYPE_ID));
            paymentTerms.setRate(this.getDouble(FRM_RATE));
            paymentTerms.setAmount(this.getDouble(FRM_AMOUNT));
            paymentTerms.setNote(this.getString(FRM_NOTE));
            this.paymentTerms = paymentTerms;
        } catch(Exception e) {
            System.out.println("Exc: "+e);
            paymentTerms = new PaymentTerms();
        }
    }  
}
