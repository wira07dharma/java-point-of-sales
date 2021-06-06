/* 
 * Ctrl Name  		:  CtrlCurrencyType.java 
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

package com.dimata.common.form.payment;

/* java package */ 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 

/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;

/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.DBException;

/* project package */
import com.dimata.common.entity.payment.*;

public class CtrlCurrencyType extends Control implements I_Language 
{
	public static int RSLT_OK = 0;
	public static int RSLT_UNKNOWN_ERROR = 1;
	public static int RSLT_EST_CODE_EXIST = 2;
	public static int RSLT_FORM_INCOMPLETE = 3;

	public static String[][] resultText = {
		{"Berhasil", "Tidak dapat diproses", "Indeks sudah ada", "Data tidak lengkap"},
		{"Succes", "Can not process", "Index exist", "Data incomplete"}
	};

	private int start;
	private String msgString;
	private CurrencyType currencyType;
	private PstCurrencyType pstCurrencyType;
	private FrmCurrencyType frmCurrencyType;
	int language = LANGUAGE_DEFAULT;

	public CtrlCurrencyType(HttpServletRequest request){
		msgString = "";
		currencyType = new CurrencyType();
		try{
			pstCurrencyType = new PstCurrencyType(0);
		}catch(Exception e){;}
		frmCurrencyType = new FrmCurrencyType(request, currencyType);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmCurrencyType.addError(frmCurrencyType.FRM_FIELD_CURRENCY_TYPE_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public CurrencyType getCurrencyType() { return currencyType; } 

	public FrmCurrencyType getForm() { return frmCurrencyType; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidCurrencyType){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidCurrencyType != 0){
					try{
						currencyType = PstCurrencyType.fetchExc(oidCurrencyType);
					}catch(Exception exc){
					}
				}

				frmCurrencyType.requestEntityObject(currencyType);

				if(frmCurrencyType.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(currencyType.getOID()==0){
					try{
						long oid = pstCurrencyType.insertExc(this.currencyType);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstCurrencyType.updateExc(this.currencyType);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidCurrencyType != 0) {
					try {
						currencyType = PstCurrencyType.fetchExc(oidCurrencyType);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidCurrencyType != 0) {
					try {
						currencyType = PstCurrencyType.fetchExc(oidCurrencyType);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidCurrencyType != 0){
					try{
						long oid = PstCurrencyType.deleteExc(oidCurrencyType);
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
