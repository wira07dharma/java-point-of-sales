/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.transaksi;

import com.dimata.aiso.entity.masterdata.transaksi.DataTransaksi;
import com.dimata.aiso.entity.masterdata.transaksi.PstDataTransaksi;
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
public class CtrlDataTransaksi extends Control implements I_Language{
    public static int RSLT_OK=0;
    public static int RSLT_UNKNOWN_ERROR=1;
    public static int RSLT_EST_CODE_EXIST=2;
    public static int RSLT_FORM_INCOMPLETE=3;        
    public static int RSLT_RECORD_NOT_FOUND=4;
    
    public static String[][] resultText={
        {"Proses Berhasil","Tidak Dapat Diproses","DataTransaksi Tersebut Sudah Ada", "Data Tidak Lengkap","Data tidak ditemukan karena telah diubah oleh user lain"},
        {"Success","Can not process","DataTransaksi No exist","Data Incomplete", "Data not found cause another user changed it"}
    };
    
    private int start;
    private String msgString;
    private DataTransaksi dataTransaksi;
    private PstDataTransaksi pstDataTransaksi;
    private FrmDataTransaksi frmDataTransaksi;
    int language = LANGUAGE_DEFAULT;

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }
    
    public CtrlDataTransaksi(HttpServletRequest request){
        msgString = "";
        dataTransaksi = new DataTransaksi();
        try{
            pstDataTransaksi = new PstDataTransaksi(0);
        }catch(Exception e){
            System.out.println("kesalahan hasil request http "+ e);
        }
        frmDataTransaksi = new FrmDataTransaksi(request, dataTransaksi);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmDataTransaksi.addError(frmDataTransaksi.FRM_FIELD_ID_ANGGOTA, resultText[language][RSLT_EST_CODE_EXIST] );
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
    
    public DataTransaksi getDataTransaksi(){
        return dataTransaksi;
    }
    
    public FrmDataTransaksi getForm(){
        return frmDataTransaksi;
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
                        dataTransaksi = pstDataTransaksi.fetchExc(Oid);
                    }catch(Exception exc){
                    
                    }
		}
                
                frmDataTransaksi.requestEntityObject(dataTransaksi);
                if(frmDataTransaksi.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }

		if(dataTransaksi.getOID()==0){
                    try{
                        long oid = pstDataTransaksi.insertExc(this.dataTransaksi);
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
                        long oid = pstDataTransaksi.updateExc(this.dataTransaksi);
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
                        dataTransaksi = pstDataTransaksi.fetchExc(Oid);
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
                        dataTransaksi = pstDataTransaksi.fetchExc(Oid);
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
                        long oid = pstDataTransaksi.deleteExc(Oid);
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
