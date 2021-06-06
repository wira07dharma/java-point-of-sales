
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
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

public class Level extends Entity { 

    private long groupRankId ;
	private String level = "";
	private String description = "";

    public long getGroupRankId(){
		return groupRankId;
	} 

	public void setGroupRankId(long groupRankId){
    	this.groupRankId = groupRankId;
    }

	public String getLevel(){ 
		return level; 
	} 

	public void setLevel(String level){ 
		if ( level == null ) {
			level = ""; 
		} 
		this.level = level; 
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
