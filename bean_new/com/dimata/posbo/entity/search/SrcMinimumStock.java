/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Mar 2, 2006
 * Time: 10:30:38 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.search;

import com.dimata.common.entity.location.Location;

import java.util.Vector;

public class SrcMinimumStock {
    private long locationId = 0;
    private long categoryId = 0;
    private long periodId = 0;
    private Vector vLocation = new Vector();
    private Vector vOidMaterial = new Vector();
    private String textOther = "";
    private long oidMaterial = 0;
    private long oidMerk = 0;
    private long oidSupplier = 0;
    private long materialType = 0;
    
    public long getOidSupplier() {
        return oidSupplier;
    }
    
    public void setOidSupplier(long oidSupplier) {
        this.oidSupplier = oidSupplier;
    }
    
    public long getOidMerk() {
        return oidMerk;
    }
    
    public void setOidMerk(long oidMerk) {
        this.oidMerk = oidMerk;
    }
    
    public long getOidMaterial() {
        if(vOidMaterial==null)
            vOidMaterial = new Vector(1,1);
        else{
            return Long.parseLong((String)vOidMaterial.get(0));
        }
        return 0;
    }
    
    public Vector getListOidMaterial() {
        return vOidMaterial;
    }
    
    public void resetOidMaterial() {
        vOidMaterial = new Vector();
    }
    
    public void setOidMaterial(long oidMaterial) {
        if(vOidMaterial==null)
            vOidMaterial = new Vector(1,1);
        if(oidMaterial!=0)
            vOidMaterial.add(String.valueOf(oidMaterial));
    }
    
    public String getTextOther() {
        return textOther;
    }
    
    public void setTextOther(String textOther) {
        this.textOther = textOther;
    }
    
    public Vector getvLocation() {
        return vLocation;
    }
    
    public void setvLocation(Location location) {
        this.vLocation.add(location);
    }
    
    public void setvLocation(Vector vLocation) {
        this.vLocation = vLocation;
    }
    
    public long getLocationId() {
        return locationId;
    }
    
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
    public long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
    
    public long getPeriodId() {
        return periodId;
    }
    
    public void setPeriodId(long periodId) {
        this.periodId = periodId;
    }
}
