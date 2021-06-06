/*
 * FrmPaymentTerm.java
 *
 * Created on November 12, 2007, 2:38 PM
 */

package com.dimata.aiso.form.invoice;

/**
 *
 * @author  dwi
 */
import javax.servlet.http.*;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;

// import aiso
import com.dimata.aiso.entity.invoice.*;

public class FrmPaymentTerm extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    public static final String FRM_PAYMENT_TERM = "PAYMENT_TERM";
    public static final int FRM_PLAN_DATE = 0;
    public static final int FRM_AMOUNT = 1;
    public static final int FRM_NOTE = 2;
    public static final int FRM_TYPE = 3;
    public static final int FRM_INVOICE_ID = 4;
    
    public static String[] fieldNames = {
        "PLAN_DATE",
        "AMOUNT",
        "NOTE",
        "TYPE",
        "INVOICE_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG
    };
    
    private PaymentTerm objPaymentTerm;
    
    /** Creates a new instance of FrmPaymentTerm */
    public FrmPaymentTerm(PaymentTerm objPaymentTerm) {
        this.objPaymentTerm = objPaymentTerm;
    }
    
    public FrmPaymentTerm(HttpServletRequest request, PaymentTerm objPaymentTerm) {
        super(new FrmPaymentTerm(objPaymentTerm), request);
        this.objPaymentTerm = objPaymentTerm;
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
        return FRM_PAYMENT_TERM;
    }
    
    public PaymentTerm getEntityObject(){
        return objPaymentTerm;
    }
    
    public void requestEntityObject(PaymentTerm objPaymentTerm){
        try{
            this.requestParam();
            objPaymentTerm.setAmount(this.getDouble(FRM_AMOUNT));
            objPaymentTerm.setNote(this.getString(FRM_NOTE));
            objPaymentTerm.setPlanDate(this.getDate(FRM_PLAN_DATE));
            objPaymentTerm.setType(this.getInt(FRM_TYPE));
            objPaymentTerm.setInvoiceId(this.getLong(FRM_INVOICE_ID));
            this.objPaymentTerm = objPaymentTerm;
        }catch(Exception e){
            objPaymentTerm = new PaymentTerm();
            System.out.println("Exception on FrmPaymentTerm.requestEntityObject() ::: "+e.toString());
        }
    }
}
