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
public class ReservationPackageList {

    private Date checkInDate = new Date();
    private Date checkOutDate = new Date();
    private Date depositTimeLimit = new Date();
    private long invoiceId = 0;
    private String invoiceNumber = "";
    private String guestName = "";
    private double downPayment = 0.0;
    private int docStatus = 0;
    private String description = "";
    
    public Date getCheckInDate(){
	return this.checkInDate;
    }
    
    public void setCheckInDate(Date checkInDate){
	this.checkInDate = checkInDate;
    }
    
    public Date getCheckOutDate(){
	return this.checkOutDate;
    }
    
    public void setCheckOutDate(Date checkOutDate){
	this.checkOutDate = checkOutDate;
    }
    
    public Date getDepositTimeLimit(){
	return this.depositTimeLimit;
    }
    
    public void setDepositTimeLimit(Date depositTimeLimit){
	this.depositTimeLimit = depositTimeLimit;
    }
    
    public long getInvoiceId(){
	return this.invoiceId;
    }
    
    public void setInvoiceId(long invoiceId){
	this.invoiceId = invoiceId;
    }
    
    public String getInvoiceNumber(){
	return this.invoiceNumber;
    }
    
    public void setInvoiceNumber(String invoiceNumber){
	this.invoiceNumber = invoiceNumber;
    }
    
    public String getGuestName(){
	return this.guestName;
    }
    
    public void setGuestName(String guestName){
	this.guestName = guestName;
    }
    
    public double getDownPayment(){
	return this.downPayment;
    }
    
    public void setDownPayment(double downPayment){
	this.downPayment = downPayment;
    }
    
    public int getDocStatus(){
	return this.docStatus;
    }
    
    public void setDocStatus(int docStatus){
	this.docStatus = docStatus;
    }
    
    public String getDescription(){
	return this.description;
    }
    
    public void setDescription(String description){
	this.description = description;
    }
}
