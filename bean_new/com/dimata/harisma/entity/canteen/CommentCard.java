
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

public class CommentCard extends Entity { 

	private long checklistMarkId;
	private long commentCardHeaderId;
	private long cardQuestionId;
	private String remark = "";

	public long getChecklistMarkId(){ 
		return checklistMarkId; 
	} 

	public void setChecklistMarkId(long checklistMarkId){ 
		this.checklistMarkId = checklistMarkId; 
	} 

	public long getCommentCardHeaderId(){ 
		return commentCardHeaderId; 
	} 

	public void setCommentCardHeaderId(long commentCardHeaderId){ 
		this.commentCardHeaderId = commentCardHeaderId; 
	} 

	public long getCardQuestionId(){ 
		return cardQuestionId; 
	} 

	public void setCardQuestionId(long cardQuestionId){ 
		this.cardQuestionId = cardQuestionId; 
	} 

	public String getRemark(){ 
		return remark; 
	} 

	public void setRemark(String remark){ 
		if ( remark == null ) {
			remark = ""; 
		} 
		this.remark = remark; 
	} 

}
