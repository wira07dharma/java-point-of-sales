/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice.inquery;

/**
 *
 * @author dimata005
 */
public class MaterialForApi {
     private long locationId = 0;
     private long materialId=0;
     private String name="";
     private long unitId=0;
     private double price=0.0;
     private long standartRateId=0;
     private long priceTypeId=0;
     
    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the materialId
     */
    public long getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
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
     * @return the unitId
     */
    public long getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the standartRateId
     */
    public long getStandartRateId() {
        return standartRateId;
    }

    /**
     * @param standartRateId the standartRateId to set
     */
    public void setStandartRateId(long standartRateId) {
        this.standartRateId = standartRateId;
    }

    /**
     * @return the priceTypeId
     */
    public long getPriceTypeId() {
        return priceTypeId;
    }

    /**
     * @param priceTypeId the priceTypeId to set
     */
    public void setPriceTypeId(long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }
     
}
