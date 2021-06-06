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
import com.dimata.posbo.entity.warehouse.PstSourceStockCode;
import com.dimata.posbo.entity.warehouse.SourceStockCode;
import com.dimata.posbo.db.DBException;

import javax.servlet.http.HttpServletRequest;

public class CtrSourceStockCode extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public String getStrError() {
        return strError;
    }

    private String strError = "";

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };

    private int start;
    private String msgString;
    private SourceStockCode sourceStockCode;
    private PstSourceStockCode pstSourceStockCode;
    private FrmSourceStockCode frmSourceStockCode;
    private HttpServletRequest req;

    int language = LANGUAGE_DEFAULT;

    public CtrSourceStockCode(HttpServletRequest request) {
        msgString = "";
        sourceStockCode = new SourceStockCode();
        try {
            pstSourceStockCode = new PstSourceStockCode(0);
        } catch (Exception e) {
            ;
        }
        frmSourceStockCode = new FrmSourceStockCode(request, sourceStockCode);
        req = request;
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmSourceStockCode.addError(FrmSourceStockCode.FRM_FIELD_SOURCE_STOCK_CODE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public SourceStockCode getSourceStockCode() {
        return sourceStockCode;
    }

    public FrmSourceStockCode getForm() {
        return frmSourceStockCode;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidSourceStockCode) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidSourceStockCode != 0) {
                    try {
                        sourceStockCode = PstSourceStockCode.fetchExc(oidSourceStockCode);
                    } catch (Exception exc) {
                    }
                }

                frmSourceStockCode.requestEntityObject(sourceStockCode);

                if (frmSourceStockCode.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (sourceStockCode.getOID() == 0) {
                    try {
                        long oid = pstSourceStockCode.insertExc(this.sourceStockCode);
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
                        long oid = pstSourceStockCode.updateExc(this.sourceStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidSourceStockCode != 0) {
                    try {
                        sourceStockCode = PstSourceStockCode.fetchExc(oidSourceStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidSourceStockCode != 0) {
                    try {
                        sourceStockCode = PstSourceStockCode.fetchExc(oidSourceStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidSourceStockCode != 0) {
                    try {
                        long oid = PstSourceStockCode.deleteExc(oidSourceStockCode);
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
     * long oidOpname;
     */
    public void requestBarcode(double cnt, long oid, HttpServletRequest req, long oidOpname){
        try{
            boolean boolsts = true;
            for(int k=0;k<cnt;k++){
                String strBrc = FRMQueryString.requestString(req,""+FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE]+"_"+k);
                double strVal = FRMQueryString.requestDouble(req,""+FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_VALUE]+"_"+k);
                long oidStockId = FRMQueryString.requestLong(req,""+FrmReceiveStockCode.fieldNames[FrmReceiveStockCode.FRM_FIELD_STOCK_CODE]+"_"+k+"_oid");
                if(strBrc.length()>0){
                    SourceStockCode sourceStockCode = new SourceStockCode();
                    if(oidStockId==0){
                        //buat pengcekan serial code sudah pernah disimpan atau tidak
                        String checkStockInput = CheckStockCode.checkStockCodeOpname(strBrc, oidOpname);

                        if(checkStockInput.equals("false")){
                             sourceStockCode.setSourceId(oid);
                             sourceStockCode.setStockOpnameId(oidOpname);
                             sourceStockCode.setStockCode(strBrc);
                             sourceStockCode.setStockValue(strVal);
                             try{
                                PstSourceStockCode.insertExc(sourceStockCode);
                                strError = "Kode stok barang sudah tersimpan.";
                             }catch(Exception e){}
                        }else{
                             strError = "Kode stok barang sudah Ada";
                        }
                    }else{
                        sourceStockCode.setOID(oidStockId);
                        sourceStockCode.setSourceId(oid);
                        sourceStockCode.setStockOpnameId(oidOpname);
                        sourceStockCode.setStockCode(strBrc);
                        sourceStockCode.setStockValue(strVal);
                        try{
                            PstSourceStockCode.updateExc(sourceStockCode);
                        }catch(Exception e){}
                    }
//                    if(boolsts){
//                        strError = "Kode stok barang sudah tersimpan.";
//                    }
                }else{
                    //strError = "Kode stok barang atau harga ada yang kosong!,lengkapi kode stok terus klik Simpan sekali lagi.";
                    boolsts = false;
                }
            }
        }catch(Exception e){}
    }
}
