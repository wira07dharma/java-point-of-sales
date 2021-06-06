/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author dwi
 */
public class TicketDeposit extends Entity{

    private long carrierId = 0;
    private Date depositDate = new Date();
    private double depositAmount = 0.0;
    private String description = "";
    
    public long getCarrierId(){
	return this.carrierId;
    }
    
    public void setCarrierId(long carrierId){
	this.carrierId = carrierId;
    }
    
    public Date getDepositDate(){
	return this.depositDate;
    }
    
    public void setDepositDate(Date depositDate){
	this.depositDate = depositDate;
    }
    
    public double getDepositAmount(){
	return this.depositAmount;
    }
    
    public void setDepositAmount(double depositAmount){
	this.depositAmount = depositAmount;
    }
    
    public String getDescription(){
	return this.description;
    }
    
    public void setDescription(String description){
	this.description = description;
    }	    
}
