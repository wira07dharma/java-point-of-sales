/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.dimata.posbo.form.warehouse;

import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import java.util.*;
import javax.servlet.http.*;

import com.dimata.posbo.entity.warehouse.*;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.posbo.db.DBException;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.posbo.db.OIDFactory;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialComposit;
import com.dimata.posbo.entity.masterdata.MaterialDetail;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialComposit;
import com.dimata.posbo.entity.masterdata.PstMaterialDetail;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.session.warehouse.SessMatDispatchReceive;



import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.form.FRMQueryString;
import javax.servlet.http.HttpServletRequest;

public class CtrlMatDispatchReceiveItem extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static final int COMMAND_TYPE_GROUP = 0;
    public static final int COMMAND_TYPE_ITEM = 1;

    public static String[][] resultText =
            {
                {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private MatDispatchReceiveItem matDispatchReceiveItem;
    private PstMatDispatchReceiveItem pstMatDispatchReceiveItem;
    private FrmMatDispatchReceiveItem frmMatDispatchReceiveItem;
    private MatDispatch matDispatch;
    
    private MatDispatchItem prevMatDispatchItem;
    private MatReceiveItem prevMatReceiveItem;
    private MatDispatchReceiveItem prevMatDispatchReceiveItem;
    
    private HttpServletRequest req;
    private OIDFactory oidFactory = new OIDFactory();
    int language = LANGUAGE_DEFAULT;
    private Date dateLog = new Date();
    long oid = 0;

    public CtrlMatDispatchReceiveItem() {
    }

    public CtrlMatDispatchReceiveItem(HttpServletRequest request) {
        msgString = "";
        matDispatchReceiveItem = new MatDispatchReceiveItem();
        try {
            pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(0);
        } catch (Exception e) {
          ;
        }
        req = request;
        frmMatDispatchReceiveItem = new FrmMatDispatchReceiveItem(request, matDispatchReceiveItem);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatDispatchReceiveItem.addError(frmMatDispatchReceiveItem.FRM_FIELD_DF_REC_ITEM_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MatDispatchReceiveItem getMatDispatchReceiveItem() {
        return matDispatchReceiveItem;
    }

    public FrmMatDispatchReceiveItem getForm() {
        return frmMatDispatchReceiveItem;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public long getOidTransfer() {
        return oid;
    }
    
    public int action(int cmd, long oidMatDispatchReceiveItem, long oidDfRecGroup, long oidMatDispatch, long oidDfRecGroup1, int commandType) {
        return action(cmd,oidMatDispatchReceiveItem,oidDfRecGroup,oidMatDispatch,oidDfRecGroup1,commandType,"",0);
    }
    
    public int action(int cmd, long oidMatDispatchReceiveItem, long oidDfRecGroup, long oidMatDispatch, long oidDfRecGroup1, int commandType, String nameUser, long oidUser) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                    
                     try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                     
                     //cek material receive dan dispatch
                    
                    
                }
                
                
                
                frmMatDispatchReceiveItem.requestEntityObject(matDispatchReceiveItem);
                //Date date = ControlDate.getDateTime(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE], req);
                //matCosting.setCostingDate(date);
                if (oidMatDispatchReceiveItem != 0) {
                    try 
                    {
                        prevMatDispatchItem = PstMatDispatchItem.fetchExc(matDispatchReceiveItem.getSourceItem().getOID()) ;//matDispatchReceiveItem.getSourceItem();
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                    try {
                         prevMatReceiveItem  = PstMatReceiveItem.fetchExc(matDispatchReceiveItem.getTargetItem().getOID());//matDispatchReceiveItem.getTargetItem();
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                }
                
                if (oidMatDispatchReceiveItem == 0) {
                    try {
                        //if(matDispatchReceiveItem.getDfRecGroupId()== 0){
                           // matDispatchReceiveItem.setDfRecGroupId(oidFactory.generateOID());
                       // }
                        //else{
                            //matDispatchReceiveItem.getDfRecGroupId();
                       // }
                         if(oidDfRecGroup== 0 && commandType == COMMAND_TYPE_GROUP){
                             matDispatchReceiveItem.setDfRecGroupId(oidFactory.generateOID());
                         }
                         else{
                             matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup);
                        }
                         if(commandType == COMMAND_TYPE_ITEM && oidDfRecGroup1!=0){
                            matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup1);
                         }
                         
                        //SessMatCosting sessCosting = new SessMatCosting();
                        //int maxCounter = sessCosting.getMaxCostingCounter(matCosting.getCostingDate(), matCosting);
                        //maxCounter = maxCounter + 1;
                        //matCosting.setCostingCodeCounter(maxCounter);
                        //matCosting.setCostingCode(sessCosting.generateCostingCode(matCosting));
                        //matCosting.setLastUpdate(new Date());   
                        this.oid = pstMatDispatchReceiveItem.insertExc(this.matDispatchReceiveItem);

                        /*
                         * For Update cost in Target
                         */
                       // long oidDfRecGroupCost = matDispatchReceiveItem.getDfRecGroupId();
                        //double qtyTarget = matDispatchReceiveItem.getTargetItem().getQty();
                        //double totalSource =PstMatDispatchItem.getTotalHppSourceTransfer(oidDfRecGroupCost);

                        
                        //double summaryExistingStockValue = SessMatDispatchReceive.getTotalHppExistingTarget(oidDfRecGroupCost);

                        //Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget(totalSource, oidDfRecGroupCost);
                        //Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget2(totalSource, oidDfRecGroupCost,summaryExistingStockValue );
                        //end of update HppTarget


                        //double qtyTarget = 0;
                        //double averagePrice = 0;
                        //double costTarget = 0;
                       // double costTargetNew = 0;
                        //double costTotal = 0;
                        //long oidDfRecItem = 0;

                      /* Vector vctGetQty = new Vector(1,1);
                        //vctGetQty = PstMatDispatchReceiveItem.getStockValueCurrentTarget(0,0,oidDfRecGroupCost);
                          vctGetQty = SessMatDispatchReceive.getStockValueCurrentTarget(0,0,oidDfRecGroupCost);
                        if( vctGetQty!=null &&  vctGetQty.size() > 0) {
                            for(int k=0;k<vctGetQty.size();k++){
                                MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)vctGetQty.get(k);
                                qtyTarget = dfRecItem.getTargetItem().getQty();
                                averagePrice = dfRecItem.getTargetItem().getMaterialTarget().getAveragePrice();
                                oidDfRecItem = dfRecItem.getTargetItem().getOID();
                                costTarget = totalSource / qtyTarget ;
                                dfRecItem.getTargetItem().setCost(costTarget);
                                costTotal = costTarget * qtyTarget;
                                dfRecItem.getTargetItem().setTotal(costTotal);
                                
                                PstMatDispatchReceiveItem.updateExc(dfRecItem);

                            }
                        }*/







                        //matDispatchReceiveItem.getTargetItem().setCost(total);
                       // System.out.println(totalSource);


                        //End of update cost Target

                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        //PstDocLogger.insertDataBo_toDocLogger(matCosting.getCostingCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matCosting.getLastUpdate(), matCosting.getRemark());
                        //--- end

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        //matCosting.setLastUpdate(new Date());
                             matDispatchReceiveItem.setOID(oidMatDispatchReceiveItem);
                             matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup1);
                        

                         this.oid = pstMatDispatchReceiveItem.updateExc(this.matDispatchReceiveItem);
                         
//                         //edit opie-eyek 20140410 untuk history tranfer unit
//                         prevMatDispatchItem = PstMatDispatchItem.fetchExc(matDispatchReceiveItem.getSourceItem().getOID()) ;//matDispatchReceiveItem.getSourceItem();
//                         prevMatReceiveItem  = PstMatReceiveItem.fetchExc(matDispatchReceiveItem.getSourceItem().getOID());//matDispatchReceiveItem.getTargetItem();
//                         
                         //PstMatReceiveItem.insertExc(matDispatchReceiveItem.getTargetItem());
                         //log history update opie-eyek 20140410
                         try{
                             insertHistory(oidUser, nameUser, cmd, oidMatDispatch);
                         }catch(Exception es){
                         }
                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        //PstDocLogger.updateDataBo_toDocLogger(matCosting.getCostingCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matCosting.getLastUpdate(), matCosting.getRemark());
                        //--- end

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                 /*
                         * For Update cost in Target
                         */
                       long oidDfRecGroupCost = matDispatchReceiveItem.getDfRecGroupId();
                        //double qtyTarget = matDispatchReceiveItem.getTargetItem().getQty();
                        double totalSource =PstMatDispatchItem.getTotalHppSourceTransfer(oidDfRecGroupCost);


                        double summaryExistingStockValue = SessMatDispatchReceive.getTotalHppExistingTarget(oidDfRecGroupCost);

                        //Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget(totalSource, oidDfRecGroupCost);
                        Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget2(totalSource, oidDfRecGroupCost,summaryExistingStockValue );
                        //end of update HppTarget

                break;

            case Command.EDIT:
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE :
                 //Untuk mendapatkan df_rec_item_id dan delete group
                if (oidMatDispatchReceiveItem == 0) {
                   Vector vect = PstMatDispatchReceiveItem.checkOID(oidDfRecGroup1);
                    if (vect != null && vect.size() > 0) {
                       for(int i=0; i<vect.size(); i++){
                        matDispatchReceiveItem = (MatDispatchReceiveItem) vect.get(i);
                        long oidMatDispatchReceiveItemGroup = matDispatchReceiveItem.getOID();
                        
                         try{
                            long oid = PstMatDispatchReceiveItem.deleteExc(oidMatDispatchReceiveItemGroup);
                            
                            if(oid!=0){
                                    //log history update opie-eyek 20140410
                                    try{
                                         insertHistory(oidUser, nameUser, cmd, oidMatDispatch);
                                    }catch(Exception es){
                                    }
                                     
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
                            }else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                                }
                            
                            }catch(DBException dbexc){
				excCode = dbexc.getErrorCode();
				msgString = getSystemMessage(excCode);
                            }catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }
                    }
                   else {
                       oidMatDispatchReceiveItem = 0;
                   }
                }
                   //end of Delete Group

                else if(oidMatDispatchReceiveItem != 0) {
                     //Untuk mendaptkan oidReceiveMaterialItem
                     //String whereClause = PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatchReceiveItem;
                       // Vector vect = PstMatDispatchReceiveItem.list(0, 0, whereClause, "");
                      //  if (vect != null && vect.size() > 0) {
                           // for (int l = 0; l < vect.size(); l++) {
                                //MatDispatchReceiveItem matDispatchReceiveItem = (MatDispatchReceiveItem) vect.get(l);
                               // CtrlMatDispatchReceiveItem ctrlMatDpsRecItm = new CtrlMatDispatchReceiveItem();
                               // ctrlMatDpsRecItm.action(Command.DELETE, matDispatchReceiveItem.getOID(), oidMatDispatchReceiveItem);
                           // }
                       // }

                    try{


                        long oid = PstMatDispatchReceiveItem.deleteExc(oidMatDispatchReceiveItem);
			if(oid!=0){
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
			}else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                               }
                        }catch(DBException dbexc){
				excCode = dbexc.getErrorCode();
				msgString = getSystemMessage(excCode);
			}catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
			}
		}
                //for delete Group
                /*if (oidDfRecGroup1 != 0) {
                    try{
                        long oidGroup = PstMatDispatchReceiveItem.deleteGroup(oidDfRecGroup1);
			if(oidGroup!=0){
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
			}else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                              }
                        //}catch(DBException dbexc){
				//excCode = dbexc.getErrorCode();
				//msgString = getSystemMessage(excCode);
			}catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
			}

              }*/
                //end of delete Group
                break;
        }
        return rsCode;
    }
    
    public int actionProduksi(int cmd, long oidMatDispatchReceiveItem, long oidDfRecGroup, long oidMatDispatch, long oidDfRecGroup1, int commandType, String nameUser, long oidUser) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExcProduksi(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                    
                     try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                     
                     //cek material receive dan dispatch
                    
                    
                }
                
                
                
                frmMatDispatchReceiveItem.requestEntityObject(matDispatchReceiveItem);
                //Date date = ControlDate.getDateTime(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE], req);
                //matCosting.setCostingDate(date);
                //for?
                if (oidMatDispatchReceiveItem != 0) {
                    try 
                    {
                        prevMatDispatchItem = PstMatDispatchItem.fetchExc(matDispatchReceiveItem.getSourceItem().getOID()) ;//matDispatchReceiveItem.getSourceItem();
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                    try {
                         prevMatReceiveItem  = PstMatReceiveItem.fetchExc(matDispatchReceiveItem.getTargetItem().getOID());//matDispatchReceiveItem.getTargetItem();
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                }
                
                if (oidMatDispatchReceiveItem == 0) {
                    try {
                        //if(matDispatchReceiveItem.getDfRecGroupId()== 0){
                           // matDispatchReceiveItem.setDfRecGroupId(oidFactory.generateOID());
                       // }
                        //else{
                            //matDispatchReceiveItem.getDfRecGroupId();
                       // }
                         if(oidDfRecGroup== 0 && commandType == COMMAND_TYPE_GROUP){
                             matDispatchReceiveItem.setDfRecGroupId(oidFactory.generateOID());
                         }
                         else{
                             matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup);
                        }
                         if(commandType == COMMAND_TYPE_ITEM && oidDfRecGroup1!=0){
                            matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup1);
                         }
                         
                        //SessMatCosting sessCosting = new SessMatCosting();
                        //int maxCounter = sessCosting.getMaxCostingCounter(matCosting.getCostingDate(), matCosting);
                        //maxCounter = maxCounter + 1;
                        //matCosting.setCostingCodeCounter(maxCounter);
                        //matCosting.setCostingCode(sessCosting.generateCostingCode(matCosting));
                        //matCosting.setLastUpdate(new Date()); 
                        
                        
                        //EDDY PRODUKSI
                        Vector listComposit = new Vector();
                        MaterialComposit materialComposit = new MaterialComposit();
                        listComposit = PstMaterialComposit.listProduksi(0, 0, PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]+" = "
                                + matDispatchReceiveItem.getTargetItem().getMaterialId(), null);
                        
                        Vector listMat = new Vector();
                        Material m = new Material();
                        double totalTarget = 0;
                        
                        Vector listStock1 = new Vector();
                        MaterialStock ms1 = new MaterialStock();
                        MatDispatch md = new MatDispatch();
                        Vector listMD = new Vector();
                        listMD = PstMatDispatch.list(0, 0, PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]+" = "
                                + oidMatDispatch, null);
                        md = (MatDispatch) listMD.get(0);
                        
                        PstMatReceiveItem.insertExc(matDispatchReceiveItem.getTargetItem());
                        for (int i = 0; i < listComposit.size(); i++) {
                            materialComposit = (MaterialComposit) listComposit.get(i);
                            listMat = PstMaterial.list(0, 0, PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" = "
                                    + materialComposit.getMaterialComposerId(), null);
                            m = (Material) listMat.get(0);
                            
                            matDispatchReceiveItem.getSourceItem().setDispatchMaterialId(matDispatchReceiveItem.getDispatchMaterialId());
                            matDispatchReceiveItem.getSourceItem().setMaterialId(materialComposit.getMaterialComposerId());
                            matDispatchReceiveItem.getSourceItem().setUnitId(materialComposit.getUnitId());
                            matDispatchReceiveItem.getSourceItem().setHpp(m.getAveragePrice());
                            matDispatchReceiveItem.getSourceItem().setQty(materialComposit.getQty() * matDispatchReceiveItem.getTargetItem().getQty());
                            matDispatchReceiveItem.getSourceItem().setHppTotal(m.getAveragePrice() * matDispatchReceiveItem.getSourceItem().getQty());
                            totalTarget += matDispatchReceiveItem.getSourceItem().getHppTotal();
                           this.oid = pstMatDispatchReceiveItem.insertExcProduksi(this.matDispatchReceiveItem); 
                           
                           //update stock
//                           listStock1 = PstMaterialStock.list(0, 0, PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+" = "
//                                + matDispatchReceiveItem.getSourceItem().getMaterialId()+" AND "
//                                + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+" = " +md.getLocationId(), null);
//                            ms1 = (MaterialStock) listStock1.get(0);
//                            double prevStock1 = 0;
//                            prevStock1 = ms1.getQty();
//                            ms1.setQty(prevStock1 - matDispatchReceiveItem.getSourceItem().getQty());
//                            PstMaterialStock.updateExc(ms1);
                        
                        }
                        matDispatchReceiveItem.getTargetItem().setTotal(totalTarget);
                        PstMatReceiveItem.updateExc(matDispatchReceiveItem.getTargetItem());
                        
                        //UPDATE STOCK
                        //PLUS
