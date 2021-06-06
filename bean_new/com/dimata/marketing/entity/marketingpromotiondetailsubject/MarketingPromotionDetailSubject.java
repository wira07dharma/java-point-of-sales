/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.entity.marketingpromotiondetailsubject;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class MarketingPromotionDetailSubject extends Entity {

    private long marketingPromotionDetailId = 0;
    private long materialId = 0;
    private int quantity = 0;
    private String validForMultiplication = "";
    private double salesPrice = 0;
    private double purchasePrice = 0;
    private double grossProfit = 0;
    private int targetQuantity = 0;

    // field join
    private long itemId = 0;
    private String itemSku = "";
    private String itemBarcode = "";
    private String itemName = "";
    private String itemCategory = "";

    public int getTargetQuantity() {
        return targetQuantity;
    }

    public void setTargetQuantity(int targetQuantity) {
        this.targetQuantity = targetQuantity;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
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

    public String getValidForMultiplication() {
        return validForMultiplication;
    }

    public void setValidForMultiplication(String validForMultiplication) {
        this.validForMultiplication = validForMultiplication;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(double grossProfit) {
        this.grossProfit = grossProfit;
    }

}
