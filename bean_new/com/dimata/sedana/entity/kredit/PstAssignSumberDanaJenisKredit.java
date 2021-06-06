/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

/**
 *
 * @author Regen
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstAssignSumberDanaJenisKredit extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_ASSIGNSUMBERDANAJENISKREDIT = "aiso_assign_sumber_dana_jenis_kredit";
  public static final int FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID = 0;
  public static final int FLD_SUMBER_DANA_ID = 1;
  public static final int FLD_TYPE_KREDIT_ID = 2;
  public static final int FLD_MAX_PERSENTASE_PENGGUNAAN = 3;

  public static String[] fieldNames = {
    "ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID",
    "SUMBER_DANA_ID",
    "TYPE_KREDIT_ID",
    "MAX_PERSENTASE_PENGGUNAAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_FLOAT
  };

  public PstAssignSumberDanaJenisKredit() {
  }

  public PstAssignSumberDanaJenisKredit(int i) throws DBException {
    super(new PstAssignSumberDanaJenisKredit());
  }

  public PstAssignSumberDanaJenisKredit(String sOid) throws DBException {
    super(new PstAssignSumberDanaJenisKredit(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstAssignSumberDanaJenisKredit(long lOid) throws DBException {
    super(new PstAssignSumberDanaJenisKredit(0));
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
    return TBL_ASSIGNSUMBERDANAJENISKREDIT;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstAssignSumberDanaJenisKredit().getClass().getName();
  }

  public static AssignSumberDanaJenisKredit fetchExc(long oid) throws DBException {
    try {
      AssignSumberDanaJenisKredit entAssignSumberDanaJenisKredit = new AssignSumberDanaJenisKredit();
      PstAssignSumberDanaJenisKredit pstAssignSumberDanaJenisKredit = new PstAssignSumberDanaJenisKredit(oid);
      entAssignSumberDanaJenisKredit.setOID(oid);
      entAssignSumberDanaJenisKredit.setSumberDanaId(pstAssignSumberDanaJenisKredit.getLong(FLD_SUMBER_DANA_ID));
      entAssignSumberDanaJenisKredit.setTypeKreditId(pstAssignSumberDanaJenisKredit.getLong(FLD_TYPE_KREDIT_ID));
      entAssignSumberDanaJenisKredit.setMaxPersentasePenggunaan(pstAssignSumberDanaJenisKredit.getfloat(FLD_MAX_PERSENTASE_PENGGUNAAN));
      return entAssignSumberDanaJenisKredit;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignSumberDanaJenisKredit(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    AssignSumberDanaJenisKredit entAssignSumberDanaJenisKredit = fetchExc(entity.getOID());
    entity = (Entity) entAssignSumberDanaJenisKredit;
    return entAssignSumberDanaJenisKredit.getOID();
  }

  public static synchronized long updateExc(AssignSumberDanaJenisKredit entAssignSumberDanaJenisKredit) throws DBException {
    try {
      if (entAssignSumberDanaJenisKredit.getOID() != 0) {
        PstAssignSumberDanaJenisKredit pstAssignSumberDanaJenisKredit = new PstAssignSumberDanaJenisKredit(entAssignSumberDanaJenisKredit.getOID());
        pstAssignSumberDanaJenisKredit.setLong(FLD_SUMBER_DANA_ID, entAssignSumberDanaJenisKredit.getSumberDanaId());
        pstAssignSumberDanaJenisKredit.setLong(FLD_TYPE_KREDIT_ID, entAssignSumberDanaJenisKredit.getTypeKreditId());
        pstAssignSumberDanaJenisKredit.setDouble(FLD_MAX_PERSENTASE_PENGGUNAAN, entAssignSumberDanaJenisKredit.getMaxPersentasePenggunaan());
        pstAssignSumberDanaJenisKredit.update();
        return entAssignSumberDanaJenisKredit.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignSumberDanaJenisKredit(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((AssignSumberDanaJenisKredit) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstAssignSumberDanaJenisKredit pstAssignSumberDanaJenisKredit = new PstAssignSumberDanaJenisKredit(oid);
      pstAssignSumberDanaJenisKredit.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignSumberDanaJenisKredit(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(AssignSumberDanaJenisKredit entAssignSumberDanaJenisKredit) throws DBException {
    try {
      PstAssignSumberDanaJenisKredit pstAssignSumberDanaJenisKredit = new PstAssignSumberDanaJenisKredit(0);
      pstAssignSumberDanaJenisKredit.setLong(FLD_SUMBER_DANA_ID, entAssignSumberDanaJenisKredit.getSumberDanaId());
      pstAssignSumberDanaJenisKredit.setLong(FLD_TYPE_KREDIT_ID, entAssignSumberDanaJenisKredit.getTypeKreditId());
      pstAssignSumberDanaJenisKredit.setDouble(FLD_MAX_PERSENTASE_PENGGUNAAN, entAssignSumberDanaJenisKredit.getMaxPersentasePenggunaan());
      pstAssignSumberDanaJenisKredit.insert();
      entAssignSumberDanaJenisKredit.setOID(pstAssignSumberDanaJenisKredit.getLong(FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignSumberDanaJenisKredit(0), DBException.UNKNOWN);
    }
    return entAssignSumberDanaJenisKredit.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((AssignSumberDanaJenisKredit) entity);
  }

  public static void resultToObject(ResultSet rs, AssignSumberDanaJenisKredit entAssignSumberDanaJenisKredit) {
    try {
      entAssignSumberDanaJenisKredit.setOID(rs.getLong(PstAssignSumberDanaJenisKredit.fieldNames[PstAssignSumberDanaJenisKredit.FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID]));
      entAssignSumberDanaJenisKredit.setSumberDanaId(rs.getLong(PstAssignSumberDanaJenisKredit.fieldNames[PstAssignSumberDanaJenisKredit.FLD_SUMBER_DANA_ID]));
      entAssignSumberDanaJenisKredit.setTypeKreditId(rs.getLong(PstAssignSumberDanaJenisKredit.fieldNames[PstAssignSumberDanaJenisKredit.FLD_TYPE_KREDIT_ID]));
      entAssignSumberDanaJenisKredit.setMaxPersentasePenggunaan(rs.getFloat(PstAssignSumberDanaJenisKredit.fieldNames[PstAssignSumberDanaJenisKredit.FLD_MAX_PERSENTASE_PENGGUNAAN]));
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
      String sql = "SELECT * FROM " + TBL_ASSIGNSUMBERDANAJENISKREDIT;
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
        AssignSumberDanaJenisKredit entAssignSumberDanaJenisKredit = new AssignSumberDanaJenisKredit();
        resultToObject(rs, entAssignSumberDanaJenisKredit);
        lists.add(entAssignSumberDanaJenisKredit);
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

  public static boolean checkOID(long entAssignSumberDanaJenisKreditId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_ASSIGNSUMBERDANAJENISKREDIT + " WHERE "
              + PstAssignSumberDanaJenisKredit.fieldNames[PstAssignSumberDanaJenisKredit.FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID] + " = " + entAssignSumberDanaJenisKreditId;
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
      String sql = "SELECT COUNT(" + PstAssignSumberDanaJenisKredit.fieldNames[PstAssignSumberDanaJenisKredit.FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID] + ") FROM " + TBL_ASSIGNSUMBERDANAJENISKREDIT;
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
          AssignSumberDanaJenisKredit entAssignSumberDanaJenisKredit = (AssignSumberDanaJenisKredit) list.get(ls);
          if (oid == entAssignSumberDanaJenisKredit.getOID()) {
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
