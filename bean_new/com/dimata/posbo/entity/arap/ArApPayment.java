
package com.dimata.posbo.entity.arap;

// package qdep
import com.dimata.qdep.entity.*;

import java.util.Date;

public class ArApPayment extends Entity {

    private String paymentNo = "";
    private Date paymentDate = new Date();
    private long idPerkiraanPayment = 0;
    private long contactId = 0;
    private long idCurrency = 0;
    private int counter = 0;
    private double rate = 0.0;
    private double amount = 0.0;
    private int paymentStatus = 0;
    private long arapMainId = 0;
    private long receiveAktivaId = 0;
    private long sellingAktivaId = 0;
    private double leftToPay = 0;
    private int arApType = 0;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(long idCurrency) {
        this.idCurrency = idCurrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

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

    public long getIdPerkiraanPayment() {
        return idPerkiraanPayment;
    }

    public void setIdPerkiraanPayment(long idPerkiraanPayment) {
        this.idPerkiraanPayment = idPerkiraanPayment;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public long getArapMainId() {
        return arapMainId;
    }

    public void setArapMainId(long arapMainId) {
        this.arapMainId = arapMainId;
    }

    public long getReceiveAktivaId() {
        return receiveAktivaId;
    }

    public void setReceiveAktivaId(long receiveAktivaId) {
        this.receiveAktivaId = receiveAktivaId;
    }

    public long getSellingAktivaId() {
        return sellingAktivaId;
    }

    public void setSellingAktivaId(long sellingAktivaId) {
        this.sellingAktivaId = sellingAktivaId;
    }

    public double getLeftToPay() {
        return leftToPay;
    }

    public void setLeftToPay(double leftToPay) {
        this.leftToPay = leftToPay;
    }

    public int getArApType() {
        return arApType;
    }

    public void setArApType(int arApType) {
        this.arApType = arApType;
    }
}