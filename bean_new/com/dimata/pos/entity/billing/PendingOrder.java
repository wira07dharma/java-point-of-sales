/*
 * PendingOrder.java
 *
 * Created on December 17, 2004, 3:42 PM
 */

package com.dimata.pos.entity.billing;

import com.dimata.qdep.entity.Entity;
import java.util.Date;
/**
 *
 * @author  wpradnyana
 */
public class PendingOrder extends Entity{

   /** Creates a new instance of PendingOrder */
    public PendingOrder() {
    }

    public int getStatusPosted() {
        return statusPosted;
    }

    public void setStatusPosted(int statusPosted) {
        this.statusPosted = statusPosted;
    }

    /**
     * Getter for property salesId.
     * @return Value of property salesId.
     */
    public long getSalesId ()
    {
        return salesId;
    }
    
    /**
     * Setter for property salesId.
     * @param salesId New value of property salesId.
     */
    public void setSalesId (long salesId)
    {
        this.salesId = salesId;
    }
    
    /**
     * Getter for property memberId.
     * @return Value of property memberId.
     */
    public long getMemberId ()
    {
        return memberId;
    }
    
    /**
     * Setter for property memberId.
     * @param memberId New value of property memberId.
     */
    public void setMemberId (long memberId)
    {
        this.memberId = memberId;
    }
    
    /**
     * Getter for property poNumber.
     * @return Value of property poNumber.
     */
    public java.lang.String getPoNumber ()
    {
        return poNumber;
    }
    
    /**
     * Setter for property poNumber.
     * @param poNumber New value of property poNumber.
     */
    public void setPoNumber (java.lang.String poNumber)
    {
        this.poNumber = poNumber;
    }
    
    /**
     * Getter for property poCounter.
     * @return Value of property poCounter.
     */
    public int getPoCounter ()
    {
        return poCounter;
    }
    
    /**
     * Setter for property poCounter.
     * @param poCounter New value of property poCounter.
     */
    public void setPoCounter (int poCounter)
    {
        this.poCounter = poCounter;
    }
    
    /**
     * Getter for property orderNumber.
     * @return Value of property orderNumber.
     */
    public java.lang.String getOrderNumber ()
    {
        return orderNumber;
    }
    
    /**
     * Setter for property orderNumber.
     * @param orderNumber New value of property orderNumber.
     */
    public void setOrderNumber (java.lang.String orderNumber)
    {
        this.orderNumber = orderNumber;
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName ()
    {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName (java.lang.String name)
    {
        this.name = name;
    }
    
    /**
     * Getter for property phone.
     * @return Value of property phone.
     */
    public java.lang.String getPhone ()
    {
        return phone;
    }
    
    /**
     * Setter for property phone.
     * @param phone New value of property phone.
     */
    public void setPhone (java.lang.String phone)
    {
        this.phone = phone;
    }
    
    /**
     * Getter for property address.
     * @return Value of property address.
     */
    public java.lang.String getAddress ()
    {
        return address;
    }
    
    /**
     * Setter for property address.
     * @param address New value of property address.
     */
    public void setAddress (java.lang.String address)
    {
        this.address = address;
    }
    
    /**
     * Getter for property creationDate.
     * @return Value of property creationDate.
     */
    public Date getCreationDate()
    {
        return creationDate;
    }
    
    /**
     * Setter for property creationDate.
     * @param creationDate New value of property creationDate.
     */
    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }
    
    /**
     * Getter for property planTakenDate.
     * @return Value of property planTakenDate.
     */
    public Date getPlanTakenDate()
    {
        return planTakenDate;
    }
    
    /**
     * Setter for property planTakenDate.
     * @param planTakenDate New value of property planTakenDate.
     */
    public void setPlanTakenDate(Date planTakenDate)
    {
        this.planTakenDate = planTakenDate;
    }
    
    /**
     * Getter for property expiredDate.
     * @return Value of property expiredDate.
     */
    public Date getExpiredDate()
    {
        return expiredDate;
    }
    
    /**
     * Setter for property expiredDate.
     * @param expiredDate New value of property expiredDate.
     */
    public void setExpiredDate(Date expiredDate)
    {
        this.expiredDate = expiredDate;
    }
    
    /**
     * Getter for property downPayment.
     * @return Value of property downPayment.
     */
    public double getDownPayment ()
    {
        return downPayment;
    }
    
    /**
     * Setter for property downPayment.
     * @param downPayment New value of property downPayment.
     */
    public void setDownPayment (double downPayment)
    {
        this.downPayment = downPayment;
    }
    
    /**
     * Getter for property rate.
     * @return Value of property rate.
     */
    public double getRate ()
    {
        return rate;
    }
    
    /**
     * Setter for property rate.
     * @param rate New value of property rate.
     */
    public void setRate (double rate)
    {
        this.rate = rate;
    }
    
    /**
     * Getter for property cashierId.
     * @return Value of property cashierId.
     */
    public long getCashierId ()
    {
        return cashierId;
    }
    
    /**
     * Setter for property cashierId.
     * @param cashierId New value of property cashierId.
     */
    public void setCashierId (long cashierId)
    {
        this.cashierId = cashierId;
    }
    
    /**
     * Getter for property currencyId.
     * @return Value of property currencyId.
     */
    public long getCurrencyId ()
    {
        return currencyId;
    }
    
    /**
     * Setter for property currencyId.
     * @param currencyId New value of property currencyId.
     */
    public void setCurrencyId (long currencyId)
    {
        this.currencyId = currencyId;
    }
    
    /**
     * Getter for property pendingOrderStatus.
     * @return Value of property pendingOrderStatus.
     */
    public int getPendingOrderStatus() {
        return pendingOrderStatus;
    }    
        
    /**
     * Setter for property pendingOrderStatus.
     * @param pendingOrderStatus New value of property pendingOrderStatus.
     */
    public void setPendingOrderStatus(int pendingOrderStatus) {
        this.pendingOrderStatus = pendingOrderStatus;
    }
    
    /**
     * Getter for property paymentStatus.
     * @return Value of property paymentStatus.
     */
    public int getPaymentStatus() {
        return paymentStatus;
    }
    
    /**
     * Setter for property paymentStatus.
     * @param paymentStatus New value of property paymentStatus.
     */
    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    private long salesId;
    private long memberId;
    private String poNumber;
    private int poCounter;
    private String orderNumber;
    private String name;
    private String phone;
    private String address;
    private Date creationDate = new Date();
    private Date planTakenDate = new Date();
    private Date expiredDate = new Date();
    private double downPayment ;
    private double rate=0;
    private long cashierId=0;
    private long currencyId=0; 
    private int pendingOrderStatus=0;
    private int paymentStatus;
    private int statusPosted = 0 ;
}
