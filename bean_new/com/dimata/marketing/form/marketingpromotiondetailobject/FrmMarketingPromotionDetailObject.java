/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotiondetailobject;

import com.dimata.marketing.entity.marketingpromotiondetailobject.MarketingPromotionDetailObject;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMarketingPromotionDetailObject extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingPromotionDetailObject entMarketingPromotionDetailObject;
    public static final String FRM_NAME_MARKETING_PROMOTION_DETAIL_OBJECT = "FRM_NAME_MARKETING_PROMOTION_DETAIL_OBJECT";
    public static final int FRM_FIELD_MARKETING_PROMOTION_DETAIL_OBJECT_ID = 0;
    public static final int FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_QUANTITY = 3;
    public static final int FRM_FIELD_MARKETING_PROMOTION_TYPE_ID = 4;
    public static final int FRM_FIELD_VALID_FOR_MULTIPLICATION = 5;
    public static final int FRM_FIELD_REGULAR_PRICE = 6;
    public static final int FRM_FIELD_PROMOTION_PRICE = 7;
    public static final int FRM_FIELD_COST = 8;

    public static String[] fieldNames = {
        "FRM_FIELD_MARKETING_PROMOTION_DETAIL_OBJECT_ID",
        "FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_QUANTITY",
        "FRM_FIELD_MARKETING_PROMOTION_TYPE_ID",
        "FRM_FIELD_VALID_FOR_MULTIPLICATION",
        "FRM_FIELD_REGULAR_PRICE",
        "FRM_FIELD_PROMOTION_PRICE",
        "FRM_FIELD_COST"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public FrmMarketingPromotionDetailObject() {
    }

    public FrmMarketingPromotionDetailObject(MarketingPromotionDetailObject entMarketingPromotionDetailObject) {
        this.entMarketingPromotionDetailObject = entMarketingPromotionDetailObject;
    }

    public FrmMarketingPromotionDetailObject(HttpServletRequest request, MarketingPromotionDetailObject entMarketingPromotionDetailObject) {
        super(new FrmMarketingPromotionDetailObject(entMarketingPromotionDetailObject), request);
        this.entMarketingPromotionDetailObject = entMarketingPromotionDetailObject;
    }

    public String getFormName() {
        return FRM_NAME_MARKETING_PROMOTION_DETAIL_OBJECT;
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

    public MarketingPromotionDetailObject getEntityObject() {
        return entMarketingPromotionDetailObject;
    }

    public void requestEntityObject(MarketingPromotionDetailObject entMarketingPromotionDetailObject) {
        try {
            this.requestParam();
//            entMarketingPromotionDetailObject.setMarketingPromotionDetailObjectId(getLong(FRM_FIELD_MARKETING_PROMOTION_DETAIL_OBJECT_ID));
            entMarketingPromotionDetailObject.setMarketingPromotionDetailId(getLong(FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID));
            entMarketingPromotionDetailObject.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entMarketingPromotionDetailObject.setQuantity(getInt(FRM_FIELD_QUANTITY));
            entMarketingPromotionDetailObject.setMarketingPromotionTypeId(getLong(FRM_FIELD_MARKETING_PROMOTION_TYPE_ID));
            entMarketingPromotionDetailObject.setValidForMultiplication(getString(FRM_FIELD_VALID_FOR_MULTIPLICATION));
            entMarketingPromotionDetailObject.setRegularPrice(getFloat(FRM_FIELD_REGULAR_PRICE));
            entMarketingPromotionDetailObject.setPromotionPrice(getFloat(FRM_FIELD_PROMOTION_PRICE));
            entMarketingPromotionDetailObject.setCost(getFloat(FRM_FIELD_COST));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
