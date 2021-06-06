/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.region;

import com.dimata.aiso.entity.masterdata.region.City;
import com.dimata.aiso.entity.masterdata.region.PstCity;
import com.dimata.qdep.db.*;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nuharta
 */
public class CtrlCity extends Control implements I_Language {
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

	private City city;

	private PstCity pstCity;

	private FrmCity frmCity;

	int language = LANGUAGE_DEFAULT;



	public CtrlCity (HttpServletRequest request){

		msgString = "";

		city = new City();

		try{

			pstCity = new PstCity(0);

		}catch(Exception e){;}

		frmCity = new FrmCity(request, city);

	}



	private String getSystemMessage(int msgCode){

		switch (msgCode){

			case I_DBExceptionInfo.MULTIPLE_ID :

				this.frmCity.addError(FrmCity.FRM_FIELD_CITY_ID, resultText[language][RSLT_EST_CODE_EXIST] );

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



	public City getcity() { return city; }



	public FrmCity getForm() { return frmCity; }



	public String getMessage(){ return msgString; }



	public int getStart() { return start; }



	public int action(int cmd , long oidCity){

		msgString = "";

		int excCode = I_DBExceptionInfo.NO_EXCEPTION;

		int rsCode = RSLT_OK;

		switch(cmd){

			case Command.ADD :

				break;



			case Command.SAVE :

				if(oidCity != 0){

					try{

						city = PstCity.fetchExc(oidCity);

					}catch(Exception exc){

					}

				}



				frmCity.requestEntityObject(city);



				if(frmCity.errorSize()>0) {

					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

					return RSLT_FORM_INCOMPLETE ;

				}



				if(city.getOID()==0){

					try{

						long oid = pstCity.insertExc(this.city);

					}catch(DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

						return getControlMsgId(excCode);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);

					}



				}else{

					try {

						long oid = pstCity.updateExc(this.city);

					}catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}



				}

				break;



			case Command.EDIT :

				if (oidCity != 0) {

					try {

						city = PstCity.fetchExc(oidCity);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidCity != 0) {

					try {

						city = PstCity.fetchExc(oidCity);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.DELETE :

				if (oidCity != 0){

					try{

						long oid = PstCity.deleteExc(oidCity);

						if(oid!=0){

							msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);

							excCode = RSLT_OK;

						}else{

							msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);

							excCode = RSLT_FORM_INCOMPLETE;

						}

					}catch(DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

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
