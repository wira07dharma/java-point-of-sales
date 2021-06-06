/* 

 * Ctrl Name  		:  CtrlEmployee.java 

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



package com.dimata.harisma.form;



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

//import com.dimata.qdep.db.*;

import com.dimata.harisma.entity.employee.*;



public class CtrlEmployee extends Control implements I_Language 

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

	private Employee employee;

	private PstEmployee pstEmployee;

	private FrmEmployee frmEmployee;

	int language = LANGUAGE_DEFAULT;



	public CtrlEmployee(HttpServletRequest request){

		msgString = "";

		employee = new Employee();

		try{

			pstEmployee = new PstEmployee(0);

		}catch(Exception e){;}

		frmEmployee = new FrmEmployee(request, employee);

	}



	private String getSystemMessage(int msgCode){

		switch (msgCode){

			case I_DBExceptionInfo.MULTIPLE_ID :

				this.frmEmployee.addError(frmEmployee.FRM_FIELD_EMPLOYEE_ID, resultText[language][RSLT_EST_CODE_EXIST] );

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



	public Employee getEmployee() { return employee; } 



	public FrmEmployee getForm() { return frmEmployee; }



	public String getMessage(){ return msgString; }



	public int getStart() { return start; }



	public int action(int cmd , long oidEmployee){

		msgString = "";

		int excCode = I_DBExceptionInfo.NO_EXCEPTION;

		int rsCode = RSLT_OK;

		switch(cmd){

			case Command.ADD :

				break;



			case Command.SAVE :

				if(oidEmployee != 0){

					try{

						employee = PstEmployee.fetchExc(oidEmployee);

					}catch(Exception exc){

					}

				}



				frmEmployee.requestEntityObject(employee);



				if(frmEmployee.errorSize()>0) {

					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

					return RSLT_FORM_INCOMPLETE ;

				}



				if(employee.getOID()==0){

					try{

						long oid = pstEmployee.insertExc(this.employee);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);

					}



				}else{

					try {

						long oid = pstEmployee.updateExc(this.employee);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 

					}



				}

				break;



			case Command.EDIT :

				if (oidEmployee != 0) {

					try {

						employee = PstEmployee.fetchExc(oidEmployee);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidEmployee != 0) {

					try {

						employee = PstEmployee.fetchExc(oidEmployee);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.DELETE :

				if (oidEmployee != 0){

					try{

						long oid = PstEmployee.deleteExc(oidEmployee);

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

