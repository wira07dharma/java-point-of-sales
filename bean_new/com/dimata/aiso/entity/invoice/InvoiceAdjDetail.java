/*
 * InvoiceAdjDetail.java
 *
 * Created on November 12, 2007, 2:13 PM
 */

package com.dimata.aiso.entity.invoice;

/**
 *
 * @author  dwi
 */
/* import java */
import java.util.*;
import java.util.Date;

/* import qdep */
import com.dimata.qdep.entity.*;

public class InvoiceAdjDetail extends Entity{
    
    /**
     * Holds value of property nameOfPax.
     */
    private String nameOfPax = "";    
   
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
     * Holds value of property itemDiscount.
     */
    private double itemDiscount = 0;
    
    /**
     * Holds value of property adjustmentId.
     */
    private long adjustmentId = 0;
    
    /**
     * Holds value of property idPerkiraan.
     */
    private long idPerkiraan = 0;
    
    /**
     * Holds value of property date.
     */
    private Date date = new Date();
    
    /**
     * Holds value of property netAmount.
     */
    private double netAmount = 0;
    
    /**
     * Getter for property nameOfPax.
     * @return Value of property nameOfPax.
     */
    public String getNameOfPax() {
        return this.nameOfPax;
    }    
    
    /**
     * Setter for property nameOfPax.
     * @param nameOfPax New value of property nameOfPax.
     */
    public void setNameOfPax(String nameOfPax) {
        this.nameOfPax = nameOfPax;
    }
    
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
     * Getter for property itemDiscount.
     * @return Value of property itemDiscount.
     */
    public double getItemDiscount() {
        return this.itemDiscount;
    }
    
    /**
     * Setter for property itemDiscount.
     * @param itemDiscount New value of property itemDiscount.
     */
    public void setItemDiscount(double itemDiscount) {
        this.itemDiscount = itemDiscount;
    }
    
    /**
     * Getter for property adjustmentId.
     * @return Value of property adjustmentId.
     */
    public long getAdjustmentId() {
        return this.adjustmentId;
    }
    
    /**
     * Setter for property adjustmentId.
     * @param adjustmentId New value of property adjustmentId.
     */
    public void setAdjustmentId(long adjustmentId) {
        this.adjustmentId = adjustmentId;
    }
    
    /**
     * Getter for property idPerkiraan.
     * @return Value of property idPerkiraan.
     */
    public long getIdPerkiraan() {
        return this.idPerkiraan;
    }
    
    /**
     * Setter for property idPerkiraan.
     * @param idPerkiraan New value of property idPerkiraan.
     */
    public void setIdPerkiraan(long idPerkiraan) {
        this.idPerkiraan = idPerkiraan;
    }
    
    /**
     * Getter for property date.
     * @return Value of property date.
     */
    public Date getDate() {
        return this.date;
    }
    
    /**
     * Setter for property date.
     * @param idPerkiraan New value of property date.
     */
    public void setDate(Date date) {
        this.date = date;
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