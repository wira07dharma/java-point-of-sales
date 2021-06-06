
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
import java.sql.*
;import java.util.*
;
/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;

/* package harisma */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;

public class PstRecrApplication extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_RECR_APPLICATION = "HR_RECR_APPLICATION";

	public static final  int FLD_RECR_APPLICATION_ID = 0;
	public static final  int FLD_POSITION = 1;
	public static final  int FLD_OTHER_POSITION = 2;
	public static final  int FLD_SALARY_EXP = 3;
	public static final  int FLD_DATE_AVAILABLE = 4;
	public static final  int FLD_FULL_NAME = 5;
	public static final  int FLD_SEX = 6;
	public static final  int FLD_BIRTH_PLACE = 7;
	public static final  int FLD_BIRTH_DATE = 8;
	public static final  int FLD_RELIGION_ID = 9;
	public static final  int FLD_ADDRESS = 10;
	public static final  int FLD_CITY = 11;
	public static final  int FLD_POSTAL_CODE = 12;
	public static final  int FLD_PHONE = 13;
	public static final  int FLD_ID_CARD_NUM = 14;
	public static final  int FLD_ASTEK_NUM = 15;
	public static final  int FLD_MARITAL_ID = 16;
	public static final  int FLD_PASSPORT_NO = 17;
	public static final  int FLD_ISSUE_PLACE = 18;
	public static final  int FLD_VALID_UNTIL = 19;
	public static final  int FLD_HEIGHT = 20;
	public static final  int FLD_WEIGHT = 21;
	public static final  int FLD_BLOOD_TYPE = 22;
	public static final  int FLD_DISTINGUISH_MARKS = 23;
	public static final  int FLD_APPL_DATE = 24;
	public static final  int FLD_FATHER_NAME = 25;
	public static final  int FLD_FATHER_AGE = 26;
	public static final  int FLD_FATHER_OCCUPATION = 27;
	public static final  int FLD_MOTHER_NAME = 28;
	public static final  int FLD_MOTHER_AGE = 29;
	public static final  int FLD_MOTHER_OCCUPATION = 30;
	public static final  int FLD_FAMILY_ADDRESS = 31;
	public static final  int FLD_FAMILY_CITY = 32;
	public static final  int FLD_FAMILY_PHONE = 33;
	public static final  int FLD_SPOUSE_NAME = 34;
	public static final  int FLD_SPOUSE_BIRTH_DATE = 35;
	public static final  int FLD_SPOUSE_OCCUPATION = 36;
	public static final  int FLD_CHILD1_NAME = 37;
	public static final  int FLD_CHILD1_BIRTHDATE = 38;
	public static final  int FLD_CHILD1_SEX = 39;
	public static final  int FLD_CHILD2_NAME = 40;
	public static final  int FLD_CHILD2_BIRTHDATE = 41;
	public static final  int FLD_CHILD2_SEX = 42;
	public static final  int FLD_CHILD3_NAME = 43;
	public static final  int FLD_CHILD3_BIRTHDATE = 44;
	public static final  int FLD_CHILD3_SEX = 45;
	public static final  int FLD_FNL_POSITION_ID = 46;
	public static final  int FLD_FNL_DEPARTMENT_ID = 47;
	public static final  int FLD_FNL_LEVEL_ID = 48;
	public static final  int FLD_FNL_MEDICAL_SCHEME = 49;
	public static final  int FLD_FNL_HOSPITALIZATION = 50;
	public static final  int FLD_FNL_ASTEK_DEDUCTION = 51;
	public static final  int FLD_FNL_BASIC_SALARY = 52;
	public static final  int FLD_FNL_SERVICE_CHARGE = 53;
	public static final  int FLD_FNL_ALLOWANCE = 54;
	public static final  int FLD_FNL_ANNUAL_LEAVE = 55;
	public static final  int FLD_FNL_OTHER_BENEFIT = 56;
	public static final  int FLD_FNL_PRIVILEGE = 57;
	public static final  int FLD_FNL_COMM_DATE = 58;
	public static final  int FLD_FNL_PROBATION = 59;

	public static final  String[] fieldNames = {
		"RECR_APPLICATION_ID",
		"POSITION",
		"OTHER_POSITION",
		"SALARY_EXP",
		"DATE_AVAILABLE",
		"FULL_NAME",
		"SEX",
		"BIRTH_PLACE",
		"BIRTH_DATE",
		"RELIGION_ID",
		"ADDRESS",
		"CITY",
		"POSTAL_CODE",
		"PHONE",
		"ID_CARD_NUM",
		"ASTEK_NUM",
		"MARITAL_ID",
		"PASSPORT_NO",
		"ISSUE_PLACE",
		"VALID_UNTIL",
		"HEIGHT",
		"WEIGHT",
		"BLOOD_TYPE",
		"DISTINGUISH_MARKS",
		"APPL_DATE",
		"FATHER_NAME",
		"FATHER_AGE",
		"FATHER_OCCUPATION",
		"MOTHER_NAME",
		"MOTHER_AGE",
		"MOTHER_OCCUPATION",
		"FAMILY_ADDRESS",
		"FAMILY_CITY",
		"FAMILY_PHONE",
		"SPOUSE_NAME",
		"SPOUSE_BIRTH_DATE",
		"SPOUSE_OCCUPATION",
		"CHILD1_NAME",
		"CHILD1_BIRTHDATE",
		"CHILD1_SEX",
		"CHILD2_NAME",
		"CHILD2_BIRTHDATE",
		"CHILD2_SEX",
		"CHILD3_NAME",
		"CHILD3_BIRTHDATE",
		"CHILD3_SEX",
		"FNL_POSITION_ID",
		"FNL_DEPARTMENT_ID",
		"FNL_LEVEL_ID",
		"FNL_MEDICAL_SCHEME",
		"FNL_HOSPITALIZATION",
		"FNL_ASTEK_DEDUCTION",
		"FNL_BASIC_SALARY",
		"FNL_SERVICE_CHARGE",
		"FNL_ALLOWANCE",
		"FNL_ANNUAL_LEAVE",
		"FNL_OTHER_BENEFIT",
		"FNL_PRIVILEGE",
		"FNL_COMM_DATE",
		"FNL_PROBATION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_INT,
		TYPE_DATE,
		TYPE_STRING,
		TYPE_INT,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_INT,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_STRING,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_INT,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_INT,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_INT,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_INT,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_STRING
	 }; 

        //gender----
        public static final int MALE 	= 0;
        public static final int FEMALE 	= 1;
        public static final String[] sexKey = {"Male","Female"};
        public static final int[] sexValue = {0,1};

	public PstRecrApplication(){
	}

	public PstRecrApplication(int i) throws DBException { 
		super(new PstRecrApplication()); 
	}

	public PstRecrApplication(String sOid) throws DBException { 
		super(new PstRecrApplication(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecrApplication(long lOid) throws DBException { 
		super(new PstRecrApplication(0)); 
		String sOid = "0"; 
		try { 
			sOid = String.valueOf(lOid); 
		}catch(Exception e) { 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	} 

	public int getFieldSize(){ 
		return fieldNames.length; 
	}

	public String getTableName(){ 
		return TBL_HR_RECR_APPLICATION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecrApplication().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		RecrApplication recrapplication = fetchExc(ent.getOID()); 
		ent = (Entity)recrapplication; 
		return recrapplication.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((RecrApplication) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((RecrApplication) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static RecrApplication fetchExc(long oid) throws DBException{ 
		try{ 
			RecrApplication recrapplication = new RecrApplication();
			PstRecrApplication pstRecrApplication = new PstRecrApplication(oid); 
			recrapplication.setOID(oid);

			recrapplication.setPosition(pstRecrApplication.getString(FLD_POSITION));
			recrapplication.setOtherPosition(pstRecrApplication.getString(FLD_OTHER_POSITION));
			recrapplication.setSalaryExp(pstRecrApplication.getInt(FLD_SALARY_EXP));
			recrapplication.setDateAvailable(pstRecrApplication.getDate(FLD_DATE_AVAILABLE));
			recrapplication.setFullName(pstRecrApplication.getString(FLD_FULL_NAME));
			recrapplication.setSex(pstRecrApplication.getInt(FLD_SEX));
			recrapplication.setBirthPlace(pstRecrApplication.getString(FLD_BIRTH_PLACE));
			recrapplication.setBirthDate(pstRecrApplication.getDate(FLD_BIRTH_DATE));
			recrapplication.setReligionId(pstRecrApplication.getlong(FLD_RELIGION_ID));
			recrapplication.setAddress(pstRecrApplication.getString(FLD_ADDRESS));
			recrapplication.setCity(pstRecrApplication.getString(FLD_CITY));
			recrapplication.setPostalCode(pstRecrApplication.getInt(FLD_POSTAL_CODE));
			recrapplication.setPhone(pstRecrApplication.getString(FLD_PHONE));
			recrapplication.setIdCardNum(pstRecrApplication.getString(FLD_ID_CARD_NUM));
			recrapplication.setAstekNum(pstRecrApplication.getString(FLD_ASTEK_NUM));
			recrapplication.setMaritalId(pstRecrApplication.getlong(FLD_MARITAL_ID));
			recrapplication.setPassportNo(pstRecrApplication.getString(FLD_PASSPORT_NO));
			recrapplication.setIssuePlace(pstRecrApplication.getString(FLD_ISSUE_PLACE));
			recrapplication.setValidUntil(pstRecrApplication.getDate(FLD_VALID_UNTIL));
			recrapplication.setHeight(pstRecrApplication.getInt(FLD_HEIGHT));
			recrapplication.setWeight(pstRecrApplication.getInt(FLD_WEIGHT));
			recrapplication.setBloodType(pstRecrApplication.getString(FLD_BLOOD_TYPE));
			recrapplication.setDistinguishMarks(pstRecrApplication.getString(FLD_DISTINGUISH_MARKS));
			recrapplication.setApplDate(pstRecrApplication.getDate(FLD_APPL_DATE));
			recrapplication.setFatherName(pstRecrApplication.getString(FLD_FATHER_NAME));
			recrapplication.setFatherAge(pstRecrApplication.getInt(FLD_FATHER_AGE));
			recrapplication.setFatherOccupation(pstRecrApplication.getString(FLD_FATHER_OCCUPATION));
			recrapplication.setMotherName(pstRecrApplication.getString(FLD_MOTHER_NAME));
			recrapplication.setMotherAge(pstRecrApplication.getInt(FLD_MOTHER_AGE));
			recrapplication.setMotherOccupation(pstRecrApplication.getString(FLD_MOTHER_OCCUPATION));
			recrapplication.setFamilyAddress(pstRecrApplication.getString(FLD_FAMILY_ADDRESS));
			recrapplication.setFamilyCity(pstRecrApplication.getString(FLD_FAMILY_CITY));
			recrapplication.setFamilyPhone(pstRecrApplication.getString(FLD_FAMILY_PHONE));
			recrapplication.setSpouseName(pstRecrApplication.getString(FLD_SPOUSE_NAME));
			recrapplication.setSpouseBirthDate(pstRecrApplication.getDate(FLD_SPOUSE_BIRTH_DATE));
			recrapplication.setSpouseOccupation(pstRecrApplication.getString(FLD_SPOUSE_OCCUPATION));
			recrapplication.setChild1Name(pstRecrApplication.getString(FLD_CHILD1_NAME));
			recrapplication.setChild1Birthdate(pstRecrApplication.getDate(FLD_CHILD1_BIRTHDATE));
			recrapplication.setChild1Sex(pstRecrApplication.getInt(FLD_CHILD1_SEX));
			recrapplication.setChild2Name(pstRecrApplication.getString(FLD_CHILD2_NAME));
			recrapplication.setChild2Birthdate(pstRecrApplication.getDate(FLD_CHILD2_BIRTHDATE));
			recrapplication.setChild2Sex(pstRecrApplication.getInt(FLD_CHILD2_SEX));
			recrapplication.setChild3Name(pstRecrApplication.getString(FLD_CHILD3_NAME));
			recrapplication.setChild3Birthdate(pstRecrApplication.getDate(FLD_CHILD3_BIRTHDATE));
			recrapplication.setChild3Sex(pstRecrApplication.getInt(FLD_CHILD3_SEX));
			recrapplication.setFnlPositionId(pstRecrApplication.getlong(FLD_FNL_POSITION_ID));
			recrapplication.setFnlDepartmentId(pstRecrApplication.getlong(FLD_FNL_DEPARTMENT_ID));
			recrapplication.setFnlLevelId(pstRecrApplication.getlong(FLD_FNL_LEVEL_ID));
			recrapplication.setFnlMedicalScheme(pstRecrApplication.getString(FLD_FNL_MEDICAL_SCHEME));
			recrapplication.setFnlHospitalization(pstRecrApplication.getString(FLD_FNL_HOSPITALIZATION));
			recrapplication.setFnlAstekDeduction(pstRecrApplication.getString(FLD_FNL_ASTEK_DEDUCTION));
			recrapplication.setFnlBasicSalary(pstRecrApplication.getString(FLD_FNL_BASIC_SALARY));
			recrapplication.setFnlServiceCharge(pstRecrApplication.getString(FLD_FNL_SERVICE_CHARGE));
			recrapplication.setFnlAllowance(pstRecrApplication.getString(FLD_FNL_ALLOWANCE));
			recrapplication.setFnlAnnualLeave(pstRecrApplication.getInt(FLD_FNL_ANNUAL_LEAVE));
			recrapplication.setFnlOtherBenefit(pstRecrApplication.getString(FLD_FNL_OTHER_BENEFIT));
			recrapplication.setFnlPrivilege(pstRecrApplication.getString(FLD_FNL_PRIVILEGE));
			recrapplication.setFnlCommDate(pstRecrApplication.getDate(FLD_FNL_COMM_DATE));
			recrapplication.setFnlProbation(pstRecrApplication.getString(FLD_FNL_PROBATION));

			return recrapplication; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrApplication(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(RecrApplication recrapplication) throws DBException{ 
		try{ 
			PstRecrApplication pstRecrApplication = new PstRecrApplication(0);

			pstRecrApplication.setString(FLD_POSITION, recrapplication.getPosition());
			pstRecrApplication.setString(FLD_OTHER_POSITION, recrapplication.getOtherPosition());
			pstRecrApplication.setInt(FLD_SALARY_EXP, recrapplication.getSalaryExp());
			pstRecrApplication.setDate(FLD_DATE_AVAILABLE, recrapplication.getDateAvailable());
			pstRecrApplication.setString(FLD_FULL_NAME, recrapplication.getFullName());
			pstRecrApplication.setInt(FLD_SEX, recrapplication.getSex());
			pstRecrApplication.setString(FLD_BIRTH_PLACE, recrapplication.getBirthPlace());
			pstRecrApplication.setDate(FLD_BIRTH_DATE, recrapplication.getBirthDate());
			pstRecrApplication.setLong(FLD_RELIGION_ID, recrapplication.getReligionId());
			pstRecrApplication.setString(FLD_ADDRESS, recrapplication.getAddress());
			pstRecrApplication.setString(FLD_CITY, recrapplication.getCity());
			pstRecrApplication.setInt(FLD_POSTAL_CODE, recrapplication.getPostalCode());
			pstRecrApplication.setString(FLD_PHONE, recrapplication.getPhone());
			pstRecrApplication.setString(FLD_ID_CARD_NUM, recrapplication.getIdCardNum());
			pstRecrApplication.setString(FLD_ASTEK_NUM, recrapplication.getAstekNum());
			pstRecrApplication.setLong(FLD_MARITAL_ID, recrapplication.getMaritalId());
			pstRecrApplication.setString(FLD_PASSPORT_NO, recrapplication.getPassportNo());
			pstRecrApplication.setString(FLD_ISSUE_PLACE, recrapplication.getIssuePlace());
			pstRecrApplication.setDate(FLD_VALID_UNTIL, recrapplication.getValidUntil());
			pstRecrApplication.setInt(FLD_HEIGHT, recrapplication.getHeight());
			pstRecrApplication.setInt(FLD_WEIGHT, recrapplication.getWeight());
			pstRecrApplication.setString(FLD_BLOOD_TYPE, recrapplication.getBloodType());
			pstRecrApplication.setString(FLD_DISTINGUISH_MARKS, recrapplication.getDistinguishMarks());
			pstRecrApplication.setDate(FLD_APPL_DATE, recrapplication.getApplDate());
			pstRecrApplication.setString(FLD_FATHER_NAME, recrapplication.getFatherName());
			pstRecrApplication.setInt(FLD_FATHER_AGE, recrapplication.getFatherAge());
			pstRecrApplication.setString(FLD_FATHER_OCCUPATION, recrapplication.getFatherOccupation());
			pstRecrApplication.setString(FLD_MOTHER_NAME, recrapplication.getMotherName());
			pstRecrApplication.setInt(FLD_MOTHER_AGE, recrapplication.getMotherAge());
			pstRecrApplication.setString(FLD_MOTHER_OCCUPATION, recrapplication.getMotherOccupation());
			pstRecrApplication.setString(FLD_FAMILY_ADDRESS, recrapplication.getFamilyAddress());
			pstRecrApplication.setString(FLD_FAMILY_CITY, recrapplication.getFamilyCity());
			pstRecrApplication.setString(FLD_FAMILY_PHONE, recrapplication.getFamilyPhone());
			pstRecrApplication.setString(FLD_SPOUSE_NAME, recrapplication.getSpouseName());
			pstRecrApplication.setDate(FLD_SPOUSE_BIRTH_DATE, recrapplication.getSpouseBirthDate());
			pstRecrApplication.setString(FLD_SPOUSE_OCCUPATION, recrapplication.getSpouseOccupation());
			pstRecrApplication.setString(FLD_CHILD1_NAME, recrapplication.getChild1Name());
			pstRecrApplication.setDate(FLD_CHILD1_BIRTHDATE, recrapplication.getChild1Birthdate());
			pstRecrApplication.setInt(FLD_CHILD1_SEX, recrapplication.getChild1Sex());
			pstRecrApplication.setString(FLD_CHILD2_NAME, recrapplication.getChild2Name());
			pstRecrApplication.setDate(FLD_CHILD2_BIRTHDATE, recrapplication.getChild2Birthdate());
			pstRecrApplication.setInt(FLD_CHILD2_SEX, recrapplication.getChild2Sex());
			pstRecrApplication.setString(FLD_CHILD3_NAME, recrapplication.getChild3Name());
			pstRecrApplication.setDate(FLD_CHILD3_BIRTHDATE, recrapplication.getChild3Birthdate());
			pstRecrApplication.setInt(FLD_CHILD3_SEX, recrapplication.getChild3Sex());
			pstRecrApplication.setLong(FLD_FNL_POSITION_ID, recrapplication.getFnlPositionId());
			pstRecrApplication.setLong(FLD_FNL_DEPARTMENT_ID, recrapplication.getFnlDepartmentId());
			pstRecrApplication.setLong(FLD_FNL_LEVEL_ID, recrapplication.getFnlLevelId());
			pstRecrApplication.setString(FLD_FNL_MEDICAL_SCHEME, recrapplication.getFnlMedicalScheme());
			pstRecrApplication.setString(FLD_FNL_HOSPITALIZATION, recrapplication.getFnlHospitalization());
			pstRecrApplication.setString(FLD_FNL_ASTEK_DEDUCTION, recrapplication.getFnlAstekDeduction());
			pstRecrApplication.setString(FLD_FNL_BASIC_SALARY, recrapplication.getFnlBasicSalary());
			pstRecrApplication.setString(FLD_FNL_SERVICE_CHARGE, recrapplication.getFnlServiceCharge());
			pstRecrApplication.setString(FLD_FNL_ALLOWANCE, recrapplication.getFnlAllowance());
			pstRecrApplication.setInt(FLD_FNL_ANNUAL_LEAVE, recrapplication.getFnlAnnualLeave());
			pstRecrApplication.setString(FLD_FNL_OTHER_BENEFIT, recrapplication.getFnlOtherBenefit());
			pstRecrApplication.setString(FLD_FNL_PRIVILEGE, recrapplication.getFnlPrivilege());
			pstRecrApplication.setDate(FLD_FNL_COMM_DATE, recrapplication.getFnlCommDate());
			pstRecrApplication.setString(FLD_FNL_PROBATION, recrapplication.getFnlProbation());

			pstRecrApplication.insert(); 
			recrapplication.setOID(pstRecrApplication.getlong(FLD_RECR_APPLICATION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrApplication(0),DBException.UNKNOWN); 
		}
		return recrapplication.getOID();
	}

	public static long updateExc(RecrApplication recrapplication) throws DBException{ 
		try{ 
			if(recrapplication.getOID() != 0){ 
				PstRecrApplication pstRecrApplication = new PstRecrApplication(recrapplication.getOID());

				pstRecrApplication.setString(FLD_POSITION, recrapplication.getPosition());
				pstRecrApplication.setString(FLD_OTHER_POSITION, recrapplication.getOtherPosition());
				pstRecrApplication.setInt(FLD_SALARY_EXP, recrapplication.getSalaryExp());
				pstRecrApplication.setDate(FLD_DATE_AVAILABLE, recrapplication.getDateAvailable());
				pstRecrApplication.setString(FLD_FULL_NAME, recrapplication.getFullName());
				pstRecrApplication.setInt(FLD_SEX, recrapplication.getSex());
				pstRecrApplication.setString(FLD_BIRTH_PLACE, recrapplication.getBirthPlace());
				pstRecrApplication.setDate(FLD_BIRTH_DATE, recrapplication.getBirthDate());
				pstRecrApplication.setLong(FLD_RELIGION_ID, recrapplication.getReligionId());
				pstRecrApplication.setString(FLD_ADDRESS, recrapplication.getAddress());
				pstRecrApplication.setString(FLD_CITY, recrapplication.getCity());
				pstRecrApplication.setInt(FLD_POSTAL_CODE, recrapplication.getPostalCode());
				pstRecrApplication.setString(FLD_PHONE, recrapplication.getPhone());
				pstRecrApplication.setString(FLD_ID_CARD_NUM, recrapplication.getIdCardNum());
				pstRecrApplication.setString(FLD_ASTEK_NUM, recrapplication.getAstekNum());
				pstRecrApplication.setLong(FLD_MARITAL_ID, recrapplication.getMaritalId());
				pstRecrApplication.setString(FLD_PASSPORT_NO, recrapplication.getPassportNo());
				pstRecrApplication.setString(FLD_ISSUE_PLACE, recrapplication.getIssuePlace());
				pstRecrApplication.setDate(FLD_VALID_UNTIL, recrapplication.getValidUntil());
				pstRecrApplication.setInt(FLD_HEIGHT, recrapplication.getHeight());
				pstRecrApplication.setInt(FLD_WEIGHT, recrapplication.getWeight());
				pstRecrApplication.setString(FLD_BLOOD_TYPE, recrapplication.getBloodType());
				pstRecrApplication.setString(FLD_DISTINGUISH_MARKS, recrapplication.getDistinguishMarks());
				pstRecrApplication.setDate(FLD_APPL_DATE, recrapplication.getApplDate());
				pstRecrApplication.setString(FLD_FATHER_NAME, recrapplication.getFatherName());
				pstRecrApplication.setInt(FLD_FATHER_AGE, recrapplication.getFatherAge());
				pstRecrApplication.setString(FLD_FATHER_OCCUPATION, recrapplication.getFatherOccupation());
				pstRecrApplication.setString(FLD_MOTHER_NAME, recrapplication.getMotherName());
				pstRecrApplication.setInt(FLD_MOTHER_AGE, recrapplication.getMotherAge());
				pstRecrApplication.setString(FLD_MOTHER_OCCUPATION, recrapplication.getMotherOccupation());
				pstRecrApplication.setString(FLD_FAMILY_ADDRESS, recrapplication.getFamilyAddress());
				pstRecrApplication.setString(FLD_FAMILY_CITY, recrapplication.getFamilyCity());
				pstRecrApplication.setString(FLD_FAMILY_PHONE, recrapplication.getFamilyPhone());
				pstRecrApplication.setString(FLD_SPOUSE_NAME, recrapplication.getSpouseName());
				pstRecrApplication.setDate(FLD_SPOUSE_BIRTH_DATE, recrapplication.getSpouseBirthDate());
				pstRecrApplication.setString(FLD_SPOUSE_OCCUPATION, recrapplication.getSpouseOccupation());
				pstRecrApplication.setString(FLD_CHILD1_NAME, recrapplication.getChild1Name());
				pstRecrApplication.setDate(FLD_CHILD1_BIRTHDATE, recrapplication.getChild1Birthdate());
				pstRecrApplication.setInt(FLD_CHILD1_SEX, recrapplication.getChild1Sex());
				pstRecrApplication.setString(FLD_CHILD2_NAME, recrapplication.getChild2Name());
				pstRecrApplication.setDate(FLD_CHILD2_BIRTHDATE, recrapplication.getChild2Birthdate());
				pstRecrApplication.setInt(FLD_CHILD2_SEX, recrapplication.getChild2Sex());
				pstRecrApplication.setString(FLD_CHILD3_NAME, recrapplication.getChild3Name());
				pstRecrApplication.setDate(FLD_CHILD3_BIRTHDATE, recrapplication.getChild3Birthdate());
				pstRecrApplication.setInt(FLD_CHILD3_SEX, recrapplication.getChild3Sex());
				pstRecrApplication.setLong(FLD_FNL_POSITION_ID, recrapplication.getFnlPositionId());
				pstRecrApplication.setLong(FLD_FNL_DEPARTMENT_ID, recrapplication.getFnlDepartmentId());
				pstRecrApplication.setLong(FLD_FNL_LEVEL_ID, recrapplication.getFnlLevelId());
				pstRecrApplication.setString(FLD_FNL_MEDICAL_SCHEME, recrapplication.getFnlMedicalScheme());
				pstRecrApplication.setString(FLD_FNL_HOSPITALIZATION, recrapplication.getFnlHospitalization());
				pstRecrApplication.setString(FLD_FNL_ASTEK_DEDUCTION, recrapplication.getFnlAstekDeduction());
				pstRecrApplication.setString(FLD_FNL_BASIC_SALARY, recrapplication.getFnlBasicSalary());
				pstRecrApplication.setString(FLD_FNL_SERVICE_CHARGE, recrapplication.getFnlServiceCharge());
				pstRecrApplication.setString(FLD_FNL_ALLOWANCE, recrapplication.getFnlAllowance());
				pstRecrApplication.setInt(FLD_FNL_ANNUAL_LEAVE, recrapplication.getFnlAnnualLeave());
				pstRecrApplication.setString(FLD_FNL_OTHER_BENEFIT, recrapplication.getFnlOtherBenefit());
				pstRecrApplication.setString(FLD_FNL_PRIVILEGE, recrapplication.getFnlPrivilege());
				pstRecrApplication.setDate(FLD_FNL_COMM_DATE, recrapplication.getFnlCommDate());
				pstRecrApplication.setString(FLD_FNL_PROBATION, recrapplication.getFnlProbation());

				pstRecrApplication.update(); 
				return recrapplication.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrApplication(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecrApplication pstRecrApplication = new PstRecrApplication(oid);
			pstRecrApplication.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecrApplication(0),DBException.UNKNOWN); 
		}
		return oid;
	}

	public static Vector listAll(){ 
		return list(0, 500, "",""); 
	}

	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_RECR_APPLICATION; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				RecrApplication recrapplication = new RecrApplication();
				resultToObject(rs, recrapplication);
				lists.add(recrapplication);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}

	public static void resultToObject(ResultSet rs, RecrApplication recrapplication){
		try{
			recrapplication.setOID(rs.getLong(PstRecrApplication.fieldNames[PstRecrApplication.FLD_RECR_APPLICATION_ID]));
			recrapplication.setPosition(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_POSITION]));
			recrapplication.setOtherPosition(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_OTHER_POSITION]));
			recrapplication.setSalaryExp(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_SALARY_EXP]));
			recrapplication.setDateAvailable(rs.getDate(PstRecrApplication.fieldNames[PstRecrApplication.FLD_DATE_AVAILABLE]));
			recrapplication.setFullName(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FULL_NAME]));
			recrapplication.setSex(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_SEX]));
			recrapplication.setBirthPlace(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_BIRTH_PLACE]));
			recrapplication.setBirthDate(rs.getDate(PstRecrApplication.fieldNames[PstRecrApplication.FLD_BIRTH_DATE]));
			recrapplication.setReligionId(rs.getLong(PstRecrApplication.fieldNames[PstRecrApplication.FLD_RELIGION_ID]));
			recrapplication.setAddress(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_ADDRESS]));
			recrapplication.setCity(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CITY]));
			recrapplication.setPostalCode(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_POSTAL_CODE]));
			recrapplication.setPhone(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_PHONE]));
			recrapplication.setIdCardNum(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_ID_CARD_NUM]));
			recrapplication.setAstekNum(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_ASTEK_NUM]));
			recrapplication.setMaritalId(rs.getLong(PstRecrApplication.fieldNames[PstRecrApplication.FLD_MARITAL_ID]));
			recrapplication.setPassportNo(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_PASSPORT_NO]));
			recrapplication.setIssuePlace(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_ISSUE_PLACE]));
			recrapplication.setValidUntil(rs.getDate(PstRecrApplication.fieldNames[PstRecrApplication.FLD_VALID_UNTIL]));
			recrapplication.setHeight(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_HEIGHT]));
			recrapplication.setWeight(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_WEIGHT]));
			recrapplication.setBloodType(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_BLOOD_TYPE]));
			recrapplication.setDistinguishMarks(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_DISTINGUISH_MARKS]));
			recrapplication.setApplDate(rs.getDate(PstRecrApplication.fieldNames[PstRecrApplication.FLD_APPL_DATE]));
			recrapplication.setFatherName(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FATHER_NAME]));
			recrapplication.setFatherAge(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FATHER_AGE]));
			recrapplication.setFatherOccupation(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FATHER_OCCUPATION]));
			recrapplication.setMotherName(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_MOTHER_NAME]));
			recrapplication.setMotherAge(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_MOTHER_AGE]));
			recrapplication.setMotherOccupation(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_MOTHER_OCCUPATION]));
			recrapplication.setFamilyAddress(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FAMILY_ADDRESS]));
			recrapplication.setFamilyCity(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FAMILY_CITY]));
			recrapplication.setFamilyPhone(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FAMILY_PHONE]));
			recrapplication.setSpouseName(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_SPOUSE_NAME]));
			recrapplication.setSpouseBirthDate(rs.getDate(PstRecrApplication.fieldNames[PstRecrApplication.FLD_SPOUSE_BIRTH_DATE]));
			recrapplication.setSpouseOccupation(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_SPOUSE_OCCUPATION]));
			recrapplication.setChild1Name(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CHILD1_NAME]));
			recrapplication.setChild1Birthdate(rs.getDate(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CHILD1_BIRTHDATE]));
			recrapplication.setChild1Sex(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CHILD1_SEX]));
			recrapplication.setChild2Name(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CHILD2_NAME]));
			recrapplication.setChild2Birthdate(rs.getDate(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CHILD2_BIRTHDATE]));
			recrapplication.setChild2Sex(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CHILD2_SEX]));
			recrapplication.setChild3Name(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CHILD3_NAME]));
			recrapplication.setChild3Birthdate(rs.getDate(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CHILD3_BIRTHDATE]));
			recrapplication.setChild3Sex(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_CHILD3_SEX]));
			recrapplication.setFnlPositionId(rs.getLong(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_POSITION_ID]));
			recrapplication.setFnlDepartmentId(rs.getLong(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_DEPARTMENT_ID]));
			recrapplication.setFnlLevelId(rs.getLong(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_LEVEL_ID]));
			recrapplication.setFnlMedicalScheme(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_MEDICAL_SCHEME]));
			recrapplication.setFnlHospitalization(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_HOSPITALIZATION]));
			recrapplication.setFnlAstekDeduction(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_ASTEK_DEDUCTION]));
			recrapplication.setFnlBasicSalary(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_BASIC_SALARY]));
			recrapplication.setFnlServiceCharge(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_SERVICE_CHARGE]));
			recrapplication.setFnlAllowance(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_ALLOWANCE]));
			recrapplication.setFnlAnnualLeave(rs.getInt(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_ANNUAL_LEAVE]));
			recrapplication.setFnlOtherBenefit(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_OTHER_BENEFIT]));
			recrapplication.setFnlPrivilege(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_PRIVILEGE]));
			recrapplication.setFnlCommDate(rs.getDate(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_COMM_DATE]));
			recrapplication.setFnlProbation(rs.getString(PstRecrApplication.fieldNames[PstRecrApplication.FLD_FNL_PROBATION]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long recrApplicationId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_APPLICATION + " WHERE " + 
						PstRecrApplication.fieldNames[PstRecrApplication.FLD_RECR_APPLICATION_ID] + " = " + recrApplicationId;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}

	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstRecrApplication.fieldNames[PstRecrApplication.FLD_RECR_APPLICATION_ID] + ") FROM " + TBL_HR_RECR_APPLICATION;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			int count = 0;
			while(rs.next()) { count = rs.getInt(1); }

			rs.close();
			return count;
		}catch(Exception e) {
			return 0;
		}finally {
			DBResultSet.close(dbrs);
		}
	}


	/* This method used to find current data */
	public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause){
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   RecrApplication recrapplication = (RecrApplication)list.get(ls);
				   if(oid == recrapplication.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
	/* This method used to find command where current data */
	public static int findLimitCommand(int start, int recordToGet, int vectSize){
		 int cmd = Command.LIST;
		 int mdl = vectSize % recordToGet;
		 vectSize = vectSize + (recordToGet - mdl);
		 if(start == 0)
			 cmd =  Command.FIRST;
		 else{
			 if(start == (vectSize-recordToGet))
				 cmd = Command.LAST;
			 else{
				 start = start + recordToGet;
				 if(start <= (vectSize - recordToGet)){
					 cmd = Command.NEXT;
					 System.out.println("next.......................");
				 }else{
					 start = start - recordToGet;
					 if(start > 0){
						 cmd = Command.PREV; 
						 System.out.println("prev.......................");
					 } 
				 }
			 } 
		 }

		 return cmd;
	}

    /* method checkReligion() dipakai untuk nge-check ada tidaknya RELIGION_ID 
       yang masih terpakai di HR_RECR_APPLICATION 
       method ini dipanggil oleh method PstReligion.checkMaster()
     */
    public static boolean checkReligion(long religionId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_APPLICATION + " WHERE " + 
                            PstRecrApplication.fieldNames[PstRecrApplication.FLD_RELIGION_ID] + " = '" + religionId+"'";

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}

    public static boolean checkMarital(long maritalId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_RECR_APPLICATION + " WHERE " + 
                            PstRecrApplication.fieldNames[PstRecrApplication.FLD_MARITAL_ID] + " = '" + maritalId+"'";

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}
}
