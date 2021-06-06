/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.masterdata;

import com.dimata.hanoman.entity.masterdata.MasterType;
import com.dimata.hanoman.entity.masterdata.PstMasterType;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sangtel6
 */
public class CtrlMasterType extends Control implements I_Language  {
    
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

	private MasterType masterType;

	private PstMasterType pstMasterType;

	private FrmMasterType frmMasterType;

	int language = LANGUAGE_DEFAULT;



	public CtrlMasterType(HttpServletRequest request){

		msgString = "";

		masterType = new MasterType();

		try{

			pstMasterType = new PstMasterType(0);

		}catch(Exception e){;}

		frmMasterType = new FrmMasterType(request, masterType);

	}



	private String getSystemMessage(int msgCode){

		switch (msgCode){

			case I_DBExceptionInfo.MULTIPLE_ID :

				this.frmMasterType.addError(frmMasterType.FRM_FIELD_MASTER_TYPE_ID, resultText[language][RSLT_EST_CODE_EXIST] );

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



	public MasterType getMasterType() { return masterType; } 



	public FrmMasterType getForm() { return frmMasterType; }



	public String getMessage(){ return msgString; }



	public int getStart() { return start; }



	public int action(int cmd , long oidMasterType, int group){

		msgString = "";

		int excCode = I_DBExceptionInfo.NO_EXCEPTION;

		int rsCode = RSLT_OK;

		switch(cmd){

			case Command.ADD :

				break;



			case Command.SAVE :

				if(oidMasterType != 0){

					try{

						masterType = PstMasterType.fetchExc(oidMasterType);

					}catch(Exception exc){

					}

				}



				frmMasterType.requestEntityObject(masterType);



                                masterType.setTypeGroup(group);

                                

                                if(PstMasterType.isCodeAlreadyExist(oidMasterType, group, masterType.getMasterCode())){

                                        this.frmMasterType.addError(FrmMasterType.FRM_FIELD_MASTER_CODE, "duplicat entry");

                                        msgString = "Can't save data, duplicat entry code for this group";

					return RSLT_FORM_INCOMPLETE ;

                                }

                                

                                if(PstMasterType.isNameAlreadyExist(oidMasterType, group, masterType.getMasterName())){

                                        this.frmMasterType.addError(FrmMasterType.FRM_FIELD_MASTER_NAME, "duplicat entry");

                                        msgString = "Can't save data, duplicat entry name for this group";

					return RSLT_FORM_INCOMPLETE ;

                                }



				if(frmMasterType.errorSize()>0) {

					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

					return RSLT_FORM_INCOMPLETE ;

				}



				if(masterType.getOID()==0){

					try{

						long oid = pstMasterType.insertExc(this.masterType);

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

						long oid = pstMasterType.updateExc(this.masterType);

					}catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 

					}



				}

				break;



			case Command.EDIT :

				if (oidMasterType != 0) {

					try {

						masterType = PstMasterType.fetchExc(oidMasterType);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidMasterType != 0) {

					try {

						masterType = PstMasterType.fetchExc(oidMasterType);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.DELETE :

				if (oidMasterType != 0){

					try{

						long oid = PstMasterType.deleteExc(oidMasterType);

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
