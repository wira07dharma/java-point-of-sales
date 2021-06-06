// Generated by Together

package com.dimata.aiso.entity.aktiva;

// package qdep

import com.dimata.qdep.entity.*;

import java.util.Date;

public class OrderAktiva extends Entity {

    private String nomorOrder = "";
    private Date tanggalOrder = new Date();
    private long supplierId = 0;
    private int typePembayaran = 0;
    private long idPerkiraanPayment = 0;
    private long idPerkiraanDp = 0;
    private long idCurrency = 0;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    private int orderStatus = 0;

    public double getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }

    private double downPayment = 0.0;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    private int counter = 0;
    private double valueRate = 0.0;

    public long getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(long idCurrency) {
        this.idCurrency = idCurrency;
    }

    public double getValueRate() {
        return valueRate;
    }

    public void setValueRate(double valueRate) {
        this.valueRate = valueRate;
    }

    public String getNomorOrder() {
        return nomorOrder;
    }

    public void setNomorOrder(String nomorOrder) {
        this.nomorOrder = nomorOrder;
    }

    public Date getTanggalOrder() {
        return tanggalOrder;
    }

    public void setTanggalOrder(Date tanggalOrder) {
        this.tanggalOrder = tanggalOrder;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public int getTypePembayaran() {
        return typePembayaran;
    }

    public void setTypePembayaran(int typePembayaran) {
        this.typePembayaran = typePembayaran;
    }

    public long getIdPerkiraanPayment() {
        return idPerkiraanPayment;
    }

    public void setIdPerkiraanPayment(long idPerkiraanPayment) {
        this.idPerkiraanPayment = idPerkiraanPayment;
    }

    public long getIdPerkiraanDp() {
        return idPerkiraanDp;
    }

    public void setIdPerkiraanDp(long idPerkiraanDp) {
        this.idPerkiraanDp = idPerkiraanDp;
    }
}