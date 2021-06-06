/*
 * ArapPerContact.java
 *
 * Created on June 3, 2006, 11:41 PM
 */

package com.dimata.interfaces.arap;


import java.util.Date;

/**
 *
 * @author  Administrator
 */
public class ArapPerContact {
    
    /** Holds value of contact id (key) */
    private long contactId = 0;
    
    /** Holds value of contact name */
    private String contactName = "";
    
    /** Holds value of prev balance */
    private double prevBalance = 0;
    
    /** Holds value of ar/ap increment */
    private double increment = 0;
    
    /** Holds value of ar/ap decrement */
    private double decrement = 0;


    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public double getPrevBalance() {
        return prevBalance;
    }

    public void setPrevBalance(double prevBalance) {
        this.prevBalance = prevBalance;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    public double getDecrement() {
        return decrement;
    }

    public void setDecrement(double decrement) {
        this.decrement = decrement;
    }

    public double getBalance(){
        return (prevBalance+increment-decrement);
    }

    
}
