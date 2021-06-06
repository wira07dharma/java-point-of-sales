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
public class CalcCogsLocation extends Entity {

    private long calcCogsMainId = 0;
    private long locationId = 0;
    private int locationCalcCogsType = 0;

    public long getCalcCogsMainId() {
        return calcCogsMainId;
    }

    public void setCalcCogsMainId(long calcCogsMainId) {
        this.calcCogsMainId = calcCogsMainId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public int getLocationCalcCogsType() {
        return locationCalcCogsType;
    }

    public void setLocationCalcCogsType(int locationCalcCogsType) {
        this.locationCalcCogsType = locationCalcCogsType;
    }

}
