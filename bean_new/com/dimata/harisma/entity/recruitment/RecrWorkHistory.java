
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

public class RecrWorkHistory extends Entity { 

	private long recrApplicationId;
	private String position = "";
	private Date startDate;
	private Date endDate;
	private String duties = "";
	private int commSalary;
	private int lastSalary;
	private String companyName = "";
	private String companyAddress = "";
	private String companyPhone = "";
	private String companyNature = "";
	private String companySpv = "";
	private String leaveReason = "";

	public long getRecrApplicationId(){ 
		return recrApplicationId; 
	} 

	public void setRecrApplicationId(long recrApplicationId){ 
		this.recrApplicationId = recrApplicationId; 
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

	public String getDuties(){ 
		return duties; 
	} 

	public void setDuties(String duties){ 
		if ( duties == null ) {
			duties = ""; 
		} 
		this.duties = duties; 
	} 

	public int getCommSalary(){ 
		return commSalary; 
	} 

	public void setCommSalary(int commSalary){ 
		this.commSalary = commSalary; 
	} 

	public int getLastSalary(){ 
		return lastSalary; 
	} 

	public void setLastSalary(int lastSalary){ 
		this.lastSalary = lastSalary; 
	} 

	public String getCompanyName(){ 
		return companyName; 
	} 

	public void setCompanyName(String companyName){ 
		if ( companyName == null ) {
			companyName = ""; 
		} 
		this.companyName = companyName; 
	} 

	public String getCompanyAddress(){ 
		return companyAddress; 
	} 

	public void setCompanyAddress(String companyAddress){ 
		if ( companyAddress == null ) {
			companyAddress = ""; 
		} 
		this.companyAddress = companyAddress; 
	} 

	public String getCompanyPhone(){ 
		return companyPhone; 
	} 

	public void setCompanyPhone(String companyPhone){ 
		if ( companyPhone == null ) {
			companyPhone = ""; 
		} 
		this.companyPhone = companyPhone; 
	} 

	public String getCompanyNature(){ 
		return companyNature; 
	} 

	public void setCompanyNature(String companyNature){ 
		if ( companyNature == null ) {
			companyNature = ""; 
		} 
		this.companyNature = companyNature; 
	} 

	public String getCompanySpv(){ 
		return companySpv; 
	} 

	public void setCompanySpv(String companySpv){ 
		if ( companySpv == null ) {
			companySpv = ""; 
		} 
		this.companySpv = companySpv; 
	} 

	public String getLeaveReason(){ 
		return leaveReason; 
	} 

	public void setLeaveReason(String leaveReason){ 
		if ( leaveReason == null ) {
			leaveReason = ""; 
		} 
		this.leaveReason = leaveReason; 
	} 

}
