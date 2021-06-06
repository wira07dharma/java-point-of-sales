/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:22:01 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

public class MatConReceiveSerialCode extends Entity {
    private long receiveMaterialCodeId = 0;
    private long receiveMaterialItemId = 0;
    private String stockCode = "";

    public long getReceiveMaterialItemId() {
        return receiveMaterialItemId;
    }

    public void setReceiveMaterialItemId(long receiveMaterialItemId) {
        this.receiveMaterialItemId = receiveMaterialItemId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public long getReceiveMaterialCodeId() {
        return receiveMaterialCodeId;
    }

    public void setReceiveMaterialCodeId(long receiveMaterialCodeId) {
        this.receiveMaterialCodeId = receiveMaterialCodeId;
    }
}
