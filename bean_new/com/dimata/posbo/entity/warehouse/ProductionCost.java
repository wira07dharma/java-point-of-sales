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
public class ProductionCost extends Entity {

    private long productionGroupId = 0;
    private long materialId = 0;
    private double stockQty = 0;
    private double stockValue = 0;
    private int costType = 0;
    private long productDistributionId = 0;

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

    public double getStockQty() {
        return stockQty;
    }

    public void setStockQty(double stockQty) {
        this.stockQty = stockQty;
    }

    public double getStockValue() {
        return stockValue;
    }

    public void setStockValue(double stockValue) {
        this.stockValue = stockValue;
    }

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
    }

    public long getProductDistributionId() {
        return productDistributionId;
    }

    public void setProductDistributionId(long productDistributionId) {
        this.productDistributionId = productDistributionId;
    }

}
