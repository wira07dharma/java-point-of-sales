/*
 * FrmInvoiceAdjDetail.java
 *
 * Created on November 12, 2007, 2:32 PM
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

public class FrmInvoiceAdjDetail extends FRMHandler implements I_FRMInterface, I_FRMType{ 
    
    public static final String FRM_INVOICE_ADJ_DETAIL = "INVOICE_ADJ_DETAIL";
    public static final int FRM_NAME_OF_PAX = 0;
    public static final int FRM_DESCRIPTION = 1;
    public static final int FRM_DATE = 2;
    public static final int FRM_NUMBER = 3;
    public static final int FRM_UNIT_PRICE = 4;
    public static final int FRM_ITEM_DISCOUNT = 5;
    public static final int FRM_ADJUSTMENT_ID = 6;
    public static final int FRM_ID_PERKIRAAN = 7;
    
    public static String[] fieldNames = {
        "NAME_OF_PAX",
        "DESCRIPTION",
        "DATE",
        "NUMBER",
        "UNIT_PRICE",
        "ITEM_DISCOUNT",
        "ADJUSTMENT_ID",
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
    
    private InvoiceAdjDetail objInvoiceAdjDetail;
    
    /** Creates a new instance of FrmInvoiceAdjDetail */
    public FrmInvoiceAdjDetail(InvoiceAdjDetail objInvoiceAdjDetail) {
        this.objInvoiceAdjDetail = objInvoiceAdjDetail;
    }
    
    public FrmInvoiceAdjDetail(HttpServletRequest request, InvoiceAdjDetail objInvoiceAdjDetail) {
        super(new FrmInvoiceAdjDetail(objInvoiceAdjDetail), request);
        this.objInvoiceAdjDetail = objInvoiceAdjDetail;
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
        return FRM_INVOICE_ADJ_DETAIL;
    }
    
    public InvoiceAdjDetail getEntityObject(){
        return objInvoiceAdjDetail;
    }
    
    public void requestEntityObject(InvoiceAdjDetail objInvoiceAdjDetail){
        try{
            this.requestParam();
            objInvoiceAdjDetail.setAdjustmentId(this.getLong(FRM_ADJUSTMENT_ID));
            objInvoiceAdjDetail.setDate(this.getDate(FRM_DATE));
            objInvoiceAdjDetail.setDescription(this.getString(FRM_DESCRIPTION));
            objInvoiceAdjDetail.setIdPerkiraan(this.getLong(FRM_ID_PERKIRAAN));
            objInvoiceAdjDetail.setItemDiscount(this.getDouble(FRM_ITEM_DISCOUNT));
            objInvoiceAdjDetail.setNameOfPax(this.getString(FRM_NAME_OF_PAX));
            objInvoiceAdjDetail.setNumber(this.getInt(FRM_NUMBER));
            objInvoiceAdjDetail.setUnitPrice(this.getDouble(FRM_UNIT_PRICE));
            this.objInvoiceAdjDetail = objInvoiceAdjDetail;
        }catch(Exception e){
            objInvoiceAdjDetail = new InvoiceAdjDetail();
            System.out.println("Exception on FrmInvoiceAdjDetail.requestEntityObject() ::: "+e.toString());
        }
    }
}

