/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.sedana.entity.tabungan.PstJenisTransaksi;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class PstBiayaTransaksi extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_BIAYA_TRANSAKSI = "sedana_pinjaman_detail";
  public static final int FLD_ID_BIAYA_TRANSAKSI = 0;
  public static final int FLD_ID_JENIS_TRANSAKSI = 1;
  public static final int FLD_ID_PINJAMAN = 2;
  public static final int FLD_VALUE_BIAYA = 3;
  public static final int FLD_TIPE_BIAYA = 4;
  public static final int FLD_ID_VALUE = 5;

  public static String[] fieldNames = {
    "ID_PINJAMAN_DETAIL",
    "ID_JENIS_TRANSAKSI",
    "ID_PINJAMAN",
    "VALUE_BIAYA",
    "TIPE_BIAYA",
    "ID_VALUE"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_FLOAT,
    TYPE_INT,
    TYPE_LONG,
  };

  public PstBiayaTransaksi() {
  }

  public PstBiayaTransaksi(int i) throws DBException {
    super(new PstBiayaTransaksi());
  }

  public PstBiayaTransaksi(String sOid) throws DBException {
    super(new PstBiayaTransaksi(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstBiayaTransaksi(long lOid) throws DBException {
    super(new PstBiayaTransaksi(0));
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
    return TBL_BIAYA_TRANSAKSI;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstBiayaTransaksi().getClass().getName();
  }

  public static BiayaTransaksi fetchExc(long oid) throws DBException {
    try {
      BiayaTransaksi entBiayaTransaksi = new BiayaTransaksi();
      PstBiayaTransaksi pstBiayaTransaksi = new PstBiayaTransaksi(oid);
      entBiayaTransaksi.setOID(oid);
      entBiayaTransaksi.setIdJenisTransaksi(pstBiayaTransaksi.getlong(FLD_ID_JENIS_TRANSAKSI));
      entBiayaTransaksi.setIdPinjaman(pstBiayaTransaksi.getlong(FLD_ID_PINJAMAN));
      entBiayaTransaksi.setValueBiaya(pstBiayaTransaksi.getdouble(FLD_VALUE_BIAYA));
      entBiayaTransaksi.setTipeBiaya(pstBiayaTransaksi.getInt(FLD_TIPE_BIAYA));
      return entBiayaTransaksi;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstBiayaTransaksi(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    BiayaTransaksi entBiayaTransaksi = fetchExc(entity.getOID());
    entity = (Entity) entBiayaTransaksi;
    return entBiayaTransaksi.getOID();
  }

  public static synchronized long updateExc(BiayaTransaksi entBiayaTransaksi) throws DBException {
    try {
      if (entBiayaTransaksi.getOID() != 0) {
        PstBiayaTransaksi pstBiayaTransaksi = new PstBiayaTransaksi(entBiayaTransaksi.getOID());
        pstBiayaTransaksi.setLong(FLD_ID_JENIS_TRANSAKSI, entBiayaTransaksi.getIdJenisTransaksi());
        pstBiayaTransaksi.setLong(FLD_ID_PINJAMAN, entBiayaTransaksi.getIdPinjaman());
        pstBiayaTransaksi.setDouble(FLD_VALUE_BIAYA, entBiayaTransaksi.getValueBiaya());
        pstBiayaTransaksi.setInt(FLD_TIPE_BIAYA, entBiayaTransaksi.getTipeBiaya());
        pstBiayaTransaksi.update();
        return entBiayaTransaksi.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstBiayaTransaksi(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((BiayaTransaksi) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstBiayaTransaksi pstBiayaTransaksi = new PstBiayaTransaksi(oid);
      pstBiayaTransaksi.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstBiayaTransaksi(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(BiayaTransaksi entBiayaTransaksi) throws DBException {
    try {
      PstBiayaTransaksi pstBiayaTransaksi = new PstBiayaTransaksi(0);
      if (entBiayaTransaksi.getIdJenisTransaksi() > 0) {
        pstBiayaTransaksi.setLong(FLD_ID_JENIS_TRANSAKSI, entBiayaTransaksi.getIdJenisTransaksi());
      }
      pstBiayaTransaksi.setLong(FLD_ID_PINJAMAN, entBiayaTransaksi.getIdPinjaman());
      pstBiayaTransaksi.setDouble(FLD_VALUE_BIAYA, entBiayaTransaksi.getValueBiaya());
      pstBiayaTransaksi.setLong(FLD_ID_VALUE, entBiayaTransaksi.getIdValue());
      pstBiayaTransaksi.setInt(FLD_TIPE_BIAYA, entBiayaTransaksi.getTipeBiaya());
      pstBiayaTransaksi.insert();
      entBiayaTransaksi.setOID(pstBiayaTransaksi.getlong(FLD_ID_BIAYA_TRANSAKSI));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstBiayaTransaksi(0), DBException.UNKNOWN);
    }
    return entBiayaTransaksi.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((BiayaTransaksi) entity);
  }

  public static void resultToObject(ResultSet rs, BiayaTransaksi entBiayaTransaksi) {
    try {
      entBiayaTransaksi.setOID(rs.getLong(PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_ID_BIAYA_TRANSAKSI]));
      entBiayaTransaksi.setIdJenisTransaksi(rs.getLong(PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_ID_JENIS_TRANSAKSI]));
      entBiayaTransaksi.setIdPinjaman(rs.getLong(PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_ID_PINJAMAN]));
      entBiayaTransaksi.setValueBiaya(rs.getDouble(PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_VALUE_BIAYA]));
      entBiayaTransaksi.setTipeBiaya(rs.getInt(PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_TIPE_BIAYA]));
      entBiayaTransaksi.setIdValue(rs.getLong(PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_ID_VALUE]));
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
      String sql = "SELECT * FROM " + TBL_BIAYA_TRANSAKSI;
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
        BiayaTransaksi entBiayaTransaksi = new BiayaTransaksi();
        resultToObject(rs, entBiayaTransaksi);
        lists.add(entBiayaTransaksi);
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

  public static boolean checkOID(long entBiayaTransaksiId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_BIAYA_TRANSAKSI + " WHERE "
              + PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_ID_BIAYA_TRANSAKSI] + " = " + entBiayaTransaksiId;
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
      String sql = "SELECT COUNT(" + PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_ID_BIAYA_TRANSAKSI] + ") FROM " + TBL_BIAYA_TRANSAKSI;
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
          BiayaTransaksi entBiayaTransaksi = (BiayaTransaksi) list.get(ls);
          if (oid == entBiayaTransaksi.getOID()) {
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

  public static Vector listJoinJenisTransaksi(int limitStart, int recordToGet, String whereClause, String order) {
    Vector lists = new Vector();
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT * FROM " + TBL_BIAYA_TRANSAKSI + " AS bt "
              + " INNER JOIN " + PstJenisTransaksi.TBL_JENISTRANSAKSI + " AS jt "
              + " ON bt." + fieldNames[FLD_ID_JENIS_TRANSAKSI] + " = jt." + PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_JENIS_TRANSAKSI_ID]
              + "";
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
        BiayaTransaksi entBiayaTransaksi = new BiayaTransaksi();
        resultToObject(rs, entBiayaTransaksi);
        entBiayaTransaksi.setTipeDoc(rs.getInt(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_TIPE_DOC]));
        entBiayaTransaksi.setInputOption(rs.getInt(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_INPUT_OPTION]));
        lists.add(entBiayaTransaksi);
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

}
