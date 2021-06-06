/*
 * CostingAdjustment.java
 *
 * Created on November 12, 2007, 2:07 PM
 */

package com.dimata.aiso.entity.costing;

/**
 *
 * @author  dwi
 */
/* import java */
import java.util.*;
import java.util.Date;
 
/* import qdep */
import com.dimata.qdep.entity.*;

public class CostingAdjustment extends Entity{
    
    /**
     * Holds value of property description.
     */
    private String description = "";    
    
    /**
     * Holds value of property number.
     */
    private int number = 0;
    
    /**
     * Holds value of property unitPrice.
     */
    private double unitPrice = 0;
    
    /**
     * Holds value of property discount.
     */
    private double discount = 0;
    
    /**
     * Holds value of property status.
     */
    private int status = 0;
    
    /**
     * Holds value of property contactId.
     */
    private long contactId = 0;
    
    /**
     * Holds value of property reference.
     */
    private String reference = "";
    
    /**
     * Holds value of property costingId.
     */
    private long costingId = 0;
    
    /**
     * Holds value of property netAmount.
     */
    private double netAmount = 0;
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return this.description;
    }    
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter for property number.
     * @return Value of property number.
     */
    public int getNumber() {
        return this.number;
    }
    
    /**
     * Setter for property number.
     * @param number New value of property number.
     */
    public void setNumber(int number) {
        this.number = number;
    }
    
    /**
     * Getter for property unitPrice.
     * @return Value of property unitPrice.
     */
    public double getUnitPrice() {
        return this.unitPrice;
    }
    
    /**
     * Setter for property unitPrice.
     * @param unitPrice New value of property unitPrice.
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    /**
     * Getter for property discount.
     * @return Value of property discount.
     */
    public double getDiscount() {
        return this.discount;
    }
    
    /**
     * Setter for property discount.
     * @param discount New value of property discount.
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public int getStatus() {
        return this.status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    /**
     * Getter for property contactId.
     * @return Value of property contactId.
     */
    public long getContactId() {
        return this.contactId;
    }
    
    /**
     * Setter for property contactId.
     * @param contactId New value of property contactId.
     */
    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
    
    /**
     * Getter for property reference.
     * @return Value of property reference.
     */
    public String getReference() {
        return this.reference;
    }
    
    /**
     * Setter for property reference.
     * @param reference New value of property reference.
     */
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    /**
     * Getter for property costingId.
     * @return Value of property costingId.
     */
    public long getCostingId() {
        return this.costingId;
    }
    
    /**
     * Setter for property costingId.
     * @param costingId New value of property costingId.
     */
    public void setCostingId(long costingId) {
        this.costingId = costingId;
    }
    
    /**
     * Getter for property netAmount.
     * @return Value of property netAmount.
     */
    public double getNetAmount() {
        return this.netAmount;
    }
    
    /**
     * Setter for property netAmount.
     * @param netAmount New value of property netAmount.
     */
    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }
    
}
