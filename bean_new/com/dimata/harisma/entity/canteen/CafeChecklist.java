
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

package com.dimata.harisma.entity.canteen; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class CafeChecklist extends Entity { 

	private long mealTimeId;
	private Date checkDate;
	private long checkedBy;
	private long approvedBy;

	public long getMealTimeId(){ 
		return mealTimeId; 
	} 

	public void setMealTimeId(long mealTimeId){ 
		this.mealTimeId = mealTimeId; 
	} 

	public Date getCheckDate(){ 
		return checkDate; 
	} 

	public void setCheckDate(Date checkDate){ 
		this.checkDate = checkDate; 
	} 

	public long getCheckedBy(){ 
		return checkedBy; 
	} 

	public void setCheckedBy(long checkedBy){ 
		this.checkedBy = checkedBy; 
	} 

	public long getApprovedBy(){ 
		return approvedBy; 
	} 

	public void setApprovedBy(long approvedBy){ 
		this.approvedBy = approvedBy; 
	} 

}
