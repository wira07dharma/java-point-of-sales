
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.ij.entity.mapping; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class IjPaymentMapping extends Entity { 

	private long paymentSystem;
	private long currency;
	private long account;

        /** Holds value of property boSystem. */
        private int boSystem;
        
        /** Holds value of property location. */
        private long location;
        
	public long getPaymentSystem(){ 
		return paymentSystem; 
	} 

	public void setPaymentSystem(long paymentSystem){ 
		this.paymentSystem = paymentSystem; 
	} 

	public long getCurrency(){ 
		return currency; 
	} 

	public void setCurrency(long currency){ 
		this.currency = currency; 
	} 

	public long getAccount(){ 
		return account; 
	} 

	public void setAccount(long account){ 
		this.account = account; 
	} 

        /** Getter for property boSystem.
         * @return Value of property boSystem.
         *
         */
        public int getBoSystem() {
            return this.boSystem;
        }
        
        /** Setter for property boSystem.
         * @param boSystem New value of property boSystem.
         *
         */
        public void setBoSystem(int boSystem) {
            this.boSystem = boSystem;
        }
        
        /** Getter for property location.
         * @return Value of property location.
         *
         */
        public long getLocation() {
            return this.location;
        }
        
        /** Setter for property location.
         * @param location New value of property location.
         *
         */
        public void setLocation(long location) {
            this.location = location;
        }
        
}
