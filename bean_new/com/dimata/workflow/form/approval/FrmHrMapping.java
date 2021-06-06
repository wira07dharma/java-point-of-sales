/* 
 * Form Name  	:  FrmHrMapping.java 
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

package com.dimata.workflow.form.approval;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.workflow.entity.approval.*;

public class FrmHrMapping extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private HrMapping hrMapping;

	public static final String FRM_NAME_HRMAPPING		=  "FRM_NAME_HRMAPPING" ;

	public static final int FRM_FIELD_HR_MAPPING_OID			=  0 ;
	public static final int FRM_FIELD_APP_MAPPING_OID			=  1 ;
	public static final int FRM_FIELD_DEPARTMENT_ID			=  2 ;
	public static final int FRM_FIELD_SECTION_ID			=  3 ;
	public static final int FRM_FIELD_POSITION_ID			=  4 ;

	public static String[] fieldNames = {
		"FRM_FIELD_HR_MAPPING_OID",  "FRM_FIELD_APP_MAPPING_OID",
		"FRM_FIELD_DEPARTMENT_ID",  "FRM_FIELD_SECTION_ID",
		"FRM_FIELD_POSITION_ID"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG,
		TYPE_LONG,  TYPE_LONG,
		TYPE_LONG
	} ;

	public FrmHrMapping(){
	}
	public FrmHrMapping(HrMapping hrMapping){
		this.hrMapping = hrMapping;
	}

	public FrmHrMapping(HttpServletRequest request, HrMapping hrMapping){
		super(new FrmHrMapping(hrMapping), request);
		this.hrMapping = hrMapping;
	}

	public String getFormName() { return FRM_NAME_HRMAPPING; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public HrMapping getEntityObject(){ return hrMapping; }

	public void requestEntityObject(HrMapping hrMapping) {
		try{
			this.requestParam();
			hrMapping.setAppMappingOid(getLong(FRM_FIELD_APP_MAPPING_OID));
			hrMapping.setDepartmentId(getLong(FRM_FIELD_DEPARTMENT_ID));
			hrMapping.setSectionId(getLong(FRM_FIELD_SECTION_ID));
			hrMapping.setPositionId(getLong(FRM_FIELD_POSITION_ID));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
