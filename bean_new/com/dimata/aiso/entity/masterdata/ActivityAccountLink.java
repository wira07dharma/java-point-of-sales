/*
 * ActivityAccountLink.java
 *
 * Created on January 13, 2007, 9:14 AM
 */

package com.dimata.aiso.entity.masterdata;

/* import package qdep */
import com.dimata.qdep.entity.Entity;


/**
 *
 * @author  dwi
 */
public class ActivityAccountLink extends Entity {
    
    /**
     * Holds value of property id_perkiraan.
     */
    private long id_perkiraan = 0;    
   
    /**
     * Holds value of property activity_id.
     */
    private long activity_id = 0;
    
    /**
     * Holds value of property act_acc_link_id.
     */
    private long act_acc_link_id = 0;
    
    /**
     * Getter for property id_perkiraan.
     * @return Value of property id_perkiraan.
     */
    public long getIdPerkiraan() {
        return this.id_perkiraan;
    }    
    
    /**
     * Setter for property id_perkiraan.
     * @param id_perkiraan New value of property id_perkiraan.
     */
    public void setIdPerkiraan(long id_perkiraan) {
        this.id_perkiraan = id_perkiraan;
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
     * Getter for property act_acc_link_id.
     * @return Value of property act_acc_link_id.
     */
    public long getActAccLinkId() {
        return this.act_acc_link_id;
    }
    
    /**
     * Setter for property act_acc_link_id.
     * @param act_acc_link_id New value of property act_acc_link_id.
     */
    public void setActAccLinkId(long act_acc_link_id) {
        this.act_acc_link_id = act_acc_link_id;
    }
    
}
