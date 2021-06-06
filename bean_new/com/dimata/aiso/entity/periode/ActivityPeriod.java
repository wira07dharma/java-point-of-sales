/*
 * ActivityPeriod.java
 *
 * Created on January 4, 2007, 1:19 PM
 */

package com.dimata.aiso.entity.periode;

/**
 *
 * @author  dwi
 */
import com.dimata.qdep.entity.*;
import java.util.*;


public class ActivityPeriod extends Entity {
    
    /**
     * Holds value of property name.
     */
    private String name = "";
    
    /**
     * Holds value of property description.
     */
    private String description = "";
    
    /**
     * Holds value of property posted.
     */
    private int posted = 0;
    private Date startDate;
    private Date endDate;
    
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter for property posted.
     * @return Value of property posted.
     */
    public int getPosted() {
        return this.posted;
    }
    
    /**
     * Setter for property posted.
     * @param posted New value of property posted.
     */
    public void setPosted(int posted) {
        this.posted = posted;
    }
    
    /** Creates a new instance of ActivityPeriod */
   
     public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
      public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}
