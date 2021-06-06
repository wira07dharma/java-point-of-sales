/*
 * SaleTypeData.java
 *
 * Created on January 14, 2005, 3:54 PM
 */

package com.dimata.ij.ibosys;

/**
 *
 * @author  gedhy
 */
public class IjSaleTypeData {
    
    /** Holds value of property stIdx. */
    private long stIdx;
    
    /** Holds value of property stName. */
    private String stName;
    
    /** Creates a new instance of SaleTypeData */
    public IjSaleTypeData() {
    }
    
    /** Getter for property stId.
     * @return Value of property stId.
     *
     */
    public long getStIdx() {
        return this.stIdx;
    }
    
    /** Setter for property stId.
     * @param stId New value of property stId.
     *
     */
    public void setStIdx(long stIdx) {
        this.stIdx = stIdx;
    }
    
    /** Getter for property stName.
     * @return Value of property stName.
     *
     */
    public String getStName() {
        return this.stName;
    }
    
    /** Setter for property stName.
     * @param stName New value of property stName.
     *
     */
    public void setStName(String stName) {
        this.stName = stName;
    }
    
}
