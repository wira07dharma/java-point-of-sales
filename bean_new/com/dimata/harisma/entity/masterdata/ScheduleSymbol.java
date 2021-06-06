
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

package com.dimata.harisma.entity.masterdata; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class ScheduleSymbol extends Entity { 

	private long scheduleCategoryId;
	private String schedule = "";
	private String symbol = "";
	private Date timeIn;
	private Date timeOut;

	public long getScheduleCategoryId(){ 
		return scheduleCategoryId; 
	} 

	public void setScheduleCategoryId(long scheduleCategoryId){ 
		this.scheduleCategoryId = scheduleCategoryId; 
	} 

	public String getSchedule(){ 
		return schedule; 
	} 

	public void setSchedule(String schedule){ 
		if ( schedule == null ) {
			schedule = ""; 
		} 
		this.schedule = schedule; 
	} 

	public String getSymbol(){ 
		return symbol; 
	} 

	public void setSymbol(String symbol){ 
		if ( symbol == null ) {
			symbol = ""; 
		} 
		this.symbol = symbol; 
	} 

	public Date getTimeIn(){ 
		return timeIn; 
	} 

	public void setTimeIn(Date timeIn){ 
		this.timeIn = timeIn; 
	} 

	public Date getTimeOut(){ 
		return timeOut; 
	} 

	public void setTimeOut(Date timeOut){ 
		this.timeOut = timeOut; 
	} 

}
