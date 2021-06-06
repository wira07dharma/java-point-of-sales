/*
 * AktivaLocation.java
 *
 * Created on February 25, 2008, 5:04 PM
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author  DWI
 */
public class AktivaLocation extends Entity {
    
    /**
     * Holds value of property aktivaLocCode.
     */
    private String aktivaLocCode = "";    
    
    /**
     * Holds value of property aktivaLocName.
     */
    private String aktivaLocName = "";
    
    /**
     * Getter for property aktivaLocCode.
     * @return Value of property aktivaLocCode.
     */
    public String getAktivaLocCode() {
        return this.aktivaLocCode;
    }    
    
    /**
     * Setter for property aktivaLocCode.
     * @param aktivaLocCode New value of property aktivaLocCode.
     */
    public void setAktivaLocCode(String aktivaLocCode) {
        this.aktivaLocCode = aktivaLocCode;
    }
    
    /**
     * Getter for property aktivaLocName.
     * @return Value of property aktivaLocName.
     */
    public String getAktivaLocName() {
        return this.aktivaLocName;
    }
    
    /**
     * Setter for property aktivaLocName.
     * @param aktivaLocName New value of property aktivaLocName.
     */
    public void setAktivaLocName(String aktivaLocName) {
        this.aktivaLocName = aktivaLocName;
    }
    
}
