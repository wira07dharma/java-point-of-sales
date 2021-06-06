/*
 * Budgeting.java
 * @author  rusdianta
 * Created on March 16, 2005, 10:49 AM
 */

package com.dimata.aiso.entity.masterdata;

public class Budgeting {
    
    private String periodName;
    private double budget;    
    
    /** Creates a new instance of Budgeting */
    public Budgeting() {
    }
    
    public String getPeriodName() {
        return periodName;
    }
    
    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }
    
    public double getBudget() {
        return budget;
    }
    
    public void setBudget(double budget) {
        this.budget = budget;
    }    
    
}
