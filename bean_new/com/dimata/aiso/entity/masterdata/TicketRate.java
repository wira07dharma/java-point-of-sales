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
public class TicketRate extends Entity{

    private long carrierId = 0;
    private long routeId = 0;
    private long classId = 0;
    private double rate = 0.0;
    private double netRateToAirLine = 0.0;
    
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
    
    public double getRate(){
	return this.rate;
    }
    
    public void setRate(double rate){
	this.rate = rate;
    }
    
    public double getNetRateToAirLine(){
	return this.netRateToAirLine;
    }
    
    public void setNetRateToAirLine(double netRateToAirLine){
	this.netRateToAirLine = netRateToAirLine;
    }
}
