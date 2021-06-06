/*
 * OtherCost.java
 *
 * Created on August 3, 2005, 4:23 PM
 */

package com.dimata.pos.entity.billing;

import com.dimata.qdep.entity.Entity;
/**
 *
 * @author  pulantara
 */
public class OtherCost extends Entity{
    
    private long billMainId = 0;
    private String name = "";
    private String description = "";
    private long currencyId = 0;
    private double rate = 0;
    private double amount = 0;
    
    /** for process only, not include in Pst */
    private int updateStatus = 0;
    
    /** Creates a new instance of OtherCost */
    public OtherCost() {
    }
    
    /**
     * Getter for property billMainId.
     * @return Value of property billMainId.
     */
    public long getBillMainId() {
        return billMainId;
    }
    
    /**
     * Setter for property billMainId.
     * @param billMainId New value of property billMainId.
     */
    public void setBillMainId(long billMainId) {
        this.billMainId = billMainId;
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /**
     * Getter for property currencyId.
     * @return Value of property currencyId.
     */
    public long getCurrencyId() {
        return currencyId;
    }
    
    /**
     * Setter for property currencyId.
     * @param currencyId New value of property currencyId.
     */
    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }
    
    /**
     * Getter for property rate.
     * @return Value of property rate.
     */
    public double getRate() {
        return rate;
    }
    
    /**
     * Setter for property rate.
     * @param rate New value of property rate.
     */
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    /**
     * Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /**
     * Getter for property updateStatus.
     * @return Value of property updateStatus.
     */
    public int getUpdateStatus() {
        return updateStatus;
    }
    
    /**
     * Setter for property updateStatus.
     * @param updateStatus New value of property updateStatus.
     */
    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }
    
}
