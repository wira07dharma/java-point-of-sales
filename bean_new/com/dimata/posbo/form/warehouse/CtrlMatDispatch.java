package com.dimata.posbo.form.warehouse;

/* java package */
import com.dimata.common.entity.location.Location;
import java.util.*;
import javax.servlet.http.*;

import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.entity.I_IJGeneral;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.common.entity.logger.PstDocLogger;
//generate oid
import com.dimata.posbo.db.OIDFactory;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;

import com.dimata.common.entity.system.*;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialDetail;
import com.dimata.posbo.entity.masterdata.PstMaterial;

import static com.dimata.posbo.form.warehouse.FrmMatDispatch.FRM_FIELD_DISPATCH_DATE;

import com.dimata.posbo.entity.masterdata.PstMaterialDetail;

import com.dimata.posbo.session.masterdata.SessPosting;

public class CtrlMatDispatch extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;

    public static String[][] resultText
            = {
                {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
                {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
            };

    private int start;
    private String msgString;
    private MatDispatch matDispatch;
    private MatDispatch prevMatDispatch;
    private PstMatDispatch pstMatDispatch;
    private FrmMatDispatch frmMatDispatch;
    private Material mat;
    private HttpServletRequest req;
    int language = LANGUAGE_DEFAULT;
    long oid = 0;
    //for receive item
    private OIDFactory oidFactory = new OIDFactory();
    private Date dateLog = new Date();

    public CtrlMatDispatch(HttpServletRequest request) {
        msgString = "";
        matDispatch = new MatDispatch();
        try {
            pstMatDispatch = new PstMatDispatch(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmMatDispatch = new FrmMatDispatch(request, matDispatch);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatDispatch.addError(frmMatDispatch.FRM_FIELD_DISPATCH_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MatDispatch getMatDispatch() {
        return matDispatch;
    }

    public FrmMatDispatch getForm() {
        return frmMatDispatch;
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

    public int action(int cmd, long oidMatDispatch) {
        return action(cmd, oidMatDispatch, "", 0);
    }

    public int action(int cmd, long oidMatDispatch, String nameUser, long userID) {

        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMatDispatch != 0) {
                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }

                    //by dyas
                    //tambah try catch untuk mem-fetch data di pstMatDispatch berdasarkan id yang dibawa
                    try {
                        prevMatDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                    }
                }

                int typeOfBusinessDetail = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));

                frmMatDispatch.requestEntityObject(matDispatch);
                Date date = ControlDate.getDateTime(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], req);
				if(date != null){
					matDispatch.setDispatchDate(date);
				}

                //for getting location type
                int LocationType = matDispatch.getLocationType();
                //End of getting location type

                if (oidMatDispatch == 0 && LocationType != PstMatDispatch.FLD_TYPE_TRANSFER_UNIT && LocationType != PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR) {
                    try {
                        SessMatDispatch sessDispatch = new SessMatDispatch();
                        int maxCounter = sessDispatch.getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
                        maxCounter = maxCounter + 1;
                        matDispatch.setDispatchCodeCounter(maxCounter);
                        matDispatch.setDispatchCode(sessDispatch.generateDispatchCode(matDispatch));
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatDispatch.insertExc(this.matDispatch);

                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oid);
                        }

                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                /*
                 * Automatically Making a new Document for Receive Material
                 * if Dispatch Unit was creating
                 * Document Number is same with Dispatch Unit
                 * By Mirahu
                 */
                if (oidMatDispatch == 0 && (LocationType == PstMatDispatch.FLD_TYPE_TRANSFER_UNIT || LocationType == PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR)) {
                    try {
                        SessMatDispatch sessDispatch = new SessMatDispatch();
                        int maxCounter = sessDispatch.getMaxDispatchUnitCounter(matDispatch.getDispatchDate(), matDispatch);
                        maxCounter = maxCounter + 1;
                        matDispatch.setDispatchCodeCounter(maxCounter);
                        matDispatch.setDispatchCode(sessDispatch.generateDispatchUnitCode(matDispatch));
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatDispatch.insertExc(this.matDispatch);

                        //by dyas
                        //tambah kondisi untuk memanggil methods insertHistory dan melakukan insert
                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oid);
                        }

                        /*
                         * Automatically Making a new Document for Receive Material
                         * if Dispatch Unit was creating
                         * Document Number is same with Dispatch Unit
                         * By Mirahu
                         */
                        if (matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_UNIT || matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR) {
                            //matDispatch = pstMatDispatch.fetchExc(oid);
                            MatReceive matReceive = new MatReceive();
                            matReceive.setOID(oidFactory.generateOID());
                            matReceive.setLocationId(matDispatch.getDispatchTo());
                            matReceive.setLocationType(matDispatch.getLocationType());
                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                            matReceive.setReceiveStatus(matDispatch.getDispatchStatus());
                            matReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH_UNIT);
                            if (matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_UNIT) {
                                matReceive.setRemark("Automatic Receive process from transfer unit number : " + matDispatch.getDispatchCode());
                            } else if (matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR) {
                                matReceive.setRemark("Automatic Receive process from transfer lebur number : " + matDispatch.getDispatchCode());
                            }
                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                            matReceive.setRecCode(matDispatch.getDispatchCode());
                            matReceive.setRecCodeCnt(matDispatch.getDispatchCodeCounter());
                            matReceive.setDispatchMaterialId(matDispatch.getOID());
                            //added by dewok for jewelry 2018-02-21
                            matReceive.setReceiveItemType(matDispatch.getDispatchItemType());
                            //added by dewok 20180719
                            matReceive.setIdBillMainSalesOrder(matDispatch.getIdBillMainSalesOrder());
                            PstMatReceive.insertExc(matReceive);
                        }

                        /*
                         * End of making a new Document for receive material
                         */
                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                } /*
                 * End of making a new Document for receive material
                 */ // } else {

                if (oidMatDispatch != 0) {
                    try {
                        //updated by dewok 2018-02-05 for generate new code
//                        Location lastLoc = PstLocation.fetchExc(matDispatch.getLocationId());
//                        Location newLoc = PstLocation.fetchExc(matDispatch.getDispatchTo());
//                        String lastCode = matDispatch.getDispatchCode();
//                        String splitCode[] = lastCode.split("-");
//                        String newCode = lastLoc.getCode()+"-"+splitCode[1]+"-"+splitCode[2]+"-"+newLoc.getCode()+"-"+splitCode[4];
//                        matDispatch.setDispatchCode(newCode);
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatDispatch.updateExc(this.matDispatch);
						int dutyFree = Integer.parseInt(PstSystemProperty.getValueByName("ENABLE_DUTY_FREE"));
						if(dutyFree == 1){
							if(matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){
								String whereClause = PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_DISPATCH_MATERIAL_ITEM_ID] 
										+ "=" + matDispatch.getOID();
								Vector listMdb = PstMatDispatchBill.list(0, 0, whereClause, "");
								for(int i = 0; i < listMdb.size(); i++){
									MatDispatchBill mdb = (MatDispatchBill) listMdb.get(i);
									BillMain bm = PstBillMain.fetchExc(mdb.getCashBillOid());
									if(bm != null){
										bm.setStatus(PstBillMain.DELIVERY_STATUS_IN_TRANSIT);
										PstBillMain.updateExc(bm);
									}
								}
							}
						}
                        long oidRecMaterial = 0;

                        //by dyas
                        //tambah kondisi untuk memanggil methods insertHistory dan melakukan update
                        if (oid != 0) {
                            int cmdHistory = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdHistory, oid);
                        }

                        /*
                         * Updating Doc Receive Material if dispatch Unit was updating
                         * By Mirahu
                         */
                        if (matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_UNIT || matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR) {
                            oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);
                            MatReceive matReceive = new MatReceive();
                            matReceive = PstMatReceive.fetchExc(oidRecMaterial);
                            matReceive.setLocationId(matDispatch.getDispatchTo());
                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                            matReceive.setReceiveStatus(matDispatch.getDispatchStatus());
                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                            matReceive.setLastUpdate(new Date());
                            PstMatReceive.updateExc(matReceive);
                        }

                        //update by dewok 2018-03-13 nilai berat per item dilebur 
                        if (matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL && matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR) {
                            Vector listDispatchItem = PstMatDispatchItem.list(0, 0, matDispatch.getOID());
                            if (listDispatchItem != null && listDispatchItem.size() > 0) {
                                for (int x = 0; x < listDispatchItem.size(); x++) {
                                    Vector temp = (Vector) listDispatchItem.get(x);
                                    MatDispatchItem dfItem = (MatDispatchItem) temp.get(0);
                                    Material m = PstMaterial.fetchExc(dfItem.getMaterialId());
                                    //update berat item material
                                    Vector<MaterialDetail> listMatDetail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = '" + dfItem.getMaterialId() + "'", "");
                                    if (!listMatDetail.isEmpty()) {
                                        MaterialDetail detail = (MaterialDetail) listMatDetail.get(0);
                                        MaterialDetail prevDetail = PstMaterialDetail.fetchExc(detail.getOID());
                                        detail.setQty(0);
                                        detail.setBerat(0);
                                        PstMaterialDetail.updateExc(detail);
                                        String historyChanged = detail.getLogDetail(prevDetail);
                                        insertHistoryMaterial(userID, nameUser, cmd, dfItem.getMaterialId(), m, historyChanged, "Transfer Lebur");
                                    }
                                }
                            }
                        }

                        /*
                         * End of Updating receive material
                         */
                        //automatic creating receiving
                        // by Mirahu
                        //201109
                        String autoRec = "";
                        String receiveOutoFinalByDf = "";
                        try {
                            autoRec = PstSystemProperty.getValueByName("AUTO_REC_FA");

                        } catch (Exception exc) {
                            System.out.println(exc);
                            autoRec = "Y";
                        }

                        try {
                            receiveOutoFinalByDf = PstSystemProperty.getValueByName("RECEIVE_AUTO_FINAL_BY_DF");
                        } catch (Exception exc) {
                            System.out.println(exc);
                            receiveOutoFinalByDf = "1";
                        }

                        oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);

                        //proses auto input jika menggunakan perhitungan fifo dan status dokument final
                        int calculateStock = Integer.valueOf(PstSystemProperty.getValueByName("CALCULATE_STOCK_VALUE"));
                        if (calculateStock == 1 && matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                            //cek material yang akan di di transfer berdasarkan dokument transfer
                            //String where = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]+"='"+oidMatDispatch+"'";
                            Vector matSerialCode = PstMatDispatchItem.list(0, 0, oidMatDispatch, "");
                            if (matSerialCode != null && matSerialCode.size() > 0) {
                                for (int x = 0; x < matSerialCode.size(); x++) {
                                    Vector vMatdispatchitem = (Vector) matSerialCode.get(x);
                                    MatDispatchItem dfItem = (MatDispatchItem) vMatdispatchitem.get(0);
                                    Material mat = (Material) vMatdispatchitem.get(1);
                                    if (mat.getRequiredSerialNumber() == 0) {
                                        double qty = dfItem.getQty();
                                        double value = dfItem.getHpp();
                                        boolean xx = PstDispatchStockCode.automaticInsertSerialNumber(qty, dfItem.getMaterialId(), value, this.matDispatch.getLocationId(), dfItem.getOID());

                                    }
                                }
                            }
                        }

                        if (matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL && matDispatch.getLocationType() != PstMatDispatch.FLD_TYPE_TRANSFER_UNIT && matDispatch.getLocationType() != PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR && autoRec.equals("Y") && oidRecMaterial == 0) {
                            MatReceive matReceive = new MatReceive();
                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                            matReceive.setLocationId(matDispatch.getDispatchTo());
                            matReceive.setLocationType(PstLocation.TYPE_LOCATION_STORE);
                            if (receiveOutoFinalByDf.equals("1")) {
                                matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_FINAL);
                            } else {
                                matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            }
                            matReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH);
                            matReceive.setDispatchMaterialId(matDispatch.getOID());
                            matReceive.setRemark("Automatic Receive process from transfer number : " + matDispatch.getDispatchCode());
                            int docType = -1;
                            try {
                                I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
                                docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
                            } catch (Exception e) {
                            }
                            matReceive.setRecCodeCnt(SessMatReceive.getIntCode(matReceive, matReceive.getReceiveDate(), 0, docType, 0, true));
                            matReceive.setRecCode(SessMatReceive.getCodeReceive(matReceive));
                            matReceive.setCurrencyId(1);
                            matReceive.setGondolaId(matDispatch.getGondolaId());
                            matReceive.setGondolaToId(matDispatch.getGondolaToId());
                            matReceive.setReceiveItemType(matDispatch.getDispatchItemType());
                            //added by dewok 20180719
                            matReceive.setIdBillMainSalesOrder(matDispatch.getIdBillMainSalesOrder());
							//add by arisena 15/10/2019
							if(dutyFree == 1){
								matReceive.setJenisDokumen(matDispatch.getJenisDokumen());
								matReceive.setNomorBc(matDispatch.getNomorBeaCukai());
								matReceive.setTglBc(matDispatch.getTanggalBC());
							}

                            oid = PstMatReceive.insertExc(matReceive);

                            Vector listDispatchItem = new Vector();
                            listDispatchItem = PstMatDispatchItem.list(0, 0, matDispatch.getOID());
                            if (listDispatchItem != null && listDispatchItem.size() > 0) {
                                double totalBerat = 0;
                                for (int x = 0; x < listDispatchItem.size(); x++) {
                                    Vector temp = (Vector) listDispatchItem.get(x);
                                    MatDispatchItem dfItem = (MatDispatchItem) temp.get(0);
                                    MatReceiveItem matReceiveItem = new MatReceiveItem();
                                    matReceiveItem.setUnitId(dfItem.getUnitId());
                                    matReceiveItem.setQty(dfItem.getQty());
                                    matReceiveItem.setResidueQty(dfItem.getQty());
                                    matReceiveItem.setMaterialId(dfItem.getMaterialId());
                                    matReceiveItem.setReceiveMaterialId(oid);
                                    matReceiveItem.setCost(dfItem.getHpp());
                                    matReceiveItem.setCurrBuyingPrice(dfItem.getHpp());
                                    if (typeOfBusinessDetail == 2) {
                                        matReceiveItem.setTotal(dfItem.getHpp() + dfItem.getOngkos());
                                    } else {
                                        matReceiveItem.setTotal(matReceiveItem.getQty() * matReceiveItem.getCost());
                                    }
                                    //update by de koyo
                                    matReceiveItem.setCurrencyId(matReceive.getCurrencyId());
                                    //dewok-2017
                                    matReceiveItem.setForwarderCost(dfItem.getOngkos());
                                    matReceiveItem.setBerat(dfItem.getBeratCurrent());
                                    totalBerat += dfItem.getBeratCurrent();

                                    long oidInsertOtomatic = PstMatReceiveItem.insertExc(matReceiveItem);

                                    if (typeOfBusinessDetail == 2) {
                                        //update nilai stock for litama by dewok 2018-02-06
                                        Material m = PstMaterial.fetchExc(dfItem.getMaterialId());
                                        Material prevMat = PstMaterial.fetchExc(dfItem.getMaterialId());
                                        m.setAveragePrice(dfItem.getHpp());
                                        //m.setGondolaCode(dfItem.getGondolaToId());
                                        PstMaterial.updateExc(m);
                                        String historyChanged = m.getLogDetail(prevMat);
                                        //update berat item material
                                        Vector<MaterialDetail> listMatDetail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = '" + dfItem.getMaterialId() + "'", "");
                                        if (!listMatDetail.isEmpty()) {
                                            MaterialDetail detail = (MaterialDetail) listMatDetail.get(0);
                                            MaterialDetail prevDetail = PstMaterialDetail.fetchExc(detail.getOID());
                                            detail.setBerat(dfItem.getBeratCurrent());
                                            PstMaterialDetail.updateExc(detail);
                                            historyChanged += detail.getLogDetail(prevDetail);
                                        }
                                        insertHistoryMaterial(userID, nameUser, cmd, dfItem.getMaterialId(), m, historyChanged, "Transfer");
                                    }

                                    //add opie-eyek 20131206
                                    //untuk insert otomatis serial code dari transfer
                                    if (oidInsertOtomatic != 0) {
                                        Vector listSerialCode = new Vector();
                                        listSerialCode = PstDispatchStockCode.checkListStockCodeDispatch(dfItem.getOID());
                                        for (int c = 0; c < listSerialCode.size(); c++) {
                                            DispatchStockCode dispatchStockCode = (DispatchStockCode) listSerialCode.get(c);
                                            ReceiveStockCode receiveStockCode = new ReceiveStockCode();
                                            receiveStockCode.setReceiveMaterialItemId(oidInsertOtomatic);
                                            receiveStockCode.setStockCode(dispatchStockCode.getStockCode());
                                            receiveStockCode.setStockValue(dispatchStockCode.getStockValue());
                                            receiveStockCode.setReceiveMaterialId(oid);
                                            try {
                                                PstReceiveStockCode.insertExc(receiveStockCode);
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                }
                                //dewok-2017
                                matReceive.setBerat(totalBerat);
                                PstMatReceive.updateExc(matReceive);
                                MatReceive prevMatReceive = PstMatReceive.fetchExc(matReceive.getOID());
                                insertHistoryMatReceive(userID, nameUser, cmd, matReceive.getOID(), matReceive, prevMatReceive);
                            }

                        }

//                        //comment out by PAK TUT
                         /*delete receiving if status document df back to draft
                         by mirahu
                         update 12032012 */
                        if (matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && matDispatch.getLocationType() != PstMatDispatch.FLD_TYPE_TRANSFER_UNIT && matDispatch.getLocationType() != PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR && autoRec.equals("Y")) {
                            if (oidMatDispatch != 0) {
                                oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);
                                if (oidRecMaterial != 0) {
                                    CtrlMatReceiveItem objCtlItem = new CtrlMatReceiveItem();
                                    MatReceiveItem objItem = new MatReceiveItem();
                                    String stWhereClose = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + oidRecMaterial;
                                    Vector vListItem = PstMatReceiveItem.list(0, 0, stWhereClose, "");
                                    if (vListItem != null && vListItem.size() > 0) {
                                        for (int i = 0; i < vListItem.size(); i++) {
                                            objItem = (MatReceiveItem) vListItem.get(i);
                                            objCtlItem.action(Command.DELETE, objItem.getOID(), oidRecMaterial);
                                        }
                                    }
                                    CtrlMatReceive objRec = new CtrlMatReceive();
                                    MatReceive objRecId = new MatReceive();
                                    String stWhereRec = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + " = " + oidRecMaterial;
                                    Vector vListRec = PstMatReceive.list(0, 0, stWhereRec, "");
                                    if (vListRec != null && vListRec.size() > 0) {
                                        for (int i = 0; i < vListRec.size(); i++) {
                                            objRecId = (MatReceive) vListRec.get(i);
                                            objRec.action(Command.DELETE, objRecId.getOID());
                                        }
                                    }
                                }
                            }
                        }

                        //posting jika statusnya final langsung di posting
//                        if(matDispatch.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
//                             SessPosting sessPosting = new SessPosting();
//                             boolean isOK = sessPosting.postedDispatchDoc(oidMatDispatch);
//                             if(isOK){
//                                 matDispatch.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
//                             }
//                             
//                        }
                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.updateDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidMatDispatch != 0) {
                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatDispatch != 0) {
                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatDispatch != 0) {
                    try {

                        /**
                         * by mirahu proses penghapusan di pos dispatch receive
                         * item
                         */
                        String whereClauseTransferUnit = PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatch;
                        Vector vect2 = PstMatDispatchReceiveItem.list(0, 0, whereClauseTransferUnit, "");
                        if (vect2 != null && vect2.size() > 0) {
                            for (int k = 0; k < vect2.size(); k++) {
                                MatDispatchReceiveItem matDispatchReceiveItem = (MatDispatchReceiveItem) vect2.get(k);
                                CtrlMatDispatchReceiveItem ctrlMatDpsRecItm = new CtrlMatDispatchReceiveItem();
                                ctrlMatDpsRecItm.action(Command.DELETE, matDispatchReceiveItem.getOID(), 0, oidMatDispatch, 0, -1);
                            }
                        }
                        /*End Of delete pos dispatch receive item*/

                        String whereClause = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatch;
                        Vector vect = PstMatDispatchItem.list(0, 0, whereClause, "");
                        if (vect != null && vect.size() > 0) {
                            for (int k = 0; k < vect.size(); k++) {
                                MatDispatchItem matDispatchItem = (MatDispatchItem) vect.get(k);
                                CtrlMatDispatchItem ctrlMatDpsItm = new CtrlMatDispatchItem();
                                ctrlMatDpsItm.action(Command.DELETE, matDispatchItem.getOID(), oidMatDispatch);
                            }
                        }

                        /**
                         * gadnyan proses penghapusan di doc logger jika tidak
                         * di perlukan uncoment perintah ini
                         */
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                        PstDocLogger.deleteDataBo_inDocLogger(matDispatch.getDispatchCode(), I_DocType.MAT_DOC_TYPE_DF);
                        // -- end

                        /*
                         * Deleting Doc receive Material if Dispatch Unit was deleting
                         * By Mirahu
                         */
                        if (matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_UNIT) {
                            long oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);
                            //MatReceive matReceive = new MatReceive();
                            //matReceive = PstMatReceive.fetchExc(oidRecMaterial);
                            long oidRec = PstMatReceive.deleteExc(oidRecMaterial);
                            if (oidRec != 0) {
                                msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                excCode = RSLT_OK;
                            } else {
                                msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                excCode = RSLT_FORM_INCOMPLETE;
                            }
                        }
                        /*
                         * End of Deleting Doc receive Material
                         */

                        long oid = PstMatDispatch.deleteExc(oidMatDispatch);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;

                            //by dyas
                            //tambah try catch
                            //untuk memanggil methods insertHistory
                            try {
                                insertHistory(userID, nameUser, cmd, oid);
                            } catch (Exception e) {

                            }
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        System.out.println("exception dbexc : " + dbexc.toString());
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        System.out.println("exception exc : " + exc.toString());
                    }
                }
                break;

            default:
                break;
        }
        return rsCode;
    }

    public int actionProduksi(int cmd, long oidMatDispatch, String nameUser, long userID, HttpServletRequest request) {

        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMatDispatch != 0) {
                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }

                    //by dyas
                    //tambah try catch untuk mem-fetch data di pstMatDispatch berdasarkan id yang dibawa
                    try {
                        prevMatDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                    }
                }

                frmMatDispatch.requestEntityObject(matDispatch);
                //Date date = ControlDate.getDateTime(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], req);
                //matDispatch.setDispatchDate(date);
                String date = FRMQueryString.requestString(request, "FRM_FIELD_DISPATCH_DATE");

                //commented by eddy produksi
                // matDispatch.setDispatchDate(Formater.formatDate(date, "yyyy-mm-dd"));
                //for getting location type
                int LocationType = matDispatch.getLocationType();
                //End of getting location type
                if (LocationType == 3 || LocationType == 4) {
                    matDispatch.setDispatchDate(Formater.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
                } else {
                    matDispatch.setDispatchDate(Formater.formatDate(date, "yyyy-mm-dd"));
                }

                if (oidMatDispatch == 0 && (LocationType != PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI && LocationType != 4)) {
                    try {
                        SessMatDispatch sessDispatch = new SessMatDispatch();
                        int maxCounter = sessDispatch.getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
                        maxCounter = maxCounter + 1;
                        matDispatch.setDispatchCodeCounter(maxCounter);
                        matDispatch.setDispatchCode(sessDispatch.generateDispatchCode(matDispatch));
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatDispatch.insertExc(this.matDispatch);

                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oid);
                        }

                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                /*
                 * Automatically Making a new Document for Receive Material
                 * if Dispatch Unit was creating
                 * Document Number is same with Dispatch Unit
                 * By Mirahu
                 */
                if (oidMatDispatch == 0 && (LocationType == PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI || LocationType == 4)) {
                    try {
                        SessMatDispatch sessDispatch = new SessMatDispatch();
                        int maxCounter = sessDispatch.getMaxDispatchUnitCounter(matDispatch.getDispatchDate(), matDispatch);
                        maxCounter = maxCounter + 1;
                        matDispatch.setDispatchCodeCounter(maxCounter);
                        matDispatch.setDispatchCode(sessDispatch.generateDispatchUnitCode(matDispatch));
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatDispatch.insertExc(this.matDispatch);

                        //by dyas
                        //tambah kondisi untuk memanggil methods insertHistory dan melakukan insert
                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oid);
                        }

                        /*
                         * Automatically Making a new Document for Receive Material
                         * if Dispatch Unit was creating
                         * Document Number is same with Dispatch Unit
                         * By Mirahu
                         */
                        if (matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI || matDispatch.getLocationType() == 4) {
                            //matDispatch = pstMatDispatch.fetchExc(oid);
                            MatReceive matReceive = new MatReceive();
                            matReceive.setOID(oidFactory.generateOID());
                            matReceive.setLocationId(matDispatch.getDispatchTo());
                            matReceive.setLocationType(matDispatch.getLocationType());
                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                            matReceive.setReceiveStatus(matDispatch.getDispatchStatus());
                            matReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH_UNIT);
                            matReceive.setRemark("Automatic Receive process from transfer unit number : " + matDispatch.getDispatchCode());
                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                            matReceive.setRecCode(matDispatch.getDispatchCode());
                            matReceive.setRecCodeCnt(matDispatch.getDispatchCodeCounter());
                            matReceive.setDispatchMaterialId(matDispatch.getOID());
                            PstMatReceive.insertExc(matReceive);
                        }
                        /*
                         * End of making a new Document for receive material
                         */

                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                } /*
                 * End of making a new Document for receive material
                 */ // } else {
                else {
                    try {
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatDispatch.updateExc(this.matDispatch);
                        long oidRecMaterial = 0;

                        //by dyas
                        //tambah kondisi untuk memanggil methods insertHistory dan melakukan update
                        if (oid != 0) {
                            int cmdHistory = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdHistory, oid);
                        }

                        /*
                         * Updating Doc Receive Material if dispatch Unit was updating
                         * By Mirahu
                         */
                        if (matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI || matDispatch.getLocationType() == 4) {
                            oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);
                            MatReceive matReceive = new MatReceive();
                            matReceive = PstMatReceive.fetchExc(oidRecMaterial);
                            matReceive.setLocationId(matDispatch.getDispatchTo());
                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                            matReceive.setReceiveStatus(matDispatch.getDispatchStatus());
                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                            matReceive.setLastUpdate(new Date());
                            PstMatReceive.updateExc(matReceive);
                        }
                        /*
                         * End of Updating receive material
                         */

                        //automatic creating receiving
                        // by Mirahu
                        //201109
                        String autoRec = "";
                        String receiveOutoFinalByDf = "";
                        try {
                            autoRec = PstSystemProperty.getValueByName("AUTO_REC_FA");

                        } catch (Exception exc) {
                            System.out.println(exc);
                            autoRec = "Y";
                        }

                        try {
                            receiveOutoFinalByDf = PstSystemProperty.getValueByName("RECEIVE_AUTO_FINAL_BY_DF");
                        } catch (Exception exc) {
                            System.out.println(exc);
                            receiveOutoFinalByDf = "1";
                        }

                        oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);

                        //proses auto input jika menggunakan perhitungan fifo dan status dokument final
                        int calculateStock = Integer.valueOf(PstSystemProperty.getValueByName("CALCULATE_STOCK_VALUE"));
                        if (calculateStock == 1 && matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                            //cek material yang akan di di transfer berdasarkan dokument transfer
                            //String where = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]+"='"+oidMatDispatch+"'";
                            Vector matSerialCode = PstMatDispatchItem.list(0, 0, oidMatDispatch, "");
                            if (matSerialCode != null && matSerialCode.size() > 0) {
                                for (int x = 0; x < matSerialCode.size(); x++) {
                                    Vector vMatdispatchitem = (Vector) matSerialCode.get(x);
                                    MatDispatchItem dfItem = (MatDispatchItem) vMatdispatchitem.get(0);
                                    Material mat = (Material) vMatdispatchitem.get(1);
                                    if (mat.getRequiredSerialNumber() == 0) {
                                        double qty = dfItem.getQty();
                                        double value = dfItem.getHpp();
                                        boolean xx = PstDispatchStockCode.automaticInsertSerialNumber(qty, dfItem.getMaterialId(), value, this.matDispatch.getLocationId(), dfItem.getOID());

                                    }
                                }
                            }
                        }

                        if (matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL && matDispatch.getLocationType() != PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI && autoRec.equals("Y") && oidRecMaterial == 0) {
                            MatReceive matReceive = new MatReceive();
                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                            matReceive.setLocationId(matDispatch.getDispatchTo());
                            matReceive.setLocationType(PstLocation.TYPE_LOCATION_STORE);
                            if (receiveOutoFinalByDf.equals("1")) {
                                matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_FINAL);
                            } else {
                                matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            }
                            matReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH);
                            matReceive.setDispatchMaterialId(matDispatch.getOID());
                            matReceive.setRemark("Automatic Receive process from transfer number : " + matDispatch.getDispatchCode());
                            int docType = -1;
                            try {
                                I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
                                docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
                            } catch (Exception e) {
                            }
                            matReceive.setRecCodeCnt(SessMatReceive.getIntCode(matReceive, matReceive.getReceiveDate(), 0, docType, 0, true));
                            matReceive.setRecCode(SessMatReceive.getCodeReceive(matReceive));
                            matReceive.setCurrencyId(1);
                            matReceive.setGondolaId(matDispatch.getGondolaId());
                            matReceive.setGondolaToId(matDispatch.getGondolaToId());

                            oid = PstMatReceive.insertExc(matReceive);

                            Vector listDispatchItem = new Vector();
                            listDispatchItem = PstMatDispatchItem.list(0, 0, matDispatch.getOID());
                            if (listDispatchItem != null && listDispatchItem.size() > 0) {
                                for (int x = 0; x < listDispatchItem.size(); x++) {
                                    Vector temp = (Vector) listDispatchItem.get(x);
                                    MatDispatchItem dfItem = (MatDispatchItem) temp.get(0);
                                    MatReceiveItem matReceiveItem = new MatReceiveItem();
                                    matReceiveItem.setUnitId(dfItem.getUnitId());
                                    matReceiveItem.setQty(dfItem.getQty());
                                    matReceiveItem.setResidueQty(dfItem.getQty());
                                    matReceiveItem.setMaterialId(dfItem.getMaterialId());
                                    matReceiveItem.setReceiveMaterialId(oid);
                                    matReceiveItem.setCost(dfItem.getHpp());
                                    matReceiveItem.setCurrBuyingPrice(dfItem.getHpp());
                                    matReceiveItem.setTotal(matReceiveItem.getQty() * matReceiveItem.getCost());
                                    //update by de koyo
                                    matReceiveItem.setCurrencyId(matReceive.getCurrencyId());

                                    long oidInsertOtomatic = PstMatReceiveItem.insertExc(matReceiveItem);

                                    //add opie-eyek 20131206
                                    //untuk insert otomatis serial code dari transfer
                                    if (oidInsertOtomatic != 0) {
                                        Vector listSerialCode = new Vector();
                                        listSerialCode = PstDispatchStockCode.checkListStockCodeDispatch(dfItem.getOID());
                                        for (int c = 0; c < listSerialCode.size(); c++) {
                                            DispatchStockCode dispatchStockCode = (DispatchStockCode) listSerialCode.get(c);
                                            ReceiveStockCode receiveStockCode = new ReceiveStockCode();
                                            receiveStockCode.setReceiveMaterialItemId(oidInsertOtomatic);
                                            receiveStockCode.setStockCode(dispatchStockCode.getStockCode());
                                            receiveStockCode.setStockValue(dispatchStockCode.getStockValue());
                                            receiveStockCode.setReceiveMaterialId(oid);
                                            try {
                                                PstReceiveStockCode.insertExc(receiveStockCode);
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                }
                            }

                        }

//                        //comment out by PAK TUT
                         /*delete receiving if status document df back to draft
                         by mirahu
                         update 12032012 */
                        if (matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && matDispatch.getLocationType() != PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI && autoRec.equals("Y")) {
                            if (oidMatDispatch != 0) {
                                oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);
                                if (oidRecMaterial != 0) {
                                    CtrlMatReceiveItem objCtlItem = new CtrlMatReceiveItem();
                                    MatReceiveItem objItem = new MatReceiveItem();
                                    String stWhereClose = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + oidRecMaterial;
                                    Vector vListItem = PstMatReceiveItem.list(0, 0, stWhereClose, "");
                                    if (vListItem != null && vListItem.size() > 0) {
                                        for (int i = 0; i < vListItem.size(); i++) {
                                            objItem = (MatReceiveItem) vListItem.get(i);
                                            objCtlItem.action(Command.DELETE, objItem.getOID(), oidRecMaterial);
                                        }
                                    }
                                    CtrlMatReceive objRec = new CtrlMatReceive();
                                    MatReceive objRecId = new MatReceive();
                                    String stWhereRec = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + " = " + oidRecMaterial;
                                    Vector vListRec = PstMatReceive.list(0, 0, stWhereRec, "");
                                    if (vListRec != null && vListRec.size() > 0) {
                                        for (int i = 0; i < vListRec.size(); i++) {
                                            objRecId = (MatReceive) vListRec.get(i);
                                            objRec.action(Command.DELETE, objRecId.getOID());
                                        }
                                    }
                                }
                            }
                        }

                        //posting jika statusnya final langsung di posting
//                        if(matDispatch.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
//                             SessPosting sessPosting = new SessPosting();
//                             boolean isOK = sessPosting.postedDispatchDoc(oidMatDispatch);
//                             if(isOK){
//                                 matDispatch.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
//                             }
//                             
//                        }
                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.updateDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidMatDispatch != 0) {
                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatDispatch != 0) {
                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatDispatch != 0) {
                    try {

                        /**
                         * by mirahu proses penghapusan di pos dispatch receive
                         * item
                         */
                        String whereClauseTransferUnit = PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatch;
                        Vector vect2 = PstMatDispatchReceiveItem.list(0, 0, whereClauseTransferUnit, "");
                        if (vect2 != null && vect2.size() > 0) {
                            for (int k = 0; k < vect2.size(); k++) {
                                MatDispatchReceiveItem matDispatchReceiveItem = (MatDispatchReceiveItem) vect2.get(k);
                                CtrlMatDispatchReceiveItem ctrlMatDpsRecItm = new CtrlMatDispatchReceiveItem();
                                ctrlMatDpsRecItm.action(Command.DELETE, matDispatchReceiveItem.getOID(), 0, oidMatDispatch, 0, -1);
                            }
                        }
                        /*End Of delete pos dispatch receive item*/

                        String whereClause = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatch;
                        Vector vect = PstMatDispatchItem.list(0, 0, whereClause, "");
                        if (vect != null && vect.size() > 0) {
                            for (int k = 0; k < vect.size(); k++) {
                                MatDispatchItem matDispatchItem = (MatDispatchItem) vect.get(k);
                                CtrlMatDispatchItem ctrlMatDpsItm = new CtrlMatDispatchItem();
                                ctrlMatDpsItm.action(Command.DELETE, matDispatchItem.getOID(), oidMatDispatch);
                            }
                        }

                        /**
                         * gadnyan proses penghapusan di doc logger jika tidak
                         * di perlukan uncoment perintah ini
                         */
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                        PstDocLogger.deleteDataBo_inDocLogger(matDispatch.getDispatchCode(), I_DocType.MAT_DOC_TYPE_DF);
                        // -- end

                        /*
                         * Deleting Doc receive Material if Dispatch Unit was deleting
                         * By Mirahu
                         */
                        if (matDispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_PRODUKSI || matDispatch.getLocationType() == 4) {
                            long oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);
                            //MatReceive matReceive = new MatReceive();
                            //matReceive = PstMatReceive.fetchExc(oidRecMaterial);
                            long oidRec = PstMatReceive.deleteExc(oidRecMaterial);
                            if (oidRec != 0) {
                                msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                excCode = RSLT_OK;
                            } else {
                                msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                excCode = RSLT_FORM_INCOMPLETE;
                            }
                        }
                        /*
                         * End of Deleting Doc receive Material
                         */

                        long oid = PstMatDispatch.deleteExc(oidMatDispatch);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;

                            //by dyas
                            //tambah try catch
                            //untuk memanggil methods insertHistory
                            try {
                                insertHistory(userID, nameUser, cmd, oid);
                            } catch (Exception e) {

                            }
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        System.out.println("exception dbexc : " + dbexc.toString());
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        System.out.println("exception exc : " + exc.toString());
                    }
                }
                break;

            default:
                break;
        }
        return rsCode;
    }

    public int actionHPP(int cmd, long oidMatDispatch, String nameUser, long userID, HttpServletRequest request) {

        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;

        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidMatDispatch != 0) {
                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                        System.out.println("Exception : " + exc.toString());
                    }

                    //by dyas
                    //tambah try catch untuk mem-fetch data di pstMatDispatch berdasarkan id yang dibawa
                    try {
                        prevMatDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (Exception exc) {
                    }
                }

                frmMatDispatch.requestEntityObject(matDispatch);

                int LocationType = matDispatch.getLocationType();
                String date = FRMQueryString.requestString(request, "FRM_FIELD_DISPATCH_DATE");
                if (LocationType == 4) {
                    matDispatch.setDispatchDate(Formater.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
                } else {
                    matDispatch.setDispatchDate(Formater.formatDate(date, "yyyy-mm-dd"));
                }

                if (oidMatDispatch == 0 && LocationType != 4) {
                    try {
                        SessMatDispatch sessDispatch = new SessMatDispatch();
                        int maxCounter = sessDispatch.getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
                        maxCounter = maxCounter + 1;
                        matDispatch.setDispatchCodeCounter(maxCounter);
                        matDispatch.setDispatchCode(sessDispatch.generateDispatchCode(matDispatch));
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatDispatch.insertExc(this.matDispatch);

                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oid);
                        }

                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                /*
                 * Automatically Making a new Document for Receive Material
                 * if Dispatch Unit was creating
                 * Document Number is same with Dispatch Unit
                 * By Mirahu
                 */
                if (oidMatDispatch == 0 && LocationType == 4) {
                    try {
                        SessMatDispatch sessDispatch = new SessMatDispatch();
                        int maxCounter = sessDispatch.getMaxDispatchUnitCounter(matDispatch.getDispatchDate(), matDispatch);
                        maxCounter = maxCounter + 1;
                        matDispatch.setDispatchCodeCounter(maxCounter);
                        matDispatch.setDispatchCode(sessDispatch.generateDispatchUnitCode(matDispatch));
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatDispatch.insertExc(this.matDispatch);

                        //by dyas
                        //tambah kondisi untuk memanggil methods insertHistory dan melakukan insert
                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oid);
                        }

                        /*
                         * Automatically Making a new Document for Receive Material
                         * if Dispatch Unit was creating
                         * Document Number is same with Dispatch Unit
                         * By Mirahu
                         */
                        if (matDispatch.getLocationType() == 4) {
                            //matDispatch = pstMatDispatch.fetchExc(oid);
                            MatReceive matReceive = new MatReceive();
                            matReceive.setOID(oidFactory.generateOID());
                            matReceive.setLocationId(matDispatch.getDispatchTo());
                            matReceive.setLocationType(matDispatch.getLocationType());
                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                            matReceive.setReceiveStatus(matDispatch.getDispatchStatus());
                            matReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH_UNIT);
                            matReceive.setRemark("Automatic Receive process from transfer unit number : " + matDispatch.getDispatchCode());
                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                            matReceive.setRecCode(matDispatch.getDispatchCode());
                            matReceive.setRecCodeCnt(matDispatch.getDispatchCodeCounter());
                            matReceive.setDispatchMaterialId(matDispatch.getOID());
                            PstMatReceive.insertExc(matReceive);
                        }
                        /*
                         * End of making a new Document for receive material
                         */

                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.insertDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                } /*
                 * End of making a new Document for receive material
                 */ // } else {
                else {
                    try {
                        matDispatch.setLast_update(new Date());
                        this.oid = pstMatDispatch.updateExc(this.matDispatch);
                        long oidRecMaterial = 0;

                        //by dyas
                        //tambah kondisi untuk memanggil methods insertHistory dan melakukan update
                        if (oid != 0) {
                            int cmdHistory = Command.UPDATE;
                            insertHistory(userID, nameUser, cmdHistory, oid);
                        }

                        /*
                         * Updating Doc Receive Material if dispatch Unit was updating
                         * By Mirahu
                         */
                        if (matDispatch.getLocationType() == 4) {
                            oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);
                            MatReceive matReceive = new MatReceive();
                            matReceive = PstMatReceive.fetchExc(oidRecMaterial);
                            matReceive.setLocationId(matDispatch.getDispatchTo());
                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                            matReceive.setReceiveStatus(matDispatch.getDispatchStatus());
                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                            matReceive.setLastUpdate(new Date());
                            PstMatReceive.updateExc(matReceive);
                        }
                        /*
                         * End of Updating receive material
                         */

                        //automatic creating receiving
                        // by Mirahu
                        //201109
                        String autoRec = "";
                        String receiveOutoFinalByDf = "";
                        try {
                            autoRec = PstSystemProperty.getValueByName("AUTO_REC_FA");

                        } catch (Exception exc) {
                            System.out.println(exc);
                            autoRec = "Y";
                        }

                        try {
                            receiveOutoFinalByDf = PstSystemProperty.getValueByName("RECEIVE_AUTO_FINAL_BY_DF");
                        } catch (Exception exc) {
                            System.out.println(exc);
                            receiveOutoFinalByDf = "1";
                        }

                        oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);

                        //proses auto input jika menggunakan perhitungan fifo dan status dokument final
                        int calculateStock = Integer.valueOf(PstSystemProperty.getValueByName("CALCULATE_STOCK_VALUE"));
                        if (calculateStock == 1 && matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                            //cek material yang akan di di transfer berdasarkan dokument transfer
                            //String where = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]+"='"+oidMatDispatch+"'";
                            Vector matSerialCode = PstMatDispatchItem.list(0, 0, oidMatDispatch, "");
                            if (matSerialCode != null && matSerialCode.size() > 0) {
                                for (int x = 0; x < matSerialCode.size(); x++) {
                                    Vector vMatdispatchitem = (Vector) matSerialCode.get(x);
                                    MatDispatchItem dfItem = (MatDispatchItem) vMatdispatchitem.get(0);
                                    Material mat = (Material) vMatdispatchitem.get(1);
                                    if (mat.getRequiredSerialNumber() == 0) {
                                        double qty = dfItem.getQty();
                                        double value = dfItem.getHpp();
                                        boolean xx = PstDispatchStockCode.automaticInsertSerialNumber(qty, dfItem.getMaterialId(), value, this.matDispatch.getLocationId(), dfItem.getOID());

                                    }
                                }
                            }
                        }

                        if (matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL && matDispatch.getLocationType() != 4 && autoRec.equals("Y") && oidRecMaterial == 0) {
                            MatReceive matReceive = new MatReceive();
                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                            matReceive.setLocationId(matDispatch.getDispatchTo());
                            matReceive.setLocationType(PstLocation.TYPE_LOCATION_STORE);
                            if (receiveOutoFinalByDf.equals("1")) {
                                matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_FINAL);
                            } else {
                                matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            }
                            matReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH);
                            matReceive.setDispatchMaterialId(matDispatch.getOID());
                            matReceive.setRemark("Automatic Receive process from transfer number : " + matDispatch.getDispatchCode());
                            int docType = -1;
                            try {
                                I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
                                docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
                            } catch (Exception e) {
                            }
                            matReceive.setRecCodeCnt(SessMatReceive.getIntCode(matReceive, matReceive.getReceiveDate(), 0, docType, 0, true));
                            matReceive.setRecCode(SessMatReceive.getCodeReceive(matReceive));
                            matReceive.setCurrencyId(1);
                            matReceive.setGondolaId(matDispatch.getGondolaId());
                            matReceive.setGondolaToId(matDispatch.getGondolaToId());

                            oid = PstMatReceive.insertExc(matReceive);

                            Vector listDispatchItem = new Vector();
                            listDispatchItem = PstMatDispatchItem.list(0, 0, matDispatch.getOID());
                            if (listDispatchItem != null && listDispatchItem.size() > 0) {
                                double totalBerat = 0;
                                for (int x = 0; x < listDispatchItem.size(); x++) {
                                    Vector temp = (Vector) listDispatchItem.get(x);
                                    MatDispatchItem dfItem = (MatDispatchItem) temp.get(0);
                                    MatReceiveItem matReceiveItem = new MatReceiveItem();
                                    matReceiveItem.setUnitId(dfItem.getUnitId());
                                    matReceiveItem.setQty(dfItem.getQty());
                                    matReceiveItem.setResidueQty(dfItem.getQty());
                                    matReceiveItem.setMaterialId(dfItem.getMaterialId());
                                    matReceiveItem.setReceiveMaterialId(oid);
                                    matReceiveItem.setCost(dfItem.getHpp());
                                    matReceiveItem.setCurrBuyingPrice(dfItem.getHpp());
                                    matReceiveItem.setTotal(matReceiveItem.getQty() * matReceiveItem.getCost());
                                    //update by de koyo
                                    matReceiveItem.setCurrencyId(matReceive.getCurrencyId());
                                    //dewok-2017
                                    matReceiveItem.setBerat(dfItem.getBeratCurrent());
                                    totalBerat += dfItem.getBeratCurrent();

                                    long oidInsertOtomatic = PstMatReceiveItem.insertExc(matReceiveItem);
                                    //update berat item material
                                    Vector<MaterialDetail> listMatDetail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = '" + dfItem.getMaterialId() + "'", "");
                                    if (!listMatDetail.isEmpty()) {
                                        MaterialDetail detail = (MaterialDetail) listMatDetail.get(0);
                                        detail.setBerat(dfItem.getBeratCurrent());
                                        PstMaterialDetail.updateExc(detail);
                                    }

                                    //add opie-eyek 20131206
                                    //untuk insert otomatis serial code dari transfer
                                    if (oidInsertOtomatic != 0) {
                                        Vector listSerialCode = new Vector();
                                        listSerialCode = PstDispatchStockCode.checkListStockCodeDispatch(dfItem.getOID());
                                        for (int c = 0; c < listSerialCode.size(); c++) {
                                            DispatchStockCode dispatchStockCode = (DispatchStockCode) listSerialCode.get(c);
                                            ReceiveStockCode receiveStockCode = new ReceiveStockCode();
                                            receiveStockCode.setReceiveMaterialItemId(oidInsertOtomatic);
                                            receiveStockCode.setStockCode(dispatchStockCode.getStockCode());
                                            receiveStockCode.setStockValue(dispatchStockCode.getStockValue());
                                            receiveStockCode.setReceiveMaterialId(oid);
                                            try {
                                                PstReceiveStockCode.insertExc(receiveStockCode);
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                }
                                //dewok-2017
                                matReceive.setBerat(totalBerat);
                                PstMatReceive.updateExc(matReceive);
                            }

                        }

//                        //comment out by PAK TUT
                         /*delete receiving if status document df back to draft
                         by mirahu
                         update 12032012 */
                        if (matDispatch.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && matDispatch.getLocationType() != 4 && autoRec.equals("Y")) {
                            if (oidMatDispatch != 0) {
                                oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);
                                if (oidRecMaterial != 0) {
                                    CtrlMatReceiveItem objCtlItem = new CtrlMatReceiveItem();
                                    MatReceiveItem objItem = new MatReceiveItem();
                                    String stWhereClose = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + oidRecMaterial;
                                    Vector vListItem = PstMatReceiveItem.list(0, 0, stWhereClose, "");
                                    if (vListItem != null && vListItem.size() > 0) {
                                        for (int i = 0; i < vListItem.size(); i++) {
                                            objItem = (MatReceiveItem) vListItem.get(i);
                                            objCtlItem.action(Command.DELETE, objItem.getOID(), oidRecMaterial);
                                        }
                                    }
                                    CtrlMatReceive objRec = new CtrlMatReceive();
                                    MatReceive objRecId = new MatReceive();
                                    String stWhereRec = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + " = " + oidRecMaterial;
                                    Vector vListRec = PstMatReceive.list(0, 0, stWhereRec, "");
                                    if (vListRec != null && vListRec.size() > 0) {
                                        for (int i = 0; i < vListRec.size(); i++) {
                                            objRecId = (MatReceive) vListRec.get(i);
                                            objRec.action(Command.DELETE, objRecId.getOID());
                                        }
                                    }
                                }
                            }
                        }

                        //posting jika statusnya final langsung di posting
//                        if(matDispatch.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
//                             SessPosting sessPosting = new SessPosting();
//                             boolean isOK = sessPosting.postedDispatchDoc(oidMatDispatch);
//                             if(isOK){
//                                 matDispatch.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
//                             }
//                             
//                        }
                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.updateDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidMatDispatch != 0) {
                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatDispatch != 0) {
                    try {
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatDispatch != 0) {
                    try {

                        /**
                         * by mirahu proses penghapusan di pos dispatch receive
                         * item
                         */
                        String whereClauseTransferUnit = PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatch;
                        Vector vect2 = PstMatDispatchReceiveItem.list(0, 0, whereClauseTransferUnit, "");
                        if (vect2 != null && vect2.size() > 0) {
                            for (int k = 0; k < vect2.size(); k++) {
                                MatDispatchReceiveItem matDispatchReceiveItem = (MatDispatchReceiveItem) vect2.get(k);
                                CtrlMatDispatchReceiveItem ctrlMatDpsRecItm = new CtrlMatDispatchReceiveItem();
                                ctrlMatDpsRecItm.action(Command.DELETE, matDispatchReceiveItem.getOID(), 0, oidMatDispatch, 0, -1);
                            }
                        }
                        /*End Of delete pos dispatch receive item*/

                        String whereClause = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatch;
                        Vector vect = PstMatDispatchItem.list(0, 0, whereClause, "");
                        if (vect != null && vect.size() > 0) {
                            for (int k = 0; k < vect.size(); k++) {
                                MatDispatchItem matDispatchItem = (MatDispatchItem) vect.get(k);
                                CtrlMatDispatchItem ctrlMatDpsItm = new CtrlMatDispatchItem();
                                ctrlMatDpsItm.action(Command.DELETE, matDispatchItem.getOID(), oidMatDispatch);
                            }
                        }

                        /**
                         * gadnyan proses penghapusan di doc logger jika tidak
                         * di perlukan uncoment perintah ini
                         */
                        matDispatch = PstMatDispatch.fetchExc(oidMatDispatch);
                        PstDocLogger.deleteDataBo_inDocLogger(matDispatch.getDispatchCode(), I_DocType.MAT_DOC_TYPE_DF);
                        // -- end

                        /*
                         * Deleting Doc receive Material if Dispatch Unit was deleting
                         * By Mirahu
                         */
                        if (matDispatch.getLocationType() == 4) {
                            long oidRecMaterial = PstMatReceive.getOidReceiveMaterial(oidMatDispatch);
                            //MatReceive matReceive = new MatReceive();
                            //matReceive = PstMatReceive.fetchExc(oidRecMaterial);
                            long oidRec = PstMatReceive.deleteExc(oidRecMaterial);
                            if (oidRec != 0) {
                                msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                                excCode = RSLT_OK;
                            } else {
                                msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                                excCode = RSLT_FORM_INCOMPLETE;
                            }
                        }
                        /*
                         * End of Deleting Doc receive Material
                         */

                        long oid = PstMatDispatch.deleteExc(oidMatDispatch);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;

                            //by dyas
                            //tambah try catch
                            //untuk memanggil methods insertHistory
                            try {
                                insertHistory(userID, nameUser, cmd, oid);
                            } catch (Exception e) {

                            }
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        System.out.println("exception dbexc : " + dbexc.toString());
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        System.out.println("exception exc : " + exc.toString());
                    }
                }
                break;

            default:
                break;
        }
        return rsCode;
    }

    //by dyas
    //tambah methods insertHistory
    //digunakan untuk men-set data yang nantinya akan diinputkan ke logHistory
    public void insertHistory(long userID, String nameUser, int cmd, long oid) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("material/dispatch/df_stock_wh_material_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(matDispatch.getDispatchCode());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(this.matDispatch.getLogDetail(prevMatDispatch));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    public void insertHistoryMatReceive(long userID, String nameUser, int cmd, long oid, MatReceive rec, MatReceive prevRec) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("material/dispatch/df_stock_wh_material_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(rec.getRecCode());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(rec.getLogDetail(prevRec));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    //added by dewok 2018-03-13
    public void insertHistoryMaterial(long userID, String nameUser, int cmd, long oid, Material material, String historyChanged, String action) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("warehouse/material/dispatch/df_stock_wh_material_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType("Pos material");
            logSysHistory.setLogUserAction(action);
            logSysHistory.setLogDocumentNumber(material.getFullName());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(historyChanged);

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
}
