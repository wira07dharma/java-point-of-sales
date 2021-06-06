/**
 * User: wardana
 * Date: Mar 23, 2004
 * Time: 12:53:24 PM
 * Version: 1.0
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

public class DispatchToProjectItem extends Entity {
    private long lDispatchMaterialId = 0;
    private long lMaterialId = 0;
    private long lUnitId = 0;
    private double iQty = 0;
    private double dCost = 0;

    public long getlDispatchMaterialId() {
        return lDispatchMaterialId;
    }

    public void setlDispatchMaterialId(long lDispatchMaterialId) {
        this.lDispatchMaterialId = lDispatchMaterialId;
    }

    public long getlMaterialId() {
        return lMaterialId;
    }

    public void setlMaterialId(long lMaterialId) {
        this.lMaterialId = lMaterialId;
    }

    public long getlUnitId() {
        return lUnitId;
    }

    public void setlUnitId(long lUnitId) {
        this.lUnitId = lUnitId;
    }

    public double getiQty() {
        return iQty;
    }

    public void setiQty(double iQty) {
        this.iQty = iQty;
    }

    public double getdCost() {
        return dCost;
    }

    public void setdCost(double dCost) {
        this.dCost = dCost;
    }

}
