/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.form.FrmClsSpending;

/*
Description : Controll ClsSpending
Date : Tue Jun 25 2019
Author :
*/

public class CtrlClsSpending extends Control implements I_Language {

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
	private ClsSpending entClsSpending;
	private PstClsSpending pstClsSpending;
	private FrmClsSpending frmClsSpending;
	int language = LANGUAGE_DEFAULT;

	public CtrlClsSpending(HttpServletRequest request) {
		msgString = "";
		entClsSpending = new ClsSpending();
		try {
			pstClsSpending = new PstClsSpending(0);
		} catch (Exception e) {;
		}
		frmClsSpending = new FrmClsSpending(request, entClsSpending);
	}

	private String getSystemMessage(int msgCode) {
		switch (msgCode) {
			case I_DBExceptionInfo.MULTIPLE_ID:
				this.frmClsSpending.addError(frmClsSpending.FRM_FIELD_SPENDING_ID, resultText[language][RSLT_EST_CODE_EXIST]);
				return resultText[language][RSLT_EST_CODE_EXIST];
			default:
				return resultText[language][RSLT_UNKNOWN_ERROR];
		}
	}

	private int getControlMsgId(int msgCode) {
		switch (msgCode) {
			case I_DBExceptionInfo.MULTIPLE_ID:
				return RSLT_EST_CODE_EXIST;
			default:
				return RSLT_UNKNOWN_ERROR;
		}
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public ClsSpending getClsSpending() {
		return entClsSpending;
	}

	public FrmClsSpending getForm() {
		return frmClsSpending;
	}

	public String getMessage() {
		return msgString;
	}

	public int getStart() {
		return start;
	}

	public int action(int cmd, long oidClsSpending) {
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch (cmd) {
			case Command.ADD:
				break;

			case Command.SAVE:
				if (oidClsSpending != 0) {
					try {
						entClsSpending = PstClsSpending.fetchExc(oidClsSpending);
					} catch (Exception exc) {
					}
				}

				frmClsSpending.requestEntityObject(entClsSpending);

				if (frmClsSpending.errorSize() > 0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE;
				}

				if (entClsSpending.getOID() == 0) {
					try {
						long oid = pstClsSpending.insertExc(this.entClsSpending);
					} catch (DBException dbexc) {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
						return getControlMsgId(excCode);
					} catch (Exception exc) {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				} else {
					try {
						long oid = pstClsSpending.updateExc(this.entClsSpending);
					} catch (DBException dbexc) {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc) {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}

				}
				break;

			case Command.EDIT:
				if (oidClsSpending != 0) {
					try {
						entClsSpending = PstClsSpending.fetchExc(oidClsSpending);
					} catch (DBException dbexc) {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc) {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK:
				if (oidClsSpending != 0) {
					try {
						entClsSpending = PstClsSpending.fetchExc(oidClsSpending);
					} catch (DBException dbexc) {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc) {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE:
				if (oidClsSpending != 0) {
					try {
						long oid = PstClsSpending.deleteExc(oidClsSpending);
						if (oid != 0) {
							msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
							excCode = RSLT_OK;
						} else {
							msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
							excCode = RSLT_FORM_INCOMPLETE;
						}
					} catch (DBException dbexc) {
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc) {
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			default:

		}
		return rsCode;
	}
}