/*
 * LapKegiatanUsaha.java
 *
 * Created on December 19, 2007, 2:00 PM
 */

package com.dimata.aiso.entity.report;

import java.util.Vector;

/**
 *
 * @author  dwi
 */
public class LapKegiatanUsaha {
    
    /**
     * Holds value of property agent.
     */
    private String agent = "";    
    
    /**
     * Holds value of property service.
     */
    private String service = "";
    
    /**
     * Holds value of property invNumber.
     */
    private String invNumber = "";
    
    /**
     * Holds value of property salesAmount.
     */
    private double salesAmount = 0.0;
    
    /**
     * Holds value of property supplier.
     */
    private String supplier = "";
    
    /**
     * Holds value of property costingAmount.
     */
    private double costingAmount = 0.0;
    
    /**
     * Holds value of property termOfPayment.
     */
    private int termOfPayment = 0;
    
    /**
     * Holds value of property creditSalesAmount.
     */
    private double creditSalesAmount = 0.0;
    
    /**
     * Holds value of property description.
     */
    private String description = "";
    
    /**
     * Holds value of property reference.
     */
    private String reference = "";
    
    /**
     * Holds value of property invoiceStatus.
     */
    private int invoiceStatus = 0;
    
    private Vector vCosting = new Vector();
    
    /**
     * Holds value of property invoiceMainId.
     */
    private long invoiceMainId = 0;
    
    /**
     * Holds value of property balance.
     */
    private double balance = 0.0;
    
    /**
     * Getter for property agent.
     * @return Value of property agent.
     */
    public String getAgent() {
        return this.agent;
    }    
    
    /**
     * Setter for property agent.
     * @param agent New value of property agent.
     */
    public void setAgent(String agent) {
        this.agent = agent;
    }
    
    /**
     * Getter for property service.
     * @return Value of property service.
     */
    public String getService() {
        return this.service;
    }
    
    /**
     * Setter for property service.
     * @param service New value of property service.
     */
    public void setService(String service) {
        this.service = service;
    }
    
    /**
     * Getter for property invNumber.
     * @return Value of property invNumber.
     */
    public String getInvNumber() {
        return this.invNumber;
    }
    
    /**
     * Setter for property invNumber.
     * @param invNumber New value of property invNumber.
     */
    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }
    
    /**
     * Getter for property salesAmount.
     * @return Value of property salesAmount.
     */
    public double getSalesAmount() {
        return this.salesAmount;
    }
    
    /**
     * Setter for property salesAmount.
     * @param salesAmount New value of property salesAmount.
     */
    public void setSalesAmount(double salesAmount) {
        this.salesAmount = salesAmount;
    }
    
    /**
     * Getter for property supplier.
     * @return Value of property supplier.
     */
    public String getSupplier() {
        return this.supplier;
    }
    
    /**
     * Setter for property supplier.
     * @param supplier New value of property supplier.
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    /**
     * Getter for property costingAmount.
     * @return Value of property costingAmount.
     */
    public double getCostingAmount() {
        return this.costingAmount;
    }
    
    /**
     * Setter for property costingAmount.
     * @param costingAmount New value of property costingAmount.
     */
    public void setCostingAmount(double costingAmount) {
        this.costingAmount = costingAmount;
    }
    
    /**
     * Getter for property termOfPayment.
     * @return Value of property termOfPayment.
     */
    public int getTermOfPayment() {
        return this.termOfPayment;
    }
    
    /**
     * Setter for property termOfPayment.
     * @param termOfPayment New value of property termOfPayment.
     */
    public void setTermOfPayment(int termOfPayment) {
        this.termOfPayment = termOfPayment;
    }
    
    /**
     * Getter for property creditSalesAmount.
     * @return Value of property creditSalesAmount.
     */
    public double getCreditSalesAmount() {
        return this.creditSalesAmount;
    }
    
    /**
     * Setter for property creditSalesAmount.
     * @param creditSalesAmount New value of property creditSalesAmount.
     */
    public void setCreditSalesAmount(double creditSalesAmount) {
        this.creditSalesAmount = creditSalesAmount;
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
    
    public Vector getCosting(){
        return this.vCosting; 
    }
    
    public void setCosting(Vector vCosting){
        this.vCosting = vCosting;
    }
    
    /**
     * Getter for property invoiceMainId.
     * @return Value of property invoiceMainId.
     */
    public long getInvoiceMainId() {
        return this.invoiceMainId;
    }
    
    /**
     * Setter for property invoiceMainId.
     * @param invoiceMainId New value of property invoiceMainId.
     */
    public void setInvoiceMainId(long invoiceMainId) {
        this.invoiceMainId = invoiceMainId;
    }
    
    /**
     * Getter for property balance.
     * @return Value of property balance.
     */
    public double getBalance() {
        return this.balance;
    }
    
    /**
     * Setter for property balance.
     * @param balance New value of property balance.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
}
