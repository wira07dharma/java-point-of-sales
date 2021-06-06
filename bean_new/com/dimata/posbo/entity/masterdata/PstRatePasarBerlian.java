/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author Regen
 */
public class PstRatePasarBerlian extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_RATEPASARBERLIAN = "pos_rate_pasar_berlian";
  public static final int FLD_RATEPASARID = 0;
  public static final int FLD_CODE = 1;
  public static final int FLD_NAME = 2;
  public static final int FLD_RATE = 3;
  public static final int FLD_DESCRIPTION = 4;
  public static final int FLD_UPDATEDATE = 5;

  public static String[] fieldNames = {
    "RATE_PASAR_ID",
    "CODE",
    "NAME",
    "RATE",
    "DESCRIPTION",
    "UPDATE_DATE"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_FLOAT,
    TYPE_STRING,
    TYPE_DATE
  };

  public PstRatePasarBerlian() {
  }

  public PstRatePasarBerlian(int i) throws DBException {
    super(new PstRatePasarBerlian());
  }

  public PstRatePasarBerlian(String sOid) throws DBException {
    super(new PstRatePasarBerlian(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstRatePasarBerlian(long lOid) throws DBException {
    super(new PstRatePasarBerlian(0));
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
    return TBL_RATEPASARBERLIAN;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstRatePasarBerlian().getClass().getName();
  }

  public static RatePasarBerlian fetchExc(long oid) throws DBException {
    try {
      RatePasarBerlian entRatePasarBerlian = new RatePasarBerlian();
      PstRatePasarBerlian pstRatePasarBerlian = new PstRatePasarBerlian(oid);
      entRatePasarBerlian.setOID(oid);
      entRatePasarBerlian.setCode(pstRatePasarBerlian.getString(FLD_CODE));
      entRatePasarBerlian.setName(pstRatePasarBerlian.getString(FLD_NAME));
      entRatePasarBerlian.setRate(pstRatePasarBerlian.getdouble(FLD_RATE));
      entRatePasarBerlian.setDescription(pstRatePasarBerlian.getString(FLD_DESCRIPTION));
      entRatePasarBerlian.setUpdateDate(pstRatePasarBerlian.getDate(FLD_UPDATEDATE));
      return entRatePasarBerlian;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstRatePasarBerlian(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    RatePasarBerlian entRatePasarBerlian = fetchExc(entity.getOID());
    entity = (Entity) entRatePasarBerlian;
    return entRatePasarBerlian.getOID();
  }

  public static synchronized long updateExc(RatePasarBerlian entRatePasarBerlian) throws DBException {
    try {
      if (entRatePasarBerlian.getOID() != 0) {
        PstRatePasarBerlian pstRatePasarBerlian = new PstRatePasarBerlian(entRatePasarBerlian.getOID());
        pstRatePasarBerlian.setString(FLD_CODE, entRatePasarBerlian.getCode());
        pstRatePasarBerlian.setString(FLD_NAME, entRatePasarBerlian.getName());
        pstRatePasarBerlian.setDouble(FLD_RATE, entRatePasarBerlian.getRate());
        pstRatePasarBerlian.setString(FLD_DESCRIPTION, entRatePasarBerlian.getDescription());
        pstRatePasarBerlian.setDate(FLD_UPDATEDATE, entRatePasarBerlian.getUpdateDate());
        pstRatePasarBerlian.update();
        return entRatePasarBerlian.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstRatePasarBerlian(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((RatePasarBerlian) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstRatePasarBerlian pstRatePasarBerlian = new PstRatePasarBerlian(oid);
      pstRatePasarBerlian.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstRatePasarBerlian(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(RatePasarBerlian entRatePasarBerlian) throws DBException {
    try {
      if (entRatePasarBerlian.getUpdateDate() == null) {
        entRatePasarBerlian.setUpdateDate(new Date());
      }
      PstRatePasarBerlian pstRatePasarBerlian = new PstRatePasarBerlian(0);
      pstRatePasarBerlian.setString(FLD_CODE, entRatePasarBerlian.getCode());
      pstRatePasarBerlian.setString(FLD_NAME, entRatePasarBerlian.getName());
      pstRatePasarBerlian.setDouble(FLD_RATE, entRatePasarBerlian.getRate());
      pstRatePasarBerlian.setString(FLD_DESCRIPTION, entRatePasarBerlian.getDescription());
        pstRatePasarBerlian.setDate(FLD_UPDATEDATE, entRatePasarBerlian.getUpdateDate());
      pstRatePasarBerlian.insert();
      entRatePasarBerlian.setOID(pstRatePasarBerlian.getlong(FLD_RATEPASARID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstRatePasarBerlian(0), DBException.UNKNOWN);
    }
    return entRatePasarBerlian.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((RatePasarBerlian) entity);
  }

  public static void resultToObject(ResultSet rs, RatePasarBerlian entRatePasarBerlian) {
    try {
      entRatePasarBerlian.setOID(rs.getLong(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_RATEPASARID]));
      entRatePasarBerlian.setCode(rs.getString(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_CODE]));
      entRatePasarBerlian.setName(rs.getString(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_NAME]));
      entRatePasarBerlian.setRate(rs.getDouble(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_RATE]));
      entRatePasarBerlian.setDescription(rs.getString(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_DESCRIPTION]));
      entRatePasarBerlian.setUpdateDate(rs.getDate(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_UPDATEDATE]));
    } catch (Exception e) {
    }
  }

  public static Vector listAll() {
    return list(0, 500, "", "");
  }

  public static String getQueryAll() {
    return "SELECT *, IF(a.`active` IS NOT NULL, 'Aktif', 'Histori') as `status` FROM pos_rate_pasar_berlian pb LEFT JOIN (SELECT p1.`RATE_PASAR_ID` AS `active` FROM pos_rate_pasar_berlian p1 WHERE p1.`UPDATE_DATE` = (SELECT MAX(p2.`UPDATE_DATE`) FROM pos_rate_pasar_berlian p2 WHERE p1.`CODE` = p2.`CODE`)) a ON pb.`RATE_PASAR_ID` = a.`active`";
  }

  public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
    Vector lists = new Vector();
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT * FROM " + TBL_RATEPASARBERLIAN;
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
        RatePasarBerlian entRatePasarBerlian = new RatePasarBerlian();
        resultToObject(rs, entRatePasarBerlian);
        lists.add(entRatePasarBerlian);
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

  public static boolean checkOID(long entRatePasarBerlianId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_RATEPASARBERLIAN + " WHERE "
              + PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_RATEPASARID] + " = " + entRatePasarBerlianId;
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
      String sql = "SELECT COUNT(" + PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_RATEPASARID] + ") FROM " + TBL_RATEPASARBERLIAN;
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
          RatePasarBerlian entRatePasarBerlian = (RatePasarBerlian) list.get(ls);
          if (oid == entRatePasarBerlian.getOID()) {
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
                RatePasarBerlian ratePasarBerlian = PstRatePasarBerlian.fetchExc(oid);
                object.put(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_RATEPASARID], ratePasarBerlian.getOID());
                object.put(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_CODE], ratePasarBerlian.getCode());
                object.put(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_DESCRIPTION], ratePasarBerlian.getDescription());
                object.put(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_NAME], ratePasarBerlian.getName());
                object.put(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_RATE], ratePasarBerlian.getRate());
                object.put(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_UPDATEDATE], ratePasarBerlian.getUpdateDate());
            }catch(Exception exc){}

            return object;
        }
}
