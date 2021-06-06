/*
 * IjAdjustmentDoc.java
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
public class IjAdjustmentDoc {
    
    /**
     * Holds value of property lDocId.
     */
    private long lDocId;
    
    /**
     * Holds value of property sDocNumber.
     */
    private String sDocNumber;
    
    /**
     * Holds value of property dtDocTransDate.
     */
    private Date dtDocTransDate;
    
    /**
     * Holds value of property lDocCurrency.
     */
    private long lDocCurrency;
    
    /**
     * Holds value of property lVillaId.
     */
    private long lVillaId;
    
    /**
     * Holds value of property lGuestId.
     */
    private long lGuestId;
    
    /**
     * Holds value of property dTotalAdjustment.
     */
    private double dTotalAdjustment;
    
    /**
     * Holds value of property dTotalTax.
     */
    private double dTotalTax;
    
    /**
     * Holds value of property dTotalService.
     */
    private double dTotalService;
    
    /**
     * Holds value of property dtLastUpdate.
     */
    private Date dtLastUpdate;
    
    /**
     * Holds value of property dAdjustPeriodId.
     */
    private Date dtAdjustDate;
    
    /**
     * Holds value of property dDocRate.
     */
    private double dDocRate;
    
    /**
     * Holds value of property dSalesTax.
     */
    private double dTax;
    
    /**
     * Holds value of property lSalesPeriodId.
     */
    private Date dtSalesDate;
    
    /**
     * Holds value of property iAdjType.
     */
    private int iAdjType;
    
    /** Creates a new instance of IjAdjustmentDoc */
    public IjAdjustmentDoc() {
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
     * Getter for property dtDocTransDate.
     * @return Value of property dtDocTransDate.
     */
    public Date getDtDocTransDate() {
        return this.dtDocTransDate;
    }
    
    /**
     * Setter for property dtDocTransDate.
     * @param dtDocTransDate New value of property dtDocTransDate.
     */
    public void setDtDocTransDate(Date dtDocTransDate) {
        this.dtDocTransDate = dtDocTransDate;
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
     * Getter for property lVillaId.
     * @return Value of property lVillaId.
     */
    public long getLVillaId() {
        return this.lVillaId;
    }
    
    /**
     * Setter for property lVillaId.
     * @param lVillaId New value of property lVillaId.
     */
    public void setLVillaId(long lVillaId) {
        this.lVillaId = lVillaId;
    }
    
    /**
     * Getter for property dTotalAdjustment.
     * @return Value of property dTotalAdjustment.
     */
    public double getDAdjustmentValue() {
        return this.dTotalAdjustment;
    }
    
    /**
     * Setter for property dTotalAdjustment.
     * @param dTotalAdjustment New value of property dTotalAdjustment.
     */
    public void setDAdjustmentValue(double dTotalAdjustment) {
        this.dTotalAdjustment = dTotalAdjustment;
    }
    
    /**
     * Getter for property dTotalService.
     * @return Value of property dTotalService.
     */
    public double getDService() {
        return this.dTotalService;
    }
    
    /**
     * Setter for property dTotalService.
     * @param dTotalService New value of property dTotalService.
     */
    public void setDService(double dTotalService) {
        this.dTotalService = dTotalService;
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
     * Getter for property dAdjustPeriodId.
     * @return Value of property dAdjustPeriodId.
     */
    public Date getDtAdjustDate() {
        return this.dtAdjustDate;
    }
    
    /**
     * Setter for property dAdjustPeriodId.
     * @param dAdjustPeriodId New value of property dAdjustPeriodId.
     */
    public void setDtAdjustDate(Date dtAdjustDate) {
        this.dtAdjustDate = dtAdjustDate;
    }
    
    /**
     * Getter for property dDocRate.
     * @return Value of property dDocRate.
     */
    public double getDDocRate() {
        return this.dDocRate;
    }
    
    /**
     * Setter for property dDocRate.
     * @param dDocRate New value of property dDocRate.
     */
    public void setDDocRate(double dDocRate) {
        this.dDocRate = dDocRate;
    }
    
    /**
     * Getter for property dSalesTax.
     * @return Value of property dSalesTax.
     */
    public double getDTax() {
        return this.dTax;
    }
    
    /**
     * Setter for property dSalesTax.
     * @param dSalesTax New value of property dSalesTax.
     */
    public void setDTax(double dTax) {
        this.dTax = dTax;
    }
    
    /**
     * Getter for property lSalesPeriodId.
     * @return Value of property lSalesPeriodId.
     */
    public Date getDtSalesDate() {
        return this.dtSalesDate;
    }
    
    /**
     * Setter for property lSalesPeriodId.
     * @param lSalesPeriodId New value of property lSalesPeriodId.
     */
    public void setDtSalesDate(Date dtSalesDate) {
        this.dtSalesDate = dtSalesDate;
    }
    
    /**
     * Getter for property iAdjType.
     * @return Value of property iAdjType.
     */
    public int getIAdjType() {
        return this.iAdjType;
    }
    
    /**
     * Setter for property iAdjType.
     * @param iAdjType New value of property iAdjType.
     */
    public void setIAdjType(int iAdjType) {
        this.iAdjType = iAdjType;
    }
    
}
