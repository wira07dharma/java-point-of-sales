
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

public class CardQuestion extends Entity { 

	private long cardQuestionGroupId;
	private String question = "";

	public long getCardQuestionGroupId(){ 
		return cardQuestionGroupId; 
	} 

	public void setCardQuestionGroupId(long cardQuestionGroupId){ 
		this.cardQuestionGroupId = cardQuestionGroupId; 
	} 

	public String getQuestion(){ 
		return question; 
	} 

	public void setQuestion(String question){ 
		if ( question == null ) {
			question = ""; 
		} 
		this.question = question; 
	} 

}
