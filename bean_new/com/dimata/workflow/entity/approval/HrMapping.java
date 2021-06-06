
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

package com.dimata.workflow.entity.approval; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class HrMapping extends Entity { 

	private long appMappingOid;
	private long departmentId;
	private long sectionId;
	private long positionId;

	public long getAppMappingOid(){ 
		return appMappingOid; 
	} 

	public void setAppMappingOid(long appMappingOid){ 
		this.appMappingOid = appMappingOid; 
	} 

	public long getDepartmentId(){ 
		return departmentId; 
	} 

	public void setDepartmentId(long departmentId){ 
		this.departmentId = departmentId; 
	} 

	public long getSectionId(){ 
		return sectionId; 
	} 

	public void setSectionId(long sectionId){ 
		this.sectionId = sectionId; 
	} 

	public long getPositionId(){ 
		return positionId; 
	} 

	public void setPositionId(long positionId){ 
		this.positionId = positionId; 
	} 

}
