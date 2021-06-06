/*
 * Form Name  	:  FrmChangesStatus.java 
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

package com.dimata.workflow.form.status;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.workflow.entity.status.*;

public class FrmChangesStatus extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private ChangesStatus changesStatus;

	public static final String FRM_NAME_CHANGESSTATUS		=  "FRM_NAME_CHANGESSTATUS" ;

	public static final int FRM_FIELD_APP_MAPPING_OID			=  0 ;
	public static final int FRM_FIELD_DOC_TYPE_STATUS_OID			=  1 ;

	public static String[] fieldNames = {
		"FRM_FIELD_APP_MAPPING_OID",  "FRM_FIELD_DOC_TYPE_STATUS_OID"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG
	} ;

	public FrmChangesStatus(){
	}
	public FrmChangesStatus(ChangesStatus changesStatus){
		this.changesStatus = changesStatus;
	}

	public FrmChangesStatus(HttpServletRequest request, ChangesStatus changesStatus){
		super(new FrmChangesStatus(changesStatus), request);
		this.changesStatus = changesStatus;
	}

	public String getFormName() { return FRM_NAME_CHANGESSTATUS; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public ChangesStatus getEntityObject(){ return changesStatus; }

	public void requestEntityObject(ChangesStatus changesStatus) {
		try{
			this.requestParam();
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
