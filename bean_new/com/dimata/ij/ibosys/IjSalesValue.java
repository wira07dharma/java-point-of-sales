/*
 * salesValue.java
 *
 * Created on January 7, 2005, 8:50 AM
 */

package com.dimata.ij.ibosys;

/**
 *
 * @author  gedhy
 */
public class IjSalesValue {
    
    /** Holds value of property prodDepartment. */
    private long prodDepartment;
    
    /** Holds value of property salesValue. */
    private double salesValue;
    
    /** Holds value of property costValue. */
    private double costValue;
    
    /** Creates a new instance of salesValue */
    public IjSalesValue() {
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
    
    /** Getter for property salesValue.
     * @return Value of property salesValue.
     *
     */
    public double getSalesValue() {
        return this.salesValue;
    }
    
    /** Setter for property salesValue.
     * @param salesValue New value of property salesValue.
     *
     */
    public void setSalesValue(double salesValue) {
        this.salesValue = salesValue;
    }
    
    /** Getter for property costValue.
     * @return Value of property costValue.
     *
     */
    public double getCostValue() {
        return this.costValue;
    }
    
    /** Setter for property costValue.
     * @param costValue New value of property costValue.
     *
     */
    public void setCostValue(double costValue) {
        this.costValue = costValue;
    }
    
}
