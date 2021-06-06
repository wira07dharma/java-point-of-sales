
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

public class TrainingDept extends Entity { 

	private long departmentId;
	private long trainingId;

	public long getDepartmentId(){
		return departmentId; 
	} 

	public void setDepartmentId(long departmentId){
		this.departmentId = departmentId; 
	} 

	public long getTrainingId(){ 
		return trainingId; 
	} 

	public void setTrainingId(long trainingId){ 
		this.trainingId = trainingId; 
	} 

}
