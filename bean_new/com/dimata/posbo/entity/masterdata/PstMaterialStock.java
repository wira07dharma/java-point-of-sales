package com.dimata.posbo.entity.masterdata;

/* package java */

import java.sql.*;
import java.util.*;
/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;

import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.entity.warehouse.PstMatReturn;
import com.dimata.posbo.entity.warehouse.PstMatReturnItem;
import com.dimata.posbo.entity.warehouse.PstMatDispatchItem;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.periode.Periode;
import com.dimata.posbo.entity.search.SrcMaterial;
import org.json.JSONObject;

public class PstMaterialStock extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MATERIAL_STOCK = "pos_material_stock";

    public static final int FLD_MATERIAL_STOCK_ID = 0;
    public static final int FLD_PERIODE_ID = 1;
    public static final int FLD_MATERIAL_UNIT_ID = 2;
    public static final int FLD_LOCATION_ID = 3;
    public static final int FLD_QTY = 4;
    public static final int FLD_QTY_MIN = 5;
    public static final int FLD_QTY_MAX = 6;
    public static final int FLD_QTY_IN = 7;
    public static final int FLD_QTY_OUT = 8;
    public static final int FLD_OPENING_QTY = 9;
    public static final int FLD_CLOSING_QTY = 10;
    public static final int FLD_QT_OPTIMUM = 11;
    
    public static final int FLD_BERAT = 12;
    public static final int FLD_BERAT_IN = 13;
    public static final int FLD_BERAT_OUT = 14;
    public static final int FLD_OPENING_BERAT = 15;
    public static final int FLD_CLOSING_BERAT = 16;
    public static final int FLD_UPDATE_DATE = 17;
    
    public static final String[] fieldNames = {
        "MATERIAL_STOCK_ID",
        "PERIODE_ID",
        "MATERIAL_UNIT_ID",
        "LOCATION_ID",
        "QTY",
        "QTY_MIN",
        "QTY_MAX",
        "QTY_IN",
        "QTY_OUT",
        "OPENING_QTY",
        "CLOSING_QTY",
        "QTY_OPTIMUM",
        "BERAT",
        "BERAT_IN",
        "BERAT_OUT",
        "OPENING_BERAT",
        "CLOSING_BERAT",
        "UPDATE_DATE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_DATE
    };

    public PstMaterialStock() {
    }

    public PstMaterialStock(int i) throws DBException {
        super(new PstMaterialStock());
    }

    public PstMaterialStock(String sOid) throws DBException {
        super(new PstMaterialStock(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMaterialStock(long lOid) throws DBException {
        super(new PstMaterialStock(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_MATERIAL_STOCK;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialStock().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MaterialStock materialStock = fetchExc(ent.getOID());
        ent = (Entity) materialStock;
        return materialStock.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((MaterialStock) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((MaterialStock) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MaterialStock fetchExc(long oid) throws DBException {
        try {
            MaterialStock materialStock = new MaterialStock();
            PstMaterialStock pstMaterialStock = new PstMaterialStock(oid);
            materialStock.setOID(oid);

            materialStock.setPeriodeId(pstMaterialStock.getlong(FLD_PERIODE_ID));
            materialStock.setMaterialUnitId(pstMaterialStock.getlong(FLD_MATERIAL_UNIT_ID));
            materialStock.setLocationId(pstMaterialStock.getlong(FLD_LOCATION_ID));
            materialStock.setQty(pstMaterialStock.getdouble(FLD_QTY));
            materialStock.setQtyMin(pstMaterialStock.getdouble(FLD_QTY_MIN));
            materialStock.setQtyMax(pstMaterialStock.getdouble(FLD_QTY_MAX));
            materialStock.setQtyIn(pstMaterialStock.getdouble(FLD_QTY_IN));
            materialStock.setQtyOut(pstMaterialStock.getdouble(FLD_QTY_OUT));
            materialStock.setOpeningQty(pstMaterialStock.getdouble(FLD_OPENING_QTY));
            materialStock.setClosingQty(pstMaterialStock.getdouble(FLD_CLOSING_QTY));
            materialStock.setQtyOptimum(pstMaterialStock.getdouble(FLD_QT_OPTIMUM));            
            materialStock.setBerat(pstMaterialStock.getdouble(FLD_BERAT));
            materialStock.setBeratIn(pstMaterialStock.getdouble(FLD_BERAT_IN));
            materialStock.setBeratOut(pstMaterialStock.getdouble(FLD_BERAT_OUT));
            materialStock.setOpeningBerat(pstMaterialStock.getdouble(FLD_OPENING_BERAT));
            materialStock.setClosingBerat(pstMaterialStock.getdouble(FLD_CLOSING_BERAT));
            materialStock.setUpdateDate(pstMaterialStock.getDate(FLD_UPDATE_DATE));
            return materialStock;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialStock(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MaterialStock materialStock) throws DBException {
        try {
            PstMaterialStock pstMaterialStock = new PstMaterialStock(0);

            pstMaterialStock.setLong(FLD_PERIODE_ID, materialStock.getPeriodeId());
            pstMaterialStock.setLong(FLD_MATERIAL_UNIT_ID, materialStock.getMaterialUnitId());
            pstMaterialStock.setLong(FLD_LOCATION_ID, materialStock.getLocationId());
            pstMaterialStock.setDouble(FLD_QTY, materialStock.getQty());
            pstMaterialStock.setDouble(FLD_QTY_MIN, materialStock.getQtyMin());
            pstMaterialStock.setDouble(FLD_QTY_MAX, materialStock.getQtyMax());
            pstMaterialStock.setDouble(FLD_QTY_IN, materialStock.getQtyIn());
            pstMaterialStock.setDouble(FLD_QTY_OUT, materialStock.getQtyOut());
            pstMaterialStock.setDouble(FLD_OPENING_QTY, materialStock.getOpeningQty());
            pstMaterialStock.setDouble(FLD_CLOSING_QTY, materialStock.getClosingQty());
            pstMaterialStock.setDouble(FLD_QT_OPTIMUM, materialStock.getQtyOptimum());
            pstMaterialStock.setDouble(FLD_BERAT, materialStock.getBerat());
            pstMaterialStock.setDouble(FLD_BERAT_IN, materialStock.getBeratIn());
            pstMaterialStock.setDouble(FLD_BERAT_OUT, materialStock.getBeratOut());
            pstMaterialStock.setDouble(FLD_OPENING_BERAT, materialStock.getOpeningBerat());
            pstMaterialStock.setDouble(FLD_CLOSING_BERAT, materialStock.getClosingBerat());
            pstMaterialStock.setDate(FLD_UPDATE_DATE, materialStock.getUpdateDate());
            pstMaterialStock.insert();
            materialStock.setOID(pstMaterialStock.getlong(FLD_MATERIAL_STOCK_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialStock(0), DBException.UNKNOWN);
        }
        return materialStock.getOID();
    }

    public static long updateExc(MaterialStock materialStock) throws DBException {
        try {
            if (materialStock.getOID() != 0) {
                PstMaterialStock pstMaterialStock = new PstMaterialStock(materialStock.getOID());

                pstMaterialStock.setLong(FLD_PERIODE_ID, materialStock.getPeriodeId());
                pstMaterialStock.setLong(FLD_MATERIAL_UNIT_ID, materialStock.getMaterialUnitId());
                pstMaterialStock.setLong(FLD_LOCATION_ID, materialStock.getLocationId());
                pstMaterialStock.setDouble(FLD_QTY, materialStock.getQty());
                pstMaterialStock.setDouble(FLD_QTY_MIN, materialStock.getQtyMin());
                pstMaterialStock.setDouble(FLD_QTY_MAX, materialStock.getQtyMax());
                pstMaterialStock.setDouble(FLD_QTY_IN, materialStock.getQtyIn());
                pstMaterialStock.setDouble(FLD_QTY_OUT, materialStock.getQtyOut());
                pstMaterialStock.setDouble(FLD_OPENING_QTY, materialStock.getOpeningQty());
                pstMaterialStock.setDouble(FLD_CLOSING_QTY, materialStock.getClosingQty());
                pstMaterialStock.setDouble(FLD_QT_OPTIMUM, materialStock.getQtyOptimum());
                pstMaterialStock.setDouble(FLD_BERAT, materialStock.getBerat());
                pstMaterialStock.setDouble(FLD_BERAT_IN, materialStock.getBeratIn());
                pstMaterialStock.setDouble(FLD_BERAT_OUT, materialStock.getBeratOut());
                pstMaterialStock.setDouble(FLD_OPENING_BERAT, materialStock.getOpeningBerat());
                pstMaterialStock.setDouble(FLD_CLOSING_BERAT, materialStock.getClosingBerat());
                pstMaterialStock.setDate(FLD_UPDATE_DATE, materialStock.getUpdateDate());
                pstMaterialStock.update();
                return materialStock.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialStock(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMaterialStock pstMaterialStock = new PstMaterialStock(oid);
            pstMaterialStock.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialStock(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_STOCK;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

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
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();
                resultToObject(rs, materialStock);
                lists.add(materialStock);
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
     * Ari_wiweka 20130902
     * Menghitung jumlah stock di gudang
     * @param rs
     * @param materialStock
     */

    public static double getStockWh(long locId, long matId) {
        return getStockWh(locId,matId,0);
    }

    public static double getStockWh(long locId, long matId, long matPeriode) {
        double sum = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(MS."+fieldNames[PstMaterialStock.FLD_QTY]+")"
                    + " FROM " + TBL_MATERIAL_STOCK +" AS MS "
                    + " WHERE MS."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+ " = '"+locId+"'"
                    + " AND MS."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+ " = '"+matId+"'";

            if(matPeriode!=0){
                sql=sql+" AND MS."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+ " = '"+matPeriode+"'";
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                sum = rs.getInt(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return sum;
    }

    /*public static Vector listStockWh(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MS."+fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]
                    + ", MS."+fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
                    + ", MS."+fieldNames[PstMaterialStock.FLD_LOCATION_ID]
                    + ", MS."+fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]
                    + ", SUM(MS."+fieldNames[PstMaterialStock.FLD_QTY]+")"
                    + " FROM " + TBL_MATERIAL_STOCK +" AS MS";
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

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
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();
                resultToObject(rs, materialStock);
                lists.add(materialStock);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }*/

    public static void resultToObject(ResultSet rs, MaterialStock materialStock) {
        try {
            materialStock.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
            materialStock.setPeriodeId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]));
            materialStock.setMaterialUnitId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]));
            materialStock.setLocationId(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]));
            materialStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
            materialStock.setQtyMin(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN]));
            materialStock.setQtyMax(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX]));
            materialStock.setQtyIn(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN]));
            materialStock.setQtyOut(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT]));
            materialStock.setOpeningQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY]));
            materialStock.setClosingQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY]));
            materialStock.setQtyOptimum(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QT_OPTIMUM]));
            materialStock.setBerat(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT]));
            materialStock.setBeratIn(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT_IN]));
            materialStock.setBeratOut(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT_OUT]));
            materialStock.setOpeningBerat(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_BERAT]));
            materialStock.setClosingBerat(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_BERAT]));
            materialStock.setUpdateDate(rs.getTimestamp(PstMaterialStock.fieldNames[PstMaterialStock.FLD_UPDATE_DATE]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long materialStockId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_STOCK + " WHERE " +
                    fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] +
                    " = " + materialStockId;
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
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] + ") FROM " +
                    TBL_MATERIAL_STOCK;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    /**
     * @param oidMaterial
     * @param oidLocation
     * @return
     * @update by Edhy
     */
    public static long fetchByMaterialLocation(long oidMaterial, long oidLocation) {
        long hasil = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] +
                    " FROM " + PstPeriode.TBL_STOCK_PERIODE + " PRD" +
                    " INNER JOIN " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS" +
                    " ON PRD." + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] +
                    " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " WHERE PRD." + PstPeriode.fieldNames[PstPeriode.FLD_STATUS] +
                    " = " + PstPeriode.FLD_STATUS_RUNNING +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = " + oidMaterial +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + oidLocation;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                hasil = rs.getLong(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

    public static Connection getLocalConnection() {
        Connection koneksi = null;
        try {
            koneksi = getConnection();
        } catch (Exception x) {

        }
        return koneksi;
    }

    public static boolean closeLocalConnection(Connection koneksi) {
        boolean hasil = false;
        try {
            closeConnection(koneksi);
            hasil = true;
        } catch (Exception x) {

        }
        return hasil;
    }

    /**
     *
     * @param start
     * @param record
     * @param oidLocation
     * @param oidPeriode
     * @return
     */
    public static Vector getDaftarBarangStock(int start, int record, long oidLocation, long oidPeriode) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT * FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;

            if (record != 0)
                sql = sql + " ORDER BY M.NAME LIMIT " + start + "," + record;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();
                Material material = new Material();
                Vector temp = new Vector(1, 1);

                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                material.setBuyUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]));
                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
                temp.add(material);

                materialStock.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
                materialStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                temp.add(materialStock);

                list.add(temp);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    public static Vector getDaftarBarangStock(int start, int record, long oidLocation,
            long oidPeriode, long kategori, String code, String nama) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT * FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE ;

            if (kategori != 0)
                sql = sql + " AND M.CATEGORY_ID = " + kategori;

            if (code.length() > 0) {
                sql += " AND (M."+ PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + code + "%'";
                sql += " OR M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + code + "%')";
            }

            if (nama.length() > 0) {
                sql = sql + " AND M.NAME LIKE '%" + nama + "%'";
            }

            if (record != 0)
                sql = sql + " ORDER BY M.NAME LIMIT " + start + "," + record;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double price = 0;
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();
                Material material = new Material();
                Vector temp = new Vector(1, 1);

                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                material.setBuyUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]));

               /*
                if(PstMaterial.STATUS_PRICE_CONDITION==PstMaterial.STATUS_WITH_HPP){
                    price = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]);
                }else{
                    price = PstPriceTypeMapping.getSellPrice(material.getOID(),);
                }*/

                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));


                temp.add(material);

                materialStock.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
                materialStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                temp.add(materialStock);

                list.add(temp);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


      public static Vector getDaftarBarangStockorGlobalStock(int start, int record, long oidLocation,
            long oidPeriode, long kategori, String code, String nama, int txtViewItem) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT * FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                         " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID ";

            if(txtViewItem==1){

               sql =sql+ " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                         " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                         " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
                }

               sql=sql + " WHERE MS.LOCATION_ID = " + oidLocation +
                         " AND MS.PERIODE_ID = " + oidPeriode +
                         " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE +
                         " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + "!=" + PstMaterial.EDIT_NON_AKTIVE ;
            if(txtViewItem==1){
               sql = sql + " AND MS.QTY <> 0 AND LENGTH(M.NAME )>2" ;
            }
            if (kategori != 0)
                sql = sql + " AND M.CATEGORY_ID = " + kategori;

            if (code.length() > 0) {
                sql += " AND (M."+ PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + code + "%'";
                sql += " OR M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + code + "%')";
            }

            if (nama.length() > 0) {
                sql = sql + " AND M.NAME LIKE '%" + nama + "%'";
            }

            if (record != 0)
                sql = sql + " ORDER BY M.NAME LIMIT " + start + "," + record;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double price = 0;
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();
                Material material = new Material();
                Vector temp = new Vector(1, 1);

                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                material.setBuyUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]));

               /*
                if(PstMaterial.STATUS_PRICE_CONDITION==PstMaterial.STATUS_WITH_HPP){
                    price = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]);
                }else{
                    price = PstPriceTypeMapping.getSellPrice(material.getOID(),);
                }*/

                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));


                temp.add(material);

                materialStock.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
                materialStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                temp.add(materialStock);

                list.add(temp);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector getDaftarBarangStock(int start, int record, long oidLocation,
            long oidPeriode, long kategori, String code, String nama, int typeConsig) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT * FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE+
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CONSIGMENT_TYPE] + "="+typeConsig;

            if (kategori != 0)
                sql = sql + " AND M.CATEGORY_ID = " + kategori;

            if (code.length() > 0) {
                sql = sql + " AND M.SKU LIKE '%" + code + "%'";
            }

            if (nama.length() > 0) {
                sql = sql + " AND M.NAME LIKE '%" + nama + "%'";
            }

            if (record != 0)
                sql = sql + " ORDER BY M.NAME LIMIT " + start + "," + record;
            System.out.println("getDaftarSemuaBarang>>\n"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double price = 0;
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();
                Material material = new Material();
                Vector temp = new Vector(1, 1);

                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                material.setBuyUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]));

               /*
                if(PstMaterial.STATUS_PRICE_CONDITION==PstMaterial.STATUS_WITH_HPP){
                    price = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]);
                }else{
                    price = PstPriceTypeMapping.getSellPrice(material.getOID(),);
                }*/

                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));


                temp.add(material);

                materialStock.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
                materialStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                temp.add(materialStock);

                list.add(temp);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /**
     * get data stock yang link juga ke supplier
     * @param start
     * @param record
     * @param oidLocation
     * @param oidPeriode
     * @param supplierId
     * @return
     */
    public static Vector getDaftarBarangStock(int start, int record, long oidLocation, long oidPeriode, long supplierId) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT * FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " INNER JOIN "+ PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE+" AS VP ON M.MATERIAL_ID = VP.MATERIAL_ID" +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode +
                    " AND VP.VENDOR_ID = " + supplierId +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;

            if (record != 0)
                sql = sql + " ORDER BY M.NAME LIMIT " + start + "," + record;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();
                Material material = new Material();
                Vector temp = new Vector(1, 1);

                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                material.setBuyUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]));
                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
                temp.add(material);

                materialStock.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
                materialStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                temp.add(materialStock);

                list.add(temp);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }
 
     /**
     * get data stock tanpa composit for dispatch unit
     * @param start
     * @param record
     * @param oidLocation
     * @param oidPeriode
     * @param kategori
     * @param code
     * @param nama
     * @param whereAdd
     * @return
     */
    
    public static Vector getDaftarBarangStockUnit(int start, int record, long oidLocation,
            long oidPeriode, long kategori, String code, String nama, String whereAdd) {
        DBResultSet dbrs = null;
        Vector list = new Vector(1, 1);
        try {
            String sql = "SELECT * FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "!=" + PstMaterial.MATERIAL_TYPE_COMPOSITE +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE ;

//            if (kategori != 0)
//                sql = sql + " AND M.CATEGORY_ID = " + kategori;
            if (kategori> 0) {
                //buatkan seperti
                sql += " AND ( M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + kategori;
                
                String where = PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]+"='"+kategori+"' OR "+
                               PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+"='"+kategori+"'";
                
                Vector masterCatAcak = PstCategory.list(0,0,where,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                
                if(masterCatAcak.size()>1){
                    //Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                    //Vector<Long> levelParent = new Vector<Long>();
                    for(int i=0; i<masterCatAcak.size(); i++) {
                         Category mGroup = (Category)masterCatAcak.get(i);
                         sql +=" OR M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                sql +=")";
            } 
            
            if (code.length() > 0) {
                sql += " AND (M."+ PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + code + "%'";
                sql += " OR M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + code + "%')";
            }

            if (nama.length() > 0) {
                sql = sql + " AND M.NAME LIKE '%" + nama + "%'";
            }

            //updated by dewok 2018-02-06
            if (whereAdd.length() > 0) {
                sql += whereAdd;
            }

            if (record != 0)
                sql = sql + " ORDER BY M.NAME LIMIT " + start + "," + record;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double price = 0;
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();
                Material material = new Material();
                Vector temp = new Vector(1, 1);

                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                material.setDefaultPrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                material.setBuyUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID]));

               /*
                if(PstMaterial.STATUS_PRICE_CONDITION==PstMaterial.STATUS_WITH_HPP){
                    price = rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]);
                }else{
                    price = PstPriceTypeMapping.getSellPrice(material.getOID(),);
                }*/

                material.setAveragePrice(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));


                temp.add(material);

                materialStock.setOID(rs.getLong(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID]));
                materialStock.setQty(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                materialStock.setBerat(rs.getDouble(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT]));
                temp.add(materialStock);

                list.add(temp);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }
        
    public static Vector getDaftarBarangStockUnit(int start, int record, long oidLocation,
            long oidPeriode, long kategori, String code, String nama) {
        return getDaftarBarangStockUnit(start, record, oidLocation, oidPeriode, kategori, code, nama, "");
    }

    /**
     *
     * @param oidLocation
     * @param oidPeriode
     * @return
     */
    public static int getCountDaftarBarangStock(long oidLocation, long oidPeriode) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(MS.MATERIAL_STOCK_ID) FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }


    public static int getCountDaftarBarangStock(long oidLocation, long oidPeriode, long kategori, String code, String name) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(MS.MATERIAL_STOCK_ID) FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;

            if (kategori != 0)
                sql = sql + " AND M.CATEGORY_ID=" + kategori;

            if (code.length() > 0) {
                sql += " AND (M."+ PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + code + "%'";
                sql += " OR M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + code + "%')";
            }

            if (name.length() > 0) {
                sql = sql + " AND M.NAME LIKE '%" + name + "%'";
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

     public static int getCountDaftarBarangStockorGlobalStock(long oidLocation, long oidPeriode, long kategori, String code, String name, int txtViewItem) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(MS.MATERIAL_STOCK_ID) FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                         " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID ";

            if(txtViewItem==1){

               sql =sql+ " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " AS U " + //UNIT
                         " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " = U." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " LEFT JOIN " + PstCategory.TBL_CATEGORY + " AS C " +
                         " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];
                }

               sql=sql + " WHERE MS.LOCATION_ID = " + oidLocation +
                         " AND MS.PERIODE_ID = " + oidPeriode +
                         " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE +
                         " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + "!=" + PstMaterial.EDIT_NON_AKTIVE ;

            if(txtViewItem==1){
               sql = sql + " AND MS.QTY <> 0 AND LENGTH(M.NAME )>2" ;
            }

            if (kategori != 0)
                sql = sql + " AND M.CATEGORY_ID=" + kategori;

            if (code.length() > 0) {
                sql += " AND (M."+ PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + code + "%'";
                sql += " OR M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + code + "%')";
            }

            if (name.length() > 0) {
                sql = sql + " AND M.NAME LIKE '%" + name + "%'";
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static int getCountDaftarBarangStock(long oidLocation, long oidPeriode, long kategori,
            String code, String name, int typeConsig) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(MS.MATERIAL_STOCK_ID) FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE+
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_CONSIGMENT_TYPE] + "=" + typeConsig;

            if (kategori != 0)
                sql = sql + " AND M.CATEGORY_ID=" + kategori;

            if (code.length() > 0) {
                sql = sql + " AND M.SKU LIKE '%" + code + "%'";
            }

            if (name.length() > 0) {
                sql = sql + " AND M.NAME LIKE '%" + name + "%'";
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    /**
     * UNTUK COUNT PENGAMBILAN DATA YANG DARI STOCK
     * BERDASARKAN SUPPLIER ID
     * @param oidLocation
     * @param oidPeriode
     * @param supplierId
     * @return
     */
    public static int getCountDaftarBarangStock(long oidLocation, long oidPeriode, long supplierId) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(MS.MATERIAL_STOCK_ID) FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " INNER JOIN "+ PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE+" AS VP ON M.MATERIAL_ID = VP.MATERIAL_ID" +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode +
                    " AND VP.VENDOR_ID = " + supplierId +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

     /**
     * For dispatch Unit
     * @param oidSupplier
     * @return
     */
    public static int getCountDaftarBarangStockUnit(long oidLocation, long oidPeriode, long kategori, String code, String name) {
        return getCountDaftarBarangStockUnit(oidLocation,oidPeriode,kategori,code,name,"");
    }
    
    public static int getCountDaftarBarangStockUnit(long oidLocation, long oidPeriode, long kategori, String code, String name, String whereAdd) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(MS.MATERIAL_STOCK_ID) FROM "+ TBL_MATERIAL_STOCK +" AS MS " +
                    " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS M ON MS.MATERIAL_UNIT_ID = M.MATERIAL_ID " +
                    " WHERE MS.LOCATION_ID = " + oidLocation +
                    " AND MS.PERIODE_ID = " + oidPeriode +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "!=" + PstMaterial.MATERIAL_TYPE_COMPOSITE +
                    " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;

//            if (kategori != 0)
//                sql = sql + " AND M.CATEGORY_ID=" + kategori;
             if (kategori> 0) {
                //buatkan seperti
                sql += " AND ( M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + kategori;
                
                String where = PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]+"='"+kategori+"' OR "+
                               PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+"='"+kategori+"'";
                
                Vector masterCatAcak = PstCategory.list(0,0,where,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                    Vector<Long> levelParent = new Vector<Long>();
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         sql +=" OR M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                sql +=")";
            } 
            if (code.length() > 0) {
                sql += " AND (M."+ PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + code + "%'";
                sql += " OR M." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + code + "%')";
            }

            if (name.length() > 0) {
                sql = sql + " AND M.NAME LIKE '%" + name + "%'";
            }
            
            //added by dewok for litama 2018-02-06
            if (whereAdd.length() > 0) {
                sql += whereAdd;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("error list : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }


    /**
     *
     * @param oidSupplier
     * @return
     */
    public static Vector listMaterialStockCurrPeriod(long oidSupplier) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS" +
                    " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " MAT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                    " = " + oidSupplier +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + currPeriod.getOID() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " > 0" +
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE +
                    " ORDER BY MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();

                materialStock.setOID(rs.getLong(1));
                materialStock.setPeriodeId(rs.getLong(2));
                materialStock.setMaterialUnitId(rs.getLong(3));
                materialStock.setLocationId(rs.getLong(4));
                materialStock.setQty(rs.getDouble(5));
                materialStock.setQtyMin(rs.getDouble(6));
                materialStock.setQtyMax(rs.getDouble(7));
                materialStock.setQtyIn(rs.getDouble(8));
                materialStock.setQtyOut(rs.getDouble(9));
                materialStock.setOpeningQty(rs.getDouble(10));
                materialStock.setClosingQty(rs.getDouble(11));

                result.add(materialStock);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * @param oidSupplier
     * @return
     * @update by Edhy
     */
    public static Vector listMaterialStockCurrPeriod(long oidSupplier, long oidLocation) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT DISTINCT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";
            if (oidSupplier != 0) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " MAT" +
                        " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                        " = MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }
            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + currPeriod.getOID() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + oidLocation +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " > 0" +
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;
            if (oidSupplier != 0) {
                sql = sql + " AND MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + oidSupplier;
            }
            sql = sql + " ORDER BY MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            //System.out.println("listMaterialStockCurrPeriod >>> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();

                materialStock.setOID(rs.getLong(1));
                materialStock.setPeriodeId(rs.getLong(2));
                materialStock.setMaterialUnitId(rs.getLong(3));
                materialStock.setLocationId(rs.getLong(4));
                materialStock.setQty(rs.getDouble(5));
                materialStock.setQtyMin(rs.getDouble(6));
                materialStock.setQtyMax(rs.getDouble(7));
                materialStock.setQtyIn(rs.getDouble(8));
                materialStock.setQtyOut(rs.getDouble(9));
                materialStock.setOpeningQty(rs.getDouble(10));
                materialStock.setClosingQty(rs.getDouble(11));

                result.add(materialStock);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * Fungsi ini digunakan untuk mendapatkan list material yang masih memiliki stok pada periode aktif
     * create by: gwawan@dimata 4 Dec 2007
     * @param SrcMaterial
     * @param int
     * @param int
     * @param String
     * @return Vector
     */
    public static Vector listMaterialStockCurrPeriod(SrcMaterial objSrcMaterial, int start, int recordToGet, String orderBy) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT DISTINCT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";

            if(objSrcMaterial.getSupplierId() != 0) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " MAT" +
                        " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                        " = MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }
            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + currPeriod.getOID() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + objSrcMaterial.getLocationId() +
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE+
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + "!=4"+
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;
            //opsi for stock nol
            if(objSrcMaterial.getShowStokNol()>0){
                sql = sql + " AND (MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " <= 0";
                sql = sql + " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " > 0)";
            }
            else {
                sql = sql + " AND (MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " > 0 OR MS."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT]+" > 0) ";
            }

            if(objSrcMaterial.getSupplierId() > 0) {
                sql = sql + " AND MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + objSrcMaterial.getSupplierId();
            }
            if(objSrcMaterial.getCategoryId() > 0) {
                //sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + objSrcMaterial.getCategoryId();
                 String strGroup = " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + objSrcMaterial.getCategoryId();
                
                Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak,objSrcMaterial.getCategoryId()) ;
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         strGroup=strGroup + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                
                strGroup=strGroup+")";
                sql = sql + strGroup;
            }
            if(objSrcMaterial.getMatcode() != "") {
                //sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'";
                //update search by barcode
                sql += " AND (MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'";
                sql += " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getMatcode() + "%')";
            }
            if(objSrcMaterial.getMatname() != "") {
                sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + objSrcMaterial.getMatname() + "%'";
            }
            sql = sql + " ORDER BY MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

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

            //System.out.println("listMaterialStockCurrPeriod():"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();

                materialStock.setOID(rs.getLong(1));
                materialStock.setPeriodeId(rs.getLong(2));
                materialStock.setMaterialUnitId(rs.getLong(3));
                materialStock.setLocationId(rs.getLong(4));
                materialStock.setQty(rs.getDouble(5));
                materialStock.setQtyMin(rs.getDouble(6));
                materialStock.setQtyMax(rs.getDouble(7));
                materialStock.setQtyIn(rs.getDouble(8));
                materialStock.setQtyOut(rs.getDouble(9));
                materialStock.setOpeningQty(rs.getDouble(10));
                materialStock.setClosingQty(rs.getDouble(11));
                materialStock.setBerat(rs.getDouble(12));

                result.add(materialStock);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * Fungsi ini digunakan untuk mendapatkan jumlah material yang masih memiliki stok pada periode aktif
     * create by: gwawan@dimata 4 Dec 2007
     * @param SrcMaterial
     * @return int
     */
    public static int countMaterialStockCurrPeriod(SrcMaterial objSrcMaterial) {
        int result = 0;
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT COUNT(DISTINCT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] + ")" +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";
            if(objSrcMaterial.getSupplierId() != 0) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " MAT" +
                        " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                        " = MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }
            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + currPeriod.getOID() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + objSrcMaterial.getLocationId() +
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE+
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + "!=4"+
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;
            
              //opsi for stock nol
            if(objSrcMaterial.getShowStokNol()>0){
               // sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                   // " >= 0";
                 sql = sql + " AND (MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " <= 0";
                sql = sql + " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " > 0)";
            }
            else {
                sql = sql + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    " > 0";
            }

            if(objSrcMaterial.getSupplierId() > 0) {
                sql = sql + " AND MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + objSrcMaterial.getSupplierId();
            }
            if(objSrcMaterial.getCategoryId() > 0) {
                //sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + objSrcMaterial.getCategoryId();
                 String strGroup = " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + objSrcMaterial.getCategoryId();
                
                Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak,objSrcMaterial.getCategoryId()) ;
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         strGroup=strGroup + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                
                strGroup=strGroup+")";
                sql += strGroup;
            }
            
            if(objSrcMaterial.getMatcode() != "") {
                //sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'";
                //update search by barcode
                sql += " AND (MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'";
                sql += " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getMatcode() + "%')";
            }
            if(objSrcMaterial.getMatname() != "") {
                sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + objSrcMaterial.getMatname() + "%'";
            }
            if(objSrcMaterial.getBarCode() != "") {
               sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getBarCode() + "%'";
           }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    //Fungsi untuk mendapatkan qty stok all di search penerimaan
    public static Vector listMaterialStockCurrPeriodAll(SrcMaterial objSrcMaterial, int start, int recordToGet, String orderBy) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT DISTINCT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY] +
                    ", MT.*"+
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";

            if(objSrcMaterial.getSupplierId() != 0) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " MAT" +
                        " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                        " = MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }
            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + currPeriod.getOID() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + objSrcMaterial.getLocationId() +
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;
            
            if(objSrcMaterial.getSupplierId() > 0) {
                sql = sql + " AND MT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + objSrcMaterial.getSupplierId();
            }
            
