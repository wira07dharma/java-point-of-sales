/*
 * ForwarderInfo.java
 *
 * Created on May 30, 2007, 6:11 PM
 */

package com.dimata.posbo.entity.warehouse;

/* package java */

import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

/**
 *
 * @author  gwawan
 */
public class ForwarderInfo extends Entity {
    private long receiveId = 0;
    private long locationId = 0;
    private String docNumber = "";
    private Date docDate = new Date();
    private long contactId = 0;
    private long currencyId = 0;
    private double transRate = 0;
    private double totalCost = 0;
    private String notes = "";
    private int status = 0;
    private int counter = 0;
    
    /** Creates a new instance of ForwarderInfo */
    public ForwarderInfo() {
    }
    
    /**
     * Getter for property receiveId.
     * @return Value of property receiveId.
     */
    public long getReceiveId() {
        return receiveId;
    }
    
    /**
     * Setter for property receiveId.
     * @param receiveId New value of property receiveId.
     */
    public void setReceiveId(long receiveId) {
        this.receiveId = receiveId;
    }
    
    /**
     * Getter for property locationId.
     * @return Value of property locationId.
     */
    public long getLocationId() {
        return locationId;
    }
    
    /**
     * Setter for property locationId.
     * @param locationId New value of property locationId.
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
    /**
     * Getter for property docNumber.
     * @return Value of property docNumber.
     */
    public java.lang.String getDocNumber() {
        return docNumber;
    }
    
    /**
     * Setter for property docNumber.
     * @param docNumber New value of property docNumber.
     */
    public void setDocNumber(java.lang.String docNumber) {
        this.docNumber = docNumber;
    }
    
    /**
     * Getter for property docDate.
     * @return Value of property docDate.
     */
    public java.util.Date getDocDate() {
        return docDate;
    }
    
    /**
     * Setter for property docDate.
     * @param docDate New value of property docDate.
     */
    public void setDocDate(java.util.Date docDate) {
        this.docDate = docDate;
    }
    
    /**
     * Getter for property contactId.
     * @return Value of property contactId.
     */
    public long getContactId() {
        return contactId;
    }
    
    /**
     * Setter for property contactId.
     * @param contactId New value of property contactId.
     */
    public void setContactId(long contactId) {
        this.contactId = contactId;
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
     * Getter for property transRate.
     * @return Value of property transRate.
     */
    public double getTransRate() {
        return transRate;
    }
    
    /**
     * Setter for property transRate.
     * @param transRate New value of property transRate.
     */
    public void setTransRate(double transRate) {
        this.transRate = transRate;
    }
    
    /**
     * Getter for property totalCost.
     * @return Value of property totalCost.
     */
    public double getTotalCost() {
        return totalCost;
    }
    
    /**
     * Setter for property totalCost.
     * @param totalCost New value of property totalCost.
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    /**
     * Getter for property notes.
     * @return Value of property notes.
     */
    public java.lang.String getNotes() {
        return notes;
    }
    
    /**
     * Setter for property notes.
     * @param notes New value of property notes.
     */
    public void setNotes(java.lang.String notes) {
        this.notes = notes;
    }
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    /**
     * Getter for property counter.
     * @return Value of property counter.
     */
    public int getCounter() {
        return counter;
    }
    
    /**
     * Setter for property counter.
     * @param counter New value of property counter.
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }
    
}
