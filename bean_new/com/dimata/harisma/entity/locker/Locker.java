
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.locker; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Locker extends Entity { 

	private long locationId;
	private String lockerNumber = "";
	private String keyNumber = "";
	private String spareKey;
        private long conditionId;

	public long getLocationId(){ 
		return locationId; 
	} 

	public void setLocationId(long locationId){ 
		this.locationId = locationId; 
	} 

	public String getLockerNumber(){ 
		return lockerNumber; 
	} 

	public void setLockerNumber(String lockerNumber){ 
		if ( lockerNumber == null ) {
			lockerNumber = ""; 
		} 
		this.lockerNumber = lockerNumber; 
	} 

	public String getKeyNumber(){ 
		return keyNumber; 
	} 

	public void setKeyNumber(String keyNumber){ 
		if ( keyNumber == null ) {
			keyNumber = ""; 
		} 
		this.keyNumber = keyNumber; 
	} 

	public String getSpareKey(){ 
		return spareKey; 
	} 

	public void setSpareKey(String spareKey){ 
		this.spareKey = spareKey; 
	} 

	public long getConditionId(){ 
		return conditionId; 
	} 

	public void setConditionId(long conditionId){ 
		this.conditionId = conditionId; 
	} 
}