//            if(objSrcMaterial.getCategoryId() > 0) {
//                sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + objSrcMaterial.getCategoryId();
//            }
            //update opie-eyek 20140416
            if (objSrcMaterial.getCategoryId() > 0) {
                //buatkan seperti
                sql += " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + objSrcMaterial.getCategoryId();
                
                String where = PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]+"='"+objSrcMaterial.getCategoryId()+"' OR "+
                               PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+"='"+objSrcMaterial.getCategoryId()+"'";
                
                Vector masterCatAcak = PstCategory.list(0,0,where,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                    Vector<Long> levelParent = new Vector<Long>();
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         sql +=" OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                sql +=")";
            }
            //if(objSrcMaterial.getMatcode() != "") {
               // sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'";
           // }
            if(objSrcMaterial.getMatcode() != "") {
               sql += " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'" +
                      " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getMatcode() + "%')";
           }
            if(objSrcMaterial.getMatname() != "") {
                sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + objSrcMaterial.getMatname() + "%'";
            }
             //if(objSrcMaterial.getBarCode() != "") {
                //sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getBarCode() + "%'";
            //}
            sql = sql + " ORDER BY MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

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

            //System.out.println("listMaterialStockCurrPeriod():"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector addVec = new Vector();
                Material material = new Material();
                material.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDefaultCostCurrencyId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]));
                material.setDefaultStockUnitId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                addVec.add(material);
                
                MaterialStock materialStock = new MaterialStock();
                materialStock.setOID(rs.getLong(1));
                materialStock.setPeriodeId(rs.getLong(2));
                materialStock.setMaterialUnitId(rs.getLong(3));
                materialStock.setLocationId(rs.getLong(4));
                materialStock.setQty(rs.getDouble(5));
                materialStock.setQtyMin(rs.getDouble(6));
                materialStock.setQtyMax(rs.getDouble(7));
                materialStock.setQtyIn(rs.getDouble(8));
                materialStock.setQtyOut(rs.getDouble(9));
                materialStock.setOpeningQty(rs.getDouble(10));
                materialStock.setClosingQty(rs.getDouble(11));
                addVec.add(materialStock);
                
                result.add(addVec);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    //Fungsi untuk mendapatkan qty stok all di search penerimaan dan bisa disearch berdasarkan barcode
    public static Vector getlistMaterialStockCurrPeriodAllBarcode(SrcMaterial objSrcMaterial, int start, int recordToGet, String orderBy) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
              String sql = "SELECT DISTINCT " +
                    " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY] +
                    ", MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY] +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";

            if(objSrcMaterial.getSupplierId() != 0) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " MT" +
                        " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                        " = MT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }

            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

            String strGroup = "";
            if (objSrcMaterial.getCategoryId() > 0) {
                strGroup = " MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + objSrcMaterial.getCategoryId();
            }

            String strSubCategory = "";
            if (objSrcMaterial.getSubCategoryId() > 0) {
                strSubCategory = " MT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                        " = " + objSrcMaterial.getSubCategoryId();
            }

            String strCode = "";
            if (objSrcMaterial.getMatcode() != "" && objSrcMaterial.getMatcode().length() > 0) {
                strCode = " (MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                        " LIKE '%" + objSrcMaterial.getMatcode() + "%')";
            }

            String strBarcode = "";
            if(objSrcMaterial.getBarCode() != "" && objSrcMaterial.getBarCode().length() > 0) {
                strBarcode = "(MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                        " LIKE '%" + objSrcMaterial.getBarCode() + "%')";
            }

            String strName = "";
            if (objSrcMaterial.getMatname() != "" && objSrcMaterial.getMatname().length() > 0) {
                strName = " (MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                        " LIKE '%" + objSrcMaterial.getMatname() + "%')";
            }


            String whereClause = "";
            if (strGroup.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strGroup;
                } else {
                    whereClause = strGroup;
                }
            }

            if (strSubCategory.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategory;
                } else {
                    whereClause = whereClause + strSubCategory;
                }
            }

            if (strCode.length() > 0) {
                if(strBarcode.length() > 0) { // kondisi pencarian berdasarkan code/barcode material
                    if (whereClause.length() > 0) {
                        whereClause = whereClause + " AND (" + strCode + " OR " + strBarcode + ")";
                    } else {
                        whereClause = whereClause + "(" + strCode + " OR " + strBarcode + ")";
                    }
                } else { // kondisi pencarian hanya berdasarkan code material
                    if (whereClause.length() > 0) {
                        whereClause = whereClause + " AND " + strCode;
                    } else {
                        whereClause = whereClause + strCode;
                    }
                }
            }

            if (strName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strName;
                } else {
                    whereClause = whereClause + strName;
                }
            }

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + currPeriod.getOID();
                whereClause = whereClause + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + objSrcMaterial.getLocationId();
                whereClause = whereClause + " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
                sql = sql + " WHERE " + whereClause;
            } else {
                whereClause = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + currPeriod.getOID();
                whereClause = whereClause + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + objSrcMaterial.getLocationId();
                whereClause = whereClause + " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
                sql = sql + " WHERE " + whereClause;
            }

            //sql = sql + " ORDER BY " + orderBy;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialStock materialStock = new MaterialStock();

                materialStock.setOID(rs.getLong(1));
                materialStock.setPeriodeId(rs.getLong(2));
                materialStock.setMaterialUnitId(rs.getLong(3));
                materialStock.setLocationId(rs.getLong(4));
                materialStock.setQty(rs.getDouble(5));
                materialStock.setQtyMin(rs.getDouble(6));
                materialStock.setQtyMax(rs.getDouble(7));
                materialStock.setQtyIn(rs.getDouble(8));
                materialStock.setQtyOut(rs.getDouble(9));
                materialStock.setOpeningQty(rs.getDouble(10));
                materialStock.setClosingQty(rs.getDouble(11));

                result.add(materialStock);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }


    /**
     * Fungsi ini digunakan untuk mendapatkan jumlah material yang masih memiliki stok pada periode aktif
     */
    public static int countMaterialStockCurrPeriodAll(SrcMaterial objSrcMaterial) {
        int result = 0;
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT COUNT(DISTINCT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] + ")" +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";
            if(objSrcMaterial.getSupplierId() != 0) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " MT" +
                        " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                        " = MAT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }
            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " WHERE MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                    " = " + currPeriod.getOID() +
                    " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                    " = " + objSrcMaterial.getLocationId() +
                    " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE;
            if(objSrcMaterial.getSupplierId() > 0) {
                sql = sql + " AND MT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                        " = " + objSrcMaterial.getSupplierId();
            }
