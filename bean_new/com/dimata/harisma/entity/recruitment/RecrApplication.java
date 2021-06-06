
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

public class RecrApplication extends Entity { 

	private String position = "";
	private String otherPosition = "";
	private int salaryExp;
	private Date dateAvailable;
	private String fullName = "";
	private int sex;
	private String birthPlace = "";
	private Date birthDate;
	private long religionId;
	private String address = "";
	private String city = "";
	private int postalCode;
	private String phone = "";
	private String idCardNum = "";
	private String astekNum = "";
	private long maritalId;
	private String passportNo = "";
	private String issuePlace = "";
	private Date validUntil;
	private int height;
	private int weight;
	private String bloodType = "";
	private String distinguishMarks = "";
	private Date applDate;
	private String fatherName = "";
	private int fatherAge;
	private String fatherOccupation = "";
	private String motherName = "";
	private int motherAge;
	private String motherOccupation = "";
	private String familyAddress = "";
	private String familyCity = "";
	private String familyPhone = "";
	private String spouseName = "";
	private Date spouseBirthDate;
	private String spouseOccupation = "";
	private String child1Name = "";
	private Date child1Birthdate;
	private int child1Sex;
	private String child2Name = "";
	private Date child2Birthdate;
	private int child2Sex;
	private String child3Name = "";
	private Date child3Birthdate;
	private int child3Sex;
	private long fnlPositionId;
	private long fnlDepartmentId;
	private long fnlLevelId;
	private String fnlMedicalScheme = "";
	private String fnlHospitalization = "";
	private String fnlAstekDeduction = "";
	private String fnlBasicSalary = "";
	private String fnlServiceCharge = "";
	private String fnlAllowance = "";
	private int fnlAnnualLeave;
	private String fnlOtherBenefit = "";
	private String fnlPrivilege = "";
	private Date fnlCommDate;
	private String fnlProbation = "";

	public String getPosition(){ 
		return position; 
	} 

	public void setPosition(String position){ 
		if ( position == null ) {
			position = ""; 
		} 
		this.position = position; 
	} 

	public String getOtherPosition(){ 
		return otherPosition; 
	} 

	public void setOtherPosition(String otherPosition){ 
		if ( otherPosition == null ) {
			otherPosition = ""; 
		} 
		this.otherPosition = otherPosition; 
	} 

	public int getSalaryExp(){ 
		return salaryExp; 
	} 

	public void setSalaryExp(int salaryExp){ 
		this.salaryExp = salaryExp; 
	} 

	public Date getDateAvailable(){ 
		return dateAvailable; 
	} 

