/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.masterdata;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class PstCompetency extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_COMPETENCY = "hr_competency";
  public static final int FLD_COMPETENCY_GROUP_ID = 0;
  public static final int FLD_COMPETENCY_TYPE_ID = 1;
  public static final int FLD_COMPETENCY_ID = 2;
  public static final int FLD_COMPETENCY_NAME = 3;
  public static final int FLD_DESCRIPTION = 4;
  public static String[] fieldNames = {
    "COMPETENCY_GROUP_ID",
    "COMPETENCY_TYPE_ID",
    "COMPETENCY_ID",
    "COMPETENCY_NAME",
    "DESCRIPTION"
  };
  public static int[] fieldTypes = {
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_STRING,
    TYPE_STRING
  };

  public PstCompetency() {
  }

  public PstCompetency(int i) throws DBException {
    super(new PstCompetency());
  }

  public PstCompetency(String sOid) throws DBException {
    super(new PstCompetency(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstCompetency(long lOid) throws DBException {
    super(new PstCompetency(0));
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
    return TBL_COMPETENCY;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstCompetency().getClass().getName();
  }

  public static Competency fetchExc(long oid) throws DBException {
    try {
      Competency entCompetency = new Competency();
      PstCompetency pstCompetency = new PstCompetency(oid);
      entCompetency.setOID(oid);
      entCompetency.setCompetencyGroupId(pstCompetency.getlong(FLD_COMPETENCY_GROUP_ID));
      entCompetency.setCompetencyTypeId(pstCompetency.getlong(FLD_COMPETENCY_TYPE_ID));
      entCompetency.setCompetencyName(pstCompetency.getString(FLD_COMPETENCY_NAME));
      entCompetency.setDescription(pstCompetency.getString(FLD_DESCRIPTION));
      return entCompetency;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstCompetency(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    Competency entCompetency = fetchExc(entity.getOID());
    entity = (Entity) entCompetency;
    return entCompetency.getOID();
  }

  public long insertExc(Entity ent) throws Exception {
    return insertExc((Competency) ent);
  }

  public long updateExc(Entity ent) throws Exception {
    return updateExc((Competency) ent);
  }

  public long deleteExc(Entity ent) throws Exception {
    if (ent == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(ent.getOID());
  }

  public static long insertExc(Competency entCompetency) throws DBException {
    try {
      PstCompetency pstCompetency = new PstCompetency(0);

      pstCompetency.setLong(FLD_COMPETENCY_GROUP_ID, entCompetency.getCompetencyGroupId());
      pstCompetency.setLong(FLD_COMPETENCY_TYPE_ID, entCompetency.getCompetencyTypeId());
      pstCompetency.setString(FLD_COMPETENCY_NAME, entCompetency.getCompetencyName());
      pstCompetency.setString(FLD_DESCRIPTION, entCompetency.getDescription());

      pstCompetency.insert();
      entCompetency.setOID(pstCompetency.getLong(FLD_COMPETENCY_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstCompetency(0), DBException.UNKNOWN);
    }
    return entCompetency.getOID();
  }

  public static long updateExc(Competency entCompetency) throws DBException {
    try {
      if (entCompetency.getOID() != 0) {
        PstCompetency pstCompetency = new PstCompetency(entCompetency.getOID());

        pstCompetency.setLong(FLD_COMPETENCY_GROUP_ID, entCompetency.getCompetencyGroupId());
        pstCompetency.setLong(FLD_COMPETENCY_TYPE_ID, entCompetency.getCompetencyTypeId());
        pstCompetency.setString(FLD_COMPETENCY_NAME, entCompetency.getCompetencyName());
        pstCompetency.setString(FLD_DESCRIPTION, entCompetency.getDescription());

        pstCompetency.update();
        return entCompetency.getOID();

      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstCompetency(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public static long deleteExc(long oid) throws DBException {
    try {
      PstCompetency pstCompetency = new PstCompetency(oid);
      pstCompetency.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstCompetency(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public static Vector listAll() {
    return list(0, 500, "", "");
  }

  public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
    return list(limitStart, recordToGet, whereClause, null, order);
  }

  public static Vector list(int limitStart, int recordToGet, String whereClause, String join, String order) {
    Vector lists = new Vector();
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT * FROM " + TBL_COMPETENCY;
      if (join != null && join.length() > 0) {
        sql += join;
      }
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
        Competency entCompetency = new Competency();
        resultToObject(rs, entCompetency);
        lists.add(entCompetency);
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

  private static void resultToObject(ResultSet rs, Competency entCompetency) {
    try {

      entCompetency.setOID(rs.getLong(PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_ID]));
      entCompetency.setCompetencyGroupId(rs.getLong(PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_GROUP_ID]));
      entCompetency.setCompetencyTypeId(rs.getLong(PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_TYPE_ID]));
      entCompetency.setCompetencyName(rs.getString(PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_NAME]));
      entCompetency.setDescription(rs.getString(PstCompetency.fieldNames[PstCompetency.FLD_DESCRIPTION]));

    } catch (Exception e) {
    }
  }

  public static boolean checkOID(long entCompetencyId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_COMPETENCY + " WHERE "
              + PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_ID] + " = " + entCompetencyId;

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

  public static String getCompetencyName(long oid) {
    String name = "";
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT COMPETENCY_NAME FROM " + TBL_COMPETENCY + " WHERE COMPETENCY_ID=" + oid;

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();

      while (rs.next()) {
        name = rs.getString(PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_NAME]);
      }

    } catch (Exception e) {
      System.out.println("err : " + e.toString());
    }
    return name;
  }

  public static int getCount(String whereClause) {
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT COUNT(" + PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_GROUP_ID] + ") FROM " + TBL_COMPETENCY;
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


  /* This method used to find current data */
  public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
    int size = getCount(whereClause);
    int start = 0;
    boolean found = false;
    for (int i = 0; (i < size) && !found; i = i + recordToGet) {
      Vector list = list(i, recordToGet, whereClause, orderClause);
      start = i;
      if (list.size() > 0) {
        for (int ls = 0; ls < list.size(); ls++) {
          Competency entCompetency = (Competency) list.get(ls);
          if (oid == entCompetency.getOID()) {
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

  /* This method used to find command where current data */

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
