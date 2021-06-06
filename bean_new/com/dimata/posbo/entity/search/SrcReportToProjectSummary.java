/**
 * User: wardana
 * Date: Mar 25, 2004
 * Time: 3:00:13 PM
 * Version: 1.0 
 */
package com.dimata.posbo.entity.search;

import java.util.Date;

public class SrcReportToProjectSummary {

    private long lLocationId = 0;
    private long lDispatchTo = 0;
    private int iStatus = 0;
    private Date dtDateFrom = new Date();
    private Date dtDateTo = new Date();

    public long getlLocationId() {
        return lLocationId;
    }

    public void setlLocationId(long lLocationId) {
        this.lLocationId = lLocationId;
    }

    public long getlDispatchTo() {
        return lDispatchTo;
    }

    public void setlDispatchTo(long lDispatchTo) {
        this.lDispatchTo = lDispatchTo;
    }

    public int getiStatus() {
        return iStatus;
    }

    public void setiStatus(int iStatus) {
        this.iStatus = iStatus;
    }

    public Date getDtDateFrom() {
        return dtDateFrom;
    }

    public void setDtDateFrom(Date dtDateFrom) {
        this.dtDateFrom = dtDateFrom;
    }

    public Date getDtDateTo() {
        return dtDateTo;
    }

    public void setDtDateTo(Date dtDateTo) {
        this.dtDateTo = dtDateTo;
    }
}
