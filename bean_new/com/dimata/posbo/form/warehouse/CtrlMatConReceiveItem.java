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
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
/* project package */
//import com.dimata.garment.db.*;
//import com.dimata.garment.entity.warehouse.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.masterdata.SessPosting;
import com.dimata.posbo.session.warehouse.SessForwarderInfo;

public class CtrlMatConReceiveItem extends Control implements I_Language {
    
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
    private MatConReceiveItem matReceiveItem;
    private PstMatConReceiveItem pstMatConReceiveItem;
    private FrmMatConReceiveItem frmMatConReceiveItem;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlMatConReceiveItem() {
    }
    
    public CtrlMatConReceiveItem(HttpServletRequest request) {
        msgString = "";
        matReceiveItem = new MatConReceiveItem();
        try {
            pstMatConReceiveItem = new PstMatConReceiveItem(0);
        } catch (Exception e) {
            ;
        }
        frmMatConReceiveItem = new FrmMatConReceiveItem(request, matReceiveItem);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                this.frmMatConReceiveItem.addError(frmMatConReceiveItem.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_MATERIAL_EXIST]);
                return resultText[language][RSLT_MATERIAL_EXIST];
            case RSLT_QTY_NULL:
                this.frmMatConReceiveItem.addError(frmMatConReceiveItem.FRM_FIELD_QTY, resultText[language][RSLT_QTY_NULL]);
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
    
    public MatConReceiveItem getMatConReceiveItem() {
        return matReceiveItem;
    }
    
