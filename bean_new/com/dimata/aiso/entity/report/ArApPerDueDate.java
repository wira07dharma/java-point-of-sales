package com.dimata.aiso.entity.report;

import java.util.Date;

/**
 * User: pulantara
 * Date: Oct 24, 2005
 * Time: 3:09:23 PM
 * Description:
 */
public class ArApPerDueDate {
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
