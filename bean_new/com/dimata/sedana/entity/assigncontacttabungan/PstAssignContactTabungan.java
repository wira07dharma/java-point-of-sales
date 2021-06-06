/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.assigncontacttabungan;

/**
 *
 * @author Regen
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.sedana.entity.assigntabungan.PstAssignTabungan;
import com.dimata.sedana.entity.masterdata.PstMasterTabungan;
import com.dimata.sedana.entity.tabungan.PstDataTabungan;
import com.dimata.sedana.entity.tabungan.PstJenisSimpanan;
import com.dimata.util.Command;
import java.util.Vector;

public class PstAssignContactTabungan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_ASSIGNCONTACTTABUNGAN = "sedana_assign_contact_tabungan";
  public static final int FLD_ASSIGN_TABUNGAN_ID = 0;
  public static final int FLD_MASTER_TABUNGAN_ID = 1;
  public static final int FLD_CONTACT_ID = 2;
  public static final int FLD_NO_TABUNGAN = 3;

  public static String[] fieldNames = {
    "ASSIGN_TABUNGAN_ID",
    "MASTER_TABUNGAN_ID",
    "CONTACT_ID",
    "NO_TABUNGAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_STRING
  };

  public PstAssignContactTabungan() {
  }

  public PstAssignContactTabungan(int i) throws DBException {
    super(new PstAssignContactTabungan());
  }

  public PstAssignContactTabungan(String sOid) throws DBException {
    super(new PstAssignContactTabungan(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstAssignContactTabungan(long lOid) throws DBException {
    super(new PstAssignContactTabungan(0));
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
    return TBL_ASSIGNCONTACTTABUNGAN;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstAssignContactTabungan().getClass().getName();
  }

  public static AssignContactTabungan fetchExc(long oid) {
    AssignContactTabungan entAssignContactTabungan = new AssignContactTabungan();
    try {
      PstAssignContactTabungan pstAssignContactTabungan = new PstAssignContactTabungan(oid);
      entAssignContactTabungan.setOID(oid);
      entAssignContactTabungan.setMasterTabunganId(pstAssignContactTabungan.getlong(FLD_MASTER_TABUNGAN_ID));
      entAssignContactTabungan.setContactId(pstAssignContactTabungan.getlong(FLD_CONTACT_ID));
      entAssignContactTabungan.setNoTabungan(pstAssignContactTabungan.getString(FLD_NO_TABUNGAN));
    } catch (DBException dbe) {
      System.err.println(dbe);
    } catch (Exception e) {
      System.err.println(e);
    }
    return entAssignContactTabungan;
  }

  public static AssignContactTabungan fetchWhere(String Where) throws DBException {
    Vector<AssignContactTabungan> t = list(0, 0, Where, "");
    return(t.size()>0?t.get(0):new AssignContactTabungan());
  }

  public long fetchExc(Entity entity) throws Exception {
    AssignContactTabungan entAssignContactTabungan = fetchExc(entity.getOID());
    entity = (Entity) entAssignContactTabungan;
    return entAssignContactTabungan.getOID();
  }

  public static synchronized long updateExc(AssignContactTabungan entAssignContactTabungan) throws DBException {
    try {
      if (entAssignContactTabungan.getOID() != 0) {
        PstAssignContactTabungan pstAssignContactTabungan = new PstAssignContactTabungan(entAssignContactTabungan.getOID());
        pstAssignContactTabungan.setLong(FLD_MASTER_TABUNGAN_ID, entAssignContactTabungan.getMasterTabunganId());
        pstAssignContactTabungan.setLong(FLD_CONTACT_ID, entAssignContactTabungan.getContactId());
        pstAssignContactTabungan.setString(FLD_NO_TABUNGAN, entAssignContactTabungan.getNoTabungan());
        pstAssignContactTabungan.update();
        return entAssignContactTabungan.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignContactTabungan(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((AssignContactTabungan) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstAssignContactTabungan pstAssignContactTabungan = new PstAssignContactTabungan(oid);
      pstAssignContactTabungan.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignContactTabungan(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(AssignContactTabungan entAssignContactTabungan) throws DBException {
    try {
      PstAssignContactTabungan pstAssignContactTabungan = new PstAssignContactTabungan(0);
      pstAssignContactTabungan.setLong(FLD_MASTER_TABUNGAN_ID, entAssignContactTabungan.getMasterTabunganId());
      pstAssignContactTabungan.setLong(FLD_CONTACT_ID, entAssignContactTabungan.getContactId());
      pstAssignContactTabungan.setString(FLD_NO_TABUNGAN, entAssignContactTabungan.getNoTabungan());
      pstAssignContactTabungan.insert();
      entAssignContactTabungan.setOID(pstAssignContactTabungan.getlong(FLD_ASSIGN_TABUNGAN_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignContactTabungan(0), DBException.UNKNOWN);
    }
    return entAssignContactTabungan.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((AssignContactTabungan) entity);
  }

  public static void resultToObject(ResultSet rs, AssignContactTabungan entAssignContactTabungan) {
    try {
      entAssignContactTabungan.setOID(rs.getLong(PstAssignContactTabungan.fieldNames[PstAssignContactTabungan.FLD_ASSIGN_TABUNGAN_ID]));
      entAssignContactTabungan.setMasterTabunganId(rs.getLong(PstAssignContactTabungan.fieldNames[PstAssignContactTabungan.FLD_MASTER_TABUNGAN_ID]));
      entAssignContactTabungan.setContactId(rs.getLong(PstAssignContactTabungan.fieldNames[PstAssignContactTabungan.FLD_CONTACT_ID]));
      entAssignContactTabungan.setNoTabungan(rs.getString(PstAssignContactTabungan.fieldNames[PstAssignContactTabungan.FLD_NO_TABUNGAN]));
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
      String sql = "SELECT * FROM " + TBL_ASSIGNCONTACTTABUNGAN;
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
        AssignContactTabungan entAssignContactTabungan = new AssignContactTabungan();
        resultToObject(rs, entAssignContactTabungan);
        lists.add(entAssignContactTabungan);
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

  public static Vector listJoinGetNoTabungan(int limitStart, int recordToGet, String whereClause, String order) {
    Vector lists = new Vector();
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT * FROM " + TBL_ASSIGNCONTACTTABUNGAN + " AS asscontab"
              + " INNER JOIN " + PstMasterTabungan.TBL_MASTERTABUNGAN + " mastab"
              + " ON mastab." + PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_MASTER_TABUNGAN_ID] + " = asscontab." + fieldNames[FLD_MASTER_TABUNGAN_ID]
              + " INNER JOIN " + PstAssignTabungan.TBL_ASSIGNTABUNGAN + " asstab "
              + " ON asstab." + PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_MASTER_TABUNGAN] + " = mastab." + PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_MASTER_TABUNGAN_ID]
              + " INNER JOIN " + PstJenisSimpanan.TBL_JENISSIMPANAN + " AS jensim"
              + " ON jensim." + PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_ID_JENIS_SIMPANAN] + " = asstab." + PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_ID_JENIS_SIMPANAN]
              + " ";
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
        AssignContactTabungan entAssignContactTabungan = new AssignContactTabungan();
        resultToObject(rs, entAssignContactTabungan);
//        entAssignContactTabungan.setSimpananId(rs.getLong(PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_SIMPANAN]));
        entAssignContactTabungan.setJenisSimpananId(rs.getLong(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_ID_JENIS_SIMPANAN]));
        lists.add(entAssignContactTabungan);
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

  public static boolean checkOID(long entAssignContactTabunganId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_ASSIGNCONTACTTABUNGAN + " WHERE "
              + PstAssignContactTabungan.fieldNames[PstAssignContactTabungan.FLD_ASSIGN_TABUNGAN_ID] + " = " + entAssignContactTabunganId;
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
      String sql = "SELECT COUNT(" + PstAssignContactTabungan.fieldNames[PstAssignContactTabungan.FLD_ASSIGN_TABUNGAN_ID] + ") FROM " + TBL_ASSIGNCONTACTTABUNGAN;
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
          AssignContactTabungan entAssignContactTabungan = (AssignContactTabungan) list.get(ls);
          if (oid == entAssignContactTabungan.getOID()) {
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
