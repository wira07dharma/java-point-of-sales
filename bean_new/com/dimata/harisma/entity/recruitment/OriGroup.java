
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

public class OriGroup extends Entity { 

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
