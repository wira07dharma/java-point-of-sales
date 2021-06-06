
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

public class CafeEvaluation extends Entity { 

	private long checklistMarkId;
	private long cafeChecklistId;
	private long checklistItemId;
	private String remark = "";

	public long getChecklistMarkId(){ 
		return checklistMarkId; 
	} 

	public void setChecklistMarkId(long checklistMarkId){ 
		this.checklistMarkId = checklistMarkId; 
	} 

	public long getCafeChecklistId(){ 
		return cafeChecklistId; 
	} 

	public void setCafeChecklistId(long cafeChecklistId){ 
		this.cafeChecklistId = cafeChecklistId; 
	} 

	public long getChecklistItemId(){ 
		return checklistItemId; 
	} 

	public void setChecklistItemId(long checklistItemId){ 
		this.checklistItemId = checklistItemId; 
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
