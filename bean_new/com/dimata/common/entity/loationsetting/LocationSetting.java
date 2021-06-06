/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.loationsetting;

/**
 *
 * @author dimata005
 */
import com.dimata.qdep.entity.Entity;

public class LocationSetting extends Entity {

    private long locationId = 0;
    private String name = "";
    private String mobileIcon = "";
    private String desktopIcon = "";
    private int type = 0;
    private long defaultItem = 0;
    private long defaultCategory = 0;
    private long defaultSubCategory = 0;

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileIcon() {
        return mobileIcon;
    }

    public void setMobileIcon(String mobileIcon) {
        this.mobileIcon = mobileIcon;
    }

    public String getDesktopIcon() {
        return desktopIcon;
    }

    public void setDesktopIcon(String desktopIcon) {
        this.desktopIcon = desktopIcon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDefaultSubCategory(int defaultSubCategory) {
        this.setDefaultSubCategory(defaultSubCategory);
    }

    /**
     * @return the defaultItem
     */
    public long getDefaultItem() {
        return defaultItem;
    }

    /**
     * @param defaultItem the defaultItem to set
     */
    public void setDefaultItem(long defaultItem) {
        this.defaultItem = defaultItem;
    }

    /**
     * @return the defaultCategory
     */
    public long getDefaultCategory() {
        return defaultCategory;
    }

    /**
     * @param defaultCategory the defaultCategory to set
     */
    public void setDefaultCategory(long defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    /**
     * @return the defaultSubCategory
     */
    public long getDefaultSubCategory() {
        return defaultSubCategory;
    }

    /**
     * @param defaultSubCategory the defaultSubCategory to set
     */
    public void setDefaultSubCategory(long defaultSubCategory) {
        this.defaultSubCategory = defaultSubCategory;
    }

}