	public void setDateAvailable(Date dateAvailable){ 
		this.dateAvailable = dateAvailable; 
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

	public String getAddress(){ 
		return address; 
	} 

	public void setAddress(String address){ 
		if ( address == null ) {
			address = ""; 
		} 
		this.address = address; 
	} 

	public String getCity(){ 
		return city; 
	} 

	public void setCity(String city){ 
		if ( city == null ) {
			city = ""; 
		} 
		this.city = city; 
	} 

	public int getPostalCode(){ 
		return postalCode; 
	} 

	public void setPostalCode(int postalCode){ 
		this.postalCode = postalCode; 
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

	public String getIdCardNum(){ 
		return idCardNum; 
	} 

	public void setIdCardNum(String idCardNum){ 
		if ( idCardNum == null ) {
			idCardNum = ""; 
		} 
		this.idCardNum = idCardNum; 
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

	public long getMaritalId(){ 
		return maritalId; 
	} 

	public void setMaritalId(long maritalId){ 
		this.maritalId = maritalId; 
	} 

	public String getPassportNo(){ 
		return passportNo; 
	} 

	public void setPassportNo(String passportNo){ 
		if ( passportNo == null ) {
			passportNo = ""; 
		} 
		this.passportNo = passportNo; 
	} 

	public String getIssuePlace(){ 
		return issuePlace; 
	} 

	public void setIssuePlace(String issuePlace){ 
		if ( issuePlace == null ) {
			issuePlace = ""; 
		} 
		this.issuePlace = issuePlace; 
	} 

	public Date getValidUntil(){ 
		return validUntil; 
	} 

	public void setValidUntil(Date validUntil){ 
		this.validUntil = validUntil; 
	} 

	public int getHeight(){ 
		return height; 
	} 

	public void setHeight(int height){ 
		this.height = height; 
	} 

	public int getWeight(){ 
		return weight; 
	} 

	public void setWeight(int weight){ 
		this.weight = weight; 
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

	public String getDistinguishMarks(){ 
		return distinguishMarks; 
	} 

	public void setDistinguishMarks(String distinguishMarks){ 
		if ( distinguishMarks == null ) {
			distinguishMarks = ""; 
		} 
		this.distinguishMarks = distinguishMarks; 
	} 

	public Date getApplDate(){ 
		return applDate; 
	} 

	public void setApplDate(Date applDate){ 
		this.applDate = applDate; 
	} 

	public String getFatherName(){ 
		return fatherName; 
	} 

	public void setFatherName(String fatherName){ 
		if ( fatherName == null ) {
			fatherName = ""; 
		} 
		this.fatherName = fatherName; 
	} 

	public int getFatherAge(){ 
		return fatherAge; 
	} 

	public void setFatherAge(int fatherAge){ 
		this.fatherAge = fatherAge; 
	} 

	public String getFatherOccupation(){ 
		return fatherOccupation; 
	} 

	public void setFatherOccupation(String fatherOccupation){ 
		if ( fatherOccupation == null ) {
			fatherOccupation = ""; 
		} 
		this.fatherOccupation = fatherOccupation; 
	} 

	public String getMotherName(){ 
		return motherName; 
	} 

	public void setMotherName(String motherName){ 
		if ( motherName == null ) {
			motherName = ""; 
		} 
		this.motherName = motherName; 
	} 

	public int getMotherAge(){ 
		return motherAge; 
	} 

	public void setMotherAge(int motherAge){ 
		this.motherAge = motherAge; 
	} 

	public String getMotherOccupation(){ 
		return motherOccupation; 
	} 

	public void setMotherOccupation(String motherOccupation){ 
		if ( motherOccupation == null ) {
			motherOccupation = ""; 
		} 
		this.motherOccupation = motherOccupation; 
	} 

	public String getFamilyAddress(){ 
		return familyAddress; 
	} 

	public void setFamilyAddress(String familyAddress){ 
		if ( familyAddress == null ) {
			familyAddress = ""; 
		} 
		this.familyAddress = familyAddress; 
	} 

	public String getFamilyCity(){ 
		return familyCity; 
	} 

	public void setFamilyCity(String familyCity){ 
		if ( familyCity == null ) {
			familyCity = ""; 
		} 
		this.familyCity = familyCity; 
	} 

	public String getFamilyPhone(){ 
		return familyPhone; 
	} 

	public void setFamilyPhone(String familyPhone){ 
		if ( familyPhone == null ) {
			familyPhone = ""; 
		} 
		this.familyPhone = familyPhone; 
	} 

	public String getSpouseName(){ 
		return spouseName; 
	} 

	public void setSpouseName(String spouseName){ 
		if ( spouseName == null ) {
			spouseName = ""; 
		} 
		this.spouseName = spouseName; 
	} 

	public Date getSpouseBirthDate(){ 
		return spouseBirthDate; 
	} 

	public void setSpouseBirthDate(Date spouseBirthDate){ 
		this.spouseBirthDate = spouseBirthDate; 
	} 

	public String getSpouseOccupation(){ 
		return spouseOccupation; 
	} 

	public void setSpouseOccupation(String spouseOccupation){ 
		if ( spouseOccupation == null ) {
			spouseOccupation = ""; 
		} 
		this.spouseOccupation = spouseOccupation; 
	} 

	public String getChild1Name(){ 
		return child1Name; 
	} 

	public void setChild1Name(String child1Name){ 
		if ( child1Name == null ) {
			child1Name = ""; 
		} 
		this.child1Name = child1Name; 
	} 

	public Date getChild1Birthdate(){ 
		return child1Birthdate; 
	} 

	public void setChild1Birthdate(Date child1Birthdate){ 
		this.child1Birthdate = child1Birthdate; 
	} 

	public int getChild1Sex(){ 
		return child1Sex; 
	} 

	public void setChild1Sex(int child1Sex){ 
		this.child1Sex = child1Sex; 
	} 

	public String getChild2Name(){ 
		return child2Name; 
	} 

	public void setChild2Name(String child2Name){ 
		if ( child2Name == null ) {
			child2Name = ""; 
		} 
		this.child2Name = child2Name; 
	} 

	public Date getChild2Birthdate(){ 
		return child2Birthdate; 
	} 

	public void setChild2Birthdate(Date child2Birthdate){ 
		this.child2Birthdate = child2Birthdate; 
	} 

	public int getChild2Sex(){ 
		return child2Sex; 
	} 

	public void setChild2Sex(int child2Sex){ 
		this.child2Sex = child2Sex; 
	} 

	public String getChild3Name(){ 
		return child3Name; 
	} 

	public void setChild3Name(String child3Name){ 
		if ( child3Name == null ) {
			child3Name = ""; 
		} 
		this.child3Name = child3Name; 
	} 

	public Date getChild3Birthdate(){ 
		return child3Birthdate; 
	} 

	public void setChild3Birthdate(Date child3Birthdate){ 
		this.child3Birthdate = child3Birthdate; 
	} 

	public int getChild3Sex(){ 
		return child3Sex; 
	} 

	public void setChild3Sex(int child3Sex){ 
		this.child3Sex = child3Sex; 
	} 

	public long getFnlPositionId(){ 
		return fnlPositionId; 
	} 

	public void setFnlPositionId(long fnlPositionId){ 
		this.fnlPositionId = fnlPositionId; 
	} 

	public long getFnlDepartmentId(){ 
		return fnlDepartmentId; 
	} 

	public void setFnlDepartmentId(long fnlDepartmentId){ 
		this.fnlDepartmentId = fnlDepartmentId; 
	} 

	public long getFnlLevelId(){ 
		return fnlLevelId; 
	} 

	public void setFnlLevelId(long fnlLevelId){ 
		this.fnlLevelId = fnlLevelId; 
	} 

	public String getFnlMedicalScheme(){ 
		return fnlMedicalScheme; 
	} 

	public void setFnlMedicalScheme(String fnlMedicalScheme){ 
		if ( fnlMedicalScheme == null ) {
			fnlMedicalScheme = ""; 
		} 
		this.fnlMedicalScheme = fnlMedicalScheme; 
	} 

	public String getFnlHospitalization(){ 
		return fnlHospitalization; 
	} 

	public void setFnlHospitalization(String fnlHospitalization){ 
		if ( fnlHospitalization == null ) {
			fnlHospitalization = ""; 
		} 
		this.fnlHospitalization = fnlHospitalization; 
	} 

	public String getFnlAstekDeduction(){ 
		return fnlAstekDeduction; 
	} 

	public void setFnlAstekDeduction(String fnlAstekDeduction){ 
		if ( fnlAstekDeduction == null ) {
			fnlAstekDeduction = ""; 
		} 
		this.fnlAstekDeduction = fnlAstekDeduction; 
	} 

	public String getFnlBasicSalary(){ 
		return fnlBasicSalary; 
	} 

	public void setFnlBasicSalary(String fnlBasicSalary){ 
		if ( fnlBasicSalary == null ) {
			fnlBasicSalary = ""; 
		} 
		this.fnlBasicSalary = fnlBasicSalary; 
	} 

	public String getFnlServiceCharge(){ 
		return fnlServiceCharge; 
	} 

	public void setFnlServiceCharge(String fnlServiceCharge){ 
		if ( fnlServiceCharge == null ) {
			fnlServiceCharge = ""; 
		} 
		this.fnlServiceCharge = fnlServiceCharge; 
	} 

	public String getFnlAllowance(){ 
		return fnlAllowance; 
	} 

	public void setFnlAllowance(String fnlAllowance){ 
		if ( fnlAllowance == null ) {
			fnlAllowance = ""; 
		} 
		this.fnlAllowance = fnlAllowance; 
	} 

	public int getFnlAnnualLeave(){ 
		return fnlAnnualLeave; 
	} 

	public void setFnlAnnualLeave(int fnlAnnualLeave){ 
		this.fnlAnnualLeave = fnlAnnualLeave; 
	} 

	public String getFnlOtherBenefit(){ 
		return fnlOtherBenefit; 
	} 

	public void setFnlOtherBenefit(String fnlOtherBenefit){ 
		if ( fnlOtherBenefit == null ) {
			fnlOtherBenefit = ""; 
		} 
		this.fnlOtherBenefit = fnlOtherBenefit; 
	} 

	public String getFnlPrivilege(){ 
		return fnlPrivilege; 
	} 

	public void setFnlPrivilege(String fnlPrivilege){ 
		if ( fnlPrivilege == null ) {
			fnlPrivilege = ""; 
		} 
		this.fnlPrivilege = fnlPrivilege; 
	} 

	public Date getFnlCommDate(){ 
		return fnlCommDate; 
	} 

	public void setFnlCommDate(Date fnlCommDate){ 
		this.fnlCommDate = fnlCommDate; 
	} 

	public String getFnlProbation(){ 
		return fnlProbation; 
	} 

	public void setFnlProbation(String fnlProbation){ 
		if ( fnlProbation == null ) {
			fnlProbation = ""; 
		} 
		this.fnlProbation = fnlProbation; 
	} 
}
