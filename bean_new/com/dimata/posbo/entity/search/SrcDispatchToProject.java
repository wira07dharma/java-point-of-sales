/**
 * User: wardana
 * Date: Mar 25, 2004
 * Time: 8:58:08 AM
 * Version: 1.0 
 */
package com.dimata.posbo.entity.search;

import com.dimata.qdep.entity.Entity;

import java.util.Date;

public class SrcDispatchToProject extends Entity{

    private String stDispatchCode = "";
	private Date dtDispatchDateFrom = new Date();
	private Date dtDispatchDateTo = new Date();
	private boolean bIgnoreDate = false;
	private long lDispatchFrom = 0;
	private long lDispatchTo = 0;
	private int iStatus = -1;
	private int iSortBy;

    public String getStDispatchCode() {
        return stDispatchCode;
    }

    public void setStDispatchCode(String stDispatchCode) {
        this.stDispatchCode = stDispatchCode;
    }

    public Date getDtDispatchDateFrom() {
        return dtDispatchDateFrom;
    }

    public void setDtDispatchDateFrom(Date dtDispatchDateFrom) {
        this.dtDispatchDateFrom = dtDispatchDateFrom;
    }

    public Date getDtDispatchDateTo() {
        return dtDispatchDateTo;
    }

    public void setDtDispatchDateTo(Date dtDispatchDateTo) {
        this.dtDispatchDateTo = dtDispatchDateTo;
    }

    public boolean isbIgnoreDate() {
        return bIgnoreDate;
    }

    public void setbIgnoreDate(boolean bIgnoreDate) {
        this.bIgnoreDate = bIgnoreDate;
    }

    public long getlDispatchFrom() {
        return lDispatchFrom;
    }

    public void setlDispatchFrom(long lDispatchFrom) {
        this.lDispatchFrom = lDispatchFrom;
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

    public int getiSortBy() {
        return iSortBy;
    }

    public void setiSortBy(int iSortBy) {
        this.iSortBy = iSortBy;
    }
}
