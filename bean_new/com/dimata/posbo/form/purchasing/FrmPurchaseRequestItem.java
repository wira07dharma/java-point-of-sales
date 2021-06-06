/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.purchasing;

import com.dimata.posbo.entity.purchasing.PurchaseRequestItem;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dimata005
 */
public class FrmPurchaseRequestItem extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    private PurchaseRequestItem prItem;
    
    public static final String FRM_NAME_PURCHASE_ORDER_ITEM = "FRM_NAME_PURCHASE_REQUEST_ITEM";

    public static final int FRM_FIELD_PURCHASE_REQUEST_ITEM_ID = 0;
    public static final int FRM_FIELD_PURCHASE_REQUEST_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_UNIT_ID = 3;
    public static final int FRM_FIELD_QUANTITY = 4;
    public static final int FRM_FIELD_SUPPLIER_ID = 5;
    public static final int FRM_FIELD_PRICE_BUYING = 6;
    public static final int FRM_FIELD_UNIT_REQUEST_ID= 7;
    public static final int FRM_FIELD_SUPPLIER_NAME = 8;
    public static final int FRM_FIELD_TOTAL_PRICE = 9;
    public static final int FRM_FIELD_TERMS = 10;
    
    public static String[] fieldNames = {
        "FRM_FIELD_PURCHASE_REQUEST_ITEM_ID",
        "FRM_FIELD_PURCHASE_REQUEST_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_UNIT_ID",
        "FRM_FIELD_QUANTITY",
        "FRM_FIELD_SUPPLIER_ID",
        "FRM_FIELD_PRICE_BUYING",
        "FRM_FIELD_UNIT_REQUEST_ID",
        "FRM_FIELD_SUPPLIER_NAME",
        "FRM_FIELD_TOTAL_PRICE",
        "FRM_FIELD_TERMS"
    };


    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT
    };

    public FrmPurchaseRequestItem() {
    }

    public FrmPurchaseRequestItem(PurchaseRequestItem prItem) {
        this.prItem = prItem;
    }
    
    public FrmPurchaseRequestItem(HttpServletRequest request, PurchaseRequestItem prItem) {
        super(new FrmPurchaseRequestItem(prItem), request);
        this.prItem = prItem;
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

    public PurchaseRequestItem getEntityObject() {
        return prItem;
    }

    public void requestEntityObject(PurchaseRequestItem poItem) {
        try {
            this.requestParam();
            poItem.setPurchaseOrderId(getLong(FRM_FIELD_PURCHASE_REQUEST_ID));
            poItem.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            poItem.setUnitId(getLong(FRM_FIELD_UNIT_ID));
            poItem.setQuantity(getDouble(FRM_FIELD_QUANTITY));
            poItem.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
            poItem.setPriceBuying(getDouble(FRM_FIELD_PRICE_BUYING));
            poItem.setUnitRequestId(getLong(FRM_FIELD_UNIT_REQUEST_ID));
            poItem.setSupplierName(getString(FRM_FIELD_SUPPLIER_NAME));
            poItem.setTotalPrice(getDouble(FRM_FIELD_TOTAL_PRICE));
            poItem.setTermPurchaseRequest(getInt(FRM_FIELD_TERMS));
        } catch (Exception e) {
            System.out.println("!!!!!Error on requestEntityObject : " + e.toString());
        }
    }

}
