/*
 * ArapDetailPayment.java
 *
 * Created on June 9, 2006, 9:44 PM
 */

package com.dimata.interfaces.arap;

import java.util.Date;

/**
 *
 * @author  Administrator
 */
public class ArapDetailPayment {
    
    /** Holds value of paymentNo */
    private String paymentNo = "";
    
    /** Holds value of paymentDate */
    private Date paymentDate = new Date();
    
    /** Holds value of nominal */
    private double nominal = 0;

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }
    
}
