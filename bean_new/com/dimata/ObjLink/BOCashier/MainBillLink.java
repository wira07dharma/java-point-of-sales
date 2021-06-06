/*
 * MainBill.java
 *
 * Created on February 26, 2005, 1:42 PM
 */

package com.dimata.ObjLink.BOCashier;

/**
 *
 * @author  wpradnyana
 */
public class MainBillLink
{

    public static final int DOC_TYPE_CASH = 0;
    public static final int DOC_TYPE_COMPLIMENT = 1;
    public static final int DOC_TYPE_HOUSE_USE = 2;

    
    /**
     * Holds value of property userId.
     */
    private long userId;
    
    /**
     * Holds value of property billMainId.
     */
    private long billMainId;
    
    /**
     * Holds value of property billDate.
     */
    private java.util.Date billDate;
    
    /**
     * Holds value of property invoiceNumber.
     */
    private String invoiceNumber;
    
    /**
     * Holds value of property disc.
     */
    private double disc;
    
    /**
     * Holds value of property discType.
     */
    //tidak dipakao di hanoman : data berupa 0 dan 1, by percent/by value
    private int discType;
    
    /**
     * Holds value of property docType.
     */
    private int docType;
    
    /**
     * Holds value of property locationId.
     */
    private long locationId;
    
    /**
     * Holds value of property salesCode.
     */
    private String salesCode;
    
    /**
     * Holds value of property servicePct.
     */
    private double servicePct;
    
    /**
     * Holds value of property serviceValue.
     */
    private double serviceValue;
    
    /**
     * Holds value of property shiftId.
     */
    private long shiftId;
    
    /**
     * Holds value of property taxPct.
     */
    private double taxPct;
    
    /**
     * Holds value of property transStatus.
     */
    private int transStatus;
    
    /**
     * Holds value of property transType.
     */
    private int transType;
    
    /**
     * Holds value of property reservationId.
     */
    private long reservationId;
    
    /**
     * Holds value of property soldRate.
     */
    private double soldRate;
    
    /**
     * Holds value of property soldCurrency.
     */
    private int soldCurrency;
    private double taxValue;
    private long customerId;
    private double totalAmount;
    
    /** Creates a new instance of MainBill */
    public MainBillLink ()
    {
    }
    
