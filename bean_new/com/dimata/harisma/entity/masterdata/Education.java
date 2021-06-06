
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

package com.dimata.harisma.entity.masterdata; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Education extends Entity { 

	private String education = "";
	private String educationDesc = "";

	public String getEducation(){ 
		return education; 
	} 

	public void setEducation(String education){ 
		if ( education == null ) {
			education = ""; 
		} 
		this.education = education; 
	} 

	public String getEducationDesc(){ 
		return educationDesc; 
	} 

	public void setEducationDesc(String educationDesc){ 
		if ( educationDesc == null ) {
			educationDesc = ""; 
		} 
		this.educationDesc = educationDesc; 
	} 

}
