
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

public class ChecklistGroup extends Entity { 

	private String checklistGroup = "";

	public String getChecklistGroup(){ 
		return checklistGroup; 
	} 

	public void setChecklistGroup(String checklistGroup){ 
		if ( checklistGroup == null ) {
			checklistGroup = ""; 
		} 
		this.checklistGroup = checklistGroup; 
	} 

}
