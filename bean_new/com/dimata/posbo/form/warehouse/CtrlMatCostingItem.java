package com.dimata.posbo.form.warehouse;

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;

import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.posbo.session.masterdata.SessPosting;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.posbo.entity.warehouse.MatCostingItem;
import com.dimata.posbo.db.DBException;

import com.dimata.posbo.entity.masterdata.PstMaterialComposit;
import com.dimata.posbo.entity.masterdata.MaterialComposit;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.warehouse.MatCosting;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.qdep.entity.I_DocType;
import java.util.Date;


import javax.servlet.http.HttpServletRequest;

import java.util.Vector;

public class CtrlMatCostingItem extends Control implements I_Language {

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
    private MatCostingItem matCostingItem;
    private PstMatCostingItem pstMatCostingItem;
    private FrmMatCostingItem frmMatCostingItem;
    private Date dateLog = new Date();
    MatCostingItem prevMatCostingItem = null;
    MatCosting prevMatCosting = null;
    int language = LANGUAGE_DEFAULT;

    public CtrlMatCostingItem() {
    }

    public CtrlMatCostingItem(HttpServletRequest request) {
        msgString = "";
        matCostingItem = new MatCostingItem();
        try {
            pstMatCostingItem = new PstMatCostingItem(0);
        } catch (Exception e) {
        }
        frmMatCostingItem = new FrmMatCostingItem(request, matCostingItem);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                this.frmMatCostingItem.addError(frmMatCostingItem.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_MATERIAL_EXIST]);
                return resultText[language][RSLT_MATERIAL_EXIST];
            case RSLT_QTY_NULL:
                this.frmMatCostingItem.addError(frmMatCostingItem.FRM_FIELD_QTY, resultText[language][RSLT_QTY_NULL]);
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

    public MatCostingItem getMatCostingItem() {
        return matCostingItem;
    }

