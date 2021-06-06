/*
 * ArapPerDueDate.java
 *
 * Created on June 3, 2006, 11:41 PM
 */

package com.dimata.interfaces.arap;


import java.util.Date;

/**
 *
 * @author  Administrator
 */
public class ArapPerDueDate {
    
    /** Holds value of due date */
    private Date dueDate = new Date();
    
    /** Holds value of receivable */
    private double receivable = 0;
    
    /** Holds value of payable */
    private double payable = 0;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getReceivable() {
        return receivable;
    }

    public void setReceivable(double receivable) {
        this.receivable = receivable;
    }

    public double getPayable() {
        return payable;
    }

    public void setPayable(double payable) {
        this.payable = payable;
    }

    public double getDiffer() {
        return receivable-payable;
    }
    
}
