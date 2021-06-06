/*
 * SpecialJournalDetailAssignt.java
 *
 * Created on February 9, 2007, 9:48 AM
 */

package com.dimata.aiso.entity.specialJournal;

/* import package qdep Entity */
import com.dimata.qdep.entity.Entity;

/**
 *
 * @author  dwi
 */
public class SpecialJournalDetailAssignt extends Entity {
    
    /**
     * Holds value of property amount.
     */
    private double amount = 0;    
    
    /**
     * Holds value of property share_procentage.
     */
    private float share_procentage = 0;
    
    /**
     * Holds value of property journal_detail_id.
     */
    private long journal_detail_id = 0;
    
    /**
     * Holds value of property activity_id.
     */
    private long activity_id = 0;
    
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
    private long jurnalIndex;
    
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
     * Getter for property share_procentage.
     * @return Value of property share_procentage.
     */
    public float getShareProcentage() {
        return this.share_procentage;
    }
    
    /**
     * Setter for property share_procentage.
     * @param share_procentage New value of property share_procentage.
     */
    public void setShareProcentage(float share_procentage) {
        this.share_procentage = share_procentage;
    }
    
    /**
     * Getter for property journal_detail_id.
     * @return Value of property journal_detail_id.
     */
    public long getJournalDetailId() {
        return this.journal_detail_id;
    }
    
    /**
     * Setter for property journal_detail_id.
     * @param journal_detail_id New value of property journal_detail_id.
     */
    public void setJournalDetailId(long journal_detail_id) {
        this.journal_detail_id = journal_detail_id;
    }
    
    /**
     * Getter for property activity_id.
     * @return Value of property activity_id.
     */
    public long getActivityId() {
        return this.activity_id;
    }
    
    /**
     * Setter for property activity_id.
     * @param activity_id New value of property activity_id.
     */
    public void setActivityId(long activity_id) {
        this.activity_id = activity_id;
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
    
}
