/*
 * FrmCosting.java
 *
 * Created on November 12, 2007, 2:28 PM
 */

package com.dimata.aiso.form.costing;

/**
 *
 * @author  dwi
 */
import javax.servlet.http.*;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;

// import aiso
import com.dimata.aiso.entity.costing.*;

public class FrmCosting extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    public static final String FRM_COSTING = "COSTING";
    
    public static final int FRM_DESCRIPTION = 0;
    public static final int FRM_NUMBER = 1;
    public static final int FRM_UNIT_PRICE = 2;
    public static final int FRM_DISCOUNT = 3;
    public static final int FRM_STATUS = 4;
    public static final int FRM_INVOICE_ID = 5;
    public static final int FRM_ID_PERKIRAAN_HPP = 6;
    public static final int FRM_ID_PERKIRAAN_HUTANG = 7;
    public static final int FRM_REFERENCE = 8;
    public static final int FRM_CONTACT_ID = 9;
    
    public static String[] fieldNames = {
        "DESCRIPTION",
        "NUMBER",
        "UNIT_PRICE",
        "DISCOUNT",
        "STATUS",
        "INVOICE_ID",
        "ID_PERKIRAAN_HPP",
        "ID_PERKIRAAN_HUTANG",
        "REFERENCE",
        "CONTACT_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG
    };
    
    private Costing objCosting;
    
    /** Creates a new instance of FrmCosting */
    public FrmCosting(Costing objCosting) {
        this.objCosting = objCosting;
    }
    
    public FrmCosting(HttpServletRequest request, Costing objCosting) {
        super(new FrmCosting(objCosting),request);
        this.objCosting = objCosting;
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
        return FRM_COSTING;
    }
    
    public Costing getEntityObject(){
        return objCosting;
    }
    
    public void requestEntityObject(Costing objCosting){
        try{
            this.requestParam();
            objCosting.setContactId(this.getLong(FRM_CONTACT_ID));
            objCosting.setDescription(this.getString(FRM_DESCRIPTION));
            objCosting.setDiscount(this.getDouble(FRM_DISCOUNT));
            objCosting.setIdPerkiraanHpp(this.getLong(FRM_ID_PERKIRAAN_HPP));
            objCosting.setIdPerkiraanHutang(this.getLong(FRM_ID_PERKIRAAN_HUTANG));
            objCosting.setInvoiceId(this.getLong(FRM_INVOICE_ID));
            objCosting.setNumber(this.getInt(FRM_NUMBER));
            objCosting.setReference(this.getString(FRM_REFERENCE));
            objCosting.setStatus(this.getInt(FRM_STATUS));
            objCosting.setUnitPrice(this.getDouble(FRM_UNIT_PRICE));
            this.objCosting = objCosting;
        }catch(Exception e){
            objCosting = new Costing();
            System.out.println("Exception on FrmCosting.requestEntityObject() ::: "+e.toString());
        }
    }
}
