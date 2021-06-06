/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.PriceProtection;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dimata005
 */
public class FrmPriceProtection extends FRMHandler implements I_FRMInterface, I_FRMType {  
    
    PriceProtection priceProtection;
    
    public static String FRM_PRICE_PROTECTION = "FRM_PRICE_PROTECTION";
    public static int FRM_FLD_POS_PRICE_PROTECTION_ID = 0;
    public static int FRM_FLD_NOMOR_PRICE_PROTECTION = 1;
    public static int FRM_FLD_COUNTER = 2;
    public static int FRM_FLD_LOCATION_ID = 3;
    public static int FRM_FLD_CREATE_DATE=4;
    public static int FRM_FLD_APPROVAL_DATE=5;
    public static int FRM_FLD_TOTAL_AMOUNT=6;
    public static int FRM_FLD_EXCHANGE_RATE=7;
    public static int FRM_FLD_STATUS=8;
    public static int FRM_FLD_REMARK=9;
    
    public static String[] fieldNames= {
        "FRM_FLD_POS_PRICE_PROTECTION_ID",
        "FRM_FLD_NOMOR_PRICE_PROTECTION",
        "FRM_FLD_COUNTER",
        "FRM_FLD_LOCATION_ID",
        "FRM_FLD_CREATE_DATE",
        "FRM_FLD_APPROVAL_DATE",
        "FRM_FLD_TOTAL_AMOUNT",
        "FRM_FLD_EXCHANGE_RATE",
        "FRM_FLD_STATUS",
        "FRM_FLD_REMARK"
        
    };
    
    public static int fieldTypes[]={
        TYPE_LONG,//0
        TYPE_STRING, //1
        TYPE_INT, //2
        TYPE_LONG, //3
        TYPE_DATE, //4
        TYPE_DATE, //5
        TYPE_FLOAT, //6
        TYPE_FLOAT, //7
        TYPE_INT, //8
        TYPE_STRING //9
    };
    
    public void requestEntityObject(PriceProtection priceProtection){
        try{
            
            this.requestParam();
            //priceProtection.setNumberPP(getString(FRM_FLD_NOMOR_PRICE_PROTECTION));
            //priceProtection.setPpCounter(getInt(FRM_FLD_COUNTER));
            priceProtection.setLocationId(getLong(FRM_FLD_LOCATION_ID));
            priceProtection.setDateCreated(getDate(FRM_FLD_CREATE_DATE));
            priceProtection.setDateApproved(getDate(FRM_FLD_APPROVAL_DATE));
            priceProtection.setTotalAmount(getDouble(FRM_FLD_TOTAL_AMOUNT));
            priceProtection.setExchangeRate(getDouble(FRM_FLD_EXCHANGE_RATE));
            priceProtection.setStatus(getInt(FRM_FLD_STATUS));
            priceProtection.setRemark(getString(FRM_FLD_REMARK));
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Creates a new instance of FrmPriceProtection */
    public FrmPriceProtection() {
    }
    public FrmPriceProtection(PriceProtection priceProtection) {
        this.priceProtection = priceProtection;
    }
    
    public FrmPriceProtection(HttpServletRequest request, PriceProtection priceProtection) {
        super(new FrmPriceProtection(priceProtection),request);
        this.priceProtection = priceProtection;
    }
    
    public String[] getFieldNames() {
        return this.fieldNames;
        //throw new UnsupportedOperationException ();
    }
    
    public int getFieldSize() {
        return this.fieldNames.length;
        //throw new UnsupportedOperationException ();
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
        //throw new UnsupportedOperationException ();
    }
    
    public String getFormName() {
        return FRM_PRICE_PROTECTION;
        //throw new UnsupportedOperationException ();
    }
}
