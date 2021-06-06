package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author Witar
 */

import com.dimata.qdep.entity.Entity;

public class MappingProductLocationStoreRequest extends Entity { 
    private long materialId = 0;
    private long locationId = 0;
    private String itemName = "";
    private String unitCode ="";

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the unitCode
     */
    public String getUnitCode() {
        return unitCode;
    }

    /**
     * @param unitCode the unitCode to set
     */
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
