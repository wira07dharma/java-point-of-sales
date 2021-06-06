
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

public class OriChecklist extends Entity { 

	private long recrApplicationId;
	private long interviewerId;
	private Date signatureDate;
	private Date interviewDate;
	private String skills = "";

	public long getRecrApplicationId(){ 
		return recrApplicationId; 
	} 

	public void setRecrApplicationId(long recrApplicationId){ 
		this.recrApplicationId = recrApplicationId; 
	} 

	public long getInterviewerId(){ 
		return interviewerId; 
	} 

	public void setInterviewerId(long interviewerId){ 
		this.interviewerId = interviewerId; 
	} 

	public Date getSignatureDate(){ 
		return signatureDate; 
	} 

	public void setSignatureDate(Date signatureDate){ 
		this.signatureDate = signatureDate; 
	} 

	public Date getInterviewDate(){ 
		return interviewDate; 
	} 

	public void setInterviewDate(Date interviewDate){ 
		this.interviewDate = interviewDate; 
	} 

	public String getSkills(){ 
		return skills; 
	} 

	public void setSkills(String skills){ 
		if ( skills == null ) {
			skills = ""; 
		} 
		this.skills = skills; 
	} 
}
