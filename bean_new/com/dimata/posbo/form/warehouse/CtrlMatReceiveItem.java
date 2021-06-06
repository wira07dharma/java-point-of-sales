package com.dimata.posbo.form.warehouse;

/* java package */
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import java.util.*;
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
import com.dimata.posbo.entity.masterdata.PstMatVendorPrice;
import com.dimata.posbo.entity.masterdata.MatVendorPrice;
import com.dimata.common.entity.system.*;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialDetail;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialDetail;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_DocType;

public class CtrlMatReceiveItem extends Control implements I_Language {

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
    private MatReceiveItem matReceiveItem;
    private PstMatReceiveItem pstMatReceiveItem;
    private FrmMatReceiveItem frmMatReceiveItem;
    private PstReceiveStockCode pstReceiveStockCode;
    private Material mat;
    private Date dateLog = new Date();
    MatReceiveItem prevMatReceiveItem = null;
    MatReceive prevMatReceive = null;
    MatReceive matReceive = null;
    int language = LANGUAGE_DEFAULT;

    public CtrlMatReceiveItem() {
    }

    public CtrlMatReceiveItem(HttpServletRequest request) {
        msgString = "";
        matReceiveItem = new MatReceiveItem();
        try {
            pstMatReceiveItem = new PstMatReceiveItem(0);
        } catch (Exception e) {
            ;
        }
        frmMatReceiveItem = new FrmMatReceiveItem(request, matReceiveItem);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                this.frmMatReceiveItem.addError(frmMatReceiveItem.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_MATERIAL_EXIST]);
                return resultText[language][RSLT_MATERIAL_EXIST];
            case RSLT_QTY_NULL:
                this.frmMatReceiveItem.addError(frmMatReceiveItem.FRM_FIELD_QTY, resultText[language][RSLT_QTY_NULL]);
                return resultText[language][RSLT_QTY_NULL];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                return RSLT_MATERIAL_EXIST;
            case RSLT_QTY_NULL:
                return RSLT_QTY_NULL;
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

    public MatReceiveItem getMatReceiveItem() {
        return matReceiveItem;
    }

