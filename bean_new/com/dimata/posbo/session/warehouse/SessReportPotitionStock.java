/*
 * SessReportPotitionStock.java
 *
 * Created on February 18, 2008, 10:36 AM
 */
package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.hanoman.entity.masterdata.MasterType;
import com.dimata.hanoman.entity.masterdata.PstMasterType;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMatVendorPrice;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialMappingType;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.masterdata.PstMerk;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstSubCategory;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.SubCategory;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.search.SrcMaterial;
import com.dimata.posbo.entity.search.SrcReportPotitionStock;
import com.dimata.posbo.entity.search.SrcSaleReport;
import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.posbo.entity.warehouse.PstMatDispatchItem;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatReturn;
import com.dimata.posbo.entity.warehouse.PstMatReturnItem;
import com.dimata.posbo.entity.warehouse.PstMatStockOpname;
import com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem;
import com.dimata.posbo.entity.warehouse.PstProduction;
import com.dimata.posbo.entity.warehouse.PstProductionCost;
import com.dimata.posbo.entity.warehouse.PstProductionGroup;
import com.dimata.posbo.entity.warehouse.PstProductionProduct;
import com.dimata.posbo.entity.warehouse.PstStockCardReport;
import com.dimata.posbo.form.search.FrmSrcReportPotitionStock;
import com.dimata.posbo.session.masterdata.SessMaterial;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

import javax.servlet.jsp.JspWriter;
import com.dimata.qdep.form.FRMHandler;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author gwawan
 */
public class SessReportPotitionStock {

  // for report posisi stock
  public static final String TBL_MATERIAL_STOCK_REPORT = "pos_material_stock_report";
  public static final String TBL_MATERIAL_STOCK_REPORT_HIS = "pos_material_stock_report_temp";
  public static final int TYPE_REPORT_POSISI_ALL = 0;
  public static final int TYPE_REPORT_POSISI_CATEGORY = 1;
  public static final int TYPE_REPORT_POSISI_SUPPLIER = 2;

  // constant for session stock potition report
  public static final String SESS_SRC_STOCK_POTITION_REPORT = "SESS_SRC_STOCK_POTITION_REPORT";

  public static final int STOCK_VALUE_BEGIN = 0;
  public static final int STOCK_VALUE_OPNAME = 1;
  public static final int STOCK_VALUE_RECEIVE = 2;
  public static final int STOCK_VALUE_DISPATCH = 3;
  public static final int STOCK_VALUE_RETURN = 4;
  public static final int STOCK_VALUE_SALE = 5;
  public static final int STOCK_VALUE_RETURN_CUST = 6;
  public static final int STOCK_VALUE_CLOSING = 7;
  public static final int STOCK_VALUE_COST = 8;
  public static final int STOCK_VALUE_PRODUCTION_COST = 9;
  public static final int STOCK_VALUE_PRODUCTION_PRODUCT = 10;

  /**
   * Creates a new instance of SessReportPotitionStock
   */
  public SessReportPotitionStock() {
  }

  /**
   * Fungsi ini digunakan untuk melakukan rekap data transaksi, untuk
   * selanjutnya disimpan sementara dalam tabel
   *
   * @param boolean Jenis laporan yang akan menggunakan hasil data rekapan
   * (Weekly/Potition)
   * @param SrcReportPotitionStock Instance pencarian
   * @boolean Parameter untuk menentukkan apakah proses melibatkan stok bernilai
   * nol
   * @created gwawan@dimata 2008-01-21 update opie-eyek 20130713
   */
  synchronized public static boolean summarryTransactionData(boolean weekReport, SrcReportPotitionStock srcReportPotitionStock, boolean isZero) {
    /**
     * hapus isi tabel temporary dalam keadaan kosong
     */
    deleteHistory(TBL_MATERIAL_STOCK_REPORT, srcReportPotitionStock);
    deleteHistory(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportPotitionStock);

    Vector list = new Vector(1, 1);
    try {
      System.out.println("masukkan data transaksi ke tabel " + TBL_MATERIAL_STOCK_REPORT);
      // mengambil data trnasaksi sesuai dengan periode yang di select
      System.out.println(" proses transaksi receive....");
      insertSelectReceive(TBL_MATERIAL_STOCK_REPORT, srcReportPotitionStock);
      System.out.println(" proses transaksi return supplier....");
      insertSelectReturn(TBL_MATERIAL_STOCK_REPORT, srcReportPotitionStock);
      System.out.println(" proses transaksi dispatch....");
      insertSelectDispatch(TBL_MATERIAL_STOCK_REPORT, srcReportPotitionStock);
      System.out.println(" proses transaksi costing....");
      insertSelectCosting(TBL_MATERIAL_STOCK_REPORT, srcReportPotitionStock);
      System.out.println(" proses transaksi sale + return sale....");
      insertSelectSale(TBL_MATERIAL_STOCK_REPORT, srcReportPotitionStock);
      System.out.println(" proses transaksi opname....");
      insertSelectOpname(TBL_MATERIAL_STOCK_REPORT, srcReportPotitionStock);
      //ADDED BY DEWOK 20190925 FOR PRODUCTION
      System.out.println(" proses produksi....");
      insertSelectProductionCost(TBL_MATERIAL_STOCK_REPORT, srcReportPotitionStock);
      insertSelectProductionProduct(TBL_MATERIAL_STOCK_REPORT, srcReportPotitionStock);

      // set date last find data
      Date dateTo = srcReportPotitionStock.getDateFrom();
      dateTo.setDate(dateTo.getDate() - 1);
      srcReportPotitionStock.setDateTo(dateTo);

      Vector vect = PstPeriode.list(0, 0, "", PstPeriode.fieldNames[PstPeriode.FLD_START_DATE]);
      if (vect != null && vect.size() > 0) {
        Periode periode = (Periode) vect.get(0);
        srcReportPotitionStock.setDateFrom(periode.getStartDate());
        srcReportPotitionStock.setPeriodeId(periode.getOID());
      }

      System.out.println("masukkan data transaksi ke tabel " + TBL_MATERIAL_STOCK_REPORT_HIS);
      // mengambil data sebelum periode yang diselect
      System.out.println(" proses transaksi receive....");
      insertSelectReceive(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportPotitionStock);
      System.out.println(" proses transaksi return supplier....");
      insertSelectReturn(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportPotitionStock);
      System.out.println(" proses transaksi dispatch....");
      insertSelectDispatch(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportPotitionStock);
      System.out.println(" proses transaksi costing....");
      insertSelectCosting(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportPotitionStock);
      System.out.println(" proses transaksi sale + return sale....");
      insertSelectSale(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportPotitionStock);
      System.out.println(" proses transaksi opname....");
      insertSelectOpname(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportPotitionStock);
      //ADDED BY DEWOK 20190925 FOR PRODUCTION
      System.out.println(" proses produksi....");
      insertSelectProductionCost(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportPotitionStock);
      insertSelectProductionProduct(TBL_MATERIAL_STOCK_REPORT_HIS, srcReportPotitionStock);

    } catch (Exception e) {
      System.out.println("Exc. summarryTransactionData(#,#,#,#) >> " + e.toString());
      return false;
    }

    return true;
  }

  /*
     * Fungsi ini digunakan untuk menghapus isi tabel temporary pada saat proses genarate laporan posisi stok
     * @param String Nama tabel yang isinya akan dihapus
     * @param SrcReportPotitionStock Search key
     * @create gwawan@dimata 2008-02-17
   */
  public static void deleteHistory(String TBL_NAME, SrcReportPotitionStock objSrcReportPotitionStock) {
    try {
      System.out.println("== >> DELETE HISTORY in TABLE " + TBL_NAME + "; USER ID: " + objSrcReportPotitionStock.getUserId());
      String sql = "DELETE FROM " + TBL_NAME;
      sql += " WHERE USER_ID = " + objSrcReportPotitionStock.getUserId();
      DBHandler.execUpdate(sql);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  /**
   * Fungsi ini digunakan untuk mendapatkan nilai stok dari laporan posisi stok
   *
   * @param SrcReportPotitionStock instance dari SrcReportPotitionStock
   * @return Vector Merupakan objek yang menampung jumlah dari list stok dan
   * nilai stok
   * @created by gwawan@20080121
   */
  public static Vector getGrandTotalReportPosisiStockAll(SrcReportPotitionStock srcReportPotitionStock, boolean isCalculateZeroQty) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    String sql = "";
    Vector stockValue = new Vector(1, 1);

    double stockValueBegin = 0;
    double stockValueOpname = 0;
    double stockValueReceive = 0;
    double stockValueDispatch = 0;
    double stockValueReturn = 0;
    double stockValueSale = 0;
    double stockValueReturnCust = 0;
    double stockValueClosing = 0;

    double grandTotalStockValueBegin = 0;
    double grandTotalStockValueOpname = 0;
    double grandTotalStockValueReceive = 0;
    double grandTotalStockValueDispatch = 0;
    double grandTotalStockValueReturn = 0;
    double grandTotalStockValueSale = 0;
    double grandTotalStockValueReturnCust = 0;
    double grandTotalStockValueClosing = 0;

    try {
      /**
       * mencari daftar semua stok
       */
      Vector vect = new Vector(1, 1);
      vect = getReportStockAll(srcReportPotitionStock, isCalculateZeroQty, 0, 0);

      /**
       * untuk mencari posisi stock
       */
      if (vect != null && vect.size() > 0) {
        for (int k = 0; k < vect.size(); k++) {
          Vector vt = (Vector) vect.get(k);
          Material material = (Material) vt.get(0);
          MaterialStock materialStock = (MaterialStock) vt.get(1);
          Unit unit = (Unit) vt.get(2);

          MaterialStock matStock = new MaterialStock();
          Vector vctTemp = new Vector(1, 1);

          stockValueBegin = 0;
          stockValueOpname = 0;
          stockValueReceive = 0;
          stockValueDispatch = 0;
          stockValueReturn = 0;
          stockValueSale = 0;
          stockValueReturnCust = 0;
          stockValueClosing = 0;

          /**
           * get qty opname
           */
          sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE, (QTY_OPNAME * COGS_PRICE) AS STOCK_VALUE_OPNAME FROM " + TBL_MATERIAL_STOCK_REPORT;
          sql += " WHERE MATERIAL_ID = " + material.getOID();
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
          sql += " AND OPNAME_ITEM_ID != 0 ORDER BY TRS_DATE DESC";

          dbrs = DBHandler.execQueryResult(sql);
          ResultSet rs = dbrs.getResultSet();

          Date date = new Date();
          boolean withOpBool = false;
          while (rs.next()) {
            withOpBool = true;
            matStock.setOpnameQty(rs.getDouble("QTY_OPNAME"));
            date = DBHandler.convertDate(rs.getDate("TRS_DATE"), rs.getTime("TRS_DATE"));
            stockValueOpname = rs.getDouble("STOCK_VALUE_OPNAME");
            break;
          }

          /**
           * get qty all
           */
          sql = "SELECT SUM(QTY_RECEIVE) AS RECEIVE, SUM(QTY_RECEIVE * COGS_PRICE) AS STOCK_VALUE_RECEIVE";
          sql += ", SUM(QTY_DISPATCH) AS DISPATCH, SUM(QTY_DISPATCH * COGS_PRICE) AS STOCK_VALUE_DISPATCH";
          sql += ", SUM(QTY_RETURN) AS RETUR, SUM(QTY_RETURN * COGS_PRICE) AS STOCK_VALUE_RETURN";
          sql += ", SUM(QTY_SALE) AS SALE, SUM(QTY_SALE * COGS_PRICE) AS STOCK_VALUE_SALE";
          sql += ", SUM(QTY_RETURN_CUST) AS RETURN_CUST, SUM(QTY_RETURN_CUST * COGS_PRICE) AS STOCK_VALUE_RETURN_CUST";
          sql += " FROM " + TBL_MATERIAL_STOCK_REPORT;
          sql += " WHERE MATERIAL_ID=" + material.getOID();
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
          if (withOpBool) {
            sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date, "yyyy-MM-dd") + " " + Formater.formatTimeLocale(date, "kk:mm:ss") + "'";
          }
          sql = sql + " GROUP BY MATERIAL_ID";

          dbrs = DBHandler.execQueryResult(sql);
          rs = dbrs.getResultSet();
          while (rs.next()) {
            matStock.setQtyIn(rs.getDouble("RECEIVE"));
            matStock.setQtyOut(rs.getDouble("DISPATCH"));
            matStock.setQtyMin(rs.getDouble("RETUR"));
            matStock.setSaleQty(rs.getDouble("SALE"));
            matStock.setQtyMax(rs.getDouble("RETURN_CUST"));
            stockValueReceive = rs.getDouble("STOCK_VALUE_RECEIVE");
            stockValueDispatch = rs.getDouble("STOCK_VALUE_DISPATCH");
            stockValueReturn = rs.getDouble("STOCK_VALUE_RETURN");
            stockValueSale = rs.getDouble("STOCK_VALUE_SALE");
            stockValueReturnCust = rs.getDouble("STOCK_VALUE_RETURN_CUST");
          }

          /**
           * get begin stock
           */
          //matStock.setQty(reportQtyAwalPosisiStock(material.getOID(), materialStock.getLocationId()));
          Vector vctBeginQty = getBeginQty(material.getOID(), materialStock.getLocationId(), srcReportPotitionStock.getUserId());
          matStock.setQty(Double.parseDouble((String) vctBeginQty.get(0)));
          stockValueBegin = Double.parseDouble((String) vctBeginQty.get(1));
          /**
           * end get begin stock
           */

          /**
           * get end stock
           */
          /*if (withOpBool) {
                        matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn()+ matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin())-matStock.getSaleQty());
                        stockValueClosing = (stockValueOpname+stockValueReceive+stockValueReturnCust) - (stockValueDispatch+stockValueReturn) - stockValueSale;
                    } else {
                        matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin())-matStock.getSaleQty());
                        stockValueClosing = (stockValueBegin+stockValueReceive+stockValueReturnCust) - (stockValueDispatch+stockValueReturn) - stockValueSale;
                    }*/
          /**
           * get end stock
           */
          if (withOpBool) {
            matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
            stockValueClosing = (stockValueOpname + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale;
          } else {
            matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
            stockValueClosing = (stockValueBegin + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale;
          }

          grandTotalStockValueBegin += stockValueBegin;
          grandTotalStockValueOpname += stockValueOpname;
          grandTotalStockValueReceive += stockValueReceive;
          grandTotalStockValueDispatch += stockValueDispatch;
          grandTotalStockValueReturn += stockValueReturn;
          grandTotalStockValueSale += stockValueSale;
          grandTotalStockValueReturnCust += stockValueReturnCust;
          grandTotalStockValueClosing += stockValueClosing;
        }
      }

      stockValue.add(STOCK_VALUE_BEGIN, String.valueOf(grandTotalStockValueBegin));
      stockValue.add(STOCK_VALUE_OPNAME, String.valueOf(grandTotalStockValueOpname));
      stockValue.add(STOCK_VALUE_RECEIVE, String.valueOf(grandTotalStockValueReceive));
      stockValue.add(STOCK_VALUE_DISPATCH, String.valueOf(grandTotalStockValueDispatch));
      stockValue.add(STOCK_VALUE_RETURN, String.valueOf(grandTotalStockValueReturn));
      stockValue.add(STOCK_VALUE_SALE, String.valueOf(grandTotalStockValueSale));
      stockValue.add(STOCK_VALUE_RETURN_CUST, String.valueOf(grandTotalStockValueReturnCust));
      stockValue.add(STOCK_VALUE_CLOSING, String.valueOf(grandTotalStockValueClosing));

      list.add(String.valueOf(vect.size()));
      list.add(stockValue);

    } catch (Exception e) {
      System.out.println("Exc. in getGrandTotalReportPosisiStockAll(#,#) : " + e.toString());
    }
    return list;
  }

  /**
   * Fungsi ini digunakan untuk mendapatkan list stok
   *
   * @param SrcReportPotitionStock Objek untuk melakukan pencarian
   * @param boolean Kondisi untuk menentukkan apakah proses kalkulasi melibatkan
   * stok bernilai nol
   * @param int Start list
   * @param int Banyaknya list yang harus ditampilkan
   * @return Vector yang menampung instance dari class Material, MaterialStock
   * dan Unit
   * @create by gwawan@dimata 3 Jan 2008
   * @updated by gwawan@dimata 17 Jan 2008
   */
  public static Vector getReportStockAll(SrcReportPotitionStock srcReportPotitionStock, boolean isZero, int limitStart, int recordToGet) {
    DBResultSet dbrs = null;
    Vector result = new Vector(1, 1);
    try {
      String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + // SKU
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + // NAME
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]
              + // QTY
              ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME]
              + //UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]
              + // DEFAULT_COST
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]
              + // DEFAULT_PRICE
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
              + // BUYING_UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + // STOCK_UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + // SELLING_PRICE
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + // QTY
              ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + //UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]
              + // DEFAULT_COST_CURRENCY_ID
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]
              + // LOCATION
              " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS "
              + //  MATERIAL_STOCK
              " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
              + // MATERIAL
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + // MATERIAL_STOCK_ID
              " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U "
              + //UNIT
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]
              + " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]
              + " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportPotitionStock.getPeriodeId()
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getMerkId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportPotitionStock.getMerkId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportPotitionStock.getCategoryId();
      }

      if (!isZero) {
        sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
      }

      sql = sql + " ORDER BY "
              + " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

      switch (DBHandler.DBSVR_TYPE) {
        case DBHandler.DBSVR_MYSQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + limitStart + "," + recordToGet;
          }

          break;

        case DBHandler.DBSVR_POSTGRESQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
          }

          break;

        case DBHandler.DBSVR_SYBASE:
          break;

        case DBHandler.DBSVR_ORACLE:
          break;

        case DBHandler.DBSVR_MSSQL:
          break;

        default:
                    ;
      }

      System.out.println("SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + sql);
      System.out.print("proses generate report....  ");
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      PstUnit.loadQtyPerBaseUnitByHash();
      while (rs.next()) {
        Vector vectTemp = new Vector(1, 1);

        // calculate cost per stock unit id
        double costPerBuyUnit = rs.getDouble(5);
        long buyUnitId = rs.getLong(7);
        long stockUnitId = rs.getLong(8);
        //double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
        double qtyPerStockUnit = PstUnit.getQtyPerBaseUnitByHash(buyUnitId, stockUnitId);
        double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;

        Material material = new Material();
        material.setSku(rs.getString(1));
        material.setName(rs.getString(2));
        material.setDefaultCost(costPerStockUnit);
        material.setDefaultPrice(rs.getDouble(6));
        material.setAveragePrice(rs.getDouble(9));// * standarRate);
        material.setOID(rs.getLong(10));
        vectTemp.add(material);

        MaterialStock materialStock = new MaterialStock();
        materialStock.setQty(rs.getDouble(3));
        materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
        vectTemp.add(materialStock);

        Unit unit = new Unit();
        unit.setName(rs.getString(4));
        unit.setCode(rs.getString(11));
        vectTemp.add(unit);

        result.add(vectTemp);
      }

      rs.close();
      System.out.println("OK!");
    } catch (Exception e) {
      System.out.println("Exc on SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
      return result;
    }
  }

  public static void getReportStockAllDutyFree(JspWriter out, int infoShowed, int stockValueBy, int language, int start, SrcReportPotitionStock srcReportPotitionStock, boolean isZero, int limitStart, int recordToGet, String cellStyle) {
    //public static void getReportStockAll(JspWriter out, int infoShowed, int stockValueBy, int language,int start, SrcReportPotitionStock srcReportPotitionStock, int limitStart, int recordToGet, String cellStyle) {
    DBResultSet dbrs = null;
    Vector result = new Vector(1, 1);
    Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);

    try {
      String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + // SKU
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + // NAME
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]
              + // QTY
              ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME]
              + //UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]
              + // DEFAULT_COST
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]
              + // DEFAULT_PRICE
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
              + // BUYING_UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + // STOCK_UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + // SELLING_PRICE
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + // QTY
              ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + //UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]
              + // DEFAULT_COST_CURRENCY_ID
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]
              // LOCATION
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE];

      if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
        sql = sql + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
        sql = sql + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
      }

      //if(srcReportPotitionStock.getGroupBy()==SrcSaleReport.GROUP_BY_CATEGORY){
      sql = sql + ", C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
      sql = sql + ", C." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CAT_NAME ";
      sql = sql + ", C." + PstCategory.fieldNames[PstCategory.FLD_CODE] + " AS CAT_CODE ";
      //}

      sql = sql + " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS "
              + //  MATERIAL_STOCK
              " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
              + // MATERIAL
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + // MATERIAL_STOCK_ID
              " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U "
              + //UNIT
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]
              + " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];

      //if(srcReportPotitionStock.getGroupBy()==SrcSaleReport.GROUP_BY_SUPPLIER){
