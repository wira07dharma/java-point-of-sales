/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class ProductionProduct extends Entity {

    private long productionGroupId = 0;
    private long materialId = 0;
    private int materialType = 0;
    private double costPct = 0;
    private int costType = 0;
    private double stockQty = 0;
    private double costValue = 0;
    private double salesValue = 0;
    private int periodDistribution = 0;

    public long getProductionGroupId() {
        return productionGroupId;
    }

    public void setProductionGroupId(long productionGroupId) {
        this.productionGroupId = productionGroupId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public int getMaterialType() {
        return materialType;
    }

    public void setMaterialType(int materialType) {
        this.materialType = materialType;
    }

    public double getCostPct() {
        return costPct;
    }

    public void setCostPct(double costPct) {
        this.costPct = costPct;
    }

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
    }

    public double getStockQty() {
        return stockQty;
    }

    public void setStockQty(double stockQty) {
        this.stockQty = stockQty;
    }

    public double getCostValue() {
        return costValue;
    }

    public void setCostValue(double costValue) {
        this.costValue = costValue;
    }

    public double getSalesValue() {
        return salesValue;
    }

    public void setSalesValue(double salesValue) {
        this.salesValue = salesValue;
    }

    public int getPeriodDistribution() {
        return periodDistribution;
    }

    public void setPeriodDistribution(int periodDistribution) {
        this.periodDistribution = periodDistribution;
    }

}
