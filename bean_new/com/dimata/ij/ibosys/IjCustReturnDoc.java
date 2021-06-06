/*
 * IjCustReturnDoc.java
 *
 * Created on January 14, 2006, 6:34 AM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class IjCustReturnDoc {
    
    /**
     * Holds value of property lDocId.
     */
    private long lDocId;
    
    /**
     * Holds value of property sDocNumber.
     */
    private String sDocNumber;
    
    /**
     * Holds value of property dDocDate.
     */
    private Date dtDocDate;
    
    /**
     * Holds value of property sDocRemark.
     */
    private String sDocRemark;
    
    /**
     * Holds value of property sDocReturnReason.
     */
    private String sDocReturnReason;
    
    /**
     * Holds value of property dTotalValue.
     */
    private double dTotalValue;
    
    /**
     * Holds value of property dTotalCost.
     */
    private double dTotalCost;
    
    /**
     * Holds value of property dtLastUpdate.
     */
    private Date dtLastUpdate;
    
    /**
     * Holds value of property iPaymentType.
     */
    private int iPaymentType;
    
    /**
     * Holds value of property listPayment.
     */
    private Vector listPayment;
    
    /** Creates a new instance of IjCustReturnDoc */
    public IjCustReturnDoc() {
    }
    
    /**
     * Getter for property lDocId.
     * @return Value of property lDocId.
     */
    public long getLDocId() {
        return this.lDocId;
    }
    
    /**
     * Setter for property lDocId.
     * @param lDocId New value of property lDocId.
     */
    public void setLDocId(long lDocId) {
        this.lDocId = lDocId;
    }
    
    /**
     * Getter for property sDocNumber.
     * @return Value of property sDocNumber.
     */
    public String getSDocNumber() {
        return this.sDocNumber;
    }
    
    /**
     * Setter for property sDocNumber.
     * @param sDocNumber New value of property sDocNumber.
     */
    public void setSDocNumber(String sDocNumber) {
        this.sDocNumber = sDocNumber;
    }
    
    /**
     * Getter for property dDocDate.
     * @return Value of property dDocDate.
     */
    public Date getDtDocDate() {
        return this.dtDocDate;
    }
    
    /**
     * Setter for property dDocDate.
     * @param dDocDate New value of property dDocDate.
     */
    public void setDtDocDate(Date dtDocDate) {
        this.dtDocDate = dtDocDate;
    }
    
    /**
     * Getter for property sDocRemark.
     * @return Value of property sDocRemark.
     */
    public String getSDocRemark() {
        return this.sDocRemark;
    }
    
    /**
     * Setter for property sDocRemark.
     * @param sDocRemark New value of property sDocRemark.
     */
    public void setSDocRemark(String sDocRemark) {
        this.sDocRemark = sDocRemark;
    }
    
    /**
     * Getter for property sDocReturnReason.
     * @return Value of property sDocReturnReason.
     */
    public String getSDocReturnReason() {
        return this.sDocReturnReason;
    }
    
    /**
     * Setter for property sDocReturnReason.
     * @param sDocReturnReason New value of property sDocReturnReason.
     */
    public void setSDocReturnReason(String sDocReturnReason) {
        this.sDocReturnReason = sDocReturnReason;
    }
    
    /**
     * Getter for property dTotalValue.
     * @return Value of property dTotalValue.
     */
    public double getDTotalValue() {
        return this.dTotalValue;
    }
    
    /**
     * Setter for property dTotalValue.
     * @param dTotalValue New value of property dTotalValue.
     */
    public void setDTotalValue(double dTotalValue) {
        this.dTotalValue = dTotalValue;
    }
    
    /**
     * Getter for property dTotalCost.
     * @return Value of property dTotalCost.
     */
    public double getDTotalCost() {
        return this.dTotalCost;
    }
    
    /**
     * Setter for property dTotalCost.
     * @param dTotalCost New value of property dTotalCost.
     */
    public void setDTotalCost(double dTotalCost) {
        this.dTotalCost = dTotalCost;
    }
    
    /**
     * Getter for property dtLastUpdate.
     * @return Value of property dtLastUpdate.
     */
    public Date getDtLastUpdate() {
        return this.dtLastUpdate;
    }
    
    /**
     * Setter for property dtLastUpdate.
     * @param dtLastUpdate New value of property dtLastUpdate.
     */
    public void setDtLastUpdate(Date dtLastUpdate) {
        this.dtLastUpdate = dtLastUpdate;
    }
    
    /**
     * Getter for property iPaymentType.
     * @return Value of property iPaymentType.
     */
    public int getIPaymentType() {
        return this.iPaymentType;
    }
    
    /**
     * Setter for property iPaymentType.
     * @param iPaymentType New value of property iPaymentType.
     */
    public void setIPaymentType(int iPaymentType) {
        this.iPaymentType = iPaymentType;
    }
    
    /**
     * Getter for property listPayment.
     * @return Value of property listPayment.
     */
    public Vector getListPayment() {
        return this.listPayment;
    }
    
    /**
     * Setter for property listPayment.
     * @param listPayment New value of property listPayment.
     */
    public void setListPayment(Vector listPayment) {
        this.listPayment = listPayment;
    }
    
}
