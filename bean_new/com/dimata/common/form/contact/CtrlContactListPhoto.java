/* 
 * Ctrl Name  		:  CtrlContactListPhoto.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  		: karya 
 * @version  		: 01 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.form.contact;

/* java package */ 
import javax.servlet.http.*; 
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.DBException;
/* project package */
//import com.dimata.prochain02.db.*;
import com.dimata.common.entity.contact.*;
import com.dimata.common.session.contact.*;

public class CtrlContactListPhoto extends Control implements I_Language 
{
	public static int RSLT_OK = 0;
	public static int RSLT_UNKNOWN_ERROR = 1;
	public static int RSLT_EST_CODE_EXIST = 2;
	public static int RSLT_FORM_INCOMPLETE = 3;

	public static String[][] resultText = {
		{"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
		{"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
	};

	private int start;
	private String msgString;
	private ContactListPhoto contactListPhoto;
	private PstContactListPhoto pstContactListPhoto;
	private FrmContactListPhoto frmContactListPhoto;
	int language = LANGUAGE_DEFAULT;

	public CtrlContactListPhoto(HttpServletRequest request){
		msgString = "";
		contactListPhoto = new ContactListPhoto();
		try{
			pstContactListPhoto = new PstContactListPhoto(0);
		}catch(Exception e){;}
		frmContactListPhoto = new FrmContactListPhoto(request, contactListPhoto);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmContactListPhoto.addError(frmContactListPhoto.FRM_FIELD_CONTACT_ID, resultText[language][RSLT_EST_CODE_EXIST] );
				return resultText[language][RSLT_EST_CODE_EXIST];
			default:
				return resultText[language][RSLT_UNKNOWN_ERROR]; 
		}
	}

	private int getControlMsgId(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				return RSLT_EST_CODE_EXIST;
			default:
				return RSLT_UNKNOWN_ERROR;
		}
	}

	public int getLanguage(){ return language; }

	public void setLanguage(int language){ this.language = language; }

	public ContactListPhoto getContactListPhoto() { return contactListPhoto; } 

	public FrmContactListPhoto getForm() { return frmContactListPhoto; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidContactListPhoto){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidContactListPhoto != 0){
					try{
						contactListPhoto = PstContactListPhoto.fetchExc(oidContactListPhoto);
					}catch(Exception exc){
					}
				}

				frmContactListPhoto.requestEntityObject(contactListPhoto);

				if(frmContactListPhoto.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(contactListPhoto.getOID()==0){
					try{
						long oid = pstContactListPhoto.insertExc(this.contactListPhoto);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstContactListPhoto.updateExc(this.contactListPhoto);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidContactListPhoto != 0) {
					try {
						contactListPhoto = PstContactListPhoto.fetchExc(oidContactListPhoto);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidContactListPhoto != 0) {
					try {
						contactListPhoto = PstContactListPhoto.fetchExc(oidContactListPhoto);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;
			case Command.POST :
				if (oidContactListPhoto != 0) {
					try{
						int errCode = SessContactListPhotoPict.deleteImage(oidContactListPhoto);
						contactListPhoto = PstContactListPhoto.fetchExc(oidContactListPhoto);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidContactListPhoto != 0){
					try{
						long oid = PstContactListPhoto.deleteExc(oidContactListPhoto);
						if(oid!=0){
							msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
							excCode = RSLT_OK;
						}else{
							msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
							excCode = RSLT_FORM_INCOMPLETE;
						}
					}catch(Exception exc){	
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			default :

		}
		return rsCode;
	}
}
