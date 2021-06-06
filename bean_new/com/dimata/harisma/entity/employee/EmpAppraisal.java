
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

public class EmpAppraisal extends Entity { 

	private long employeeId;
	private long appraisorId;
	private Date dateOfAppraisal;
	private Date lastAppraisal;
	private int totalScore;
	private int totalCriteria;
	private double scoreAverage;
	private Date datePerformance;
	private Date timePerformance;

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public long getAppraisorId(){ 
		return appraisorId; 
	} 

	public void setAppraisorId(long appraisorId){ 
		this.appraisorId = appraisorId; 
	} 

	public Date getDateOfAppraisal(){ 
		return dateOfAppraisal; 
	} 

	public void setDateOfAppraisal(Date dateOfAppraisal){ 
		this.dateOfAppraisal = dateOfAppraisal; 
	} 

	public Date getLastAppraisal(){ 
		return lastAppraisal; 
	} 

	public void setLastAppraisal(Date lastAppraisal){ 
		this.lastAppraisal = lastAppraisal; 
	} 

	public int getTotalScore(){ 
		return totalScore; 
	} 

	public void setTotalScore(int totalScore){ 
		this.totalScore = totalScore; 
	} 

	public int getTotalCriteria(){ 
		return totalCriteria; 
	} 

	public void setTotalCriteria(int totalCriteria){ 
		this.totalCriteria = totalCriteria; 
	} 

	public double getScoreAverage(){ 
		return scoreAverage; 
	} 

	public void setScoreAverage(double scoreAverage){ 
		this.scoreAverage = scoreAverage; 
	} 

	public Date getDatePerformance(){ 
		return datePerformance; 
	} 

	public void setDatePerformance(Date datePerformance){ 
		this.datePerformance = datePerformance; 
	} 

	public Date getTimePerformance(){ 
		return timePerformance; 
	} 

	public void setTimePerformance(Date timePerformance){ 
		this.timePerformance = timePerformance; 
	} 

}
