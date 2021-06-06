/*
 * CtrlCashMaster.java
 *
 * Created on January 8, 2004, 9:53 AM
 */
package com.dimata.pos.form.billing;

/**
 *
 * @author  gedhy
 */
// java package
import java.util.*;
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
import com.dimata.pos.session.billing.*;
import com.dimata.posbo.db.DBException;

import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;

public class  CtrlBillMain  extends Control implements I_Language   {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    
    
    public static int BILL_CASH = 0;
    public static int BILL_OPEN_BILL = 1;
    public static int BILL_TRANS_CREDIT = 2;
    public static int BILL_TRANS_CREDIT_LUNAS = 3;
    public static int BILL_RETURN_TUNAI = 4;
    public static int BILL_RETURN_KREDIT = 5;
    public static int BILL_BATAL = 6;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Code Material Sales sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Material Sales Code already exist", "Data incomplete"}
    };
    private int start;
    private String msgString;
    private BillMain billMain;
    private PstBillMain pstBillMain;
    private FrmBillMain frmBillMain;
    int language = LANGUAGE_FOREIGN;

    public CtrlBillMain(HttpServletRequest request) {
        msgString = "";
        billMain = new BillMain();
        try {
            pstBillMain = new PstBillMain(0);
        } catch (Exception e) {
            ;
        }
        frmBillMain = new FrmBillMain(request, billMain);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmBillMain.addError(frmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public BillMain getBillMain() {
        return billMain;
    }

    public FrmBillMain getForm() {
        return frmBillMain;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidBillMain, long idCustomer) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        int counter = 0;
        long idCustTmp=0;
        boolean incrementAllPrType = true;
        BillMain prevBillMain = null;

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidBillMain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidBillMain);
                        counter = billMain.getInvoiceCounter();
                        idCustTmp = billMain.getCustomerId();
                    } catch (Exception exc) {
                    }
                    try {
                        prevBillMain = PstBillMain.fetchExc(oidBillMain);
                    } catch (Exception exc) {
                    }
                }
                
                frmBillMain.requestEntityObject(billMain);

                billMain.setBillDate(new Date());
                Date billdate = null;
                billdate = billMain.getBillDate();
                //cek customerId
                if(idCustTmp!=0){
                    billMain.setCustomerId(idCustTmp);
                }
                if(idCustomer != 0){
                    billMain.setCustomerId(idCustomer);
                }

                 Date dueDatePayment = new Date();
                 if(billMain.getDueDatePayment()>0){
                    dueDatePayment.setDate(dueDatePayment.getDate()+billMain.getDueDatePayment());
                 }
                 
                 billMain.setDateTermOfPayment(dueDatePayment);

                /**
                 * Ari Wiweka 20130625
                 * Generete Invoice Number
                 */
                String noInvoice ="";
                if(billMain.getCashCashierId()!= 1 && billMain.getLocationId() != 1){
                if (oidBillMain == 0 ) {
                    billMain.setDiscType(3);
                    billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType));
                    noInvoice = SessBilling.getCodeOrderMaterial(billMain);
                    billMain.setInvoiceNumber(noInvoice);
                    billMain.setInvoiceNo(noInvoice);
                } else {
                    if (prevBillMain.getLocationId() == billMain.getLocationId()) {
                       // billMain.setInvoiceCounter(counter);
                        billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType));
                        noInvoice=SessBilling.getCodeOrderMaterial(billMain);
                        billMain.setInvoiceNumber(noInvoice);
                        billMain.setInvoiceNo(noInvoice); // billMain.getPoCode()
                    } else {
                        billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType));
                        noInvoice=SessBilling.getCodeOrderMaterial(billMain);
                        billMain.setInvoiceNumber(noInvoice);
                        billMain.setInvoiceNo(noInvoice);
                    }
                }
                }else{
//                    int value = 0;
//                    billMain.setInvoiceCounter(value);
//                    billMain.setInvoiceNumber(""+value);
//                    billMain.setInvoiceNo(""+value);
                }

                if (frmBillMain.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (billMain.getOID() == 0) {
                    try {
                        long oid = pstBillMain.insertExc(this.billMain);
                   
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstBillMain.updateExc(this.billMain);
                   
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidBillMain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidBillMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidBillMain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidBillMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidBillMain != 0) {
                    try {
                        /**
                         * Ari wiweka 20130716
                         * delete bill detail
                         */
                        long oidbilldetail = PstBillDetail.deleteBillDetail(oidBillMain);
                        long oid = PstBillMain.deleteExc(oidBillMain);
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
    
    public int action(int cmd,BillMain entBillMain ){
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        int counter = 0;

        
        switch (cmd) {
            case Command.SAVE :
                if (entBillMain.getOID() == 0) {
                    try {
                        long oid = pstBillMain.insertExc(entBillMain);
                   
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
                        long oid = pstBillMain.updateExc(entBillMain);
                   
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
        }
        
        return rsCode;
    }
    
     public int actionSaveChart(long idCustomer, Vector fetchDataTemp, long oidBillMain) {
         return actionSaveChart(idCustomer,fetchDataTemp,oidBillMain,"");
     }
    
     public int actionSaveChart(long idCustomer, Vector fetchDataTemp, long oidBillMain, String noteGlobal) {
        int sucsess=0;
         //save bill detail dengan transaksi open bill
        try{
            BillMain billMain = PstBillMain.fetchExc(oidBillMain);
            String prevNote = billMain.getNotes();
            billMain.setNotes( prevNote +" " +noteGlobal);
            long oid = PstBillMain.updateExc(billMain);
        }catch(Exception ex){
        }
        
        Date now = new Date();
        if(fetchDataTemp.size()>0 && oidBillMain!=0){
            for (int i = 0; i < fetchDataTemp.size(); i++) {
                 Vector vt = (Vector) fetchDataTemp.get(i);
                 Material material = (Material) vt.get(0);
                 String note = (String) vt.get(3);
                 PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
                 String nameMat = (String) vt.get(4);
                 int amount = (Integer) vt.get(2);
                 Billdetail billdetail = new Billdetail();
                 billdetail.setBillMainId(oidBillMain);
                 billdetail.setMaterialId(Long.parseLong(material.getMaterialId()));
                 billdetail.setQty(amount);
                 billdetail.setItemPrice(priceTypeMapping.getPrice());
                 double totalItemPrice=amount*priceTypeMapping.getPrice();
                 billdetail.setTotalPrice(totalItemPrice);
                 billdetail.setSku(material.getSku());
                 billdetail.setItemName(nameMat);
                 billdetail.setMaterialType(material.getMaterialType());
                 billdetail.setCost(material.getAveragePrice());
                 billdetail.setUnitId(material.getDefaultStockUnitId());
                 billdetail.setLengthOrder(now);
                 billdetail.setNote(note);
                 try{
                      PstBillDetail.insertExc(billdetail);
                 }catch(Exception ex){
                      sucsess=1;
                 }
             }
        }
         
        return sucsess;
     }
     
     public int actionUpdateItem(int cmd,long oidBillDetail, int status) {
         return actionUpdateItem(cmd,oidBillDetail,status,"");
     }
     
     public int actionUpdateItem(int cmd,long oidBillDetail, int status, String note) {
           int sucsess=0;
           switch (cmd) {
               case Command.YES:
                    int xx = PstBillDetail.updateStatusItem(oidBillDetail,status);
                    break;
                case Command.CANCEL:
                     try {
                        BillMain billMain = PstBillMain.fetchExc(oidBillDetail);
                        billMain.setDocType(0);
                        billMain.setTransctionType(0);
                        billMain.setTransactionStatus(2);
                        billMain.setNotes(note);
                        PstBillMain.updateExc(billMain);
                    } catch (Exception ex) {
                        //Logger.getLogger(CtrlBillMain.class.getName()).log(Level.SEVERE, null, ex);
                    } 
               case Command.DELETE:
                   try{
                       Billdetail billdetail = PstBillDetail.fetchExc(oidBillDetail);
                       billdetail.setNote(note);
                       billdetail.setLengthFinishOrder(new Date());
                       long oid = PstBillDetailVoid.insertExc(billdetail);
                       if(oid!=0){
                           long xxy = PstBillDetail.deleteExc(oidBillDetail);
                       }
                   }catch(Exception ex ){
                   }
                   break;
               case Command.APPROVE:
                    try {
                        Billdetail billDetail = PstBillDetail.fetchExc(oidBillDetail);
                        billDetail.setItemPrice(0);
                        billDetail.setTotalPrice(0);
                        billDetail.setNote(note);
                        PstBillDetail.updateExc(billDetail);
                    } catch (DBException ex) {
                        //Logger.getLogger(CtrlBillMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
               default:
           }
           return sucsess;
     }
     
     public int actionSaveBillMainFromOpenBill (int cmd, long oidBillMain, long idCustomer, int typeBill,long oidOpenbillmain) {
        int rsCode = RSLT_OK;
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int counter = 0;
        long idCustTmp=0;
        boolean incrementAllPrType = true;
        BillMain prevBillMain = null;
        
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidBillMain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidBillMain);
                        counter = billMain.getInvoiceCounter();
                        idCustTmp = billMain.getCustomerId();
                    } catch (Exception exc) {
                    }
                    try {
                        prevBillMain = PstBillMain.fetchExc(oidBillMain);
                    } catch (Exception exc) {
                    }
                }
                
                frmBillMain.requestEntityObject(billMain);
                
                if (oidOpenbillmain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidOpenbillmain);
                        billMain.setOID(0);
                    } catch (Exception exc) {
                    }
                }
                frmBillMain.xrequestEntityObject(billMain);
                
                if(typeBill == BILL_CASH){
                    billMain.setDocType(0);
                    billMain.setTransactionStatus(0);
                    billMain.setTransctionType(0);
                }
                
                if(typeBill == BILL_BATAL){
                    billMain.setDocType(0);
                    billMain.setTransactionStatus(2);
                    billMain.setTransctionType(0);
                }
                
                if(typeBill == BILL_OPEN_BILL){
                    billMain.setDocType(0);
                    billMain.setTransactionStatus(1);
                    billMain.setTransctionType(0);
                }
                
                if(typeBill == BILL_RETURN_KREDIT){
                    billMain.setDocType(1);
                    billMain.setTransactionStatus(0);
                    billMain.setTransctionType(1);
                }
                
                if(typeBill == BILL_RETURN_TUNAI){
                    billMain.setDocType(1);
                    billMain.setTransactionStatus(0);
                    billMain.setTransctionType(0);    
                }
                if(typeBill == BILL_TRANS_CREDIT){
                    billMain.setDocType(0);
                    billMain.setTransactionStatus(1);
                    billMain.setTransctionType(1);
                }
                
                if(typeBill == BILL_TRANS_CREDIT_LUNAS){
                    billMain.setDocType(0);
                    billMain.setTransactionStatus(0);
                    billMain.setTransctionType(1);
                }
                
                billMain.setBillDate(new Date());
                Date billdate = null;
                billdate = billMain.getBillDate();
                //cek customerId
                if(idCustTmp!=0){
                    billMain.setCustomerId(idCustTmp);
                }
                if(idCustomer != 0){
                    billMain.setCustomerId(idCustomer);
                }

                 Date dueDatePayment = new Date();
                 if(billMain.getDueDatePayment()>0){
                    dueDatePayment.setDate(dueDatePayment.getDate()+billMain.getDueDatePayment());
                 }
                 
                 billMain.setDateTermOfPayment(dueDatePayment);

                /**
                 * Ari Wiweka 20130625
                 * Generete Invoice Number
                 */
                String noInvoice ="";
                if(billMain.getCashCashierId()!= 1 && billMain.getLocationId() != 1){
                    if (oidBillMain == 0 ) {
                        billMain.setDiscType(3);
                        billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType));
                        noInvoice = SessBilling.getCodeOrderMaterial(billMain);
                        billMain.setInvoiceNumber(noInvoice);
                        billMain.setInvoiceNo(noInvoice);
                    } else {
                        if (prevBillMain.getLocationId() == billMain.getLocationId()) {
                           // billMain.setInvoiceCounter(counter);
                            billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType));
                            noInvoice=SessBilling.getCodeOrderMaterial(billMain);
                            billMain.setInvoiceNumber(noInvoice);
                            billMain.setInvoiceNo(noInvoice); // billMain.getPoCode()
                        } else {
                            billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType));
                            noInvoice=SessBilling.getCodeOrderMaterial(billMain);
                            billMain.setInvoiceNumber(noInvoice);
                            billMain.setInvoiceNo(noInvoice);
                        }
                    }
                }

                if (frmBillMain.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (billMain.getOID() == 0) {
                    try {
                        long oid = pstBillMain.insertExc(this.billMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstBillMain.updateExc(this.billMain);
                    
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidBillMain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidBillMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidBillMain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidBillMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidBillMain != 0) {
                    try {
                        /**
                         * Ari wiweka 20130716
                         * delete bill detail
                         */
                        long oidbilldetail = PstBillDetail.deleteBillDetail(oidBillMain);
                        long oid = PstBillMain.deleteExc(oidBillMain);
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
     
     
     
     public int actionSaveOpenBill(int cmd, long oidBillMain, long idCustomer) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        int counter = 0;
        long idCustTmp=0;
        boolean incrementAllPrType = true;
        BillMain prevBillMain = null;

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidBillMain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidBillMain);
                        counter = billMain.getInvoiceCounter();
                        idCustTmp = billMain.getCustomerId();
                    } catch (Exception exc) {
                    }
                    try {
                        prevBillMain = PstBillMain.fetchExc(oidBillMain);
                    } catch (Exception exc) {
                    }
                }
                
                frmBillMain.requestEntityObject(billMain);

                billMain.setBillDate(new Date());
                Date billdate = null;
                billdate = billMain.getBillDate();
                //cek customerId
                if(idCustTmp!=0){
                    billMain.setCustomerId(idCustTmp);
                }
                if(idCustomer != 0){
                    billMain.setCustomerId(idCustomer);
                }

                 Date dueDatePayment = new Date();
                 if(billMain.getDueDatePayment()>0){
                    dueDatePayment.setDate(dueDatePayment.getDate()+billMain.getDueDatePayment());
                 }
                 
                 billMain.setDateTermOfPayment(dueDatePayment);

                /**
                 * Ari Wiweka 20130625
                 * Generete Invoice Number
                 */
                String noInvoice ="";
                if(billMain.getCashCashierId()!= 1 && billMain.getLocationId() != 1){
                if (oidBillMain == 0 ) {
                    billMain.setDiscType(3);
                    billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType));
                    noInvoice = SessBilling.getCodeOrderMaterial(billMain);
                    billMain.setInvoiceNumber(noInvoice);
                    billMain.setInvoiceNo(noInvoice);
                } else {
                    if (prevBillMain.getLocationId() == billMain.getLocationId()) {
                       // billMain.setInvoiceCounter(counter);
                        billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType));
                        noInvoice=SessBilling.getCodeOrderMaterial(billMain);
                        billMain.setInvoiceNumber(noInvoice);
                        billMain.setInvoiceNo(noInvoice); // billMain.getPoCode()
                    } else {
                        billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billdate, oidBillMain, counter, incrementAllPrType));
                        noInvoice=SessBilling.getCodeOrderMaterial(billMain);
                        billMain.setInvoiceNumber(noInvoice);
                        billMain.setInvoiceNo(noInvoice);
                    }
                }
                }else{
//                    int value = 0;
//                    billMain.setInvoiceCounter(value);
//                    billMain.setInvoiceNumber(""+value);
//                    billMain.setInvoiceNo(""+value);
                }

                if (frmBillMain.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (billMain.getOID() == 0) {
                    try {
                        long oid = pstBillMain.insertExc(this.billMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstBillMain.updateExc(this.billMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidBillMain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidBillMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidBillMain != 0) {
                    try {
                        billMain = PstBillMain.fetchExc(oidBillMain);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidBillMain != 0) {
                    try {
                        /**
                         * Ari wiweka 20130716
                         * delete bill detail
                         */
                        long oidbilldetail = PstBillDetail.deleteBillDetail(oidBillMain);
                        long oid = PstBillMain.deleteExc(oidBillMain);
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
     
     public int actionCommand(int cmd , long oidBillMain){

		msgString = "";

		int excCode = I_DBExceptionInfo.NO_EXCEPTION;

		int rsCode = RSLT_OK;

		switch(cmd){

			case Command.ADD :

				break;



			case Command.SAVE :

				if(oidBillMain != 0){

					try{

						billMain = PstBillMain.fetchExc(oidBillMain);

					}catch(Exception exc){

					}

				}



				frmBillMain.requestEntityObject(billMain);



				if(frmBillMain.errorSize()>0) {

					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);

					return RSLT_FORM_INCOMPLETE ;

				}



				if(billMain.getOID()==0){

					try{

						long oid = pstBillMain.insertExc(this.billMain);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);

					}



				}else{

					try {

						long oid = pstBillMain.updateExc(this.billMain);

					}catch (Exception exc){

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 

					}



				}

				break;



			case Command.EDIT :

				if (oidBillMain != 0) {

					try {

						billMain = PstBillMain.fetchExc(oidBillMain);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.ASK :

				if (oidBillMain != 0) {

					try {

						billMain = PstBillMain.fetchExc(oidBillMain);

					} catch (Exception exc){ 

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			case Command.DELETE :

				if (oidBillMain != 0){

					try{

						long oid = PstBillMain.deleteExc(oidBillMain);

						if(oid!=0){

							msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);

							excCode = RSLT_OK;

						}else{

							msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);

							excCode = RSLT_FORM_INCOMPLETE;

						}

					}catch(Exception exc){	

						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);

					}

				}

				break;



			default :



		}

		return rsCode;

	}
    
}
