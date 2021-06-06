/*
 * Subledger.java
 *
 * Created on December 29, 2004, 12:07 PM
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
public class IjAPSubledger extends Entity {

    /** Holds value of property apParent. */
    private long apParent;
    
    /** Holds value of property apContact. */
    private long apContact;
    
    /** Holds value of property apAccChart. */
    private long apAccChart;
    
    /** Holds value of property apNoBill. */
    private String apNoBill;
    
    /** Holds value of property apTransDate. */
    private Date apTransDate;
    
    /** Holds value of property apDebet. */
    private double apDebet;
    
    /** Holds value of property apCredit. */
    private double apCredit;
    
    /** Holds value of property apExpiredDate. */
    private Date apExpiredDate;
    
    /** Holds value of property apPaidStatus. */
    private int apPaidStatus;
    
    /** Holds value of property apTransCurrency. */
    private long apTransCurrency;
    
    /** Holds value of property apTransRate. */
    private double apTransRate;
    
    /** Holds value of property apDetailOid. */
    private long apDetailOid;
    
    /** Creates a new instance of APSubledger */
    public IjAPSubledger() {
    }

    /** Getter for property apParent.
     * @return Value of property apParent.
     *
     */
    public long getApParent() {
        return this.apParent;
    }
    
    /** Setter for property apParent.
     * @param apParent New value of property apParent.
     *
     */
    public void setApParent(long apParent) {
        this.apParent = apParent;
    }
    
    /** Getter for property apContact.
     * @return Value of property apContact.
     *
     */
    public long getApContact() {
        return this.apContact;
    }
    
    /** Setter for property apContact.
     * @param apContact New value of property apContact.
     *
     */
    public void setApContact(long apContact) {
        this.apContact = apContact;
    }
    
    /** Getter for property apAccChart.
     * @return Value of property apAccChart.
     *
     */
    public long getApAccChart() {
        return this.apAccChart;
    }
    
    /** Setter for property apAccChart.
     * @param apAccChart New value of property apAccChart.
     *
     */
    public void setApAccChart(long apAccChart) {
        this.apAccChart = apAccChart;
    }
    
    /** Getter for property apNoBill.
     * @return Value of property apNoBill.
     *
     */
    public String getApNoBill() {
        return this.apNoBill;
    }
    
    /** Setter for property apNoBill.
     * @param apNoBill New value of property apNoBill.
     *
     */
    public void setApNoBill(String apNoBill) {
        this.apNoBill = apNoBill;
    }
    
    /** Getter for property apTransDate.
     * @return Value of property apTransDate.
     *
     */
    public Date getApTransDate() {
        return this.apTransDate;
    }
    
    /** Setter for property apTransDate.
     * @param apTransDate New value of property apTransDate.
     *
     */
    public void setApTransDate(Date apTransDate) {
        this.apTransDate = apTransDate;
    }
    
    /** Getter for property apDebet.
     * @return Value of property apDebet.
     *
     */
    public double getApDebet() {
        return this.apDebet;
    }
    
    /** Setter for property apDebet.
     * @param apDebet New value of property apDebet.
     *
     */
    public void setApDebet(double apDebet) {
        this.apDebet = apDebet;
    }
    
    /** Getter for property apCredit.
     * @return Value of property apCredit.
     *
     */
    public double getApCredit() {
        return this.apCredit;
    }
    
    /** Setter for property apCredit.
     * @param apCredit New value of property apCredit.
     *
     */
    public void setApCredit(double apCredit) {
        this.apCredit = apCredit;
    }
    
    /** Getter for property apExpiredDate.
     * @return Value of property apExpiredDate.
     *
     */
    public Date getApExpiredDate() {
        return this.apExpiredDate;
    }
    
    /** Setter for property apExpiredDate.
     * @param apExpiredDate New value of property apExpiredDate.
     *
     */
    public void setApExpiredDate(Date apExpiredDate) {
        this.apExpiredDate = apExpiredDate;
    }
    
    /** Getter for property apPaidStatus.
     * @return Value of property apPaidStatus.
     *
     */
    public int getApPaidStatus() {
        return this.apPaidStatus;
    }
    
    /** Setter for property apPaidStatus.
     * @param apPaidStatus New value of property apPaidStatus.
     *
     */
    public void setApPaidStatus(int apPaidStatus) {
        this.apPaidStatus = apPaidStatus;
    }
    
    /** Getter for property apTransCurrency.
     * @return Value of property apTransCurrency.
     */
    public long getApTransCurrency() {
        return apTransCurrency;
    }
    
    /** Setter for property apTransCurrency.
     * @param apTransCurrency New value of property apTransCurrency.
     */
    public void setApTransCurrency(long apTransCurrency) {
        this.apTransCurrency = apTransCurrency;
    }
    
    /** Getter for property apTransRate.
     * @return Value of property apTransRate.
     */
    public double getApTransRate() {
        return apTransRate;
    }
    
    /** Setter for property apTransRate.
     * @param apTransRate New value of property apTransRate.
     */
    public void setApTransRate(double apTransRate) {
        this.apTransRate = apTransRate;
    }
    
    /** Getter for property apDetailOid.
     * @return Value of property apDetailOid.
     */
    public long getApDetailOid() {
        return apDetailOid;
    }
    
    /** Setter for property apDetailOid.
     * @param apDetailOid New value of property apDetailOid.
     */
    public void setApDetailOid(long apDetailOid) {
        this.apDetailOid = apDetailOid;
    }
    
}
