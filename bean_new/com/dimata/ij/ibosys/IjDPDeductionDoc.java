/*
 * DPDeductionDoc.java
 *
 * Created on January 7, 2005, 8:34 AM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;

/**
 *
 * @author  gedhy
 */
public class IjDPDeductionDoc {    
    
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
    
    /** Creates a new instance of DPDeductionDoc */
    public IjDPDeductionDoc() {
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
}
