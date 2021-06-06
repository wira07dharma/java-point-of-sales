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
public class PstProductionGroup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PRODUCTION_GROUP = "pos_production_group";
    public static final int FLD_PRODUCTION_GROUP_ID = 0;
    public static final int FLD_PRODUCTION_ID = 1;
    public static final int FLD_BATCH_NUMBER = 2;
    public static final int FLD_CHAIN_PERIOD_ID = 3;
    public static final int FLD_DATE_START = 4;
    public static final int FLD_DATE_END = 5;
    public static final int FLD_PRODUCTION_GROUP_PARENT_ID = 6;
    public static final int FLD_INDEX = 7;

    public static String[] fieldNames = {
        "PRODUCTION_GROUP_ID",
        "PRODUCTION_ID",
        "BATCH_NUMBER",
        "CHAIN_PERIOD_ID",
        "DATE_START",
        "DATE_END",
        "PRODUCTION_GROUP_PARENT_ID",
        "GROUP_INDEX"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_INT
    };

    public PstProductionGroup() {
    }

    public PstProductionGroup(int i) throws DBException {
        super(new PstProductionGroup());
    }

    public PstProductionGroup(String sOid) throws DBException {
        super(new PstProductionGroup(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstProductionGroup(long lOid) throws DBException {
        super(new PstProductionGroup(0));
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
        return TBL_PRODUCTION_GROUP;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstProductionGroup().getClass().getName();
    }

    public static ProductionGroup fetchExc(long oid) throws DBException {
        try {
            ProductionGroup entProductionGroup = new ProductionGroup();
            PstProductionGroup pstProductionGroup = new PstProductionGroup(oid);
            entProductionGroup.setOID(oid);
            entProductionGroup.setProductionId(pstProductionGroup.getlong(FLD_PRODUCTION_ID));
            entProductionGroup.setBatchNumber(pstProductionGroup.getString(FLD_BATCH_NUMBER));
            entProductionGroup.setChainPeriodId(pstProductionGroup.getlong(FLD_CHAIN_PERIOD_ID));
            entProductionGroup.setDateStart(pstProductionGroup.getDate(FLD_DATE_START));
            entProductionGroup.setDateEnd(pstProductionGroup.getDate(FLD_DATE_END));
            entProductionGroup.setProductionGroupParentId(pstProductionGroup.getlong(FLD_PRODUCTION_GROUP_PARENT_ID));
            entProductionGroup.setIndex(pstProductionGroup.getInt(FLD_INDEX));
            return entProductionGroup;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionGroup(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ProductionGroup entProductionGroup = fetchExc(entity.getOID());
        entity = (Entity) entProductionGroup;
        return entProductionGroup.getOID();
    }

    public static synchronized long updateExc(ProductionGroup entProductionGroup) throws DBException {
        try {
            if (entProductionGroup.getOID() != 0) {
                PstProductionGroup pstProductionGroup = new PstProductionGroup(entProductionGroup.getOID());
                pstProductionGroup.setLong(FLD_PRODUCTION_ID, entProductionGroup.getProductionId());
                pstProductionGroup.setString(FLD_BATCH_NUMBER, entProductionGroup.getBatchNumber());
                pstProductionGroup.setLong(FLD_CHAIN_PERIOD_ID, entProductionGroup.getChainPeriodId());
                pstProductionGroup.setDate(FLD_DATE_START, entProductionGroup.getDateStart());
                pstProductionGroup.setDate(FLD_DATE_END, entProductionGroup.getDateEnd());
                pstProductionGroup.setLong(FLD_PRODUCTION_GROUP_PARENT_ID, entProductionGroup.getProductionGroupParentId());
                pstProductionGroup.setInt(FLD_INDEX, entProductionGroup.getIndex());
                pstProductionGroup.update();
                return entProductionGroup.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionGroup(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ProductionGroup) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstProductionGroup pstProductionGroup = new PstProductionGroup(oid);
            pstProductionGroup.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionGroup(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ProductionGroup entProductionGroup) throws DBException {
        try {
            PstProductionGroup pstProductionGroup = new PstProductionGroup(0);
            pstProductionGroup.setLong(FLD_PRODUCTION_ID, entProductionGroup.getProductionId());
            pstProductionGroup.setString(FLD_BATCH_NUMBER, entProductionGroup.getBatchNumber());
            pstProductionGroup.setLong(FLD_CHAIN_PERIOD_ID, entProductionGroup.getChainPeriodId());
            pstProductionGroup.setDate(FLD_DATE_START, entProductionGroup.getDateStart());
            pstProductionGroup.setDate(FLD_DATE_END, entProductionGroup.getDateEnd());
            pstProductionGroup.setLong(FLD_PRODUCTION_GROUP_PARENT_ID, entProductionGroup.getProductionGroupParentId());
            pstProductionGroup.setInt(FLD_INDEX, entProductionGroup.getIndex());
            pstProductionGroup.insert();
            entProductionGroup.setOID(pstProductionGroup.getlong(FLD_PRODUCTION_GROUP_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProductionGroup(0), DBException.UNKNOWN);
        }
        return entProductionGroup.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ProductionGroup) entity);
    }

    public static void resultToObject(ResultSet rs, ProductionGroup entProductionGroup) {
        try {
            entProductionGroup.setOID(rs.getLong(PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID]));
            entProductionGroup.setProductionId(rs.getLong(PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID]));
            entProductionGroup.setBatchNumber(rs.getString(PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER]));
            entProductionGroup.setChainPeriodId(rs.getLong(PstProductionGroup.fieldNames[PstProductionGroup.FLD_CHAIN_PERIOD_ID]));
            entProductionGroup.setDateStart(rs.getTimestamp(PstProductionGroup.fieldNames[PstProductionGroup.FLD_DATE_START]));
            entProductionGroup.setDateEnd(rs.getTimestamp(PstProductionGroup.fieldNames[PstProductionGroup.FLD_DATE_END]));
            entProductionGroup.setProductionGroupParentId(rs.getLong(PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_PARENT_ID]));
            entProductionGroup.setIndex(rs.getInt(PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX]));
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
            String sql = "SELECT * FROM " + TBL_PRODUCTION_GROUP;
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
                ProductionGroup entProductionGroup = new ProductionGroup();
                resultToObject(rs, entProductionGroup);
                lists.add(entProductionGroup);
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

    public static boolean checkOID(long entProductionGroupId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PRODUCTION_GROUP + " WHERE "
                    + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID] + " = " + entProductionGroupId;
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
            String sql = "SELECT COUNT(" + PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID] + ") FROM " + TBL_PRODUCTION_GROUP;
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
                    ProductionGroup entProductionGroup = (ProductionGroup) list.get(ls);
                    if (oid == entProductionGroup.getOID()) {
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
         ProductionGroup productionGroup = PstProductionGroup.fetchExc(oid);
         object.put(PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_ID], productionGroup.getOID());
         object.put(PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID], productionGroup.getProductionId());
         object.put(PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER], productionGroup.getBatchNumber());
         object.put(PstProductionGroup.fieldNames[PstProductionGroup.FLD_CHAIN_PERIOD_ID], productionGroup.getChainPeriodId());
         object.put(PstProductionGroup.fieldNames[PstProductionGroup.FLD_DATE_START], productionGroup.getDateStart());
         object.put(PstProductionGroup.fieldNames[PstProductionGroup.FLD_DATE_END], productionGroup.getDateEnd());
         object.put(PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_GROUP_PARENT_ID], productionGroup.getProductionGroupParentId());
         object.put(PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX], productionGroup.getIndex());
      }catch(Exception exc){}
      return object;
   }
}
