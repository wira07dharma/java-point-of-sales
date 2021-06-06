/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.TypeTabungan;
import com.dimata.aiso.entity.masterdata.mastertabungan.PstTypeTabungan;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.qdep.db.*;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nuharta
 */
public class CtrlTypeTabungan extends Control implements I_Language {
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

	private TypeTabungan typeTabungan;

	private PstTypeTabungan pstTypeTabungan;

	private FrmTypeTabungan frmTypeTabungan;

	int language = LANGUAGE_DEFAULT;



	public CtrlTypeTabungan (HttpServletRequest request){

		msgString = "";

		typeTabungan = new TypeTabungan();

		try{

			pstTypeTabungan = new PstTypeTabungan(0);

		}catch(Exception e){;}

		frmTypeTabungan = new FrmTypeTabungan(request, typeTabungan);

	}



	private String getSystemMessage(int msgCode){

		switch (msgCode){

			case I_DBExceptionInfo.MULTIPLE_ID :

				this.frmTypeTabungan.addError(FrmTypeTabungan.FRM_FIELD_ID_TIPE_TABUNGAN, resultText[language][RSLT_EST_CODE_EXIST] );

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



	public TypeTabungan getTypeTabungan() { return typeTabungan; }



	public FrmTypeTabungan getForm() { return frmTypeTabungan; }



	public String getMessage(){ return msgString; }



	public int getStart() { return start; }



	public int action(int cmd , long oidTypeTabungan){

		msgString = "";

		int excCode = I_DBExceptionInfo.NO_EXCEPTION;

		int rsCode = RSLT_OK;

		switch(cmd){

			case Command.ADD :

				break;



			case Command.SAVE :

				if(oidTypeTabungan != 0){

					try{

						typeTabungan = PstTypeTabungan.fetchExc(oidTypeTabungan);

					}catch(Exception exc){

					}

				}



				frmTypeTabungan.requestEntityObject(typeTabungan);



				if(frmTypeTabungan.errorSize()>0) {

					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

					return RSLT_FORM_INCOMPLETE ;

				}



				if(typeTabungan.getOID()==0){

					try{

						long oid = pstTypeTabungan.insertExc(this.typeTabungan);

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

						long oid = pstTypeTabungan.updateExc(this.typeTabungan);

					}catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}



				}

				break;



			case Command.EDIT :

				if (oidTypeTabungan != 0) {

					try {

						typeTabungan = PstTypeTabungan.fetchExc(oidTypeTabungan);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidTypeTabungan != 0) {

					try {

						typeTabungan = PstTypeTabungan.fetchExc(oidTypeTabungan);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.DELETE :

				if (oidTypeTabungan != 0){

					try{

						long oid = PstTypeTabungan.deleteExc(oidTypeTabungan);

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
