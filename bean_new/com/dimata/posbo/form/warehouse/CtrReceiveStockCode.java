/*
 * Ctrl Name  		:  CtrlDiscountType.java
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

package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.ajax.CheckStockCode;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.qdep.system.I_DBExceptionInfo;

import com.dimata.posbo.entity.warehouse.ReceiveStockCode;
import com.dimata.posbo.entity.warehouse.PstReceiveStockCode;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

public class CtrReceiveStockCode extends Control implements I_Language {
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
    private ReceiveStockCode receiveStockCode;
    private PstReceiveStockCode pstReceiveStockCode;
    private FrmReceiveStockCode frmReceiveStockCode;
    private HttpServletRequest req;
    private String strError = "";

    int language = LANGUAGE_DEFAULT;

    public String getStrError(){
        return strError;
    }

    public CtrReceiveStockCode(HttpServletRequest request) {
        msgString = "";
        receiveStockCode = new ReceiveStockCode();
        try {
            pstReceiveStockCode = new PstReceiveStockCode(0);
        } catch (Exception e) {
            ;
        }
        frmReceiveStockCode = new FrmReceiveStockCode(request, receiveStockCode);
        req = request;
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmReceiveStockCode.addError(FrmReceiveStockCode.FRM_FIELD_MATERIAL_RECEIVE_CODE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ReceiveStockCode getReceiveStockCode() {
        return receiveStockCode;
    }

    public FrmReceiveStockCode getForm() {
        return frmReceiveStockCode;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidReceiveStockCode) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidReceiveStockCode != 0) {
                    try {
                        receiveStockCode = PstReceiveStockCode.fetchExc(oidReceiveStockCode);
                    } catch (Exception exc) {
                    }
                }

                frmReceiveStockCode.requestEntityObject(receiveStockCode);

                if (frmReceiveStockCode.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (receiveStockCode.getOID() == 0) {
                    try {
                        long oid = pstReceiveStockCode.insertExc(this.receiveStockCode);
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
                        long oid = pstReceiveStockCode.updateExc(this.receiveStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidReceiveStockCode != 0) {
                    try {
                        receiveStockCode = PstReceiveStockCode.fetchExc(oidReceiveStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidReceiveStockCode != 0) {
                    try {
                        receiveStockCode = PstReceiveStockCode.fetchExc(oidReceiveStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidReceiveStockCode != 0) {
                    try {
                        long oid = PstReceiveStockCode.deleteExc(oidReceiveStockCode);
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

            default :

        }
        return rsCode;
    }

    /**
     * ini untuk get request
     * @param vList
     * @param req
     */
    public String requestBarcode(double cnt, long oid, HttpServletRequest req, long oidReceive){
        try{
            boolean boolsts = true;
            for(int k=0;k<cnt;k++){
                String strBrc = FRMQueryString.requestString(req,""+FrmReceiveStockCode.fieldNames[FrmReceiveStockCode.FRM_FIELD_STOCK_CODE]+"_"+k);
                long oidStockId = FRMQueryString.requestLong(req,""+FrmReceiveStockCode.fieldNames[FrmReceiveStockCode.FRM_FIELD_STOCK_CODE]+"_"+k+"_oid");
                double strVal = FRMQueryString.requestDouble(req,""+FrmReceiveStockCode.fieldNames[FrmReceiveStockCode.FRM_FIELD_STOCK_VALUE]+"_"+k);
                if(strBrc.length()!=0){
                    ReceiveStockCode receiveStockCode = new ReceiveStockCode();
                    if(oidStockId==0){
                        String checkStockInput = CheckStockCode.checkStockCodeReceive(strBrc, oidReceive);
                         if(checkStockInput.equals("false")){
                            receiveStockCode.setReceiveMaterialItemId(oid);
                            receiveStockCode.setStockCode(strBrc);
                            receiveStockCode.setStockValue(strVal);
                            //dyas 20131130
                            //tambah setReceiveMaterialId untuk mengambil nilai yang dibawa oleh oidReceive
                            receiveStockCode.setReceiveMaterialId(oidReceive);
                            try{
                                PstReceiveStockCode.insertExc(receiveStockCode);
                            }catch(Exception e){}
                             strError = "Kode stok barang sudah tersimpan.";
                         }else{
                             strError = "Kode stok barang sudah Ada";
                        }
                    }else{
                        receiveStockCode.setOID(oidStockId);
                        receiveStockCode.setReceiveMaterialItemId(oid);
                        receiveStockCode.setStockCode(strBrc);
                         receiveStockCode.setStockValue(strVal);

                        //dyas 20131130
                        //tambah setReceiveMaterialId untuk mengambil nilai yang dibawa oleh oidReceive
                         receiveStockCode.setReceiveMaterialId(oidReceive);
                        try{
                            PstReceiveStockCode.updateExc(receiveStockCode);
                        }catch(Exception e){}
                    }
                    if(boolsts){
                        strError = "Kode stok barang sudah tersimpan.";
                    }
                }else{
                    strError = "Kode stok barang ada yang kosong!,lengkapi kode stok terus klik Simpan sekali lagi.";
                    boolsts = false;
                }
            }
        }catch(Exception e){}
        return strError;
    }
	
	public String generateBarcode(double cnt, long oid, HttpServletRequest req, long oidReceive){
        try{
			MatReceive matRec = new MatReceive();
			int year = 0;
			try {
				matRec = PstMatReceive.fetchExc(oidReceive);
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(matRec.getReceiveDate());
				year = calendar.get(Calendar.YEAR);
			} catch (Exception exc){
				
			}
            boolean boolsts = true;
			int countCurrentData = PstReceiveStockCode.getCount(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+oid);
            for(int k=0;k<(cnt-countCurrentData);k++){
				
                String strBrc = PstReceiveStockCode.getLastSN(year);
                long oidStockId = FRMQueryString.requestLong(req,""+FrmReceiveStockCode.fieldNames[FrmReceiveStockCode.FRM_FIELD_STOCK_CODE]+"_"+k+"_oid");
                double strVal = FRMQueryString.requestDouble(req,""+FrmReceiveStockCode.fieldNames[FrmReceiveStockCode.FRM_FIELD_STOCK_VALUE]+"_"+k);
                if(strBrc.length()!=0){
                    ReceiveStockCode receiveStockCode = new ReceiveStockCode();
                    if(oidStockId==0){
                        String checkStockInput = CheckStockCode.checkStockCodeReceive(strBrc, oidReceive);
                         if(checkStockInput.equals("false")){
                            receiveStockCode.setReceiveMaterialItemId(oid);
                            receiveStockCode.setStockCode(strBrc);
                            receiveStockCode.setStockValue(strVal);
                            //dyas 20131130
                            //tambah setReceiveMaterialId untuk mengambil nilai yang dibawa oleh oidReceive
                            receiveStockCode.setReceiveMaterialId(oidReceive);
                            try{
                                PstReceiveStockCode.insertExc(receiveStockCode);
                            }catch(Exception e){}
                             strError = "Kode stok barang sudah tersimpan.";
                         }else{
                             strError = "Kode stok barang sudah Ada";
                        }
                    }else{
                        receiveStockCode.setOID(oidStockId);
                        receiveStockCode.setReceiveMaterialItemId(oid);
                        receiveStockCode.setStockCode(strBrc);
                         receiveStockCode.setStockValue(strVal);

                        //dyas 20131130
                        //tambah setReceiveMaterialId untuk mengambil nilai yang dibawa oleh oidReceive
                         receiveStockCode.setReceiveMaterialId(oidReceive);
                        try{
                            PstReceiveStockCode.updateExc(receiveStockCode);
                        }catch(Exception e){}
                    }
                    if(boolsts){
                        strError = "Kode stok barang sudah tersimpan.";
                    }
                }else{
                    strError = "Kode stok barang ada yang kosong!,lengkapi kode stok terus klik Simpan sekali lagi.";
                    boolsts = false;
                }
            }
        }catch(Exception e){}
        return strError;
    }


    public String requestRecReturnBarcode(int cnt, long oid, HttpServletRequest req){
        try{
            boolean boolsts = true;
            for(int k=0;k<cnt;k++){
                String strBrc = FRMQueryString.requestString(req,""+FrmReceiveStockCode.fieldNames[FrmReceiveStockCode.FRM_FIELD_STOCK_CODE]+"_"+k);
                long oidStockId = FRMQueryString.requestLong(req,""+FrmReceiveStockCode.fieldNames[FrmReceiveStockCode.FRM_FIELD_STOCK_CODE]+"_"+k+"_oid");
                if(strBrc.length()!=0){
                    ReceiveStockCode receiveStockCode = new ReceiveStockCode();
                    if(oidStockId==0){
                        receiveStockCode.setReceiveMaterialItemId(oid);
                        receiveStockCode.setStockCode(strBrc);
                        try{
                            PstReceiveStockCode.insertExc(receiveStockCode);
                        }catch(Exception e){}
                    }else{
                        receiveStockCode.setOID(oidStockId);
                        receiveStockCode.setReceiveMaterialItemId(oid);
                        receiveStockCode.setStockCode(strBrc);
                        try{
                            PstReceiveStockCode.updateExc(receiveStockCode);
                        }catch(Exception e){}
                    }
                    if(boolsts){
                        strError = "Kode stok barang sudah tersimpan.";
                    }
                }else{
                    strError = "Kode stok barang ada yang kosong!,lengkapi kode stok terus klik Simpan sekali lagi.";
                    boolsts = false;
                }
            }
        }catch(Exception e){}
        return strError;
    }
    

   
    
}
