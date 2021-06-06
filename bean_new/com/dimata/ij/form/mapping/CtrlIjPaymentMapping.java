/* 
 * Ctrl Name  		:  CtrlIjPaymentMapping.java 
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

package com.dimata.ij.form.mapping;

// java package 
import javax.servlet.http.*; 

// dimata package
import com.dimata.util.*;
import com.dimata.util.lang.*;

// qdep package
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;

// project package
import com.dimata.posbo.db.DBException;
import com.dimata.ij.entity.mapping.*;

public class CtrlIjPaymentMapping extends Control implements I_Language 
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
	private IjPaymentMapping ijPaymentMapping;
	private PstIjPaymentMapping pstIjPaymentMapping;
	private FrmIjPaymentMapping frmIjPaymentMapping;
	int language = LANGUAGE_DEFAULT;

	public CtrlIjPaymentMapping(HttpServletRequest request){
		msgString = "";
		ijPaymentMapping = new IjPaymentMapping();
		try{
			pstIjPaymentMapping = new PstIjPaymentMapping(0);
		}catch(Exception e){;}
		frmIjPaymentMapping = new FrmIjPaymentMapping(request, ijPaymentMapping);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmIjPaymentMapping.addError(frmIjPaymentMapping.FRM_FIELD_IJ_MAP_PAYMENT_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public IjPaymentMapping getIjPaymentMapping() { return ijPaymentMapping; } 

	public FrmIjPaymentMapping getForm() { return frmIjPaymentMapping; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidIjPaymentMapping){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidIjPaymentMapping != 0){
					try{
						ijPaymentMapping = PstIjPaymentMapping.fetchExc(oidIjPaymentMapping);
					}catch(Exception exc){
					}
				}

				frmIjPaymentMapping.requestEntityObject(ijPaymentMapping);

				if(frmIjPaymentMapping.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(ijPaymentMapping.getOID()==0){
					try{
						long oid = pstIjPaymentMapping.insertExc(this.ijPaymentMapping);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstIjPaymentMapping.updateExc(this.ijPaymentMapping);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidIjPaymentMapping != 0) {
					try {
						ijPaymentMapping = PstIjPaymentMapping.fetchExc(oidIjPaymentMapping);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidIjPaymentMapping != 0) {
					try {
						ijPaymentMapping = PstIjPaymentMapping.fetchExc(oidIjPaymentMapping);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidIjPaymentMapping != 0){
					try{
						long oid = PstIjPaymentMapping.deleteExc(oidIjPaymentMapping);
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
