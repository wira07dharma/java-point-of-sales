/*
 * DepartmentalCost.java
 *
 * Created on June 21, 2007, 4:12 PM
 */

package com.dimata.aiso.entity.report;

/**
 *
 * @author  dwi
 */
public class DepartmentalCost {
    
    /**
     * Holds value of property idParent.
     */
    private long idParent = 0;
   
    /**
     * Holds value of property accName.
     */
    private String accName = "";    
    
    /**
     * Holds value of property accNameEnglish.
     */
    private String accNameEnglish = "";
    
    /**
     * Holds value of property debitCreditAssignt.
     */
    private int debitCreditAssignt = 0;
    
    /**
     * Holds value of property mtdBudget.
     */
    private double mtdBudget = 0.0;
    
    /**
     * Holds value of property ytdBudget.
     */
    private double ytdBudget = 0.0;
    
    /**
     * Holds value of property mtdActual.
     */
    private double mtdActual = 0.0;
    
    /**
     * Holds value of property ytdActual.
     */
    private double ytdActual = 0.0;
    
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
     * Getter for property accName.
     * @return Value of property accName.
     */
    public String getAccName() {
        return this.accName;
    }
    
    /**
     * Setter for property accName.
     * @param accName New value of property accName.
     */
    public void setAccName(String accName) {
        this.accName = accName;
    }
    
    /**
     * Getter for property accNameEnglish.
     * @return Value of property accNameEnglish.
     */
    public String getAccNameEnglish() {
        return this.accNameEnglish;
    }
    
    /**
     * Setter for property accNameEnglish.
     * @param accNameEnglish New value of property accNameEnglish.
     */
    public void setAccNameEnglish(String accNameEnglish) {
        this.accNameEnglish = accNameEnglish;
    }
    
    /**
     * Getter for property debitCreditAssignt.
     * @return Value of property debitCreditAssignt.
     */
    public int getDebitCreditAssignt() {
        return this.debitCreditAssignt;
    }
    
    /**
     * Setter for property debitCreditAssignt.
     * @param debitCreditAssignt New value of property debitCreditAssignt.
     */
    public void setDebitCreditAssignt(int debitCreditAssignt) {
        this.debitCreditAssignt = debitCreditAssignt;
    }
    
    /**
     * Getter for property mtdBudget.
     * @return Value of property mtdBudget.
     */
    public double getMtdBudget() {
        return this.mtdBudget;
    }
    
    /**
     * Setter for property mtdBudget.
     * @param mtdBudget New value of property mtdBudget.
     */
    public void setMtdBudget(double mtdBudget) {
        this.mtdBudget = mtdBudget;
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
     * Getter for property mtdActual.
     * @return Value of property mtdActual.
     */
    public double getMtdActual() {
        return this.mtdActual;
    }
    
    /**
     * Setter for property mtdActual.
     * @param mtdActual New value of property mtdActual.
     */
    public void setMtdActual(double mtdActual) {
        this.mtdActual = mtdActual;
    }
    
    /**
     * Getter for property ytdActual.
     * @return Value of property ytdActual.
     */
    public double getYtdActual() {
        return this.ytdActual;
    }
    
    /**
     * Setter for property ytdActual.
     * @param ytdActual New value of property ytdActual.
     */
    public void setYtdActual(double ytdActual) {
        this.ytdActual = ytdActual;
    }
    
}
