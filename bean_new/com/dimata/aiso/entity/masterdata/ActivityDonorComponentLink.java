/*
 * ActivityDonorComponentLink.java
 *
 * Created on January 23, 2007, 1:15 PM
 */

package com.dimata.aiso.entity.masterdata;

/* import package qdep */
import com.dimata.qdep.entity.Entity;

/**
 *
 * @author  dwi
 */
public class ActivityDonorComponentLink extends Entity { 
    
    /**
     * Holds value of property activity_id.
     */
    private long activity_id;    
    
    /**
     * Holds value of property activity_period_id.
     */
    private long activity_period_id;
    
    /**
     * Holds value of property donor_component_id.
     */
    private long donor_component_id;
    
    /**
     * Holds value of property share_budget.
     */
    private float share_budget;
    
    /**
     * Holds value of property share_procentage.
     */
    private float share_procentage;
    
    /**
     * Getter for property activity_id.
     * @return Value of property activity_id.
     */
    public long getActivityId() {
        return this.activity_id;
    }    
    
    /**
     * Setter for property activity_id.
     * @param activity_id New value of property activity_id.
     */
    public void setActivityId(long activity_id) {
        this.activity_id = activity_id;
    }
    
    /**
     * Getter for property activity_period_id.
     * @return Value of property activity_period_id.
     */
    public long getActivityPeriodId() {
        return this.activity_period_id;
    }
    
    /**
     * Setter for property activity_period_id.
     * @param activity_period_id New value of property activity_period_id.
     */
    public void setActivityPeriodId(long activity_period_id) {
        this.activity_period_id = activity_period_id;
    }
    
    /**
     * Getter for property donor_component_id.
     * @return Value of property donor_component_id.
     */
    public long getDonorComponentId() {
        return this.donor_component_id;
    }
    
    /**
     * Setter for property donor_component_id.
     * @param donor_component_id New value of property donor_component_id.
     */
    public void setDonorComponentId(long donor_component_id) {
        this.donor_component_id = donor_component_id;
    }
    
    /**
     * Getter for property share_budget.
     * @return Value of property share_budget.
     */
    public float getShareBudget() {
        return this.share_budget;
    }
    
    /**
     * Setter for property share_budget.
     * @param share_budget New value of property share_budget.
     */
    public void setShareBudget(float share_budget) {
        this.share_budget = share_budget;
    }
    
    /**
     * Getter for property share_procentage.
     * @return Value of property share_procentage.
     */
    public float getShareProcentage() {
        return this.share_procentage;
    }
    
    /**
     * Setter for property share_procentage.
     * @param share_procentage New value of property share_procentage.
     */
    public void setShareProcentage(float share_procentage) {
        this.share_procentage = share_procentage;
    }
    
}
