package com.dimata.posbo.form.warehouse;

/* java package */

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
/* project package */
import com.dimata.common.entity.location.*;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.periode.Periode;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.posbo.session.masterdata.SessPosting;

public class CtrlMatConReturnItem extends Control implements I_Language {
    public static final int RSLT_OK = 0;
    public static final int RSLT_UNKNOWN_ERROR = 1;
    public static final int RSLT_MATERIAL_EXIST = 2;
    public static final int RSLT_FORM_INCOMPLETE = 3;
    public static final int RSLT_QTY_NULL = 4;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Material sudah ada", "Data tidak lengkap", "Jumlah material tidak boleh nol ..."},
        {"Succes", "Can not process", "Material already exist ...", "Data incomplete", "Quantity may not zero ..."}
    };
    
    private int start;
    private String msgString;
    private MatConReturnItem matReturnItem;
    private PstMatConReturnItem pstMatConReturnItem;
    private FrmMatConReturnItem frmMatConReturnItem;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlMatConReturnItem() {
    }
    
    public CtrlMatConReturnItem(HttpServletRequest request) {
        msgString = "";
        matReturnItem = new MatConReturnItem();
        try {
            pstMatConReturnItem = new PstMatConReturnItem(0);
        } catch (Exception e) {
            ;
        }
        frmMatConReturnItem = new FrmMatConReturnItem(request, matReturnItem);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                this.frmMatConReturnItem.addError(frmMatConReturnItem.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_MATERIAL_EXIST]);
                return resultText[language][RSLT_MATERIAL_EXIST];
            case RSLT_QTY_NULL:
                this.frmMatConReturnItem.addError(frmMatConReturnItem.FRM_FIELD_QTY, resultText[language][RSLT_QTY_NULL]);
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
    
    public MatConReturnItem getMatConReturnItem() {
        return matReturnItem;
    }
    
    public FrmMatConReturnItem getForm() {
        return frmMatConReturnItem;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
    synchronized public int action(int cmd, long oidMatConReturnItem, long oidMatConReturn) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
                
            case Command.SAVE:
                double qtymax = 0;
                if (oidMatConReturnItem != 0) {
                    try {
                        matReturnItem = PstMatConReturnItem.fetchExc(oidMatConReturnItem);
                        qtymax = matReturnItem.getQty();
                    } catch (Exception exc) {
                    }
                }
                
                frmMatConReturnItem.requestEntityObject(matReturnItem);
                matReturnItem.setResidueQty(matReturnItem.getQty());
                matReturnItem.setReturnMaterialId(oidMatConReturn);
                
                // check if current material already exist in orderMaterial
                /*if(matReturnItem.getOID()==0 && PstMatConReturnItem.materialExist(matReturnItem.getMaterialId(),oidMatConReturn)) {
                    msgString = getSystemMessage(RSLT_MATERIAL_EXIST);
                    return getControlMsgId(RSLT_MATERIAL_EXIST);
                }*/
                
                // check if current material already exist in orderMaterial
                if (matReturnItem.getQty() == 0) {
                    msgString = getSystemMessage(RSLT_QTY_NULL);
                    return getControlMsgId(RSLT_QTY_NULL);
                }
                
                
                if (frmMatConReturnItem.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                /**
                 * ini di pakai untuk mencari objeck dispatch
                 * dan di pakai untuk mencari object receive item
                 * untuk mengetahui nilai sisa yang sebenarnya.
                 */
                MatReceiveItem matReceiveItem = new MatReceiveItem();
                MatConReturn matReturn = new MatConReturn();
                try {
                    matReturn = PstMatConReturn.fetchExc(oidMatConReturn);
                } catch (Exception e) {
                }
                
                
                // jika invoice supplier kosong berarti return ini adalah tampa invoice
                // jadi cek qty ke stock, dan sebaliknya jika dengan invoice cek qty ke receive
              /*  if (matReturn.getInvoiceSupplier().equals("")) {
                    System.out.println("== >>> RETUR TAMPA INVOICE");
                    String whereClause = PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + "=" + PstPeriode.FLD_STATUS_RUNNING;
                    Vector vt = PstPeriode.list(0, 0, whereClause, "");
                    if (vt.size() > 0) {
                        Periode matPeriode = (Periode) vt.get(0);
               
                        whereClause = PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + matReturn.getLocationId() +
                                " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + matPeriode.getOID() +
                                " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + "=" + matReturnItem.getMaterialId();
               
                        Vector vtstock = PstMaterialStock.list(0, 0, whereClause, "");
               
                        Vector vtret = SessMatConReturn.getReturnItemOutInvoice(matReturn.getLocationId(), matReturn.getSupplierId());
                        int qtyret = 0;
                        if (vtret.size() > 0) {
                            for (int j = 0; j < vtret.size(); j++) {
                                MatConReturnItem matRetItem = (MatConReturnItem) vtret.get(j);
                                if (matRetItem.getMaterialId() == matReturnItem.getMaterialId()) {
                                    qtyret = matRetItem.getQty();
                                    break;
                                }
                            }
                        }
               
                        // ini di gunakan untuk store
                        // karena di store harus di kurangi juga dengan
                        // data penjualan
                        int qtysell = 0;
                        if (matReturn.getLocationType() == PstLocation.TYPE_LOCATION_STORE) {
                            qtysell = SessReportSale.getBillDetail(matReturnItem.getMaterialId(), matReturn.getLocationId(), I_DocStatus.DOCUMENT_STATUS_DRAFT);
                        }
               
                        // cari qty akhir setelah hasil pengurangan
                        if (vtstock.size() > 0) {
                            MaterialStock matStock = (MaterialStock) vtstock.get(0);
                            qtymax = matStock.getQty() - qtyret; // ((matStock.getQty() - qtysell) - ((qtyret - qtymax)));
                        }
                    }
                } else {*/
                if (!matReturn.getInvoiceSupplier().equals("")) {
                    System.out.println("== >>> RETUR DENGAN INVOICE ");
                    matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matReturn.getInvoiceSupplier(), matReturn.getReceiveMaterialId(), matReturnItem.getMaterialId());
                    qtymax = qtymax + matReceiveItem.getResidueQty();
                }
                //}
                
                System.out.println("===>>> SISA QTY : " + qtymax);
                /**
                 * jika qty melebihi qty maxsimal
                 * maka return error
                 */
                if (matReturnItem.getQty() > qtymax) {
                    //frmMatConReturnItem.addError(0, "");
                    //msgString = "maksimal qty adalah =" + qtymax;
                    //return RSLT_FORM_INCOMPLETE;
                }
                
                if (matReturnItem.getOID() == 0) {
                    try {
                        long oid = pstMatConReturnItem.insertExc(this.matReturnItem);
                        
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
                        long oid = pstMatConReturnItem.updateExc(this.matReturnItem);
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
                 * pengurangan pada object Receive Item karena proses INSERT / UPDATE sukses.
                 */
                if (!matReturn.getInvoiceSupplier().equals("")) {
                    matReceiveItem.setResidueQty(qtymax - matReturnItem.getQty());
                    try {
                        PstMatReceiveItem.updateExc(matReceiveItem);
                    } catch (Exception e) {
                    }
                    
                    // update transfer status
                    PstMatReceive.processUpdate(matReceiveItem.getReceiveMaterialId());
                }
                
                break;
                
            case Command.EDIT:
                if (oidMatConReturnItem != 0) {
                    try {
                        matReturnItem = PstMatConReturnItem.fetchExc(oidMatConReturnItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK:
                if (oidMatConReturnItem != 0) {
                    try {
                        matReturnItem = PstMatConReturnItem.fetchExc(oidMatConReturnItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE:
                if (oidMatConReturnItem != 0) {
                    try {
                        
                        MatConReturnItem matReturnItem = PstMatConReturnItem.fetchExc(oidMatConReturnItem);
                        qtymax = matReturnItem.getQty();
                        
                        matReturn = new MatConReturn();
                        try {
                            matReturn = PstMatConReturn.fetchExc(oidMatConReturn);
                        } catch (Exception e) {
                        }
                        matReceiveItem = PstMatReceiveItem.getObjectReceiveItem(matReturn.getInvoiceSupplier(), matReturn.getReceiveMaterialId(), matReturnItem.getMaterialId());
                        qtymax = qtymax + matReceiveItem.getResidueQty();
                        
                        // penghapusan serial code
                        SessPosting.deleteUpdateStockCode(0, 0, oidMatConReturnItem, SessPosting.DOC_TYPE_RETURN);
                        long oid = PstMatConReturnItem.deleteExc(oidMatConReturnItem);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                        
                        /**
                         * update status receive
                         */
                        if (excCode == RSLT_OK) {
                            if (!matReturn.getInvoiceSupplier().equals("")) {
                                // update receive item
                                matReceiveItem.setResidueQty(qtymax);
                                try {
                                    PstMatReceiveItem.updateExc(matReceiveItem);
                                } catch (Exception e) {
                                }
                                
                                // update transfer status
                                PstMatReceive.processUpdate(matReceiveItem.getReceiveMaterialId());
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
                    
                }
                break;
                
            default :
                
        }
        return rsCode;
    }
}
