/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotiontype;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class MarketingPromotionType extends Entity {

    private int promotionType = 0;
    private String promotionTypeName = "";

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public String getPromotionTypeName() {
        return promotionTypeName;
    }

    public void setPromotionTypeName(String promotionTypeName) {
        this.promotionTypeName = promotionTypeName;
    }

}
