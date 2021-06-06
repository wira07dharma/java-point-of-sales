
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

public class TrainingHistory extends Entity { 

	private long trainingHistoryId;
	private long employeeId;
	private long trainingId;
	private Date startDate;
	private Date endDate;
	private String trainer = "";
	private String remark = "";
    private int duration;
    private int presence;
    private Date startTime;

	public long getTrainingHistoryId(){ 
		return trainingHistoryId; 
	} 

	public void setTrainingHistoryId(long trainingHistoryId){ 
		this.trainingHistoryId = trainingHistoryId; 
	} 

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public long getTrainingId(){ return trainingId; } 

	public void setTrainingId(long trainingId){ this.trainingId = trainingId; } 

	public Date getStartDate(){ 
		return startDate; 
	} 

	public void setStartDate(Date startDate){ 
		this.startDate = startDate; 
	} 

	public Date getEndDate(){ 
		return endDate; 
	} 

	public void setEndDate(Date endDate){ 
		this.endDate = endDate; 
	} 

	public String getTrainer(){ 
		return trainer; 
	} 

	public void setTrainer(String trainer){ 
		if ( trainer == null ) {
			trainer = ""; 
		} 
		this.trainer = trainer; 
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

    public int getDuration(){ return duration; }

    public void setDuration(int duration){ this.duration = duration; }

    public int getPresence(){ return presence; }

    public void setPresence(int presence){ this.presence = presence; }

    public Date getStartTime(){ return startTime; }

    public void setStartTime(Date startTime){ this.startTime = startTime; }
}
