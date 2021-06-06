/*
 * FrmCostingAdjustment.java
 *
 * Created on November 12, 2007, 2:29 PM
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

public class FrmCostingAdjustment extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    public static final String FRM_COSTING_ADJUSTMENT = "COSTING_ADJUSTMENT";
    
    public static final int FRM_DESCRIPTION = 0;
    public static final int FRM_NUMBER = 1;
    public static final int FRM_UNIT_PRICE = 2;
    public static final int FRM_DISCOUNT = 3;
    public static final int FRM_STATUS = 4;
    public static final int FRM_CONTACT_ID = 5;
    public static final int FRM_REFERENCE = 6;
    public static final int FRM_COSTING_ID = 7;
    
    public static String[] fieldNames = {
        "DESCRIPTION",
        "NUMBER",
        "UNIT_PRICE",
        "DISCOUNT",
        "STATUS",
        "CONTACT_ID",
        "REFERENCE",
        "COSTING_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG
    };
    
    private CostingAdjustment objCostingAdjustment;
    
    /** Creates a new instance of FrmCostingAdjustment */
    public FrmCostingAdjustment(CostingAdjustment objCostingAdjustment) {
        this.objCostingAdjustment = objCostingAdjustment;
    }
    
    public FrmCostingAdjustment(HttpServletRequest request, CostingAdjustment objCostingAdjustment) {
        super(new FrmCostingAdjustment(objCostingAdjustment),request);
        this.objCostingAdjustment = objCostingAdjustment;
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
        return FRM_COSTING_ADJUSTMENT;
    }
    
    public CostingAdjustment getEntityObject(){
        return objCostingAdjustment;
    }
    
    public void requestEntityObject(CostingAdjustment objCostingAdjustment){
        try{
            this.requestParam();
            objCostingAdjustment.setContactId(this.getLong(FRM_CONTACT_ID));
            objCostingAdjustment.setCostingId(this.getLong(FRM_COSTING_ID));
            objCostingAdjustment.setDescription(this.getString(FRM_DESCRIPTION));
            objCostingAdjustment.setDiscount(this.getDouble(FRM_DISCOUNT));
            objCostingAdjustment.setNumber(this.getInt(FRM_NUMBER));
            objCostingAdjustment.setReference(this.getString(FRM_REFERENCE));
            objCostingAdjustment.setStatus(this.getInt(FRM_STATUS));
            objCostingAdjustment.setUnitPrice(this.getDouble(FRM_UNIT_PRICE));
            this.objCostingAdjustment = objCostingAdjustment;
        }catch(Exception e){
            objCostingAdjustment = new CostingAdjustment();
            System.out.println("Exception on FrmCostingAdjustment.requestEntityObject() ::: "+e.toString());
        }
    }
}

