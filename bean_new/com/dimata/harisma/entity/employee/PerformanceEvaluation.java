
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

public class PerformanceEvaluation extends Entity { 

	private long employeeAppraisal;
	private long evaluationId;
	private long categoryCriteriaId;
	private String justification = "";

	public long getEmployeeAppraisal(){ 
		return employeeAppraisal; 
	} 

	public void setEmployeeAppraisal(long employeeAppraisal){ 
		this.employeeAppraisal = employeeAppraisal; 
	} 

	public long getEvaluationId(){ 
		return evaluationId; 
	} 

	public void setEvaluationId(long evaluationId){ 
		this.evaluationId = evaluationId; 
	} 

	public long getCategoryCriteriaId(){ 
		return categoryCriteriaId; 
	} 

	public void setCategoryCriteriaId(long categoryCriteriaId){ 
		this.categoryCriteriaId = categoryCriteriaId; 
	} 

	public String getJustification(){ 
		return justification; 
	} 

	public void setJustification(String justification){ 
		if ( justification == null ) {
			justification = ""; 
		} 
		this.justification = justification; 
	} 

}
