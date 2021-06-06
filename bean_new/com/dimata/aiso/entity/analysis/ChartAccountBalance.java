/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.analysis;

/**
 *
 * @author dwi
 */
public class ChartAccountBalance {
    
    private String sAccountName = "";
    private String sAccountCode = "";
    private double dBalance = 0.0;
    private int iDataType = 0;
    
    public ChartAccountBalance(){}
    
    public ChartAccountBalance(String sAccountName, String sAccountCode, double dBalance, int iDataType){
	this.sAccountName = sAccountName;
	this.sAccountCode = sAccountCode;
	this.dBalance = dBalance;
	this.iDataType = iDataType;
    }
    
    public String getAccountName(){
	return this.sAccountName;
    }
    
    public void setAccountName(String sAccountName){
	this.sAccountName = sAccountName;
    }
    
    public String getAccountCode(){
	return this.sAccountCode;
    }
    
    public void setAccountCode(String sAccountCode){
	this.sAccountCode = sAccountCode;
    }
    
    public double getBalance(){
	return this.dBalance;
    }
    
    public void setBalance(double dBalance){
	this.dBalance = dBalance;
    }
    
    public int getDataType(){
	return this.iDataType;
    }
    
    public void setDataType(int iDataType){
	this.iDataType = iDataType;
    }
}
