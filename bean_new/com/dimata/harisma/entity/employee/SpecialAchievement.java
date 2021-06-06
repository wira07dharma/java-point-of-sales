
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

public class SpecialAchievement extends Entity { 

	private long employeeId;
	private String typeOfAward = "";
	private String presentedBy = "";
	private Date date;

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public String getTypeOfAward(){ 
		return typeOfAward; 
	} 

	public void setTypeOfAward(String typeOfAward){ 
		if ( typeOfAward == null ) {
			typeOfAward = ""; 
		} 
		this.typeOfAward = typeOfAward; 
	} 

	public String getPresentedBy(){ 
		return presentedBy; 
	} 

	public void setPresentedBy(String presentedBy){ 
		if ( presentedBy == null ) {
			presentedBy = ""; 
		} 
		this.presentedBy = presentedBy; 
	} 

	public Date getDate(){ 
		return date; 
	} 

	public void setDate(Date date){ 
		this.date = date; 
	} 

}
