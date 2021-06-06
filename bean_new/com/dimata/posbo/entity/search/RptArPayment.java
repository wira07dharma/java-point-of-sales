/*
 * RptArPayment.java
 *
 * Created on July 20, 2005, 11:09 AM
 */

package com.dimata.posbo.entity.search;
import java.util.Date;
/**
 *
 * @author  pulantara
 */
public class RptArPayment {
    
    private long creditPaymentMainId = 0;
    private String payInvoiceNo = "";
    private Date payDate = new Date();
    private String refInvoiceNo = "";
    private String custName = "";
    private double totalPay = 0;    
    
    /** Creates a new instance of RptArPayment */
    public RptArPayment() {
    }
    
    /**
     * Getter for property creditPaymentMainId.
     * @return Value of property creditPaymentMainId.
     */
    public long getCreditPaymentMainId() {
        return creditPaymentMainId;
    }
    
    /**
     * Setter for property creditPaymentMainId.
     * @param creditPaymentMainId New value of property creditPaymentMainId.
     */
    public void setCreditPaymentMainId(long creditPaymentMainId) {
        this.creditPaymentMainId = creditPaymentMainId;
    }
    
    /**
     * Getter for property payInvoiceNo.
     * @return Value of property payInvoiceNo.
     */
    public java.lang.String getPayInvoiceNo() {
        return payInvoiceNo;
    }
    
    /**
     * Setter for property payInvoiceNo.
     * @param payInvoiceNo New value of property payInvoiceNo.
     */
    public void setPayInvoiceNo(java.lang.String payInvoiceNo) {
        this.payInvoiceNo = payInvoiceNo;
    }
    
    /**
     * Getter for property payDate.
     * @return Value of property payDate.
     */
    public java.util.Date getPayDate() {
        return payDate;
    }
    
    /**
     * Setter for property payDate.
     * @param payDate New value of property payDate.
     */
    public void setPayDate(java.util.Date payDate) {
        this.payDate = payDate;
    }
    
    /**
     * Getter for property refInvoiceNo.
     * @return Value of property refInvoiceNo.
     */
    public java.lang.String getRefInvoiceNo() {
        return refInvoiceNo;
    }
    
    /**
     * Setter for property refInvoiceNo.
     * @param refInvoiceNo New value of property refInvoiceNo.
     */
    public void setRefInvoiceNo(java.lang.String refInvoiceNo) {
        this.refInvoiceNo = refInvoiceNo;
    }
    
    /**
     * Getter for property custName.
     * @return Value of property custName.
     */
    public java.lang.String getCustName() {
        return custName;
    }
    
    /**
     * Setter for property custName.
     * @param custName New value of property custName.
     */
    public void setCustName(java.lang.String custName) {
        this.custName = custName;
    }
    
    /**
     * Getter for property totalPay.
     * @return Value of property totalPay.
     */
    public double getTotalPay() {
        return totalPay;
    }
    
    /**
     * Setter for property totalPay.
     * @param totalPay New value of property totalPay.
     */
    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }
    
}
