/*
 * paymentTypeDoc.java
 *
 * Created on January 7, 2005, 9:03 AM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author  gedhy
 */
public class IjPaymentTypeDoc {
    
    /** Holds value of property payTransDate. */
    private Date payTransDate;
    
    /** Holds value of property payContact. */
    private long payContact;
    
    /** Holds value of property payCurrency. */
    private long payCurrency;
    
    /** Holds value of property payDueDate. */
    private Date payDueDate;
    
    /** Holds value of property payId. */
    private long payId;
    
    /** Holds value of property payNominal. */
    private double payNominal;
    
    /** Holds value of property payNumber. */
    private String payNumber;
    
    /** Holds value of property payRate. */
    private double payRate;
    
    /** Holds value of property payType. */
    private long payType;
    
    /** Holds value of property refDocNumber. */
    private String refDocNumber;
    
    /** Holds value of property dtLastUpdate. */
    private Date dtLastUpdate;
    
    /** Holds value of property payTypeClearing. */
    private long payTypeClearing;
    
    /** Creates a new instance of paymentTypeDoc */
    public IjPaymentTypeDoc() {
    }
    
    /** Getter for property docTransDate.
     * @return Value of property docTransDate.
     *
     */
    public Date getPayTransDate() {
        return this.payTransDate;
    }
    
    /** Setter for property docTransDate.
     * @param docTransDate New value of property docTransDate.
     *
     */
    public void setPayTransDate(Date payTransDate) {
        this.payTransDate = payTransDate;
    }
    
    /** Getter for property docContact.
     * @return Value of property docContact.
     *
     */
    public long getPayContact() {
        return this.payContact;
    }
    
    /** Setter for property docContact.
     * @param docContact New value of property docContact.
     *
     */
    public void setPayContact(long payContact) {
        this.payContact = payContact;
    }
    
    /** Getter for property payCurrency.
     * @return Value of property payCurrency.
     */
    public long getPayCurrency() {
        return payCurrency;
    }
    
    /** Setter for property payCurrency.
     * @param payCurrency New value of property payCurrency.
     */
    public void setPayCurrency(long payCurrency) {
        this.payCurrency = payCurrency;
    }
    
    /** Getter for property payDueDate.
     * @return Value of property payDueDate.
     */
    public Date getPayDueDate() {
        return payDueDate;
    }
    
    /** Setter for property payDueDate.
     * @param payDueDate New value of property payDueDate.
     */
    public void setPayDueDate(Date payDueDate) {
        this.payDueDate = payDueDate;
    }
    
    /** Getter for property payId.
     * @return Value of property payId.
     */
    public long getPayId() {
        return payId;
    }
    
    /** Setter for property payId.
     * @param payId New value of property payId.
     */
    public void setPayId(long payId) {
        this.payId = payId;
    }
    
    /** Getter for property payNominal.
     * @return Value of property payNominal.
     */
    public double getPayNominal() {
        return payNominal;
    }
    
    /** Setter for property payNominal.
     * @param payNominal New value of property payNominal.
     */
    public void setPayNominal(double payNominal) {
        this.payNominal = payNominal;
    }
    
    /** Getter for property payNumber.
     * @return Value of property payNumber.
     */
    public String getPayNumber() {
        return payNumber;
    }
    
    /** Setter for property payNumber.
     * @param payNumber New value of property payNumber.
     */
    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }
    
    /** Getter for property payRate.
     * @return Value of property payRate.
     */
    public double getPayRate() {
        return payRate;
    }
    
    /** Setter for property payRate.
     * @param payRate New value of property payRate.
     */
    public void setPayRate(double payRate) {
        this.payRate = payRate;
    }
    
    /** Getter for property payType.
     * @return Value of property payType.
     */
    public long getPayType() {
        return payType;
    }
    
    /** Setter for property payType.
     * @param payType New value of property payType.
     */
    public void setPayType(long payType) {
        this.payType = payType;
    }
    
    /** Getter for property refDocNumber.
     * @return Value of property refDocNumber.
     */
    public String getRefDocNumber() {
        return refDocNumber;
    }
    
    /** Setter for property refDocNumber.
     * @param refDocNumber New value of property refDocNumber.
     */
    public void setRefDocNumber(String refDocNumber) {
        this.refDocNumber = refDocNumber;
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
    
    /** Getter for property payTypeClearing.
     * @return Value of property payTypeClearing.
     */
    public long getPayTypeClearing() {
        return payTypeClearing;
    }
    
    /** Setter for property payTypeClearing.
     * @param payTypeClearing New value of property payTypeClearing.
     */
    public void setPayTypeClearing(long payTypeClearing) {
        this.payTypeClearing = payTypeClearing;
    }
    
}
