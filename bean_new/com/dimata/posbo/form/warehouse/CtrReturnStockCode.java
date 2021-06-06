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
import com.dimata.posbo.entity.warehouse.PstReturnStockCode;
import com.dimata.posbo.entity.warehouse.ReturnStockCode;
import com.dimata.posbo.entity.warehouse.MaterialStockCode;
import com.dimata.posbo.entity.warehouse.PstMaterialStockCode;
import com.dimata.posbo.entity.warehouse.MatReturnItem;
import com.dimata.posbo.entity.warehouse.PstMatReturnItem;
import com.dimata.posbo.db.DBException;

import javax.servlet.http.HttpServletRequest;

public class CtrReturnStockCode extends Control implements I_Language {
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
    private ReturnStockCode returnStockCode;
    private PstReturnStockCode pstReturnStockCode;
    private FrmReturnStockCode frmReturnStockCode;
    private HttpServletRequest req;
    private String strError = "";

    int language = LANGUAGE_DEFAULT;

    public String getStrError() {
        return strError;
    }

    public CtrReturnStockCode(HttpServletRequest request) {
        msgString = "";
        returnStockCode = new ReturnStockCode();
        try {
            pstReturnStockCode = new PstReturnStockCode(0);
        } catch (Exception e) {
            ;
        }
        frmReturnStockCode = new FrmReturnStockCode(request, returnStockCode);
        req = request;
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmReturnStockCode.addError(FrmReturnStockCode.FRM_FIELD_MATERIAL_RETURN_CODE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public ReturnStockCode getReturnStockCode() {
        return returnStockCode;
    }

    public FrmReturnStockCode getForm() {
        return frmReturnStockCode;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidReturnStockCode) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidReturnStockCode != 0) {
                    try {
                        returnStockCode = PstReturnStockCode.fetchExc(oidReturnStockCode);
                    } catch (Exception exc) {
                    }
                }

                frmReturnStockCode.requestEntityObject(returnStockCode);

                if (frmReturnStockCode.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (returnStockCode.getOID() == 0) {
                    try {
                        long oid = pstReturnStockCode.insertExc(this.returnStockCode);
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
                        long oid = pstReturnStockCode.updateExc(this.returnStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidReturnStockCode != 0) {
                    try {
                        returnStockCode = PstReturnStockCode.fetchExc(oidReturnStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidReturnStockCode != 0) {
                    try {
                        returnStockCode = PstReturnStockCode.fetchExc(oidReturnStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidReturnStockCode != 0) {
                    try {
                        MatReturnItem matReturnItem = new MatReturnItem();
                        if (oidReturnStockCode != 0) {
                            try {
                                returnStockCode = PstReturnStockCode.fetchExc(oidReturnStockCode);
                                matReturnItem = PstMatReturnItem.fetchExc(returnStockCode.getReturnMaterialItemId());
                            } catch (Exception exc) {
                            }
                        }

                        long oid = PstReturnStockCode.deleteExc(oidReturnStockCode);

                        // proses update serial di stock code
                        try {
                            MaterialStockCode materialStockCode = PstMaterialStockCode.cekExistByCode(returnStockCode.getStockCode(), matReturnItem.getMaterialId());
                            materialStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                            PstMaterialStockCode.updateExc(materialStockCode);
                        } catch (Exception e) {
                        }

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

     public String requestBarcode(double cnt, long oid, HttpServletRequest req, long oidReturnId){
        try{
            boolean boolsts = true;
            for(int k=0;k<cnt;k++){
                String strBrc = FRMQueryString.requestString(req,""+FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE]+"_"+k);
                long oidStockId = FRMQueryString.requestLong(req,""+FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE]+"_"+k+"_oid");
                //double strVal = FRMQueryString.requestDouble(req,""+FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_VALUE]+"_"+k);
                //cari nilai value stock
                double strVal = CheckStockCode.checkStockValueOnStock(strBrc);
                if(strBrc.length()!=0){
                    ReturnStockCode returnStockCode = new ReturnStockCode();
                    if(oidStockId==0){
                        returnStockCode.setReturnMaterialItemId(oid);
                        returnStockCode.setStockCode(strBrc);
                        returnStockCode.setStockValue(strVal);
                        //dyas 20131202
                        //tambah setReturnMaterialId untuk mengambil nilai yang dibawa oleh oidReturn
                        returnStockCode.setReturnMaterialId(oidReturnId);
                        try{
                            PstReturnStockCode.insertExc(returnStockCode);
                        }catch(Exception e){}
                    }else{
                        returnStockCode.setOID(oidStockId);
                        returnStockCode.setReturnMaterialItemId(oid);
                        returnStockCode.setStockCode(strBrc);
                        returnStockCode.setStockValue(strVal);

                        //dyas 20131202
                        //tambah setReturnMaterialId untuk mengambil nilai yang dibawa oleh oidReturn
                        returnStockCode.setReturnMaterialId(oidReturnId);
                        try{
                            PstReturnStockCode.updateExc(returnStockCode);
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
