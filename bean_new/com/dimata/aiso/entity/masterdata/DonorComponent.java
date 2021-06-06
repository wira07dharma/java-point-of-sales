/*
 * DonorComponent.java
 *
 * Created on December 31, 2007, 3:40 PM
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.*;

/**
 *
 * @author  dwi
 */
public class DonorComponent extends Entity {
    
    /**
     * Holds value of property code.
     */
    private String code;
    
    /**
     * Holds value of property name.
     */
    private String name;
    
    /**
     * Holds value of property description.
     */
    private String description;
    
    /**
     * Holds value of property contactId.
     */
    private long contactId;
    
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
     * Getter for property contactId.
     * @return Value of property contactId.
     */
    public long getContactId() {
        return this.contactId;
    }
    
    /**
     * Setter for property contactId.
     * @param contactId New value of property contactId.
     */
    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
    
    /** Creates a new instance of DonorComponent */
    
    
}
