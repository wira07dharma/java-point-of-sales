/*
 * FrmInvoiceAdjMain.java
 *
 * Created on November 12, 2007, 2:34 PM
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

public class FrmInvoiceAdjMain extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    public static final String FRM_INVOICE_ADJ_MAIN = "INVOICE_ADJ_MAIN";
    public static final int FRM_NOTE = 0;
    public static final int FRM_DATE = 1;
    public static final int FRM_STATUS = 2;
    public static final int FRM_INVOICE_ID = 3;
    public static final int FRM_REFERENCE = 4;
    public static final int FRM_TYPE = 5;
    
    public static String[] fieldNames = {
        "NOTE",
        "DATE",
        "STATUS",
        "INVOICE_ID",
        "REFERENCE",
        "TYPE"
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT
    };
    
    private InvoiceAdjMain objInvoiceAdjMain;
    
    /** Creates a new instance of FrmInvoiceAdjMain */
    public FrmInvoiceAdjMain(InvoiceAdjMain objInvoiceAdjMain) {
        this.objInvoiceAdjMain = objInvoiceAdjMain;
    }
    
     public FrmInvoiceAdjMain(HttpServletRequest request, InvoiceAdjMain objInvoiceAdjMain) {
        super(new FrmInvoiceAdjMain(objInvoiceAdjMain), request);
         this.objInvoiceAdjMain = objInvoiceAdjMain;
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
        return FRM_INVOICE_ADJ_MAIN;
    }
    
    public InvoiceAdjMain getEntityObject(){
        return objInvoiceAdjMain;
    }
    
    public void requestEntityObject(InvoiceAdjMain objInvoiceAdjMain){
        try{
            this.requestParam();
            objInvoiceAdjMain.setDate(this.getDate(FRM_DATE));
            objInvoiceAdjMain.setInvoiceId(this.getLong(FRM_INVOICE_ID));
            objInvoiceAdjMain.setNote(this.getString(FRM_NOTE));
            objInvoiceAdjMain.setReference(this.getString(FRM_REFERENCE));
            objInvoiceAdjMain.setStatus(this.getInt(FRM_STATUS));
            objInvoiceAdjMain.setType(this.getInt(FRM_TYPE));
            this.objInvoiceAdjMain = objInvoiceAdjMain;
        }catch(Exception e){
            objInvoiceAdjMain = new InvoiceAdjMain();
            System.out.println("Exception on FrmInvoiceAdjMain.requestEntityObject() ::: "+e.toString());
        }
    }
}
