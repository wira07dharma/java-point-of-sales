package com.dimata.posbo.form.purchasing;

/* java package */

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.posbo.db.*;
/* project package */
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.purchasing.*;
import com.dimata.posbo.entity.warehouse.MatStockOpname;
import com.dimata.posbo.entity.warehouse.MatStockOpnameItem;
import com.dimata.posbo.entity.warehouse.PstMatStockOpname;
import com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.form.*;
import com.dimata.qdep.system.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.*;

public class CtrlPurchaseOrderItem extends Control implements I_Language {
    public static final int RSLT_OK = 0;
    public static final int RSLT_UNKNOWN_ERROR = 1;
    public static final int RSLT_MATERIAL_EXIST = 2;
    public static final int RSLT_FORM_INCOMPLETE = 3;
    public static final int RSLT_QTY_NULL = 4;
    public static final int RSLT_QTY_STOCK_NULL = 5;
    public static final int RSLT_NO_ITEM = 6;
    public static final int RSLT_PRICE_NULL = 7;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Material sudah ada", "Data tidak lengkap", "Jumlah material tidak boleh nol ...","Jumlah stock material tidak boleh nol ...","Silahkan pilih material..","Harga stock material tidak boleh nol ..."},
        {"Succes", "Can not process", "Material already exist ...", "Data incomplete", "Quantity may not zero ...","Quantity stock not zero ...","Please select material..","Stock price not zero ..."}
    };
    
    private int start;
    private String msgString;
    private PurchaseOrderItem poItem;
    private PstPurchaseOrderItem pstPoItem;
    private PstPurchaseOrder pstPo;
    private FrmPurchaseOrderItem frmPoItem;
    private Date dateLog = new Date();
    PurchaseOrderItem prevPoItem = null;
    PurchaseOrder purchaseOrder = null;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlPurchaseOrderItem() {
    }
    
    public CtrlPurchaseOrderItem(HttpServletRequest request) {
        msgString = "";
        poItem = new PurchaseOrderItem();
        try {
            pstPoItem = new PstPurchaseOrderItem(0);
        } catch (Exception e) {
            ;
        }
        frmPoItem = new FrmPurchaseOrderItem(request, poItem);
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case RSLT_MATERIAL_EXIST:
                this.frmPoItem.addError(frmPoItem.FRM_FIELD_MATERIAL_ID, resultText[language][RSLT_MATERIAL_EXIST]);
                return resultText[language][RSLT_MATERIAL_EXIST];
            case RSLT_QTY_NULL:
                this.frmPoItem.addError(frmPoItem.FRM_FIELD_QUANTITY, resultText[language][RSLT_QTY_NULL]);
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
            case RSLT_NO_ITEM:
                return RSLT_NO_ITEM;
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
    
    public PurchaseOrderItem getPurchaseOrderItem() {
        return poItem;
    }
    
    public FrmPurchaseOrderItem getForm() {
        return frmPoItem;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public int getStart() {
        return start;
    }
    public int action(int cmd, long oidPoItem, long oidPurchaseOrder){
     return action(cmd,oidPoItem, oidPurchaseOrder, "", 0);
    }
    
    public int actionUsingHash(int cmd, long oidPurchaseOrder,Hashtable<Long,PurchaseOrderItem> hashTemp,String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        
        switch (cmd) {
            case Command.ADD:
            break;
            case Command.SAVE:
                try {
                    purchaseOrder = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                } catch (Exception e) {
                }
                
                Enumeration en=hashTemp.keys();
                while (en.hasMoreElements()) {
                    long hashKey = (Long)en.nextElement();
                    poItem = (PurchaseOrderItem) hashTemp.get(hashKey);
                    if (poItem.getOID() == 0) {
                        if(purchaseOrder.getExchangeRate()!=1){
                            double exhangeRate=purchaseOrder.getExchangeRate();
                            poItem.setDiscNominal(exhangeRate*poItem.getDiscNominal());
                            poItem.setDiscount(exhangeRate*poItem.getDiscount());
                            poItem.setDiscount1(exhangeRate*poItem.getDiscount1());
                            poItem.setDiscount2(exhangeRate*poItem.getDiscount2());
                            poItem.setOrgBuyingPrice(exhangeRate*poItem.getOrgBuyingPrice());
                            poItem.setPrice(exhangeRate*poItem.getPrice());
                            poItem.setTotal(exhangeRate*poItem.getTotal());
                            poItem.setCurBuyingPrice(exhangeRate*poItem.getCurBuyingPrice());
                        }
                        try {
                            long oid = pstPoItem.insertExc(this.poItem);
                            
                            if(oid!=0){
                                long oidUserLast = PstLogSysHistory.getFirstUserId(oidPurchaseOrder);
                                if(oidUserLast != 0 && oidUserLast != userID )
                                {
                                    insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
                                }
                            }
                            
                            int versiSettingDiscount = Integer.valueOf(PstSystemProperty.getValueByName("VERSI_SETTING_DISCOUNT"));
                            boolean discountAvailable=pstPoItem.discountExist(purchaseOrder.getOID());
                            if(versiSettingDiscount==1 && discountAvailable){
                                //cari total yang tanpa bonus
                                String whereTotalWithoutBonus = pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'"+
                                                               " AND "+pstPoItem.fieldNames[pstPoItem.FLD_BONUS]+"='0'";

                                double totalWithoutBonus=pstPoItem.getTotalAmount(whereTotalWithoutBonus);
                                //cari total yang mendapatkan bonus
                                String whereTotalBonus= pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'"+
                                                               " AND "+pstPoItem.fieldNames[pstPoItem.FLD_BONUS]+"='1'";
                                double totalBonusOnly = pstPoItem.getTotalAmount(whereTotalBonus);

                                double totalBonus = totalWithoutBonus+totalBonusOnly;

                                //persentaseDisc=totalWithoutBonus/totalBonus
                                double persentaseDisc=0;
                                if(totalBonus!=0){
                                    persentaseDisc=totalWithoutBonus/totalBonus;
                                }

                                double rateNominal=0;
                                if(purchaseOrder.getExchangeRate()==1){
                                    rateNominal=1;      
                                }else{
                                    rateNominal=purchaseOrder.getExchangeRate();
                                }

                                //cari list semua item yang di terima
                                Vector vPurchaseItem = pstPoItem.list(0, 0, pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'", "");
                                if(vPurchaseItem.size()>0){
                                     for(int j=0; j<vPurchaseItem.size(); j++) {
                                         PurchaseOrderItem poitem = (PurchaseOrderItem) vPurchaseItem.get(j);
                                         double costGetdiscNominal=0;
                                         double discNominal=0;
                                         double discNominalPerQty=0;

                                         if(poitem.getCurBuyingPrice()!=0){
                                             //hitung persentase discount yang di dapat dan total nilai nya
                                             costGetdiscNominal=((poitem.getCurBuyingPrice() * poitem.getQuantity() * rateNominal)*persentaseDisc);

                                             //disc
                                             discNominal=(poitem.getCurBuyingPrice() * poitem.getQuantity() * rateNominal)-costGetdiscNominal;

                                             //disc per item
                                             discNominalPerQty=discNominal/poitem.getQuantity();

                                             poitem.setDiscNominal(discNominalPerQty);

                                             poitem.setCurBuyingPrice(poitem.getCurBuyingPrice()-discNominalPerQty);

                                             poitem.setTotal(poitem.getCurBuyingPrice()*poitem.getQuantity());

                                             // update datanya
                                             PstPurchaseOrderItem.updateExc(poitem);
                                         }
                                     }
                                }
                            }
                            
                        } catch (DBException dbexc) {
                            System.out.println(dbexc);
                            excCode = dbexc.getErrorCode();
                            msgString = getSystemMessage(excCode);
                            return getControlMsgId(excCode);
                        } catch (Exception exc) {
                            System.out.println(exc);
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                        }
                        
                    }else{
                        if(purchaseOrder.getExchangeRate()!=1){
                            double exhangeRate=purchaseOrder.getExchangeRate();
                            poItem.setDiscNominal(exhangeRate*poItem.getDiscNominal());
                            poItem.setDiscount(exhangeRate*poItem.getDiscount());
                            poItem.setDiscount1(exhangeRate*poItem.getDiscount1());
                            poItem.setDiscount2(exhangeRate*poItem.getDiscount2());
                            poItem.setOrgBuyingPrice(exhangeRate*poItem.getOrgBuyingPrice());
                            poItem.setPrice(exhangeRate*poItem.getPrice());
                            poItem.setTotal(exhangeRate*poItem.getTotal());
                            poItem.setCurBuyingPrice(exhangeRate*poItem.getCurBuyingPrice());
                        }    
                        try {
                            long oid = pstPoItem.updateExc(this.poItem);
                        
                            if(oid!=0){
                                int cmdHistory = Command.UPDATE;
                                insertHistory(userID, nameUser, cmdHistory, oidPurchaseOrder);
                            }
                            
                            int versiSettingDiscount = Integer.valueOf(PstSystemProperty.getValueByName("VERSI_SETTING_DISCOUNT"));
                            boolean discountAvailable=pstPoItem.discountExist(purchaseOrder.getOID());
                            
                            if(versiSettingDiscount==1 && discountAvailable){
                                //cari total yang tanpa bonus
                                String whereTotalWithoutBonus = pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'"+
                                                               " AND "+pstPoItem.fieldNames[pstPoItem.FLD_BONUS]+"='0'";

                                double totalWithoutBonus=pstPoItem.getTotalAmount(whereTotalWithoutBonus);
                                //cari total yang mendapatkan bonus
                                String whereTotalBonus= pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'"+
                                                               " AND "+pstPoItem.fieldNames[pstPoItem.FLD_BONUS]+"='1'";
                                double totalBonusOnly = pstPoItem.getTotalAmount(whereTotalBonus);

                                double totalBonus = totalWithoutBonus+totalBonusOnly;

                                //persentaseDisc=totalWithoutBonus/totalBonus
                                double persentaseDisc=0;
                                if(totalBonus!=0){
                                    persentaseDisc=totalWithoutBonus/totalBonus;
                                }

                                double rateNominal=0;
                                if(purchaseOrder.getExchangeRate()==1){
                                    rateNominal=1;      
                                }else{
                                    rateNominal=purchaseOrder.getExchangeRate();
                                }

                                //cari list semua item yang di terima
                                Vector vPurchaseItem = pstPoItem.list(0, 0, pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'", "");
                                if(vPurchaseItem.size()>0){
                                     for(int j=0; j<vPurchaseItem.size(); j++) {
                                         PurchaseOrderItem poitem = (PurchaseOrderItem) vPurchaseItem.get(j);
                                         double costGetdiscNominal=0;
                                         double discNominal=0;
                                         double discNominalPerQty=0;

                                         if(poitem.getCurBuyingPrice()!=0){

                                              //hitung persentase discount yang di dapat dan total nilai nya
                                             costGetdiscNominal=((poitem.getCurBuyingPrice() * poitem.getQuantity() * rateNominal)*persentaseDisc);

                                             //disc
                                             discNominal=(poitem.getCurBuyingPrice() * poitem.getQuantity() * rateNominal)-costGetdiscNominal;

                                             //disc per item
                                             discNominalPerQty=discNominal/poitem.getQuantity();

                                             poitem.setDiscNominal(discNominalPerQty);

                                             poitem.setCurBuyingPrice(poitem.getCurBuyingPrice()-discNominalPerQty);

                                             poitem.setTotal(poitem.getCurBuyingPrice()*poitem.getQuantity());

                                             // update datanya
                                             PstPurchaseOrderItem.updateExc(poitem);
                                         }
                                     }
                                }
                            }
                            
                        } catch (DBException dbexc) {
                            System.out.println(dbexc);
                            excCode = dbexc.getErrorCode();
                            msgString = getSystemMessage(excCode);
                        } catch (Exception exc) {
                            System.out.println(exc);
                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        }
                        
                    }
                }
                
            break;
        }
        
        return rsCode;
    }
    
    public int action(int cmd, long oidPoItem, long oidPurchaseOrder, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
                
            case Command.SAVE:
                if (oidPoItem != 0) {
                    try {
                        poItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
                    } catch (Exception exc) {
                    }

                    try {
                        prevPoItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
                    } catch (Exception exc) {
                    }
                }
                
                frmPoItem.requestEntityObject(poItem);
                poItem.setPurchaseOrderId(oidPurchaseOrder);
                
                // check if current material already exist in orderMaterial
                if (poItem.getOID() == 0 && PstPurchaseOrderItem.materialExist(poItem.getMaterialId(), oidPurchaseOrder)) {
                    msgString = getSystemMessage(RSLT_MATERIAL_EXIST);
                    return getControlMsgId(RSLT_MATERIAL_EXIST);
                }
                
                // check if current material already exist in orderMaterial
                if (poItem.getQuantity() == 0) {
                    msgString = getSystemMessage(RSLT_QTY_NULL);
                    return getControlMsgId(RSLT_QTY_NULL);
                }
                
                if (poItem.getPrice() == 0 && poItem.getCurBuyingPrice()== 0) {
                    msgString = getSystemMessage(RSLT_PRICE_NULL);
                    return getControlMsgId(RSLT_PRICE_NULL);
                }
                
                if (frmPoItem.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                if (poItem.getOID() == 0) {
                    
                    try {
                        
                        if(this!=null && oidPurchaseOrder!=0 )
                        {
                            purchaseOrder = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                        }
                        
                        if(purchaseOrder.getExchangeRate()!=1){
                            double exhangeRate=purchaseOrder.getExchangeRate();
                            poItem.setDiscNominal(exhangeRate*poItem.getDiscNominal());
                            poItem.setDiscount(exhangeRate*poItem.getDiscount());
                            poItem.setDiscount1(exhangeRate*poItem.getDiscount1());
                            poItem.setDiscount2(exhangeRate*poItem.getDiscount2());
                            poItem.setOrgBuyingPrice(exhangeRate*poItem.getOrgBuyingPrice());
                            poItem.setPrice(exhangeRate*poItem.getPrice());
                            poItem.setTotal(exhangeRate*poItem.getTotal());
                            poItem.setCurBuyingPrice(exhangeRate*poItem.getCurBuyingPrice());
                        }
                        
                        long oid = pstPoItem.insertExc(this.poItem);

                    
                        if(oid!=0)
                        {
                            long oidUserLast = PstLogSysHistory.getFirstUserId(oidPurchaseOrder);
                            if(oidUserLast != 0 && oidUserLast != userID )
                            {
                                insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
                            }
                        }
                        
                        
                        //update barang bonus
                        int versiSettingDiscount = Integer.valueOf(PstSystemProperty.getValueByName("VERSI_SETTING_DISCOUNT"));
                        boolean discountAvailable=pstPoItem.discountExist(purchaseOrder.getOID());
                        if(versiSettingDiscount==1 && discountAvailable){
                            //cari total yang tanpa bonus
                            String whereTotalWithoutBonus = pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'"+
                                                           " AND "+pstPoItem.fieldNames[pstPoItem.FLD_BONUS]+"='0'";

                            double totalWithoutBonus=pstPoItem.getTotalAmount(whereTotalWithoutBonus);
                            //cari total yang mendapatkan bonus
                            String whereTotalBonus= pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'"+
                                                           " AND "+pstPoItem.fieldNames[pstPoItem.FLD_BONUS]+"='1'";
                            double totalBonusOnly = pstPoItem.getTotalAmount(whereTotalBonus);

                            double totalBonus = totalWithoutBonus+totalBonusOnly;

                            //persentaseDisc=totalWithoutBonus/totalBonus
                            double persentaseDisc=0;
                            if(totalBonus!=0){
                                persentaseDisc=totalWithoutBonus/totalBonus;
                            }

                            double rateNominal=0;
                            if(purchaseOrder.getExchangeRate()==1){
                                rateNominal=1;      
                            }else{
                                rateNominal=purchaseOrder.getExchangeRate();
                            }
                            
                            //cari list semua item yang di terima
                            Vector vPurchaseItem = pstPoItem.list(0, 0, pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'", "");
                            if(vPurchaseItem.size()>0){
                                 for(int j=0; j<vPurchaseItem.size(); j++) {
                                     PurchaseOrderItem poitem = (PurchaseOrderItem) vPurchaseItem.get(j);
                                     double costGetdiscNominal=0;
                                     double discNominal=0;
                                     double discNominalPerQty=0;

                                     if(poitem.getCurBuyingPrice()!=0){
                                         //hitung persentase discount yang di dapat dan total nilai nya
                                         costGetdiscNominal=((poitem.getCurBuyingPrice() * poitem.getQuantity() * rateNominal)*persentaseDisc);

                                         //disc
                                         discNominal=(poitem.getCurBuyingPrice() * poitem.getQuantity() * rateNominal)-costGetdiscNominal;

                                         //disc per item
                                         discNominalPerQty=discNominal/poitem.getQuantity();

                                         poitem.setDiscNominal(discNominalPerQty);
                                         
                                         poitem.setCurBuyingPrice(poitem.getCurBuyingPrice()-discNominalPerQty);
                                         
                                         poitem.setTotal(poitem.getCurBuyingPrice()*poitem.getQuantity());
                                         
                                         // update datanya
                                         PstPurchaseOrderItem.updateExc(poitem);
                                     }
                                 }
                            }
                        }

                    } catch (DBException dbexc) {
                        System.out.println(dbexc);
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        System.out.println(exc);
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                } else {
                    try {
                         //SET NILAI CMD AGAR MENJADI UPDATE
                         try {
                            if(oidPurchaseOrder!=0 ) {
                                purchaseOrder = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                            }
                         } catch (Exception exc) {
                         }
                         
                        if(purchaseOrder.getExchangeRate()!=1){
                                double exhangeRate=purchaseOrder.getExchangeRate();
                                poItem.setDiscNominal(exhangeRate*poItem.getDiscNominal());
                                poItem.setDiscount(exhangeRate*poItem.getDiscount());
                                poItem.setDiscount1(exhangeRate*poItem.getDiscount1());
                                poItem.setDiscount2(exhangeRate*poItem.getDiscount2());
                                poItem.setOrgBuyingPrice(exhangeRate*poItem.getOrgBuyingPrice());
                                poItem.setPrice(exhangeRate*poItem.getPrice());
                                poItem.setTotal(exhangeRate*poItem.getTotal());
                                poItem.setCurBuyingPrice(exhangeRate*poItem.getCurBuyingPrice());
                        }    
                         
                        long oid = pstPoItem.updateExc(this.poItem);
                        
                        if(oid!=0)
                        {
                            int cmdHistory = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdHistory, oidPurchaseOrder);
                        }
                        
                        //update barang bonus
                        int versiSettingDiscount = Integer.valueOf(PstSystemProperty.getValueByName("VERSI_SETTING_DISCOUNT"));
                        boolean discountAvailable=pstPoItem.discountExist(purchaseOrder.getOID());
                        if(versiSettingDiscount==1 && discountAvailable){
                            //cari total yang tanpa bonus
                            String whereTotalWithoutBonus = pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'"+
                                                           " AND "+pstPoItem.fieldNames[pstPoItem.FLD_BONUS]+"='0'";

                            double totalWithoutBonus=pstPoItem.getTotalAmount(whereTotalWithoutBonus);
                            //cari total yang mendapatkan bonus
                            String whereTotalBonus= pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'"+
                                                           " AND "+pstPoItem.fieldNames[pstPoItem.FLD_BONUS]+"='1'";
                            double totalBonusOnly = pstPoItem.getTotalAmount(whereTotalBonus);

                            double totalBonus = totalWithoutBonus+totalBonusOnly;

                            //persentaseDisc=totalWithoutBonus/totalBonus
                            double persentaseDisc=0;
                            if(totalBonus!=0){
                                persentaseDisc=totalWithoutBonus/totalBonus;
                            }

                            double rateNominal=0;
                            if(purchaseOrder.getExchangeRate()==1){
                                rateNominal=1;      
                            }else{
                                rateNominal=purchaseOrder.getExchangeRate();
                            }
                            
                            //cari list semua item yang di terima
                            Vector vPurchaseItem = pstPoItem.list(0, 0, pstPoItem.fieldNames[pstPoItem.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrder.getOID()+"'", "");
                            if(vPurchaseItem.size()>0){
                                 for(int j=0; j<vPurchaseItem.size(); j++) {
                                     PurchaseOrderItem poitem = (PurchaseOrderItem) vPurchaseItem.get(j);
                                     double costGetdiscNominal=0;
                                     double discNominal=0;
                                     double discNominalPerQty=0;

                                     if(poitem.getCurBuyingPrice()!=0){
                                         
                                          //hitung persentase discount yang di dapat dan total nilai nya
                                         costGetdiscNominal=((poitem.getCurBuyingPrice() * poitem.getQuantity() * rateNominal)*persentaseDisc);

                                         //disc
                                         discNominal=(poitem.getCurBuyingPrice() * poitem.getQuantity() * rateNominal)-costGetdiscNominal;

                                         //disc per item
                                         discNominalPerQty=discNominal/poitem.getQuantity();

                                         poitem.setDiscNominal(discNominalPerQty);
                                         
                                         poitem.setCurBuyingPrice(poitem.getCurBuyingPrice()-discNominalPerQty);
                                         
                                         poitem.setTotal(poitem.getCurBuyingPrice()*poitem.getQuantity());
                                         
                                         // update datanya
                                         PstPurchaseOrderItem.updateExc(poitem);
                                     }
                                 }
                            }
                        }
                        
                    } catch (DBException dbexc) {
                        System.out.println(dbexc);
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        System.out.println(exc);
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }
                
                
                
                
                
                
                break;
                
            case Command.EDIT:
                if (oidPoItem != 0) {
                    try {
                        poItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK:
                if (oidPoItem != 0) {
                    try {
                        poItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
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
                        purchaseOrder = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                        poItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
                        prevPoItem = null;
                    } catch (Exception exc) {
                    }
                //================================================================
                if (oidPoItem != 0) {
                    try {
                        //long oid = PstPurchaseOrderItem.deleteExc(oidPoItem);
                        long oid = pstPoItem.deleteExc(this.poItem);
                         //long oid = pstPoItem.updateExc(this.poItem);
                        if (oid != 0) {
                            //SET NILAI CMD AGAR MENJADI UPDATE
                            //cmd = 6;
                            long oidUserLast = PstLogSysHistory.getFirstUserId(oidPurchaseOrder);
                            //cek siapa yang update? di log jika yang update  berbeda dengan user yang mengcreate pertama
//                            if(oidUserLast != 0 && oidUserLast != userID )
//                            {
//                                insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
//                            }
                            insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
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
                
            default :
                
        }
        return rsCode;
    }

    public  void insertHistory(long userID, String nameUser, int getCmd, long oidPurchaseOrder)
    {
       try
       {
            LogSysHistory logSysHistory = new LogSysHistory();
            //logSysHistory.setLogUserId(poItem.getMaterialId());
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("purchasing/material/pom/pomaterialitem.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogUserAction(Command.commandString[getCmd]);
            logSysHistory.setLogDocumentNumber(purchaseOrder.getPoCode());
            logSysHistory.setLogDocumentId(oidPurchaseOrder);

            logSysHistory.setLogDetail(this.poItem.getLogDetail(prevPoItem));

            if(!logSysHistory.getLogDetail().equals("") || getCmd==Command.DELETE)
            {
                 long oid2 = PstLogSysHistory.insertLog(logSysHistory);
}
            }
       catch(Exception e)
       {
       }
    }
   
    public int actionSaveAll(HttpServletRequest request, Vector list,long oidPurchaseOrderID, long curencyId, long categoryID, long locationId){
        int aksi=1;
            for(int i=0; i<list.size(); i++){
                double qtyInput =FRMQueryString.requestDouble(request, FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]+"_"+i);    
                long materialId= FRMQueryString.requestLong(request, FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_MATERIAL_ID]+"_"+i);
                //store request
                if(qtyInput!=0 && materialId!=0){
                    PurchaseOrderItem matItem = new PurchaseOrderItem();
                    
                    matItem.setPurchaseOrderId(oidPurchaseOrderID);
                    
                    matItem.setMaterialId(materialId);
                    matItem.setCurrencyId(curencyId);
                    matItem.setUnitId(FRMQueryString.requestLong(request, FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]+"_"+i));
                    matItem.setUnitRequestId(FRMQueryString.requestLong(request, FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]+"_"+i));
                    matItem.setBonus(0);
                    matItem.setCurBuyingPrice(FRMQueryString.requestDouble(request, FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_ORG_BUYING_PRICE]+"_"+i));
                    matItem.setQtyRequest(FRMQueryString.requestDouble(request, FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]+"_"+i));
                    matItem.setPrice(FRMQueryString.requestDouble(request, FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE]+"_"+i));
                    matItem.setQtyInputStock(FRMQueryString.requestDouble(request, FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY_STOCK]+"_"+i));
                    matItem.setQuantity(FRMQueryString.requestLong(request, FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]+"_"+i));
                    double total=0;
                    total = matItem.getQuantity()*matItem.getPrice();
                    matItem.setTotal(total);
                    
                    try {
                        long oid = PstPurchaseOrderItem.insertExc(matItem);
                        
                    } catch (DBException ex) {
                    }
                }
                
            }
            
            
            
        return aksi;
    }
    
    
    
     public int actionSR(int cmd, long oidPoItem, long oidPurchaseOrder, String nameUser, long userID) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
                
            case Command.SAVE:
                if (oidPoItem != 0) {
                    try {
                        poItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
                    } catch (Exception exc) {
                    }

                    try {
                        prevPoItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
                    } catch (Exception exc) {
                    }
                }
                
                frmPoItem.requestEntityObjectSR(poItem);
                poItem.setPurchaseOrderId(oidPurchaseOrder);
                
                // check if current material already exist in orderMaterial
                if (poItem.getOID() == 0 && PstPurchaseOrderItem.materialExist(poItem.getMaterialId(), oidPurchaseOrder)) {
                    msgString = getSystemMessage(RSLT_MATERIAL_EXIST);
                    return getControlMsgId(RSLT_MATERIAL_EXIST);
                }
                
//                if (poItem.getQtyInputStock() == 0) {
//                    msgString = getSystemMessage(RSLT_QTY_STOCK_NULL);
//                    return getControlMsgId(RSLT_QTY_STOCK_NULL);
//                }
                
                // check if current material already exist in orderMaterial
                if (poItem.getMaterialId()==0){
                    msgString = getSystemMessage(RSLT_NO_ITEM);
                    return getControlMsgId(RSLT_NO_ITEM);
                }else {
                    if (poItem.getQuantity() == 0) {
                        msgString = getSystemMessage(RSLT_QTY_NULL);
                        return getControlMsgId(RSLT_QTY_NULL);
                    }
                }
               
                
                if (frmPoItem.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                
                if (poItem.getOID() == 0) {
                    
                    try {
                        
                        if(this!=null && oidPurchaseOrder!=0 )
                        {
                            purchaseOrder = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                        }
                        
                        if(purchaseOrder.getExchangeRate()!=1){
                            double exhangeRate=purchaseOrder.getExchangeRate();
                            poItem.setDiscNominal(exhangeRate*poItem.getDiscNominal());
                            poItem.setDiscount(exhangeRate*poItem.getDiscount());
                            poItem.setDiscount1(exhangeRate*poItem.getDiscount1());
                            poItem.setDiscount2(exhangeRate*poItem.getDiscount2());
                            poItem.setOrgBuyingPrice(exhangeRate*poItem.getOrgBuyingPrice());
                            poItem.setPrice(exhangeRate*poItem.getPrice());
                            poItem.setTotal(exhangeRate*poItem.getTotal());
                            poItem.setCurBuyingPrice(exhangeRate*poItem.getCurBuyingPrice());
                        }
                        
                        long oid = pstPoItem.insertExc(this.poItem);

                    
                        if(oid!=0)
                        {
                            long oidUserLast = PstLogSysHistory.getFirstUserId(oidPurchaseOrder);
                            if(oidUserLast != 0 && oidUserLast != userID )
                            {
                                insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
                            }
                        }


                    } catch (DBException dbexc) {
                        System.out.println(dbexc);
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        System.out.println(exc);
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                } else {
                    try {
                         //SET NILAI CMD AGAR MENJADI UPDATE
                         try {
                            if(oidPurchaseOrder!=0 ) {
                                purchaseOrder = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                            }
                         } catch (Exception exc) {
                         }
                         
                         if(purchaseOrder.getExchangeRate()!=1){
                                double exhangeRate=purchaseOrder.getExchangeRate();
                                poItem.setDiscNominal(exhangeRate*poItem.getDiscNominal());
                                poItem.setDiscount(exhangeRate*poItem.getDiscount());
                                poItem.setDiscount1(exhangeRate*poItem.getDiscount1());
                                poItem.setDiscount2(exhangeRate*poItem.getDiscount2());
                                poItem.setOrgBuyingPrice(exhangeRate*poItem.getOrgBuyingPrice());
                                poItem.setPrice(exhangeRate*poItem.getPrice());
                                poItem.setTotal(exhangeRate*poItem.getTotal());
                                poItem.setCurBuyingPrice(exhangeRate*poItem.getCurBuyingPrice());
                        }    
                         
                        long oid = pstPoItem.updateExc(this.poItem);
                        
                        if(oid!=0)
                        {
                            int cmdHistory = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdHistory, oidPurchaseOrder);
                        }
                    } catch (DBException dbexc) {
                        System.out.println(dbexc);
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        System.out.println(exc);
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }
                break;
                
            case Command.EDIT:
                if (oidPoItem != 0) {
                    try {
                        poItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK:
                if (oidPoItem != 0) {
                    try {
                        poItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
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
                        purchaseOrder = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
                        poItem = PstPurchaseOrderItem.fetchExc(oidPoItem);
                        prevPoItem = null;
                    } catch (Exception exc) {
                    }
                //================================================================
                if (oidPoItem != 0) {
                    try {
                        //long oid = PstPurchaseOrderItem.deleteExc(oidPoItem);
                        long oid = pstPoItem.deleteExc(this.poItem);
                         //long oid = pstPoItem.updateExc(this.poItem);
                        if (oid != 0) {
                            //SET NILAI CMD AGAR MENJADI UPDATE
                            //cmd = 6;
                            long oidUserLast = PstLogSysHistory.getFirstUserId(oidPurchaseOrder);
                            //cek siapa yang update? di log jika yang update  berbeda dengan user yang mengcreate pertama
//                            if(oidUserLast != 0 && oidUserLast != userID )
//                            {
//                                insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
//                            }
                            insertHistory(userID, nameUser, cmd, oidPurchaseOrder);
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
                
            default :
                
        }
        return rsCode;
    }
    
    
}


