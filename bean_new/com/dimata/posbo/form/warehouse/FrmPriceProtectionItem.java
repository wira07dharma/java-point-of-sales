/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.PriceProtectionItem;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dimata005
 */
public class FrmPriceProtectionItem extends FRMHandler implements I_FRMInterface, I_FRMType {  
    
    PriceProtectionItem priceProtectionItem;
    
    public static String FRM_PRICE_PROTECTION_ITEM = "FRM_PRICE_PROTECTION_ITEM";
    
    public static int FRM_FLD_POS_PRICE_PROTECTION_ITEM_ID  = 0;
    public static int FRM_FLD_POS_PRICE_PROTECTION_ID = 1;
    public static int FRM_FLD_MATERIAL_ID = 2;
    public static int FRM_FLD_AMOUNT= 3;
    public static int FRM_FLD_QTY_ON_HAND= 4;
    public static int FRM_FLD_TOTAL_AMOUNT= 5;
    
    public static String[] fieldNames= {
        "FRM_FLD_POS_PRICE_PROTECTION_ITEM_ID",
        "FRM_FLD_POS_PRICE_PROTECTION_ID",
        "FRM_FLD_MATERIAL_ID",
        "FRM_FLD_AMOUNT",
        "FRM_FLD_QTY_ON_HAND",
        "FRM_FLD_TOTAL_AMOUNT_ITEM"
        
    };
    
    public static int fieldTypes[]={
        TYPE_LONG,//0
        TYPE_LONG, //1
        TYPE_LONG, //2
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public void requestEntityObject(PriceProtectionItem priceProtectionItem){
        try{
            
            this.requestParam();
            priceProtectionItem.setPriceProtectionId(getLong(FRM_FLD_POS_PRICE_PROTECTION_ID));
            priceProtectionItem.setMaterialId(getLong(FRM_FLD_MATERIAL_ID));
            priceProtectionItem.setAmount(getDouble(FRM_FLD_AMOUNT));
            priceProtectionItem.setStockOnHand(getDouble(FRM_FLD_QTY_ON_HAND));
            priceProtectionItem.setTotalAmount(getDouble(FRM_FLD_TOTAL_AMOUNT));
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Creates a new instance of FrmPriceProtection */
    public FrmPriceProtectionItem() {
    }
    public FrmPriceProtectionItem(PriceProtectionItem priceProtectionItem) {
        this.priceProtectionItem = priceProtectionItem;
    }
    
    public FrmPriceProtectionItem(HttpServletRequest request, PriceProtectionItem priceProtectionItem) {
        super(new FrmPriceProtectionItem(priceProtectionItem),request);
        this.priceProtectionItem = priceProtectionItem;
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
        return FRM_PRICE_PROTECTION_ITEM;
        //throw new UnsupportedOperationException ();
    }
}
