/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.transaksi;

import com.dimata.aiso.entity.masterdata.transaksi.Deposito;
import com.dimata.aiso.entity.masterdata.transaksi.PstDeposito;
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
public class CtrlDeposito extends Control implements I_Language{
    public static int RSLT_OK=0;
    public static int RSLT_UNKNOWN_ERROR=1;
    public static int RSLT_EST_CODE_EXIST=2;
    public static int RSLT_FORM_INCOMPLETE=3;        
    public static int RSLT_RECORD_NOT_FOUND=4;
    
    public static String[][] resultText={
        {"Proses Berhasil","Tidak Dapat Diproses","Deposito Tersebut Sudah Ada", "Data Tidak Lengkap","Data tidak ditemukan karena telah diubah oleh user lain"},
        {"Success","Can not process","Deposito No exist","Data Incomplete", "Data not found cause another user changed it"}
    };
    
    private int start;
    private String msgString;
    private Deposito deposito;
    private PstDeposito pstDeposito;
    private FrmDeposito frmDeposito;
    int language = LANGUAGE_DEFAULT;

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }
    
    public CtrlDeposito(HttpServletRequest request){
        msgString = "";
        deposito = new Deposito();
        try{
            pstDeposito = new PstDeposito(0);
        }catch(Exception e){
            System.out.println("kesalahan hasil request http "+ e);
        }
        frmDeposito = new FrmDeposito(request, deposito);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmDeposito.addError(frmDeposito.FRM_FLD_ID_ANGGOTA, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public Deposito getDeposito(){
        return deposito;
    }
    
    public FrmDeposito getForm(){
        return frmDeposito;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart(){
        return start;
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
    
    public int Action(int cmd, long Oid) throws DBException{
        int errCode = -1;
        int excCode = 0;
        int rsCode = 0;
        
        switch(cmd){
            case Command.ADD :
            break;

            case Command.SAVE :
		if(Oid != 0){
                    try{
                        deposito = pstDeposito.fetchExc(Oid);
                    }catch(Exception exc){
                    
                    }
		}
                
                frmDeposito.requestEntityObject(deposito);
                if(frmDeposito.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }

		if(deposito.getOID()==0){
                    try{
                        long oid = pstDeposito.insertExc(this.deposito);
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
                        long oid = pstDeposito.updateExc(this.deposito);
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
                        deposito = pstDeposito.fetchExc(Oid);
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
                        deposito = pstDeposito.fetchExc(Oid);
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
                        long oid = pstDeposito.deleteExc(Oid);
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

            default :

        }//end switch

        return excCode;
    }
}
