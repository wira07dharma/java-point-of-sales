/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotiondetailassign;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class MarketingPromotionDetailAssign extends Entity {

    private long marketingPromotionDetailSubjectId = 0;
    private long marketingPromotionDetailObjectId = 0;

    public long getMarketingPromotionDetailSubjectId() {
        return marketingPromotionDetailSubjectId;
    }

    public void setMarketingPromotionDetailSubjectId(long marketingPromotionDetailSubjectId) {
        this.marketingPromotionDetailSubjectId = marketingPromotionDetailSubjectId;
    }

    public long getMarketingPromotionDetailObjectId() {
        return marketingPromotionDetailObjectId;
    }

    public void setMarketingPromotionDetailObjectId(long marketingPromotionDetailObjectId) {
        this.marketingPromotionDetailObjectId = marketingPromotionDetailObjectId;
    }

}
