/*
 * ProfitLoss.java
 *
 * Created on September 11, 2003, 1:30 PM
 */

package com.dimata.aiso.entity.report;

/**
 *
 * @author  gedhy
 */
public class ProfitLoss {

    private long accountId    = 0;
    private double debetBefore  = 0;
    private double debetSelected  = 0;
    private double creditBefore = 0;
    private double creditSelected = 0;

    // new
    private double budget = 0;

    public String getNamaPerkiraan() {
        return namaPerkiraan;
    }

    public void setNamaPerkiraan(String namaPerkiraan) {
        this.namaPerkiraan = namaPerkiraan;
    }

    private String namaPerkiraan = "";

    public long getAccountId() {
        return accountId;
    } 

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private double value = 0;

    /**
     * Holds value of property accountNameEnglish.
     */
    private String accountNameEnglish = "";
    
    /**
     * Holds value of property ytdBudget.
     */
    private double ytdBudget = 0;
    
    /**
     * Holds value of property accNumber.
     */
    private String accNumber = "";
    
    /**
     * Holds value of property ytdValue.
     */
    private double ytdValue = 0;
    
    /**
     * Holds value of property idParent.
     */
    private long idParent = 0;
    
    public long getAccocuntId(){
        return accountId; 
    }

    public void setAccocuntId(long accountId){ 
        this.accountId = accountId; 
    }

    public double getDebetBefore(){
        return debetBefore; 
    }

    public void setDebetBefore(double debetBefore){
        this.debetBefore = debetBefore; 
    }
    
    public double getDebetSelected(){ 
        return debetSelected; 
    }

    public void setDebetSelected(double debetSelected){ 
        this.debetSelected = debetSelected; 
    }    
    
    public double getCreditBefore(){ 
        return creditBefore; 
    }

    public void setCreditBefore(double creditBefore){ 
        this.creditBefore = creditBefore; 
    }
    
    public double getCreditSelected(){ 
        return creditSelected; 
    }

    public void setCreditSelected(double creditSelected){ 
        this.creditSelected = creditSelected; 
    }       
    
    /**
     * Getter for property accountNameEnglish.
     * @return Value of property accountNameEnglish.
     */
    public String getAccountNameEnglish() {
        return this.accountNameEnglish;
    }
    
    /**
     * Setter for property accountNameEnglish.
     * @param accountNameEnglish New value of property accountNameEnglish.
     */
    public void setAccountNameEnglish(String accountNameEnglish) {
        this.accountNameEnglish = accountNameEnglish;
    }
    
    /**
     * Getter for property ytdBudget.
     * @return Value of property ytdBudget.
     */
    public double getYtdBudget() {
        return this.ytdBudget;
    }
    
    /**
     * Setter for property ytdBudget.
     * @param ytdBudget New value of property ytdBudget.
     */
    public void setYtdBudget(double ytdBudget) {
        this.ytdBudget = ytdBudget;
    }
    
    /**
     * Getter for property accNumber.
     * @return Value of property accNumber.
     */
    public String getAccNumber() {
        return this.accNumber;
    }
    
    /**
     * Setter for property accNumber.
     * @param accNumber New value of property accNumber.
     */
    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }
    
    /**
     * Getter for property ytdValue.
     * @return Value of property ytdValue.
     */
    public double getYtdValue() {
        return this.ytdValue;
    }
    
    /**
     * Setter for property ytdValue.
     * @param ytdValue New value of property ytdValue.
     */
    public void setYtdValue(double ytdValue) {
        this.ytdValue = ytdValue;
    }
    
    /**
     * Getter for property idParent.
     * @return Value of property idParent.
     */
    public long getIdParent() {
        return this.idParent;
    }
    
    /**
     * Setter for property idParent.
     * @param idParent New value of property idParent.
     */
    public void setIdParent(long idParent) {
        this.idParent = idParent;
    }
    
}
