/* 
 * Ctrl Name  		:  CtrlIjAccountMapping.java 
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

public class CtrlIjAccountMapping extends Control implements I_Language 
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
	private IjAccountMapping ijAccountMapping;
	private PstIjAccountMapping pstIjAccountMapping;
	private FrmIjAccountMapping frmIjAccountMapping;
	int language = LANGUAGE_DEFAULT;

	public CtrlIjAccountMapping(HttpServletRequest request){
		msgString = "";
		ijAccountMapping = new IjAccountMapping();
		try{
			pstIjAccountMapping = new PstIjAccountMapping(0);
		}catch(Exception e){;}
		frmIjAccountMapping = new FrmIjAccountMapping(request, ijAccountMapping);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmIjAccountMapping.addError(frmIjAccountMapping.FRM_FIELD_IJ_MAP_ACCOUNT_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public IjAccountMapping getIjAccountMapping() { return ijAccountMapping; } 

	public FrmIjAccountMapping getForm() { return frmIjAccountMapping; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidIjAccountMapping){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidIjAccountMapping != 0){
					try{
						ijAccountMapping = PstIjAccountMapping.fetchExc(oidIjAccountMapping);
					}catch(Exception exc){
					}
				}

				frmIjAccountMapping.requestEntityObject(ijAccountMapping);

				if(frmIjAccountMapping.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(ijAccountMapping.getOID()==0){
					try{
						long oid = pstIjAccountMapping.insertExc(this.ijAccountMapping);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstIjAccountMapping.updateExc(this.ijAccountMapping);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidIjAccountMapping != 0) {
					try {
						ijAccountMapping = PstIjAccountMapping.fetchExc(oidIjAccountMapping);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidIjAccountMapping != 0) {
					try {
						ijAccountMapping = PstIjAccountMapping.fetchExc(oidIjAccountMapping);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidIjAccountMapping != 0){
					try{
						long oid = PstIjAccountMapping.deleteExc(oidIjAccountMapping);
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
