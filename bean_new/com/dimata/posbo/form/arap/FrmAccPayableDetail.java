/*
 * FrmAccPayableDetail.java
 *
 * Created on May 5, 2007, 12:32 PM
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
import com.dimata.posbo.entity.arap.AccPayableDetail;
/**
 *
 * @author  gwawan
 */
public class FrmAccPayableDetail extends FRMHandler implements I_FRMInterface, I_FRMType {
    private AccPayableDetail accPayableDetail;
    
    public static final String FRM_ACC_PAYABLE_DETAIL = "FRM_ACC_PAYABLE_DETAIL";
    public static final int FRM_ACC_PAYABLE_DETAIL_ID = 0;
    public static final int FRM_ACC_PAYABLE_ID = 1;
    public static final int FRM_PAYMENT_SYSTEM_ID = 2;
    public static final int FRM_CURRENCY_TYPE_ID = 3;
    public static final int FRM_RATE = 4;
    public static final int FRM_AMOUNT = 5;
    
    public static final String[] fieldNames = {
        "FRM_ACC_PAYABLE_DETAIL_ID",
        "FRM_ACC_PAYABLE_ID",
        "FRM_PAYMENT_SYSTEM_ID",
        "FRM_CURRENCY_TYPE_ID",
        "FRM_RATE",
        "FRM_AMOUNT"
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    /** Creates a new instance of FrmAccPayableDetail */
    public FrmAccPayableDetail() {
    }
    
    public FrmAccPayableDetail(AccPayableDetail accPayableDetail) {
        this.accPayableDetail = accPayableDetail;
    }
    
    public FrmAccPayableDetail(HttpServletRequest request, AccPayableDetail accPayableDetail) {
        super(new FrmAccPayableDetail(accPayableDetail), request);
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
        return this.FRM_ACC_PAYABLE_DETAIL;
    }
    
    public void requestEntityObject(AccPayableDetail accPayableDetail) {
        try {
            this.requestParam();
            accPayableDetail.setOID(this.getLong(FRM_ACC_PAYABLE_DETAIL_ID));
            accPayableDetail.setAccPayableId(this.getLong(FRM_ACC_PAYABLE_ID));
            accPayableDetail.setPaymentSystemId(this.getLong(FRM_PAYMENT_SYSTEM_ID));
            accPayableDetail.setCurrencyTypeId(this.getLong(FRM_CURRENCY_TYPE_ID));
            accPayableDetail.setRate(this.getDouble(FRM_RATE));
            accPayableDetail.setAmount(this.getDouble(FRM_AMOUNT));
            this.accPayableDetail = accPayableDetail;
        } catch(Exception e) {
            System.out.println("Exc: "+e);
            accPayableDetail = new AccPayableDetail();
        }
    }
    
}
