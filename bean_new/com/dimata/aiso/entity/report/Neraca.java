/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Aug 23, 2005
 * Time: 4:18:29 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.entity.report;

public class Neraca { 

    private String namaPerkiraan = "";
    private double value = 0.0;
    private double budget = 0.0;

    /**
     * Holds value of property accountNameEnglish.
     */
    private String accountNameEnglish = "";
    
    /**
     * Holds value of property idPerkiraan.
     */
    private long idPerkiraan = 0;
    
    /**
     * Holds value of property idParent.
     */
    private long idParent = 0;
    
    /**
     * Holds value of property prevValue.
     */
    private double prevValue = 0.0;
    
    /**
     * Holds value of property accNumber.
     */
    private String accNumber = "";
    
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getNamaPerkiraan() {
        return namaPerkiraan;
    }

    public void setNamaPerkiraan(String namaPerkiraan) {
        this.namaPerkiraan = namaPerkiraan;
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
     * Getter for property idPerkiraan.
     * @return Value of property idPerkiraan.
     */
    public long getIdPerkiraan() {
        return this.idPerkiraan;
    }
    
    /**
     * Setter for property idPerkiraan.
     * @param idPerkiraan New value of property idPerkiraan.
     */
    public void setIdPerkiraan(long idPerkiraan) {
        this.idPerkiraan = idPerkiraan;
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
    
    /**
     * Getter for property prevValue.
     * @return Value of property prevValue.
     */
    public double getPrevValue() {
        return this.prevValue;
    }
    
    /**
     * Setter for property prevValue.
     * @param prevValue New value of property prevValue.
     */
    public void setPrevValue(double prevValue) {
        this.prevValue = prevValue;
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
    
}
