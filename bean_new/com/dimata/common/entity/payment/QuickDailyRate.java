/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.entity.payment;

/**
 *
 * @author PT. Dimata
 */

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class QuickDailyRate extends Entity {
    private long currencyTypeId;
    private double sellingRate;
    private Date rosterDate;

    /**
     * @return the currencyTypeId
     */
    public long getCurrencyTypeId() {
        return currencyTypeId;
    }

    /**
     * @param currencyTypeId the currencyTypeId to set
     */
    public void setCurrencyTypeId(long currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

    /**
     * @return the sellingRate
     */
    public double getSellingRate() {
        return sellingRate;
    }

    /**
     * @param sellingRate the sellingRate to set
     */
    public void setSellingRate(double sellingRate) {
        this.sellingRate = sellingRate;
    }

    /**
     * @return the rosterDate
     */
    public Date getRosterDate() {
        return rosterDate;
    }

    /**
     * @param rosterDate the rosterDate to set
     */
    public void setRosterDate(Date rosterDate) {
        this.rosterDate = rosterDate;
    }

}
