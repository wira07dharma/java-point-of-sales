package com.dimata.posbo.entity.warehouse;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatDistributionDetail extends Entity {
    
    private long dispatchMaterialId = 0;
    private long materialId = 0;
    private double qty = 0;
    private long distributionMaterialId;
    private long locationId;
    
    public long getDispatchMaterialId() {
        return dispatchMaterialId;
    }
    
    public void setDispatchMaterialId(long dispatchMaterialId) {
        this.dispatchMaterialId = dispatchMaterialId;
    }
    
    public long getMaterialId() {
        return materialId;
    }
    
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }
    
    public double getQty() {
        return qty;
    }
    
    public void setQty(double qty) {
        this.qty = qty;
    }
    
    public long getDistributionMaterialId() {
        return this.distributionMaterialId;
    }
    
    public void setDistributionMaterialId(long distributionMaterialId) {
        this.distributionMaterialId = distributionMaterialId;
    }
    
    public long getLocationId() {
        return this.locationId;
    }
    
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
}
