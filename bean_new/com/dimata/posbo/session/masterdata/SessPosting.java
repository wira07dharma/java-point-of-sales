package com.dimata.posbo.session.masterdata;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.qdep.entity.I_DocStatus; 
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.util.Formater;

//replaced by widi
import com.dimata.pos.entity.billing.*;

import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrder;
import com.dimata.posbo.entity.purchasing.PurchaseOrderItem;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.session.warehouse.SessMatReceive;
import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.common.entity.payment.PstStandartRate;

import com.dimata.posbo.session.warehouse.SessStockCard;

//import matDispatchReceiveItem
import com.dimata.posbo.entity.warehouse.MatDispatchReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatDispatchReceiveItem;

//import cash cashier
import com.dimata.pos.entity.balance.*;

//import system property
import com.dimata.common.entity.system.*;

//import costing
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.entity.warehouse.MatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.posbo.entity.warehouse.MatCosting;
import com.dimata.posbo.session.warehouse.SessMatCosting;
import com.dimata.posbo.session.warehouse.SessMatCostingStokFisik;
import com.dimata.posbo.session.warehouse.SessProduction;

import java.util.Date;
import java.util.Vector;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONObject;

public class SessPosting {
    // Document Type in posting
    public static final int DOC_TYPE_RECEIVE = 0;
    public static final int DOC_TYPE_RETURN = 1;
    public static final int DOC_TYPE_DISPATCH = 2;
    public static final int DOC_TYPE_SALE_REGULAR = 3;
    public static final int DOC_TYPE_SALE_COMPOSIT = 4;
    public static final int DOC_TYPE_COSTING = 5;
    public static final int DOC_TYPE_SALE_RETURN = 6;
    public static final int DOC_TYPE_FORWARDER = 7;
    public static final int DOC_TYPE_STOCK = 8;
    public static final int DOC_TYPE_PRODUCTION = 9;
     
    // Mode in accessing MaterialStock
    public static final int MODE_UPDATE = 0;
    public static final int MODE_INSERT = 1;

    /** Holds value of property vListUnPostedLGRDoc. */
    private Vector vListUnPostedLGRDoc;

    /** Holds value of property vListUnPostedReturnDoc. */
    private Vector vListUnPostedReturnDoc;

    /** Holds value of property vListUnPostedDFDoc. */
    private Vector vListUnPostedDFDoc;

    /** Holds value of property vListUnPostedSalesDoc. */
    private Vector vListUnPostedSalesDoc;

    /** Holds value of property vListUnPostedCostDoc. */
    private Vector vListUnPostedCostDoc;

    /** Getter for property vListUnPostedLGRDoc.
     * @return Value of property vListUnPostedLGRDoc.
     *
     */
    public Vector getVListUnPostedLGRDoc() {
        return this.vListUnPostedLGRDoc;
    }

    /** Setter for property vListUnPostedLGRDoc.
     * @param vListUnPostedLGRDoc New value of property vListUnPostedLGRDoc.
     *
     */
    public void setVListUnPostedLGRDoc(Vector vListUnPostedLGRDoc) {
        this.vListUnPostedLGRDoc = vListUnPostedLGRDoc;
    }

    /** Getter for property vListUnPostedReturnDoc.
     * @return Value of property vListUnPostedReturnDoc.
     *
     */
    public Vector getVListUnPostedReturnDoc() {
        return this.vListUnPostedReturnDoc;
    }

    /** Setter for property vListUnPostedReturnDoc.
     * @param vListUnPostedReturnDoc New value of property vListUnPostedReturnDoc.
     *
     */
    public void setVListUnPostedReturnDoc(Vector vListUnPostedReturnDoc) {
        this.vListUnPostedReturnDoc = vListUnPostedReturnDoc;
    }

    /** Getter for property vListUnPostedDFDoc.
     * @return Value of property vListUnPostedDFDoc.
     *
     */
    public Vector getVListUnPostedDFDoc() {
        return this.vListUnPostedDFDoc;
    }

    /** Setter for property vListUnPostedDFDoc.
     * @param vListUnPostedDFDoc New value of property vListUnPostedDFDoc.
     *
     */
    public void setVListUnPostedDFDoc(Vector vListUnPostedDFDoc) {
        this.vListUnPostedDFDoc = vListUnPostedDFDoc;
    }

    /** Getter for property vListUnPostedSalesDoc.
     * @return Value of property vListUnPostedSalesDoc.
     *
     */
    public Vector getVListUnPostedSalesDoc() {
        return this.vListUnPostedSalesDoc;
    }

    /** Setter for property vListUnPostedSalesDoc.
     * @param vListUnPostedSalesDoc New value of property vListUnPostedSalesDoc.
     *
     */
    public void setVListUnPostedSalesDoc(Vector vListUnPostedSalesDoc) {
        this.vListUnPostedSalesDoc = vListUnPostedSalesDoc;
    }

    /** Getter for property vListUnPostedCostDoc.
     * @return Value of property vListUnPostedCostDoc.
     *
     */
    public Vector getVListUnPostedCostDoc() {
        return this.vListUnPostedCostDoc;
    }

    /** Setter for property vListUnPostedCostDoc.
     * @param vListUnPostedCostDoc New value of property vListUnPostedCostDoc.
     *
     */
    public void setVListUnPostedCostDoc(Vector vListUnPostedCostDoc) {
        this.vListUnPostedCostDoc = vListUnPostedCostDoc;
    }

    /**
     * @param postingDate
     * @param oidLocation
     * @return
     */
     synchronized public boolean postingTransDocument(Date postingDate, long oidLocation, boolean postedDateCheck) {
         boolean bPostingOK = false;
        Periode objPeriode = PstPeriode.getPeriodeRunning();
        long oidPeriode = objPeriode.getOID();

         // Process Dispatch
        Vector vUnPostedDFDoc = getUnPostedDFDocument(postingDate, oidLocation, oidPeriode, postedDateCheck);
        this.vListUnPostedDFDoc = vUnPostedDFDoc;
        System.out.println("vListUnPostedDFDoc : "+vListUnPostedDFDoc);

        // Process Receive
        Vector vUnPostedLGRDoc = getUnPostedLGRDocument(postingDate, oidLocation, oidPeriode, postedDateCheck);
        this.vListUnPostedLGRDoc = vUnPostedLGRDoc;
        System.out.println("vListUnPostedLGRDoc : "+vListUnPostedLGRDoc);

        // Process Return
        Vector vUnPostedReturnDoc = getUnPostedReturnDocument(postingDate, oidLocation, oidPeriode, postedDateCheck);
        this.vListUnPostedReturnDoc = vUnPostedReturnDoc;
        System.out.println("vListUnPostedReturnDoc : "+vListUnPostedReturnDoc);

        // Process Cost
       /* Vector vUnPostedCostDoc = getUnPostedCostDocument(postingDate, oidLocation, oidPeriode, postedDateCheck);
        this.vListUnPostedCostDoc = vUnPostedCostDoc;
        System.out.println("vListUnPostedCostDoc : "+vListUnPostedCostDoc);*/

        // Process Sale
        Vector vUnPostedSalesDoc = getUnPostedSalesDocument(postingDate, oidLocation, oidPeriode, postedDateCheck);
        //getUnPostedSalesDocument(postingDate, oidLocation, oidPeriode, postedDateCheck, DOC_TYPE_SALE_RETURN);
	
        this.vListUnPostedSalesDoc = vUnPostedSalesDoc;
        System.out.println("vListUnPostedSalesDoc : "+vListUnPostedSalesDoc);

         // Process Cost
        Vector vUnPostedCostDoc = getUnPostedCostDocument(postingDate, oidLocation, oidPeriode, postedDateCheck);
        this.vListUnPostedCostDoc = vUnPostedCostDoc;
        System.out.println("vListUnPostedCostDoc : "+vListUnPostedCostDoc);
        
        // Process Opname

        if (((vUnPostedLGRDoc != null && vUnPostedLGRDoc.size() > 0)
                || (vListUnPostedReturnDoc != null && vListUnPostedReturnDoc.size() > 0)
                || (vListUnPostedDFDoc != null && vListUnPostedDFDoc.size() > 0)
                || (vListUnPostedCostDoc != null && vListUnPostedCostDoc.size() > 0)
                || (vListUnPostedSalesDoc != null && vListUnPostedSalesDoc.size() > 0))) {
            return true;
        }

        return bPostingOK;
    }


    private Vector getTimeDurationOfTransDocument(long oidPeriode) {
        Vector vResult = new Vector(1, 1);

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        try {
            Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
            dStartDatePeriod = objMaterialPeriode.getStartDate();
            dEndDatePeriod = objMaterialPeriode.getEndDate();
        } catch (Exception e) {
            System.out.println("Exc " + new SessPosting().getClass().getName() + ".PostingSaleWithoutItem() - fetch period : " + e.toString());
        }

        int iDayOfShiftInterval = 0;
        String sStartTime = "";
        String sEndTime = "";
        Vector vListShift = PstShift.list(0, 0, "", PstShift.fieldNames[PstShift.FLD_START_TIME]);
        
        if (vListShift != null && vListShift.size() > 0) {
            int iShiftCount = vListShift.size();
            for (int i = 0; i < iShiftCount; i++) {
                Shift objShift = (Shift) vListShift.get(i);

                // set startTime with first record of result
                if (i == 0) {
                    sStartTime = Formater.formatDate(objShift.getStartTime(), "HH:mm:00");
                }

                // set endTime with last record of result
                if (i == (iShiftCount - 1)) {
                    sEndTime = Formater.formatDate(objShift.getEndTime(), "HH:mm:00");
                }

                if ((objShift.getStartTime().getHours()) > (objShift.getEndTime().getHours())) {
                    iDayOfShiftInterval = 1;
                }
            }
        }

        if (dEndDatePeriod != null) {
            int iOldDate = dEndDatePeriod.getDate();
            dEndDatePeriod.setDate(iOldDate + iDayOfShiftInterval);
        }

        if (dStartDatePeriod != null && dEndDatePeriod != null) {
            vResult.add(dStartDatePeriod);
            vResult.add(dEndDatePeriod);
            vResult.add(sStartTime);
            vResult.add(sEndTime);
        }

        return vResult;
    }

