/*
 * Form Name  	:  FrmSrcAppDocMapping.java 
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

package com.dimata.workflow.form.search;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*;

/* qdep package */ 
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.workflow.entity.search.*;

public class FrmSrcAppMapping extends FRMHandler implements I_FRMInterface, I_FRMType
{
	private SrcAppMapping srcAppMapping;

	public static final String FRM_NAME_SRCAPPMAPPING	=  "FRM_NAME_SRCAPPMAPPING" ;

	public static final int FRM_FIELD_SYSTYPE				=  0 ;
	public static final int FRM_FIELD_APPTITLE				=  1 ;
	public static final int FRM_FIELD_APPTYPE_OID			=  2 ;
	public static final int FRM_FIELD_DOCTYPE_TYPE			=  3 ;
	public static final int FRM_FIELD_DEPARTMENT_OID		=  4 ;
	public static final int FRM_FIELD_SECTION_OID			=  5 ;
	public static final int FRM_FIELD_POSITION_OID			=  6 ;
	public static final int FRM_FIELD_SORT_BY				=  7 ;

	public static String[] fieldNames = {
        "FRM_FIELD_SYSTYPE",
        "FRM_FIELD_APPTITLE",
        "FRM_FIELD_APPTYPE_OID",
		"FRM_FIELD_DOCTYPE_TYPE",
		"FRM_FIELD_DEPARTMENT",
		"FRM_FIELD_SECTION",
		"FRM_FIELD_POSITION",
		"FRM_FIELD_SORTBY"
	} ;

	public static int[] fieldTypes = {
		TYPE_INT,
        TYPE_STRING,
        TYPE_LONG,
		TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT
	} ;

	public FrmSrcAppMapping(){
	}

	public FrmSrcAppMapping(SrcAppMapping srcAppMapping){
		this.srcAppMapping = srcAppMapping;
	}

	public FrmSrcAppMapping(HttpServletRequest request, SrcAppMapping srcAppMapping){
		super(new FrmSrcAppMapping(srcAppMapping), request);
		this.srcAppMapping = srcAppMapping;
	}

	public String getFormName() { return FRM_NAME_SRCAPPMAPPING; }

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcAppMapping getEntityObject(){ return srcAppMapping; }

	public void requestEntityObject(SrcAppMapping srcAppMapping) {
		try{
			this.requestParam();
			srcAppMapping.setSystemType(getInt(FRM_FIELD_SYSTYPE));
			srcAppMapping.setAppTitle(getString(FRM_FIELD_APPTITLE));
			srcAppMapping.setAppTypeOid(getLong(FRM_FIELD_APPTYPE_OID));
			srcAppMapping.setDocTypeType(getInt(FRM_FIELD_DOCTYPE_TYPE));
			srcAppMapping.setDepartmentOid(getLong(FRM_FIELD_DEPARTMENT_OID));
			srcAppMapping.setSectionId(getLong(FRM_FIELD_SECTION_OID));
			srcAppMapping.setPositionOid(getLong(FRM_FIELD_POSITION_OID));
			srcAppMapping.setSortBy(getInt(FRM_FIELD_SORT_BY));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}

