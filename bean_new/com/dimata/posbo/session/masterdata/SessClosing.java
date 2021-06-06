package com.dimata.posbo.session.masterdata;

/* java package */

import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReturn;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.util.Formater;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;

import java.util.Vector;
import java.util.Date;
import java.util.Calendar;
import java.sql.ResultSet;

public class SessClosing {




    // Err code
    public static final int CLOSING_OK = 0;
    public static final int ERR_NOT_VALID_DATE = 1;
    public static final int ERR_UNPOSTED_DOC = 2;
    public static final int ERR_CREATE_NEW_PERIOD = 3;
    public static final int ERR_CLOSE_CURRENT_PERIOD = 4;
    public static final int ERR_TRANSFER_INTO_NEW_PERIOD = 5;
    public static String[][] sErrClosingPeriod =
            {
                {"", "Tanggal tutup periode tidak sesuai", "Daftar dokumen yang belum di-posting :",
                 "Gagal membuat periode baru", "Gagal mengubah status periode lama menjadi close",
                 "Gagal men-transfer sisa stok ke periode baru"},
                {"", "Not valid day to close period", "List of un posted document :",
                 "Creating new period fail !!!", "Closing current period fail",
                 "Transfer stock balance into new period fail"}
            };


    private String sClosingMessage = "";

    /** Holds value of property vListUnPostedLGRDoc. */
    private Vector vListUnPostedLGRDoc;

    /** Holds value of property vListUnPostedReturnDoc. */
    private Vector vListUnPostedReturnDoc;

    /** Holds value of property vListUnPostedDFDoc. */
    private Vector vListUnPostedDFDoc;

    /** Holds value of property vListUnPostedSalesDoc. */
    private Vector vListUnPostedSalesDoc;

    /** Holds value of property vListunPostedCostDoc. */
    private Vector vListUnPostedCostDoc;
    private Vector vListUnPostedOpnameDoc;

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

    /** Getter for property vListunPostedCostDoc.
     * @return Value of property vListunPostedCostDoc.
     *
     */
    public Vector getVListUnPostedCostDoc() {
        return this.vListUnPostedCostDoc;
    }

    /** Setter for property vListunPostedCostDoc.
     * @param vListunPostedCostDoc New value of property vListunPostedCostDoc.
     *
     */
    public void setVListUnPostedCostDoc(Vector vListUnPostedCostDoc) {
        this.vListUnPostedCostDoc = vListUnPostedCostDoc;
    }
      /**
   * @return the vListUnPostedOpnameDoc
   */
  public Vector getvListUnPostedOpnameDoc() {
    return vListUnPostedOpnameDoc;
  }

  /**
   * @param vListUnPostedOpnameDoc the vListUnPostedOpnameDoc to set
   */
  public void setvListUnPostedOpnameDoc(Vector vListUnPostedOpnameDoc) {
    this.vListUnPostedOpnameDoc = vListUnPostedOpnameDoc;
  }

    public void setMessage(String sClosingMessage) {
        this.sClosingMessage = sClosingMessage;
    }

    public String getMessage() {
        return sClosingMessage;
    }


