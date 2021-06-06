
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

public class Employee extends Entity { 

    private long departmentId;
    private long positionId;
    private long sectionId;
    private String employeeNum = "";
    private long empCategoryId;
    private long levelId;
    private String fullName = "";
    private String address = "";
    private String phone = "";
    private String handphone = "";
    private int postalCode;
    private int sex;
    private String birthPlace = "";
    private Date birthDate;
    private long religionId;
    private String bloodType = "";
    private String astekNum = "";
    private Date astekDate;
    private long maritalId;
    private long lockerId;
    private Date commencingDate;
    private int resigned;
    private Date resignedDate;
    private String barcodeNumber;// = "";
    private long resignedReasonId;
    private String resignedDesc = "";
    private double basicSalary;
    private boolean isAssignToAccounting;
    private long divisionId;
	private long companyId;
	//added arisena
	private long locationId = 0;


    public long getDivisionId(){
        return divisionId;
    }

    public void setDivisionId(long divisionId){
        this.divisionId = divisionId;
    }

	public long getDepartmentId(){
		return departmentId; 
	} 

	public void setDepartmentId(long departmentId){ 
		this.departmentId = departmentId; 
	} 

	public long getPositionId(){ 
		return positionId; 
	} 

	public void setPositionId(long positionId){ 
		this.positionId = positionId; 
	} 

	public long getSectionId(){ 
		return sectionId; 
	} 

	public void setSectionId(long sectionId){ 
		this.sectionId = sectionId; 
	} 

	public String getEmployeeNum(){ 
		return employeeNum; 
	} 

	public void setEmployeeNum(String employeeNum){ 
		if ( employeeNum == null ) {
			employeeNum = ""; 
		} 
		this.employeeNum = employeeNum; 
	} 

	public long getEmpCategoryId(){ 
		return empCategoryId; 
	} 

	public void setEmpCategoryId(long empCategoryId){ 
		this.empCategoryId = empCategoryId; 
	} 

	public long getLevelId(){ 
		return levelId; 
	} 

	public void setLevelId(long levelId){ 
		this.levelId = levelId; 
	} 

	public String getFullName(){ 
		return fullName; 
	} 

	public void setFullName(String fullName){ 
		if ( fullName == null ) {
			fullName = ""; 
		} 
		this.fullName = fullName; 
	} 

	public String getAddress(){ 
		return address; 
	} 

	public void setAddress(String address){ 
		if ( address == null ) {
			address = ""; 
		} 
		this.address = address; 
	} 

	public String getPhone(){ 
		return phone; 
	} 

	public void setPhone(String phone){ 
		if ( phone == null ) {
			phone = ""; 
		} 
		this.phone = phone; 
	} 

	public String getHandphone(){ 
		return handphone; 
	} 

	public void setHandphone(String handphone){ 
		if ( handphone == null ) {
			handphone = ""; 
		} 
		this.handphone = handphone; 
	} 

	public int getPostalCode(){ 
		return postalCode; 
	} 

	public void setPostalCode(int postalCode){ 
		this.postalCode = postalCode; 
	} 

	public int getSex(){
		return sex; 
	} 

	public void setSex(int sex){
		this.sex = sex; 
	} 

	public String getBirthPlace(){ 
		return birthPlace; 
	} 

	public void setBirthPlace(String birthPlace){ 
		if ( birthPlace == null ) {
			birthPlace = ""; 
		} 
		this.birthPlace = birthPlace; 
	} 

	public Date getBirthDate(){ 
		return birthDate; 
	} 

	public void setBirthDate(Date birthDate){ 
		this.birthDate = birthDate; 
	} 

	public long getReligionId(){ 
		return religionId; 
	} 

	public void setReligionId(long religionId){ 
		this.religionId = religionId; 
	} 

	public String getBloodType(){ 
		return bloodType; 
	} 

	public void setBloodType(String bloodType){ 
		if ( bloodType == null ) {
			bloodType = ""; 
		} 
		this.bloodType = bloodType; 
	} 

	public String getAstekNum(){ 
		return astekNum; 
	} 

	public void setAstekNum(String astekNum){ 
		if ( astekNum == null ) {
			astekNum = ""; 
		} 
		this.astekNum = astekNum; 
	} 

	public Date getAstekDate(){ 
		return astekDate; 
	} 

	public void setAstekDate(Date astekDate){ 
		this.astekDate = astekDate; 
	} 

	public long getMaritalId(){ 
		return maritalId; 
	} 

	public void setMaritalId(long maritalId){ 
		this.maritalId = maritalId; 
	} 

	public long getLockerId(){ 
		return lockerId; 
	} 

	public void setLockerId(long lockerId){ 
		this.lockerId = lockerId; 
	} 

	public Date getCommencingDate(){ 
		return commencingDate; 
	} 

	public void setCommencingDate(Date commencingDate){ 
		this.commencingDate = commencingDate; 
	} 

	public int getResigned(){ 
		return resigned; 
	} 

	public void setResigned(int resigned){ 
		this.resigned = resigned; 
	} 

	public Date getResignedDate(){ 
		return resignedDate; 
	} 

	public void setResignedDate(Date resignedDate){ 
		this.resignedDate = resignedDate; 
	} 

	public String getBarcodeNumber(){ 
		return barcodeNumber; 
	} 

	public void setBarcodeNumber(String barcodeNumber){ 
		//if ( barcodeNumber == null ) {
		//	barcodeNumber = ""; 
		//} 
		this.barcodeNumber = barcodeNumber; 
	} 

	public long getResignedReasonId(){ 
		return resignedReasonId; 
	} 

	public void setResignedReasonId(long resignedReasonId){ 
		this.resignedReasonId = resignedReasonId; 
	} 

	public String getResignedDesc(){ 
		return resignedDesc; 
	} 

	public void setResignedDesc(String resignedDesc){ 
		if (resignedDesc == null) {
			resignedDesc = ""; 
		} 
		this.resignedDesc = resignedDesc; 
	}

    public double getBasicSalary(){
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary){
        this.basicSalary = basicSalary;
    }

    public boolean getIsAssignToAccounting(){
        return isAssignToAccounting;
    }

    public void setIsAssignToAccounting(boolean isAssignToAccounting){
        this.isAssignToAccounting = isAssignToAccounting;
    }

	/**
	 * @return the companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the locationId
	 */
	public long getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
}
