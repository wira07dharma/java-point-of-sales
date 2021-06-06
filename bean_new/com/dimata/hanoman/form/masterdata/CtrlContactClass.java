/*

 * Ctrl Name  		:  CtrlContactClass.java 

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



package com.dimata.hanoman.form.masterdata;



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

import com.dimata.hanoman.entity.masterdata.*;


public class CtrlContactClass extends Control implements I_Language 

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

	private ContactClass contactClass;

	private PstContactClass pstContactClass;

	private FrmContactClass frmContactClass;

	int language = LANGUAGE_DEFAULT;



	public CtrlContactClass(HttpServletRequest request){

		msgString = "";

		contactClass = new ContactClass();

		try{

			pstContactClass = new PstContactClass(0);

		}catch(Exception e){;}

		frmContactClass = new FrmContactClass(request, contactClass);

	}



	private String getSystemMessage(int msgCode){

		switch (msgCode){

			case I_DBExceptionInfo.MULTIPLE_ID :

				this.frmContactClass.addError(frmContactClass.FRM_FIELD_CONTACT_CLASS_ID, resultText[language][RSLT_EST_CODE_EXIST] );

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



	public ContactClass getContactClass() { return contactClass; } 



	public FrmContactClass getForm() { return frmContactClass; }



	public String getMessage(){ return msgString; }



	public int getStart() { return start; }



	public int action(int cmd , long oidContactClass){

		msgString = "";

		int excCode = I_DBExceptionInfo.NO_EXCEPTION;

		int rsCode = RSLT_OK;

		switch(cmd){

			case Command.ADD :

				break;



			case Command.SAVE :

				if(oidContactClass != 0){

					try{

						contactClass = PstContactClass.fetchExc(oidContactClass);

					}catch(Exception exc){

					}

				}



				frmContactClass.requestEntityObject(contactClass);



				if(frmContactClass.errorSize()>0) {

					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

					return RSLT_FORM_INCOMPLETE ;

				}



				if(contactClass.getOID()==0){

					try{

						long oid = pstContactClass.insertExc(this.contactClass);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);

					}



				}else{

					try {

						long oid = pstContactClass.updateExc(this.contactClass);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 

					}



				}

				break;



			case Command.EDIT :

				if (oidContactClass != 0) {

					try {

						contactClass = PstContactClass.fetchExc(oidContactClass);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidContactClass != 0) {

					try {

						contactClass = PstContactClass.fetchExc(oidContactClass);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.DELETE :

				if (oidContactClass != 0){

					try{

						long oid = PstContactClass.deleteExc(oidContactClass);

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

