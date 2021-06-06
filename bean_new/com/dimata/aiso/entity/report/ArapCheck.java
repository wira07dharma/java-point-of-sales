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
public class ArapCheck {
    
    private String notaNo = "";
    private String voucherNo = "";
    private Date notaDate = new Date();
    private Date voucherDate = new Date();
    private String contactName = "";
    private double amount = 0.0;
    private long accountId = 0;
    private long contactId = 0;
    private long idOppAccount = 0;
    
    public String getNotaNo(){
	return this.notaNo;
    }
    
    public void setNotaNo(String notaNo){
	this.notaNo = notaNo;
    }
    
    public String getVoucherNo(){
	return this.voucherNo;
    }
    
    public void setVoucherNo(String voucherNo){
	this.voucherNo = voucherNo;
    }
    
    public Date getNotaDate(){
	return this.notaDate;
    }
    
    public void setNotaDate(Date notaDate){
	this.notaDate = notaDate;    
    }
    
    public Date getVoucherDate(){
	return this.voucherDate;
    }
    
    public void setVoucherDate(Date voucherDate){
	this.voucherDate = voucherDate;
    }
    
    public String getContactName(){
	return this.contactName;
    }
    
    public void setContactName(String contactName){
	this.contactName = contactName;
    }
    
    public double getAmount(){
	return this.amount;
    }
    
    public void setAmount(double amount){
	this.amount = amount;
    }
    
    public long getAccountId(){
	return this.accountId;
    }
    
    public void setAccountId(long accountId){
	this.accountId = accountId;
    }
    
    public long getContactId(){
	return this.contactId;
    }
    
    public void setContactId(long contactId){
	this.contactId = contactId;
    }
    
    public long getIdOppAccount(){
	return this.idOppAccount;
    }
    
    public void setIdOppAccount(long idOppAccount){
	this.idOppAccount = idOppAccount;
    }
}
