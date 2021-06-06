/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:22:01 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

public class ReturnStockCode extends Entity {
    private long returnMaterialItemId = 0;
    private long returnMaterialId = 0;
    private String stockCode = "";
    private double stockValue=0.0;

    public String getStockCode() {
        return stockCode;
    }

    public long getReturnMaterialItemId() {
        return returnMaterialItemId;
    }

    public void setReturnMaterialItemId(long returnMaterialItemId) {
        this.returnMaterialItemId = returnMaterialItemId;
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
     * @return the returnMaterialId
     */
    public long getReturnMaterialId() {
        return returnMaterialId;
}

    /**
     * @param returnMaterialId the returnMaterialId to set
     */
    public void setReturnMaterialId(long returnMaterialId) {
        this.returnMaterialId = returnMaterialId;
    }

}
