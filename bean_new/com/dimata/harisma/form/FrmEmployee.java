/* 

 * Form Name  	:  FrmEmployee.java 

 * Created on 	:  [date] [time] AM/PM 

 * 

 * @author  	: karya 

 * @version  	: 01 

 */



/*******************************************************************

 * Class Description 	: [project description ... ] 

 * Imput Parameters 	: [input parameter ...] 

 * Output 		: [output ...] 

 *******************************************************************/



package com.dimata.harisma.form;



/* java package */ 

import java.io.*; 

import java.util.*; 

import javax.servlet.*;

import javax.servlet.http.*; 

/* qdep package */ 

import com.dimata.qdep.form.*;

/* project package */

import com.dimata.harisma.entity.employee.*;



public class FrmEmployee extends FRMHandler implements I_FRMInterface, I_FRMType 

{

	private Employee employee;



	public static final String FRM_NAME_EMPLOYEE		=  "FRM_NAME_EMPLOYEE" ;



	public static final int FRM_FIELD_EMPLOYEE_ID			=  0 ;

	public static final int FRM_FIELD_DEPARTMENT_ID			=  1 ;

	public static final int FRM_FIELD_POSITION_ID			=  2 ;

	public static final int FRM_FIELD_SECTION_ID			=  3 ;

	public static final int FRM_FIELD_EMPLOYEE_NUM			=  4 ;

	public static final int FRM_FIELD_EMP_CATEGORY_ID			=  5 ;

	public static final int FRM_FIELD_LEVEL_ID			=  6 ;

	public static final int FRM_FIELD_FULL_NAME			=  7 ;

	public static final int FRM_FIELD_ADDRESS			=  8 ;

	public static final int FRM_FIELD_PHONE			=  9 ;

	public static final int FRM_FIELD_HANDPHONE			=  10 ;

	public static final int FRM_FIELD_POSTAL_CODE			=  11 ;

	public static final int FRM_FIELD_SEX			=  12 ;

	public static final int FRM_FIELD_BIRTH_PLACE			=  13 ;

	public static final int FRM_FIELD_BIRTH_DATE			=  14 ;

	public static final int FRM_FIELD_RELIGION_ID			=  15 ;

	public static final int FRM_FIELD_BLOOD_TYPE			=  16 ;

	public static final int FRM_FIELD_ASTEK_NUM			=  17 ;

	public static final int FRM_FIELD_ASTEK_DATE			=  18 ;

	public static final int FRM_FIELD_MARITAL_ID			=  19 ;

	public static final int FRM_FIELD_LOCKER_ID			=  20 ;

	public static final int FRM_FIELD_COMMENCING_DATE			=  21 ;

	public static final int FRM_FIELD_RESIGNED			=  22 ;

	public static final int FRM_FIELD_RESIGNED_DATE			=  23 ;

	public static final int FRM_FIELD_BASIC_SALARY			=  24 ;
	
	public static final int FRM_FIELD_COMPANY_ID			=  25 ;



	public static String[] fieldNames = {

            "FRM_FIELD_EMPLOYEE_ID",  
            "FRM_FIELD_DEPARTMENT_ID",
            "FRM_FIELD_POSITION_ID",  
            "FRM_FIELD_SECTION_ID",
            "FRM_FIELD_EMPLOYEE_NUM",  
            "FRM_FIELD_EMP_CATEGORY_ID",
            "FRM_FIELD_LEVEL_ID",  
            "FRM_FIELD_FULL_NAME",
            "FRM_FIELD_ADDRESS",  
            "FRM_FIELD_PHONE",
            "FRM_FIELD_HANDPHONE",  
            "FRM_FIELD_POSTAL_CODE",
            "FRM_FIELD_SEX",  
            "FRM_FIELD_BIRTH_PLACE",
            "FRM_FIELD_BIRTH_DATE",  
            "FRM_FIELD_RELIGION_ID",
            "FRM_FIELD_BLOOD_TYPE",  
            "FRM_FIELD_ASTEK_NUM",
            "FRM_FIELD_ASTEK_DATE",  
            "FRM_FIELD_MARITAL_ID",
            "FRM_FIELD_LOCKER_ID",  
            "FRM_FIELD_COMMENCING_DATE",
            "FRM_FIELD_RESIGNED",  
            "FRM_FIELD_RESIGNED_DATE",
            "FRM_FIELD_BASIC_SALARY",
			"FRM_FIELD_COMPANY_ID"
	} ;



