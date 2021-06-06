/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.form.location;

import com.dimata.posbo.db.DBException;
import com.dimata.common.entity.location.Kecamatan;
import com.dimata.common.entity.location.PstKecamatan;
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
public class CtrlKecamatan extends Control implements I_Language {
    
    
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
	private Kecamatan kecamatan;
	private PstKecamatan pstKecamatan;
	private FrmKecamatan frmKecamatan;
	int language = LANGUAGE_DEFAULT;

	public CtrlKecamatan(HttpServletRequest request){
		msgString = "";
		kecamatan = new Kecamatan();
		try{
			pstKecamatan = new PstKecamatan(0);
		}catch(Exception e){;}
		frmKecamatan = new FrmKecamatan(request, kecamatan);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmKecamatan.addError(frmKecamatan.FRM_FIELD_KD_KECAMATAN, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public Kecamatan getKecamatan() { return kecamatan; }

	public FrmKecamatan getForm() { return frmKecamatan; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidKecamatan){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidKecamatan != 0){
					try{
						kecamatan = PstKecamatan.fetchExc(oidKecamatan);
					}catch(Exception exc){
					}
				}

				frmKecamatan.requestEntityObject(kecamatan);

				if(frmKecamatan.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(kecamatan.getOID()==0){
					try{
						long oid = pstKecamatan.insertExc(this.kecamatan);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstKecamatan.updateExc(this.kecamatan);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}

				}
				break;

			case Command.EDIT :
				if (oidKecamatan != 0) {
					try {
						kecamatan = PstKecamatan.fetchExc(oidKecamatan);
					} catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidKecamatan != 0) {
					try {
						kecamatan = PstKecamatan.fetchExc(oidKecamatan);
					} catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidKecamatan != 0){
					try{
						long oid = PstKecamatan.deleteExc(oidKecamatan);
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