    // Proses closing monthly
    /**
     * @param oidPeriode
     * @param language
     * @return
     * @update by Edhy
     */
    public int Process(long oidPeriode, int language) {
        int iResult = CLOSING_OK;
        boolean bProcessOK = false;
        long oidPeriodeNew = 0;

        // --- start get time duration of transaction document that will posting ---
        // get current period "start date" and "end date" combine to "shift"
        Date dStartDatePeriod = null;
        Date dEndDatePeriod = null;
        try {
            Periode objMaterialPeriode = PstPeriode.fetchExc(oidPeriode);
            dStartDatePeriod = objMaterialPeriode.getStartDate();
            dEndDatePeriod = objMaterialPeriode.getEndDate();
        } catch (Exception e) {
            System.out.println("Exc " + new SessClosing().getClass().getName() + ".PostingSaleWithoutItem() - fetch period : " + e.toString());
        }

        int iDayOfShiftInterval = 0;
        String sStartTime = "";
        String sEndTime = "";
        String sOrdShift = PstShift.fieldNames[PstShift.FLD_START_TIME];
        Vector vListShift = PstShift.list(0, 0, "", sOrdShift);
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
        // --- finish get time duration of transaction document that will posting ---



        // Check apakah hari ini adalah valid untuk closing bulanan
        bProcessOK = isValidClosePeriodeTime(new Date(), oidPeriode);

        // Jika hari ini adalah valid utk closing bulanan
        if (bProcessOK) {
            // Check apakah semua dokumen sudah ter-"posted"
            // bProcessOK = CheckAllPosted(oidPeriode);
            Vector vUnPostedLGRDoc = getListUnPostedLGR(dStartDatePeriod, dEndDatePeriod, sStartTime, sEndTime);
            Vector vUnPostedReturnDoc = getListUnPostedReturnDoc(dStartDatePeriod, dEndDatePeriod, sStartTime, sEndTime);
            Vector vUnPostedDFDoc = getListUnPostedDFDoc(dStartDatePeriod, dEndDatePeriod, sStartTime, sEndTime);
            Vector vUnPostedCostDoc = getListUnPostedCostDoc(dStartDatePeriod, dEndDatePeriod, sStartTime, sEndTime);
            Vector vUnPostedSalesDoc = getListUnPostedSalesDoc(dStartDatePeriod, dEndDatePeriod, sStartTime, sEndTime);
            Vector vUnPostedOpnameDoc = getListUnPostedOpnameDoc(dStartDatePeriod, dEndDatePeriod, sStartTime, sEndTime);

            this.vListUnPostedLGRDoc = vUnPostedLGRDoc;
            this.vListUnPostedReturnDoc = vUnPostedReturnDoc;
            this.vListUnPostedDFDoc = vUnPostedDFDoc;
            this.vListUnPostedCostDoc = vUnPostedCostDoc;
            this.vListUnPostedSalesDoc = vUnPostedSalesDoc;
            this.vListUnPostedOpnameDoc = vUnPostedOpnameDoc;

            if ((vUnPostedLGRDoc != null && vUnPostedLGRDoc.size() > 0) ||
                    (vUnPostedReturnDoc != null && vUnPostedReturnDoc.size() > 0) ||
                    (vUnPostedDFDoc != null && vUnPostedDFDoc.size() > 0) ||
                    (vUnPostedCostDoc != null && vUnPostedCostDoc.size() > 0) ||
                    (vUnPostedSalesDoc != null && vUnPostedSalesDoc.size() > 0)
            ) {
                bProcessOK = false;
            }

            // Jika semua dokumen sudah ter-"posted"
            if (bProcessOK) {
                // Generate periode baru
                oidPeriodeNew = CreateNewPeriode(oidPeriode, language);
                bProcessOK = oidPeriodeNew != 0 ? true : false;

                // Jika periode baru berhasil dibuat
                if (bProcessOK) {
                    // Set status current periode menjadi close
                    bProcessOK = CloseCurrentPeriode(oidPeriode);

                    // Jika proses "set status old period menjadi close" berhasil
                    if (bProcessOK) {
                        // Transfer data ke new periode
                        bProcessOK = TransferStock(oidPeriode, oidPeriodeNew);

                        // Jika proses "Transfer data ke new periode" gagal
                        if (!bProcessOK) {
                            iResult = ERR_TRANSFER_INTO_NEW_PERIOD;
                            sClosingMessage = sErrClosingPeriod[language][ERR_TRANSFER_INTO_NEW_PERIOD];
                        }

                    }

                    // Jika proses "set status old period menjadi close" gagal
                    else {
                        iResult = ERR_CLOSE_CURRENT_PERIOD;
                        sClosingMessage = sErrClosingPeriod[language][ERR_CLOSE_CURRENT_PERIOD];
                    }
                }


                // Jika periode baru gagal dibuat
                else {
                    iResult = ERR_CREATE_NEW_PERIOD;
                    sClosingMessage = sErrClosingPeriod[language][ERR_CREATE_NEW_PERIOD];
                }
            }


            // Jika ada dokumen yang belum ter-"posted"
            else {
                iResult = ERR_UNPOSTED_DOC;
                sClosingMessage = sErrClosingPeriod[language][ERR_UNPOSTED_DOC];
            }
        }


        // Jika hari ini adalah tidak valid utk closing bulanan
        else {
            iResult = ERR_NOT_VALID_DATE;
            sClosingMessage = sErrClosingPeriod[language][ERR_NOT_VALID_DATE];
        }

        return iResult;
    }


