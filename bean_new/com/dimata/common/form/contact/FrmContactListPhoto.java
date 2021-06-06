/* 
 * Form Name  	:  FrmContactListPhoto.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya 
 * @version  	: 01 
 */
 
/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.form.contact;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*; 
/* project package */
import com.dimata.common.entity.contact.*;

public class FrmContactListPhoto extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private ContactListPhoto contactListPhoto;

	public static final String FRM_NAME_CONTACTLISTPHOTO		=  "FRM_NAME_CONTACTLISTPHOTO" ;

	public static final int FRM_FIELD_CONTACT_ID			=  0 ;
	public static final int FRM_FIELD_CONTACT_PHOTO			=  1 ;

	public static String[] fieldNames = {
		"FRM_FIELD_CONTACT_ID",  "FRM_FIELD_CONTACT_PHOTO"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_STRING
	} ;

	public FrmContactListPhoto(){
	}
	public FrmContactListPhoto(ContactListPhoto contactListPhoto){
		this.contactListPhoto = contactListPhoto;
	}

	public FrmContactListPhoto(HttpServletRequest request, ContactListPhoto contactListPhoto){
		super(new FrmContactListPhoto(contactListPhoto), request);
		this.contactListPhoto = contactListPhoto;
	}

	public String getFormName() { return FRM_NAME_CONTACTLISTPHOTO; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public ContactListPhoto getEntityObject(){ return contactListPhoto; }

	public void requestEntityObject(ContactListPhoto contactListPhoto) {
		try{
			this.requestParam();
			contactListPhoto.setContactPhoto(getString(FRM_FIELD_CONTACT_PHOTO));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
