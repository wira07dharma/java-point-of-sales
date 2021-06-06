/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class InsentifData extends Entity {

    private long cashBillMainId = 0;
    private long cashBillDetailId = 0;
    private long employeeId = 0;
    private long positionId = 0;
    private long insentifMasterId = 0;
    private double insentifPoint = 0;
    private double insentifValue = 0;

    public long getCashBillMainId() {
        return cashBillMainId;
    }

    public void setCashBillMainId(long cashBillMainId) {
        this.cashBillMainId = cashBillMainId;
    }

    public long getCashBillDetailId() {
        return cashBillDetailId;
    }

    public void setCashBillDetailId(long cashBillDetailId) {
        this.cashBillDetailId = cashBillDetailId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public long getInsentifMasterId() {
        return insentifMasterId;
    }

    public void setInsentifMasterId(long insentifMasterId) {
        this.insentifMasterId = insentifMasterId;
    }

    public double getInsentifPoint() {
        return insentifPoint;
    }

    public void setInsentifPoint(double insentifPoint) {
        this.insentifPoint = insentifPoint;
    }

    public double getInsentifValue() {
        return insentifValue;
    }

    public void setInsentifValue(double insentifValue) {
        this.insentifValue = insentifValue;
    }

}
