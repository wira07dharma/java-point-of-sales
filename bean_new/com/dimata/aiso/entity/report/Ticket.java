/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.report;

/**
 *
 * @author dwi
 */
public class Ticket {

    private String carrierCode = "";
    private String route = "";
    private String ticketNumber = "";
    private double ticketPrice = 0.0;
    private long ticketId = 0;
    
    public String getCarrierCode(){
	return this.carrierCode;
    }
    
    public void setCarrierCode(String carrierCode){
	this.carrierCode = carrierCode;
    }
    
    public String getRoute(){
	return this.route;
    }
    
    public void setRoute(String route){
	this.route = route;
    }
    
    public String getTicketNumber(){
	return this.ticketNumber;
    }
    
    public void setTicketNumber(String ticketNumber){
	this.ticketNumber = ticketNumber;
    }
    
    public double getTicketPrice(){
	return this.ticketPrice;
    }
    
    public void setTicketPrice(double ticketPrice){
	this.ticketPrice = ticketPrice;
    }
    
    public long getTicketId(){
	return this.ticketId;
    }
    
    public void setTicketId(long ticketId){
	this.ticketId = ticketId;
    }
}
