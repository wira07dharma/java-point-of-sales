/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotionmembertype;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class MarketingPromotionMemberType extends Entity {

    private long marketingPromotionId = 0;
    private long memberTypeId = 0;
    private String memberTypeName = "";

    public String getMemberTypeName() {
        return memberTypeName;
    }

    public void setMemberTypeName(String memberTypeName) {
        this.memberTypeName = memberTypeName;
    }

    public long getMarketingPromotionId() {
        return marketingPromotionId;
    }

    public void setMarketingPromotionId(long marketingPromotionId) {
        this.marketingPromotionId = marketingPromotionId;
    }

    public long getMemberTypeId() {
        return memberTypeId;
    }

    public void setMemberTypeId(long memberTypeId) {
        this.memberTypeId = memberTypeId;
    }

}
