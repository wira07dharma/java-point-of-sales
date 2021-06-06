/* 
 * Form Name  	:  FrmDocAppNote.java 
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

public class FrmDocAppNote extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private DocAppNote docAppNote;

	public static final String FRM_NAME_DOCAPPNOTE		=  "FRM_NAME_DOCAPPNOTE" ;

	public static final int FRM_FIELD_DOC_APP_NOTE_OID			=  0 ;
	public static final int FRM_FIELD_DOCAPP_MAIN_OID			=  1 ;
	public static final int FRM_FIELD_NOTE_DATE			=  2 ;
	public static final int FRM_FIELD_DESCRIPTION			=  3 ;

	public static String[] fieldNames = {
		"FRM_FIELD_DOC_APP_NOTE_OID",  "FRM_FIELD_DOCAPP_MAIN_OID",
		"FRM_FIELD_NOTE_DATE",  "FRM_FIELD_DESCRIPTION"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG,
		TYPE_DATE,  TYPE_STRING
	} ;

	public FrmDocAppNote(){
	}
	public FrmDocAppNote(DocAppNote docAppNote){
		this.docAppNote = docAppNote;
	}

	public FrmDocAppNote(HttpServletRequest request, DocAppNote docAppNote){
		super(new FrmDocAppNote(docAppNote), request);
		this.docAppNote = docAppNote;
	}

	public String getFormName() { return FRM_NAME_DOCAPPNOTE; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public DocAppNote getEntityObject(){ return docAppNote; }

	public void requestEntityObject(DocAppNote docAppNote) {
		try{
			this.requestParam();
			docAppNote.setDocappMainOid(getLong(FRM_FIELD_DOCAPP_MAIN_OID));
			docAppNote.setNoteDate(getDate(FRM_FIELD_NOTE_DATE));
			docAppNote.setDescription(getString(FRM_FIELD_DESCRIPTION));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
