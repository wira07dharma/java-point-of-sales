
package com.dimata.aiso.entity.arap;

// package qdep
import com.dimata.qdep.entity.*;

import java.util.Date;

public class ArApMain extends Entity {

    private String voucherNo = "";
    private Date voucherDate = new Date();
    private long contactId = 0;
    private int numberOfPayment = 0;
    private long idPerkiraanLawan = 0;
    private long idPerkiraan = 0;
    private long idCurrency = 0;
    private int counter = 0;
    private double rate = 0.0;
    private double amount = 0.0;
    private String notaNo = "";
    private Date notaDate = new Date();
    private String description = "";
    private int arApMainStatus = 0;
    private int arApType = 0;
    private int arApDocStatus = 0;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
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

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public int getNumberOfPayment() {
        return numberOfPayment;
    }

    public void setNumberOfPayment(int numberOfPayment) {
        this.numberOfPayment = numberOfPayment;
    }

    public long getIdPerkiraanLawan() {
        return idPerkiraanLawan;
    }

    public void setIdPerkiraanLawan(long idPerkiraanLawan) {
        this.idPerkiraanLawan = idPerkiraanLawan;
    }

    public long getIdPerkiraan() {
        return idPerkiraan;
    }

    public void setIdPerkiraan(long idPerkiraan) {
        this.idPerkiraan = idPerkiraan;
    }

    public String getNotaNo() {
        return notaNo;
    }

    public void setNotaNo(String notaNo) {
        this.notaNo = notaNo;
    }

    public Date getNotaDate() {
        return notaDate;
    }

    public void setNotaDate(Date notaDate) {
        this.notaDate = notaDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getArApMainStatus() {
        return arApMainStatus;
    }

    public void setArApMainStatus(int arApMainStatus) {
        this.arApMainStatus = arApMainStatus;
    }

    public int getArApType() {
        return arApType;
    }

    public void setArApType(int arApType) {
        this.arApType = arApType;
    }

    public int getArApDocStatus() {
        return arApDocStatus;
    }

    public void setArApDocStatus(int arApDocStatus) {
        this.arApDocStatus = arApDocStatus;
    }
}