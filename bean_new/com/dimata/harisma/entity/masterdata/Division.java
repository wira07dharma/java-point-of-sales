
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

package com.dimata.harisma.entity.masterdata;
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Division extends Entity { 

	private String division = "";
	private String description = "";
    private long locationId = 0;

 
  /**
   * @return the locationId
   */
  public long getLocationId() {
    return locationId;
  }

  /**
   * @param locationId the locationId to set
   */
  public void setLocationId(long locationId) {
    this.locationId = locationId;
  }

 
	public String getDivision(){
		return division;
	} 

	public void setDivision(String division){
		if ( division == null ) {
			division = "";
		} 
		this.division = division;
	} 

	public String getDescription(){ 
		return description; 
	} 

	public void setDescription(String description){ 
		if ( description == null ) {
			description = ""; 
		} 
		this.description = description; 
	} 

}
