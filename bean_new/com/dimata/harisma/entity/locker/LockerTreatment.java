
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
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

public class LockerTreatment extends Entity { 

	private long locationId;
	private Date treatmentDate;
	private String treatment = "";

	public long getLocationId(){ 
		return locationId; 
	} 

	public void setLocationId(long locationId){ 
		this.locationId = locationId; 
	} 

	public Date getTreatmentDate(){ 
		return treatmentDate; 
	} 

	public void setTreatmentDate(Date treatmentDate){ 
		this.treatmentDate = treatmentDate; 
	} 

	public String getTreatment(){ 
		return treatment; 
	} 

	public void setTreatment(String treatment){ 
		if ( treatment == null ) {
			treatment = ""; 
		} 
		this.treatment = treatment; 
	} 

}
