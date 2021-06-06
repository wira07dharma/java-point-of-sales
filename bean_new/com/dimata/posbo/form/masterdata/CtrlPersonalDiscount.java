/* 
 * Ctrl Name  		:  CtrlDailyRate.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.posbo.form.masterdata;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;

import com.dimata.posbo.entity.masterdata.*;

public class CtrlPersonalDiscount extends Control implements I_Language 
{
	public static int RSLT_OK = 0;
	public static int RSLT_UNKNOWN_ERROR = 1;
	public static int RSLT_EST_CODE_EXIST = 2;
	public static int RSLT_FORM_INCOMPLETE = 3;

	public static String[][] resultText = {
		{"Berhasil", "Tidak dapat diproses", "Produk sudah ada", "Data tidak lengkap"},
		{"Succes", "Can not process", "Item exist", "Data incomplete"}
	};

	private int start;
	private String msgString;
	private PersonalDiscount personalDiscount;
	private PstPersonalDiscount pstPersonalDiscount;
	private FrmPersonalDiscount frmPersonalDiscount;
	int language = LANGUAGE_DEFAULT;

	public CtrlPersonalDiscount(HttpServletRequest request){
		msgString = "";
		personalDiscount = new PersonalDiscount();
		try{
			pstPersonalDiscount = new PstPersonalDiscount(0);
		}catch(Exception e){;}
		frmPersonalDiscount = new FrmPersonalDiscount(request, personalDiscount);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmPersonalDiscount.addError(frmPersonalDiscount.FRM_PERSONAL_DISCOUNT_ID , resultText[language][RSLT_EST_CODE_EXIST] );
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

	public PersonalDiscount getPersonalDiscount() { return personalDiscount; } 

	public FrmPersonalDiscount getForm() { return frmPersonalDiscount; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidPersonalDiscount, long oidMember ){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidPersonalDiscount != 0){
					try{
						personalDiscount = PstPersonalDiscount.fetchExc(oidPersonalDiscount);
					}catch(Exception exc){
					}
				}

				frmPersonalDiscount.requestEntityObject(personalDiscount);
                                personalDiscount.setContactId(oidMember);
                                
                                boolean checkOID = PstPersonalDiscount.checkOID(personalDiscount.getMaterialId(),oidMember); 

				if(frmPersonalDiscount.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(personalDiscount.getOID()==0){
                                        boolean checkOIDMaterial = PstPersonalDiscount.checkOID(personalDiscount.getMaterialId(),oidMember); 
                                        if(checkOIDMaterial){
                                            msgString = resultText[language][RSLT_EST_CODE_EXIST];
                                            return RSLT_EST_CODE_EXIST; 
                                        }
					try{
						long oid = pstPersonalDiscount.insertExc(this.personalDiscount);
                                                
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
						long oid = pstPersonalDiscount.updateExc(this.personalDiscount);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidPersonalDiscount != 0) {
					try {
						personalDiscount = PstPersonalDiscount.fetchExc(oidPersonalDiscount);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidPersonalDiscount != 0) {
					try {
						personalDiscount = PstPersonalDiscount.fetchExc(oidPersonalDiscount);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidPersonalDiscount != 0){
					try{
						long oid = PstPersonalDiscount.deleteExc(oidPersonalDiscount);
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
