
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

public class RecrInterviewPoint extends Entity { 

	private int interviewPoint;
	private String interviewMark = "";

	public int getInterviewPoint(){ 
		return interviewPoint; 
	} 

	public void setInterviewPoint(int interviewPoint){ 
		this.interviewPoint = interviewPoint; 
	} 

	public String getInterviewMark(){ 
		return interviewMark; 
	} 

	public void setInterviewMark(String interviewMark){ 
		if ( interviewMark == null ) {
			interviewMark = ""; 
		} 
		this.interviewMark = interviewMark; 
	} 

}