    public FrmMatReceiveItem getForm() {
        return frmMatReceiveItem;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    /**
     * @param cmd
     * @param oidMatReceiveItem
     * @param oidMatReceive
     * @return
     * @created <CODE>on Jan 30, 2004</CODE>
     * @created <CODE>by Gedhy</CODE>
     */
    public int action(int cmd, long oidMatReceiveItem, long oidMatReceive) {
        return action(cmd, oidMatReceiveItem, oidMatReceive, "", 0);
    }

    synchronized public int action(int cmd, long oidMatReceiveItem, long oidMatReceive, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                double qtymax = 0;
                if (oidMatReceiveItem != 0) {
                    try {
                        matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                        qtymax = matReceiveItem.getQty();
                    } catch (Exception exc) {
                    }

                    try {
                        prevMatReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                    } catch (Exception exc) {
                    }
                }
                frmMatReceiveItem.requestEntityObject(matReceiveItem);
                if (matReceiveItem.getExpiredDate() == null) {
                    //Date tgl = FRMQueryString.requestDateString(null, nameUser)
                }
                matReceiveItem.setResidueQty(matReceiveItem.getQty());
                matReceiveItem.setReceiveMaterialId(oidMatReceive);

                // check if current material already exist in orderMaterial
                if (matReceiveItem.getOID() == 0 && PstMatReceiveItem.materialExist(matReceiveItem.getMaterialId(), oidMatReceive)) {
                    //jika auto save penerimaan, maka di akumulasi
                    String autoSaveRec = PstSystemProperty.getValueByName("AUTO_SAVE_RECEIVING");
                    if (autoSaveRec.equals("1")){
                        MatReceiveItem recItem = new MatReceiveItem();
                        MatReceive rec = new MatReceive();
                        try {
                            rec = PstMatReceive.fetchExc(oidMatReceive);
                        } catch (Exception exc){
                            
                        }
                        recItem = PstMatReceiveItem.getObjectReceiveItem(rec.getInvoiceSupplier(), 0, matReceiveItem.getMaterialId());
                        
                        if (recItem.getOID() != 0){
                            recItem.setQty(recItem.getQty()+1);
                            recItem.setQtyEntry(recItem.getQtyEntry()+1);
                            try {
                                pstMatReceiveItem.updateExc(recItem);
                                break;
                             } catch (Exception exc){
                                msgString = getSystemMessage(RSLT_UNKNOWN_ERROR);
                                return getControlMsgId(RSLT_UNKNOWN_ERROR);
                            }
                        } else {
                            msgString = getSystemMessage(RSLT_UNKNOWN_ERROR);
                            return getControlMsgId(RSLT_UNKNOWN_ERROR);
                        }
                    } else {
                        msgString = getSystemMessage(RSLT_MATERIAL_EXIST);
                        return getControlMsgId(RSLT_MATERIAL_EXIST);
                    }
                }

                /**
                 * check if current material already exist in orderMaterial
                 *
                 * @created <CODE>by Gedhy</CODE>
                 */
                if (matReceiveItem.getQty() == 0) {
                    msgString = getSystemMessage(RSLT_QTY_NULL);
                    return getControlMsgId(RSLT_QTY_NULL);
                }

                if (frmMatReceiveItem.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                // untuk cek document receive
                MatReceive matReceive = new MatReceive();
                try {
                    matReceive = PstMatReceive.fetchExc(oidMatReceive);
                } catch (Exception e) {
                }

                //update opie-eyek jika ada ratenya
                /*if(matReceive.getTransRate()>1){
                 matReceiveItem.setCost(matReceiveItem.getCost()*matReceive.getTransRate());
                 matReceiveItem.setCurrBuyingPrice(matReceiveItem.getCurrBuyingPrice()*matReceive.getTransRate());
                 matReceiveItem.setDiscNominal(matReceiveItem.getDiscNominal()*matReceive.getTransRate());
                 matReceiveItem.setDiscount(matReceiveItem.getDiscount()* matReceive.getTransRate());
                 matReceiveItem.setDiscount2(matReceiveItem.getDiscount2()*matReceive.getTransRate());
                 matReceiveItem.setForwarderCost(matReceiveItem.getForwarderCost()*matReceive.getTransRate());
                 matReceiveItem.setTotal(matReceiveItem.getTotal()*matReceive.getTransRate());
                 //matReceiveItem.
                 }*/
                if (this != null && oidMatReceive != 0) {
                    try {
                        prevMatReceive = PstMatReceive.fetchExc(oidMatReceive);
                    } catch (Exception e) {
                    }
                }
                int calculateStock = Integer.valueOf(PstSystemProperty.getValueByName("CALCULATE_STOCK_VALUE"));

                switch (matReceive.getReceiveSource()) {
                    /**
                     * kalo tipe receive adalah dari return toko proses ini
                     * dilakukan di WAREHOUSE
                     *
                     * @created <CODE>by Edhy</CODE>
                     */
                    case PstMatReceive.SOURCE_FROM_RETURN:
                        MatReturnItem matReturnItem = PstMatReturnItem.getObjectReturnItem(matReceiveItem.getMaterialId(), matReceive.getReturnMaterialId());

                        qtymax = qtymax + matReturnItem.getResidueQty();
                        System.out.println("===>>> SISA QTY : " + qtymax);
                        if (matReceiveItem.getQty() > qtymax) {
                            frmMatReceiveItem.addError(0, "");
                            msgString = "maksimal qty adalah =" + qtymax;
                            return RSLT_FORM_INCOMPLETE;
                        }

                        if (matReceiveItem.getOID() == 0) {
                            try {
                                oidMatReceiveItem = pstMatReceiveItem.insertExc(this.matReceiveItem);

                                if (oidMatReceiveItem != 0) {
                                    long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatReceive);
                                    if (oidUserLast != 0 && oidUserLast != userID) {
                                        insertHistory(userID, nameUser, cmd, oidMatReceive);
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
                                oidMatReceiveItem = pstMatReceiveItem.updateExc(this.matReceiveItem);

                                if (oidMatReceiveItem != 0) {
                                    int cmdHistory = Command.UPDATE;
                                    insertHistory(userID, nameUser, cmdHistory, oidMatReceive);
                                }
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }

                        matReturnItem.setResidueQty(qtymax - matReceiveItem.getQty());
                        try {
                            PstMatReturnItem.updateExc(matReturnItem);
                        } catch (Exception e) {
                        }

                        // update transfer status
                        PstMatReturn.processUpdate(matReceive.getReturnMaterialId());

                        // proses serial code
                        PstReceiveStockCode.getInsertSerialFromReturn(oidMatReceiveItem, matReceive.getReturnMaterialId(), matReceiveItem.getMaterialId());
                        break;

                    /**
                     * kalo tipe receive adalah dari dispatch warehouse proses
                     * ini dilakukan di STORE
                     *
                     * @created <CODE>by Edhy</CODE>
                     */
                    case PstMatReceive.SOURCE_FROM_DISPATCH:
                        MatDispatchItem matDispatchItem = PstMatDispatchItem.getObjectDispatchItem(matReceiveItem.getMaterialId(), matReceive.getDispatchMaterialId());

                        qtymax = qtymax + matDispatchItem.getResidueQty();
                        System.out.println("===>>> SISA QTY : " + qtymax);
                        if (matReceiveItem.getQty() > qtymax) {
                            frmMatReceiveItem.addError(0, "");
                            msgString = "maksimal qty adalah =" + qtymax;
                            return RSLT_FORM_INCOMPLETE;
                        }

                        if (matReceiveItem.getOID() == 0) {
                            try {
                                long oid = pstMatReceiveItem.insertExc(this.matReceiveItem);

                                if (this != null && oidMatReceive != 0) {
                                    matReceive = PstMatReceive.fetchExc(oidMatReceive);
                                }
                                if (oid != 0) {
                                    long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatReceive);
                                    if (oidUserLast != 0 && oidUserLast != userID) {
                                        insertHistory(userID, nameUser, cmd, oidMatReceive);
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
                                long oid = pstMatReceiveItem.updateExc(this.matReceiveItem);
                                if (oid != 0) {
                                    int cmdHistory = Command.UPDATE;
                                    insertHistory(userID, nameUser, cmdHistory, oidMatReceive);
                                }
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }

                        matDispatchItem.setResidueQty(qtymax - matReceiveItem.getQty());
                        try {
                            PstMatDispatchItem.updateExc(matDispatchItem);
                        } catch (Exception e) {
                        }

                        // update transfer status
                        PstMatDispatch.processUpdate(matReceive.getDispatchMaterialId());

                        break;
                        
                        //untuk buyback update sorting
                    case PstMatReceive.SOURCE_FROM_BUYBACK:   
                        if (matReceiveItem.getOID() != 0){
                            try {
                                long oid = pstMatReceiveItem.updateExc(this.matReceiveItem); 
                            } catch (Exception exc){}
                        }
                        break;

                    default:

                        if (matReceiveItem.getOID() == 0) {
                            try {
                                long oid = pstMatReceiveItem.insertExc(this.matReceiveItem);
                                //update data untuk jewelry
                                int typeOfBusinessDetail = Integer.valueOf(com.dimata.system.entity.PstSystemProperty.getValueIntByName("TYPE_OF_BUSINESS_DETAIL"));
                                if (typeOfBusinessDetail == 2) {
                                    //set harga emas / harga beli (berlian)
                                    updateHargaEmasBerlian(matReceive,userID,nameUser);
                                }
                                
                                if (this != null && oidMatReceive != 0) {
                                    matReceive = PstMatReceive.fetchExc(oidMatReceive);
                                }
                                if (oid != 0) {
                                    if (calculateStock == 1) {
                                        mat = PstMaterial.fetchExc(matReceiveItem.getMaterialId());
                                        //automatic insert serrial number
                                        if (mat.getRequiredSerialNumber() == 0) {
                                            double qty = matReceiveItem.getQty();
                                            long oidReceive = matReceiveItem.getReceiveMaterialId();
                                            double value = matReceiveItem.getCurrBuyingPrice();
                                            boolean x = pstReceiveStockCode.automaticInsertSerialNumber(qty, oid, oidReceive, value);
                                        }
                                    }
                                    long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatReceive);
                                    if (oidUserLast != 0 && oidUserLast != userID) {
                                        insertHistory(userID, nameUser, cmd, oidMatReceive);
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

                                long oid = pstMatReceiveItem.updateExc(this.matReceiveItem);                                
                                //update data untuk jewelry
                                int typeOfBusinessDetail = Integer.valueOf(com.dimata.system.entity.PstSystemProperty.getValueIntByName("TYPE_OF_BUSINESS_DETAIL"));
                                if (typeOfBusinessDetail == 2) {                                    
                                    //set harga emas / harga beli (berlian)
                                    updateHargaEmasBerlian(matReceive,userID,nameUser);
                                }
                                //update opie-eyek 20141209
                                //setting detail item jika ada bonus
                                //cek apakah di item receive ada barang bonus apa tidak, jika ada barang bonus cek apakah system properties menggunakan perhitungan RTC
                                int versiSettingDiscount = Integer.valueOf(PstSystemProperty.getValueByName("VERSI_SETTING_DISCOUNT"));
                                boolean discountAvailable = PstMatReceiveItem.discountExist(this.matReceiveItem.getReceiveMaterialId());
                                if (versiSettingDiscount == 1 && discountAvailable) {

//                                     if(this!=null && oidMatReceive!=0 ){
//                                        try {
//                                             prevMatReceive = PstMatReceive.fetchExc(oidMatReceive);
//                                        } catch (Exception e) {
//                                        }
//                                    }
                                    //cari total yang tanpa bonus
                                    String whereTotalWithoutBonus = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "='" + this.matReceiveItem.getReceiveMaterialId() + "'"
                                            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "='0'";

                                    double totalWithoutBonus = PstMatReceiveItem.getTotalAmount(whereTotalWithoutBonus);
                                    //cari total yang mendapatkan bonus
                                    String whereTotalBonus = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "='" + this.matReceiveItem.getReceiveMaterialId() + "'"
                                            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "='1'";
                                    double totalBonusOnly = PstMatReceiveItem.getTotalAmount(whereTotalBonus);

                                    double totalBonus = totalWithoutBonus + totalBonusOnly;

                                    //persentaseDisc=totalWithoutBonus/totalBonus
                                    double persentaseDisc = 0;
                                    if (totalBonus != 0) {
                                        persentaseDisc = totalWithoutBonus / totalBonus;
                                    }

                                    double rateNominal = 0;
                                    if (prevMatReceive.getTransRate() == 0) {
                                        rateNominal = 1;
                                    } else {
                                        rateNominal = prevMatReceive.getTransRate();
                                    }
                                    //cari list semua item yang di terima
                                    Vector vReceiveItem = PstMatReceiveItem.list(0, 0, PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + "='" + this.matReceiveItem.getReceiveMaterialId() + "'", "");
                                    if (vReceiveItem.size() > 0) {
                                        for (int j = 0; j < vReceiveItem.size(); j++) {
                                            MatReceiveItem matReceiveItem = (MatReceiveItem) vReceiveItem.get(j);
                                            double costGetdiscNominal = 0;
                                            double discNominal = 0;
                                            double discNominalPerQty = 0;

                                            if (matReceiveItem.getTotal() != 0) {
                                                //hitung persentase discount yang di dapat dan total nilai nya
                                                //total harga-disc

                                                costGetdiscNominal = ((matReceiveItem.getCost() * matReceiveItem.getQty() * rateNominal) * persentaseDisc);

                                                //disc
                                                discNominal = (matReceiveItem.getCost() * matReceiveItem.getQty() * rateNominal) - costGetdiscNominal;

                                                //disc per item
                                                discNominalPerQty = discNominal / matReceiveItem.getQty();

                                                matReceiveItem.setDiscNominal(discNominalPerQty);

                                                matReceiveItem.setTotal(costGetdiscNominal + matReceiveItem.getForwarderCost());

                                            }

                                            // update datanya
                                            PstMatReceiveItem.updateExc(matReceiveItem);
                                        }
                                    }
                                }

                                if (oid != 0) {

                                    if (calculateStock == 1 && matReceive.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                        //automatic insert serrial number
                                        mat = PstMaterial.fetchExc(matReceiveItem.getMaterialId());
                                        //automatic insert serrial number
                                        if (mat.getRequiredSerialNumber() == 0) {
                                            double qty = matReceiveItem.getQty();
                                            long oidReceive = matReceiveItem.getReceiveMaterialId();
                                            double value = matReceiveItem.getCurrBuyingPrice();
                                            //first delete
                                            String whereClause = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "='" + oid + "'";
                                            int deleteSucc = pstReceiveStockCode.deleteReceiveMaterialStockWithReceiveMaterialItemId(whereClause);
                                            //and insert again
                                            boolean x = pstReceiveStockCode.automaticInsertSerialNumber(qty, oid, oidReceive, value);
                                        }
                                    }

                                    int cmdHistory = Command.UPDATE;
                                    insertHistory(userID, nameUser, cmdHistory, oidMatReceive);
                                }
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }
                        // set vendor price untuk  barang ini
                        try {
                            double newCost = matReceiveItem.getCost();
                            double lastDisc = matReceiveItem.getDiscount();
                            double lastDisc2 = matReceiveItem.getDiscount2();
                            double lastDiscNom = matReceiveItem.getDiscNominal();
                            double newForwarderCost = matReceiveItem.getForwarderCost();
                            //+ppn
                            double Ppn = matReceive.getTotalPpn();

                            MatVendorPrice matVdrPrc = new MatVendorPrice();
                            matVdrPrc.setVendorId(matReceive.getSupplierId());
                            matVdrPrc.setPriceCurrency(matReceive.getCurrencyId());
                            matVdrPrc.setMaterialId(matReceiveItem.getMaterialId());
                            matVdrPrc.setBuyingUnitId(matReceiveItem.getUnitId());
                            matVdrPrc.setOrgBuyingPrice(matReceiveItem.getCost());
                            //matVdrPrc.setCurrBuyingPrice(matReceiveItem.getCost());
                            matVdrPrc.setLastDiscount(matReceiveItem.getDiscNominal());

                            //set Discount1 & Discount2, ForwarderCost
                            matVdrPrc.setLastDiscount1(lastDisc);
                            matVdrPrc.setLastDiscount2(lastDisc2);
                            matVdrPrc.setLastCostCargo(newForwarderCost);

                            //set Total Ppn by Mirahu 25 Mei 2011
                            matVdrPrc.setLastVat(Ppn);
                                //int includePpn = Integer.parseInt(PstSystemProperty.getValueByName("INCLUDE_PPN_MASUKAN"));

                            //calculate hpp & discount
                            // By Mirahu 25 Februari2011
                            double totalDiscount = newCost * lastDisc / 100;
                            double totalMinus = newCost - totalDiscount;
                            double totalDiscount2 = totalMinus * lastDisc2 / 100;
                            double totalCost = (totalMinus - totalDiscount2) - lastDiscNom;
                            //update cost + PPn by Mirahu 25 Mei 2011
                            //if(includePpn == 0){
                            double totalCostAll = totalCost + newForwarderCost;
                            matVdrPrc.setCurrBuyingPrice(totalCostAll);
                                //}
                            //else if (includePpn == 1){
                            // double totalPpn = totalCost * Ppn /100;
                            //double totalCostAllPpn = totalCost + totalPpn;
                            //matVdrPrc.setCurrBuyingPrice(totalCostAllPpn);
                            // }

                            //if(includePpn == 0){
                            // matVdrPrc.setCurrBuyingPrice(totalCostAll);
                            // }
                            // else if (includePpn == 1){
                            //matVdrPrc.setCurrBuyingPrice(totalCostAllPpn);
                            //}
                            PstMatVendorPrice.insertUpdateExc(matVdrPrc);

                        } catch (Exception exc) {
                            System.out.println(" Exc. in update/insert vendor price ");
                        }

                        /**
                         * set total cost pada forwarder info dengan value
                         * terkini!
                         */
                        try {
                            System.out.println("oidMatReceiveItem >>> " + this.matReceiveItem.getReceiveMaterialId());
                            String whereClause = "" + PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID] + "=" + this.matReceiveItem.getReceiveMaterialId();
                            Vector vctListFi = PstForwarderInfo.list(0, 0, whereClause, "");
                            ForwarderInfo forwarderInfo = new ForwarderInfo();
                            for (int j = 0; j < vctListFi.size(); j++) {
                                forwarderInfo = (ForwarderInfo) vctListFi.get(j);
                                forwarderInfo.setTotalCost(SessForwarderInfo.getTotalCost(this.matReceiveItem.getReceiveMaterialId()));
                                System.out.println("totalCost >> " + forwarderInfo.getTotalCost());
                                long oid = PstForwarderInfo.updateExc(forwarderInfo);
                            }
                        } catch (Exception e) {
                            System.out.println("Exc in update total_cost, forwarder_info >>> " + e.toString());
                        }

                        break;
                }

                break;

            case Command.EDIT:
                if (oidMatReceiveItem != 0) {
                    try {
                        matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatReceiveItem != 0) {
                    try {
                        matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
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
                    prevMatReceive = PstMatReceive.fetchExc(oidMatReceive);
                    matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                    prevMatReceiveItem = null;
                } catch (Exception exc) {
                }
                //================================================================
                if (oidMatReceiveItem != 0) {

                    MatReceive matRec = new MatReceive();
                    MatReceiveItem matReceiveItem = new MatReceiveItem();
                    try {
                        matRec = PstMatReceive.fetchExc(oidMatReceive);
                        matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                    } catch (Exception e) {
                        System.out.println("Err when fetch matReceive and matReceiveItem : " + e.toString());
                    }
                    qtymax = matReceiveItem.getQty();

                    try {
                        // untuk penghapusan code di stock code
                        SessPosting.deleteUpdateStockCode(0, 0, oidMatReceiveItem, SessPosting.DOC_TYPE_RECEIVE);
                        long oid = PstMatReceiveItem.deleteExc(oidMatReceiveItem);
                        //update data item penerimaan emas
                        int typeOfBusinessDetail = Integer.valueOf(com.dimata.system.entity.PstSystemProperty.getValueIntByName("TYPE_OF_BUSINESS_DETAIL"));
                        if (typeOfBusinessDetail == 2) {
                            //updated by dewok 2018-09-12 : hapus material item saat hapus item penerimaan
                            MaterialDetail md = PstMaterialDetail.fetchExcMaterialDetailId(matReceiveItem.getMaterialId());
                            PstMaterialDetail.deleteExc(md.getOID());
                            PstMaterial.deleteExc(matReceiveItem.getMaterialId());
                            if (prevMatReceive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS) {
                                //get total berat seluruh item per penerimaan
                                Vector<MatReceiveItem> listItemRec = PstMatReceiveItem.list(0, 0, "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = '" + prevMatReceive.getOID() + "'", "");
                                double totalBeratItem = 0;
                                for (int i = 0; i < listItemRec.size(); i++) {
                                    totalBeratItem += listItemRec.get(i).getBerat();
                                }
                                //set harga emas per item
                                for (int i = 0; i < listItemRec.size(); i++) {
                                    MatReceiveItem mri = (MatReceiveItem) listItemRec.get(i);                                        
                                    double totalHe = prevMatReceive.getTotalHe();
                                    double beratItem = mri.getBerat();
                                    double hargaEmas = (totalHe * beratItem) / totalBeratItem;
                                    double total = hargaEmas;// + mri.getForwarderCost();
                                    mri.setCost(hargaEmas);
                                    mri.setTotal(total);
                                    PstMatReceiveItem.updateExc(mri);
                                }
                            }
                        }
                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oidMatReceive);
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
                         * kalo tipe receive adalah dari return toko proses ini
                         * dilakukan di WAREHOUSE
                         *
                         * @created <CODE>by Edhy</CODE>
                         */
                        case PstMatReceive.SOURCE_FROM_RETURN:
                            MatReturnItem matReturnItem = new MatReturnItem();
                            try {
                                matReturnItem = PstMatReturnItem.getObjectReturnItem(matReceiveItem.getMaterialId(), matRec.getReturnMaterialId());
                            } catch (Exception e) {
                                System.out.println("Err when fetch matReturnItem : " + e.toString());
                            }

                            qtymax = qtymax + matReturnItem.getResidueQty();
                            matReturnItem.setResidueQty(qtymax);

                            try {
                                PstMatReturnItem.updateExc(matReturnItem);
                            } catch (Exception e) {
                                System.out.println("Err when update MatReturnItem : " + e.toString());
                            }

                            // update status transfer
                            PstMatReturn.processUpdate(matRec.getReturnMaterialId());

                            break;

                        /**
                         * kalo tipe receive adalah dari dispatch warehouse
                         * proses ini dilakukan di STORE
                         *
                         * @created <CODE>by Edhy</CODE>
                         */
                        case PstMatReceive.SOURCE_FROM_DISPATCH:
                            MatDispatchItem matDispatchItem = new MatDispatchItem();
                            try {
                                matDispatchItem = PstMatDispatchItem.getObjectDispatchItem(matReceiveItem.getMaterialId(), matRec.getDispatchMaterialId());
                            } catch (Exception e) {
                                System.out.println("Err when fetch MatDispatchItem : " + e.toString());
                            }

                            qtymax = qtymax + matDispatchItem.getResidueQty();
                            matDispatchItem.setResidueQty(qtymax);

                            try {
                                PstMatDispatchItem.updateExc(matDispatchItem);
                            } catch (Exception e) {
                                System.out.println("Err when update MatDispatchItem : " + e.toString());
                            }

                            // update status transfer
                            PstMatDispatch.processUpdate(matRec.getDispatchMaterialId());

                            break;
                    }
                }

                break;

            default:
                break;

        }
        return rsCode;
    }

    synchronized public int action2(int cmd, long oidMatReceiveItem, long oidMatReceive, String nameUser, long userID, HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                double qtymax = 0;
                if (oidMatReceiveItem != 0) {
                    try {
                        matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                        qtymax = matReceiveItem.getQty();
                    } catch (Exception exc) {
                    }

                    try {
                        prevMatReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                    } catch (Exception exc) {
                    }
                }
                frmMatReceiveItem.requestEntityObject(matReceiveItem);
                if (matReceiveItem.getExpiredDate() == null) {
                    String tgl = FRMQueryString.requestString(request, "" + frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_EXPIRED_DATE] + "");
                    matReceiveItem.setExpiredDate(Formater.formatDate(tgl, "MM/dd/yyyy"));
                }
                matReceiveItem.setResidueQty(matReceiveItem.getQty());
                matReceiveItem.setReceiveMaterialId(oidMatReceive);

                // check if current material already exist in orderMaterial
                if (matReceiveItem.getOID() == 0 && PstMatReceiveItem.materialExist(matReceiveItem.getMaterialId(), oidMatReceive)) {
                    msgString = getSystemMessage(RSLT_MATERIAL_EXIST);
                    return getControlMsgId(RSLT_MATERIAL_EXIST);
                }

                /**
                 * check if current material already exist in orderMaterial
                 *
                 * @created <CODE>by Gedhy</CODE>
                 */
                if (matReceiveItem.getQty() == 0) {
                    msgString = getSystemMessage(RSLT_QTY_NULL);
                    return getControlMsgId(RSLT_QTY_NULL);
                }

                if (frmMatReceiveItem.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                // untuk cek document receive
                MatReceive matReceive = new MatReceive();
                try {
                    matReceive = PstMatReceive.fetchExc(oidMatReceive);
                } catch (Exception e) {
                }

                //update opie-eyek jika ada ratenya
                /*if(matReceive.getTransRate()>1){
                 matReceiveItem.setCost(matReceiveItem.getCost()*matReceive.getTransRate());
                 matReceiveItem.setCurrBuyingPrice(matReceiveItem.getCurrBuyingPrice()*matReceive.getTransRate());
                 matReceiveItem.setDiscNominal(matReceiveItem.getDiscNominal()*matReceive.getTransRate());
                 matReceiveItem.setDiscount(matReceiveItem.getDiscount()* matReceive.getTransRate());
                 matReceiveItem.setDiscount2(matReceiveItem.getDiscount2()*matReceive.getTransRate());
                 matReceiveItem.setForwarderCost(matReceiveItem.getForwarderCost()*matReceive.getTransRate());
                 matReceiveItem.setTotal(matReceiveItem.getTotal()*matReceive.getTransRate());
                 //matReceiveItem.
                 }*/
                if (this != null && oidMatReceive != 0) {
                    try {
                        prevMatReceive = PstMatReceive.fetchExc(oidMatReceive);
                    } catch (Exception e) {
                    }
                }
                int calculateStock = Integer.valueOf(PstSystemProperty.getValueByName("CALCULATE_STOCK_VALUE"));

                switch (matReceive.getReceiveSource()) {
                    /**
                     * kalo tipe receive adalah dari return toko proses ini
                     * dilakukan di WAREHOUSE
                     *
                     * @created <CODE>by Edhy</CODE>
                     */
                    case PstMatReceive.SOURCE_FROM_RETURN:
                        MatReturnItem matReturnItem = PstMatReturnItem.getObjectReturnItem(matReceiveItem.getMaterialId(), matReceive.getReturnMaterialId());

                        qtymax = qtymax + matReturnItem.getResidueQty();
                        System.out.println("===>>> SISA QTY : " + qtymax);
                        if (matReceiveItem.getQty() > qtymax) {
                            frmMatReceiveItem.addError(0, "");
                            msgString = "maksimal qty adalah =" + qtymax;
                            return RSLT_FORM_INCOMPLETE;
                        }

                        if (matReceiveItem.getOID() == 0) {
                            try {
                                oidMatReceiveItem = pstMatReceiveItem.insertExc(this.matReceiveItem);

                                if (oidMatReceiveItem != 0) {
                                    long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatReceive);
                                    if (oidUserLast != 0 && oidUserLast != userID) {
                                        insertHistory(userID, nameUser, cmd, oidMatReceive);
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
                                oidMatReceiveItem = pstMatReceiveItem.updateExc(this.matReceiveItem);

                                if (oidMatReceiveItem != 0) {
                                    int cmdHistory = Command.UPDATE;
                                    insertHistory(userID, nameUser, cmdHistory, oidMatReceive);
                                }
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }

                        matReturnItem.setResidueQty(qtymax - matReceiveItem.getQty());
                        try {
                            PstMatReturnItem.updateExc(matReturnItem);
                        } catch (Exception e) {
                        }

                        // update transfer status
                        PstMatReturn.processUpdate(matReceive.getReturnMaterialId());

                        // proses serial code
                        PstReceiveStockCode.getInsertSerialFromReturn(oidMatReceiveItem, matReceive.getReturnMaterialId(), matReceiveItem.getMaterialId());
                        break;

                    /**
                     * kalo tipe receive adalah dari dispatch warehouse proses
                     * ini dilakukan di STORE
                     *
                     * @created <CODE>by Edhy</CODE>
                     */
                    case PstMatReceive.SOURCE_FROM_DISPATCH:
                        MatDispatchItem matDispatchItem = PstMatDispatchItem.getObjectDispatchItem(matReceiveItem.getMaterialId(), matReceive.getDispatchMaterialId());

                        qtymax = qtymax + matDispatchItem.getResidueQty();
                        System.out.println("===>>> SISA QTY : " + qtymax);
                        if (matReceiveItem.getQty() > qtymax) {
                            frmMatReceiveItem.addError(0, "");
                            msgString = "maksimal qty adalah =" + qtymax;
                            return RSLT_FORM_INCOMPLETE;
                        }

                        if (matReceiveItem.getOID() == 0) {
                            try {
                                long oid = pstMatReceiveItem.insertExc(this.matReceiveItem);

                                if (this != null && oidMatReceive != 0) {
                                    matReceive = PstMatReceive.fetchExc(oidMatReceive);
                                }
                                if (oid != 0) {
                                    long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatReceive);
                                    if (oidUserLast != 0 && oidUserLast != userID) {
                                        insertHistory(userID, nameUser, cmd, oidMatReceive);
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
                                long oid = pstMatReceiveItem.updateExc(this.matReceiveItem);
                                if (oid != 0) {
                                    int cmdHistory = Command.UPDATE;
                                    insertHistory(userID, nameUser, cmdHistory, oidMatReceive);
                                }
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }

                        matDispatchItem.setResidueQty(qtymax - matReceiveItem.getQty());
                        try {
                            PstMatDispatchItem.updateExc(matDispatchItem);
                        } catch (Exception e) {
                        }

                        // update transfer status
                        PstMatDispatch.processUpdate(matReceive.getDispatchMaterialId());

                        break;

                    default:

                        if (matReceiveItem.getOID() == 0) {
                            try {
                                long oid = pstMatReceiveItem.insertExc(this.matReceiveItem);

                                if (this != null && oidMatReceive != 0) {
                                    matReceive = PstMatReceive.fetchExc(oidMatReceive);
                                }
                                if (oid != 0) {
                                    if (calculateStock == 1) {
                                        mat = PstMaterial.fetchExc(matReceiveItem.getMaterialId());
                                        //automatic insert serrial number
                                        if (mat.getRequiredSerialNumber() == 0) {
                                            double qty = matReceiveItem.getQty();
                                            long oidReceive = matReceiveItem.getReceiveMaterialId();
                                            double value = matReceiveItem.getCurrBuyingPrice();
                                            boolean x = pstReceiveStockCode.automaticInsertSerialNumber(qty, oid, oidReceive, value);
                                        }
                                    }
                                    long oidUserLast = PstLogSysHistory.getFirstUserId(oidMatReceive);
                                    if (oidUserLast != 0 && oidUserLast != userID) {
                                        insertHistory(userID, nameUser, cmd, oidMatReceive);
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

                                long oid = pstMatReceiveItem.updateExc(this.matReceiveItem);

                                //update opie-eyek 20141209
                                //setting detail item jika ada bonus
                                //cek apakah di item receive ada barang bonus apa tidak, jika ada barang bonus cek apakah system properties menggunakan perhitungan RTC
                                int versiSettingDiscount = Integer.valueOf(PstSystemProperty.getValueByName("VERSI_SETTING_DISCOUNT"));
                                boolean discountAvailable = PstMatReceiveItem.discountExist(this.matReceiveItem.getReceiveMaterialId());
                                if (versiSettingDiscount == 1 && discountAvailable) {

//                                     if(this!=null && oidMatReceive!=0 ){
//                                        try {
//                                             prevMatReceive = PstMatReceive.fetchExc(oidMatReceive);
//                                        } catch (Exception e) {
//                                        }
//                                    }
                                    //cari total yang tanpa bonus
                                    String whereTotalWithoutBonus = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "='" + this.matReceiveItem.getReceiveMaterialId() + "'"
                                            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "='0'";

                                    double totalWithoutBonus = PstMatReceiveItem.getTotalAmount(whereTotalWithoutBonus);
                                    //cari total yang mendapatkan bonus
                                    String whereTotalBonus = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "='" + this.matReceiveItem.getReceiveMaterialId() + "'"
                                            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "='1'";
                                    double totalBonusOnly = PstMatReceiveItem.getTotalAmount(whereTotalBonus);

                                    double totalBonus = totalWithoutBonus + totalBonusOnly;

                                    //persentaseDisc=totalWithoutBonus/totalBonus
                                    double persentaseDisc = 0;
                                    if (totalBonus != 0) {
                                        persentaseDisc = totalWithoutBonus / totalBonus;
                                    }

                                    double rateNominal = 0;
                                    if (prevMatReceive.getTransRate() == 0) {
                                        rateNominal = 1;
                                    } else {
                                        rateNominal = prevMatReceive.getTransRate();
                                    }
                                    //cari list semua item yang di terima
                                    Vector vReceiveItem = PstMatReceiveItem.list(0, 0, PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + "='" + this.matReceiveItem.getReceiveMaterialId() + "'", "");
                                    if (vReceiveItem.size() > 0) {
                                        for (int j = 0; j < vReceiveItem.size(); j++) {
                                            MatReceiveItem matReceiveItem = (MatReceiveItem) vReceiveItem.get(j);
                                            double costGetdiscNominal = 0;
                                            double discNominal = 0;
                                            double discNominalPerQty = 0;

                                            if (matReceiveItem.getTotal() != 0) {
                                                //hitung persentase discount yang di dapat dan total nilai nya
                                                //total harga-disc

                                                costGetdiscNominal = ((matReceiveItem.getCost() * matReceiveItem.getQty() * rateNominal) * persentaseDisc);

                                                //disc
                                                discNominal = (matReceiveItem.getCost() * matReceiveItem.getQty() * rateNominal) - costGetdiscNominal;

                                                //disc per item
                                                discNominalPerQty = discNominal / matReceiveItem.getQty();

                                                matReceiveItem.setDiscNominal(discNominalPerQty);

                                                matReceiveItem.setTotal(costGetdiscNominal + matReceiveItem.getForwarderCost());

                                            }

                                            // update datanya
                                            PstMatReceiveItem.updateExc(matReceiveItem);
                                        }
                                    }
                                }

                                if (oid != 0) {

                                    if (calculateStock == 1 && matReceive.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                        //automatic insert serrial number
                                        mat = PstMaterial.fetchExc(matReceiveItem.getMaterialId());
                                        //automatic insert serrial number
                                        if (mat.getRequiredSerialNumber() == 0) {
                                            double qty = matReceiveItem.getQty();
                                            long oidReceive = matReceiveItem.getReceiveMaterialId();
                                            double value = matReceiveItem.getCurrBuyingPrice();
                                            //first delete
                                            String whereClause = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "='" + oid + "'";
                                            int deleteSucc = pstReceiveStockCode.deleteReceiveMaterialStockWithReceiveMaterialItemId(whereClause);
                                            //and insert again
                                            boolean x = pstReceiveStockCode.automaticInsertSerialNumber(qty, oid, oidReceive, value);
                                        }
                                    }

                                    int cmdHistory = Command.UPDATE;
                                    insertHistory(userID, nameUser, cmdHistory, oidMatReceive);
                                }
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }
                        // set vendor price untuk  barang ini
                        try {
                            double newCost = matReceiveItem.getCost();
                            double lastDisc = matReceiveItem.getDiscount();
                            double lastDisc2 = matReceiveItem.getDiscount2();
                            double lastDiscNom = matReceiveItem.getDiscNominal();
                            double newForwarderCost = matReceiveItem.getForwarderCost();
                            //+ppn
                            double Ppn = matReceive.getTotalPpn();

                            MatVendorPrice matVdrPrc = new MatVendorPrice();
                            matVdrPrc.setVendorId(matReceive.getSupplierId());
                            matVdrPrc.setPriceCurrency(matReceive.getCurrencyId());
                            matVdrPrc.setMaterialId(matReceiveItem.getMaterialId());
                            matVdrPrc.setBuyingUnitId(matReceiveItem.getUnitId());
                            matVdrPrc.setOrgBuyingPrice(matReceiveItem.getCost());
                            //matVdrPrc.setCurrBuyingPrice(matReceiveItem.getCost());
                            matVdrPrc.setLastDiscount(matReceiveItem.getDiscNominal());

                            //set Discount1 & Discount2, ForwarderCost
                            matVdrPrc.setLastDiscount1(lastDisc);
                            matVdrPrc.setLastDiscount2(lastDisc2);
                            matVdrPrc.setLastCostCargo(newForwarderCost);

                            //set Total Ppn by Mirahu 25 Mei 2011
                            matVdrPrc.setLastVat(Ppn);
                                //int includePpn = Integer.parseInt(PstSystemProperty.getValueByName("INCLUDE_PPN_MASUKAN"));

                            //calculate hpp & discount
                            // By Mirahu 25 Februari2011
                            double totalDiscount = newCost * lastDisc / 100;
                            double totalMinus = newCost - totalDiscount;
                            double totalDiscount2 = totalMinus * lastDisc2 / 100;
                            double totalCost = (totalMinus - totalDiscount2) - lastDiscNom;
                            //update cost + PPn by Mirahu 25 Mei 2011
                            //if(includePpn == 0){
                            double totalCostAll = totalCost + newForwarderCost;
                            matVdrPrc.setCurrBuyingPrice(totalCostAll);
                                //}
                            //else if (includePpn == 1){
                            // double totalPpn = totalCost * Ppn /100;
                            //double totalCostAllPpn = totalCost + totalPpn;
                            //matVdrPrc.setCurrBuyingPrice(totalCostAllPpn);
                            // }

                            //if(includePpn == 0){
                            // matVdrPrc.setCurrBuyingPrice(totalCostAll);
                            // }
                            // else if (includePpn == 1){
                            //matVdrPrc.setCurrBuyingPrice(totalCostAllPpn);
                            //}
                            PstMatVendorPrice.insertUpdateExc(matVdrPrc);
                        } catch (Exception exc) {
                            System.out.println(" Exc. in update/insert vendor price ");
                        }

                        /**
                         * set total cost pada forwarder info dengan value
                         * terkini!
                         */
                        try {
                            System.out.println("oidMatReceiveItem >>> " + this.matReceiveItem.getReceiveMaterialId());
                            String whereClause = "" + PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID] + "=" + this.matReceiveItem.getReceiveMaterialId();
                            Vector vctListFi = PstForwarderInfo.list(0, 0, whereClause, "");
                            ForwarderInfo forwarderInfo = new ForwarderInfo();
                            for (int j = 0; j < vctListFi.size(); j++) {
                                forwarderInfo = (ForwarderInfo) vctListFi.get(j);
                                forwarderInfo.setTotalCost(SessForwarderInfo.getTotalCost(this.matReceiveItem.getReceiveMaterialId()));
                                System.out.println("totalCost >> " + forwarderInfo.getTotalCost());
                                long oid = PstForwarderInfo.updateExc(forwarderInfo);
                            }
                        } catch (Exception e) {
                            System.out.println("Exc in update total_cost, forwarder_info >>> " + e.toString());
                        }

                        break;
                }

                break;

            case Command.EDIT:
                if (oidMatReceiveItem != 0) {
                    try {
                        matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatReceiveItem != 0) {
                    try {
                        matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
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
                    prevMatReceive = PstMatReceive.fetchExc(oidMatReceive);
                    matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                    prevMatReceiveItem = null;
                } catch (Exception exc) {
                }
                //================================================================
                if (oidMatReceiveItem != 0) {

                    MatReceive matRec = new MatReceive();
                    MatReceiveItem matReceiveItem = new MatReceiveItem();
                    try {
                        matRec = PstMatReceive.fetchExc(oidMatReceive);
                        matReceiveItem = PstMatReceiveItem.fetchExc(oidMatReceiveItem);
                    } catch (Exception e) {
                        System.out.println("Err when fetch matReceive and matReceiveItem : " + e.toString());
                    }
                    qtymax = matReceiveItem.getQty();

                    try {
                        // untuk penghapusan code di stock code
                        SessPosting.deleteUpdateStockCode(0, 0, oidMatReceiveItem, SessPosting.DOC_TYPE_RECEIVE);
                        long oid = PstMatReceiveItem.deleteExc(oidMatReceiveItem);
                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oidMatReceive);
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
                         * kalo tipe receive adalah dari return toko proses ini
                         * dilakukan di WAREHOUSE
                         *
                         * @created <CODE>by Edhy</CODE>
                         */
                        case PstMatReceive.SOURCE_FROM_RETURN:
                            MatReturnItem matReturnItem = new MatReturnItem();
                            try {
                                matReturnItem = PstMatReturnItem.getObjectReturnItem(matReceiveItem.getMaterialId(), matRec.getReturnMaterialId());
                            } catch (Exception e) {
                                System.out.println("Err when fetch matReturnItem : " + e.toString());
                            }

                            qtymax = qtymax + matReturnItem.getResidueQty();
                            matReturnItem.setResidueQty(qtymax);

                            try {
                                PstMatReturnItem.updateExc(matReturnItem);
                            } catch (Exception e) {
                                System.out.println("Err when update MatReturnItem : " + e.toString());
                            }

                            // update status transfer
                            PstMatReturn.processUpdate(matRec.getReturnMaterialId());

                            break;

                        /**
                         * kalo tipe receive adalah dari dispatch warehouse
                         * proses ini dilakukan di STORE
                         *
                         * @created <CODE>by Edhy</CODE>
                         */
                        case PstMatReceive.SOURCE_FROM_DISPATCH:
                            MatDispatchItem matDispatchItem = new MatDispatchItem();
                            try {
                                matDispatchItem = PstMatDispatchItem.getObjectDispatchItem(matReceiveItem.getMaterialId(), matRec.getDispatchMaterialId());
                            } catch (Exception e) {
                                System.out.println("Err when fetch MatDispatchItem : " + e.toString());
                            }

                            qtymax = qtymax + matDispatchItem.getResidueQty();
                            matDispatchItem.setResidueQty(qtymax);

                            try {
                                PstMatDispatchItem.updateExc(matDispatchItem);
                            } catch (Exception e) {
                                System.out.println("Err when update MatDispatchItem : " + e.toString());
                            }

                            // update status transfer
                            PstMatDispatch.processUpdate(matRec.getDispatchMaterialId());

                            break;
                    }
                }

                break;

            default:
                break;

        }
        return rsCode;
    }

    private void insertHistory(long userID, String nameUser, int cmd, long oidMatReceive) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            //logSysHistory.setLogUserId(poItem.getMaterialId());
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("material/receive/receive_wh_supp_po_material_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(prevMatReceive.getRecCode());
            logSysHistory.setLogDocumentId(oidMatReceive);

            logSysHistory.setLogDetail(this.matReceiveItem.getLogDetail(prevMatReceiveItem));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {
        }
    }
    
    //added by dewok 2018-02-09
    public void insertHistoryMaterial(long userID, String nameUser, int cmd, long oid, Material material, Material prevMaterial, String historyChanged) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("material/receive/receive_wh_supp_po_material_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType("Pos material");
            logSysHistory.setLogUserAction("Penerimaan");
            logSysHistory.setLogDocumentNumber(material.getFullName());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(historyChanged);

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
    
    public int actionSaveAll(HttpServletRequest request, Vector list, long oidMatReceive) {
        int aksi = 1;
        MatReceive matReceive = new MatReceive();
        try {
            matReceive = PstMatReceive.fetchExc(oidMatReceive);
        } catch (Exception ex) {

        }
        int calculateStock = Integer.valueOf(PstSystemProperty.getValueByName("CALCULATE_STOCK_VALUE"));
        for (int i = 0; i < list.size(); i++) {
            double qtyInput = FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT] + "_" + i);
            if (qtyInput != 0) {
                MatReceiveItem matItem = new MatReceiveItem();

                matItem.setMaterialId(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID] + "_" + i));

                matItem.setExpiredDate(FRMQueryString.requestDate(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_EXPIRED_DATE] + "_" + i));

                matItem.setUnitId(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID] + "_" + i));
                matItem.setCost(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST] + "_" + i));

                matItem.setCurrencyId(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID] + "_" + i));
                matItem.setQty(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY] + "_" + i));
                matItem.setDiscount(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT] + "_" + i));

                matItem.setTotal(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL] + "_" + i));

