/*
 * ActivityPeriodLink.java
 *
 * Created on January 16, 2007, 10:58 AM
 */

package com.dimata.aiso.entity.masterdata;

/* import package qdep */
import com.dimata.qdep.entity.Entity;

/**
 *
 * @author  dwi
 */
public class ActivityPeriodLink extends Entity {
    
    /**
     * Holds value of property activity_period_id.
     */
    private long activity_period_id = 0;    
   
    /**
     * Holds value of property activity_id.
     */
    private long activity_id = 0;
    
    /**
     * Holds value of property budget.
     */
    private float budget = 0;
    
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
     * Getter for property budget.
     * @return Value of property budget.
     */
    public float getBudget() {
        return this.budget;
    }
    
    /**
     * Setter for property budget.
     * @param budget New value of property budget.
     */
    public void setBudget(float budget) {
        this.budget = budget;
    }
    
}
