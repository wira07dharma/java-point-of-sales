/*
 * SpecialJournalDetail.java
 *
 * Created on February 6, 2007, 10:08 AM
 */

package com.dimata.aiso.entity.specialJournal;

/* import package qdep Entity */
import com.dimata.qdep.entity.Entity;

/**
 *
 * @author  dwi
 */
public class SpecialJournalDetail extends Entity {
    
    /**
     * Holds value of property description.
     */
    private String description = "";    
    
    /**
     * Holds value of property amount.
     */
    private double amount = 0;
    
    /**
     * Holds value of property amount_status.
     */
    private int amount_status = 0;
    
    /**
     * Holds value of property currency_rate.
     */
    private double currency_rate = 0;
    
    /**
     * Holds value of property journal_main_id.
     */
    private long journal_main_id = 0;
    
    /**
     * Holds value of property id_perkiraan.
     */
    private long id_perkiraan = 0;
    
    /**
     * Holds value of property contact_id.
     */
    private long contact_id = 0;
    
    /**
     * Holds value of property currency_type_id.
     */
    private long currency_type_id = 0;
    
    /**
     * Holds value of property dataStatus.
     */
    private int dataStatus = 0;
    
    /**
     * Holds value of property index.
     */
    private int index;
    
    /**
     * Holds value of property jurnalIndex.
     */
    private long jurnalIndex = 0;
    
    /**
     * Holds value of property department_id.
     */
    private long department_id;
    
    /**
     * Holds value of property budgetBalance.
     */
    private double budgetBalance = 0;
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */    
    
    public String getDescription() {
        return this.description;
    }    
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return this.amount;
    }
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /**
     * Getter for property amount_status.
     * @return Value of property amount_status.
     */
    public int getAmountStatus() {
        return this.amount_status;
    }
    
    /**
     * Setter for property amount_status.
     * @param amount_status New value of property amount_status.
     */
    public void setAmountStatus(int amount_status) {
        this.amount_status = amount_status;
    }
    
    /**
     * Getter for property currency_rate.
     * @return Value of property currency_rate.
     */
    public double getCurrencyRate() {
        return this.currency_rate;
    }
    
    /**
     * Setter for property currency_rate.
     * @param currency_rate New value of property currency_rate.
     */
    public void setCurrencyRate(double currency_rate) {
        this.currency_rate = currency_rate;
    }
    
    /**
     * Getter for property journal_main_id.
     * @return Value of property journal_main_id.
     */
    public long getJournalMainId() {
        return this.journal_main_id;
    }
    
    /**
     * Setter for property journal_main_id.
     * @param journal_main_id New value of property journal_main_id.
     */
    public void setJournalMainId(long journal_main_id) {
        this.journal_main_id = journal_main_id;
    }
    
    /**
     * Getter for property id_perkiraan.
     * @return Value of property id_perkiraan.
     */
    public long getIdPerkiraan() {
        return this.id_perkiraan;
    }
    
    /**
     * Setter for property id_perkiraan.
     * @param id_perkiraan New value of property id_perkiraan.
     */
    public void setIdPerkiraan(long id_perkiraan) {
        this.id_perkiraan = id_perkiraan;
    }
    
    /**
     * Getter for property contact_id.
     * @return Value of property contact_id.
     */
    public long getContactId() {
        return this.contact_id;
    }
    
    /**
     * Setter for property contact_id.
     * @param contact_id New value of property contact_id.
     */
    public void setContactId(long contact_id) {
        this.contact_id = contact_id;
    }
    
    /**
     * Getter for property currency_type_id.
     * @return Value of property currency_type_id.
     */
    public long getCurrencyTypeId() {
        return this.currency_type_id;
    }
    
    /**
     * Setter for property currency_type_id.
     * @param currency_type_id New value of property currency_type_id.
     */
    public void setCurrencyTypeId(long currency_type_id) {
        this.currency_type_id = currency_type_id;
    }
    
    /**
     * Getter for property dataStatus.
     * @return Value of property dataStatus.
     */
    public int getDataStatus() {
        return this.dataStatus;
    }
    
    /**
     * Setter for property dataStatus.
     * @param dataStatus New value of property dataStatus.
     */
    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }
    
    /**
     * Getter for property index.
     * @return Value of property index.
     */
    public int getIndex() {
        return this.index;
    }
    
    /**
     * Setter for property index.
     * @param index New value of property index.
     */
    public void setIndex(int index) {
        this.index = index;
    }
    
    /**
     * Getter for property jurnalIndex.
     * @return Value of property jurnalIndex.
     */
    public long getJurnalIndex() {
        return this.jurnalIndex;
    }
    
    /**
     * Setter for property jurnalIndex.
     * @param jurnalIndex New value of property jurnalIndex.
     */
    public void setJurnalIndex(long jurnalIndex) {
        this.jurnalIndex = jurnalIndex;
    }
    
    /**
     * Getter for property department_id.
     * @return Value of property department_id.
     */
    public long getDepartmentId() {
        return this.department_id;
    }
    
    /**
     * Setter for property department_id.
     * @param department_id New value of property department_id.
     */
    public void setDepartmentId(long department_id) {
        this.department_id = department_id;
    }
    
    /**
     * Getter for property budgetBalance.
     * @return Value of property budgetBalance.
     */
    public double getBudgetBalance() {
        return this.budgetBalance;
    }
    
    /**
     * Setter for property budgetBalance.
     * @param budgetBalance New value of property budgetBalance.
     */
    public void setBudgetBalance(double budgetBalance) {
        this.budgetBalance = budgetBalance;
    }
    
}
