/*
 * InventoryDoc.java
 *
 * Created on January 7, 2005, 8:43 AM
 */

package com.dimata.ij.ibosys;

/**
 *
 * @author  gedhy
 */
public class IjInventoryDoc {
    
    /** Holds value of property invSourceLocation. */
    private long invSourceLocation;
    
    /** Holds value of property invDestLocation. */
    private long invDestLocation;
    
    /** Holds value of property invProdDepartment. */
    private long invProdDepartment;
    
    /** Holds value of property invValue. */
    private double invValue;
    
    /** Creates a new instance of InventoryDoc */
    public IjInventoryDoc() {
    }
    
    /** Getter for property invSourceLocation.
     * @return Value of property invSourceLocation.
     *
     */
    public long getInvSourceLocation() {
        return this.invSourceLocation;
    }
    
    /** Setter for property invSourceLocation.
     * @param invSourceLocation New value of property invSourceLocation.
     *
     */
    public void setInvSourceLocation(long invSourceLocation) {
        this.invSourceLocation = invSourceLocation;
    }
    
    /** Getter for property invDestLocation.
     * @return Value of property invDestLocation.
     *
     */
    public long getInvDestLocation() {
        return this.invDestLocation;
    }
    
    /** Setter for property invDestLocation.
     * @param invDestLocation New value of property invDestLocation.
     *
     */
    public void setInvDestLocation(long invDestLocation) {
        this.invDestLocation = invDestLocation;
    }
    
    /** Getter for property prodDepartment.
     * @return Value of property prodDepartment.
     *
     */
    public long getInvProdDepartment() {
        return this.invProdDepartment;
    }
    
    /** Setter for property prodDepartment.
     * @param prodDepartment New value of property prodDepartment.
     *
     */
    public void setInvProdDepartment(long invProdDepartment) {
        this.invProdDepartment = invProdDepartment;
    }
    
    /** Getter for property invValue.
     * @return Value of property invValue.
     *
     */
    public double getInvValue() {
        return this.invValue;
    }
    
    /** Setter for property invValue.
     * @param invValue New value of property invValue.
     *
     */
    public void setInvValue(double invValue) {
        this.invValue = invValue;
    }
    
}
