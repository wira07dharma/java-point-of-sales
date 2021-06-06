/*
 * RptArInvoice.java
 *
 * Created on July 20, 2005, 10:54 AM
 */

package com.dimata.posbo.entity.search;

import java.util.Date;
import java.util.Vector;
/**
 *
 * @author  pulantara
 */
public class RptArInvoice {
    
    private long memberId = 0;
    private long billMainId = 0;
    private long locationId = 0;
    private String custName = "";
    private String invoiceNo = "";
    private String notes = "";
    private Date billDate = new Date();
    private double saleNetto = 0;
    private double totalPay = 0;
    private Vector vectPayment = new Vector();
    private double discount = 0;
    private double tax = 0;
    private double rate = 0;
    private double totalReturn = 0;

    //adding variabel service
    private double service = 0;
    
    /** Creates a new instance of RptArInvoice */
    public RptArInvoice() { 
    }
    
    /**
     * Getter for property memberId.
     * @return Value of property memberId.
     */
    public long getMemberId() {
        return memberId;
    }
    
    /**
     * Setter for property memberId.
     * @param memberId New value of property memberId.
     */
    public void setMemberId(long memberId) {
        this.memberId = memberId;
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
     * Getter for property custName.
     * @return Value of property custName.
     */
    public java.lang.String getCustName() {
        return custName;
    }
    
    /**
     * Setter for property custName.
     * @param custName New value of property custName.
     */
    public void setCustName(java.lang.String custName) {
        this.custName = custName;
    }
    
    /**
     * Getter for property invoiceNo.
     * @return Value of property invoiceNo.
     */
    public java.lang.String getInvoiceNo() {
        return invoiceNo;
    }
    
    /**
     * Setter for property invoiceNo.
     * @param invoiceNo New value of property invoiceNo.
     */
    public void setInvoiceNo(java.lang.String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    
    /**
     * Getter for property billDate.
     * @return Value of property billDate.
     */
    public java.util.Date getBillDate() {
        return billDate;
    }
    
    /**
     * Setter for property billDate.
     * @param billDate New value of property billDate.
     */
    public void setBillDate(java.util.Date billDate) {
        this.billDate = billDate;
    }
    
    /**
     * Getter for property saleNetto.
     * @return Value of property saleNetto.
     */
    public double getSaleNetto() {
        return saleNetto;
    }
    
    /**
     * Setter for property saleNetto.
     * @param saleNetto New value of property saleNetto.
     */
    public void setSaleNetto(double saleNetto) {
        this.saleNetto = saleNetto;
    }
    
    /**
     * Getter for property totalPay.
     * @return Value of property totalPay.
     */
    public double getTotalPay() {
        return totalPay;
    }
    
    /**
     * Setter for property totalPay.
     * @param totalPay New value of property totalPay.
     */
    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }
    
    /**
     * Getter for property vectPayment.
     * @return Value of property vectPayment.
     */
    public java.util.Vector getVectPayment() {
        return vectPayment;
    }
    
    /**
     * Setter for property vectPayment.
     * @param vectPayment New value of property vectPayment.
     */
    public void setVectPayment(java.util.Vector vectPayment) {
        this.vectPayment = vectPayment;
    }
    
    /**
     * getBalance
     */
    public double getBalance(){
        return saleNetto - totalPay;
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
     * Getter for property discount.
     * @return Value of property discount.
     */
    public double getDiscount() {
        return discount;
    }
    
    /**
     * Setter for property discount.
     * @param discount New value of property discount.
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    /**
     * Getter for property tax.
     * @return Value of property tax.
     */
    public double getTax() {
        return tax;
    }
    
    /**
     * Setter for property tax.
     * @param tax New value of property tax.
     */
    public void setTax(double tax) {
        this.tax = tax;
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
    
    public double getTotalReturn() {
        return totalReturn;
    }
    
    public void setTotalReturn(double totalReturn) {
        this.totalReturn = totalReturn;
    }

    /**
     * @return the service
     */
    public double getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(double service) {
        this.service = service;
    }
    
}