//                        Vector listStock = new Vector();
//                        MaterialStock ms = new MaterialStock();
//                        
//                        listStock = PstMaterialStock.list(0, 0, PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+" = "
//                                + matDispatchReceiveItem.getTargetItem().getMaterialId()+" AND "
//                                + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+" = " +md.getDispatchTo(), null);
//                        ms = (MaterialStock) listStock.get(0);
//                        
//                        double prevStock = 0;
//                        prevStock = ms.getQty();
//                        ms.setQty(prevStock + matDispatchReceiveItem.getTargetItem().getQty());
//                        PstMaterialStock.updateExc(ms);

                        
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        //matCosting.setLastUpdate(new Date());
                             //matDispatchReceiveItem.setOID(oidMatDispatchReceiveItem);
                             matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup1);
                        
                        //UPDATE PRODUKSI ED
                         PstMatReceiveItem.updateExc(matDispatchReceiveItem.getTargetItem());
                         
                        Vector listSource = new Vector();
                        Vector listUpdate = new Vector();
                        Vector listTransfer = new Vector();
                        Vector listTransferValue = new Vector();
//                        MatDispatchReceiveItem mdriOID = new MatDispatchReceiveItem();
//                        listTransfer = PstMatDispatchReceiveItem.list(0, 0, PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+" = "
//                                + matDispatchReceiveItem.getTargetItem().getOID(), null);
//                        
                        MaterialComposit materialComposit = new MaterialComposit();
                        listSource = PstMaterialComposit.listProduksi(0, 0, PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]+" = "
                                + matDispatchReceiveItem.getTargetItem().getMaterialId(), null);
                          
                            listTransfer = PstMatDispatchItem.list(0, 0, PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]+" = "
                                    + matDispatch.getOID(), null);
                        
                        for (int i = 0; i < listSource.size(); i++) {
                            materialComposit = (MaterialComposit) listSource.get(i);
                            MatDispatchItem mdi = (MatDispatchItem) listTransfer.get(i);
                            matDispatchReceiveItem.getSourceItem().setOID(mdi.getOID());
                            matDispatchReceiveItem.getSourceItem().setDispatchMaterialId(matDispatchReceiveItem.getDispatchMaterialId());
                            matDispatchReceiveItem.getSourceItem().setMaterialId(materialComposit.getMaterialComposerId());
                            matDispatchReceiveItem.getSourceItem().setUnitId(materialComposit.getUnitId());
                            matDispatchReceiveItem.getSourceItem().setQty(materialComposit.getQty() * matDispatchReceiveItem.getTargetItem().getQty());
                            
                            PstMatDispatchItem.updateExc(matDispatchReceiveItem.getSourceItem());
                        }
                         //log history update opie-eyek 20140410
                         try{
                             insertHistory(oidUser, nameUser, cmd, oidMatDispatch);
                         }catch(Exception es){
                         }
                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        //PstDocLogger.updateDataBo_toDocLogger(matCosting.getCostingCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matCosting.getLastUpdate(), matCosting.getRemark());
                        //--- end

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                 /*
                         * For Update cost in Target
                         */
                       long oidDfRecGroupCost = matDispatchReceiveItem.getDfRecGroupId();
                        //double qtyTarget = matDispatchReceiveItem.getTargetItem().getQty();
                        double totalSource =PstMatDispatchItem.getTotalHppSourceTransfer(oidDfRecGroupCost);


                        double summaryExistingStockValue = SessMatDispatchReceive.getTotalHppExistingTarget(oidDfRecGroupCost);

                        //Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget(totalSource, oidDfRecGroupCost);
                        Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget2(totalSource, oidDfRecGroupCost,summaryExistingStockValue );
                        //end of update HppTarget

                break;

            case Command.EDIT:
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExcProduksi(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE :
                 //Untuk mendapatkan df_rec_item_id dan delete group
                if (oidMatDispatchReceiveItem == 0) {
                   Vector vect = PstMatDispatchReceiveItem.checkOID(oidDfRecGroup1);
                    if (vect != null && vect.size() > 0) {
                       for(int i=0; i<vect.size(); i++){
                        matDispatchReceiveItem = (MatDispatchReceiveItem) vect.get(i);
                        long oidMatDispatchReceiveItemGroup = matDispatchReceiveItem.getOID();
                        
                         try{
                            long oid = PstMatDispatchReceiveItem.deleteExc(oidMatDispatchReceiveItemGroup);
                            
                            if(oid!=0){
                                    //log history update opie-eyek 20140410
                                    try{
                                         insertHistory(oidUser, nameUser, cmd, oidMatDispatch);
                                    }catch(Exception es){
                                    }
                                     
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
                            }else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                                }
                            
                            }catch(DBException dbexc){
				excCode = dbexc.getErrorCode();
				msgString = getSystemMessage(excCode);
                            }catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }
                    }
                   else {
                       oidMatDispatchReceiveItem = 0;
                   }
                }
                   //end of Delete Group

                else if(oidMatDispatchReceiveItem != 0) {
                     //Untuk mendaptkan oidReceiveMaterialItem
                     //String whereClause = PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatchReceiveItem;
                       // Vector vect = PstMatDispatchReceiveItem.list(0, 0, whereClause, "");
                      //  if (vect != null && vect.size() > 0) {
                           // for (int l = 0; l < vect.size(); l++) {
                                //MatDispatchReceiveItem matDispatchReceiveItem = (MatDispatchReceiveItem) vect.get(l);
                               // CtrlMatDispatchReceiveItem ctrlMatDpsRecItm = new CtrlMatDispatchReceiveItem();
                               // ctrlMatDpsRecItm.action(Command.DELETE, matDispatchReceiveItem.getOID(), oidMatDispatchReceiveItem);
                           // }
                       // }

                    try{


                        long oid = PstMatDispatchReceiveItem.deleteExc(oidMatDispatchReceiveItem);
			if(oid!=0){
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
			}else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                               }
                        }catch(DBException dbexc){
				excCode = dbexc.getErrorCode();
				msgString = getSystemMessage(excCode);
			}catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
			}
		}
                //for delete Group
                /*if (oidDfRecGroup1 != 0) {
                    try{
                        long oidGroup = PstMatDispatchReceiveItem.deleteGroup(oidDfRecGroup1);
			if(oidGroup!=0){
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
			}else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                              }
                        //}catch(DBException dbexc){
				//excCode = dbexc.getErrorCode();
				//msgString = getSystemMessage(excCode);
			}catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
			}

              }*/
                //end of delete Group
                break;
        }
        return rsCode;
    }

    public int actionLebur(int cmd, long oidMatDispatchReceiveItem, long oidDfRecGroup, long oidMatDispatch, long oidDfRecGroup1, int commandType, String nameUser, long oidUser) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:

                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }

                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                }

                frmMatDispatchReceiveItem.requestEntityObject(matDispatchReceiveItem);
                MatDispatchItem matDispatchItem = matDispatchReceiveItem.getSourceItem();
                double berat = FRMQueryString.requestDouble(req, FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_BERAT_CURRENT]);
                double ongkos = FRMQueryString.requestDouble(req, FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_ONGKOS]);
                matDispatchItem.setBeratCurrent(berat);
                matDispatchItem.setOngkos(ongkos);
                MatReceiveItem matReceiveItem = matDispatchReceiveItem.getTargetItem();
                double beratBaru = FRMQueryString.requestDouble(req, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BERAT]);
                double ongkosBaru = FRMQueryString.requestDouble(req, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]);
                String keterangan = FRMQueryString.requestString(req, FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_REMARK]);
                matReceiveItem.setBerat(beratBaru);
                matReceiveItem.setForwarderCost(ongkosBaru);
                matReceiveItem.setRemark(keterangan);
                
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        prevMatDispatchItem = PstMatDispatchItem.fetchExc(matDispatchReceiveItem.getSourceItem().getOID());//matDispatchReceiveItem.getSourceItem();
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                    try {
                        prevMatReceiveItem = PstMatReceiveItem.fetchExc(matDispatchReceiveItem.getTargetItem().getOID());//matDispatchReceiveItem.getTargetItem();
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                }

                if (oidMatDispatchReceiveItem == 0) {
                    try {
                        
                        if (oidDfRecGroup == 0 && commandType == COMMAND_TYPE_GROUP) {
                            matDispatchReceiveItem.setDfRecGroupId(oidFactory.generateOID());
                        } else {
                            matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup);
                        }
                        if (commandType == COMMAND_TYPE_ITEM && oidDfRecGroup1 != 0) {
                            matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup1);
                        }

                        this.oid = pstMatDispatchReceiveItem.insertExc(this.matDispatchReceiveItem);
                        
                        Material m = PstMaterial.fetchExc(matDispatchReceiveItem.getTargetItem().getMaterialId());
                        m.setAveragePrice(matDispatchReceiveItem.getTargetItem().getCost());
                        PstMaterial.updateExc(m);
                        Vector<MaterialDetail> listMatDet = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = '" + matDispatchReceiveItem.getTargetItem().getMaterialId() + "'", "");
                        if (!listMatDet.isEmpty()) {
                            MaterialDetail md = PstMaterialDetail.fetchExc(listMatDet.get(0).getOID());
                            md.setOngkos(ongkosBaru);
                            PstMaterialDetail.updateExc(md);
                        }

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {

                        matDispatchReceiveItem.setOID(oidMatDispatchReceiveItem);
                        matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup1);

                        this.oid = pstMatDispatchReceiveItem.updateExc(this.matDispatchReceiveItem);
                        
                        Material m = PstMaterial.fetchExc(matDispatchReceiveItem.getTargetItem().getMaterialId());
                        m.setAveragePrice(matDispatchReceiveItem.getTargetItem().getCost());
                        PstMaterial.updateExc(m);
                        Vector<MaterialDetail> listMatDet = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = '" + matDispatchReceiveItem.getTargetItem().getMaterialId() + "'", "");
                        if (!listMatDet.isEmpty()) {
                            MaterialDetail md = PstMaterialDetail.fetchExc(listMatDet.get(0).getOID());
                            md.setOngkos(ongkosBaru);
                            PstMaterialDetail.updateExc(md);
                        }
                        
                        //log history update opie-eyek 20140410
                        try {
                            insertHistory(oidUser, nameUser, cmd, oidMatDispatch);
                        } catch (Exception es) {
                        }
                        
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                /*
                 * For Update cost in Target
                 */
                long oidDfRecGroupCost = matDispatchReceiveItem.getDfRecGroupId();
                double totalSource = PstMatDispatchItem.getTotalHppSourceTransfer(oidDfRecGroupCost);
                double summaryExistingStockValue = SessMatDispatchReceive.getTotalHppExistingTarget(oidDfRecGroupCost);

                //Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget2(totalSource, oidDfRecGroupCost, summaryExistingStockValue);

                break;

            case Command.EDIT:
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                //Untuk mendapatkan df_rec_item_id dan delete group
                if (oidMatDispatchReceiveItem == 0) {
                    Vector vect = PstMatDispatchReceiveItem.checkOID(oidDfRecGroup1);
                    if (vect != null && vect.size() > 0) {
                        for (int i = 0; i < vect.size(); i++) {
                            matDispatchReceiveItem = (MatDispatchReceiveItem) vect.get(i);
                            long oidMatDispatchReceiveItemGroup = matDispatchReceiveItem.getOID();

                            try {
                                long oid = PstMatDispatchReceiveItem.deleteExc(oidMatDispatchReceiveItemGroup);

                                if (oid != 0) {
                                    //log history update opie-eyek 20140410
                                    try {
                                        insertHistory(oidUser, nameUser, cmd, oidMatDispatch);
                                    } catch (Exception es) {
                                    }

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
                    } else {
                        oidMatDispatchReceiveItem = 0;
                    }
                } else if (oidMatDispatchReceiveItem != 0) {                     
                    try {
                        long oid = PstMatDispatchReceiveItem.deleteExc(oidMatDispatchReceiveItem);
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


    //by dyas
    //tambah methods insertHistory
    //digunakan untuk men-set data yang nantinya akan diinputkan ke logHistory
    public  void insertHistory(long userID, String nameUser, int cmd, long oid)
    {
        try
        {
           LogSysHistory logSysHistory = new LogSysHistory();
           logSysHistory.setLogUserId(userID);
           logSysHistory.setLogLoginName(nameUser);
           logSysHistory.setLogApplication("Prochain");
           logSysHistory.setLogOpenUrl("warehouse/material/dispatch/df_unit_wh_material_edit.jsp");
           logSysHistory.setLogUpdateDate(dateLog);
           logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
           logSysHistory.setLogUserAction(Command.commandString[cmd]);
           logSysHistory.setLogDocumentNumber(matDispatch.getDispatchCode());
           logSysHistory.setLogDocumentId(oid);
           logSysHistory.setLogDetail(this.matDispatchReceiveItem.getLogDetail(prevMatDispatchItem, prevMatReceiveItem));

          if(!logSysHistory.getLogDetail().equals("") || cmd==Command.DELETE)
           {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
           }
      }
      catch(Exception e)
      {

      }
    }
    
    public int actionHpp(int cmd, long oidMatDispatchReceiveItem, long oidDfRecGroup, long oidMatDispatch, long oidDfRecGroup1, int commandType, String nameUser, long oidUser) {
        msgString = "";
        long oidTarget = 0;
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExcHpp(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                    
                     try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }
                     
                     //cek material receive dan dispatch
                    
                    
                }
               
                
                frmMatDispatchReceiveItem.requestEntityObject(matDispatchReceiveItem);
                //Date date = ControlDate.getDateTime(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE], req);
                //matCosting.setCostingDate(date);
                if (oidMatDispatchReceiveItem != 0) {
                    try 
                    {
                        if (matDispatchReceiveItem.getSourceItem().getOID() != 0){
                        prevMatDispatchItem = PstMatDispatchItem.fetchExc(matDispatchReceiveItem.getSourceItem().getOID());//matDispatchReceiveItem.getSourceItem();
                        
//                        Vector listTarget = PstMatDispatchReceiveItem.list(0, 0, PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_ITEM_ID]+" = "
//                                +oidMatDispatchReceiveItem, null);
//                        MatDispatchReceiveItem mdri = (MatDispatchReceiveItem) listTarget.get(0);
//                        Vector listTarget = PstMatReceive.list(0, 0, PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID]+" = "
//                                + oidMatDispatch, null);
//                        MatReceive mr = (MatReceive) listTarget.get(0);
//                        Vector listTargetItem = PstMatReceiveItem.list(0, 0, PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+" = "
//                                + mr.getOID()+" AND "
//                                +PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_]+" = ", null);
//                        MatReceiveItem mri = (MatReceiveItem) listTargetItem.get(0);
                        //matDispatchReceiveItem.getTargetItem().setOID(mdri.getTargetItem().getReceiveMaterialId());
                        }
                    } catch (Exception exc) {
                    System.out.println("Exception : " + exc.toString());
                    }
                    try {
                        if (matDispatchReceiveItem.getTargetItem().getOID() != 0){
                        prevMatReceiveItem  = PstMatReceiveItem.fetchExc(matDispatchReceiveItem.getTargetItem().getOID());//matDispatchReceiveItem.getTargetItem();
                        
                        //isi oid source agar tidak nol di transfer table
//                        Vector listSource = PstMatDispatchItem.list(0, 0, PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]+" = "
//                                + oidMatDispatch, null);
//                        MatDispatchItem mdi = (MatDispatchItem) listSource.get(0);
////                        Vector listSourceItem = PstMatDispatchReceiveItem.list(0, 0, PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID]+" = "
////                                + mdi.getOID(), null);
////                        MatReceiveItem mri = (MatReceiveItem) listSourceItem.get(0);
//                        matDispatchReceiveItem.getSourceItem().setOID(mdi.getOID());
                        
                        }
                    } catch (Exception exc) {
                    System.out.println("Exception : " + exc.toString());
                    }
                }
                
                if (oidMatDispatchReceiveItem == 0) {
                    try {
                        //if(matDispatchReceiveItem.getDfRecGroupId()== 0){
                           // matDispatchReceiveItem.setDfRecGroupId(oidFactory.generateOID());
                       // }
                        //else{
                            //matDispatchReceiveItem.getDfRecGroupId();
                       // }
                         if(oidDfRecGroup== 0 && commandType == COMMAND_TYPE_GROUP){
                             matDispatchReceiveItem.setDfRecGroupId(oidFactory.generateOID());
                         }
                         else{
                             matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup);
                        }
                         if(commandType == COMMAND_TYPE_ITEM && oidDfRecGroup1!=0){
                            matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup1);
                         }
                         
                        //SessMatCosting sessCosting = new SessMatCosting();
                        //int maxCounter = sessCosting.getMaxCostingCounter(matCosting.getCostingDate(), matCosting);
                        //maxCounter = maxCounter + 1;
                        //matCosting.setCostingCodeCounter(maxCounter);
                        //matCosting.setCostingCode(sessCosting.generateCostingCode(matCosting));
                        //matCosting.setLastUpdate(new Date());   
                        this.oid = pstMatDispatchReceiveItem.insertExc(this.matDispatchReceiveItem);

                        /*
                         * For Update cost in Target
                         */
                       // long oidDfRecGroupCost = matDispatchReceiveItem.getDfRecGroupId();
                        //double qtyTarget = matDispatchReceiveItem.getTargetItem().getQty();
                        //double totalSource =PstMatDispatchItem.getTotalHppSourceTransfer(oidDfRecGroupCost);

                        
                        //double summaryExistingStockValue = SessMatDispatchReceive.getTotalHppExistingTarget(oidDfRecGroupCost);

                        //Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget(totalSource, oidDfRecGroupCost);
                        //Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget2(totalSource, oidDfRecGroupCost,summaryExistingStockValue );
                        //end of update HppTarget


                        //double qtyTarget = 0;
                        //double averagePrice = 0;
                        //double costTarget = 0;
                       // double costTargetNew = 0;
                        //double costTotal = 0;
                        //long oidDfRecItem = 0;

                      /* Vector vctGetQty = new Vector(1,1);
                        //vctGetQty = PstMatDispatchReceiveItem.getStockValueCurrentTarget(0,0,oidDfRecGroupCost);
                          vctGetQty = SessMatDispatchReceive.getStockValueCurrentTarget(0,0,oidDfRecGroupCost);
                        if( vctGetQty!=null &&  vctGetQty.size() > 0) {
                            for(int k=0;k<vctGetQty.size();k++){
                                MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)vctGetQty.get(k);
                                qtyTarget = dfRecItem.getTargetItem().getQty();
                                averagePrice = dfRecItem.getTargetItem().getMaterialTarget().getAveragePrice();
                                oidDfRecItem = dfRecItem.getTargetItem().getOID();
                                costTarget = totalSource / qtyTarget ;
                                dfRecItem.getTargetItem().setCost(costTarget);
                                costTotal = costTarget * qtyTarget;
                                dfRecItem.getTargetItem().setTotal(costTotal);
                                
                                PstMatDispatchReceiveItem.updateExc(dfRecItem);

                            }
                        }*/







                        //matDispatchReceiveItem.getTargetItem().setCost(total);
                       // System.out.println(totalSource);


                        //End of update cost Target

                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        //PstDocLogger.insertDataBo_toDocLogger(matCosting.getCostingCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matCosting.getLastUpdate(), matCosting.getRemark());
                        //--- end

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                } else {
                    try {
                        //matCosting.setLastUpdate(new Date());
                             matDispatchReceiveItem.setOID(oidMatDispatchReceiveItem);
                             matDispatchReceiveItem.setDfRecGroupId(oidDfRecGroup1);
                        
                             //ed
//                                Vector listTarget = PstMatReceive.list(0, 0, PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID]+" = "
//                                        + oidMatDispatch, null);
//                                MatReceive mr = (MatReceive) listTarget.get(0);
//                                Vector listTargetItem = PstMatReceiveItem.list(0, 0, PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+" = "
//                                        + mr.getOID(), null);
//                                MatReceiveItem mri = (MatReceiveItem) listTargetItem.get(0);
//                                matDispatchReceiveItem.getTargetItem().setOID(mri.getOID());

                         this.oid = pstMatDispatchReceiveItem.updateExcHpp(this.matDispatchReceiveItem);
                         
//                         //edit opie-eyek 20140410 untuk history tranfer unit
//                         prevMatDispatchItem = PstMatDispatchItem.fetchExc(matDispatchReceiveItem.getSourceItem().getOID()) ;//matDispatchReceiveItem.getSourceItem();
//                         prevMatReceiveItem  = PstMatReceiveItem.fetchExc(matDispatchReceiveItem.getSourceItem().getOID());//matDispatchReceiveItem.getTargetItem();
//                         
                         //PstMatReceiveItem.insertExc(matDispatchReceiveItem.getTargetItem());
                         //log history update opie-eyek 20140410
                         try{
                             insertHistory(oidUser, nameUser, cmd, oidMatDispatch);
                         }catch(Exception es){
                         }
                        /**
                         * gadnyana
                         * untuk insert ke doc logger
                         * jika tidak di perlukan uncomment
                         */
                        //PstDocLogger.updateDataBo_toDocLogger(matCosting.getCostingCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matCosting.getLastUpdate(), matCosting.getRemark());
                        //--- end

                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                 /*
                         * For Update cost in Target
                         */
                       long oidDfRecGroupCost = matDispatchReceiveItem.getDfRecGroupId();
                        //double qtyTarget = matDispatchReceiveItem.getTargetItem().getQty();
                        double totalSource =PstMatDispatchItem.getTotalHppSourceTransfer(oidDfRecGroupCost);

                        //ed
                        //double summaryExistingStockValue = SessMatDispatchReceive.getTotalHppExistingTarget(oidDfRecGroupCost);

                        //Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget(totalSource, oidDfRecGroupCost);
                        
                        //ed
                       // Vector updateHppTarget = SessMatDispatchReceive.updateHppTarget2(totalSource, oidDfRecGroupCost,summaryExistingStockValue );
                        //end of update HppTarget

                break;

            case Command.EDIT:
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatDispatchReceiveItem != 0) {
                    try {
                        matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oidMatDispatchReceiveItem);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE :
                 //Untuk mendapatkan df_rec_item_id dan delete group
                if (oidMatDispatchReceiveItem == 0) {
                   Vector vect = PstMatDispatchReceiveItem.checkOID(oidDfRecGroup1);
                    if (vect != null && vect.size() > 0) {
                       for(int i=0; i<vect.size(); i++){
                        matDispatchReceiveItem = (MatDispatchReceiveItem) vect.get(i);
                        long oidMatDispatchReceiveItemGroup = matDispatchReceiveItem.getOID();
                        
                         try{
                            long oid = PstMatDispatchReceiveItem.deleteExc(oidMatDispatchReceiveItemGroup);
                            
                            if(oid!=0){
                                    //log history update opie-eyek 20140410
                                    try{
                                         insertHistory(oidUser, nameUser, cmd, oidMatDispatch);
                                    }catch(Exception es){
                                    }
                                     
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
                            }else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                                }
                            
                            }catch(DBException dbexc){
				excCode = dbexc.getErrorCode();
				msgString = getSystemMessage(excCode);
                            }catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }
                    }
                   else {
                       oidMatDispatchReceiveItem = 0;
                   }
                }
                   //end of Delete Group

                else if(oidMatDispatchReceiveItem != 0) {
                     //Untuk mendaptkan oidReceiveMaterialItem
                     //String whereClause = PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatchReceiveItem;
                       // Vector vect = PstMatDispatchReceiveItem.list(0, 0, whereClause, "");
                      //  if (vect != null && vect.size() > 0) {
                           // for (int l = 0; l < vect.size(); l++) {
                                //MatDispatchReceiveItem matDispatchReceiveItem = (MatDispatchReceiveItem) vect.get(l);
                               // CtrlMatDispatchReceiveItem ctrlMatDpsRecItm = new CtrlMatDispatchReceiveItem();
                               // ctrlMatDpsRecItm.action(Command.DELETE, matDispatchReceiveItem.getOID(), oidMatDispatchReceiveItem);
                           // }
                       // }

                    try{


                        long oid = PstMatDispatchReceiveItem.deleteExc(oidMatDispatchReceiveItem);
			if(oid!=0){
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
			}else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                               }
                        }catch(DBException dbexc){
				excCode = dbexc.getErrorCode();
				msgString = getSystemMessage(excCode);
			}catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
			}
		}
                //for delete Group
                /*if (oidDfRecGroup1 != 0) {
                    try{
                        long oidGroup = PstMatDispatchReceiveItem.deleteGroup(oidDfRecGroup1);
			if(oidGroup!=0){
                                    msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                    excCode = RSLT_OK;
			}else{
                                    msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                    excCode = RSLT_FORM_INCOMPLETE;
                              }
                        //}catch(DBException dbexc){
				//excCode = dbexc.getErrorCode();
				//msgString = getSystemMessage(excCode);
			}catch(Exception exc){
				msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
			}

              }*/
                //end of delete Group
                break;
        }
        return rsCode;
    }
    
    
}
