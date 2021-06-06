/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisTabungan;
import com.dimata.aiso.entity.masterdata.mastertabungan.MasterTabungan;
import com.dimata.aiso.entity.masterdata.mastertabungan.PstJenisTabungan;
import com.dimata.aiso.entity.masterdata.mastertabungan.PstMasterTabungan;
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
public class CtrlJenisTabungan extends Control implements I_Language {
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

	private JenisTabungan jenisTabungan;
        
        private MasterTabungan masterTabungan;

	private PstJenisTabungan pstJenisTabungan;
        
        private PstMasterTabungan pstMasterTabungan;

	private FrmJenisTabungan frmJenisTabungan;
        
        private FrmMasterTabungan frmMasterTabungan;

	int language = LANGUAGE_DEFAULT;



	public CtrlJenisTabungan (HttpServletRequest request){

		msgString = "";

		jenisTabungan = new JenisTabungan();

		try{

			pstJenisTabungan = new PstJenisTabungan(0);

		}catch(Exception e){;}

		frmJenisTabungan = new FrmJenisTabungan(request, jenisTabungan);

	}



	private String getSystemMessage(int msgCode){

		switch (msgCode){

			case I_DBExceptionInfo.MULTIPLE_ID :

				this.frmJenisTabungan.addError(FrmJenisTabungan.FRM_FIELD_JENIS_TABUNGAN_ID, resultText[language][RSLT_EST_CODE_EXIST] );

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



	public JenisTabungan getJenisTabungan() { return jenisTabungan; }



	public FrmJenisTabungan getForm() { return frmJenisTabungan; }



	public String getMessage(){ return msgString; }



	public int getStart() { return start; }



	public int action(int cmd , long oidJenisTabungan){

		msgString = "";

		int excCode = I_DBExceptionInfo.NO_EXCEPTION;

		int rsCode = RSLT_OK;

		switch(cmd){

			case Command.ADD :

				break;



			case Command.SAVE :

				if(oidJenisTabungan != 0){

					try{

						jenisTabungan = PstJenisTabungan.fetchExc(oidJenisTabungan);

					}catch(Exception exc){

					}

				}


                                
				frmJenisTabungan.requestEntityObject(jenisTabungan);
                                


				if(frmJenisTabungan.errorSize()>0) {

					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

					return RSLT_FORM_INCOMPLETE ;

				}



				if(jenisTabungan.getOID()==0){

					try{

						long oid = pstJenisTabungan.insertExc(this.jenisTabungan);

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

						long oid = pstJenisTabungan.updateExc(this.jenisTabungan);

					}catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}



				}

				break;



			case Command.EDIT :

				if (oidJenisTabungan != 0) {

					try {

						jenisTabungan = PstJenisTabungan.fetchExc(oidJenisTabungan);
                                                
                                               //masterTabungan = PstMasterTabungan.fetchExc(oidMasterTabungan);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidJenisTabungan != 0) {

					try {

						jenisTabungan = PstJenisTabungan.fetchExc(oidJenisTabungan);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.DELETE :

				if (oidJenisTabungan != 0){

					try{

						long oid = PstJenisTabungan.deleteExc(oidJenisTabungan);

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
