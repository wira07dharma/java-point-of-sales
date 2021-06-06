/*
 * PrintBalanceObj.java
 *
 * Created on January 5, 2005, 2:18 PM
 */

package com.dimata.pos.printAPI;

/**
 *
 * @author  yogi
 */
import java.util.Date;
import java.util.Vector;

public class PrintBalanceObj {
    
    /** Holds value of property dateBalance. */
    private Date dateBalance;
    
    /** Holds value of property timeBalance. */
    private Date timeBalance;
    
    /** Holds value of property cashier. */
    private String cashier;
    
    /** Holds value of property listDataBalance. */
    private Vector listDataBalance;
    
    /** Creates a new instance of PrintBalanceObj */
    public PrintBalanceObj() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }
    
    /** Getter for property dateBalance.
     * @return Value of property dateBalance.
     *
     */
    public Date getDateBalance() {
        return this.dateBalance;
    }
    
    /** Setter for property dateBalance.
     * @param dateBalance New value of property dateBalance.
     *
     */
    public void setDateBalance(Date dateBalance) {
        this.dateBalance = dateBalance;
    }
    
    /** Getter for property timeBalance.
     * @return Value of property timeBalance.
     *
     */
    public Date getTimeBalance() {
        return this.timeBalance;
    }
    
    /** Setter for property timeBalance.
     * @param timeBalance New value of property timeBalance.
     *
     */
    public void setTimeBalance(Date timeBalance) {
        this.timeBalance = timeBalance;
    }
    
    /** Getter for property cashier.
     * @return Value of property cashier.
     *
     */
    public String getCashier() {
        return this.cashier;
    }
    
    /** Setter for property cashier.
     * @param cashier New value of property cashier.
     *
     */
    public void setCashier(String cashier) {
        this.cashier = cashier;
    }
    
    /** Getter for property listDataBalance.
     * @return Value of property listDataBalance.
     *
     */
    public Vector getListDataBalance() {
        return this.listDataBalance;
    }
    
    /** Setter for property listDataBalance.
     * @param listDataBalance New value of property listDataBalance.
     *
     */
    public void setListDataBalance(Vector listDataBalance) {
        this.listDataBalance = listDataBalance;
    }
    
}
