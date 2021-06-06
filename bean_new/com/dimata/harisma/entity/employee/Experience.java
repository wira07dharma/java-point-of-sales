
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

package com.dimata.harisma.entity.employee; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Experience extends Entity { 

	private long employeeId;
	private String companyName = "";
	private int startDate;
	private int endDate;
	private String position = "";
	private String moveReason = "";

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public String getCompanyName(){ 
		return companyName; 
	} 

	public void setCompanyName(String companyName){ 
		if ( companyName == null ) {
			companyName = ""; 
		} 
		this.companyName = companyName; 
	} 

	public int getStartDate(){ 
		return startDate; 
	} 

	public void setStartDate(int startDate){
		this.startDate = startDate; 
	} 

	public int getEndDate(){ 
		return endDate; 
	} 

	public void setEndDate(int endDate){
		this.endDate = endDate; 
	} 

	public String getPosition(){ 
		return position; 
	} 

	public void setPosition(String position){ 
		if ( position == null ) {
			position = ""; 
		} 
		this.position = position; 
	} 

	public String getMoveReason(){ 
		return moveReason; 
	} 

	public void setMoveReason(String moveReason){ 
		if ( moveReason == null ) {
			moveReason = ""; 
		} 
		this.moveReason = moveReason; 
	} 

}