    /**
     * This method used to get un posted LGR document created before or on specified <CODE>dEndDatePeriod</CODE>
     * @param <CODE>dEndDatePeriod</CODE>maximum boundary of LGR's created date that will search by
     * @return <CODE>Vector</CODE> of un posted "MatReceive Object"
     * algoritm :
     *  - check if <CODE>dEndDatePeriod</CODE> is null
     *  - if not null, list MatReceive obj with 'un posted' status that created before or on <CODE>dEndDatePeriod</CODE>
     *  - return result
     * @created by Edhy
     */
    private static Vector getListUnPostedLGR(Date dStartDatePeriod, Date dEndDatePeriod, String sStartTime, String sEndTime) {
        Vector vResult = new Vector(1, 1);

        if (dStartDatePeriod != null && dEndDatePeriod != null) {
            String sWhereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] +
                    " <> " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                    " AND " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] +
                    " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                    " AND \"" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd") + " " + sEndTime + "\"";
            String sOrderBy = PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] +
                    ", " + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE_CNT];
            vResult = PstMatReceive.list(0, 0, sWhereClause, sOrderBy);
        }

        return vResult;
    }


    /**
     * This method used to get un posted Return document created before or on specified <CODE>dEndDatePeriod</CODE>
     * @param <CODE>dEndDatePeriod</CODE>maximum boundary of Return Doc's created date that will search by
     * @return <CODE>Vector</CODE> of un posted "MatReturn Object"
     * algoritm :
     *  - check if <CODE>dEndDatePeriod</CODE> is null
     *  - if not null, list MatReturn obj with 'un posted' status that created before or on <CODE>dEndDatePeriod</CODE>
     *  - return result
     * @created by Edhy
     */
    private static Vector getListUnPostedReturnDoc(Date dStartDatePeriod, Date dEndDatePeriod, String sStartTime, String sEndTime) {
        Vector vResult = new Vector(1, 1);

        if (dEndDatePeriod != null) {
            String sWhereClause = PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] +
                    " <> " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                    " AND " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE] +
                    " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                    " AND \"" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd") + " " + sEndTime + "\"";
            String sOrderBy = PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE] +
                    ", " + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE_CNT];
            vResult = PstMatReturn.list(0, 0, sWhereClause, sOrderBy);
        }

        return vResult;
    }


    /**
     * This method used to get un posted Dispatch document created before or on specified <CODE>dEndDatePeriod</CODE>
     * @param <CODE>dEndDatePeriod</CODE>maximum boundary of Dispatch Doc's created date that will search by
     * @return <CODE>Vector</CODE> of un posted "MatDispatch Object"
     * algoritm :
     *  - check if <CODE>dEndDatePeriod</CODE> is null
     *  - if not null, list MatDispatch obj with 'un posted' status that created before or on <CODE>dEndDatePeriod</CODE>
     *  - return result
     * @created by Edhy
     */
    private static Vector getListUnPostedDFDoc(Date dStartDatePeriod, Date dEndDatePeriod, String sStartTime, String sEndTime) {
        Vector vResult = new Vector(1, 1);

        if (dEndDatePeriod != null) {
            String sWhereClause = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] +
                    " <> " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                    " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] +
                    " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                    " AND \"" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd") + " " + sEndTime + "\"";
            String sOrderBy = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] +
                    ", " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE_COUNTER];
            vResult = PstMatDispatch.list(0, 0, sWhereClause, sOrderBy);
        }

        return vResult;
    }


    /**
     * This method used to get un posted Costing document created before or on specified <CODE>dEndDatePeriod</CODE>
     * @param <CODE>dEndDatePeriod</CODE>maximum boundary of Costing Doc's created date that will search by
     * @return <CODE>Vector</CODE> of un posted "MatCosting Object"
     * algoritm :
     *  - check if <CODE>dEndDatePeriod</CODE> is null
     *  - if not null, list MatCosting obj with 'un posted' status that created before or on <CODE>dEndDatePeriod</CODE>
     *  - return result
     * @created by Edhy
     */
    private static Vector getListUnPostedCostDoc(Date dStartDatePeriod, Date dEndDatePeriod, String sStartTime, String sEndTime) {
        Vector vResult = new Vector(1, 1);

        if (dEndDatePeriod != null) {
            String sWhereClause = PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                    " <> " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                    " AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                    " AND \"" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd") + " " + sEndTime + "\"";
            String sOrderBy = PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] +
                    ", " + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE_COUNTER];
            vResult = PstMatCosting.list(0, 0, sWhereClause, sOrderBy);
        }

        return vResult;
    }


    /**
     * This method used to get un posted Dispatch document created before or on specified <CODE>dEndDatePeriod</CODE>
     * @param <CODE>dEndDatePeriod</CODE>maximum boundary of Dispatch Doc's created date that will search by
     * @return <CODE>Vector</CODE> of un posted "MatDispatch Object"
     * algoritm :
     *  - check if <CODE>dEndDatePeriod</CODE> is null
     *  - if not null, list MatDispatch obj with 'un posted' status that created before or on <CODE>dEndDatePeriod</CODE>
     *  - return result
     * @created by Edhy
     */
    private static Vector getListUnPostedSalesDoc(Date dStartDatePeriod, Date dEndDatePeriod, String sStartTime, String sEndTime) {
        Vector vResult = new Vector(1, 1);

        if (dEndDatePeriod != null) {
            String sWhereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] +
                    " <> " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] +
                    " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                    " AND \"" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd") + " " + sEndTime + "\"";
            String sOrderBy = PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +
                    ", " + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_COUNTER];
            vResult = PstBillMain.list(0, 0, sWhereClause, sOrderBy);
        }

        return vResult;
    }

    private static Vector getListUnPostedOpnameDoc(Date dStartDatePeriod, Date dEndDatePeriod, String sStartTime, String sEndTime) {
        Vector vResult = new Vector(1, 1);

        if (dEndDatePeriod != null) {
            String sWhereClause = PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] +
                    " <> " + I_DocStatus.DOCUMENT_STATUS_POSTED +
                    " AND " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                    " BETWEEN \"" + Formater.formatDate(dStartDatePeriod, "yyyy-MM-dd") + " " + sStartTime + "\"" +
                    " AND \"" + Formater.formatDate(dEndDatePeriod, "yyyy-MM-dd") + " " + sEndTime + "\"";
            String sOrderBy = PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER] +
                    ", " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CODE_COUNTER];
            vResult = PstMatStockOpname.list(0, 0, sWhereClause, sOrderBy);
        }

        return vResult;
    }

    /**
     * Create new material periode
     * @param oidPeriode
     * @param language
     * @return
     * @created by Edhy
     */
    private static long CreateNewPeriode(long oidPeriode, int language) {
        long hasil = 0;

        String monthText[][] =
                {
                    {"Januari", "Pebruari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "Nopember", "Desember"},
                    {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"}
                };

        try {
            // Fetch previous period
            Periode mp = PstPeriode.fetchExc(oidPeriode);
            Date startDate = mp.getEndDate();
            int bulan = startDate.getMonth();
            int tahun = startDate.getYear() + 1900;

            String newPeriode = "";
            if (bulan < 11) {
                bulan += 1;
            } else {
                bulan = 0;
                tahun += 1;
            }
            newPeriode = monthText[language][bulan] + " " + tahun;

            // untuk start date periode
            Date asu = new Date(tahun - 1900, bulan, 1);

            // untuk end date periode
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.setTime(asu);
            Date currDate = newCalendar.getTime();
            Date asuakhir = new Date(currDate.getYear(), currDate.getMonth(), newCalendar.getActualMaximum(newCalendar.DAY_OF_MONTH));

            // untuk last entry periode
            Date lastEntry = new Date(currDate.getYear(), currDate.getMonth() + 1, 15);

            Periode matPer = new Periode();
            matPer.setPeriodeName(newPeriode);
            matPer.setStartDate(asu);
            matPer.setEndDate(asuakhir);
            matPer.setLastEntry(lastEntry);
            matPer.setPeriodeType(0);
            matPer.setStatus(PstPeriode.FLD_STATUS_RUNNING);
            hasil = PstPeriode.insertExc(matPer);
        } catch (Exception exc) {
            System.out.println("Create new periode : " + exc);
        }
        return hasil;
    }


    // Set status current periode menjadi close
    /**
     * @param oidPeriode
     * @return
     * @update by Edhy
     */
    private static boolean CloseCurrentPeriode(long oidPeriode) {
        boolean hasil = false;
        try {
            // Fetch entity to update
            Periode mp = PstPeriode.fetchExc(oidPeriode);
            mp.setStatus(PstPeriode.FLD_STATUS_CLOSED);
            long oidMatPer = PstPeriode.updateExc(mp);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Close current periode : " + exc);
        }
        return hasil;
    }


    //Transfer tabel material stock, set opening_qty = qty, qty lainnya nol
    /**
     * @param oidPeriode
     * @param oidPeriodeNew
     * @return
     * @update by Edhy
     */
    private static boolean TransferStock(long oidPeriode, long oidPeriodeNew) {
        boolean hasil = true;
        DBResultSet dbrs = null;
        try {
            int jumlah = 0;

            // Process Transfer Stock
            String sql = "SELECT * " +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK +
                    " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + oidPeriode;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                hasil = false;

                //Get record as needed
                MaterialStock materialStock = new MaterialStock();
                materialStock.setPeriodeId(oidPeriodeNew);
                materialStock.setMaterialUnitId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]));
                materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
                materialStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                materialStock.setQtyMin(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN]));
                materialStock.setQtyMax(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX]));
                materialStock.setQtyIn(0);
                materialStock.setQtyOut(0);
                materialStock.setOpeningQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                materialStock.setClosingQty(0);

                //insert modified field into new records
                long oidBaru = PstMaterialStock.insertExc(materialStock);
                if (oidBaru != 0) {
                    hasil = true;
                } else {
                    hasil = false;
                }

                if (hasil == false) break;
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("Transfer Stock Err : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }


    /**
     * this method used to check if closebook time is valid or not
     * @param toDay
     * @param periodId
     * @return
     * @created by Edhy
     */
    public static boolean isValidClosePeriodeTime(Date toDay, long periodId) {
        Vector tempPeriod = PstPeriode.list(0, 0, " PERIODE_ID = " + periodId, "");
        Periode per = new Periode();
        if (tempPeriod != null && tempPeriod.size() > 0) {
            per = (Periode) tempPeriod.get(0);
        }

        toDay = new Date(toDay.getYear(), toDay.getMonth(), toDay.getDate());

        // close period is valid if TO DAY is same or after END DATE of current/running period
        if (toDay.before(per.getEndDate())) {
            return false;
        } else {
            return true;
        }
    }
}
