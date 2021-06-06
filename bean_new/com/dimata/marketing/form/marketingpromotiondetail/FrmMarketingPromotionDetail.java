/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.form.marketingpromotiondetail;

import com.dimata.marketing.entity.marketingpromotiondetail.MarketingPromotionDetail;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmMarketingPromotionDetail extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MarketingPromotionDetail entMarketingPromotionDetail;
    public static final String FRM_NAME_MARKETING_PROMOTION_DETAIL = "FRM_NAME_MARKETING_PROMOTION_DETAIL";
    public static final int FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID = 0;
    public static final int FRM_FIELD_MARKETING_PROMOTION_ID = 1;
    public static final int FRM_FIELD_PROMOTION_TYPE_ID = 2;
    public static final int FRM_FIELD_REASON_OF_PROMOTION = 3;
    public static final int FRM_FIELD_PROMOTION_TAGLINE = 4;
    public static final int FRM_FIELD_DISCOUNT_QUANTITY_STATUS = 5;
    public static final int FRM_FIELD_STATUS_APPROVE = 6;
    public static final int FRM_FIELD_SUBJECT_COMBINATION = 7;
    public static final int FRM_FIELD_OBJECT_COMBINATION = 8;

    public static String[] fieldNames = {
        "FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID",
        "FRM_FIELD_MARKETING_PROMOTION_ID",
        "FRM_FIELD_PROMOTION_TYPE_ID",
        "FRM_FIELD_REASON_OF_PROMOTION",
        "FRM_FIELD_PROMOTION_TAGLINE",
        "DISCOUNT_QUANTITY_STATUS",
        "STATUS_APPROVE",
        "SUBJECT_COMBINATION",
        "OBJECT_COMBINATION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

    public FrmMarketingPromotionDetail() {
    }

    public FrmMarketingPromotionDetail(MarketingPromotionDetail entMarketingPromotionDetail) {
        this.entMarketingPromotionDetail = entMarketingPromotionDetail;
    }

    public FrmMarketingPromotionDetail(HttpServletRequest request, MarketingPromotionDetail entMarketingPromotionDetail) {
        super(new FrmMarketingPromotionDetail(entMarketingPromotionDetail), request);
        this.entMarketingPromotionDetail = entMarketingPromotionDetail;
    }

    public String getFormName() {
        return FRM_NAME_MARKETING_PROMOTION_DETAIL;
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

    public MarketingPromotionDetail getEntityObject() {
        return entMarketingPromotionDetail;
    }

    public void requestEntityObject(MarketingPromotionDetail entMarketingPromotionDetail) {
        try {
            this.requestParam();
//            entMarketingPromotionDetail.setMarketingPromotionDetailId(getLong(FRM_FIELD_MARKETING_PROMOTION_DETAIL_ID));
            entMarketingPromotionDetail.setMarketingPromotionId(getLong(FRM_FIELD_MARKETING_PROMOTION_ID));
            entMarketingPromotionDetail.setPromotionTypeId(getLong(FRM_FIELD_PROMOTION_TYPE_ID));
            entMarketingPromotionDetail.setReasonOfPromotion(getString(FRM_FIELD_REASON_OF_PROMOTION));
            entMarketingPromotionDetail.setPromotionTagline(getString(FRM_FIELD_PROMOTION_TAGLINE));
            entMarketingPromotionDetail.setDiscountQuantityStatus(getString(FRM_FIELD_DISCOUNT_QUANTITY_STATUS));
            entMarketingPromotionDetail.setStatusApprove(getString(FRM_FIELD_STATUS_APPROVE));
            entMarketingPromotionDetail.setSubjectCombination(getString(FRM_FIELD_SUBJECT_COMBINATION));
            entMarketingPromotionDetail.setObjectCombination(getString(FRM_FIELD_OBJECT_COMBINATION));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
