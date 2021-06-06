package com.dimata.posbo.form.purchasing;

/* java package */

import com.dimata.common.entity.contact.ContactClass;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.hanoman.entity.masterdata.Contact;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.db.*;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.hanoman.entity.masterdata.PstContact;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.session.billing.SessBilling;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MemberGroup;
import com.dimata.posbo.entity.masterdata.MemberReg;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstMemberGroup;
import com.dimata.posbo.entity.masterdata.PstMemberReg;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
import com.dimata.posbo.entity.purchasing.*;
import com.dimata.posbo.entity.warehouse.MatStockOpname;
import com.dimata.posbo.entity.warehouse.MatStockOpnameItem;
import com.dimata.posbo.entity.warehouse.PstMatStockOpname;
import com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem;
import com.dimata.posbo.session.purchasing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class CtrlPurchaseOrder extends Control implements I_Language {
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
    private PurchaseOrder po;
    PurchaseOrder prevPo = null;
    private PstPurchaseOrder pstpo;
    private FrmPurchaseOrder frmpo;
    int language = LANGUAGE_DEFAULT;
    private Date purchDate = null;
    Date dateLog = new  Date();
    boolean incrementAllPrType = true;
    int counter = 0;
    int st = 0;


    public CtrlPurchaseOrder(HttpServletRequest request) {
        msgString = "";
        po = new PurchaseOrder();
        try {
            pstpo = new PstPurchaseOrder(0);
        } catch (Exception e) {
            ;
        }
        //po=po-2;
        frmpo = new FrmPurchaseOrder(request, po);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmpo.addError(frmpo.FRM_FIELD_PURCHASE_ORDER_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public PurchaseOrder getPurchaseOrder() {
        return po;
    }

    public FrmPurchaseOrder getForm() {
        return frmpo;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidPurchaseOrder){
     return action(cmd,oidPurchaseOrder,"", 0);
    }
    
    public int actionSR(int cmd, long oidPurchaseOrder){
     return actionSR(cmd,oidPurchaseOrder,"", 0);
    }
   

    public int action(int cmd, long oidPurchaseOrder,String nameUser, long userID) {
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
                
            case Command.UPDATE:
                
                break;    
            case Command.SAVE:
                PurchaseOrder poReplace = new PurchaseOrder();
                if (oidPurchaseOrder != 0) {
                    try {
                        po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                        poReplace.setRemark(po.getRemark());
                        purchDate = po.getPurchDate();
                        counter = po.getPoCodeCounter();
                        st = po.getPoStatus();
                    } catch (Exception exc) {
                    }

                    //by dyas 20131126
                    try {
                        prevPo = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                    } catch (Exception exc) {
                    }
                }

                frmpo.requestEntityObject(po);
                po.setRemark(poReplace.getRemark() + ";" + po.getRemark());

                if (oidPurchaseOrder == 0) {
                    po.setPoCodeCounter(SessPurchaseOrder.getIntCode(po, purchDate, oidPurchaseOrder, counter, incrementAllPrType));
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(po));
                } else {
                    if(prevPo.getLocationId()== po.getLocationId()){
                    po.setPoCodeCounter(po.getPoCodeCounter());
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(po)); // po.getPoCode()
                    } else {
                    po.setPoCodeCounter(SessPurchaseOrder.getIntCode(po, purchDate, oidPurchaseOrder, counter, incrementAllPrType));
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(po));                        
                    }

                    // ini proses yang di gunakan untuk nomor PO yang di revisi
                    if(po.getCodeRevisi().length()>0){
                        po.setPoCode(po.getPoCode()+"-"+po.getCodeRevisi());
                    }
                }


                if (frmpo.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (po.getOID() == 0) {
                   
                    try {
                        long oid = pstpo.insertExc(this.po);
                        //PROSES UNTUK MENYIMPAN HISTORY JIKA OID DARI ORDER PO =! 0
                        if(oid!=0)
                        {
                            insertHistory(userID, nameUser, cmd, oid);
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
                        //RUBAH NILAI CMD MENJADI UPDATE
                        int cmdHistory = Command.UPDATE;
                        long oid = pstpo.updateExc(this.po);
                        if(oid!=0)
                        {
                            //FUNGSI SQL UNTUK MELAKUKAN INSERT KE TABEL LOG_HISTORY_NEW KETIKA SUDAH 2 JAM
                            //int getDiffHour = PstLogSysHistory.getLastUpdateTime(oid);
                            /*if(getDiffHour > 2)
                            {
                               insertHistory(userID, nameUser, cmdHistory, oid, cmd, true);
                            }
                            else
                            {*/
                                insertHistory(userID, nameUser, cmdHistory, oid);
                            //}
                        }
                        
                        //cek status po jika sudah final dan merupakan integrasi sales order seperti queen tandorr
                        /**
                         * logika:
                         * 1. Jika status PO sudah final dan integrasi SO create, Sales order dengan status open bill berdasarkan dokument PO
                         */
//                        int integrationSO=1;
//                         if(this.po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL && integrationSO==1){
//                             //create opname
//                                MatStockOpname soItem = new MatStockOpname();
//                                soItem.setCategoryId(po.getCategoryId());
//                                soItem.setLocationId(po.getLocationId());
//                                soItem.setStockOpnameDate(new Date());
//                                soItem.setStockOpnameStatus(0);
//                                soItem.setSupplierId(0);
//                                long oidOpname = 0;
//                                try {
//                                    oidOpname = PstMatStockOpname.insertExc(soItem);
//                                } catch (DBException ex) {
//                                }
//
//                                if(oidOpname!=0){
//                                    String whereOrderItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"='"+oid+"'";
//                                    Vector listSOItem = PstPurchaseOrderItem.list(0, 0, whereOrderItem);
//                                    if (listSOItem != null && listSOItem.size() > 0) {
//                                        for (int i = 0; i < listSOItem.size(); i++) {
//                                             Vector temp = (Vector)listSOItem.get(i);
//                                             PurchaseOrderItem purchaseOrderItem = (PurchaseOrderItem) temp.get(0);
//                                             Material material = (Material)temp.get(1);
//
//                                             MatStockOpnameItem matItem = new MatStockOpnameItem();
//                                             matItem.setStockOpnameId(oidOpname);
//                                             matItem.setMaterialId(purchaseOrderItem.getMaterialId());
//                                             matItem.setUnitId(purchaseOrderItem.getUnitId());
//                                             matItem.setQtyOpname(purchaseOrderItem.getQtyInputStock());
//                                             matItem.setCost(0);
//                                             try{
//                                                    long oidOpnameItem = PstMatStockOpnameItem.insertExc(matItem);
//                                             }catch(Exception ex){
//
//                                             }
//                                    }
//                                }
//                             }
//                        }
//                         
//                        if(this.po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL && integrationSO==1){
//                            //buat sales order
//                            //
//                            BillMain billMain = new BillMain();
//                            billMain.setBillDate(new Date());
//                            //disini di cek berdasarkan lokasi
//                            
//                            Vector customer = PstMemberReg.listFromLocationId(0, 0, PstMemberReg.fieldNames[PstMemberReg.FLD_LOCATION_ID]+"='"+this.po.getLocationId()+"'", "");
//                            long customerId = 0;
//                            long priceTypeId=0;
//                            long standartId = Long.parseLong(com.dimata.system.entity.PstSystemProperty.getValueByName("ID_STANDART_RATE"));
//                            String address = "";
//                            MemberReg member = new MemberReg();
//                            if(customer.size()>0){
//                                for (int i = 0; i < customer.size(); i++) {
//                                    member = (MemberReg) customer.get(0);
//                                    customerId=member.getOID();
//                                    address=member.getBussAddress();
//                                    MemberGroup memberGroup= PstMemberGroup.fetchExc(member.getMemberGroupId());
//                                    priceTypeId=memberGroup.getPriceTypeId();
//                                }
//                            }
//                            
//                            MemberReg memberSupp = new MemberReg();
//                            
//                            long locationId=0;
//                            try{
//                                memberSupp=PstMemberReg.fetchExc(this.po.getSupplierId());
//                                locationId = memberSupp.getLocationId();
//                                
//                            }catch(Exception ex){
//                            }
//                            
//                            billMain.setCustomerId(customerId);
//                            billMain.setCoverNumber("1");
//                            billMain.setLocationId(locationId);
//                            billMain.setParentId(0);
//                            billMain.setCashCashierId(1);
//                            
//                            //set status open bill
//                            billMain.setTransactionStatus(1);
//                            billMain.setTransctionType(0);
//                            billMain.setDocType(0);
//                            
//                            //buatkan no bill
//                            billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billMain.getBillDate(), 0, 0, true));
//                            String noInvoice = SessBilling.getCodeOrderMaterial(billMain);
//                            billMain.setInvoiceNumber(noInvoice);
//                            billMain.setInvoiceNo(noInvoice);
//                            billMain.setNotes(""+this.po.getPoCode()+";"+this.po.getLocationId()+";"+this.po.getOID());
//                            billMain.setCashPendingOrderId(0);
//                            billMain.setAppUserId(0);
//                            billMain.setShiftId(0);
//                            billMain.setDiscPct(0);
//                            billMain.setDiscount(0);
//                            billMain.setTaxPercentage(0);
//                            billMain.setTaxValue(0);
//                            
//                            //set currency id
//                            billMain.setCurrencyId(1);
//                            billMain.setRate(1);
//                            
//                            billMain.setStockLocationId(locationId);
//                            billMain.setShippingAddress(address);
//                            billMain.setShippingCountry(member.getHomeCountry());
//                            billMain.setShippingCity(member.getCompState());
//                            billMain.setShippingProvince(member.getProvince());
//                            
//                            long oidBillMain = 0;
//                            try{
//                                oidBillMain = PstBillMain.insertExc(billMain);
//                            }catch(Exception ex){
//                                
//                            }
//                            double totalAmount=0.0;
//                            if(oidBillMain!=0){
//                                Vector listSOItem = new Vector();
//                                String whereOrderItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"='"+oid+"'";
//                                listSOItem = PstPurchaseOrderItem.list(0, 0, whereOrderItem);
//                                if (listSOItem != null && listSOItem.size() > 0) {
//                                    for (int i = 0; i < listSOItem.size(); i++) {
//                                    
//                                        Vector temp = (Vector)listSOItem.get(i);
//                                        PurchaseOrderItem purchaseOrderItem = (PurchaseOrderItem) temp.get(0);
//                                        Material material = (Material)temp.get(1);
//                                        
//                                        Billdetail billdetail = new Billdetail();
//                                        
//                                        billdetail.setBillMainId(oidBillMain);
//                                        billdetail.setUnitId(purchaseOrderItem.getUnitId());
//                                        billdetail.setMaterialId(purchaseOrderItem.getMaterialId());
//                                        billdetail.setQty(purchaseOrderItem.getQuantity());
//                                        
//                                        double price = PstPriceTypeMapping.getPrice(purchaseOrderItem.getMaterialId(), standartId, priceTypeId);
//                                        double totalPrice=billdetail.getQty()*price;
//                                        billdetail.setItemPrice(price);
//                                        billdetail.setTotalPrice(totalPrice);
//                                        billdetail.setSku(material.getSku());
//                                        billdetail.setItemName(material.getName());
//                                        billdetail.setQtyStock(purchaseOrderItem.getQuantity());
//                                        billdetail.setCost(material.getAveragePrice());
//                                        totalAmount =totalAmount+billdetail.getTotalAmount();
//                                        try{
//                                            long oidBillDetail = PstBillDetail.insertExc(billdetail);
//                                        }catch(Exception ex){
//                                        }
//                                    }    
//                                        
//                                }
//                            }
//                            
//                            billMain.setAmount(totalAmount);
//                            try{
//                                PstBillMain.updateTotalAmount(oidBillMain,totalAmount);
//                            }catch(Exception ex){
//                                
//                            }
//                            
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
                if (oidPurchaseOrder != 0) {
                    try {
                        po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPurchaseOrder != 0) {
                    try {
                        po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidPurchaseOrder != 0) {
                    try {
                        po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                        purchDate = po.getPurchDate();
                        counter = po.getPoCodeCounter();
                        st = po.getPoStatus();
                    } catch (Exception exc) {
                    }
                    try {
                        prevPo = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                    } catch (Exception exc) {
                    }
                }

                frmpo.requestEntityObject(po);

                if (oidPurchaseOrder == 0) {
                    po.setPoCodeCounter(SessPurchaseOrder.getIntCode(po, purchDate, oidPurchaseOrder, counter, incrementAllPrType));
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(po));
                } else {
                    if(prevPo.getLocationId()== po.getLocationId()){
                    po.setPoCodeCounter(po.getPoCodeCounter());
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(po)); // po.getPoCode()
                    } else {
                    po.setPoCodeCounter(SessPurchaseOrder.getIntCode(po, purchDate, oidPurchaseOrder, counter, incrementAllPrType));
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(po));
                    }

                    // ini proses yang di gunakan untuk nomor PO yang di revisi
                    if(po.getCodeRevisi().length()>0){
                        po.setPoCode(po.getPoCode()+"-"+po.getCodeRevisi());
                    }
                }
                if (oidPurchaseOrder != 0) {
                    try {
                        PstPurchaseOrderItem.deleteByPurchaseOrder(oidPurchaseOrder);
                        long oid = PstPurchaseOrder.deleteExc(oidPurchaseOrder);
                        //RUBAH NILAI CMD MENJADI DELETE
                        //cmd = 6;
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                            try
                            {
                                insertHistory(userID, nameUser, cmd, oid);
                            }
                            catch(Exception e)
                            {
                                
                            }

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
    
    
    public int actionSR(int cmd, long oidPurchaseOrder,String nameUser, long userID) {
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
                
             case Command.UPDATE:
                if (oidPurchaseOrder != 0) {
                    try {
                        po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                        PurchaseOrder purchaseOrderUpdate = new PurchaseOrder();
                        frmpo.requestEntityObject(purchaseOrderUpdate);
                        po.setPoStatus(purchaseOrderUpdate.getPoStatus());
                        
                        PstPurchaseOrder.updateExc(po);
                        int integrationSO=1;
                        integrasiPOSales(integrationSO,oidPurchaseOrder);
                        
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                } 
                break;    

            case Command.SAVE:
                if (oidPurchaseOrder != 0) {
                    try {
                        po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                        purchDate = po.getPurchDate();
                        counter = po.getPoCodeCounter();
                        st = po.getPoStatus();
                    } catch (Exception exc) {
                    }

                    //by dyas 20131126
                    try {
                        prevPo = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                    } catch (Exception exc) {
                    }
                }

                frmpo.requestEntityObject(po);
                
                //cek perbedan waktu
                String tanggalAwal = "03-10-2012";
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date tanggal = new Date();
                Date tanggalAkhir = new Date();
                long bedaWaktu = 0;
                long bedaHari=0;
                int batas =10;
                if(po.getPurchDate()!=null){
                    tanggalAwal= Formater.formatDate(po.getPurchDate(), "dd-MM-yyyy");
                    try {
                        tanggal = (Date) dateFormat.parse(tanggalAwal);
                    } catch (ParseException ex) {
                        
                    }
                    tanggalAkhir = po.getPurchDateRequest();
                    bedaWaktu = Math.abs(tanggalAkhir.getTime()-tanggal.getTime());
                    bedaHari = TimeUnit.MILLISECONDS.toDays(bedaWaktu);
                    String hari = Long.toString(bedaHari);
                    po.setCreditTime(Integer.parseInt(hari));
                }
                
                if (oidPurchaseOrder == 0) {
                    po.setPoCodeCounter(SessPurchaseOrder.getIntCode(po, purchDate, oidPurchaseOrder, counter, incrementAllPrType));
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterialStoreRequest(po));
                } else {
                    if(prevPo.getLocationId()== po.getLocationId()){
                        po.setPoCodeCounter(po.getPoCodeCounter());
                        po.setPoCode(SessPurchaseOrder.getCodeOrderMaterialStoreRequest(po)); // po.getPoCode()
                    } else {
                        po.setPoCodeCounter(SessPurchaseOrder.getIntCode(po, purchDate, oidPurchaseOrder, counter, incrementAllPrType));
                        po.setPoCode(SessPurchaseOrder.getCodeOrderMaterialStoreRequest(po));                        
                    }

                    // ini proses yang di gunakan untuk nomor PO yang di revisi
                    if(po.getCodeRevisi().length()>0){
                        po.setPoCode(po.getPoCode()+"-"+po.getCodeRevisi());
                    }
                }


                if (frmpo.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (po.getOID() == 0) {
                   
                    try {
                        long oid = pstpo.insertExc(this.po);
                        //PROSES UNTUK MENYIMPAN HISTORY JIKA OID DARI ORDER PO =! 0
                        if(oid!=0)
                        {
                            insertHistory(userID, nameUser, cmd, oid);
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
                        //RUBAH NILAI CMD MENJADI UPDATE
                        int cmdHistory = Command.UPDATE;
                        long oid = pstpo.updateExc(this.po);
                        if(oid!=0)
                        {
                            //FUNGSI SQL UNTUK MELAKUKAN INSERT KE TABEL LOG_HISTORY_NEW KETIKA SUDAH 2 JAM
                            //int getDiffHour = PstLogSysHistory.getLastUpdateTime(oid);
                            /*if(getDiffHour > 2)
                            {
                               insertHistory(userID, nameUser, cmdHistory, oid, cmd, true);
                            }
                            else
                            {*/
                                insertHistory(userID, nameUser, cmdHistory, oid);
                            //}
                        }
                        
                        //cek status po jika sudah final dan merupakan integrasi sales order seperti queen tandorr
                        /**
                         * logika:
                         * 1. Jika status PO sudah final dan integrasi SO create, Sales order dengan status open bill berdasarkan dokument PO
                         */
                        int integrationSO=1;
                        integrasiPOSales(integrationSO,oid);

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidPurchaseOrder != 0) {
                    try {
                        po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
//                        PurchaseOrder purchaseOrderUpdate = new PurchaseOrder();
//                        frmpo.requestEntityObject(purchaseOrderUpdate);
//                        po.setPoStatus(purchaseOrderUpdate.getPoStatus());
                        
                        //PstPurchaseOrder.updateExc(po);
                        
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidPurchaseOrder != 0) {
                    try {
                        po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidPurchaseOrder != 0) {
                    try {
                        po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                        purchDate = po.getPurchDate();
                        counter = po.getPoCodeCounter();
                        st = po.getPoStatus();
                    } catch (Exception exc) {
                    }
                    try {
                        prevPo = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                    } catch (Exception exc) {
                    }
                }

                frmpo.requestEntityObject(po);

                if (oidPurchaseOrder == 0) {
                    po.setPoCodeCounter(SessPurchaseOrder.getIntCode(po, purchDate, oidPurchaseOrder, counter, incrementAllPrType));
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(po));
                } else {
                    if(prevPo.getLocationId()== po.getLocationId()){
                    po.setPoCodeCounter(po.getPoCodeCounter());
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(po)); // po.getPoCode()
                    } else {
                    po.setPoCodeCounter(SessPurchaseOrder.getIntCode(po, purchDate, oidPurchaseOrder, counter, incrementAllPrType));
                    po.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(po));
                    }

                    // ini proses yang di gunakan untuk nomor PO yang di revisi
                    if(po.getCodeRevisi().length()>0){
                        po.setPoCode(po.getPoCode()+"-"+po.getCodeRevisi());
                    }
                }
                if (oidPurchaseOrder != 0) {
                    try {
                        PstPurchaseOrderItem.deleteByPurchaseOrder(oidPurchaseOrder);
                        long oid = PstPurchaseOrder.deleteExc(oidPurchaseOrder);
                        //RUBAH NILAI CMD MENJADI DELETE
                        //cmd = 6;
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                            try
                            {
                                insertHistory(userID, nameUser, cmd, oid);
                            }
                            catch(Exception e)
                            {
                                
                            }

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
     * Insert Data ke log History
     */
    public  void insertHistory(long userID, String nameUser, int cmd, long oid)
    {
        try
        {
           LogSysHistory logSysHistory = new LogSysHistory();
           logSysHistory.setLogUserId(userID);
           logSysHistory.setLogLoginName(nameUser);
           logSysHistory.setLogApplication("Prochain");
           logSysHistory.setLogOpenUrl("purchasing/material/pom/pomaterial_edit.jsp");
           logSysHistory.setLogUpdateDate(dateLog);
           logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
           logSysHistory.setLogUserAction(Command.commandString[cmd]);
           logSysHistory.setLogDocumentNumber(po.getPoCode());
           logSysHistory.setLogDocumentId(oid);
           logSysHistory.setLogDetail(this.po.getLogDetail(prevPo));

           //jika sudah lewat 2 jam di log
//           if(timeUpdate == true)
//           {
//               long oid2 = PstLogSysHistory.insertLog(logSysHistory);
//           }//jika log detail tidak kosong atau cmd nya sama dengan delete di simpan
//           else
           if(!logSysHistory.getLogDetail().equals("") || cmd==Command.DELETE)
           {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
           }
      }
      catch(Exception e)
      {

      }
    }
    
   public void integrasiPOSales(int integrationSO, long oid){
       
        if(this.po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL && integrationSO==1){
                //create opname
                MatStockOpname soItem = new MatStockOpname();
                soItem.setCategoryId(po.getCategoryId());
                soItem.setLocationId(po.getLocationId());
                soItem.setStockOpnameDate(new Date());
                soItem.setStockOpnameStatus(0);
                soItem.setSupplierId(0);
                long oidOpname = 0;
                try {
                    oidOpname = PstMatStockOpname.insertExc(soItem);
                } catch (DBException ex) {
                }

                if(oidOpname!=0){
                    String whereOrderItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"='"+oid+"'";
                    Vector listSOItem = PstPurchaseOrderItem.list(0, 0, whereOrderItem);
                    if (listSOItem != null && listSOItem.size() > 0) {
                        for (int i = 0; i < listSOItem.size(); i++) {
                             Vector temp = (Vector)listSOItem.get(i);
                             PurchaseOrderItem purchaseOrderItem = (PurchaseOrderItem) temp.get(0);
                             Material material = (Material)temp.get(1);

                             MatStockOpnameItem matItem = new MatStockOpnameItem();
                             matItem.setStockOpnameId(oidOpname);
                             matItem.setMaterialId(purchaseOrderItem.getMaterialId());
                             matItem.setUnitId(purchaseOrderItem.getUnitId());
                             matItem.setQtyOpname(purchaseOrderItem.getQtyInputStock());
                             matItem.setCost(0);
                             try{
                                    long oidOpnameItem = PstMatStockOpnameItem.insertExc(matItem);
                             }catch(Exception ex){

                             }
                    }
                }
             }
        }

        if(this.po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL && integrationSO==1){
            //buat sales order
            //
            BillMain billMain = new BillMain();
            billMain.setBillDate(new Date());
            //disini di cek berdasarkan lokasi

            Vector customer = PstMemberReg.listFromLocationId(0, 0, PstMemberReg.fieldNames[PstMemberReg.FLD_LOCATION_ID]+"='"+this.po.getLocationId()+"'", "");
            long customerId = 0;
            long priceTypeId=0;
            long standartId = Long.parseLong(com.dimata.system.entity.PstSystemProperty.getValueByName("ID_STANDART_RATE"));
            String address = "";
            MemberReg member = new MemberReg();
            if(customer.size()>0){
                for (int i = 0; i < customer.size(); i++) {
                    member = (MemberReg) customer.get(0);
                    customerId=member.getOID();
                    address=member.getBussAddress();
                    MemberGroup memberGroup=  new MemberGroup();
                    try {
                        memberGroup= PstMemberGroup.fetchExc(member.getMemberGroupId());
                    } catch (DBException ex) {
                        //Logger.getLogger(CtrlPurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    priceTypeId=memberGroup.getPriceTypeId();
                }
            }

            MemberReg memberSupp = new MemberReg();

            long locationId=0;
            try{
                memberSupp=PstMemberReg.fetchExc(this.po.getSupplierId());
                locationId = memberSupp.getLocationId();

            }catch(Exception ex){
            }

            billMain.setCustomerId(customerId);
            billMain.setCoverNumber("1");
            billMain.setLocationId(locationId);
            billMain.setParentId(0);
            billMain.setCashCashierId(1);

            //set status open bill
            billMain.setTransactionStatus(1);
            billMain.setTransctionType(0);
            billMain.setDocType(0);

            //buatkan no bill
            billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billMain.getBillDate(), 0, 0, true));
            String noInvoice = SessBilling.getCodeOrderMaterial(billMain);
            billMain.setInvoiceNumber(noInvoice);
            billMain.setInvoiceNo(noInvoice);
            billMain.setNotes(""+this.po.getPoCode()+";"+this.po.getLocationId()+";"+this.po.getOID());
            billMain.setCashPendingOrderId(0);
            billMain.setAppUserId(0);
            billMain.setShiftId(0);
            billMain.setDiscPct(0);
            billMain.setDiscount(0);
            billMain.setTaxPercentage(0);
            billMain.setTaxValue(0);

            //set currency id
            billMain.setCurrencyId(1);
            billMain.setRate(1);

            billMain.setStockLocationId(locationId);
            billMain.setShippingAddress(address);
            billMain.setShippingCountry(member.getHomeCountry());
            billMain.setShippingCity(member.getCompState());
            billMain.setShippingProvince(member.getProvince());

            long oidBillMain = 0;
            try{
                oidBillMain = PstBillMain.insertExc(billMain);
            }catch(Exception ex){

            }
            double totalAmount=0.0;
            if(oidBillMain!=0){
                Vector listSOItem = new Vector();
                String whereOrderItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"='"+oid+"'";
                listSOItem = PstPurchaseOrderItem.list(0, 0, whereOrderItem);
                if (listSOItem != null && listSOItem.size() > 0) {
                    for (int i = 0; i < listSOItem.size(); i++) {

                        Vector temp = (Vector)listSOItem.get(i);
                        PurchaseOrderItem purchaseOrderItem = (PurchaseOrderItem) temp.get(0);
                        Material material = (Material)temp.get(1);

                        Billdetail billdetail = new Billdetail();

                        billdetail.setBillMainId(oidBillMain);
                        billdetail.setUnitId(purchaseOrderItem.getUnitId());
                        billdetail.setMaterialId(purchaseOrderItem.getMaterialId());
                        billdetail.setQtyRequestSo(purchaseOrderItem.getQuantity());

                        //double price = PstPriceTypeMapping.getPrice(purchaseOrderItem.getMaterialId(), standartId, priceTypeId);
                        double price = 0;
                        double pctUpPrice = Double.valueOf(PstSystemProperty.getValueByName("PERCENTASE_UP_PRICE_STORE_REQUEST"));
                        if (pctUpPrice==0){
                            price = material.getAveragePrice();
                        }else{
                            price = material.getAveragePrice() + (material.getAveragePrice()*pctUpPrice/100);
                        }
                        
                        double totalPrice=billdetail.getQty()*price;
                        billdetail.setItemPrice(price);
                        billdetail.setTotalPrice(totalPrice);
                        billdetail.setSku(material.getSku());
                        billdetail.setItemName(material.getName());
                        billdetail.setQtyStock(purchaseOrderItem.getQuantity());
                        billdetail.setCost(material.getAveragePrice());
                        totalAmount =totalAmount+billdetail.getTotalAmount();
                        try{
                            long oidBillDetail = PstBillDetail.insertExc(billdetail);
                        }catch(Exception ex){
                        }
                    }    

                }
            }

            billMain.setAmount(totalAmount);
            try{
                PstBillMain.updateTotalAmount(oidBillMain,totalAmount);
            }catch(Exception ex){

            }

        }
   
   }
}
