/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
import com.dimata.qdep.entity.Entity;

public class KitchenProduksiMapping extends Entity {

    private long subCategoryId = 0;
    private long locationOrderId = 0;
    private long locationProduksiId = 0;
    
    private String locationOrder="";
    private String locationProduksi="";
    private String subCategory="";

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public long getLocationOrderId() {
        return locationOrderId;
    }

    public void setLocationOrderId(long locationOrderId) {
        this.locationOrderId = locationOrderId;
    }

    public long getLocationProduksiId() {
        return locationProduksiId;
    }

    public void setLocationProduksiId(long locationProduksiId) {
        this.locationProduksiId = locationProduksiId;
    }

    /**
     * @return the locationOrder
     */
    public String getLocationOrder() {
        return locationOrder;
    }

    /**
     * @param locationOrder the locationOrder to set
     */
    public void setLocationOrder(String locationOrder) {
        this.locationOrder = locationOrder;
    }

    /**
     * @return the locationProduksi
     */
    public String getLocationProduksi() {
        return locationProduksi;
    }

    /**
     * @param locationProduksi the locationProduksi to set
     */
    public void setLocationProduksi(String locationProduksi) {
        this.locationProduksi = locationProduksi;
    }

    /**
     * @return the subCategory
     */
    public String getSubCategory() {
        return subCategory;
    }

    /**
     * @param subCategory the subCategory to set
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

}
