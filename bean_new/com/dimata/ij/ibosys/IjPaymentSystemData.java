/*
 * PaymentSystemData.java
 *
 * Created on January 14, 2005, 3:54 PM
 */

package com.dimata.ij.ibosys;

/**
 *
 * @author  gedhy
 */
public class IjPaymentSystemData {
    
    /** Holds value of property psId. */
    private long psId;
    
    /** Holds value of property psName. */
    private String psName;
    
    /** Creates a new instance of PaymentSystemData */
    public IjPaymentSystemData() {
    }
    
    /** Getter for property psId.
     * @return Value of property psId.
     *
     */
    public long getPsId() {
        return this.psId;
    }
    
    /** Setter for property psId.
     * @param psId New value of property psId.
     *
     */
    public void setPsId(long psId) {
        this.psId = psId;
    }
    
    /** Getter for property psName.
     * @return Value of property psName.
     *
     */
    public String getPsName() {
        return this.psName;
    }
    
    /** Setter for property psName.
     * @param psName New value of property psName.
     *
     */
    public void setPsName(String psName) {
        this.psName = psName;
    }
    
}