	public static int[] fieldTypes = {

		TYPE_LONG,  
                TYPE_LONG,
		TYPE_LONG,  
                TYPE_LONG,
		TYPE_STRING ,  
                TYPE_LONG,
		TYPE_LONG,  
                TYPE_STRING,
		TYPE_STRING,  
                TYPE_STRING,
		TYPE_STRING,  
                TYPE_INT,
		TYPE_INT,  
                TYPE_STRING,
		TYPE_DATE,  
                TYPE_LONG,
		TYPE_STRING ,  
                TYPE_STRING,
		TYPE_DATE,  
                TYPE_LONG,
		TYPE_LONG,  
                TYPE_DATE,
		TYPE_INT,  
                TYPE_DATE,
                TYPE_FLOAT,
				TYPE_FLOAT
	} ;



	public FrmEmployee(){

	}

	public FrmEmployee(Employee employee){

		this.employee = employee;

	}



	public FrmEmployee(HttpServletRequest request, Employee employee){

		super(new FrmEmployee(employee), request);

		this.employee = employee;

	}



	public String getFormName() { return FRM_NAME_EMPLOYEE; } 



	public int[] getFieldTypes() { return fieldTypes; }



	public String[] getFieldNames() { return fieldNames; } 



	public int getFieldSize() { return fieldNames.length; } 



	public Employee getEntityObject(){ return employee; }



	public void requestEntityObject(Employee employee) {

		try{

			this.requestParam();

			employee.setDepartmentId(getLong(FRM_FIELD_DEPARTMENT_ID));

			employee.setPositionId(getLong(FRM_FIELD_POSITION_ID));

			employee.setSectionId(getLong(FRM_FIELD_SECTION_ID));

			employee.setEmployeeNum(getString(FRM_FIELD_EMPLOYEE_NUM));

			employee.setEmpCategoryId(getLong(FRM_FIELD_EMP_CATEGORY_ID));

			employee.setLevelId(getLong(FRM_FIELD_LEVEL_ID));

			employee.setFullName(getString(FRM_FIELD_FULL_NAME));

			employee.setAddress(getString(FRM_FIELD_ADDRESS));

			employee.setPhone(getString(FRM_FIELD_PHONE));

			employee.setHandphone(getString(FRM_FIELD_HANDPHONE));

			employee.setPostalCode(getInt(FRM_FIELD_POSTAL_CODE));

			employee.setSex(getInt(FRM_FIELD_SEX));

			employee.setBirthPlace(getString(FRM_FIELD_BIRTH_PLACE));

			employee.setBirthDate(getDate(FRM_FIELD_BIRTH_DATE));

			employee.setReligionId(getLong(FRM_FIELD_RELIGION_ID));

			employee.setBloodType(getString(FRM_FIELD_BLOOD_TYPE));

			employee.setAstekNum(getString(FRM_FIELD_ASTEK_NUM));

			employee.setAstekDate(getDate(FRM_FIELD_ASTEK_DATE));

			employee.setMaritalId(getLong(FRM_FIELD_MARITAL_ID));

			employee.setLockerId(getLong(FRM_FIELD_LOCKER_ID));

			employee.setCommencingDate(getDate(FRM_FIELD_COMMENCING_DATE));

			employee.setResigned(getInt(FRM_FIELD_RESIGNED));

			employee.setResignedDate(getDate(FRM_FIELD_RESIGNED_DATE));

            employee.setBasicSalary(getDouble(FRM_FIELD_BASIC_SALARY));
			
			employee.setCompanyId(getLong(FRM_FIELD_COMPANY_ID));

		}catch(Exception e){

			System.out.println("Error on requestEntityObject : "+e.toString());

		}

	}

}

