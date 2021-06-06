
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

package com.dimata.posbo.entity.masterdata; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class PersonalDiscount extends Entity { 

	private long materialId;
	private double persDiscVal;
	private double persDiscPct;

        /** Holds value of property contactId. */
        private long contactId;
        
	public long getMaterialId(){
            return materialId;
        } 

	public void setMaterialId(long materialId){
            this.materialId = materialId;
        } 

	public double getPersDiscVal(){
            return persDiscVal;
        } 

	public void setPersDiscVal(double persDiscVal){
            this.persDiscVal = persDiscVal;
        } 

	public double getPersDiscPct(){
            return persDiscPct;
        } 

	public void setPersDiscPct(double persDiscPct){
            this.persDiscPct = persDiscPct;
        } 

        /** Getter for property contactId.
         * @return Value of property contactId.
         *
         */
        public long getContactId() { 
            return this.contactId;
        }
        
        /** Setter for property contactId.
         * @param contactId New value of property contactId.
         *
         */
        public void setContactId(long contactId) {
            this.contactId = contactId;
        }
        
}
