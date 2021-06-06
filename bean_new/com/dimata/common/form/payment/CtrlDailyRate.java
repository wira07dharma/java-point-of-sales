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

package com.dimata.common.form.payment;

/* java package */ 
import java.util.*; 
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
import com.dimata.gui.jsp.ControlDate;

public class CtrlDailyRate extends Control implements I_Language 
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
	private DailyRate dailyRate;
	private PstDailyRate pstDailyRate;
	private FrmDailyRate frmDailyRate;
	int language = LANGUAGE_DEFAULT;
        private HttpServletRequest req;
        
	public CtrlDailyRate(HttpServletRequest request){
		msgString = "";
		dailyRate = new DailyRate();
		try{
			pstDailyRate = new PstDailyRate(0);
		}catch(Exception e){;}
                req = request;
		frmDailyRate = new FrmDailyRate(request, dailyRate);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmDailyRate.addError(frmDailyRate.FRM_FIELD_DAILY_RATE_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public DailyRate getDailyRate() { return dailyRate; } 

	public FrmDailyRate getForm() { return frmDailyRate; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidDailyRate){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidDailyRate != 0){
					try{
						dailyRate = PstDailyRate.fetchExc(oidDailyRate);
					}catch(Exception exc){
					}
				}

				frmDailyRate.requestEntityObject(dailyRate);
                                Date date = ControlDate.getDateTime(FrmDailyRate.fieldNames[FrmDailyRate.FRM_FIELD_ROSTER_DATE], req);
                                dailyRate.setRosterDate(date);
				if(frmDailyRate.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(dailyRate.getOID()==0){
					try{
						long oid = pstDailyRate.insertExc(this.dailyRate);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstDailyRate.updateExc(this.dailyRate);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidDailyRate != 0) {
					try {
						dailyRate = PstDailyRate.fetchExc(oidDailyRate);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidDailyRate != 0) {
					try {
						dailyRate = PstDailyRate.fetchExc(oidDailyRate);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidDailyRate != 0){
					try{
						long oid = PstDailyRate.deleteExc(oidDailyRate);
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
