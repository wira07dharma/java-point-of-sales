
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

public class ChecklistMark extends Entity { 

	private String checklistMark = "";

	public String getChecklistMark(){ 
		return checklistMark; 
	} 

	public void setChecklistMark(String checklistMark){ 
		if ( checklistMark == null ) {
			checklistMark = ""; 
		} 
		this.checklistMark = checklistMark; 
	} 

}
