/*
 * PaymentDocument.java
 *
 * Created on December 29, 2004, 1:08 PM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;

/**
 *
 * @author  Administrator
 * @version 
 */
public class IjPaymentDoc {

    /** Holds value of property payId. */
    private long payId;
    
    /** Holds value of property payType. */
    private long payType;
    
    /** Holds value of property payCurrency. */
    private long payCurrency;
    
    /** Holds value of property payDueDate. */
    private Date payDueDate;
    
    /** Holds value of property payRate. */
    private double payRate;
    
    /** Holds value of property payNominal. */
    private double payNominal;
    
    /** Holds value of property payNumber. */
    private String payNumber = "";
    
    /** Holds value of property refDocNumber. */
    private String refDocNumber;
    
    /** Creates new PaymentDocument */
    public IjPaymentDoc() {
    }

    /** Getter for property payId.
     * @return Value of property payId.
     *
     */
    public long getPayId() {
        return this.payId;
    }
    
    /** Setter for property payId.
     * @param payId New value of property payId.
     *
     */
    public void setPayId(long payId) {
        this.payId = payId;
    }
    
    /** Getter for property payType.
     * @return Value of property payType.
     *
     */
    public long getPayType() {
        return this.payType;
    }
    
    /** Setter for property payType.
     * @param payType New value of property payType.
     *
     */
    public void setPayType(long payType) {
        this.payType = payType;
    }
    
    /** Getter for property payCurrency.
     * @return Value of property payCurrency.
     *
     */
    public long getPayCurrency() {
        return this.payCurrency;
    }
    
    /** Setter for property payCurrency.
     * @param payCurrency New value of property payCurrency.
     *
     */
    public void setPayCurrency(long payCurrency) {
        this.payCurrency = payCurrency;
    }
    
    /** Getter for property payDueDate.
     * @return Value of property payDueDate.
     *
     */
    public Date getPayDueDate() {
        return this.payDueDate;
    }
    
    /** Setter for property payDueDate.
     * @param payDueDate New value of property payDueDate.
     *
     */
    public void setPayDueDate(Date payDueDate) {
        this.payDueDate = payDueDate;
    }
    
    /** Getter for property payRate.
     * @return Value of property payRate.
     *
     */
    public double getPayRate() {
        return this.payRate;
    }
    
    /** Setter for property payRate.
     * @param payRate New value of property payRate.
     *
     */
    public void setPayRate(double payRate) {
    }
    
    /** Getter for property payNominal.
     * @return Value of property payNominal.
     *
     */
    public double getPayNominal() {
        return this.payNominal;
    }
    
    /** Setter for property payNominal.
     * @param payNominal New value of property payNominal.
     *
     */
    public void setPayNominal(double payNominal) {
        this.payNominal = payNominal;
    }
    
    /** Getter for property payNumber.
     * @return Value of property payNumber.
     *
     */
    public String getPayNumber() {
        return this.payNumber;
    }
    
    /** Setter for property payNumber.
     * @param payNumber New value of property payNumber.
     *
     */
    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }
    
    /** Getter for property payDocRef.
     * @return Value of property payDocRef.
     *
     */
    public String getRefDocNumber() {
        return this.refDocNumber;
    }
    
    /** Setter for property payDocRef.
     * @param payDocRef New value of property payDocRef.
     *
     */
    public void setRefDocNumber(String refDocNumber) {
        this.refDocNumber = refDocNumber;
    }
    
}
