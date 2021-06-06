/*
 * FrmAccPayable.java
 *
 * Created on May 5, 2007, 12:31 PM
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
import com.dimata.posbo.entity.arap.AccPayable;
/**
 *
 * @author  gwawan
 */
public class FrmAccPayable extends FRMHandler implements I_FRMInterface, I_FRMType {
    private AccPayable accPayable;
    
    public static final String FRM_ACC_PAYABLE = "FRM_ACC_PAYABLE";
    public static final int FRM_ACC_PAYABLE_ID = 0;
    public static final int FRM_RECEIVE_MATERIAL_ID = 1;
    public static final int FRM_PAYMENT_DATE = 2;
    public static final int FRM_DESCRIPTION = 3;
    public static final int FRM_NUM_OF_PAYMENT = 4;
    
    public static final String[] fieldNames = {
        "FRM_ACC_PAYABLE_ID",
        "FRM_RECEIVE_MATERIAL_ID",
        "FRM_PAYMENT_DATE",
        "FRM_DESCRIPTION",
        "FRM_NUM_OF_PAYMENT"
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_DATE + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_INT
    };
    
    /** Creates a new instance of FrmAccPayable */
    public FrmAccPayable() {
    }
    
    public FrmAccPayable(AccPayable accPayable) {
        this.accPayable = accPayable;
    }
    
    public FrmAccPayable(HttpServletRequest request, AccPayable accPayable) {
        super(new FrmAccPayable(accPayable), request);
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
        return this.FRM_ACC_PAYABLE;
    }
    
    public AccPayable getEntityObject() {
        return this.accPayable;
    }
    
    public void requestEntityObject(AccPayable accPayable) {
        try {
            this.requestParam();
            accPayable.setOID(this.getLong(FRM_ACC_PAYABLE_ID));
            accPayable.setReceiveMaterialId(this.getLong(FRM_RECEIVE_MATERIAL_ID));
            accPayable.setPaymentDate(this.getDate(FRM_PAYMENT_DATE));
            accPayable.setDescription(this.getString(FRM_DESCRIPTION));
            accPayable.setNumOfPayment(this.getInt(FRM_NUM_OF_PAYMENT));
            this.accPayable = accPayable;
        } catch(Exception e) {
            System.out.println("Exc: "+e);
            accPayable = new AccPayable();
        }
    }
    
}
