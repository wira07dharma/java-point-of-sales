package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.session.masterdata.SessPosting;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.posbo.db.DBException;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;

import javax.servlet.http.HttpServletRequest;

public class CtrlMatConDispatchItem extends Control implements I_Language {

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
    private MatConDispatchItem matDispatchItem;
    private PstMatConDispatchItem pstMatConDispatchItem;
    private FrmMatConDispatchItem frmMatConDispatchItem;
    int language = LANGUAGE_DEFAULT;

    public CtrlMatConDispatchItem() {
    }

    public CtrlMatConDispatchItem(HttpServletRequest request) {
        msgString = "";
        matDispatchItem = new MatConDispatchItem();
        try {
            pstMatConDispatchItem = new PstMatConDispatchItem(0);
        } catch (Exception e) {
        }
        frmMatConDispatchItem = new FrmMatConDispatchItem(request, matDispatchItem);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                this.frmMatConDispatchItem.addError(frmMatConDispatchItem.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_MATERIAL_EXIST]);
                return resultText[language][RSLT_MATERIAL_EXIST];
            case RSLT_QTY_NULL:
                this.frmMatConDispatchItem.addError(frmMatConDispatchItem.FRM_FIELD_QTY, resultText[language][RSLT_QTY_NULL]);
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

    public MatConDispatchItem getMatConDispatchItem() {
        return matDispatchItem;
    }

    public FrmMatConDispatchItem getForm() {
        return frmMatConDispatchItem;
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
     * @param <CODE>oidMatConDispatchItem</CODE>
     * @param oidMatConDispatch
     * @return
     */
    synchronized public int action(int cmd, long oidMatConDispatchItem, long oidMatConDispatch) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                double qtymax = 0;
                if (oidMatConDispatchItem != 0) {
                    try {
                        matDispatchItem = PstMatConDispatchItem.fetchExc(oidMatConDispatchItem);
                        qtymax = matDispatchItem.getQty();
                    } catch (Exception exc) {
                    }
                }

                frmMatConDispatchItem.requestEntityObject(matDispatchItem);System.out.println(matDispatchItem.getQty());
                matDispatchItem.setResidueQty(matDispatchItem.getQty());
                matDispatchItem.setDispatchMaterialId(oidMatConDispatch);

                if (frmMatConDispatchItem.errorSize() > 0) {
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
                MatConDispatch matDispatch = new MatConDispatch();
                try{
                    matDispatch = PstMatConDispatch.fetchExc(oidMatConDispatch);
                }catch(Exception e){}
                /*MatReceiveItem matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matDispatch.getInvoiceSupplier(), 0,matDispatchItem.getMaterialId());

                qtymax = qtymax + matReceiveItem.getResidueQty();
                System.out.println("===>>> SISA QTY : "+qtymax);

                // jika quantity yg akan didispatch melebihi maximum yg ada, maka return error
                if(matDispatchItem.getQty() > qtymax){
                    frmMatConDispatchItem.addError(0,"");
                    msgString = "maksimal qty adalah ="+qtymax;
                    return RSLT_FORM_INCOMPLETE ;
                } */

                //double qty = PstMaterialStock.cekQtyStock(matDispatchItem.getDispatchMaterialId(),matDispatchItem.getMaterialId(),matDispatch.getLocationId());
                //if(qty<matDispatchItem.getQty()){
                //    frmMatConDispatchItem.addError(FrmMatConDispatchItem.FRM_FIELD_MATERIAL_ID,"Qty stok tidak cukup!.");
                //    msgString = "Qty stok tidak cukup!."; //getSystemMessage(RSLT_QTY_NULL);
                //    return getControlMsgId(RSLT_QTY_NULL);
                //}

                /**
                 * jika quantity yg akan didispctch memenuhi syarat, maka proses seperti biasa
                 * INSERT atau UPDATE sesuai dengan nilai OID dispstch
                 */
                if (matDispatchItem.getOID() == 0) {
                    try {
                        long oid = pstMatConDispatchItem.insertExc(this.matDispatchItem);

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
                        long oid = pstMatConDispatchItem.updateExc(this.matDispatchItem);
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
                if (oidMatConDispatchItem != 0) {
                    try {
                        matDispatchItem = PstMatConDispatchItem.fetchExc(oidMatConDispatchItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatConDispatchItem != 0) {
                    try {
                        matDispatchItem = PstMatConDispatchItem.fetchExc(oidMatConDispatchItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatConDispatchItem != 0) {
                    /**
                     * Proses peng-update-an quantity sisa di dokumen receive kalau proses
                     * delete pada dokumen dispatch ini berhasil
                     */
                    MatConDispatchItem matDispatchItem = new MatConDispatchItem();
                    try {
                        matDispatchItem = PstMatConDispatchItem.fetchExc(oidMatConDispatchItem);
                    } catch (Exception e) {
                        System.out.println("Err when fetch matDispatchItem " + e.toString());
                    }

                    qtymax = matDispatchItem.getQty();

                    /* matDispatch = new MatConDispatch();
                     try{
                         matDispatch = PstMatConDispatch.fetchExc(oidMatConDispatch);
                     }catch(Exception e){}
                     matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matDispatch.getInvoiceSupplier(), 0,matDispatchItem.getMaterialId());
                     qtymax = qtymax + matReceiveItem.getResidueQty();*/

                    try {
                        SessPosting.deleteUpdateStockCode(0, 0, oidMatConDispatchItem, SessPosting.DOC_TYPE_DISPATCH);
                        long oid = PstMatConDispatchItem.deleteExc(oidMatConDispatchItem);
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
}
