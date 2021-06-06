/*
 * AccPayable.java
 *
 * Created on May 4, 2007, 6:12 PM
 */

package com.dimata.posbo.entity.arap;

import java.util.Date;

import com.dimata.qdep.entity.Entity;
/**
 *
 * @author  gwawan
 */
public class AccPayable extends Entity {
    private long receiveMaterialId = 0;
    private Date paymentDate = new Date();
    private String description = "";
    private int numOfPayment = 0;
    private int status;
    private String paymentNumber="";
    private int noUrut=0;
    
    /** Creates a new instance of AccPayable */
    public AccPayable() {
    }
    
    public long getReceiveMaterialId() {
        return receiveMaterialId;
    }
    
    public void setReceiveMaterialId(long receiveMaterialId) {
        this.receiveMaterialId = receiveMaterialId;
    }
    
    public java.util.Date getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(java.util.Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public java.lang.String getDescription() {
        return description;
    }
    
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    public int getNumOfPayment() {
        return numOfPayment;
    }
    
    public void setNumOfPayment(int numOfPayment) {
        this.numOfPayment = numOfPayment;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the paymentNumber
     */
    public String getPaymentNumber() {
        return paymentNumber;
    }

    /**
     * @param paymentNumber the paymentNumber to set
     */
    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    /**
     * @return the noUrut
     */
    public int getNoUrut() {
        return noUrut;
    }

    /**
     * @param noUrut the noUrut to set
     */
    public void setNoUrut(int noUrut) {
        this.noUrut = noUrut;
    }
    
}
