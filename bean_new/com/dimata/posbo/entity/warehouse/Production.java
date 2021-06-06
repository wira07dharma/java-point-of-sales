/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class Production extends Entity {

    private String productionNumber = "";
    private Date productionDate = null;
    private int productionStatus = 0;
    private long locationFromId = 0;
    private long locationToId = 0;
    private String remark = "";

    public String getProductionNumber() {
        return productionNumber;
    }

    public void setProductionNumber(String productionNumber) {
        this.productionNumber = productionNumber;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public int getProductionStatus() {
        return productionStatus;
    }

    public void setProductionStatus(int productionStatus) {
        this.productionStatus = productionStatus;
    }

    public long getLocationFromId() {
        return locationFromId;
    }

    public void setLocationFromId(long locationFromId) {
        this.locationFromId = locationFromId;
    }

    public long getLocationToId() {
        return locationToId;
    }

    public void setLocationToId(long locationToId) {
        this.locationToId = locationToId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
