package com.dimata.posbo.entity.warehouse;

/* package java */

import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatConDispatch extends Entity {
    private long locationId = 0;
    private long dispatchTo = 0;
    private int locationType = 0;
    private Date dispatchDate = new Date();
    private String dispatchCode = "";
    private int dispatchCodeCounter = 0;
    private int dispatchStatus = 0;
    private String remark = "";
    private String invoiceSupplier = "";

    /** Holds value of property transferStatus. */
    private int transferStatus;
    private Date last_update = new Date();

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getDispatchTo() {
        return dispatchTo;
    }

    public void setDispatchTo(long dispatchTo) {
        this.dispatchTo = dispatchTo;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getDispatchCode() {
        return dispatchCode;
    }

    public void setDispatchCode(String dispatchCode) {
        if (dispatchCode == null) {
            dispatchCode = "";
        }
        this.dispatchCode = dispatchCode;
    }

    public int getDispatchCodeCounter() {
        return dispatchCodeCounter;
    }

    public void setDispatchCodeCounter(int dispatchCodeCounter) {
        this.dispatchCodeCounter = dispatchCodeCounter;
    }

    public int getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(int dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        if (remark == null) {
            remark = "";
        }
        this.remark = remark;
    }

    public String getInvoiceSupplier() {
        return invoiceSupplier;
    }

    public void setInvoiceSupplier(String invoiceSupplier) {
        if (invoiceSupplier == null) {
            invoiceSupplier = "";
        }
        this.invoiceSupplier = invoiceSupplier;
    }

    /** Getter for property transferStatus.
     * @return Value of property transferStatus.
     *
     */
    public int getTransferStatus() {
        return this.transferStatus;
    }

    /** Setter for property transferStatus.
     * @param transferStatus New value of property transferStatus.
     *
     */
    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

}
