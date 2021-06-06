/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.search;

import java.util.Date;

/**
 *
 * @author dwi
 */
public class SrcSalesTicketList {
    
    private Date transDateFr = new Date();
    private Date transDateTo = new Date();
    private String passName = "";
    private long carrierId = 0;
    
    public Date getTransDateFr(){
	return this.transDateFr;
    }
    
    public void setTransDateFr(Date transDateFr){
	this.transDateFr = transDateFr;
    }
    
    public Date getTransDateTo(){
	return this.transDateTo;
    }
    
    public void setTransDateTo(Date transDateTo){
	this.transDateTo = transDateTo;
    }
    
    public String getPassName(){
	return this.passName;
    }
    
    public void setPassName(String passName){
	this.passName = passName;
    }
    
    public long getCarrierId(){
	return this.carrierId;
    }
    
    public void setCarrierId(long carrierId){
	this.carrierId = carrierId;
    }
}
