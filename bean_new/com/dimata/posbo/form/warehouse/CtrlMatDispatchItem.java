package com.dimata.posbo.form.warehouse;

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.session.masterdata.SessPosting;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.system.entity.PstSystemProperty;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class CtrlMatDispatchItem extends Control implements I_Language {

    public static final int RSLT_OK = 0;
    public static final int RSLT_UNKNOWN_ERROR = 1;
    public static final int RSLT_MATERIAL_EXIST = 2;
    public static final int RSLT_FORM_INCOMPLETE = 3;
    public static final int RSLT_QTY_NULL = 4;

    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Barang sudah ada", "Data tidak lengkap", "Jumlah barang tidak boleh nol ..."},
        {"Succes", "Can not process", "Material already exist ...", "Data incomplete", "Quantity may not zero ..."}
    };

    private int start;
    private String msgString;
    private MatDispatchItem matDispatchItem;
    private PstMatDispatchItem pstMatDispatchItem;
    private FrmMatDispatchItem frmMatDispatchItem;
    MatDispatchItem prevMatDispatchItem = null;
    MatDispatch prevMatDispatch = null;
    int language = LANGUAGE_DEFAULT;
    private Date dateLog = new Date();

    public CtrlMatDispatchItem() {
    }

    public CtrlMatDispatchItem(HttpServletRequest request) {
        msgString = "";
        matDispatchItem = new MatDispatchItem();
        try {
            pstMatDispatchItem = new PstMatDispatchItem(0);
        } catch (Exception e) {
        }
        frmMatDispatchItem = new FrmMatDispatchItem(request, matDispatchItem);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                this.frmMatDispatchItem.addError(frmMatDispatchItem.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_MATERIAL_EXIST]);
                return resultText[language][RSLT_MATERIAL_EXIST];
            case RSLT_QTY_NULL:
                this.frmMatDispatchItem.addError(frmMatDispatchItem.FRM_FIELD_QTY, resultText[language][RSLT_QTY_NULL]);
                return resultText[language][RSLT_QTY_NULL];
            default :
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                return RSLT_MATERIAL_EXIST;
            case RSLT_QTY_NULL:
                return RSLT_QTY_NULL;
            default :
                return RSLT_UNKNOWN_ERROR;
        }
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public MatDispatchItem getMatDispatchItem() {
        return matDispatchItem;
    }

    public FrmMatDispatchItem getForm() {
        return frmMatDispatchItem;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }


    /**
     *
     * @param cmd
     * @param <CODE>oidMatDispatchItem</CODE>
     * @param oidMatDispatch
     * @return
     */

    public int action(int cmd, long oidMatDispatchItem, long oidMatDispatch){
     return action(cmd,oidMatDispatchItem, oidMatDispatch, "", 0);
    }

    synchronized public int action(int cmd, long oidMatDispatchItem, long oidMatDispatch, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                double qtymax = 0;
                if (oidMatDispatchItem != 0) {
                    try {
                        matDispatchItem = PstMatDispatchItem.fetchExc(oidMatDispatchItem);
                        qtymax = matDispatchItem.getQty();
                    } catch (Exception exc) {
                    }
                }

                frmMatDispatchItem.requestEntityObject(matDispatchItem);System.out.println(matDispatchItem.getQty());
                matDispatchItem.setResidueQty(matDispatchItem.getQty());
                matDispatchItem.setDispatchMaterialId(oidMatDispatch);

                if ((frmMatDispatchItem.errorSize() > 0) || (matDispatchItem.getQty()<0.000)) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }


                /**
                 * check if current material already exist in orderMaterial
                 * @created <CODE>by Gedhy</CODE>
                 */
                if (matDispatchItem.getQty() == 0) {
                    msgString = getSystemMessage(RSLT_QTY_NULL);
                    return getControlMsgId(RSLT_QTY_NULL);
                }

                /**
                 * ini di pakai untuk mencari objeck dispatch
                 * dan di pakai untuk mencari object receive item
                 * untuk mengetahui nilai sisa yang sebenarnya.
                 */
                MatDispatch matDispatch = new MatDispatch();
                try{
                    matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                }catch(Exception e){}


                if(this!=null && oidMatDispatch!=0 ){
                    try {
                         prevMatDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                         prevMatDispatchItem = PstMatDispatchItem.fetchExc(oidMatDispatchItem);
                    } catch (Exception e) {
                    }
                }
                /*MatReceiveItem matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matDispatch.getInvoiceSupplier(), 0,matDispatchItem.getMaterialId());

                qtymax = qtymax + matReceiveItem.getResidueQty();
                System.out.println("===>>> SISA QTY : "+qtymax);

                // jika quantity yg akan didispatch melebihi maximum yg ada, maka return error
                if(matDispatchItem.getQty() > qtymax){
                    frmMatDispatchItem.addError(0,"");
                    msgString = "maksimal qty adalah ="+qtymax;
                    return RSLT_FORM_INCOMPLETE ;
                } */

                //double qty = PstMaterialStock.cekQtyStock(matDispatchItem.getDispatchMaterialId(),matDispatchItem.getMaterialId(),matDispatch.getLocationId());
                //if(qty<matDispatchItem.getQty()){
                //    frmMatDispatchItem.addError(FrmMatDispatchItem.FRM_FIELD_MATERIAL_ID,"Qty stok tidak cukup!.");
                //    msgString = "Qty stok tidak cukup!."; //getSystemMessage(RSLT_QTY_NULL);
                //    return getControlMsgId(RSLT_QTY_NULL);
                //}
                
                String autoSaveRec = PstSystemProperty.getValueByName("AUTO_SAVE_TRANSFER");
                if (autoSaveRec.equals("1")){
                    if (matDispatchItem.getOID() == 0 && PstMatDispatchItem.materialExist(matDispatchItem.getMaterialId(), oidMatDispatch)) {
                        MatDispatchItem disItem = new MatDispatchItem();
                        MatDispatch dis = new MatDispatch();
                        try {
                            dis = PstMatDispatch.fetchExc(oidMatDispatch);
                        } catch (Exception exc){
                            
                        }
                        disItem = PstMatDispatchItem.getObjectDispatchItem(matDispatchItem.getMaterialId(), oidMatDispatch);
                        
                        if (disItem.getOID() != 0){
                            disItem.setQty(disItem.getQty()+1);
                            double qty = PstMaterialStock.cekQtyStock(matDispatchItem.getDispatchMaterialId(),matDispatchItem.getMaterialId(),matDispatch.getLocationId());
                            if(qty<(disItem.getQty())){
                                frmMatDispatchItem.addError(FrmMatDispatchItem.FRM_FIELD_MATERIAL_ID,"Qty stok tidak cukup!.");
                                msgString = "Qty stok tidak cukup!."; //getSystemMessage(RSLT_QTY_NULL);
                                return getControlMsgId(RSLT_QTY_NULL);
                            }
                            try {
                                pstMatDispatchItem.updateExc(disItem);
                                break;
                             } catch (Exception exc){
                                msgString = getSystemMessage(RSLT_UNKNOWN_ERROR);
                                return getControlMsgId(RSLT_UNKNOWN_ERROR);
                            }
                        } else {
                            msgString = getSystemMessage(RSLT_UNKNOWN_ERROR);
                            return getControlMsgId(RSLT_UNKNOWN_ERROR);
                        }
                    }
                }

                /**
                 * jika quantity yg akan didispctch memenuhi syarat, maka proses seperti biasa
                 * INSERT atau UPDATE sesuai dengan nilai OID dispstch
                 */
                if (matDispatchItem.getOID() == 0) {
                    try {
                        long oid = pstMatDispatchItem.insertExc(this.matDispatchItem);
                            
                            //by dyas
                            //tambah perintah untuk memanggil methods insertHistory
                            if(oid!=0){
                                long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatDispatch);
                                if(oidUserLast != 0 && oidUserLast != userID ){
                                    insertHistory(userID, nameUser, cmd, oidMatDispatch);
                                }
                            }
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
                        long oid = pstMatDispatchItem.updateExc(this.matDispatchItem);

                        //by dyas
                        //tambah perintah untuk memanggil methods insertHistory
                        if(oid!=0){
                            int cmdHistory = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdHistory, oidMatDispatch);
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                /**
                 * Jika proses INSERT atau UPDATE berhasil (no exception), maka lakukan proses
                 * peng-update-an quantity sisa di receive berdasarkan NO INVOICE yg ada
                 */
                /* matReceiveItem.setResidueQty(qtymax - matDispatchItem.getQty());
                try{
                    PstMatReceiveItem.updateExc(matReceiveItem);
                }catch(Exception e){}

                // update transfer status
                PstMatReceive.processUpdate(matReceiveItem.getReceiveMaterialId());  */

                break;

            case Command.EDIT:
                if (oidMatDispatchItem != 0) {
                    try {
                        matDispatchItem = PstMatDispatchItem.fetchExc(oidMatDispatchItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatDispatchItem != 0) {
                    try {
                        matDispatchItem = PstMatDispatchItem.fetchExc(oidMatDispatchItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                //=================================================================
                try {
                        prevMatDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                        matDispatchItem = PstMatDispatchItem.fetchExc(oidMatDispatchItem);
                        prevMatDispatchItem = null;
                    } catch (Exception exc) {
                    }
                //================================================================

                if (oidMatDispatchItem != 0) {
                    /**
                     * Proses peng-update-an quantity sisa di dokumen receive kalau proses
                     * delete pada dokumen dispatch ini berhasil
                     */
                    MatDispatchItem matDispatchItem = new MatDispatchItem();
                    try {
                        matDispatchItem = PstMatDispatchItem.fetchExc(oidMatDispatchItem);
                    } catch (Exception e) {
                        System.out.println("Err when fetch matDispatchItem " + e.toString());
                    }

                    qtymax = matDispatchItem.getQty();

                    /* matDispatch = new MatDispatch();
                     try{
                         matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                     }catch(Exception e){}
                     matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matDispatch.getInvoiceSupplier(), 0,matDispatchItem.getMaterialId());
                     qtymax = qtymax + matReceiveItem.getResidueQty();*/

                    //by dyas
                    //tambah insertHistory
                    //untuk memanggil methods insertHistory
                    try {
                        SessPosting.deleteUpdateStockCode(0, 0, oidMatDispatchItem, SessPosting.DOC_TYPE_DISPATCH);
                        long oid = PstMatDispatchItem.deleteExc(oidMatDispatchItem);
                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oidMatDispatch);
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                    // update receive item
                    /*matReceiveItem.setResidueQty(qtymax);
                    try{
                        PstMatReceiveItem.updateExc(matReceiveItem);
                    }catch(Exception e){}

                    // update transfer status
                    PstMatReceive.processUpdate(matReceiveItem.getReceiveMaterialId()); */
                }
                break;

            default :
                break;
        }
        return rsCode;
    }

    //by dyas
    //tambah methods insertHistory
    //untuk men-set data yang nantinya akan diinputkan ke logHistory
    private void insertHistory(long userID, String nameUser, int cmd, long oidMatDispatch) {
        try{
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("material/dispatch/df_stock_wh_material_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(prevMatDispatch.getDispatchCode());
            logSysHistory.setLogDocumentId(oidMatDispatch);

            logSysHistory.setLogDetail(this.matDispatchItem.getLogDetail(prevMatDispatchItem));

            if(!logSysHistory.getLogDetail().equals("") || cmd==Command.DELETE)
            {
                 long oid2 = PstLogSysHistory.insertLog(logSysHistory);
}
            }
       catch(Exception e)
       {
       }
    }
}
