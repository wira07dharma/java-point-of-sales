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
public class PstProductionProduct extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PRODUCTION_PRODUCT = "pos_production_product";
    public static final int FLD_PRODUCTION_PRODUCT_ID = 0;
    public static final int FLD_PRODUCTION_GROUP_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_MATERIAL_TYPE = 3;
    public static final int FLD_COST_PCT = 4;
    public static final int FLD_COST_TYPE = 5;
    public static final int FLD_STOCK_QTY = 6;
    public static final int FLD_COST_VALUE = 7;
    public static final int FLD_SALES_VALUE = 8;
    public static final int FLD_PERIOD_DISTRIBUTION = 9;

    public static String[] fieldNames = {
        "PRODUCTION_PRODUCT_ID",
        "PRODUCTION_GROUP_ID",
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
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT
    };

    public static final int TYPE_RESULT_NEXT_COST = 0;
    public static final int TYPE_RESULT = 1;
    public static final int TYPE_NEXT_COST = 2;
    public static final int TYPE_REFERENCED_COST = 3;

    public static final String[] TYPE_TITLE = {
        "Result & Next Cost", "Result", "Next Cost", "Referenced Cost"
    };

    public static final int COST_TYPE_AUTO = 0;
    public static final int COST_TYPE_CUSTOM = 1;
    public static final int COST_TYPE_REFERENCED = 2;
    public static final String[] COST_TYPE_TITLE = {
        "Auto", "Custom", "Referenced"
    };

    public PstProductionProduct() {
    }

    public PstProductionProduct(int i) throws DBException {
        super(new PstProductionProduct());
    }

    public PstProductionProduct(String sOid) throws DBException {
        super(new PstProductionProduct(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstProductionProduct(long lOid) throws DBException {
        super(new PstProductionProduct(0));
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
        return TBL_PRODUCTION_PRODUCT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstProductionProduct().getClass().getName();
    }

    public static ProductionProduct fetchExc(long oid) throws DBException {
        try {
            ProductionProduct entProductionProduct = new ProductionProduct();
            PstProductionProduct pstProductionProduct = new PstProductionProduct(oid);
            entProductionProduct.setOID(oid);
            entProductionProduct.setProductionGroupId(pstProductionProduct.getlong(FLD_PRODUCTION_GROUP_ID));
            entProductionProduct.setMaterialId(pstProductionProduct.getlong(FLD_MATERIAL_ID));
            entProductionProduct.setMaterialType(pstProductionProduct.getInt(FLD_MATERIAL_TYPE));
            entProductionProduct.setCostPct(pstProductionProduct.getdouble(FLD_COST_PCT));
            entProductionProduct.setCostType(pstProductionProduct.getInt(FLD_COST_TYPE));
            entProductionProduct.setStockQty(pstProductionProduct.getdouble(FLD_STOCK_QTY));
            entProductionProduct.setCostValue(pstProductionProduct.getdouble(FLD_COST_VALUE));
            entProductionProduct.setSalesValue(pstProductionProduct.getdouble(FLD_SALES_VALUE));
            entProductionProduct.setPeriodDistribution(pstProductionProduct.getInt(FLD_PERIOD_DISTRIBUTION));
            return entProductionProduct;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionProduct(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ProductionProduct entProductionProduct = fetchExc(entity.getOID());
        entity = (Entity) entProductionProduct;
        return entProductionProduct.getOID();
    }

    public static synchronized long updateExc(ProductionProduct entProductionProduct) throws DBException {
        try {
            if (entProductionProduct.getOID() != 0) {
                PstProductionProduct pstProductionProduct = new PstProductionProduct(entProductionProduct.getOID());
                pstProductionProduct.setLong(FLD_PRODUCTION_GROUP_ID, entProductionProduct.getProductionGroupId());
                pstProductionProduct.setLong(FLD_MATERIAL_ID, entProductionProduct.getMaterialId());
                pstProductionProduct.setInt(FLD_MATERIAL_TYPE, entProductionProduct.getMaterialType());
                pstProductionProduct.setDouble(FLD_COST_PCT, entProductionProduct.getCostPct());
                pstProductionProduct.setInt(FLD_COST_TYPE, entProductionProduct.getCostType());
                pstProductionProduct.setDouble(FLD_STOCK_QTY, entProductionProduct.getStockQty());
                pstProductionProduct.setDouble(FLD_COST_VALUE, entProductionProduct.getCostValue());
                pstProductionProduct.setDouble(FLD_SALES_VALUE, entProductionProduct.getSalesValue());
                pstProductionProduct.setInt(FLD_PERIOD_DISTRIBUTION, entProductionProduct.getPeriodDistribution());
                pstProductionProduct.update();
                return entProductionProduct.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionProduct(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ProductionProduct) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstProductionProduct pstProductionProduct = new PstProductionProduct(oid);
            pstProductionProduct.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionProduct(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ProductionProduct entProductionProduct) throws DBException {
        try {
            PstProductionProduct pstProductionProduct = new PstProductionProduct(0);
            pstProductionProduct.setLong(FLD_PRODUCTION_GROUP_ID, entProductionProduct.getProductionGroupId());
            pstProductionProduct.setLong(FLD_MATERIAL_ID, entProductionProduct.getMaterialId());
            pstProductionProduct.setInt(FLD_MATERIAL_TYPE, entProductionProduct.getMaterialType());
            pstProductionProduct.setDouble(FLD_COST_PCT, entProductionProduct.getCostPct());
            pstProductionProduct.setInt(FLD_COST_TYPE, entProductionProduct.getCostType());
            pstProductionProduct.setDouble(FLD_STOCK_QTY, entProductionProduct.getStockQty());
            pstProductionProduct.setDouble(FLD_COST_VALUE, entProductionProduct.getCostValue());
            pstProductionProduct.setDouble(FLD_SALES_VALUE, entProductionProduct.getSalesValue());
            pstProductionProduct.setInt(FLD_PERIOD_DISTRIBUTION, entProductionProduct.getPeriodDistribution());
            pstProductionProduct.insert();
            entProductionProduct.setOID(pstProductionProduct.getlong(FLD_PRODUCTION_PRODUCT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionProduct(0), DBException.UNKNOWN);
        }
        return entProductionProduct.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ProductionProduct) entity);
    }

    public static void resultToObject(ResultSet rs, ProductionProduct entProductionProduct) {
        try {
            entProductionProduct.setOID(rs.getLong(PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID]));
            entProductionProduct.setProductionGroupId(rs.getLong(PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID]));
            entProductionProduct.setMaterialId(rs.getLong(PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID]));
            entProductionProduct.setMaterialType(rs.getInt(PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_TYPE]));
            entProductionProduct.setCostPct(rs.getDouble(PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_PCT]));
            entProductionProduct.setCostType(rs.getInt(PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_TYPE]));
            entProductionProduct.setStockQty(rs.getDouble(PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY]));
            entProductionProduct.setCostValue(rs.getDouble(PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE]));
            entProductionProduct.setSalesValue(rs.getDouble(PstProductionProduct.fieldNames[PstProductionProduct.FLD_SALES_VALUE]));
            entProductionProduct.setPeriodDistribution(rs.getInt(PstProductionProduct.fieldNames[PstProductionProduct.FLD_PERIOD_DISTRIBUTION]));
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
            String sql = "SELECT * FROM " + TBL_PRODUCTION_PRODUCT;
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
                ProductionProduct entProductionProduct = new ProductionProduct();
                resultToObject(rs, entProductionProduct);
                lists.add(entProductionProduct);
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

    public static boolean checkOID(long entProductionProductId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PRODUCTION_PRODUCT + " WHERE "
                    + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID] + " = " + entProductionProductId;
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
            String sql = "SELECT COUNT(" + PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID] + ") FROM " + TBL_PRODUCTION_PRODUCT;
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
                    ProductionProduct entProductionProduct = (ProductionProduct) list.get(ls);
                    if (oid == entProductionProduct.getOID()) {
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
         ProductionProduct productionProduct = PstProductionProduct.fetchExc(oid);
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_PRODUCT_ID], productionProduct.getOID());
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID], productionProduct.getProductionGroupId());
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_ID], productionProduct.getMaterialId());
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_MATERIAL_TYPE], productionProduct.getMaterialType());
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_PCT], productionProduct.getCostPct());
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_TYPE], productionProduct.getCostType());
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_STOCK_QTY], productionProduct.getStockQty());
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_COST_VALUE], productionProduct.getCostValue());
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_SALES_VALUE], productionProduct.getSalesValue());
         object.put(PstProductionProduct.fieldNames[PstProductionProduct.FLD_PERIOD_DISTRIBUTION], productionProduct.getPeriodDistribution());
      }catch(Exception exc){}
      return object;
   }
}
