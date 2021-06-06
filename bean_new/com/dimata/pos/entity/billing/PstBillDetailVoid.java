/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.pos.entity.billing;

/**
 *
 * @author dimata005
 */

import com.dimata.interfaces.BOCashier.I_BillingDetail;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
/* package java */

/* package qdep */
import java.sql.ResultSet;
import java.util.Vector;

//import com.dimata.qdep.db.*;
/* package cashier */
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;

import com.dimata.util.Formater;
import java.util.Date;

public class PstBillDetailVoid  extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_BillingDetail{

    public static final String TBL_CASH_BILL_DETAIL = "cash_bill_detail_void";
    public static final int FLD_BILL_DETAIL_ID = 0;
    public static final int FLD_BILL_MAIN_ID = 1;
    public static final int FLD_UNIT_ID = 2;
    public static final int FLD_MATERIAL_ID = 3;
    public static final int FLD_QUANTITY = 4;
    public static final int FLD_ITEM_PRICE = 5;
    public static final int FLD_DISC_TYPE = 6;
    public static final int FLD_DISC = 7;
    public static final int FLD_TOTAL_PRICE = 8;
    public static final int FLD_SKU = 9;
    public static final int FLD_ITEM_NAME = 10;
    public static final int FLD_MATERIAL_TYPE = 11;
    public static final int FLD_COST = 12;
    public static final int FLD_DISC_PCT = 13;
    public static final int FLD_QTY_STOCK = 14;
    public static final int FLD_ITEM_PRICE_STOCK = 15;
    public static final int FLD_DISC_GLOBAL = 16;

    //Ari wiweka 20130719
    public static final int FLD_DISC1 = 17;
    public static final int FLD_DISC2 = 18;
    public static final int FLD_TOTAL_DISC = 19;
    public static final int FLD_NOTE = 20;
    
    public static final int FLD_STATUS=21;
    public static final int FLD_STATUS_PRINT=22;
    public static final int FLD_LENGTH_OF_ORDER=23;
    public static final int FLD_LENGTH_OF_FINISH_ORDER=24;
    
    public static final String[] fieldNames = {
        "CASH_BILL_DETAIL_ID",
        "CASH_BILL_MAIN_ID",
        "UNIT_ID",
        "MATERIAL_ID",
        "QTY",
        "ITEM_PRICE",
        "DISC_TYPE",
        "DISC",
        "TOTAL_PRICE",
        "SKU",
        "ITEM_NAME",
        "MATERIAL_TYPE",
        "COST", "DISC_PCT",
        "QTY_STOCK", "ITEM_PRICE_STOCK",
        "DISC_GLOBAL",
        "DISC1",
        "DISC2",
        "TOTAL_DISC",
        "NOTE",
        "STATUS",
        "STATUS_PRINT",
        "LENGTH_OF_ORDER",
        "LENGTH_OF_ORDER_FINISH"
            
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG + TYPE_FK,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_DATE
    };
    public static final int TYPE_DISC_VAL = 0;
    public static final int TYPE_DISC_PCT = 1;
    public static final int UPDATE_STATUS_NONE = 0;
    public static final int UPDATE_STATUS_INSERTED = 1;
    public static final int UPDATE_STATUS_UPDATED = 2;
    public static final int UPDATE_STATUS_DELETED = 3;

    //constructor
    public PstBillDetailVoid() {
    }

    public PstBillDetailVoid(int i) throws DBException {
        super(new PstBillDetailVoid());
    }

