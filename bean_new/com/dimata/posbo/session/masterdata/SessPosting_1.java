
package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.qdep.entity.I_DocStatus; 
import com.dimata.util.Formater;

//replaced by widi
import com.dimata.pos.entity.billing.*;

import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrder;
import com.dimata.posbo.entity.purchasing.PurchaseOrderItem;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;

import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.common.entity.payment.PstStandartRate;





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

//import data sync
import com.dimata.posbo.entity.masterdata.PstDataSyncSql;

//import temp post doc

import java.util.Date;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.Connection;


public class SessPosting_1 {
    // Document Type in posting
    public static final int DOC_TYPE_RECEIVE = 0;
    public static final int DOC_TYPE_RETURN = 1;
    public static final int DOC_TYPE_DISPATCH = 2;
    public static final int DOC_TYPE_SALE_REGULAR = 3;
    public static final int DOC_TYPE_COSTING = 5;
    public static final int DOC_TYPE_SALE_RETURN = 6;

    // Mode in accessing MaterialStock
    public static final int MODE_UPDATE = 0;
    public static final int MODE_INSERT = 1;

    // Mode in accessing PPn Masukan
    public static final int MODE_BIAYA = 0;
    public static final int MODE_DIKREDITKAN = 1;

    /**
     * @return the vecDbConn
     */
    public static Vector getVecDbConn() {
        return vecDbConn;
    }

    /**
     * @param aVecDbConn the vecDbConn to set
     */
    public static void setVecDbConn(Vector aVecDbConn) {
        vecDbConn = aVecDbConn;
    }

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

    private static Vector vecDbConn=null;
    
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

    //PostingProgress postingProgress = new PostingProgress();

