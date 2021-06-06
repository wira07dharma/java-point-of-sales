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
public class PstProduction extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PRODUCTION = "pos_production";
    public static final int FLD_PRODUCTION_ID = 0;
    public static final int FLD_PRODUCTION_NUMBER = 1;
    public static final int FLD_PRODUCTION_DATE = 2;
    public static final int FLD_PRODUCTION_STATUS = 3;
    public static final int FLD_LOCATION_FROM_ID = 4;
    public static final int FLD_LOCATION_TO_ID = 5;
    public static final int FLD_REMARK = 6;

    public static String[] fieldNames = {
        "PRODUCTION_ID",
        "PRODUCTION_NUMBER",
        "PRODUCTION_DATE",
        "PRODUCTION_STATUS",
        "LOCATION_FROM_ID",
        "LOCATION_TO_ID",
        "REMARK"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING
    };
    
    public static final int PRODUCTION_COST = 1;
    public static final int PRODUCTION_PRODUCT = 2;

    public PstProduction() {
    }

    public PstProduction(int i) throws DBException {
        super(new PstProduction());
    }

    public PstProduction(String sOid) throws DBException {
        super(new PstProduction(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstProduction(long lOid) throws DBException {
        super(new PstProduction(0));
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
        return TBL_PRODUCTION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstProduction().getClass().getName();
    }

    public static Production fetchExc(long oid) throws DBException {
        try {
            Production entProduction = new Production();
            PstProduction pstProduction = new PstProduction(oid);
            entProduction.setOID(oid);
            entProduction.setProductionNumber(pstProduction.getString(FLD_PRODUCTION_NUMBER));
            entProduction.setProductionDate(pstProduction.getDate(FLD_PRODUCTION_DATE));
            entProduction.setProductionStatus(pstProduction.getInt(FLD_PRODUCTION_STATUS));
            entProduction.setLocationFromId(pstProduction.getlong(FLD_LOCATION_FROM_ID));
            entProduction.setLocationToId(pstProduction.getlong(FLD_LOCATION_TO_ID));
            entProduction.setRemark(pstProduction.getString(FLD_REMARK));
            return entProduction;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProduction(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        Production entProduction = fetchExc(entity.getOID());
        entity = (Entity) entProduction;
        return entProduction.getOID();
    }

    public static synchronized long updateExc(Production entProduction) throws DBException {
        try {
            if (entProduction.getOID() != 0) {
                PstProduction pstProduction = new PstProduction(entProduction.getOID());
                pstProduction.setString(FLD_PRODUCTION_NUMBER, entProduction.getProductionNumber());
                pstProduction.setDate(FLD_PRODUCTION_DATE, entProduction.getProductionDate());
                pstProduction.setInt(FLD_PRODUCTION_STATUS, entProduction.getProductionStatus());
                pstProduction.setLong(FLD_LOCATION_FROM_ID, entProduction.getLocationFromId());
                pstProduction.setLong(FLD_LOCATION_TO_ID, entProduction.getLocationToId());
                pstProduction.setString(FLD_REMARK, entProduction.getRemark());
                pstProduction.update();
                return entProduction.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProduction(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((Production) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstProduction pstProduction = new PstProduction(oid);
            pstProduction.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProduction(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(Production entProduction) throws DBException {
        try {
            PstProduction pstProduction = new PstProduction(0);
            pstProduction.setString(FLD_PRODUCTION_NUMBER, entProduction.getProductionNumber());
            pstProduction.setDate(FLD_PRODUCTION_DATE, entProduction.getProductionDate());
            pstProduction.setInt(FLD_PRODUCTION_STATUS, entProduction.getProductionStatus());
            pstProduction.setLong(FLD_LOCATION_FROM_ID, entProduction.getLocationFromId());
            pstProduction.setLong(FLD_LOCATION_TO_ID, entProduction.getLocationToId());
            pstProduction.setString(FLD_REMARK, entProduction.getRemark());
            pstProduction.insert();
            entProduction.setOID(pstProduction.getlong(FLD_PRODUCTION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstProduction(0), DBException.UNKNOWN);
        }
        return entProduction.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((Production) entity);
    }

    public static void resultToObject(ResultSet rs, Production entProduction) {
        try {
            entProduction.setOID(rs.getLong(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID]));
            entProduction.setProductionNumber(rs.getString(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER]));
            entProduction.setProductionDate(rs.getDate(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE]));
            entProduction.setProductionStatus(rs.getInt(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS]));
            entProduction.setLocationFromId(rs.getLong(PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID]));
            entProduction.setLocationToId(rs.getLong(PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID]));
            entProduction.setRemark(rs.getString(PstProduction.fieldNames[PstProduction.FLD_REMARK]));
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
            String sql = "SELECT * FROM " + TBL_PRODUCTION;
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
                Production entProduction = new Production();
                resultToObject(rs, entProduction);
                lists.add(entProduction);
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

    public static boolean checkOID(long entProductionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PRODUCTION + " WHERE "
                    + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID] + " = " + entProductionId;
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
            String sql = "SELECT COUNT(" + PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID] + ") FROM " + TBL_PRODUCTION;
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
                    Production entProduction = (Production) list.get(ls);
                    if (oid == entProduction.getOID()) {
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
         Production production = PstProduction.fetchExc(oid);
         object.put(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_ID], production.getOID());
         object.put(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_NUMBER], production.getProductionNumber());
         object.put(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_DATE], production.getProductionDate());
         object.put(PstProduction.fieldNames[PstProduction.FLD_PRODUCTION_STATUS], production.getProductionStatus());
         object.put(PstProduction.fieldNames[PstProduction.FLD_LOCATION_FROM_ID], production.getLocationFromId());
         object.put(PstProduction.fieldNames[PstProduction.FLD_LOCATION_TO_ID], production.getLocationToId());
         object.put(PstProduction.fieldNames[PstProduction.FLD_REMARK], production.getRemark());
      }catch(Exception exc){}
      return object;
   }
}
