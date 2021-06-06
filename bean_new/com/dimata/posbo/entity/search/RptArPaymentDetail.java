/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.search;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author dimata005
 */
public class RptArPaymentDetail extends Entity{
    private long creditPaymentMainId = 0;
    private String payInvoiceNo = "";
    private Date payDate = new Date();
    private String refInvoiceNo = "";
    private String custName = "";
    private double totalPay = 0;
    private long paymentId=0;
    private String paymentName="";
    private double rate=0.0;
    private String currCode="";
    
    /**
     * @return the creditPaymentMainId
     */
    public long getCreditPaymentMainId() {
        return creditPaymentMainId;
    }

    /**
     * @param creditPaymentMainId the creditPaymentMainId to set
     */
    public void setCreditPaymentMainId(long creditPaymentMainId) {
        this.creditPaymentMainId = creditPaymentMainId;
    }

    /**
     * @return the payInvoiceNo
     */
    public String getPayInvoiceNo() {
        return payInvoiceNo;
    }

    /**
     * @param payInvoiceNo the payInvoiceNo to set
     */
    public void setPayInvoiceNo(String payInvoiceNo) {
        this.payInvoiceNo = payInvoiceNo;
    }

    /**
     * @return the payDate
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * @param payDate the payDate to set
     */
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    /**
     * @return the refInvoiceNo
     */
    public String getRefInvoiceNo() {
        return refInvoiceNo;
    }

    /**
     * @param refInvoiceNo the refInvoiceNo to set
     */
    public void setRefInvoiceNo(String refInvoiceNo) {
        this.refInvoiceNo = refInvoiceNo;
    }

    /**
     * @return the custName
     */
    public String getCustName() {
        return custName;
    }

    /**
     * @param custName the custName to set
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * @return the totalPay
     */
    public double getTotalPay() {
        return totalPay;
    }

    /**
     * @param totalPay the totalPay to set
     */
    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    /**
     * @return the paymentId
     */
    public long getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId the paymentId to set
     */
    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * @return the paymentName
     */
    public String getPaymentName() {
        return paymentName;
    }

    /**
     * @param paymentName the paymentName to set
     */
    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    /**
     * @return the rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * @return the currCode
     */
    public String getCurrCode() {
        return currCode;
    }

    /**
     * @param currCode the currCode to set
     */
    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }
}
