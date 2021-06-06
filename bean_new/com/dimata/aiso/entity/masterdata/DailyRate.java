/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author dwi
 */
public class DailyRate extends Entity{
 
    private long currencyId = 0;
    private double buyingAmount = 0.0;
    private double sellingAmount = 0.0;
    private int status = 0;
    private Date startDate = new Date();
    private Date endDate = new Date();
    private int localForeign = 0;

    public double getBuyingAmount() {
        return buyingAmount;
    }

    public void setBuyingAmount(double buyingAmount) {
        this.buyingAmount = buyingAmount;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getSellingAmount() {
        return sellingAmount;
    }

    public void setSellingAmount(double sellingAmount) {
        this.sellingAmount = sellingAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLocalForeign() {
        return localForeign;
    }

    public void setLocalForeign(int localForeign) {
        this.localForeign = localForeign;
    }
    
    
}
