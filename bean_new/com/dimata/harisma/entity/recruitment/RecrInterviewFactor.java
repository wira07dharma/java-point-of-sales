
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

public class RecrInterviewFactor extends Entity { 

	private String interviewFactor = "";

	public String getInterviewFactor(){ 
		return interviewFactor; 
	} 

	public void setInterviewFactor(String interviewFactor){ 
		if ( interviewFactor == null ) {
			interviewFactor = ""; 
		} 
		this.interviewFactor = interviewFactor; 
	} 

}
