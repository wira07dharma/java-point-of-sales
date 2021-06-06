/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotion;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dewa
 */
public class MarketingPromotion extends Entity {

    private Date marketingPromotionPurpose = null;
    private String marketingPromotionObjectives = "";
    private String marketingPromotionEvent = "";
    private Date marketingPromotionStart = null;
    private Date marketingPromotionEnd = null;
    private String marketingPromotionRecurring = "";
    private long marketingPromotionStandardRateId = 0;
    private String currencyName = "";
    private String currencyCode = "";

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String standardRateName) {
        this.currencyName = standardRateName;
    }

    public long getMarketingPromotionStandardRateId() {
        return marketingPromotionStandardRateId;
    }

    public void setMarketingPromotionStandardRateId(long marketingPromotionStandardRateId) {
        this.marketingPromotionStandardRateId = marketingPromotionStandardRateId;
    }

    public Date getMarketingPromotionPurpose() {
        return marketingPromotionPurpose;
    }

    public void setMarketingPromotionPurpose(Date marketingPromotionPurpose) {
        this.marketingPromotionPurpose = marketingPromotionPurpose;
    }

    public String getMarketingPromotionObjectives() {
        return marketingPromotionObjectives;
    }

    public void setMarketingPromotionObjectives(String marketingPromotionObjectives) {
        this.marketingPromotionObjectives = marketingPromotionObjectives;
    }

    public String getMarketingPromotionEvent() {
        return marketingPromotionEvent;
    }

    public void setMarketingPromotionEvent(String marketingPromotionEvent) {
        this.marketingPromotionEvent = marketingPromotionEvent;
    }

    public Date getMarketingPromotionStart() {
        return marketingPromotionStart;
    }

    public void setMarketingPromotionStart(Date marketingPromotionStart) {
        this.marketingPromotionStart = marketingPromotionStart;
    }

    public Date getMarketingPromotionEnd() {
        return marketingPromotionEnd;
    }

    public void setMarketingPromotionEnd(Date marketingPromotionEnd) {
        this.marketingPromotionEnd = marketingPromotionEnd;
    }

    public String getMarketingPromotionRecurring() {
        return marketingPromotionRecurring;
    }

    public void setMarketingPromotionRecurring(String marketingPromotionRecurring) {
        this.marketingPromotionRecurring = marketingPromotionRecurring;
    }

}
