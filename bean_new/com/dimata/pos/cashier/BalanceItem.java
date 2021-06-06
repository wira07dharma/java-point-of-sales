/*
 * BalanceItem.java
 *
 * Created on January 5, 2005, 2:57 PM
 */

package com.dimata.pos.cashier;

import com.dimata.pos.entity.payment.PstCashPayment;

/**
 *
 * @author  pulantara
 */

public class BalanceItem {
    
   
    /** payment type */
    private int paymentType = PstCashPayment.CASH;
    /** currency code */
    private long currencyID;
    /** amount in current currency used */
    private double amount = 0;
    /** total in IDR */
    private double totalIDR = 0;
    
    /** Holds value of property currencyCode. */
    private String currencyCode = "";

    /** Creates a new instance of BalanceItem */
    public BalanceItem() {
    }
    
    /**
     * Getter for property paymentType.
     * @return Value of property paymentType.
     */
    public int getPaymentType() {
        return paymentType;
    }
    
    /**
     * Setter for property paymentType.
     * @param paymentType New value of property paymentType.
     */
    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }
    
    /**
     * Getter for property totalIDR.
     * @return Value of property totalIDR.
     */
    public double getTotalIDR() {
        return totalIDR;
    }
    
    /**
     * Setter for property totalIDR.
     * @param totalIDR New value of property totalIDR.
     */
    public void setTotalIDR(double totalIDR) {
        this.totalIDR = totalIDR;
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
     * Getter for property currencyCode.
     * @return Value of property currencyCode.
     */
    public long getCurrencyID() {
        return currencyID;
    }
    
    /**
     * Setter for property currencyCode.
     * @param currencyCode New value of property currencyCode.
     */
    public void setCurrencyID(long currencyID) {
        this.currencyID = currencyID;
    }
    
    /** Getter for property currencyCode.
     * @return Value of property currencyCode.
     *
     */
    public String getCurrencyCode() {
        return this.currencyCode;
    }
    
    /** Setter for property currencyCode.
     * @param currencyCode New value of property currencyCode.
     *
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
   
    
}
