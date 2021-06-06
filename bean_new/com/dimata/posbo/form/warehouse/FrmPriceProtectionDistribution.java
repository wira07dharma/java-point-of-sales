/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.PriceProtectionDistribution;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dimata005
 */
public class FrmPriceProtectionDistribution extends FRMHandler implements I_FRMInterface, I_FRMType {  
    
    PriceProtectionDistribution priceProtectionDistribution;
    
    public static String FRM_PRICE_PROTECTION_DISTRIBUTION = "FRM_PRICE_PROTECTION_DISTRIBUTION";
    
    public static int FRM_FLD_POS_PRICE_DISTRIBUTION_ITEM_ID  = 0;
    public static int FRM_FLD_POS_PRICE_PROTECTION_ID = 1;
    public static int FRM_FLD_SUPPLIER_ID = 2;
    public static int FRM_FLD_AMOUNT_ISSUE= 3;
    public static int FRM_FLD_EXCHANGE_RATE= 4;
    public static int FRM_FLD_STATUS= 5;
    
    public static String[] fieldNames= {
        "FRM_FLD_POS_PRICE_DISTRIBUTION_ITEM_ID",
        "FRM_FLD_POS_PRICE_PROTECTION_ID",
        "FRM_FLD_SUPPLIER_ID",
        "FRM_FLD_AMOUNT_ISSUE",
        "FRM_FLD_EXCHANGE_RATE",
        "FRM_FLD_STATUS"
        
    };
    
    public static int fieldTypes[]={
        TYPE_LONG,//0
        TYPE_LONG, //1
        TYPE_LONG, //2
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT
    };
    
    public void requestEntityObject(PriceProtectionDistribution priceProtectionDistribution){
        try{
            
            this.requestParam();
            priceProtectionDistribution.setPriceProtectionId(getLong(FRM_FLD_POS_PRICE_PROTECTION_ID));
            priceProtectionDistribution.setSupplierId(getLong(FRM_FLD_SUPPLIER_ID));
            priceProtectionDistribution.setAmountIssue(getDouble(FRM_FLD_AMOUNT_ISSUE));
            priceProtectionDistribution.setExchangeRate(getDouble(FRM_FLD_EXCHANGE_RATE));
            priceProtectionDistribution.setStatus(getInt(FRM_FLD_STATUS));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Creates a new instance of FrmPriceProtection */
    public FrmPriceProtectionDistribution() {
    }
    public FrmPriceProtectionDistribution(PriceProtectionDistribution priceProtectionDistribution) {
        this.priceProtectionDistribution = priceProtectionDistribution;
    }
    
    public FrmPriceProtectionDistribution(HttpServletRequest request, PriceProtectionDistribution priceProtectionDistribution) {
        super(new FrmPriceProtectionDistribution(priceProtectionDistribution),request);
        this.priceProtectionDistribution = priceProtectionDistribution;
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
        return FRM_PRICE_PROTECTION_DISTRIBUTION;
        //throw new UnsupportedOperationException ();
    }
}
