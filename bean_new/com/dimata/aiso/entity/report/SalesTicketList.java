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
public class SalesTicketList {
    
    private Date transDate = new Date();
    private String description = "";
    private String ticketInfo = "";
    private String passName = "";
    private double debet = 0.0;
    private double credit = 0.0;
    
    public Date getTransDate(){
	return this.transDate;
    }
    
    public void setTransDate(Date transDate){
	this.transDate = transDate;
    }
    
    public String getDescription(){
	return this.description;
    }
    
    public void setDescription(String description){
	this.description = description;
    }
    
    public String getTicketInfo(){
	return this.ticketInfo;
    }
    
    public void setTicketInfo(String ticketInfo){
	this.ticketInfo = ticketInfo;
    }
    
    public String getPassName(){
	return this.passName;
    }
    
    public void setPassName(String passName){
	this.passName = passName;
    }
    
    public double getDebet(){
	return this.debet;
    }
    
    public void setDebet(double debet){
	this.debet = debet;
    }
    
    public double getCredit(){
	return this.credit;
    }
    
    public void setCredit(double credit){
	this.credit = credit;
    }
}
