
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

public class StaffRequisition extends Entity { 

	private long departmentId;
	private long sectionId;
	private long positionId;
	private long empCategoryId;
	private int requisitionType;
	private int neededMale;
	private int neededFemale;
	private Date expCommDate;
	private int tempFor;
	private long approvedBy;
	private Date approvedDate;
	private long acknowledgedBy;
	private Date acknowledgedDate;
	private long requestedBy;
	private Date requestedDate;

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

	public long getEmpCategoryId(){ 
		return empCategoryId; 
	} 

	public void setEmpCategoryId(long empCategoryId){ 
		this.empCategoryId = empCategoryId; 
	} 

	public int getRequisitionType(){ 
		return requisitionType; 
	} 

	public void setRequisitionType(int requisitionType){ 
		this.requisitionType = requisitionType; 
	} 

	public int getNeededMale(){ 
		return neededMale; 
	} 

	public void setNeededMale(int neededMale){ 
		this.neededMale = neededMale; 
	} 

	public int getNeededFemale(){ 
		return neededFemale; 
	} 

	public void setNeededFemale(int neededFemale){ 
		this.neededFemale = neededFemale; 
	} 

	public Date getExpCommDate(){ 
		return expCommDate; 
	} 

	public void setExpCommDate(Date expCommDate){ 
		this.expCommDate = expCommDate; 
	} 

	public int getTempFor(){ 
		return tempFor; 
	} 

	public void setTempFor(int tempFor){ 
		this.tempFor = tempFor; 
	} 

	public long getApprovedBy(){ 
		return approvedBy; 
	} 

	public void setApprovedBy(long approvedBy){ 
		this.approvedBy = approvedBy; 
	} 

	public Date getApprovedDate(){ 
		return approvedDate; 
	} 

	public void setApprovedDate(Date approvedDate){ 
		this.approvedDate = approvedDate; 
	} 

	public long getAcknowledgedBy(){ 
		return acknowledgedBy; 
	} 

	public void setAcknowledgedBy(long acknowledgedBy){ 
		this.acknowledgedBy = acknowledgedBy; 
	} 

	public Date getAcknowledgedDate(){ 
		return acknowledgedDate; 
	} 

	public void setAcknowledgedDate(Date acknowledgedDate){ 
		this.acknowledgedDate = acknowledgedDate; 
	} 

	public long getRequestedBy(){ 
		return requestedBy; 
	} 

	public void setRequestedBy(long requestedBy){ 
		this.requestedBy = requestedBy; 
	} 

	public Date getRequestedDate(){ 
		return requestedDate; 
	} 

	public void setRequestedDate(Date requestedDate){ 
		this.requestedDate = requestedDate; 
	} 

}
