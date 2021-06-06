/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

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
public class PstProductionCost extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PRODUCTION_COST = "pos_production_cost";
    public static final int FLD_PRODUCTION_COST_ID = 0;
    public static final int FLD_PRODUCTION_GROUP_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_STOCK_QTY = 3;
    public static final int FLD_STOCK_VALUE = 4;
    public static final int FLD_COST_TYPE = 5;
    public static final int FLD_PRODUCT_DISTRIBUTION_ID = 6;

    public static String[] fieldNames = {
        "PRODUCTION_COST_ID",
        "PRODUCTION_GROUP_ID",
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
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG
    };

    public static final int COST_TYPE_GENERAL = 0;
    public static final int COST_TYPE_REFERENCED = 1;

    public static final String[] COST_TYPE_TITLE = {
        "General Cost", "Referenced Cost"
    };

    public PstProductionCost() {
    }

    public PstProductionCost(int i) throws DBException {
        super(new PstProductionCost());
    }

    public PstProductionCost(String sOid) throws DBException {
        super(new PstProductionCost(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstProductionCost(long lOid) throws DBException {
        super(new PstProductionCost(0));
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
        return TBL_PRODUCTION_COST;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstProductionCost().getClass().getName();
    }

    public static ProductionCost fetchExc(long oid) throws DBException {
        try {
            ProductionCost entProductionCost = new ProductionCost();
            PstProductionCost pstProductionCost = new PstProductionCost(oid);
            entProductionCost.setOID(oid);
            entProductionCost.setProductionGroupId(pstProductionCost.getlong(FLD_PRODUCTION_GROUP_ID));
            entProductionCost.setMaterialId(pstProductionCost.getlong(FLD_MATERIAL_ID));
            entProductionCost.setStockQty(pstProductionCost.getdouble(FLD_STOCK_QTY));
            entProductionCost.setStockValue(pstProductionCost.getdouble(FLD_STOCK_VALUE));
            entProductionCost.setCostType(pstProductionCost.getInt(FLD_COST_TYPE));
            entProductionCost.setProductDistributionId(pstProductionCost.getlong(FLD_PRODUCT_DISTRIBUTION_ID));
            return entProductionCost;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionCost(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ProductionCost entProductionCost = fetchExc(entity.getOID());
        entity = (Entity) entProductionCost;
        return entProductionCost.getOID();
    }

    public static synchronized long updateExc(ProductionCost entProductionCost) throws DBException {
        try {
            if (entProductionCost.getOID() != 0) {
                PstProductionCost pstProductionCost = new PstProductionCost(entProductionCost.getOID());
                pstProductionCost.setLong(FLD_PRODUCTION_GROUP_ID, entProductionCost.getProductionGroupId());
                pstProductionCost.setLong(FLD_MATERIAL_ID, entProductionCost.getMaterialId());
                pstProductionCost.setDouble(FLD_STOCK_QTY, entProductionCost.getStockQty());
                pstProductionCost.setDouble(FLD_STOCK_VALUE, entProductionCost.getStockValue());
                pstProductionCost.setInt(FLD_COST_TYPE, entProductionCost.getCostType());
                pstProductionCost.setLong(FLD_PRODUCT_DISTRIBUTION_ID, entProductionCost.getProductDistributionId());
                pstProductionCost.update();
                return entProductionCost.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionCost(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ProductionCost) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstProductionCost pstProductionCost = new PstProductionCost(oid);
            pstProductionCost.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionCost(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ProductionCost entProductionCost) throws DBException {
        try {
            PstProductionCost pstProductionCost = new PstProductionCost(0);
            pstProductionCost.setLong(FLD_PRODUCTION_GROUP_ID, entProductionCost.getProductionGroupId());
            pstProductionCost.setLong(FLD_MATERIAL_ID, entProductionCost.getMaterialId());
            pstProductionCost.setDouble(FLD_STOCK_QTY, entProductionCost.getStockQty());
            pstProductionCost.setDouble(FLD_STOCK_VALUE, entProductionCost.getStockValue());
            pstProductionCost.setInt(FLD_COST_TYPE, entProductionCost.getCostType());
            pstProductionCost.setLong(FLD_PRODUCT_DISTRIBUTION_ID, entProductionCost.getProductDistributionId());
            pstProductionCost.insert();
            entProductionCost.setOID(pstProductionCost.getlong(FLD_PRODUCTION_COST_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionCost(0), DBException.UNKNOWN);
        }
        return entProductionCost.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ProductionCost) entity);
    }

    public static void resultToObject(ResultSet rs, ProductionCost entProductionCost) {
        try {
            entProductionCost.setOID(rs.getLong(PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_COST_ID]));
            entProductionCost.setProductionGroupId(rs.getLong(PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID]));
            entProductionCost.setMaterialId(rs.getLong(PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID]));
            entProductionCost.setStockQty(rs.getDouble(PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_QTY]));
            entProductionCost.setStockValue(rs.getDouble(PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_VALUE]));
            entProductionCost.setCostType(rs.getInt(PstProductionCost.fieldNames[PstProductionCost.FLD_COST_TYPE]));
            entProductionCost.setProductDistributionId(rs.getLong(PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCT_DISTRIBUTION_ID]));
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
            String sql = "SELECT * FROM " + TBL_PRODUCTION_COST;
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
                ProductionCost entProductionCost = new ProductionCost();
                resultToObject(rs, entProductionCost);
                lists.add(entProductionCost);
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

    public static boolean checkOID(long entProductionCostId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PRODUCTION_COST + " WHERE "
                    + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_COST_ID] + " = " + entProductionCostId;
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
            String sql = "SELECT COUNT(" + PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_COST_ID] + ") FROM " + TBL_PRODUCTION_COST;
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
                    ProductionCost entProductionCost = (ProductionCost) list.get(ls);
                    if (oid == entProductionCost.getOID()) {
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
         ProductionCost productionCost = PstProductionCost.fetchExc(oid);
         object.put(PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_COST_ID], productionCost.getOID());
         object.put(PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID], productionCost.getProductionGroupId());
         object.put(PstProductionCost.fieldNames[PstProductionCost.FLD_MATERIAL_ID], productionCost.getMaterialId());
         object.put(PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_QTY], productionCost.getStockQty());
         object.put(PstProductionCost.fieldNames[PstProductionCost.FLD_STOCK_VALUE], productionCost.getStockValue());
         object.put(PstProductionCost.fieldNames[PstProductionCost.FLD_COST_TYPE], productionCost.getCostType());
         object.put(PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCT_DISTRIBUTION_ID], productionCost.getProductDistributionId());
      }catch(Exception exc){}
      return object;
   }
}
