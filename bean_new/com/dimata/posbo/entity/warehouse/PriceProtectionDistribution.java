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
public class PriceProtectionDistribution extends Entity{
    
     private long PriceProtectionId;
     private long SupplierId;
     private double AmountIssue;
     private double ExchangeRate;
     private int Status;
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
     * @return the SupplierId
     */
    public long getSupplierId() {
        return SupplierId;
    }

    /**
     * @param SupplierId the SupplierId to set
     */
    public void setSupplierId(long SupplierId) {
        this.SupplierId = SupplierId;
    }

    /**
     * @return the AmountIssue
     */
    public double getAmountIssue() {
        return AmountIssue;
    }

    /**
     * @param AmountIssue the AmountIssue to set
     */
    public void setAmountIssue(double AmountIssue) {
        this.AmountIssue = AmountIssue;
    }

    /**
     * @return the ExchangeRate
     */
    public double getExchangeRate() {
        return ExchangeRate;
    }

    /**
     * @param ExchangeRate the ExchangeRate to set
     */
    public void setExchangeRate(double ExchangeRate) {
        this.ExchangeRate = ExchangeRate;
    }

    /**
     * @return the Status
     */
    public int getStatus() {
        return Status;
    }

    /**
     * @param Status the Status to set
     */
    public void setStatus(int Status) {
        this.Status = Status;
    }
     
}
 