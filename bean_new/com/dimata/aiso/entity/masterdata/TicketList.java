/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dwi
 */
public class TicketList extends Entity{
    
    private long carrierId = 0;
    private String ticketNumber = "";
    private long ticketDepositId = 0;
    private long invoiceDetailId = 0;
    private double ticketPrice = 0.0;
    
    public long getCarrierId(){
	return this.carrierId;
    }
    
    public void setCarrierId(long carrierId){
	this.carrierId = carrierId;
    }
    
    public String getTicketNumber(){
	return this.ticketNumber;
    }
    
    public void setTicketNumber(String ticketNumber){
	this.ticketNumber = ticketNumber;
    }
    
    public long getTicketDepositId(){
	return this.ticketDepositId;
    }
    
    public void setTicketDepositId(long ticketDepositId){
	this.ticketDepositId = ticketDepositId;    
    }
    
    public long getInvoiceDetailId(){
	return this.invoiceDetailId;
    }
    
    public void setInvoiceDetailId(long invoiceDetailId){
	this.invoiceDetailId = invoiceDetailId;
    }
    
    public double getTicketPrice(){
	return this.ticketPrice;
    }
    
    public void setTicketPrice(double ticketPrice){
	this.ticketPrice = ticketPrice;
    }
}
