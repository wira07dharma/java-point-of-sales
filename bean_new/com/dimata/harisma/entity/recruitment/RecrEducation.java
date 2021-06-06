
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

public class RecrEducation extends Entity { 

	private long recrApplicationId;
	private long educationId;
	private Date startDate;
	private Date endDate;
	private String study = "";
	private String degree = "";

	public long getRecrApplicationId(){ 
		return recrApplicationId; 
	} 

	public void setRecrApplicationId(long recrApplicationId){ 
		this.recrApplicationId = recrApplicationId; 
	} 

	public long getEducationId(){ 
		return educationId; 
	} 

	public void setEducationId(long educationId){ 
		this.educationId = educationId; 
	} 

	public Date getStartDate(){ 
		return startDate; 
	} 

	public void setStartDate(Date startDate){ 
		this.startDate = startDate; 
	} 

	public Date getEndDate(){ 
		return endDate; 
	} 

	public void setEndDate(Date endDate){ 
		this.endDate = endDate; 
	} 

	public String getStudy(){ 
		return study; 
	} 

	public void setStudy(String study){ 
		if ( study == null ) {
			study = ""; 
		} 
		this.study = study; 
	} 

	public String getDegree(){ 
		return degree; 
	} 

	public void setDegree(String degree){ 
		if ( degree == null ) {
			degree = ""; 
		} 
		this.degree = degree; 
	} 

}
