/*
 * Jdetail.java
 *
 * Created on December 29, 2004, 12:06 PM
 */

package com.dimata.ij.iaiso;

// qdep package
import com.dimata.qdep.entity.*;

// import core java package
import java.util.*;

/**
 *
 * @author  Administrator
 * @version 
 */
public class IjJournalDetail extends Entity {

    /** Holds value of property jdetAccChart. */
    private long jdetAccChart;
    
    /** Holds value of property jdetDebet. */
    private double jdetDebet;
    
    /** Holds value of property jdetCredit. */
    private double jdetCredit;
    
    /** Holds value of property jdetMainOid. */
    private long jdetMainOid;
    
    /** Holds value of property jdetTranCurrency. */
    private long jdetTransCurrency;
    
    /** Holds value of property jdetTranRate. */
    private double jdetTransRate;
    
    /** Creates new Jdetail */
    public IjJournalDetail() {
    }

    /** Getter for property jdetAccChart.
     * @return Value of property jdetAccChart.
     *
     */
    public long getJdetAccChart() {
        return this.jdetAccChart;
    }
    
    /** Setter for property jdetAccChart.
     * @param jdetAccChart New value of property jdetAccChart.
     *
     */
    public void setJdetAccChart(long jdetAccChart) {
        this.jdetAccChart = jdetAccChart;
    }
    
    /** Getter for property jdetDebet.
     * @return Value of property jdetDebet.
     *
     */
    public double getJdetDebet() {
        return this.jdetDebet;
    }
    
    /** Setter for property jdetDebet.
     * @param jdetDebet New value of property jdetDebet.
     *
     */
    public void setJdetDebet(double jdetDebet) {
        this.jdetDebet = jdetDebet;
    }
    
    /** Getter for property jdetCredit.
     * @return Value of property jdetCredit.
     *
     */
    public double getJdetCredit() {
        return this.jdetCredit;
    }
    
    /** Setter for property jdetCredit.
     * @param jdetCredit New value of property jdetCredit.
     *
     */
    public void setJdetCredit(double jdetCredit) {
        this.jdetCredit = jdetCredit;
    }
    
    /** Getter for property jdetMainOid.
     * @return Value of property jdetMainOid.
     */
    public long getJdetMainOid() {
        return jdetMainOid;
    }
    
    /** Setter for property jdetMainOid.
     * @param jdetMainOid New value of property jdetMainOid.
     */
    public void setJdetMainOid(long jdetMainOid) {
        this.jdetMainOid = jdetMainOid;
    }
    
    /** Getter for property jdetTransCurrency.
     * @return Value of property jdetTransCurrency.
     */
    public long getJdetTransCurrency() {
        return jdetTransCurrency;
    }
    
    /** Setter for property jdetTransCurrency.
     * @param jdetTranCurrency New value of property jdetTransCurrency.
     */
    public void setJdetTransCurrency(long jdetTransCurrency) {
        this.jdetTransCurrency = jdetTransCurrency;
    }
    
    /** Getter for property jdetTransRate.
     * @return Value of property jdetTransRate.
     */
    public double getJdetTransRate() {
        return jdetTransRate;
    }
    
    /** Setter for property jdetTransRate.
     * @param jdetTransRate New value of property jdetTransRate.
     */
    public void setJdetTransRate(double jdetTransRate) {
        this.jdetTransRate = jdetTransRate;
    }
    
}
