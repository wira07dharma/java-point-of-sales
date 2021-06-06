
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

public class CardQuestionGroup extends Entity { 

	private String groupName = "";

	public String getGroupName(){ 
		return groupName; 
	} 

	public void setGroupName(String groupName){ 
		if ( groupName == null ) {
			groupName = ""; 
		} 
		this.groupName = groupName; 
	} 

}
