/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.masterdata;



import com.dimata.hanoman.entity.masterdata.MasterGroup;
import com.dimata.hanoman.entity.masterdata.PstMasterGroup;
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
public class CtrlMasterGroup extends Control implements I_Language {
    
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

	private MasterGroup masterGroup;

	private PstMasterGroup pstMasterGroup;

	private FrmMasterGroup frmMasterGroup;

	int language = LANGUAGE_DEFAULT;



	public CtrlMasterGroup(HttpServletRequest request){

		msgString = "";

		masterGroup = new MasterGroup();

		try{

			pstMasterGroup = new PstMasterGroup(0);

		}catch(Exception e){;}

		frmMasterGroup = new FrmMasterGroup(request, masterGroup);

	}



	private String getSystemMessage(int msgCode){

		switch (msgCode){

			case I_DBExceptionInfo.MULTIPLE_ID :

				this.frmMasterGroup.addError(frmMasterGroup.FRM_FIELD_MASTER_GROUP_ID, resultText[language][RSLT_EST_CODE_EXIST] );

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



	public MasterGroup getMasterGroup() { return masterGroup; } 



	public FrmMasterGroup getForm() { return frmMasterGroup; }



	public String getMessage(){ return msgString; }



	public int getStart() { return start; }



	public int action(int cmd , long oidMasterGroup){

		msgString = "";

		int excCode = I_DBExceptionInfo.NO_EXCEPTION;

		int rsCode = RSLT_OK;

		switch(cmd){

			case Command.ADD :

				break;



			case Command.SAVE :

				if(oidMasterGroup != 0){

					try{

						masterGroup = PstMasterGroup.fetchExc(oidMasterGroup);

					}catch(Exception exc){

					}

				}



				frmMasterGroup.requestEntityObject(masterGroup);



                                //masterGroup.setTypeGroup(group);

                                

                                

                                /*(if(PstMasterGroup.isNameAlreadyExist(oidMasterGroup, group, masterGroup.getNamaGroup())){

                                        this.frmMasterGroup.addError(FrmMasterGroup.FRM_FIELD_NAME_GROUP, "duplicat entry");

                                        msgString = "Can't save data, duplicat entry name for this group";

					return RSLT_FORM_INCOMPLETE ;

                                }*/



				if(frmMasterGroup.errorSize()>0) {

					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

					return RSLT_FORM_INCOMPLETE ;

				}



				if(masterGroup.getOID()==0){

					try{

						long oid = pstMasterGroup.insertExc(this.masterGroup);

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

						long oid = pstMasterGroup.updateExc(this.masterGroup);

					}catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 

					}



				}

				break;



			case Command.EDIT :

				if (oidMasterGroup != 0) {

					try {

						masterGroup = PstMasterGroup.fetchExc(oidMasterGroup);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidMasterGroup != 0) {

					try {

						masterGroup = PstMasterGroup.fetchExc(oidMasterGroup);

					} catch (DBException dbexc){

						excCode = dbexc.getErrorCode();

						msgString = getSystemMessage(excCode);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.DELETE :

				if (oidMasterGroup != 0){

					try{

						long oid = PstMasterGroup.deleteExc(oidMasterGroup);

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
