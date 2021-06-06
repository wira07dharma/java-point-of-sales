package com.dimata.pos.entity.search; 
 
import java.util.Date;

/* package java */ 
public class SrcPendingOrder
{ 
    
    public static final String SESS_SRC_PENDING_ORDER = "SESS_SRC_PENDING_ORDER"; 
    public static final int STATUS_OPENED=0;
    public static final int STATUS_CLOSED=1;
    public static final int STATUS_EXPIRED=2;
    public static final int STATUS_DELETED=3;
    
    
    /**
     * Holds value of property dateCreatedFrom.
     */
    private Date dateCreatedFrom;
    
    /**
     * Holds value of property dateCreatedTo.
     */
    private Date dateCreatedTo;
    
    /**
     * Holds value of property salesName.
     */
    private String salesName;
    
    /**
     * Holds value of property customerName.
     */
    private String customerName;
    
    /**
     * Holds value of property dateExpiredTo.
     */
    private Date dateExpiredTo;
    
    /**
     * Holds value of property dateExpiredFrom.
     */
    private Date dateExpiredFrom;
    
    /**
     * Holds value of property dateFinishedFrom.
     */
    private Date dateFinishedFrom;
    
    /**
     * Holds value of property boxOrderCode.
     */
    private String boxOrderCode;
    
    /**
     * Holds value of property sortMethod.
     */
    private int sortMethod;
    
    /**
     * Holds value of property locationId.
     */
    private long locationId;
    
    /**
     * Holds value of property shiftId.
     */
    private long shiftId;
    
    /**
     * Holds value of property operatorId.
     */
    private long operatorId;
    
    /**
     * Holds value of property dateFinishedTo.
     */
    private Date dateFinishedTo;
    
    /**
     * Holds value of property downaPaymentValue.
     */
    private double downaPaymentValue;
    
    /**
     * Holds value of property locationName.
     */
    private String locationName;
    
    /**
     * Holds value of property operatorName.
     */
    private String operatorName;
    
    /**
     * Holds value of property shiftName.
     */
    private String shiftName;
    
    /**
     * Holds value of property salesId.
     */
    private long salesId;
    
    /**
     * Holds value of property memberName.
     */
    private String memberName;
    
    /**
     * Holds value of property memberId.
     */
    private long memberId;
    
    /**
     * Holds value of property paidInvoiceNumber.
     */
    private String paidInvoiceNumber;
    
    /**
     * Holds value of property paidInvoiceId.
     */
    private long paidInvoiceId;
    
    /**
     * Getter for property dateFrom.
     * @return Value of property dateFrom.
     */
    public Date getDateCreatedFrom()
    {
        return this.dateCreatedFrom;
    }
    
    /**
     * Setter for property dateFrom.
     * @param dateFrom New value of property dateFrom.
     */
    public void setDateCreatedFrom(Date dateCreatedFrom)
    {
        this.dateCreatedFrom = dateCreatedFrom;
    }
    
    /**
     * Getter for property dateTo.
     * @return Value of property dateTo.
     */
    public Date getDateCreatedTo()
    {
        return this.dateCreatedTo;
    }
    
    /**
     * Setter for property dateTo.
     * @param dateTo New value of property dateTo.
     */
    public void setDateCreatedTo(Date dateCreatedTo)
    {
        this.dateCreatedTo = dateCreatedTo;
    }
    
    /**
     * Getter for property salesName.
     * @return Value of property salesName.
     */
    public String getSalesName ()
    {
        return this.salesName;
    }
    
