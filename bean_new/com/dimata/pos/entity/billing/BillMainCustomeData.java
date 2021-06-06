/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.pos.entity.billing;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class BillMainCustomeData extends Entity{
    private long cashBillMainId=0;
    private int type=0;
    private String name="";
    private String value="";
    private double latitude=0.0;
    private double longitude=0.0;
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the cashBillMainId
     */
    public long getCashBillMainId() {
        return cashBillMainId;
    }

    /**
     * @param cashBillMainId the cashBillMainId to set
     */
    public void setCashBillMainId(long cashBillMainId) {
        this.cashBillMainId = cashBillMainId;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
}
