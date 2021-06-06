/*
 * ProductionCostValue.java
 *
 * Created on January 7, 2005, 8:39 AM
 */

package com.dimata.ij.ibosys;

/**
 *
 * @author  gedhy
 */
public class IjProductionCostValue {    
    
    /** Holds value of property prodCostValue. */
    private double prodCostValue;
    
    /** Holds value of property prodDepartment. */
    private long prodDepartment;
    
    /** Creates a new instance of ProductionCostValue */
    public IjProductionCostValue() {
    }
    
    /** Getter for property buyingNominal.
     * @return Value of property buyingNominal.
     *
     */
    public double getProdCostValue() {
        return this.prodCostValue;
    }
    
    /** Setter for property buyingNominal.
     * @param buyingNominal New value of property buyingNominal.
     *
     */
    public void setProdCostValue(double prodCostValue) {
        this.prodCostValue = prodCostValue;
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
