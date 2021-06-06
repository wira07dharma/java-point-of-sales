
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

public class Presence extends Entity { 

	private long employeeId;
	private Date presenceDatetime;
	private int status;
        private int analyzed;

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public Date getPresenceDatetime(){ 
		return presenceDatetime; 
	} 

	public void setPresenceDatetime(Date presenceDatetime){ 
		this.presenceDatetime = presenceDatetime; 
	} 

	public int getStatus(){ 
		return status; 
	} 

	public void setStatus(int status){ 
		this.status = status; 
	} 

	public int getAnalyzed(){ 
		return analyzed; 
	} 

	public void setAnalyzed(int analyzed){ 
		this.analyzed = analyzed; 
	} 
}
