/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

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

public class PstKolektibilitasPembayaran extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_KOLEKTIBILITASPEMBAYARAN = "sedana_kolektibilitas_pembayaran";
  public static final int FLD_KOLEKTIBILITAS_ID = 0;
  public static final int FLD_KODE_KOLEKTIBILITAS = 1;
  public static final int FLD_TINGKAT_KOLEKTIBILITAS = 2;
  public static final int FLD_JUDUL_KOLEKTIBILITAS = 3;
  public static final int FLD_MAX_HARI_TUNGGAKAN_POKOK = 4;
  public static final int FLD_MAX_HARI_JUMLAH_TUNGGAKAN_BUNGA = 5;
  public static final int FLD_TINGKAT_RESIKO = 6;

  public static String[] fieldNames = {
    "KOLEKTIBILITAS_ID",
    "KODE_KOLEKTIBILITAS",
    "TINGKAT_KOLEKTIBILITAS",
    "JUDUL_KOLEKTIBILITAS",
    "MAX_HARI_TUNGGAKAN_POKOK",
    "MAX_HARI_JUMLAH_TUNGGAKAN_BUNGA",
    "TINGKAT_RESIKO"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_STRING,
    TYPE_INT,
    TYPE_STRING,
    TYPE_INT,
    TYPE_INT,
    TYPE_FLOAT
  };

  public PstKolektibilitasPembayaran() {
  }

  public PstKolektibilitasPembayaran(int i) throws DBException {
    super(new PstKolektibilitasPembayaran());
  }

  public PstKolektibilitasPembayaran(String sOid) throws DBException {
    super(new PstKolektibilitasPembayaran(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstKolektibilitasPembayaran(long lOid) throws DBException {
    super(new PstKolektibilitasPembayaran(0));
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
    return TBL_KOLEKTIBILITASPEMBAYARAN;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstKolektibilitasPembayaran().getClass().getName();
  }

  public static KolektibilitasPembayaran fetchExc(long oid) throws DBException {
    try {
      KolektibilitasPembayaran entKolektibilitasPembayaran = new KolektibilitasPembayaran();
      PstKolektibilitasPembayaran pstKolektibilitasPembayaran = new PstKolektibilitasPembayaran(oid);
      entKolektibilitasPembayaran.setOID(oid);
      entKolektibilitasPembayaran.setKodeKolektibilitas(pstKolektibilitasPembayaran.getString(FLD_KODE_KOLEKTIBILITAS));
      entKolektibilitasPembayaran.setTingkatKolektibilitas(pstKolektibilitasPembayaran.getInt(FLD_TINGKAT_KOLEKTIBILITAS));
      entKolektibilitasPembayaran.setJudulKolektibilitas(pstKolektibilitasPembayaran.getString(FLD_JUDUL_KOLEKTIBILITAS));
      entKolektibilitasPembayaran.setTingkatResiko(pstKolektibilitasPembayaran.getfloat(FLD_TINGKAT_RESIKO));
      return entKolektibilitasPembayaran;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKolektibilitasPembayaran(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    KolektibilitasPembayaran entKolektibilitasPembayaran = fetchExc(entity.getOID());
    entity = (Entity) entKolektibilitasPembayaran;
    return entKolektibilitasPembayaran.getOID();
  }

  public static synchronized long updateExc(KolektibilitasPembayaran entKolektibilitasPembayaran) throws DBException {
    try {
      if (entKolektibilitasPembayaran.getOID() != 0) {
        PstKolektibilitasPembayaran pstKolektibilitasPembayaran = new PstKolektibilitasPembayaran(entKolektibilitasPembayaran.getOID());
        pstKolektibilitasPembayaran.setString(FLD_KODE_KOLEKTIBILITAS, entKolektibilitasPembayaran.getKodeKolektibilitas());
        pstKolektibilitasPembayaran.setInt(FLD_TINGKAT_KOLEKTIBILITAS, entKolektibilitasPembayaran.getTingkatKolektibilitas());
        pstKolektibilitasPembayaran.setString(FLD_JUDUL_KOLEKTIBILITAS, entKolektibilitasPembayaran.getJudulKolektibilitas());
        pstKolektibilitasPembayaran.setDouble(FLD_TINGKAT_RESIKO, entKolektibilitasPembayaran.getTingkatResiko());
        pstKolektibilitasPembayaran.update();
        return entKolektibilitasPembayaran.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKolektibilitasPembayaran(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((KolektibilitasPembayaran) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstKolektibilitasPembayaran pstKolektibilitasPembayaran = new PstKolektibilitasPembayaran(oid);
      pstKolektibilitasPembayaran.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKolektibilitasPembayaran(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(KolektibilitasPembayaran entKolektibilitasPembayaran) throws DBException {
    try {
      PstKolektibilitasPembayaran pstKolektibilitasPembayaran = new PstKolektibilitasPembayaran(0);
      pstKolektibilitasPembayaran.setString(FLD_KODE_KOLEKTIBILITAS, entKolektibilitasPembayaran.getKodeKolektibilitas());
      pstKolektibilitasPembayaran.setInt(FLD_TINGKAT_KOLEKTIBILITAS, entKolektibilitasPembayaran.getTingkatKolektibilitas());
      pstKolektibilitasPembayaran.setString(FLD_JUDUL_KOLEKTIBILITAS, entKolektibilitasPembayaran.getJudulKolektibilitas());
      pstKolektibilitasPembayaran.setDouble(FLD_TINGKAT_RESIKO, entKolektibilitasPembayaran.getTingkatResiko());
      pstKolektibilitasPembayaran.insert();
      entKolektibilitasPembayaran.setOID(pstKolektibilitasPembayaran.getLong(FLD_KOLEKTIBILITAS_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKolektibilitasPembayaran(0), DBException.UNKNOWN);
    }
    return entKolektibilitasPembayaran.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((KolektibilitasPembayaran) entity);
  }

  public static void resultToObject(ResultSet rs, KolektibilitasPembayaran entKolektibilitasPembayaran) {
    try {
      entKolektibilitasPembayaran.setOID(rs.getLong(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_KOLEKTIBILITAS_ID]));
      entKolektibilitasPembayaran.setKodeKolektibilitas(rs.getString(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_KODE_KOLEKTIBILITAS]));
      entKolektibilitasPembayaran.setTingkatKolektibilitas(rs.getInt(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_TINGKAT_KOLEKTIBILITAS]));
      entKolektibilitasPembayaran.setJudulKolektibilitas(rs.getString(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_JUDUL_KOLEKTIBILITAS]));
      entKolektibilitasPembayaran.setTingkatResiko(rs.getFloat(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_TINGKAT_RESIKO]));
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
      String sql = "SELECT * FROM " + TBL_KOLEKTIBILITASPEMBAYARAN;
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
        KolektibilitasPembayaran entKolektibilitasPembayaran = new KolektibilitasPembayaran();
        resultToObject(rs, entKolektibilitasPembayaran);
        lists.add(entKolektibilitasPembayaran);
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

  public static boolean checkOID(long entKolektibilitasPembayaranId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_KOLEKTIBILITASPEMBAYARAN + " WHERE "
              + PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_KOLEKTIBILITAS_ID] + " = " + entKolektibilitasPembayaranId;
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
      String sql = "SELECT COUNT(" + PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_KOLEKTIBILITAS_ID] + ") FROM " + TBL_KOLEKTIBILITASPEMBAYARAN;
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
          KolektibilitasPembayaran entKolektibilitasPembayaran = (KolektibilitasPembayaran) list.get(ls);
          if (oid == entKolektibilitasPembayaran.getOID()) {
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
