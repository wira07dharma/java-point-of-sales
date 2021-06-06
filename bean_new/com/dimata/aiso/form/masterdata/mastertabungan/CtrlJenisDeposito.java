/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisDeposito;
import com.dimata.aiso.entity.masterdata.mastertabungan.PstJenisDeposito;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author HaddyPuutraa
 */
public class CtrlJenisDeposito extends Control implements I_Language{
    public static final int RSLT_OK = 0;
    public static final int RSLT_SAME = 1;
    public static final int RSLT_INCOMPLETE = 2;
    public static final int RSLT_EXIST = 3;
    public static final int RSLT_UNKNOWN = 4;
    
    public static String resultText[][] = {
        {"OK ...", "Data sama ...", "Form belum lengkap ...", "Account link sudah ada ...", "Kesalahan tidak diketahui ..."},
        {"OK ...", "Same Data ...", "Form incomplete ...", "Link account already exist ...", "Unknown Error ..."}
    };
    
    private int start;
    private String msgString;
    private JenisDeposito jenisDeposito;
    private PstJenisDeposito pstJenisDeposito;
    private FrmJenisDeposito frmJenisDeposito;
    private int language = LANGUAGE_DEFAULT;
    
    public CtrlJenisDeposito(HttpServletRequest request) {
        msgString = "";
        jenisDeposito = new JenisDeposito();
        try {
            pstJenisDeposito = new PstJenisDeposito(0);
        } catch (Exception e) {
        }
        frmJenisDeposito = new FrmJenisDeposito(request,jenisDeposito);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmJenisDeposito.addError(frmJenisDeposito.FRM_NAMA_JENIS_DEPOSITO, resultText[language][RSLT_EXIST] );
		return resultText[language][RSLT_EXIST];

            default:
                return resultText[language][RSLT_UNKNOWN];

	}
    }
    
    private int getControlMsgId(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
		return RSLT_EXIST;

            default:
		return RSLT_UNKNOWN;
	}
    }
    
    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public JenisDeposito getJenisDeposito() {
        return jenisDeposito;
    }

    public FrmJenisDeposito getForm() {
        return frmJenisDeposito;
    }

    public String getMessage() {
        return msgString;
    }
    
    public int actionList(int listCmd, int start, int vectSize, int recordToGet) {
        msgString = "";
        
        switch(listCmd){                
            case Command.FIRST :                
                this.start = 0;
                break;
                
            case Command.PREV :                
                this.start = start - recordToGet;
                if(start < 0){
                    this.start = 0;
                }                
                break;
                
            case Command.NEXT :
                this.start = start + recordToGet;
                if(start >= vectSize){
                    this.start = start - recordToGet;
                }                
                break;
                
            case Command.LAST :
                int mdl = vectSize % recordToGet;
                if(mdl>0){
                    this.start = vectSize - mdl;
                }
                else{
                    this.start = vectSize - recordToGet;
                }                
                
                break;
                
            default:
                this.start = start;
                if(vectSize<1)
                    this.start = 0;                
                
                if(start > vectSize){
                    // set to last
                     mdl = vectSize % recordToGet;
                    if(mdl>0){
                        this.start = vectSize - mdl;
                    }
                    else{
                        this.start = vectSize - recordToGet;
                    }                
                }                
                break;
        } //end switch
        return this.start;
    }
    
    public int action(int cmd, long Oid) throws DBException{
        int errCode = -1;
        int excCode = 0;
        int rsCode = 0;
        
        switch(cmd){
            case Command.ADD :
            break;

            case Command.SAVE :
		if(Oid != 0){
                    try{
                        jenisDeposito = pstJenisDeposito.fetchExc(Oid);
                    }catch(Exception exc){
                    
                    }
		}
                
                frmJenisDeposito.requestEntityObject(jenisDeposito);
                if(frmJenisDeposito.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_INCOMPLETE ;
                }

		if(jenisDeposito.getOID()==0){
                    try{
                        long oid = pstJenisDeposito.insertExc(this.jenisDeposito);
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
                        long oid = pstJenisDeposito.updateExc(this.jenisDeposito);
                    }catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
            break;

            case Command.EDIT :
                if (Oid != 0) {
                    try {
                        jenisDeposito = pstJenisDeposito.fetchExc(Oid);
                    } catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
            break;

            case Command.ASK :
                if (Oid != 0) {
                    try {
                        jenisDeposito = pstJenisDeposito.fetchExc(Oid);
                    } catch (DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
            break;

            case Command.DELETE :
                if (Oid != 0){
                    try{
                        long oid = pstJenisDeposito.deleteExc(Oid);
                        if(oid!=0){
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }else{
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_INCOMPLETE;
                        }
                    }catch(DBException dbexc){
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }catch(Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
            break;

            default :

        }//end switch

        return excCode;
    }
}
