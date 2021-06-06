
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

package com.dimata.harisma.entity.recruitment; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class OriChecklistActivity extends Entity { 

	private long oriChecklistId;
	private long oriActivityId;
	private int done;
	private Date activityDate;

	public long getOriChecklistId(){ 
		return oriChecklistId; 
	} 

	public void setOriChecklistId(long oriChecklistId){ 
		this.oriChecklistId = oriChecklistId; 
	} 

	public long getOriActivityId(){ 
		return oriActivityId; 
	} 

	public void setOriActivityId(long oriActivityId){ 
		this.oriActivityId = oriActivityId; 
	} 

	public int getDone(){ 
		return done; 
	} 

	public void setDone(int done){ 
		this.done = done; 
	} 

	public Date getActivityDate(){ 
		return activityDate; 
	} 

	public void setActivityDate(Date activityDate){ 
		this.activityDate = activityDate; 
	} 

}
