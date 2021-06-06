/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:22:01 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

public class MatConDispatchSerialCode extends Entity {
    private long dispatchMaterialItemId = 0;
    private String stockCode = "";

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

}
