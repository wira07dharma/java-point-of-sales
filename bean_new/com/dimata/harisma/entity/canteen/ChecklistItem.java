
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

public class ChecklistItem extends Entity { 

	private long checklistGroupId;
	private String checklistItem = "";

	public long getChecklistGroupId(){ 
		return checklistGroupId; 
	} 

	public void setChecklistGroupId(long checklistGroupId){ 
		this.checklistGroupId = checklistGroupId; 
	} 

	public String getChecklistItem(){ 
		return checklistItem; 
	} 

	public void setChecklistItem(String checklistItem){ 
		if ( checklistItem == null ) {
			checklistItem = ""; 
		} 
		this.checklistItem = checklistItem; 
	} 

}