    public FrmMatCostingItem getForm() {
        return frmMatCostingItem;
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
     * @param <CODE>oidMatCostingItem</CODE>
     * @param oidMatDispatch
     * @return
     */

//    by dyas - 20131126
//    tambah methods action dengan 3 variabel
    public int action(int cmd, long oidMatCostingItem, long oidMatCosting){
     return action(cmd,oidMatCostingItem, oidMatCosting, "", 0);
    }

//    by dyas - 20131126
//    tambah methods action dengan 5 variabel
    synchronized public int action(int cmd, long oidMatCostingItem, long oidMatCosting, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                double qtymax = 0;
                if (oidMatCostingItem != 0) {
                    try {
                        matCostingItem = PstMatCostingItem.fetchExc(oidMatCostingItem);
                        qtymax = matCostingItem.getQty();
                    } catch (Exception exc) {
                    }

                    // by dyas - 20131126
                    // tambah try catch
                    // untuk mem-fetch oidMatCostingItem dan menyimpannya pada variabel prevMatCostingItem
                    try {
                        prevMatCostingItem = PstMatCostingItem.fetchExc(oidMatCostingItem);
                    } catch (Exception exc) {
                }
                }

                frmMatCostingItem.requestEntityObject(matCostingItem);
                //matCostingItem.setResidueQty(matCostingItem.getQty());
                matCostingItem.setCostingMaterialId(oidMatCosting);

                if (frmMatCostingItem.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }


                /**
                 * check if current material already exist in orderMaterial
                 * @created <CODE>by Gedhy</CODE>
                 */
                /**
                 * disini di open saja, bisa input qty jika 0
                 */
//                if (matCostingItem.getQty() == 0) {
//                    msgString = getSystemMessage(RSLT_QTY_NULL);
//                    return getControlMsgId(RSLT_QTY_NULL);
//                }


                // by dyas - 20131126
                // tambah kondisi
                // untuk mengecek jika data tidak kosong dan oidMatCosting tidak sama dengan 0
                // maka akan dilakukan pencarian data pada PstMatCosting berdasarkan oidMatCosting dan hasilnya disimpan pada variabel prevMatCosting
                if(this!=null && oidMatCosting!=0 ){
                    try {
                         prevMatCosting = PstMatCosting.fetchExc(oidMatCosting);
                    } catch (Exception e) {
                    }
                }

                /**
                 * ini di pakai untuk mencari objeck dispatch
                 * dan di pakai untuk mencari object receive item
                 * untuk mengetahui nilai sisa yang sebenarnya.
                 */
                /* MatCosting matCosting = new MatCosting();
                 try {
                     matCosting = PstMatCosting.fetchExc(oidMatCosting);
                 } catch (Exception e) {
                 }
                 MatReceiveItem matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matCosting.getInvoiceSupplier(), 0, matCostingItem.getMaterialId());

                 qtymax = qtymax + matReceiveItem.getResidueQty();
                 System.out.println("===>>> SISA QTY : " + qtymax);

                 // jika quantity yg akan didispatch melebihi maximum yg ada, maka return error
                 if (matCostingItem.getQty() > qtymax) {
                     frmMatCostingItem.addError(0, "");
                     msgString = "maksimal qty adalah =" + qtymax;
                     return RSLT_FORM_INCOMPLETE;
                 } */

                //check tipe material composit atau bukan
                double qtyCostingItem = matCostingItem.getQty();


             

                /**
                 * jika quantity yg akan didispctch memenuhi syarat, maka proses seperti biasa
                 * INSERT atau UPDATE sesuai dengan nilai OID dispstch
                 */
                if (matCostingItem.getOID() == 0) {
                    
                    try {
                        
                     Vector componentComposit = new Vector();
                     componentComposit = PstMaterialComposit.ListComponentComposit(matCostingItem.getMaterialId());   
                     
                     if(componentComposit !=null && componentComposit.size()>0) {
                        //simpan record pertama
                        matCostingItem.setQtyComposite(matCostingItem.getQty()); 
                        matCostingItem.setParentId(0);
                        matCostingItem.setQty(0);
                        long parentMaterialId = pstMatCostingItem.insertExc(this.matCostingItem);
                        
                        for(int i = 0; i<componentComposit.size(); i++){
                             Vector temp = (Vector)componentComposit.get(i);
                             MaterialComposit matComposit = (MaterialComposit)temp.get(0);
                             Material material = (Material)temp.get(1);
                             matCostingItem.setMaterialId(matComposit.getMaterialComposerId());
                             matCostingItem.setUnitId(matComposit.getUnitId());
                             double qtyComposit = matComposit.getQty();
                             double qtyAll = qtyCostingItem * qtyComposit;
                             matCostingItem.setQty(qtyAll);
                             matCostingItem.setHpp(material.getAveragePrice());
                             matCostingItem.setQtyComposite(0); 
                             matCostingItem.setParentId(parentMaterialId);
                              try {
                                long oid = pstMatCostingItem.insertExc(this.matCostingItem);
                                // by dyas - 20131126
                                // tambah kondisi
                                // untuk melakukan pengecekan oid dan user yang sedang login dan memanggil mehtods insertHistory
                                if(oid!=0){
                                    long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatCosting);
                                    if(oidUserLast != 0 && oidUserLast != userID ){
                                        insertHistory(userID, nameUser, cmd, oidMatCosting);
                                    }
                                }
//                             } catch ( DBException dbexc) {
//                             excCode = dbexc.getErrorCode();
//                             msgString = getSystemMessage(excCode);
//                             return getControlMsgId(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                            }
                       }

                    }else {
                         
                        matCostingItem.setQtyComposite(0); 
                        matCostingItem.setParentId(0);
                        long oid = pstMatCostingItem.insertExc(this.matCostingItem);

                        // by dyas - 20131126
                        // tambah kondisi
                        // untuk melakukan pengecekan oid dan user yang sedang login dan memanggil mehtods insertHistory
                        if(oid!=0){
                            long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatCosting);
                            if(oidUserLast != 0 && oidUserLast != userID ){
                                insertHistory(userID, nameUser, cmd, oidMatCosting);
                     }
                        }
                     }

                       

//                    } catch ( DBException dbexc) {
//                        excCode = dbexc.getErrorCode();
//                        msgString = getSystemMessage(excCode);
//                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    
                    try {
                        long oid=0;
                        Vector componentComposit = new Vector();
                        componentComposit = PstMaterialComposit.ListComponentComposit(matCostingItem.getMaterialId());
                        if(componentComposit !=null && componentComposit.size()>0) {
                            //delete yang sebelumnya
                            if(matCostingItem.getOID()!=0){
                                long oidx = pstMatCostingItem.deleteComponentByParentId(matCostingItem.getOID());
                                long parentMaterialId = matCostingItem.getOID();
                                matCostingItem.setQtyComposite(matCostingItem.getQty()); 
                                matCostingItem.setParentId(0);
                                matCostingItem.setQty(0);
                                oid = pstMatCostingItem.updateExc(this.matCostingItem);

                                //kemudian inputkan kembali
                                for(int i = 0; i<componentComposit.size(); i++){
                                     Vector temp = (Vector)componentComposit.get(i);
                                     MaterialComposit matComposit = (MaterialComposit)temp.get(0);
                                     Material material = (Material)temp.get(1);
                                     matCostingItem.setMaterialId(matComposit.getMaterialComposerId());
                                     matCostingItem.setUnitId(matComposit.getUnitId());
                                     double qtyComposit = matComposit.getQty();
                                     double qtyAll = qtyCostingItem * qtyComposit;
                                     matCostingItem.setQty(qtyAll);
                                     matCostingItem.setHpp(material.getAveragePrice());
                                     matCostingItem.setQtyComposite(0); 
                                     matCostingItem.setParentId(parentMaterialId);
                                      try {
                                        long oidcom = pstMatCostingItem.insertExc(this.matCostingItem);

                                        // by dyas - 20131126
                                        // tambah kondisi
                                        // untuk melakukan pengecekan oid dan user yang sedang login dan memanggil mehtods insertHistory
                                        if(oid!=0){
                                            long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatCosting);
                                            if(oidUserLast != 0 && oidUserLast != userID ){
                                                insertHistory(userID, nameUser, cmd, oidMatCosting);
                                            }
                                        }
    //                                 } catch ( DBException dbexc) {
    //                                 excCode = dbexc.getErrorCode();
    //                                 msgString = getSystemMessage(excCode);
    //                                 return getControlMsgId(excCode);
                                    } catch (Exception exc) {
                                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                                    }
                                }
                            }
                        }else{
                            matCostingItem.setQtyComposite(0); 
                            matCostingItem.setParentId(0);
                            oid = pstMatCostingItem.updateExc(this.matCostingItem);
                        }
                        // by dyas - 20131126
                        // tambah kondisi
                        // untuk melakukan pengecekan oid dan memanggil mehtods insertHistory
                        if(oid!=0){
                            int cmdHistory = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdHistory, oidMatCosting);
                        }
//                    } catch (DBException dbexc) {
//                        excCode = dbexc.getErrorCode();
//                        msgString = getSystemMessage(excCode);
//                       return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                /**
                 * Jika proses INSERT atau UPDATE berhasil (no exception), maka lakukan proses
                 * peng-update-an quantity sisa di receive berdasarkan NO INVOICE yg ada
                 */
                /*matReceiveItem.setResidueQty(qtymax - matCostingItem.getQty());
                try {
                    PstMatReceiveItem.updateExc(matReceiveItem);
                } catch (Exception e) {
                }

                // update transfer status
                PstMatReceive.processUpdate(matReceiveItem.getReceiveMaterialId());*/

                break;

            case Command.EDIT:
                if (oidMatCostingItem != 0) {
                    try {
                        matCostingItem = PstMatCostingItem.fetchExc(oidMatCostingItem);
//                    } catch (DBException dbexc) {
//                        excCode = dbexc.getErrorCode();
//                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatCostingItem != 0) {
                    try {
                        matCostingItem = PstMatCostingItem.fetchExc(oidMatCostingItem);
//                    } catch (DBException dbexc) {
//                        excCode = dbexc.getErrorCode();
//                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                //=================================================================
                try {
                        prevMatCosting = PstMatCosting.fetchExc(oidMatCosting);
                        matCostingItem = PstMatCostingItem.fetchExc(oidMatCostingItem);
                        prevMatCostingItem = null;
                    } catch (Exception exc) {
                    }
                //================================================================
                if (oidMatCostingItem != 0) {
                    /**
                     * Proses peng-update-an quantity sisa di dokumen receive kalau proses
                     * delete pada dokumen dispatch ini berhasil
                     */
                    MatCostingItem matCostingItem = new MatCostingItem();
                    try {
                        matCostingItem = PstMatCostingItem.fetchExc(oidMatCostingItem);
                    } catch (Exception e) {
                        System.out.println("Err when fetch matCostingItem " + e.toString());
                    }

                    /*qtymax = matCostingItem.getQty();

                    matCosting = new MatCosting();
                    try {
                        matCosting = PstMatCosting.fetchExc(oidMatCosting);
                    } catch (Exception e) {
                    }
                    matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matCosting.getInvoiceSupplier(), 0, matCostingItem.getMaterialId());
                    qtymax = qtymax + matReceiveItem.getResidueQty();*/
                    try {
                        if(matCostingItem.getOID()!=0){
                            long oidx = pstMatCostingItem.deleteComponentByParentId(matCostingItem.getOID());
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if(oidMatCostingItem!=0){
                            SessPosting.deleteUpdateStockCode(0, 0, oidMatCostingItem, SessPosting.DOC_TYPE_COSTING);
                            long oid = PstMatCostingItem.deleteExc(oidMatCostingItem);
                            if (oid != 0) {
                                msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                excCode = RSLT_OK;

                                // by dyas - 20131126
                                // tambah code untuk memanggil mehtods insertHistory
                                insertHistory(userID, nameUser, cmd, oidMatCosting);
                            } else {
                                msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                excCode = RSLT_FORM_INCOMPLETE;
                            }
                        }
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default :
                break;
        }
        return rsCode;
    }

    // by dyas - 20131126
    // tambah mehtods insertHistory
    private void insertHistory(long userID, String nameUser, int cmd, long oidMatCosting) {
        try{
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("material/dispatch/costing_material_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(prevMatCosting.getCostingCode());
            logSysHistory.setLogDocumentId(oidMatCosting);

            logSysHistory.setLogDetail(this.matCostingItem.getLogDetail(prevMatCostingItem));

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
