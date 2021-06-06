/* 
 * Form Name  	:  FrmMemberRegistrationHistory.java 
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

package com.dimata.posbo.form.masterdata;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmMemberRegistrationHistory extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private MemberRegistrationHistory memberRegistrationHistory;

	public static final String FRM_NAME_MEMBERREGISTRATIONHISTORY		=  "FRM_NAME_MEMBERREGISTRATIONHISTORY" ;

	public static final int FRM_FIELD_MEMBER_REGISTRATION_HISTORY_ID			=  0 ;
	public static final int FRM_FIELD_MEMBER_ID			=  1 ;
	public static final int FRM_FIELD_REGISTRATION_DATE			=  2 ;
	public static final int FRM_FIELD_VALID_START_DATE			=  3 ;
	public static final int FRM_FIELD_VALID_EXPIRED_DATE			=  4 ;

	public static String[] fieldNames = {
		"FRM_FIELD_MEMBER_REGISTRATION_HISTORY_ID",  "FRM_FIELD_MEMBER_ID",
		"FRM_FIELD_REGISTRATION_DATE",  "FRM_FIELD_VALID_START_DATE",
		"FRM_FIELD_VALID_EXPIRED_DATE"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG,
		TYPE_DATE,  TYPE_DATE,
		TYPE_DATE
	} ;

	public FrmMemberRegistrationHistory(){
	}
	public FrmMemberRegistrationHistory(MemberRegistrationHistory memberRegistrationHistory){
		this.memberRegistrationHistory = memberRegistrationHistory;
	}

	public FrmMemberRegistrationHistory(HttpServletRequest request, MemberRegistrationHistory memberRegistrationHistory){
		super(new FrmMemberRegistrationHistory(memberRegistrationHistory), request);
		this.memberRegistrationHistory = memberRegistrationHistory;
	}

	public String getFormName() { return FRM_NAME_MEMBERREGISTRATIONHISTORY; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public MemberRegistrationHistory getEntityObject(){ return memberRegistrationHistory; }

	public void requestEntityObject(MemberRegistrationHistory memberRegistrationHistory) {
		try{
			this.requestParam();
			memberRegistrationHistory.setMemberId(getLong(FRM_FIELD_MEMBER_ID));
			memberRegistrationHistory.setRegistrationDate(getDate(FRM_FIELD_REGISTRATION_DATE));
			memberRegistrationHistory.setValidStartDate(getDate(FRM_FIELD_VALID_START_DATE));
			memberRegistrationHistory.setValidExpiredDate(getDate(FRM_FIELD_VALID_EXPIRED_DATE));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
