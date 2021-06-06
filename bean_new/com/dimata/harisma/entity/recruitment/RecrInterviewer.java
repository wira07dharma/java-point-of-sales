
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

public class RecrInterviewer extends Entity { 

	private String interviewer = "";

	public String getInterviewer(){ 
		return interviewer; 
	} 

	public void setInterviewer(String interviewer){ 
		if (interviewer == null ) {
			interviewer = ""; 
		} 
		this.interviewer = interviewer; 
	} 

}
