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
 * @author IanRizky
 */
public class PstChainAddCost extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CHAIN_ADD_COST = "pos_chain_add_cost";
    public static final int FLD_CHAIN_ADD_COST_ID = 0;
    public static final int FLD_CHAIN_PERIOD_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_STOCK_QTY = 3;
    public static final int FLD_STOCK_VALUE = 4;
    public static final int FLD_COST_TYPE = 5;
    public static final int FLD_PRODUCT_DISTRIBUTION_ID = 6;

    public static String[] fieldNames = {
        "CHAIN_ADD_COST_ID",
        "CHAIN_PERIOD_ID",
        "MATERIAL_ID",
        "STOCK_QTY",
        "STOCK_VALUE",
        "COST_TYPE",
        "PRODUCT_DISTRIBUTION_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG
    };

    public static final int COST_TYPE_GENERAL = 0;
    public static final int COST_TYPE_REFERENCED = 1;

    public static final String[] COST_TYPE_TITLE = {
        "General Cost", "Referenced Cost"
    };

    public PstChainAddCost() {
    }

    public PstChainAddCost(int i) throws DBException {
        super(new PstChainAddCost());
    }

    public PstChainAddCost(String sOid) throws DBException {
        super(new PstChainAddCost(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstChainAddCost(long lOid) throws DBException {
        super(new PstChainAddCost(0));
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
        return TBL_CHAIN_ADD_COST;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstChainAddCost().getClass().getName();
    }

    public static ChainAddCost fetchExc(long oid) throws DBException {
        try {
            ChainAddCost entChainAddCost = new ChainAddCost();
            PstChainAddCost pstChainAddCost = new PstChainAddCost(oid);
            entChainAddCost.setOID(oid);
            entChainAddCost.setChainPeriodId(pstChainAddCost.getlong(FLD_CHAIN_PERIOD_ID));
            entChainAddCost.setMaterialId(pstChainAddCost.getlong(FLD_MATERIAL_ID));
            entChainAddCost.setStockQty(pstChainAddCost.getInt(FLD_STOCK_QTY));
            entChainAddCost.setStockValue(pstChainAddCost.getdouble(FLD_STOCK_VALUE));
            entChainAddCost.setCostType(pstChainAddCost.getInt(FLD_COST_TYPE));
            entChainAddCost.setProductDistributionId(pstChainAddCost.getlong(FLD_PRODUCT_DISTRIBUTION_ID));
            return entChainAddCost;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainAddCost(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ChainAddCost entChainAddCost = fetchExc(entity.getOID());
        entity = (Entity) entChainAddCost;
        return entChainAddCost.getOID();
    }

    public static synchronized long updateExc(ChainAddCost entChainAddCost) throws DBException {
        try {
            if (entChainAddCost.getOID() != 0) {
                PstChainAddCost pstChainAddCost = new PstChainAddCost(entChainAddCost.getOID());
                pstChainAddCost.setLong(FLD_CHAIN_PERIOD_ID, entChainAddCost.getChainPeriodId());
                pstChainAddCost.setLong(FLD_MATERIAL_ID, entChainAddCost.getMaterialId());
                pstChainAddCost.setInt(FLD_STOCK_QTY, entChainAddCost.getStockQty());
                pstChainAddCost.setDouble(FLD_STOCK_VALUE, entChainAddCost.getStockValue());
                pstChainAddCost.setInt(FLD_COST_TYPE, entChainAddCost.getCostType());
                pstChainAddCost.setLong(FLD_PRODUCT_DISTRIBUTION_ID, entChainAddCost.getProductDistributionId());
                pstChainAddCost.update();
                return entChainAddCost.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainAddCost(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ChainAddCost) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstChainAddCost pstChainAddCost = new PstChainAddCost(oid);
            pstChainAddCost.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainAddCost(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ChainAddCost entChainAddCost) throws DBException {
        try {
            PstChainAddCost pstChainAddCost = new PstChainAddCost(0);
            pstChainAddCost.setLong(FLD_CHAIN_PERIOD_ID, entChainAddCost.getChainPeriodId());
            pstChainAddCost.setLong(FLD_MATERIAL_ID, entChainAddCost.getMaterialId());
            pstChainAddCost.setInt(FLD_STOCK_QTY, entChainAddCost.getStockQty());
            pstChainAddCost.setDouble(FLD_STOCK_VALUE, entChainAddCost.getStockValue());
            pstChainAddCost.setInt(FLD_COST_TYPE, entChainAddCost.getCostType());
            pstChainAddCost.setLong(FLD_PRODUCT_DISTRIBUTION_ID, entChainAddCost.getProductDistributionId());
            pstChainAddCost.insert();
            entChainAddCost.setOID(pstChainAddCost.getlong(FLD_CHAIN_ADD_COST_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainAddCost(0), DBException.UNKNOWN);
        }
        return entChainAddCost.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ChainAddCost) entity);
    }

    public static void resultToObject(ResultSet rs, ChainAddCost entChainAddCost) {
        try {
            entChainAddCost.setOID(rs.getLong(PstChainAddCost.fieldNames[PstChainAddCost.FLD_CHAIN_ADD_COST_ID]));
            entChainAddCost.setChainPeriodId(rs.getLong(PstChainAddCost.fieldNames[PstChainAddCost.FLD_CHAIN_PERIOD_ID]));
            entChainAddCost.setMaterialId(rs.getLong(PstChainAddCost.fieldNames[PstChainAddCost.FLD_MATERIAL_ID]));
            entChainAddCost.setStockQty(rs.getInt(PstChainAddCost.fieldNames[PstChainAddCost.FLD_STOCK_QTY]));
            entChainAddCost.setStockValue(rs.getDouble(PstChainAddCost.fieldNames[PstChainAddCost.FLD_STOCK_VALUE]));
            entChainAddCost.setCostType(rs.getInt(PstChainAddCost.fieldNames[PstChainAddCost.FLD_COST_TYPE]));
            entChainAddCost.setProductDistributionId(rs.getLong(PstChainAddCost.fieldNames[PstChainAddCost.FLD_PRODUCT_DISTRIBUTION_ID]));
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
            String sql = "SELECT * FROM " + TBL_CHAIN_ADD_COST;
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
                ChainAddCost entChainAddCost = new ChainAddCost();
                resultToObject(rs, entChainAddCost);
                lists.add(entChainAddCost);
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

    public static boolean checkOID(long entChainAddCostId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CHAIN_ADD_COST + " WHERE "
                    + PstChainAddCost.fieldNames[PstChainAddCost.FLD_CHAIN_ADD_COST_ID] + " = " + entChainAddCostId;
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
            String sql = "SELECT COUNT(" + PstChainAddCost.fieldNames[PstChainAddCost.FLD_CHAIN_ADD_COST_ID] + ") FROM " + TBL_CHAIN_ADD_COST;
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

    public static double sumTotalCostPeriod(long oidPeriod) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + fieldNames[FLD_STOCK_QTY] + " * " + fieldNames[FLD_STOCK_VALUE] + ")"
                    + " FROM " + TBL_CHAIN_ADD_COST
                    + " WHERE " + fieldNames[FLD_CHAIN_PERIOD_ID] + " = " + oidPeriod
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double total = 0;
            while (rs.next()) {
                total = rs.getDouble(1);
            }
            rs.close();
            return total;
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
                    ChainAddCost entChainAddCost = (ChainAddCost) list.get(ls);
                    if (oid == entChainAddCost.getOID()) {
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
        } else if (start == (vectSize - recordToGet)) {
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
        return cmd;
    }

        
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                ChainAddCost chainAddCost = PstChainAddCost.fetchExc(oid);
                object.put(PstChainAddCost.fieldNames[PstChainAddCost.FLD_CHAIN_ADD_COST_ID], chainAddCost.getOID());
                object.put(PstChainAddCost.fieldNames[PstChainAddCost.FLD_CHAIN_PERIOD_ID], chainAddCost.getChainPeriodId());
                object.put(PstChainAddCost.fieldNames[PstChainAddCost.FLD_COST_TYPE], chainAddCost.getCostType());
                object.put(PstChainAddCost.fieldNames[PstChainAddCost.FLD_MATERIAL_ID], chainAddCost.getMaterialId());
                object.put(PstChainAddCost.fieldNames[PstChainAddCost.FLD_PRODUCT_DISTRIBUTION_ID], chainAddCost.getProductDistributionId());
                object.put(PstChainAddCost.fieldNames[PstChainAddCost.FLD_STOCK_QTY], chainAddCost.getStockQty());
                object.put(PstChainAddCost.fieldNames[PstChainAddCost.FLD_STOCK_VALUE], chainAddCost.getStockValue());
            }catch(Exception exc){}

            return object;
        }    

}
