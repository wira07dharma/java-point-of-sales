/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:22:01 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;
/**
 * update opie-eyek 20130906 untuk stockValue nilai stock fifo
 * @author dimata005
 */
public class SourceStockCode extends Entity {
    private long sourceId = 0;
    private String stockCode = "";
    private double stockValue;
    private long stockOpnameId=0;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
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
     * @return the stockOpnameId
     */
    public long getStockOpnameId() {
        return stockOpnameId;
    }

    /**
     * @param stockOpnameId the stockOpnameId to set
     */
    public void setStockOpnameId(long stockOpnameId) {
        this.stockOpnameId = stockOpnameId;
    }
}
