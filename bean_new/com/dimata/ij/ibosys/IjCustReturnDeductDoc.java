/*
 * IjCustReturnDeductionDoc.java
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
public class IjCustReturnDeductDoc {
    
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
     * Holds value of property sDocReference.
     */
    private String sDocReference;
    
    /**
     * Holds value of property dTotalReturnValue.
     */
    private double dTotalReturnValue;
    
    /**
     * Holds value of property dtLastUpdate.
     */
    private Date dtLastUpdate;
    
    /** Creates a new instance of IjCustReturnDeductionDoc */
    public IjCustReturnDeductDoc() {
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
     * Getter for property sDocReference.
     * @return Value of property sDocReference.
     */
    public String getSDocReference() {
        return this.sDocReference;
    }
    
    /**
     * Setter for property sDocReference.
     * @param sDocReference New value of property sDocReference.
     */
    public void setSDocReference(String sDocReference) {
        this.sDocReference = sDocReference;
    }
    
    /**
     * Getter for property dTotalReturnValue.
     * @return Value of property dTotalReturnValue.
     */
    public double getDTotalReturnValue() {
        return this.dTotalReturnValue;
    }
    
    /**
     * Setter for property dTotalReturnValue.
     * @param dTotalReturnValue New value of property dTotalReturnValue.
     */
    public void setDTotalReturnValue(double dTotalReturnValue) {
        this.dTotalReturnValue = dTotalReturnValue;
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
    
}
