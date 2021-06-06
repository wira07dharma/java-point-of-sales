
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

public class TrainingActivityActual extends Entity { 

	private long trainingActivityPlanId;
	private Date date;
	private Date startTime;
	private Date endTime;
	private int atendees;
	private String venue = "";
	private String remark = "";

	public long getTrainingActivityPlanId(){ 
		return trainingActivityPlanId; 
	} 

	public void setTrainingActivityPlanId(long trainingActivityPlanId){ 
		this.trainingActivityPlanId = trainingActivityPlanId; 
	} 

	public Date getDate(){ 
		return date; 
	} 

	public void setDate(Date date){ 
		this.date = date; 
	} 

	public Date getStartTime(){ 
		return startTime; 
	} 

	public void setStartTime(Date startTime){ 
		this.startTime = startTime; 
	} 

	public Date getEndTime(){ 
		return endTime; 
	} 

	public void setEndTime(Date endTime){ 
		this.endTime = endTime; 
	} 

	public int getAtendees(){ 
		return atendees; 
	} 

	public void setAtendees(int atendees){ 
		this.atendees = atendees; 
	} 

	public String getVenue(){ 
		return venue; 
	} 

	public void setVenue(String venue){ 
		if ( venue == null ) {
			venue = ""; 
		} 
		this.venue = venue; 
	} 

	public String getRemark(){ 
		return remark; 
	} 

	public void setRemark(String remark){ 
		if ( remark == null ) {
			remark = ""; 
		} 
		this.remark = remark; 
	} 

}
