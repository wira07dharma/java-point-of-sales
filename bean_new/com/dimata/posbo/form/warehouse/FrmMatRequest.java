/* 
 * Form Name  	:  FrmMaterialRequest.java 
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

package com.dimata.posbo.form.warehouse;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.warehouse.*;

public class FrmMatRequest extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private MatDispatch materialDispatch;

	public static final String FRM_NAME_MATERIALREQUEST		=  "FRM_NAME_MATERIALREQUEST" ;

	public static final int FRM_FIELD_MAT_DISPATCH_ID			=  0 ;
	public static final int FRM_FIELD_DISPATCH_FROM			=  1 ;
	public static final int FRM_FIELD_DISPATCH_TO			=  2 ;
	public static final int FRM_FIELD_DISPATCH_TYPE			=  3 ;
	public static final int FRM_FIELD_DFM_DATE			=  4 ;
	public static final int FRM_FIELD_DISPATCH_CODE			=  5 ;
	public static final int FRM_FIELD_DEPARTMENT_HEAD_APPROVE			=  6 ;
	public static final int FRM_FIELD_STORE_ROOM_APPROVE			=  7 ;
	public static final int FRM_FIELD_DF_STATUS			=  8 ;

	public static String[] fieldNames = {
		"FRM_FIELD_MAT_DISPATCH_ID",  "FRM_FIELD_DISPATCH_FROM",
		"FRM_FIELD_DISPATCH_TO",  "FRM_FIELD_DISPATCH_TYPE",
		"FRM_FIELD_DFM_DATE",  "FRM_FIELD_DISPATCH_CODE",
		"FRM_FIELD_DEPARTMENT_HEAD_APPROVE",  "FRM_FIELD_STORE_ROOM_APPROVE",
		"FRM_FIELD_DF_STATUS"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG + ENTRY_REQUIRED,
		TYPE_LONG + ENTRY_REQUIRED,  TYPE_INT,
		TYPE_DATE + ENTRY_REQUIRED,  TYPE_STRING,
		TYPE_INT,  TYPE_INT,
		TYPE_INT
	} ;

	public FrmMatRequest(){
	}
	public FrmMatRequest(MatDispatch materialDispatch){
		this.materialDispatch = materialDispatch;
	}

	public FrmMatRequest(HttpServletRequest request, MatDispatch materialDispatch){
		super(new FrmMatRequest(materialDispatch), request);
		this.materialDispatch = materialDispatch;
	}

	public String getFormName() { return FRM_NAME_MATERIALREQUEST; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public MatDispatch getEntityObject(){ return materialDispatch; }

	public void requestEntityObject(MatDispatch materialDispatch) {
		try{
			this.requestParam();
			//materialDispatch.setDispatchFrom(getLong(FRM_FIELD_DISPATCH_FROM));
			materialDispatch.setDispatchTo(getLong(FRM_FIELD_DISPATCH_TO));
			//materialDispatch.setDispatchType(getInt(FRM_FIELD_DISPATCH_TYPE));
			//materialDispatch.setDfmDate(getDate(FRM_FIELD_DFM_DATE));
			//materialDispatch.setDispatchCode(getString(FRM_FIELD_DISPATCH_CODE));
			//materialDispatch.setDepartmentHeadApprove(getInt(FRM_FIELD_DEPARTMENT_HEAD_APPROVE));
			//materialDispatch.setStoreRoomApprove(getInt(FRM_FIELD_STORE_ROOM_APPROVE));
			//materialDispatch.setDfStatus(getInt(FRM_FIELD_DF_STATUS));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
