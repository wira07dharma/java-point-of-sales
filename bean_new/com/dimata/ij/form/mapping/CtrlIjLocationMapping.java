/* 
 * Ctrl Name  		:  CtrlIjLocationMapping.java 
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

public class CtrlIjLocationMapping extends Control implements I_Language 
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
	private IjLocationMapping ijLocationMapping;
	private PstIjLocationMapping pstIjLocationMapping;
	private FrmIjLocationMapping frmIjLocationMapping;
	int language = LANGUAGE_DEFAULT;

	public CtrlIjLocationMapping(HttpServletRequest request){
		msgString = "";
		ijLocationMapping = new IjLocationMapping();
		try{
			pstIjLocationMapping = new PstIjLocationMapping(0);
		}catch(Exception e){;}
		frmIjLocationMapping = new FrmIjLocationMapping(request, ijLocationMapping);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmIjLocationMapping.addError(frmIjLocationMapping.FRM_FIELD_IJ_MAP_LOCATION_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public IjLocationMapping getIjLocationMapping() { return ijLocationMapping; } 

	public FrmIjLocationMapping getForm() { return frmIjLocationMapping; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidIjLocationMapping){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidIjLocationMapping != 0){
					try{
						ijLocationMapping = PstIjLocationMapping.fetchExc(oidIjLocationMapping);
					}catch(Exception exc){
					}
				}

				frmIjLocationMapping.requestEntityObject(ijLocationMapping);

				if(frmIjLocationMapping.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(ijLocationMapping.getOID()==0){
					try{
						long oid = pstIjLocationMapping.insertExc(this.ijLocationMapping);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstIjLocationMapping.updateExc(this.ijLocationMapping);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidIjLocationMapping != 0) {
					try {
						ijLocationMapping = PstIjLocationMapping.fetchExc(oidIjLocationMapping);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidIjLocationMapping != 0) {
					try {
						ijLocationMapping = PstIjLocationMapping.fetchExc(oidIjLocationMapping);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidIjLocationMapping != 0){
					try{
						long oid = PstIjLocationMapping.deleteExc(oidIjLocationMapping);
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
