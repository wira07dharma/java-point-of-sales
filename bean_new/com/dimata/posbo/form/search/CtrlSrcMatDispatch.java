/* 
 * Ctrl Name  		:  CtrlSrcMaterialDispatchExc.java 
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

package com.dimata.posbo.form.search;

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
import com.dimata.posbo.db.*;
/* project package */
//import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.search.*;

public class CtrlSrcMatDispatch extends Control implements I_Language 
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
	private SrcMatDispatch srcMaterialDispatchExc;
	//private PstSrcMaterialDispatchExc pstSrcMaterialDispatchExc;
	private FrmSrcMatDispatch frmSrcMaterialDispatchExc;
	int language = LANGUAGE_DEFAULT;

	public CtrlSrcMatDispatch(HttpServletRequest request){
		msgString = "";
		srcMaterialDispatchExc = new SrcMatDispatch();
		try{
			//pstSrcMaterialDispatchExc = new PstSrcMaterialDispatchExc(0);
		}catch(Exception e){;}
		frmSrcMaterialDispatchExc = new FrmSrcMatDispatch(request, srcMaterialDispatchExc);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				//this.frmSrcMaterialDispatchExc.addError(frmSrcMaterialDispatchExc.FRM_FIELD_MATERIAL_DISPATCH_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public SrcMatDispatch getSrcMatDispatch() { return srcMaterialDispatchExc; }

	public FrmSrcMatDispatch getForm() { return frmSrcMaterialDispatchExc; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SUBMIT :
				/*if(oidSrcMaterialDispatchExc != 0){
					try{
						srcMaterialDispatchExc = PstSrcMaterialDispatchExc.fetchExc(oidSrcMaterialDispatchExc);
					}catch(Exception exc){
					}
				}*/

				frmSrcMaterialDispatchExc.requestEntityObject(srcMaterialDispatchExc);

				/*if(frmSrcMaterialDispatchExc.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(srcMaterialDispatchExc.getOID()==0){
					try{
						long oid = pstSrcMaterialDispatchExc.insertExc(this.srcMaterialDispatchExc);
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
						long oid = pstSrcMaterialDispatchExc.updateExc(this.srcMaterialDispatchExc);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}*/
				break;

			/*case Command.EDIT :
				if (oidSrcMaterialDispatchExc != 0) {
					try {
						srcMaterialDispatchExc = PstSrcMaterialDispatchExc.fetchExc(oidSrcMaterialDispatchExc);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidSrcMaterialDispatchExc != 0) {
					try {
						srcMaterialDispatchExc = PstSrcMaterialDispatchExc.fetchExc(oidSrcMaterialDispatchExc);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidSrcMaterialDispatchExc != 0){
					try{
						long oid = PstSrcMaterialDispatchExc.deleteExc(oidSrcMaterialDispatchExc);
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
            */

			default :

		}
		return rsCode;
	}
}
