
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

public class RecrGeneral extends Entity { 

	private String question = "";

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
