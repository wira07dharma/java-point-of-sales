
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

public class DevImprovement extends Entity { 

	private long employeeAppraisal;
	private long groupCategoryId;
	private String improvement = "";

	public long getEmployeeAppraisal(){ 
		return employeeAppraisal; 
	} 

	public void setEmployeeAppraisal(long employeeAppraisal){ 
		this.employeeAppraisal = employeeAppraisal; 
	} 

	public long getGroupCategoryId(){ 
		return groupCategoryId; 
	} 

	public void setGroupCategoryId(long groupCategoryId){ 
		this.groupCategoryId = groupCategoryId; 
	} 

	public String getImprovement(){ 
		return improvement; 
	} 

	public void setImprovement(String improvement){ 
		if ( improvement == null ) {
			improvement = ""; 
		} 
		this.improvement = improvement; 
	} 

}
