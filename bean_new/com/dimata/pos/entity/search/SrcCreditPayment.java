/*
 * SrcCreditPayment.java
 *
 * Created on February 9, 2005, 5:58 PM
 */

package com.dimata.pos.entity.search;

/**
 *
 * @author  wpradnyana
 */
public class SrcCreditPayment
{
    
    /**
     * Holds value of property refTransactionNumber.
     */
    private String refTransactionNumber;
    
    /**
     * Holds value of property paymentCode.
     */
    private String paymentCode;
    
    /**
     * Holds value of property customerName.
     */
    private String customerName;
    
    /** Creates a new instance of SrcCreditPayment */
    public SrcCreditPayment ()
    {
    }
    
    /**
     * Getter for property refTransactionNumber.
     * @return Value of property refTransactionNumber.
     */
    public String getRefTransactionNumber ()
    {
        return this.refTransactionNumber;
    }
    
    /**
     * Setter for property refTransactionNumber.
     * @param refTransactionNumber New value of property refTransactionNumber.
     */
    public void setRefTransactionNumber (String refTransactionNumber)
    {
        this.refTransactionNumber = refTransactionNumber;
    }
    
    /**
     * Getter for property paymentCode.
     * @return Value of property paymentCode.
     */
    public String getPaymentCode ()
    {
        return this.paymentCode;
    }
    
    /**
     * Setter for property paymentCode.
     * @param paymentCode New value of property paymentCode.
     */
    public void setPaymentCode (String paymentCode)
    {
        this.paymentCode = paymentCode;
    }
    
    /**
     * Getter for property customerName.
     * @return Value of property customerName.
     */
    public String getCustomerName ()
    {
        return this.customerName;
    }
    
    /**
     * Setter for property customerName.
     * @param customerName New value of property customerName.
     */
    public void setCustomerName (String customerName)
    {
        this.customerName = customerName;
    }
    
}
