/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

public class ChainAddCost extends Entity {

    private long chainPeriodId = 0;
    private long materialId = 0;
    private int stockQty = 0;
    private double stockValue = 0;
    private int costType = 0;
    private long productDistributionId = 0;

    public long getChainPeriodId() {
        return chainPeriodId;
    }

    public void setChainPeriodId(long chainPeriodId) {
        this.chainPeriodId = chainPeriodId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
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