    /**
     * Getter for property userId.
     * @return Value of property userId.
     */
    public long getUserId ()
    {
        return this.userId;
    }
    
    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId (long userId)
    {
        this.userId = userId;
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
     * Getter for property billDate.
     * @return Value of property billDate.
     */
    public java.util.Date getBillDate ()
    {
        return this.billDate;
    }
    
    /**
     * Setter for property billDate.
     * @param billDate New value of property billDate.
     */
    public void setBillDate (java.util.Date billDate)
    {
        this.billDate = billDate;
    }
    
    /**
     * Getter for property invoiceNumber.
     * @return Value of property invoiceNumber.
     */
    public String getInvoiceNumber ()
    {
        return this.invoiceNumber;
    }
    
    /**
     * Setter for property invoiceNumber.
     * @param invoiceNumber New value of property invoiceNumber.
     */
    public void setInvoiceNumber (String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }
    
    /**
     * Getter for property disc.
     * @return Value of property disc.
     */
    public double getDisc ()
    {
        return this.disc;
    }
    
    /**
     * Setter for property disc.
     * @param disc New value of property disc.
     */
    public void setDisc (double disc)
    {
        this.disc = disc;
    }
    
    /**
     * Getter for property discType.
     * @return Value of property discType.
     */
    public int getDiscType ()
    {
        return this.discType;
    }
    
    /**
     * Setter for property discType.
     * @param discType New value of property discType.
     */
    public void setDiscType (int discType)
    {
        this.discType = discType;
    }
    
    /**
     * Getter for property docType.
     * @return Value of property docType.
     */
    public int getDocType ()
    {
        return this.docType;
    }
    
    /**
     * Setter for property docType.
     * @param docType New value of property docType.
     */
    public void setDocType (int docType)
    {
        this.docType = docType;
    }
    
    /**
     * Getter for property locationId.
     * @return Value of property locationId.
     */
    public long getLocationId ()
    {
        return this.locationId;
    }
    
    /**
     * Setter for property locationId.
     * @param locationId New value of property locationId.
     */
    public void setLocationId (long locationId)
    {
        this.locationId = locationId;
    }
    
    /**
     * Getter for property salesCode.
     * @return Value of property salesCode.
     */
    public String getSalesCode ()
    {
        return this.salesCode;
    }
    
    /**
     * Setter for property salesCode.
     * @param salesCode New value of property salesCode.
     */
    public void setSalesCode (String salesCode)
    {
        this.salesCode = salesCode;
    }
    
    /**
     * Getter for property servicePct.
     * @return Value of property servicePct.
     */
    public double getServicePct ()
    {
        return this.servicePct;
    }
    
    /**
     * Setter for property servicePct.
     * @param servicePct New value of property servicePct.
     */
    public void setServicePct (double servicePct)
    {
        this.servicePct = servicePct;
    }
    
    /**
     * Getter for property serviceValue.
     * @return Value of property serviceValue.
     */
    public double getServiceValue ()
    {
        return this.serviceValue;
    }
    
    /**
     * Setter for property serviceValue.
     * @param serviceValue New value of property serviceValue.
     */
    public void setServiceValue (double serviceValue)
    {
        this.serviceValue = serviceValue;
    }
    
    /**
     * Getter for property shiftId.
     * @return Value of property shiftId.
     */
    public long getShiftId ()
    {
        return this.shiftId;
    }
    
    /**
     * Setter for property shiftId.
     * @param shiftId New value of property shiftId.
     */
    public void setShiftId (long shiftId)
    {
        this.shiftId = shiftId;
    }
    
    /**
     * Getter for property taxPct.
     * @return Value of property taxPct.
     */
    public double getTaxPct ()
    {
        return this.taxPct;
    }
    
    /**
     * Setter for property taxPct.
     * @param taxPct New value of property taxPct.
     */
    public void setTaxPct (double taxPct)
    {
        this.taxPct = taxPct;
    }
    
    /**
     * Getter for property transStatus.
     * @return Value of property transStatus.
     */
    public int getTransStatus ()
    {
        return this.transStatus;
    }
    
    /**
     * Setter for property transStatus.
     * @param transStatus New value of property transStatus.
     */
    public void setTransStatus (int transStatus)
    {
        this.transStatus = transStatus;
    }
    
    /**
     * Getter for property transType.
     * @return Value of property transType.
     */
    public int getTransType ()
    {
        return this.transType;
    }
    
    /**
     * Setter for property transType.
     * @param transType New value of property transType.
     */
    public void setTransType (int transType)
    {
        this.transType = transType;
    }
    
    /**
     * Getter for property reservationId.
     * @return Value of property reservationId.
     */
    public long getReservationId ()
    {
        return this.reservationId;
    }
    
    /**
     * Setter for property reservationId.
     * @param reservationId New value of property reservationId.
     */
    public void setReservationId (long reservationId)
    {
        this.reservationId = reservationId;
    }
    
    /**
     * Getter for property soldRate.
     * @return Value of property soldRate.
     */
    public double getSoldRate ()
    {
        return this.soldRate;
    }
    
    /**
     * Setter for property soldRate.
     * @param soldRate New value of property soldRate.
     */
    public void setSoldRate (double soldRate)
    {
        this.soldRate = soldRate;
    }
    
    /**
     * Getter for property soldCurrency.
     * @return Value of property soldCurrency.
     */
    public int getSoldCurrency ()
    {
        return this.soldCurrency;
    }
    
    /**
     * Setter for property soldCurrency.
     * @param soldCurrency New value of property soldCurrency.
     */
    public void setSoldCurrency (int soldCurrency)
    {
        this.soldCurrency = soldCurrency;
    }

    public double getTaxValue(){ return taxValue; }    

    public void setTaxValue(double taxValue){ this.taxValue = taxValue; }

    public long getCustomerId(){ return customerId; }

    public void setCustomerId(long customerId){ this.customerId = customerId; }

    public double getTotalAmount(){ return totalAmount; }

    public void setTotalAmount(double totalAmount){ this.totalAmount = totalAmount; }
}
