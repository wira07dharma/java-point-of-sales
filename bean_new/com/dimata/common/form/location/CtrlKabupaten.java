/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.form.location;

import com.dimata.posbo.db.DBException;
import com.dimata.common.entity.location.Kabupaten;
import com.dimata.common.entity.location.PstKabupaten;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Acer
 */
public class CtrlKabupaten extends Control implements I_Language {
    
    public static int RSLT_OK = 0;
	public static int RSLT_UNKNOWN_ERROR = 1;
	public static int RSLT_EST_CODE_EXIST = 2;
	public static int RSLT_FORM_INCOMPLETE = 3;

	public static String[][] resultText = {
		{"Berhasil", "Tidak dapat diproses", "Kode status pegawai sudah ada ...", "Data tidak lengkap"},
		{"Succes", "Can not process", "Code already exist ...", "Data incomplete"}
	};

	private int start;
	private String msgString;
	private Kabupaten kabupaten;
	private PstKabupaten pstKabupaten;
	private FrmKabupaten frmKabupaten;
	int language = LANGUAGE_DEFAULT;

	public CtrlKabupaten(HttpServletRequest request){
		msgString = "";
		kabupaten = new Kabupaten();
		try{
			pstKabupaten = new PstKabupaten(0);
		}catch(Exception e){;}
		frmKabupaten = new FrmKabupaten(request, kabupaten);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmKabupaten.addError(frmKabupaten.FRM_FIELD_KD_KABUPATEN, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public Kabupaten getKabupaten() { return kabupaten; }

	public FrmKabupaten getForm() { return frmKabupaten; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidKabupaten){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidKabupaten != 0){
					try{
						kabupaten = PstKabupaten.fetchExc(oidKabupaten);
					}catch(Exception exc){
					}
				}

				frmKabupaten.requestEntityObject(kabupaten);

				if(frmKabupaten.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(kabupaten.getOID()==0){
					try{
						long oid = pstKabupaten.insertExc(this.kabupaten);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstKabupaten.updateExc(this.kabupaten);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}

				}
				break;

			case Command.EDIT :
				if (oidKabupaten != 0) {
					try {
						kabupaten = PstKabupaten.fetchExc(oidKabupaten);
					} catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidKabupaten != 0) {
					try {
						kabupaten = PstKabupaten.fetchExc(oidKabupaten);
					} catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidKabupaten != 0){
					try{
						long oid = PstKabupaten.deleteExc(oidKabupaten);
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