    public PstBillDetailVoid(String sOid) throws DBException {
        super(new PstBillDetailVoid(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
    }
    }

    public PstBillDetailVoid(long lOid) throws DBException {
        super(new PstBillDetailVoid(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
    }
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_CASH_BILL_DETAIL;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstBillDetailVoid().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Billdetail billdetail = fetchExc(ent.getOID());
        ent = (Entity) billdetail;
        return billdetail.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Billdetail) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Billdetail) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Billdetail fetchExc(long oid) throws DBException {
        try {
            Billdetail billdetail = new Billdetail();
            PstBillDetailVoid pstBillDetailVoid = new PstBillDetailVoid(oid);
            billdetail.setOID(oid);
            billdetail.setBillMainId(pstBillDetailVoid.getlong(FLD_BILL_MAIN_ID));
            billdetail.setUnitId(pstBillDetailVoid.getlong(FLD_UNIT_ID));
            billdetail.setMaterialId(pstBillDetailVoid.getlong(FLD_MATERIAL_ID));
            billdetail.setItemName(pstBillDetailVoid.getString(FLD_ITEM_NAME));
            billdetail.setItemPrice(pstBillDetailVoid.getdouble(FLD_ITEM_PRICE));
            billdetail.setDiscType(pstBillDetailVoid.getInt(FLD_DISC_TYPE));
            billdetail.setDisc(pstBillDetailVoid.getdouble(FLD_DISC));
            billdetail.setQty(pstBillDetailVoid.getdouble(FLD_QUANTITY));
            billdetail.setTotalPrice(pstBillDetailVoid.getdouble(FLD_TOTAL_PRICE));
            billdetail.setSku(pstBillDetailVoid.getString(FLD_SKU));
            billdetail.setMaterialType(pstBillDetailVoid.getInt(FLD_MATERIAL_TYPE));
            billdetail.setCost(pstBillDetailVoid.getdouble(FLD_COST));
            billdetail.setDiscPct(pstBillDetailVoid.getdouble(FLD_DISC_PCT));
            billdetail.setQtyStock(pstBillDetailVoid.getdouble(FLD_QTY_STOCK));
            billdetail.setItemPriceStock(pstBillDetailVoid.getdouble(FLD_ITEM_PRICE_STOCK));
            billdetail.setDiscGlobal(pstBillDetailVoid.getdouble(FLD_DISC_GLOBAL));
            billdetail.setDisc1(pstBillDetailVoid.getdouble(FLD_DISC1));
            billdetail.setDisc2(pstBillDetailVoid.getdouble(FLD_DISC2));
            billdetail.setTotalDisc(pstBillDetailVoid.getdouble(FLD_TOTAL_DISC));
            billdetail.setSku(pstBillDetailVoid.getString(FLD_SKU));
            billdetail.setNote(pstBillDetailVoid.getString(FLD_NOTE));
            billdetail.setStatus(pstBillDetailVoid.getInt(FLD_STATUS));
            billdetail.setStatusPrint(pstBillDetailVoid.getInt(FLD_STATUS_PRINT));
            billdetail.setLengthOrder(pstBillDetailVoid.getDate(FLD_LENGTH_OF_ORDER));
            return billdetail;
        } catch (DBException dbe) {
            System.out.println("error =" + dbe);
            throw dbe;
        } catch (Exception e) {
            System.out.println("error =" + e);
            throw new DBException(new PstBillDetailVoid(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Billdetail billdetail) throws DBException {
        try {
            PstBillDetailVoid pstBillDetailVoid = new PstBillDetailVoid(0);
            pstBillDetailVoid.setLong(FLD_BILL_MAIN_ID, billdetail.getBillMainId());
            pstBillDetailVoid.setLong(FLD_UNIT_ID, billdetail.getUnitId());
            pstBillDetailVoid.setLong(FLD_MATERIAL_ID, billdetail.getMaterialId());
            pstBillDetailVoid.setString(FLD_ITEM_NAME, billdetail.getItemName());
            pstBillDetailVoid.setDouble(FLD_ITEM_PRICE, billdetail.getItemPrice());
            pstBillDetailVoid.setInt(FLD_DISC_TYPE, billdetail.getDiscType());
            pstBillDetailVoid.setDouble(FLD_DISC, billdetail.getDisc());
            pstBillDetailVoid.setDouble(FLD_QUANTITY, billdetail.getQty());
            pstBillDetailVoid.setDouble(FLD_TOTAL_PRICE, billdetail.getTotalPrice());
            pstBillDetailVoid.setString(FLD_SKU, billdetail.getSku());
            pstBillDetailVoid.setInt(FLD_MATERIAL_TYPE, billdetail.getMaterialType());
            pstBillDetailVoid.setDouble(FLD_COST, billdetail.getCost());
            pstBillDetailVoid.setDouble(FLD_DISC_PCT, billdetail.getDiscPct());
            pstBillDetailVoid.setDouble(FLD_QTY_STOCK, billdetail.getQtyStock());
            pstBillDetailVoid.setDouble(FLD_ITEM_PRICE_STOCK, billdetail.getItemPriceStock());
            pstBillDetailVoid.setDouble(FLD_DISC1, billdetail.getDisc1());
            pstBillDetailVoid.setDouble(FLD_DISC2, billdetail.getDisc2());
            pstBillDetailVoid.setDouble(FLD_TOTAL_DISC, billdetail.getTotalDisc());
            pstBillDetailVoid.setString(FLD_NOTE, billdetail.getNote());
            pstBillDetailVoid.setInt(FLD_STATUS, billdetail.getStatus());
            pstBillDetailVoid.setInt(FLD_STATUS_PRINT, billdetail.getStatusPrint());
            pstBillDetailVoid.setDate(FLD_LENGTH_OF_ORDER, billdetail.getLengthOrder());
            
            pstBillDetailVoid.insert();
            billdetail.setOID(pstBillDetailVoid.getlong(FLD_BILL_DETAIL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBillDetailVoid(0), DBException.UNKNOWN);
        }
        return billdetail.getOID();
    }

    public static long insertExcByOid(Billdetail billdetail) throws DBException {
        try {
            PstBillDetailVoid pstBillDetailVoid = new PstBillDetailVoid(0);
            pstBillDetailVoid.setLong(FLD_BILL_MAIN_ID, billdetail.getBillMainId());
            pstBillDetailVoid.setLong(FLD_UNIT_ID, billdetail.getUnitId());
            pstBillDetailVoid.setLong(FLD_MATERIAL_ID, billdetail.getMaterialId());
            pstBillDetailVoid.setString(FLD_ITEM_NAME, billdetail.getItemName());
            pstBillDetailVoid.setDouble(FLD_ITEM_PRICE, billdetail.getItemPrice());
            pstBillDetailVoid.setInt(FLD_DISC_TYPE, billdetail.getDiscType());
            pstBillDetailVoid.setDouble(FLD_DISC, billdetail.getDisc());
            pstBillDetailVoid.setDouble(FLD_QUANTITY, billdetail.getQty());
            pstBillDetailVoid.setDouble(FLD_TOTAL_PRICE, billdetail.getTotalPrice());
            pstBillDetailVoid.setString(FLD_SKU, billdetail.getSku());
            pstBillDetailVoid.setInt(FLD_MATERIAL_TYPE, billdetail.getMaterialType());
            pstBillDetailVoid.setDouble(FLD_COST, billdetail.getCost());
            pstBillDetailVoid.setDouble(FLD_DISC_PCT, billdetail.getDiscPct());
            pstBillDetailVoid.setDouble(FLD_QTY_STOCK, billdetail.getQtyStock());
            pstBillDetailVoid.setDouble(FLD_ITEM_PRICE_STOCK, billdetail.getItemPriceStock());
            pstBillDetailVoid.setDouble(FLD_DISC_GLOBAL, billdetail.getDiscGlobal());
            pstBillDetailVoid.setDouble(FLD_DISC1, billdetail.getDisc1());
            pstBillDetailVoid.setDouble(FLD_DISC2, billdetail.getDisc2());
            pstBillDetailVoid.setDouble(FLD_TOTAL_DISC, billdetail.getTotalDisc());
            pstBillDetailVoid.setString(FLD_NOTE, billdetail.getNote());
            pstBillDetailVoid.setInt(FLD_STATUS, billdetail.getStatus());
            pstBillDetailVoid.setInt(FLD_STATUS_PRINT, billdetail.getStatusPrint());
            pstBillDetailVoid.setDate(FLD_LENGTH_OF_ORDER, billdetail.getLengthOrder());
            
            pstBillDetailVoid.insertByOid(billdetail.getOID());
             //billdetail.setOID(pstBillDetailVoid.getlong(FLD_BILL_DETAIL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBillDetailVoid(0), DBException.UNKNOWN);
        }
        return billdetail.getOID();
    }

    public static long updateExc(Billdetail billdetail) throws DBException {
        try {
            if (billdetail.getOID() != 0) {
                PstBillDetailVoid pstBillDetailVoid = new PstBillDetailVoid(billdetail.getOID());
                pstBillDetailVoid.setLong(FLD_BILL_MAIN_ID, billdetail.getBillMainId());
                pstBillDetailVoid.setLong(FLD_UNIT_ID, billdetail.getUnitId());
                pstBillDetailVoid.setLong(FLD_MATERIAL_ID, billdetail.getMaterialId());
                pstBillDetailVoid.setString(FLD_ITEM_NAME, billdetail.getItemName());
                pstBillDetailVoid.setDouble(FLD_ITEM_PRICE, billdetail.getItemPrice());
                pstBillDetailVoid.setInt(FLD_DISC_TYPE, billdetail.getDiscType());
                pstBillDetailVoid.setDouble(FLD_DISC, billdetail.getDisc());
                pstBillDetailVoid.setDouble(FLD_QUANTITY, billdetail.getQty());
                pstBillDetailVoid.setDouble(FLD_TOTAL_PRICE, billdetail.getTotalPrice());
                pstBillDetailVoid.setString(FLD_SKU, billdetail.getSku());
                pstBillDetailVoid.setInt(FLD_MATERIAL_TYPE, billdetail.getMaterialType());
                pstBillDetailVoid.setDouble(FLD_COST, billdetail.getCost());
                pstBillDetailVoid.setDouble(FLD_DISC_PCT, billdetail.getDiscPct());
                pstBillDetailVoid.setDouble(FLD_QTY_STOCK, billdetail.getQtyStock());
                pstBillDetailVoid.setDouble(FLD_ITEM_PRICE_STOCK, billdetail.getItemPriceStock());
                pstBillDetailVoid.setDouble(FLD_DISC_GLOBAL, billdetail.getDiscGlobal());
                pstBillDetailVoid.setDouble(FLD_DISC1, billdetail.getDisc1());
                pstBillDetailVoid.setDouble(FLD_DISC2, billdetail.getDisc2());
                pstBillDetailVoid.setDouble(FLD_TOTAL_DISC, billdetail.getTotalDisc());
                pstBillDetailVoid.setString(FLD_NOTE, billdetail.getNote());
                pstBillDetailVoid.setInt(FLD_STATUS, billdetail.getStatus());
                pstBillDetailVoid.setInt(FLD_STATUS_PRINT, billdetail.getStatusPrint());
                pstBillDetailVoid.setDate(FLD_LENGTH_OF_ORDER, billdetail.getLengthOrder());
                
                pstBillDetailVoid.update();
                return billdetail.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBillDetailVoid(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstBillDetailVoid pstBillDetailVoid = new PstBillDetailVoid(oid);
            pstBillDetailVoid.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBillDetailVoid(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

     public static Vector listTmp(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CASH_BILL_DETAIL;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
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
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            }

            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Billdetail billdetail = new Billdetail();
                resultToObject(rs, billdetail);
                lists.add(billdetail);
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

     /**
     * di cek dimana yang makai seperti ini?
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CASH_BILL_DETAIL;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
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
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                //Vector temp = new Vector();
                Billdetail billdetail = new Billdetail();

                //resultToObject(rs, billdetail);
                billdetail.setOID(rs.getLong("CASH_BILL_DETAIL_ID"));
                billdetail.setBillMainId(rs.getLong("CASH_BILL_MAIN_ID"));
                billdetail.setUnitId(rs.getLong("UNIT_ID"));
                billdetail.setMaterialId(rs.getLong("MATERIAL_ID"));
                billdetail.setSku(rs.getString("SKU"));
                billdetail.setItemName(rs.getString("ITEM_NAME"));
                billdetail.setQty(rs.getInt("QTY"));
                billdetail.setItemPrice(rs.getDouble("ITEM_PRICE"));
                billdetail.setDisc(rs.getDouble("DISC"));

                billdetail.setDisc1(rs.getDouble("DISC1"));
                billdetail.setDisc2(rs.getDouble("DISC2"));
                billdetail.setTotalDisc(rs.getDouble("TOTAL_DISC"));

                billdetail.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                billdetail.setNote(rs.getString("NOTE"));
                //temp.add(billdetail);
                // lists.add(temp);
                lists.add(billdetail);
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

    /**
     * untuk mencari list item barang yang di pesan
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
     public static Vector listDetailInvoicing(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CASH_BILL_DETAIL;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
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
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector();
                Billdetail billdetail = new Billdetail();

                //resultToObject(rs, billdetail);
                billdetail.setOID(rs.getLong("CASH_BILL_DETAIL_ID"));
                billdetail.setBillMainId(rs.getLong("CASH_BILL_MAIN_ID"));
                billdetail.setUnitId(rs.getLong("UNIT_ID"));
                billdetail.setMaterialId(rs.getLong("MATERIAL_ID"));
                billdetail.setSku(rs.getString("SKU"));
                billdetail.setItemName(rs.getString("ITEM_NAME"));
                billdetail.setQty(rs.getInt("QTY"));
                billdetail.setItemPrice(rs.getDouble("ITEM_PRICE"));
                billdetail.setDisc1(rs.getDouble("DISC1"));
                billdetail.setDisc2(rs.getDouble("DISC2"));
                billdetail.setDisc(rs.getDouble("DISC"));
                billdetail.setTotalDisc(rs.getDouble("TOTAL_DISC"));
                billdetail.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                billdetail.setNote(rs.getString("NOTE"));
                temp.add(billdetail);
                lists.add(temp);
                //lists.add(billdetail);
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


    /**
     * Ari wiweka 20130721
     * List material for cashier
     * @param rs
     * @param billdetail
     */
    public static Vector listMat(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT CD.*, "
                    + " UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE]
                    + ", pm."+PstMaterial.fieldNames[PstMaterial.FLD_BARCODE]
                    + " FROM " + TBL_CASH_BILL_DETAIL + " AS CD INNER JOIN "
                    + PstUnit.TBL_P2_UNIT + " AS UNT ON "
                    + " CD." + fieldNames[PstBillDetailVoid.FLD_UNIT_ID] + " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " INNER JOIN pos_material AS pm ON pm.MATERIAL_ID=CD.MATERIAL_ID";

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
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
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector();
                Billdetail billdetail = new Billdetail();
                Unit unit = new Unit();
                //resultToObject(rs, billdetail);
                billdetail.setOID(rs.getLong("CASH_BILL_DETAIL_ID"));
                billdetail.setBillMainId(rs.getLong("CASH_BILL_MAIN_ID"));
                billdetail.setUnitId(rs.getLong("UNIT_ID"));
                billdetail.setMaterialId(rs.getLong("MATERIAL_ID"));
                billdetail.setSku(rs.getString("SKU"));
                billdetail.setItemName(rs.getString("ITEM_NAME"));
                billdetail.setQty(rs.getInt("QTY"));
                billdetail.setItemPrice(rs.getDouble("ITEM_PRICE"));
                billdetail.setDisc1(rs.getDouble("DISC1"));
                billdetail.setDisc2(rs.getDouble("DISC2"));
                billdetail.setDisc(rs.getDouble("DISC"));
                billdetail.setTotalDisc(rs.getDouble("TOTAL_DISC"));
                billdetail.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                billdetail.setNote(rs.getString("NOTE"));
                billdetail.setBarcodeMat(rs.getLong("BARCODE"));
                billdetail.setStatus(rs.getInt("STATUS"));
                temp.add(billdetail);

                unit.setCode(rs.getString("CODE"));
                temp.add(unit);

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

    public static void resultToObject(ResultSet rs, Billdetail billdetail) {
        try {
            billdetail.setOID(rs.getLong(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_DETAIL_ID]));
            billdetail.setBillMainId(rs.getLong(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID]));
            billdetail.setUnitId(rs.getLong(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_UNIT_ID]));
            billdetail.setMaterialId(rs.getLong(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_MATERIAL_ID]));
            billdetail.setItemName(rs.getString(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_ITEM_NAME]));
            billdetail.setItemPrice(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_ITEM_PRICE]));
            billdetail.setDiscType(rs.getInt(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_DISC_TYPE]));
            billdetail.setDisc(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_DISC]));
            billdetail.setQty(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_QUANTITY]));
            billdetail.setTotalPrice(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_TOTAL_PRICE]));
            billdetail.setSku(rs.getString(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_SKU]));
            billdetail.setMaterialType(rs.getInt(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_MATERIAL_TYPE]));
            billdetail.setCost(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_COST]));
            billdetail.setDiscPct(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_DISC_PCT]));
            billdetail.setQtyStock(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_QTY_STOCK]));
            billdetail.setItemPriceStock(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_ITEM_PRICE_STOCK]));
            billdetail.setDiscGlobal(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_DISC_GLOBAL]));
            billdetail.setDisc1(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_DISC1]));
            billdetail.setDisc2(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_DISC2]));
            billdetail.setTotalDisc(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_TOTAL_DISC]));
            billdetail.setNote(rs.getString(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_NOTE]));
            billdetail.setStatus(rs.getInt(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_STATUS]));
            billdetail.setStatusPrint(rs.getInt(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_STATUS_PRINT]));
            
        } catch (Exception e) {
            System.out.println("err>>> : " + e.toString());
        }
    }
    //Discount Type
    public static int DISC_TYPE_PERCENT = 1;
    public static int DISC_TYPE_VALUE = 0;

    public static boolean checkOID(long billDetailId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CASH_BILL_DETAIL
                    + " WHERE " + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_DETAIL_ID]
                    + " = " + billDetailId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static int getCount(String whereClause) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[FLD_BILL_DETAIL_ID] + ") FROM " + TBL_CASH_BILL_DETAIL;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

       /**
     * Ari_wiweka 20130730
     * cek untuk item yang sudah exist
     */
    public static int getCountExist(long oidBillMain, long oidMaterial) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(CD."+fieldNames[PstBillDetailVoid.FLD_BILL_DETAIL_ID]+") "
                    + " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+" AS CM INNER JOIN "+TBL_CASH_BILL_DETAIL+" AS CD "
                    + " ON CM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = CD."+fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID]
                    + " WHERE CM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = '"+oidBillMain+"' "
                    + " AND CD."+fieldNames[PstBillDetailVoid.FLD_MATERIAL_ID]+" = '"+oidMaterial+"'";


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    //Menghitung Quantity Stock yang tersisa
     public static double getCountStock(long idBillMain, long matId) {

        double count = 0;
        double sum = 0;
        DBResultSet dbrs = null;
        try {
            // String sql = "SELECT " +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+ " FROM " +PstBillMain.TBL_CASH_BILL_MAIN+
            //   " WHERE " +PstBillMain.fieldNames[PstBillMain.FLD_PARENT_ID]+ " = '"+idBillMain+"'";

            // sql = "SELECT  SUM("+fieldNames[FLD_QUANTITY]+") FROM " +TBL_CASH_BILL_DETAIL+
            //   " WHERE "+ fieldNames[FLD_BILL_MAIN_ID] +" =(" +sql+ ") AND " +fieldNames[FLD_MATERIAL_ID]+ " ='"+matId+"'" ;

            String sql = "SELECT SUM(cd." + fieldNames[FLD_QUANTITY] + ") FROM " +
                    TBL_CASH_BILL_DETAIL + " AS cd INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS cm "
                    + " ON cd." + fieldNames[FLD_BILL_MAIN_ID] + " = cm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE cm." + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_ID] + " = '" + idBillMain
                    + "' AND cd." + fieldNames[FLD_MATERIAL_ID]+ " = '" + matId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rsTmp = dbrs.getResultSet();
            while (rsTmp.next()) {
                sum = rsTmp.getDouble(1);
            }
            rsTmp.close();
            if (sum == 0) {
                sql = "'0'";
            }

            sql = "SELECT ((SELECT SUM(" + fieldNames[FLD_QUANTITY] + ") FROM " + TBL_CASH_BILL_DETAIL
                    + " WHERE " + fieldNames[FLD_BILL_MAIN_ID] + " ='" + idBillMain
                    + "' AND " + fieldNames[FLD_MATERIAL_ID] + " = '" + matId + "') - (" + sql + ")) AS sisaStock";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static double getSumTotalItem(String whereClause) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + fieldNames[FLD_TOTAL_PRICE] + ") FROM " + TBL_CASH_BILL_DETAIL;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

    public static double getSumTotalItemBruto(String whereClause) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + fieldNames[FLD_ITEM_PRICE] + ") FROM " + TBL_CASH_BILL_DETAIL;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

    public static double getTotalCOGS(long lBillMainOid) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_COST]
                    + "*" + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_QUANTITY] + ")"
                    + " FROM " + TBL_CASH_BILL_DETAIL
                    + " WHERE " + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID]
                    + " = " + lBillMainOid;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }


    public static long getCashBillDetailId(long lBillMainOid, long materialId) {
        long count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT (" + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_DETAIL_ID]+ ")"
                    + " FROM " + TBL_CASH_BILL_DETAIL
                    + " WHERE " + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID]
                    + " = '" + lBillMainOid+"'"
                    + " AND " + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_MATERIAL_ID]
                    + " = '"+ materialId+"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getLong(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    /**
     * Untuk menghitung total penjualan
     * Ari_wiweka 20130701
     * @param oidCashCashier
     * @return
     */
    public static double getTotalAmount(long oidBillMain) {
        double count = 0;
        double sum = 0;
        DBResultSet dbrs = null;
        try {
            String sql1 = "SELECT SUM(CD."+PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_TOTAL_PRICE]+") FROM "+PstBillDetailVoid.TBL_CASH_BILL_DETAIL+" AS CD "
                        + " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" AS CM ON CD."+PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID]+" = CM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                        + " WHERE CM."+PstBillMain.fieldNames[PstBillMain.FLD_PARENT_ID]+" = '"+oidBillMain+"'";


            dbrs = DBHandler.execQueryResult(sql1);
            ResultSet rsTmp = dbrs.getResultSet();
            while (rsTmp.next()) {
                sum = rsTmp.getDouble(1);
            }

            rsTmp.close();
            if (sum == 0) {
                sql1 = "'0'";
            }

            String sql = "SELECT SUM(" + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_TOTAL_PRICE] + ")"
                    + " FROM " + TBL_CASH_BILL_DETAIL
                    + " WHERE " + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID]
                    + " = " + oidBillMain;

            sql = "SELECT ("+sql+") - ("+sql1+") AS totalJumlah";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    /**
     * Ari wiweka 20130716
     * delete bill detail berdasarkan bill main
     * @param oidCashCashier
     * @return
     */
    public static long deleteBillDetail(long oidBillMain) {

        String sql = "DELETE * FROM" + TBL_CASH_BILL_DETAIL
                + " WHERE " + fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID] + " = '" + oidBillMain + "'";

        try {
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
        }
        return oidBillMain;
    }

    public static double getReturSales(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
           // String sql = "SELECT SUM(" + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_TOTAL_PRICE] + ")" +
            String sql = "SELECT SUM(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_RATE] + " * CBD." + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_TOTAL_PRICE] + ")"
                    + //adding tax, service
           //by mirahu 31122011
           //adding discount
           //by opie-eyek 11102012
                    ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE] + " AS TAX_VALUE"
                    + ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE] + " AS SERVICE_VALUE"
                    + ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT] + " AS DISCOUNT_VALUE"
                    + " FROM " + TBL_CASH_BILL_DETAIL + " CBD"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CBD." + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "= 1"
                    + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!= 2"
                    + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]
                    + " = " + oidCashCashier
                    + //group by cash bill main id
            //by mirahu 31122011
                    " GROUP BY CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " ORDER BY " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                //count = rs.getInt(1);
                count += rs.getDouble(1) + rs.getDouble("TAX_VALUE") + rs.getDouble("SERVICE_VALUE") - rs.getDouble("DISCOUNT_VALUE");
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public boolean isCatalogUsed(long oid) {

        String where = fieldNames[FLD_MATERIAL_ID] + "=" + oid;
        int count = getCount(where);

        if (count > 0) {
            return true;
        }
        return false;

    }
    
    public static int updateStatusItem(long oidBillDetail, int statusOrder) {
            int ud=0;
            Date now = new Date();
            String sql = "UPDATE " + TBL_CASH_BILL_DETAIL
                    + " SET " + fieldNames[FLD_STATUS] + " = '" + statusOrder + "', "
                    + fieldNames[FLD_LENGTH_OF_FINISH_ORDER] + " = '" + Formater.formatDate(now, "yyyy-MM-dd hh:mm:ss") + "' "
                    + " WHERE " + fieldNames[FLD_BILL_DETAIL_ID] + " = '" + oidBillDetail + "'";

            try {
                
                DBHandler.execUpdate(sql);
                
            } catch (Exception e) {
                   ud=-1;
            }
            
        return ud;
    }
    
    
    public static boolean checkUpdateStatusItem(long billDetailId, int statusOrder) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CASH_BILL_DETAIL
                    + " WHERE " + PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_DETAIL_ID]
                    + " = " + billDetailId+
                    " AND "+PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_STATUS]+
                    " = 0";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static int updateStatusPrintItem(long oidBillDetail, int statusPrint) {
            int ud=0;
            
            String sql = "UPDATE " + TBL_CASH_BILL_DETAIL
                    + " SET " + fieldNames[FLD_STATUS_PRINT] + " = '" + statusPrint + "'"
                    + " WHERE " + fieldNames[FLD_BILL_DETAIL_ID] + " = '" + oidBillDetail + "'";

            try {
                
                DBHandler.execUpdate(sql);
                
            } catch (Exception e) {
                   ud=-1;
            }
            
        return ud;
    }

}