//                        sql = sql + " LEFT JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " AS MV "
//                                  + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
//                                  + " = MV." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
      sql = sql + " LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " = M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID];
      //}

      sql = sql + " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " !=" + PstMaterial.EDIT_NON_AKTIVE;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql += " AND CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = " + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getMerkId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportPotitionStock.getMerkId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportPotitionStock.getCategoryId();
      }

      if (srcReportPotitionStock.getSku().length() > 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " = '" + srcReportPotitionStock.getSku() + "'";
      }

      if (!isZero) {
        sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
      }

      sql = sql + " GROUP BY " + " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

      switch (DBHandler.DBSVR_TYPE) {
        case DBHandler.DBSVR_MYSQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + limitStart + "," + recordToGet;
          }

          break;

        case DBHandler.DBSVR_POSTGRESQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
          }

          break;

        case DBHandler.DBSVR_SYBASE:
          break;

        case DBHandler.DBSVR_ORACLE:
          break;

        case DBHandler.DBSVR_MSSQL:
          break;

        default:
                    ;
      }

      System.out.println("SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + sql);
      System.out.print("proses generate report....  ");
      dbrs = DBHandler.execQueryResult(sql);

      ResultSet rs = dbrs.getResultSet();
      double stockValueBegin = 0;
      double stockValueOpname = 0;
      double stockValueReceive = 0;
      double stockValueDispatch = 0;
      double stockValueReturn = 0;
      double stockValueSale = 0;
      double stockValueReturnCust = 0;
      double stockValueCost = 0;
      double stockValueClosing = 0;
      double stockValueProductionCost = 0;
      double stockValueProductionProduct = 0;

      double subTotalStockValueBegin = 0;
      double subTotalStockValueOpname = 0;
      double subTotalStockValueReceive = 0;
      double subTotalStockValueDispatch = 0;
      double subTotalStockValueReturn = 0;
      double subTotalStockValueSale = 0;
      double subTotalStockValueReturnCust = 0;
      double subTotalStockValueCost = 0;
      double subTotalStockValueClosing = 0;
      double subTotalStockValueProductionCost = 0;
      double subTotalStockValueProductionProduct = 0;
      PstUnit.loadQtyPerBaseUnitByHash();
      long oldSupplier = 0;
      boolean headerSupplier = false;

      double subtotalgroupdaily = 0;
      double subtotalAwalDaily = 0;
      double subtotalOpnameDaily = 0;
      double subtotalTerimaDaily = 0;
      double subtotalTransferDaily = 0;
      double subtotalReturnSuppDaily = 0;
      double subtotalJualDaily = 0;
      double subtotalReturnCustDaily = 0;
      double subtotalCostDaily = 0;
      double subtotalProductionCostDaily = 0;
      double subtotalProductionProductDaily = 0;
      double subtotalSaldoDaily = 0;

      double grandtotalAwalDaily = 0;
      double grandtotalOpnameDaily = 0;
      double grandtotalTerimaDaily = 0;
      double grandtotalTransferDaily = 0;
      double grandtotalReturnSuppDaily = 0;
      double grandtotalJualDaily = 0;
      double grandtotalReturnCustDaily = 0;
      double grandtotalCostingDaily = 0;
      double grandtotalProductionCostDaily = 0;
      double grandtotalProductionProductDaily = 0;
      double grandtotalSaldoDaily = 0;

      double subtotalGroup = 0;
      double subtotalAwal = 0;
      double subtotalOpname = 0;
      double subtotalTerima = 0;
      double subtotalTransfer = 0;
      double subtotalReturnSupp = 0;
      double subtotalJual = 0;
      double subtotalReturnCust = 0;
      double subtotalPembiayaan = 0;
      double subtotalProduksiBiaya = 0;
      double subtotalProduksiHasil = 0;
      double subtotalSaldo = 0;

      int loop = 0;
      while (rs.next()) {
        loop = loop + 1;
        double costPerBuyUnit = rs.getDouble(5);
        long buyUnitId = rs.getLong(7);
        long stockUnitId = rs.getLong(8);
        double qtyPerStockUnit = PstUnit.getQtyPerBaseUnitByHash(buyUnitId, stockUnitId);
        double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;

        Material material = new Material();
        material.setSku(rs.getString(1));
        material.setName(rs.getString(2));
        material.setDefaultCost(costPerStockUnit);
        material.setDefaultPrice(rs.getDouble(6));
        material.setAveragePrice(rs.getDouble(9));// * standarRate);
        material.setOID(rs.getLong(10));
        material.setBarCode(rs.getString("BARCODE"));

        MaterialStock materialStock = new MaterialStock();
        materialStock.setQty(rs.getDouble(3));
        materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));

        Unit unit = new Unit();
        unit.setName(rs.getString(4));
        unit.setCode(rs.getString(11));

        ContactList contact = new ContactList();
        if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
          contact.setOID(rs.getLong("CONTACT_ID"));
          contact.setCompName(rs.getString("COMP_NAME"));
          if (oldSupplier != contact.getOID()) {
            headerSupplier = true;
          } else {
            headerSupplier = false;
            //loop=1;
          }
          oldSupplier = contact.getOID();
        }

        Category category = new Category();
        category.setOID(rs.getLong("CATEGORY_ID"));
        category.setName(rs.getString("CAT_NAME"));
        category.setCode(rs.getString("CAT_CODE"));

        MaterialStock matStock = new MaterialStock();
        Vector vctTemp = new Vector(1, 1);
        Vector stockValue = new Vector(1, 1);

        stockValueBegin = 0;
        stockValueOpname = 0;
        stockValueReceive = 0;
        stockValueDispatch = 0;
        stockValueReturn = 0;
        stockValueSale = 0;
        stockValueReturnCust = 0;
        stockValueCost = 0;
        stockValueClosing = 0;
        stockValueProductionCost = 0;
        stockValueProductionProduct = 0;

        int masterSeason = 0;
        try {
          masterSeason = Integer.valueOf(PstSystemProperty.getValueByName("SEASON_GROUP_TYPE"));
        } catch (Exception exc) {

        }

        /**
         * get qty opname
         */
        //contoh
        //material.setOID(50442851);
        sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE, (QTY_OPNAME * COGS_PRICE) AS STOCK_VALUE_OPNAME FROM " + TBL_MATERIAL_STOCK_REPORT;
        sql += " WHERE MATERIAL_ID = " + material.getOID();
        if (srcReportPotitionStock.getIncWH() == 1) {
          try {
            Location location = PstLocation.fetchExc(materialStock.getLocationId());
            sql += " AND LOCATION_ID IN (" + materialStock.getLocationId() + "," + location.getParentLocationId() + ")";
          } catch (Exception exc) {
            sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          }
        } else {
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
        }
        sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
        sql += " AND OPNAME_ITEM_ID != 0 ORDER BY TRS_DATE DESC";

        dbrs = DBHandler.execQueryResult(sql);
        ResultSet rsx = dbrs.getResultSet();

        Date date = new Date();
        boolean withOpBool = false;
        while (rsx.next()) {
          withOpBool = false; //UPDATE BY DEWOK 2018-11-22, value "withOpBool" dijadikan FALSE
          //karena konsep melibatkan tanggal opname hanya digunakan untuk mencari nilai stok awal
          //tidak bisa dijadikan parameter tambahan untuk mencari data di range tanggal pencarian
          matStock.setOpnameQty(rsx.getDouble("QTY_OPNAME"));
          date = DBHandler.convertDate(rsx.getDate("TRS_DATE"), rsx.getTime("TRS_DATE"));
          stockValueOpname = rsx.getDouble("STOCK_VALUE_OPNAME");
          break;
        }

        /**
         * get qty all (meanggantikan query diatas)
         */
        //buatkan if, jika yang dipakai apakah berdasarkan everage atau penjualan
        String stockValueSelected = "";
        double price = 0.0;
        if (srcReportPotitionStock.getStockValueSale() == 0 || srcReportPotitionStock.getStockValueSale() == 1) {
          stockValueSelected = "COGS_PRICE ";
        } else {
          price = PstPriceTypeMapping.getSellPrice(material.getOID(), srcReportPotitionStock.getStandartId(), srcReportPotitionStock.getStockValueSale());
          stockValueSelected = "" + price;
        }

        sql = "SELECT SUM(QTY_RECEIVE) AS RECEIVE, SUM(QTY_RECEIVE * " + stockValueSelected + ") AS STOCK_VALUE_RECEIVE";
        sql += ", SUM(QTY_DISPATCH) AS DISPATCH, SUM(QTY_DISPATCH * " + stockValueSelected + ") AS STOCK_VALUE_DISPATCH";
        sql += ", SUM(QTY_RETURN) AS RETUR, SUM(QTY_RETURN * " + stockValueSelected + ") AS STOCK_VALUE_RETURN";
        sql += ", SUM(QTY_SALE) AS SALE, SUM(QTY_SALE * " + stockValueSelected + ") AS STOCK_VALUE_SALE";
        sql += ", SUM(QTY_RETURN_CUST) AS RETURN_CUST, SUM(QTY_RETURN_CUST * " + stockValueSelected + ") AS STOCK_VALUE_RETURN_CUST";
        sql += ", SUM(QTY_COST) AS COST, SUM(QTY_COST * " + stockValueSelected + ") AS STOCK_VALUE_COST";
        sql += " FROM " + TBL_MATERIAL_STOCK_REPORT;
        sql += " WHERE MATERIAL_ID=" + material.getOID();
        if (srcReportPotitionStock.getIncWH() == 1) {
          try {
            Location location = PstLocation.fetchExc(materialStock.getLocationId());
            sql += " AND LOCATION_ID IN (" + materialStock.getLocationId() + "," + location.getParentLocationId() + ")";
          } catch (Exception exc) {
            sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          }
        } else {
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
        }
        sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
        /**
         * kondisi ini berfungsi untuk membatasi transaksi (receive, dispatch,
         * return, sale) yang harus diproses, dimana hanya transaksi yang
         * terjadi setelah (tanggal dan waktu) proses opname saja yang perlu di
         * proses
         */
        if (withOpBool) {
          sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date, "yyyy-MM-dd") + " " + Formater.formatTimeLocale(date, "kk:mm:ss") + "'";
        }
        sql = sql + " GROUP BY MATERIAL_ID";

        dbrs = DBHandler.execQueryResult(sql);
        rsx = dbrs.getResultSet();
        while (rsx.next()) {
          matStock.setQtyIn(rsx.getDouble("RECEIVE"));
          matStock.setQtyOut(rsx.getDouble("DISPATCH"));
          matStock.setQtyMin(rsx.getDouble("RETUR"));
          matStock.setSaleQty(rsx.getDouble("SALE"));
          matStock.setQtyMax(rsx.getDouble("RETURN_CUST"));
          matStock.setQtyCost(rsx.getDouble("COST"));
          stockValueReceive = rsx.getDouble("STOCK_VALUE_RECEIVE");
          stockValueDispatch = rsx.getDouble("STOCK_VALUE_DISPATCH");
          stockValueReturn = rsx.getDouble("STOCK_VALUE_RETURN");
          stockValueSale = rsx.getDouble("STOCK_VALUE_SALE");
          stockValueReturnCust = rsx.getDouble("STOCK_VALUE_RETURN_CUST");
          stockValueCost = rsx.getDouble("STOCK_VALUE_COST");
        }

        /**
         * get begin stock
         */
        //matStock.setQty(reportQtyAwalPosisiStock(material.getOID(), materialStock.getLocationId()));
        Vector vctBeginQty = getBeginQty(material.getOID(), materialStock.getLocationId(), srcReportPotitionStock.getUserId(), stockValueSelected);
        matStock.setQty(Double.parseDouble((String) vctBeginQty.get(0)));
        stockValueBegin = Double.parseDouble((String) vctBeginQty.get(1));
        /**
         * end get begin stock
         */

        /**
         * get end stock
         */
        if (withOpBool) {
          matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty() - matStock.getQtyCost());
          stockValueClosing = (stockValueOpname + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale - stockValueCost;
        } else {
          matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty() - matStock.getQtyCost());
          stockValueClosing = (stockValueBegin + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale - stockValueCost;
        }

        stockValue.add(STOCK_VALUE_BEGIN, String.valueOf(stockValueBegin));
        stockValue.add(STOCK_VALUE_OPNAME, String.valueOf(stockValueOpname));
        stockValue.add(STOCK_VALUE_RECEIVE, String.valueOf(stockValueReceive));
        stockValue.add(STOCK_VALUE_DISPATCH, String.valueOf(stockValueDispatch));
        stockValue.add(STOCK_VALUE_RETURN, String.valueOf(stockValueReturn));
        stockValue.add(STOCK_VALUE_SALE, String.valueOf(stockValueSale));
        stockValue.add(STOCK_VALUE_RETURN_CUST, String.valueOf(stockValueReturnCust));
        stockValue.add(STOCK_VALUE_CLOSING, String.valueOf(stockValueClosing));
        stockValue.add(STOCK_VALUE_COST, String.valueOf(stockValueCost));
        stockValue.add(STOCK_VALUE_PRODUCTION_COST, String.valueOf(stockValueProductionCost));
        stockValue.add(STOCK_VALUE_PRODUCTION_PRODUCT, String.valueOf(stockValueProductionProduct));

        subtotalgroupdaily = stockValueClosing;

        subtotalAwalDaily = matStock.getQty();
        subtotalOpnameDaily = matStock.getOpnameQty();
        subtotalTerimaDaily = matStock.getQtyIn();
        subtotalTransferDaily = matStock.getQtyOut();
        subtotalReturnSuppDaily = matStock.getQtyMin();
        subtotalJualDaily = matStock.getSaleQty();
        subtotalReturnCustDaily = matStock.getQtyMax();
        subtotalCostDaily = matStock.getQtyCost();
        subtotalProductionCostDaily = matStock.getQtyProductionCost();
        subtotalProductionProductDaily = matStock.getQtyProductionProduct();
        subtotalSaldoDaily = matStock.getClosingQty();

        //grandTotal
        grandtotalAwalDaily = grandtotalAwalDaily + matStock.getQty();
        grandtotalOpnameDaily = grandtotalOpnameDaily + matStock.getOpnameQty();
        grandtotalTerimaDaily = grandtotalTerimaDaily + matStock.getQtyIn();
        grandtotalTransferDaily = grandtotalTransferDaily + matStock.getQtyOut();
        grandtotalReturnSuppDaily = grandtotalReturnSuppDaily + matStock.getQtyMin();
        grandtotalJualDaily = grandtotalJualDaily + matStock.getSaleQty();
        grandtotalReturnCustDaily = grandtotalReturnCustDaily + matStock.getQtyMax();
        grandtotalCostingDaily = grandtotalCostingDaily + matStock.getQtyCost();
        grandtotalProductionCostDaily = grandtotalProductionCostDaily + matStock.getQtyProductionCost();
        grandtotalProductionProductDaily = grandtotalProductionProductDaily + matStock.getQtyProductionProduct();
        grandtotalSaldoDaily = grandtotalSaldoDaily + matStock.getClosingQty();

        //subTotal
        subTotalStockValueBegin += stockValueBegin;
        subTotalStockValueOpname += stockValueOpname;
        subTotalStockValueReceive += stockValueReceive;
        subTotalStockValueDispatch += stockValueDispatch;
        subTotalStockValueReturn += stockValueReturn;
        subTotalStockValueSale += stockValueSale;
        subTotalStockValueReturnCust += stockValueReturnCust;
        subTotalStockValueCost += stockValueCost;
        subTotalStockValueClosing += stockValueClosing;
        subTotalStockValueProductionCost += stockValueProductionCost;
        subTotalStockValueProductionProduct += stockValueProductionProduct;

        System.out.println("Sub Total :" + subTotalStockValueClosing);

        out.print("<tr valign=\"top\">");
        //added by dewok 2018-05-03 for litama
        int typeOfBusinessDetail = Integer.valueOf(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));
        if (typeOfBusinessDetail == 2) {
          material.setName(SessMaterial.setItemNameForLitama(material.getOID()));
        }

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + String.valueOf(start + 1) + ".</div></td>");
        out.print("<td style='mso-number-format:\"\\@\"' class=\"" + cellStyle + "\" div align=\"left\">" + category.getCode() + "</div></td>");
        out.print("<td style='mso-number-format:\"\\@\"' class=\"" + cellStyle + "\" div align=\"left\">" + material.getName() + "</div></td>");
        out.print("<td style='mso-number-format:\"\\@\"' class=\"" + cellStyle + "\" div align=\"left\">" + unit.getCode() + "</div></td>");

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueBegin) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueReceive) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueDispatch) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueOpname) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueClosing) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(0.0) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueClosing) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">-</div></td>");

        out.print("</tr>");

        //subtotal
        start++;
      }

      rs.close();
      System.out.println("OK!");
    } catch (Exception e) {
      System.out.println("Exc on SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
      return;
    }
  }

  /**
   * Fungsi ini digunakan untuk mendapatkan list stok
   *
   * @param SrcReportPotitionStock Objek untuk melakukan pencarian
   * @param boolean Kondisi untuk menentukkan apakah proses kalkulasi melibatkan
   * stok bernilai nol
   * @param int Start list
   * @param int Banyaknya list yang harus ditampilkan
   * @return Vector yang menampung instance dari class Material, MaterialStock
   * dan Unit
   * @create by gwawan@dimata 3 Jan 2008
   * @updated by gwawan@dimata 17 Jan 2008
   * @modified by mirah@dimata 22 Feb 2011
   */
  public static void getReportStockAll(JspWriter out, int infoShowed, int stockValueBy, int language, int start, SrcReportPotitionStock srcReportPotitionStock, boolean isZero, int limitStart, int recordToGet, String cellStyle) {
    //public static void getReportStockAll(JspWriter out, int infoShowed, int stockValueBy, int language,int start, SrcReportPotitionStock srcReportPotitionStock, int limitStart, int recordToGet, String cellStyle) {
    DBResultSet dbrs = null;
    Vector result = new Vector(1, 1);
    Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);

    try {
      String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + // SKU
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + // NAME
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]
              + // QTY
              ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME]
              + //UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]
              + // DEFAULT_COST
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]
              + // DEFAULT_PRICE
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
              + // BUYING_UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + // STOCK_UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + // SELLING_PRICE
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + // QTY
              ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + //UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]
              + // DEFAULT_COST_CURRENCY_ID
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]
              // LOCATION
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE];

      if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
        sql = sql + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
        sql = sql + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
      }

      //if(srcReportPotitionStock.getGroupBy()==SrcSaleReport.GROUP_BY_CATEGORY){
      sql = sql + ", C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
      sql = sql + ", C." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CAT_NAME ";
      //}

      sql = sql + " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS "
              + //  MATERIAL_STOCK
              " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
              + // MATERIAL
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + // MATERIAL_STOCK_ID
              " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U "
              + //UNIT
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]
              + " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];

      //if(srcReportPotitionStock.getGroupBy()==SrcSaleReport.GROUP_BY_SUPPLIER){
