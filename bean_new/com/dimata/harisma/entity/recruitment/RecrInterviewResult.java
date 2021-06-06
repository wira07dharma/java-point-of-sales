
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

public class RecrInterviewResult extends Entity { 

	private long recrInterviewPointId;
	private long recrInterviewerFactorId;
	private long recrApplicationId;

	public long getRecrInterviewPointId(){ 
		return recrInterviewPointId; 
	} 

	public void setRecrInterviewPointId(long recrInterviewPointId){ 
		this.recrInterviewPointId = recrInterviewPointId; 
	} 

	public long getRecrInterviewerFactorId(){ 
		return recrInterviewerFactorId; 
	} 

	public void setRecrInterviewerFactorId(long recrInterviewerFactorId){ 
		this.recrInterviewerFactorId = recrInterviewerFactorId; 
	} 

	public long getRecrApplicationId(){ 
		return recrApplicationId; 
	} 

	public void setRecrApplicationId(long recrApplicationId){ 
		this.recrApplicationId = recrApplicationId; 
	} 

}