    /**
     * @param postingDate
     * @param oidLocation
     * @return
     */
     synchronized public boolean postingTransDocument(Date postingDate, long oidLocation, boolean postedDateCheck) {
         boolean bPostingOK = false;


         
         //delete history posting sebelumnya
         PstTempPostDoc.deleteHistoryPosting();

         //insert select document yang make table temporary
         /*try {
            System.out.println("masukkan data transaksi ke tabel "+PstTempPostDoc.TBL_TEMP_POST_DOC);
            // mengambil data trnasaksi sesuai dengan periode yang di select
            System.out.println(" proses transaksi receive....");
            PstTempPostDoc.insertSelectReceivePosting(oidLocation, con);
            System.out.println(" proses transaksi dispatch....");
            PstTempPostDoc.insertSelectDispatchPosting(oidLocation, con);
            System.out.println(" proses transaksi sales....");
            PstTempPostDoc.insertSelectSalesPosting(oidLocation, con);
            System.out.println(" proses transaksi costing....");
            PstTempPostDoc.insertSelectCostingPosting(oidLocation, con);
            System.out.println(" proses transaksi return....");
            PstTempPostDoc.insertSelectReturnPosting(oidLocation, con);

         } catch (Exception e) {
            System.out.println("Exc. insertTo table temp_post_doc(#,#,#,#) >> " + e.toString());

        }*/



        Periode objPeriode = PstPeriode.getPeriodeRunning();
        long oidPeriode = objPeriode.getOID();

         // Process Dispatch
        /**Vector vUnPostedDFDoc = getUnPostedDFDocument(oidLocation, oidPeriode);
        this.vListUnPostedDFDoc = vUnPostedDFDoc;
        System.out.println("vListUnPostedDFDoc : "+vListUnPostedDFDoc);

        // Process Receive
        Vector vUnPostedLGRDoc = getUnPostedLGRDocument(oidLocation, oidPeriode);
        this.vListUnPostedLGRDoc = vUnPostedLGRDoc;
        System.out.println("vListUnPostedLGRDoc : "+vListUnPostedLGRDoc);

        // Process Return
        Vector vUnPostedReturnDoc = getUnPostedReturnDocument(oidLocation, oidPeriode);
        this.vListUnPostedReturnDoc = vUnPostedReturnDoc;
        System.out.println("vListUnPostedReturnDoc : "+vListUnPostedReturnDoc);

        // Process Sale
        Vector vUnPostedSalesDoc = getUnPostedSalesDocument(oidLocation, oidPeriode);
        //getUnPostedSalesDocument(postingDate, oidLocation, oidPeriode, postedDateCheck, DOC_TYPE_SALE_RETURN);
	
        this.vListUnPostedSalesDoc = vUnPostedSalesDoc;
        System.out.println("vListUnPostedSalesDoc : "+vListUnPostedSalesDoc);

         // Process Cost
        Vector vUnPostedCostDoc = getUnPostedCostDocument(oidLocation, oidPeriode);
        this.vListUnPostedCostDoc = vUnPostedCostDoc;
        System.out.println("vListUnPostedCostDoc : "+vListUnPostedCostDoc);**/
        
    

       /** if (((vUnPostedLGRDoc != null && vUnPostedLGRDoc.size() > 0)
                || (vListUnPostedReturnDoc != null && vListUnPostedReturnDoc.size() > 0)
                || (vListUnPostedDFDoc != null && vListUnPostedDFDoc.size() > 0)
                || (vListUnPostedCostDoc != null && vListUnPostedCostDoc.size() > 0)
                || (vListUnPostedSalesDoc != null && vListUnPostedSalesDoc.size() > 0))) {
            return true;
        }**/
       try {
            System.out.println("update Stok ");
            //updateQtyStock(oidLocation, oidPeriode);
            return true;
       } catch (Exception e) {
            System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
            return false;
        }

     // return bPostingOK;
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

    // ------------------------------- POSTING DISPATCH START ---------------------------
    /**
     * This method use to posting dispatch document
     * @param lLocationOid
     * @param lPeriodeOid
     * @return
     * @created by Mirah
     * 20110901
     * algoritm :
     *  - summary qty dispatch and grouping by item dispatch based on docId and dispatchId in table temp_post_doc
     *  - update qty_out to material Stock
     */
      // private Vector getUnPostedDFDocument(long oidLocation, long oidPeriode) {
     //protected Vector getUnPostedDFDocument(long oidLocation, long oidPeriode) {
       public boolean getUnPostedDFDocument(long oidLocation, long oidPeriode, PostingProgress postingProgress, int threadSleep, Connection con) {
        //public int getUnPostedDFDocument(long oidLocation, long oidPeriode, PostingProgress postingProgress) {

        Vector vResult = new Vector(1, 1);
        int countStock = 0;
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
        boolean updateProses = false;
        try {
            // Select all receive today with detail
            String sql = "SELECT DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                         ",MAX(DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + ") AS MAX_DATE " +
                         ",DFITEM." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] +
                         ",SUM(DFITEM." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS SUM_QTY " +
                         " FROM " + PstMatDispatch.TBL_DISPATCH + " DF " +
                         " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM +  " DFITEM " +
                         " ON DFITEM." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                         " = DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                         " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                         " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                         " = " + oidLocation;

             sql = sql + " GROUP BY DFITEM." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
                       + " ORDER BY DF."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " DESC";


            System.out.println(">>> " + new SessPosting_1().getClass().getName() + ".getUnPostedDFDocument() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            boolean isOK = false;
            

            while (rs.next()) {
                //updateProses = true;

                long oidDF = rs.getLong(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]);
                //Date dfDate = DBHandler.convertDate(rs.getDate(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]),rs.getTime(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]));
                //Date dfDate = rs.getTimestamp(PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]);
                Date dfDate = rs.getTimestamp("MAX_DATE");
                long oidMaterial = rs.getLong(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]);
                double qtyDf = rs.getDouble("SUM_QTY");

               if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    updateProses = updateMaterialStock(oidMaterial, oidLocation, 0, qtyDf, MODE_UPDATE, DOC_TYPE_DISPATCH, oidPeriode, con);
                } else {
                    //Update Qty only
                    updateProses = updateMaterialStock(oidMaterial, oidLocation, 0, qtyDf, MODE_INSERT, DOC_TYPE_DISPATCH, oidPeriode, con);
                }
                 if(!updateProses){
                    postingProgress.setNoteSumReceive("Id Material : "+oidMaterial + " FAILED UPDATED");
                    break;
                } else {
                      countStock = countStock+1;
                      postingProgress.setNoteSumDispatch("Id Material :"+oidMaterial);
                      postingProgress.setSumDocDispatchDone(countStock); //+ postingProgress.getSumDocDispatchDone());
                }
               
                //serial number update opie-eyek 20131202
                boolean inputSerialCode = postingSerialCode(oidDF,oidMaterial,DOC_TYPE_DISPATCH,oidLocation);

               //Thread.sleep(3000L);
                Thread.sleep(threadSleep);
            }
            //postingProgress.setSumDocDispatchDone(countStock + postingProgress.getSumDocDispatchDone());

            rs.close();
            //return countStock;
        } catch (Exception exc) {
            System.out.println("DF : " + exc);
        //update opie-eyek 20121026
        } finally {
            DBResultSet.close(dbrs);
        }
        return updateProses;
        //return vResult;
        //return countStock;



    }

    // ------------------------------- POSTING DISPATCH FINISH ---------------------------


    // ------------------------------- POSTING RECEIVE START ---------------------------
    /**
     * This method use to posting LGR document
     * @return
     * algoritm :
     *  - summary qty receive and grouping by item Receive based on docId and receiveId in table temp_post_doc
     *  - update qty_in to material Stock
     */
    //private Vector getUnPostedLGRDocument(long oidLocation, long lPeriodeOid) {
      //protected Vector getUnPostedLGRDocument(long oidLocation, long lPeriodeOid) {
       public boolean getUnPostedLGRDocument(long oidLocation, long lPeriodeOid,PostingProgress postingProgress, int threadSleep, Connection con) {
        //public int getUnPostedLGRDocument(long oidLocation, long lPeriodeOid, PostingProgress postingProgress) {
         int countStock = 0;
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
        //get ppn value based on system property
        // if ppn value not in system property, ppnValueDef = 0;
        int ppnValueDef = 0;
          try{
               ppnValueDef = Integer.parseInt(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
            }catch(Exception exc){
                  System.out.println(exc);
                  ppnValueDef = 0;
           }

        

        //get konfigurasi Ppn masukan di penerimaan, 0 = Biaya, 1 = Dikreditkan
        // if ppn value not in system property, Ppn masukan = 0;
        int ppnMasukan = 0;
        try {
              ppnMasukan = Integer.parseInt(PstSystemProperty.getValueByName("INCLUDE_PPN_MASUKAN"));
           }catch(Exception exc){
               System.out.println(exc);
                  ppnMasukan = 0;
           }

        
            
        // --- finish get time duration of transaction document that will posting ---
        boolean updateProses = false;
        try {
            String sql = "SELECT REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                         ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
                         ", MAX(REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") AS MAX_DATE " +
                         ", RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                         ", SUM(RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + ") AS SUM_QTY "+
                         //adding sum(qty* cost)for average stock
                         //by Mirahu
                         //20110927
                         ", SUM(RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + " * "+
                         " RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] + ") AS SUM_COST " ;
                          //adding ppn masukan biaya atau dikreditkan sum(qty* cost)for average stock
                         //by Mirahu
                         //20110929
                        
         if(ppnMasukan == MODE_DIKREDITKAN){
             /*sql = sql + ", SUM(RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + " * (" +
                         //"RECITEM." +PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]+
                         "RECITEM." +PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]+
                         "/1+(" +ppnValueDef+ "/100 * REC."+ PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN] +
                         "))) AS sumCostPPnMasukan " ;**/
             //adding cost+discount by Mirahu 20120712
              sql = sql + ", SUM(RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + 
                         "/1+(" +ppnValueDef+ "/100 * REC."+ PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN] +
                         ")) AS sumCostPPnMasukan " ;
         }
         else
             /*sql = sql + ", SUM(RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + " * " +
                         "RECITEM." +PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]+
                         "*(1+(" +ppnValueDef+ "/100 *(1-REC."+ PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN] +
                         ")))) AS sumCostPPnMasukan " ;*/
             //adding cost+discount by Mirahu 20120712
             sql = sql + ", SUM(RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + 
                         "*(1+(" +ppnValueDef+ "/100 *(1-REC."+ PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN] +
                         ")))) AS sumCostPPnMasukan " ;
                        
             sql = sql + " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC " +
                         " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RECITEM " +
                         " ON RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                         " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                         " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                         " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                         " = " + oidLocation;
         
            
            sql = sql + " GROUP BY RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                      + " ORDER BY REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + " DESC";
            
            System.out.println(">>> " + new SessPosting_1().getClass().getName() + ".getUnPostedLGRDocument() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                //updateProses = true;
                // Fetch all we needed
                long oidRM = rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]);
                int recSource = rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]);
                Date recDate = rs.getTimestamp("MAX_DATE");
                long oidMaterial = rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]);
                double qtyRec = rs.getDouble("SUM_QTY");
                double sumCostRec = rs.getDouble("SUM_COST");
                double sumCostRecPPnMasukan = rs.getDouble("sumCostPPnMasukan");
                
                Vector vErrUpdateStockByReceiveItem = new Vector(1,1);
                try {
                    //vErrUpdateStockByReceiveItem = updateStockByReceiveItem(lPeriodeOid, oidLocation, oidRM, recSource, ppn);
                     vErrUpdateStockByReceiveItem = updateStockByReceiveItem(lPeriodeOid,oidLocation, oidMaterial, recDate, qtyRec, sumCostRecPPnMasukan, con);
                }
                catch(Exception e) {
                    System.out.println("Exc when get vector vErrUpdateStockByReceiveItem... "+e.toString());
                    vErrUpdateStockByReceiveItem = new Vector(1,1);
                }

                /*Vector recFromPo = new Vector();
                try {
                    recFromPo = getReceivingDocFromPO(oidLocation);
                 }
                catch(Exception e) {
                    System.out.println("Exc when get vector getReceivingFromPO... "+e.toString());
                    recFromPo = new Vector(1,1);
                }*/

                boolean isOK = false;
                
                 // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, lPeriodeOid) == true) {
                    if (qtyRec > 0) {
                        updateProses = updateMaterialStock(oidMaterial, oidLocation, qtyRec, 0, MODE_UPDATE, DOC_TYPE_RECEIVE, lPeriodeOid, con);
                    }
                } else {
                    // Insert into MaterialStock
                    updateProses = updateMaterialStock(oidMaterial, oidLocation, qtyRec, 0, MODE_INSERT, DOC_TYPE_RECEIVE, lPeriodeOid, con);
                }
                if(!updateProses){
                    postingProgress.setNoteSumReceive("Id Material : "+oidMaterial + " FAILED UPDATED");
                    break;
                } else {
                countStock = countStock+1;
                postingProgress.setNoteSumReceive("Id Material : "+oidMaterial);
                postingProgress.setSumDocReceiveDone(countStock);// + postingProgress.getSumDocReceiveDone());
                }

                //proses penginputan serial code

                boolean inputSerialCode = postingSerialCode(oidRM,oidMaterial,DOC_TYPE_RECEIVE,oidLocation);


                //Thread.sleep(3000L);
                Thread.sleep(threadSleep);
            }
             if(updateProses){
              Vector recFromPo = new Vector();
                try {
                    recFromPo = getReceivingDocFromPO(oidLocation);
                 }
                catch(Exception e) {
                    System.out.println("Exc when get vector getReceivingFromPO... "+e.toString());
                    recFromPo = new Vector(1,1);
                }
             }
            //postingProgress.setSumDocReceiveDone(countStock + postingProgress.getSumDocReceiveDone());
            rs.close();
        } catch (Exception exc) {
              postingProgress.setNoteSumReceive("Exc in getUnPostedLGRDocument(#,#,#,#) : "+exc);
            //System.out.println("Exc in getUnPostedLGRDocument(#,#,#,#) : " + exc);
        //} finally {
            //DBResultSet.close(dbrs);
        //add opie-eyek 20121026
        }finally {
            DBResultSet.close(dbrs);
        }
        return updateProses;
        //return countStock;
        
    }

    /**
     *
     * @param oidPeriode
     * @param oidLocation
     * @param materialOid
     * @param receiveDate
     * @return
     * create by Mirahu
     * 20110902
     */

      private Vector updateStockByReceiveItem(long oidPeriode, long oidLocation, long materialOid, Date receiveDate, double recQtyNew, double sumRecCostPpnMasukan, Connection con) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);
        long oidOldLocation = 0;
        try {
            String sql = "SELECT " +
                    "REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
                    ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN] +
                    ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN] +
                    ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
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
                    " INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
                    " ON RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                    " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    " WHERE RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    " = " + materialOid +
                    " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                    " = " + oidLocation +
                    " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                    " = " +I_DocStatus.DOCUMENT_STATUS_FINAL +
                    " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                    " = '" + receiveDate+ "'" ;

            System.out.println(">>> " + new SessPosting_1().getClass().getName() + ".updateStockByReceiveItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();


            while (rs.next()) {
                int recSource = rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]);
                double ppn = rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN]);
                int includePpn = 0;
                includePpn = rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN]);
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
                //int includePpnMasukan = Integer.parseInt(PstSystemProperty.getValueByName("INCLUDE_PPN_MASUKAN"));
                //int ppnDefault = Integer.parseInt(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
                //double lastVat = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT]);
                //double lastVat = rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN]);
                //double lastVat = ppn;
                double currBuyPrice = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE]);
                double totalCostPpn = 0.0;
                double totalCurrBuyPrice = 0.0;

                if(includePpn == PstMatReceive.INCLUDE_PPN){
                    totalCostPpn = totalCost/1.1;
                   // totalCurrBuyPrice = totalCostAll;
                    totalCurrBuyPrice = totalCost + newForwarderCost;
                }
                else {
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
                if ((recSource == PstMatReceive.SOURCE_FROM_SUPPLIER) ||
                        (recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO || recSource == PstMatReceive.SOURCE_FROM_DISPATCH_UNIT )) {

                    //isOK = updateCostMaster(oidMaterial, oidCurrency, newCost);
                  if(recSource != PstMatReceive.SOURCE_FROM_DISPATCH_UNIT){
                    if(includePpn == PstMatReceive.INCLUDE_PPN){
                        isOK = updateCostMaster(oidMaterial, oidCurrency, totalCostPpn, con);
                        System.out.println("=============== update currBuy(cost)include Ppn : "+totalCostPpn);

                    }
                    else {
                    isOK = updateCostMaster(oidMaterial, oidCurrency, totalCost, con);
                    System.out.println("=============== update currBuy(cost) : "+totalCost);
                    }
                    updatePPnMaster(oidMaterial, oidCurrency, ppn, con);
                    updateForwarderCostMaster(oidMaterial, oidCurrency, newForwarderCost, con);



                    //updateCostSupplierPrice(matreceive.getSupplierId(), oidMaterial, matreceive.getCurrencyId(), (newCost + newForwarderCost));
                    //updateCostSupplierPrice(matreceive.getSupplierId(), oidMaterial, matreceive.getCurrencyId(), (totalCost + newForwarderCost));
                    //System.out.println("=============== update cost master : "+(totalCost + newForwarderCost));

                    //updateCurrBuyPrice + PPn 
                    updateCurrBuyPriceMaster(oidMaterial, oidCurrency, totalCurrBuyPrice, con);
                    System.out.println("=============== update currBuyPrice+PPn : "+totalCurrBuyPrice);
                    //end of CurrBuyPrice
                  }



                    // Update average price for each receive stock
                    double new_price = 0.0;
                    double avg_new_price = 0.0;
                    double sumNewCost = 0.0;
                    StandartRate startdartRate = PstStandartRate.getActiveStandardRate(oidCurrency);
                    //newCost = (startdartRate.getSellingRate() * newCost);
                 
                   sumNewCost = (startdartRate.getSellingRate() * sumRecCostPpnMasukan);
                
                        
                    
                    
                    double qtyStock = checkStockMaterial(oidMaterial, oidLocation, oidPeriode);
                    //double qtyStock = checkStockMaterial(oidMaterial, 0, oidPeriode);
                    if(qtyStock < 0) qtyStock = 0;
                    //avg_new_price = ((averagePrice * qtyStock) + (recQty * newCost)) / (qtyStock + recQty);
                    avg_new_price = ((averagePrice * qtyStock) + sumNewCost) / (qtyStock + recQtyNew);
                    updateAveragePrice(oidMaterial, oidCurrency, avg_new_price, con);

                    /*
                     * Cek if item receive mengandung component composit
                     */

                    Vector componentComposit = new Vector();
                    //cek disini opie-eyek 20121115 untuk composit
                    //buat con (transaction) sehingga mau sejalan dengan yang lainnya....
                    //contoh :  componentComposit = checkComponentComposit(oidMaterial, con);
                    //solusi : di hidden dlu, yang componentComposit = checkComponentComposit(oidMaterial); coba jalan kan query check apakah query berjalan sempurna ?
                    //componentComposit = checkComponentComposit(oidMaterial);
                    componentComposit = checkComponentComposit(oidMaterial,con);

                    /**
                     * untuk cek qty po
                     */
                    //if(recSource == PstMatReceive.SOURCE_FROM_SUPPLIER_PO){
                       // updateItemPO(oidMaterial,matreceive.getPurchaseOrderId(),recQty);
                   // }
                    //} else {
                    //if(recSource == PstMatReceive.SOURCE_FROM_RETURN){
                        //try{
                            //MatReturn matReturn = PstMatReturn.fetchExc(matreceive.getReturnMaterialId());
                            //oidOldLocation = matReturn.getLocationId();
                       // }catch(Exception e){}
                   // }else if(recSource == PstMatReceive.SOURCE_FROM_DISPATCH){
                        //try{
                            //MatDispatch matDispatch = PstMatDispatch.fetchExc(matreceive.getDispatchMaterialId());
                            //oidOldLocation = matDispatch.getLocationId();
                        //}catch(Exception e){}
                    //}
                    isOK = true;
                }

               

            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc in updateStockByReceiveItem(#,#,#,#) : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }
      
      /**
     * Get Receiving from Purchase Order
     * @param oidPeriode
     * @param oidLocation
     * @param materialOid
     * @param receiveDate
     * @return
     * create by Mirahu
     * 20110919
     */
     

     private Vector getReceivingDocFromPO(long oidLocation) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);
        long oidOldLocation = 0;
        try {
            String sql = "SELECT " +
                    " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
                    ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM] +
                    ", REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID] +
                    " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
                    " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RI" +
                    " ON RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                    " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP" +
                    " ON TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                    " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                    " = " + oidLocation +
                    " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                    " = " +I_DocStatus.DOCUMENT_STATUS_FINAL +
                    " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] +
                    " = " + PstMatReceive.SOURCE_FROM_SUPPLIER_PO ;

            System.out.println(">>> " + new SessPosting_1().getClass().getName() + ".updateStockByReceiveItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();


            while (rs.next()) {
                long oidRecmaterial = rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]);
                int recSource = rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]);
                long recFrom = rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM]);
                long oidPo = rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID]);
                
                
                /**
                  * untuk cek qty po
                 */
                    
                    getReceivingItemFromPO(oidRecmaterial, oidPo);
                   

               

            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc in updateStockByReceiveItem(#,#,#,#) : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }

     /**
     * Get Receiving from Purchase Order
     * @param oidPeriode
     * @param oidLocation
     * @param materialOid
     * @param receiveDate
     * @return
     * create by Mirahu
     * 20110919
     */


     private Vector getReceivingItemFromPO(long oidRecMaterial, long oidPo) {
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1, 1);
        long oidOldLocation = 0;
        try {
            String sql = "SELECT " +
                    "RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] +
                    ", RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
                    " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
                    " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RI" +
                    " ON RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                    " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                    " ON RI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                    " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP" +
                    " ON TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                    " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    " = " + oidRecMaterial ;

            System.out.println(">>> " + new SessPosting_1().getClass().getName() + ".updateStockByReceiveItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();


            while (rs.next()) {
                long oidMaterial = rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]);
                double newCost = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]);
                double recQty = rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]);


                /**
                  * untuk cek qty po
                 */

                    updateItemPO(oidMaterial,oidPo,recQty);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc in updateStockByReceiveItem(#,#,#,#) : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
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
                        purchOrder.setPoStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
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

    // ------------------------------- POSTING RECEIVE FINISH ---------------------------




    // ------------------------------- POSTING RETURN START ---------------------------
    /**
     * This method use to posting return document
     * @param lLocationOid
     * @param lPeriodeOid
     * @return
     * @created by Edhy
     * algoritm :
     *  - summary qty return and grouping by item return based on docId and returnId in table temp_post_doc
     *  - update qty_out to material Stock
     */
    //private Vector getUnPostedReturnDocument(long oidLocation, long oidPeriode) {
      //protected Vector getUnPostedReturnDocument(long oidLocation, long oidPeriode) {
      public boolean getUnPostedReturnDocument(long oidLocation, long oidPeriode, PostingProgress postingProgress, int threadSleep, Connection con) {
       //public int getUnPostedReturnDocument(long oidLocation, long oidPeriode, PostingProgress postingProgress) {
        int countStock = 0;
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

        boolean updateProses = false;
        try {
            // Select all receive today with detail
            String sql = "SELECT RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         ", RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_SOURCE] +
                         ", MAX(RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + ") AS MAX_DATE "+
                         ", RETITEM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] +
                         ", SUM(RETITEM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS SUM_QTY "+
                         " FROM " + PstMatReturn.TBL_MAT_RETURN + " RET " +
                         " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RETITEM " +
                         " ON RETITEM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                         " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                         " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                         " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                         " = " + oidLocation;


            sql = sql + " GROUP BY RETITEM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID]
                      + " ORDER BY RET."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] + " DESC";

            System.out.println(">>> " + new SessPosting_1().getClass().getName() + ".getUnPostedReturnDocument() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            boolean isOK = false;
            while (rs.next()) {
                //updateProses = true;
                // Fetch all we needed
                //int countStock = 0;
                long oidRET = rs.getLong(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID]);
                int retSource = rs.getInt(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_SOURCE]);
                //Date retDate = DBHandler.convertDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]),rs.getTime(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]));
                //Date retDate = rs.getTimestamp(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]);
                Date retDate = rs.getTimestamp("MAX_DATE");
                long oidMaterial = rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID]);
                double qtyRet = rs.getDouble("SUM_QTY");

                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    updateProses = updateMaterialStock(oidMaterial, oidLocation, 0, qtyRet, MODE_UPDATE, DOC_TYPE_RETURN, oidPeriode, con);
                } else {
                    //Update Qty only
                    updateProses = updateMaterialStock(oidMaterial, oidLocation, 0, qtyRet, MODE_INSERT, DOC_TYPE_RETURN, oidPeriode, con);
                }
                if(!updateProses){
                    postingProgress.setNoteSumReceive("Id Material : "+oidMaterial + " FAILED UPDATED");
                    break;
                } else {
                    countStock = countStock+1;
                    postingProgress.setNoteSumReturn("Id Material :"+oidMaterial);
                    postingProgress.setSumDocReturnDone(countStock); //+ postingProgress.getSumDocReturnDone());   
                }
                
                //posting serial number
                 boolean inputSerialCode = postingSerialCode(oidRET,oidMaterial,DOC_TYPE_RETURN,oidLocation);

               // Thread.sleep(3000L);
                Thread.sleep(threadSleep);

   
            }
            //postingProgress.setSumDocReturnDone(countStock + postingProgress.getSumDocReturnDone());
            rs.close();
        } catch (Exception exc) {
            System.out.println("RET : " + exc);
        //update opie-eyek 20121026
        } finally {
           DBResultSet.close(dbrs);
        }
        return updateProses;
        //return vResult;
        //return countStock;
    }

    // ------------------------------- POSTING RETURN FINISH ---------------------------





    // ------------------------------- POSTING COST START ---------------------------
    /**
     * This method use to posting costing document
     * @param lLocationOid
     * @param lPeriodeOid
     * @return
     * @created by Mirahu
     * 20110902
     * algoritm :
     *  - summary qty costing and grouping by item Costing based on docId and costingId in table temp_post_doc
     *  - update qty_out to material Stock
     */
    //private Vector getUnPostedCostDocument(long oidLocation, long oidPeriode) {
      //protected Vector getUnPostedCostDocument(long oidLocation, long oidPeriode) {
      public boolean getUnPostedCostDocument(long oidLocation, long oidPeriode, PostingProgress postingProgress, int threadSleep, Connection con) {
       //public int getUnPostedCostDocument(long oidLocation, long oidPeriode, PostingProgress postingProgress) {
        int countStock = 0;
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
        boolean updateProses = false;
        try {

             String sql = "SELECT COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                         ", MAX(COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + ") AS MAX_DATE"+
                         ", COSTITEM." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                         ", SUM(COSTITEM." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + ") AS SUM_QTY "+
                         " FROM " + PstMatCosting.TBL_COSTING + " COST " +
                         " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " COSTITEM " +
                         " ON COSTITEM." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                         " = COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                         " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                         " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                         " = " + oidLocation;


            sql = sql + " GROUP BY COSTITEM." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]
                      + " ORDER BY COST."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " DESC";
            
            System.out.println(">>> " + new SessPosting_1().getClass().getName() + ".getUnPostedDFDocument() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            boolean isOK = false;

            while (rs.next()) {
                //updateProses = true;
                // Fetch all we needed
                
                long oidCost = rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]);
                //Date costDate = DBHandler.convertDate(rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]),rs.getTime(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
                //Date costDate = rs.getTimestamp(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]);
                Date costDate = rs.getTimestamp("MAX_DATE");
                long oidMaterial = rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]);
                //double qtyCost = rs.getDouble("SUM_QTY");
                double dfQtyReal = rs.getDouble("SUM_QTY");

                //double qtyPerBaseUnit = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]), rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //double dfQtyReal = qtyCost * qtyPerBaseUnit; // jumlah barang yg akan masuk stock

                // Check if this item is allready exists in MaterialStock
                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    updateProses = updateMaterialStock(oidMaterial, oidLocation, 0, dfQtyReal, MODE_UPDATE, DOC_TYPE_COSTING, oidPeriode, con);
                } else {
                    //Update Qty only
                    updateProses = updateMaterialStock(oidMaterial, oidLocation, 0, dfQtyReal, MODE_INSERT, DOC_TYPE_COSTING, oidPeriode, con);
                }
                if(!updateProses){
                    postingProgress.setNoteSumReceive("Id Material : "+oidMaterial + " FAILED UPDATED");
                    break;
                } else {
                    countStock = countStock+1;
                    postingProgress.setNoteSumCosting("Id Material :"+oidMaterial);
                    postingProgress.setSumDocCostingDone(countStock); //+ postingProgress.getSumDocCostingDone());
                }
                
                //posting serial number 20131202
                boolean inputSerialCode = postingSerialCode(oidCost,oidMaterial,DOC_TYPE_COSTING,oidLocation);

                //Thread.sleep(3000L);
                Thread.sleep(threadSleep);
                
            }
            //postingProgress.setSumDocCostingDone(countStock + postingProgress.getSumDocCostingDone());
            rs.close();
        } catch (Exception exc) {
            System.out.println("DF : " + exc);
        //} finally {
            //DBResultSet.close(dbrs);
        }
        return updateProses;
        //return vResult;
        //return countStock;

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
     *  - summary qty receive and grouping by item Receive based on docId and receiveId in table temp_post_doc
     *  - update qty_in to material Stock
     */
    //private Vector getUnPostedSalesDocument(long oidLocation, long oidPeriode) {
      //protected Vector getUnPostedSalesDocument(long oidLocation, long oidPeriode) {
        public boolean getUnPostedSalesDocument(long oidLocation, long oidPeriode, PostingProgress postingProgress, int threadSleep,Connection con) {
       //public int getUnPostedSalesDocument(long oidLocation, long oidPeriode, PostingProgress postingProgress) {
        int countStock = 0;
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
        boolean updateProses = false;
        try {


            String sql = " SELECT MAX(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") AS MAX_DATE " +
                         ", CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                         ", SUM(CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") AS SUM_QTY " +
                         ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
                         " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM " +
                         " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD " +
                         " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                         " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " CSH " +
                         " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
                         " = CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] ;

                String sWhereClause = "";
                 sWhereClause = "(CBM." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +" = " + I_DocStatus.DOCUMENT_STATUS_FINAL +" OR CBM." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +" = " + I_DocStatus.DOCUMENT_STATUS_DRAFT+" OR CBM." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +" = " + I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED+")"+
                            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                         
                           // " AND CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE]+
                           // " = " + PstMaterial.MAT_TYPE_REGULAR;
                            " AND ( CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE]+
                            " = " + PstMaterial.MAT_TYPE_REGULAR+
                            " OR CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE]+
                            " = " + PstMaterial.MAT_TYPE_COMPOSITE+" )";
                         
                sWhereClause = sWhereClause + " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                        " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";

                sWhereClause = sWhereClause + " AND CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] +
                        " != 1";
                
                if (sWhereClause != null && sWhereClause.length() > 0) {
                    sql = sql + " WHERE " + sWhereClause;
                }
                //order by cash_cashier
                sql = sql + " GROUP BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                          + ", CBD." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+
                            " ORDER BY CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID];
           

            System.out.println(">>> " + new SessPosting().getClass().getName() + ".getUnPostedSalesDocument() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //updateProses = true;
                // Fetch all we needed
                boolean isOK = false;
                //int countStock = 0;
                //Date billDate = DBHandler.convertDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]),rs.getTime(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                //Date billDate = rs.getTimestamp(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]);
                Date billDate = rs.getTimestamp("MAX_DATE");
                long oidMaterial = rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]);
                double qtyBill = rs.getDouble("SUM_QTY");
                int docType = rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]);

                if (checkMaterialStock(oidMaterial, oidLocation, oidPeriode) == true) {
                    // Update Qty only
                    if (qtyBill != 0) {
                                if (docType == PstBillMain.TYPE_INVOICE)
                                    updateProses = updateMaterialStock(oidMaterial, oidLocation, 0, qtyBill, MODE_UPDATE, DOC_TYPE_SALE_REGULAR, oidPeriode, con);

                                else
                                    updateProses = updateMaterialStock(oidMaterial, oidLocation, qtyBill, 0, MODE_UPDATE, DOC_TYPE_RECEIVE, oidPeriode, con); // retur
                            }
                        } else {
                            // Insert Stock and its qty
                            if (docType == PstBillMain.TYPE_INVOICE) {
                                //sellQtyReal = 0 - sellQtyReal; // karena sudah ada penjualan maka qty menjadi min.
                                updateProses = updateMaterialStock(oidMaterial, oidLocation, 0, qtyBill, MODE_INSERT, DOC_TYPE_SALE_REGULAR, oidPeriode, con);
                            } else {
                                updateProses = updateMaterialStock(oidMaterial, oidLocation, qtyBill, 0, MODE_INSERT, DOC_TYPE_RECEIVE, oidPeriode, con);
                            }
                        }
                     if(!updateProses){
                        postingProgress.setNoteSumReceive("Id Material : "+oidMaterial + " FAILED UPDATED");
                        break;
                    } else {
                        countStock = countStock+1;
                        postingProgress.setNoteSumSales("Id Material :"+oidMaterial);
                        postingProgress.setSumDocSalesDone(countStock); //+ postingProgress.getSumDocSalesDone());
                
                        //Vector vErrUpdateStockBySaleItem = updateStockBySalesItem(postingDate, oidSL, oidLocation, oidPeriode);
                        //Vector vErrUpdateStockBySaleItem = updateCompositBySalesItem(oidLocation, oidPeriode);   
               }
                     
                          
                //Thread.sleep(3000L);
                Thread.sleep(threadSleep);
            }
            
            //Vector vErrUpdateStockBySaleItem = updateCompositBySalesItem(oidLocation, oidPeriode, con);
            
            //postingProgress.setSumDocSalesDone(countStock + postingProgress.getSumDocSalesDone());
            rs.close();
        } catch (Exception exc) {
            System.out.println("Exc when Process SALE : " + exc);
        //update opie-eyek 20121026
        } finally {
            DBResultSet.close(dbrs);
        }
        return updateProses;
        //return countStock;
        //return vResult;

        
    }

   
    // synchronized private Vector updateCompositBySalesItem(long oidLocation, long oidPeriode, Connection con) {
       synchronized public Vector updateCompositBySalesItem(long oidLocation, long oidPeriode, Connection con) {
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
                    //+inner join temp for select doc yang termasuk dalam proses posting
                    //by mirahu 20120913
                    " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                    " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                    //" = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                    " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED +
                    " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    " = " + oidLocation +
                    " AND BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE] +
                    " = " + PstMaterial.MAT_TYPE_COMPOSITE +
                    " AND CASH." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID]+
                    " != 1" +
                    " ORDER BY CASH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID];
            
            //System.out.println(">>> "+ new SessPosting().getClass().getName()+".updateStockBySalesItem() : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
          
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
              
                
                Thread.sleep(50);
                boolean isOK = false;
             
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

                          //generate costing by shift=oidCashCashierDetail
                          //membuat main document costing, pembiayaan dilakukan di shif dimana transaksi composite terjadi
                          getListCostingMaterial = getCostingMaterial(oidCashCashierDetail);

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
                           matCosting.setCostingStatus(0); //update opie-eyek 20140422 costing tidak langsung final
                           matCosting.setRemark("Generated by System for Composite CoGS");

                           int maxCounter = sessCosting.getMaxCostingCounter(matCosting.getCostingDate(), matCosting);
                           maxCounter = maxCounter + 1;

                           matCosting.setCostingCodeCounter(maxCounter);
                           matCosting.setCostingCode(sessCosting.generateCostingCode(matCosting));
                           matCosting.setCostingTo(oidLocation);
                           matCosting.setCostingId(0);
                           matCosting.setCashCashierId(oidCashCashierDetail);
                           matCosting.setLastUpdate(new Date());
                          
                           //disini ditmbahkan con, transaction con.
                           //exmp : oid = pstMatCosting.insertExc(matCosting, con);
                           //contohnya bisa di lihat di updateQtyMaterialStock
                           oid = pstMatCosting.insertExc(matCosting);


                           //Vector vListComposit = updateStockBySalesItem(postingDate, oidSL, oidLocation, oidPeriode, oidCashCashier);
                        }

                         if(oidCashCashierDetail == oidCashCashierCosting){
                             matCosting.setOID(oid);
                             matCosting.setLocationId(oidLocation);
                             matCosting.setLocationType(1);
                             matCosting.setCostingDate(costingDate);
                             matCosting.setCostingStatus(0);//update opie-eyek 20140214 untuk document costing generate by sistem di buatkan draft dlu
                             matCosting.setRemark(costingRemark);
                             matCosting.setCostingCodeCounter(costingCodeCounter);
                             matCosting.setCostingCode(costingCode);
                             matCosting.setCostingTo(oidLocation);
                             matCosting.setCostingId(0);
                             matCosting.setLastUpdate(new Date());

                           //disini ditmbahkan con, transaction con.
                           //exmp : oid = pstMatCosting.insertExc(matCosting, con);
                           //contohnya bisa di lihat di updateQtyMaterialStock
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

                        //disini ditmbahkan con, transaction con.
                        //exmp : oid = pstMatCosting.insertExc(matCosting, con);
                        //contohnya bisa di lihat di updateQtyMaterialStock
                        //proses insert detail costing
                        listItemCosting = ListComponentComposit(oidMaterial, oid, sellQtyReal);

            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("SALE : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
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

    /**
     *
     * @param oidLocation
     * @param oidPeriode
     * @return
     * by Mirahu
     */
    private static MaterialStock getMaterialStockNew(long oidLocation, long oidPeriode) {
        MaterialStock materialStock = new MaterialStock();
        DBResultSet dbrs = null;
        try {
            //Select all receive today with detail
            String sql = "SELECT * " +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK +
                    " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
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

    

   // public static boolean setDocumentByPosted(long oidDocument, int docType) {
        //return setPosted(docType);

   //public static synchronized void updateDocStatus(int docType, PostingProgress postingProgress){
    /**public static synchronized boolean updateDocStatus(int docType, Connection con){
    boolean hasil = false;
    Connection con = DBHandler.getDBConnection();

    try{
	//boolean hasil = false;
	con.setAutoCommit(false);

	hasil = setPosted(docType);

	if(hasil = true){
	    con.commit();
	}else{
	    con.rollback();
	}
    }catch(Exception e){
        System.out.println("eRR >>= UpdQtyStiock : " + e.toString());
    } finally {
        try {
            con.close();
        }catch(Exception e){
            System.out.println("eRR >>connectionClose : " + e.toString());
        }
    }

    return hasil;
}**/


   // }
    /**
     * Set status semua document yg sudah diposting menjadi posted
     * @param oidDocument
     * @param docType
     * @return
     * @update by Edhy
     */
    private static boolean setPosted(int docType, Connection con) {
        boolean hasil = false;
        try {
            String sql = "";
            switch (docType) {
                case DOC_TYPE_RECEIVE:// Receive
                    sql = " UPDATE " + PstMatReceive.TBL_MAT_RECEIVE + " REC " +
                          " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                          " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                          " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                          " SET REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                          " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED ;
                    break;

                case DOC_TYPE_RETURN:// Return
                    sql = "UPDATE " + PstMatReturn.TBL_MAT_RETURN + " RET " +
                          " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                          " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                          " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                          " SET RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                          " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED ;
                    break;

                case DOC_TYPE_DISPATCH://Dispatch
                    sql = "UPDATE " + PstMatDispatch.TBL_DISPATCH + " DF " +
                          " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                          " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                          " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                          " SET DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                          " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED ;
                    break;

                case DOC_TYPE_SALE_REGULAR://Sale
                    sql = "UPDATE " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM " +
                          " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                          " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                          " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                          " SET CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                          " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED ;
                    break;

                case DOC_TYPE_COSTING:// costing
                    sql = "UPDATE " + PstMatCosting.TBL_COSTING + " COST " +
                          " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                          " ON COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                          " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                          " SET COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                          " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED ;
                    break;      
            }
            System.out.println("set posted: "+sql);
            int a = DBHandler.execUpdate(sql, con);
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
    private static boolean updateCostMaster(long oidMaterial, long currencyId, double newCost, Connection con) {
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
            int a = DBHandler.execUpdate(sql, con);
            //adding update ke pstDataSyncSql 20120601
            //update opie-eyek 20121024
            if (vecDbConn!=null && vecDbConn.size()>0){
                long oidDataSync=PstDataSyncSql.insertExc(sql);
                PstDataSyncStatus.insertExc(oidDataSync, vecDbConn);
            }
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
    private static boolean updatePPnMaster(long oidMaterial, long currencyId, double ppn, Connection con) {
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
            int a = DBHandler.execUpdate(sql, con);
            //adding update ke pstDataSyncSql 20120601
            long oidDataSync=PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);
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
    private static boolean updateForwarderCostMaster(long oidMaterial, long currencyId, double forwarderCost, Connection con) {
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
            int a = DBHandler.execUpdate(sql, con);
            //adding update ke pstDataSyncSql 20120601
            long oidDataSync=PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);
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
    private static boolean updateCurrBuyPriceMaster(long oidMaterial, long currencyId, double newCost, Connection con) {
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
            int a = DBHandler.execUpdate(sql, con);
            //adding update ke pstDataSyncSql 20120601
            long oidDataSync=PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);
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
    private static boolean updateAveragePrice(long oidMaterial, long currencyId, double newAverage, Connection con) {
        boolean hasil = false;
        try {
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
                    " SET " + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " = " + newAverage;
                    if(currencyId!=0){
                        sql = sql + ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
                        " = " + currencyId;
                    }
                    sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            // System.out.println(" sql >> " + sql);
            int a = DBHandler.execUpdate(sql, con);
            //adding update ke pstDataSyncSql 20120601
            long oidDataSync=PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);
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
     * @update by Mirah
     */
    synchronized private static boolean updateMaterialStock(long oidMaterial, long oidLocation, double QtyIn, double QtyOut, int mode, int docType, long oidPeriode, Connection con) {
        boolean hasil = false;
        try {
            String sql = "";
            switch (mode) {
                case MODE_UPDATE:
                    //MaterialStock materialStock = new MaterialStock();
                    //materialStock = getMaterialStock(oidMaterial, oidLocation, oidPeriode);
                    //update opie-eyek 2012-10-10
                    switch (docType) {
                        case DOC_TYPE_RECEIVE:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                                    //" = " + (QtyIn + materialStock.getQtyIn()) ;
                                    " = " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +" + "+ QtyIn;
                            break;

                        case DOC_TYPE_RETURN:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                                    //" = " + (materialStock.getQtyOut() + QtyOut) ;
                                    " = " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +" + "+ QtyOut;
                            break;

                        case DOC_TYPE_DISPATCH:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                                   // " = " + (materialStock.getQtyOut() + QtyOut) ;
                                     " = " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +" + "+ QtyOut;
                            break;

                        case DOC_TYPE_COSTING:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                                   // " = " + (materialStock.getQtyOut() + QtyOut) ;
                                     " = " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +" + "+ QtyOut;
                            break;

                        case DOC_TYPE_SALE_REGULAR:
                            sql = "UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                                    " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                                    //" = " + (materialStock.getQtyOut() + QtyOut) ;
                                     " = " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +" + "+ QtyOut;
                            break;

                      
                    }

                    sql = sql + " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                            " = " + oidMaterial +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                            " = " + oidPeriode;


                    int iUpdated = 1; // DBHandler.execUpdate(sql);
                    DBHandler.execUpdate(sql,con);
                    if (iUpdated > 0) {
                        hasil = true;
                        System.out.println("upadate Qty temp sukses");
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
  * checkComponentComposit untuk
  * opie-eyek 20121115
  * @param oidMaterial
  * @param con
  * @return
  */
     private static Vector checkComponentComposit(long oidMaterial,Connection con ) {
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
                     vGrandTotalComposit = grandTotalComposit(materialId,con);
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


         private static Vector grandTotalComposit(long materialId,Connection con) {
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

                updateCostAndStockValueMaster(materialId,sumStockValue, sumCostValue, sumCostValuePpn, lastPpn,con);
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
        String sql ="";
        try {
            // Select all receive today with detail
            sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
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
            System.out.println("eRR >>= sql : " + sql );
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        return hasil;
    }

      private static boolean updateCostAndStockValueMaster(long oidMaterial, double stockValue, double newCost, double newCostPpn, double lastVat,Connection con) {
        boolean hasil = false;
        String sql ="";
        try {
            // Select all receive today with detail
            sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
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
            int a = DBHandler.execUpdate(sql,con);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= sql : " + sql );
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
                    //cek, jika material tsb adalah tipe composite.
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
                 double qtyPrev = 0;
                 
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
                             qtyPrev = matCostingItem.getResidueQty();
                          }
                  }


                 //qtyAll =  qtySale * (qty + qtyCostItem);
                 //cara perhitungan opie-eyek 20140422
                 qtyAll =(qtySale*qty)+qtyCostItem;
                  
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
 * update opie-eyek
 * @param oidMaterial
 * @param oidCostingMaterial
 * @param qtySale
 * @param con
 * @return
 */
      private static Vector ListComponentComposit(long oidMaterial, long oidCostingMaterial, double qtySale, Connection con) {
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
                    //disini kacau opie-eyek 15112012
                    Vector listItemCosting = ListComponentComposit(materialComponentId, oidCostingMaterial, qtySale,con);
                   // Vector componentComposit = new Vector();
                   //componentComposit = checkComponentComposit(oidMaterial,con);
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


   //public static synchronized void updateQtyStock(long oidLocation, long oidPeriode){
    //public static synchronized boolean updateQtyStock(long oidLocation, long oidPeriode){
    /** public static boolean updateQtyStock(long oidLocation, long oidPeriode){
     boolean hasil = false;
      Connection con = DBHandler.getDBConnection();
    
      try{
	//boolean hasil = false;
	con.setAutoCommit(false);

	hasil = updateQtyMaterialStock(oidLocation, oidPeriode);

	if(hasil = true){
	    con.commit();
	}else{
	    con.rollback();
	}
    }catch(Exception e){
        System.out.println("eRR >>= UpdQtyStiock : " + e.toString());
    } finally {
        try {
            con.close();
        }catch(Exception e){
            System.out.println("eRR >>connectionClose : " + e.toString());
        }
    }
    return hasil;
}**/

  public static boolean updateQtyStockAndDoc(long oidLocation, long oidPeriode, PostingProgress postingProgress, Connection con){
     boolean hasil = false;
     boolean hasilStock = false;
     boolean hasilRec = false;
     boolean hasilDf = false;
     boolean hasilSales = false;
     boolean hasilRet = false;
     boolean hasilCost = false;


    try{
	//boolean hasil = false;
	

        //set update stock
        try {
           hasilStock = updateQtyMaterialStock(oidLocation, oidPeriode,con);
           if (hasilStock == true ){
                  postingProgress.setNoteUpdateStock("Update Stock Success");
           } else {
                postingProgress.setNoteUpdateDocReceive("Update Stock Failed");
           }
        }catch (Exception e) {
           System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
           //setStatusTxt(" update Stock Failed ...."+ e.toString());
        }
        //setStatusTxt(" set Document Status....");
        //set Document Receive to closed
        try {
            System.out.println("update status Document Receive :");
            //threadPosting.setStatusTxt(" update Status Document Receive....");
            //hasilRec = updateDocStatus(DOC_TYPE_RECEIVE);
            hasilRec = setPosted(DOC_TYPE_RECEIVE,con);
            if (hasilRec == true ){
                  postingProgress.setNoteUpdateDocReceive("Update Document Receive Success");
           } else {
                  postingProgress.setNoteUpdateDocReceive("Update Document Receive Failed");
            }
        } catch (Exception e) {
           System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
           //threadPosting.setStatusTxt(" update Status Document Receive Failed ...."+ e.toString());
        }

        //set Document Dispatch to closed
        try {
            System.out.println("update status Document Dispatch :");
            //threadPosting.setStatusTxt(" update Status Document Dispatch....");
            //hasilDf = updateDocStatus(DOC_TYPE_DISPATCH);
            hasilDf = setPosted(DOC_TYPE_DISPATCH,con);
             if (hasilDf == true ){
                   postingProgress.setNoteUpdateDocDispatch("Update Document Dispatch Success");
             } else {
                   postingProgress.setNoteUpdateDocDispatch("Update Document Dispatch Failed");
             }
        } catch (Exception e) {
           System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
           //threadPosting.setStatusTxt(" update Status Document Dispatch Failed ...."+ e.toString());
        }

        //set Document Sales to closed
        try {
            System.out.println("update status Document Sales :");
            //threadPosting.setStatusTxt(" update Status Document Sales....");
            //hasilSales = updateDocStatus(DOC_TYPE_SALE_REGULAR);
            hasilSales = setPosted(DOC_TYPE_SALE_REGULAR,con);
            if (hasilSales == true ){
                  postingProgress.setNoteUpdateDocSales("Update Document Sales Success");
             } else {
                  postingProgress.setNoteUpdateDocSales("Update Document Sales Failed");
             }
        } catch (Exception e) {
          System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
          //threadPosting.setStatusTxt(" update Status Document Sales Failed ...."+ e.toString());
        }

        //set Document Return to closed
        try {
            System.out.println("update status Document Return :");
            //threadPosting.setStatusTxt(" update Status Document Return....");
            //hasilRet = updateDocStatus(DOC_TYPE_SALE_RETURN);
            hasilRet = setPosted(DOC_TYPE_RETURN,con);
             if (hasilRet == true ){
                   postingProgress.setNoteUpdateDocReturn("Update Document Return Success");
             } else {
                   postingProgress.setNoteUpdateDocReturn("Update Document Return Failed");
             }
        } catch (Exception e) {
            System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
            //threadPosting.setStatusTxt(" update Status Document Return Failed ...."+ e.toString());
        }

        //set Document Costing to closed
        try {
            System.out.println("update status Document Costing :");
            //threadPosting.setStatusTxt(" update Status Document Costing....");
            //hasilCost = updateDocStatus(DOC_TYPE_COSTING);
            hasilCost = setPosted(DOC_TYPE_COSTING,con);
            if (hasilCost == true ){
                  postingProgress.setNoteUpdateDocCosting("Update Document Costing Success");
            } else {
                  postingProgress.setNoteUpdateDocCosting("Update Document Costing Failed");
           }
        } catch (Exception e) {
             System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
              //threadPosting.setStatusTxt(" update Status Document Costing Failed ...."+ e.toString());
        }

	if(hasilStock == true && hasilRec == true && hasilDf == true && hasilSales == true && hasilRet == true && hasilCost == true){	  
            hasil = true;
	}else{
	    hasil = false;
	}
    }catch(Exception e){
        System.out.println("eRR >>= UpdQtyStiock : " + e.toString());
    } finally {
        try {
            con.close();
        }catch(Exception e){
            System.out.println("eRR >>connectionClose : " + e.toString());
        }
    }
    return hasil;
}
  
  
  public static boolean updateQtyStockAndDocPerDoc(long oidLocation, long oidPeriode, PostingProgress postingProgress, Connection con, int docType){
     boolean hasil = false;
     boolean hasilStock = false;
     boolean hasilDoc = false;
     
    try{
	//boolean hasil = false;
	

        //set update stock
        try {
           hasilStock = updateQtyMaterialStock(oidLocation, oidPeriode,con);
           if (hasilStock == true ){
                  postingProgress.setNoteUpdateStock("Update Stock Success");
           } else {
                postingProgress.setNoteUpdateStock("Update Stock Failed");
           }
        }catch (Exception e) {
           System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
           //setStatusTxt(" update Stock Failed ...."+ e.toString());
        }

        //setStatusTxt(" set Document Status....");
        //set Document Receive to closed
        try {
            System.out.println("update status Document Receive :");
            //threadPosting.setStatusTxt(" update Status Document Receive....");
            //hasilRec = updateDocStatus(DOC_TYPE_RECEIVE);
            hasilDoc = setPosted(docType,con);
            if (hasilDoc == true ){
                  if(docType == DOC_TYPE_RECEIVE) {
                        postingProgress.setNoteUpdateDocReceive("Update Document Receive Success");
                  } else if(docType == DOC_TYPE_DISPATCH){
                        postingProgress.setNoteUpdateDocDispatch("Update Document Dispatch Success");   
                  }  else if(docType == DOC_TYPE_SALE_REGULAR){
                        postingProgress.setNoteUpdateDocSales("Update Document Sales Success");   
                  }  else if(docType == DOC_TYPE_RETURN){
                        postingProgress.setNoteUpdateDocReturn("Update Document Return Success");   
                  }  else if(docType == DOC_TYPE_COSTING){
                        postingProgress.setNoteUpdateDocCosting("Update Document Costing Success");   
                  }
           } else {
                  if(docType == DOC_TYPE_RECEIVE) {
                  postingProgress.setNoteUpdateDocReceive("Update Document Receive Failed");
                  }  else if(docType == DOC_TYPE_DISPATCH){
                        postingProgress.setNoteUpdateDocDispatch("Update Document Dispatch Failed");   
                  }  else if(docType == DOC_TYPE_SALE_REGULAR){
                        postingProgress.setNoteUpdateDocSales("Update Document Sales Failed");   
                  }  else if(docType == DOC_TYPE_RETURN){
                        postingProgress.setNoteUpdateDocReturn("Update Document Return Failed");   
                  }  else if(docType == DOC_TYPE_COSTING){
                        postingProgress.setNoteUpdateDocCosting("Update Document Costing Failed");   
                  }
            }
        } catch (Exception e) {
           System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
           //threadPosting.setStatusTxt(" update Status Document Receive Failed ...."+ e.toString());
        }

        
	if(hasilStock == true && hasilDoc == true){	  
            hasil = true;
	}else{
	    hasil = false;
	}
    }catch(Exception e){
        System.out.println("eRR >>= UpdQtyStiock : " + e.toString());
    //} finally {
       // try {
            //con.close();
        //}catch(Exception e){
           // System.out.println("eRR >>connectionClose : " + e.toString());
        //}
    }
    return hasil;
}



   private static boolean updateQtyMaterialStock(long oidLocation, long oidPeriode, Connection con) {
       // MaterialStock materialStock = new MaterialStock();
       // materialStock = getMaterialStockNew(oidLocation, oidPeriode);
        boolean hasil = false;
        try {
            // Select all receive today with detail
            String sql = " UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                         " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                         " = (" + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]+
                         " + " +PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN]+
                         " - " +PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +")";
                        // " = " + (materialStock.getQty() + materialStock.getQtyIn()- materialStock.getQtyOut());
                   sql = sql + " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                            " = " + oidPeriode;

            int a = DBHandler.execUpdate(sql, con);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        return hasil;
    }
   //set qty in and qty out =0
   public static void setQtyInOutStokNol(long oidLocation, long oidPeriode) {
        try {
            // Select all receive today with detail
            String sql = " UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                         " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                         " = 0," +PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT]+
                         " = 0 ";
                        // " = " + (materialStock.getQty() + materialStock.getQtyIn()- materialStock.getQtyOut());
                   sql = sql + " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                            " = " + oidPeriode;

            int a = DBHandler.execUpdate(sql);
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        
    }
   
   //set qty in and qty out =0
   public static void setQtyInOutStokNolCon(long oidLocation, long oidPeriode, Connection con)
    throws DBException {
        try {
            // Select all receive today with detail
            String sql = " UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                         " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                         " = 0," +PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT]+
                         " = 0 ";
                        // " = " + (materialStock.getQty() + materialStock.getQtyIn()- materialStock.getQtyOut());
                   sql = sql + " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                            " = " + oidPeriode;

            int a = DBHandler.execUpdate(sql,con);
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
             throw new DBException(null, exc);
        }
        
    }


 
/*
 * Get Count Receive For Update Stock
 * By Mirahu
 * 20110912
 */
 public static int getCountRec(long oidLocation) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(DISTINCT RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + ") FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC " +
                        //"SELECT COUNT(" + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] + ") FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC " +
                         " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RECITEM " +
                         " ON RECITEM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                         " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                         " = " +oidLocation+
                         " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                         "= " +I_DocStatus.DOCUMENT_STATUS_FINAL;
           

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                //postingProgress.setSumDocReceive(rs.getInt(1) + postingProgress.getSumDocReceive());
                count = rs.getInt(1);
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
            //System.out.println("eRR >>= CountRec : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }

 /*
 * Get Count Dispatch For Update Stock
 * By Mirahu
 * 20110912
 */
 public static int getCountDf(long oidLocation) {
   //public void getCountDf(long oidLocation) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(DISTINCT DFITEM." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + ") FROM " + PstMatDispatch.TBL_DISPATCH + " DF " +
                    //"SELECT COUNT(" + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] + ") FROM " + PstMatDispatch.TBL_DISPATCH + " DF " +
                         " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFITEM " +
                         " ON DFITEM." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                         " = DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                         " = " +oidLocation+
                         " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                         "= " +I_DocStatus.DOCUMENT_STATUS_FINAL;


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
                //postingProgress.setSumDocDispatch(rs.getInt(1) + postingProgress.getSumDocDispatch());
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
            //System.out.println("eRR >>= CountDf : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }

  /*
 * Get Count Return For Update Stock
 * By Mirahu
 * 20110912
 */

 
 public static int getCountSales(long oidLocation) {
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT COUNT(DISTINCT CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                         ", CBM." +PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+ ") AS COUNT FROM "
                          + PstBillMain.TBL_CASH_BILL_MAIN + " CBM " +
                         " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD " +
                         " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                         " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " CSH " +
                         " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
                         " = CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] ;

                String sWhereClause = "";
                 sWhereClause = "(CBM." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +" = " + I_DocStatus.DOCUMENT_STATUS_FINAL +" OR CBM." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +" = " + I_DocStatus.DOCUMENT_STATUS_DRAFT+" OR CBM." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +" = " + I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED+")"+
                            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            //update opie-eyek 20140422
                            " AND ( CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE]+
                            " = " + PstMaterial.MAT_TYPE_REGULAR+
                            " OR CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE]+
                            " = " + PstMaterial.MAT_TYPE_COMPOSITE+" )"+
                         
                            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;

                sWhereClause = sWhereClause + " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                        " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";

                sWhereClause = sWhereClause + " AND CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] +
                        " != 1";

                 if (sWhereClause != null && sWhereClause.length() > 0) {
                    sql = sql + " WHERE " + sWhereClause;
                }
                
                //order by cash_cashier
                sql = sql + " ORDER BY CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID];
                        /*"SELECT COUNT(" + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] + ") FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM " +
                         //" INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD " +
                         //" ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                         //" = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                         " = " +oidLocation+
                         " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                         "= " +I_DocStatus.DOCUMENT_STATUS_FINAL;*/


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
                //postingProgress.setSumDocSales(rs.getInt(1) + postingProgress.getSumDocSales());
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
            //System.out.println("eRR >>= CountSales : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }
 
 public static int getCountSalesComposit(long oidLocation) {
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT COUNT(DISTINCT CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                         ", CBM." +PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+ ") AS COUNT FROM "
                          + PstBillMain.TBL_CASH_BILL_MAIN + " CBM " +
                         " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD " +
                         " ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                         " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " CSH " +
                         " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
                         " = CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] ;

                String sWhereClause = "";
                 sWhereClause = "CBM." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                            " = " + I_DocStatus.DOCUMENT_STATUS_FINAL +
                            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            " AND CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE]+
                            " = " + PstMaterial.MATERIAL_TYPE_COMPOSITE+
                            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;

                sWhereClause = sWhereClause + " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                        " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")";

                sWhereClause = sWhereClause + " AND CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] +
                        " != 1";

                 if (sWhereClause != null && sWhereClause.length() > 0) {
                    sql = sql + " WHERE " + sWhereClause;
                }
                
                //order by cash_cashier
                sql = sql + " ORDER BY CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID];
                        /*"SELECT COUNT(" + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] + ") FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM " +
                         //" INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " CBD " +
                         //" ON CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                         //" = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                         " = " +oidLocation+
                         " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                         "= " +I_DocStatus.DOCUMENT_STATUS_FINAL;*/


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
                //postingProgress.setSumDocSales(rs.getInt(1) + postingProgress.getSumDocSales());
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
            //System.out.println("eRR >>= CountSales : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }

 /*
 * Get Count Return For Update Stock
 * By Mirahu
 * 20110912
 */
 public static int getCountRet(long oidLocation) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(DISTINCT RETITEM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + ") FROM " + PstMatReturn.TBL_MAT_RETURN+ " RET " +
                        //"SELECT COUNT(" + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] + ") FROM " + PstMatReturn.TBL_MAT_RETURN+ " RET " +
                         " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " RETITEM " +
                         " ON RETITEM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                         " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                         " = " +oidLocation+
                         " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                         "= " +I_DocStatus.DOCUMENT_STATUS_FINAL;


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
                //postingProgress.setSumDocReturn(rs.getInt(1) + postingProgress.getSumDocReturn());
            }

            rs.close();
            return count;
        } catch (Exception e) {
           return 0;
            //System.out.println("eRR >>= CountRet : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }

 /*
 * Get Count Costing For Update Stock
 * By Mirahu
 * 20110912
 */
 public static int getCountCosting(long oidLocation) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(DISTINCT COSTITEM." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] + ") FROM " + PstMatCosting.TBL_COSTING+ " COST " +
                    //"SELECT COUNT(" + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] + ") FROM " + PstMatCosting.TBL_COSTING+ " COST " +
                         " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " COSTITEM " +
                         " ON COSTITEM." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                         " = COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                         " INNER JOIN " + PstTempPostDoc.TBL_TEMP_POST_DOC + " TEMP " +
                         " ON COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                         " = TEMP." + PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID] +
                         " WHERE COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                         " = " +oidLocation+
                         " AND COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                         "= " +I_DocStatus.DOCUMENT_STATUS_FINAL;


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
               count = rs.getInt(1);
                //postingProgress.setSumDocCosting(rs.getInt(1) + postingProgress.getSumDocReturn());
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
            //System.out.println("eRR >>= CountCost : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    /**
     * proses mencari serial number berdasarkan document
     * @param oidDoc
     * @param oidMaterial
     * @param docType
     * @param oidLocation
     * @return
     */
    public static boolean postingSerialCode(long oidDoc, long oidMaterial, int docType, long oidLocation) {
                boolean postingSerialCode=false;
                String where ="";

               
                Vector vectCode = new Vector();
                switch (docType) {
                        case DOC_TYPE_RECEIVE:
                                where = "PRM."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + "=" +oidDoc;
                                vectCode =   PstReceiveStockCode.listReceieStockCodePosting(0, 0, where, "");
                                
                                postingSerialCode =insertSerialCode(vectCode,oidLocation);
                                
                            break;

                        case DOC_TYPE_RETURN:
                            
                                where = "PRM."+PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] + "=" +oidDoc;
                                vectCode =   PstReturnStockCode.listPostingReturnSerialCode(0, 0, where, "");

                                postingSerialCode = deleteSerialCode(vectCode,oidLocation);
                                
                            break;

                        case DOC_TYPE_DISPATCH:
                                where = "PRM."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] + "=" +oidDoc;
                                vectCode =   PstDispatchStockCode.listDispatchStockCodePosting(0, 0, where, "");

                                postingSerialCode = deleteSerialCode(vectCode,oidLocation);
                                
                            break;

                        case DOC_TYPE_COSTING:
                                where = "PRM."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] + "=" +oidDoc;
                                vectCode =   PstCostingStockCode.listPostingCostingSerialCode(0, 0, where, "");

                                postingSerialCode = deleteSerialCode(vectCode,oidLocation);
                            break;

                        case DOC_TYPE_SALE_REGULAR:

                            break;
                            
                        default:
                            break;
                            
                    }

            return  postingSerialCode;
    }

    /**
     * proses input serial number
     * @param vectCode
     * @param oidLocation
     * @return
     */
    public static boolean insertSerialCode(Vector vectCode,long oidLocation) {
        boolean postingSerialCode=false;
        if (vectCode != null && vectCode.size() > 0) {
                    for (int k = 0; k < vectCode.size(); k++) {
                        SourceStockCode sourceStockCode = (SourceStockCode) vectCode.get(k);
                        MaterialStockCode materialStockCode = new MaterialStockCode();
                        try {
                            if (materialStockCode.getOID() == 0) {
                                materialStockCode = new MaterialStockCode();
                                materialStockCode.setLocationId(oidLocation);
                                materialStockCode.setMaterialId(sourceStockCode.getSourceId());
                                materialStockCode.setStockCode(sourceStockCode.getStockCode());
                                materialStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                materialStockCode.setStockValue(sourceStockCode.getStockValue());
                                materialStockCode.setDateStock(new Date());

                                PstMaterialStockCode.insertExc(materialStockCode);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        return  postingSerialCode;
    }

    /**
     * delete serial code berdasarkan asal location pengiriman
     * @param vectCode
     * @param oidLocation
     * @return
     */
    public static boolean deleteSerialCode(Vector vectCode,long oidLocation) {
        boolean postingSerialCode=false;
        if (vectCode != null && vectCode.size() > 0) {
                    for (int k = 0; k < vectCode.size(); k++) {
                        SourceStockCode sourceStockCode = (SourceStockCode) vectCode.get(k);
                        try {
                             PstMaterialStockCode.deleteStockCodeBySerialCode(sourceStockCode.getStockCode(), oidLocation);
                        } catch (Exception e) {
                        }
                    }
                }
        return  postingSerialCode;
    }
}
