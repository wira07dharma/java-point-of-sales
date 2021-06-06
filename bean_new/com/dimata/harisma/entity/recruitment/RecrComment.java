
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

public class RecrComment extends Entity { 

	private long recrInterviewerId;
	private long recrApplicationId;
	private String comment = "";

	public long getRecrInterviewerId(){ 
		return recrInterviewerId; 
	} 

	public void setRecrInterviewerId(long recrInterviewerId){ 
		this.recrInterviewerId = recrInterviewerId; 
	} 

	public long getRecrApplicationId(){ 
		return recrApplicationId; 
	} 

	public void setRecrApplicationId(long recrApplicationId){ 
		this.recrApplicationId = recrApplicationId; 
	} 

	public String getComment(){ 
		return comment; 
	} 

	public void setComment(String comment){ 
		if ( comment == null ) {
			comment = ""; 
		} 
		this.comment = comment; 
	} 

}
