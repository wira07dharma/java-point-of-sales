/*
 * IjEngineParam.java
 *
 * Created on October 2, 2005, 5:58 PM
 */

package com.dimata.ij.session.engine;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;

/**
 *
 * @author  Administrator 
 * @version 
 */
public class IjEngineParam {

    /** Holds value of property dStartTransactionDate. */
    private Date dStartTransactionDate;
    
    /** Holds value of property dFinishTransactionDate. */
    private Date dFinishTransactionDate;
    
    /** Holds value of property iBoSystem. */
    private int iBoSystem;
    
    /** Holds value of property vLocationOid. */
    private Vector vLocationOid;
    
    /** Holds value of property lBookType. */
    private long lBookType;
    
    /** Holds value of property iLanguage. */
    private int iLanguage;
    
    /** Holds value of property lCurrPeriodeOid. */
    private long lCurrPeriodeOid;
    
    /** Holds value of property dStartDatePeriode. */
    private Date dStartDatePeriode;
    
    /** Holds value of property dEndDatePeriode. */
    private Date dEndDatePeriode;
    
    /** Holds value of property dLastEntryDatePeriode. */
    private Date dLastEntryDatePeriode;
    
    /** Holds value of property lOperatorOid. */
    private long lOperatorOid;
    
    /** Holds value of property iJournalSystem. */
    private int iJournalSystem;
    
    /** Holds value of property iConfPayment. */
    private int iConfPayment;
    
    /** Holds value of property iConfInventory. */
    private int iConfInventory;
    
    /** Holds value of property iConfTaxOnSales. */
    private int iConfTaxOnSales;
    
    /** Holds value of property iConfTaxOnBuy. */
    private int iConfTaxOnBuy;
    
    /** Holds value of property hStandartRate. */
    private Hashtable hStandartRate;
    
    /** Holds value of property sIjImplBo. */
    private String sIjImplBo;
    
    /**
     * Holds value of property bRptDepartmental.
     */
    private boolean bRptDepartmental;
    
    /** Creates new IjEngineParam */
    public IjEngineParam() {
    }

    /** Getter for property dStartTransactionDate.
     * @return Value of property dStartTransactionDate.
     */
    public Date getDStartTransactionDate() {
        return dStartTransactionDate;
    }
    
    /** Setter for property dStartTransactionDate.
     * @param dStartTransactionDate New value of property dStartTransactionDate.
     */
    public void setDStartTransactionDate(Date dStartTransactionDate) {
        this.dStartTransactionDate = dStartTransactionDate;
    }
    
    /** Getter for property dFinishTransactionDate.
     * @return Value of property dFinishTransactionDate.
     */
    public Date getDFinishTransactionDate() {
        return dFinishTransactionDate;
    }
    
    /** Setter for property dFinishTransactionDate.
     * @param dFinishTransactionDate New value of property dFinishTransactionDate.
     */
    public void setDFinishTransactionDate(Date dFinishTransactionDate) {
        this.dFinishTransactionDate = dFinishTransactionDate;
    }
    
    /** Getter for property iBoSystem.
     * @return Value of property iBoSystem.
     */
    public int getIBoSystem() {
        return iBoSystem;
    }
    
    /** Setter for property iBoSystem.
     * @param iBoSystem New value of property iBoSystem.
     */
    public void setIBoSystem(int iBoSystem) {
        this.iBoSystem = iBoSystem;
    }
    
    /** Getter for property vLocationOid.
     * @return Value of property vLocationOid.
     */
    public Vector getVLocationOid() {
        return vLocationOid;
    }
    
    /** Setter for property vLocationOid.
     * @param vLocationOid New value of property vLocationOid.
     */
    public void setVLocationOid(Vector vLocationOid) {
        this.vLocationOid = vLocationOid;
    }
    
    /** Getter for property lBookType.
     * @return Value of property lBookType.
     */
    public long getLBookType() {
        return lBookType;
    }
    
    /** Setter for property lBookType.
     * @param lBookType New value of property lBookType.
     */
    public void setLBookType(long lBookType) {
        this.lBookType = lBookType;
    }
    
    /** Getter for property iLanguage.
     * @return Value of property iLanguage.
     */
    public int getILanguage() {
        return iLanguage;
    }
    
    /** Setter for property iLanguage.
     * @param iLanguage New value of property iLanguage.
     */
    public void setILanguage(int iLanguage) {
        this.iLanguage = iLanguage;
    }
    
    /** Getter for property lCurrPeriodeOid.
     * @return Value of property lCurrPeriodeOid.
     */
    public long getLCurrPeriodeOid() {
        return lCurrPeriodeOid;
    }
    
    /** Setter for property lCurrPeriodeOid.
     * @param lCurrPeriodeOid New value of property lCurrPeriodeOid.
     */
    public void setLCurrPeriodeOid(long lCurrPeriodeOid) {
        this.lCurrPeriodeOid = lCurrPeriodeOid;
    }
    
    /** Getter for property dStartDatePeriode.
     * @return Value of property dStartDatePeriode.
     */
    public Date getDStartDatePeriode() {
        return dStartDatePeriode;
    }
    
