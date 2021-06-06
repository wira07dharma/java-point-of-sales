/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.search;

/* package java */

import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class SrcMatDispatchReceiveItem extends Entity {

    private String dispatchCode = "";
    private Date dispatchDateFrom = new Date();
    private Date dispatchDateTo = new Date();
    private boolean ignoreDate = true;
    private long dispatchFrom = 0;
    private long dispatchTo = 0;
    private int status = -1;
    private int sortBy;

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

}