    /**
     * Setter for property salesName.
     * @param salesName New value of property salesName.
     */
    public void setSalesName (String salesName)
    {
        this.salesName = salesName;
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
    
    /**
     * Getter for property finishedDate.
     * @return Value of property finishedDate.
     */
    public Date getDateExpiredTo()
    {
        return this.dateExpiredTo;
    }
    
    /**
     * Setter for property finishedDate.
     * @param finishedDate New value of property finishedDate.
     */
    public void setDateExpiredTo(Date dateExpiredTo)
    {
        this.dateExpiredTo = dateExpiredTo;
    }
    
    /**
     * Getter for property expiredDate.
     * @return Value of property expiredDate.
     */
    public Date getDateExpiredFrom()
    {
        return this.dateExpiredFrom;
    }
    
    /**
     * Setter for property expiredDate.
     * @param expiredDate New value of property expiredDate.
     */
    public void setDateExpiredFrom(Date dateExpiredFrom)
    {
        this.dateExpiredFrom = dateExpiredFrom;
    }
    
    /**
     * Getter for property dateFinishedFrom.
     * @return Value of property dateFinishedFrom.
     */
    public Date getDateFinishedFrom()
    {
        return this.dateFinishedFrom;
    }
    
    /**
     * Setter for property dateFinishedFrom.
     * @param dateFinishedFrom New value of property dateFinishedFrom.
     */
    public void setDateFinishedFrom(Date dateFinishedFrom)
    {
        this.dateFinishedFrom = dateFinishedFrom;
    }
    
    /**
     * Getter for property boxOrderCode.
     * @return Value of property boxOrderCode.
     */
    public String getBoxOrderCode ()
    {
        return this.boxOrderCode;
    }
    
    /**
     * Setter for property boxOrderCode.
     * @param boxOrderCode New value of property boxOrderCode.
     */
    public void setBoxOrderCode (String boxOrderCode)
    {
        this.boxOrderCode = boxOrderCode;
    }
    
    /**
     * Getter for property sortMethod.
     * @return Value of property sortMethod.
     */
    public int getSortMethod ()
    {
        return this.sortMethod;
    }
    
    /**
     * Setter for property sortMethod.
     * @param sortMethod New value of property sortMethod.
     */
    public void setSortMethod (int sortMethod)
    {
        this.sortMethod = sortMethod;
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
     * Getter for property operatorId.
     * @return Value of property operatorId.
     */
    public long getOperatorId ()
    {
        return this.operatorId;
    }
    
    /**
     * Setter for property operatorId.
     * @param operatorId New value of property operatorId.
     */
    public void setOperatorId (long operatorId)
    {
        this.operatorId = operatorId;
    }
    
    /**
     * Getter for property dateFinishedTo.
     * @return Value of property dateFinishedTo.
     */
    public Date getDateFinishedTo()
    {
        return this.dateFinishedTo;
    }
    
    /**
     * Setter for property dateFinishedTo.
     * @param dateFinishedTo New value of property dateFinishedTo.
     */
    public void setDateFinishedTo(Date dateFinishedTo)
    {
        this.dateFinishedTo = dateFinishedTo;
    }
    
    /**
     * Getter for property downaPaymentValue.
     * @return Value of property downaPaymentValue.
     */
    public double getDownaPaymentValue ()
    {
        return this.downaPaymentValue;
    }
    
    /**
     * Setter for property downaPaymentValue.
     * @param downaPaymentValue New value of property downaPaymentValue.
     */
    public void setDownaPaymentValue (double downaPaymentValue)
    {
        this.downaPaymentValue = downaPaymentValue;
    }
    
    /**
     * Getter for property locationName.
     * @return Value of property locationName.
     */
    public String getLocationName ()
    {
        return this.locationName;
    }
    
    /**
     * Setter for property locationName.
     * @param locationName New value of property locationName.
     */
    public void setLocationName (String locationName)
    {
        this.locationName = locationName;
    }
    
    /**
     * Getter for property operatorName.
     * @return Value of property operatorName.
     */
    public String getOperatorName ()
    {
        return this.operatorName;
    }
    
    /**
     * Setter for property operatorName.
     * @param operatorName New value of property operatorName.
     */
    public void setOperatorName (String operatorName)
    {
        this.operatorName = operatorName;
    }
    
    /**
     * Getter for property shiftName.
     * @return Value of property shiftName.
     */
    public String getShiftName ()
    {
        return this.shiftName;
    }
    
    /**
     * Setter for property shiftName.
     * @param shiftName New value of property shiftName.
     */
    public void setShiftName (String shiftName)
    {
        this.shiftName = shiftName;
    }
    
    /**
     * Getter for property salesId.
     * @return Value of property salesId.
     */
    public long getSalesId ()
    {
        return this.salesId;
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
     * Getter for property memberName.
     * @return Value of property memberName.
     */
    public String getMemberName ()
    {
        return this.memberName;
    }
    
    /**
     * Setter for property memberName.
     * @param memberName New value of property memberName.
     */
    public void setMemberName (String memberName)
    {
        this.memberName = memberName;
    }
    
    /**
     * Getter for property memberId.
     * @return Value of property memberId.
     */
    public long getMemberId ()
    {
        return this.memberId;
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
     * Getter for property paidInvoiceNumber.
     * @return Value of property paidInvoiceNumber.
     */
    public String getPaidInvoiceNumber ()
    {
        return this.paidInvoiceNumber;
    }    
    
    /**
     * Setter for property paidInvoiceNumber.
     * @param paidInvoiceNumber New value of property paidInvoiceNumber.
     */
    public void setPaidInvoiceNumber (String paidInvoiceNumber)
    {
        this.paidInvoiceNumber = paidInvoiceNumber;
    }
    
    /**
     * Getter for property paidInvoiceId.
     * @return Value of property paidInvoiceId.
     */
    public long getPaidInvoiceId ()
    {
        return this.paidInvoiceId;
    }
    
    /**
     * Setter for property paidInvoiceId.
     * @param paidInvoiceId New value of property paidInvoiceId.
     */
    public void setPaidInvoiceId (long paidInvoiceId)
    {
        this.paidInvoiceId = paidInvoiceId;
    }
    
}
