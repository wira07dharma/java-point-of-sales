/*
 * DonorReport.java
 *
 * Created on June 1, 2007, 2:10 PM
 */

package com.dimata.aiso.entity.report;

/**
 *
 * @author  dwi
 */
public class DonorReport {
    
    /**
     * Holds value of property actCode.
     */
    private String actCode = "";    
   
    /**
     * Holds value of property actDescription.
     */
    private String actDescription = "";
    
    /**
     * Holds value of property mtdBudget.
     */
    private double mtdBudget = 0;
    
    /**
     * Holds value of property ytdBudget.
     */
    private double ytdBudget = 0;
    
    /**
     * Holds value of property mtdAmount.
     */
    private double mtdAmount = 0;
    
    /**
     * Holds value of property ytdAmount.
     */
    private double ytdAmount = 0;
    
    /**
     * Holds value of property level.
     */
    private int level = 0;
    
    /**
     * Holds value of property sptAmount.
     */
    private double sptAmount = 0;
    
    /**
     * Getter for property actCode.
     * @return Value of property actCode.
     */
    public String getActCode() {
        return this.actCode;
    }    
    
    /**
     * Setter for property actCode.
     * @param actCode New value of property actCode.
     */
    public void setActCode(String actCode) {
        this.actCode = actCode;
    }
    
    /**
     * Getter for property actDescription.
     * @return Value of property actDescription.
     */
    public String getActDescription() {
        return this.actDescription;
    }
    
    /**
     * Setter for property actDescription.
     * @param actDescription New value of property actDescription.
     */
    public void setActDescription(String actDescription) {
        this.actDescription = actDescription;
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
     * Getter for property mtdAmount.
     * @return Value of property mtdAmount.
     */
    public double getMtdAmount() {
        return this.mtdAmount;
    }
    
    /**
     * Setter for property mtdAmount.
     * @param mtdAmount New value of property mtdAmount.
     */
    public void setMtdAmount(double mtdAmount) {
        this.mtdAmount = mtdAmount;
    }
    
    /**
     * Getter for property ytdAmount.
     * @return Value of property ytdAmount.
     */
    public double getYtdAmount() {
        return this.ytdAmount;
    }
    
    /**
     * Setter for property ytdAmount.
     * @param ytdAmount New value of property ytdAmount.
     */
    public void setYtdAmount(double ytdAmount) {
        this.ytdAmount = ytdAmount;
    }
    
    /**
     * Getter for property level.
     * @return Value of property level.
     */
    public int getLevel() {
        return this.level;
    }
    
    /**
     * Setter for property level.
     * @param level New value of property level.
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Getter for property sptAmount.
     * @return Value of property sptAmount.
     */
    public double getSptAmount() {
        return this.sptAmount;
    }
    
    /**
     * Setter for property sptAmount.
     * @param sptAmount New value of property sptAmount.
     */
    public void setSptAmount(double sptAmount) {
        this.sptAmount = sptAmount;
    }
    
}
