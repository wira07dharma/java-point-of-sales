package com.dimata.posbo.form.warehouse;

/* java package */
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import java.util.*;
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
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Color;
import com.dimata.posbo.entity.masterdata.Kadar;

/*import payment terms */
/*import VendorPrice */
import com.dimata.posbo.entity.masterdata.PstMatVendorPrice;
import com.dimata.posbo.entity.masterdata.MatVendorPrice;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialDetail;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstColor;
import com.dimata.posbo.entity.masterdata.PstKadar;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialDetail;
import com.dimata.posbo.entity.masterdata.PstMerk;
import com.dimata.posbo.form.masterdata.CtrlMaterial;

/**
 * import system Property *
 */
import com.dimata.system.entity.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CtrlMatReceive extends Control implements I_Language {

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
    private MatReceive matReceive;
    private MatReceive prevMatReceive;
    private PstMatReceive pstMatReceive;
    private FrmMatReceive frmMatReceive;
    private HttpServletRequest req;
    Date dateLog = new Date();
    int language = LANGUAGE_DEFAULT;
    private long newOid = 0;

    public CtrlMatReceive() {
    }

    public CtrlMatReceive(HttpServletRequest request) {
        msgString = "";
        matReceive = new MatReceive();
        try {
            pstMatReceive = new PstMatReceive(0);
        } catch (Exception e) {
            ;
        }
        req = request;
        frmMatReceive = new FrmMatReceive(request, matReceive);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmMatReceive.addError(frmMatReceive.FRM_FIELD_RECEIVE_MATERIAL_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public MatReceive getMatReceive() {
        return matReceive;
    }

    public FrmMatReceive getForm() {
        return frmMatReceive;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public long getNewOid() {
        return newOid;
    }

    public void setNewOid(long newOid) {
        this.newOid = newOid;
    }

    public int action(int cmd, long oidMatReceive) {
        return action(cmd, oidMatReceive, "", 0);
    }
    
    public int action(int cmd, long oidMatReceive, String nameUser, long userID) {
        return action(cmd, oidMatReceive, nameUser, userID, null);
    }

    public int action(int cmd, long oidMatReceive, String nameUser, long userID, HttpServletRequest request) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;
            case Command.SAVE:
                boolean incrementAllReceiveType = true;
                Date rDate = new Date();
                int recType = 0;
                int counter = 0;
                if (oidMatReceive != 0) {
                    try {
                        matReceive = PstMatReceive.fetchExc(oidMatReceive);
                        rDate = matReceive.getReceiveDate();
                        recType = matReceive.getReceiveStatus();
                        counter = matReceive.getRecCodeCnt();
                    } catch (Exception exc) {
                        System.out.println("Exc. when fetch  MatReceive: " + exc.toString());
                    }
                    try {
                        prevMatReceive = PstMatReceive.fetchExc(oidMatReceive);
                    } catch (Exception exc) {
                    }

                }

                frmMatReceive.requestEntityObject(matReceive);
                Date date = ControlDate.getDateTime(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], req);
                if (date == null) {
                    date = FRMQueryString.requestDate(req, "" + FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE] + "");
                }
                matReceive.setReceiveDate(date);

                int docType = -1;
                try {
                    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
                    docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
                } catch (Exception e) {
                    System.out.println("Exc : " + e.toString());
                }

                if (frmMatReceive.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                // set currency id untuk penerimaan antar lokasi
                CurrencyType defaultCurrencyType = new CurrencyType();
                if (this.matReceive.getSupplierId() == 0 && this.matReceive.getCurrencyId() == 0) {
                    defaultCurrencyType = PstCurrencyType.getDefaultCurrencyType();
                    this.matReceive.setCurrencyId(defaultCurrencyType.getOID());
                }

                /**
                 * Untuk mendapatkan besarnya standard rate per satuan default
                 * (:currency rate = 1) yang digunakan untuk nilai pada
                 * transaksi rate create by gwawan@dimata 16 Agutus 2007 di
                 * disable oleh opie, karena untuk rate sudah di tentukan di jsp
                 * nya
                 */
                /*if(this.matReceive.getCurrencyId() != 0) {
                 String whereClause = PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+" = "+this.matReceive.getCurrencyId();
                 whereClause += " AND "+PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS]+" = "+PstStandartRate.ACTIVE;
                 Vector listStandardRate = PstStandartRate.list(0, 1, whereClause, "");
                 StandartRate objStandartRate = (StandartRate)listStandardRate.get(0);
                 this.matReceive.setTransRate(objStandartRate.getSellingRate());
                 }
                 else {
                 this.matReceive.setTransRate(0);
                 }*/
                if (matReceive.getOID() == 0) {
                    try {
                        this.matReceive.setRecCodeCnt(SessMatReceive.getIntCode(matReceive, rDate, oidMatReceive, docType, counter, incrementAllReceiveType));
                        this.matReceive.setRecCode(SessMatReceive.getCodeReceive(matReceive));
                        matReceive.setLastUpdate(new Date());
                        //otomatis include ppn
                        double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
                        if (defaultPpn != 0) {
                            matReceive.setIncludePpn(1);
                        }
                        long oid = pstMatReceive.insertExc(this.matReceive);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
                        //PROSES UNTUK MENYIMPAN HISTORY JIKA OID DARI ORDER PO =! 0
                        if (oid != 0) {
                            this.newOid = oid;
                            insertHistory(userID, nameUser, cmd, oid);
                        }
//
//
//                        /**
//                         * gadnyana
//                         * untuk insert ke doc logger
//                         * jika tidak di perlukan uncomment
//                         */
//                        //PstDocLogger.insertDataBo_toDocLogger(matReceive.getRecCode(), I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR, matReceive.getLastUpdate(), matReceive.getRemark());
//                        //--- end
//                        System.out.println("action comlpete....");
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        System.out.println("exception: " + exc.toString());
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        matReceive.setLastUpdate(new Date());
                        //update kode penerimaan by dewok 2018-02-12
//                        try {
//                            Location newLoc = PstLocation.fetchExc(matReceive.getLocationId());
//                            String lastCode = matReceive.getRecCode();
//                            String splitCode[] = lastCode.split("-");
//                            String newCode = newLoc.getCode()+"-"+splitCode[1]+"-"+splitCode[2]+"-"+splitCode[3];
//                            matReceive.setRecCode(newCode);
//                        } catch (Exception e) {
//                            System.out.println(e.getMessage());
//                        }
                        
                        long oid = pstMatReceive.updateExc(this.matReceive);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                        this.newOid = oid;

                        //added by dewok for litama
                        int typeOfBusinessDetail = Integer.valueOf(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));
                        if (typeOfBusinessDetail == 2) {
                            if (matReceive.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                                
                                //add by gunadi untuk sorting item
                                if (matReceive.getReceiveSource() == PstMatReceive.SOURCE_FROM_BUYBACK){
                                    /* ambil material item */
                                    String whereRecItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "="+oid;
                                    Vector<MatReceiveItem> listItem = PstMatReceiveItem.list(0, 0, whereRecItem, "");
                                    
                                    /* buat material baru */
                                    if (!listItem.isEmpty()){
                                        
                                        Vector<MatDispatchItem> listItemDispatch = new Vector<MatDispatchItem>();
                                        for (MatReceiveItem ri : listItem){
                                            try {
                                                long oidNewMaterial = 0;
                                                if (ri.getPrevMaterialId() == 0 && ri.getSortStatus() == PstMatReceiveItem.SORT_DIRECT_SALES){
                                                    Material prevMaterial = new Material();
                                                    Material material = new Material();
                                                    if (ri.getMaterialId() > 0 && PstMaterial.checkOID(ri.getMaterialId())){
                                                        prevMaterial = PstMaterial.fetchExc(ri.getMaterialId());
                                                        material = PstMaterial.fetchExc(ri.getMaterialId());
                                                    }
                                                    String codeSku = "";
                                                    if (material.getKepemilikanId() != 0) {
                                                        ContactList contactList = PstContactList.fetchExc(material.getKepemilikanId());
                                                        codeSku = contactList.getContactCode();
                                                    }
                                                    if (material.getCategoryId() != 0) {
                                                        Category category = PstCategory.fetchExc(material.getCategoryId());
                                                        codeSku = codeSku + category.getCode();
                                                    }
                                                    if (material.getMerkId() != 0) {
                                                        Merk merk = PstMerk.fetchExc(material.getMerkId());
                                                        codeSku = codeSku + merk.getCode();
                                                    }
                                                    if (material.getPosKadar() != 0) {
                                                        Kadar kadar = PstKadar.fetchExc(material.getPosKadar());
                                                        codeSku = codeSku + kadar.getKodeKadar();
                                                    }
                                                    if (material.getSupplierId() != 0) {
                                                        ContactList contactList = PstContactList.fetchExc(material.getSupplierId());
                                                        codeSku = codeSku + contactList.getContactCode();
                                                    }
                                                    if (material.getPosColor() != 0) {
                                                        Color color = PstColor.fetchExc(material.getPosColor());
                                                        codeSku = codeSku + color.getColorCode();
                                                    }
                                                    String year = Formater.formatDate(new Date(), "yyyy");
													year = year.substring(year.length() - 2, year.length());
													int lastCounter = PstMaterial.getLastCounterLitamaByYear(year);
													String lastCode = PstMaterial.getCounterLitama(lastCounter);
													material.setSku(codeSku + year + lastCode);
													material.setBarCode(material.getSku());
                                                    material.setDefaultCost(ri.getTotal());
                                                    material.setAveragePrice(ri.getTotal());
                                                    material.setMaterialDescription(ri.getRemark());
													material.setLastUpdate(new Date());
													try {
														oidNewMaterial = PstMaterial.insertExc(material);
													} catch (Exception exc){
														System.out.println(exc.toString());
													}
                                                    
                                                    Vector vlocation = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                                    Periode maPeriode = PstPeriode.getPeriodeRunning();
                                                    CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
                                                    ctrlMaterial.setMinimumStockPerLocation(vlocation, oidNewMaterial, 0, maPeriode.getOID(), userID, nameUser, request);

                                                    /* buat material detail */
                                                    Vector listMatDetail = PstMaterialDetail.list(0, 0, PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID]+"="+prevMaterial.getOID(), "");
                                                    if (!listMatDetail.isEmpty()){
                                                        MaterialDetail matDetail = new MaterialDetail();
                                                        matDetail.setBerat(ri.getBerat());
                                                        matDetail.setHargaBeli(ri.getCost());
                                                        matDetail.setMaterialId(oidNewMaterial);
                                                        matDetail.setOngkos(ri.getForwarderCost());
                                                        matDetail.setQty(1);

                                                        long oidNewMatDetail = PstMaterialDetail.insertExc(matDetail);
                                                    }

                                                    ri.setMaterialId(oidNewMaterial);
                                                    ri.setPrevMaterialId(prevMaterial.getOID());

                                                    PstMatReceiveItem.updateExc(ri);
                                                } else {
                                                    oidNewMaterial = ri.getMaterialId();
                                                }

                                                if (ri.getSortStatus() == PstMatReceiveItem.SORT_LEBUR){
                                                    MatDispatchItem matDispatchItem = new MatDispatchItem();
                                                    matDispatchItem.setMaterialId(oidNewMaterial);
                                                    matDispatchItem.setUnitId(ri.getUnitId());
                                                    matDispatchItem.setQty(1);
                                                    matDispatchItem.setHpp(ri.getCost());
                                                    matDispatchItem.setOngkos(ri.getForwarderCost());
                                                    matDispatchItem.setHppTotal(ri.getTotal());
                                                    matDispatchItem.setBeratCurrent(ri.getBerat());
                                                    listItemDispatch.add(matDispatchItem);
                                                }
                                                
                                            } catch (Exception exc){}
                                        }
                                        
                                        if (!listItemDispatch.isEmpty()){
                                            /* buat dokumen dispatch */
                                            MatDispatch matDispatch = new MatDispatch();
                                            matDispatch.setLocationId(matReceive.getLocationId());
                                            matDispatch.setDispatchTo(matReceive.getLocationId());
                                            matDispatch.setLocationType(PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR);
                                            matDispatch.setDispatchDate(new Date());
                                            matDispatch.setDispatchStatus(0);
                                            matDispatch.setRemark("Automatic transfer lebur process for buyback number : "+matReceive.getRecCode());
                                            int maxCounter = SessMatDispatch.getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
                                            maxCounter = maxCounter + 1;
                                            matDispatch.setDispatchCodeCounter(maxCounter);
                                            matDispatch.setDispatchCode(SessMatDispatch.generateDispatchCode(matDispatch));
                                            matDispatch.setLast_update(new Date());
                                            matDispatch.setDispatchItemType(matReceive.getReceiveItemType());
                                            matDispatch.setReceiveMatId(oid);

                                            long oidMatDispatch = PstMatDispatch.insertExc(matDispatch);
                                            
                                            /* buat dokumen receive */
                                            MatReceive matReceive = new MatReceive();
                                            matReceive.setLocationId(matDispatch.getDispatchTo());
                                            matReceive.setLocationType(PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR);
                                            matReceive.setReceiveDate(matDispatch.getDispatchDate());
                                            matReceive.setReceiveStatus(matDispatch.getDispatchStatus());
                                            matReceive.setRemark("Automatic Receive process from transfer lebur number : " + matDispatch.getDispatchCode());
                                            matReceive.setReceiveFrom(matDispatch.getLocationId());
                                            matReceive.setRecCode(matDispatch.getDispatchCode());
                                            matReceive.setRecCodeCnt(matDispatch.getDispatchCodeCounter());
                                            matReceive.setDispatchMaterialId(matDispatch.getOID());
                                            matReceive.setReceiveItemType(matDispatch.getDispatchItemType());
                                            long oidReceive = PstMatReceive.insertExc(matReceive);
                                            
                                            
                                            for (MatDispatchItem di : listItemDispatch){
                                                try {
                                                    di.setDispatchMaterialId(oidMatDispatch);
                                                    //long oidDispatchItem = PstMatDispatchItem.insertExc(di);
                                                    
                                                    MatReceiveItem matRecItem = new MatReceiveItem();
                                                    matRecItem.setReceiveMaterialId(oidReceive);
                                                    
                                                    //long oidRecItem = PstMatReceiveItem.insertExc(matRecItem);
                                                    
                                                    long groupId = com.dimata.qdep.db.OIDFactory.generateOID();
                                                    MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
                                                    matDispatchReceiveItem.setDfRecGroupId(groupId);
                                                    matDispatchReceiveItem.setDispatchMaterialId(oidMatDispatch);
                                                    matDispatchReceiveItem.setTargetItem(matRecItem);
                                                    matDispatchReceiveItem.setSourceItem(di);
                                                    PstMatDispatchReceiveItem.insertExc(matDispatchReceiveItem);
                                                } catch (Exception exc){}
                                            }
                                        }
                                    }
                                }
                                
                                //cari etalase/gondola tujuan
                                Vector<MatDispatchItem> listDispatchItem = PstMatDispatchItem.list(0, 0, ""
                                        + "" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + " = '" + matReceive.getDispatchMaterialId() + "'"
                                        + " AND " + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_GONDOLA_TO_ID] + " <> 0"
                                        + "", "");
                                if (!listDispatchItem.isEmpty()) {
                                    MatDispatch dispatch = PstMatDispatch.fetchExc(matReceive.getDispatchMaterialId());
                                    
                                    if (dispatch.getLocationType() == PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE) {
                                        for (MatDispatchItem md : listDispatchItem) {
                                            try {
                                                Material prevMaterial = new Material();
                                                Material material = new Material();
                                                if (md.getMaterialId() > 0 && PstMaterial.checkOID(md.getMaterialId())) {
                                                    prevMaterial = PstMaterial.fetchExc(md.getMaterialId());
                                                    material = PstMaterial.fetchExc(md.getMaterialId());
                                                }
                                                material.setGondolaCode(md.getGondolaToId());
                                                PstMaterial.updateExc(material);
                                                //save history
                                                insertHistoryMaterial(userID, nameUser, cmd, material.getOID(), prevMaterial, material);
                                            } catch (Exception e) {
                                                System.out.println(e.getMessage());
                                            }
                                        }
                                    }
                                    
                                    if (dispatch.getLocationType() == PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR) {
                                        long gondolaTujuan = listDispatchItem.get(0).getGondolaToId();
                                        if (gondolaTujuan != 0) {
                                            //cari semua item yg di transfer
                                            Vector<MatReceiveItem> listRecItem = PstMatReceiveItem.list(0, 0, "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = '" + matReceive.getOID() + "'", "");
                                            for (MatReceiveItem mri : listRecItem) {
                                                try {
                                                    Material prevMaterial = new Material();
                                                    Material material = new Material();
                                                    if (mri.getMaterialId() > 0 && PstMaterial.checkOID(mri.getMaterialId())) {
                                                        prevMaterial = PstMaterial.fetchExc(mri.getMaterialId());
                                                        material = PstMaterial.fetchExc(mri.getMaterialId());
                                                    }
                                                    material.setGondolaCode(gondolaTujuan);
                                                    PstMaterial.updateExc(material);
                                                    //save history
                                                    insertHistoryMaterial(userID, nameUser, cmd, material.getOID(), prevMaterial, material);
                                                } catch (Exception e) {
                                                    System.out.println(e.getMessage());
                                                }
                                            }
                                        }
                                    }
                                    
                                }
                            }
                        }

//                        //proses posting
//                        int typeOfBussiness = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS"));
//                        
//                        //typer retail
//                            if(typeOfBussiness==I_ApplicationType.APPLICATION_DISTRIBUTIONS){
//                               SessPosting sessPosting = new SessPosting();
//                               switch (matReceive.getReceiveStatus()) {
//                                    case I_DocStatus.DOCUMENT_STATUS_APPROVED:
//                                            boolean isOKP =  sessPosting.postedQtyReceiveOnlyDoc(oid);
//                                            if(isOKP){
//                                               matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_APPROVED);     
//                                            }
//                                        break;
//                                     case I_DocStatus.DOCUMENT_STATUS_FINAL:
//                                            boolean isOK = sessPosting.postedValueReceiveOnlyDoc(oid);
//                                            if(isOK){
//                                               matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);     
//                                            }
//                                        break;
//
//                                    default:
//                                       // break;
//                               }
//                            //maka statusnya final = posting value
//                            }else{
//                                //type kecuali retail, klo final langsung posting
//                                if(matReceive.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
//                                      SessPosting sessPosting = new SessPosting();
//                                      boolean isOK = sessPosting.postedReceiveDoc(oid);
//                                      if(isOK){
//                                               matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);     
//                                      }
//                                }
//                            }

                        if (oid != 0) {
                            insertHistory(userID, nameUser, cmd, oid);
                        }

                        double ppn = matReceive.getTotalPpn();
                        int includePpn = matReceive.getIncludePpn();

                        //disini update jika new supplier ga perlu di update
                        long mappingNewSupplier = 0;
                        try {
                            mappingNewSupplier = Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));
                        } catch (Exception ex) {
                        }

                        if (mappingNewSupplier != matReceive.getSupplierId()) {
                            if (matReceive.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {

                                //update Ppn di Vendor Price
                                CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem();
                                Vector ListMatReceiveItem = new Vector();
                                String whereClauseItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oid;
                                ListMatReceiveItem = PstMatReceiveItem.list(0, 0, whereClauseItem, "");
                                for (int l = 0; l < ListMatReceiveItem.size(); l++) {
                                    MatReceiveItem matReceiveItem = new MatReceiveItem();
                                    matReceiveItem = (MatReceiveItem) ListMatReceiveItem.get(l);
                                    long oidMatReceivePpn = matReceiveItem.getReceiveMaterialId();
                                    long oidMatReceiveItemPpn = matReceiveItem.getOID();
                                    //long oidMaterial = matReceiveItem.getMaterialId();

                                    double newCost = matReceiveItem.getCost();
                                    double lastDisc = matReceiveItem.getDiscount();
                                    double lastDisc2 = matReceiveItem.getDiscount2();
                                    double lastDiscNom = matReceiveItem.getDiscNominal();
                                    double newForwarderCost = matReceiveItem.getForwarderCost();
                                    //+ppn
                                    //double Ppn = matReceive.getTotalPpn();

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
                                    matVdrPrc.setLastVat(ppn);

                                    //calculate hpp & discount
                                    // By Mirahu 25 Februari2011
                                    double totalDiscount = newCost * lastDisc / 100;
                                    double totalMinus = newCost - totalDiscount;
                                    double totalDiscount2 = totalMinus * lastDisc2 / 100;
                                    double totalCost = (totalMinus - totalDiscount2) - lastDiscNom;
                                     //update cost + PPn by Mirahu 25 Mei 2011
                                    //double totalCostAll = totalCost + newForwarderCost;
                                    //matVdrPrc.setCurrBuyingPrice(totalCostAll);

                                    //include or not include
                                    double valuePpn = 0.0;
                                    double totalCostAll = 0.0;

                                    if (includePpn == PstMatReceive.INCLUDE_PPN) {
                                        valuePpn = totalCost - (totalCost / 1.1);
                                        totalCostAll = totalCost + newForwarderCost;
                                        matVdrPrc.setLastVat(0);
                                    } else if (includePpn == PstMatReceive.EXCLUDE_PPN) {
                                        valuePpn = totalCost * ppn / 100;
                                        double totalCostAllPpn = totalCost + valuePpn;
                                        totalCostAll = totalCostAllPpn + newForwarderCost;
                                    }
                                    matVdrPrc.setCurrBuyingPrice(totalCostAll);

                                    PstMatVendorPrice.insertUpdateExc(matVdrPrc);

                                }
                            }
                        }

                        //update ppn di VendorPrice
                        /**
                         * gadnyana untuk insert ke doc logger jika tidak di
                         * perlukan uncomment
                         */
                        PstDocLogger.updateDataBo_toDocLogger(matReceive.getRecCode(), I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR, matReceive.getLastUpdate(), matReceive.getRemark());
                        //--- end

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                /**
                 * set status pada forwarder info dengan value terkini!
                 */
                try {
                    String whereClause = "" + PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID] + "=" + this.matReceive.getOID();
                    Vector vctListFi = PstForwarderInfo.list(0, 0, whereClause, "");
                    ForwarderInfo forwarderInfo = new ForwarderInfo();
                    for (int j = 0; j < vctListFi.size(); j++) {
                        forwarderInfo = (ForwarderInfo) vctListFi.get(j);
                        forwarderInfo.setStatus(this.matReceive.getReceiveStatus());
                        long oid = PstForwarderInfo.updateExc(forwarderInfo);
                    }
                } catch (Exception e) {
                    System.out.println("Exc in update status, forwarder_info >>> " + e.toString());
                }

                break;

            case Command.EDIT:
                if (oidMatReceive != 0) {
                    try {
                        matReceive = PstMatReceive.fetchExc(oidMatReceive);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidMatReceive != 0) {
                    try {
                        matReceive = PstMatReceive.fetchExc(oidMatReceive);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidMatReceive != 0) {
                    try {
                        // memproses item penerimaan barang
                        CtrlMatReceiveItem objCtlItem = new CtrlMatReceiveItem();
                        MatReceiveItem objItem = new MatReceiveItem();
                        String stWhereClose = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + oidMatReceive;
                        Vector vListItem = PstMatReceiveItem.list(0, 0, stWhereClose, "");
                        if (vListItem != null && vListItem.size() > 0) {
                            for (int i = 0; i < vListItem.size(); i++) {
                                objItem = (MatReceiveItem) vListItem.get(i);
                                objCtlItem.action(Command.DELETE, objItem.getOID(), oidMatReceive);

                                //delete serial code penerimaan dari toko/gudang jika statusnya di balik dari final ke draft
                            }
                        }

                        /**
                         * gadnyan proses penghapusan di doc logger jika tidak
                         * di perlukan uncoment perintah ini
                         */
                        matReceive = PstMatReceive.fetchExc(oidMatReceive);
                        PstDocLogger.deleteDataBo_inDocLogger(matReceive.getRecCode(), I_DocType.MAT_DOC_TYPE_LMRR);
                        // -- end

                        /**
                         * delete forwarder information berdasarkan dok. receive
                         */
                        String whereClause = "" + PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID] + "=" + matReceive.getOID();
                        Vector vctListFi = PstForwarderInfo.list(0, 0, whereClause, "");
                        ForwarderInfo forwarderInfo = new ForwarderInfo();
                        CtrlForwarderInfo ctrlForwarderInfo = new CtrlForwarderInfo();
                        for (int j = 0; j < vctListFi.size(); j++) {
                            forwarderInfo = (ForwarderInfo) vctListFi.get(j);
                            ctrlForwarderInfo.action(Command.DELETE, forwarderInfo.getOID());
                        }

                        /**
                         * delete receive
                         */
                        long oid = PstMatReceive.deleteExc(oidMatReceive);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
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
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            default:

        }
        return rsCode;
    }

    public void insertHistory(long userID, String nameUser, int cmd, long oid) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("material/receive/receive_wh_supp_po_material_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(matReceive.getRecCode());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(this.matReceive.getLogDetail(prevMatReceive));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
    
    public void insertHistoryMaterial(long userID, String nameUser, int cmd, long oid, Material prevMaterial, Material newMaterial) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("material/receive/receive_wh_supp_po_material_edit.jsp");
            logSysHistory.setLogUpdateDate(dateLog);
            logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
            logSysHistory.setLogUserAction(Command.commandString[cmd]);
            logSysHistory.setLogDocumentNumber(matReceive.getRecCode());
            logSysHistory.setLogDocumentId(oid);            
            logSysHistory.setLogDetail(newMaterial.getLogDetail(prevMaterial));

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }
}
