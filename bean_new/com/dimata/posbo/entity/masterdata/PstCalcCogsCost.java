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
public class PstCalcCogsCost extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CALC_COGS_COST = "pos_calc_cogs_cost";
    public static final int FLD_CALC_COGS_COST_ID = 0;
    public static final int FLD_CALC_COGS_MAIN_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_UNIT_ID = 3;
    public static final int FLD_QTY_ITEM = 4;
    public static final int FLD_STOCK_LEFT = 5;
    public static final int FLD_SUB_TOTAL_COST = 6;
    public static final int FLD_STOK_AWAL = 7;

    public static String[] fieldNames = {
        "CALC_COGS_COST_ID",
        "CALC_COGS_MAIN_ID",
        "MATERIAL_ID",
        "UNIT_ID",
        "QTY_ITEM",
        "STOCK_LEFT",
        "SUB_TOTAL_COST",
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

    public PstCalcCogsCost() {
    }

    public PstCalcCogsCost(int i) throws DBException {
        super(new PstCalcCogsCost());
    }

    public PstCalcCogsCost(String sOid) throws DBException {
        super(new PstCalcCogsCost(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstCalcCogsCost(long lOid) throws DBException {
        super(new PstCalcCogsCost(0));
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
        return TBL_CALC_COGS_COST;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCalcCogsCost().getClass().getName();
    }

    public static CalcCogsCost fetchExc(long oid) throws DBException {
        try {
            CalcCogsCost entCalcCogsCost = new CalcCogsCost();
            PstCalcCogsCost pstCalcCogsCost = new PstCalcCogsCost(oid);
            entCalcCogsCost.setOID(oid);
            entCalcCogsCost.setCalcCogsMainId(pstCalcCogsCost.getlong(FLD_CALC_COGS_MAIN_ID));
            entCalcCogsCost.setMaterialId(pstCalcCogsCost.getlong(FLD_MATERIAL_ID));
            entCalcCogsCost.setUnitId(pstCalcCogsCost.getlong(FLD_UNIT_ID));
            entCalcCogsCost.setQtyItem(pstCalcCogsCost.getdouble(FLD_QTY_ITEM));
            entCalcCogsCost.setStockLeft(pstCalcCogsCost.getdouble(FLD_STOCK_LEFT));
            entCalcCogsCost.setSubTotalCost(pstCalcCogsCost.getdouble(FLD_SUB_TOTAL_COST));
            entCalcCogsCost.setStokAwal(pstCalcCogsCost.getdouble(FLD_STOK_AWAL));
            return entCalcCogsCost;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsCost(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        CalcCogsCost entCalcCogsCost = fetchExc(entity.getOID());
        entity = (Entity) entCalcCogsCost;
        return entCalcCogsCost.getOID();
    }

    public static synchronized long updateExc(CalcCogsCost entCalcCogsCost) throws DBException {
        try {
            if (entCalcCogsCost.getOID() != 0) {
                PstCalcCogsCost pstCalcCogsCost = new PstCalcCogsCost(entCalcCogsCost.getOID());
                pstCalcCogsCost.setLong(FLD_CALC_COGS_MAIN_ID, entCalcCogsCost.getCalcCogsMainId());
                pstCalcCogsCost.setLong(FLD_MATERIAL_ID, entCalcCogsCost.getMaterialId());
                pstCalcCogsCost.setLong(FLD_UNIT_ID, entCalcCogsCost.getUnitId());
                pstCalcCogsCost.setDouble(FLD_QTY_ITEM, entCalcCogsCost.getQtyItem());
                pstCalcCogsCost.setDouble(FLD_STOCK_LEFT, entCalcCogsCost.getStockLeft());
                pstCalcCogsCost.setDouble(FLD_SUB_TOTAL_COST, entCalcCogsCost.getSubTotalCost());
                pstCalcCogsCost.setDouble(FLD_STOK_AWAL, entCalcCogsCost.getStokAwal());
                pstCalcCogsCost.update();
                return entCalcCogsCost.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsCost(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((CalcCogsCost) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstCalcCogsCost pstCalcCogsCost = new PstCalcCogsCost(oid);
            pstCalcCogsCost.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsCost(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(CalcCogsCost entCalcCogsCost) throws DBException {
        try {
            PstCalcCogsCost pstCalcCogsCost = new PstCalcCogsCost(0);
            pstCalcCogsCost.setLong(FLD_CALC_COGS_MAIN_ID, entCalcCogsCost.getCalcCogsMainId());
            pstCalcCogsCost.setLong(FLD_MATERIAL_ID, entCalcCogsCost.getMaterialId());
            pstCalcCogsCost.setLong(FLD_UNIT_ID, entCalcCogsCost.getUnitId());
            pstCalcCogsCost.setDouble(FLD_QTY_ITEM, entCalcCogsCost.getQtyItem());
            pstCalcCogsCost.setDouble(FLD_STOCK_LEFT, entCalcCogsCost.getStockLeft());
            pstCalcCogsCost.setDouble(FLD_SUB_TOTAL_COST, entCalcCogsCost.getSubTotalCost());
            pstCalcCogsCost.setDouble(FLD_STOK_AWAL, entCalcCogsCost.getStokAwal());
            pstCalcCogsCost.insert();
            entCalcCogsCost.setOID(pstCalcCogsCost.getlong(FLD_CALC_COGS_COST_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCalcCogsCost(0), DBException.UNKNOWN);
        }
        return entCalcCogsCost.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((CalcCogsCost) entity);
    }

    public static void resultToObject(ResultSet rs, CalcCogsCost entCalcCogsCost) {
        try {
            entCalcCogsCost.setOID(rs.getLong(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_CALC_COGS_COST_ID]));
            entCalcCogsCost.setCalcCogsMainId(rs.getLong(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_CALC_COGS_MAIN_ID]));
            entCalcCogsCost.setMaterialId(rs.getLong(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_MATERIAL_ID]));
            entCalcCogsCost.setUnitId(rs.getLong(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_UNIT_ID]));
            entCalcCogsCost.setQtyItem(rs.getDouble(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_QTY_ITEM]));
            entCalcCogsCost.setStockLeft(rs.getDouble(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_STOCK_LEFT]));
            entCalcCogsCost.setSubTotalCost(rs.getDouble(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_SUB_TOTAL_COST]));
            entCalcCogsCost.setStokAwal(rs.getDouble(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_STOK_AWAL]));
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
            String sql = "SELECT * FROM " + TBL_CALC_COGS_COST;
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
                CalcCogsCost entCalcCogsCost = new CalcCogsCost();
                resultToObject(rs, entCalcCogsCost);
                lists.add(entCalcCogsCost);
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

    public static boolean checkOID(long entCalcCogsCostId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CALC_COGS_COST + " WHERE "
                    + PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_CALC_COGS_COST_ID] + " = " + entCalcCogsCostId;
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
            String sql = "SELECT COUNT(" + PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_CALC_COGS_COST_ID] + ") FROM " + TBL_CALC_COGS_COST;
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
                    CalcCogsCost entCalcCogsCost = (CalcCogsCost) list.get(ls);
                    if (oid == entCalcCogsCost.getOID()) {
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
    
            // ------------------ end added by eri  -----------------------------------
	public static JSONObject fetchJSON(long oid){
		JSONObject object = new JSONObject();
		try {
                    CalcCogsCost calcCogsCost = PstCalcCogsCost.fetchExc(oid);
                    object.put(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_CALC_COGS_COST_ID], calcCogsCost.getOID());
                    object.put(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_CALC_COGS_MAIN_ID], calcCogsCost.getCalcCogsMainId());
                    object.put(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_MATERIAL_ID], calcCogsCost.getMaterialId());
                    object.put(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_QTY_ITEM], calcCogsCost.getQtyItem());
                    object.put(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_STOCK_LEFT], calcCogsCost.getStockLeft());
                    object.put(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_STOK_AWAL], calcCogsCost.getStokAwal());
                    object.put(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_SUB_TOTAL_COST], calcCogsCost.getSubTotalCost());
                    object.put(PstCalcCogsCost.fieldNames[PstCalcCogsCost.FLD_UNIT_ID], calcCogsCost.getUnitId());
                
                }catch(Exception exc){}
            
                return object;
            }
}
