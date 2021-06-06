package com.dimata.aiso.entity.report;

import java.util.Vector;

/**
 * User: pulantara
 * Date: Oct 26, 2005
 * Time: 3:16:18 PM
 * Description:
 */
public class ArApDetail {

    /** Holds value of contact id */
    private long contactId = 0;
    /** Holds value of contact name */
    private String contactName = "";
    /** Holds value of totalNominal */
    private double totalNominal = 0;
    /** Holds value of totalPay */
    private double totalPay = 0;
    /** Holds vector of ArApDetailItem */
    private Vector detail = new Vector();
    /** Holds vector of ArApDetailPayment */
    private Vector payment = new Vector();

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

    public double getTotalNominal() {
        return totalNominal;
    }

    public void setTotalNominal(double totalNominal) {
        this.totalNominal = totalNominal;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public double getTotalBalance() {
        return totalNominal-totalPay;
    }


    public Vector getDetail() {
        return detail;
    }

    public void setDetail(Vector detail) {
        this.detail = detail;
    }

    public Vector getPayment() {
        return payment;
    }

    public void setPayment(Vector payment) {
        this.payment = payment;
    }
}
