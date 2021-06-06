/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class PstCalcCogsSales extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CALC_COGS_SALES = "pos_calc_cogs_sales";
    public static final int FLD_CALC_COGS_SALES_ID = 0;
    public static final int FLD_CALC_COGS_MAIN_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_UNIT_ID = 3;
    public static final int FLD_QTY_ITEM = 4;
    public static final int FLD_STOCK_LEFT = 5;
    public static final int FLD_SUB_TOTAL_SALES = 6;
    public static final int FLD_STOK_AWAL = 7;

    public static String[] fieldNames = {
        "CALC_COGS_SALES_ID",
        "CALC_COGS_MAIN_ID",
        "MATERIAL_ID",
        "UNIT_ID",
        "QTY_ITEM",
        "STOCK_LEFT",
        "SUB_TOTAL_SALES",
        "STOK_AWAL"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public PstCalcCogsSales() {
    }

    public PstCalcCogsSales(int i) throws DBException {
        super(new PstCalcCogsSales());
    }

    public PstCalcCogsSales(String sOid) throws DBException {
        super(new PstCalcCogsSales(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstCalcCogsSales(long lOid) throws DBException {
        super(new PstCalcCogsSales(0));
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
        return TBL_CALC_COGS_SALES;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCalcCogsSales().getClass().getName();
    }

    public static CalcCogsSales fetchExc(long oid) throws DBException {
        try {
            CalcCogsSales entCalcCogsSales = new CalcCogsSales();
            PstCalcCogsSales pstCalcCogsSales = new PstCalcCogsSales(oid);
            entCalcCogsSales.setOID(oid);
            entCalcCogsSales.setCalcCogsMainId(pstCalcCogsSales.getLong(FLD_CALC_COGS_MAIN_ID));
            entCalcCogsSales.setMaterialId(pstCalcCogsSales.getLong(FLD_MATERIAL_ID));
            entCalcCogsSales.setUnitId(pstCalcCogsSales.getLong(FLD_UNIT_ID));
            entCalcCogsSales.setQtyItem(pstCalcCogsSales.getdouble(FLD_QTY_ITEM));
            entCalcCogsSales.setStockLeft(pstCalcCogsSales.getdouble(FLD_STOCK_LEFT));
            entCalcCogsSales.setSubTotalSales(pstCalcCogsSales.getdouble(FLD_SUB_TOTAL_SALES));
            entCalcCogsSales.setStokAwal(pstCalcCogsSales.getdouble(FLD_STOK_AWAL));
            return entCalcCogsSales;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsSales(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        CalcCogsSales entCalcCogsSales = fetchExc(entity.getOID());
        entity = (Entity) entCalcCogsSales;
        return entCalcCogsSales.getOID();
    }

    public static synchronized long updateExc(CalcCogsSales entCalcCogsSales) throws DBException {
        try {
            if (entCalcCogsSales.getOID() != 0) {
                PstCalcCogsSales pstCalcCogsSales = new PstCalcCogsSales(entCalcCogsSales.getOID());
                pstCalcCogsSales.setLong(FLD_CALC_COGS_MAIN_ID, entCalcCogsSales.getCalcCogsMainId());
                pstCalcCogsSales.setLong(FLD_MATERIAL_ID, entCalcCogsSales.getMaterialId());
                pstCalcCogsSales.setLong(FLD_UNIT_ID, entCalcCogsSales.getUnitId());
                pstCalcCogsSales.setDouble(FLD_QTY_ITEM, entCalcCogsSales.getQtyItem());
                pstCalcCogsSales.setDouble(FLD_STOCK_LEFT, entCalcCogsSales.getStockLeft());
                pstCalcCogsSales.setDouble(FLD_SUB_TOTAL_SALES, entCalcCogsSales.getSubTotalSales());
                pstCalcCogsSales.setDouble(FLD_STOK_AWAL, entCalcCogsSales.getStokAwal());
                pstCalcCogsSales.update();
                return entCalcCogsSales.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsSales(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((CalcCogsSales) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstCalcCogsSales pstCalcCogsSales = new PstCalcCogsSales(oid);
            pstCalcCogsSales.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsSales(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(CalcCogsSales entCalcCogsSales) throws DBException {
        try {
            PstCalcCogsSales pstCalcCogsSales = new PstCalcCogsSales(0);
            pstCalcCogsSales.setLong(FLD_CALC_COGS_MAIN_ID, entCalcCogsSales.getCalcCogsMainId());
            pstCalcCogsSales.setLong(FLD_MATERIAL_ID, entCalcCogsSales.getMaterialId());
            pstCalcCogsSales.setLong(FLD_UNIT_ID, entCalcCogsSales.getUnitId());
            pstCalcCogsSales.setDouble(FLD_QTY_ITEM, entCalcCogsSales.getQtyItem());
            pstCalcCogsSales.setDouble(FLD_STOCK_LEFT, entCalcCogsSales.getStockLeft());
            pstCalcCogsSales.setDouble(FLD_SUB_TOTAL_SALES, entCalcCogsSales.getSubTotalSales());
            pstCalcCogsSales.setDouble(FLD_STOK_AWAL, entCalcCogsSales.getStokAwal());
            pstCalcCogsSales.insert();
            entCalcCogsSales.setOID(pstCalcCogsSales.getlong(FLD_CALC_COGS_SALES_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsSales(0), DBException.UNKNOWN);
        }
        return entCalcCogsSales.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((CalcCogsSales) entity);
    }

    public static void resultToObject(ResultSet rs, CalcCogsSales entCalcCogsSales) {
        try {
            entCalcCogsSales.setOID(rs.getLong(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_CALC_COGS_SALES_ID]));
            entCalcCogsSales.setCalcCogsMainId(rs.getLong(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_CALC_COGS_MAIN_ID]));
            entCalcCogsSales.setMaterialId(rs.getLong(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_MATERIAL_ID]));
            entCalcCogsSales.setUnitId(rs.getLong(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_UNIT_ID]));
            entCalcCogsSales.setQtyItem(rs.getDouble(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_QTY_ITEM]));
            entCalcCogsSales.setStockLeft(rs.getDouble(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_STOCK_LEFT]));
            entCalcCogsSales.setSubTotalSales(rs.getDouble(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_SUB_TOTAL_SALES]));
            entCalcCogsSales.setStokAwal(rs.getDouble(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_STOK_AWAL]));
        } catch (Exception e) {
        }
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CALC_COGS_SALES;
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
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CalcCogsSales entCalcCogsSales = new CalcCogsSales();
                resultToObject(rs, entCalcCogsSales);
                lists.add(entCalcCogsSales);
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

    public static boolean checkOID(long entCalcCogsSalesId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CALC_COGS_SALES + " WHERE "
                    + PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_CALC_COGS_SALES_ID] + " = " + entCalcCogsSalesId;
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
            return result;
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_CALC_COGS_SALES_ID] + ") FROM " + TBL_CALC_COGS_SALES;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
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

    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    CalcCogsSales entCalcCogsSales = (CalcCogsSales) list.get(ls);
                    if (oid == entCalcCogsSales.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }
        return start;
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0) {
            cmd = Command.FIRST;
        } else {
            if (start == (vectSize - recordToGet)) {
                cmd = Command.LAST;
            } else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }
    
            
	public static JSONObject fetchJSON(long oid){
		JSONObject object = new JSONObject();
		try {
                    CalcCogsSales calcCogsSales = PstCalcCogsSales.fetchExc(oid);
                    object.put(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_CALC_COGS_SALES_ID], calcCogsSales.getOID());
                    object.put(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_CALC_COGS_MAIN_ID], calcCogsSales.getCalcCogsMainId());
                    object.put(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_MATERIAL_ID], calcCogsSales.getMaterialId());
                    object.put(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_QTY_ITEM], calcCogsSales.getQtyItem());
                    object.put(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_STOCK_LEFT], calcCogsSales.getStockLeft());
                    object.put(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_STOK_AWAL], calcCogsSales.getStokAwal());
                    object.put(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_SUB_TOTAL_SALES], calcCogsSales.getSubTotalSales());
                    object.put(PstCalcCogsSales.fieldNames[PstCalcCogsSales.FLD_UNIT_ID], calcCogsSales.getUnitId());
                
                }catch(Exception exc){}
            
                return object;
            }
}