//            if(objSrcMaterial.getCategoryId() > 0) {
//                sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + objSrcMaterial.getCategoryId();
//            }
            //if(objSrcMaterial.getMatcode() != "") {
               // sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'";
            //}
            //update opie-eyek 20140416
            if (objSrcMaterial.getCategoryId() > 0) {
                //buatkan seperti
                sql += " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + objSrcMaterial.getCategoryId();
                
                String where = PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]+"='"+objSrcMaterial.getCategoryId()+"' OR "+
                               PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+"='"+objSrcMaterial.getCategoryId()+"'";
                
                Vector masterCatAcak = PstCategory.list(0,0,where,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                
                if(masterCatAcak.size()>1){
                    Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                    Vector<Long> levelParent = new Vector<Long>();
                    for(int i=0; i<materGroup.size(); i++) {
                         Category mGroup = (Category)materGroup.get(i);
                         sql +=" OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            " = " + mGroup.getOID();
                    }
                }
                sql +=")";
            }
            
            if(objSrcMaterial.getMatcode() != "") {
               sql += " AND ( MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + objSrcMaterial.getMatcode() + "%'" +
                      " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + objSrcMaterial.getMatcode() + "%')";
           }
            if(objSrcMaterial.getMatname() != "") {
                sql += " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + objSrcMaterial.getMatname() + "%'";
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    //Count Qty stok penerimaan dan bs disearch berdasrkan barcode
    public static int countMaterialStockCurrPeriodAllBarcode(SrcMaterial objSrcMaterial) {
        int result = 0;
        DBResultSet dbrs = null;
        Periode currPeriod = PstPeriode.getPeriodeRunning();
        try {
            String sql = "SELECT COUNT(DISTINCT MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID] + ")" +
                    " FROM " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS";

             if(objSrcMaterial.getSupplierId() != 0) {
                sql = sql + " INNER JOIN " + PstMatVendorPrice.TBL_MATERIAL_VENDOR_PRICE + " MT" +
                        " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                        " = MT." + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID];
            }

            sql = sql + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MT" +
                    " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                    " = MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

            String strGroup = "";
            if (objSrcMaterial.getCategoryId() > 0) {
                strGroup = " MT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + objSrcMaterial.getCategoryId();
            }

            String strSubCategory = "";
            if (objSrcMaterial.getSubCategoryId() > 0) {
                strSubCategory = " MT." + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                        " = " + objSrcMaterial.getSubCategoryId();
            }

            String strCode = "";
            if (objSrcMaterial.getMatcode() != "" && objSrcMaterial.getMatcode().length() > 0) {
                strCode = " (MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                        " LIKE '%" + objSrcMaterial.getMatcode() + "%')";
            }

            String strBarcode = "";
            if(objSrcMaterial.getBarCode() != "" && objSrcMaterial.getBarCode().length() > 0) {
                strBarcode = "(MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                        " LIKE '%" + objSrcMaterial.getBarCode() + "%')";
            }

            String strName = "";
            if (objSrcMaterial.getMatname() != "" && objSrcMaterial.getMatname().length() > 0) {
                strName = " (MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                        " LIKE '%" + objSrcMaterial.getMatname() + "%')";
            }


            String whereClause = "";
            if (strGroup.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND " + strGroup;
                } else {
                    whereClause = strGroup;
                }
            }

            if (strSubCategory.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubCategory;
                } else {
                    whereClause = whereClause + strSubCategory;
                }
            }

            if (strCode.length() > 0) {
                if(strBarcode.length() > 0) { // kondisi pencarian berdasarkan code/barcode material
                    if (whereClause.length() > 0) {
                        whereClause = whereClause + " AND (" + strCode + " OR " + strBarcode + ")";
                    } else {
                        whereClause = whereClause + "(" + strCode + " OR " + strBarcode + ")";
                    }
                } else { // kondisi pencarian hanya berdasarkan code material
                    if (whereClause.length() > 0) {
                        whereClause = whereClause + " AND " + strCode;
                    } else {
                        whereClause = whereClause + strCode;
                    }
                }
            }

            if (strName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strName;
                } else {
                    whereClause = whereClause + strName;
                }
            }

            if (whereClause.length() > 0) {
                whereClause = whereClause + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + currPeriod.getOID();
                whereClause = whereClause + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + objSrcMaterial.getLocationId();
                whereClause = whereClause + " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
                sql = sql + " WHERE " + whereClause;
            } else {
                    whereClause = " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + " = " + currPeriod.getOID();
                    whereClause = whereClause + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + objSrcMaterial.getLocationId();
                    whereClause = whereClause + " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " != " + PstMaterial.DELETE;
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }



    /**
     * pengecekan qty barang yang di gudang
     * sesuai dengan dokumen yang mengurangi stok
     * @return
     */
    public static double cekQtyStock(long oidDoc, long materialOid, long locationOid) {
        try {
            String where = fieldNames[FLD_MATERIAL_UNIT_ID] + "=" + materialOid +
                    " AND " + fieldNames[FLD_LOCATION_ID] + "=" + locationOid;
            Vector list = list(0, 0, where, "");

            // proses get object material stock
            MaterialStock materialStock = new MaterialStock();
            if (list != null && list.size() > 0) {
                materialStock = (MaterialStock) list.get(0);
            }

            DBResultSet dbrs = null;
            // get Object return
            String sql = "SELECT SUM(ITM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_QTY] + ") AS TTL FROM " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS ITM " +
                    " INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " AS RET " +
                    " ON ITM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] +
                    " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                    " WHERE ITM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + materialOid +
                    " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] + "!=" + oidDoc +
                    " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] + "=" + locationOid +
                    " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_DRAFT;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double qtyRetun = 0;
            while (rs.next()) {
                qtyRetun = rs.getDouble(1);
            }

            // get object dispath atau transfer
            sql = "SELECT SUM(ITM." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + ") AS TTL FROM " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS ITM " +
                    " INNER JOIN " + PstMatDispatch.TBL_DISPATCH + " AS RET " +
                    " ON ITM." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " = RET." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " WHERE ITM." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + "=" + materialOid +
                    " AND RET." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] + "!=" + oidDoc +
                    " AND RET." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + "=" + locationOid +
                    " AND RET." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_DRAFT;

            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            double qtyDispatch = 0;
            while (rs.next()) {
                qtyDispatch = rs.getDouble(1);
            }

            // get object costing barang
            sql = "SELECT SUM(ITM." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + ") AS TTL FROM " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " AS ITM " +
                    " INNER JOIN " + PstMatCosting.TBL_COSTING + " AS RET " +
                    " ON ITM." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " = RET." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " WHERE ITM." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] + "=" + materialOid +
                    " AND RET." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] + "!=" + oidDoc +
                    " AND RET." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + locationOid +
                    " AND RET." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_DRAFT;

            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            double qtyCosting = 0;
            while (rs.next()) {
                qtyCosting = rs.getDouble(1);
            }
            return materialStock.getQty() - (qtyRetun + qtyDispatch + qtyCosting);
        } catch (Exception e) {
        }
        return 0;
    }


    /**
     * Fungsi ini digunakan untuk update stock.
     * Alur Proses: file excel --> tabel pos_material_qty_temp --> pos_material_stock
     * Proses dari file excel --> tabel pos_material_qty_temp, dilakukan secara manual.
     * Sedangkan proses tabel pos_material_qty_temp --> pos_material_stock, dilakukan oleh fungsi dibawah ini.
     * create by: gwawan@dimata 30 okt 2007
     */
    public static final int updateStockByCode() {
        int result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "";

            /** get list material yang akan dilakukan proses update stock */
            sql = "select * from pos_material_qty_temp";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                String kode = "";
                double qtyNew = 0;
                long locationId = 0;
                long periodeId = 0;
                int status = 0;
                long materialId = 0;
                long stockId = 0;

                kode = rs.getString("kode");
                qtyNew = rs.getDouble("qty");
                locationId = rs.getLong("location_id");
                periodeId = rs.getLong("periode_id");
                status = rs.getInt("status");

                /** cek apakah material sudah diproses (1) atau belum (0) */
                if(status == 0) {
                    /** get material id berdasarkan kode material */
                    sql = " SELECT "+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" FROM";
                    sql+= " "+PstMaterial.TBL_MATERIAL+" WHERE "+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" like '%"+kode+"%'";
                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs1 = dbrs.getResultSet();
                    while (rs1.next()) {
                        materialId = rs1.getLong(1);
                    }
                }

                if(materialId != 0) {
                    /** cari, apakah sudah memiliki stock id atau belum */
                    sql = " SELECT "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID];
                    sql+= " FROM "+PstMaterialStock.TBL_MATERIAL_STOCK+" WHERE";
                    sql+= " "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+materialId;
                    sql+= " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+locationId;
                    sql+= " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+"="+periodeId;
                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs2 = dbrs.getResultSet();
                    while (rs2.next()) {
                        stockId = rs2.getLong(1);
                    }

                    /** lakukan insert stock jika belum memiliki stock id */
                    MaterialStock objMaterialStock = new MaterialStock();
                    if(stockId == 0) {
                        objMaterialStock.setMaterialUnitId(materialId);
                        objMaterialStock.setLocationId(locationId);
                        objMaterialStock.setQty(qtyNew);
                        objMaterialStock.setPeriodeId(periodeId);
                        long oidStock = PstMaterialStock.insertExc(objMaterialStock);
                    }
                    /** lakukan update stock jika sudah memiliki stock id */
                    else {
                        objMaterialStock = PstMaterialStock.fetchExc(stockId);
                        objMaterialStock.setQty(objMaterialStock.getQty() + qtyNew);
                        long oidStock = PstMaterialStock.updateExc(objMaterialStock);
                    }

                    sql = "update pos_material_qty_temp set status = 1 where kode like '%"+kode+"%'";
                    result = DBHandler.execUpdate(sql);
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("Process status: "+result);
        return result;
    }

  /* update table stock untuk material yang belum ada di stock*/
  public static int refreshCatalogStock(){
   int err=0;
   Vector locations = com.dimata.common.entity.location.PstLocation.listAll();
   String sql ="";
   Periode per= PstPeriode.getCurrentPeriode();
   for(int i=0;(locations!=null && i<locations.size());i++) {
     com.dimata.common.entity.location.Location loc = (com.dimata.common.entity.location.Location) locations.get(i);
     sql="insert into `pos_material_stock` (`MATERIAL_STOCK_ID` ,`PERIODE_ID`,`MATERIAL_UNIT_ID`,`LOCATION_ID`,"+
          "`QTY`,`QTY_MIN`,`QTY_MAX`,`OPENING_QTY`,`CLOSING_QTY`,`QTY_IN`,`QTY_OUT`) "+
          " (select m.material_id+"+loc.getOID()+","+per.getOID()+",m.material_id,'"+loc.getOID()+"', 0, 0, 0, 0, 0, 0, 0 "+
          " from pos_material m where m.material_id not in (select material_unit_id as material_id  from pos_material_stock "+
          " where location_id='"+loc.getOID()+"'))";
        try {
            err = DBHandler.execUpdate(sql);
        } catch(Exception e) {
            System.out.println(e.toString());
        }
     }
     return err;
    }
     public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MaterialStock materialStock = PstMaterialStock.fetchExc(oid);
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID], materialStock.getOID());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT], materialStock.getBerat());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT_IN], materialStock.getBeratIn());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT_OUT], materialStock.getBeratOut());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_BERAT], materialStock.getClosingBerat());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY], materialStock.getClosingQty());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID], materialStock.getLocationId());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID], materialStock.getMaterialUnitId());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_BERAT], materialStock.getOpeningBerat());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY], materialStock.getOpeningQty());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID], materialStock.getPeriodeId());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY], materialStock.getQty());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN], materialStock.getQtyIn());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX], materialStock.getQtyMax());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN], materialStock.getQtyMin());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT], materialStock.getQtyOut());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QT_OPTIMUM], materialStock.getQtyOptimum());
                object.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_UPDATE_DATE], materialStock.getUpdateDate());
            }catch(Exception exc){}

            return object;
        } 
  
}
