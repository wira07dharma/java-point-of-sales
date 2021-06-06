/*
 * Form Name  	:  FrmDocStatus.java 
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

public class FrmDocStatus extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private DocStatus docStatus;

	public static final String FRM_NAME_DOCSTATUS		=  "FRM_NAME_DOCSTATUS" ;

	public static final int FRM_FIELD_DOC_STATUS_OID	=  0 ;
	public static final int FRM_FIELD_TYPE				=  1 ;
	public static final int FRM_FIELD_NAME				=  2 ;
	public static final int FRM_FIELD_DESCRIPTION		=  3 ;

	public static String[] fieldNames = {
		"FRM_FIELD_DOC_STATUS_OID",
        "FRM_FIELD_STATUS_TYPE",
		"FRM_FIELD_STATUS_NAME",
        "FRM_FIELD_STATUS_DESCRIPTION"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,
        TYPE_INT,
		TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING
	} ;

	public FrmDocStatus(){
	}
	public FrmDocStatus(DocStatus docStatus){
		this.docStatus = docStatus;
	}

	public FrmDocStatus(HttpServletRequest request, DocStatus docStatus){
		super(new FrmDocStatus(docStatus), request);
		this.docStatus = docStatus;
	}

	public String getFormName() { return FRM_NAME_DOCSTATUS; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public DocStatus getEntityObject(){ return docStatus; }

	public void requestEntityObject(DocStatus docStatus) {
		try{
			this.requestParam();
			docStatus.setType(getInt(FRM_FIELD_TYPE));
			docStatus.setName(getString(FRM_FIELD_NAME));
			docStatus.setDescription(getString(FRM_FIELD_DESCRIPTION));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
