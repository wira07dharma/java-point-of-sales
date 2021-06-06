
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

public class RecrAnswerIllness extends Entity { 

	private long recrIllnessId;
	private long recrApplicationId;
	private int answer;

	public long getRecrIllnessId(){ 
		return recrIllnessId; 
	} 

	public void setRecrIllnessId(long recrIllnessId){ 
		this.recrIllnessId = recrIllnessId; 
	} 

	public long getRecrApplicationId(){ 
		return recrApplicationId; 
	} 

	public void setRecrApplicationId(long recrApplicationId){ 
		this.recrApplicationId = recrApplicationId; 
	} 

	public int getAnswer(){ 
		return answer; 
	} 

	public void setAnswer(int answer){ 
		this.answer = answer; 
	} 

}
