/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.arap;

import java.util.Date;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author roy ajus
 */
public class PaymentTerms extends Entity {
    private long purchaseOrderId = 0;
    private long receiveMaterialId = 0;
    private Date dueDate = new Date();
    private long paymentSystemId = 0;
    private long currencyTypeId = 0;
    private double rate = 0;
    private double amount = 0;
    private String note = "";

    /**
     * @return the purchaseOrderId
     */
    public long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    /**
     * @param purchaseOrderId the purchaseOrderId to set
     */
    public void setPurchaseOrderId(long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    /**
     * @return the receiveMaterialId
     */
    public long getReceiveMaterialId() {
        return receiveMaterialId;
    }

    /**
     * @param receiveMaterialId the receiveMaterialId to set
     */
    public void setReceiveMaterialId(long receiveMaterialId) {
        this.receiveMaterialId = receiveMaterialId;
    }

    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the paymentSystemId
     */
    public long getPaymentSystemId() {
        return paymentSystemId;
    }

    /**
     * @param paymentSystemId the paymentSystemId to set
     */
    public void setPaymentSystemId(long paymentSystemId) {
        this.paymentSystemId = paymentSystemId;
    }

    /**
     * @return the currencyTypeId
     */
    public long getCurrencyTypeId() {
        return currencyTypeId;
    }

    /**
     * @param currencyTypeId the currencyTypeId to set
     */
    public void setCurrencyTypeId(long currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
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
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
       if (note == null) {
            note = "";
        }
        this.note = note;
    }

   

    


}
