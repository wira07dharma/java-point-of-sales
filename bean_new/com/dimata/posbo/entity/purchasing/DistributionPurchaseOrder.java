/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.purchasing;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class DistributionPurchaseOrder extends Entity{
    private long purchaseOrderId=0;
    private long locationId=0;
    private double qty=0;

    private String locationName="";

    /**
     * @return the purchaseOrderId
     */
    public long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    /**
     * @param purchaseOrderId the purchaseOrderId to set
     */
    public void setPurchaseOrderId(long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

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
     * @return the qty
     */
    public double getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(double qty) {
        this.qty = qty;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    
}
