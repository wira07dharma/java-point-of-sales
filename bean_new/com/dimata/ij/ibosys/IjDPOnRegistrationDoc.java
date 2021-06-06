/*
 * IjDPOnRegistrationDoc.java
 *
 * Created on February 1, 2006, 9:52 PM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class IjDPOnRegistrationDoc {
    
    /** Holds value of property lDocOid. */
    private long lDocOid;
    
    /** Holds value of property sNumber. */
    private String sNumber;
    
    /** Holds value of property dtTransDate. */
    private Date dtTransDate;
    
    /** Holds value of property lLocation. */
    private long lLocation;
    
    /** Holds value of property lTransCurrency. */
    private long lTransCurrency;
    
    /** Holds value of property iSaleType. */
    private int iSaleType;
    
    /** Holds value of property lProdDepartment. */
    private long lProdDepartment;
    
    /** Holds value of property dtDueDate. */
    private Date dtDueDate;
    
    /** Holds value of property dNominal. */
    private double dNominal;
    
    /** Holds value of property dRate. */
    private double dRate;
    
    /** Holds value of property lPaymentType. */
    private long lPaymentType;
    
    /** Holds value of property dtLastUpdate. */
    private Date dtLastUpdate;
    
    /**
     * Holds value of property lDocVillaId.
     */
    private long lDocVillaId;
    
    /**
     * Holds value of property lDocGuestId.
     */
    private long lDocGuestId;
    
    /** Creates new IjDPOnRegistrationDoc */
    public IjDPOnRegistrationDoc() {
    }

    /** Getter for property docId.
     * @return Value of property docId.
     *
     */
    public long getLDocOid() {
        return this.lDocOid;
    }
    
    /** Setter for property docId.
     * @param docId New value of property docId.
     *
     */
    public void setLDocOid(long LDocOid) {
        this.lDocOid = LDocOid;
    }
    
    /** Getter for property docNumber.
     * @return Value of property docNumber.
     *
     */
    public String getSNumber() {
        return this.sNumber;
    }
    
    /** Setter for property docNumber.
     * @param docNumber New value of property docNumber.
     *
     */
    public void setSNumber(String SNumber) {
        this.sNumber = SNumber;
    }
    
    /** Getter for property docTransDate.
     * @return Value of property docTransDate.
     *
     */
    public Date getDtTransDate() {
        return this.dtTransDate;
    }
    
    /** Setter for property docTransDate.
     * @param docTransDate New value of property docTransDate.
     *
     */
    public void setDtTransDate(Date dtTransDate) {
        this.dtTransDate = dtTransDate;
    }
    
    /** Getter for property docLocation.
     * @return Value of property docLocation.
     *
     */
    public long getLLocation() {
        return this.lLocation;
    }
    
    /** Setter for property docLocation.
     * @param docLocation New value of property docLocation.
     *
     */
    public void setLLocation(long LLocation) {
        this.lLocation = LLocation;
    }
    
    /** Getter for property docTransCurrency.
     * @return Value of property docTransCurrency.
     *
     */
    public long getLTransCurrency() {
        return this.lTransCurrency;
    }
    
    /** Setter for property docTransCurrency.
     * @param docTransCurrency New value of property docTransCurrency.
     *
     */
    public void setLTransCurrency(long LTransCurrency) {
        this.lTransCurrency = LTransCurrency;
    }
    
    /** Getter for property docDueDate.
     * @return Value of property docDueDate.
     *
     */
    public Date getDtDueDate() {
        return this.dtDueDate;
    }
    
    /** Setter for property docDueDate.
     * @param docDueDate New value of property docDueDate.
     *
     */
    public void setDtDueDate(Date dtDueDate) {
        this.dtDueDate = dtDueDate;
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
    
    /**
     * Getter for property lDocVillaId.
     * @return Value of property lDocVillaId.
     */
    public long getLDocVillaId() {
        return this.lDocVillaId;
    }
    
    /**
     * Setter for property lDocVillaId.
     * @param lDocVillaId New value of property lDocVillaId.
     */
    public void setLDocVillaId(long lDocVillaId) {
        this.lDocVillaId = lDocVillaId;
    }
    
    /**
     * Getter for property lDocGuestId.
     * @return Value of property lDocGuestId.
     */
    public long getLDocGuestId() {
        return this.lDocGuestId;
    }
    
    /**
     * Setter for property lDocGuestId.
     * @param lDocGuestId New value of property lDocGuestId.
     */
    public void setLDocGuestId(long lDocGuestId) {
        this.lDocGuestId = lDocGuestId;
    }
    
}
