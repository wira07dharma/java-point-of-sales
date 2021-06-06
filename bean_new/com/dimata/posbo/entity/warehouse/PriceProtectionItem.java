/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class PriceProtectionItem extends Entity{
    private long PriceProtectionId;
    private String serialNumber;
    private double amount;
    private long supplierId;
    private long locationId;
    private long materialId;
    private double stockOnHand=0.0;
    private double totalAmount=0.0;
    
    /**
     * @return the PriceProtectionId
     */
    public long getPriceProtectionId() {
        return PriceProtectionId;
    }

    /**
     * @param PriceProtectionId the PriceProtectionId to set
     */
    public void setPriceProtectionId(long PriceProtectionId) {
        this.PriceProtectionId = PriceProtectionId;
    }

    /**
     * @return the serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the supplierId
     */
    public long getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the materialId
     */
    public long getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    /**
     * @return the stockOnHand
     */
    public double getStockOnHand() {
        return stockOnHand;
    }

    /**
     * @param stockOnHand the stockOnHand to set
     */
    public void setStockOnHand(double stockOnHand) {
        this.stockOnHand = stockOnHand;
    }

    /**
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    
}
