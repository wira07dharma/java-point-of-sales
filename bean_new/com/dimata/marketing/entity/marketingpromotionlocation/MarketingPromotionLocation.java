/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotionlocation;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class MarketingPromotionLocation extends Entity {

    private long marketingPromotionId = 0;
    private long locationId = 0;
    private String locationName = "";

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public long getMarketingPromotionId() {
        return marketingPromotionId;
    }

    public void setMarketingPromotionId(long marketingPromotionId) {
        this.marketingPromotionId = marketingPromotionId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

}
