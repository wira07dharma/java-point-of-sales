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
public class AssignDiscount extends Entity {

    private long employeeId = 0;
    private double maxDisc = 0;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public double getMaxDisc() {
        return maxDisc;
    }

    public void setMaxDisc(double maxDisc) {
        this.maxDisc = maxDisc;
    }

}
