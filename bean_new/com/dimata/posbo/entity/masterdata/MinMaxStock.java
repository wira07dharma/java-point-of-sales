package com.dimata.posbo.entity.masterdata;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MinMaxStock extends Entity {
    private long materialId = 0;
    private long locationId = 0;
    private double qtyMin = 0;
    private double qtyMax = 0;
    
    
    public long getMaterialId(){ return materialId; }
    
    public void setMaterialId(long materialId){ this.materialId = materialId; }
    
    public long getLocationId() {
        return locationId;
    }
    
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
    public double getQtyMin() {
        return qtyMin;
    }
    
    public void setQtyMin(double qtyMin) {
        this.qtyMin = qtyMin;
    }
    
    public double getQtyMax() {
        return qtyMax;
    }
    
    public void setQtyMax(double qtyMax) {
        this.qtyMax = qtyMax;
    }
    
}
