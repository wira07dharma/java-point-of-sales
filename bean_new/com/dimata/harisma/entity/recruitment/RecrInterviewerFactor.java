
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

public class RecrInterviewerFactor extends Entity { 

	private long recrInterviewFactorId;
	private long recrInterviewerId;

	public long getRecrInterviewFactorId(){ 
		return recrInterviewFactorId; 
	} 

	public void setRecrInterviewFactorId(long recrInterviewFactorId){ 
		this.recrInterviewFactorId = recrInterviewFactorId; 
	} 

	public long getRecrInterviewerId(){ 
		return recrInterviewerId; 
	} 

	public void setRecrInterviewerId(long recrInterviewerId){ 
		this.recrInterviewerId = recrInterviewerId; 
	} 

}
