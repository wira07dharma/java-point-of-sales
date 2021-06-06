
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

package com.dimata.harisma.entity.employee; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Recognition extends Entity { 

	private long employeeId;
	private Date recogDate;
	private int point;

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public Date getRecogDate(){ 
		return recogDate; 
	} 

	public void setRecogDate(Date recogDate){ 
		this.recogDate = recogDate; 
	} 

	public int getPoint(){ 
		return point; 
	} 

	public void setPoint(int point){ 
		this.point = point; 
	} 

}
