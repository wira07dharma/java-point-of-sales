/*
 * IjCommisionDoc.java
 *
 * Created on January 14, 2006, 3:01 PM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;

/**
 *
 * @author  Administrator
 */
public class IjCommisionDoc {
    
    /**
     * Holds value of property lDocId.
     */
    private long lDocId;
    
    /**
     * Holds value of property sDocNumber.
     */
    private String sDocNumber;
    
    /**
     * Holds value of property lDocCurrency.
     */
    private long lDocCurrency;
    
    /**
     * Holds value of property dtDocTransDate.
     */
    private Date dtDocTransDate;
    
    /**
     * Holds value of property lDocContactId.
     */
    private long lDocContactId;
    
    /**
     * Holds value of property sDocRefNumber.
     */
    private String sDocRefNumber;
    
    /**
     * Holds value of property dTotalCommision.
     */
    private double dTotalCommision;
    
    /**
     * Holds value of property dtLastUpdate.
     */
    private Date dtLastUpdate;
    
    /** Creates a new instance of IjCommisionDoc */
    public IjCommisionDoc() {
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
     * Getter for property lDocCurrency.
     * @return Value of property lDocCurrency.
     */
    public long getLDocCurrency() {
        return this.lDocCurrency;
    }
    
    /**
     * Setter for property lDocCurrency.
     * @param lDocCurrency New value of property lDocCurrency.
     */
    public void setLDocCurrency(long lDocCurrency) {
        this.lDocCurrency = lDocCurrency;
    }
    
    /**
     * Getter for property dtDicTransDate.
     * @return Value of property dtDicTransDate.
     */
    public Date getDtDocTransDate() {
        return this.dtDocTransDate;
    }
    
    /**
     * Setter for property dtDicTransDate.
     * @param dtDicTransDate New value of property dtDicTransDate.
     */
    public void setDtDocTransDate(Date dtDocTransDate) {
        this.dtDocTransDate = dtDocTransDate;
    }
    
    /**
     * Getter for property lDocContactId.
     * @return Value of property lDocContactId.
     */
    public long getLDocContactId() {
        return this.lDocContactId;
    }
    
    /**
     * Setter for property lDocContactId.
     * @param lDocContactId New value of property lDocContactId.
     */
    public void setLDocContactId(long lDocContactId) {
        this.lDocContactId = lDocContactId;
    }
    
    /**
     * Getter for property sDocRefNumber.
     * @return Value of property sDocRefNumber.
     */
    public String getSDocRefNumber() {
        return this.sDocRefNumber;
    }
    
    /**
     * Setter for property sDocRefNumber.
     * @param sDocRefNumber New value of property sDocRefNumber.
     */
    public void setSDocRefNumber(String sDocRefNumber) {
        this.sDocRefNumber = sDocRefNumber;
    }
    
    /**
     * Getter for property dTotalCommision.
     * @return Value of property dTotalCommision.
     */
    public double getDTotalCommision() {
        return this.dTotalCommision;
    }
    
    /**
     * Setter for property dTotalCommision.
     * @param dTotalCommision New value of property dTotalCommision.
     */
    public void setDTotalCommision(double dTotalCommision) {
        this.dTotalCommision = dTotalCommision;
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
