/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotion;

import com.dimata.marketing.entity.marketingpromotion.MarketingPromotion;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMarketingPromotion extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingPromotion entMarketingPromotion;
    public static final String FRM_NAME_MARKETING_PROMOTION = "FRM_NAME_MARKETING_PROMOTION";
    public static final int FRM_FIELD_MARKETING_PROMOTION_ID = 0;
    public static final int FRM_FIELD_MARKETING_PROMOTION_PURPOSE = 1;
    public static final int FRM_FIELD_MARKETING_PROMOTION_OBJECTIVES = 2;
    public static final int FRM_FIELD_MARKETING_PROMOTION_EVENT = 3;
    public static final int FRM_FIELD_MARKETING_PROMOTION_START = 4;
    public static final int FRM_FIELD_MARKETING_PROMOTION_END = 5;
    public static final int FRM_FIELD_MARKETING_PROMOTION_RECURRING = 6;
    public static final int FRM_FIELD_MARKETING_PROMOTION_STANDARD_RATE_ID = 7;

    public static String[] fieldNames = {
        "FRM_FIELD_MARKETING_PROMOTION_ID",
        "FRM_FIELD_MARKETING_PROMOTION_PURPOSE",
        "FRM_FIELD_MARKETING_PROMOTION_OBJECTIVES",
        "FRM_FIELD_MARKETING_PROMOTION_EVENT",
        "FRM_FIELD_MARKETING_PROMOTION_START",
        "FRM_FIELD_MARKETING_PROMOTION_END",
        "FRM_FIELD_MARKETING_PROMOTION_RECURRING",
        "FRM_FIELD_MARKETING_PROMOTION_STANDARD_RATE_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG
    };

    public FrmMarketingPromotion() {
    }

    public FrmMarketingPromotion(MarketingPromotion entMarketingPromotion) {
        this.entMarketingPromotion = entMarketingPromotion;
    }

    public FrmMarketingPromotion(HttpServletRequest request, MarketingPromotion entMarketingPromotion) {
        super(new FrmMarketingPromotion(entMarketingPromotion), request);
        this.entMarketingPromotion = entMarketingPromotion;
    }

    public String getFormName() {
        return FRM_NAME_MARKETING_PROMOTION;
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

    public MarketingPromotion getEntityObject() {
        return entMarketingPromotion;
    }

    public void requestEntityObject(MarketingPromotion entMarketingPromotion) {
        try {
            this.requestParam();
//            entMarketingPromotion.setMarketingPromotionId(getLong(FRM_FIELD_MARKETING_PROMOTION_ID));
            entMarketingPromotion.setMarketingPromotionPurpose(Formater.formatDate(getString(FRM_FIELD_MARKETING_PROMOTION_PURPOSE),"yyyy-MM-dd"));
            entMarketingPromotion.setMarketingPromotionObjectives(getString(FRM_FIELD_MARKETING_PROMOTION_OBJECTIVES));
            entMarketingPromotion.setMarketingPromotionEvent(getString(FRM_FIELD_MARKETING_PROMOTION_EVENT));
            entMarketingPromotion.setMarketingPromotionStart(Formater.formatDate(getString(FRM_FIELD_MARKETING_PROMOTION_START),"yyyy-MM-dd HH:mm:ss"));
            entMarketingPromotion.setMarketingPromotionEnd(Formater.formatDate(getString(FRM_FIELD_MARKETING_PROMOTION_END),"yyyy-MM-dd HH:mm:ss"));
            entMarketingPromotion.setMarketingPromotionRecurring(getString(FRM_FIELD_MARKETING_PROMOTION_RECURRING));
            entMarketingPromotion.setMarketingPromotionStandardRateId(getLong(FRM_FIELD_MARKETING_PROMOTION_STANDARD_RATE_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
