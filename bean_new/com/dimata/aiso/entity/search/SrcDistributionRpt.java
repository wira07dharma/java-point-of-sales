/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.search;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dwi
 */
public class SrcDistributionRpt extends Entity{
    
    private long binisCenterId = 0;
    private long periodId = 0;
    private long currencyId = 0;

    public long getBinisCenterId() {
        return binisCenterId;
    }

    public void setBinisCenterId(long binisCenterId) {
        this.binisCenterId = binisCenterId;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(long periodId) {
        this.periodId = periodId;
    }
    
    
}
