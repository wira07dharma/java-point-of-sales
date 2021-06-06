/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.form.purchasing;

/**
 *
 * @author dimata005
 */
/* java package */

import java.util.*;
import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.purchasing.DistributionPurchaseOrder;
import com.dimata.posbo.entity.purchasing.PstDistributionPurchaseOrder;

public class CtrlDistributionPurchaseOrder  extends Control implements I_Language{
    public static final String className = I_DocType.DOCTYPE_CLASSNAME;
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
    private DistributionPurchaseOrder dpo;
    private PstDistributionPurchaseOrder pstdpo;
    private FrmDistributionPurchaseOrder frmdpo;
    int language = LANGUAGE_DEFAULT;
    private Date purchDate = null;
    Date dateLog = new  Date();
    boolean incrementAllPrType = true;
    int counter = 0;
    int st = 0;
    private String strError = "";


    public CtrlDistributionPurchaseOrder(HttpServletRequest request) {
        msgString = "";
        dpo = new DistributionPurchaseOrder();
        try {
            pstdpo = new PstDistributionPurchaseOrder(0);
        } catch (Exception e) {
            ;
        }
        //po=po-2;
        frmdpo = new FrmDistributionPurchaseOrder(request, dpo);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmdpo.addError(frmdpo.FRM_FIELD_PURCHASE_DISTRIBUTION_ORDER_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public DistributionPurchaseOrder getDistributionPurchaseOrder() {
        return dpo;
    }

    public FrmDistributionPurchaseOrder getForm() {
        return frmdpo;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }
    
    public int action(int cmd, long oidPurchaseOrder, long oidDistributionPurchaseOrder,String nameUser, long userID,HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        int prMaterialType = -1;
        try {
            I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();
            prMaterialType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_POR);
        } catch (Exception e) {
            System.out.println("Error action Order Material");
        }
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidDistributionPurchaseOrder != 0) {
                    try {
                        dpo = PstDistributionPurchaseOrder.fetchExc(oidDistributionPurchaseOrder);
                    } catch (Exception exc) {
                    }
                }

                frmdpo.requestEntityObject(dpo);

                if (frmdpo.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                String locs[] = request.getParameterValues(FrmDistributionPurchaseOrder.fieldNames[FrmDistributionPurchaseOrder.FRM_FIELD_LOCATION_ID]);
                
                if (dpo.getOID() == 0) {
                    try {
                        long oid = pstdpo.insertExc(this.dpo);
                        //PROSES UNTUK MENYIMPAN HISTORY JIKA OID DARI ORDER PO =! 0
                        /*if(oid!=0)
                        {
                            insertHistory(userID, nameUser, cmd, oid);
                        }*/
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
                        //RUBAH NILAI CMD MENJADI UPDATE
                        //int cmdHistory = Command.UPDATE;
                        long oid = pstdpo.updateExc(this.dpo);
//                        if(oid!=0)
//                        {
//                            //FUNGSI SQL UNTUK MELAKUKAN INSERT KE TABEL LOG_HISTORY_NEW KETIKA SUDAH 2 JAM
//                            //int getDiffHour = PstLogSysHistory.getLastUpdateTime(oid);
//                            /*if(getDiffHour > 2)
//                            {
//                               insertHistory(userID, nameUser, cmdHistory, oid, cmd, true);
//                            }
//                            else
//                            {
//                                insertHistory(userID, nameUser, cmdHistory, oid);
//                            }*/
//                        }

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidDistributionPurchaseOrder != 0) {
                    try {
                        dpo = PstDistributionPurchaseOrder.fetchExc(oidDistributionPurchaseOrder);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidDistributionPurchaseOrder != 0) {
                    try {
                        dpo = PstDistributionPurchaseOrder.fetchExc(oidDistributionPurchaseOrder);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidDistributionPurchaseOrder != 0) {
                    try {
                        long oid = PstDistributionPurchaseOrder.deleteExc(oidPurchaseOrder);
                        //RUBAH NILAI CMD MENJADI DELETE
                        //cmd = 6;
                        if (oid == 0) {
//                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
//                            excCode = RSLT_OK;
//                            try
//                            {
//                                insertHistory(userID, nameUser, cmd, oid);
//                            }
//                            catch(Exception e)
//                            {
//
//                            }
//
//                        } else {
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


    public String requestBarcode(int cnt, long oid, HttpServletRequest req){
        try{
            boolean boolsts = true;
            for(int k=0;k<cnt;k++){
                long strLocation = FRMQueryString.requestLong(req,""+FrmDistributionPurchaseOrder.fieldNames[FrmDistributionPurchaseOrder.FRM_FIELD_LOCATION_ID]+"_"+k);
                long oidDistributionPOId = FRMQueryString.requestLong(req,""+FrmDistributionPurchaseOrder.fieldNames[FrmDistributionPurchaseOrder.FRM_FIELD_PURCHASE_DISTRIBUTION_ORDER_ID]+"_"+k+"_oid");
                double qty = FRMQueryString.requestDouble(req,""+FrmDistributionPurchaseOrder.fieldNames[FrmDistributionPurchaseOrder.FRM_FIELD_QTY]+"_"+k);
                if(strLocation!=0){
                    DistributionPurchaseOrder distributionPurchaseOrder = new DistributionPurchaseOrder();
                    if(oidDistributionPOId==0){
                        distributionPurchaseOrder.setPurchaseOrderId(oid);
                        distributionPurchaseOrder.setLocationId(strLocation);
                        distributionPurchaseOrder.setQty(qty);
                        try{
                            PstDistributionPurchaseOrder.insertExc(distributionPurchaseOrder);
                        }catch(Exception e){}
                    }else{
                        distributionPurchaseOrder.setOID(oidDistributionPOId);
                        distributionPurchaseOrder.setPurchaseOrderId(oid);
                        distributionPurchaseOrder.setLocationId(strLocation);
                        distributionPurchaseOrder.setQty(qty);
                        try{
                            PstDistributionPurchaseOrder.updateExc(distributionPurchaseOrder);
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
