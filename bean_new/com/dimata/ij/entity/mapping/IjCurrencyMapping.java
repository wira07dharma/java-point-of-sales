
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

public class IjCurrencyMapping extends Entity { 

	private long boCurrency;
	private long aisoCurrency;

        /** Holds value of property boSystem. */
        private int boSystem;
        
	public long getBoCurrency(){ 
		return boCurrency; 
	} 

	public void setBoCurrency(long boCurrency){ 
		this.boCurrency = boCurrency; 
	} 

	public long getAisoCurrency(){ 
		return aisoCurrency; 
	} 

	public void setAisoCurrency(long aisoCurrency){ 
		this.aisoCurrency = aisoCurrency; 
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
        
}
