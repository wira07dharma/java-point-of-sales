
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

public class IjLocationMapping extends Entity { 

	private int transactionType;
	private int salesType=-1;
	private long currency;
	private long location;
	private long prodDepartment;
	private long account;

        /** Holds value of property boSystem. */
        private int boSystem;
        
        /** Holds value of property priceType. */
        private long priceType;
        
	public int getTransactionType(){ 
		return transactionType; 
	} 

	public void setTransactionType(int transactionType){ 
		this.transactionType = transactionType; 
	} 

	public int getSalesType(){ 
		return salesType; 
	} 

	public void setSalesType(int salesType){ 
		this.salesType = salesType; 
	} 

	public long getCurrency(){ 
		return currency; 
	} 

	public void setCurrency(long currency){ 
		this.currency = currency; 
	} 

	public long getLocation(){ 
		return location; 
	} 

	public void setLocation(long location){ 
		this.location = location; 
	} 

	public long getProdDepartment(){ 
		return prodDepartment; 
	} 

	public void setProdDepartment(long prodDepartment){ 
		this.prodDepartment = prodDepartment; 
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
        
        /** Getter for property priceType.
         * @return Value of property priceType.
         *
         */
        public long getPriceType() {
            return this.priceType;
        }
        
        /** Setter for property priceType.
         * @param priceType New value of property priceType.
         *
         */
        public void setPriceType(long priceType) {
            this.priceType = priceType;
        }
        
}
