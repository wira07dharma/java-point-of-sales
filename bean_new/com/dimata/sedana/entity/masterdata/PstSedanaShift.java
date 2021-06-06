package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.Vector;

public class PstSedanaShift extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_SEDANASHIFT = "sedana_shift";
  public static final int FLD_SHIFT_ID = 0;
  public static final int FLD_NAME = 1;
  public static final int FLD_END_TIME = 2;
  public static final int FLD_START_TIME = 3;
  public static final int FLD_REMARK = 4;

  public static String[] fieldNames = {
    "SHIFT_ID",
    "NAME",
    "END_TIME",
    "START_TIME",
    "REMARK"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_STRING,
    TYPE_DATE,
    TYPE_DATE,
    TYPE_STRING
  };

  public PstSedanaShift() {
  }

  public PstSedanaShift(int i) throws DBException {
    super(new PstSedanaShift());
  }

  public PstSedanaShift(String sOid) throws DBException {
    super(new PstSedanaShift(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstSedanaShift(long lOid) throws DBException {
    super(new PstSedanaShift(0));
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
    return TBL_SEDANASHIFT;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstSedanaShift().getClass().getName();
  }

  public static SedanaShift fetchExc(long oid) throws DBException {
    try {
      SedanaShift entSedanaShift = new SedanaShift();
      PstSedanaShift pstSedanaShift = new PstSedanaShift(oid);
      entSedanaShift.setOID(oid); 
      entSedanaShift.setName(pstSedanaShift.getString(FLD_NAME));
      entSedanaShift.setEndTime(pstSedanaShift.getDate(FLD_END_TIME));
      entSedanaShift.setStartTime(pstSedanaShift.getDate(FLD_START_TIME)); 
      entSedanaShift.setRemark(pstSedanaShift.getString(FLD_REMARK));
      return entSedanaShift;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstSedanaShift(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    SedanaShift entSedanaShift = fetchExc(entity.getOID());
    entity = (Entity) entSedanaShift;
    return entSedanaShift.getOID();
  }

  public static synchronized long updateExc(SedanaShift entSedanaShift) throws DBException {
    try {
      if (entSedanaShift.getOID() != 0) {
        PstSedanaShift pstSedanaShift = new PstSedanaShift(entSedanaShift.getOID());
        pstSedanaShift.setString(FLD_NAME, entSedanaShift.getName());
        pstSedanaShift.setDate(FLD_END_TIME, entSedanaShift.getEndTime());
        pstSedanaShift.setDate(FLD_START_TIME, entSedanaShift.getStartTime());
        pstSedanaShift.setString(FLD_REMARK, entSedanaShift.getRemark());
        pstSedanaShift.update();
        return entSedanaShift.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstSedanaShift(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((SedanaShift) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstSedanaShift pstSedanaShift = new PstSedanaShift(oid);
      pstSedanaShift.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstSedanaShift(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(SedanaShift entSedanaShift) throws DBException {
    try {
      PstSedanaShift pstSedanaShift = new PstSedanaShift(0);
      pstSedanaShift.setString(FLD_NAME, entSedanaShift.getName());
      pstSedanaShift.setDate(FLD_END_TIME, entSedanaShift.getEndTime());
      pstSedanaShift.setDate(FLD_START_TIME, entSedanaShift.getStartTime());
      pstSedanaShift.setString(FLD_REMARK, entSedanaShift.getRemark());
      pstSedanaShift.insert();
      entSedanaShift.setOID(pstSedanaShift.getlong(FLD_SHIFT_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstSedanaShift(0), DBException.UNKNOWN);
    }
    return entSedanaShift.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((SedanaShift) entity);
  }

  public static void resultToObject(ResultSet rs, SedanaShift entSedanaShift) {
    try {
      entSedanaShift.setOID(rs.getLong(PstSedanaShift.fieldNames[PstSedanaShift.FLD_SHIFT_ID]));
      entSedanaShift.setName(rs.getString(PstSedanaShift.fieldNames[PstSedanaShift.FLD_NAME]));
      entSedanaShift.setEndTime(Formater.formatDate(rs.getString(PstSedanaShift.fieldNames[PstSedanaShift.FLD_END_TIME])+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
      entSedanaShift.setStartTime(Formater.formatDate(rs.getString(PstSedanaShift.fieldNames[PstSedanaShift.FLD_START_TIME])+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
      entSedanaShift.setRemark(rs.getString(PstSedanaShift.fieldNames[PstSedanaShift.FLD_REMARK]));
      ;
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
      String sql = "SELECT * FROM " + TBL_SEDANASHIFT;
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
        SedanaShift entSedanaShift = new SedanaShift();
        resultToObject(rs, entSedanaShift);
        lists.add(entSedanaShift);
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

  public static boolean checkOID(long entSedanaShiftId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_SEDANASHIFT + " WHERE "
              + PstSedanaShift.fieldNames[PstSedanaShift.FLD_SHIFT_ID] + " = " + entSedanaShiftId;
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
      String sql = "SELECT COUNT(" + PstSedanaShift.fieldNames[PstSedanaShift.FLD_SHIFT_ID] + ") FROM " + TBL_SEDANASHIFT;
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
          SedanaShift entSedanaShift = (SedanaShift) list.get(ls);
          if (oid == entSedanaShift.getOID()) {
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
}
