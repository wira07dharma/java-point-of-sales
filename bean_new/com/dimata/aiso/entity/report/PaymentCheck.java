/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.report;

import java.util.Date;

/**
 *
 * @author dwi
 */
public class PaymentCheck {

    private String notaNo = "";
    private String paymentNo = "";
    private Date notaDate = new Date();
    private Date paymentDate = new Date();
    private double amount = 0.0;
    private long lAccountId = 0;
    private long lContactId = 0;
    
    public String getNotaNo(){
	return this.notaNo;
    }
    
    public void setNotaNo(String notaNo){
	this.notaNo = notaNo;
    }
    
    public String getPaymentNo(){
	return this.paymentNo;
    }
    
    public void setPaymentNo(String paymentNo){
	this.paymentNo = paymentNo;
    }
    
    public Date getNotaDate(){
	return this.notaDate;
    }
    
    public void setNotaDate(Date notaDate){
	this.notaDate = notaDate;
    }
    
    public Date getPaymentDate(){
	return this.paymentDate;
    }
    
    public void setPaymentDate(Date paymentDate){
	this.paymentDate = paymentDate;
    }
    
    public double getAmount(){
	return this.amount;
    }
    
    public void setAmount(double amount){
	this.amount = amount;
    }
    
    public long getAccountId(){
	return this.lAccountId;
    }
    
    public void setAccountId(long lAccountId){
	this.lAccountId = lAccountId;
    }
    
    public long getContactId(){
	return this.lContactId;
    }
    
    public void setContactId(long lContactId){
	this.lContactId = lContactId;
    }
}
