package com.dimata.posbo.form.purchasing;

/* java package */

import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.purchasing.*;

public class FrmPurchaseOrderItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    private PurchaseOrderItem poItem;
    
    public static final String FRM_NAME_PURCHASE_ORDER_ITEM = "FRM_NAME_PURCHASE_ORDER_ITEM";
    
    public static final int FRM_FIELD_PURCHASE_ORDER_ITEM_ID = 0;
    public static final int FRM_FIELD_PURCHASE_ORDER_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_UNIT_ID = 3;
    public static final int FRM_FIELD_PRICE = 4;
    public static final int FRM_FIELD_CURRENCY_ID = 5;
    public static final int FRM_FIELD_QUANTITY = 6;
    public static final int FRM_FIELD_DISCOUNT = 7;
    public static final int FRM_FIELD_TOTAL = 8;
    public static final int FRM_FIELD_ORG_BUYING_PRICE = 9;
    public static final int FRM_FIELD_DISCOUNT2 = 10;
    public static final int FRM_FIELD_DISCOUNT_NOMINAL = 11;
    public static final int FRM_FIELD_CURR_BUYING_PRICE = 12;
    public static final int FRM_FIELD_DISCOUNT1 = 13;
    
    public static final int FRM_FIELD_QTY_INPUT = 14;
    public static final int FRM_FIELD_UNIT_ID_KONVERSI = 15;
    public static final int FRM_FIELD_PRICE_KONVERSI = 16;
    public static final int FRM_FIELD_BONUS=17;
    public static final int FRM_FIELD_QUANTITY_STOCK = 18;
    
    public static String[] fieldNames = {
        "FRM_FIELD_PURCHASE_ORDER_ITEM_ID",
        "FRM_FIELD_PURCHASE_ORDER_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_PRICE",
        "FRM_FIELD_CURRENCY_ID",
        "FRM_FIELD_QUANTITY",
        "FRM_FIELD_DISCOUNT",
        "FRM_FIELD_TOTAL",
        "FRM_FIELD_ORG_BUYING_PRICE",
        "FRM_FIELD_DISCOUNT2",
        "FRM_FIELD_DISCOUNT_NOMINAL",
        "FRM_FIELD_CURR_BUYING_PRICE",
        "FRM_FIELD_DISCOUNT1",
        "FRM_FIELD_QTY_INPUT",
        "FRM_FIELD_UNIT_ID_KONVERSI",
        "FRM_FIELD_PRICE_KONVERSI",
        "FRM_FIELD_BONUS",
        "FRM_FIELD_QUANTITY_STOCK"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_FLOAT,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT
    };
    
    public FrmPurchaseOrderItem() {
    }
    
    public FrmPurchaseOrderItem(PurchaseOrderItem poItem) {
        this.poItem = poItem;
    }
    
    public FrmPurchaseOrderItem(HttpServletRequest request, PurchaseOrderItem poItem) {
        super(new FrmPurchaseOrderItem(poItem), request);
        this.poItem = poItem;
    }
    
    public String getFormName() {
        return FRM_NAME_PURCHASE_ORDER_ITEM;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public PurchaseOrderItem getEntityObject() {
        return poItem;
    }
    
    public void requestEntityObject(PurchaseOrderItem poItem) {
        try {
            this.requestParam();
            poItem.setPurchaseOrderId(getLong(FRM_FIELD_PURCHASE_ORDER_ID));
            poItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            poItem.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            poItem.setPrice(getDouble(FRM_FIELD_PRICE));
            poItem.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
            poItem.setQuantity(getDouble(FRM_FIELD_QUANTITY));
            poItem.setDiscount(getDouble(FRM_FIELD_DISCOUNT));
            poItem.setTotal(getDouble(FRM_FIELD_TOTAL));
            poItem.setOrgBuyingPrice(getDouble(FRM_FIELD_ORG_BUYING_PRICE));
            poItem.setDiscount2(getDouble(FRM_FIELD_DISCOUNT2));
            poItem.setDiscNominal(getDouble(FRM_FIELD_DISCOUNT_NOMINAL));
            poItem.setCurBuyingPrice(getDouble(FRM_FIELD_CURR_BUYING_PRICE));
            poItem.setDiscount1(getDouble(FRM_FIELD_DISCOUNT1));
            
            //add opie-eyek 20140320
            poItem.setQtyRequest(getDouble(FRM_FIELD_QTY_INPUT));
            poItem.setUnitRequestId(getLong(FRM_FIELD_UNIT_ID_KONVERSI));
            poItem.setPriceKonv(getDouble(FRM_FIELD_PRICE_KONVERSI));
            
            poItem.setBonus(getInt(FRM_FIELD_BONUS));
            poItem.setQtyInputStock(getDouble(FRM_FIELD_QUANTITY_STOCK));
            
        } catch (Exception e) {
            System.out.println("!!!!!Error on requestEntityObject : " + e.toString());
        }
    }
    
    
    public void requestEntityObjectSR(PurchaseOrderItem poItem) {
        try {
            this.requestParam();
            poItem.setPurchaseOrderId(getLong(FRM_FIELD_PURCHASE_ORDER_ID));
            poItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            poItem.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            poItem.setPrice(getDouble(FRM_FIELD_PRICE));
            poItem.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
            poItem.setQuantity(getDouble(FRM_FIELD_QUANTITY));
            poItem.setDiscount(getDouble(FRM_FIELD_DISCOUNT));
            poItem.setTotal(getDouble(FRM_FIELD_TOTAL));
            poItem.setOrgBuyingPrice(getDouble(FRM_FIELD_ORG_BUYING_PRICE));
            poItem.setDiscount2(getDouble(FRM_FIELD_DISCOUNT2));
            poItem.setDiscNominal(getDouble(FRM_FIELD_DISCOUNT_NOMINAL));
            poItem.setCurBuyingPrice(getDouble(FRM_FIELD_CURR_BUYING_PRICE));
            poItem.setDiscount1(getDouble(FRM_FIELD_DISCOUNT1));
            
            //add opie-eyek 20140320
            poItem.setQtyRequest(getDouble(FRM_FIELD_QUANTITY));
            poItem.setUnitRequestId(getLong(FRM_FIELD_UNIT_ID));
            poItem.setPriceKonv(getDouble(FRM_FIELD_PRICE_KONVERSI));
            
            poItem.setBonus(getInt(FRM_FIELD_BONUS));
            poItem.setQtyInputStock(getDouble(FRM_FIELD_QUANTITY_STOCK));
            
        } catch (Exception e) {
            System.out.println("!!!!!Error on requestEntityObject : " + e.toString());
        }
    }
}
