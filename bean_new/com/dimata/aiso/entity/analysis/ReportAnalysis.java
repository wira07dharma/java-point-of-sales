/*
 * AccountValue.java
 * @author  rusdianta
 * Created on April 5, 2005, 2:43 PM
 */

package com.dimata.aiso.entity.analysis;

public class ReportAnalysis {
    
    private String accountName;
    private double amount;
    private int accountSign;
    
    /** Creates a new instance of AccountValue */
    public ReportAnalysis() {
        accountName = "";
        amount = 0;
        accountSign = 0;
    }
    
    public String getAccountName() {
        return accountName;
    }
    
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public int getAccountSign() {
        return accountSign;
    }
    
    public void setAccountSign(int accountSign) {
        this.accountSign = accountSign;
    }
}