    // ------------------------------- POSTING RECEIVE START ---------------------------
    /**
     * This method use to posting LGR document
     * @return
     * algoritm :
     *  - posting all sales document during specified time interval (with sales item)
     *  - if there are sales document cannot posting, do posting process for outstanding trans document (without sales item)
     */
    private Vector getUnPostedLGRDocument(Date postingDate, long oidLocation, long lPeriodeOid, boolean postedDateCheck) {
         Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        Vector vTimeDuration = getTimeDurationOfTransDocument(lPeriodeOid);
        if (vTimeDuration != null && vTimeDuration.size() > 3) {
            dStartDatePeriod = (Date) vTimeDuration.get(0);
            dEndDatePeriod = (Date) vTimeDuration.get(1);
        }
        // --- finish get time duration of transaction document that will posting ---
        try {
            String sql = "SELECT " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    ", " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
                    ", " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] +
                    //Ppn
                    ", " + PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN] +
                    ", " + PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN]+
                    " FROM " + PstMatReceive.TBL_MAT_RECEIVE;

            if (postedDateCheck) {
                sql = sql + " WHERE " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                        " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                } else {
                    sWhereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }
                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sql = sql + " WHERE " + sWhereClause;
                }
            }
            sql = sql + " ORDER BY "+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE];
            
            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".getUnPostedLGRDocument() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidRM = rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]);
                int recSource = rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]);
                //PPn
                double ppn = rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN]);
                int includePpn = rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN]);

                Vector vErrUpdateStockByReceiveItem = new Vector(1,1);
                try {
                    //vErrUpdateStockByReceiveItem = updateStockByReceiveItem(lPeriodeOid, oidLocation, oidRM, recSource, ppn);
                     vErrUpdateStockByReceiveItem = updateStockByReceiveItem(lPeriodeOid, oidLocation, oidRM, recSource, ppn, includePpn, "", 0);
                }
                catch(Exception e) {
                    System.out.println("Exc when get vector vErrUpdateStockByReceiveItem... "+e.toString());
                    vErrUpdateStockByReceiveItem = new Vector(1,1);
                }
                
                // Set status document receive menjadi posted
                boolean isOK = false;
                if (!(vErrUpdateStockByReceiveItem != null && vErrUpdateStockByReceiveItem.size() > 0)) {
                    isOK = setPosted(oidRM, DOC_TYPE_RECEIVE);
                }
                
                /** set status pada forwarder info dengan value terkini! */
                try {
                    String whereClause = ""+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID]+"="+oidRM;
                    Vector vctListFi = PstForwarderInfo.list(0, 0, whereClause, "");
                    ForwarderInfo forwarderInfo = new ForwarderInfo();
                    
                    if(vctListFi.size() != 0) {
                        forwarderInfo = (ForwarderInfo)vctListFi.get(0);
                        isOK = setPosted(forwarderInfo.getOID(), DOC_TYPE_FORWARDER);
                    }
                }
                catch(Exception e) {
                    System.out.println("Exc in update status, forwarder_info >>> "+e.toString());
                }

                if (!isOK) {
                    try {
                        MatReceive objBillMain = PstMatReceive.fetchExc(oidRM);
                        vResult.add(objBillMain);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                    break;
                }
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc in getUnPostedLGRDocument(#,#,#,#) : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
        /*// posting LGR document that have lgr item
        vResult = PostingLGRToStock(dPostingDate, lLocationOid, lPeriodeOid, bPostedDateCheck);

        // posting LGR document that haven't lgr item
        Vector vUnPostedLGR = listLGRWithoutItem(lPeriodeOid, dPostingDate, lLocationOid, bPostedDateCheck);
        if (vUnPostedLGR != null && vUnPostedLGR.size() > 0) {
            vResult.addAll(vUnPostedLGR);
        }
        return vResult;*/
    }


    /**
     * gadnyana
     * proses posted doc receive
     */
    public boolean postedReceiveDoc(long oidRec, String userName, long userId){
        boolean isOK = false;
        try{
            MatReceive matReceive = PstMatReceive.fetchExc(oidRec);
            Periode periode = PstPeriode.getPeriodeRunning();
            //Vector vErrUpdateStockByReceiveItem = updateStockByReceiveItem(periode.getOID(), matReceive.getLocationId(), matReceive.getOID(), matReceive.getReceiveSource());
            Vector vErrUpdateStockByReceiveItem = updateStockByReceiveItem(periode.getOID(), matReceive.getLocationId(), matReceive.getOID(), matReceive.getReceiveSource(), matReceive.getTotalPpn(), matReceive.getIncludePpn(), userName, userId);

            // Set status document receive menjadi posted
            if (!(vErrUpdateStockByReceiveItem != null && vErrUpdateStockByReceiveItem.size() > 0)) {
                isOK = setPosted(oidRec, DOC_TYPE_RECEIVE);
            }
        }catch(Exception e){
            System.out.println("err > postedReceiveDoc(long oidRec) : "+e.toString());
        }
        return isOK;
    }
    
    
    /**
     * ini untuk posting stok only
     * @param oidRec
     * @return 
     * logika : hanya untuk po
     */
    public boolean postedQtyReceiveOnlyDoc(long oidRec){
        boolean isOK = false;
        try{
            MatReceive matReceive = PstMatReceive.fetchExc(oidRec);
            Periode periode = PstPeriode.getPeriodeRunning();
            Vector vErrUpdateQtyStockOnlyByReceiveItem = updateQtyStockOnlyByReceiveItem(periode.getOID(), matReceive.getLocationId(), matReceive.getOID(), matReceive.getReceiveSource(), matReceive.getTotalPpn(), matReceive.getIncludePpn());

        }catch(Exception e){
            System.out.println("err > postedReceiveDoc(long oidRec) : "+e.toString());
        }
        return isOK;
    }
    
    /**
     * update opie-eyek 20140324
     * @param oidRec
     * @return 
     * // untuk posting stok
     */
    public boolean postedValueReceiveOnlyDoc(long oidRec){
        boolean isOK = false;
        try{
            MatReceive matReceive = PstMatReceive.fetchExc(oidRec);
            Periode periode = PstPeriode.getPeriodeRunning();
            
            Vector vErrUpdateValueStockOnlyByReceiveItem = updateValueStockOnlyByReceiveItem(periode.getOID(), matReceive.getLocationId(), matReceive.getOID(), matReceive.getReceiveSource(), matReceive.getTotalPpn(), matReceive.getIncludePpn());
            
            if (!(vErrUpdateValueStockOnlyByReceiveItem != null && vErrUpdateValueStockOnlyByReceiveItem.size() > 0)) {
                isOK = setPosted(oidRec, DOC_TYPE_RECEIVE);
            }
        }catch(Exception e){
            System.out.println("err > postedReceiveDoc(long oidRec) : "+e.toString());
        }
        return isOK;
    }

    /**
     * update opie-eyek 20131205 untuk posting return per document
     * @param oidRet
     * @return
     */
    public boolean postedReturnDoc(long oidRet, long userId, String userName){
        boolean isOK = false;
        try{
            MatReturn matReturn = PstMatReturn.fetchExc(oidRet);
            Periode periode = PstPeriode.getPeriodeRunning();

            Vector vErrUpdateStockByReturnItem = updateStockByReturnItem(periode.getOID(), matReturn.getLocationId(), oidRet, userId, userName);
            // Set status document return menjadi posted
            if (!(vErrUpdateStockByReturnItem != null && vErrUpdateStockByReturnItem.size() > 0)) {
                isOK = setPosted(oidRet, DOC_TYPE_RETURN);
            }
        }catch(Exception e){
            System.out.println("err > postedReceiveDoc(long oidRec) : "+e.toString());
        }
        return isOK;
    }

    /**
     * posting per document untuk transfer
     * @param oidDispatch
     * @return
     */
    public boolean postedDispatchDoc(long oidDispatch){
        boolean isOK = false;
        try{
            MatDispatch matDispatch = PstMatDispatch.fetchExc(oidDispatch);
            Periode periode = PstPeriode.getPeriodeRunning();

           // Vector vErrUpdateStockByReturnItem = updateStockByReturnItem(periode.getOID(), matReturn.getLocationId(), oidRet);
            Vector vErrUpdateStockByDispatchItem = xUpdateStockByDispatchItem(periode.getOID(), matDispatch.getLocationId(), oidDispatch);
            // Set status document return menjadi posted
            if (!(vErrUpdateStockByDispatchItem != null && vErrUpdateStockByDispatchItem.size() > 0)) {
                isOK = setPosted(oidDispatch, DOC_TYPE_DISPATCH);
            }
        }catch(Exception e){
            System.out.println("err > postedReceiveDoc(long oidRec) : "+e.toString());
        }
        return isOK;
    }

    /**
     * posting perdocument untuk costing
     * @param oidDispatch
     * @return
     */
     public boolean postedCostingDoc(long oidCosting){
        boolean isOK = false;
        try{
            MatCosting matCosting = PstMatCosting.fetchExc(oidCosting);
            Periode periode = PstPeriode.getPeriodeRunning();

           // Vector vErrUpdateStockByReturnItem = updateStockByReturnItem(periode.getOID(), matReturn.getLocationId(), oidRet);
            Vector vErrUpdateStockByCostingItem = updateStockByCostingItem(periode.getOID(), matCosting.getLocationId(), oidCosting);
            // Set status document return menjadi posted
            if (!(vErrUpdateStockByCostingItem != null && vErrUpdateStockByCostingItem.size() > 0)) {
                isOK = setPosted(oidCosting, DOC_TYPE_COSTING);
            }
        }catch(Exception e){
            System.out.println("err > postedReceiveDoc(long oidRec) : "+e.toString());
        }
        return isOK;
    }


   public boolean postedSalesTranscaction(long oidBillMain){
        boolean isOK = false;
        try{
            BillMain billMain = PstBillMain.fetchExc(oidBillMain);
            //MatReceive matReceive = PstMatReceive.fetchExc(oidRec);
            Periode periode = PstPeriode.getPeriodeRunning();
            //Vector vErrUpdateStockByReceiveItem = updateStockByReceiveItem(periode.getOID(), matReceive.getLocationId(), matReceive.getOID(), matReceive.getReceiveSource());
            //synchronized private Vector updateStockBySalesItem(Date postingDate, long lBillMainOid, long oidLocation, long oidPeriode, long oidCashCashier) {
            Vector vErrUpdateStockBySaleItem = updateStockBySalesItem(billMain.getBillDate(), billMain.getOID(), billMain.getLocationId(), periode.getOID(), billMain.getCashCashierId());

            // Set status document receive menjadi posted
            if (!(vErrUpdateStockBySaleItem != null && vErrUpdateStockBySaleItem.size() > 0)) {
                isOK = setPosted(oidBillMain, DOC_TYPE_SALE_REGULAR);
            }
        }catch(Exception e){
            System.out.println("err > postedReceiveDoc(long oidRec) : "+e.toString());
        }
        return isOK;
    }
   
    public synchronized boolean postedProductionDoc(long oidProduction) {
        boolean isOK = false;
        try {
            Production production = PstProduction.fetchExc(oidProduction);
            Periode periode = PstPeriode.getPeriodeRunning();

            Vector vErrUpdateStockByProductionCost = updateStockByProductionItemCost(periode.getOID(), production.getLocationFromId(), oidProduction);
            Vector vErrUpdateStockByProductionProduct = updateStockByProductionItemProduct(periode.getOID(), production.getLocationToId(), oidProduction);
            // Set status document produksi menjadi posted
            if (!(vErrUpdateStockByProductionCost != null && vErrUpdateStockByProductionCost.size() > 0) && !(vErrUpdateStockByProductionProduct != null && vErrUpdateStockByProductionProduct.size() > 0)) {
                isOK = setPosted(oidProduction, DOC_TYPE_PRODUCTION);
                if (isOK) {
                    //update everage price item hasil produksi
                    SessProduction.updateAllAveragePriceProductFromProduction(oidProduction);
                }
            }
        } catch (Exception e) {
            System.out.println("err > postedReceiveDoc(long oidRec) : " + e.toString());
        }
        return isOK;
    }

    /**
     *
     * @param oidPeriode
     * @param oidLocation
     * @param lReceiveMaterialOid
     * @param recSource
     * @return
     */
    //private Vector updateStockByReceiveItem(long oidPeriode, long oidLocation, long lReceiveMaterialOid, int recSource, double ppn) {
    private Vector updateStockByReceiveItem(long oidPeriode, long oidLocation, long lReceiveMaterialOid, int recSource, double ppn, int includePpn, String userName, long userId) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);
        long oidOldLocation = 0;
        try {
            int includePpnMasukan = Integer.parseInt(PstSystemProperty.getValueByName("INCLUDE_PPN_MASUKAN"));
            int ppnDefault = Integer.parseInt(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
            int hppCalculate = Integer.parseInt(PstSystemProperty.getValueByName("HPP_CALCULATION"));

            MatReceive matreceive = PstMatReceive.fetchExc(lReceiveMaterialOid);
            String sql = "SELECT "
                    + " RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURR_BUYING_PRICE]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]
                    + ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
                    + ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]
                    + //Gunadi menambahkan berat untuk litama
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST]
                    + //+query Discount & ppn by Mirahu (19 Feb 2011)
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT2]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISC_NOMINAL]
                    + ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT]
                    + ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE]
                    + ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]
                    + //end of query
                    " FROM " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RI"
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT"
                    + " ON RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " WHERE RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]
                    + " = " + lReceiveMaterialOid;

            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".updateStockByReceiveItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                long oidMaterial = rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]);
                double newCost = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]);
                double newForwarderCost = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST]);
                long oidCurrency = rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID]);
                double recQty = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]);
                double qtyPerBaseUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double recQtyReal = recQty * qtyPerBaseUnit;
                double averagePrice = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]);
                //get discount
                double lastDisc = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT]);
                double lastDisc2 = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT2]);
                double lastDiscNom = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISC_NOMINAL]);

                double berat = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT]);

                double totalDiscount = newCost * lastDisc / 100;
                double totalMinus = newCost - totalDiscount;
                double totalDiscount2 = totalMinus * lastDisc2 / 100;
                double totalCost = (totalMinus - totalDiscount2) - lastDiscNom;
                //double totalCostAll = totalCost + newForwarderCost;
                //end get dan calculate discount
                //get currBuyPrice + PPn
                //double lastVat = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT]);
                //double lastVat = rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN]);
                //double lastVat = ppn;
                double currBuyPrice = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE]);
                double totalCostPpn = 0.0;
                double totalCurrBuyPrice = 0.0;

                //get opie-eyek 20160820
                long receiveMaterialId = rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]);

                if (includePpn == PstMatReceive.INCLUDE_PPN) {
                    totalCostPpn = totalCost / 1.1;
                    totalCurrBuyPrice = totalCost + newForwarderCost;
                } else if (includePpn == PstMatReceive.EXCLUDE_PPN) {
                    //totalCostPpn = totalCost+(totalCost*(1+(ppn/100)));
                    totalCostPpn = (totalCost * (ppn / 100)) + totalCost;
                    totalCurrBuyPrice = totalCostPpn + newForwarderCost;
                }
                //double totalPpn = totalCost * lastVat/100;
                //double totalCurrBuyPrice = totalCost + totalPpn;
                //double totalCurrBuyPrice = totalCost*(1+(lastVat/100));

                //end of currBuyPrice
                Thread.sleep(50);
                // Update Cost di Master
                boolean isOK = false;
                if ((recSource == PstMatReceive.SOURCE_FROM_SUPPLIER) || (recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO || recSource == PstMatReceive.SOURCE_FROM_DISPATCH_UNIT || recSource == PstMatReceive.SOURCE_FROM_RETURN)) {

                    //isOK = updateCostMaster(oidMaterial, oidCurrency, newCost);
                    if (recSource != PstMatReceive.SOURCE_FROM_DISPATCH_UNIT) {
                        if (includePpn == PstMatReceive.INCLUDE_PPN) {
                            //disini di update 20140318 jika costnya=0 || costnya < 0 jangan di update costnya
                            if (totalCost > 0) {
                                isOK = updateCostMaster(oidMaterial, oidCurrency, totalCostPpn);
                            } else {
                                isOK = true;
                            }//System.out.println("=============== update currBuy(cost)include Ppn : "+totalCostPpn);
                        } else if (includePpn == PstMatReceive.EXCLUDE_PPN) {

                            //disini di update 20140318 jika costnya=0 || costnya < 0 jangan di update costnya
                            if (totalCost > 0) {
                                isOK = updateCostMaster(oidMaterial, oidCurrency, totalCost);
                            } else {
                                isOK = true;
                            }
                            //System.out.println("=============== update currBuy(cost) : "+totalCost);
                        }

                        //disini di update 20140318 jika costnya=0 || costnya < 0 jangan di update costnya
                        if (totalCost > 0) {
                            updatePPnMaster(oidMaterial, oidCurrency, ppn);
                            updateForwarderCostMaster(oidMaterial, oidCurrency, newForwarderCost);
                        }

                    //updateCostSupplierPrice(matreceive.getSupplierId(), oidMaterial, matreceive.getCurrencyId(), (newCost + newForwarderCost));
                        //updateCostSupplierPrice(matreceive.getSupplierId(), oidMaterial, matreceive.getCurrencyId(), (totalCost + newForwarderCost));
                        //System.out.println("=============== update cost master : "+(totalCost + newForwarderCost));
                        //updateCurrBuyPrice + PPn 
                        //disini di update 20140318 jika costnya=0 || costnya < 0 jangan di update costnya
                        if (totalCost > 0) {
                            updateCurrBuyPriceMaster(oidMaterial, oidCurrency, totalCurrBuyPrice);
                        }
                        //System.out.println("=============== update currBuyPrice+PPn : "+totalCurrBuyPrice);
                        //end of CurrBuyPrice
                    }

                    // Update average price for each receive stock
                    double new_price = 0.0;
                    double avg_new_price = 0.0;
                    StandartRate startdartRate = PstStandartRate.getActiveStandardRate(oidCurrency);
                    //newCost = (startdartRate.getSellingRate() * newCost);
                    //include or not include ppn
                    if (includePpnMasukan == 1) {
                        newCost = (startdartRate.getSellingRate() * totalCost);
                    } else if (includePpnMasukan == 0) {
                        newCost = (startdartRate.getSellingRate() * totalCurrBuyPrice);
                    }

                    //cek stok, ubah agar sesuai dengan //ubah cara pencarian nya
                    MatReceive matReceive = new MatReceive();
                    if (receiveMaterialId != 0) {
                        try {
                            matReceive = PstMatReceive.fetchExc(receiveMaterialId);
                        } catch (Exception es) {
                        }
                    }
                    double qtyStock = 0;
                    try {
                        //UPDATE BY DEWOK 20191001, UNTUK MENGHITUNG HPP DIBUTUHKAN QTY STOCK DARI SEMUA LOKASI,
                        //BUKAN PER LOKASI SEPERTI METHOD DI BAWAH INI
                        //qtyStock = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(oidMaterial, oidLocation, matReceive.getReceiveDate(), 0);
                        
                        //CARI QTY STOCK DARI SEMUA LOKASI
                        SrcStockCard srcStockCard = new SrcStockCard();
                        Vector vectSt = new Vector(1, 1);
                        vectSt.add("2");
                        vectSt.add("5");
                        vectSt.add("7");
                        srcStockCard.setDocStatus(vectSt);
                        srcStockCard.setMaterialId(oidMaterial);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(matreceive.getReceiveDate());
                        cal.add(Calendar.DATE, 1);
                        srcStockCard.setStardDate(cal.getTime());
                        qtyStock = SessStockCard.qtyStock(srcStockCard) - recQty;//sumQtyStockAllLocation(oidMaterial);
                    } catch (Exception es) {
                    }

                    //double qtyStock = checkStockMaterial(oidMaterial, 0, oidPeriode);
                    if (qtyStock < 0) {
                        qtyStock = 0;
                    }
                    avg_new_price = ((averagePrice * qtyStock) + (recQty * newCost)) / (qtyStock + recQty);

                    //update opie-eyek, untuk penerimaan menggunakan hpp average apa hpp dari harga beli terakhir
                    if (hppCalculate == 1) {
                        avg_new_price = newCost;
                    } 
//                    else {
//                        //cek avg_new_price
//                        if (avg_new_price > newCost) {
//                            double newValue = avg_new_price - newCost;
//                            if (newValue > 100000) {
//                                avg_new_price = newCost;
//                            }
//                        }
//                    }
                    //updated by dewok 20180123 for litama
                    int typeOfBusinessDetail = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));
                    if (typeOfBusinessDetail != 2) {
                        updateAveragePrice(oidMaterial, oidCurrency, avg_new_price);
                        
                        String hargaBeli = String.format("%,.2f",newCost);
                        String hppAwal = String.format("%,.2f",averagePrice);
                        String formula = "(( "+hppAwal+" * "+qtyStock+" ) + ( "+hargaBeli+" * "+recQty+" )) / "+(qtyStock + recQty);
                        String logDetail = "{\"HARGA_BELI\" : \""+hargaBeli+"\", ";
                                logDetail += "\"QTY_PENERIMAAN\" : \""+recQty+"\", ";
                                logDetail += "\"HPP_AWAL\" : \""+hppAwal+"\", ";
                                logDetail += "\"QTY_AWAL\" : \""+qtyStock+"\", ";
                                logDetail += "\"RUMUS_HPP\" : \""+formula+"\", ";
                                logDetail += "\"HPP\" : \""+String.format("%,.2f",avg_new_price)+"\"}";
                        
                        LogSysHistory logSysHistory = new LogSysHistory();
                        logSysHistory.setLogDocumentId(oidMaterial);
                        logSysHistory.setLogUserId(userId);
                        logSysHistory.setLogLoginName(userName);
                        logSysHistory.setLogDocumentNumber(matreceive.getRecCode());
                        logSysHistory.setLogDocumentType("HPP");
                        logSysHistory.setLogUserAction("Update");
                        logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
                        logSysHistory.setLogUpdateDate(new Date());
                        logSysHistory.setLogApplication("Prochain");
                        logSysHistory.setLogDetail(logDetail);
                        try {
                            PstLogSysHistory.insertExc(logSysHistory);
                        } catch (Exception exc){}
                    }

                    if (recSource == PstMatReceive.SOURCE_FROM_SUPPLIER || recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO) {
                        if (receiveMaterialId != 0) {
                            try {
                                if (matReceive.getSupplierId() != 0) {
                                    updateSupplierMaterial(oidMaterial, matReceive.getSupplierId(), 0);
                                }
                            } catch (Exception es) {
                            }
                        }
                    }

                    /*
                     * Cek if item receive mengandung component composit
                     */
                    Vector componentComposit = new Vector();
                    componentComposit = checkComponentComposit(oidMaterial);

                    /**
                     * untuk cek qty po
                     */
                    if (recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO) {
                        updateItemPO(oidMaterial, matreceive.getPurchaseOrderId(), recQty);
                    }

                } else {

                    if (recSource == PstMatReceive.SOURCE_FROM_RETURN) {
                        try {
                            MatReturn matReturn = PstMatReturn.fetchExc(matreceive.getReturnMaterialId());
                            oidOldLocation = matReturn.getLocationId();
                        } catch (Exception e) {
                        }
                    } else if (recSource == PstMatReceive.SOURCE_FROM_DISPATCH) {
                        try {
                            MatDispatch matDispatch = PstMatDispatch.fetchExc(matreceive.getDispatchMaterialId());
                            oidOldLocation = matDispatch.getLocationId();
                        } catch (Exception e) {
                        }
                    }
                    isOK = true;

                }

                //Gunadi untuk buyback
                if (recSource == PstMatReceive.SOURCE_FROM_BUYBACK) {
                    try {
                        int typeOfBusinessDetail = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));
                        if (typeOfBusinessDetail == 2) {
                            MaterialDetail matDetail = PstMaterialDetail.fetchExcMaterialDetailId(oidMaterial);
                            matDetail.setBerat(berat);
                            matDetail.setHargaJual(newCost);
                            matDetail.setOngkos(newForwarderCost);
                            try {
                                PstMaterialDetail.updateExc(matDetail);
                            } catch (Exception exc) {

                            }

                            Material material = PstMaterial.fetchExc(oidMaterial);
                            material.setDefaultCost(newCost);
                            material.setAveragePrice(newCost);
                            try {
                                PstMaterial.updateExc(material);
                            } catch (Exception exc) {

                            }

                        }
                    } catch (Exception exc) {

                    }
                }

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    if (recQtyReal > 0) {
                        isOK = updateMaterialStock(oidMaterial, oidLocation, recQtyReal, 0, MODE_UPDATE, DOC_TYPE_RECEIVE, oidPeriode);
                    }
                } else {
                    // Insert into MaterialStock
                    isOK = updateMaterialStock(oidMaterial, oidLocation, recQtyReal, 0, MODE_INSERT, DOC_TYPE_RECEIVE, oidPeriode);
                }

                if (!isOK) {
                    vResult.add(oidMaterial + " on " + lReceiveMaterialOid);
                }

                // Proses insert/update stok code
                cekInsertUpdateStockCode(oidOldLocation, oidLocation, oidMaterial, rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]), DOC_TYPE_RECEIVE, recSource, matreceive.getReceiveDate(), totalCostPpn);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc in updateStockByReceiveItem(#,#,#,#) : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }


      
   private Vector updateQtyStockOnlyByReceiveItem(long oidPeriode, long oidLocation, long lReceiveMaterialOid, int recSource, double ppn, int includePpn) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);
        long oidOldLocation = 0;
        try {
            MatReceive matreceive = PstMatReceive.fetchExc(lReceiveMaterialOid);
            String sql = "SELECT " +
                    " RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURR_BUYING_PRICE] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST] +
                    //+query Discount & ppn by Mirahu (19 Feb 2011)
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT2] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISC_NOMINAL] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT]+
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
                    //end of query
                    " FROM " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RI" +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                    " = " + lReceiveMaterialOid;

            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".updateStockByReceiveItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                long oidMaterial = rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]);
                double newCost = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]);
                double newForwarderCost = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST]);
                long oidCurrency = rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID]);
                double recQty = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]);
                double qtyPerBaseUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double recQtyReal = recQty * qtyPerBaseUnit;
                double averagePrice = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]);
                //get discount
                double lastDisc = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT]);
                double lastDisc2 = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT2]);
                double lastDiscNom = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISC_NOMINAL]);

                double totalDiscount = newCost * lastDisc/100;
                double totalMinus = newCost - totalDiscount;
                double totalDiscount2 = totalMinus * lastDisc2/100;
                double totalCost = (totalMinus - totalDiscount2)-lastDiscNom;
                //double totalCostAll = totalCost + newForwarderCost;
                //end get dan calculate discount

                //get currBuyPrice + PPn
                int includePpnMasukan = Integer.parseInt(PstSystemProperty.getValueByName("INCLUDE_PPN_MASUKAN"));
                int ppnDefault = Integer.parseInt(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
                //double lastVat = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT]);
                //double lastVat = rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN]);
                //double lastVat = ppn;
                double currBuyPrice = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE]);
                double totalCostPpn = 0.0;
                double totalCurrBuyPrice = 0.0;

                if(includePpn == PstMatReceive.INCLUDE_PPN){
                    totalCostPpn = totalCost/1.1;
                    totalCurrBuyPrice = totalCost + newForwarderCost;
                }
                else if(includePpn == PstMatReceive.EXCLUDE_PPN) {
                    totalCostPpn = (totalCost * (ppn / 100)) + totalCost;
                    totalCurrBuyPrice = totalCostPpn + newForwarderCost;
                }

                Thread.sleep(50);
                // Update Cost di Master
                boolean isOK = false;
                if ((recSource == PstMatReceive.SOURCE_FROM_SUPPLIER) ||
                        (recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO || recSource == PstMatReceive.SOURCE_FROM_DISPATCH_UNIT )) {

                  if(recSource != PstMatReceive.SOURCE_FROM_DISPATCH_UNIT){
                    if(includePpn == PstMatReceive.INCLUDE_PPN){

                    }
                    else if(includePpn == PstMatReceive.EXCLUDE_PPN){
        
                    }
                 
                  }

                    /**
                     * untuk cek qty po
                     */
                    if(recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO){
                        updateItemPO(oidMaterial,matreceive.getPurchaseOrderId(),recQty);
                    }
                    
                } else {
                    if(recSource == PstMatReceive.SOURCE_FROM_RETURN){
                        try{
                            MatReturn matReturn = PstMatReturn.fetchExc(matreceive.getReturnMaterialId());
                            oidOldLocation = matReturn.getLocationId();
                        }catch(Exception e){}
                    }else if(recSource == PstMatReceive.SOURCE_FROM_DISPATCH){
                        try{
                            MatDispatch matDispatch = PstMatDispatch.fetchExc(matreceive.getDispatchMaterialId());
                            oidOldLocation = matDispatch.getLocationId();
                        }catch(Exception e){}
                    }
                    isOK = true;
                }

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    if (recQtyReal > 0) {
                        isOK = updateMaterialStock(oidMaterial, oidLocation, recQtyReal, 0, MODE_UPDATE, DOC_TYPE_RECEIVE, oidPeriode);
                    }
                } else {
                    // Insert into MaterialStock
                    isOK = updateMaterialStock(oidMaterial, oidLocation, recQtyReal, 0, MODE_INSERT, DOC_TYPE_RECEIVE, oidPeriode);
                }

                if (!isOK) {
                    vResult.add(oidMaterial + " on " + lReceiveMaterialOid);
                }

                // Proses insert/update stok code
                cekInsertUpdateStockCode(oidOldLocation, oidLocation, oidMaterial, rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]), DOC_TYPE_RECEIVE,recSource, matreceive.getReceiveDate(),totalCostPpn);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc in updateStockByReceiveItem(#,#,#,#) : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }
   
   
   
   private Vector updateValueStockOnlyByReceiveItem(long oidPeriode, long oidLocation, long lReceiveMaterialOid, int recSource, double ppn, int includePpn) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);
        long oidOldLocation = 0;
        try {
            MatReceive matreceive = PstMatReceive.fetchExc(lReceiveMaterialOid);
            String sql = "SELECT " +
                    " RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURR_BUYING_PRICE] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST] +
                    //+query Discount & ppn by Mirahu (19 Feb 2011)
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT2] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISC_NOMINAL] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT]+
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
                    
                    ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE] +
                    //end of query
                    " FROM " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RI" +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    //update opie-eyek
                    " LEFT JOIN "+PstMatReceive.TBL_MAT_RECEIVE+" RM "+
                    " ON RM."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+
                    " = RI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+
                    
                    " WHERE RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                    " = " + lReceiveMaterialOid;

            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".updateStockByReceiveItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                long oidMaterial = rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]);
                double newCost = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]);
                double newForwarderCost = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST]);
                long oidCurrency = rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID]);
                double recQty = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]);
                //double qtyPerBaseUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //double recQtyReal = recQty * qtyPerBaseUnit;
                double averagePrice = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]);
                
                double exchangeRate = rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]);
                //get discount
                double lastDisc = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT]);
                double lastDisc2 = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT2]);
                double lastDiscNom = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISC_NOMINAL]);

                double totalDiscount = newCost * lastDisc/100;
                double totalMinus = newCost - totalDiscount;
                double totalDiscount2 = totalMinus * lastDisc2/100;
                double totalCost = (totalMinus - totalDiscount2)-lastDiscNom;
                
                int includePpnMasukan = Integer.parseInt(PstSystemProperty.getValueByName("INCLUDE_PPN_MASUKAN"));
                double totalCostPpn = 0.0;
                double totalCurrBuyPrice = 0.0;

                if(includePpn == PstMatReceive.INCLUDE_PPN){
                    totalCostPpn = totalCost/1.1;
                   // totalCurrBuyPrice = totalCostAll;
                    totalCurrBuyPrice = totalCost + newForwarderCost;
                }
                else if(includePpn == PstMatReceive.EXCLUDE_PPN) {
                    //totalCostPpn = totalCost+(totalCost*(1+(ppn/100)));
                    totalCostPpn = (totalCost * (ppn / 100)) + totalCost;
                    totalCurrBuyPrice = totalCostPpn + newForwarderCost;
                }

                Thread.sleep(50);
                // Update Cost di Master
                boolean isOK = false;
                if ((recSource == PstMatReceive.SOURCE_FROM_SUPPLIER) ||
                        (recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO || recSource == PstMatReceive.SOURCE_FROM_DISPATCH_UNIT )) {

                    //isOK = updateCostMaster(oidMaterial, oidCurrency, newCost);
                  if(recSource != PstMatReceive.SOURCE_FROM_DISPATCH_UNIT){
                    if(includePpn == PstMatReceive.INCLUDE_PPN){
                        //disini di update 20140318 jika costnya=0 || costnya < 0 jangan di update costnya
                        if(totalCost>0){
                            isOK = updateCostMaster(oidMaterial, oidCurrency, totalCostPpn);
                        }//System.out.println("=============== update currBuy(cost)include Ppn : "+totalCostPpn);
                    }
                    else if(includePpn == PstMatReceive.EXCLUDE_PPN){
                    
                    //disini di update 20140318 jika costnya=0 || costnya < 0 jangan di update costnya
                    if(totalCost>0){
                            isOK = updateCostMaster(oidMaterial, oidCurrency, totalCost);
                    }
                    //System.out.println("=============== update currBuy(cost) : "+totalCost);
                    }
                    
                    //disini di update 20140318 jika costnya=0 || costnya < 0 jangan di update costnya
                    if(totalCost>0){
                        updatePPnMaster(oidMaterial, oidCurrency, ppn);
                        updateForwarderCostMaster(oidMaterial, oidCurrency, newForwarderCost);
                    }

                    if(totalCost>0){
                        updateCurrBuyPriceMaster(oidMaterial, oidCurrency, totalCurrBuyPrice);
                    }
                  }

                    double avg_new_price = 0.0;
                    if(includePpnMasukan == 1){
                        newCost = (exchangeRate * totalCost);
                    }
                    else if (includePpnMasukan == 0){
                        newCost = (exchangeRate * totalCurrBuyPrice);
                    }
                    
                    double qtyStock = checkStockMaterial(oidMaterial, oidLocation, oidPeriode);
                    
                    if(qtyStock!=0){
                        qtyStock=qtyStock-recQty;
                    }
                    
                    //double qtyStock = checkStockMaterial(oidMaterial, 0, oidPeriode);
                    if(qtyStock < 0) qtyStock = 0;
                    
                    avg_new_price = ((averagePrice * qtyStock) + (recQty * newCost)) / (qtyStock + recQty);
                    
                    //GET MATERIAL BEFORE UPDATE
                    Material prevMaterial = PstMaterial.fetchExc(oidMaterial);
                    
                    //UPDATE MATERIAL
                    updateAveragePrice(oidMaterial, oidCurrency, avg_new_price);
                    
                    //GET MATERIAL AFTER UPDATE
                    Material newMaterial = PstMaterial.fetchExc(oidMaterial);
                    
                    //UPDATE HISTORY AVERAGE PRICE
                    String log = newMaterial.getLogDetail(prevMaterial);
                    if (!log.isEmpty()) {
                        insertHistoryMaterial(0, "", "Posting Receive Doc " + matreceive.getRecCode(), newMaterial, log);
                    }

                    /*
                     * Cek if item receive mengandung component composit
                     */

                    Vector componentComposit = new Vector();
                    componentComposit = checkComponentComposit(oidMaterial);

                    /**
                     * untuk cek qty po
                     */
//                    if(recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO){
//                        updateItemPO(oidMaterial,matreceive.getPurchaseOrderId(),recQty);
//                    }
                    
                } else {
                    if(recSource == PstMatReceive.SOURCE_FROM_RETURN){
                        try{
                            MatReturn matReturn = PstMatReturn.fetchExc(matreceive.getReturnMaterialId());
                            oidOldLocation = matReturn.getLocationId();
                        }catch(Exception e){}
                    }else if(recSource == PstMatReceive.SOURCE_FROM_DISPATCH){
                        try{
                            MatDispatch matDispatch = PstMatDispatch.fetchExc(matreceive.getDispatchMaterialId());
                            oidOldLocation = matDispatch.getLocationId();
                        }catch(Exception e){}
                    }
                    isOK = true;
                }
                
                //cek update di pos_receive_material_item_code
                updatePriceSerialNumber(oidMaterial, totalCostPpn, rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]));
                
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc in updateStockByReceiveItem(#,#,#,#) : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }
    
    private static boolean updatePriceSerialNumber(long oidMaterial,double newPrice,long oidDocItem) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            
           String sqlSelect = "SELECT " +
                            PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_CODE_ID] +
                            "," + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] +
                            "," + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE] +
                            "," + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_VALUE] +
                            " FROM " + PstReceiveStockCode.TBL_POS_RECEIVE_MATERIAL_CODE +
                            " WHERE " + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + oidDocItem;    
            dbrs = DBHandler.execQueryResult(sqlSelect);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                try{
                   updatePriceSerialNumber(DOC_TYPE_SALE_REGULAR, newPrice, 0, rs.getString(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE]));
                   updatePriceSerialNumber(DOC_TYPE_RECEIVE, newPrice,0, rs.getString(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE]));
                   updatePriceSerialNumber(DOC_TYPE_RETURN, newPrice, 0, rs.getString(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE]));
                   updatePriceSerialNumber(DOC_TYPE_COSTING, newPrice, 0, rs.getString(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE]));
                   updatePriceSerialNumber(DOC_TYPE_DISPATCH, newPrice, 0, rs.getString(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE]));
                   updatePriceSerialNumber(DOC_TYPE_STOCK, newPrice,0, rs.getString(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE]));

                }catch(Exception ex){}
            }
            
            hasil = true;
            
        } catch (Exception exc) {
            System.out.println(" XXXXX updateAveragePrice : " + exc);
        }
        return hasil;
    }
   
    private static boolean updatePriceSerialNumber(int docType, double newPrice, long receiveMatId, String serialCode) {
         boolean hasil = false;
         try {
              String sql = "";
                    switch (docType) {
                        case DOC_TYPE_RECEIVE: // Receive
                             sql =  " UPDATE " + PstReceiveStockCode.TBL_POS_RECEIVE_MATERIAL_CODE +
                                    " SET " + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_VALUE] + " = " + newPrice+ 
                                    " WHERE "+ PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE] +" ='" + serialCode+"'";
                            break;

                        case DOC_TYPE_RETURN: // Receive
                             sql =  " UPDATE " + PstReturnStockCode.TBL_POS_RETURN_MATERIAL_CODE +
                                    " SET " + PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_VALUE] + " = " + newPrice+ 
                                    " WHERE "+ PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_CODE] +" ='" + serialCode+"'";
                            break;    

                        case DOC_TYPE_COSTING: // Receive
                              sql =  " UPDATE " + PstCostingStockCode.TBL_POS_COSTING_MATERIAL_CODE +
                                    " SET " + PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_VALUE] + " = " + newPrice+ 
                                    " WHERE "+ PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_CODE] +" ='" +serialCode+"'";
                            break;

                        case DOC_TYPE_DISPATCH: // Receive
                              sql =  " UPDATE " + PstDispatchStockCode.TBL_POS_DISPATCH_MATERIAL_CODE +
                                    " SET " + PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_STOCK_VALUE] + " = " + newPrice+ 
                                    " WHERE "+ PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_STOCK_CODE] +" ='" +serialCode+"'";
                            break;

                        case DOC_TYPE_SALE_REGULAR: // Receive
                              sql =  " UPDATE " + PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE +
                                    " SET " + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_VALUE] + " = " + newPrice+ 
                                    " WHERE "+ PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_CODE] +" ='" +serialCode+"'";
                            break;

                        case DOC_TYPE_SALE_RETURN: // Receive
                              sql =  " UPDATE " + PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE +
                                    " SET " + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_VALUE] + " = " + newPrice+ 
                                    " WHERE "+ PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_CODE] +" ='" +serialCode+"'";
                            break;
                            
                        case DOC_TYPE_STOCK: // Receive
                              sql = " UPDATE " + PstMaterialStockCode.TBL_POS_MATERIAL_STOCK_CODE +
                                    " SET " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_VALUE] + " = " + newPrice+ 
                                    " WHERE "+ PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] +" ='" +serialCode+"'"+
                                    " AND "+ PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_VALUE] +" ='0'";
                            break;
                            
                        default:
                            break;
                    }

                    int a = DBHandler.execUpdate(sql);
                    
         }catch (Exception exc) {
            System.out.println(" XXXXX updateAveragePrice : " + exc);
         }
          
         return hasil;
    }
   
    /**
     * Update data PO untuk
     * itemnya sesuai dengan qty receive
     * @param oidMat
     * @param oidPo
     * @param qtyRec
     */
    public void updateItemPO(long oidMat, long oidPo, double qtyRec) {
        try {
            PurchaseOrderItem purchOrderItem = new PurchaseOrderItem();
            purchOrderItem = PstPurchaseOrderItem.getPurchaseOrderItem(purchOrderItem, oidMat, oidPo);
            if (purchOrderItem.getOID() != 0) {
                if ((purchOrderItem.getQuantity() - purchOrderItem.getResiduQty()) > 0) {
                    double qtyRe = purchOrderItem.getResiduQty() + qtyRec;
                    purchOrderItem.setResiduQty(qtyRe);

                    // update item
                    PstPurchaseOrderItem.updateExc(purchOrderItem);
                    if(cekPurchaseOrder(oidPo)){
                        PurchaseOrder purchOrder = PstPurchaseOrder.fetchExc(oidPo);
                        purchOrder.setPoStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
                        PstPurchaseOrder.updateExc(purchOrder);
                    }else{
                        PurchaseOrder purchOrder = PstPurchaseOrder.fetchExc(oidPo);
                        purchOrder.setPoStatus(I_DocStatus.DOCUMENT_STATUS_FINAL);
                        PstPurchaseOrder.updateExc(purchOrder);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     *
     * @param oidPurchase
     * @return
     */
    public boolean cekPurchaseOrder(long oidPurchase) {
        DBResultSet dbrs = null;
        boolean bool = false;
        try {
            String sql = "select sum(" + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_QUANTITY] + ") as tot_qty " +
                    " ,sum(" + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_RESIDU_QTY] + ") as tot_res_qty " +
                    " from "+PstPurchaseOrderItem.TBL_PURCHASE_ORDER_ITEM+
                    " where " + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] + "=" + oidPurchase +
                    " group by "+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                double totalQty = rs.getDouble("tot_qty");
                double totalResiduQty = rs.getDouble("tot_res_qty");
                if(totalResiduQty < totalQty){
                    bool = false;
                }
                else{
                    bool = true;
                }
            }
        } catch (Exception e) {
        }finally{

        }
        return bool;
    }

    /**
     * @param postingDate
     * @param oidLocation
     * @param oidPeriode
     * @return
     * @update by Edhy
     * algoritm :
     1. Update Cost in Master
     2. Insert MaterialStock if not exists
     3. Inc Qty In in MaterialStock
     4. Inc Qty in MaterialStock
     5. Set document status into posted
     *
     */
    private Vector PostingLGRToStock(Date postingDate, long oidLocation, long oidPeriode, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;

        try {
            // Select all receive today with detail
            String sql = "SELECT RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    ", RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                    " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " RM" +
                    " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RI" +
                    " ON RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    " = RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

            if (postedDateCheck) {
                sql = sql + " WHERE RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                        " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                        " AND RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                        " = " + oidLocation;

            } else {
                Date dStartDatePeriod = null;
                Date dEndDatePeriod = null;
                try {
                    Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                    dStartDatePeriod = objMaterialPeriode.getStartDate();
                    dEndDatePeriod = objMaterialPeriode.getEndDate();
                } catch (Exception e) {
                    System.out.println("Exc " + new SessClosing().getClass().getName() + ".PostingLGRToStock() - fetch period : " + e.toString());
                }

                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = " RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                            " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:00:01") + "'" +
                            " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:59:59") + "'";
                }

                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sWhereClause = sWhereClause + " AND RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                } else {
                    sWhereClause = " RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }

                sql = sql + " WHERE " + sWhereClause;
            }

            //System.out.println(">>> " + new SessClosing().getClass().getName() + ".PostingLGRToStock() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidRM = rs.getLong(1);
                int recSource = rs.getInt(2);
                long oidMaterial = rs.getLong(3);
                double newCost = rs.getDouble(4);
                long oidCurrency = rs.getLong(5);

                // jumlah dalam order per buy unit
                double recQtyPerBuyUnit = rs.getDouble(6);

                // jumlah barang per dalam buying unit relatif terhadap sell/base unit
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(7), rs.getLong(8));
                double averagePrice = rs.getDouble(7);

                // jumlah barang yg akan masuk stock
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;

                // Update Cost di Master
                boolean isOK = false;
                if ((recSource == PstMatReceive.SOURCE_FROM_SUPPLIER) || (recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO)) {
                    isOK = updateCostMaster(oidMaterial, oidCurrency, newCost);
                } else {
                    isOK = true;
                }

                // Update average price for each receive stock
                double new_price = 0.0;
                double avg_new_price = 0.0;
                double qtyStock = checkStockMaterial(oidMaterial, oidLocation, oidPeriode);
                
                averagePrice = averagePrice * qtyStock;
                new_price = recQty * newCost;
                avg_new_price = (averagePrice + new_price) / (qtyStock + recQty);
                //if (avg_new_price != 0) {
                //    avg_new_price = 0;
                //}
                updateAveragePrice(oidMaterial, oidCurrency, avg_new_price);

                
                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    if (recQty > 0) {
                        isOK = updateMaterialStock(oidMaterial, oidLocation, recQty, 0, MODE_UPDATE, DOC_TYPE_RECEIVE, oidPeriode);
                    }
                } else {
                    // Insert into MaterialStock
                    isOK = updateMaterialStock(oidMaterial, oidLocation, recQty, 0, MODE_INSERT, DOC_TYPE_RECEIVE, oidPeriode);
                }

                // Set status document receive menjadi posted
                if (isOK == true) {
                    isOK = setPosted(oidRM, DOC_TYPE_RECEIVE);
                }

                if (!isOK) {
                    try {
                        BillMain objBillMain = PstBillMain.fetchExc(oidRM);
                        vResult.add(objBillMain);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                    break;
                }

                // Proses insert/update stok code
                cekInsertUpdateStockCode(0, oidLocation, oidMaterial, rs.getLong(9), DOC_TYPE_RECEIVE,0,rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]),0);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc in PostingLGRToStock(#,#,#,#) : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
    }


    /**
     * This method using to posting LGR document without lgr item include during specified period
     * @param oidPeriode
     * @param postingDate
     * @param oidLocation
     * @param postedDateCheck
     * @return
     * @created by Edhy
     * algoritm :
     *  - list all lgr document that still draft
     *  - iterate process of set lgr document status to POSTED without care about stock
     */
    private Vector listLGRWithoutItem(long oidPeriode, Date postingDate, long oidLocation, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);

        String sWhereClause = "";
        if (postedDateCheck) {
            sWhereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                    " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                    " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                    " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                    " = " + oidLocation;
        } else {
            Date dStartDatePeriod = null;
            Date dEndDatePeriod = null;
            try {
                Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                dStartDatePeriod = objMaterialPeriode.getStartDate();
                dEndDatePeriod = objMaterialPeriode.getEndDate();
            } catch (Exception e) {
                System.out.println("Exc " + new SessClosing().getClass().getName() + ".listLGRWithoutItem() - fetch period : " + e.toString());
            }

            if (dStartDatePeriod != null && dEndDatePeriod != null) {
                sWhereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                        " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:59:59") + "'";
            }

            if (sWhereClause != null && sWhereClause.length() > 0) {
                sWhereClause = sWhereClause + " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                sWhereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            }
        }

        Vector vListUnPostedLGRDoc = PstMatReceive.list(0, 0, sWhereClause, "");
        if (vListUnPostedLGRDoc != null && vListUnPostedLGRDoc.size() > 0) {
            int unPostedLGRDocCount = vListUnPostedLGRDoc.size();
            for (int i = 0; i < unPostedLGRDocCount; i++) {
                MatReceive objMatReceive = (MatReceive) vListUnPostedLGRDoc.get(i);
                vResult.add(objMatReceive);
            }
        }

        return vResult;
    }
    // ------------------------------- POSTING RECEIVE FINISH ---------------------------





    // ------------------------------- POSTING RETURN START ---------------------------
    /**
     * This method use to posting return document
     * @param dPostingDate
     * @param lLocationOid
     * @param lPeriodeOid
     * @param bPostedDateCheck
     * @return
     * @created by Edhy
     * algoritm :
     *  - posting all return document during specified time interval (with return item)
     *  - if there are return document cannot posting, do posting process for outstanding trans document (without return item)
     */
    private Vector getUnPostedReturnDocument(Date postingDate, long oidLocation, long oidPeriode, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        String sStartTime = "";
        String sEndTime = "";
        Vector vTimeDuration = getTimeDurationOfTransDocument(oidPeriode);
        if (vTimeDuration != null && vTimeDuration.size() > 3) {
            dStartDatePeriod = (Date) vTimeDuration.get(0);
            dEndDatePeriod = (Date) vTimeDuration.get(1);
            sStartTime = (String) vTimeDuration.get(2);
            sEndTime = (String) vTimeDuration.get(3);
        }
        // --- finish get time duration of transaction document that will posting ---


        try {
            // Select all receive today with detail
            String sql = "SELECT " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                    ", " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_SOURCE] +
                    " FROM " + PstMatReturn.TBL_MAT_RETURN;

            if (postedDateCheck) {
                sql = sql + " WHERE " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                        " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                        " = " + oidLocation;

                /*" AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                " BETWEEN \"" + Formater.formatDate(postingDate, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                " AND \"" + Formater.formatDate(postingDate, "yyyy-MM-dd") + " " + sEndTime + "\"";*/
            } else {
                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                            " = " + oidLocation;

                    /*" AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                    " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                    " AND \"" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd") + " " + sEndTime + "\"";*/
                } else {
                    sWhereClause = PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }

                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sql = sql + " WHERE " + sWhereClause;
                }
            }

            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".getUnPostedReturnDocument() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidRET = rs.getLong(1);
                int retSource = rs.getInt(2);

                Vector vErrUpdateStockByReturnItem = updateStockByReturnItem(oidPeriode, oidLocation, oidRET,0,"");

                // Set status document receive menjadi posted
                boolean isOK = false;
                if (!(vErrUpdateStockByReturnItem != null && vErrUpdateStockByReturnItem.size() > 0)) {
                    isOK = setPosted(oidRET, DOC_TYPE_RETURN);
                }

                if (!isOK) {
                    try {
                        MatReturn objBillMain = PstMatReturn.fetchExc(oidRET);
                        vResult.add(objBillMain);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                    break;
                }
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("RET : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;

        /**Vector vResult = new Vector(1, 1);
         // posting Return document that have return item
         vResult = PostingReturnToStock(dPostingDate, lLocationOid, lPeriodeOid, bPostedDateCheck);
         // posting Return document that haven't resutn item
         Vector vUnPostedReturn = listReturnDocWithoutItem(lPeriodeOid, dPostingDate, lLocationOid, bPostedDateCheck);
         if (vUnPostedReturn != null && vUnPostedReturn.size() > 0) {
         vResult.addAll(vUnPostedReturn);
         }
         return vResult;*/
    }

    synchronized private Vector updateStockByReturnItem(long oidPeriode, long oidLocation, long lReturnOid, long userId, String userName) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);

        try {
            MatReturn matreturn = PstMatReturn.fetchExc(lReturnOid);

            // Select all receive today with detail
            String sql = "SELECT RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    " FROM " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RTI" +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                    " = " + lReturnOid;

            // System.out.println(">>> " + new SessPosting().getClass().getName() + ".updateStockByReturnItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                long oidMaterial = rs.getLong(1);
                double newCost = rs.getDouble(2);
                long oidCurrency = rs.getLong(3);
                double rtnQty = rs.getDouble(4);
                double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double rtnQtyReal = rtnQty * qtyBase;
                long returnId = rs.getLong(9);
                double averagePrice = rs.getDouble(10);
                //System.out.println(">>> rtnQty: "+rtnQty+"\n>>> rtnQtyReal: "+rtnQtyReal);
                Thread.sleep(100);
                // Update Cost di Master
                boolean isOK = false;

                // jumlah barang per dalam buying unit relatif terhadap sell/base unit
                // int qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(5), rs.getLong(6));

                // jumlah barang yg akan masuk stock
                // retQty = retQty * qtyPerSellingUnit;

                // Check if this item is allready exists in MaterialStock  
                
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, rtnQtyReal, MODE_UPDATE, DOC_TYPE_RETURN, oidPeriode);
                } else {
                    // Insert Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, rtnQtyReal, MODE_INSERT, DOC_TYPE_RETURN, oidPeriode);
                }

                if (!isOK) {
                    vResult.add(oidMaterial + " on " + lReturnOid);
                }
                // Proses insert/update stok code
                cekInsertUpdateStockCode(matreturn.getReturnTo(), oidLocation, oidMaterial, rs.getLong(7), DOC_TYPE_RETURN,0,matreturn.getReturnDate(),0);
                
                MatReturn matReturn = new MatReturn();
                try {
                    matReturn = PstMatReturn.fetchExc(returnId);
                } catch (Exception exc){}
                int hppCalculate = Integer.parseInt(PstSystemProperty.getValueByName("HPP_CALCULATION"));
                if (hppCalculate == 0){
                    double qtyStock = 0;
                    double avg_new_price = 0;
                    try {
                        //UPDATE BY DEWOK 20191001, UNTUK MENGHITUNG HPP DIBUTUHKAN QTY STOCK DARI SEMUA LOKASI,
                        //BUKAN PER LOKASI SEPERTI METHOD DI BAWAH INI
                        //qtyStock = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(oidMaterial, oidLocation, matReceive.getReceiveDate(), 0);

                        //CARI QTY STOCK DARI SEMUA LOKASI
                        SrcStockCard srcStockCard = new SrcStockCard();
                        Vector vectSt = new Vector(1, 1);
                        vectSt.add("2");
                        vectSt.add("5");
                        vectSt.add("7");
                        srcStockCard.setDocStatus(vectSt);
                        srcStockCard.setMaterialId(oidMaterial);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(matReturn.getReturnDate());
                        cal.add(Calendar.DATE, 1);
                        srcStockCard.setStardDate(cal.getTime());
                        qtyStock = SessStockCard.qtyStock(srcStockCard) + rtnQty;//sumQtyStockAllLocation(oidMaterial);
                    } catch (Exception es) {
                    }

                    //double qtyStock = checkStockMaterial(oidMaterial, 0, oidPeriode);
                    if (qtyStock < 0) {
                        qtyStock = 0;
                    }
                    avg_new_price = ((averagePrice * qtyStock) - (rtnQty * newCost)) / (qtyStock - rtnQty);

                    //update opie-eyek, untuk penerimaan menggunakan hpp average apa hpp dari harga beli terakhir
                    if (hppCalculate == 1) {
                        avg_new_price = newCost;
                    } 
//                    else {
//                        //cek avg_new_price
//                        if (avg_new_price > newCost) {
//                            double newValue = avg_new_price - newCost;
//                            if (newValue > 100000) {
//                                avg_new_price = newCost;
//                            }
//                        }
//                    }
                    //updated by dewok 20180123 for litama
                    int typeOfBusinessDetail = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));
                    if (typeOfBusinessDetail != 2) {
                        updateAveragePrice(oidMaterial, oidCurrency, avg_new_price);

                        String hargaBeli = String.format("%,.2f",newCost);
                        String hppAwal = String.format("%,.2f",averagePrice);
                        String formula = "(( "+hppAwal+" * "+qtyStock+" ) - ( "+hargaBeli+" * "+rtnQty+" )) / "+(qtyStock - rtnQty);
                        String logDetail = "{\"HARGA_BELI\" : \""+hargaBeli+"\", ";
                                logDetail += "\"QTY_RETURN\" : \""+rtnQty+"\", ";
                                logDetail += "\"HPP_AWAL\" : \""+hppAwal+"\", ";
                                logDetail += "\"QTY_AWAL\" : \""+qtyStock+"\", ";
                                logDetail += "\"RUMUS_HPP\" : \""+formula+"\", ";
                                logDetail += "\"HPP\" : \""+String.format("%,.2f",avg_new_price)+"\"}";

                        LogSysHistory logSysHistory = new LogSysHistory();
                        logSysHistory.setLogDocumentId(oidMaterial);
                        logSysHistory.setLogUserId(userId);
                        logSysHistory.setLogLoginName(userName);
                        logSysHistory.setLogDocumentNumber(matReturn.getRetCode());
                        logSysHistory.setLogDocumentType("HPP Return");
                        logSysHistory.setLogUserAction("Update");
                        logSysHistory.setLogOpenUrl("master/material/material_main.jsp");
                        logSysHistory.setLogUpdateDate(new Date());
                        logSysHistory.setLogApplication("Prochain");
                        logSysHistory.setLogDetail(logDetail);
                        try {
                            PstLogSysHistory.insertExc(logSysHistory);
                        } catch (Exception exc){}
                    }
                }
                
                
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("RET : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }


    /**
     * @param postingDate
     * @param oidLocation
     * @param oidPeriode
     * @return
     * @update by Edhy
     * algoritm :
     *  - Inc Qty Out in MaterialStock
     *  - Dec Qty in MaterialStock
     *  -Set document status into posted
     */
    private Vector PostingReturnToStock(Date postingDate, long oidLocation, long oidPeriode, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            // Select all receive today with detail
            String sql = "SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                    ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_SOURCE] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_CURRENCY_ID] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID] +
                    ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                    " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET" +
                    " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RTI" +
                    " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                    " = RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON RTI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

            if (postedDateCheck) {
                sql = sql + " WHERE RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                        " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                        " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                Date dStartDatePeriod = null;
                Date dEndDatePeriod = null;
                try {
                    Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                    dStartDatePeriod = objMaterialPeriode.getStartDate();
                    dEndDatePeriod = objMaterialPeriode.getEndDate();
                } catch (Exception e) {
                    System.out.println("Exc " + new SessClosing().getClass().getName() + ".PostingReturnToStock() - fetch period : " + e.toString());
                }

                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                            " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:;00:01") + "'" +
                            " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:59:59") + "'";
                }

                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sWhereClause = sWhereClause + " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                } else {
                    sWhereClause = " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }

                sql = sql + " WHERE " + sWhereClause;
            }

            // System.out.println(">>> " + new SessClosing().getClass().getName() + ".PostingReturnToStock() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidRET = rs.getLong(1);
                int retSource = rs.getInt(2);
                long oidMaterial = rs.getLong(3);
                double newCost = rs.getDouble(4);
                long oidCurrency = rs.getLong(5);
                double retQty = rs.getDouble(6);                

                // Update Cost di Master
                boolean isOK = false;

                // jumlah barang per dalam buying unit relatif terhadap sell/base unit
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(7), rs.getLong(8));

                // jumlah barang yg akan masuk stock
                retQty = retQty * qtyPerSellingUnit;

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, retQty, MODE_UPDATE, DOC_TYPE_RETURN, oidPeriode);
                } else {
                    // Insert Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, retQty, MODE_INSERT, DOC_TYPE_RETURN, oidPeriode);
                }

                // Set status document receive menjadi posted
                if (isOK == true) {
                    isOK = setPosted(oidRET, DOC_TYPE_RETURN);
                }


                if (!isOK) {
                    try {
                        BillMain objBillMain = PstBillMain.fetchExc(oidRET);
                        vResult.add(objBillMain);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                    break;
                }

                // Proses insert/update stok code
                cekInsertUpdateStockCode(0, oidLocation, oidMaterial, rs.getLong(9), DOC_TYPE_RETURN,0,rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]),0);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("RET : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
    }


    /**
     * This method using to posting Return document without return item include during specified period
     * @param oidPeriode
     * @param postingDate
     * @param oidLocation
     * @param postedDateCheck
     * @return
     * @created by Edhy
     * algoritm :
     *  - list all return document that still draft
     *  - iterate process of set return document status to POSTED without care about stock
     */
    private Vector listReturnDocWithoutItem(long oidPeriode, Date postingDate, long oidLocation, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        String sWhereClause = "";
        if (postedDateCheck) {
            sWhereClause = PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                    " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                    " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                    " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                    " = " + oidLocation;
        } else {
            Date dStartDatePeriod = null;
            Date dEndDatePeriod = null;
            try {
                Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                dStartDatePeriod = objMaterialPeriode.getStartDate();
                dEndDatePeriod = objMaterialPeriode.getEndDate();
            } catch (Exception e) {
                System.out.println("Exc " + new SessClosing().getClass().getName() + ".listReturnDocWithoutItem() - fetch period : " + e.toString());
            }
            if (dStartDatePeriod != null && dEndDatePeriod != null) {
                sWhereClause = PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                        " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:59:59") + "'";
            }

            if (sWhereClause != null && sWhereClause.length() > 0) {
                sWhereClause = sWhereClause + " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                sWhereClause = PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            }
        }

        Vector vListUnPostedReturnDoc = PstMatReturn.list(0, 0, sWhereClause, "");
        if (vListUnPostedReturnDoc != null && vListUnPostedReturnDoc.size() > 0) {
            int unPostedReturnDocCount = vListUnPostedReturnDoc.size();
            for (int i = 0; i < unPostedReturnDocCount; i++) {
                MatReturn objMatReturn = (MatReturn) vListUnPostedReturnDoc.get(i);
                vResult.add(objMatReturn);
            }
        }

        return vResult;
    }
    // ------------------------------- POSTING RETURN FINISH ---------------------------





    // ------------------------------- POSTING DISPATCH START ---------------------------
    /**
     * This method use to posting dispatch document
     * @param dPostingDate
     * @param lLocationOid
     * @param lPeriodeOid
     * @param bPostedDateCheck
     * @return
     * @created by Edhy
     * algoritm :
     *  - posting all dispatch document during specified time interval (with df item)
     *  - if there are dispatch document cannot posting, do posting process for outstanding trans document (without df item)
     */
    private Vector getUnPostedDFDocument(Date postingDate, long oidLocation, long oidPeriode, boolean postedDateCheck) {

        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        String sStartTime = "";
        String sEndTime = "";
        Vector vTimeDuration = getTimeDurationOfTransDocument(oidPeriode);
        if (vTimeDuration != null && vTimeDuration.size() > 3) {
            dStartDatePeriod = (Date) vTimeDuration.get(0);
            dEndDatePeriod = (Date) vTimeDuration.get(1);
            sStartTime = (String) vTimeDuration.get(2);
            sEndTime = (String) vTimeDuration.get(3);
        }
        // --- finish get time duration of transaction document that will posting ---

        try {
            // Select all receive today with detail
            String sql = "SELECT " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " FROM " + PstMatDispatch.TBL_DISPATCH;

            if (postedDateCheck) {
                sql = sql + " WHERE " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                        " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                        " = " + oidLocation;

                /*" AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                " BETWEEN \"" + Formater.formatDate(postingDate, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                " AND \"" + Formater.formatDate(postingDate, "yyyy-MM-dd") + " " + sEndTime + "\"";*/
            } else {
                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                            " = " + oidLocation;

                    /*" AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                    " AND \"" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd") + " " + sEndTime + "\"";*/
                } else {
                    sWhereClause = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }

                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sql = sql + " WHERE " + sWhereClause;
                }
            }

            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".getUnPostedDFDocument() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidDF = rs.getLong(1);

                Vector vErrUpdateStockByDispatchItem = updateStockByDispatchItem(oidPeriode, oidLocation, oidDF);

                // Set status document receive menjadi posted
                boolean isOK = false;
                if (!(vErrUpdateStockByDispatchItem != null && vErrUpdateStockByDispatchItem.size() > 0)) {
                    isOK = setPosted(oidDF, DOC_TYPE_DISPATCH);
                }
                
                if (!isOK) {
                    try {
                        MatDispatch objBillMain = PstMatDispatch.fetchExc(oidDF);
                        vResult.add(objBillMain);
                        System.out.println(">>> DF ERROR : "+objBillMain.getDispatchCode());
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                    //break;
                }
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("DF : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;

        /*Vector vResult = new Vector(1, 1);
        // posting DF document that have df item
        vResult = PostingDFToStock(dPostingDate, lLocationOid, lPeriodeOid, bPostedDateCheck);
        // posting DF document that haven't df item
        Vector vUnPostedDF = listDFDocWithoutItem(lPeriodeOid, dPostingDate, lLocationOid, bPostedDateCheck);
        if (vUnPostedDF != null && vUnPostedDF.size() > 0) {
            vResult.addAll(vUnPostedDF);
        }
        return vResult;*/
    }

    synchronized public Vector updateStockByDocDispatchItem(long oidPeriode, long oidLocation, long lDispacthOid) {
        return updateStockByDispatchItem(oidPeriode,oidLocation, lDispacthOid);
    }

    synchronized private Vector updateStockByDispatchItem(long oidPeriode, long oidLocation, long lDispacthOid) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        String sStartTime = "";
        String sEndTime = "";
        Vector vTimeDuration = getTimeDurationOfTransDocument(oidPeriode);
        if (vTimeDuration != null && vTimeDuration.size() > 3) {
            dStartDatePeriod = (Date) vTimeDuration.get(0);
            dEndDatePeriod = (Date) vTimeDuration.get(1);
            sStartTime = (String) vTimeDuration.get(2);
            sEndTime = (String) vTimeDuration.get(3);
        }
        // --- finish get time duration of transaction document that will posting ---


        try { 
            // Select all receive today with detail
            String sql = "SELECT DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +
                    " FROM " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " = " + lDispacthOid;

            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".updateStockByDispatchItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet(); 

            MatDispatch matDispatch = new MatDispatch();
	    MatReceive matReceive = new MatReceive();

             try {
                matDispatch = PstMatDispatch.fetchExc(lDispacthOid);
            } catch (Exception e) {
                System.out.println("Exc. when fetch Dispatch in updateStockBydispatchItem(#,#,#,#): "+e.toString());
            }
            
            long oid = 0;
           //kondisi if location_type !=2
          if(matDispatch.getLocationType()!= PstMatDispatch.FLD_TYPE_TRANSFER_UNIT){
            try{
                //matDispatch = PstMatDispatch.fetchExc(lDispacthOid);

                // proses pembuatan penerimaan
                // dari dispatch secara otomatis                
                matReceive.setReceiveDate(matDispatch.getDispatchDate());
                matReceive.setReceiveFrom(matDispatch.getLocationId());
                matReceive.setLocationId(matDispatch.getDispatchTo());
                matReceive.setLocationType(PstLocation.TYPE_LOCATION_STORE);
                matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
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
		
		oid = PstMatReceive.insertExc(matReceive); 
            }catch(Exception e){}   
         }
            while (rs.next()) {
                boolean isOK = false;
                long oidMaterial = rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]);
                double dfQty = rs.getDouble(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]);
                double cost = rs.getDouble(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP]);
                double qtyPerBaseUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double dfQtyReal = dfQty * qtyPerBaseUnit;                

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQtyReal, MODE_UPDATE, DOC_TYPE_DISPATCH, oidPeriode);
                } else {
                    //Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQtyReal, MODE_INSERT, DOC_TYPE_DISPATCH, oidPeriode);
                }

                // ini di pakai untuk pengecekan barang di lokasi tujuan dispatch
               /* if (checkMaterialStock(oidMaterial, matDispatch.getDispatchTo(), oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, matDispatch.getDispatchTo(), dfQty, 0 , MODE_UPDATE, DOC_TYPE_RECEIVE, oidPeriode);
                } else {
                    //Update Qty only
                    isOK = updateMaterialStock(oidMaterial, matDispatch.getDispatchTo(), dfQty, 0 , MODE_INSERT, DOC_TYPE_RECEIVE, oidPeriode);
                }*/

                /// insert item for terima barang dari barang dispatch
                 //kondisi if location_type !=2
                if(matDispatch.getLocationType()!= PstMatDispatch.FLD_TYPE_TRANSFER_UNIT){
                try{
                    MatReceiveItem matReceiveItem = new MatReceiveItem();
                    matReceiveItem.setUnitId(rs.getLong(3));
                    //matReceiveItem.setQty(rs.getInt(2));
                    matReceiveItem.setQty(rs.getDouble(2));
                    matReceiveItem.setResidueQty(rs.getDouble(2));
                    //matReceiveItem.setResidueQty(rs.getInt(2));
                    matReceiveItem.setMaterialId(rs.getLong(1));
                    matReceiveItem.setReceiveMaterialId(oid);
                    matReceiveItem.setCost(cost);
                    matReceiveItem.setCurrBuyingPrice(cost);
                    matReceiveItem.setTotal(matReceiveItem.getQty() * matReceiveItem.getCost());

		    PstMatReceiveItem.insertExc(matReceiveItem);
                }catch(Exception e){}
             }

                if (!isOK) {
                    vResult.add(oidMaterial + " on " + lDispacthOid);
                }

                // Proses insert/update serial stock codenya
               // cekInsertUpdateStockCode(oidLocation, matDispatch.getDispatchTo(), oidMaterial, rs.getLong(5), DOC_TYPE_DISPATCH);
            }
	    //kondisi if location_type !=2
          if(matDispatch.getLocationType()!= PstMatDispatch.FLD_TYPE_TRANSFER_UNIT){

	    // update menjadi status final
	    matReceive.setOID(oid);
	    matReceive.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_FINAL);
	    PstMatReceive.updateExc(matReceive);
         }
	    
            rs.close();
        } catch (Exception exc) {
            System.out.println("DF : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }


    /**
     * update dispatch, yang ga perlu pembuatan dokument transfeer
     * @param oidPeriode
     * @param oidLocation
     * @param lDispacthOid
     * @return
     */
    synchronized private Vector xUpdateStockByDispatchItem(long oidPeriode, long oidLocation, long lDispacthOid) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        String sStartTime = "";
        String sEndTime = "";
        Vector vTimeDuration = getTimeDurationOfTransDocument(oidPeriode);
        if (vTimeDuration != null && vTimeDuration.size() > 3) {
            dStartDatePeriod = (Date) vTimeDuration.get(0);
            dEndDatePeriod = (Date) vTimeDuration.get(1);
            sStartTime = (String) vTimeDuration.get(2);
            sEndTime = (String) vTimeDuration.get(3);
        }
        // --- finish get time duration of transaction document that will posting ---

        try {
            // Select all receive today with detail
            String sql = "SELECT DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] +                    
                    " FROM " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " = " + lDispacthOid;

            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".updateStockByDispatchItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            MatDispatch matDispatch = new MatDispatch();
	   //MatReceive matReceive = new MatReceive();

             try {
                matDispatch = PstMatDispatch.fetchExc(lDispacthOid);
            } catch (Exception e) {
                System.out.println("Exc. when fetch Dispatch in updateStockBydispatchItem(#,#,#,#): "+e.toString());
            }

            long oid = 0;
           //kondisi if location_type !=2
          
            while (rs.next()) {
                boolean isOK = false;
                long oidMaterial = rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]);
                double dfQty = rs.getDouble(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]);
               // double cost = rs.getDouble(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP]);
                double qtyPerBaseUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double dfQtyReal = dfQty * qtyPerBaseUnit;                

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQtyReal, MODE_UPDATE, DOC_TYPE_DISPATCH, oidPeriode);
                } else {
                    //Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQtyReal, MODE_INSERT, DOC_TYPE_DISPATCH, oidPeriode);
                }

                // ini di pakai untuk pengecekan barang di lokasi tujuan dispatch
               /* if (checkMaterialStock(oidMaterial, matDispatch.getDispatchTo(), oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, matDispatch.getDispatchTo(), dfQty, 0 , MODE_UPDATE, DOC_TYPE_RECEIVE, oidPeriode);
                } else {
                    //Update Qty only
                    isOK = updateMaterialStock(oidMaterial, matDispatch.getDispatchTo(), dfQty, 0 , MODE_INSERT, DOC_TYPE_RECEIVE, oidPeriode);
                }*/

                /// insert item for terima barang dari barang dispatch
                 //kondisi if location_type !=2

                if (!isOK) {
                    vResult.add(oidMaterial + " on " + lDispacthOid);
                }

                // Proses insert/update serial stock codenya
               //cekInsertUpdateStockCode(oidLocation, matDispatch.getDispatchTo(), oidMaterial, rs.getLong(5), DOC_TYPE_DISPATCH,matDispatch.getDispatchDate());
                cekInsertUpdateStockCode(oidLocation, matDispatch.getDispatchTo(), oidMaterial, rs.getLong(5), DOC_TYPE_DISPATCH,0,matDispatch.getDispatchDate(),0);

            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("DF : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }



    /**
     * @param postingDate
     * @param oidLocation
     * @param oidPeriode
     * @return
     * @update by Edhy
     *  - Inc Qty Out in MaterialStock
     *  - Dec Qty in MaterialStock
     *  - Set document status into posted
     */
    private Vector PostingDFToStock(Date postingDate, long oidLocation, long oidPeriode, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;

        try {
            // Select all receive today with detail
            String sql = "SELECT DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] +
                    ", DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID] +
                    ", DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    " FROM " + PstMatDispatch.TBL_DISPATCH + " DF" +
                    " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI" +
                    " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

            if (postedDateCheck) {
                sql = sql + " WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                        " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                        " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                Date dStartDatePeriod = null;
                Date dEndDatePeriod = null;
                try {
                    Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                    dStartDatePeriod = objMaterialPeriode.getStartDate();
                    dEndDatePeriod = objMaterialPeriode.getEndDate();
                } catch (Exception e) {
                    System.out.println("Exc " + new SessClosing().getClass().getName() + ".PostingDFToStock() - fetch period : " + e.toString());
                }

                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                            " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:00:01") + "'" +
                            " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:59:59") + "'";
                }

                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sWhereClause = sWhereClause + " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                } else {
                    sWhereClause = " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }

                sql = sql + " WHERE " + sWhereClause;
            }

            // System.out.println(">>> " + new SessClosing().getClass().getName() + ".PostingDFToStock() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidDF = rs.getLong(1);
                long oidMaterial = rs.getLong(2);
                double dfQty = rs.getDouble(3);
                long oidNewLocation = rs.getLong(6);
                boolean isOK = false;

                // jumlah barang per dalam buying unit relatif terhadap sell/base unit
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(4), rs.getLong(5));

                // jumlah barang yg akan masuk stock
                dfQty = dfQty * qtyPerSellingUnit;

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQty, MODE_UPDATE, DOC_TYPE_DISPATCH, oidPeriode);
                } else {
                    //Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQty, MODE_INSERT, DOC_TYPE_DISPATCH, oidPeriode);
                }

                // Set status document receive menjadi posted
                if (isOK == true) {
                    isOK = setPosted(oidDF, DOC_TYPE_DISPATCH);
                }

                if (!isOK) {
                    try {
                        BillMain objBillMain = PstBillMain.fetchExc(oidDF);
                        vResult.add(objBillMain);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                    break;
                }

                // Proses insert/update serial stock codenya
                cekInsertUpdateStockCode(oidLocation, oidNewLocation, oidMaterial, rs.getLong(7), DOC_TYPE_DISPATCH,0,rs.getDate( PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]),0);

            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("DF : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
    }


    /**
     * This method using to posting DF document without df item include during specified period
     * @param oidPeriode
     * @param postingDate
     * @param oidLocation
     * @param postedDateCheck
     * @return
     * @created by Edhy
     * algoritm :
     *  - list all df document that still draft
     *  - iterate process of set df document status to POSTED without care about stock
     */
    private Vector listDFDocWithoutItem(long oidPeriode, Date postingDate, long oidLocation, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);

        String sWhereClause = "";
        if (postedDateCheck) {
            sWhereClause = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                    " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                    " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                    " = " + oidLocation;
        } else {
            Date dStartDatePeriod = null;
            Date dEndDatePeriod = null;
            try {
                Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                dStartDatePeriod = objMaterialPeriode.getStartDate();
                dEndDatePeriod = objMaterialPeriode.getEndDate();
            } catch (Exception e) {
                System.out.println("Exc " + new SessClosing().getClass().getName() + ".listDFDocWithoutItem() - fetch period : " + e.toString());
            }

            if (dStartDatePeriod != null && dEndDatePeriod != null) {
                sWhereClause = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                        " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:59:59") + "'";
            }

            if (sWhereClause != null && sWhereClause.length() > 0) {
                sWhereClause = sWhereClause + " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                sWhereClause = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            }
        }

        Vector vListUnPostedDFDoc = PstMatDispatch.list(0, 0, sWhereClause, "");
        if (vListUnPostedDFDoc != null && vListUnPostedDFDoc.size() > 0) {
            int unPostedDFDocCount = vListUnPostedDFDoc.size();
            for (int i = 0; i < unPostedDFDocCount; i++) {
                MatDispatch objMatDispatch = (MatDispatch) vListUnPostedDFDoc.get(i);
                vResult.add(objMatDispatch);
            }
        }

        return vResult;
    }
    // ------------------------------- POSTING DISPATCH FINISH ---------------------------




    // ------------------------------- POSTING COST START ---------------------------
    /**
     * This method use to posting dispatch document
     * @param dPostingDate
     * @param lLocationOid
     * @param lPeriodeOid
     * @param bPostedDateCheck
     * @return
     * @created by Edhy
     * algoritm :
     *  - posting all costing document during specified time interval (with df item)
     *  - if there are costing document cannot posting, do posting process for outstanding trans document (without df item)
     */
    private Vector getUnPostedCostDocument(Date postingDate, long oidLocation, long oidPeriode, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        String sStartTime = "";
        String sEndTime = "";
        Vector vTimeDuration = getTimeDurationOfTransDocument(oidPeriode);
        if (vTimeDuration != null && vTimeDuration.size() > 3) {
            dStartDatePeriod = (Date) vTimeDuration.get(0);
            dEndDatePeriod = (Date) vTimeDuration.get(1);
            sStartTime = (String) vTimeDuration.get(2);
            sEndTime = (String) vTimeDuration.get(3);
        }
        // --- finish get time duration of transaction document that will posting ---

        try {
            // Select all receive today with detail
            String sql = "SELECT " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " FROM " + PstMatCosting.TBL_COSTING;

            if (postedDateCheck) {
                sql = sql + " WHERE " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                        " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                        " = " + oidLocation;
                /*" AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                " BETWEEN \"" + Formater.formatDate(postingDate, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                " AND \"" + Formater.formatDate(postingDate, "yyyy-MM-dd") + " " + sEndTime + "\"";*/
            } else {
                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                    /*" AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                    " AND \"" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd") + " " + sEndTime + "\"";*/
                } else {
                    sWhereClause = PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }

                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sql = sql + " WHERE " + sWhereClause;
                }
            }

            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".getUnPostedDFDocument() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidDF = rs.getLong(1);

                Vector vErrUpdateStockByCostingItem = updateStockByCostingItem(oidPeriode, oidLocation, oidDF);

                // Set status document receive menjadi posted
                boolean isOK = false;
                if (!(vErrUpdateStockByCostingItem != null && vErrUpdateStockByCostingItem.size() > 0)) {
                    isOK = setPosted(oidDF, DOC_TYPE_COSTING);
                }

                if (!isOK) {
                    try {
                        MatCosting objBillMain = PstMatCosting.fetchExc(oidDF);
                        vResult.add(objBillMain);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                    break;
                }
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("DF : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;


        /*Vector vResult = new Vector(1, 1);
        // posting Costing document that have df item
        vResult = PostingCostToStock(dPostingDate, lLocationOid, lPeriodeOid, bPostedDateCheck);
        // posting Costing document that haven't df item
        Vector vUnPostedCosting = listCostDocWithoutItem(lPeriodeOid, dPostingDate, lLocationOid, bPostedDateCheck);
        if (vUnPostedCosting != null && vUnPostedCosting.size() > 0) {
            vResult.addAll(vUnPostedCosting);
        }
        return vResult;*/

    }


    synchronized private Vector updateStockByCostingItem(long oidPeriode, long oidLocation, long lCostingOid) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        String sStartTime = "";
        String sEndTime = "";
        Vector vTimeDuration = getTimeDurationOfTransDocument(oidPeriode);
        if (vTimeDuration != null && vTimeDuration.size() > 3) {
            dStartDatePeriod = (Date) vTimeDuration.get(0);
            dEndDatePeriod = (Date) vTimeDuration.get(1);
            sStartTime = (String) vTimeDuration.get(2);
            sEndTime = (String) vTimeDuration.get(3);
        }
        // --- finish get time duration of transaction document that will posting ---
        

        try {
             MatCosting matCosting = PstMatCosting.fetchExc(lCostingOid);
            // Select all receive today with detail
            String sql = "SELECT DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    ", DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID] +
                    ", DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID] +
                    //check material type by mirahu 08072011
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " FROM " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " DFI" +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE DFI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " = " + lCostingOid +
                    " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " != " + PstMaterial.MAT_TYPE_SERVICE;

            //System.out.println(">>> " + new SessPosting().getClass().getName() + ".updateStockByDispatchItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                long oidMaterial = rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]);
                double dfQty = rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY]);
                boolean isOK = false;
                
                double qtyPerBaseUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double dfQtyReal = dfQty * qtyPerBaseUnit; // jumlah barang yg akan masuk stock                

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQtyReal, MODE_UPDATE, DOC_TYPE_COSTING, oidPeriode);
                } else {
                    //Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQtyReal, MODE_INSERT, DOC_TYPE_COSTING, oidPeriode);
                }

                if (!isOK) {
                    vResult.add(oidMaterial + " on " + lCostingOid);
                }

                // Proses insert/update serial stock codenya
                //Date dateCosting = new Date();
                cekInsertUpdateStockCode(oidLocation, oidLocation, oidMaterial, rs.getLong(5), DOC_TYPE_COSTING,0,matCosting.getCostingDate(),0);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("DF : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }


    /**
     * @param postingDate
     * @param oidLocation
     * @param oidPeriode
     * @return
     * @update by Edhy
     *  - Inc Qty Out in MaterialStock
     *  - Dec Qty in MaterialStock
     *  - Set document status into posted
     */
    private Vector PostingCostToStock(Date postingDate, long oidLocation, long oidPeriode, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            // Select all receive today with detail
            String sql = "SELECT CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    ", CSI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    ", CSI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] +
                    " FROM " + PstMatCosting.TBL_COSTING + " CST" +
                    " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " CSI" +
                    " ON CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " = CSI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON CSI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

            if (postedDateCheck) {
                sql = sql + " WHERE CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                        " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                        " AND CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                Date dStartDatePeriod = null;
                Date dEndDatePeriod = null;
                try {
                    Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                    dStartDatePeriod = objMaterialPeriode.getStartDate();
                    dEndDatePeriod = objMaterialPeriode.getEndDate();
                } catch (Exception e) {
                    System.out.println("Exc " + new SessClosing().getClass().getName() + ".PostingCostToStock() - fetch period : " + e.toString());
                }
                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = " CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                            " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:00:01") + "'" +
                            " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:23:59") + "'";
                }

                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sWhereClause = sWhereClause + " AND CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                } else {
                    sWhereClause = " CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND CST." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }

                sql = sql + " WHERE " + sWhereClause;
            }

            //System.out.println(">>> " + new SessClosing().getClass().getName() + ".PostingCostToStock() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidCST = rs.getLong(1);
                long oidMaterial = rs.getLong(2);
                double dfQty = rs.getDouble(3);
                long oidNewLocation = rs.getLong(6);
                boolean isOK = false;
                
                // jumlah barang per dalam buying unit relatif terhadap sell/base unit
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(4), rs.getLong(5));

                // jumlah barang yg akan masuk stock
                dfQty = dfQty * qtyPerSellingUnit;

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQty, MODE_UPDATE, DOC_TYPE_DISPATCH, oidPeriode);
                } else {
                    //Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, dfQty, MODE_INSERT, DOC_TYPE_DISPATCH, oidPeriode);
                }

                // Set status document receive menjadi posted
                if (isOK == true) {
                    isOK = setPosted(oidCST, DOC_TYPE_COSTING);
                }

                if (!isOK) {
                    try {
                        BillMain objBillMain = PstBillMain.fetchExc(oidCST);
                        vResult.add(objBillMain);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                    break;
                }
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("COST : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
    }


    /**
     * This method using to posting Cost document without df item include during specified period
     * @param oidPeriode
     * @param postingDate
     * @param oidLocation
     * @param postedDateCheck
     * @return
     * @created by Edhy
     * algoritm :
     *  - list all df document that still draft
     *  - iterate process of set cost document status to POSTED without care about stock
     */
    private Vector listCostDocWithoutItem(long oidPeriode, Date postingDate, long oidLocation, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        String sWhereClause = "";
        if (postedDateCheck) {
            sWhereClause = PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                    " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                    " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                    " = " + oidLocation;
        } else {
            Date dStartDatePeriod = null;
            Date dEndDatePeriod = null;
            try {
                Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                dStartDatePeriod = objMaterialPeriode.getStartDate();
                dEndDatePeriod = objMaterialPeriode.getEndDate();
            } catch (Exception e) {
                System.out.println("Exc " + new SessClosing().getClass().getName() + ".listCostDocWithoutItem() - fetch period : " + e.toString());
            }

            if (dStartDatePeriod != null && dEndDatePeriod != null) {
                sWhereClause = PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                        " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:59:59") + "'";
            }

            if (sWhereClause != null && sWhereClause.length() > 0) {
                sWhereClause = sWhereClause + " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                sWhereClause = PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            }
        }

        Vector vListUnPostedCostDoc = PstMatCosting.list(0, 0, sWhereClause, "");
        if (vListUnPostedCostDoc != null && vListUnPostedCostDoc.size() > 0) {
            int unPostedCostDocCount = vListUnPostedCostDoc.size();
            for (int i = 0; i < unPostedCostDocCount; i++) {
                MatCosting objMatCosting = (MatCosting) vListUnPostedCostDoc.get(i);
                vResult.add(objMatCosting);
            }
        }

        return vResult;
    }
    // ------------------------------- POSTING COSTING FINISH ---------------------------



    // ------------------------------- POSTING SALES START ---------------------------
    /**
     * This method use to posting sales document
     * @param dPostingDate
     * @param lLocationOid
     * @param lPeriodeOid
     * @param bPostedDateCheck
     * @return
     * @created by Edhy
     * algoritm :
     *  - posting all sales document during specified time interval (with sales item)
     *  - if there are sales document cannot posting, do posting process for outstanding trans document (without sales item)
     */
    private Vector getUnPostedSalesDocument(Date postingDate, long oidLocation, long oidPeriode, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        String sStartTime = "";
        String sEndTime = "";
        Vector vTimeDuration = getTimeDurationOfTransDocument(oidPeriode);
        if (vTimeDuration != null && vTimeDuration.size() > 3) {
            dStartDatePeriod = (Date) vTimeDuration.get(0);
            dEndDatePeriod = (Date) vTimeDuration.get(1);
            sStartTime = (String) vTimeDuration.get(2);
            sEndTime = (String) vTimeDuration.get(3);
        }
        // --- finish get time duration of transaction document that will posting ---

        try {
            // Select all sale today with detail
            //String sql = "SELECT " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    //" FROM " + PstBillMain.TBL_CASH_BILL_MAIN;


            String sql = "SELECT CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM " +
                    " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " CSH " +
                    " ON CBM." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
                    " = CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] ;

            if (postedDateCheck) { 
                sql = sql + " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                        " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                        " = " + oidLocation +
                        " AND " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                        " BETWEEN \"" + Formater.formatDate(postingDate, "yyyy-MM-dd") + " 00:00:00\"" +
                        " AND \"" + Formater.formatDate(postingDate, "yyyy-MM-dd") + " 23:59:59\"";
            } else { 
                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                            " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " 00:00:00\"" +
                            " AND \"" + Formater.formatDate(postingDate, "yyyy-MM-dd") + " 23:59:59\"";
                } else {
                    sWhereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }

                sWhereClause = sWhereClause + " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                        " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";

                sWhereClause = sWhereClause + " AND CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] +
                        " != 1";
                
                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sql = sql + " WHERE " + sWhereClause;
                }
                //order by cash_cashier
                sql = sql + " ORDER BY CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID];
            }

            System.out.println(">>> " + new SessPosting().getClass().getName() + ".getUnPostedSalesDocument() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidSL = rs.getLong(1);
                //cash cashier id
                long oidCashCashier = rs.getLong(2);
                
                //Vector vErrUpdateStockBySaleItem = updateStockBySalesItem(postingDate, oidSL, oidLocation, oidPeriode);
                Vector vErrUpdateStockBySaleItem = updateStockBySalesItem(postingDate, oidSL, oidLocation, oidPeriode, oidCashCashier);

                // Set status document receive menjadi posted
                boolean isOK = false;
                if (!(vErrUpdateStockBySaleItem != null && vErrUpdateStockBySaleItem.size() > 0)) {
                    isOK = setPosted(oidSL, DOC_TYPE_SALE_REGULAR);
                }


                if (!isOK) {
                    try {
                        BillMain objBillMain = PstBillMain.fetchExc(oidSL);
                        vResult.add(objBillMain);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                }
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc when Process SALE : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;

        /*        Vector vResult = new Vector(1, 1);
        System.out.println("dPostingDate : " + dPostingDate);
        System.out.println("lLocationOid : " + lLocationOid);
        System.out.println("lPeriodeOid : " + lPeriodeOid);
        System.out.println("bPostedDateCheck : " + bPostedDateCheck);

        // posting sales document that have sales item
        Vector vResultTemp = PostingSaleToStock(dPostingDate, lLocationOid, lPeriodeOid, bPostedDateCheck);

        // posting sales document that haven't sales item
        Vector vUnPostedSales = PostingSaleWithoutItem(lPeriodeOid, dPostingDate, lLocationOid, bPostedDateCheck);
        if (vUnPostedSales != null && vUnPostedSales.size() > 0) {
            vResult.addAll(vUnPostedSales);
        }
        return vResult;*/
    }


    //synchronized private Vector updateStockBySalesItem(Date postingDate, long lBillMainOid, long oidLocation, long oidPeriode) {
     synchronized private Vector updateStockBySalesItem(Date postingDate, long lBillMainOid, long oidLocation, long oidPeriode, long oidCashCashier) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);

        try {
            // Select all receive today with detail
            String sql = "SELECT BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE] +
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID] +
                    ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID] +
                    ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    //for make a new doc costing from composit
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    ", CASH." + PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE]+
                    " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD " +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT " +
                    " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    //inner join cash_cashier
                    " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS CBM " +
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = BD." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " AS CASH " +
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+
                    " = CASH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+
                    //end of cash_cashier
                    " WHERE CBM." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                    " = " + lBillMainOid;
            
            //System.out.println(">>> "+ new SessPosting().getClass().getName()+".updateStockBySalesItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            BillMain billMain = new BillMain();
            try {
                billMain = PstBillMain.fetchExc(lBillMainOid);
            } catch (Exception e) {
                System.out.println("Exc. when fetch BillMain in updateStockBysalesItem(#,#,#,#): "+e.toString());
            }

            while (rs.next()) {
                long oidMaterial = rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]); //rs.getLong(1);
                double slQty = rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]);//rs.getDouble(2);
                int materialType = rs.getInt(PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE]);//rs.getInt(3); // kalau SQL Value = NULL, maka nilai materialType adalah 0 (Regular)
                double qtyPerBaseUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double sellQtyReal = slQty * qtyPerBaseUnit;
                long oidCashCashierDetail = rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]);
                //Date billDateMaterial = rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]);
                Date billDateMaterial = rs.getTimestamp(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]);
                Date cashCashierDate = rs.getTimestamp(PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE]);
                
                //System.out.println(">>> slQty: "+slQty+"\n>>>sellQtyReal: "+sellQtyReal);
                
                Thread.sleep(50);
                boolean isOK = false;
                switch (materialType) {
                    case PstMaterial.MATERIAL_TYPE_REGULAR:
                        // Check if this item is allready exists in MaterialStock
                        if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                            // Update Qty only
                            if (sellQtyReal != 0) {
                                if (billMain.getDocType() == PstBillMain.TYPE_INVOICE)
                                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, sellQtyReal, MODE_UPDATE, DOC_TYPE_SALE_REGULAR, oidPeriode);
                                else
                                    isOK = updateMaterialStock(oidMaterial, oidLocation, sellQtyReal, 0, MODE_UPDATE, DOC_TYPE_RECEIVE, oidPeriode); // retur
                            }
                        } else {
                            // Insert Stock and its qty
                            if (billMain.getDocType() == PstBillMain.TYPE_INVOICE) {
                                //sellQtyReal = 0 - sellQtyReal; // karena sudah ada penjualan maka qty menjadi min.
                                isOK = updateMaterialStock(oidMaterial, oidLocation, 0, sellQtyReal, MODE_INSERT, DOC_TYPE_SALE_REGULAR, oidPeriode);
                            } else {
                                isOK = updateMaterialStock(oidMaterial, oidLocation, sellQtyReal, 0, MODE_INSERT, DOC_TYPE_RECEIVE, oidPeriode);
                            }
                        }
                        
                        if (!isOK) {
                            vResult.add(oidMaterial + " on " + lBillMainOid);
                        }
                        break;

                    case PstMaterial.MATERIAL_TYPE_COMPOSITE:
                         long oid = 0;
                         Date costingDate = new Date();
                         int costingStatus = 0;
                         String costingRemark = "";
                         String costingCode = "";
                         int costingCodeCounter = 0;
                         long oidCashCashierCosting = 0;

                          PstMatCosting pstMatCosting = new PstMatCosting();
                          SessMatCosting sessCosting = new SessMatCosting();
                          MatCosting matCosting = new MatCosting();

                          Vector getListCostingMaterial = new Vector();
                          getListCostingMaterial = getCostingMaterial(oidCashCashier);

                          if(getListCostingMaterial !=null && getListCostingMaterial .size()>0) {
                           for(int i = 0; i<getListCostingMaterial.size(); i++){
                             matCosting = (MatCosting)getListCostingMaterial.get(i);
                             oid = matCosting.getOID();
                             costingDate = matCosting.getCostingDate();
                             costingStatus = matCosting.getCostingStatus();
                             costingRemark = matCosting.getRemark();
                             costingCode = matCosting.getCostingCode();
                             costingCodeCounter = matCosting.getCostingCodeCounter();
                             oidCashCashierCosting = matCosting.getCashCashierId();
                          }
                        }

                        if((oidCashCashierDetail != oidCashCashierCosting) || oidCashCashierCosting ==0){
                           
                           matCosting.setLocationId(oidLocation);
                           matCosting.setLocationType(1);
                           matCosting.setCostingDate(cashCashierDate);
                           matCosting.setCostingStatus(2);
                           matCosting.setRemark("Generated by System for Composite CoGS");

                            int maxCounter = sessCosting.getMaxCostingCounter(matCosting.getCostingDate(), matCosting);
                           maxCounter = maxCounter + 1;

                           matCosting.setCostingCodeCounter(maxCounter);
                           matCosting.setCostingCode(sessCosting.generateCostingCode(matCosting));
                           matCosting.setCostingTo(oidLocation);
                           matCosting.setCostingId(0);
                           matCosting.setCashCashierId(oidCashCashier);
                           matCosting.setLastUpdate(new Date());
                          
                           
                           oid = pstMatCosting.insertExc(matCosting);


                           //Vector vListComposit = updateStockBySalesItem(postingDate, oidSL, oidLocation, oidPeriode, oidCashCashier);
                    }

                         if(oidCashCashierDetail == oidCashCashierCosting){
                             matCosting.setOID(oid);
                             matCosting.setLocationId(oidLocation);
                             matCosting.setLocationType(1);
                             matCosting.setCostingDate(costingDate);
                             matCosting.setCostingStatus(2);
                             matCosting.setRemark(costingRemark);
                             matCosting.setCostingCodeCounter(costingCodeCounter);
                             matCosting.setCostingCode(costingCode);
                             matCosting.setCostingTo(oidLocation);
                             matCosting.setCostingId(0);
                             matCosting.setLastUpdate(new Date());
                            oid = pstMatCosting.updateExc(matCosting);
                        }


                        long oidComposit = 0;
                        long materialId = 0;
                        long materialComponentId = 0;
                        double qty = 0;
                        long unitId = 0;
                        double stockValue = 0;
                        double costValue = 0;
                        int materialTypeComponent = 0;


                        Vector listItemCosting = new Vector();
                        listItemCosting = ListComponentComposit(oidMaterial, oid, sellQtyReal);
                        //listItemCosting = ListComponentComposit2(oidMaterial, oid, sellQtyReal);

                       /* if(listItemCosting !=null && listItemCosting.size()>0) {
                           for(int i = 0; i<listItemCosting.size(); i++){
                             Vector temp = (Vector)getListCostingMaterial.get(i);
                             MaterialComposit matComposit = (MaterialComposit)temp.get(0);
                             Material material = (Material)temp.get(1);

                             oidComposit = matComposit.getOID();
                             materialId = matComposit.getMaterialId();
                             materialComponentId = matComposit.getMaterialComposerId();
                             qty = matComposit.getQty();
                             unitId = matComposit.getUnitId();
                             stockValue = material.getAveragePrice();
                             costValue = material.getDefaultCost();
                             materialTypeComponent = material.getMaterialType();

                             if(materialTypeComponent == PstMaterial.MAT_TYPE_COMPOSITE){
                                listItemCosting = ListComponentComposit2(oidMaterial, oid, sellQtyReal);
                             }
                          }
                        }*/

                        
                        //Vector listComposer = getMaterialComposer(oidMaterial);
                        //Vector listComponentComposit = getMaterialComposer(oidMaterial);
                        // Decrement Qty composernya
                        //Vector listComposer = getMaterialComposer(oidMaterial);
                        //for (int i = 0; i < listComposer.size(); i++) {
                            //MaterialComposit matCom = (MaterialComposit) listComposer.get(i);
                            //isOK = updateMaterialStock(matCom.getMaterialComposerId(), oidLocation, 0, (sellQtyReal * matCom.getQty()), MODE_UPDATE, DOC_TYPE_SALE_REGULAR, oidPeriode);
                            //if (isOK == false) break;
                       // }

                        // Create automatic transaction for each material composer
                        // Insert Bill Main
                        //BillMain bm = new BillMain();
                        //bm.setAppUserId(0);
                        //bm.setBillDate(postingDate);
                        //bm.setBillStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                        //bm.setInvoiceNo("AUTO-COMPOSIT");
                        //bm.setLocationId(oidLocation);
                        //long oidBillMain = PstBillMain.insertExc(bm);

                        // Insert Bill Detail
                        //for (int j = 0; j < listComposer.size(); j++) {
                            //Billdetail bd = new Billdetail();
                            //MaterialComposit matCom = (MaterialComposit) listComposer.get(j);

                            // Fetch Material Info
                            //Material mat = new Material();
                            //try {
                                //mat = PstMaterial.fetchExc(matCom.getMaterialComposerId());
                          //  } catch (Exception ex) {
                               // System.out.println("Exc when fetch material composer : " + ex.toString());
                           // }
                            //bd.setBillMainId(oidBillMain);
                            //bd.setMaterialId(matCom.getMaterialComposerId());
                           // bd.setMaterialType(PstMaterial.MATERIAL_TYPE_REGULAR);
                            //bd.setQty(matCom.getQty());
                           // bd.setSku(mat.getSku());
                            //bd.setItemName(mat.getName());
                           // bd.setUnitId(mat.getDefaultStockUnitId());
                            //long oidBD = PstBillDetail.insertExc(bd);
                       // }
                        
                        // Update compositnya
                        //if (isOK) {
                           // isOK = updateMaterialStock(oidMaterial, oidLocation, 0, sellQtyReal, MODE_UPDATE, DOC_TYPE_SALE_COMPOSIT, oidPeriode);
                        //}
                        
                        ///System.out.println("isOK COMPOSIT : " + isOK);
                        //if (!isOK) {
                            //vResult.add(oidMaterial + " on " + lBillMainOid);
                        //}
                        break;
                }
                
                // update serial code stock
                Date dateSales = new Date();
                cekInsertUpdateStockCode(0, oidLocation, oidMaterial, rs.getLong(4), DOC_TYPE_SALE_REGULAR,0,dateSales,0);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("SALE : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }

    synchronized private Vector updateStockByProductionItemCost(long oidPeriode, long oidLocation, long oidProduction) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);

        try {
            // Select all production item with detail
            String sql = "SELECT "
                    + "  PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID]
                    + " ,PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_QTY]
                    + " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
                    + " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + " ,PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_COST_ID]
                    + " FROM " + PstProductionCost.TBL_PRODUCTION_COST + " PC "
                    + "  INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT "
                    + "    ON PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID]
                    + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + "  INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " PG "
                    + "    ON PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + " = PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID]
                    + " WHERE PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID] + " = '" + oidProduction + "'"
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Production production = new Production();
            try {
                production = PstProduction.fetchExc(oidProduction);
            } catch (Exception e) {
                System.out.println("Exc. when fetch Production in updateStockByProductionItem(#,#,#,#): " + e.toString());
            }

            while (rs.next()) {
                boolean isOK = false;
                long oidProductionCost = rs.getLong(PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_COST_ID]);
                long oidMaterial = rs.getLong(PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID]);
                double qtyPerBaseUnit = 1;//PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double qty = rs.getDouble(PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_QTY]);
                double qtyReal = qty * qtyPerBaseUnit;

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, qtyReal, MODE_UPDATE, DOC_TYPE_PRODUCTION, oidPeriode);
                } else {
                    //Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, qtyReal, MODE_INSERT, DOC_TYPE_PRODUCTION, oidPeriode);
                }

                if (!isOK) {
                    vResult.add(oidMaterial + " on " + oidProduction);
                }

                // Proses insert/update serial stock codenya
                cekInsertUpdateStockCode(oidLocation, production.getLocationToId(), oidMaterial, oidProductionCost, DOC_TYPE_PRODUCTION, 0, production.getProductionDate(), 0);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("PRODUCTION : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
    }

    synchronized private Vector updateStockByProductionItemProduct(long oidPeriode, long oidLocation, long oidProduction) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);

        try {
            // Select all production item with detail
            String sql = "SELECT "
                    + "  PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]
                    + " ,PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]
                    + " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
                    + " ,MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
                    + " ,PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID]
                    + " FROM " + PstProductionProduct.TBL_PRODUCTION_PRODUCT + " PP "
                    + "  INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT "
                    + "    ON PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]
                    + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + "  INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " PG "
                    + "    ON PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
                    + " = PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID]
                    + " WHERE PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID] + " = '" + oidProduction + "'"
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Production production = new Production();
            try {
                production = PstProduction.fetchExc(oidProduction);
            } catch (Exception e) {
                System.out.println("Exc. when fetch Production in updateStockByProductionItem(#,#,#,#): " + e.toString());
            }

            while (rs.next()) {
                boolean isOK = false;
                long oidProductionCost = rs.getLong(PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID]);
                long oidMaterial = rs.getLong(PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]);
                double qtyPerBaseUnit = 1;//PstUnit.getQtyPerBaseUnit(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double qty = rs.getDouble(PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]);
                double qtyReal = qty * qtyPerBaseUnit;

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, qtyReal, MODE_UPDATE, DOC_TYPE_PRODUCTION, oidPeriode);
                } else {
                    //Update Qty only
                    isOK = updateMaterialStock(oidMaterial, oidLocation, 0, qtyReal, MODE_INSERT, DOC_TYPE_PRODUCTION, oidPeriode);
                }

                if (!isOK) {
                    vResult.add(oidMaterial + " on " + oidProduction);
                }

                // Proses insert/update serial stock codenya
                cekInsertUpdateStockCode(oidLocation, production.getLocationToId(), oidMaterial, oidProductionCost, DOC_TYPE_PRODUCTION, 0, production.getProductionDate(), 0);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("PRODUCTION : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
    }

    /**
     * This method using to posting sales document with sales item include during specified period
     * @param postingDate
     * @param oidLocation
     * @param oidPeriode
     * @return
     * @update by Edhy
     * algoritm :
     *  - Inc Qty Out in MaterialStock
     *  - Dec Qty in MaterialStock
     *  - Set document status into posted
     */
    private Vector PostingSaleToStock(Date postingDate, long oidLocation, long oidPeriode, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);
        DBResultSet dbrs = null;

        try {
            // Select all sale today with detail
            String sql = "SELECT CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE] +
                    ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID] +
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
                    " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD" +
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID];

            if (postedDateCheck) {
                sql = sql + " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                        " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                        " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                Date dStartDatePeriod = null;
                Date dEndDatePeriod = null;
                try {
                    Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                    dStartDatePeriod = objMaterialPeriode.getStartDate();
                    dEndDatePeriod = objMaterialPeriode.getEndDate();
                } catch (Exception e) {
                    System.out.println("Exc " + new SessClosing().getClass().getName() + ".PostingSaleToStock() - fetch period : " + e.toString());
                }

                String sWhereClause = "";
                if (dStartDatePeriod != null && dEndDatePeriod != null) {
                    sWhereClause = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                            " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:00:01") + "'" +
                            " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:59:59") + "'";
                }

                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sWhereClause = sWhereClause + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                } else {
                    sWhereClause = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + oidLocation;
                }

                sql = sql + " WHERE " + sWhereClause;
            }

            //System.out.println(">>> " + new SessClosing().getClass().getName() + ".PostingSaleToStock() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                // Fetch all we needed
                long oidSL = rs.getLong(1);
                long oidMaterial = rs.getLong(2);
                double slQty = rs.getDouble(3);
                int materialType = rs.getInt(4); // kalau SQL Value = NULL, maka nilai materialType adalah 0 (Regular)
                
                boolean isOK = false;
                switch (materialType) {
                    case PstMaterial.MATERIAL_TYPE_REGULAR:
                        // Check if this item is allready exists in MaterialStock
                        //System.out.println("oidMaterial : " + oidMaterial);
                        //System.out.println("oidLocation : " + oidLocation);
                        //System.out.println("oidPeriode : " + oidPeriode);

                        if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {

                            //System.out.println("true slQty : " + slQty);
                            // Update Qty only
                            if (slQty > 0) {
                                isOK = updateMaterialStock(oidMaterial, oidLocation, 0, slQty, MODE_UPDATE, DOC_TYPE_SALE_REGULAR, oidPeriode);
                                //System.out.println("true isOK : " + isOK);
                            }
                        } else {
                            //System.out.println("false slQty : " + slQty);
                            // Insert Stock and its qty
                            isOK = updateMaterialStock(oidMaterial, oidLocation, 0, slQty, MODE_INSERT, DOC_TYPE_SALE_REGULAR, oidPeriode);
                            //System.out.println("false isOK : " + isOK);
                        }
                        break;

                    case PstMaterial.MATERIAL_TYPE_COMPOSITE:

                        //System.out.println("MATERIAL_TYPE_COMPOSITE oidMaterial : " + oidMaterial);
                        //System.out.println("MATERIAL_TYPE_COMPOSITE oidLocation : " + oidLocation);
                        //System.out.println("MATERIAL_TYPE_COMPOSITE oidPeriode : " + oidPeriode);

                        // Decrement Qty composernya
                        /*Vector listComposer = getMaterialComposer(oidMaterial);
                        for (int i = 0; i < listComposer.size(); i++) {
                            MaterialComposit matCom = (MaterialComposit) listComposer.get(i);
                            isOK = updateMaterialStock(matCom.getMaterialComposerId(), oidLocation, 0, (slQty * matCom.getQty()), MODE_UPDATE, DOC_TYPE_SALE_REGULAR, oidPeriode);
                            if (isOK == false) break;
                        }

                        // Create automatic transaction for each material composer
                        // Insert Bill Main
                        BillMain bm = new BillMain();
                        bm.setAppUserId(0);
                        bm.setBillDate(postingDate);
                        bm.setBillStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                        bm.setInvoiceNo("AUTO-COMPOSIT");
                        bm.setLocationId(oidLocation);
                        long oidBillMain = PstBillMain.insertExc(bm);

                        // Insert Bill Detail
                        for (int j = 0; j < listComposer.size(); j++) {
                            Billdetail bd = new Billdetail();
                            MaterialComposit matCom = (MaterialComposit) listComposer.get(j);

                            // Fetch Material Info
                            Material mat = new Material();
                            try {
                                mat = PstMaterial.fetchExc(matCom.getMaterialComposerId());
                            } catch (Exception ex) {
                                System.out.println("Exc when fetch material composer : " + ex.toString());
                            }
                            bd.setBillMainId(oidBillMain);
                            bd.setMaterialId(matCom.getMaterialComposerId());
                            bd.setMaterialType(PstMaterial.MATERIAL_TYPE_REGULAR);
                            bd.setQty(matCom.getQty());
                            bd.setSku(mat.getSku());
                            bd.setItemName(mat.getName());
                            bd.setUnitId(mat.getDefaultStockUnitId());
                            long oidBD = PstBillDetail.insertExc(bd);
                        }

                        // Update compositnya
                        if (isOK == true) {
                            isOK = updateMaterialStock(oidMaterial, oidLocation, 0, slQty, MODE_UPDATE, DOC_TYPE_SALE_COMPOSIT, oidPeriode);
                        }
                        break;*/
                }

                // Set status document receive menjadi posted
                if (isOK == true) {
                    isOK = setPosted(oidSL, DOC_TYPE_SALE_REGULAR);
                }

                // update serial code stock
                cekInsertUpdateStockCode(0, oidLocation, oidMaterial, rs.getLong(5), DOC_TYPE_SALE_REGULAR,0, rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]),0);

                if (!isOK) {
                    try {
                        BillMain objBillMain = PstBillMain.fetchExc(oidSL);
                        vResult.add(objBillMain);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                    break;
                }
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc when Process SALE : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return vResult;
    }


    /**
     * This method using to posting sales document without sales item include during specified period
     * @param oidPeriode
     * @param postingDate
     * @param oidLocation
     * @param postedDateCheck
     * @return
     * @created by Edhy
     * algoritm :
     *  - list all sales document that still draft
     *  - iterate process of set sales document status to POSTED without care about stock
     */
    private Vector PostingSaleWithoutItem(long oidPeriode, Date postingDate, long oidLocation, boolean postedDateCheck) {
        Vector vResult = new Vector(1, 1);

        String sWhereClause = "";
        if (postedDateCheck) {
            sWhereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " BETWEEN '" + Formater.formatDate(postingDate, "yyyy-MM-dd 00:00:01") + "'" +
                    " AND '" + Formater.formatDate(postingDate, "yyyy-MM-dd 23:59:59") + "'" +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                    " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    " = " + oidLocation;
        } else {
            Date dStartDatePeriod = null;
            Date dEndDatePeriod = null;
            try {
                Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
                dStartDatePeriod = objMaterialPeriode.getStartDate();
                dEndDatePeriod = objMaterialPeriode.getEndDate();
            } catch (Exception e) {
                System.out.println("Exc " + new SessClosing().getClass().getName() + ".PostingSaleWithoutItem() - fetch period : " + e.toString());
            }

            if (dStartDatePeriod != null && dEndDatePeriod != null) {
                sWhereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                        " BETWEEN '" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd 00:00:01") + "'" +
                        " AND '" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd 23:59:59") + "'";
            }

            if (sWhereClause != null && sWhereClause.length() > 0) {
                sWhereClause = sWhereClause + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            } else {
                sWhereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                        " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT +
                        " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                        " = " + oidLocation;
            }
        }

        Vector vListUnPostedSalesDoc = PstBillMain.list(0, 0, sWhereClause, "");
        if (vListUnPostedSalesDoc != null && vListUnPostedSalesDoc.size() > 0) {
            int unPostedSalesDocCount = vListUnPostedSalesDoc.size();
            for (int i = 0; i < unPostedSalesDocCount; i++) {
                BillMain objBillMain = (BillMain) vListUnPostedSalesDoc.get(i);

                // check apakah ada cash bill detail
                // kalau tidak ada, update bill status menjadi posted tanpa mempengaruhi stock
                if (!salesItemExist(objBillMain.getOID())) {
                    try {
                        objBillMain.setBillStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                        long lResult = PstBillMain.updateExc(objBillMain);
                    } catch (Exception e) {
                        vResult.add(objBillMain);
                        System.out.println(">>> Exc " + new SessClosing().getClass().getName() + ".PostingSaleWithoutItem() - update bill main status : " + e.toString());
                    }
                }
            }
        }

        return vResult;
    }


    /**
     * This method used to check if sales item for specified bill document already exist or not
     * @param lBillMainOid
     * @return <CODE>true</CODE>if sales item already exist, otherwise <CODE>false</CODE>
     * @created by Edhy
     */
    private boolean salesItemExist(long lBillMainOid) {
        boolean lResult = false;

        String sWhereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + lBillMainOid;
        Vector vListSalesItem = PstBillDetail.list(0, 0, sWhereClause, "");
        if (vListSalesItem != null && vListSalesItem.size() > 0) {
            lResult = true;
        }

        return lResult;
    }
    // ------------------------------- POSTING SALES FINISH ---------------------------






    /**
     * Periksa apakah material sudah ada di MaterialStock
     * @param oidMaterial
     * @param oidLocation
     * @param oidPeriode
     * @return
     */
    private static boolean checkMaterialStock(long oidMaterial, long oidLocation, long oidPeriode) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Select all receive today with detail
            String sql = "SELECT * " +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK +
                    " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = " + oidMaterial +
                    " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + oidLocation +
                    " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + oidPeriode;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                hasil = true;
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("CekMatStock : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }


    /**
     * untuk pencarian data qty stock di material,
     * lokasi dan sesuai dengan periode terpilih
     * @param oidMaterial
     * @param oidLocation
     * @param oidPeriode
     * @return
     */
    private static double checkStockMaterial(long oidMaterial, long oidLocation, long oidPeriode) {
        double qty = 0.00;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]+
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK +
                    " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = " + oidMaterial;
            if(oidLocation > 0) {
                sql += " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + oidLocation;
            }
            sql += " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + oidPeriode;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                qty = rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("CekMatStock : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return qty;
    }


    /**
     *
     * @param oidDocItem
     * @param docType
     * @return
     */
    private static boolean cekInsertUpdateStockCode(long oidOldLocation, long oidNewLocation,
                                                    long oidMaterial, long oidDocItem, int docType, int recSouce, Date docDate, double cost) {
        DBResultSet dbrs = null;
        boolean hasil = false;
        try {
            String sql = "";
            switch (docType) {
                case DOC_TYPE_RECEIVE: // Receive
                    sql = "SELECT " +
                            PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_CODE_ID] +
                            "," + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] +
                            "," + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE] +
                            "," + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_VALUE] +
                            " FROM " + PstReceiveStockCode.TBL_POS_RECEIVE_MATERIAL_CODE +
                            " WHERE " + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + oidDocItem;
                    break;

                case DOC_TYPE_RETURN: //  return
                    sql = "SELECT " +
                            PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_CODE_ID] +
                            "," + PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID] +
                            "," + PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_CODE] +
                            "," + PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_VALUE] +
                            " FROM " + PstReturnStockCode.TBL_POS_RETURN_MATERIAL_CODE +
                            " WHERE " + PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID] + "=" + oidDocItem;
                    break;

                case DOC_TYPE_DISPATCH: // dispatch
                    sql = "SELECT " +
                            PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_CODE_ID] +
                            "," + PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID] +
                            "," + PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_STOCK_CODE] +
                            "," + PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_STOCK_VALUE] +
                            " FROM " + PstDispatchStockCode.TBL_POS_DISPATCH_MATERIAL_CODE +
                            " WHERE " + PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID] + "=" + oidDocItem;
                    break;

                case DOC_TYPE_COSTING: // costing
                    sql = "SELECT " +
                            PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_CODE_ID] +
                            "," + PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID] +
                            "," + PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_CODE] +
                            "," + PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_VALUE] +
                            " FROM " + PstCostingStockCode.TBL_POS_COSTING_MATERIAL_CODE +
                            " WHERE " + PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID] + "=" + oidDocItem;
                    break;

                case DOC_TYPE_SALE_REGULAR: // penjualan
                    sql = "SELECT " +
                            PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_CASH_BILL_DETAIL_CODE_ID] +
                            "," + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID] +
                            "," + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_CODE] +
                            "," + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_VALUE] +
                            " FROM " + PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE +
                            " WHERE " + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID] + "=" + oidDocItem;
                    break;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                switch (docType) {
                    case DOC_TYPE_RECEIVE: // Receive
                        switch(recSouce){
                            case PstMatReceive.SOURCE_FROM_SUPPLIER:
                                MaterialStockCode matStockCode = new MaterialStockCode();
                                matStockCode.setMaterialId(oidMaterial);
                                matStockCode.setLocationId(oidNewLocation);
                                matStockCode.setStockCode(rs.getString(3));
                                matStockCode.setStockValue(cost);
                                matStockCode.setDateStock(docDate);
                                matStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                try {
                                    PstMaterialStockCode.insertExc(matStockCode);
                                } catch (Exception e) {
                                    System.out.println("error insert stock : " + e.toString());
                                }
                                break;
                            case PstMatReceive.SOURCE_FROM_SUPPLIER_PO:
                                matStockCode = new MaterialStockCode();
                                matStockCode.setMaterialId(oidMaterial);
                                matStockCode.setLocationId(oidNewLocation);
                                matStockCode.setStockCode(rs.getString(3));
                                matStockCode.setStockValue(cost);
                                matStockCode.setDateStock(docDate);
                                matStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                try {
                                    PstMaterialStockCode.insertExc(matStockCode);
                                } catch (Exception e) {
                                    System.out.println("error insert stock : " + e.toString());
                                }
                                break;
                            case PstMatReceive.SOURCE_FROM_RETURN:
                                // update status serial code di lokasi return
                                String where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'" +
                                        " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID] + "=" + oidOldLocation;
                                Vector vect = PstMaterialStockCode.list(where, "");
                                if (vect != null && vect.size() > 0) {
                                    MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                                    stockCode.setLocationId(oidNewLocation);
                                    stockCode.setDateStock(docDate);
                                    stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                    try {
                                        PstMaterialStockCode.updateExc(stockCode);
                                    } catch (Exception e) {
                                        System.out.println("error update stock : " + e.toString());
                                    }
                                }
                                break;
                            case PstMatReceive.SOURCE_FROM_DISPATCH:
                                // update status serial code di lokasi return
                                where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'" +
                                        " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID] + "=" + oidOldLocation;
                                vect = PstMaterialStockCode.list(where, "");
                                if (vect != null && vect.size() > 0) {
                                    MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                                    stockCode.setLocationId(oidNewLocation);
                                    stockCode.setDateStock(docDate);
                                    stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                    try {
                                        PstMaterialStockCode.updateExc(stockCode);
                                    } catch (Exception e) {
                                        System.out.println("error update stock : " + e.toString());
                                    }
                                }else{
                                    matStockCode = new MaterialStockCode();
                                    matStockCode.setMaterialId(oidMaterial);
                                    matStockCode.setLocationId(oidNewLocation);
                                    matStockCode.setStockCode(rs.getString(3));
                                    matStockCode.setStockValue(rs.getDouble(4));
                                    matStockCode.setDateStock(docDate);
                                    matStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                    try {
                                        PstMaterialStockCode.insertExc(matStockCode);
                                    } catch (Exception e) {
                                        System.out.println("error insert stock : " + e.toString());
                                    }
                                }
                                break;
                        }
                        break;

                    case DOC_TYPE_RETURN: // retur
                        // update status serial code di lokasi return
                        String where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'" +
                                " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID] + "=" + oidNewLocation;
                        Vector vect = PstMaterialStockCode.list(where, "");
                        if (vect != null && vect.size() > 0) {
                            MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                            if(oidOldLocation==0)
                                stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_RETURN);
                            else
                                stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_BAD);
                            try {
                                PstMaterialStockCode.updateExc(stockCode);
                            } catch (Exception e) {
                                System.out.println("error update stock : " + e.toString());
                            }
                        }
                        break;

                    case DOC_TYPE_DISPATCH: // dispatch
                        where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'"+
                                " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID] + "=" + oidOldLocation;
                        vect = PstMaterialStockCode.list(where, "");
                        if (vect != null && vect.size() > 0) {
                            MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                            stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_DELIVERED);
                            try {
                                PstMaterialStockCode.updateExc(stockCode);
                            } catch (Exception e) {
                                System.out.println("error update stock : " + e.toString());
                            }
                        }
                        break;

                    case DOC_TYPE_COSTING: // dispatch
                        where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'"+
                                " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID] + "=" + oidNewLocation;
                        vect = PstMaterialStockCode.list(where, "");
                        if (vect != null && vect.size() > 0) {
                            MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                            stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_DELIVERED);
                            stockCode.setLocationId(oidNewLocation);
                            try {
                                PstMaterialStockCode.updateExc(stockCode);
                            } catch (Exception e) {
                                System.out.println("error update stock : " + e.toString());
                            }
                        }
                        break;

                    case DOC_TYPE_SALE_REGULAR: // penjualan
                        //System.out.println("serial code jual:"+rs.getString(3));
                        where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'"+
                                " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID] + "=" + oidNewLocation;
                        vect = PstMaterialStockCode.list(where, "");
                        if (vect != null && vect.size() > 0) {
                            //System.out.println("==>> update : PstMaterialStockCode.FLD_STOCK_STATUS_SOLED "+PstMaterialStockCode.FLD_STOCK_STATUS_SOLED);
                            MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                            stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_SOLED);
                            try {
                                PstMaterialStockCode.updateExc(stockCode);
                            } catch (Exception e) {
                                System.out.println("error update stock : " + e.toString());
                            }
                        }
                        break;
                }
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("cekInsertUpdateStockCode " + exc);
        }
        return hasil;
    }


    /**
     *
     * @param oidOldLocation
     * @param oidNewLocation
     * @param oidMaterial
     * @param oidDocItem
     * @param docType
     * @return
     */
    public static boolean deleteUpdateStockCode(long oidNewLocation, long oidMaterial, long oidDocItem, int docType) {
        DBResultSet dbrs = null;
        boolean hasil = false;
        try {
            String sql = "";
            switch (docType) {
                case DOC_TYPE_RECEIVE: // Receive
                    sql = "SELECT " +
                            PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_CODE_ID] +
                            "," + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] +
                            "," + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_STOCK_CODE] +
                            " FROM " + PstReceiveStockCode.TBL_POS_RECEIVE_MATERIAL_CODE +
                            " WHERE " + PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + oidDocItem;
                    break;

                case DOC_TYPE_RETURN: //  return
                    sql = "SELECT " +
                            PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_CODE_ID] +
                            "," + PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID] +
                            "," + PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_CODE] +
                            " FROM " + PstReturnStockCode.TBL_POS_RETURN_MATERIAL_CODE +
                            " WHERE " + PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID] + "=" + oidDocItem;
                    break;

                case DOC_TYPE_DISPATCH: // dispatch
                    sql = "SELECT " +
                            PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_CODE_ID] +
                            "," + PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID] +
                            "," + PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_STOCK_CODE] +
                            " FROM " + PstDispatchStockCode.TBL_POS_DISPATCH_MATERIAL_CODE +
                            " WHERE " + PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID] + "=" + oidDocItem;
                    break;

                case DOC_TYPE_COSTING: // costing
                    sql = "SELECT " +
                            PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_CODE_ID] +
                            "," + PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID] +
                            "," + PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_CODE] +
                            " FROM " + PstCostingStockCode.TBL_POS_COSTING_MATERIAL_CODE +
                            " WHERE " + PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID] + "=" + oidDocItem;
                    break;

                case DOC_TYPE_SALE_REGULAR: // penjualan
                    sql = "SELECT " +
                            PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_CASH_BILL_DETAIL_CODE_ID] +
                            "," + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID] +
                            "," + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_CODE] +
                            " FROM " + PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE +
                            " WHERE " + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID] + "=" + oidDocItem;
                    break;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                switch (docType) {
                    case DOC_TYPE_RECEIVE: // Receive
                        try {
                            PstReceiveStockCode.deleteExc(rs.getLong(PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_CODE_ID]));
                        } catch (Exception e) {
                            System.out.println("error delete receive stock code : " + e.toString());
                        }
                        break;

                    case DOC_TYPE_RETURN: // retur
                        String where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'";
                        Vector vect = PstMaterialStockCode.list(where, "");
                        if (vect != null && vect.size() > 0) {
                            MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                            stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                            try {
                                PstMaterialStockCode.updateExc(stockCode);
                                PstReturnStockCode.deleteExc(rs.getLong(0));
                            } catch (Exception e) {
                                System.out.println("error update stock : " + e.toString());
                            }
                        }
                        break;

                    case DOC_TYPE_DISPATCH: // dispatch
                        where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'";
                        vect = PstMaterialStockCode.list(where, "");
                        if (vect != null && vect.size() > 0) {
                            MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                            stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                            try {
                                PstMaterialStockCode.updateExc(stockCode);
                                PstDispatchStockCode.deleteExc(rs.getLong(0));
                            } catch (Exception e) {
                                System.out.println("error update stock : " + e.toString());
                            }
                        }
                        break;

                    case DOC_TYPE_COSTING: // dispatch
                        where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'";
                        vect = PstMaterialStockCode.list(where, "");
                        if (vect != null && vect.size() > 0) {
                            MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                            stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                            try {
                                PstMaterialStockCode.updateExc(stockCode);
                                PstCostingStockCode.deleteExc(rs.getLong(0));
                            } catch (Exception e) {
                                System.out.println("error update stock : " + e.toString());
                            }
                        }
                        break;

                    case DOC_TYPE_SALE_REGULAR: // penjualan
                        where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + "='" + rs.getString(3) + "'";
                        vect = PstMaterialStockCode.list(where, "");
                        if (vect != null && vect.size() > 0) {
                            MaterialStockCode stockCode = (MaterialStockCode) vect.get(0);
                            stockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                            try {
                                PstMaterialStockCode.updateExc(stockCode);
                                PstBillDetailCode.deleteExc(rs.getLong(0));
                            } catch (Exception e) {
                                System.out.println("error update stock : " + e.toString());
                            }
                        }
                        break;
                }
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("cekInsertUpdateStockCode " + exc);
        }
        return hasil;
    }

    /**
     *
     * @param oidMaterial
     * @param oidLocation
     * @param oidPeriode
     * @return
     */
    private static MaterialStock getMaterialStock(long oidMaterial, long oidLocation, long oidPeriode) {
        MaterialStock materialStock = new MaterialStock();
        DBResultSet dbrs = null;
        try {
            //Select all receive today with detail
            String sql = "SELECT * " +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK +
                    " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = " + oidMaterial +
                    " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + oidLocation +
                    " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + oidPeriode;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PstMaterialStock.resultToObject(rs, materialStock);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("CekMatStock : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return materialStock;
    }

    public static boolean setDocumentByPosted(long oidDocument, int docType) {
        return setPosted(oidDocument,docType);
    }
    /**
     * Set status semua document yg sudah diposting menjadi posted
     * @param oidDocument
     * @param docType
     * @return
     * @update by Edhy
     */
    private static boolean setPosted(long oidDocument, int docType) {
        boolean hasil = false;
        try {
            String sql = "";
            switch (docType) {
                case DOC_TYPE_RECEIVE:// Receive
                    sql = "UPDATE " + PstMatReceive.TBL_MAT_RECEIVE +
                            " SET " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                            " WHERE " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                            " = " + oidDocument;
					PstDataCustom.insertDataForSyncAllLocation(sql);
                    break;

                case DOC_TYPE_RETURN:// Return
                    sql = "UPDATE " + PstMatReturn.TBL_MAT_RETURN +
                            " SET " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                            " WHERE " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                            " = " + oidDocument;
					PstDataCustom.insertDataForSyncAllLocation(sql);
                    break;

                case DOC_TYPE_DISPATCH://Dispatch
                    sql = "UPDATE " + PstMatDispatch.TBL_DISPATCH +
                            " SET " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                            " WHERE " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                            " = " + oidDocument;
					PstDataCustom.insertDataForSyncAllLocation(sql);
                    break;

                case DOC_TYPE_SALE_REGULAR://Sale
                    sql = "UPDATE " + PstBillMain.TBL_CASH_BILL_MAIN +
                            " SET " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                            " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                            " = " + oidDocument;
					PstDataCustom.insertDataForSyncAllLocation(sql);
                    break;

                case DOC_TYPE_COSTING:// costing
                    sql = "UPDATE " + PstMatCosting.TBL_COSTING +
                            " SET " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                            " WHERE " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                            " = " + oidDocument;
					PstDataCustom.insertDataForSyncAllLocation(sql);
                    break;
                    
                case DOC_TYPE_FORWARDER://Forwarder
                    sql = "UPDATE " + PstForwarderInfo.TBL_FORWARDER_INFO +
                            " SET " + PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                            " WHERE " + PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_FORWARDER_ID] +
                            " = " + oidDocument;
					PstDataCustom.insertDataForSyncAllLocation(sql);
                    break;
                    
                case DOC_TYPE_PRODUCTION://Production
                    sql = "UPDATE " + PstProduction.TBL_PRODUCTION +
                            " SET " + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                            " WHERE " + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID] +
                            " = " + oidDocument;
					PstDataCustom.insertDataForSyncAllLocation(sql);
                    break;
                    
            }
            System.out.println("set posted: "+sql);
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("POST " + exc);
        }
        return hasil;
    }


    /**
     * Update master cost di material jika receive dari supplier
     * @param oidMaterial
     * @param currencyId
     * @param newCost
     * @return
     */
    private static boolean updateCostMaster(long oidMaterial, long currencyId, double newCost) {
        boolean hasil = false;
        try {
            // Select all receive today with detail
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
                    " SET " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    " = " + newCost;
                    if(currencyId!=0){
                        sql = sql + ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
                        " = " + currencyId;
                    }
                    sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            //System.out.println("updateCostMaster >>> "+sql);
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        return hasil;
    }

    /**
     * Update master cost di material jika receive dari supplier
     * @param oidMaterial
     * @param currencyId
     * @param ppn
     * @return
     */
    private static boolean updatePPnMaster(long oidMaterial, long currencyId, double ppn) {
        boolean hasil = false;
        try {
            // Select all receive today with detail
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
                    " SET " + PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT] +
                    " = " + ppn;
                    if(currencyId!=0){
                        sql = sql + ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
                        " = " + currencyId;
                    }
                    sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            //System.out.println("updateCostMaster >>> "+sql);
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        return hasil;
    }

    /**
     * Update master cost di material jika receive dari supplier
     * @param oidMaterial
     * @param currencyId
     * @param ppn
     * @return
     */
    private static boolean updateForwarderCostMaster(long oidMaterial, long currencyId, double forwarderCost) {
        boolean hasil = false;
        try {
            // Select all receive today with detail
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
                    " SET " + PstMaterial.fieldNames[PstMaterial.FLD_LAST_COST_CARGO] +
                    " = " + forwarderCost;
                    if(currencyId!=0){
                        sql = sql + ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
                        " = " + currencyId;
                    }
                    sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            //System.out.println("updateCostMaster >>> "+sql);
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        return hasil;
    }



    /**
     * ini gunanya untuk mengupdate harga
     * barang supplier terakhir
     * @param oidSupplier
     * @param oidMaterial
     * @param currencyId
     * @param newCost
     * @return
     */
    private static boolean updateCostSupplierPrice(long oidSupplier, long oidMaterial, long currencyId, double newCost) {
        boolean hasil = false;
        try {               
            String sql = "UPDATE " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE +
                    " SET " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_CURR_BUYING_PRICE] + " = " + newCost;
                    if(currencyId!=0){
                        sql = sql + ", " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_PRICE_CURRENCY] + " = " + currencyId;
                    }
                    sql = sql + ", " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_ORG_BUYING_PRICE] + " = " + newCost +
                    " WHERE " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] + " = " + oidMaterial + 
                    " AND " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + oidSupplier;
            //System.out.println("==>>> Update Cost Supplier Success");
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Update cost material price: " + exc);
        }
        return hasil;
    }

    /**
     * Update master currbuyprice + ppn di material jika receive dari supplier
     * @param oidMaterial
     * @param currencyId
     * @param newCost
     * @return
     */
    private static boolean updateCurrBuyPriceMaster(long oidMaterial, long currencyId, double newCost) {
        boolean hasil = false;
        try {
            // Select all receive today with detail
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
                    " SET " + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
                    " = " + newCost;
                    if(currencyId!=0){
                        sql = sql + ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
                        " = " + currencyId;
                    }
                    sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            //System.out.println("updateCostMaster >>> "+sql);
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        return hasil;
    }


    /**
     * update hpp satu barang
     * yang proses ini di jalankan secara otomatis sewaktu posting
     * pada document penerimaan barang
     * @param oidMaterial
     * @param currencyId
     * @param newCost
     * @return
     * @created by gadnyana
     */
    private static boolean updateAveragePrice(long oidMaterial, long currencyId, double newAverage) {
        boolean hasil = false;
        try {
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
                    " SET " + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " = " + newAverage;
//                    if(currencyId!=0){ disable opie-eyek 20140318
//                        sql = sql + ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
//                        " = " + currencyId;
//                    }
                    sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            // System.out.println(" sql >> " + sql);
            int a = DBHandler.execUpdate(sql);
            
            String sqlInsert = " INSERT INTO pos_material_average_price_history VALUES ('"+oidMaterial+"','"+newAverage+"','"+Formater.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"') ";
            int result = DBHandler.execUpdate(sqlInsert);
            hasil = true;
        } catch (Exception exc) {
            System.out.println(" XXXXX updateAveragePrice : " + exc);
        }
        return hasil;
    }
    
    
    private static boolean updateSupplierMaterial(long oidMaterial, long oidSupplier, double newAverage) {
        boolean hasil = false;
        try {
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
                    " SET " + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + oidSupplier;
                    sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            // System.out.println(" sql >> " + sql);
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println(" XXXXX updateAveragePrice : " + exc);
        }
        return hasil;
    }


    /**
     * Update master cost di material jika receive dari supplier
     * @param oidMaterial
     * @param oidLocation
     * @param QtyIn
     * @param QtyOut
     * @param mode
     * @param docType
     * @param oidPeriode
     * @return
     * @update by Edhy
     */
    synchronized private static boolean updateMaterialStock(long oidMaterial, long oidLocation, double QtyIn, double QtyOut, int mode, int docType, long oidPeriode) {
        boolean hasil = false;
        try {
            String sql = "";
            switch (mode) {
                case MODE_UPDATE:
                    MaterialStock materialStock = new MaterialStock();
                    materialStock = getMaterialStock(oidMaterial, oidLocation, oidPeriode);
                    switch (docType) {
                        case DOC_TYPE_RECEIVE:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                                    " = " + (QtyIn + materialStock.getQtyIn()) +
                                    ", " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                                    " = " + (QtyIn + materialStock.getQty());
                            break;

                        case DOC_TYPE_RETURN:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                                    " = " + (materialStock.getQtyOut() + QtyOut) +
                                    ", " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                                    " = " + (materialStock.getQty() - QtyOut);
                            break;

                        case DOC_TYPE_DISPATCH:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                                    " = " + (materialStock.getQtyOut() + QtyOut) +
                                    ", " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                                    " = " + (materialStock.getQty() - QtyOut);
                            break;

                        case DOC_TYPE_COSTING:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                                    " = " + (materialStock.getQtyOut() + QtyOut) +
                                    ", " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                                    " = " + (materialStock.getQty() - QtyOut);
                            break;

                        case DOC_TYPE_SALE_REGULAR:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                                    " = " + (materialStock.getQtyOut() + QtyOut) +
                                    ", " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                                    " = " + (materialStock.getQty() - QtyOut);
                            break;

                        case DOC_TYPE_SALE_COMPOSIT:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                                    " = " + (materialStock.getQtyOut() + QtyOut) +
                                    ", " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                                    " = " + (materialStock.getQtyIn() + QtyIn);
                            break;
                    }

                    sql = sql + " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                            " = " + oidMaterial +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                            " = " + oidPeriode;

                    
                    // for update stock get previous stock 
                    balanceStock(oidMaterial,oidLocation,oidPeriode);

                    int iUpdated = 1; // DBHandler.execUpdate(sql);
                    if (iUpdated > 0) {
                        hasil = true;
                    }
                    break;


                case MODE_INSERT:
                    MaterialStock matStock = new MaterialStock();
                    long oid = 0;
                    switch (docType) {
                        case DOC_TYPE_RECEIVE:
                            matStock.setPeriodeId(oidPeriode);
                            matStock.setMaterialUnitId(oidMaterial);
                            matStock.setLocationId(oidLocation);
                            matStock.setQtyIn(QtyIn);
                            matStock.setQty(QtyIn);
                            oid = PstMaterialStock.insertExc(matStock);
                            if (oid != 0) {
                                hasil = true;
                            }
                            break;

                        case DOC_TYPE_SALE_REGULAR:
                            matStock.setPeriodeId(oidPeriode);
                            matStock.setMaterialUnitId(oidMaterial);
                            matStock.setLocationId(oidLocation);
                            matStock.setQtyOut(QtyOut);
                            matStock.setQty(0 - QtyOut);
                            oid = PstMaterialStock.insertExc(matStock);
                            if (oid != 0) {
                                hasil = true;
                            }
                            break;

                       case DOC_TYPE_DISPATCH:
                            matStock.setPeriodeId(oidPeriode);
                            matStock.setMaterialUnitId(oidMaterial);
                            matStock.setLocationId(oidLocation);
                            matStock.setQtyOut(QtyOut);
                            matStock.setQty(0 - QtyOut);
                            oid = PstMaterialStock.insertExc(matStock);
                            if (oid != 0) {
                                hasil = true;
                            }
                            break;

                       case DOC_TYPE_RETURN:
                            matStock.setPeriodeId(oidPeriode);
                            matStock.setMaterialUnitId(oidMaterial);
                            matStock.setLocationId(oidLocation);
                            matStock.setQtyOut(QtyOut);
                            matStock.setQty(0 - QtyOut);
                            oid = PstMaterialStock.insertExc(matStock);
                            if (oid != 0) {
                                hasil = true;
                            }
                            break;

                      case DOC_TYPE_COSTING:
                            matStock.setPeriodeId(oidPeriode);
                            matStock.setMaterialUnitId(oidMaterial);
                            matStock.setLocationId(oidLocation);
                            matStock.setQtyOut(QtyOut);
                            matStock.setQty(0 - QtyOut);
                            oid = PstMaterialStock.insertExc(matStock);
                            if (oid != 0) {
                                hasil = true;
                            }
                            break;

                      case DOC_TYPE_SALE_COMPOSIT:
                            matStock.setPeriodeId(oidPeriode);
                            matStock.setMaterialUnitId(oidMaterial);
                            matStock.setLocationId(oidLocation);
                            matStock.setQtyOut(QtyOut);
                            matStock.setQty(0 - QtyOut);
                            oid = PstMaterialStock.insertExc(matStock);
                            if (oid != 0) {
                                hasil = true;
                            }
                            break;



                            /*default :
                                matStock.setPeriodeId(oidPeriode);
                                matStock.setMaterialUnitId(oidMaterial);
                                matStock.setLocationId(oidLocation);
                                matStock.setQtyOut(QtyOut);
                                matStock.setQty(0 - QtyOut);
                                oid = PstMaterialStock.insertExc(matStock);
                                if (oid != 0) {
                                    hasil = true;
                                }
                                break;*/
                    }
            }
        } catch (Exception exc) { 
            System.out.println("UpdMatStock : " + exc);
            PstDocLogger.insertDataBo_toDocLogger("updateMaterialStock - SessPosting : ", docType, new Date(), exc.toString());

        }
        return hasil;
    }


    /**
     *
     * @param oidMaterial
     * @param oidLocation
     * @param QtyIn
     * @param QtyOut
     * @param mode
     * @param oidPeriode
     * @return
     */
    synchronized private static boolean updateMaterialStock(long oidMaterial, long oidLocation, double QtyIn, double QtyOut, int mode, long oidPeriode) {
        boolean hasil = false;
        try {
            String sql = "";
            switch (mode) {
                case MODE_UPDATE: // Update Only
                    // System.out.println("---MODE UPDATE---");
                    sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                            " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                            " = " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                            " + " + QtyIn +
                            ", " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                            " = " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                            " + " + QtyOut;

                    sql += " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                            " = " + oidMaterial +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                            " = " + oidPeriode;
                    int a = DBHandler.execUpdate(sql);
                    hasil = true;
                    break;

                case MODE_INSERT: // Insert Only and the docType can only be Receive
                    // System.out.println("---MODE INSERT---");
                    MaterialStock matStock = new MaterialStock();
                    long oid = 0;
                    matStock.setPeriodeId(oidPeriode);
                    matStock.setMaterialUnitId(oidMaterial);
                    matStock.setLocationId(oidLocation);
                    matStock.setQtyIn(QtyOut); // );QtyOut(
                    matStock.setQty(QtyOut);
                    oid = PstMaterialStock.insertExc(matStock);
                    if (oid != 0) hasil = true;
            }
        } catch (Exception exc) {
            System.out.println("UpdMatStock : " + exc);
        }
        return hasil;
    }
    
    /**
     * Get Current Stock Periode (There can be only one with status open !!!)
     * @param oidMaterial
     * @return
     * @update by Edhy
     */
    private static Vector getMaterialComposer(long oidMaterial) {
        Vector hasil = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT *" +
                    " FROM " + PstMaterialComposit.TBL_MATERIAL_COMPOSIT +
                    " WHERE " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialComposit objMaterialComposit = new MaterialComposit();

                objMaterialComposit.setOID(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID]));
                objMaterialComposit.setMaterialId(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]));
                objMaterialComposit.setMaterialComposerId(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID]));
                objMaterialComposit.setUnitId(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID]));
                objMaterialComposit.setQty(rs.getDouble(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY]));

                hasil.add(objMaterialComposit);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("GetMaterialComposer : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }
    
    private static MaterialStock getProductOidStock(long oidMaterial, long oidLocation, long oidPeriode) {
        MaterialStock materialStock = new MaterialStock();
        DBResultSet dbrs = null;
        try {
            //Select all receive today with detail
            String sql = "SELECT * " +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK +
                    " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = " + oidMaterial +
                    " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + oidLocation +
                    " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + oidPeriode;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                PstMaterialStock.resultToObject(rs, materialStock);
            //oidstock= rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("CekMatStock : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return materialStock;
    }
    
    
    public static int balanceStock(long materialOid, long locationOid, long periodeOid) {
        try {
	    Date dtstart = new Date();
            Date dtend = new Date();

	    Periode periode = PstPeriode.getPeriodeRunning();
	    if(periode.getStartDate().after(new Date())){
		dtstart = periode.getEndDate();
		dtend = periode.getEndDate();
	    }
            dtstart.setDate(dtend.getDate() - 1);

            SrcStockCard srcStockCard = new SrcStockCard();
            srcStockCard.setStardDate(dtstart);
            srcStockCard.setEndDate(dtend); 
            srcStockCard.setMaterialId(materialOid);
            srcStockCard.setLocationId(locationOid);
            Vector list = SessStockCard.createHistoryStockCard(srcStockCard);
            double qtyReal = prosesGetPrivousDataStockCard(list);
            //-- added by dewok 2017 for nilai berat
            double berat = prosesGetPrivousDataStockCardBerat(list);
            int typeOfBusinessDetail = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));
            if (typeOfBusinessDetail == 2) {
                if (qtyReal > 1) {qtyReal = 1;}
            }
            //--
            MaterialStock matStock = getProductOidStock(materialOid, locationOid, periodeOid);
            try {
                if (matStock.getOID() != 0) { 
                    matStock.setQty(qtyReal);
                    //-- added by dewok 2017 for nilai berat
                    matStock.setBerat(berat);
                    matStock.setUpdateDate(new Date());
                    //--
                    PstMaterialStock.updateExc(matStock);
                    System.out.println("=>> Suksess update stock : " + qtyReal);

                // this for delete serial code

                } else {
                    System.out.println("=>> belum basuk stock mt oid : " + matStock.getMaterialUnitId());
                }
            } catch (Exception e) {
                System.out.println("=>> Err upda material . : mt oid : " + matStock.getMaterialUnitId() + " loc :" + locationOid);
            }

        } catch (Exception ee) {
        }
        return 1; 
    }

    // this for balance stock
    public static double prosesGetPrivousDataStockCard(Vector objectClassx) {
        double qtyawal = 0;
        StockCardReport stockCrp = (StockCardReport) objectClassx.get(0);
        Vector objectClass = (Vector) objectClassx.get(1);
        qtyawal = stockCrp.getQty();
        int initloop = objectClass.size();
	
	System.out.println("qtyawal : "+qtyawal);
	
        try {
            for (int i = 0; i < initloop; i++) {
                StockCardReport stockCardReport = (StockCardReport) objectClass.get(i);
		
		System.out.println("stockCardReport : "+stockCardReport.getQty()+" - "+stockCardReport.getKeterangan());
                switch (stockCardReport.getDocType()) {
                    case I_DocType.MAT_DOC_TYPE_LMRR:
                        qtyawal = qtyawal + stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_ROMR:
                        qtyawal = qtyawal - stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_DF:
                        qtyawal = qtyawal - stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_OPN:
                        qtyawal = stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_SALE:
                        switch (stockCardReport.getTransaction_type()) {
                            case PstBillMain.TYPE_INVOICE:
                                qtyawal = qtyawal - stockCardReport.getQty();
                                break;
                            case PstBillMain.TYPE_RETUR:
                                qtyawal = qtyawal + stockCardReport.getQty();
                                break;
                        }
                        break;
                    //costing barang
                   case I_DocType.MAT_DOC_TYPE_COS:
                        qtyawal = qtyawal - stockCardReport.getQty();
                        break;
                    // ADDED BY DEWOK 20190924 FOR PRODUCTION
                    case I_DocType.MAT_DOC_TYPE_PROD:
                        switch (stockCardReport.getTransaction_type()) {
                            case PstProduction.PRODUCTION_COST:
                                qtyawal = qtyawal - stockCardReport.getQty();
                                break;
                            case PstProduction.PRODUCTION_PRODUCT:
                                qtyawal = qtyawal + stockCardReport.getQty();
                                break;
                        }
                        break;
                }
                System.out.println("==>>>>> Qty awal : " + qtyawal);
            }
        } catch (Exception e) {
            System.out.println("prosesGetPrivousDataStockCard : " + e.toString());
        }
        return qtyawal;
    }

    public static double prosesGetPrivousDataStockCardBerat(Vector objectClassx) {
        double beratAwal = 0;
        StockCardReport stockCrp = (StockCardReport) objectClassx.get(0);
        Vector objectClass = (Vector) objectClassx.get(1);
        beratAwal = stockCrp.getBerat();
        int initloop = objectClass.size();
	
	System.out.println("beratawal : "+beratAwal);
	
        try {
            for (int i = 0; i < initloop; i++) {
                StockCardReport stockCardReport = (StockCardReport) objectClass.get(i);
		
		System.out.println("stockCardReport : "+stockCardReport.getBerat()+" - "+stockCardReport.getKeterangan());
                switch (stockCardReport.getDocType()) {
                    case I_DocType.MAT_DOC_TYPE_LMRR:
                        beratAwal = beratAwal + stockCardReport.getBerat();
                        break;
                    case I_DocType.MAT_DOC_TYPE_ROMR:
                        beratAwal = beratAwal - stockCardReport.getBerat();
                        break;
                    case I_DocType.MAT_DOC_TYPE_DF:
                        beratAwal = beratAwal - stockCardReport.getBerat();
                        break;
                    case I_DocType.MAT_DOC_TYPE_OPN:
                        beratAwal = stockCardReport.getBerat();
                        break;
                    case I_DocType.MAT_DOC_TYPE_SALE:
                        switch (stockCardReport.getTransaction_type()) {
                            case PstBillMain.TYPE_INVOICE:
                                beratAwal = beratAwal - stockCardReport.getBerat();
                                break;
                            case PstBillMain.TYPE_RETUR:
                                beratAwal = beratAwal + stockCardReport.getBerat();
                                break;
                        }
                        break;
                    //costing barang
                   case I_DocType.MAT_DOC_TYPE_COS:
                        beratAwal = beratAwal - stockCardReport.getBerat();
                        break;

                }
                System.out.println("==>>>>> Berat awal : " + beratAwal);
            }
        } catch (Exception e) {
            System.out.println("prosesGetPrivousDataStockCard : " + e.toString());
        }
        return beratAwal;
    }

 //Method for composit

 /**
     * check jika material item di receive material merupakan komponen dari komposit
     * @param oidMaterial
     * @param currencyId
     * @param newCost
     * @return
     * @created by Mirahu
     */
     private static Vector checkComponentComposit(long oidMaterial) {
        Vector hasil = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT *" +
                    " FROM " + PstMaterialComposit.TBL_MATERIAL_COMPOSIT +
                    " WHERE " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
                    " = " + oidMaterial;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialComposit objMaterialComposit = new MaterialComposit();

                long oidComposit = rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID]);
                long materialId = rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]);
                long materialComponentId = rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID]);
                long unitId = rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID]);
                double qty = rs.getDouble(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY]);
                
                Vector vGrandTotalComposit = new Vector(1,1);
                try {
                    //vErrUpdateStockByReceiveItem = updateStockByReceiveItem(lPeriodeOid, oidLocation, oidRM, recSource, ppn);
                     vGrandTotalComposit = grandTotalComposit(materialId);
                }
                catch(Exception e) {
                    System.out.println("Exc when get vGrandTotalComposit... "+e.toString());
                    vGrandTotalComposit = new Vector(1,1);
                }



                hasil.add(objMaterialComposit);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("GetMaterialComposer : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

   /**
     * check
     * @param oidMaterial
     * @return
     * @created by Mirahu
     */

     private static Vector grandTotalComposit(long materialId) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT SUM(MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + ") AS SUM_STOCK_VALUE " +
            ", SUM(MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + ") AS SUM_COST_VALUE " +
            ", SUM(MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] + ") AS SUM_COST_VALUE_PPN " +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT] + " AS LAST_VAT " +
            " FROM (" + PstMaterialComposit.TBL_MATERIAL_COMPOSIT +
            " MC INNER JOIN " + PstUnit.TBL_P2_UNIT +
            " UN ON MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
            " = UN." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL +
            " MAT ON MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " WHERE MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
            " = " + materialId;

            System.out.println("sql list > " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                 Vector temp = new Vector();

                double sumStockValue = rs.getDouble("SUM_STOCK_VALUE");
                double sumCostValue = rs.getDouble("SUM_COST_VALUE");
                double sumCostValuePpn = rs.getDouble("SUM_COST_VALUE_PPN");
                double lastPpn = rs.getDouble("LAST_VAT");

                updateCostAndStockValueMaster(materialId,sumStockValue, sumCostValue, sumCostValuePpn, lastPpn);





                //result.add(temp);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    // end of grand total

    /*
     * Update Stock Value & Cost Master
     * By Mirahu
     * 13 Juni 2011
     */
    private static boolean updateCostAndStockValueMaster(long oidMaterial, double stockValue, double newCost, double newCostPpn, double lastVat) {
        boolean hasil = false;
        try {
            // Select all receive today with detail
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
                    " SET " + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    " = " + stockValue +
                    "," + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    " = " + newCost +
                    "," + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
                    " = " + newCostPpn +
                    "," + PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT] +
                    " = " + lastVat;
                    sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        return hasil;
    }

    /**
     * list componet material item di composit
     * @param oidMaterial
     * @param oidCostingMaterial
     * @param qtySale
     * @return
     * @created by Mirahu
     */

     private static Vector ListComponentComposit(long oidMaterial, long oidCostingMaterial, double qtySale) {
        Vector hasil = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID] +
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY]+
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]+
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                         " FROM " + PstMaterialComposit.TBL_MATERIAL_COMPOSIT + " COMP " +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT " +
                         " ON COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID]+
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNIT " +
                         " ON COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
                         " = UNIT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " WHERE COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
                         " = " + oidMaterial;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialComposit objMaterialComposit = new MaterialComposit();

                long oidComposit = rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID]);
                long materialId = rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]);
                long materialComponentId = rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID]);
                double qty = rs.getDouble(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY]);
                long unitId = rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID]);
                double stockValue = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]);
                double costValue = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]);
                int materialType = rs.getInt(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE]);

                if(materialType == PstMaterial.MAT_TYPE_COMPOSITE){
                    Vector listItemCosting = ListComponentComposit(materialComponentId, oidCostingMaterial, qtySale);
                }
                else if(materialType != PstMaterial.MAT_TYPE_COMPOSITE){
                

                 long oid = 0;
                 long costingMaterialId = 0;
                 long materialCostItemId = 0;
                 long unitCostItemId = 0;
                 double qtyCostItem = 0;
                 double hppCostItem = 0;
                 double residueQtyCostItem = 0;
                 double qtyAll = 0;

                 MatCostingItem matCostingItem = new MatCostingItem();
                 PstMatCostingItem pstMatCostingItem = new PstMatCostingItem();

                 Vector getListCostingMaterialItem = new Vector();
                  getListCostingMaterialItem = getCostingMaterialItem(oidCostingMaterial,materialComponentId);

                  if(getListCostingMaterialItem !=null && getListCostingMaterialItem .size()>0) {
                           for(int i = 0; i<getListCostingMaterialItem.size(); i++){
                             matCostingItem = (MatCostingItem)getListCostingMaterialItem.get(i);
                             oid = matCostingItem.getOID();
                             oidCostingMaterial = matCostingItem.getCostingMaterialId();
                             materialComponentId = matCostingItem.getMaterialId();
                             unitId = matCostingItem.getUnitId();
                             qtyCostItem= matCostingItem.getQty();
                             costValue = matCostingItem.getHpp();
                             qty = matCostingItem.getResidueQty();
                          }
                  }


                 qtyAll =  qtySale * (qty + qtyCostItem);
                  
                  
                 matCostingItem.setOID(oid);
                 matCostingItem.setCostingMaterialId(oidCostingMaterial);
                 matCostingItem.setMaterialId(materialComponentId);
                 matCostingItem.setUnitId(unitId);
                 matCostingItem.setQty(qtyAll);
                 matCostingItem.setHpp(costValue);
                 matCostingItem.setResidueQty(qtyAll);

                 if (oid == 0){
                 oid = pstMatCostingItem.insertExc(matCostingItem);
                }
                else  if(oid != 0){
                  oid = pstMatCostingItem.updateExc(matCostingItem);
                }
            }

            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("GetMaterialComposer : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

     /**
     * list componet material item di composit
     * @param oidMaterial
     * @param currencyId
     * @param newCost
     * @return
     * @created by Mirahu
     */

     private static Vector ListComponentComposit2(long oidMaterial, long oidCostingMaterial, double qtySale) {
        Vector hasil = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID] +
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY]+
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]+
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                         " FROM " + PstMaterialComposit.TBL_MATERIAL_COMPOSIT + " COMP " +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT " +
                         " ON COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID]+
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNIT " +
                         " ON COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
                         " = UNIT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " WHERE COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
                         " = " + oidMaterial;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector(1, 1);
                MaterialComposit objMaterialComposit = new MaterialComposit();
                Material material = new Material();

                objMaterialComposit.setOID(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID]));
                objMaterialComposit.setMaterialId(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]));
                objMaterialComposit.setMaterialComposerId(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID]));
                objMaterialComposit.setQty(rs.getDouble(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY]));
                objMaterialComposit.setUnitId(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID]));
                 vt.add(objMaterialComposit);

                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setMaterialType(rs.getInt(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE]));
                vt.add(material);

                hasil.add(vt);



            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("GetMaterialComposer : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

   //get costingId di pos Costing
     private static Vector getCostingMaterial(long oidCashCashier) {
        Vector hasil = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * "  //PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]
                    + " FROM " + PstMatCosting.TBL_COSTING
                    + " WHERE " +PstMatCosting.fieldNames[PstMatCosting.FLD_CASH_CASHIER_ID]
                    + " = " +oidCashCashier;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatCosting objMatCosting = new MatCosting();


                objMatCosting.setOID(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]));
                objMatCosting.setLocationId(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID]));
                objMatCosting.setLocationType(rs.getInt(PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_TYPE]));
                objMatCosting.setCostingDate(rs.getTimestamp(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
                objMatCosting.setCostingStatus(rs.getInt(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]));
                objMatCosting.setRemark(rs.getString(PstMatCosting.fieldNames[PstMatCosting.FLD_REMARK]));
                objMatCosting.setCostingCode(rs.getString(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE]));
                objMatCosting.setCostingCodeCounter(rs.getInt(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE_COUNTER]));
                objMatCosting.setCostingTo(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO]));
                objMatCosting.setCashCashierId(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_CASH_CASHIER_ID]));

               hasil.add(objMatCosting);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

   //get material id di costing material item composit
     private static Vector getCostingMaterialItem(long oidCostingMaterialId, long oidMaterial) {
        Vector hasil = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + PstMatCostingItem.TBL_MAT_COSTING_ITEM
                    + " WHERE " + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]
                    + " = " +oidCostingMaterialId
                    + " AND " + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]
                    + " = " +oidMaterial;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatCostingItem objMatCostingItem = new MatCostingItem();


                objMatCostingItem.setOID(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID]));
                objMatCostingItem.setCostingMaterialId(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]));
                objMatCostingItem.setMaterialId(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]));
                objMatCostingItem.setUnitId(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]));
                objMatCostingItem.setQty(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY]));
                objMatCostingItem.setHpp(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_HPP]));
                objMatCostingItem.setResidueQty(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_RESIDUE_QTY]));
               

               hasil.add(objMatCostingItem);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

    public static synchronized double sumQtyStockAllLocation(long materialId) {
        double qtyStock = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " SUM(" + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + ") AS " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]
                    + " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK
                    + " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + " = " + materialId
                    + "";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                qtyStock = rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return qtyStock;
    }
    
    public void insertHistoryMaterial(long userID, String nameUser, String userAction, Material m, String log) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType("Pos material");
            logSysHistory.setLogUserAction(userAction);
            logSysHistory.setLogDocumentNumber(m.getFullName());
            logSysHistory.setLogDocumentId(m.getOID());
            logSysHistory.setLogDetail(log);
            PstLogSysHistory.insertLog(logSysHistory);
        } catch (Exception e) {

        }
    }
}