                matItem.setDiscount2(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2] + "_" + i));
                matItem.setDiscNominal(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL] + "_" + i));
                matItem.setCurrBuyingPrice(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURR_BUYING_PRICE] + "_" + i));
                matItem.setForwarderCost(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST] + "_" + i));
                matItem.setQtyEntry(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT] + "_" + i));
                matItem.setUnitKonversi(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI] + "_" + i));
                matItem.setPriceKonv(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI] + "_" + i));
                matItem.setBonus(FRMQueryString.requestInt(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS] + "_" + i));

                matItem.setReceiveMaterialId(oidMatReceive);
                matItem.setResidueQty(matItem.getQty());
                matItem.setCurrencyId(matReceive.getCurrencyId());
                try {
                    long oid = pstMatReceiveItem.insertExc(matItem);
                    Material mater = new Material();
                    //memasukan serial number
                    if (calculateStock == 1 && oid != 0) {
                        mater = PstMaterial.fetchExc(matItem.getMaterialId());
                        //automatic insert serrial number
                        if (mater.getRequiredSerialNumber() == 0) {
                            double qty = matItem.getQty();
                            long oidReceive = matItem.getReceiveMaterialId();
                            double value = matItem.getCurrBuyingPrice();
                            boolean x = pstReceiveStockCode.automaticInsertSerialNumber(qty, oid, oidReceive, value);
                        }
                    }

                } catch (DBException ex) {
                    // Logger.getLogger(CtrlMatReceiveItem.class.getName()).log(Level.SEVERE, null, ex);
                    aksi = 0;
                }
            }
        }
        return aksi;
    }

    public int actionSaveAll2(HttpServletRequest request, long oidMatReceive) {
        int aksi = 1;
        int calculateStock = Integer.valueOf(PstSystemProperty.getValueByName("CALCULATE_STOCK_VALUE"));
        double qtyInput = FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT] + "_0");
        if (qtyInput != 0) {
            MatReceiveItem matItem = new MatReceiveItem();

            matItem.setMaterialId(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID] + "_0"));

            matItem.setExpiredDate(FRMQueryString.requestDate(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_EXPIRED_DATE] + "_0"));

            matItem.setUnitId(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID] + "_0"));
            matItem.setCost(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST] + "_0"));

            matItem.setCurrencyId(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID] + "_0"));
            matItem.setQty(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY] + "_0"));
            matItem.setDiscount(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT] + "_0"));

            matItem.setTotal(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL] + "_0"));

            matItem.setDiscount2(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2] + "_0"));
            matItem.setDiscNominal(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL] + "_0"));
            matItem.setCurrBuyingPrice(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURR_BUYING_PRICE] + "_0"));
            matItem.setForwarderCost(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST] + "_0"));
            matItem.setQtyEntry(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT] + "_0"));
            matItem.setUnitKonversi(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI] + "_0"));
            matItem.setPriceKonv(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI] + "_0"));
            matItem.setBonus(FRMQueryString.requestInt(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS] + "_0"));

            matItem.setReceiveMaterialId(oidMatReceive);
            matItem.setResidueQty(matItem.getQty());
            try {
                long oid = pstMatReceiveItem.insertExc(matItem);
                Material mater = new Material();
                //memasukan serial number
                if (calculateStock == 1 && oid != 0) {
                    mater = PstMaterial.fetchExc(matItem.getMaterialId());
                    //automatic insert serrial number
                    if (mater.getRequiredSerialNumber() == 0) {
                        double qty = matItem.getQty();
                        long oidReceive = matItem.getReceiveMaterialId();
                        double value = matItem.getCurrBuyingPrice();
                        boolean x = pstReceiveStockCode.automaticInsertSerialNumber(qty, oid, oidReceive, value);
                    }
                }

            } catch (DBException ex) {
                // Logger.getLogger(CtrlMatReceiveItem.class.getName()).log(Level.SEVERE, null, ex);
                aksi = 0;
            }
        }

        return aksi;
    }

    public int actionSaveAllOutlet(HttpServletRequest request, Vector list, long oidMatReceive) {
        int aksi = 1;
        int calculateStock = Integer.valueOf(PstSystemProperty.getValueByName("CALCULATE_STOCK_VALUE"));
        for (int i = 0; i < list.size(); i++) {
            double qtyInput = FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT] + "_" + i);
            if (qtyInput != 0) {
                MatReceiveItem matItem = new MatReceiveItem();

                matItem.setMaterialId(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID] + "_" + i));

                matItem.setExpiredDate(FRMQueryString.requestDate(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_EXPIRED_DATE] + "_" + i));

                matItem.setUnitId(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID] + "_" + i));
                matItem.setCost(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST] + "_" + i));

                matItem.setCurrencyId(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID] + "_" + i));
                matItem.setQty(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY] + "_" + i));
                matItem.setDiscount(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT] + "_" + i));

                double total = matItem.getQty() * matItem.getCost();
                matItem.setTotal(total);
                matItem.setDiscount2(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2] + "_" + i));
                matItem.setDiscNominal(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL] + "_" + i));
                matItem.setCurrBuyingPrice(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURR_BUYING_PRICE] + "_" + i));
                matItem.setForwarderCost(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST] + "_" + i));
                matItem.setQtyEntry(FRMQueryString.requestDouble(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT] + "_" + i));
                matItem.setUnitKonversi(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI] + "_" + i));
                matItem.setPriceKonv(FRMQueryString.requestLong(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI] + "_" + i));
                matItem.setBonus(FRMQueryString.requestInt(request, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS] + "_" + i));

                matItem.setReceiveMaterialId(oidMatReceive);
                matItem.setResidueQty(matItem.getQty());
                try {
                    long oid = pstMatReceiveItem.insertExc(matItem);
                    Material mater = new Material();
                    //memasukan serial number
                    if (calculateStock == 1 && oid != 0) {
                        mater = PstMaterial.fetchExc(matItem.getMaterialId());
                        //automatic insert serrial number
                        if (mater.getRequiredSerialNumber() == 0) {
                            double qty = matItem.getQty();
                            long oidReceive = matItem.getReceiveMaterialId();
                            double value = matItem.getCurrBuyingPrice();
                            boolean x = pstReceiveStockCode.automaticInsertSerialNumber(qty, oid, oidReceive, value);
                        }
                    }

                } catch (DBException ex) {
                    // Logger.getLogger(CtrlMatReceiveItem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return aksi;
    }
    
    public void updateHargaEmasBerlian(MatReceive matReceive, long userID, String nameUser) {
        if (matReceive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS || matReceive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
            //get total berat seluruh item per penerimaan
            Vector<MatReceiveItem> listItemRec = PstMatReceiveItem.list(0, 0, "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = '" + matReceive.getOID() + "'", "");
            double totalBeratItem = 0;
            for (int i = 0; i < listItemRec.size(); i++) {
                totalBeratItem += listItemRec.get(i).getBerat();
            }
            //set harga emas per item
            for (int i = 0; i < listItemRec.size(); i++) {
                try {
                    MatReceiveItem mri = (MatReceiveItem) listItemRec.get(i);
                    double totalHe = matReceive.getTotalHe();
                    double beratItem = mri.getBerat();
                    double hargaEmas = (totalHe * beratItem) / totalBeratItem;
                    double total = hargaEmas;
                    mri.setCost(hargaEmas);
                    mri.setTotal(total);
                    PstMatReceiveItem.updateExc(mri);
                    //update master data item
                    Material m = PstMaterial.fetchExc(mri.getMaterialId());
                    Material prevMat = PstMaterial.fetchExc(mri.getMaterialId());
                    m.setAveragePrice(hargaEmas);
                    PstMaterial.updateExc(m);
                    String historyChanged = m.getLogDetail(prevMat);
                    insertHistoryMaterial(userID, nameUser, Command.UPDATE, m.getOID(), m, prevMat, historyChanged);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }                
            }
        } else if (matReceive.getReceiveItemType() == Material.MATERIAL_TYPE_BERLIAN) {
            try {
                Material m = PstMaterial.fetchExc(matReceiveItem.getMaterialId());
                Material prevMat = PstMaterial.fetchExc(matReceiveItem.getMaterialId());
                m.setAveragePrice(matReceiveItem.getCost());
                PstMaterial.updateExc(m);
                String historyChanged = m.getLogDetail(prevMat);
                insertHistoryMaterial(userID, nameUser, Command.UPDATE, m.getOID(), m, prevMat, historyChanged);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }            
        }
        //update material detail
        Vector detail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = '" + matReceiveItem.getMaterialId() + "'", "");
        if (!detail.isEmpty()) {
            try {
                MaterialDetail md = (MaterialDetail) detail.get(0);
                long idMatDet = md.getOID();
                MaterialDetail prevMd = PstMaterialDetail.fetchExc(idMatDet);
                md.setBerat(matReceiveItem.getBerat());
                md.setQty(matReceiveItem.getQty());
                md.setOngkos(matReceiveItem.getForwarderCost());
                if (matReceive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS) {
                    //
                } else if (matReceive.getReceiveItemType() == Material.MATERIAL_TYPE_BERLIAN) {
                    md.setHargaBeli(matReceiveItem.getCost());
                }
                PstMaterialDetail.updateExc(md);
                //update data keterangan item (dipisah dari update item di atas agar tidak di ulang)
                Material m = PstMaterial.fetchExc(matReceiveItem.getMaterialId());
                Material prevMat = PstMaterial.fetchExc(matReceiveItem.getMaterialId());
                m.setMaterialDescription(matReceiveItem.getRemark());
                PstMaterial.updateExc(m);
                String historyChanged = m.getLogDetail(prevMat);
                historyChanged += md.getLogDetail(prevMd);
                insertHistoryMaterial(userID, nameUser, Command.UPDATE, m.getOID(), m, prevMat, historyChanged);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }            
        }
    }

}
