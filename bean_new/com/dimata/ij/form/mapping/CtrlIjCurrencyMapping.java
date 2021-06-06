/* 
 * Ctrl Name  		:  CtrlIjCurrencyMapping.java 
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

public class CtrlIjCurrencyMapping extends Control implements I_Language 
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
	private IjCurrencyMapping ijCurrencyMapping;
	private PstIjCurrencyMapping pstIjCurrencyMapping;
	private FrmIjCurrencyMapping frmIjCurrencyMapping;
	int language = LANGUAGE_DEFAULT;

	public CtrlIjCurrencyMapping(HttpServletRequest request){
		msgString = "";
		ijCurrencyMapping = new IjCurrencyMapping();
		try{
			pstIjCurrencyMapping = new PstIjCurrencyMapping(0);
		}catch(Exception e){;}
		frmIjCurrencyMapping = new FrmIjCurrencyMapping(request, ijCurrencyMapping);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmIjCurrencyMapping.addError(frmIjCurrencyMapping.FRM_FIELD_IJ_MAP_CURR_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public IjCurrencyMapping getIjCurrencyMapping() { return ijCurrencyMapping; } 

	public FrmIjCurrencyMapping getForm() { return frmIjCurrencyMapping; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidIjCurrencyMapping){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidIjCurrencyMapping != 0){
					try{
						ijCurrencyMapping = PstIjCurrencyMapping.fetchExc(oidIjCurrencyMapping);
					}catch(Exception exc){
					}
				}

				frmIjCurrencyMapping.requestEntityObject(ijCurrencyMapping);                                

				if(frmIjCurrencyMapping.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(ijCurrencyMapping.getOID()==0){
					try{
						long oid = pstIjCurrencyMapping.insertExc(this.ijCurrencyMapping);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstIjCurrencyMapping.updateExc(this.ijCurrencyMapping);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidIjCurrencyMapping != 0) {
					try {
						ijCurrencyMapping = PstIjCurrencyMapping.fetchExc(oidIjCurrencyMapping);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidIjCurrencyMapping != 0) {
					try {
						ijCurrencyMapping = PstIjCurrencyMapping.fetchExc(oidIjCurrencyMapping);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidIjCurrencyMapping != 0){
					try{
						long oid = PstIjCurrencyMapping.deleteExc(oidIjCurrencyMapping);
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
