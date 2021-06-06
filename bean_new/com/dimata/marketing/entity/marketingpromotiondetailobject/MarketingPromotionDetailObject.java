/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotiondetailobject;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class MarketingPromotionDetailObject extends Entity {

    private long marketingPromotionDetailId = 0;
    private long materialId = 0;
    private int quantity = 0;
    private long marketingPromotionTypeId = 0;
    private String validForMultiplication = "";
    private double regularPrice = 0;
    private double promotionPrice = 0;
    private double cost = 0;

    // field join
    private long itemId = 0;
    private String itemSku = "";
    private String itemBarcode = "";
    private String itemName = "";
    private String itemCategory = "";
    private String promoTypeName = "";

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getPromoTypeName() {
        return promoTypeName;
    }

    public void setPromoTypeName(String promoTypeName) {
        this.promoTypeName = promoTypeName;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public long getMarketingPromotionDetailId() {
        return marketingPromotionDetailId;
    }

    public void setMarketingPromotionDetailId(long marketingPromotionDetailId) {
        this.marketingPromotionDetailId = marketingPromotionDetailId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getMarketingPromotionTypeId() {
        return marketingPromotionTypeId;
    }

    public void setMarketingPromotionTypeId(long marketingPromotionTypeId) {
        this.marketingPromotionTypeId = marketingPromotionTypeId;
    }

    public String getValidForMultiplication() {
        return validForMultiplication;
    }

    public void setValidForMultiplication(String validForMultiplication) {
        this.validForMultiplication = validForMultiplication;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}
