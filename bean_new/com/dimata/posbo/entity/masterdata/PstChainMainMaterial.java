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
public class PstChainMainMaterial extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CHAIN_MAIN_MATERIAL = "pos_chain_material";
    public static final int FLD_CHAIN_MAIN_MATERIAL_ID = 0;
    public static final int FLD_CHAIN_PERIOD_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_MATERIAL_TYPE = 3;
    public static final int FLD_COST_PCT = 4;
    public static final int FLD_COST_TYPE = 5;
    public static final int FLD_STOCK_QTY = 6;
    public static final int FLD_COST_VALUE = 7;
    public static final int FLD_SALES_VALUE = 8;
    public static final int FLD_PERIOD_DISTRIBUTION = 9;

    public static String[] fieldNames = {
        "CHAIN_MATERIAL_ID",
        "CHAIN_PERIOD_ID",
        "MATERIAL_ID",
        "MATERIAL_TYPE",
        "COST_PCT",
        "COST_TYPE",
        "STOCK_QTY",
        "COST_VALUE",
        "SALES_VALUE",
        "PERIOD_DISTRIBUTION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT
    };

    public static final int TYPE_RESULT_NEXT_COST = 0;
    public static final int TYPE_RESULT = 1;
    public static final int TYPE_NEXT_COST = 2;
    public static final int TYPE_REFERENCED_COST = 3;

    public static final String[] typeStr = {
        "Result & Next Cost", "Result", "Next Cost", "Referenced Cost"
    };

    public static final int COST_TYPE_AUTO = 0;
    public static final int COST_TYPE_CUSTOM = 1;
    public static final int COST_TYPE_REFERENCED = 2;
    public static final String[] COST_TYPE_TITLE = {"Auto", "Custom", "Referenced"};

    public PstChainMainMaterial() {
    }

    public PstChainMainMaterial(int i) throws DBException {
        super(new PstChainMainMaterial());
    }

    public PstChainMainMaterial(String sOid) throws DBException {
        super(new PstChainMainMaterial(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstChainMainMaterial(long lOid) throws DBException {
        super(new PstChainMainMaterial(0));
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
        return TBL_CHAIN_MAIN_MATERIAL;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstChainMainMaterial().getClass().getName();
    }

    public static ChainMainMaterial fetchExc(long oid) throws DBException {
        try {
            ChainMainMaterial entChainMainMaterial = new ChainMainMaterial();
            PstChainMainMaterial pstChainMainMaterial = new PstChainMainMaterial(oid);
            entChainMainMaterial.setOID(oid);
            entChainMainMaterial.setChainPeriodId(pstChainMainMaterial.getlong(FLD_CHAIN_PERIOD_ID));
            entChainMainMaterial.setMaterialId(pstChainMainMaterial.getlong(FLD_MATERIAL_ID));
            entChainMainMaterial.setMaterialType(pstChainMainMaterial.getInt(FLD_MATERIAL_TYPE));
            entChainMainMaterial.setCostPct(pstChainMainMaterial.getdouble(FLD_COST_PCT));
            entChainMainMaterial.setCostType(pstChainMainMaterial.getInt(FLD_COST_TYPE));
            entChainMainMaterial.setStockQty(pstChainMainMaterial.getInt(FLD_STOCK_QTY));
            entChainMainMaterial.setCostValue(pstChainMainMaterial.getdouble(FLD_COST_VALUE));
            entChainMainMaterial.setSalesValue(pstChainMainMaterial.getdouble(FLD_SALES_VALUE));
            entChainMainMaterial.setPeriodDistribution(pstChainMainMaterial.getInt(FLD_PERIOD_DISTRIBUTION));
            return entChainMainMaterial;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainMainMaterial(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ChainMainMaterial entChainMainMaterial = fetchExc(entity.getOID());
        entity = (Entity) entChainMainMaterial;
        return entChainMainMaterial.getOID();
    }

    public static synchronized long updateExc(ChainMainMaterial entChainMainMaterial) throws DBException {
        try {
            if (entChainMainMaterial.getOID() != 0) {
                PstChainMainMaterial pstChainMainMaterial = new PstChainMainMaterial(entChainMainMaterial.getOID());
                pstChainMainMaterial.setLong(FLD_CHAIN_PERIOD_ID, entChainMainMaterial.getChainPeriodId());
                pstChainMainMaterial.setLong(FLD_MATERIAL_ID, entChainMainMaterial.getMaterialId());
                pstChainMainMaterial.setInt(FLD_MATERIAL_TYPE, entChainMainMaterial.getMaterialType());
                pstChainMainMaterial.setDouble(FLD_COST_PCT, entChainMainMaterial.getCostPct());
                pstChainMainMaterial.setInt(FLD_COST_TYPE, entChainMainMaterial.getCostType());
                pstChainMainMaterial.setInt(FLD_STOCK_QTY, entChainMainMaterial.getStockQty());
                pstChainMainMaterial.setDouble(FLD_COST_VALUE, entChainMainMaterial.getCostValue());
                pstChainMainMaterial.setDouble(FLD_SALES_VALUE, entChainMainMaterial.getSalesValue());
                pstChainMainMaterial.setInt(FLD_PERIOD_DISTRIBUTION, entChainMainMaterial.getPeriodDistribution());
                pstChainMainMaterial.update();
                return entChainMainMaterial.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainMainMaterial(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ChainMainMaterial) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstChainMainMaterial pstChainMainMaterial = new PstChainMainMaterial(oid);
            pstChainMainMaterial.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainMainMaterial(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ChainMainMaterial entChainMainMaterial) throws DBException {
        try {
            PstChainMainMaterial pstChainMainMaterial = new PstChainMainMaterial(0);
            pstChainMainMaterial.setLong(FLD_CHAIN_PERIOD_ID, entChainMainMaterial.getChainPeriodId());
            pstChainMainMaterial.setLong(FLD_MATERIAL_ID, entChainMainMaterial.getMaterialId());
            pstChainMainMaterial.setInt(FLD_MATERIAL_TYPE, entChainMainMaterial.getMaterialType());
            pstChainMainMaterial.setDouble(FLD_COST_PCT, entChainMainMaterial.getCostPct());
            pstChainMainMaterial.setInt(FLD_COST_TYPE, entChainMainMaterial.getCostType());
            pstChainMainMaterial.setInt(FLD_STOCK_QTY, entChainMainMaterial.getStockQty());
            pstChainMainMaterial.setDouble(FLD_COST_VALUE, entChainMainMaterial.getCostValue());
            pstChainMainMaterial.setDouble(FLD_SALES_VALUE, entChainMainMaterial.getSalesValue());
            pstChainMainMaterial.setInt(FLD_PERIOD_DISTRIBUTION, entChainMainMaterial.getPeriodDistribution());
            pstChainMainMaterial.insert();
            entChainMainMaterial.setOID(pstChainMainMaterial.getlong(FLD_CHAIN_MAIN_MATERIAL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainMainMaterial(0), DBException.UNKNOWN);
        }
        return entChainMainMaterial.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ChainMainMaterial) entity);
    }

    public static void resultToObject(ResultSet rs, ChainMainMaterial entChainMainMaterial) {
        try {
            entChainMainMaterial.setOID(rs.getLong(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_MAIN_MATERIAL_ID]));
            entChainMainMaterial.setChainPeriodId(rs.getLong(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_PERIOD_ID]));
            entChainMainMaterial.setMaterialId(rs.getLong(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_MATERIAL_ID]));
            entChainMainMaterial.setMaterialType(rs.getInt(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_MATERIAL_TYPE]));
            entChainMainMaterial.setCostPct(rs.getDouble(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_COST_PCT]));
            entChainMainMaterial.setCostType(rs.getInt(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_COST_TYPE]));
            entChainMainMaterial.setStockQty(rs.getInt(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_STOCK_QTY]));
            entChainMainMaterial.setCostValue(rs.getDouble(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_COST_VALUE]));
            entChainMainMaterial.setSalesValue(rs.getDouble(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_SALES_VALUE]));
            entChainMainMaterial.setPeriodDistribution(rs.getInt(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_PERIOD_DISTRIBUTION]));
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
            String sql = "SELECT * FROM " + TBL_CHAIN_MAIN_MATERIAL;
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
                ChainMainMaterial entChainMainMaterial = new ChainMainMaterial();
                resultToObject(rs, entChainMainMaterial);
                lists.add(entChainMainMaterial);
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

    public static boolean checkOID(long entChainMainMaterialId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CHAIN_MAIN_MATERIAL + " WHERE "
                    + PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_MAIN_MATERIAL_ID] + " = " + entChainMainMaterialId;
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
            String sql = "SELECT COUNT(" + PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_MAIN_MATERIAL_ID] + ") FROM " + TBL_CHAIN_MAIN_MATERIAL;
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
                    ChainMainMaterial entChainMainMaterial = (ChainMainMaterial) list.get(ls);
                    if (oid == entChainMainMaterial.getOID()) {
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

    public static double sumTotalCostPeriod(long chainPeriodId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " SUM(" + fieldNames[FLD_STOCK_QTY] + " * " + fieldNames[FLD_COST_VALUE] + ")"
                    + " FROM " + TBL_CHAIN_MAIN_MATERIAL
                    + " WHERE " + fieldNames[FLD_CHAIN_PERIOD_ID] + " = " + chainPeriodId
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (DBException | SQLException e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static double sumTotalSalesPeriod(long chainPeriodId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " SUM(" + fieldNames[FLD_STOCK_QTY] + " * " + fieldNames[FLD_SALES_VALUE] + ")"
                    + " FROM " + TBL_CHAIN_MAIN_MATERIAL
                    + " WHERE " + fieldNames[FLD_CHAIN_PERIOD_ID] + " = " + chainPeriodId
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (DBException | SQLException e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static double sumTotalCostPeriodSpecificCostType(long chainPeriodId, String costType) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " SUM(" + fieldNames[FLD_STOCK_QTY] + " * " + fieldNames[FLD_COST_VALUE] + ")"
                    + " FROM " + TBL_CHAIN_MAIN_MATERIAL
                    + " WHERE " + fieldNames[FLD_CHAIN_PERIOD_ID] + " = " + chainPeriodId
                    + " AND " + fieldNames[FLD_MATERIAL_TYPE] + " IN (" + costType + ")"
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (DBException | SQLException e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
         
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                ChainMainMaterial chainMainMaterial = PstChainMainMaterial.fetchExc(oid);
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_MAIN_MATERIAL_ID], chainMainMaterial.getOID());
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_PERIOD_ID], chainMainMaterial.getChainPeriodId());
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_COST_PCT], chainMainMaterial.getCostPct());
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_COST_TYPE], chainMainMaterial.getCostType());
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_COST_VALUE], chainMainMaterial.getCostValue());
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_MATERIAL_ID], chainMainMaterial.getMaterialId());
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_MATERIAL_TYPE], chainMainMaterial.getMaterialType());
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_PERIOD_DISTRIBUTION], chainMainMaterial.getPeriodDistribution());
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_SALES_VALUE], chainMainMaterial.getSalesValue());
                object.put(PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_STOCK_QTY], chainMainMaterial.getStockQty());
            }catch(Exception exc){}

            return object;
        }

}
