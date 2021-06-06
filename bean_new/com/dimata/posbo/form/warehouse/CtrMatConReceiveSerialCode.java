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

import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.qdep.system.I_DBExceptionInfo;

import com.dimata.posbo.entity.warehouse.MatConReceiveSerialCode;
import com.dimata.posbo.entity.warehouse.PstMatConReceiveSerialCode;
import com.dimata.posbo.db.DBException;

import javax.servlet.http.HttpServletRequest;
import java.util.Vector;

public class CtrMatConReceiveSerialCode extends Control implements I_Language {
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
    private MatConReceiveSerialCode receiveStockCode;
    private PstMatConReceiveSerialCode pstMatConReceiveSerialCode;
    private FrmMatConReceiveSerialCode frmMatConReceiveSerialCode;
    private HttpServletRequest req;
    private String strError = "";

    int language = LANGUAGE_DEFAULT;

    public String getStrError(){
        return strError;
    }

    public CtrMatConReceiveSerialCode(HttpServletRequest request) {
        msgString = "";
        receiveStockCode = new MatConReceiveSerialCode();
        try {
            pstMatConReceiveSerialCode = new PstMatConReceiveSerialCode(0);
        } catch (Exception e) {
            ;
        }
        frmMatConReceiveSerialCode = new FrmMatConReceiveSerialCode(request, receiveStockCode);
        req = request;
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatConReceiveSerialCode.addError(FrmMatConReceiveSerialCode.FRM_FIELD_MATERIAL_RECEIVE_CODE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MatConReceiveSerialCode getMatConReceiveSerialCode() {
        return receiveStockCode;
    }

    public FrmMatConReceiveSerialCode getForm() {
        return frmMatConReceiveSerialCode;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidMatConReceiveSerialCode) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMatConReceiveSerialCode != 0) {
                    try {
                        receiveStockCode = PstMatConReceiveSerialCode.fetchExc(oidMatConReceiveSerialCode);
                    } catch (Exception exc) {
                    }
                }

                frmMatConReceiveSerialCode.requestEntityObject(receiveStockCode);

                if (frmMatConReceiveSerialCode.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (receiveStockCode.getOID() == 0) {
                    try {
                        long oid = pstMatConReceiveSerialCode.insertExc(this.receiveStockCode);
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
                        long oid = pstMatConReceiveSerialCode.updateExc(this.receiveStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidMatConReceiveSerialCode != 0) {
                    try {
                        receiveStockCode = PstMatConReceiveSerialCode.fetchExc(oidMatConReceiveSerialCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatConReceiveSerialCode != 0) {
                    try {
                        receiveStockCode = PstMatConReceiveSerialCode.fetchExc(oidMatConReceiveSerialCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatConReceiveSerialCode != 0) {
                    try {
                        long oid = PstMatConReceiveSerialCode.deleteExc(oidMatConReceiveSerialCode);
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
    public String requestBarcode(int cnt, long oid, HttpServletRequest req){
        try{
            boolean boolsts = true;
            for(int k=0;k<cnt;k++){
                String strBrc = FRMQueryString.requestString(req,""+FrmMatConReceiveSerialCode.fieldNames[FrmMatConReceiveSerialCode.FRM_FIELD_STOCK_CODE]+"_"+k);
                long oidStockId = FRMQueryString.requestLong(req,""+FrmMatConReceiveSerialCode.fieldNames[FrmMatConReceiveSerialCode.FRM_FIELD_STOCK_CODE]+"_"+k+"_oid");
                if(strBrc.length()!=0){
                    MatConReceiveSerialCode receiveStockCode = new MatConReceiveSerialCode();
                    if(oidStockId==0){
                        receiveStockCode.setReceiveMaterialItemId(oid);
                        receiveStockCode.setStockCode(strBrc);
                        try{
                            PstMatConReceiveSerialCode.insertExc(receiveStockCode);
                        }catch(Exception e){}
                    }else{
                        receiveStockCode.setOID(oidStockId);
                        receiveStockCode.setReceiveMaterialItemId(oid);
                        receiveStockCode.setStockCode(strBrc);
                        try{
                            PstMatConReceiveSerialCode.updateExc(receiveStockCode);
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
