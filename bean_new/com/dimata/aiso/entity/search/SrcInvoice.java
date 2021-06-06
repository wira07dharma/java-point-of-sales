/*
 * SrcInvoice.java
 *
 * Created on November 20, 2007, 5:50 PM
 */

package com.dimata.aiso.entity.search;

import java.util.Date;

/**
 *
 * @author  dwi
 */
public class SrcInvoice {
    
    /**
     * Holds value of property invoice_number.
     */
    private String invoice_number;
    
    /**
     * Holds value of property contactId.
     */
    private long contactId = 0;
    
    private Date startDate = new Date();
    private Date endDate = new Date();
    
    /**
     * Holds value of property dateType.
     */
    private int dateType = 0;
    
    /**
     * Holds value of property invoiceType.
     */
    private int invoiceType;
    
    /**
     * Holds value of property invoiceStatus.
     */
    private int invoiceStatus;
    
    /**
     * Getter for property invoice_number.
     * @return Value of property invoice_number.
     */
    public String getInvoice_number() {
        return this.invoice_number;
    }
    
    /**
     * Setter for property invoice_number.
     * @param invoice_number New value of property invoice_number.
     */
    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
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
    
    public Date getStartDate(){
        return this.startDate;
    }
    
    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }
    
     public Date getEndDate(){
        return this.endDate;
    }
    
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
    
    /**
     * Getter for property dateType.
     * @return Value of property dateType.
     */
    public int getDateType() {
        return this.dateType;
    }
    
    /**
     * Setter for property dateType.
     * @param dateType New value of property dateType.
     */
    public void setDateType(int dateType) {
        this.dateType = dateType;
    }
    
    /**
     * Getter for property invoiceType.
     * @return Value of property invoiceType.
     */
    public int getInvoiceType() {
        return this.invoiceType;
    }
    
    /**
     * Setter for property invoiceType.
     * @param invoiceType New value of property invoiceType.
     */
    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }
    
    /**
     * Getter for property invoiceStatus.
     * @return Value of property invoiceStatus.
     */
    public int getInvoiceStatus() {
        return this.invoiceStatus;
    }
    
    /**
     * Setter for property invoiceStatus.
     * @param invoiceStatus New value of property invoiceStatus.
     */
    public void setInvoiceStatus(int invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
    
}
