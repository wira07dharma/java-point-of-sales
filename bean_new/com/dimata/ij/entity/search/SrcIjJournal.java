/*
 * srcIjJournal.java
 *
 * Created on February 21, 2005, 8:19 AM
 */

package com.dimata.ij.entity.search;

// import core java package
import java.util.Date;

/**
 *
 * @author  gedhy
 */
public class SrcIjJournal {
    
    /** Holds value of property selectedTransDate. */
    private int selectedTransDate;
    
    /** Holds value of property transStartDate. */
    private Date transStartDate;
    
    /** Holds value of property transEndDate. */
    private Date transEndDate;
    
    /** Holds value of property billNumber. */
    private String billNumber;
    
    /** Holds value of property contactName. */
    private String contactName;
    
    /** Holds value of property transCurrency. */
    private long transCurrency;
    
    /** Holds value of property journalStatus. */
    private int journalStatus;
    
    /** Holds value of property sortBy. */
    private int sortBy;
    
    /** Creates a new instance of srcIjJournal */
    public SrcIjJournal() {
    }
    
    /** Getter for property selectedTransDate.
     * @return Value of property selectedTransDate.
     *
     */
    public int getSelectedTransDate() {
        return this.selectedTransDate;
    }
    
    /** Setter for property selectedTransDate.
     * @param selectedTransDate New value of property selectedTransDate.
     *
     */
    public void setSelectedTransDate(int selectedTransDate) {
        this.selectedTransDate = selectedTransDate;
    }
    
    /** Getter for property startDate.
     * @return Value of property startDate.
     *
     */
    public Date getTransStartDate() {
        return this.transStartDate;
    }
    
    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     *
     */
    public void setTransStartDate(Date transStartDate) {
        this.transStartDate = transStartDate;
    }
    
    /** Getter for property endDate.
     * @return Value of property endDate.
     *
     */
    public Date getTransEndDate() {
        return this.transEndDate;
    }
    
    /** Setter for property endDate.
     * @param endDate New value of property endDate.
     *
     */
    public void setTransEndDate(Date transEndDate) {
        this.transEndDate = transEndDate;
    }
    
    /** Getter for property billNumber.
     * @return Value of property billNumber.
     *
     */
    public String getBillNumber() {
        return this.billNumber;
    }
    
    /** Setter for property billNumber.
     * @param billNumber New value of property billNumber.
     *
     */
    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }
    
    /** Getter for property contactName.
     * @return Value of property contactName.
     *
     */
    public String getContactName() {
        return this.contactName;
    }
    
    /** Setter for property contactName.
     * @param contactName New value of property contactName.
     *
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    
    /** Getter for property transCurrency.
     * @return Value of property transCurrency.
     *
     */
    public long getTransCurrency() {
        return this.transCurrency;
    }
    
    /** Setter for property transCurrency.
     * @param transCurrency New value of property transCurrency.
     *
     */
    public void setTransCurrency(long transCurrency) {
        this.transCurrency = transCurrency;
    }
    
    /** Getter for property journalStatus.
     * @return Value of property journalStatus.
     *
     */
    public int getJournalStatus() {
        return this.journalStatus;
    }
    
    /** Setter for property journalStatus.
     * @param journalStatus New value of property journalStatus.
     *
     */
    public void setJournalStatus(int journalStatus) {
        this.journalStatus = journalStatus;
    }
    
    /** Getter for property sortBy.
     * @return Value of property sortBy.
     *
     */
    public int getSortBy() {
        return this.sortBy;
    }
    
    /** Setter for property sortBy.
     * @param sortBy New value of property sortBy.
     *
     */
    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }
    
}
