/*
 * FrmInvoiceDetail.java
 *
 * Created on November 12, 2007, 2:35 PM
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

public class FrmInvoiceDetail extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    public static final String FRM_INVOICE_DETAIL = "INVOICE_DETAIL";
    public static final int FRM_NAME_OF_PAX = 0;
    public static final int FRM_DESCRIPTION = 1;
    public static final int FRM_DATE = 2;
    public static final int FRM_NUMBER = 3;
    public static final int FRM_UNIT_PRICE = 4;
    public static final int FRM_ITEM_DISCOUNT = 5;
    public static final int FRM_INVOICE_ID = 6;
    public static final int FRM_ID_PERKIRAAN = 7;
    
    public static String[] fieldNames = {
        "NAME_OF_PAX",
        "DESCRIPTION",
        "DATE",
        "NUMBER",
        "UNIT_PRICE",
        "ITEM_DISCOUNT",
        "INVOICE_ID",
        "ID_PERKIRAAN"
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG
    };
    
    private InvoiceDetail objInvoiceDetail;
    
    /** Creates a new instance of FrmInvoiceDetail */
    public FrmInvoiceDetail(InvoiceDetail objInvoiceDetail) {
        this.objInvoiceDetail = objInvoiceDetail;
    }
    
     public FrmInvoiceDetail(HttpServletRequest request, InvoiceDetail objInvoiceDetail) {
        super(new FrmInvoiceDetail(objInvoiceDetail),request);
        this.objInvoiceDetail = objInvoiceDetail;
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
        return FRM_INVOICE_DETAIL;
    }
    
    public InvoiceDetail getEntityObject(){
        return objInvoiceDetail;
    }
    
    public void requestEntityObject(InvoiceDetail objInvoiceDetail){
        try{
            this.requestParam();
            objInvoiceDetail.setDate(this.getDate(FRM_DATE));
            objInvoiceDetail.setDescription(this.getString(FRM_DESCRIPTION));
            objInvoiceDetail.setIdPerkiraan(this.getLong(FRM_ID_PERKIRAAN));
            objInvoiceDetail.setInvoiceId(this.getLong(FRM_INVOICE_ID));
            objInvoiceDetail.setItemDiscount(this.getDouble(FRM_ITEM_DISCOUNT));
            objInvoiceDetail.setNameOfPax(this.getString(FRM_NAME_OF_PAX));
            objInvoiceDetail.setNumber(this.getInt(FRM_NUMBER));
            objInvoiceDetail.setUnitPrice(this.getDouble(FRM_UNIT_PRICE));
            this.objInvoiceDetail = objInvoiceDetail;
        }catch(Exception e){
            objInvoiceDetail = new InvoiceDetail();
            System.out.println("Exception on FrmInvoiceDetail.requestEntityObject() :::: "+e.toString());
        }
    }
}
