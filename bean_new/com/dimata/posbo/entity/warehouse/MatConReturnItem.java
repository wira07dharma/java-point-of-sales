package com.dimata.posbo.entity.warehouse;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatConReturnItem extends Entity {
    
    private long returnMaterialId;
    private long materialId = 0;
    private long unitId = 0;
    private double cost = 0.00;
    private long currencyId = 0;
    private double qty = 0.00;
    private double total = 0.00;
    private double residueQty = 0.00;
    
    public long getReturnMaterialId() {
        return returnMaterialId;
    }
    
    public void setReturnMaterialId(long returnMaterialId) {
        this.returnMaterialId = returnMaterialId;
    }
    
    public long getMaterialId() {
        return materialId;
    }
    
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
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
    
}
