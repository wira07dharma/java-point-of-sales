
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

package com.dimata.harisma.entity.employee; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class ExpCoverage extends Entity { 

	private long groupRankId;
	private String descriptions = "";
	private String defCoverage = "";

	public long getGroupRankId(){ 
		return groupRankId; 
	} 

	public void setGroupRankId(long groupRankId){ 
		this.groupRankId = groupRankId; 
	} 

	public String getDescriptions(){ 
		return descriptions; 
	} 

	public void setDescriptions(String descriptions){ 
		if ( descriptions == null ) {
			descriptions = ""; 
		} 
		this.descriptions = descriptions; 
	} 

	public String getDefCoverage(){ 
		return defCoverage; 
	} 

	public void setDefCoverage(String defCoverage){ 
		if ( defCoverage == null ) {
			defCoverage = ""; 
		} 
		this.defCoverage = defCoverage; 
	} 

}