//                        sql = sql + " LEFT JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " AS MV "
//                                  + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
//                                  + " = MV." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
      sql = sql + " LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " = M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID];
      //}

      sql = sql + " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportPotitionStock.getPeriodeId()
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " !=" + PstMaterial.EDIT_NON_AKTIVE;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql += " AND CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = " + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getMerkId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportPotitionStock.getMerkId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportPotitionStock.getCategoryId();
      }

      if (srcReportPotitionStock.getSku().length() > 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " = '" + srcReportPotitionStock.getSku() + "'";
      }

      if (!isZero) {
        sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
      }

      if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
        sql = sql + " ORDER BY " + " CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " ASC ";
      } else if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
        sql = sql + " ORDER BY " + " C." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " ASC ";
      } else if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_STOCK) {
        sql = sql + " ORDER BY " + " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " DESC ";
      } else {
        sql = sql + " ORDER BY " + " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
      }

      switch (DBHandler.DBSVR_TYPE) {
        case DBHandler.DBSVR_MYSQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + limitStart + "," + recordToGet;
          }

          break;

        case DBHandler.DBSVR_POSTGRESQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
          }

          break;

        case DBHandler.DBSVR_SYBASE:
          break;

        case DBHandler.DBSVR_ORACLE:
          break;

        case DBHandler.DBSVR_MSSQL:
          break;

        default:
                    ;
      }

      System.out.println("SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + sql);
      System.out.print("proses generate report....  ");
      dbrs = DBHandler.execQueryResult(sql);

      ResultSet rs = dbrs.getResultSet();
      double stockValueBegin = 0;
      double stockValueOpname = 0;
      double stockValueReceive = 0;
      double stockValueDispatch = 0;
      double stockValueReturn = 0;
      double stockValueSale = 0;
      double stockValueReturnCust = 0;
      double stockValueCost = 0;
      double stockValueClosing = 0;
      double stockValueProductionCost = 0;
      double stockValueProductionProduct = 0;

      double subTotalStockValueBegin = 0;
      double subTotalStockValueOpname = 0;
      double subTotalStockValueReceive = 0;
      double subTotalStockValueDispatch = 0;
      double subTotalStockValueReturn = 0;
      double subTotalStockValueSale = 0;
      double subTotalStockValueReturnCust = 0;
      double subTotalStockValueCost = 0;
      double subTotalStockValueClosing = 0;
      double subTotalStockValueProductionCost = 0;
      double subTotalStockValueProductionProduct = 0;
      PstUnit.loadQtyPerBaseUnitByHash();
      long oldSupplier = 0;
      boolean headerSupplier = false;

      double subtotalgroupdaily = 0;
      double subtotalAwalDaily = 0;
      double subtotalOpnameDaily = 0;
      double subtotalTerimaDaily = 0;
      double subtotalTransferDaily = 0;
      double subtotalReturnSuppDaily = 0;
      double subtotalJualDaily = 0;
      double subtotalReturnCustDaily = 0;
      double subtotalCostDaily = 0;
      double subtotalProductionCostDaily = 0;
      double subtotalProductionProductDaily = 0;
      double subtotalSaldoDaily = 0;

      double grandtotalAwalDaily = 0;
      double grandtotalOpnameDaily = 0;
      double grandtotalTerimaDaily = 0;
      double grandtotalTransferDaily = 0;
      double grandtotalReturnSuppDaily = 0;
      double grandtotalJualDaily = 0;
      double grandtotalReturnCustDaily = 0;
      double grandtotalCostingDaily = 0;
      double grandtotalProductionCostDaily = 0;
      double grandtotalProductionProductDaily = 0;
      double grandtotalSaldoDaily = 0;

      double subtotalGroup = 0;
      double subtotalAwal = 0;
      double subtotalOpname = 0;
      double subtotalTerima = 0;
      double subtotalTransfer = 0;
      double subtotalReturnSupp = 0;
      double subtotalJual = 0;
      double subtotalReturnCust = 0;
      double subtotalPembiayaan = 0;
      double subtotalProduksiBiaya = 0;
      double subtotalProduksiHasil = 0;
      double subtotalSaldo = 0;

      int loop = 0;
      while (rs.next()) {
        loop = loop + 1;
        double costPerBuyUnit = rs.getDouble(5);
        long buyUnitId = rs.getLong(7);
        long stockUnitId = rs.getLong(8);
        double qtyPerStockUnit = PstUnit.getQtyPerBaseUnitByHash(buyUnitId, stockUnitId);
        double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;

        Material material = new Material();
        material.setSku(rs.getString(1));
        material.setName(rs.getString(2));
        material.setDefaultCost(costPerStockUnit);
        material.setDefaultPrice(rs.getDouble(6));
        material.setAveragePrice(rs.getDouble(9));// * standarRate);
        material.setOID(rs.getLong(10));
        material.setBarCode(rs.getString("BARCODE"));

        MaterialStock materialStock = new MaterialStock();
        materialStock.setQty(rs.getDouble(3));
        materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));

        Unit unit = new Unit();
        unit.setName(rs.getString(4));
        unit.setCode(rs.getString(11));

        ContactList contact = new ContactList();
        if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
          contact.setOID(rs.getLong("CONTACT_ID"));
          contact.setCompName(rs.getString("COMP_NAME"));
          if (oldSupplier != contact.getOID()) {
            headerSupplier = true;
          } else {
            headerSupplier = false;
            //loop=1;
          }
          oldSupplier = contact.getOID();
        }

        Category category = new Category();
        if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
          category.setOID(rs.getLong("CATEGORY_ID"));
          category.setName(rs.getString("CAT_NAME"));
          if (oldSupplier != category.getOID()) {
            headerSupplier = true;
          } else {
            headerSupplier = false;
            //loop=1;
          }
          oldSupplier = category.getOID();
        }

        MaterialStock matStock = new MaterialStock();
        Vector vctTemp = new Vector(1, 1);
        Vector stockValue = new Vector(1, 1);

        stockValueBegin = 0;
        stockValueOpname = 0;
        stockValueReceive = 0;
        stockValueDispatch = 0;
        stockValueReturn = 0;
        stockValueSale = 0;
        stockValueReturnCust = 0;
        stockValueCost = 0;
        stockValueClosing = 0;
        stockValueProductionCost = 0;
        stockValueProductionProduct = 0;

        int masterSeason = 0;
        try {
          masterSeason = Integer.valueOf(PstSystemProperty.getValueByName("SEASON_GROUP_TYPE"));
        } catch (Exception exc) {

        }

        /**
         * get qty opname
         */
        //contoh
        //material.setOID(50442851);
        sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE, (QTY_OPNAME * COGS_PRICE) AS STOCK_VALUE_OPNAME FROM " + TBL_MATERIAL_STOCK_REPORT;
        sql += " WHERE MATERIAL_ID = " + material.getOID();
        if (srcReportPotitionStock.getIncWH() == 1) {
          try {
            Location location = PstLocation.fetchExc(materialStock.getLocationId());
            sql += " AND LOCATION_ID IN (" + materialStock.getLocationId() + "," + location.getParentLocationId() + ")";
          } catch (Exception exc) {
            sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          }
        } else {
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
        }
        sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
        sql += " AND OPNAME_ITEM_ID != 0 ORDER BY TRS_DATE DESC";

        dbrs = DBHandler.execQueryResult(sql);
        ResultSet rsx = dbrs.getResultSet();

        Date date = new Date();
        boolean withOpBool = false;
        while (rsx.next()) {
          withOpBool = false; //UPDATE BY DEWOK 2018-11-22, value "withOpBool" dijadikan FALSE
          //karena konsep melibatkan tanggal opname hanya digunakan untuk mencari nilai stok awal
          //tidak bisa dijadikan parameter tambahan untuk mencari data di range tanggal pencarian
          matStock.setOpnameQty(rsx.getDouble("QTY_OPNAME"));
          date = DBHandler.convertDate(rsx.getDate("TRS_DATE"), rsx.getTime("TRS_DATE"));
          stockValueOpname = rsx.getDouble("STOCK_VALUE_OPNAME");
          break;
        }

        /**
         * get qty all (meanggantikan query diatas)
         */
        //buatkan if, jika yang dipakai apakah berdasarkan everage atau penjualan
        String stockValueSelected = "";
        double price = 0.0;
        if (srcReportPotitionStock.getStockValueSale() == 0 || srcReportPotitionStock.getStockValueSale() == 1) {
          stockValueSelected = "COGS_PRICE ";
        } else {
          price = PstPriceTypeMapping.getSellPrice(material.getOID(), srcReportPotitionStock.getStandartId(), srcReportPotitionStock.getStockValueSale());
          stockValueSelected = "" + price;
        }

        sql = "SELECT SUM(QTY_RECEIVE) AS RECEIVE, SUM(QTY_RECEIVE * " + stockValueSelected + ") AS STOCK_VALUE_RECEIVE";
        sql += ", SUM(QTY_DISPATCH) AS DISPATCH, SUM(QTY_DISPATCH * " + stockValueSelected + ") AS STOCK_VALUE_DISPATCH";
        sql += ", SUM(QTY_RETURN) AS RETUR, SUM(QTY_RETURN * " + stockValueSelected + ") AS STOCK_VALUE_RETURN";
        sql += ", SUM(QTY_SALE) AS SALE, SUM(QTY_SALE * " + stockValueSelected + ") AS STOCK_VALUE_SALE";
        sql += ", SUM(QTY_RETURN_CUST) AS RETURN_CUST, SUM(QTY_RETURN_CUST * " + stockValueSelected + ") AS STOCK_VALUE_RETURN_CUST";
        sql += ", SUM(QTY_COST) AS COST, SUM(QTY_COST * " + stockValueSelected + ") AS STOCK_VALUE_COST";
        sql += ", SUM(QTY_PRODUCTION_COST) AS PRODUCTION_COST, SUM(QTY_PRODUCTION_COST * " + stockValueSelected + ") AS STOCK_VALUE_PRODUCTION_COST";
        sql += ", SUM(QTY_PRODUCTION_PRODUCT) AS PRODUCTION_PRODUCT, SUM(QTY_PRODUCTION_PRODUCT * " + stockValueSelected + ") AS STOCK_VALUE_PRODUCTION_PRODUCT";
        sql += " FROM " + TBL_MATERIAL_STOCK_REPORT;
        sql += " WHERE MATERIAL_ID=" + material.getOID();
        if (srcReportPotitionStock.getIncWH() == 1) {
          try {
            Location location = PstLocation.fetchExc(materialStock.getLocationId());
            sql += " AND LOCATION_ID IN (" + materialStock.getLocationId() + "," + location.getParentLocationId() + ")";
          } catch (Exception exc) {
            sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          }
        } else {
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
        }
        sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
        /**
         * kondisi ini berfungsi untuk membatasi transaksi (receive, dispatch,
         * return, sale) yang harus diproses, dimana hanya transaksi yang
         * terjadi setelah (tanggal dan waktu) proses opname saja yang perlu di
         * proses
         */
        if (withOpBool) {
          sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date, "yyyy-MM-dd") + " " + Formater.formatTimeLocale(date, "kk:mm:ss") + "'";
        }
        sql = sql + " GROUP BY MATERIAL_ID";

        dbrs = DBHandler.execQueryResult(sql);
        rsx = dbrs.getResultSet();
        while (rsx.next()) {
          matStock.setQtyIn(rsx.getDouble("RECEIVE"));
          matStock.setQtyOut(rsx.getDouble("DISPATCH"));
          matStock.setQtyMin(rsx.getDouble("RETUR"));
          matStock.setSaleQty(rsx.getDouble("SALE"));
          matStock.setQtyMax(rsx.getDouble("RETURN_CUST"));
          matStock.setQtyCost(rsx.getDouble("COST"));
          matStock.setQtyProductionCost(rsx.getDouble("PRODUCTION_COST"));
          matStock.setQtyProductionProduct(rsx.getDouble("PRODUCTION_PRODUCT"));
          stockValueReceive = rsx.getDouble("STOCK_VALUE_RECEIVE");
          stockValueDispatch = rsx.getDouble("STOCK_VALUE_DISPATCH");
          stockValueReturn = rsx.getDouble("STOCK_VALUE_RETURN");
          stockValueSale = rsx.getDouble("STOCK_VALUE_SALE");
          stockValueReturnCust = rsx.getDouble("STOCK_VALUE_RETURN_CUST");
          stockValueCost = rsx.getDouble("STOCK_VALUE_COST");
          stockValueProductionCost = rsx.getDouble("STOCK_VALUE_PRODUCTION_COST");
          stockValueProductionProduct = rsx.getDouble("STOCK_VALUE_PRODUCTION_PRODUCT");
        }

        /**
         * get begin stock
         */
        //matStock.setQty(reportQtyAwalPosisiStock(material.getOID(), materialStock.getLocationId()));
        Vector vctBeginQty = getBeginQty(material.getOID(), materialStock.getLocationId(), srcReportPotitionStock.getUserId(), stockValueSelected);
        matStock.setQty(Double.parseDouble((String) vctBeginQty.get(0)));
        stockValueBegin = Double.parseDouble((String) vctBeginQty.get(1));
        /**
         * end get begin stock
         */

        /**
         * get end stock
         */
        if (withOpBool) {
          matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty() - matStock.getQtyCost());
          stockValueClosing = (stockValueOpname + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale - stockValueCost;
        } else {
          matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty() - matStock.getQtyCost());
          stockValueClosing = (stockValueBegin + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale - stockValueCost;
        }

        stockValue.add(STOCK_VALUE_BEGIN, String.valueOf(stockValueBegin));
        stockValue.add(STOCK_VALUE_OPNAME, String.valueOf(stockValueOpname));
        stockValue.add(STOCK_VALUE_RECEIVE, String.valueOf(stockValueReceive));
        stockValue.add(STOCK_VALUE_DISPATCH, String.valueOf(stockValueDispatch));
        stockValue.add(STOCK_VALUE_RETURN, String.valueOf(stockValueReturn));
        stockValue.add(STOCK_VALUE_SALE, String.valueOf(stockValueSale));
        stockValue.add(STOCK_VALUE_RETURN_CUST, String.valueOf(stockValueReturnCust));
        stockValue.add(STOCK_VALUE_CLOSING, String.valueOf(stockValueClosing));
        stockValue.add(STOCK_VALUE_COST, String.valueOf(stockValueCost));
        stockValue.add(STOCK_VALUE_PRODUCTION_COST, String.valueOf(stockValueProductionCost));
        stockValue.add(STOCK_VALUE_PRODUCTION_PRODUCT, String.valueOf(stockValueProductionProduct));

        subtotalgroupdaily = stockValueClosing;

        subtotalAwalDaily = matStock.getQty();
        subtotalOpnameDaily = matStock.getOpnameQty();
        subtotalTerimaDaily = matStock.getQtyIn();
        subtotalTransferDaily = matStock.getQtyOut();
        subtotalReturnSuppDaily = matStock.getQtyMin();
        subtotalJualDaily = matStock.getSaleQty();
        subtotalReturnCustDaily = matStock.getQtyMax();
        subtotalCostDaily = matStock.getQtyCost();
        subtotalProductionCostDaily = matStock.getQtyProductionCost();
        subtotalProductionProductDaily = matStock.getQtyProductionProduct();
        subtotalSaldoDaily = matStock.getClosingQty();

        //grandTotal
        grandtotalAwalDaily = grandtotalAwalDaily + matStock.getQty();
        grandtotalOpnameDaily = grandtotalOpnameDaily + matStock.getOpnameQty();
        grandtotalTerimaDaily = grandtotalTerimaDaily + matStock.getQtyIn();
        grandtotalTransferDaily = grandtotalTransferDaily + matStock.getQtyOut();
        grandtotalReturnSuppDaily = grandtotalReturnSuppDaily + matStock.getQtyMin();
        grandtotalJualDaily = grandtotalJualDaily + matStock.getSaleQty();
        grandtotalReturnCustDaily = grandtotalReturnCustDaily + matStock.getQtyMax();
        grandtotalCostingDaily = grandtotalCostingDaily + matStock.getQtyCost();
        grandtotalProductionCostDaily = grandtotalProductionCostDaily + matStock.getQtyProductionCost();
        grandtotalProductionProductDaily = grandtotalProductionProductDaily + matStock.getQtyProductionProduct();
        grandtotalSaldoDaily = grandtotalSaldoDaily + matStock.getClosingQty();

        //subTotal
        subTotalStockValueBegin += stockValueBegin;
        subTotalStockValueOpname += stockValueOpname;
        subTotalStockValueReceive += stockValueReceive;
        subTotalStockValueDispatch += stockValueDispatch;
        subTotalStockValueReturn += stockValueReturn;
        subTotalStockValueSale += stockValueSale;
        subTotalStockValueReturnCust += stockValueReturnCust;
        subTotalStockValueCost += stockValueCost;
        subTotalStockValueClosing += stockValueClosing;
        subTotalStockValueProductionCost += stockValueProductionCost;
        subTotalStockValueProductionProduct += stockValueProductionProduct;

        System.out.println("Sub Total :" + subTotalStockValueClosing);

        if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER || srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
          if (headerSupplier) {
            if (loop != 1) {

              out.print("<tr valign=\"top\">");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\">.</div></td>");
              if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"3\" ><b>SUB TOTAL </b></div></td>");
              } else if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"3\" ><b>SUB TOTAL </b></div></td>");
              }

              if ((infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) || (infoShowed == SrcReportPotitionStock.SHOW_BOTH && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER)) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
              } else if (srcReportPotitionStock.getStockValueSale() != 0) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
              } else {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
              }

              //disini update harga jual
              if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
                for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
                  for (int j = 0; j < listCurrStandardX.size(); j++) {
                    Vector temp = (Vector) listCurrStandardX.get(j);
                    CurrencyType curr = (CurrencyType) temp.get(0);
                    PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
                    out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
                  }
                }
              }

              if (infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"center\">" + FRMHandler.userFormatStringDecimal(subtotalAwal) + "</div>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalOpname) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalTerima) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalTransfer) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalReturnSupp) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalJual) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalReturnCust) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalPembiayaan + subtotalProduksiBiaya) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalProduksiHasil) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalSaldo) + "</div></td>");
              } else if (infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalAwal) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalOpname) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalTerima) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalTransfer) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalReturnSupp) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalJual) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalReturnCust) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalPembiayaan + subtotalProduksiBiaya) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalProduksiHasil) + "</div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalSaldo) + "</div></td>");
              }

              if (infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalGroup) + "</div>");
              }
              out.print("</tr>");

              out.print("<tr valign=\"top\">");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\">.</div></td>");
              if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"3\" ></div></td>");
              } else if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"3\" ></div></td>");
              }

              if ((infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) || (infoShowed == SrcReportPotitionStock.SHOW_BOTH && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER)) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
              } else if (srcReportPotitionStock.getStockValueSale() != 0) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
              } else {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
              }

              if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
                for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
                  for (int j = 0; j < listCurrStandardX.size(); j++) {
                    Vector temp = (Vector) listCurrStandardX.get(j);
                    CurrencyType curr = (CurrencyType) temp.get(0);
                    PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
                    out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
                  }
                }
              }

              if (infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              } else if (infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              }

              if (infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
                out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div>");
              }
              out.print("</tr>");
            }
            subtotalGroup = 0;

            subtotalAwal = 0;
            subtotalOpname = 0;
            subtotalTerima = 0;
            subtotalTransfer = 0;
            subtotalReturnSupp = 0;
            subtotalJual = 0;
            subtotalReturnCust = 0;
            subtotalPembiayaan = 0;
            subtotalProduksiBiaya = 0;
            subtotalProduksiHasil = 0;
            subtotalSaldo = 0;

            subtotalGroup = subtotalGroup + subtotalgroupdaily;
            subtotalAwal = subtotalAwal + subtotalAwalDaily;
            subtotalOpname = subtotalOpname + subtotalOpnameDaily;
            subtotalTerima = subtotalTerima + subtotalTerimaDaily;
            subtotalTransfer = subtotalTransfer + subtotalTransferDaily;
            subtotalReturnSupp = subtotalReturnSupp + subtotalReturnSuppDaily;
            subtotalJual = subtotalJual + subtotalJualDaily;
            subtotalReturnCust = subtotalReturnCust + subtotalReturnCustDaily;
            subtotalPembiayaan = subtotalPembiayaan + subtotalCostDaily;
            subtotalProduksiBiaya = subtotalProduksiBiaya + subtotalProductionCostDaily;
            subtotalProduksiBiaya = subtotalProduksiHasil + subtotalProductionProductDaily;
            subtotalSaldo = subtotalSaldo + subtotalSaldoDaily;

            out.print("<tr valign=\"top\">");
            out.print("<td class=\"" + cellStyle + "\" div align=\"right\">.</div></td>");
            if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
              out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"3\" ><b>" + contact.getCompName() + "</b></div></td>");
            } else if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
              out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"3\" ><b>" + category.getName() + "</b></div></td>");
            }

            if ((infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) || (infoShowed == SrcReportPotitionStock.SHOW_BOTH && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER)) {
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
            } else if (srcReportPotitionStock.getStockValueSale() != 0) {
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
            } else {
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
            }

            if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
              for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
                for (int j = 0; j < listCurrStandardX.size(); j++) {
                  Vector temp = (Vector) listCurrStandardX.get(j);
                  CurrencyType curr = (CurrencyType) temp.get(0);
                  PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
                  out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
                }
              }
            }

            if (infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
              out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
            } else if (infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
            }

            if (infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
              out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div>");
            }
            out.print("</tr>");
          } else {
            subtotalGroup = subtotalGroup + subtotalgroupdaily;
            subtotalAwal = subtotalAwal + subtotalAwalDaily;
            subtotalOpname = subtotalOpname + subtotalOpnameDaily;
            subtotalTerima = subtotalTerima + subtotalTerimaDaily;
            subtotalTransfer = subtotalTransfer + subtotalTransferDaily;
            subtotalReturnSupp = subtotalReturnSupp + subtotalReturnSuppDaily;
            subtotalJual = subtotalJual + subtotalJualDaily;
            subtotalReturnCust = subtotalReturnCust + subtotalReturnCustDaily;
            subtotalPembiayaan = subtotalPembiayaan + subtotalCostDaily;
            subtotalProduksiBiaya = subtotalProduksiBiaya + subtotalProductionCostDaily;
            subtotalProduksiHasil = subtotalProduksiHasil + subtotalProductionProductDaily;
            subtotalSaldo = subtotalSaldo + subtotalSaldoDaily;
          }
        }

        out.print("<tr valign=\"top\">");
        //added by dewok 2018-05-03 for litama
        int typeOfBusinessDetail = Integer.valueOf(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));
        if (typeOfBusinessDetail == 2) {
          material.setName(SessMaterial.setItemNameForLitama(material.getOID()));
        }

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + String.valueOf(start + 1) + ".</div></td>");
        out.print("<td style='mso-number-format:\"\\@\"' class=\"" + cellStyle + "\" div align=\"left\">" + material.getSku() + "</div></td>");
        out.print("<td style='mso-number-format:\"\\@\"' class=\"" + cellStyle + "\" div align=\"left\">" + material.getBarCode() + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"left\"><a href=\"javascript:cmdViewKartuStock('" + material.getOID() + "','" + material.getSku() + "','" + material.getName() + "')\">" + material.getName() + "</a></div></td>");

        String strCategory = "-";
        try {
          strCategory = rs.getString("CAT_NAME");
        } catch (Exception exception) {
        }

        out.print("<td class=\"" + cellStyle + "\" div align=\"left\">" + strCategory + "</div></td>");

        if (masterSeason != 0) {
          long oidMappingSeason = PstMaterialMappingType.getSelectedTypeId(masterSeason, material.getOID());
          String season = "-";
          try {
            MasterType type = PstMasterType.fetchExc(oidMappingSeason);
            season = type.getMasterName();
          } catch (Exception exc) {
          }
          out.print("<td class=\"" + cellStyle + "\" div align=\"left\">" + season + "</div></td>");
        }

        if ((infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) || (infoShowed == SrcReportPotitionStock.SHOW_BOTH && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER)) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(material.getAveragePrice()) + "</div></td>"); // HPP/COGS
        } else if (srcReportPotitionStock.getStockValueSale() != 0) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(price) + "</div></td>"); // HPP/COGS
        } else {
          //updated by dewok 2018-05-03 : di hilangkan karna merusak tampilan data saat view report by qty only
          //out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(material.getAveragePrice()) + "</div></td>"); // HPP/COGS
        }

        //SrcMaterial srcMaterial = new SrcMaterial();
        //Hashtable memberPrice = SessMaterial.getPriceSaleInTypePriceMember(srcMaterial, mat, mat.getOID());
        if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
          SrcMaterial srcMaterial = new SrcMaterial();
          Hashtable memberPrice = SessMaterial.getPriceSaleInTypePriceMember(srcMaterial, material, material.getOID());
          for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
            PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
            for (int j = 0; j < listCurrStandardX.size(); j++) {
              /*Vector temp = (Vector)listCurrStandardX.get(j);
                             CurrencyType curr = (CurrencyType)temp.get(0);
                             PriceType pricetype = (PriceType)srcReportPotitionStock.getvPriceTypeId().get(i); */
              //Vector temp = (Vector)listCurrStandardX.get(j);
              Vector tempStand = (Vector) listCurrStandardX.get(j);
              CurrencyType currx = (CurrencyType) tempStand.get(0);
              StandartRate standart = (StandartRate) tempStand.get(1);

              PriceTypeMapping pTypeMapping = null;
              if (memberPrice != null && !memberPrice.isEmpty()) {

                pTypeMapping = (PriceTypeMapping) memberPrice.get("" + pricetype.getOID() + "_" + standart.getOID());
              }

              if (pTypeMapping == null) {
                pTypeMapping = new PriceTypeMapping();
                pTypeMapping.setMaterialId(material.getOID());
                pTypeMapping.setPriceTypeId(pricetype.getOID());
                pTypeMapping.setStandartRateId(currx.getOID());
              }
              out.print("<td class=\"" + cellStyle + "\" div align=\"center\">" + FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice()) + "</div>");
            }
          }
        }

        if (infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"center\">" + unit.getCode() + "</div>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getQty()) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getOpnameQty()) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getQtyIn()) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getQtyOut()) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getQtyMin()) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getSaleQty()) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getQtyMax()) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getQtyCost() + matStock.getQtyProductionCost()) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getQtyProductionProduct()) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(matStock.getClosingQty()) + "</div></td>");
        } else if (infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueBegin) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueOpname) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueReceive) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueDispatch) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueReturn) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueSale) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueReturnCust) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueCost + stockValueProductionCost) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueProductionProduct) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueClosing) + "</div></td>");
        }

        if (infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockValueClosing) + "</div>");
        }

        out.print("</tr>");

        //subtotal
        start++;
      }
      System.out.println("Sql Total : " + sql);
      if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER || srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {

        out.print("<tr valign=\"top\">");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">.</div></td>");
        if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"3\" ><b>SUB TOTAL </b></div></td>");
        } else if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"3\" ><b>SUB TOTAL </b></div></td>");
        }

        if ((infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) || (infoShowed == SrcReportPotitionStock.SHOW_BOTH && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER)) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        } else if (srcReportPotitionStock.getStockValueSale() != 0) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        } else {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        }

        if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
          for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
            for (int j = 0; j < listCurrStandardX.size(); j++) {
              Vector temp = (Vector) listCurrStandardX.get(j);
              CurrencyType curr = (CurrencyType) temp.get(0);
              PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
              out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
            }
          }
        }

