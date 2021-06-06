/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.marketing;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Sunima
 */
public class MarketingCatalogDetail  extends Entity{
    
    
    
    private long marketing_catalog_id = 0;
    private long marketing_material_id = 0;
    private long marketing_material_category_id = 0;

    /**
     * @return the marketing_catalog_id
     */
    public long getMarketing_catalog_id() {
        return marketing_catalog_id;
    }

    /**
     * @param marketing_catalog_id the marketing_catalog_id to set
     */
    public void setMarketing_catalog_id(long marketing_catalog_id) {
        this.marketing_catalog_id = marketing_catalog_id;
    }

    /**
     * @return the marketing_material_id
     */
    public long getMarketing_material_id() {
        return marketing_material_id;
    }

    /**
     * @param marketing_material_id the marketing_material_id to set
     */
    public void setMarketing_material_id(long marketing_material_id) {
        this.marketing_material_id = marketing_material_id;
    }

    /**
     * @return the marketing_material_category_id
     */
    public long getMarketing_material_category_id() {
        return marketing_material_category_id;
    }

    /**
     * @param marketing_material_category_id the marketing_material_category_id to set
     */
    public void setMarketing_material_category_id(long marketing_material_category_id) {
        this.marketing_material_category_id = marketing_material_category_id;
    }
}
