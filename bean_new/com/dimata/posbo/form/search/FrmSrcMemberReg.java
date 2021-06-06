/* 
 * Form Name  	:  FrmSrcMemberReg.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.posbo.form.search;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.search.*;

public class FrmSrcMemberReg extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcMemberReg srcMemberReg;

	public static final String FRM_NAME_SRCMEMBERREG		=  "FRM_NAME_SRCMEMBERREG" ;

	public static final int FRM_FIELD_BARCODE			=  0 ;
	public static final int FRM_FIELD_NAME			=  1 ;
	public static final int FRM_FIELD_GROUPMEMBER			=  2 ;
	public static final int FRM_FIELD_STATUS			=  3 ;
        public static final int FRM_FIELD_ALL_BIRTHDATE			=  4 ;
        public static final int FRM_FIELD_ALL_REGDATE			=  5 ;
        public static final int FRM_FIELD_RELIGION			=  6 ;
        public static final int FRM_FIELD_BIRTH_DATE_FROM		=  7 ;
        public static final int FRM_FIELD_BIRTH_DATE_TO 		=  8 ;
        public static final int FRM_FIELD_REG_DATE_FROM 		=  9 ;
        public static final int FRM_FIELD_REG_DATE_TO    		=  10 ;
        public static final int FRM_FIELD_SORT_BY       		=  11 ;
        public static final int FRM_FIELD_SUPPLIER_CODE       		=  12 ;
        public static final int FRM_FIELD_CONTACT_PERSON       		=  13 ;
        public static final int FRM_FIELD_COMPANY_NAME       		=  14 ;
        public static final int FRM_FIELD_CODE                          =15;

	public static String[] fieldNames = {
		"FRM_FIELD_BARCODE",  "FRM_FIELD_NAME",
		"FRM_FIELD_GROUPMEMBER",  "FRM_FIELD_STATUS",
                "FRM_FIELD_ALL_BIRTHDATE","FRM_FIELD_ALL_REGDATE",
                "FRM_FIELD_RELIGION","FRM_FIELD_BIRTH_DATE_FROM",
                "FRM_FIELD_BIRTH_DATE_TO","FRM_FIELD_REG_DATE_FROM",
                "FRM_FIELD_REG_DATE_TO","FRM_FIELD_SORT_BY",
                "FRM_FIELD_SUPPLIER_CODE","FRM_FIELD_CONTACT_PERSON",
                "FRM_FIELD_COMPANY_NAME",
                "FRM_FIELD_CODE"
	} ;

	public static int[] fieldTypes = {
		TYPE_STRING,  TYPE_STRING,
		TYPE_LONG,  TYPE_INT,
                TYPE_BOOL, TYPE_BOOL,
                TYPE_LONG, TYPE_DATE,
                TYPE_DATE, TYPE_DATE,
                TYPE_DATE, TYPE_INT,
                TYPE_STRING,TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING
	} ;

	public FrmSrcMemberReg(){
	}
	public FrmSrcMemberReg(SrcMemberReg srcMemberReg){
		this.srcMemberReg = srcMemberReg;
	}

	public FrmSrcMemberReg(HttpServletRequest request, SrcMemberReg srcMemberReg){
		super(new FrmSrcMemberReg(srcMemberReg), request);
		this.srcMemberReg = srcMemberReg;
	}

	public String getFormName() { return FRM_NAME_SRCMEMBERREG; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcMemberReg getEntityObject(){ return srcMemberReg; }

	public void requestEntityObject(SrcMemberReg srcMemberReg) {
		try{
			this.requestParam();
			srcMemberReg.setBarcode(getString(FRM_FIELD_BARCODE));
			srcMemberReg.setName(getString(FRM_FIELD_NAME));
			srcMemberReg.setGroupmember(getLong(FRM_FIELD_GROUPMEMBER));
			srcMemberReg.setStatus(getInt(FRM_FIELD_STATUS));
                        srcMemberReg.setAllBirthDate(getBoolean(FRM_FIELD_ALL_BIRTHDATE));
                        srcMemberReg.setAllRegDate(getBoolean(FRM_FIELD_ALL_REGDATE));
                        srcMemberReg.setBirthDateFrom(getDate(FRM_FIELD_BIRTH_DATE_FROM));
                        srcMemberReg.setBirthDateTo(getDate(FRM_FIELD_BIRTH_DATE_TO));
                        srcMemberReg.setRegDateFrom(getDate(FRM_FIELD_REG_DATE_FROM));
                        srcMemberReg.setRegDateTo(getDate(FRM_FIELD_REG_DATE_TO));
                        srcMemberReg.setReligion(getLong(FRM_FIELD_RELIGION));
                        srcMemberReg.setSortBy(getInt(FRM_FIELD_SORT_BY));
                        srcMemberReg.setCodeSupplier(getString(FRM_FIELD_SUPPLIER_CODE));
                        srcMemberReg.setCompanyName(getString(FRM_FIELD_COMPANY_NAME));
                        srcMemberReg.setContactPerson(getString(FRM_FIELD_CONTACT_PERSON));
                        srcMemberReg.setMemberCode(getString(FRM_FIELD_CODE));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