//                                if (infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                } else if (infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
//                                    out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalGroup) + "</div></td>");
//                                }
        if (infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"center\">" + FRMHandler.userFormatStringDecimal(subtotalAwal) + "</div>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalOpname) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalTerima) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalTransfer) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalReturnSupp) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalJual) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalReturnCust) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalPembiayaan + subtotalProduksiBiaya) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalProduksiHasil) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalSaldo) + "</div></td>");
        } else if (infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalAwal) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalOpname) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalTerima) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalTransfer) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalReturnSupp) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalJual) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalReturnCust) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalPembiayaan + subtotalProduksiBiaya) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalProduksiHasil) + "</div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalSaldo) + "</div></td>");
        }

        if (infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(subtotalGroup) + "</div>");
        }
        out.print("</tr>");

        out.print("<tr valign=\"top\">");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">.</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"2\" ></div></td>");
        } else if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"left\" colspan=\"2\" ></div></td>");
        }

        if ((infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) || (infoShowed == SrcReportPotitionStock.SHOW_BOTH && srcReportPotitionStock.getStockValueSale() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER)) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        } else if (srcReportPotitionStock.getStockValueSale() != 0) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        } else {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        }

        if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
          for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
            for (int j = 0; j < listCurrStandardX.size(); j++) {
              Vector temp = (Vector) listCurrStandardX.get(j);
              CurrencyType curr = (CurrencyType) temp.get(0);
              PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
              out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
            }
          }
        }

        if (infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        } else if (infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        }

        if (infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div>");
        }
        out.print("</tr>");
      }

      //gui jsp subtotal stop
      if (infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
        out.print("<tr valign=\"top\">");

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"left\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"left\"><b>GRAND TOTAL</b></div></td>");

        if (stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        }

        if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
          for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
            for (int j = 0; j < listCurrStandardX.size(); j++) {
              Vector temp = (Vector) listCurrStandardX.get(j);
              CurrencyType curr = (CurrencyType) temp.get(0);
              PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
              out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
            }
          }
        }

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueBegin) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueOpname) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueReceive) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueDispatch) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueReturn) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueSale) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueReturnCust) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueCost + subTotalStockValueProductionCost) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueProductionProduct) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueClosing) + "</div></td>");
        out.print("</tr>");
      } else if (infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
        out.print("<tr valign=\"top\">");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"left\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"left\"><b>GRAND TOTAL</b></div></td>");

        if (stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        }

        if (srcReportPotitionStock.getvPriceTypeId() != null && srcReportPotitionStock.getvPriceTypeId().size() > 0) {
          for (int i = 0; i < srcReportPotitionStock.getvPriceTypeId().size(); i++) {
            for (int j = 0; j < listCurrStandardX.size(); j++) {
              Vector temp = (Vector) listCurrStandardX.get(j);
              CurrencyType curr = (CurrencyType) temp.get(0);
              PriceType pricetype = (PriceType) srcReportPotitionStock.getvPriceTypeId().get(i);
              out.print("<td class=\"" + cellStyle + "\" div align=\"center\"></div>");
            }
          }
        }

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalAwalDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalOpnameDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalTerimaDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalTransferDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalReturnSuppDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalJualDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalReturnCustDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalCostingDaily + grandtotalProductionCostDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalProductionProductDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandtotalSaldoDaily) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalStockValueClosing) + "</div></td>");
        out.print("</tr>");
      }
      System.out.println("List Sql getReportStockAll : " + sql);
      rs.close();
      System.out.println("OK!");
    } catch (Exception e) {
      System.out.println("Exc on SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
      return;
    }
  }

  /**
   * Fungsi ini digunakan untuk mendapatkan Grand total list stok
   *
   * @param SrcReportPotitionStock Objek untuk melakukan pencarian
   * @param boolean Kondisi untuk menentukkan apakah proses kalkulasi melibatkan
   * stok bernilai nol
   * @param int Start list
   * @param int Banyaknya list yang harus ditampilkan
   * @return Vector yang menampung instance dari class Material, MaterialStock
   * dan Unit
   * @create by gwawan@dimata 3 Jan 2008
   * @updated by gwawan@dimata 17 Jan 2008
   * @modified by mirah@dimata 22 Feb 2011
   */
  public static void getGrandTotalReportStockAll(JspWriter out, int infoShowed, int stockValueBy, int language, int start, SrcReportPotitionStock srcReportPotitionStock, boolean isZero, int limitStart, int recordToGet, String cellStyle) {
    //public static void getReportStockAll(JspWriter out, int infoShowed, int stockValueBy, int language,int start, SrcReportPotitionStock srcReportPotitionStock, int limitStart, int recordToGet, String cellStyle) {
    DBResultSet dbrs = null;
    Vector result = new Vector(1, 1);
    try {
      String sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + // SKU
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + // NAME
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]
              + // QTY
              ", U." + PstUnit.fieldNames[PstUnit.FLD_NAME]
              + //UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]
              + // DEFAULT_COST
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]
              + // DEFAULT_PRICE
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
              + // BUYING_UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + // STOCK_UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + // SELLING_PRICE
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + // QTY
              ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + //UNIT
              ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]
              + // DEFAULT_COST_CURRENCY_ID
              ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]
              + // LOCATION
              " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS "
              + //  MATERIAL_STOCK
              " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
              + // MATERIAL
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + // MATERIAL_STOCK_ID
              " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U "
              + //UNIT
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]
              + " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]
              + " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportPotitionStock.getPeriodeId()
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;

      if (srcReportPotitionStock.getLocationId() != 0) {
        if (srcReportPotitionStock.getIncWH() == 1) {
          try {
            Location location = PstLocation.fetchExc(srcReportPotitionStock.getLocationId());
            sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " IN (" + srcReportPotitionStock.getLocationId() + "," + location.getParentLocationId() + ")";
          } catch (Exception exc) {
            sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportPotitionStock.getLocationId();
          }
        } else {
          sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportPotitionStock.getLocationId();
        }
      }

      if (srcReportPotitionStock.getMerkId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportPotitionStock.getMerkId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportPotitionStock.getCategoryId();
      }

      if (!isZero) {
        sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
      }

      sql = sql + " ORDER BY "
              + " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

      switch (DBHandler.DBSVR_TYPE) {
        case DBHandler.DBSVR_MYSQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + limitStart + "," + recordToGet;
          }

          break;

        case DBHandler.DBSVR_POSTGRESQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
          }

          break;

        case DBHandler.DBSVR_SYBASE:
          break;

        case DBHandler.DBSVR_ORACLE:
          break;

        case DBHandler.DBSVR_MSSQL:
          break;

        default:
                    ;
      }

      System.out.println("SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + sql);
      System.out.print("proses generate report....  ");
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();

      double stockValueBegin = 0;
      double stockValueOpname = 0;
      double stockValueReceive = 0;
      double stockValueDispatch = 0;
      double stockValueReturn = 0;
      double stockValueSale = 0;
      double stockValueReturnCust = 0;
      double stockValueClosing = 0;

      double grandTotalStockValueBegin = 0;
      double grandTotalStockValueOpname = 0;
      double grandTotalStockValueReceive = 0;
      double grandTotalStockValueDispatch = 0;
      double grandTotalStockValueReturn = 0;
      double grandTotalStockValueSale = 0;
      double grandTotalStockValueReturnCust = 0;
      double grandTotalStockValueClosing = 0;
      //double grandTotalStockValueClosing2 = 0;
      PstUnit.loadQtyPerBaseUnitByHash();
      while (rs.next()) {
        Vector vectTemp = new Vector(1, 1);

        // calculate cost per stock unit id
        double costPerBuyUnit = rs.getDouble(5);
        long buyUnitId = rs.getLong(7);
        long stockUnitId = rs.getLong(8);
        double qtyPerStockUnit = PstUnit.getQtyPerBaseUnitByHash(buyUnitId, stockUnitId);
        //double qtyPerStockUnit = PstUnit.getQtyPerBaseUnit(buyUnitId, stockUnitId);
        double costPerStockUnit = costPerBuyUnit / qtyPerStockUnit;

        Material material = new Material();
        material.setSku(rs.getString(1));
        material.setName(rs.getString(2));
        material.setDefaultCost(costPerStockUnit);
        material.setDefaultPrice(rs.getDouble(6));
        material.setAveragePrice(rs.getDouble(9));// * standarRate);
        material.setOID(rs.getLong(10));
        vectTemp.add(material);

        MaterialStock materialStock = new MaterialStock();
        materialStock.setQty(rs.getDouble(3));
        materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
        vectTemp.add(materialStock);

        Unit unit = new Unit();
        unit.setName(rs.getString(4));
        unit.setCode(rs.getString(11));
        vectTemp.add(unit);

        MaterialStock matStock = new MaterialStock();
        Vector vctTemp = new Vector(1, 1);
        Vector stockValue = new Vector(1, 1);

        stockValueBegin = 0;
        stockValueOpname = 0;
        stockValueReceive = 0;
        stockValueDispatch = 0;
        stockValueReturn = 0;
        stockValueSale = 0;
        stockValueReturnCust = 0;
        stockValueClosing = 0;

        /**
         * get qty opname
         */
        sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE, (QTY_OPNAME * COGS_PRICE) AS STOCK_VALUE_OPNAME FROM " + TBL_MATERIAL_STOCK_REPORT;
        sql += " WHERE MATERIAL_ID = " + material.getOID();
        if (srcReportPotitionStock.getIncWH() == 1) {
          try {
            Location location = PstLocation.fetchExc(materialStock.getLocationId());
            sql += " AND LOCATION_ID IN (" + materialStock.getLocationId() + "," + location.getParentLocationId() + ")";
          } catch (Exception exc) {
            sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          }
        } else {
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
        }
        sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
        sql += " AND OPNAME_ITEM_ID != 0 ORDER BY TRS_DATE DESC";

        dbrs = DBHandler.execQueryResult(sql);
        ResultSet rsx = dbrs.getResultSet();

        Date date = new Date();
        boolean withOpBool = false;
        while (rsx.next()) {
          withOpBool = false; //UPDATE BY DEWOK 2018-11-22, value "withOpBool" dijadikan FALSE
          //karena konsep melibatkan tanggal opname hanya digunakan untuk mencari nilai stok awal
          //tidak bisa dijadikan parameter tambahan untuk mencari data di range tanggal pencarian
          matStock.setOpnameQty(rsx.getDouble("QTY_OPNAME"));
          date = DBHandler.convertDate(rsx.getDate("TRS_DATE"), rsx.getTime("TRS_DATE"));
          stockValueOpname = rsx.getDouble("STOCK_VALUE_OPNAME");
          break;
        }

        /**
         * get qty all (meanggantikan query diatas)
         */
        sql = "SELECT SUM(QTY_RECEIVE) AS RECEIVE, SUM(QTY_RECEIVE * COGS_PRICE) AS STOCK_VALUE_RECEIVE";
        sql += ", SUM(QTY_DISPATCH) AS DISPATCH, SUM(QTY_DISPATCH * COGS_PRICE) AS STOCK_VALUE_DISPATCH";
        sql += ", SUM(QTY_RETURN) AS RETUR, SUM(QTY_RETURN * COGS_PRICE) AS STOCK_VALUE_RETURN";
        sql += ", SUM(QTY_SALE) AS SALE, SUM(QTY_SALE * COGS_PRICE) AS STOCK_VALUE_SALE";
        sql += ", SUM(QTY_RETURN_CUST) AS RETURN_CUST, SUM(QTY_RETURN_CUST * COGS_PRICE) AS STOCK_VALUE_RETURN_CUST";
        sql += " FROM " + TBL_MATERIAL_STOCK_REPORT;
        sql += " WHERE MATERIAL_ID=" + material.getOID();
        if (srcReportPotitionStock.getIncWH() == 1) {
          try {
            Location location = PstLocation.fetchExc(materialStock.getLocationId());
            sql += " AND LOCATION_ID IN (" + materialStock.getLocationId() + "," + location.getParentLocationId() + ")";
          } catch (Exception exc) {
            sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          }
        } else {
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
        }
        sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
        /**
         * kondisi ini berfungsi untuk membatasi transaksi (receive, dispatch,
         * return, sale) yang harus diproses, dimana hanya transaksi yang
         * terjadi setelah (tanggal dan waktu) proses opname saja yang perlu di
         * proses
         */

        if (withOpBool) {
          sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date, "yyyy-MM-dd") + " " + Formater.formatTimeLocale(date, "kk:mm:ss") + "'";
        }
        sql = sql + " GROUP BY MATERIAL_ID";

        dbrs = DBHandler.execQueryResult(sql);
        rsx = dbrs.getResultSet();
        while (rsx.next()) {
          matStock.setQtyIn(rsx.getDouble("RECEIVE"));
          matStock.setQtyOut(rsx.getDouble("DISPATCH"));
          matStock.setQtyMin(rsx.getDouble("RETUR"));
          matStock.setSaleQty(rsx.getDouble("SALE"));
          matStock.setQtyMax(rsx.getDouble("RETURN_CUST"));
          stockValueReceive = rsx.getDouble("STOCK_VALUE_RECEIVE");
          stockValueDispatch = rsx.getDouble("STOCK_VALUE_DISPATCH");
          stockValueReturn = rsx.getDouble("STOCK_VALUE_RETURN");
          stockValueSale = rsx.getDouble("STOCK_VALUE_SALE");
          stockValueReturnCust = rsx.getDouble("STOCK_VALUE_RETURN_CUST");
        }

        /**
         * get begin stock
         */
        //matStock.setQty(reportQtyAwalPosisiStock(material.getOID(), materialStock.getLocationId()));
        Vector vctBeginQty = getBeginQty(material.getOID(), materialStock.getLocationId(), srcReportPotitionStock.getUserId());
        matStock.setQty(Double.parseDouble((String) vctBeginQty.get(0)));
        stockValueBegin = Double.parseDouble((String) vctBeginQty.get(1));
        /**
         * end get begin stock
         */

        /**
         * get end stock
         */
        if (withOpBool) {
          matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
          stockValueClosing = (stockValueOpname + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale;
        } else {
          matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
          stockValueClosing = (stockValueBegin + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale;
        }

        stockValue.add(STOCK_VALUE_BEGIN, String.valueOf(stockValueBegin));
        stockValue.add(STOCK_VALUE_OPNAME, String.valueOf(stockValueOpname));
        stockValue.add(STOCK_VALUE_RECEIVE, String.valueOf(stockValueReceive));
        stockValue.add(STOCK_VALUE_DISPATCH, String.valueOf(stockValueDispatch));
        stockValue.add(STOCK_VALUE_RETURN, String.valueOf(stockValueReturn));
        stockValue.add(STOCK_VALUE_SALE, String.valueOf(stockValueSale));
        stockValue.add(STOCK_VALUE_RETURN_CUST, String.valueOf(stockValueReturnCust));
        stockValue.add(STOCK_VALUE_CLOSING, String.valueOf(stockValueClosing));

        grandTotalStockValueBegin += stockValueBegin;
        grandTotalStockValueOpname += stockValueOpname;
        grandTotalStockValueReceive += stockValueReceive;
        grandTotalStockValueDispatch += stockValueDispatch;
        grandTotalStockValueReturn += stockValueReturn;
        grandTotalStockValueSale += stockValueSale;
        grandTotalStockValueReturnCust += stockValueReturnCust;
        grandTotalStockValueClosing += stockValueClosing;

        System.out.println("No. " + start++);
        System.out.println(". Grand Total :" + grandTotalStockValueClosing);

      }
      //grandTotalStockValueClosing2 += stockValueClosing;
      //System.out.println("Grand Total :" +grandTotalStockValueClosing);

      if (infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
        out.print("<tr valign=\"top\">");

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"left\"><b>Grand Total</b></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"left\"></div></td>");

        if (stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        }

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(grandTotalStockValueBegin) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(grandTotalStockValueOpname) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(grandTotalStockValueReceive) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(grandTotalStockValueDispatch) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(grandTotalStockValueReturn) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(grandTotalStockValueSale) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(grandTotalStockValueReturnCust) + "</div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(grandTotalStockValueClosing) + "</div></td>");
        out.print("</tr>");
        //start++;
        //result.add(vectTemp);
      } else if (infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
        out.print("<tr valign=\"top\">");

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        //out.print("<td class=\""+cellStyle+ "\" div align=\"left\"><b>Sub Total</b></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"left\"><b>Grand Total</b></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"left\"></div></td>");

        if (stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
          out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>"); // HPP/COGS
        }

        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\"></div></td>");
        out.print("<td class=\"" + cellStyle + "\" div align=\"right\">" + FRMHandler.userFormatStringDecimal(grandTotalStockValueClosing) + "</div></td>");
        out.print("</tr>");
        start++;
      }

      rs.close();
      System.out.println("OK!");
    } catch (Exception e) {
      System.out.println("Exc on SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
      return;
    }
  }

  /**
   * Fungsi ini digunakan untuk mendapatkan count list stok
   *
   * @param SrcReportPotitionStock Objek untuk melakukan pencarian
   * @param boolean Kondisi untuk menentukkan apakah proses kalkulasi melibatkan
   * stok bernilai nol
   * @param int Start list
   * @param int Banyaknya list yang harus ditampilkan
   * @return Vector yang menampung instance dari class Material, MaterialStock
   * dan Unit
   * @create by gwawan@dimata 3 Jan 2008
   * @updated by gwawan@dimata 17 Jan 2008
   * @modified by mirah@dimata 25 Feb 2011
   */
  public static int getCountReportStockAll(SrcReportPotitionStock srcReportPotitionStock, boolean isZero, int limitStart, int recordToGet) {
    int count = 0;
    DBResultSet dbrs = null;
    Vector result = new Vector(1, 1);
    try {
      String sql = "SELECT COUNT(MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + ") AS CNT FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS "
              + //  MATERIAL_STOCK
              " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
              + // MATERIAL
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + // MATERIAL_STOCK_ID
              " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U "
              + //UNIT
              " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]
              + " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];

      if (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
        sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " AS MV "
                + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                + " = MV." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
        sql = sql + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
                + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
                + " = MV." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID];
      }

      sql = sql + " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + srcReportPotitionStock.getPeriodeId()
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_REGULAR
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getMerkId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + srcReportPotitionStock.getMerkId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0 && (srcReportPotitionStock.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER)) {
        sql += " AND CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = " + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + srcReportPotitionStock.getCategoryId();
      }

      if (!isZero) {
        sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " <> 0";
      }

      sql = sql + " ORDER BY "
              + " M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

      switch (DBHandler.DBSVR_TYPE) {
        case DBHandler.DBSVR_MYSQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + limitStart + "," + recordToGet;
          }

          break;

        case DBHandler.DBSVR_POSTGRESQL:
          if (limitStart == 0 && recordToGet == 0) {
            sql = sql + "";
          } else {
            sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
          }

          break;

        case DBHandler.DBSVR_SYBASE:
          break;

        case DBHandler.DBSVR_ORACLE:
          break;

        case DBHandler.DBSVR_MSSQL:
          break;

        default:
                    ;
      }

      System.out.println("SessReportPotitionStock.getReportStockAll(#,#,#,#) : " + sql);
      System.out.print("proses generate report....  ");
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();

      while (rs.next()) {
        count = rs.getInt(1);
      }

      rs.close();
      System.out.println("OK!");
    } catch (Exception e) {
      System.out.println("Exc on SessReportPotitionStock.getCountReportStockAll(#,#,#,#) : " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
      return count;
    }
  }

  public static void insertSelectOpname(String TBL_NAME, SrcReportPotitionStock srcReportPotitionStock) {
    try {
      String sql = "INSERT INTO " + TBL_NAME
              + " (SUB_CATEGORY_NAME, OPNAME_ITEM_ID, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID,"
              + " BARCODE,MATERIAL,SELL_PRICE,UNIT,QTY_OPNAME, LOCATION_ID, COGS_PRICE, USER_ID) "
              + " SELECT DISTINCT "
              + " SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER]
              + ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID]
              + ", SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]
              + // " ,SUM(RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+") "+
              ", SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql += ", SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST];
      } else {
        sql += ", 0";
      }
      sql += ", " + srcReportPotitionStock.getUserId();
      sql += " FROM " + PstMaterial.TBL_MATERIAL + " AS M "
              + " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " AS SOI "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]
              + " INNER JOIN " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME + " AS SO "
              + " ON SOI." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]
              + " = SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]
              + " WHERE SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]
              + " BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'"
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + srcReportPotitionStock.getLocationId();
      }
      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
      }

      if (srcReportPotitionStock.getSubCategoryId() != 0) {
        // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportPotitionStock.getSubCategoryId();
      }

      //sql = sql + " AND MR." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED;
      //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
      //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
      sql += " AND (SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED;
      sql += " OR SO." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";

      //System.out.println("SQL OPNAME : " + sql);
      int i = DBHandler.execSqlInsert(sql);

    } catch (Exception e) {
    }
  }

  public static void insertSelectReceive(String TBL_NAME, SrcReportPotitionStock srcReportPotitionStock) {
    DBResultSet dbrs = null;
    try {
      String sql = "INSERT INTO " + TBL_NAME
              + " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL,"
              + " SELL_PRICE, UNIT, QTY_RECEIVE, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) "
              + " SELECT DISTINCT "
              + " MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]
              + ", MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + ", MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]
              + ", MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + ", MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
              + ", MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql += ", (MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE];
        sql += " * (MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST];
        sql += " + MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST] + "))";

      } else {
        sql += ", 0";
      }
      sql += ", " + srcReportPotitionStock.getUserId();
      sql += " FROM " + PstMaterial.TBL_MATERIAL + " AS M "
              + " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS MRI "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
              + " INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " AS MR "
              + " ON MRI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]
              + " = MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]
              + " WHERE MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]
              + " BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'"
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + "=" + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
      }

      if (srcReportPotitionStock.getSubCategoryId() != 0) {
        //sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportPotitionStock.getSubCategoryId();
      }

      //sql = sql + " AND (MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED +
      //        " OR MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";
      //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
      //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
      sql += " AND ( MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED;
      sql += " OR MR." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";

      //System.out.println("SQL RECEIVE : " + sql);
      int i = DBHandler.execSqlInsert(sql);
      // untuk mengalikan qty sesuai dengan base unit
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        //sql = "SELECT * FROM " + TBL_NAME;
        sql = "SELECT * FROM " + TBL_NAME + " WHERE USER_ID ='" + srcReportPotitionStock.getUserId() + "' AND LOCATION_ID ='" + srcReportPotitionStock.getLocationId() + "'";
        dbrs = DBHandler.execQueryResult(sql);
        ResultSet rs = dbrs.getResultSet();
        PstUnit.loadQtyPerBaseUnitByHash();
        while (rs.next()) {
          long unitId = rs.getLong("UNIT_ID");
          long baseUnitId = rs.getLong("BASE_UNIT_ID");

          double qtyBase = PstUnit.getQtyPerBaseUnitByHash(unitId, baseUnitId);
          sql = "UPDATE " + TBL_NAME + " SET QTY_RECEIVE = " + (rs.getDouble("QTY_RECEIVE") * qtyBase);
          sql += " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs.getLong("MATERIAL_ID") + ";";
          DBHandler.execUpdate(sql);
        }
      }
      //System.out.println("====>>>> END TRANSFER DATA RECEIVE");
    } catch (Exception e) {
      System.out.println("Exc. in insertSelectReceive(#,#) >> " + e.toString());
    }
  }

  public static void insertSelectReturn(String TBL_NAME, SrcReportPotitionStock srcReportPotitionStock) {
    DBResultSet dbrs = null;
    try {
      String sql = "INSERT INTO " + TBL_NAME
              + " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID,"
              + " BARCODE,MATERIAL, SELL_PRICE, UNIT, QTY_RETURN, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) "
              + " SELECT DISTINCT "
              + " MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE]
              + ", MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + ", MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY]
              + // " ,SUM(RMI."+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY]+") "+
              ", MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_UNIT_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + ", MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID]
              + ", MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql += ", (MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_COST];
        sql += " * MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_TRANS_RATE] + ")";
      } else {
        sql += ", 0";
      }
      sql += ", " + srcReportPotitionStock.getUserId();
      sql += " FROM " + PstMaterial.TBL_MATERIAL + " AS M "
              + " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS MRI "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID]
              + " INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " AS MR "
              + " ON MRI." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID]
              + " = MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID]
              + " WHERE MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]
              + " BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd 23:23:59") + "'"
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
      }

      if (srcReportPotitionStock.getSubCategoryId() != 0) {
        // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportPotitionStock.getSubCategoryId();
      }

      //sql = sql + " AND (MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED +
      //        " OR MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";
      //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
      //sql += " AND MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_DRAFT;
      //sql += " AND MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_FINAL;
      sql += " AND ( MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED;
      sql += " OR MR." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";

      // System.out.println("SQL RETUR : " + sql);
      int i = DBHandler.execSqlInsert(sql);

      // untuk mengalikan qty sesuai dengan base unit
      //System.out.println("====>>>> START TRANSFER DATA RETURN");
      // sql = "SELECT * FROM " + TBL_NAME;
      sql = "SELECT * FROM " + TBL_NAME + " WHERE USER_ID ='" + srcReportPotitionStock.getUserId() + "' AND LOCATION_ID ='" + srcReportPotitionStock.getLocationId() + "'";
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      PstUnit.loadQtyPerBaseUnitByHash();
      while (rs.next()) {
        long unitId = rs.getLong("UNIT_ID");
        long baseUnitId = rs.getLong("BASE_UNIT_ID");

        // double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId);
        double qtyBase = PstUnit.getQtyPerBaseUnitByHash(unitId, baseUnitId);
        sql = "UPDATE " + TBL_NAME + " SET QTY_RETURN = " + (rs.getDouble("QTY_RETURN") * qtyBase);
        sql += " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs.getLong("MATERIAL_ID");
        DBHandler.execUpdate(sql);
      }
      //System.out.println("====>>>> END TRANSFER DATA RETURN");

    } catch (Exception e) {
    }
  }

  public static void insertSelectDispatch(String TBL_NAME, SrcReportPotitionStock srcReportPotitionStock) {
    DBResultSet dbrs = null;
    try {
      String sql = "INSERT INTO " + TBL_NAME
              + " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL, SELL_PRICE,"
              + " UNIT, QTY_DISPATCH, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) "
              + " SELECT DISTINCT "
              + " MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]
              + ", MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + ", MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]
              + // " ,SUM(RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+") "+
              ", MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + ", MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]
              + ", MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql += ", MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP];
      } else {
        sql += ", 0";
      }
      sql += ", " + srcReportPotitionStock.getUserId();
      sql += " FROM " + PstMaterial.TBL_MATERIAL + " AS M "
              + " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS MDI "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
              + " INNER JOIN " + PstMatDispatch.TBL_DISPATCH + " AS MD "
              + " ON MDI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]
              + " = MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]
              + " WHERE MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]
              + " BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'"
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
      }

      if (srcReportPotitionStock.getSubCategoryId() != 0) {
        // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportPotitionStock.getSubCategoryId();
      }

      //sql = sql + " AND (MR." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
      //       " OR MR." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";
      //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
      //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
      //sql += " AND MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_DRAFT;
      //sql += " AND MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_FINAL;
      sql += " AND ( MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED;
      sql += " OR MD." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";

      //System.out.println("SQL DISPATCH : " + sql);
      int i = DBHandler.execSqlInsert(sql);
      // untuk mengalikan qty sesuai dengan base unit
      //System.out.println("====>>>> START TRANSFER DATA DISPATCH");
      //sql = "SELECT * FROM " + TBL_NAME;
      sql = "SELECT * FROM " + TBL_NAME + " WHERE USER_ID ='" + srcReportPotitionStock.getUserId() + "' AND LOCATION_ID ='" + srcReportPotitionStock.getLocationId() + "'";
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      PstUnit.loadQtyPerBaseUnitByHash();
      while (rs.next()) {
        long unitId = rs.getLong("UNIT_ID");
        long baseUnitId = rs.getLong("BASE_UNIT_ID");

        //double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId);
        double qtyBase = PstUnit.getQtyPerBaseUnitByHash(unitId, baseUnitId);
        sql = "UPDATE " + TBL_NAME + " SET QTY_DISPATCH = " + (rs.getDouble("QTY_DISPATCH") * qtyBase);
        sql += " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs.getLong("MATERIAL_ID");
        DBHandler.execUpdate(sql);
      }
      //System.out.println("====>>>> END TRANSFER DATA DISPATCH");

    } catch (Exception e) {
    }
  }

  public static void insertSelectCosting(String TBL_NAME, SrcReportPotitionStock srcReportPotitionStock) {
    DBResultSet dbrs = null;
    try {
      String sql = "INSERT INTO " + TBL_NAME
              + " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL,"
              + " SELL_PRICE, UNIT, QTY_COST, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) "
              + " SELECT DISTINCT "
              + " MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE]
              + ", MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + ", MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY]
              + // " ,SUM(RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+") "+
              ", MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + ", MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID]
              + ", MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql += ", MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_HPP];
      } else {
        sql += ", 0";
      }
      sql += ", " + srcReportPotitionStock.getUserId();
      sql += " FROM " + PstMaterial.TBL_MATERIAL + " AS M "
              + " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " AS MCI "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]
              + " INNER JOIN " + PstMatCosting.TBL_COSTING + " AS MC "
              + " ON MCI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]
              + " = MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]
              + " WHERE MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]
              + " BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'"
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
      }

      if (srcReportPotitionStock.getSubCategoryId() != 0) {
        // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportPotitionStock.getSubCategoryId();
      }

      //sql = sql + " AND (MR." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED +
      //       " OR MR." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";
      //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
      //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
      //sql += " AND MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_DRAFT;
      //sql += " AND MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_FINAL;
      sql += " AND ( MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED;
      sql += " OR MC." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";

      //System.out.println("SQL COSTING : " + sql);
      int i = DBHandler.execSqlInsert(sql);
      // untuk mengalikan qty sesuai dengan base unit
      //System.out.println("====>>>> START TRANSFER DATA COSTING");
      //sql = "SELECT * FROM " + TBL_NAME;
      sql = "SELECT * FROM " + TBL_NAME + " WHERE USER_ID ='" + srcReportPotitionStock.getUserId() + "' AND LOCATION_ID ='" + srcReportPotitionStock.getLocationId() + "'";
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      PstUnit.loadQtyPerBaseUnitByHash();
      while (rs.next()) {
        long unitId = rs.getLong("UNIT_ID");
        long baseUnitId = rs.getLong("BASE_UNIT_ID");

        //double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId);
        double qtyBase = PstUnit.getQtyPerBaseUnitByHash(unitId, baseUnitId);
        sql = "UPDATE " + TBL_NAME + " SET QTY_COST = " + (rs.getDouble("QTY_COST") * qtyBase);
        sql += " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND MATERIAL_ID = " + rs.getLong("MATERIAL_ID");
        DBHandler.execUpdate(sql);
      }
      //System.out.println("====>>>> END TRANSFER DATA DISPATCH");

    } catch (Exception e) {
    }
  }

  public static void insertSelectSale(String TBL_NAME, SrcReportPotitionStock srcReportPotitionStock) {
    DBResultSet dbrs = null;
    try {
      String sql = "INSERT INTO " + TBL_NAME
              + " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL, SELL_PRICE,"
              + " UNIT, QTY_SALE, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) "
              + " SELECT DISTINCT "
              + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]
              + ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        //sql += ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK];
        sql += ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql += ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY];
      }
      sql += ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]
              + ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID];
      switch (srcReportPotitionStock.getStockValueBy()) {
        case SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER:
          sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
          break;
        case SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION:
          sql += ", (BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE];
          sql += " * BM." + PstBillMain.fieldNames[PstBillMain.FLD_RATE] + ")";
          break;
        default:
          sql += ", 0";
          break;
      }
      sql += ", " + srcReportPotitionStock.getUserId();
      sql += " FROM " + PstMaterial.TBL_MATERIAL + " AS M "
              + " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
              + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
              + " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
              + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
              + " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
              + " BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'"
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;

      sql = sql + " AND ( "
              + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE
              + " AND "
              + " ( BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TYPE_RETUR
              + " OR BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TYPE_INVOICE
              + " ) "
              + " AND "
              + " ( BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TYPE_RETUR
              + " OR BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TYPE_INVOICE
              + " ) "
              + " ) ";
      //+ " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE

      sql = sql + " AND (BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED
              + " OR BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";

      String sql2 = "INSERT INTO " + TBL_NAME
              + " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL,"
              + " SELL_PRICE, UNIT, QTY_RETURN_CUST, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) "
              + " SELECT DISTINCT "
              + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]
              + ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        //sql2 += ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK];
        sql2 += ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql2 += ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY];
      }
      sql2 += ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + ", BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]
              + ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        sql2 += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql2 += ", (BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE];
        sql2 += " * BM." + PstBillMain.fieldNames[PstBillMain.FLD_RATE] + ")";
      } else {
        sql2 += ", 0";
      }
      sql2 += ", " + srcReportPotitionStock.getUserId();
      sql2 += " FROM " + PstMaterial.TBL_MATERIAL + " AS M "
              + " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
              + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
              + " ON BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
              + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
              + " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
              + " BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'"
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR
              + " AND ";
      sql2 = sql2 + "( "
              + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_RETUR
              + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TYPE_INVOICE
              + " AND "
              + " ( BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TYPE_RETUR
              + " OR BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TYPE_INVOICE
              + " ) "
              + " ) ";
      sql2 = sql2 + " AND (BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED
              + " OR BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcReportPotitionStock.getLocationId();
        sql2 += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
        sql2 = sql2 + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getSubCategoryId() != 0) {
        // sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + "=" + srcReportPotitionStock.getSubCategoryId();
      }

      //sql = sql + " AND (MR." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED +
      //        " OR MR." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";
      //sql2 = sql2 + " AND (MR." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_POSTED +
      //        " OR MR." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_CLOSED + ")";
      //sql = sql + " GROUP BY M."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
      //sql = sql + " GROUP BY RMI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID];
      //System.out.println("SQL PENJUALAN : " + sql+"");
      int i = DBHandler.execSqlInsert(sql);
      /**
       * Proses ini tidak diperlukan lagi, karena field qty_stock merupakan qty
       * berdasarkan unit dari stock // untuk mengalikan qty sesuai dengan base
       * unit //System.out.println("====>>>> START TRANSFER DATA INVOICE"); sql
       * = "SELECT * FROM " + TBL_NAME; dbrs = DBHandler.execQueryResult(sql);
       * ResultSet rs = dbrs.getResultSet(); while (rs.next()) { long unitId =
       * rs.getLong("UNIT_ID"); long baseUnitId = rs.getLong("BASE_UNIT_ID");
       *
       * double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId); sql =
       * "UPDATE " + TBL_NAME + " SET QTY_SALE = " + (rs.getDouble("QTY_SALE") *
       * qtyBase); sql+= " WHERE ITEM_ID = " + rs.getLong("ITEM_ID") + " AND
       * MATERIAL_ID = " + rs.getLong("MATERIAL_ID"); DBHandler.execUpdate(sql);
       * }
       */
      //System.out.println("====>>>> END TRANSFER DATA INVOICE");

      //System.out.println("SQL RETUR PENJUALAN : " + sql2);
      int a = DBHandler.execSqlInsert(sql2);
      /**
       * Proses ini tidak diperlukan lagi, karena field qty_stock merupakan qty
       * berdasarkan unit dari stock // untuk mengalikan qty sesuai dengan base
       * unit //System.out.println("====>>>> START TRANSFER DATA CUSTOMER
       * RETURN"); sql2 = "SELECT * FROM " + TBL_NAME; dbrs =
       * DBHandler.execQueryResult(sql2); ResultSet rs2 = dbrs.getResultSet();
       * while (rs2.next()) { long unitId = rs2.getLong("UNIT_ID"); long
       * baseUnitId = rs2.getLong("BASE_UNIT_ID");
       *
       * double qtyBase = PstUnit.getQtyPerBaseUnit(unitId, baseUnitId); sql =
       * "UPDATE " + TBL_NAME + " SET QTY_RETURN_CUST = " +
       * (rs2.getDouble("QTY_RETURN_CUST") * qtyBase); sql+= " WHERE ITEM_ID = "
       * + rs2.getLong("ITEM_ID") + " AND MATERIAL_ID = " +
       * rs2.getLong("MATERIAL_ID"); DBHandler.execUpdate(sql); }
       */
      //System.out.println("====>>>> END TRANSFER DATA CUSTOMER RETURN");

    } catch (Exception e) {
      System.out.println("Error insert penjualan " + e.toString());
    }
  }

  public static void insertSelectProductionCost(String TBL_NAME, SrcReportPotitionStock srcReportPotitionStock) {
    DBResultSet dbrs = null;
    try {
      String sql = "INSERT INTO " + TBL_NAME
              + " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL, SELL_PRICE,"
              + " UNIT, QTY_PRODUCTION_COST, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) "
              + " SELECT DISTINCT "
              + " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]
              + ", P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + ", PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_QTY]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + ", P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
              + ", P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql += ", PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_VALUE];
      } else {
        sql += ", 0";
      }
      sql += ", " + srcReportPotitionStock.getUserId();
      sql += " FROM " + PstMaterial.TBL_MATERIAL + " AS M "
              + " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " INNER JOIN " + PstProductionCost.TBL_PRODUCTION_COST + " AS PC "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID]
              + " INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " AS PG "
              + " ON PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
              + " = PC." + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID]
              + " INNER JOIN " + PstProduction.TBL_PRODUCTION + " AS P "
              + " ON P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
              + " = PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
              + " WHERE P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
              + " BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'"
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID] + "=" + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
      }

      sql += " AND ( P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED;
      sql += " OR P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";

      int i = DBHandler.execSqlInsert(sql);
    } catch (Exception e) {
    }
  }

  public static void insertSelectProductionProduct(String TBL_NAME, SrcReportPotitionStock srcReportPotitionStock) {
    DBResultSet dbrs = null;
    try {
      String sql = "INSERT INTO " + TBL_NAME
              + " (SUB_CATEGORY_NAME, TRS_DATE, SUB_CATEGORY_ID, SUPPLIER_ID, MATERIAL_ID, BARCODE, MATERIAL, SELL_PRICE,"
              + " UNIT, QTY_PRODUCTION_PRODUCT, UNIT_ID, BASE_UNIT_ID, ITEM_ID, LOCATION_ID, COGS_PRICE, USER_ID) "
              + " SELECT DISTINCT "
              + " P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]
              + ", P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
              + ", U." + PstUnit.fieldNames[PstUnit.FLD_CODE]
              + ", PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + ", P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
              + ", P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID];
      if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        sql += ", M." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE];
      } else if (srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        sql += ", PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE];
      } else {
        sql += ", 0";
      }
      sql += ", " + srcReportPotitionStock.getUserId();
      sql += " FROM " + PstMaterial.TBL_MATERIAL + " AS M "
              + " INNER JOIN " + PstUnit.TBL_P2_UNIT + " AS U "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]
              + " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
              + " INNER JOIN " + PstProductionProduct.TBL_PRODUCTION_PRODUCT + " AS PP "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]
              + " INNER JOIN " + PstProductionGroup.TBL_PRODUCTION_GROUP + " AS PG "
              + " ON PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]
              + " = PP." + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID]
              + " INNER JOIN " + PstProduction.TBL_PRODUCTION + " AS P "
              + " ON P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]
              + " = PG." + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]
              + " WHERE P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]
              + " BETWEEN '" + Formater.formatDate(srcReportPotitionStock.getDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" + Formater.formatDate(srcReportPotitionStock.getDateTo(), "yyyy-MM-dd 23:59:59") + "'"
              + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;

      if (srcReportPotitionStock.getLocationId() != 0) {
        sql += " AND P." + PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID] + "=" + srcReportPotitionStock.getLocationId();
      }

      if (srcReportPotitionStock.getSupplierId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + "=" + srcReportPotitionStock.getSupplierId();
      }

      if (srcReportPotitionStock.getCategoryId() != 0) {
        sql = sql + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + srcReportPotitionStock.getCategoryId();
      }

      sql += " AND ( P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_CLOSED;
      sql += " OR P." + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_POSTED + ")";

      int i = DBHandler.execSqlInsert(sql);
    } catch (Exception e) {
    }
  }

  /**
   * Fungsi ini digunakan untuk mendapatkan laporan posisi stock. Fungsi ini
   * dipanggil dari fungsi: reportPosisiStock(boolean weekReport, int type,
   * SrcReportPotitionStock srcReportPotitionStock, int language, boolean
   * isZero, int pgStart, int pgNext) create by: gwawan@dimata 13/09/2007
   *
   * @param srcReportPotitionStock SrcReportPotitionStock
   * @param sql String
   */
  public static Vector reportPosisiStockAll(SrcReportPotitionStock srcReportPotitionStock, String sql, int start, int recordToGet, boolean isCalculateZeroQty) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      /**
       * mencari daftar semua stok
       */
      Vector vect = new Vector(1, 1);
      vect = getReportStockAll(srcReportPotitionStock, isCalculateZeroQty, start, recordToGet);

      double stockValueBegin = 0;
      double stockValueOpname = 0;
      double stockValueReceive = 0;
      double stockValueDispatch = 0;
      double stockValueReturn = 0;
      double stockValueSale = 0;
      double stockValueReturnCust = 0;
      double stockValueClosing = 0;

      /**
       * untuk mencari posisi stock
       */
      if (vect != null && vect.size() > 0) {
        for (int k = 0; k < vect.size(); k++) {
          Vector vt = (Vector) vect.get(k);
          Material material = (Material) vt.get(0);
          MaterialStock materialStock = (MaterialStock) vt.get(1);
          Unit unit = (Unit) vt.get(2);

          MaterialStock matStock = new MaterialStock();
          Vector vctTemp = new Vector(1, 1);
          Vector stockValue = new Vector(1, 1);

          stockValueBegin = 0;
          stockValueOpname = 0;
          stockValueReceive = 0;
          stockValueDispatch = 0;
          stockValueReturn = 0;
          stockValueSale = 0;
          stockValueReturnCust = 0;
          stockValueClosing = 0;

          /**
           * get qty opname
           */
          sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE, (QTY_OPNAME * COGS_PRICE) AS STOCK_VALUE_OPNAME FROM " + TBL_MATERIAL_STOCK_REPORT;
          sql += " WHERE MATERIAL_ID = " + material.getOID();
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
          sql += " AND OPNAME_ITEM_ID != 0 ORDER BY TRS_DATE DESC";

          dbrs = DBHandler.execQueryResult(sql);
          ResultSet rs = dbrs.getResultSet();

          Date date = new Date();
          boolean withOpBool = false;
          while (rs.next()) {
            withOpBool = true;
            matStock.setOpnameQty(rs.getDouble("QTY_OPNAME"));
            date = DBHandler.convertDate(rs.getDate("TRS_DATE"), rs.getTime("TRS_DATE"));
            stockValueOpname = rs.getDouble("STOCK_VALUE_OPNAME");
            break;
          }

          /**
           * get qty all (meanggantikan query diatas)
           */
          sql = "SELECT SUM(QTY_RECEIVE) AS RECEIVE, SUM(QTY_RECEIVE * COGS_PRICE) AS STOCK_VALUE_RECEIVE";
          sql += ", SUM(QTY_DISPATCH) AS DISPATCH, SUM(QTY_DISPATCH * COGS_PRICE) AS STOCK_VALUE_DISPATCH";
          sql += ", SUM(QTY_RETURN) AS RETUR, SUM(QTY_RETURN * COGS_PRICE) AS STOCK_VALUE_RETURN";
          sql += ", SUM(QTY_SALE) AS SALE, SUM(QTY_SALE * COGS_PRICE) AS STOCK_VALUE_SALE";
          sql += ", SUM(QTY_RETURN_CUST) AS RETURN_CUST, SUM(QTY_RETURN_CUST * COGS_PRICE) AS STOCK_VALUE_RETURN_CUST";
          sql += " FROM " + TBL_MATERIAL_STOCK_REPORT;
          sql += " WHERE MATERIAL_ID=" + material.getOID();
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
          /**
           * kondisi ini berfungsi untuk membatasi transaksi (receive, dispatch,
           * return, sale) yang harus diproses, dimana hanya transaksi yang
           * terjadi setelah (tanggal dan waktu) proses opname saja yang perlu
           * di proses
           */
          if (withOpBool) {
            sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date, "yyyy-MM-dd") + " " + Formater.formatTimeLocale(date, "kk:mm:ss") + "'";
          }
          sql = sql + " GROUP BY MATERIAL_ID";

          dbrs = DBHandler.execQueryResult(sql);
          rs = dbrs.getResultSet();
          while (rs.next()) {
            matStock.setQtyIn(rs.getDouble("RECEIVE"));
            matStock.setQtyOut(rs.getDouble("DISPATCH"));
            matStock.setQtyMin(rs.getDouble("RETUR"));
            matStock.setSaleQty(rs.getDouble("SALE"));
            matStock.setQtyMax(rs.getDouble("RETURN_CUST"));
            stockValueReceive = rs.getDouble("STOCK_VALUE_RECEIVE");
            stockValueDispatch = rs.getDouble("STOCK_VALUE_DISPATCH");
            stockValueReturn = rs.getDouble("STOCK_VALUE_RETURN");
            stockValueSale = rs.getDouble("STOCK_VALUE_SALE");
            stockValueReturnCust = rs.getDouble("STOCK_VALUE_RETURN_CUST");
          }

          /**
           * get begin stock
           */
          //matStock.setQty(reportQtyAwalPosisiStock(material.getOID(), materialStock.getLocationId()));
          Vector vctBeginQty = getBeginQty(material.getOID(), materialStock.getLocationId(), srcReportPotitionStock.getUserId());
          matStock.setQty(Double.parseDouble((String) vctBeginQty.get(0)));
          stockValueBegin = Double.parseDouble((String) vctBeginQty.get(1));
          /**
           * end get begin stock
           */

          /**
           * get end stock
           */
          if (withOpBool) {
            matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
            stockValueClosing = (stockValueOpname + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale;
          } else {
            matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
            stockValueClosing = (stockValueBegin + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale;
          }

          stockValue.add(STOCK_VALUE_BEGIN, String.valueOf(stockValueBegin));
          stockValue.add(STOCK_VALUE_OPNAME, String.valueOf(stockValueOpname));
          stockValue.add(STOCK_VALUE_RECEIVE, String.valueOf(stockValueReceive));
          stockValue.add(STOCK_VALUE_DISPATCH, String.valueOf(stockValueDispatch));
          stockValue.add(STOCK_VALUE_RETURN, String.valueOf(stockValueReturn));
          stockValue.add(STOCK_VALUE_SALE, String.valueOf(stockValueSale));
          stockValue.add(STOCK_VALUE_RETURN_CUST, String.valueOf(stockValueReturnCust));
          stockValue.add(STOCK_VALUE_CLOSING, String.valueOf(stockValueClosing));

          /**
           * simpan kedalam vector, untuk selanjutnya diolah di JSP
           */
          vctTemp.add(material);
          vctTemp.add(matStock);
          vctTemp.add(unit);
          vctTemp.add(stockValue);
          list.add(vctTemp);
        }
      }

    } catch (Exception e) {
      System.out.println("Exc. in reportPosisiStockAll(#,#) : " + e.toString());
    }
    return list;
  }

  /**
   * Fungsi ini digunakan untuk mendapatkan laporan posisi stock. Fungsi ini
   * dipanggil dari fungsi: reportPosisiStock(boolean weekReport, int type,
   * SrcReportPotitionStock srcReportPotitionStock, int language, boolean
   * isZero, int pgStart, int pgNext) create by: gwawan@dimata 13/09/2007
   * Modified by Mirah@dimata 22/02/2011 tambah parameter jspWriter
   *
   * @param srcReportPotitionStock SrcReportPotitionStock
   * @param sql String
   */
  public static void reportPosisiStockAll(JspWriter out, SrcReportPotitionStock srcReportPotitionStock, String sql, int start, int recordToGet, boolean isCalculateZeroQty) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      /**
       * mencari daftar semua stok
       */
      Vector vect = new Vector(1, 1);
      //vect = getReportStockAll(out,srcReportPotitionStock, isCalculateZeroQty, start, recordToGet);

      double stockValueBegin = 0;
      double stockValueOpname = 0;
      double stockValueReceive = 0;
      double stockValueDispatch = 0;
      double stockValueReturn = 0;
      double stockValueSale = 0;
      double stockValueReturnCust = 0;
      double stockValueClosing = 0;

      /**
       * untuk mencari posisi stock
       */
      if (vect != null && vect.size() > 0) {
        for (int k = 0; k < vect.size(); k++) {
          Vector vt = (Vector) vect.get(k);
          Material material = (Material) vt.get(0);
          MaterialStock materialStock = (MaterialStock) vt.get(1);
          Unit unit = (Unit) vt.get(2);

          MaterialStock matStock = new MaterialStock();
          Vector vctTemp = new Vector(1, 1);
          Vector stockValue = new Vector(1, 1);

          stockValueBegin = 0;
          stockValueOpname = 0;
          stockValueReceive = 0;
          stockValueDispatch = 0;
          stockValueReturn = 0;
          stockValueSale = 0;
          stockValueReturnCust = 0;
          stockValueClosing = 0;

          /**
           * get qty opname
           */
          sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE, (QTY_OPNAME * COGS_PRICE) AS STOCK_VALUE_OPNAME FROM " + TBL_MATERIAL_STOCK_REPORT;
          sql += " WHERE MATERIAL_ID = " + material.getOID();
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
          sql += " AND OPNAME_ITEM_ID != 0 ORDER BY TRS_DATE DESC";

          dbrs = DBHandler.execQueryResult(sql);
          ResultSet rs = dbrs.getResultSet();

          Date date = new Date();
          boolean withOpBool = false;
          while (rs.next()) {
            withOpBool = true;
            matStock.setOpnameQty(rs.getDouble("QTY_OPNAME"));
            date = DBHandler.convertDate(rs.getDate("TRS_DATE"), rs.getTime("TRS_DATE"));
            stockValueOpname = rs.getDouble("STOCK_VALUE_OPNAME");
            break;
          }

          /**
           * get qty all (meanggantikan query diatas)
           */
          sql = "SELECT SUM(QTY_RECEIVE) AS RECEIVE, SUM(QTY_RECEIVE * COGS_PRICE) AS STOCK_VALUE_RECEIVE";
          sql += ", SUM(QTY_DISPATCH) AS DISPATCH, SUM(QTY_DISPATCH * COGS_PRICE) AS STOCK_VALUE_DISPATCH";
          sql += ", SUM(QTY_RETURN) AS RETUR, SUM(QTY_RETURN * COGS_PRICE) AS STOCK_VALUE_RETURN";
          sql += ", SUM(QTY_SALE) AS SALE, SUM(QTY_SALE * COGS_PRICE) AS STOCK_VALUE_SALE";
          sql += ", SUM(QTY_RETURN_CUST) AS RETURN_CUST, SUM(QTY_RETURN_CUST * COGS_PRICE) AS STOCK_VALUE_RETURN_CUST";
          sql += " FROM " + TBL_MATERIAL_STOCK_REPORT;
          sql += " WHERE MATERIAL_ID=" + material.getOID();
          sql += " AND LOCATION_ID = " + materialStock.getLocationId();
          sql += " AND USER_ID = " + srcReportPotitionStock.getUserId();
          /**
           * kondisi ini berfungsi untuk membatasi transaksi (receive, dispatch,
           * return, sale) yang harus diproses, dimana hanya transaksi yang
           * terjadi setelah (tanggal dan waktu) proses opname saja yang perlu
           * di proses
           */
          if (withOpBool) {
            sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date, "yyyy-MM-dd") + " " + Formater.formatTimeLocale(date, "kk:mm:ss") + "'";
          }
          sql = sql + " GROUP BY MATERIAL_ID";

          dbrs = DBHandler.execQueryResult(sql);
          rs = dbrs.getResultSet();
          while (rs.next()) {
            matStock.setQtyIn(rs.getDouble("RECEIVE"));
            matStock.setQtyOut(rs.getDouble("DISPATCH"));
            matStock.setQtyMin(rs.getDouble("RETUR"));
            matStock.setSaleQty(rs.getDouble("SALE"));
            matStock.setQtyMax(rs.getDouble("RETURN_CUST"));
            stockValueReceive = rs.getDouble("STOCK_VALUE_RECEIVE");
            stockValueDispatch = rs.getDouble("STOCK_VALUE_DISPATCH");
            stockValueReturn = rs.getDouble("STOCK_VALUE_RETURN");
            stockValueSale = rs.getDouble("STOCK_VALUE_SALE");
            stockValueReturnCust = rs.getDouble("STOCK_VALUE_RETURN_CUST");
          }

          /**
           * get begin stock
           */
          //matStock.setQty(reportQtyAwalPosisiStock(material.getOID(), materialStock.getLocationId()));
          Vector vctBeginQty = getBeginQty(material.getOID(), materialStock.getLocationId(), srcReportPotitionStock.getUserId());
          matStock.setQty(Double.parseDouble((String) vctBeginQty.get(0)));
          stockValueBegin = Double.parseDouble((String) vctBeginQty.get(1));
          /**
           * end get begin stock
           */

          /**
           * get end stock
           */
          if (withOpBool) {
            matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
            stockValueClosing = (stockValueOpname + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale;
          } else {
            matStock.setClosingQty((matStock.getQtyIn() + matStock.getQty() + matStock.getQtyMax()) - (matStock.getQtyOut() + matStock.getQtyMin()) - matStock.getSaleQty());
            stockValueClosing = (stockValueBegin + stockValueReceive + stockValueReturnCust) - (stockValueDispatch + stockValueReturn) - stockValueSale;
          }

          stockValue.add(STOCK_VALUE_BEGIN, String.valueOf(stockValueBegin));
          stockValue.add(STOCK_VALUE_OPNAME, String.valueOf(stockValueOpname));
          stockValue.add(STOCK_VALUE_RECEIVE, String.valueOf(stockValueReceive));
          stockValue.add(STOCK_VALUE_DISPATCH, String.valueOf(stockValueDispatch));
          stockValue.add(STOCK_VALUE_RETURN, String.valueOf(stockValueReturn));
          stockValue.add(STOCK_VALUE_SALE, String.valueOf(stockValueSale));
          stockValue.add(STOCK_VALUE_RETURN_CUST, String.valueOf(stockValueReturnCust));
          stockValue.add(STOCK_VALUE_CLOSING, String.valueOf(stockValueClosing));

          /**
           * simpan kedalam vector, untuk selanjutnya diolah di JSP
           */
          vctTemp.add(material);
          vctTemp.add(matStock);
          vctTemp.add(unit);
          vctTemp.add(stockValue);
          list.add(vctTemp);
        }
      }

    } catch (Exception e) {
      System.out.println("Exc. in reportPosisiStockAll(#,#) : " + e.toString());
    }
    return;
  }

  /**
   * add opie-eyek biar bisa di cari pakai cogs apa nilai jual
   *
   * @param oidMaterial
   * @param oidLocation
   * @param oidUser
   * @return
   */
  public static Vector getBeginQty(long oidMaterial, long oidLocation, long oidUser) {
    return getBeginQty(oidMaterial, oidLocation, oidUser, "COGS_PRICE ");
  }

  /**
   * Fungsi ini digunakan untuk mendapatkan qty awal untuk laporan posisi stock
   *
   * @param long OID dari material yang akan dicari stock awalnya
   * @param long Merupakan OID location tempat material berada
   * @return Vector
   * @created gwawan@dimata 2008-01-29
   */
  public static Vector getBeginQty(long oidMaterial, long oidLocation, long oidUser, String cogs) {
    DBResultSet dbrs = null;
    MaterialStock matStock = new MaterialStock();
    double stockValueOpname = 0;
    double stockValueReceive = 0;
    double stockValueDispatch = 0;
    double stockValueReturn = 0;
    double stockValueSale = 0;
    double stockValueReturnCust = 0;
    double stockValueCost = 0;
    double stockValueClosing = 0;
    Vector result = new Vector(1, 1);

    try {
      Date date = new Date();
      boolean withOpBool = false;

      /**
       * get qty opname
       */
      String sql = "SELECT QTY_OPNAME,OPNAME_ITEM_ID,TRS_DATE, (QTY_OPNAME * " + cogs + ") AS STOCK_VALUE_OPNAME";
      sql += " FROM " + TBL_MATERIAL_STOCK_REPORT_HIS;
      sql += " WHERE MATERIAL_ID = " + oidMaterial;
      sql += " AND LOCATION_ID = " + oidLocation;
      sql += " AND USER_ID = " + oidUser;
      sql += " AND OPNAME_ITEM_ID != 0 ORDER BY TRS_DATE DESC";

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      while (rs.next()) {
        withOpBool = true;
        matStock.setOpnameQty(rs.getDouble("QTY_OPNAME"));
        date = DBHandler.convertDate(rs.getDate("TRS_DATE"), rs.getTime("TRS_DATE"));
        stockValueOpname = rs.getDouble("STOCK_VALUE_OPNAME");
        break;
      }

      /**
       * get qty all
       */
      sql = "SELECT SUM(QTY_RECEIVE) AS RECEIVE, SUM(QTY_RECEIVE * " + cogs + ") AS STOCK_VALUE_RECEIVE";
      sql += ", SUM(QTY_DISPATCH) AS DISPATCH, SUM(QTY_DISPATCH * " + cogs + ") AS STOCK_VALUE_DISPATCH";
      sql += ", SUM(QTY_RETURN) AS RETUR, SUM(QTY_RETURN * " + cogs + ") AS STOCK_VALUE_RETURN";
      sql += ", SUM(QTY_SALE) AS SALE, SUM(QTY_SALE * " + cogs + ") AS STOCK_VALUE_SALE";
      sql += ", SUM(QTY_RETURN_CUST) AS RETURN_CUST, SUM(QTY_RETURN_CUST * " + cogs + ") AS STOCK_VALUE_RETURN_CUST";
      sql += ", SUM(QTY_COST) AS COST, SUM(QTY_COST * " + cogs + ") AS STOCK_VALUE_COST";
      sql += " FROM " + TBL_MATERIAL_STOCK_REPORT_HIS;
      sql += " WHERE MATERIAL_ID=" + oidMaterial;
      sql += " AND LOCATION_ID = " + oidLocation;
      sql += " AND USER_ID = " + oidUser;
      if (withOpBool) {
        sql = sql + " AND TRS_DATE > '" + Formater.formatDate(date, "yyyy-MM-dd") + " " + Formater.formatTimeLocale(date, "kk:mm:ss") + "'";
      }
      sql = sql + " GROUP BY MATERIAL_ID";

      dbrs = DBHandler.execQueryResult(sql);
      rs = dbrs.getResultSet();
      while (rs.next()) {
        matStock.setQtyIn(rs.getDouble("RECEIVE"));
        matStock.setQtyOut(rs.getDouble("DISPATCH"));
        matStock.setQtyMin(rs.getDouble("RETUR"));
        matStock.setSaleQty(rs.getDouble("SALE"));
        matStock.setQtyMax(rs.getDouble("RETURN_CUST"));
        matStock.setQtyCost(rs.getDouble("COST"));

        stockValueReceive = rs.getDouble("STOCK_VALUE_RECEIVE");
        stockValueDispatch = rs.getDouble("STOCK_VALUE_DISPATCH");
        stockValueReturn = rs.getDouble("STOCK_VALUE_RETURN");
        stockValueSale = rs.getDouble("STOCK_VALUE_SALE");
        stockValueReturnCust = rs.getDouble("STOCK_VALUE_RETURN_CUST");
        stockValueCost = rs.getDouble("STOCK_VALUE_COST");
      }

      /**
       * get end qty
       */
      if (withOpBool) {
        matStock.setClosingQty((matStock.getOpnameQty() + matStock.getQtyIn() + matStock.getQtyMax()) - matStock.getQtyOut() - matStock.getQtyMin() - matStock.getSaleQty() - matStock.getQtyCost());
        stockValueClosing = stockValueOpname + stockValueReceive + stockValueReturnCust - stockValueDispatch - stockValueReturn - stockValueSale - stockValueCost;
      } else {
        matStock.setClosingQty(matStock.getQtyIn() + matStock.getQtyMax() - matStock.getQtyOut() - matStock.getQtyMin() - matStock.getSaleQty() - matStock.getQtyCost());
        stockValueClosing = stockValueReceive + stockValueReturnCust - stockValueDispatch - stockValueReturn - stockValueSale - stockValueCost;
      }

      result.add(String.valueOf(matStock.getClosingQty()));
      result.add(String.valueOf(stockValueClosing));

    } catch (Exception e) {
      System.out.println("Exc. in SessReportStock.getBeginQty(#,#) : " + e.toString());
    }

    return result;
  }

  public static Vector listReportStock(JspWriter out, SrcReportPotitionStock srcReportPotitionStock, int limitStart, int recordToGet, String whereClause, String order, String cellStyle, int groupBy) {
    Vector lists = new Vector();
    DBResultSet dbrs = null;
    try {
      String sql = "";
      sql = "SELECT M." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", MK." + PstMerk.fieldNames[PstMerk.FLD_NAME]
              + ", SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]
              + ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]
              + ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN]
              + ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX]
              + ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QT_OPTIMUM]
              + ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN]
              + ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]
              + ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY]
              + ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY]
              + ", C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]
              + ", C." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS CAT_NAME "
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] 
              + ", M." + PstMaterial.fieldNames[PstMaterial.FLD_EXPIRED_DATE];

      sql   +=  " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " AS MS "
              + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
              + " LEFT JOIN " + PstMerk.TBL_MAT_MERK + " AS MK "
              + " ON MK." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID]
              + " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID]
              + " LEFT JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS SC "
              + " ON SC." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]
              + " = M." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID]
              + " LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " = M." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]
              + " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C "
              + " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]
              + " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
      if (whereClause != null && whereClause.length() > 0) {
        sql = sql + " WHERE " + whereClause;
      }
      if (order != null && order.length() > 0) {
        sql = sql + " ORDER BY " + order;
      }
      if (limitStart == 0 && recordToGet == 0) {
        sql = sql + "";
      } else {
        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
      }

      System.out.println("List position : " + sql);
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();

      double stockValueBegin = 0;
      double stockValueOpname = 0;
      double stockValueReceive = 0;
      double stockValueDispatch = 0;
      double stockValueReturn = 0;
      double stockValueSale = 0;
      double stockValueReturnCust = 0;
      double stockValueCost = 0;
      double stockValueClosing = 0;

      double subTotalStockValueBegin = 0;
      double subTotalStockValueOpname = 0;
      double subTotalStockValueReceive = 0;
      double subTotalStockValueDispatch = 0;
      double subTotalStockValueReturn = 0;
      double subTotalStockValueSale = 0;
      double subTotalStockValueReturnCust = 0;
      double subTotalStockValueCost = 0;
      double subTotalStockValueClosing = 0;
      double subTotalStockValueProductionCost = 0;
      double subTotalStockValueProductionProduct = 0;
      PstUnit.loadQtyPerBaseUnitByHash();
      long oidSupplier = 0;
      boolean headerSupplier = false;

      double subtotalgroupdaily = 0;
      double subtotalAwalDaily = 0;
      double subtotalOpnameDaily = 0;
      double subtotalTerimaDaily = 0;
      double subtotalReturnSuppDaily = 0;
      double subtotalJualDaily = 0;
      double subtotalReturnCustDaily = 0;
      double subtotalSaldoDaily = 0;

      double grandtotalAwalDaily = 0;
      double grandtotalOpnameDaily = 0;
      double grandtotalTerimaDaily = 0;
      double grandtotalReturnSuppDaily = 0;
      double grandtotalJualDaily = 0;
      double grandtotalReturnCustDaily = 0;
      double grandtotalSaldoDaily = 0;

      double subtotalGroup = 0;
      double subtotalAwal = 0;
      double subtotalOpname = 0;
      double subtotalTerima = 0;
      double subtotalReturnSupp = 0;
      double subtotalJual = 0;
      double subtotalReturnCust = 0;
      double subtotalSaldo = 0;

      int loop = 0;
      while (rs.next()) {
        loop = loop + 1;
        Vector temp = new Vector();
        Material material = new Material();
        Merk merk = new Merk();
        SubCategory subCategory = new SubCategory();
        MaterialStock materialStock = new MaterialStock();
        ContactList contactList = new ContactList();
        Category category = new Category();
        Vector stockValue = new Vector(1, 1);

        material.setSku(rs.getString(1));
        material.setBarcode(rs.getString(2));
        material.setName(rs.getString(3));
        temp.add(material);

        merk.setName(rs.getString(4));
        temp.add(merk);

        subCategory.setName(rs.getString(5));
        temp.add(subCategory);

        materialStock.setQty(rs.getDouble(6));
        materialStock.setQtyIn(rs.getDouble(7));
        materialStock.setQtyMax(rs.getDouble(8));
        materialStock.setSaleQty(rs.getDouble(9));
        materialStock.setQtyMin(rs.getDouble(10));
        materialStock.setOpnameQty(rs.getDouble(11));
        materialStock.setClosingQty(rs.getDouble(12));
        temp.add(materialStock);
        
        contactList.setCompName(rs.getString(13));
        temp.add(contactList) ;
        
        category.setName(rs.getString(14));
        temp.add(category);
        

        if (groupBy ==  SrcSaleReport.GROUP_BY_SUPPLIER) {
          contactList.setOID(rs.getLong("CONTACT_ID"));
          contactList.setCompName(rs.getString("COMP_NAME"));
          if (oidSupplier != contactList.getOID()) {
            headerSupplier = true;
          } else {
            headerSupplier = false;
            //loop=1;
          }
          oidSupplier = contactList.getOID();
        }

        if (groupBy == SrcSaleReport.GROUP_BY_CATEGORY) {
          category.setOID(rs.getLong("CATEGORY_ID"));
          category.setName(rs.getString("CAT_NAME"));
          if (oidSupplier != category.getOID()) {
            headerSupplier = true;
          } else {
            headerSupplier = false;
            //loop=1;
          } 
          oidSupplier = category.getOID();
        }

        lists.add(temp);

      }
      rs.close();
      return lists;
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      DBResultSet.close(dbrs);
    }
    return new Vector();
  }
  public static Vector getStockPosition(int limitStart, int recordToGet, String whereClause, String order, Date periodeStart, Date periodeEnd, String tglAwal, String tglAkhir, long oidLocation){
        Vector lists = new Vector();
        DBResultSet dbrs = null;
    try {
      String sql ="";
      sql = "SELECT mat."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]
              + ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", mk." + PstMerk.fieldNames[PstMerk.FLD_NAME]
              + ", sc." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]
              + ", SUM(COALESCE(Awal, 0)) AS Awal"
              + ", SUM(COALESCE(Pembiayaan, 0)) AS Pembiayaan"
              + ", SUM(COALESCE(Pengiriman, 0)) AS Pengiriman"
              + ", SUM(COALESCE(Penerimaan, 0)) AS Penerimaan"
              + ", SUM(COALESCE(Pengembalian, 0)) AS Pengembalian"
              + ", SUM(COALESCE(Penjualan, 0)) AS Penjualan"
              + ", SUM(COALESCE(Exchange, 0)) AS Exchange"
              + ", SUM(COALESCE(Adjust, 0)) AS Adjust"
