/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.pos.form.billing;

/**
 *
 * @author Wiweka
 */
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
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;

public class  CtrlBillDetail  extends Control implements I_Language   {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Code Material Sales sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Material Sales Code already exist", "Data incomplete"}
    };
    private int start;
    private String msgString;
    private Billdetail billdetail;
    private PstBillDetail pstBillDetail;
    private PstBillMain pstBillMain;
    private FrmBillDetail frmBillDetail;
    int language = LANGUAGE_FOREIGN;

    public CtrlBillDetail(HttpServletRequest request) {
        msgString = "";
        billdetail = new Billdetail();
        try {
            pstBillDetail = new PstBillDetail(0);
        } catch (Exception e) {
            ;
        }
        frmBillDetail = new FrmBillDetail(request, billdetail);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmBillDetail.addError(frmBillDetail.FRM_FIELD_CASH_BILL_DETAIL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Billdetail getBillDetail() {
        return billdetail;
    }

    public FrmBillDetail getForm() {
        return frmBillDetail;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long getBillDetail, long oidBillMain) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (getBillDetail != 0) {
                    try {
                        billdetail = PstBillDetail.fetchExc(getBillDetail);
                    } catch (Exception exc) {
                    }
                }

                frmBillDetail.requestEntityObject(billdetail);
                billdetail.setBillMainId(oidBillMain);

                if (frmBillDetail.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (billdetail.getOID() == 0) {
                    try {
                        long oid = pstBillDetail.insertExc(this.billdetail);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                        if (oid != 0) {
                            double amount = billdetail.getTotalAmount();
                            double totalAmount = pstBillMain.updateTotalAmount(oidBillMain, amount);
                        } else {
                            msgString = getSystemMessage(1);
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
                        long oid = pstBillDetail.updateExc(this.billdetail);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (getBillDetail != 0) {
                    try {
                        billdetail = PstBillDetail.fetchExc(getBillDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (getBillDetail != 0) {
                    try {
                        billdetail = PstBillDetail.fetchExc(getBillDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (getBillDetail != 0) {
                    try {
                        long oid = PstBillDetail.deleteExc(getBillDetail);
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

          case Command.GOTO:
                if (billdetail.getOID() == 0) {
                    try {

                        long oid = pstBillDetail.insertExc(this.billdetail);

                        if (oid != 0) {
                            double amount = billdetail.getTotalAmount();
                            double totalAmount = pstBillMain.updateTotalAmount(oidBillMain, amount);
                        } else {
                            msgString = getSystemMessage(1);
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
                        long oid = pstBillDetail.updateExc(this.billdetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;
            default:
        }
        return rsCode;
    }
    
    public int action(int cmd, Billdetail entBillDetail){
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        
        switch (cmd){
            case Command.SAVE :
                if (entBillDetail.getOID() == 0) {
                    try {
                        long oid = pstBillDetail.insertExc(entBillDetail);
                        if (oid != 0) {
                            double amount = entBillDetail.getTotalAmount();
                            double totalAmount = pstBillMain.updateTotalAmount(entBillDetail.getBillMainId(), amount);
                        } else {
                            msgString = getSystemMessage(1);
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
                        long oid = pstBillDetail.updateExc(entBillDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
            break;
            case Command.DELETE:
                if (entBillDetail.getOID() != 0) {
                    try {
                        long oid = PstBillDetail.deleteExc(entBillDetail.getOID());
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
        }
        
        return rsCode;
    }
    
    public int action(int cmd,long oidBillMain,HttpServletRequest request, Vector listBill, long customerId, long priceTypeId, long oidOpenbillmain) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
                
            case Command.SAVE:
                
                long standartId = Long.parseLong(com.dimata.system.entity.PstSystemProperty.getValueByName("ID_STANDART_RATE"));
                double totalAmount=0.0;
                
                for(int i=0; i<listBill.size(); i++){
                    
                    long materialId= FRMQueryString.requestLong(request, FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_MATERIAL_ID]+"_"+i);
                    
                    if(materialId!=0){
                        
                        Billdetail billdetail = new Billdetail();
                        Material material = new  Material();

                        try{
                            material= PstMaterial.fetchExc(materialId);
                        }catch(Exception ex){}

                        billdetail.setBillMainId(oidBillMain);
                        billdetail.setUnitId(FRMQueryString.requestLong(request, FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_UNIT_ID]+"_"+i));
                        billdetail.setMaterialId(materialId);

                        billdetail.setQty(FRMQueryString.requestDouble(request, FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]+"_"+i));

                        double price = PstPriceTypeMapping.getPrice(materialId, standartId, priceTypeId);
                        double totalPrice=billdetail.getQty()*price;
                        billdetail.setItemPrice(price);
                        billdetail.setTotalPrice(totalPrice);
                        billdetail.setSku(FRMQueryString.requestString(request, FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]+"_"+i));
                        billdetail.setItemName(FRMQueryString.requestString(request, FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]+"_"+i));
                        billdetail.setQtyStock(billdetail.getQty());
                        billdetail.setCost(material.getAveragePrice());
                        double totalCost = material.getAveragePrice()*billdetail.getQty();
                        billdetail.setTotalCost(totalCost);
                        totalAmount =totalAmount+billdetail.getTotalAmount();
                        
                        try{
                            long oidBillDetail = PstBillDetail.insertExc(billdetail);
                        }catch(Exception ex){
                        }
                        
                    }   
                }
                
                try{
                    PstBillMain.updateTotalAmount(oidBillMain,totalAmount);
                }catch(Exception ex){

                }
                
                //update 
                try{
                  long updateOpenBill= PstBillMain.updateSalesOrder(oidOpenbillmain, 0, 0, 1, 2);
                }catch(Exception ex){

                }
                
                break;

            case Command.EDIT:
                
                break;

            case Command.ASK:
                
                break;

            case Command.DELETE:
                
                break;

          case Command.GOTO:
                
                break;
            default:
        }
        return rsCode;
    }
    
    
    
    public int actionDetail(int cmd, long getBillDetail, long oidBillMain,long priceTypeId,HttpServletRequest request, long billDetailId) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (getBillDetail != 0) {
                    try {
                        billdetail = PstBillDetail.fetchExc(getBillDetail);
                    } catch (Exception exc) {
                    }
                }

                //frmBillDetail.requestEntityObject(billdetail);
                //materialId;
                billdetail.setOID(billDetailId);
                billdetail.setMaterialId(FRMQueryString.requestLong(request, "materialId"));
                billdetail.setBillMainId(oidBillMain);
                
                Material material = new  Material();

                try{
                    material= PstMaterial.fetchExc(billdetail.getMaterialId());
                }catch(Exception ex){
                
                }
                
                long standartId = Long.parseLong(com.dimata.system.entity.PstSystemProperty.getValueByName("ID_STANDART_RATE"));
                double price = PstPriceTypeMapping.getPrice(billdetail.getMaterialId(), standartId, priceTypeId);
                billdetail.setBillMainId(oidBillMain);
                billdetail.setUnitId(material.getDefaultStockUnitId());  
                billdetail.setQty(FRMQueryString.requestDouble(request, FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]));
                double totalPrice=billdetail.getQty()*price;
                billdetail.setItemPrice(price);
                billdetail.setTotalPrice(totalPrice);
                billdetail.setSku(FRMQueryString.requestString(request, "matCode"));
                billdetail.setItemName(FRMQueryString.requestString(request, "matItem"));
                billdetail.setQtyStock(billdetail.getQty());
                billdetail.setCost(material.getAveragePrice());
                double totalCost = material.getAveragePrice()*billdetail.getQty();
                billdetail.setTotalCost(totalCost);
                
//                if (frmBillDetail.errorSize() > 0) {
//                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
//                    return RSLT_FORM_INCOMPLETE;
//                }

                if (billdetail.getOID() == 0) {
                    try {
                        long oid = pstBillDetail.insertExc(this.billdetail);
                        if (oid != 0) {
                            double amount = billdetail.getTotalAmount();
                            double totalAmount = pstBillMain.updateTotalAmount(oidBillMain, amount);
                        } else {
                            msgString = getSystemMessage(1);
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
                        long oid = pstBillDetail.updateExc(this.billdetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (getBillDetail != 0) {
                    try {
                        billdetail = PstBillDetail.fetchExc(getBillDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (getBillDetail != 0) {
                    try {
                        billdetail = PstBillDetail.fetchExc(getBillDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (getBillDetail != 0) {
                    try {
                        long oid = PstBillDetail.deleteExc(getBillDetail);
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

          case Command.GOTO:
                if (billdetail.getOID() == 0) {
                    try {

                        long oid = pstBillDetail.insertExc(this.billdetail);

                        if (oid != 0) {
                            double amount = billdetail.getTotalAmount();
                            double totalAmount = pstBillMain.updateTotalAmount(oidBillMain, amount);
                        } else {
                            msgString = getSystemMessage(1);
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
                        long oid = pstBillDetail.updateExc(this.billdetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;
            default:
        }
        return rsCode;
    }
    
}
