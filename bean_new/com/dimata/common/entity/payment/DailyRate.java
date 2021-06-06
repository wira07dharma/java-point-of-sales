
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

package com.dimata.common.entity.payment; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class DailyRate extends Entity { 

	private long currencyTypeId;
	private double sellingRate;
	private Date rosterDate;

	public long getCurrencyTypeId(){ 
		return currencyTypeId; 
	} 

	public void setCurrencyTypeId(long currencyTypeId){ 
		this.currencyTypeId = currencyTypeId; 
	} 

	public double getSellingRate(){ 
		return sellingRate; 
	} 

	public void setSellingRate(double sellingRate){ 
		this.sellingRate = sellingRate; 
	} 

	public Date getRosterDate(){ 
		return rosterDate; 
	} 

	public void setRosterDate(Date rosterDate){ 
		this.rosterDate = rosterDate; 
	}

}