//              + ", ( SUM(COALESCE(Pembiayaan, 0)) - SUM(COALESCE(Pengiriman, 0)) - SUM(COALESCE(Penjualan, 0)) + SUM(COALESCE(Penerimaan, 0)) + SUM(COALESCE(Pengembalian, 0)) + SUM(COALESCE(Adjust, 0)) ) AS Saldo  "
              + ", cat.`NAME`" 
              + ", con.`COMP_NAME`"
              + ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " FROM (SELECT "
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    SUM(costi.`QTY`) AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_costing_material_item` AS costi"
              + "      ON costi.`MATERIAL_ID` = m.`MATERIAL_ID`"
              + "    LEFT JOIN `pos_costing_material` AS cost"
              + "      ON cost.`COSTING_MATERIAL_ID` = costi.`COSTING_MATERIAL_ID`  "
              + "WHERE cost.`COSTING_DATE` BETWEEN '"+tglAwal+"' AND '"+tglAkhir+"'"
              + "    AND (`COSTING_STATUS` = 5 OR `COSTING_STATUS` = 7)"
              + "    AND cost.`LOCATION_ID` = "+oidLocation
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    SUM(dis.`QTY`) AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_dispatch_material_item` AS dis"
              + "      ON dis.`MATERIAL_ID` = m.`MATERIAL_ID`"
              + "    LEFT JOIN `pos_dispatch_material` AS dispatch"
              + "      ON `dispatch`.`DISPATCH_MATERIAL_ID` = dis.`DISPATCH_MATERIAL_ID`  "
              + " WHERE dispatch.`DISPATCH_DATE` BETWEEN '"+tglAwal+"' AND '"+tglAkhir+"'" 
              + "   AND (`DISPATCH_STATUS` = 5 OR `DISPATCH_STATUS` = 7)"
              + "    AND dispatch.`LOCATION_ID` = "+oidLocation
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    SUM(rec.QTY) AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_receive_material_item` AS rec"
              + "      ON rec.`MATERIAL_ID` = m.`MATERIAL_ID`"
              + "    LEFT JOIN `pos_receive_material` AS receive"
              + "      ON receive.`RECEIVE_MATERIAL_ID` = rec.`RECEIVE_MATERIAL_ID`  "
              + " WHERE receive.`RECEIVE_DATE` BETWEEN '"+tglAwal+"'  AND '"+tglAkhir+"'" 
              + "  AND (`RECEIVE_STATUS` = 5 OR `RECEIVE_STATUS` = 7)"
              + "    AND receive.`LOCATION_ID` = "+oidLocation
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    SUM(ret.QTY) AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_return_material_item` AS ret"
              + "      ON ret.MATERIAL_ID = m.MATERIAL_ID"
              + "    LEFT JOIN `pos_return_material` AS retur"
              + "      ON retur.`RETURN_MATERIAL_ID` = ret.`RETURN_MATERIAL_ID`  "
              + " WHERE retur.`RETURN_DATE` BETWEEN '"+tglAwal+"'  AND '"+tglAkhir+"'" 
              + "    AND (`RETURN_STATUS` = 5 OR `RETURN_STATUS` = 7)"
              + "    AND retur.`LOCATION_ID` = "+oidLocation
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    SUM(bd.`QTY`) AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `cash_bill_detail` AS bd"
              + "      ON bd.MATERIAL_ID = m.`MATERIAL_ID`"
              + "    LEFT JOIN `cash_bill_main` AS bm"
              + "      ON bm.`CASH_BILL_MAIN_ID` = bd.`CASH_BILL_MAIN_ID`  "
              + " WHERE bm.`BILL_DATE` BETWEEN '"+tglAwal+"'  AND '"+tglAkhir+"'" 
              + " AND (`BILL_STATUS` = 5 OR `BILL_STATUS` = 7)"
              + "    AND bm.`LOCATION_ID` = "+oidLocation
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    SUM(bd.`QTY`) AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `cash_bill_detail` AS bd"
              + "      ON bd.MATERIAL_ID = m.`MATERIAL_ID`"
              + "    LEFT JOIN `cash_bill_main` AS bm"
              + "      ON bm.`CASH_BILL_MAIN_ID` = bd.`CASH_BILL_MAIN_ID`  "
              + " WHERE bm.`BILL_DATE` BETWEEN '"+tglAwal+"'  AND '"+tglAkhir+"'" 
              + " AND (`BILL_STATUS` = 5 OR `BILL_STATUS` = 7)    "
              + " AND `DOC_TYPE` = 1"
              + " AND `TRANSACTION_STATUS` = 0"
              + " AND `TRANSACTION_TYPE` = 0"
              + "    AND bm.`LOCATION_ID` = "+oidLocation
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",   (SUM(op.`QTY_OPNAME`) - SUM(op.`QTY_SYSTEM`)) AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_stock_opname_item` AS op"
              + "      ON op.MATERIAL_ID = m.`MATERIAL_ID`"
              + "    LEFT JOIN `pos_stock_opname` AS opname"
              + "      ON opname.`STOCK_OPNAME_ID` = op.`STOCK_OPNAME_ID`  "
              + " WHERE opname.`STOCK_OPNAME_DATE` BETWEEN '"+tglAwal+"'  AND '"+tglAkhir+"'" 
              + "    AND (`STOCK_OPNAME_STATUS` = 5 OR `STOCK_OPNAME_STATUS` = 7)" 
              + "    AND opname.`LOCATION_ID` = "+oidLocation
              + "  GROUP BY m.`MATERIAL_ID`) AS final"
              + "      JOIN `pos_material` AS mat"
              + "      ON mat.`MATERIAL_ID` = final.MATERIAL_ID"
              + "      LEFT JOIN `pos_merk` AS mk"
              + "      ON mk.`MERK_ID` = mat.`MERK_ID`"
              + "      LEFT JOIN `pos_sub_category` AS sc"
              + "      ON sc.`SUB_CATEGORY_ID` = mat.`SUB_CATEGORY_ID`"
              + "      LEFT JOIN `pos_category` AS cat"
              + "      ON cat.`CATEGORY_ID` = mat.`CATEGORY_ID`"
              + "      LEFT JOIN `contact_list` AS con"
              + "      ON con.`CONTACT_ID` = mat.`SUPPLIER_ID`";
        if (whereClause != null && whereClause.length() > 0) {
          sql = sql + " WHERE " + whereClause;
        }
              sql += " GROUP BY final.MATERIAL_ID ";
        if (order != null && order.length() > 0) {
          sql = sql + " ORDER BY " + order;
        }
        if (limitStart == 0 && recordToGet == 0) {
          sql = sql + "";
        } else {
          sql = sql + " LIMIT " + limitStart + "," + recordToGet;
        }
      

      System.out.println("List Stock Position : " + sql);
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      while (rs.next()) {
        Vector data = new Vector();
        Material mat = new Material();
        Merk merk = new Merk();
        SubCategory subCategory = new SubCategory();
        MaterialStock matStock = new MaterialStock();
        Category ca = new Category();
        ContactList con = new ContactList();
        
        mat.setSku(rs.getString(1));
        mat.setBarCode(rs.getString(2));
        mat.setName(rs.getString(3));
        mat.setOID(rs.getLong(16));
        data.add(mat);

        merk.setName(rs.getString(4));
        data.add(merk);

        subCategory.setName(rs.getString(5));
        data.add(subCategory);

        matStock.setQty(rs.getDouble(6)); //Awal
        matStock.setQtyMax(rs.getDouble(7));//Pembiayaan
        matStock.setOpnameQty(rs.getDouble(8));//Pengiriman
        matStock.setQtyIn(rs.getDouble(9));//Penerimaan
        matStock.setQtyMin(rs.getDouble(10));//Pengembalian
        matStock.setSaleQty(rs.getDouble(11));//Penjualan
        matStock.setOpeningQty(rs.getDouble(12));//Exchange
        matStock.setQtyOptimum(rs.getDouble(13));//Adsj
//        matStock.setClosingQty(rs.getDouble(14));//saldo
        data.add(matStock);
        
        ca.setName(rs.getString(14));
        data.add(ca);
        
        con.setCompName(rs.getString(15));
        data.add(con);
        
        lists.add(data);
      }
      rs.close();
      return lists;
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      DBResultSet.close(dbrs);
    }
    return new Vector();
  }
  public static Vector getStockAwal(int limitStart, int recordToGet, String whereClause, String order, Date periodeStart, Date periodeEnd, String tglAwal, String tglAkhir, long oid){
        Vector lists = new Vector();
        DBResultSet dbrs = null;
    try {
      String sql ="";
      sql = "SELECT mat."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]
              + ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]
              + ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
              + ", mk." + PstMerk.fieldNames[PstMerk.FLD_NAME]
              + ", sc." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]
              + ", ( SUM(COALESCE(Pembiayaan, 0)) - SUM(COALESCE(Pengiriman, 0)) - SUM(COALESCE(Penjualan, 0)) + SUM(COALESCE(Penerimaan, 0)) + SUM(COALESCE(Pengembalian, 0)) + SUM(COALESCE(Adjust, 0)) + SUM(COALESCE(Exchange, 0)) ) AS Awal"
              + ", SUM(COALESCE(Pembiayaan, 0)) AS Pembiayaan"
              + ", SUM(COALESCE(Pengiriman, 0)) AS Pengiriman"
              + ", SUM(COALESCE(Penerimaan, 0)) AS Penerimaan"
              + ", SUM(COALESCE(Pengembalian, 0)) AS Pengembalian"
              + ", SUM(COALESCE(Penjualan, 0)) AS Penjualan"
              + ", SUM(COALESCE(Exchange, 0)) AS Exchange"
              + ", SUM(COALESCE(Adjust, 0)) AS Adjust"
              + ", ( SUM(COALESCE(Pembiayaan, 0)) - SUM(COALESCE(Pengiriman, 0)) - SUM(COALESCE(Penjualan, 0)) + SUM(COALESCE(Penerimaan, 0)) + SUM(COALESCE(Pengembalian, 0)) + SUM(COALESCE(Adjust, 0)) ) AS Saldo"
              + ", cat.`NAME`" 
              + ", con.`COMP_NAME`"
              + ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + " FROM (SELECT "
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    SUM(costi.`QTY`) AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_costing_material_item` AS costi"
              + "      ON costi.`MATERIAL_ID` = m.`MATERIAL_ID`"
              + "    LEFT JOIN `pos_costing_material` AS cost"
              + "      ON cost.`COSTING_MATERIAL_ID` = costi.`COSTING_MATERIAL_ID`  "
              + "WHERE cost.`COSTING_DATE` BETWEEN '"+periodeStart+"' AND '"+periodeEnd+"'"
              + "    AND (`COSTING_STATUS` = 5 OR `COSTING_STATUS` = 7)    "
              + " AND m.`MATERIAL_ID` = "+oid+""
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    SUM(dis.`QTY`) AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_dispatch_material_item` AS dis"
              + "      ON dis.`MATERIAL_ID` = m.`MATERIAL_ID`"
              + "    LEFT JOIN `pos_dispatch_material` AS dispatch"
              + "      ON `dispatch`.`DISPATCH_MATERIAL_ID` = dis.`DISPATCH_MATERIAL_ID`  "
              + " WHERE dispatch.`DISPATCH_DATE` BETWEEN '"+periodeStart+"' AND '"+periodeEnd+"'" 
              + "   AND (`DISPATCH_STATUS` = 5 OR `DISPATCH_STATUS` = 7)    "
              + " AND m.`MATERIAL_ID` = "+oid+""
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    SUM(rec.QTY) AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_receive_material_item` AS rec"
              + "      ON rec.`MATERIAL_ID` = m.`MATERIAL_ID`"
              + "    LEFT JOIN `pos_receive_material` AS receive"
              + "      ON receive.`RECEIVE_MATERIAL_ID` = rec.`RECEIVE_MATERIAL_ID`  "
              + " WHERE receive.`RECEIVE_DATE` BETWEEN '"+periodeStart+"'  AND '"+periodeEnd+"'" 
              + "  AND (`RECEIVE_STATUS` = 5 OR `RECEIVE_STATUS` = 7)  "
              + "  AND m.`MATERIAL_ID` = "+oid+""
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    SUM(ret.QTY) AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_return_material_item` AS ret"
              + "      ON ret.MATERIAL_ID = m.MATERIAL_ID"
              + "    LEFT JOIN `pos_return_material` AS retur"
              + "      ON retur.`RETURN_MATERIAL_ID` = ret.`RETURN_MATERIAL_ID`  "
              + " WHERE retur.`RETURN_DATE` BETWEEN '"+periodeStart+"'  AND '"+periodeEnd+"'" 
              + "    AND (`RETURN_STATUS` = 5 OR `RETURN_STATUS` = 7)  "
              + "  AND m.`MATERIAL_ID` = "+oid+""
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    SUM(bd.`QTY`) AS Penjualan"
              + ",    0 AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `cash_bill_detail` AS bd"
              + "      ON bd.MATERIAL_ID = m.`MATERIAL_ID`"
              + "    LEFT JOIN `cash_bill_main` AS bm"
              + "      ON bm.`CASH_BILL_MAIN_ID` = bd.`CASH_BILL_MAIN_ID`  "
              + " WHERE bm.`BILL_DATE` BETWEEN '"+periodeStart+"'  AND '"+periodeEnd+"'" 
              + " AND (`BILL_STATUS` = 5 OR `BILL_STATUS` = 7)  "
              + "  AND m.`MATERIAL_ID` = "+oid+""
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    SUM(bd.`QTY`) AS Exchange"
              + ",    0 AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `cash_bill_detail` AS bd"
              + "      ON bd.MATERIAL_ID = m.`MATERIAL_ID`"
              + "    LEFT JOIN `cash_bill_main` AS bm"
              + "      ON bm.`CASH_BILL_MAIN_ID` = bd.`CASH_BILL_MAIN_ID`  "
              + " WHERE bm.`BILL_DATE` BETWEEN '"+periodeStart+"'  AND '"+periodeEnd+"'" 
              + " AND (`BILL_STATUS` = 5 OR `BILL_STATUS` = 7)"
              + "  AND m.`MATERIAL_ID` = "+oid+""
              + " AND `DOC_TYPE` = 1"
              + " AND `TRANSACTION_STATUS` = 0"
              + " AND `TRANSACTION_TYPE` = 0"
              + "  GROUP BY m.`MATERIAL_ID`"
              + "  UNION"
              + "  SELECT"
              + "    m.`MATERIAL_ID`"
              + ",    0 AS Awal"
              + ",    0 AS Pembiayaan"
              + ",    0 AS Pengiriman"
              + ",    0 AS Penerimaan"
              + ",    0 AS Pengembalian"
              + ",    0 AS Penjualan"
              + ",    0 AS Exchange"
              + ",   (SUM(op.`QTY_OPNAME`) - SUM(op.`QTY_SYSTEM`)) AS Adjust"
              + "  FROM"
              + "    `pos_material` AS m"
              + "    LEFT JOIN `pos_stock_opname_item` AS op"
              + "      ON op.MATERIAL_ID = m.`MATERIAL_ID`"
              + "    LEFT JOIN `pos_stock_opname` AS opname"
              + "      ON opname.`STOCK_OPNAME_ID` = op.`STOCK_OPNAME_ID`  "
              + " WHERE opname.`STOCK_OPNAME_DATE` BETWEEN '"+periodeStart+"'  AND '"+periodeEnd+"'" 
              + "    AND (`STOCK_OPNAME_STATUS` = 5 OR `STOCK_OPNAME_STATUS` = 7)   "
              + "  AND m.`MATERIAL_ID` = "+oid+"" 
              + "  GROUP BY m.`MATERIAL_ID`) AS final"
              + "      JOIN `pos_material` AS mat"
              + "      ON mat.`MATERIAL_ID` = final.MATERIAL_ID"
              + "      LEFT JOIN `pos_merk` AS mk"
              + "      ON mk.`MERK_ID` = mat.`MERK_ID`"
              + "      LEFT JOIN `pos_sub_category` AS sc"
              + "      ON sc.`SUB_CATEGORY_ID` = mat.`SUB_CATEGORY_ID`"
              + "      LEFT JOIN `pos_category` AS cat"
              + "      ON cat.`CATEGORY_ID` = mat.`CATEGORY_ID`"
              + "      LEFT JOIN `contact_list` AS con"
              + "      ON con.`CONTACT_ID` = mat.`SUPPLIER_ID`";
        if (whereClause != null && whereClause.length() > 0) {
          sql = sql + " WHERE " + whereClause;
        }
              sql += " GROUP BY final.MATERIAL_ID ";
        if (order != null && order.length() > 0) {
          sql = sql + " ORDER BY " + order;
        }
        if (limitStart == 0 && recordToGet == 0) {
          sql = sql + "";
        } else {
          sql = sql + " LIMIT " + limitStart + "," + recordToGet;
        }
      
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      while (rs.next()) {
        Vector data = new Vector();
        Material mat = new Material();
        Merk merk = new Merk();
        SubCategory subCategory = new SubCategory();
        MaterialStock matStock = new MaterialStock();
        Category ca = new Category();
        ContactList con = new ContactList();
        
        mat.setSku(rs.getString(1));
        mat.setBarCode(rs.getString(2));
        mat.setName(rs.getString(3));
        mat.setOID(rs.getLong(17));
        data.add(mat);

        merk.setName(rs.getString(4));
        data.add(merk);

        subCategory.setName(rs.getString(5));
        data.add(subCategory);

        matStock.setQty(rs.getDouble(6)); //Awal
        matStock.setQtyMax(rs.getDouble(7));//Pembiayaan
        matStock.setOpnameQty(rs.getDouble(8));//Pengiriman
        matStock.setQtyIn(rs.getDouble(9));//Penerimaan
        matStock.setQtyMin(rs.getDouble(10));//Pengembalian
        matStock.setSaleQty(rs.getDouble(11));//Penjualan
        matStock.setOpeningQty(rs.getDouble(12));//Exchange
        matStock.setQtyOptimum(rs.getDouble(13));//Adsj
        matStock.setClosingQty(rs.getDouble(14));//saldo
        data.add(matStock);
        
        ca.setName(rs.getString(15));
        data.add(ca);
        
        con.setCompName(rs.getString(16));
        data.add(con);
        
        lists.add(data);
      }
      rs.close();
      return lists;
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      DBResultSet.close(dbrs);
    }
    return new Vector();
  }
  
  public static ArrayList<String> drawTableListPosition(String whereClause, String order, Date periodeStart, 
          Date periodeEnd, String tglAwal, String tglAkhir, String location, String useForGreenbowl, 
          int groupBy, int type, int showZero){
      DBResultSet dbrs = null;
      ArrayList<String> data = new ArrayList<String>();
      try {
          
          String sql = "SELECT "
                + "material.MATERIAL_ID"
                + ",mat."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" AS SKU"
                +",mat."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" AS MAT_NAME"
                +",mat."+PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]+" AS BARCODE"
                +",mrk."+PstMerk.fieldNames[PstMerk.FLD_NAME]+" AS MERK"
                +",cat."+PstCategory.fieldNames[PstCategory.FLD_NAME]+" AS CAT_NAME"
                +",sub."+PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]+" AS SUB_CATEGORY,"
                + "SUM(STOCK_AWAL) AS STOCK_AWAL,"
                + "SUM(OPNAME) AS OPNAME,"
                + "SUM(SALDO_SETELAH_OPNAME) AS SALDO_SETELAH_OPNAME,"
                + "SUM(PENERIMAAN) AS PENERIMAAN,"
                + "SUM(TRANSFER_IN) AS TRANSFER_IN,"
                + "SUM(TRANSFER_OUT) AS TRANSFER_OUT,"
                + "SUM(PENGEMBALIAN) AS PENGEMBALIAN,"
                + "SUM(SALES) AS SALES,"
                + "SUM(EXCHANGE) AS EXCHANGE,"
                + "(SUM(SALDO_SETELAH_OPNAME) + SUM(PENERIMAAN) + SUM(TRANSFER_IN) - SUM(TRANSFER_OUT) - SUM(PENGEMBALIAN) - SUM(SALES) + SUM(EXCHANGE)) AS END"
                + " FROM (";

          String whereLocation = "";
          if (location.length()>0){
              whereLocation = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" IN ("+location+")";
          }
          Vector listLocation = PstLocation.list(0, 0, whereLocation, "");
          for (int x = 0; x < listLocation.size();x++){
              Location loc = (Location) listLocation.get(x);
              if (x>0){
                  sql += " UNION ";
              }
              sql += "SELECT "
              + "mat."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" AS MATERIAL_ID,"
              + "'" +loc.getName()+"' AS LOCATION,"
              + " (SELECT "
                  + " IF(opname_a.STOCK_OPNAME_DATE IS NULL, 0 ,"
                  + "COALESCE("
                  + "        (SELECT"
                  + "          SUM(item.qty_opname)"
                  + "        FROM"
                  + "          `pos_stock_opname` opn"
                  + "          INNER JOIN `pos_stock_opname_item` item"
                  + "            ON opn.STOCK_OPNAME_ID = item.`STOCK_OPNAME_ID`"
                  + "        WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
                  + "          AND opn.`STOCK_OPNAME_DATE` = opname_a.STOCK_OPNAME_DATE"
                  + "          AND ("
                  + "            opn.`STOCK_OPNAME_STATUS` = 5"
                  + "            OR opn.`STOCK_OPNAME_STATUS` = 7"
                  + "          )"
                  + "          AND opn.`LOCATION_ID` = "+loc.getOID()+"),0))+"
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty)   "
              + "        FROM "
              + "           `pos_receive_material` rec "
              + "           INNER JOIN `pos_receive_material_item` item "
              + "               ON rec.`RECEIVE_MATERIAL_ID` = item.`RECEIVE_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "                AND rec.`RECEIVE_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (rec.`RECEIVE_STATUS` = 5 OR rec.`RECEIVE_STATUS` = 7)"
              + "               AND rec.`LOCATION_ID` = "+loc.getOID()+")"
                  + ","
                  + "       (SELECT "
                + "           SUM(item.qty)   "
                + "        FROM "
                + "           `pos_receive_material` rec "
                + "           INNER JOIN `pos_receive_material_item` item "
                + "               ON rec.`RECEIVE_MATERIAL_ID` = item.`RECEIVE_MATERIAL_ID` "
                + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
                + "                AND rec.`RECEIVE_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
                + "               AND (rec.`RECEIVE_STATUS` = 5 OR rec.`RECEIVE_STATUS` = 7)"
                + "               AND rec.`LOCATION_ID` = "+loc.getOID()+")"
                  + ")"
                  + ", 0 ) - "
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_return_material` ret "
              + "           INNER JOIN `pos_return_material_item` item "
              + "               ON ret.RETURN_MATERIAL_ID = item.`RETURN_MATERIAL_ID` "
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND ret.`RETURN_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (ret.`RETURN_STATUS` = 5 OR ret.`RETURN_STATUS` = 7)"
              + "               AND ret.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_return_material` ret "
              + "           INNER JOIN `pos_return_material_item` item "
              + "               ON ret.RETURN_MATERIAL_ID = item.`RETURN_MATERIAL_ID` "
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND ret.`RETURN_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (ret.`RETURN_STATUS` = 5 OR ret.`RETURN_STATUS` = 7)"
              + "               AND ret.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 ) - "
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_dispatch_material` dis "
              + "           INNER JOIN `pos_dispatch_material_item` item "
              + "               ON dis.DISPATCH_MATERIAL_ID = item.`DISPATCH_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "               AND dis.`DISPATCH_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (dis.`DISPATCH_STATUS` = 5 OR dis.`DISPATCH_STATUS` = 7)"
              + "               AND dis.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_dispatch_material` dis "
              + "           INNER JOIN `pos_dispatch_material_item` item "
              + "               ON dis.DISPATCH_MATERIAL_ID = item.`DISPATCH_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "               AND dis.`DISPATCH_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (dis.`DISPATCH_STATUS` = 5 OR dis.`DISPATCH_STATUS` = 7)"
              + "               AND dis.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 ) - "
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_costing_material` cost "
              + "           INNER JOIN `pos_costing_material_item` item "
              + "               ON cost.COSTING_MATERIAL_ID = item.`COSTING_MATERIAL_ID`"
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND cost.`COSTING_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (cost.`COSTING_STATUS` = 5 OR cost.`COSTING_STATUS` = 7)"
              + "               AND cost.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_costing_material` cost "
              + "           INNER JOIN `pos_costing_material_item` item "
              + "               ON cost.COSTING_MATERIAL_ID = item.`COSTING_MATERIAL_ID`"
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND cost.`COSTING_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND  DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (cost.`COSTING_STATUS` = 5 OR cost.`COSTING_STATUS` = 7)"
              + "               AND cost.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 ) - "
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(det.qty) "
              + "       FROM "
              + "           cash_bill_main bill "
              + "            INNER JOIN `cash_bill_detail` det "
              + "              ON bill.`CASH_BILL_MAIN_ID` = det.`CASH_BILL_MAIN_ID` "
              + "            WHERE det.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND bill.`SHIPPING_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) AND `DOC_TYPE` = 0 "
              + "               AND (bill.`STATUS` = 5 OR bill.`STATUS` = 4)"
              + "               AND bill.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(det.qty) "
              + "       FROM "
              + "           cash_bill_main bill "
              + "            INNER JOIN `cash_bill_detail` det "
              + "              ON bill.`CASH_BILL_MAIN_ID` = det.`CASH_BILL_MAIN_ID` "
              + "            WHERE det.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND bill.`SHIPPING_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND  DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) AND `DOC_TYPE` = 0 "
              + "               AND (bill.`STATUS` = 5 OR bill.`STATUS` = 4)"
              + "               AND bill.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 )) AS STOCK_AWAL, "
              + " COALESCE("
              + "   (SELECT "
              + "        SUM(item.qty_opname) "
              + "       FROM "
              + "       `pos_stock_opname` opn "
              + "       INNER JOIN `pos_stock_opname_item` item "
              + "           ON opn.STOCK_OPNAME_ID = item.`STOCK_OPNAME_ID` "
              + "       WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "           AND opn.`STOCK_OPNAME_DATE` = opname_b.STOCK_OPNAME_DATE "
              + "           AND (opn.`STOCK_OPNAME_STATUS` = 5 OR opn.`STOCK_OPNAME_STATUS` = 7)"
              + "           AND opn.`LOCATION_ID` = "+loc.getOID()+"), 0 ) AS OPNAME,"
              + " IF("
              + " COALESCE("
              + "   (SELECT "
              + "        SUM(item.qty_opname) "
              + "       FROM "
              + "       `pos_stock_opname` opn "
              + "       INNER JOIN `pos_stock_opname_item` item "
              + "           ON opn.STOCK_OPNAME_ID = item.`STOCK_OPNAME_ID` "
              + "       WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "           AND opn.`STOCK_OPNAME_DATE` = opname_b.STOCK_OPNAME_DATE "
              + "           AND (opn.`STOCK_OPNAME_STATUS` = 5 OR opn.`STOCK_OPNAME_STATUS` = 7)"
              + "           AND opn.`LOCATION_ID` = "+loc.getOID()+"), 0 ) > 0, "
              + " COALESCE("
              + "   (SELECT "
              + "        SUM(item.qty_opname) "
              + "       FROM "
              + "       `pos_stock_opname` opn "
              + "       INNER JOIN `pos_stock_opname_item` item "
              + "           ON opn.STOCK_OPNAME_ID = item.`STOCK_OPNAME_ID` "
              + "       WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "           AND opn.`STOCK_OPNAME_DATE` = opname_b.STOCK_OPNAME_DATE "
              + "           AND (opn.`STOCK_OPNAME_STATUS` = 5 OR opn.`STOCK_OPNAME_STATUS` = 7)"
              + "           AND opn.`LOCATION_ID` = "+loc.getOID()+"), 0 ), "
              + " (SELECT "
                  + " IF(opname_a.STOCK_OPNAME_DATE IS NULL, 0 ,"
                  + "COALESCE("
                  + "        (SELECT"
                  + "          SUM(item.qty_opname)"
                  + "        FROM"
                  + "          `pos_stock_opname` opn"
                  + "          INNER JOIN `pos_stock_opname_item` item"
                  + "            ON opn.STOCK_OPNAME_ID = item.`STOCK_OPNAME_ID`"
                  + "        WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
                  + "          AND opn.`STOCK_OPNAME_DATE` = opname_a.STOCK_OPNAME_DATE"
                  + "          AND ("
                  + "            opn.`STOCK_OPNAME_STATUS` = 5"
                  + "            OR opn.`STOCK_OPNAME_STATUS` = 7"
                  + "          )"
                  + "          AND opn.`LOCATION_ID` = "+loc.getOID()+"),0))+"
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty)   "
              + "        FROM "
              + "           `pos_receive_material` rec "
              + "           INNER JOIN `pos_receive_material_item` item "
              + "               ON rec.`RECEIVE_MATERIAL_ID` = item.`RECEIVE_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "                AND rec.`RECEIVE_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (rec.`RECEIVE_STATUS` = 5 OR rec.`RECEIVE_STATUS` = 7)"
              + "               AND rec.`LOCATION_ID` = "+loc.getOID()+")"
                  + ","
                  + "       (SELECT "
                + "           SUM(item.qty)   "
                + "        FROM "
                + "           `pos_receive_material` rec "
                + "           INNER JOIN `pos_receive_material_item` item "
                + "               ON rec.`RECEIVE_MATERIAL_ID` = item.`RECEIVE_MATERIAL_ID` "
                + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
                + "                AND rec.`RECEIVE_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
                + "               AND (rec.`RECEIVE_STATUS` = 5 OR rec.`RECEIVE_STATUS` = 7)"
                + "               AND rec.`LOCATION_ID` = "+loc.getOID()+")"
                  + ")"
                  + ", 0 ) - "
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_return_material` ret "
              + "           INNER JOIN `pos_return_material_item` item "
              + "               ON ret.RETURN_MATERIAL_ID = item.`RETURN_MATERIAL_ID` "
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND ret.`RETURN_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (ret.`RETURN_STATUS` = 5 OR ret.`RETURN_STATUS` = 7)"
              + "               AND ret.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_return_material` ret "
              + "           INNER JOIN `pos_return_material_item` item "
              + "               ON ret.RETURN_MATERIAL_ID = item.`RETURN_MATERIAL_ID` "
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND ret.`RETURN_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (ret.`RETURN_STATUS` = 5 OR ret.`RETURN_STATUS` = 7)"
              + "               AND ret.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 ) - "
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_dispatch_material` dis "
              + "           INNER JOIN `pos_dispatch_material_item` item "
              + "               ON dis.DISPATCH_MATERIAL_ID = item.`DISPATCH_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "               AND dis.`DISPATCH_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (dis.`DISPATCH_STATUS` = 5 OR dis.`DISPATCH_STATUS` = 7)"
              + "               AND dis.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_dispatch_material` dis "
              + "           INNER JOIN `pos_dispatch_material_item` item "
              + "               ON dis.DISPATCH_MATERIAL_ID = item.`DISPATCH_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "               AND dis.`DISPATCH_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (dis.`DISPATCH_STATUS` = 5 OR dis.`DISPATCH_STATUS` = 7)"
              + "               AND dis.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 ) - "
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_costing_material` cost "
              + "           INNER JOIN `pos_costing_material_item` item "
              + "               ON cost.COSTING_MATERIAL_ID = item.`COSTING_MATERIAL_ID`"
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND cost.`COSTING_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (cost.`COSTING_STATUS` = 5 OR cost.`COSTING_STATUS` = 7)"
              + "               AND cost.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_costing_material` cost "
              + "           INNER JOIN `pos_costing_material_item` item "
              + "               ON cost.COSTING_MATERIAL_ID = item.`COSTING_MATERIAL_ID`"
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND cost.`COSTING_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND  DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) "
              + "               AND (cost.`COSTING_STATUS` = 5 OR cost.`COSTING_STATUS` = 7)"
              + "               AND cost.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 ) - "
              + "   COALESCE( "
                  + "IF(opname_a.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(det.qty) "
              + "       FROM "
              + "           cash_bill_main bill "
              + "            INNER JOIN `cash_bill_detail` det "
              + "              ON bill.`CASH_BILL_MAIN_ID` = det.`CASH_BILL_MAIN_ID` "
              + "            WHERE det.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND bill.`SHIPPING_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) AND `DOC_TYPE` = 0 "
              + "               AND (bill.`STATUS` = 5 OR bill.`STATUS` = 4)"
              + "               AND bill.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(det.qty) "
              + "       FROM "
              + "           cash_bill_main bill "
              + "            INNER JOIN `cash_bill_detail` det "
              + "              ON bill.`CASH_BILL_MAIN_ID` = det.`CASH_BILL_MAIN_ID` "
              + "            WHERE det.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND bill.`SHIPPING_DATE` BETWEEN DATE_ADD(opname_a.STOCK_OPNAME_DATE, INTERVAL 1 DAY) AND  DATE_ADD('"+tglAwal+"', INTERVAL -1 DAY) AND `DOC_TYPE` = 0 "
              + "               AND (bill.`STATUS` = 5 OR bill.`STATUS` = 4)"
              + "               AND bill.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 )) "
              + ") AS SALDO_SETELAH_OPNAME, "
              + "   COALESCE( "
                  + "IF(opname_b.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty)   "
              + "        FROM "
              + "           `pos_receive_material` rec "
              + "           INNER JOIN `pos_receive_material_item` item "
              + "               ON rec.`RECEIVE_MATERIAL_ID` = item.`RECEIVE_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "                AND rec.`RECEIVE_DATE` BETWEEN '"+tglAwal+" 00:00:00' AND '"+tglAkhir+" 23:59:00' "
              + "               AND (rec.`RECEIVE_STATUS` = 5 OR rec.`RECEIVE_STATUS` = 7) "
              + "               AND rec.`RECEIVE_SOURCE` IN (0,1,3) "
              + "               AND rec.`LOCATION_ID` = "+loc.getOID()+")"
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty)   "
              + "        FROM "
              + "           `pos_receive_material` rec "
              + "           INNER JOIN `pos_receive_material_item` item "
              + "               ON rec.`RECEIVE_MATERIAL_ID` = item.`RECEIVE_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "                AND rec.`RECEIVE_DATE` BETWEEN opname_b.STOCK_OPNAME_DATE AND '"+tglAkhir+" 23:59:00' "
              + "               AND (rec.`RECEIVE_STATUS` = 5 OR rec.`RECEIVE_STATUS` = 7) "
              + "               AND rec.`RECEIVE_SOURCE` IN (0,1,3) "
              + "               AND rec.`LOCATION_ID` = "+loc.getOID()+")"
                  + ")"
                  + ", 0 ) AS PENERIMAAN, "
              + "   COALESCE( "
                  + "IF(opname_b.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty)   "
              + "        FROM "
              + "           `pos_receive_material` rec "
              + "           INNER JOIN `pos_receive_material_item` item "
              + "               ON rec.`RECEIVE_MATERIAL_ID` = item.`RECEIVE_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "                AND rec.`RECEIVE_DATE` BETWEEN '"+tglAwal+" 00:00:00' AND '"+tglAkhir+" 23:59:00' "
              + "               AND (rec.`RECEIVE_STATUS` = 5 OR rec.`RECEIVE_STATUS` = 7) "
              + "               AND rec.`RECEIVE_SOURCE` = 2 "
              + "               AND rec.`LOCATION_ID` = "+loc.getOID()+")"
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty)   "
              + "        FROM "
              + "           `pos_receive_material` rec "
              + "           INNER JOIN `pos_receive_material_item` item "
              + "               ON rec.`RECEIVE_MATERIAL_ID` = item.`RECEIVE_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "                AND rec.`RECEIVE_DATE` BETWEEN opname_b.STOCK_OPNAME_DATE AND '"+tglAkhir+" 23:59:00' "
              + "               AND (rec.`RECEIVE_STATUS` = 5 OR rec.`RECEIVE_STATUS` = 7) "
              + "               AND rec.`RECEIVE_SOURCE` = 2 "
              + "               AND rec.`LOCATION_ID` = "+loc.getOID()+")"
                  + ")"
                  + ", 0 ) AS TRANSFER_IN, "
              + "   COALESCE( "
                  + "IF(opname_b.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty)   "
              + "        FROM "
              + "           `pos_dispatch_material` dis "
              + "           INNER JOIN `pos_dispatch_material_item` item "
              + "               ON dis.`DISPATCH_MATERIAL_ID` = item.`DISPATCH_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "                AND dis.`DISPATCH_DATE` BETWEEN '"+tglAwal+" 00:00:00' AND '"+tglAkhir+" 23:59:00' "
              + "               AND (dis.`DISPATCH_STATUS` = 5 OR dis.`DISPATCH_STATUS` = 7) "
              + "               AND dis.`LOCATION_ID` = "+loc.getOID()+")"
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty)   "
              + "        FROM "
              + "           `pos_dispatch_material` dis "
              + "           INNER JOIN `pos_dispatch_material_item` item "
              + "               ON dis.`DISPATCH_MATERIAL_ID` = item.`DISPATCH_MATERIAL_ID` "
              + "          WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID`"
              + "                AND dis.`DISPATCH_DATE` BETWEEN opname_b.STOCK_OPNAME_DATE AND '"+tglAkhir+" 23:59:00' "
              + "               AND (dis.`DISPATCH_STATUS` = 5 OR dis.`DISPATCH_STATUS` = 7) "
              + "               AND dis.`LOCATION_ID` = "+loc.getOID()+")"
                  + ")"
                  + ", 0 ) AS TRANSFER_OUT, "
              + "   COALESCE( "
                  + "IF(opname_b.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_return_material` ret "
              + "           INNER JOIN `pos_return_material_item` item "
              + "               ON ret.RETURN_MATERIAL_ID = item.`RETURN_MATERIAL_ID` "
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND ret.`RETURN_DATE` BETWEEN '"+tglAwal+" 00:00:00' AND '"+tglAkhir+" 23:59:00' "
              + "               AND (ret.`RETURN_STATUS` = 5 OR ret.`RETURN_STATUS` = 7)"
              + "               AND ret.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(item.qty) "
              + "       FROM "
              + "           `pos_return_material` ret "
              + "           INNER JOIN `pos_return_material_item` item "
              + "               ON ret.RETURN_MATERIAL_ID = item.`RETURN_MATERIAL_ID` "
              + "           WHERE item.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND ret.`RETURN_DATE` BETWEEN opname_b.STOCK_OPNAME_DATE AND '"+tglAkhir+" 23:59:00' "
              + "               AND (ret.`RETURN_STATUS` = 5 OR ret.`RETURN_STATUS` = 7)"
              + "               AND ret.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 ) AS PENGEMBALIAN, "
              + "   COALESCE( "
                  + "IF(opname_b.STOCK_OPNAME_DATE IS NULL,"
              + "       (SELECT "
              + "           SUM(det.qty) "
              + "       FROM "
              + "           cash_bill_main bill "
              + "            INNER JOIN `cash_bill_detail` det "
              + "              ON bill.`CASH_BILL_MAIN_ID` = det.`CASH_BILL_MAIN_ID` "
              + "            WHERE det.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND bill.`SHIPPING_DATE` BETWEEN '"+tglAwal+" 00:00:00' AND '"+tglAkhir+" 23:59:00' AND `DOC_TYPE` = 0 "
              + "               AND (bill.`STATUS` = 5 OR bill.`STATUS` = 4)"
              + "               AND bill.`LOCATION_ID` = "+loc.getOID()+") "
                  + ","
                  + "       (SELECT "
              + "           SUM(det.qty) "
              + "       FROM "
              + "           cash_bill_main bill "
              + "            INNER JOIN `cash_bill_detail` det "
              + "              ON bill.`CASH_BILL_MAIN_ID` = det.`CASH_BILL_MAIN_ID` "
              + "            WHERE det.`MATERIAL_ID` = mat.`MATERIAL_ID` "
              + "               AND bill.`SHIPPING_DATE` BETWEEN opname_b.STOCK_OPNAME_DATE AND  '"+tglAkhir+" 23:59:00' AND `DOC_TYPE` = 0 "
              + "               AND (bill.`STATUS` = 5 OR bill.`STATUS` = 4)"
              + "               AND bill.`LOCATION_ID` = "+loc.getOID()+") "
                  + ")"
                  + ", 0 ) AS SALES, "
              + "   0 AS EXCHANGE "
              + " FROM "
              + "   pos_material as mat "
              + "   LEFT JOIN `pos_merk` AS mrk "
              + "      ON mat.`MERK_ID` = mrk.`MERK_ID`"
              + "   LEFT JOIN `pos_sub_category` AS sub"
              + "      ON mat.`SUB_CATEGORY_ID` = sub.`SUB_CATEGORY_ID`"
              + "   LEFT JOIN `contact_list` AS con "
              + "      ON mat.`SUPPLIER_ID` = con.`CONTACT_ID`"
              + "   LEFT JOIN `pos_category` AS cat "
              + "      ON mat.`CATEGORY_ID` = cat.`CATEGORY_ID`"
              + "  LEFT JOIN (SELECT MAX(`STOCK_OPNAME_DATE`) AS stock_opname_date, `MATERIAL_ID` "
                  + " FROM `pos_stock_opname` AS opn "
                  + " INNER JOIN `pos_stock_opname_item` itm "
                  + " ON opn.`STOCK_OPNAME_ID` = itm.`STOCK_OPNAME_ID` "
                  + " AND opn.`LOCATION_ID` = "+loc.getOID()
                  + " AND opn.`STOCK_OPNAME_DATE` <= DATE_ADD('"+tglAwal+"', INTERVAL - 1 DAY) GROUP BY itm.`MATERIAL_ID`) opname_a ON mat.`MATERIAL_ID` = opname_a.MATERIAL_ID"
             + "  LEFT JOIN (SELECT MAX(`STOCK_OPNAME_DATE`) AS stock_opname_date, `MATERIAL_ID` "
                  + " FROM `pos_stock_opname` AS opn "
                  + " INNER JOIN `pos_stock_opname_item` itm "
                  + " ON opn.`STOCK_OPNAME_ID` = itm.`STOCK_OPNAME_ID` "
                  + " AND opn.`LOCATION_ID` = "+loc.getOID()
                  + " AND opn.`STOCK_OPNAME_DATE` BETWEEN '"+tglAwal+" 00:00:00' AND '"+tglAkhir+" 23:59:00' GROUP BY itm.`MATERIAL_ID`) opname_b ON mat.`MATERIAL_ID` = opname_b.MATERIAL_ID";
              if (whereClause.length() > 0){
                  sql += " WHERE "+whereClause;
                }
          }
          
          sql += ") AS material INNER JOIN pos_material mat ON material.MATERIAL_ID = mat.MATERIAL_ID "
                  + "   LEFT JOIN `pos_merk` AS mrk "
                + "      ON mat.`MERK_ID` = mrk.`MERK_ID`"
                + "   LEFT JOIN `pos_sub_category` AS sub"
                + "      ON mat.`SUB_CATEGORY_ID` = sub.`SUB_CATEGORY_ID`"
                + "   LEFT JOIN `contact_list` AS con "
                + "      ON mat.`SUPPLIER_ID` = con.`CONTACT_ID`"
                + "   LEFT JOIN `pos_category` AS cat "
                + "      ON mat.`CATEGORY_ID` = cat.`CATEGORY_ID` GROUP BY mat.`SKU`, mat.`NAME`";
              if (order.length() > 0){
                  sql += " ORDER BY "+order;
              }
              
            
              
                int count=0;
                String oldMkName = "";
                boolean isShow = false;
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    
                    
                    int opname = rs.getInt("SALDO_SETELAH_OPNAME");
                    int saldowAwal = rs.getInt("STOCK_AWAL");
                    int adj = 0;
                    int penerimaan = rs.getInt("PENERIMAAN");
                    int pengembalian = rs.getInt("PENGEMBALIAN");
                    int sales = rs.getInt("SALES");
                    int exc = rs.getInt("EXCHANGE");
                    int tIn = rs.getInt("TRANSFER_IN");
                    int tOut = rs.getInt("TRANSFER_OUT");
                    if (opname > 0){
                        adj = opname;
                    }
                    int end = opname + penerimaan + tIn - tOut - pengembalian - sales + exc;
                    String html = "";
                    if (groupBy == SrcSaleReport.GROUP_BY_SUPPLIER) {
                        if (!oldMkName.equals("") && !oldMkName.equals(rs.getString("SUP_NAME"))) {
                            isShow = false;
                        }
                        oldMkName = rs.getString("SUP_NAME");
                        if (!isShow) {
                            isShow = true;
                            html += "<tr><td colspan=\"15\"  style=\"background-color: #b2dbff; color: #428bca\">"+rs.getString("SUP_NAME")+"</td></tr>";
                        }
                    } else if (groupBy == SrcSaleReport.GROUP_BY_CATEGORY) {
                        if (!oldMkName.equals("") && !oldMkName.equals(rs.getString("CAT_NAME"))) {
                            isShow = false;
                          }
                          oldMkName = rs.getString("CAT_NAME");
                          if (!isShow) {
                              isShow = true;
                            html += "<tr><td colspan=\"15\"  style=\"background-color: #b2dbff; color: #428bca\">"+rs.getString("CAT_NAME")+"</td></tr>";
                          }
                    } 
                    
                    if (showZero == 0){
                        if (end > 0){
                            count++;
                        } else {
                            continue;
                        } 
                    } else {
                        count++;
                    }
                    
                    if (type == 0){
                        html+= "<tr>"
                            + "<td style=\"font-size: 10px;\">"+count+"</td>"
                            + "<td style=\"font-size: 10px;\">"+rs.getString("SKU")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+rs.getString("BARCODE")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+rs.getString("MAT_NAME")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+rs.getString("MERK")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+(rs.getString("SUB_CATEGORY") != null ? rs.getString("SUB_CATEGORY") : " - ")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+saldowAwal+"</td>"
                            + "<td style=\"font-size: 10px;\">"+penerimaan+"</td>"
                            + "<td style=\"font-size: 10px;\">"+tIn+"</td>"
                            + "<td style=\"font-size: 10px;\">"+tOut+"</td>"
                            + "<td style=\"font-size: 10px;\">"+pengembalian+"</td>"
                            + "<td style=\"font-size: 10px;\">"+sales+"</td>"
                            + "<td style=\"font-size: 10px;\">"+exc+"</td>"
                            + "<td style=\"font-size: 10px;\">"+adj+"</td>"
                            + "<td style=\"font-size: 10px;\">"+end+"</td>"
                            + "";
                    } else {
                        html+= "<tr>"
                            + "<td style=\"font-size: 12px;\">"+count+"</td>"
                            + "<td style=\"font-size: 12px;\">"+rs.getString("SKU")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+rs.getString("BARCODE")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+rs.getString("MAT_NAME")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+rs.getString("MERK")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+(rs.getString("SUB_CATEGORY") != null ? rs.getString("SUB_CATEGORY") : " - ")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+saldowAwal+"</td>"
                            + "<td style=\"font-size: 12px;\">"+penerimaan+"</td>"
                            + "<td style=\"font-size: 12px;\">"+tIn+"</td>"
                            + "<td style=\"font-size: 12px;\">"+tOut+"</td>"
                            + "<td style=\"font-size: 12px;\">"+pengembalian+"</td>"
                            + "<td style=\"font-size: 12px;\">"+sales+"</td>"
                            + "<td style=\"font-size: 12px;\">"+exc+"</td>"
                            + "<td style=\"font-size: 12px;\">"+adj+"</td>"
                            + "<td style=\"font-size: 12px;\">"+end+"</td>"
                            + "";
                    }
                    
                    data.add(html);
                    System.out.println(count);
                    
                }
      }catch (Exception exc){}
      
      
      return data;
  }
  
  public static ArrayList<String> drawTableListPositionStok(String whereClause, String order, Date periodeStart, Date periodeEnd, String tglAwal, String tglAkhir, String location, String useForGreenbowl, int groupBy, int type){
      DBResultSet dbrs = null;
      ArrayList<String> data = new ArrayList<String>();
      String whereLocation = "";
      if (location.length()>0){
          whereLocation = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" IN ("+location+")";
      }
      Vector listLocation = PstLocation.list(0, 0, whereLocation, "");
      try {
          String sql = "SELECT "
              + "mat."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
              + ",mat."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" AS SKU"
              +",mat."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" AS MAT_NAME"
              +",mat."+PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]+" AS BARCODE"
              +",mrk."+PstMerk.fieldNames[PstMerk.FLD_NAME]+" AS MERK"
              +",sub."+PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]+" AS SUB_CATEGORY"
              + ",con.COMP_NAME AS SUP_NAME "
              + ",cat.NAME AS CAT_NAME"
              + " FROM "
              + "   pos_material as mat "
              + "   LEFT JOIN `pos_merk` AS mrk "
              + "      ON mat.`MERK_ID` = mrk.`MERK_ID`"
              + "   LEFT JOIN `pos_sub_category` AS sub"
              + "      ON mat.`SUB_CATEGORY_ID` = sub.`SUB_CATEGORY_ID`"
              + "   LEFT JOIN `contact_list` AS con "
              + "      ON mat.`SUPPLIER_ID` = con.`CONTACT_ID`"
              + "   LEFT JOIN `pos_category` AS cat "
              + "      ON mat.`CATEGORY_ID` = cat.`CATEGORY_ID`";
              if (whereClause.length() > 0){
                  sql += " WHERE "+whereClause;
            }
              if (order.length() > 0){
                  sql += " ORDER BY "+order;
              }
              
                int count=0;
                String oldMkName = "";
                boolean isShow = false;
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    count++;
                    
                    SrcStockCard srcStockCard = new SrcStockCard();
                    Vector vectSt = new Vector(1, 1);
                    vectSt.add("2");
                    vectSt.add("5");
                    vectSt.add("7");
                    srcStockCard.setDocStatus(vectSt);
                    srcStockCard.setMaterialId(rs.getLong("MATERIAL_ID"));
                    srcStockCard.setStardDate(periodeStart);
                    double saldowAwal = qtyStockSaldoAwal(srcStockCard, listLocation);
                    srcStockCard.setEndDate(periodeEnd);
                    double penerimaan= qtyStockReceive(srcStockCard, listLocation,0);
                    double opname = qtyStockOpname(srcStockCard, listLocation);
                    int adj = 0;
                    double pengembalian = qtyStockReturn(srcStockCard, listLocation);
                    double sales =  qtyStockSales(srcStockCard, listLocation);
                    int exc = 0;
                    double tIn = qtyStockReceive(srcStockCard, listLocation,1);
                    double tOut = qtyStockDispatch(srcStockCard, listLocation);
                    double end = qtyStockSaldoAkhir(srcStockCard, listLocation);
                    String html = "";
                    if (groupBy == SrcSaleReport.GROUP_BY_SUPPLIER) {
                        if (!oldMkName.equals("") && !oldMkName.equals(rs.getString("SUP_NAME"))) {
                            isShow = false;
                        }
                        oldMkName = rs.getString("SUP_NAME");
                        if (!isShow) {
                            isShow = true;
                            html += "<tr><td colspan=\"15\"  style=\"background-color: #b2dbff; color: #428bca\">"+rs.getString("SUP_NAME")+"</td></tr>";
                        }
                    } else if (groupBy == SrcSaleReport.GROUP_BY_CATEGORY) {
                        if (!oldMkName.equals("") && !oldMkName.equals(rs.getString("CAT_NAME"))) {
                            isShow = false;
                          }
                          oldMkName = rs.getString("CAT_NAME");
                          if (!isShow) {
                              isShow = true;
                            html += "<tr><td colspan=\"15\"  style=\"background-color: #b2dbff; color: #428bca\">"+rs.getString("CAT_NAME")+"</td></tr>";
                          }
                    } 
                    
                    if (type == 0){
                        html+= "<tr>"
                            + "<td style=\"font-size: 10px;\">"+count+"</td>"
                            + "<td style=\"font-size: 10px;\">"+rs.getString("SKU")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+rs.getString("BARCODE")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+rs.getString("MAT_NAME")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+rs.getString("MERK")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+(rs.getString("SUB_CATEGORY") != null ? rs.getString("SUB_CATEGORY") : " - ")+"</td>"
                            + "<td style=\"font-size: 10px;\">"+saldowAwal+"</td>"
                            + "<td style=\"font-size: 10px;\">"+penerimaan+"</td>"
                            + "<td style=\"font-size: 10px;\">"+tIn+"</td>"
                            + "<td style=\"font-size: 10px;\">"+tOut+"</td>"
                            + "<td style=\"font-size: 10px;\">"+pengembalian+"</td>"
                            + "<td style=\"font-size: 10px;\">"+sales+"</td>"
                            + "<td style=\"font-size: 10px;\">"+exc+"</td>"
                            + "<td style=\"font-size: 10px;\">"+opname+"</td>"
                            + "<td style=\"font-size: 10px;\">"+end+"</td>"
                            + "";
                    } else {
                        html+= "<tr>"
                            + "<td style=\"font-size: 12px;\">"+count+"</td>"
                            + "<td style=\"font-size: 12px;\">"+rs.getString("SKU")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+rs.getString("BARCODE")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+rs.getString("MAT_NAME")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+rs.getString("MERK")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+(rs.getString("SUB_CATEGORY") != null ? rs.getString("SUB_CATEGORY") : " - ")+"</td>"
                            + "<td style=\"font-size: 12px;\">"+saldowAwal+"</td>"
                            + "<td style=\"font-size: 12px;\">"+penerimaan+"</td>"
                            + "<td style=\"font-size: 12px;\">"+tIn+"</td>"
                            + "<td style=\"font-size: 12px;\">"+tOut+"</td>"
                            + "<td style=\"font-size: 12px;\">"+pengembalian+"</td>"
                            + "<td style=\"font-size: 12px;\">"+sales+"</td>"
                            + "<td style=\"font-size: 12px;\">"+exc+"</td>"
                            + "<td style=\"font-size: 12px;\">"+opname+"</td>"
                            + "<td style=\"font-size: 12px;\">"+end+"</td>"
                            + "";
                    }
                    
                    data.add(html);
                    System.out.println(count);
                    
                }
      }catch (Exception exc){}
      
      
      return data;
  }
  
  synchronized public static double qtyStockSaldoAwal(SrcStockCard srcStockCard, Vector location){
        double qtyStock = 0;
        try {
            if (srcStockCard.getStardDate() != null) {
                Vector list = new Vector(1, 1);
                //Date endDate = new Date(srcStockCard.getEndDate().getTime());
                srcStockCard.setEndDate(null);
                for (int i =0; i < location.size();i++){
                    Location loc = (Location) location.get(i);
                    PstStockCardReport.deleteExc();
                    srcStockCard.setLocationId(loc.getOID());
                    // pencarian stock sebelum rentang waktu pencarian
                    SessMatReceive.getDataMaterial(srcStockCard);
                    SessMatReturn.getDataMaterial(srcStockCard);
                    SessMatDispatch.getDataMaterial(srcStockCard);
                    SessMatStockOpname.getDataMaterial(srcStockCard);
                    SessReportSale.getDataMaterial(srcStockCard);
                    SessMatCosting.getDataMaterial(srcStockCard);
                    //-- ADDED BY DEWOK 20190924 FOR PRODUCTION
                    SessProduction.getDataMaterial(srcStockCard);
                    //--

                    String order = PstStockCardReport.fieldNames[PstStockCardReport.FLD_TANGGAL];
                    list = PstStockCardReport.list(0, 0, "", order);

                    qtyStock += SessStockCard.prosesGetPrivousDataStockCard(list);
                }
            }
        } catch (Exception exc){}
        return qtyStock;
    }
  synchronized public static double qtyStockSaldoAkhir(SrcStockCard srcStockCard, Vector location){
        double qtyStock = 0;
        try {
            if (srcStockCard.getStardDate() != null) {
                Vector list = new Vector(1, 1);
                //Date endDate = new Date(srcStockCard.getEndDate().getTime());
                for (int i =0; i < location.size();i++){
                    Location loc = (Location) location.get(i);
                    PstStockCardReport.deleteExc();
                    srcStockCard.setLocationId(loc.getOID());
                    // pencarian stock sebelum rentang waktu pencarian
                    SessMatReceive.getDataMaterial(srcStockCard);
                    SessMatReturn.getDataMaterial(srcStockCard);
                    SessMatDispatch.getDataMaterial(srcStockCard);
                    SessMatStockOpname.getDataMaterial(srcStockCard);
                    SessReportSale.getDataMaterial(srcStockCard);
                    SessMatCosting.getDataMaterial(srcStockCard);
                    //-- ADDED BY DEWOK 20190924 FOR PRODUCTION
                    SessProduction.getDataMaterial(srcStockCard);
                    //--

                    String order = PstStockCardReport.fieldNames[PstStockCardReport.FLD_TANGGAL];
                    list = PstStockCardReport.list(0, 0, "", order);

                    qtyStock += SessStockCard.prosesGetPrivousDataStockCard(list);
                }
            }
        } catch (Exception exc){}
        return qtyStock;
    }
  synchronized public static double qtyStockReceive(SrcStockCard srcStockCard, Vector location, int type){
        double qtyStock = 0;
        try {
            if (srcStockCard.getStardDate() != null) {
                Vector list = new Vector(1, 1);
                for (int i =0; i < location.size();i++){
                    Location loc = (Location) location.get(i);
                    PstStockCardReport.deleteExc();
                    srcStockCard.setLocationId(loc.getOID());
                    // pencarian stock sebelum rentang waktu pencarian
                    SessMatReceive.getDataMaterial(srcStockCard);
                    
                    String order = PstStockCardReport.fieldNames[PstStockCardReport.FLD_TANGGAL];
                    if (type == 0){
                        list = PstStockCardReport.list(0, 0, "KETERANGAN NOT LIKE '%Transfer in%'", order);
                    } else {
                        list = PstStockCardReport.list(0, 0, "KETERANGAN LIKE '%Transfer in%'", order);
                    }

                    qtyStock += SessStockCard.prosesGetPrivousDataStockCard(list);
                }
            }
        } catch (Exception exc){}
        return qtyStock;
    }
  synchronized public static double qtyStockDispatch(SrcStockCard srcStockCard, Vector location){
        double qtyStock = 0;
        try {
            if (srcStockCard.getStardDate() != null) {
                Vector list = new Vector(1, 1);
                for (int i =0; i < location.size();i++){
                    Location loc = (Location) location.get(i);
                    PstStockCardReport.deleteExc();
                    srcStockCard.setLocationId(loc.getOID());
                    // pencarian stock sebelum rentang waktu pencarian
                    SessMatDispatch.getDataMaterial(srcStockCard);
                    
                    String order = PstStockCardReport.fieldNames[PstStockCardReport.FLD_TANGGAL];
                    list = PstStockCardReport.list(0, 0, "", order);

                    qtyStock += SessStockCard.prosesGetPrivousDataStockCard(list);
                }
            }
        } catch (Exception exc){}
        return qtyStock;
    }
   synchronized public static double qtyStockReturn(SrcStockCard srcStockCard, Vector location){
        double qtyStock = 0;
        try {
            if (srcStockCard.getStardDate() != null) {
                Vector list = new Vector(1, 1);
                for (int i =0; i < location.size();i++){
                    Location loc = (Location) location.get(i);
                    PstStockCardReport.deleteExc();
                    srcStockCard.setLocationId(loc.getOID());
                    // pencarian stock sebelum rentang waktu pencarian
                    SessMatReturn.getDataMaterial(srcStockCard);
                    
                    String order = PstStockCardReport.fieldNames[PstStockCardReport.FLD_TANGGAL];
                    list = PstStockCardReport.list(0, 0, "", order);

                    qtyStock += SessStockCard.prosesGetPrivousDataStockCard(list);
                }
            }
        } catch (Exception exc){}
        return qtyStock;
    }
   synchronized public static double qtyStockSales(SrcStockCard srcStockCard, Vector location){
        double qtyStock = 0;
        try {
            if (srcStockCard.getStardDate() != null) {
                Vector list = new Vector(1, 1);
                for (int i =0; i < location.size();i++){
                    Location loc = (Location) location.get(i);
                    PstStockCardReport.deleteExc();
                    srcStockCard.setLocationId(loc.getOID());
                    // pencarian stock sebelum rentang waktu pencarian
                    SessReportSale.getDataMaterial(srcStockCard);
                    
                    String order = PstStockCardReport.fieldNames[PstStockCardReport.FLD_TANGGAL];
                    list = PstStockCardReport.list(0, 0, "", order);

                    qtyStock += SessStockCard.prosesGetPrivousDataStockCard(list);
                }
            }
        } catch (Exception exc){}
        return qtyStock;
    }
   synchronized public static double qtyStockOpname(SrcStockCard srcStockCard, Vector location){
        double qtyStock = 0;
        try {
            if (srcStockCard.getStardDate() != null) {
                Vector list = new Vector(1, 1);
                for (int i =0; i < location.size();i++){
                    Location loc = (Location) location.get(i);
                    PstStockCardReport.deleteExc();
                    srcStockCard.setLocationId(loc.getOID());
                    // pencarian stock sebelum rentang waktu pencarian
                    SessMatStockOpname.getDataMaterial(srcStockCard);
                    
                    String order = PstStockCardReport.fieldNames[PstStockCardReport.FLD_TANGGAL];
                    list = PstStockCardReport.list(0, 0, "", order);

                    qtyStock += SessStockCard.prosesGetPrivousDataStockCard(list);
                }
            }
        } catch (Exception exc){}
        return qtyStock;
    }
}
