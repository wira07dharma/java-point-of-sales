/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotiondetailsubject;

import com.dimata.marketing.entity.marketingpromotiondetailsubject.MarketingPromotionDetailSubject;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMarketingPromotionDetailSubject extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingPromotionDetailSubject entMarketingPromotionDetailSubject;
    public static final String FRM_NAME_MARKETING_PROMOTION_DETAIL_SUBJECT = "FRM_NAME_MARKETING_PROMOTION_DETAIL_SUBJECT";
    public static final int FRM_FIELD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID = 0;
    public static final int FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID = 1;
    public static final int FRM_FIELD_MATERIAL_ID = 2;
    public static final int FRM_FIELD_QUANTITY = 3;
    public static final int FRM_FIELD_VALID_FOR_MULTIPLICATION = 4;
    public static final int FRM_FIELD_SALES_PRICE = 5;
    public static final int FRM_FIELD_PURCHASE_PRICE = 6;
    public static final int FRM_FIELD_GROSS_PROFIT = 7;
    public static final int FRM_FIELD_TARGET_QUANTITY = 8;

    public static String[] fieldNames = {
        "FRM_FIELD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID",
        "FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID",
        "FRM_FIELD_MATERIAL_ID",
        "FRM_FIELD_QUANTITY",
        "FRM_FIELD_VALID_FOR_MULTIPLICATION",
        "FRM_FIELD_SALES_PRICE",
        "FRM_FIELD_PURCHASE_PRICE",
        "FRM_FIELD_GROSS_PROFIT",
        "FRM_FIELD_TARGET_QUANTITY"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT
    };

    public FrmMarketingPromotionDetailSubject() {
    }

    public FrmMarketingPromotionDetailSubject(MarketingPromotionDetailSubject entMarketingPromotionDetailSubject) {
        this.entMarketingPromotionDetailSubject = entMarketingPromotionDetailSubject;
    }

    public FrmMarketingPromotionDetailSubject(HttpServletRequest request, MarketingPromotionDetailSubject entMarketingPromotionDetailSubject) {
        super(new FrmMarketingPromotionDetailSubject(entMarketingPromotionDetailSubject), request);
        this.entMarketingPromotionDetailSubject = entMarketingPromotionDetailSubject;
    }

    public String getFormName() {
        return FRM_NAME_MARKETING_PROMOTION_DETAIL_SUBJECT;
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

    public MarketingPromotionDetailSubject getEntityObject() {
        return entMarketingPromotionDetailSubject;
    }

    public void requestEntityObject(MarketingPromotionDetailSubject entMarketingPromotionDetailSubject) {
        try {
            this.requestParam();
//            entMarketingPromotionDetailSubject.setMarketingPromotionDetailSubjectId(getLong(FRM_FIELD_MARKETING_PROMOTION_DETAIL_SUBJECT_ID));
            entMarketingPromotionDetailSubject.setMarketingPromotionDetailId(getLong(FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID));
            entMarketingPromotionDetailSubject.setMaterialId(getLong(FRM_FIELD_MATERIAL_ID));
            entMarketingPromotionDetailSubject.setQuantity(getInt(FRM_FIELD_QUANTITY));
            entMarketingPromotionDetailSubject.setValidForMultiplication(getString(FRM_FIELD_VALID_FOR_MULTIPLICATION));
            entMarketingPromotionDetailSubject.setSalesPrice(getFloat(FRM_FIELD_SALES_PRICE));
            entMarketingPromotionDetailSubject.setPurchasePrice(getFloat(FRM_FIELD_PURCHASE_PRICE));
            entMarketingPromotionDetailSubject.setGrossProfit(getFloat(FRM_FIELD_GROSS_PROFIT));
            entMarketingPromotionDetailSubject.setTargetQuantity(getInt(FRM_FIELD_TARGET_QUANTITY));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
