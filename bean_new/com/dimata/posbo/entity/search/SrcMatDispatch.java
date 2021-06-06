package com.dimata.posbo.entity.search;

/* package java */

import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class SrcMatDispatch extends Entity {

    private String dispatchCode = "";
    private Date dispatchDateFrom = new Date();
    private Date dispatchDateTo = new Date();
    private boolean ignoreDate = true;
    private long dispatchFrom = 0;
    private long dispatchTo = 0;
    private int status = -1;
    private int sortBy;

    private int statusDeliveryOrder=0;
    
    private int testCheck = 0;
    //added by dewok 20180129
    private int locationType = -1;
    
    public static final String textListSortByKey[] = {"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status"};
    public static final String textListSortByValue[] = {"1", "2", "3", "4", "5"};
    
    public String getDispatchCode() {
        return dispatchCode;
    }

    public void setDispatchCode(String dispatchCode) {
        if (dispatchCode == null) {
            dispatchCode = "";
        }
        this.dispatchCode = dispatchCode;
    }

    public Date getDispatchDateFrom() {
        return dispatchDateFrom;
    }

    public void setDispatchDateFrom(Date dispatchDateFrom) {
        this.dispatchDateFrom = dispatchDateFrom;
    }

    public Date getDispatchDateTo() {
        return dispatchDateTo;
    }

    public void setDispatchDateTo(Date dispatchDateTo) {
        this.dispatchDateTo = dispatchDateTo;
    }

    public boolean getIgnoreDate() {
        return ignoreDate;
    }

    public void setIgnoreDate(boolean ignoreDate) {
        this.ignoreDate = ignoreDate;
    }

    public long getDispatchFrom() {
        return dispatchFrom;
    }

    public void setDispatchFrom(long dispatchFrom) {
        this.dispatchFrom = dispatchFrom;
    }

    public long getDispatchTo() {
        return dispatchTo;
    }

    public void setDispatchTo(long dispatchTo) {
        this.dispatchTo = dispatchTo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * @return the statusDeliveryOrder
     */
    public int getStatusDeliveryOrder() {
        return statusDeliveryOrder;
    }

    /**
     * @param statusDeliveryOrder the statusDeliveryOrder to set
     */
    public void setStatusDeliveryOrder(int statusDeliveryOrder) {
        this.statusDeliveryOrder = statusDeliveryOrder;
    }

    /**
     * @return the testCheck
     */
    public int getTestCheck() {
        return testCheck;
    }

    /**
     * @param testCheck the testCheck to set
     */
    public void setTestCheck(int testCheck) {
        this.testCheck = testCheck;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }


}
