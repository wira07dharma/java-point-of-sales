/*
 * CustomerLink.java
 *
 * Created on February 26, 2005, 2:14 PM
 */

package com.dimata.ObjLink.BOCashier;

/**
 *
 * @author  wpradnyana
 */
public class CustomerLink
{

    public static final int CURRENCY_RP = 0;
    public static final int CURRENCY_USD = 1;

    /**
     * Holds value of property customerId.
     */
    private long customerId;
    
    /**
     * Holds value of property reservationId.
     */
    private long reservationId;
    
    /**
     * Holds value of property customerName.
     */
    private String customerName;    
    
    /** Creates a new instance of CustomerLink */
    public CustomerLink ()
    {
    }
    
    /**
     * Getter for property customerId.
     * @return Value of property customerId.
     */
    public long getCustomerId ()
    {
        return this.customerId;
    }
    
    /**
     * Setter for property customerId.
     * @param customerId New value of property customerId.
     */
    public void setCustomerId (long customerId)
    {
        this.customerId = customerId;
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

    public int getCurrency(){ return currency; }

    public void setCurrency(int currency){ this.currency = currency; }

    public String getRoomNumber(){ return roomNumber; }

    public void setRoomNumber(String roomNumber){ this.roomNumber = roomNumber; }

    public String getResNumber(){ return resNumber; }

    public void setResNumber(String resNumber){ this.resNumber = resNumber; }
    
    private int currency;
    private String roomNumber;
    private String resNumber;
}
