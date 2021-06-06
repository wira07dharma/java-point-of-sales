/* 
 * Ctrl Name  		:  CtrlDiscountMapping.java 
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
import com.dimata.posbo.entity.masterdata.*;

public class CtrlDiscountMapping extends Control implements I_Language 
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
	private DiscountMapping discountMapping;
	private PstDiscountMapping pstDiscountMapping;
	private FrmDiscountMapping frmDiscountMapping;
	int language = LANGUAGE_DEFAULT;

	public CtrlDiscountMapping(HttpServletRequest request){
		msgString = "";
		discountMapping = new DiscountMapping();
		try{
			pstDiscountMapping = new PstDiscountMapping(0);
		}catch(Exception e){;}
		frmDiscountMapping = new FrmDiscountMapping(request, discountMapping);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmDiscountMapping.addError(frmDiscountMapping.FRM_FIELD_DISCOUNT_TYPE_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public DiscountMapping getDiscountMapping() { return discountMapping; } 

	public FrmDiscountMapping getForm() { return frmDiscountMapping; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidDiscountTypeId, long oidMaterialId, long oidCurrencyTypeId){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				boolean checkOIDs = PstDiscountMapping.checkOID( oidDiscountTypeId, oidMaterialId, oidCurrencyTypeId);
				if(checkOIDs){
					 try{
						discountMapping = PstDiscountMapping.fetchExc( oidDiscountTypeId, oidMaterialId, oidCurrencyTypeId);
					 }catch(Exception exc){
					 }
				}

				frmDiscountMapping.requestEntityObject(discountMapping);
				discountMapping.setDiscountTypeId(oidDiscountTypeId);
				discountMapping.setMaterialId(oidMaterialId);
				discountMapping.setCurrencyTypeId(oidCurrencyTypeId);

				if(frmDiscountMapping.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(checkOIDs == false){
					try{
						long oid = pstDiscountMapping.insertExc(this.discountMapping);
						if(oid!=0){
							msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
							excCode = RSLT_OK;
						}else{
							msgString = FRMMessage.getMessage(FRMMessage.ERR_SAVED);
							excCode = RSLT_FORM_INCOMPLETE;
						}
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
						long oid = pstDiscountMapping.updateExc(this.discountMapping);
						if(oid!=0){
							msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
							excCode = RSLT_OK;
						}else{
							msgString = FRMMessage.getMessage(FRMMessage.ERR_SAVED);
							excCode = RSLT_FORM_INCOMPLETE;
						}
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				try {
					discountMapping = PstDiscountMapping.fetchExc( oidDiscountTypeId, oidMaterialId, oidCurrencyTypeId);
				} catch (DBException dbexc){
					excCode = dbexc.getErrorCode();
					msgString = getSystemMessage(excCode);
				} catch (Exception exc){ 
					msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
				}
				break;

			case Command.ASK :
				try {
					discountMapping = PstDiscountMapping.fetchExc( oidDiscountTypeId, oidMaterialId, oidCurrencyTypeId);
				} catch (DBException dbexc){
					excCode = dbexc.getErrorCode();
					msgString = getSystemMessage(excCode);
				} catch (Exception exc){ 
					msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
				}
				break;

			case Command.DELETE :
				try{
					long oid = PstDiscountMapping.deleteExc( oidDiscountTypeId, oidMaterialId, oidCurrencyTypeId);
				}catch(DBException dbexc){
					excCode = dbexc.getErrorCode();
					msgString = getSystemMessage(excCode);
				}catch(Exception exc){	
					msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
				}
				break;

			default :

		}
		return rsCode;
	}
}