    public FrmMatConReceiveItem getForm() {
        return frmMatConReceiveItem;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    
    
    /**
     * @param cmd
     * @param oidMatConReceiveItem
     * @param oidMatConReceive
     * @return
     * @created <CODE>on Jan 30, 2004</CODE>
     * @created <CODE>by Gedhy</CODE>
     */
    synchronized public int action(int cmd, long oidMatConReceiveItem, long oidMatConReceive) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
                
            case Command.SAVE:
                double qtymax = 0;
                if (oidMatConReceiveItem != 0) {
                    try {
                        matReceiveItem = PstMatConReceiveItem.fetchExc(oidMatConReceiveItem);
                        qtymax = matReceiveItem.getQty();
                    } catch (Exception exc) {
                    }
                }
                frmMatConReceiveItem.requestEntityObject(matReceiveItem);
                matReceiveItem.setResidueQty(matReceiveItem.getQty());
                matReceiveItem.setReceiveMaterialId(oidMatConReceive);
                
                // check if current material already exist in orderMaterial
                if (matReceiveItem.getOID() == 0 && PstMatConReceiveItem.materialExist(matReceiveItem.getMaterialId(), oidMatConReceive)) {
                    msgString = getSystemMessage(RSLT_MATERIAL_EXIST);
                    return getControlMsgId(RSLT_MATERIAL_EXIST);
                }
                
                /**
                 * check if current material already exist in orderMaterial
                 * @created <CODE>by Gedhy</CODE>
                 */
                if (matReceiveItem.getQty() == 0) {
                    msgString = getSystemMessage(RSLT_QTY_NULL);
                    return getControlMsgId(RSLT_QTY_NULL);
                }
                
                if (frmMatConReceiveItem.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                
                // untuk cek document receive
                MatConReceive matReceive = new MatConReceive();
                try {
                    matReceive = PstMatConReceive.fetchExc(oidMatConReceive);
                } catch (Exception e) {
                }
                
                switch (matReceive.getReceiveSource()) {
                    /**
                     * kalo tipe receive adalah dari return toko
                     * proses ini dilakukan di WAREHOUSE
                     * @created <CODE>by Edhy</CODE>
                     */
                    case PstMatConReceive.SOURCE_FROM_RETURN:
                        MatConReturnItem matReturnItem = PstMatConReturnItem.getObjectReturnItem(matReceiveItem.getMaterialId(), matReceive.getReturnMaterialId());
                        
                        qtymax = qtymax + matReturnItem.getResidueQty();
                        System.out.println("===>>> SISA QTY : " + qtymax);
                        if (matReceiveItem.getQty() > qtymax) {
                            frmMatConReceiveItem.addError(0, "");
                            msgString = "maksimal qty adalah =" + qtymax;
                            return RSLT_FORM_INCOMPLETE;
                        }
                        
                        if (matReceiveItem.getOID() == 0) {
                            try {
                                oidMatConReceiveItem = pstMatConReceiveItem.insertExc(this.matReceiveItem);
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
                                oidMatConReceiveItem = pstMatConReceiveItem.updateExc(this.matReceiveItem);
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }
                            
                        }
                        
                        matReturnItem.setResidueQty(qtymax - matReceiveItem.getQty());
                        try {
                            PstMatConReturnItem.updateExc(matReturnItem);
                        } catch (Exception e) {
                        }
                        
                        // update transfer status 
                        PstMatConReturn.processUpdate(matReceive.getReturnMaterialId());
                        
                        // proses serial code
                        PstReceiveStockCode.getInsertSerialFromReturn(oidMatConReceiveItem,matReceive.getReturnMaterialId(),matReceiveItem.getMaterialId());
                        break;
                        
                        /**
                         * kalo tipe receive adalah dari dispatch warehouse
                         * proses ini dilakukan di STORE
                         * @created <CODE>by Edhy</CODE>
                         */
                    case PstMatConReceive.SOURCE_FROM_DISPATCH:
                        MatConDispatchItem matDispatchItem = PstMatConDispatchItem.getObjectDispatchItem(matReceiveItem.getMaterialId(), matReceive.getDispatchMaterialId());
                        
                        qtymax = qtymax + matDispatchItem.getResidueQty();
                        System.out.println("===>>> SISA QTY : " + qtymax);
                        if (matReceiveItem.getQty() > qtymax) {
                            frmMatConReceiveItem.addError(0, "");
                            msgString = "maksimal qty adalah =" + qtymax;
                            return RSLT_FORM_INCOMPLETE;
                        }
                        
                        if (matReceiveItem.getOID() == 0) {
                            try {
                                long oid = pstMatConReceiveItem.insertExc(this.matReceiveItem);
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
                                long oid = pstMatConReceiveItem.updateExc(this.matReceiveItem);
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }
                            
                        }
                        
                        matDispatchItem.setResidueQty(qtymax - matReceiveItem.getQty());
                        try {
                            PstMatConDispatchItem.updateExc(matDispatchItem);
                        } catch (Exception e) {
                        }
                        
                        // update transfer status
                        PstMatConDispatch.processUpdate(matReceive.getDispatchMaterialId());
                        
                        break;
                        
                        
                    default :
                        if (matReceiveItem.getOID() == 0) {
                            try {
                                long oid = pstMatConReceiveItem.insertExc(this.matReceiveItem);
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
                                long oid = pstMatConReceiveItem.updateExc(this.matReceiveItem);
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }
                            
                        }
                        
                        /** set total cost pada forwarder info dengan value terkini! */
                        try {System.out.println("oidMatConReceiveItem >>> "+this.matReceiveItem.getReceiveMaterialId());
                            String whereClause = ""+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID]+"="+this.matReceiveItem.getReceiveMaterialId();
                            Vector vctListFi = PstForwarderInfo.list(0, 0, whereClause, "");
                            ForwarderInfo forwarderInfo = new ForwarderInfo();
                            for(int j=0; j<vctListFi.size(); j++) {
                                forwarderInfo = (ForwarderInfo)vctListFi.get(j);
                                forwarderInfo.setTotalCost(SessForwarderInfo.getTotalCost(this.matReceiveItem.getReceiveMaterialId()));
                                System.out.println("totalCost >> "+forwarderInfo.getTotalCost());
                                long oid = PstForwarderInfo.updateExc(forwarderInfo);
                            }
                        }
                        catch(Exception e) {
                            System.out.println("Exc in update total_cost, forwarder_info >>> "+e.toString());
                        }
                        
                        break;
                }
                
                break;
                
            case Command.EDIT:
                if (oidMatConReceiveItem != 0) {
                    try {
                        matReceiveItem = PstMatConReceiveItem.fetchExc(oidMatConReceiveItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK:
                if (oidMatConReceiveItem != 0) {
                    try {
                        matReceiveItem = PstMatConReceiveItem.fetchExc(oidMatConReceiveItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE:
                if (oidMatConReceiveItem != 0) {
                    
                    MatConReceive matRec = new MatConReceive();
                    MatConReceiveItem matReceiveItem = new MatConReceiveItem();
                    try {
                        matRec = PstMatConReceive.fetchExc(oidMatConReceive);
                        matReceiveItem = PstMatConReceiveItem.fetchExc(oidMatConReceiveItem);
                    } catch (Exception e) {
                        System.out.println("Err when fetch matReceive and matReceiveItem : " + e.toString());
                    }
                    qtymax = matReceiveItem.getQty();
                    
                    try {
                        // untuk penghapusan code di stock code
                        SessPosting.deleteUpdateStockCode(0,0,oidMatConReceiveItem,SessPosting.DOC_TYPE_RECEIVE);
                        long oid = PstMatConReceiveItem.deleteExc(oidMatConReceiveItem);
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
                    
                    switch (matRec.getReceiveSource()) {
                        
                        /**
                         * kalo tipe receive adalah dari return toko
                         * proses ini dilakukan di WAREHOUSE
                         * @created <CODE>by Edhy</CODE>
                         */
                        case PstMatConReceive.SOURCE_FROM_RETURN:
                            MatConReturnItem matReturnItem = new MatConReturnItem();
                            try {
                                matReturnItem = PstMatConReturnItem.getObjectReturnItem(matReceiveItem.getMaterialId(), matRec.getReturnMaterialId());
                            } catch (Exception e) {
                                System.out.println("Err when fetch matReturnItem : " + e.toString());
                            }
                            
                            qtymax = qtymax + matReturnItem.getResidueQty();
                            matReturnItem.setResidueQty(qtymax);
                            
                            try {
                                PstMatConReturnItem.updateExc(matReturnItem);
                            } catch (Exception e) {
                                System.out.println("Err when update MatConReturnItem : " + e.toString());
                            }
                            
                            // update status transfer
                            PstMatConReturn.processUpdate(matRec.getReturnMaterialId());
                            
                            break;
                            
                            /**
                             * kalo tipe receive adalah dari dispatch warehouse
                             * proses ini dilakukan di STORE
                             * @created <CODE>by Edhy</CODE>
                             */
                        case PstMatConReceive.SOURCE_FROM_DISPATCH:
                            MatConDispatchItem matDispatchItem = new MatConDispatchItem();
                            try {
                                matDispatchItem = PstMatConDispatchItem.getObjectDispatchItem(matReceiveItem.getMaterialId(), matRec.getDispatchMaterialId());
                            } catch (Exception e) {
                                System.out.println("Err when fetch MatConDispatchItem : " + e.toString());
                            }
                            
                            qtymax = qtymax + matDispatchItem.getResidueQty();
                            matDispatchItem.setResidueQty(qtymax);
                            
                            try {
                                PstMatConDispatchItem.updateExc(matDispatchItem);
                            } catch (Exception e) {
                                System.out.println("Err when update MatConDispatchItem : " + e.toString());
                            }
                            
                            // update status transfer
                            PstMatConDispatch.processUpdate(matRec.getDispatchMaterialId());
                            
                            break;
                    }
                }
                
                break;
                
            default :
                break;
                
        }
        return rsCode;
    }
}
