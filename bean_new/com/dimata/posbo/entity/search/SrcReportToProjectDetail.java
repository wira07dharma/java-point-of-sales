/**
 * User: wardana
 * Date: Mar 26, 2004
 * Time: 5:37:17 PM
 * Version: 1.0 
 */
package com.dimata.posbo.entity.search;

import java.util.Date;

public class SrcReportToProjectDetail {

    private long lLocationId = 0;
    private long lprojectId = 0;
    private Date dtDateFrom = new Date();
    private Date dtDateTo = new Date();
    private boolean bIgnoreDate;
    private String stProjectName = "";

    public long getlLocationId() {
        return lLocationId;
    }

    public void setlLocationId(long lLocationId) {
        this.lLocationId = lLocationId;
    }

    public long getLprojectId() {
        return lprojectId;
    }

    public void setLprojectId(long lprojectId) {
        this.lprojectId = lprojectId;
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

    public boolean isbIgnoreDate() {
        return bIgnoreDate;
    }

    public void setbIgnoreDate(boolean bIgnoreDate) {
        this.bIgnoreDate = bIgnoreDate;
    }

    public String getStProjectName() {
        return stProjectName;
    }

    public void setStProjectName(String stProjectName) {
        this.stProjectName = stProjectName;
    }
}
