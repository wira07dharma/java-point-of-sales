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

public class FrmDocAppMain extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private DocAppMain docAppMain;

	public static final String FRM_NAME_DOCAPPMAIN		=  "FRM_NAME_DOCAPPMAIN" ;

	public static final int FRM_FIELD_DOCAPP_MAIN_OID		=  0 ;
	public static final int FRM_FIELD_DOCUMENT_OID			=  1 ;
	public static final int FRM_FIELD_DOCTYPE_TYPE			=  2 ;

	public static String[] fieldNames = {
		"FRM_FIELD_DOCAPP_MAIN_OID",
        "FRM_FIELD_DOCUMENT_OID",
		"FRM_FIELD_DOCTYPE_TYPE"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,
        TYPE_LONG,
		TYPE_INT
	} ;

	public FrmDocAppMain(){
	}

	public FrmDocAppMain(DocAppMain docAppMain){
		this.docAppMain = docAppMain;
	}

	public FrmDocAppMain(HttpServletRequest request, DocAppMain docAppMain){
		super(new FrmDocAppMain(docAppMain), request);
		this.docAppMain = docAppMain;
	}

	public String getFormName() { return FRM_NAME_DOCAPPMAIN; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public DocAppMain getEntityObject(){ return docAppMain; }

	public void requestEntityObject(DocAppMain docAppMain) {
		try{
			this.requestParam();
			docAppMain.setDocumentOid(getLong(FRM_FIELD_DOCUMENT_OID));
			docAppMain.setDoctypeType(getInt(FRM_FIELD_DOCTYPE_TYPE));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
