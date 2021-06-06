/*
 * LocationData.java
 *
 * Created on January 14, 2005, 2:46 PM
 */

package com.dimata.ij.ibosys;

/**
 *
 * @author  gedhy
 */
public class IjLocationData {
    
    /** Holds value of property locId. */
    private long locId;
    
    /** Holds value of property locCode. */
    private String locCode;
    
    /** Holds value of property locName. */
    private String locName;
    
    /** Creates a new instance of LocationData */
    public IjLocationData() {
    }
    
    /** Getter for property locId.
     * @return Value of property locId.
     *
     */
    public long getLocId() {
        return this.locId;
    }
    
    /** Setter for property locId.
     * @param locId New value of property locId.
     *
     */
    public void setLocId(long locId) {
        this.locId = locId;
    }
    
    /** Getter for property locCode.
     * @return Value of property locCode.
     *
     */
    public String getLocCode() {
        return this.locCode;
    }
    
    /** Setter for property locCode.
     * @param locCode New value of property locCode.
     *
     */
    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }
    
    /** Getter for property locName.
     * @return Value of property locName.
     *
     */
    public String getLocName() {
        return this.locName;
    }
    
    /** Setter for property locName.
     * @param locName New value of property locName.
     *
     */
    public void setLocName(String locName) {
        this.locName = locName;
    }
    
}
