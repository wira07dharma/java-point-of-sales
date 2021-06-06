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
public class SrcReservationPackageList {

    private Date cInDateFr = new Date();
    private Date cInDateTo = new Date();
    private Date cOutDateFr = new Date();
    private Date cOutDateTo = new Date();
    private String guestName = "";
    private int orderBy = 0;
    
    public Date getCInDateFr(){
	return this.cInDateFr;
    }
    
    public void setCInDateFr(Date cInDateFr){
	this.cInDateFr = cInDateFr;    
    }
    
    public Date getCInDateTo(){
	return this.cInDateTo;
    }
    
    public void setCInDateTo(Date cInDateTo){
	this.cInDateTo = cInDateTo;
    }
    
    public Date getCOutDateFr(){
	return this.cOutDateFr;
    }
    
    public void setCOutDateFr(Date cOutDateFr){
	this.cOutDateFr = cOutDateFr;    
    }
    
    public Date getCOutDateTo(){
	return this.cOutDateTo;
    }
    
    public void setCOutDateTo(Date cOutDateTo){
	this.cOutDateTo = cOutDateTo;
    }
    
    public String getGuestName(){
	return this.guestName;
    }
    
    public void setGuestName(String guestName){
	this.guestName = guestName;
    }
    
    public int getOrderBy(){
	return this.orderBy;
    }
    
    public void setOrderBy(int orderBy){
	this.orderBy = orderBy;
    }
}
