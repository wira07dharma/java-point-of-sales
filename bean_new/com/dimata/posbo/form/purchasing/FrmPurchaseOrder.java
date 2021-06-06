/*
 * Form Name  	:  FrmOrderMaterial.java
 * Created on 	:  11:00 PM
 *
 * @author  	:
 * @version  	:  01
 */

package com.dimata.posbo.form.purchasing;

import javax.servlet.http.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.purchasing.*;

public class FrmPurchaseOrder extends FRMHandler implements I_FRMInterface, I_FRMType {

    private PurchaseOrder po;

    public static final String FRM_NAME_PURCHASE_ORDER = "FRM_NAME_PURCHASE_ORDER";
    public static final int FRM_FIELD_PURCHASE_ORDER_ID = 0;
    public static final int FRM_FIELD_LOCATION_ID = 1;
    public static final int FRM_FIELD_LOCATION_TYPE = 2;
    public static final int FRM_FIELD_PO_CODE = 3;
    public static final int FRM_FIELD_PO_CODE_COUNTER = 4;
    public static final int FRM_FIELD_PURCH_DATE = 5;
    public static final int FRM_FIELD_SUPPLIER_ID = 6;
    public static final int FRM_FIELD_PO_STATUS = 7;
    public static final int FRM_FIELD_REMARK = 8;
    public static final int FRM_FIELD_TERM_OF_PAYMENT = 9;
    public static final int FRM_FIELD_CREDIT_TIME = 10;
    public static final int FRM_FIELD_PPN = 11;
    public static final int FRM_FIELD_CURRENCY_ID = 12;
    public static final int FRM_FIELD_CODE_REVISI = 13;

    //new include ppn
    public static final int FRM_FIELD_INCLUDE_PPN = 14;

    //add opie-eyek 20130812
    public static final int FRM_FIELD_INCLUDE_PRINT_PRICE = 15;
    
    public static final int FRM_FIELD_EXCHANGE_RATE = 16;

    //add opie add category
    public static final int FRM_FIELD_CATEGORY_ID = 17;
    
    
    public static final int FRM_FIELD_PURCH_DATE_REQUEST =18;
    
    public static String[] fieldNames =
            {
                "FRM_FIELD_PURCHASE_ORDER_ID",
                "FRM_FIELD_LOCATION_ID",
                "FRM_FIELD_LOCATION_TYPE",
                "FRM_FIELD_PO_CODE",
                "FRM_FIELD_PO_CODE_COUNTER",
                "FRM_FIELD_PURCH_DATE",
                "FRM_FIELD_SUPPLIER_ID",
                "FRM_FIELD_PO_STATUS",
                "FRM_FIELD_REMARK",
                "FRM_FIELD_TERM_OF_PAYMENT",
                "FRM_FIELD_CREDIT_TIME",
                "FRM_FIELD_PPN",
                "FRM_FIELD_CURRENCY_ID",
                "FRM_FIELD_CODE_REVISI",
                //new include ppn
                "FRM_FIELD_INCLUDE_PPN",
                //add opie-eyek 20130812
                "FRM_FIELD_INCLUDE_PRINT_PRICE",
                "FRM_FIELD_EXCHANGE_RATE",
                "FRM_FIELD_CATEGORY_ID",
                "FRM_FIELD_PURCH_DATE_REQUEST"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_STRING,
                TYPE_INT,
                TYPE_DATE,
                TYPE_LONG,
                TYPE_INT,
                TYPE_STRING,
                TYPE_INT,
                TYPE_INT,
                TYPE_FLOAT,
                TYPE_LONG,
                TYPE_STRING,
                //new include ppn
                TYPE_INT,
                TYPE_INT,
                TYPE_FLOAT,
                //add opie-category
                TYPE_LONG,
                TYPE_DATE
            };

    public FrmPurchaseOrder() {
    }

    public FrmPurchaseOrder(PurchaseOrder po) {
        this.po = po;
    }

    public FrmPurchaseOrder(HttpServletRequest request, PurchaseOrder po) {
        super(new FrmPurchaseOrder(po), request);
        this.po = po;
    }

    public String getFormName() {
        return FRM_NAME_PURCHASE_ORDER;
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

    public PurchaseOrder getEntityObject() {
        return po;
    }

    public void requestEntityObject(PurchaseOrder po) {
        try {
            this.requestParam();
            po.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            po.setLocationType(getInt(FRM_FIELD_LOCATION_TYPE));
            po.setPurchDate(getDate(FRM_FIELD_PURCH_DATE));
            po.setPoCode(getString(FRM_FIELD_PO_CODE));
            po.setSupplierId(getLong(FRM_FIELD_SUPPLIER_ID));
            po.setPoStatus(getInt(FRM_FIELD_PO_STATUS));
            po.setRemark(getString(FRM_FIELD_REMARK));

            po.setTermOfPayment(getInt(FRM_FIELD_TERM_OF_PAYMENT));
            po.setCreditTime(getInt(FRM_FIELD_CREDIT_TIME));
            po.setPpn(getDouble(FRM_FIELD_PPN));
            po.setCurrencyId(getLong(FRM_FIELD_CURRENCY_ID));
            po.setCodeRevisi(getString(FRM_FIELD_CODE_REVISI));
            //new include ppn
            po.setIncludePpn(getInt(FRM_FIELD_INCLUDE_PPN));
            //include print price
            po.setIncludeprintprice(getInt(FRM_FIELD_INCLUDE_PRINT_PRICE));
            //
            po.setExchangeRate(getDouble(FRM_FIELD_EXCHANGE_RATE));
            //add category id
            po.setCategoryId(getLong(FRM_FIELD_CATEGORY_ID));
            
            po.setPurchDateRequest(getDate(FRM_FIELD_PURCH_DATE_REQUEST));
            
        } catch (Exception e) {
            System.out.println("FrmOrderMaterial.requestEntityObject err : " + e.toString());
        }
    }
}
