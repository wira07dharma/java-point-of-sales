
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.employee; 
 
/* package java */ 
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class CareerPath extends Entity { 

	private long employeeId;
	private long departmentId;
	private String department = "";
	private long positionId;
	private String position = "";
	private long sectionId;
	private String section = "";
	private Date workFrom;
	private Date workTo;
	private String description = "";

	public long getEmployeeId(){ 
		return employeeId; 
	} 

	public void setEmployeeId(long employeeId){ 
		this.employeeId = employeeId; 
	} 

	public long getDepartmentId(){ 
		return departmentId; 
	} 

	public void setDepartmentId(long departmentId){ 
		this.departmentId = departmentId; 
	} 

	public String getDepartment(){ 
		return department; 
	} 

	public void setDepartment(String department){ 
		if ( department == null ) {
			department = ""; 
		} 
		this.department = department; 
	} 

	public long getPositionId(){ 
		return positionId; 
	} 

	public void setPositionId(long positionId){ 
		this.positionId = positionId; 
	} 

	public String getPosition(){ 
		return position; 
	} 

	public void setPosition(String position){ 
		if ( position == null ) {
			position = ""; 
		} 
		this.position = position; 
	} 

	public long getSectionId(){ 
		return sectionId; 
	} 

	public void setSectionId(long sectionId){ 
		this.sectionId = sectionId; 
	} 

	public String getSection(){ 
		return section; 
	} 

	public void setSection(String section){ 
		if ( section == null ) {
			section = ""; 
		} 
		this.section = section; 
	} 

	public Date getWorkFrom(){ 
		return workFrom; 
	} 

	public void setWorkFrom(Date workFrom){ 
		this.workFrom = workFrom; 
	} 

	public Date getWorkTo(){ 
		return workTo; 
	} 

	public void setWorkTo(Date workTo){ 
		this.workTo = workTo; 
	} 

	public String getDescription(){ 
		return description; 
	} 

	public void setDescription(String description){ 
		if ( description == null ) {
			description = ""; 
		} 
		this.description = description; 
	} 

}
