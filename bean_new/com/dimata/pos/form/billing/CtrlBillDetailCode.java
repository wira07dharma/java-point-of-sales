
package com.dimata.pos.form.billing;
/**
 *
 * @author Wiweka
 */
import javax.servlet.http.*;

// dimata package 
import com.dimata.util.*;
import com.dimata.util.lang.*;

// qdep package 
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
//import com.dimata.qdep.db.*;

// project package 
import com.dimata.pos.entity.billing.*;
import com.dimata.posbo.db.DBException;

import com.dimata.posbo.ajax.CheckStockCode;

public class  CtrlBillDetailCode extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Code Material Sales sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Material Sales Code already exist", "Data incomplete"}
    };
    private int start;
    private String msgString;
    private BillDetailCode billDetailCode;
    private PstBillDetailCode pstBillDetailCode;
    private FrmBillDetailCode frmBillDetailCode;
    int language = LANGUAGE_FOREIGN;
    private String strError = "";
    public CtrlBillDetailCode(HttpServletRequest request) {
        msgString = "";
        billDetailCode = new BillDetailCode();
        try {
            pstBillDetailCode = new PstBillDetailCode(0);
        } catch (Exception e) {
            ;
        }
        frmBillDetailCode = new FrmBillDetailCode(request, billDetailCode);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmBillDetailCode.addError(frmBillDetailCode.FRM_FIELD_CASH_BILL_DETAIL_CODE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public BillDetailCode getBillDetailCode() {
        return billDetailCode;
    }

    public FrmBillDetailCode getForm() {
        return frmBillDetailCode;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long getBillDetailCode, long oidBillDetail) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (getBillDetailCode != 0) {
                    try {
                        billDetailCode = PstBillDetailCode.fetchExc(getBillDetailCode);
                    } catch (Exception exc) {
                    }
                }

                frmBillDetailCode.requestEntityObject(billDetailCode);
                billDetailCode.setSaleItemId(oidBillDetail);

                if (frmBillDetailCode.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (billDetailCode.getOID() == 0) {
                    try {
                        long oid = pstBillDetailCode.insertExc(this.billDetailCode);
                        
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstBillDetailCode.updateExc(this.billDetailCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (getBillDetailCode != 0) {
                    try {
                        billDetailCode = PstBillDetailCode.fetchExc(getBillDetailCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (getBillDetailCode != 0) {
                    try {
                        billDetailCode = PstBillDetailCode.fetchExc(getBillDetailCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (getBillDetailCode != 0) {
                    try {
                        long oid = PstBillDetailCode.deleteExc(getBillDetailCode);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default:

        }
        return rsCode;
    }

     public String requestBarcode(double cnt, long oid, HttpServletRequest req, long oidReceive){
        try{
            boolean boolsts = true;
            for(int k=0;k<cnt;k++){
                String strBrc = FRMQueryString.requestString(req,""+FrmBillDetailCode.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_CODE]+"_"+k);
                long oidStockId = FRMQueryString.requestLong(req,""+FrmBillDetailCode.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_CODE]+"_"+k+"_oid");
                double strVal = FRMQueryString.requestDouble(req,""+FrmBillDetailCode.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_VALUE]+"_"+k);
                if(strBrc.length()!=0){
                    BillDetailCode billDetailCode = new BillDetailCode();
                    if(oidStockId==0){
                        String checkStockInput = CheckStockCode.checkStockCodeReceive(strBrc, oidReceive);
                         if(checkStockInput.equals("false")){
                            billDetailCode.setSaleItemId(oid);
                            billDetailCode.setStockCode(strBrc);
                            billDetailCode.setValue(strVal);
                            //dyas 20131130
                            //tambah setReceiveMaterialId untuk mengambil nilai yang dibawa oleh oidReceive
                            //receiveStockCode.setReceiveMaterialId(oidReceive);
                            try{
                                PstBillDetailCode.insertExc(billDetailCode);
                            }catch(Exception e){}
                             strError = "Kode stok barang sudah tersimpan.";
                         }else{
                             strError = "Kode stok barang sudah Ada";
                        }
                    }else{
                        billDetailCode.setOID(oidStockId);
                       // receiveStockCode.setReceiveMaterialItemId(oid);
                        billDetailCode.setStockCode(strBrc);
                        billDetailCode.setValue(strVal);

                        //dyas 20131130
                        //tambah setReceiveMaterialId untuk mengambil nilai yang dibawa oleh oidReceive
                         //receiveStockCode.setReceiveMaterialId(oidReceive);
                        try{
                            PstBillDetailCode.updateExc(billDetailCode);
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
