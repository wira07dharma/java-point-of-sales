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
public class GLCheck {
    
    private Date transDate = new Date();
    private String dokRef = "";
    private String journalNo = "";
    private String contactName = "";
    private double amount = 0.0;
    private String description = "";
    private long contactId = 0;
    
    public Date getTransDate(){
	return this.transDate;
    }
    
    public void setTransDate(Date transDate){
	this.transDate = transDate;
    }
    
    public String getDokRef(){
	return this.dokRef;
    }
    
    public void setDokRef(String dokRef){
	this.dokRef = dokRef;
    }
    
    public String getJournalNo(){
	return this.journalNo;
    }
    
    public void setJournalNo(String journalNo){
	this.journalNo = journalNo;
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
    
    public String getDescription(){
	return this.description;
    }
    
    public void setDescription(String description){
	this.description = description;
    }
    
    public long getContactId(){
	return this.contactId;
    }
    
    public void setContactId(long contactId){
	this.contactId = contactId;
    }
}
