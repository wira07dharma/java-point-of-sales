package com.dimata.posbo.entity.warehouse;

/* package java */

import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatConReceiveItem extends Entity {

    private long receiveMaterialId;
    private long materialId;
    private Date expiredDate = new Date();
    private long unitId = 0;
    private double cost = 0.00;
    private long currencyId = 0;
    private double qty = 0;
    private double discount = 0.00;
    private double total = 0.00;
    private double residueQty;
    private double discount2 = 0.00;
    private double discNominal = 0.00;
    private double currBuyingPrice = 0.00;
    private double forwarderCost = 0;

    public double getCurrBuyingPrice() {
        return currBuyingPrice;
    }

    public void setCurrBuyingPrice(double currBuyingPrice) {
        this.currBuyingPrice = currBuyingPrice;
    }

    public double getDiscNominal() {
        return discNominal;
    }

    public void setDiscNominal(double discNominal) {
        this.discNominal = discNominal;
    }

    public double getDiscount2() {
        return discount2;
    }

    public void setDiscount2(double discount2) {
        this.discount2 = discount2;
    }

    public long getReceiveMaterialId() {
        return receiveMaterialId;
    }

    public void setReceiveMaterialId(long receiveMaterialId) {
        this.receiveMaterialId = receiveMaterialId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public double getResidueQty() {
        return this.residueQty;
    }
    
    public void setResidueQty(double residueQty) {
        this.residueQty = residueQty;
    }
    
    public double getForwarderCost() {
        return forwarderCost;
    }
    
    public void setForwarderCost(double forwarderCost) {
        this.forwarderCost = forwarderCost;
    }
    
}
