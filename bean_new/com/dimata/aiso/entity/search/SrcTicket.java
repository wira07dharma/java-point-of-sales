/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.search;

/**
 *
 * @author dwi
 */
public class SrcTicket {

    private long carrierId = 0;
    private long routeId = 0;
    private long classId = 0;
    private String ticketNumber = "";
    private int orderBy = 0;
    
    public long getCarrierId(){
	return this.carrierId;
    }
    
    public void setCarrierId(long carrierId){
	this.carrierId = carrierId;
    }
    
    public long getRouteId(){
	return this.routeId;
    }
    
    public void setRouteId(long routeId){
	this.routeId = routeId;
    }
    
    public long getClassId(){
	return this.classId;
    }
    
    public void setClassId(long classId){
	this.classId = classId;
    }
    
    public String getTicketNumber(){
	return this.ticketNumber;
    }
    
    public void setTicketNumber(String ticketNumber){
	this.ticketNumber = ticketNumber;
    }
    
    public int getOrderBy(){
	return this.orderBy;
    }
    
    public void setOrderBy(int orderBy){
	this.orderBy = orderBy;
    }
}
