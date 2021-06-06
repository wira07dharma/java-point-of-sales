/*
 * PurchaseValueDoc.java
 *
 * Created on January 7, 2005, 8:31 AM
 */

package com.dimata.ij.ibosys;

/**
 *
 * @author  gedhy
 */
public class IjPurchaseValue {
    
    /** Holds value of property purchValue. */
    private double purchValue;
    
    /** Holds value of property prodDepartment. */
    private long prodDepartment;
    
    /** Creates a new instance of PurchaseValueDoc */
    public IjPurchaseValue() {
    }
    
    /** Getter for property buyingNominal.
     * @return Value of property buyingNominal.
     *
     */
    public double getPurchValue() {
        return this.purchValue;
    }
    
    /** Setter for property buyingNominal.
     * @param buyingNominal New value of property buyingNominal.
     *
     */
    public void setPurchValue(double purchValue) {
        this.purchValue = purchValue;
    }
    
    /** Getter for property prodDepartment.
     * @return Value of property prodDepartment.
     *
     */
    public long getProdDepartment() {
        return this.prodDepartment;
    }
    
    /** Setter for property prodDepartment.
     * @param prodDepartment New value of property prodDepartment.
     *
     */
    public void setProdDepartment(long prodDepartment) {
        this.prodDepartment = prodDepartment;
    }
    
}
