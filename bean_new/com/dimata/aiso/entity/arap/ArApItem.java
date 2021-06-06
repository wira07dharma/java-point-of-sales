package com.dimata.aiso.entity.arap;

// package qdep

import com.dimata.qdep.entity.*;

import java.util.Date;

public class ArApItem extends Entity {

    private long arApMainId = 0;
    private long currencyId = 0;
    private double rate = 0;
    private double angsuran = 0;
    private Date dueDate = new Date();
    private String description = "";
    private int arApItemStatus = 0;
    private double leftToPay = 0;
    private long sellingAktivaId = 0;
    private long receiveAktivaId = 0;

    public long getArApMainId() {
        return arApMainId;
    }

    public void setArApMainId(long arApMainId) {
        this.arApMainId = arApMainId;
    }

    public double getAngsuran() {
        return angsuran;
    }

    public void setAngsuran(double angsuran) {
        this.angsuran = angsuran;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getArApItemStatus() {
        return arApItemStatus;
    }

    public void setArApItemStatus(int arApItemStatus) {
        this.arApItemStatus = arApItemStatus;
    }

    public double getLeftToPay() {
        return leftToPay;
    }

    public void setLeftToPay(double leftToPay) {
        this.leftToPay = leftToPay;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public long getSellingAktivaId() {
        return sellingAktivaId;
    }

    public void setSellingAktivaId(long sellingAktivaId) {
        this.sellingAktivaId = sellingAktivaId;
    }

    public long getReceiveAktivaId() {
        return receiveAktivaId;
    }

    public void setReceiveAktivaId(long receiveAktivaId) {
        this.receiveAktivaId = receiveAktivaId;
    }
}