    /** Setter for property dStartDatePeriode.
     * @param dStartDatePeriode New value of property dStartDatePeriode.
     */
    public void setDStartDatePeriode(Date dStartDatePeriode) {
        this.dStartDatePeriode = dStartDatePeriode;
    }
    
    /** Getter for property dEndDatePeriode.
     * @return Value of property dEndDatePeriode.
     */
    public Date getDEndDatePeriode() {
        return dEndDatePeriode;
    }
    
    /** Setter for property dEndDatePeriode.
     * @param dEndDatePeriode New value of property dEndDatePeriode.
     */
    public void setDEndDatePeriode(Date dEndDatePeriode) {
        this.dEndDatePeriode = dEndDatePeriode;
    }
    
    /** Getter for property dLastEntryDatePeriode.
     * @return Value of property dLastEntryDatePeriode.
     */
    public Date getDLastEntryDatePeriode() {
        return dLastEntryDatePeriode;
    }
    
    /** Setter for property dLastEntryDatePeriode.
     * @param dLastEntryDatePeriode New value of property dLastEntryDatePeriode.
     */
    public void setDLastEntryDatePeriode(Date dLastEntryDatePeriode) {
        this.dLastEntryDatePeriode = dLastEntryDatePeriode;
    }
    
    /** Getter for property lOperatorOid.
     * @return Value of property lOperatorOid.
     */
    public long getLOperatorOid() {
        return lOperatorOid;
    }
    
    /** Setter for property lOperatorOid.
     * @param lOperatorOid New value of property lOperatorOid.
     */
    public void setLOperatorOid(long lOperatorOid) {
        this.lOperatorOid = lOperatorOid;
    }
    
    /** Getter for property iJournalSystem.
     * @return Value of property iJournalSystem.
     */
    public int getIConfJournalSystem() {
        return iJournalSystem;
    }
    
    /** Setter for property iJournalSystem.
     * @param iJournalSystem New value of property iJournalSystem.
     */
    public void setIConfJournalSystem(int iJournalSystem) {
        this.iJournalSystem = iJournalSystem;
    }
    
    /** Getter for property iConfPayment.
     * @return Value of property iConfPayment.
     */
    public int getIConfPayment() {
        return iConfPayment;
    }
    
    /** Setter for property iConfPayment.
     * @param iConfPayment New value of property iConfPayment.
     */
    public void setIConfPayment(int iConfPayment) {
        this.iConfPayment = iConfPayment;
    }
    
    /** Getter for property iConfInventory.
     * @return Value of property iConfInventory.
     */
    public int getIConfInventory() {
        return iConfInventory;
    }
    
    /** Setter for property iConfInventory.
     * @param iConfInventory New value of property iConfInventory.
     */
    public void setIConfInventory(int iConfInventory) {
        this.iConfInventory = iConfInventory;
    }
    
    /** Getter for property iConfTaxOnSales.
     * @return Value of property iConfTaxOnSales.
     */
    public int getIConfTaxOnSales() {
        return iConfTaxOnSales;
    }
    
    /** Setter for property iConfTaxOnSales.
     * @param iConfTaxOnSales New value of property iConfTaxOnSales.
     */
    public void setIConfTaxOnSales(int iConfTaxOnSales) {
        this.iConfTaxOnSales = iConfTaxOnSales;
    }
    
    /** Getter for property iConfTaxOnBuy.
     * @return Value of property iConfTaxOnBuy.
     */
    public int getIConfTaxOnBuy() {
        return iConfTaxOnBuy;
    }
    
    /** Setter for property iConfTaxOnBuy.
     * @param iConfTaxOnBuy New value of property iConfTaxOnBuy.
     */
    public void setIConfTaxOnBuy(int iConfTaxOnBuy) {
        this.iConfTaxOnBuy = iConfTaxOnBuy;
    }
    
    /** Getter for property hStandartRate.
     * @return Value of property hStandartRate.
     */
    public Hashtable getHStandartRate() {
        return hStandartRate;
    }
    
    /** Setter for property hStandartRate.
     * @param hStandartRate New value of property hStandartRate.
     */
    public void setHStandartRate(Hashtable hStandartRate) {
        this.hStandartRate = hStandartRate;
    }
    
    /** Getter for property sIjImplBo.
     * @return Value of property sIjImplBo.
     */
    public String getSIjImplBo() {
        return sIjImplBo;
    }
    
    /** Setter for property sIjImplBo.
     * @param sIjImplBo New value of property sIjImplBo.
     */
    public void setSIjImplBo(String sIjImplBo) {
        this.sIjImplBo = sIjImplBo;
    }
    
    /**
     * Getter for property bRptDepartmental.
     * @return Value of property bRptDepartmental.
     */
    public boolean isBRptDepartmental() {
        return this.bRptDepartmental;
    }
    
    /**
     * Setter for property bRptDepartmental.
     * @param bRptDepartmental New value of property bRptDepartmental.
     */
    public void setBRptDepartmental(boolean bRptDepartmental) {
        this.bRptDepartmental = bRptDepartmental;
    }
    
}
