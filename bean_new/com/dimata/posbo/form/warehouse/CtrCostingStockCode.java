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
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.db.DBException;

import javax.servlet.http.HttpServletRequest;

public class CtrCostingStockCode extends Control implements I_Language {
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
    private CostingStockCode costingStockCode;
    private PstCostingStockCode pstCostingStockCode;
    private FrmCostingStockCode frmCostingStockCode;
    private HttpServletRequest req;
    private String strError = "";

    int language = LANGUAGE_DEFAULT;

    public String getStrError() {
        return strError;
    }

    public CtrCostingStockCode(HttpServletRequest request) {
        msgString = "";
        costingStockCode = new CostingStockCode();
        try {
            pstCostingStockCode = new PstCostingStockCode(0);
        } catch (Exception e) {
            ;
        }
        frmCostingStockCode = new FrmCostingStockCode(request, costingStockCode);
        req = request;
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmCostingStockCode.addError(FrmCostingStockCode.FRM_FIELD_MATERIAL_COSTING_CODE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public CostingStockCode getCostingStockCode() {
        return costingStockCode;
    }

    public FrmCostingStockCode getForm() {
        return frmCostingStockCode;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidCostingStockCode) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidCostingStockCode != 0) {
                    try {
                        costingStockCode = PstCostingStockCode.fetchExc(oidCostingStockCode);
                    } catch (Exception exc) {
                    }
                }

                frmCostingStockCode.requestEntityObject(costingStockCode);

                if (frmCostingStockCode.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (costingStockCode.getOID() == 0) {
                    try {
                        long oid = pstCostingStockCode.insertExc(this.costingStockCode);
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
                        long oid = pstCostingStockCode.updateExc(this.costingStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidCostingStockCode != 0) {
                    try {
                        costingStockCode = PstCostingStockCode.fetchExc(oidCostingStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidCostingStockCode != 0) {
                    try {
                        costingStockCode = PstCostingStockCode.fetchExc(oidCostingStockCode);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidCostingStockCode != 0) {
                    MatCostingItem matCostingItem = new MatCostingItem();
                    try {
                        try {
                            costingStockCode = PstCostingStockCode.fetchExc(oidCostingStockCode);
                            matCostingItem = PstMatCostingItem.fetchExc(costingStockCode.getCostingMaterialItemId());
                        } catch (Exception exc) {
                        }

                        long oid = PstCostingStockCode.deleteExc(oidCostingStockCode);

                        // proses update serial di stock code
                        try {
                            MaterialStockCode materialStockCode = PstMaterialStockCode.cekExistByCode(costingStockCode.getStockCode(), matCostingItem.getMaterialId());
                            materialStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                            PstMaterialStockCode.updateExc(materialStockCode);
                        } catch (Exception e) {}

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
    /*public String requestBarcode(int cnt, long oid, HttpServletRequest req) {
        try {
            for (int k = 0; k < cnt; k++) {
                int sts_chk = FRMQueryString.requestInt(req, "" + FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE] + "_" + k);
                //System.out.println("sts_chk : " + sts_chk);
                if (sts_chk != 0) {

                    // proses update status stock code
                    long oidStockCode = FRMQueryString.requestLong(req, "" + FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE] + "_st_" + k);
                    try {
                        MaterialStockCode materialStockCode = PstMaterialStockCode.fetchExc(oidStockCode);
                        materialStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_PROCESS);
                        PstMaterialStockCode.updateExc(materialStockCode);
                    } catch (Exception e) {
                    }

                    String strBrc = FRMQueryString.requestString(req, "txt_code_" + k);
                    CostingStockCode costingStockCode = new CostingStockCode();
                    costingStockCode.setCostingMaterialItemId(oid);
                    costingStockCode.setStockCode(strBrc);
                    try {
                        PstCostingStockCode.insertExc(costingStockCode);
                        strError = "Data sudah tersimpan.";
                    } catch (Exception e) {
                        strError = "Data tidak dapat disimpan.";
                    }
                }
            }
        } catch (Exception e) {
            strError = "Data tidak dapat disimpan.";
        }
        return "";
    }*/

    public String requestBarcode(double cnt, long oid, HttpServletRequest req, long oidCostingId){
        try{
            boolean boolsts = true;
            for(int k=0;k<cnt;k++){
                String strBrc = FRMQueryString.requestString(req,""+FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE]+"_"+k);
                long oidStockId = FRMQueryString.requestLong(req,""+FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE]+"_"+k+"_oid");
                //double strVal = FRMQueryString.requestDouble(req,""+FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_VALUE]+"_"+k);
                double strVal = CheckStockCode.checkStockValueOnStock(strBrc);
                if(strBrc.length()!=0){
                    CostingStockCode costingStockCode = new CostingStockCode();
                    if(oidStockId==0){
                        costingStockCode.setCostingMaterialItemId(oid);
                        costingStockCode.setStockCode(strBrc);
                        costingStockCode.setStockValue(strVal);
                        costingStockCode.setCostingMaterialId(oidCostingId);
                        try{
                            PstCostingStockCode.insertExc(costingStockCode);
                        }catch(Exception e){}
                    }else{
                        costingStockCode.setOID(oidStockId);
                        costingStockCode.setCostingMaterialItemId(oid);
                        costingStockCode.setStockCode(strBrc);
                        costingStockCode.setStockValue(strVal);
                        costingStockCode.setCostingMaterialId(oidCostingId);
                        try{
                            PstCostingStockCode.updateExc(costingStockCode);
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
