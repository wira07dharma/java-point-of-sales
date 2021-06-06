/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotiondetail;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class MarketingPromotionDetail extends Entity {

    private long marketingPromotionId = 0;
    private long promotionTypeId = 0;
    private String reasonOfPromotion = "";
    private String promotionTagline = "";
    private String promotionName = "";
    private String discountQuantityStatus = "";
    private String statusApprove = "";
    private String subjectCombination = "";
    private String objectCombination = "";

    public String getSubjectCombination() {
        return subjectCombination;
    }

    public void setSubjectCombination(String subjectCombination) {
        this.subjectCombination = subjectCombination;
    }

    public String getObjectCombination() {
        return objectCombination;
    }

    public void setObjectCombination(String objectCombination) {
        this.objectCombination = objectCombination;
    }

    public String getDiscountQuantityStatus() {
        return discountQuantityStatus;
    }

    public void setDiscountQuantityStatus(String discountQuantityStatus) {
        this.discountQuantityStatus = discountQuantityStatus;
    }

    public String getStatusApprove() {
        return statusApprove;
    }

    public void setStatusApprove(String statusApprove) {
        this.statusApprove = statusApprove;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public long getMarketingPromotionId() {
        return marketingPromotionId;
    }

    public void setMarketingPromotionId(long marketingPromotionId) {
        this.marketingPromotionId = marketingPromotionId;
    }

    public long getPromotionTypeId() {
        return promotionTypeId;
    }

    public void setPromotionTypeId(long promotionTypeId) {
        this.promotionTypeId = promotionTypeId;
    }

    public String getReasonOfPromotion() {
        return reasonOfPromotion;
    }

    public void setReasonOfPromotion(String reasonOfPromotion) {
        this.reasonOfPromotion = reasonOfPromotion;
    }

    public String getPromotionTagline() {
        return promotionTagline;
    }

    public void setPromotionTagline(String promotionTagline) {
        this.promotionTagline = promotionTagline;
    }

}
