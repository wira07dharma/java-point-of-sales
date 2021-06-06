/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotionpricetype;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class MarketingPromotionPriceType extends Entity {

    private long marketingPromotionId = 0;
    private long priceTypeId = 0;
    private String priceTypeName = "";

    public String getPriceTypeName() {
        return priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

    public long getMarketingPromotionId() {
        return marketingPromotionId;
    }

    public void setMarketingPromotionId(long marketingPromotionId) {
        this.marketingPromotionId = marketingPromotionId;
    }

    public long getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

}
