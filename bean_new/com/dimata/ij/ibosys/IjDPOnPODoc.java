/*
 * DPonPurchaseOrder.java
 *
 * Created on December 29, 2004, 1:02 PM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author  Administrator
 * @version 
 */
public class IjDPOnPODoc {
    
    /** Holds value of property lDocOid. */
    private long lDocOid;
    
    /** Holds value of property sNumber. */
    private String sNumber;
    
    /** Holds value of property lTransCurrency. */
    private long lTransCurrency;
    
    /** Holds value of property lContact. */
    private long lContact;
    
    /** Holds value of property dtTransDate. */
    private Date dtTransDate;
    
    /** Holds value of property dtDueDate. */
    private Date dtDueDate;
    
    /** Holds value of property lPaymentType. */
    private long lPaymentType;
    
    /** Holds value of property dNominal. */
    private double dNominal;
    
    /** Holds value of property dRate. */
    private double dRate;
    
    /** Holds value of property dtLastUpdate. */
    private Date dtLastUpdate;
    
    /** Creates new DPonPO */
    public IjDPOnPODoc() {
    }
    
    /** Getter for property lDocOid.
     * @return Value of property lDocOid.
     *
     */
    public long getLDocOid() {
        return this.lDocOid;
    }
    
    /** Setter for property lDocOid.
     * @param lDocOid New value of property lDocOid.
     *
     */
    public void setLDocOid(long lDocOid) {
        this.lDocOid = lDocOid;
    }
    
    /** Getter for property sNumber.
     * @return Value of property sNumber.
     *
     */
    public String getSNumber() {
        return this.sNumber;
    }
    
    /** Setter for property sNumber.
     * @param sNumber New value of property sNumber.
     *
     */
    public void setSNumber(String sNumber) {
        this.sNumber = sNumber;
    }
    
    /** Getter for property lTransCurrency.
     * @return Value of property lTransCurrency.
     *
     */
    public long getLTransCurrency() {
        return this.lTransCurrency;
    }
    
    /** Setter for property lTransCurrency.
     * @param lTransCurrency New value of property lTransCurrency.
     *
     */
    public void setLTransCurrency(long lTransCurrency) {
        this.lTransCurrency = lTransCurrency;
    }
    
    /** Getter for property dtTransDate.
     * @return Value of property dtTransDate.
     *
     */
    public Date getDtTransDate() {
        return this.dtTransDate;
    }
    
    /** Setter for property dtTransDate.
     * @param dtTransDate New value of property dtTransDate.
     *
     */
    public void setDtTransDate(Date dtTransDate) {
        this.dtTransDate = dtTransDate;
    }
    
    /** Getter for property dtDueDate.
     * @return Value of property dtDueDate.
     *
     */
    public Date getDtDueDate() {
        return this.dtDueDate;
    }
    
    /** Setter for property dtDueDate.
     * @param dtDueDate New value of property dtDueDate.
     *
     */
    public void setDtDueDate(Date dtDueDate) {
        this.dtDueDate = dtDueDate;
    }
    
    /** Getter for property lPaymentType.
     * @return Value of property lPaymentType.
     *
     */
    public long getLPaymentType() {
        return this.lPaymentType;
    }
    
    /** Setter for property lPaymentType.
     * @param lPaymentType New value of property lPaymentType.
     *
     */
    public void setLPaymentType(long lPaymentType) {
        this.lPaymentType = lPaymentType;
    }
    
    /** Getter for property dNominal.
     * @return Value of property dNominal.
     *
     */
    public double getDNominal() {
        return this.dNominal;
    }
    
    /** Setter for property dNominal.
     * @param dNominal New value of property dNominal.
     *
     */
    public void setDNominal(double dNominal) {
        this.dNominal = dNominal;
    }
    
    /** Getter for property dRate.
     * @return Value of property dRate.
     *
     */
    public double getDRate() {
        return this.dRate;
    }
    
    /** Setter for property dRate.
     * @param dRate New value of property dRate.
     *
     */
    public void setDRate(double dRate) {
        this.dRate = dRate;
    }
    
    /** Getter for property dtLastUpdate.
     * @return Value of property dtLastUpdate.
     */
    public Date getDtLastUpdate() {
        return dtLastUpdate;
    }
    
    /** Setter for property dtLastUpdate.
     * @param dtLastUpdate New value of property dtLastUpdate.
     */
    public void setDtLastUpdate(Date dtLastUpdate) {
        this.dtLastUpdate = dtLastUpdate;
    }
    
}
