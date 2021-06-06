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

public class PstKategoriKredit extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_KATEGORIKREDIT = "sedana_kategori_kredit";
  public static final int FLD_KATEGORI_KREDIT_ID = 0;
  public static final int FLD_KATEGORY_KREDIT = 1;
  public static final int FLD_KETERANGAN = 2;

  public static String[] fieldNames = {
    "KATEGORI_KREDIT_ID",
    "KATEGORY_KREDIT",
    "KETERANGAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_STRING,
    TYPE_STRING
  };

  public PstKategoriKredit() {
  }

  public PstKategoriKredit(int i) throws DBException {
    super(new PstKategoriKredit());
  }

  public PstKategoriKredit(String sOid) throws DBException {
    super(new PstKategoriKredit(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstKategoriKredit(long lOid) throws DBException {
    super(new PstKategoriKredit(0));
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
    return TBL_KATEGORIKREDIT;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstKategoriKredit().getClass().getName();
  }

  public static KategoriKredit fetchExc(long oid) throws DBException {
    try {
      KategoriKredit entKategoriKredit = new KategoriKredit();
      PstKategoriKredit pstKategoriKredit = new PstKategoriKredit(oid);
      entKategoriKredit.setOID(oid);
      entKategoriKredit.setKategoryKredit(pstKategoriKredit.getString(FLD_KATEGORY_KREDIT));
      entKategoriKredit.setKeterangan(pstKategoriKredit.getString(FLD_KETERANGAN));
      return entKategoriKredit;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKategoriKredit(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    KategoriKredit entKategoriKredit = fetchExc(entity.getOID());
    entity = (Entity) entKategoriKredit;
    return entKategoriKredit.getOID();
  }

  public static synchronized long updateExc(KategoriKredit entKategoriKredit) throws DBException {
    try {
      if (entKategoriKredit.getOID() != 0) {
        PstKategoriKredit pstKategoriKredit = new PstKategoriKredit(entKategoriKredit.getOID());
        pstKategoriKredit.setString(FLD_KATEGORY_KREDIT, entKategoriKredit.getKategoryKredit());
        pstKategoriKredit.setString(FLD_KETERANGAN, entKategoriKredit.getKeterangan());
        pstKategoriKredit.update();
        return entKategoriKredit.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKategoriKredit(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((KategoriKredit) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstKategoriKredit pstKategoriKredit = new PstKategoriKredit(oid);
      pstKategoriKredit.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKategoriKredit(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(KategoriKredit entKategoriKredit) throws DBException {
    try {
      PstKategoriKredit pstKategoriKredit = new PstKategoriKredit(0);
      pstKategoriKredit.setString(FLD_KATEGORY_KREDIT, entKategoriKredit.getKategoryKredit());
      pstKategoriKredit.setString(FLD_KETERANGAN, entKategoriKredit.getKeterangan());
      pstKategoriKredit.insert();
      entKategoriKredit.setOID(pstKategoriKredit.getLong(FLD_KATEGORI_KREDIT_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKategoriKredit(0), DBException.UNKNOWN);
    }
    return entKategoriKredit.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((KategoriKredit) entity);
  }

  public static void resultToObject(ResultSet rs, KategoriKredit entKategoriKredit) {
    try {
      entKategoriKredit.setOID(rs.getLong(PstKategoriKredit.fieldNames[PstKategoriKredit.FLD_KATEGORI_KREDIT_ID]));
      entKategoriKredit.setKategoryKredit(rs.getString(PstKategoriKredit.fieldNames[PstKategoriKredit.FLD_KATEGORY_KREDIT]));
      entKategoriKredit.setKeterangan(rs.getString(PstKategoriKredit.fieldNames[PstKategoriKredit.FLD_KETERANGAN]));
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
      String sql = "SELECT * FROM " + TBL_KATEGORIKREDIT;
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
        KategoriKredit entKategoriKredit = new KategoriKredit();
        resultToObject(rs, entKategoriKredit);
        lists.add(entKategoriKredit);
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

  public static boolean checkOID(long entKategoriKreditId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_KATEGORIKREDIT + " WHERE "
              + PstKategoriKredit.fieldNames[PstKategoriKredit.FLD_KATEGORI_KREDIT_ID] + " = " + entKategoriKreditId;
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
      String sql = "SELECT COUNT(" + PstKategoriKredit.fieldNames[PstKategoriKredit.FLD_KATEGORI_KREDIT_ID] + ") FROM " + TBL_KATEGORIKREDIT;
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
          KategoriKredit entKategoriKredit = (KategoriKredit) list.get(ls);
          if (oid == entKategoriKredit.getOID()) {
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
