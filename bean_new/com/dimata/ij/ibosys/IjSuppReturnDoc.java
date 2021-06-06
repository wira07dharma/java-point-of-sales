/*
 * IjSuppReturnDoc.java
 *
 * Created on January 14, 2006, 6:35 AM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;

/**
 *
 * @author  Administrator
 */
public class IjSuppReturnDoc {
    
    /**
     * Holds value of property lDocId.
     */
    private long lDocId;
    
    /**
     * Holds value of property sDocNumber.
     */
    private String sDocNumber;
    
    /**
     * Holds value of property dtDocDate.
     */
    private Date dtDocDate;
    
    /**
     * Holds value of property sDocRemark.
     */
    private String sDocRemark;
    
    /**
     * Holds value of property sReturnReason.
     */
    private String sReturnReason;
    
    /**
     * Holds value of property dTotalValue.
     */
    private double dTotalValue;
    
    /**
     * Holds value of property dtLastUpdate.
     */
    private Date dtLastUpdate;
    
    /**
     * Holds value of property iPaymentType.
     */
    private int iPaymentType;
    
    /** Creates a new instance of IjSuppReturnDoc */
    public IjSuppReturnDoc() {
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
     * Getter for property dtDocDate.
     * @return Value of property dtDocDate.
     */
    public Date getDtDocDate() {
        return this.dtDocDate;
    }
    
    /**
     * Setter for property dtDocDate.
     * @param dtDocDate New value of property dtDocDate.
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
     * Getter for property sReturnReason.
     * @return Value of property sReturnReason.
     */
    public String getSReturnReason() {
        return this.sReturnReason;
    }
    
    /**
     * Setter for property sReturnReason.
     * @param sReturnReason New value of property sReturnReason.
     */
    public void setSReturnReason(String sReturnReason) {
        this.sReturnReason = sReturnReason;
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
    
}
