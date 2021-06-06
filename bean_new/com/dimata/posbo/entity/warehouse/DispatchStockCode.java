/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:22:01 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

public class DispatchStockCode extends Entity {
    private long dispatchMaterialItemId = 0;
    private long dispatchMaterialId = 0;
    private String stockCode = "";
    private double stockValue=0.0;
    
    public String getStockCode() {
        return stockCode;
    }

    public long getDispatchMaterialItemId() {
        return dispatchMaterialItemId;
    }

    public void setDispatchMaterialItemId(long dispatchMaterialItemId) {
        this.dispatchMaterialItemId = dispatchMaterialItemId;
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
     * @return the dispatchMaterialId
     */
    public long getDispatchMaterialId() {
        return dispatchMaterialId;
}

    /**
     * @param dispatchMaterialId the dispatchMaterialId to set
     */
    public void setDispatchMaterialId(long dispatchMaterialId) {
        this.dispatchMaterialId = dispatchMaterialId;
    }

}
