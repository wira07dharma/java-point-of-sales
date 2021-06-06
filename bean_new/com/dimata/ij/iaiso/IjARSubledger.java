/*
 * ARSubledger.java
 *
 * Created on January 3, 2005, 7:07 PM
 */

package com.dimata.ij.iaiso;

// qdep package
import com.dimata.qdep.entity.*;

// import core java package
import java.util.*;


/**
 *
 * @author  gedhy
 */
public class IjARSubledger extends Entity {
        
    /** Holds value of property apParent. */
    private long arParent;
    
    /** Holds value of property apContact. */
    private long arContact;
    
    /** Holds value of property apAccChart. */
    private long arAccChart;
    
    /** Holds value of property apNoBill. */
    private String arNoBill;  
    
    /** Holds value of property apTransDate. */
    private Date arTransDate;
    
    /** Holds value of property apDebet. */
    private double arDebet;
    
    /** Holds value of property apCredit. */
    private double arCredit;
    
    /** Holds value of property apExpiredDate. */
    private Date arExpiredDate;
    
    /** Holds value of property apPaidStatus. */
    private int arPaidStatus;
    
    /** Holds value of property arTransCurrency. */
    private long arTransCurrency;
    
    /** Holds value of property arTransRate. */
    private double arTransRate;
    
    /** Holds value of property arDetailOid. */
    private long arDetailOid;
    
    /** Creates a new instance of ARSubledger */
    public IjARSubledger() {
    }

    /** Getter for property arParent.
     * @return Value of property arParent.
     *
     */
    public long getArParent() {
        return this.arParent;
    }
    
    /** Setter for property arParent.
     * @param arParent New value of property arParent.
     *
     */
    public void setArParent(long arParent) {
        this.arParent = arParent;
    }
    
    /** Getter for property arContact.
     * @return Value of property arContact.
     *
     */
    public long getArContact() {
        return this.arContact;
    }
    
    /** Setter for property arContact.
     * @param arContact New value of property arContact.
     *
     */
    public void setArContact(long arContact) {
        this.arContact = arContact;
    }
    
    /** Getter for property arAccChart.
     * @return Value of property arAccChart.
     *
     */
    public long getArAccChart() {
        return this.arAccChart;
    }
    
    /** Setter for property arAccChart.
     * @param arAccChart New value of property arAccChart.
     *
     */
    public void setArAccChart(long arAccChart) {
        this.arAccChart = arAccChart;
    }
    
    /** Getter for property arNoBill.
     * @return Value of property arNoBill.
     *
     */
    public String getArNoBill() {
        return this.arNoBill;
    }
    
    /** Setter for property arNoBill.
     * @param arNoBill New value of property arNoBill.
     *
     */
    public void setArNoBill(String arNoBill) {
        this.arNoBill = arNoBill;
    }
    
    /** Getter for property arTransDate.
     * @return Value of property arTransDate.
     *
     */
    public Date getArTransDate() {
        return this.arTransDate;
    }
    
    /** Setter for property arTransDate.
     * @param arTransDate New value of property arTransDate.
     *
     */
    public void setArTransDate(Date arTransDate) {
        this.arTransDate = arTransDate;
    }
    
    /** Getter for property arDebet.
     * @return Value of property arDebet.
     *
     */
    public double getArDebet() {
        return this.arDebet;
    }
    
    /** Setter for property arDebet.
     * @param arDebet New value of property arDebet.
     *
     */
    public void setArDebet(double arDebet) {
        this.arDebet = arDebet;
    }
    
    /** Getter for property arCredit.
     * @return Value of property arCredit.
     *
     */
    public double getArCredit() {
        return this.arCredit;
    }
    
    /** Setter for property arCredit.
     * @param arCredit New value of property arCredit.
     *
     */
    public void setArCredit(double arCredit) {
        this.arCredit = arCredit;
    }
    
    /** Getter for property arExpiredDate.
     * @return Value of property arExpiredDate.
     *
     */
    public Date getArExpiredDate() {
        return this.arExpiredDate;
    }
    
    /** Setter for property arExpiredDate.
     * @param arExpiredDate New value of property arExpiredDate.
     *
     */
    public void setArExpiredDate(Date arExpiredDate) {
        this.arExpiredDate = arExpiredDate;
    }
    
    /** Getter for property arPaidStatus.
     * @return Value of property arPaidStatus.
     *
     */
    public int getArPaidStatus() {
        return this.arPaidStatus;
    }
    
    /** Setter for property arPaidStatus.
     * @param arPaidStatus New value of property arPaidStatus.
     *
     */
    public void setArPaidStatus(int arPaidStatus) {
        this.arPaidStatus = arPaidStatus;
    }
    
    /** Getter for property arTransCurrency.
     * @return Value of property arTransCurrency.
     */
    public long getArTransCurrency() {
        return arTransCurrency;
    }
    
    /** Setter for property arTransCurrency.
     * @param arTransCurrency New value of property arTransCurrency.
     */
    public void setArTransCurrency(long arTransCurrency) {
        this.arTransCurrency = arTransCurrency;
    }
    
    /** Getter for property arTransRate.
     * @return Value of property arTransRate.
     */
    public double getArTransRate() {
        return arTransRate;
    }
    
    /** Setter for property arTransRate.
     * @param arTransRate New value of property arTransRate.
     */
    public void setArTransRate(double arTransRate) {
        this.arTransRate = arTransRate;
    }
    
    /** Getter for property arDetailOid.
     * @return Value of property arDetailOid.
     */
    public long getArDetailOid() {
        return arDetailOid;
    }
    
    /** Setter for property arDetailOid.
     * @param arDetailOid New value of property arDetailOid.
     */
    public void setArDetailOid(long arDetailOid) {
        this.arDetailOid = arDetailOid;
    }
    
}
