/* 
 * Ctrl Name  		:  CtrlDocAppNote.java 
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

package com.dimata.workflow.form.approval;

/* java package */ 
import com.dimata.posbo.db.DBException;
import javax.servlet.http.*; 
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.workflow.entity.approval.*;

public class CtrlDocAppNote extends Control implements I_Language 
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
	private DocAppNote docAppNote;
	private PstDocAppNote pstDocAppNote;
	private FrmDocAppNote frmDocAppNote;
	int language = LANGUAGE_DEFAULT;

	public CtrlDocAppNote(HttpServletRequest request){
		msgString = "";
		docAppNote = new DocAppNote();
		try{
			pstDocAppNote = new PstDocAppNote(0);
		}catch(Exception e){;}
		frmDocAppNote = new FrmDocAppNote(request, docAppNote);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmDocAppNote.addError(frmDocAppNote.FRM_FIELD_DOC_APP_NOTE_OID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public DocAppNote getDocAppNote() { return docAppNote; } 

	public FrmDocAppNote getForm() { return frmDocAppNote; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidDocAppNote){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidDocAppNote != 0){
					try{
						docAppNote = PstDocAppNote.fetchExc(oidDocAppNote);
					}catch(Exception exc){
					}
				}

				frmDocAppNote.requestEntityObject(docAppNote);

				if(frmDocAppNote.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(docAppNote.getOID()==0){
					try{
						long oid = pstDocAppNote.insertExc(this.docAppNote);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstDocAppNote.updateExc(this.docAppNote);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidDocAppNote != 0) {
					try {
						docAppNote = PstDocAppNote.fetchExc(oidDocAppNote);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidDocAppNote != 0) {
					try {
						docAppNote = PstDocAppNote.fetchExc(oidDocAppNote);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidDocAppNote != 0){
					try{
						long oid = PstDocAppNote.deleteExc(oidDocAppNote);
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
