
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

package com.dimata.harisma.entity.attendance; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class DayOfPayment extends Entity { 

	private long employeeId;
	private int duration;
	private Date dpFrom;
	private Date dpTo;
	private Date aprDeptheadDate;
	private String contactAddress = "";
	private String remarks = "";

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public int getDuration(){ 
		return duration; 
	} 

	public void setDuration(int duration){ 
		this.duration = duration; 
	} 

	public Date getDpFrom(){ 
		return dpFrom; 
	} 

	public void setDpFrom(Date dpFrom){ 
		this.dpFrom = dpFrom; 
	} 

	public Date getDpTo(){ 
		return dpTo; 
	} 

	public void setDpTo(Date dpTo){ 
		this.dpTo = dpTo; 
	} 

	public Date getAprDeptheadDate(){ 
		return aprDeptheadDate; 
	} 

	public void setAprDeptheadDate(Date aprDeptheadDate){ 
		this.aprDeptheadDate = aprDeptheadDate; 
	} 

	public String getContactAddress(){ 
		return contactAddress; 
	} 

	public void setContactAddress(String contactAddress){ 
		if ( contactAddress == null ) {
			contactAddress = ""; 
		} 
		this.contactAddress = contactAddress; 
	} 

	public String getRemarks(){ 
		return remarks; 
	} 

	public void setRemarks(String remarks){ 
		if ( remarks == null ) {
			remarks = ""; 
		} 
		this.remarks = remarks; 
	} 

}
