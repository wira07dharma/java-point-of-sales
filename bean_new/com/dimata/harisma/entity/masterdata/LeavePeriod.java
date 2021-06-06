
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

package com.dimata.harisma.entity.masterdata; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class LeavePeriod extends Entity { 

	private Date startDate;
	private Date endDate;
	private boolean status = false;

	public Date getStartDate(){ 
		return startDate; 
	} 

	public void setStartDate(Date startDate){ 
		this.startDate = startDate; 
	} 

	public Date getEndDate(){ 
		return endDate; 
	} 

	public void setEndDate(Date endDate){ 
		this.endDate = endDate; 
	} 

	public boolean getStatus(){ 
		return status; 
	} 

	public void setStatus(boolean status){ 
		this.status = status; 
	} 

}
