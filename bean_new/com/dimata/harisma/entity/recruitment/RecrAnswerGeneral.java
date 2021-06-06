
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

public class RecrAnswerGeneral extends Entity { 

	private long recrGeneralId;
	private long recrApplicationId;
	private String answer = "";

	public long getRecrGeneralId(){ 
		return recrGeneralId; 
	} 

	public void setRecrGeneralId(long recrGeneralId){ 
		this.recrGeneralId = recrGeneralId; 
	} 

	public long getRecrApplicationId(){ 
		return recrApplicationId; 
	} 

	public void setRecrApplicationId(long recrApplicationId){ 
		this.recrApplicationId = recrApplicationId; 
	} 

	public String getAnswer(){ 
		return answer; 
	} 

	public void setAnswer(String answer){ 
		if ( answer == null ) {
			answer = ""; 
		} 
		this.answer = answer; 
	} 

}
