/**
 * User: wardana
 * Date: Mar 23, 2004
 * Time: 12:46:46 PM
 * Version: 1.0
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

import java.util.Date;

public class DispatchToProject extends Entity {

    private long lLocationId = 0;
    private long lProjectId = 0;
    private int iLocationType = 0;
    private Date dtDispatchDate = new Date();
    private String stDispatchCode = "";
    private int iDispatchStatus = 0;
    private String stRemark = "";
    private int iDispatchCodeCounter = 0;
    private long lDispatchTo = 0;
    private String stInvoiceSupplier = "";

    public long getlLocationId() {
        return lLocationId;
    }

    public void setlLocationId(long lLocationId) {
        this.lLocationId = lLocationId;
    }

    public long getlProjectId() {
        return lProjectId;
    }

    public void setlProjectId(long lProjectId) {
        this.lProjectId = lProjectId;
    }

    public int getiLocationType() {
        return iLocationType;
    }

    public void setiLocationType(int iLocationType) {
        this.iLocationType = iLocationType;
    }

    public Date getDtDispatchDate() {
        return dtDispatchDate;
    }

    public void setDtDispatchDate(Date dtDispatchDate) {
        this.dtDispatchDate = dtDispatchDate;
    }

    public String getStDispatchCode() {
        return stDispatchCode;
    }

    public void setStDispatchCode(String stDispatchCode) {
        this.stDispatchCode = stDispatchCode;
    }

    public int getiDispatchStatus() {
        return iDispatchStatus;
    }

    public void setiDispatchStatus(int iDispatchStatus) {
        this.iDispatchStatus = iDispatchStatus;
    }

    public String getStRemark() {
        return stRemark;
    }

    public void setStRemark(String stRemark) {
        this.stRemark = stRemark;
    }

    public int getiDispatchCodeCounter() {
        return iDispatchCodeCounter;
    }

    public void setiDispatchCodeCounter(int iDispatchCodeCounter) {
        this.iDispatchCodeCounter = iDispatchCodeCounter;
    }

    public long getlDispatchTo() {
        return lDispatchTo;
    }

    public void setlDispatchTo(long lDispatchTo) {
        this.lDispatchTo = lDispatchTo;
    }

    public String getStInvoiceSupplier() {
        return stInvoiceSupplier;
    }

    public void setStInvoiceSupplier(String stInvoiceSupplier) {
        this.stInvoiceSupplier = stInvoiceSupplier;
    }

}
