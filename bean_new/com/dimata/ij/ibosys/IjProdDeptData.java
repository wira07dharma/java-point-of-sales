/*
 * ProdDeptData.java
 *
 * Created on January 14, 2005, 2:46 PM
 */

package com.dimata.ij.ibosys;

/**
 *
 * @author  gedhy
 */
public class IjProdDeptData {
    
    /** Holds value of property pdId. */
    private long pdId;
    
    /** Holds value of property pdCode. */
    private String pdCode;
    
    /** Holds value of property pdName. */
    private String pdName;
    
    /** Creates a new instance of ProdDeptData */
    public IjProdDeptData() {
    }
    
    /** Getter for property pdId.
     * @return Value of property pdId.
     *
     */
    public long getPdId() {
        return this.pdId;
    }
    
    /** Setter for property pdId.
     * @param pdId New value of property pdId.
     *
     */
    public void setPdId(long pdId) {
        this.pdId = pdId;
    }
    
    /** Getter for property pdCode.
     * @return Value of property pdCode.
     *
     */
    public String getPdCode() {
        return this.pdCode;
    }
    
    /** Setter for property pdCode.
     * @param pdCode New value of property pdCode.
     *
     */
    public void setPdCode(String pdCode) {
        this.pdCode = pdCode;
    }
    
    /** Getter for property pdName.
     * @return Value of property pdName.
     *
     */
    public String getPdName() {
        return this.pdName;
    }
    
    /** Setter for property pdName.
     * @param pdName New value of property pdName.
     *
     */
    public void setPdName(String pdName) {
        this.pdName = pdName;
    }
    
}
