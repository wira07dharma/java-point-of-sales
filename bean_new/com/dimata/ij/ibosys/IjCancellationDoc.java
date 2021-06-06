/*
 * IjCancellationDoc.java
 *
 * Created on January 14, 2006, 2:59 PM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class IjCancellationDoc {
    
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
     * Holds value of property dTotalCancellation.
     */
    private double dTotalCancellation;
    
    /**
     * Holds value of property dtLastUpdate.
     */
    private Date dtLastUpdate;
    
    /**
     * Holds value of property dDocRate.
     */
    private double dDocRate;
    
    /**
     * Holds value of property dTotalSalesTax.
     */
    private double dTotalSalesTax;
    
    /**
     * Holds value of property dCancellationCharge.
     */
    private double dCancellationCharge;
    
    /**
     * Holds value of property lSalesPeriodId.
     */
    private Date dtSalesDate;
    
    /**
     * Holds value of property lCancellationPeriodId.
     */
    private Date dtCancellationDate;
    
    /**
     * Holds value of property listPayment.
     */
    private Vector listPayment;
    
    /**
     * Holds value of property listDPDeduction.
     */
    private Vector listDPDeduction;
    
    /** Creates a new instance of IjCancellationDoc */
    public IjCancellationDoc() {
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
     * Getter for property dTotalCancellation.
     * @return Value of property dTotalCancellation.
     */
    public double getDTotalCancellation() {
        return this.dTotalCancellation;
    }
    
    /**
     * Setter for property dTotalCancellation.
     * @param dTotalCancellation New value of property dTotalCancellation.
     */
    public void setDTotalCancellation(double dTotalCancellation) {
        this.dTotalCancellation = dTotalCancellation;
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
     * Getter for property dTotalSalesTax.
     * @return Value of property dTotalSalesTax.
     */
    public double getDSalesTax() {
        return this.dTotalSalesTax;
    }
    
    /**
     * Setter for property dTotalSalesTax.
     * @param dTotalSalesTax New value of property dTotalSalesTax.
     */
    public void setDSalesTax(double dTotalSalesTax) {
        this.dTotalSalesTax = dTotalSalesTax;
    }
    
    /**
     * Getter for property dCancellationCharge.
     * @return Value of property dCancellationCharge.
     */
    public double getDCancellationCharge() {
        return this.dCancellationCharge;
    }
    
    /**
     * Setter for property dCancellationCharge.
     * @param dCancellationCharge New value of property dCancellationCharge.
     */
    public void setDCancellationCharge(double dCancellationCharge) {
        this.dCancellationCharge = dCancellationCharge;
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
     * Getter for property lCancellationPeriodId.
     * @return Value of property lCancellationPeriodId.
     */
    public Date getDtCancellationDate() {
        return this.dtCancellationDate;
    }
    
    /**
     * Setter for property lCancellationPeriodId.
     * @param lCancellationPeriodId New value of property lCancellationPeriodId.
     */
    public void setDtCancellationDate(Date dtCancellationDate) {
        this.dtCancellationDate = dtCancellationDate;
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
    
    /**
     * Getter for property listDPDeduction.
     * @return Value of property listDPDeduction.
     */
    public Vector getListDPDeduction() {
        return this.listDPDeduction;
    }
    
    /**
     * Setter for property listDPDeduction.
     * @param listDPDeduction New value of property listDPDeduction.
     */
    public void setListDPDeduction(Vector listDPDeduction) {
        this.listDPDeduction = listDPDeduction;
    }
    
}
