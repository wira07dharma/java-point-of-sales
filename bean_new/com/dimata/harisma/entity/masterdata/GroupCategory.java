
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
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

public class GroupCategory extends Entity { 

	private long groupRankId;
	private String groupName = "";

	public long getGroupRankId(){ 
		return groupRankId; 
	} 

	public void setGroupRankId(long groupRankId){ 
		this.groupRankId = groupRankId; 
	} 

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
