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

public class FrmDocAppDetail extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private DocAppDetail docAppDetail;

	public static final String FRM_NAME_DOCAPPDETAIL		=  "FRM_NAME_DOCAPPDETAIL" ;

	public static final int FRM_FIELD_DOCAPP_DETAIL_OID		=  0 ;
	public static final int FRM_FIELD_EMPLOYEE_OID			=  1 ;
	public static final int FRM_FIELD_DOCAPP_MAIN_OID		=  2 ;
	public static final int FRM_FIELD_APP_MAPPING_OID		=  3 ;
	public static final int FRM_FIELD_APP_STATUS			=  4 ;

	public static String[] fieldNames = {
		"FRM_FIELD_DOCAPP_DETAIL_OID",
        "FRM_FIELD_EMPLOYEE_OID",
		"FRM_FIELD_DOCAPP_MAIN_OID",
        "FRM_FIELD_APP_MAPPING_OID",
		"FRM_FIELD_APP_STATUS"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,
        TYPE_LONG,
		TYPE_LONG,
        TYPE_LONG,
		TYPE_INT
	} ;

	public FrmDocAppDetail(){
	}

	public FrmDocAppDetail(DocAppDetail docAppDetail){
		this.docAppDetail = docAppDetail;
	}

	public FrmDocAppDetail(HttpServletRequest request, DocAppDetail docAppDetail){
		super(new FrmDocAppDetail(docAppDetail), request);
		this.docAppDetail = docAppDetail;
	}

	public String getFormName() { return FRM_NAME_DOCAPPDETAIL; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public DocAppDetail getEntityObject(){ return docAppDetail; }

	public void requestEntityObject(DocAppDetail docAppDetail) {
		try{
			this.requestParam();
			docAppDetail.setEmployeeOid(getLong(FRM_FIELD_EMPLOYEE_OID));
			docAppDetail.setDocappMainOid(getLong(FRM_FIELD_DOCAPP_MAIN_OID));
			docAppDetail.setAppMappingOid(getLong(FRM_FIELD_APP_MAPPING_OID));
			docAppDetail.setAppStatus(getInt(FRM_FIELD_APP_STATUS));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
