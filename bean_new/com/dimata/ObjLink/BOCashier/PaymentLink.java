/*
 * Payment.java
 *
 * Created on February 26, 2005, 1:41 PM
 */

package com.dimata.ObjLink.BOCashier;

import java.util.Date;

/**
 *
 * @author  wpradnyana
 */
public class PaymentLink
{

    public static final int PAYMENT_TYPE_CASH 			= 0;
    public static final int PAYMENT_TYPE_CC 			= 1;
    public static final int PAYMENT_TYPE_BANK_TRANSFER 	= 2;
    public static final int PAYMENT_TYPE_OUTLET_OTHER 	= 3;    //no 3 & 4 hanya digunakan untuk outlet system
    public static final int PAYMENT_TYPE_INCLUDE_TO_ROOM_BILL = 4;
    public static final int PAYMENT_TYPE_CITY_LEDGER = 5;
    public static final int PAYMENT_TYPE_DEBET_CARD = 6;
    public static final int PAYMENT_TYPE_CHEQUE = 7;

    /**
     * Holds value of property paymentId.
     */
    private long paymentId;
    
    /**
     * Holds value of property amount.
     */
    private double amount;
    
    /**
     * Holds value of property billMainId.
     */
    private long billMainId;
    
    /**
     * Holds value of property rateUsed.
     */
    private double rateUsed;
    
    /**
     * Holds value of property paymentType.
     */
    private int paymentType;
    
    /**
     * Holds value of property payDateTime.
     */
    private java.util.Date payDateTime;

    //basic amount dibatasi dalam 2 currency saja yaitu IDR & USD
    private double basicAmount;
    private String bankName;
    private String holderName;
    private String ccName;
    private String ccNumber;
    private Date expDate;
    private int paymentCurrency;
    
    /** Creates a new instance of Payment */
    public PaymentLink ()
    {
    }
    
    /**
     * Getter for property paymentId.
     * @return Value of property paymentId.
     */
    public long getPaymentId ()
    {
        return this.paymentId;
    }
    
    /**
     * Setter for property paymentId.
     * @param paymentId New value of property paymentId.
     */
    public void setPaymentId (long paymentId)
    {
        this.paymentId = paymentId;
    }
    
    /**
     * Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount ()
    {
        return this.amount;
    }
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount (double amount)
    {
        this.amount = amount;
    }
    
    /**
     * Getter for property billMainId.
     * @return Value of property billMainId.
     */
    public long getBillMainId ()
    {
        return this.billMainId;
    }
    
    /**
     * Setter for property billMainId.
     * @param billMainId New value of property billMainId.
     */
    public void setBillMainId (long billMainId)
    {
        this.billMainId = billMainId;
    }    
    
    /**
     * Getter for property rateUsed.
     * @return Value of property rateUsed.
     */
    public double getRateUsed ()
    {
        return this.rateUsed;
    }
    
    /**
     * Setter for property rateUsed.
     * @param rateUsed New value of property rateUsed.
     */
    public void setRateUsed (double rateUsed)
    {
        this.rateUsed = rateUsed;
    }
    
    /**
     * Getter for property paymentType.
     * @return Value of property paymentType.
     */
    public int getPaymentType ()
    {
        return this.paymentType;
    }
    
    /**
     * Setter for property paymentType.
     * @param paymentType New value of property paymentType.
     */
    public void setPaymentType (int paymentType)
    {
        this.paymentType = paymentType;
    }
    
    /**
     * Getter for property payDateTime.
     * @return Value of property payDateTime.
     */
    public java.util.Date getPayDateTime ()
    {
        return this.payDateTime;
    }
    
    /**
     * Setter for property payDateTime.
     * @param payDateTime New value of property payDateTime.
     */
    public void setPayDateTime (java.util.Date payDateTime)
    {
        this.payDateTime = payDateTime;
    }

    public double getBasicAmount(){ return basicAmount; }    

    public void setBasicAmount(double basicAmount){ this.basicAmount = basicAmount; }

    public String getBankName(){ return bankName; }

    public void setBankName(String bankName){ this.bankName = bankName; }

    public String getHolderName(){ return holderName; }

    public void setHolderName(String holderName){ this.holderName = holderName; }

    public String getCcName(){ return ccName; }

    public void setCcName(String ccName){ this.ccName = ccName; }

    public String getCcNumber(){ return ccNumber; }

    public void setCcNumber(java.lang.String ccNumber){ this.ccNumber = ccNumber; }

    public Date getExpDate(){ return expDate; }

    public void setExpDate(Date expDate){ this.expDate = expDate; }


    //mengacu ke currency type di CustomerLink
    public int getPaymentCurrency(){ return paymentCurrency; }

    public void setPaymentCurrency(int paymentCurrency){ this.paymentCurrency = paymentCurrency; }
}
