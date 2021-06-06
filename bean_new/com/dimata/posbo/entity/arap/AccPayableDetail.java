/*
 * AccPayableDetail.java
 *
 * Created on May 4, 2007, 6:19 PM
 */

package com.dimata.posbo.entity.arap;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author  gwawan
 */
public class AccPayableDetail extends Entity {
    private long accPayableId = 0;
    private long paymentSystemId = 0;
    private long currencyTypeId = 0;
    private double rate = 0;
    private double amount = 0;
    
    /** Creates a new instance of AccPayableDetail */
    public AccPayableDetail() {
    }
    
    public long getAccPayableId() {
        return accPayableId;
    }
    
    public void setAccPayableId(long accPayableId) {
        this.accPayableId = accPayableId;
    }
    
    public long getPaymentSystemId() {
        return paymentSystemId;
    }
    
    public void setPaymentSystemId(long paymentSystemId) {
        this.paymentSystemId = paymentSystemId;
    }
    
    public long getCurrencyTypeId() {
        return currencyTypeId;
    }
    
    public void setCurrencyTypeId(long currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }
    
    public double getRate() {
        return rate;
    }
    
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}
