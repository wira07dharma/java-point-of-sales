/*
 * ServiceConfiguration.java
 *
 * Created on September 27, 2004, 9:15 PM
 */

package com.dimata.common.entity.service;

/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

/**
 *
 * @author  gedhy
 */
public class ServiceConfiguration extends Entity 
{ 
    
	private Date startTime;
	private int periode;
        private int serviceType;
        
        /** Getter for property startTime.
         * @return Value of property startTime.
         *
         */        
	public Date getStartTime(){ 
		return startTime; 
	} 

        /** Setter for property startTime.
         * @param serviceType New value of property startTime.
         *
         */                
	public void setStartTime(Date startTime){ 
		this.startTime = startTime; 
	} 

        /** Getter for property periode.
         * @return Value of property periode.
         *
         */        
	public int getPeriode(){ 
		return periode; 
	} 

        /** Setter for property periode.
         * @param serviceType New value of property periode.
         *
         */        
	public void setPeriode(int periode){ 
		this.periode = periode; 
	} 

        /** Getter for property serviceType.
         * @return Value of property serviceType.
         *
         */
        public int getServiceType() {
            return this.serviceType;
        }
        
        /** Setter for property serviceType.
         * @param serviceType New value of property serviceType.
         *
         */
        public void setServiceType(int serviceType) {
            this.serviceType = serviceType;
        }
        
}
