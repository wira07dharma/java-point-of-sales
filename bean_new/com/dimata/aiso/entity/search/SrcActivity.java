/*
 * SrcActivity.java
 *
 * Created on February 23, 2007, 9:26 AM
 */

package com.dimata.aiso.entity.search;

import java.util.*;

/**
 *
 * @author  dwi
 */
public class SrcActivity{
    
    /**
     * Holds value of property code.
     */
    private String code = "";    
   
    /**
     * Holds value of property description.
     */
    private String description = "";
    
    /**
     * Holds value of property actLevel.
     */
    private int actLevel = 0;
    
    /**
     * Holds value of property posted.
     */
    private int posted = 0;
    
    /**
     * Holds value of property actType.
     */
    private int actType = 0;
    
    /**
     * Holds value of property orderBy.
     */
    private int orderBy = 0;
    
    /**
     * Getter for property code.
     * @return Value of property code.
     */
    public String getCode() {
        return this.code;
    }
    
    /**
     * Setter for property code.
     * @param code New value of property code.
     */
    public void setCode(String code) {
        this.code = code;
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
     * Getter for property actLevel.
     * @return Value of property actLevel.
     */
    public int getActLevel() {
        return this.actLevel;
    }
    
    /**
     * Setter for property actLevel.
     * @param actLevel New value of property actLevel.
     */
    public void setActLevel(int actLevel) {
        this.actLevel = actLevel;
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
    
    /**
     * Getter for property actType.
     * @return Value of property actType.
     */
    public int getActType() {
        return this.actType;
    }
    
    /**
     * Setter for property actType.
     * @param actType New value of property actType.
     */
    public void setActType(int actType) {
        this.actType = actType;
    }
    
    /**
     * Getter for property orderBy.
     * @return Value of property orderBy.
     */
    public int getOrderBy() {
        return this.orderBy;
    }
    
    /**
     * Setter for property orderBy.
     * @param orderBy New value of property orderBy.
     */
    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }
    
}
