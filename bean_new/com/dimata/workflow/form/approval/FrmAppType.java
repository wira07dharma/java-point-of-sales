/**
 * Created on 	: 3:00 PM
 * @author	    : gedhy
 * @version	    : 01
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

public class FrmAppType extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private AppType appType;

	public static final String FRM_NAME_APPTYPE		=  "FRM_NAME_APPTYPE" ;

	public static final int FRM_FIELD_APPTYPE_OID	=  0 ;
	public static final int FRM_FIELD_NAME			=  1 ;
	public static final int FRM_FIELD_DESCRIPTION	=  2 ;

	public static String[] fieldNames = {
		"FRM_FIELD_APPTYPE_OID",
        "FRM_FIELD_NAME",
		"FRM_FIELD_DESCRIPTION"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
		TYPE_STRING
	} ;

	public FrmAppType(){
	}

	public FrmAppType(AppType appType){
		this.appType = appType;
	}

	public FrmAppType(HttpServletRequest request, AppType appType){
		super(new FrmAppType(appType), request);
		this.appType = appType;
	}

	public String getFormName() { return FRM_NAME_APPTYPE; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public AppType getEntityObject(){ return appType; }

	public void requestEntityObject(AppType appType) {
		try{
			this.requestParam();
			appType.setName(getString(FRM_FIELD_NAME));
			appType.setDescription(getString(FRM_FIELD_DESCRIPTION));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
