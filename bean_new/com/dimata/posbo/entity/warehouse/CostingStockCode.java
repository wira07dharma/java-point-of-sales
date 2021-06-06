/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:22:01 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

public class CostingStockCode extends Entity {
    private long costingMaterialItemId = 0;
    private long costingMaterialId = 0;
    private String stockCode = "";
    private double stockValue=0.0;
    
    public String getStockCode() {
        return stockCode;
    }

    public long getCostingMaterialItemId() {
        return costingMaterialItemId;
    }

    public void setCostingMaterialItemId(long costingMaterialItemId) {
        this.costingMaterialItemId = costingMaterialItemId;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    /**
     * @return the stockValue
     */
    public double getStockValue() {
        return stockValue;
    }

    /**
     * @param stockValue the stockValue to set
     */
    public void setStockValue(double stockValue) {
        this.stockValue = stockValue;
    }

    /**
     * @return the costingMaterialId
     */
    public long getCostingMaterialId() {
        return costingMaterialId;
}

    /**
     * @param costingMaterialId the costingMaterialId to set
     */
    public void setCostingMaterialId(long costingMaterialId) {
        this.costingMaterialId = costingMaterialId;
    }

}
