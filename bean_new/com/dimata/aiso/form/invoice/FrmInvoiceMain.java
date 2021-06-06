/*
 * FrmInvoiceMain.java
 *
 * Created on November 12, 2007, 2:37 PM
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

public class FrmInvoiceMain extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    public static final String FRM_INVOICE_MAIN = "INVOICE_MAIN";
    public static final int FRM_ISSUED_DATE = 0;
    public static final int FRM_CHECKIN_DATE = 1;
    public static final int FRM_CHECKOUT_DATE = 2;
    public static final int FRM_STATUS = 3;
    public static final int FRM_TOTAL_PAX = 4;
    public static final int FRM_TOTAL_ROOM = 5;
    public static final int FRM_FIRST_CONTACT_ID = 6;
    public static final int FRM_SECOND_CONTACT_ID = 7;
    public static final int FRM_GUEST_NAME = 8;
    public static final int FRM_HOTEL_NAME = 9;
    public static final int FRM_ID_PERKIRAAN = 10;
    public static final int FRM_TERM_OF_PAYMENT = 11;
    public static final int FRM_TYPE = 12;
    public static final int FRM_TOTAL_DISCOUNT = 13;
    public static final int FRM_TAX = 14;    
    public static final int FRM_DEPOSIT_TIME_LIMIT = 15;   
    public static final int FRM_DESCRIPTION = 16;
    
    public static String[] fieldNames = {
        "ISSUED_DATE",
        "CHECKIN_DATE",
        "CHECKOUT_DATE",
        "STATUS",
        "TOTAL_PAX",
        "TOTAL_ROOM",
        "FIRST_CONTACT_ID",
        "SECOND_CONTACT_ID",
        "GUEST_NAME",
        "HOTEL_NAME",
        "ID_PERKIRAAN",
        "TERM_OF_PAYMENT",
        "TYPE",
        "TOTAL_DISCOUNT",
        "TAX",
	"DEPOSIT_TIME_LIMIT",
	"DESCRIPTION"
    };
    
    public static int[] fieldTypes = {
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_STRING
    };
    
    private InvoiceMain objInvoiceMain;
    
    /** Creates a new instance of FrmInvoiceMain */
    public FrmInvoiceMain(InvoiceMain objInvoiceMain) {
        this.objInvoiceMain = objInvoiceMain;
    }
    
     public FrmInvoiceMain(HttpServletRequest request, InvoiceMain objInvoiceMain) {
        super(new FrmInvoiceMain(objInvoiceMain),request); 
        this.objInvoiceMain = objInvoiceMain;
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
        return FRM_INVOICE_MAIN;
    }
    
    public InvoiceMain getEntityObject(){
        return objInvoiceMain;
    }
    
    public void requestEntityObject(InvoiceMain objInvoiceMain){
        try{
            this.requestParam();
            objInvoiceMain.setIssuedDate(this.getDate(FRM_ISSUED_DATE));
            objInvoiceMain.setCheckInDate(this.getDate(FRM_CHECKIN_DATE));
            objInvoiceMain.setCheckOutDate(this.getDate(FRM_CHECKOUT_DATE));
            objInvoiceMain.setFirstContactId(this.getLong(FRM_FIRST_CONTACT_ID));
            objInvoiceMain.setSecondContactId(this.getLong(FRM_SECOND_CONTACT_ID));
            objInvoiceMain.setGuestName(this.getString(FRM_GUEST_NAME));
            objInvoiceMain.setHotelName(this.getString(FRM_HOTEL_NAME));
            objInvoiceMain.setIdPerkiraan(this.getLong(FRM_ID_PERKIRAAN));
            objInvoiceMain.setStatus(this.getInt(FRM_STATUS));
            objInvoiceMain.setTax(this.getDouble(FRM_TAX));
            objInvoiceMain.setTermOfPayment(this.getInt(FRM_TERM_OF_PAYMENT));
            objInvoiceMain.setTotalDiscount(this.getDouble(FRM_TOTAL_DISCOUNT));
            objInvoiceMain.setTotalPax(this.getInt(FRM_TOTAL_PAX));
            objInvoiceMain.setTotalRoom(this.getInt(FRM_TOTAL_ROOM));
            objInvoiceMain.setType(this.getInt(FRM_TYPE));
	    objInvoiceMain.setDepositTimeLimit(this.getDate(FRM_DEPOSIT_TIME_LIMIT));
	    objInvoiceMain.setDescription(this.getString(FRM_DESCRIPTION));
            this.objInvoiceMain = objInvoiceMain;
	    
        }catch(Exception e){
            objInvoiceMain = new InvoiceMain();
            System.out.println("Exception on FrmInvoiceMain.requestEntityObject() ::: "+e.toString());
        }
    }
}
