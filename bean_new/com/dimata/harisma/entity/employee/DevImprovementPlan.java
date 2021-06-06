
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

public class DevImprovementPlan extends Entity { 

	private long categoryAppraisalId;
	private long employeeAppraisal;
	private String improvPlan = "";
    private boolean recommend;

	public long getCategoryAppraisalId(){ 
		return categoryAppraisalId; 
	} 

	public void setCategoryAppraisalId(long categoryAppraisalId){ 
		this.categoryAppraisalId = categoryAppraisalId; 
	} 

	public long getEmployeeAppraisal(){ 
		return employeeAppraisal; 
	} 

	public void setEmployeeAppraisal(long employeeAppraisal){ 
		this.employeeAppraisal = employeeAppraisal; 
	} 

	public String getImprovPlan(){ 
		return improvPlan; 
	} 

	public void setImprovPlan(String improvPlan){ 
		if ( improvPlan == null ) {
			improvPlan = ""; 
		} 
		this.improvPlan = improvPlan; 
	}

 	public boolean getRecommend(){
		return recommend;
	} 

	public void setRecommend(boolean recommend){
		this.recommend = recommend;
	}

}
