/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.form.location;

import com.dimata.posbo.db.DBException;
import com.dimata.common.entity.location.Provinsi;
import com.dimata.common.entity.location.PstProvinsi;
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
public class CtrlProvinsi extends Control implements I_Language   {
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
	private Provinsi provinsi;
	private PstProvinsi pstProvinsi;
	private FrmPropinsi frmPropinsi;
	int language = LANGUAGE_DEFAULT;

	public CtrlProvinsi(HttpServletRequest request){
		msgString = "";
		provinsi = new Provinsi();
		try{
			pstProvinsi = new PstProvinsi(0);
		}catch(Exception e){;}
		frmPropinsi = new FrmPropinsi(request, provinsi);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmPropinsi.addError(frmPropinsi.FRM_FIELD_KD_PROPINSI, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public Provinsi getPropinsi() { return provinsi; }

	public FrmPropinsi getForm() { return frmPropinsi; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidPropinsi){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidPropinsi != 0){
					try{
						provinsi = PstProvinsi.fetchExc(oidPropinsi);
					}catch(Exception exc){
					}
				}

				frmPropinsi.requestEntityObject(provinsi);

				if(frmPropinsi.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(provinsi.getOID()==0){
					try{
						long oid = pstProvinsi.insertExc(this.provinsi);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstProvinsi.updateExc(this.provinsi);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidPropinsi != 0) {
					try {
						provinsi = PstProvinsi.fetchExc(oidPropinsi);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidPropinsi != 0) {
					try {
						provinsi = PstProvinsi.fetchExc(oidPropinsi);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidPropinsi != 0){
					try{
						long oid = PstProvinsi.deleteExc(oidPropinsi);
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
