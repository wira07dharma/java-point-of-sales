
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: lkarunia
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.masterdata; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class GroupRank extends Entity { 

	private String groupName = "";
	private String description = "";

	public String getGroupName(){ 
		return groupName; 
	} 

	public void setGroupName(String groupName){ 
		if ( groupName == null ) {
			groupName = ""; 
		} 
		this.groupName = groupName; 
	} 

	public String getDescription(){ 
		return description; 
	} 

	public void setDescription(String description){ 
		if ( description == null ) {
			description = ""; 
		} 
		this.description = description; 
	} 

}
