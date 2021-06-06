/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.pos.entity.payment;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author dimata005
 */
public class CashCreditPaymentsDinamis extends Entity{
    private long creditMainId;
    private long paymentType;
    private long creditCardId=0;
    private long currencyId;
    private double rate;
    private double amount;
    private Date payDateTime = new Date();
    private int updateStatus=0;
    private int paymentStatus;

    public CashCreditPaymentsDinamis() {
    }

    /**
     * @return the creditMainId
     */
    public long getCreditMainId() {
        return creditMainId;
    }

    /**
     * @param creditMainId the creditMainId to set
     */
    public void setCreditMainId(long creditMainId) {
        this.creditMainId = creditMainId;
    }

    /**
     * @return the paymentType
     */
    public long getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(long paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the creditCardId
     */
    public long getCreditCardId() {
        return creditCardId;
    }

    /**
     * @param creditCardId the creditCardId to set
     */
    public void setCreditCardId(long creditCardId) {
        this.creditCardId = creditCardId;
    }

    /**
     * @return the currencyId
     */
    public long getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId the currencyId to set
     */
    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return the rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(double rate) {
        this.rate = rate;
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
     * @return the payDateTime
     */
    public Date getPayDateTime() {
        return payDateTime;
    }

    /**
     * @param payDateTime the payDateTime to set
     */
    public void setPayDateTime(Date payDateTime) {
        this.payDateTime = payDateTime;
    }

    /**
     * @return the updateStatus
     */
    public int getUpdateStatus() {
        return updateStatus;
    }

    /**
     * @param updateStatus the updateStatus to set
     */
    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }

    /**
     * @return the paymentStatus
     */
    public int getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus the paymentStatus to set
     */
    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    
}
