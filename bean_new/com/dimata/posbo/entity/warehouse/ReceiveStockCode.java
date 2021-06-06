/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:22:01 AM
 * To change this template use Options | File Templates.
 * update opie-eyek 20130910 untuk fifo stock need stockValue
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

public class ReceiveStockCode extends Entity {
    private long receiveMaterialCodeId = 0;
    private long receiveMaterialItemId = 0;
    private long receiveMaterialId = 0;
    private String stockCode = "";
    private double stockValue=0.0;

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
     * @return the receiveMaterialId
     */
    public long getReceiveMaterialId() {
        return receiveMaterialId;
}

    /**
     * @param receiveMaterialId the receiveMaterialId to set
     */
    public void setReceiveMaterialId(long receiveMaterialId) {
        this.receiveMaterialId = receiveMaterialId;
    }
}
