package com.dimata.aiso.entity.report;

import java.util.Date;

/**
 * User: pulantara
 * Date: Oct 26, 2005
 * Time: 4:29:25 PM
 * Description:
 */
public class ArApDetailPayment {

    /** Holds value of paymentNo */
    private String paymentNo = "";
    /** Holds value of paymentDate */
    private Date paymentDate = new Date();
    /** Holds value of nominal */
    private double nominal = 0;
    
    private String notaNo = "";

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
    
    public String getNotaNo(){
        return this.notaNo;
    }
    
    public void setNotaNo(String notaNo){
        this.notaNo = notaNo;
    }
}
