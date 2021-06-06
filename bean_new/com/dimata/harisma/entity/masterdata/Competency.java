/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class Competency extends Entity {
    private long competencyGroupId = 0;
    private long competencyTypeId = 0;
    private String competencyName = "";
    private String description = "";

    /**
     * @return the competencyGroupId
     */
    public long getCompetencyGroupId() {
        return competencyGroupId;
    }

    /**
     * @param competencyGroupId the competencyGroupId to set
     */
    public void setCompetencyGroupId(long competencyGroupId) {
        this.competencyGroupId = competencyGroupId;
    }

    /**
     * @return the competencyTypeId
     */
    public long getCompetencyTypeId() {
        return competencyTypeId;
    }

    /**
     * @param competencyTypeId the competencyTypeId to set
     */
    public void setCompetencyTypeId(long competencyTypeId) {
        this.competencyTypeId = competencyTypeId;
    }

    /**
     * @return the competencyName
     */
    public String getCompetencyName() {
        return competencyName;
    }

    /**
     * @param competencyName the competencyName to set
     */
    public void setCompetencyName(String competencyName) {
        this.competencyName = competencyName;
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
