/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author sangtel6
 */
public class MasterType extends Entity {
        private int typeGroup;

	private String masterCode = "";

	private String masterName = "";

	private String description = "";

    /**
     * @return the typeGroup
     */
    public int getTypeGroup() {
        return typeGroup;
    }

    /**
     * @param typeGroup the typeGroup to set
     */
    public void setTypeGroup(int typeGroup) {
        this.typeGroup = typeGroup;
    }

    /**
     * @return the masterCode
     */
    public String getMasterCode() {
        return masterCode;
    }

    /**
     * @param masterCode the masterCode to set
     */
    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }

    /**
     * @return the masterName
     */
    public String getMasterName() {
        return masterName;
    }

    /**
     * @param masterName the masterName to set
     */
    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
}
