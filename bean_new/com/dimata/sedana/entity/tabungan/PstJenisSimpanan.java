/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.tabungan;

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

public class PstJenisSimpanan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_JENISSIMPANAN = "aiso_jenis_simpanan";
  public static final int FLD_ID_JENIS_SIMPANAN = 0;
  public static final int FLD_NAMA_SIMPANAN = 1;
  public static final int FLD_SETORAN_MIN = 2;
  public static final int FLD_BUNGA = 3;
  public static final int FLD_PROVISI = 4;
  public static final int FLD_BIAYA_ADMIN = 5;
  public static final int FLD_DESC_JENIS_SIMPANAN = 6;
  public static final int FLD_FREKUENSI_SIMPANAN = 7;
  public static final int FLD_FREKUENSI_PENARIKAN = 8;

  public static String[] fieldNames = {
    "ID_JENIS_SIMPANAN",
    "NAMA_SIMPANAN",
    "SETORAN_MIN",
    "BUNGA",
    "PROVISI",
    "BIAYA_ADMIN",
    "DESC_JENIS_SIMPANAN",
    "FREKUENSI_SIMPANAN",
    "FREKUENSI_PENARIKAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_STRING,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_INT,
    TYPE_INT
  };

  public PstJenisSimpanan() {
  }

  public PstJenisSimpanan(int i) throws DBException {
    super(new PstJenisSimpanan());
  }

  public PstJenisSimpanan(String sOid) throws DBException {
    super(new PstJenisSimpanan(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstJenisSimpanan(long lOid) throws DBException {
    super(new PstJenisSimpanan(0));
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
    return TBL_JENISSIMPANAN;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstJenisSimpanan().getClass().getName();
  }

  public static JenisSimpanan fetchExc(long oid) {
    JenisSimpanan entJenisSimpanan = new JenisSimpanan();
    try {
      PstJenisSimpanan pstJenisSimpanan = new PstJenisSimpanan(oid);
      entJenisSimpanan.setOID(oid);
      entJenisSimpanan.setNamaSimpanan(pstJenisSimpanan.getString(FLD_NAMA_SIMPANAN));
      entJenisSimpanan.setSetoranMin(pstJenisSimpanan.getlong(FLD_SETORAN_MIN));
      entJenisSimpanan.setBunga(pstJenisSimpanan.getlong(FLD_BUNGA));
      entJenisSimpanan.setProvisi(pstJenisSimpanan.getlong(FLD_PROVISI));
      entJenisSimpanan.setBiayaAdmin(pstJenisSimpanan.getlong(FLD_BIAYA_ADMIN));
      entJenisSimpanan.setDescJenisSimpanan(pstJenisSimpanan.getString(FLD_DESC_JENIS_SIMPANAN));
      entJenisSimpanan.setFrekuensiSimpanan(pstJenisSimpanan.getInt(FLD_FREKUENSI_SIMPANAN));
      entJenisSimpanan.setFrekuensiPenarikan(pstJenisSimpanan.getInt(FLD_FREKUENSI_PENARIKAN));
    } catch (DBException dbe) {
      System.err.println(dbe);
    } catch (Exception e) {
      System.err.println(e);
    }
    return entJenisSimpanan;
  }

  public long fetchExc(Entity entity) throws Exception {
    JenisSimpanan entJenisSimpanan = fetchExc(entity.getOID());
    entity = (Entity) entJenisSimpanan;
    return entJenisSimpanan.getOID();
  }

  public static synchronized long updateExc(JenisSimpanan entJenisSimpanan) throws DBException {
    try {
      if (entJenisSimpanan.getOID() != 0) {
        PstJenisSimpanan pstJenisSimpanan = new PstJenisSimpanan(entJenisSimpanan.getOID());
        pstJenisSimpanan.setString(FLD_NAMA_SIMPANAN, entJenisSimpanan.getNamaSimpanan());
        pstJenisSimpanan.setLong(FLD_SETORAN_MIN, entJenisSimpanan.getSetoranMin());
        pstJenisSimpanan.setLong(FLD_BUNGA, entJenisSimpanan.getBunga());
        pstJenisSimpanan.setLong(FLD_PROVISI, entJenisSimpanan.getProvisi());
        pstJenisSimpanan.setLong(FLD_BIAYA_ADMIN, entJenisSimpanan.getBiayaAdmin());
        pstJenisSimpanan.setString(FLD_DESC_JENIS_SIMPANAN, entJenisSimpanan.getDescJenisSimpanan());
        pstJenisSimpanan.setInt(FLD_FREKUENSI_SIMPANAN, entJenisSimpanan.getFrekuensiSimpanan());
        pstJenisSimpanan.setInt(FLD_FREKUENSI_PENARIKAN, entJenisSimpanan.getFrekuensiPenarikan());
        pstJenisSimpanan.update();
        return entJenisSimpanan.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstJenisSimpanan(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((JenisSimpanan) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstJenisSimpanan pstJenisSimpanan = new PstJenisSimpanan(oid);
      pstJenisSimpanan.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstJenisSimpanan(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(JenisSimpanan entJenisSimpanan) throws DBException {
    try {
      PstJenisSimpanan pstJenisSimpanan = new PstJenisSimpanan(0);
      pstJenisSimpanan.setString(FLD_NAMA_SIMPANAN, entJenisSimpanan.getNamaSimpanan());
      pstJenisSimpanan.setLong(FLD_SETORAN_MIN, entJenisSimpanan.getSetoranMin());
      pstJenisSimpanan.setLong(FLD_BUNGA, entJenisSimpanan.getBunga());
      pstJenisSimpanan.setLong(FLD_PROVISI, entJenisSimpanan.getProvisi());
      pstJenisSimpanan.setLong(FLD_BIAYA_ADMIN, entJenisSimpanan.getBiayaAdmin());
      pstJenisSimpanan.setString(FLD_DESC_JENIS_SIMPANAN, entJenisSimpanan.getDescJenisSimpanan());
      pstJenisSimpanan.setInt(FLD_FREKUENSI_SIMPANAN, entJenisSimpanan.getFrekuensiSimpanan());
      pstJenisSimpanan.setInt(FLD_FREKUENSI_PENARIKAN, entJenisSimpanan.getFrekuensiPenarikan());
      pstJenisSimpanan.insert();
      entJenisSimpanan.setOID(pstJenisSimpanan.getlong(FLD_ID_JENIS_SIMPANAN));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstJenisSimpanan(0), DBException.UNKNOWN);
    }
    return entJenisSimpanan.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((JenisSimpanan) entity);
  }

  public static void resultToObject(ResultSet rs, JenisSimpanan entJenisSimpanan) {
    try {
      entJenisSimpanan.setOID(rs.getLong(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_ID_JENIS_SIMPANAN]));
      entJenisSimpanan.setNamaSimpanan(rs.getString(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_NAMA_SIMPANAN]));
      entJenisSimpanan.setSetoranMin(rs.getLong(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_SETORAN_MIN]));
      entJenisSimpanan.setBunga(rs.getLong(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_BUNGA]));
      entJenisSimpanan.setProvisi(rs.getLong(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_PROVISI]));
      entJenisSimpanan.setBiayaAdmin(rs.getLong(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_BIAYA_ADMIN]));
      entJenisSimpanan.setDescJenisSimpanan(rs.getString(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_DESC_JENIS_SIMPANAN]));
      entJenisSimpanan.setFrekuensiSimpanan(rs.getInt(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_FREKUENSI_SIMPANAN]));
      entJenisSimpanan.setFrekuensiPenarikan(rs.getInt(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_FREKUENSI_PENARIKAN]));
    } catch (Exception e) {
    }
  }

  public static Vector listAll() {
    return list(0, 500, "", "");
  }

  public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
    return list(limitStart, recordToGet, whereClause, order, "");
  }

  public static Vector list(int limitStart, int recordToGet, String whereClause, String order, String query) {
    Vector lists = new Vector();
    DBResultSet dbrs = null;
    String sql = query;
    if (sql == "" || sql == null) {
      sql = "SELECT * FROM " + TBL_JENISSIMPANAN + "";

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
    }

    try {
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = (ResultSet) dbrs.getResultSet();
      while (rs.next()) {
        JenisSimpanan jenisSimpanan = new JenisSimpanan();
        resultToObject(rs, jenisSimpanan);
        lists.add(jenisSimpanan);
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

  public static boolean checkOID(long entJenisSimpananId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_JENISSIMPANAN + " WHERE "
              + PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_ID_JENIS_SIMPANAN] + " = " + entJenisSimpananId;
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
      String sql = "SELECT COUNT(" + PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_ID_JENIS_SIMPANAN] + ") FROM " + TBL_JENISSIMPANAN;
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

  public static Vector listJenisSetoran(int year, int month, long member) {
    String sql = "SELECT * FROM `aiso_jenis_simpanan` js "
            + "WHERE js.`ID_JENIS_SIMPANAN` NOT IN ( "
            + "SELECT js.`ID_JENIS_SIMPANAN` "
            + "FROM `aiso_jenis_simpanan` js "
            + "INNER JOIN `aiso_data_tabungan` dt "
            + "ON js.`ID_JENIS_SIMPANAN` = dt.`ID_JENIS_SIMPANAN` "
            + "INNER JOIN sedana_detail_transaksi sdt "
            + "ON sdt.ID_SIMPANAN = dt.ID_SIMPANAN "
            + "WHERE js.`FREKUENSI_SIMPANAN` = 1 "
            + "AND dt.`ID_ANGGOTA` = '" + member + "')"
            + "AND js.`ID_JENIS_SIMPANAN` NOT IN ( "
            + "SELECT "
            + " js.`ID_JENIS_SIMPANAN` "
            + " FROM"
            + " sedana_transaksi AS t "
            + " INNER JOIN sedana_detail_transaksi AS dt "
            + " ON dt.`TRANSAKSI_ID` = t.`TRANSAKSI_ID` "
            + " INNER JOIN aiso_data_tabungan AS adt "
            + " ON adt.`ID_SIMPANAN` = dt.`ID_SIMPANAN` "
            + " INNER JOIN aiso_jenis_simpanan AS js "
            + " ON js.`ID_JENIS_SIMPANAN` = adt.`ID_JENIS_SIMPANAN` "
            + " WHERE t.`ID_ANGGOTA` = '"+member+"' "
            + " AND dt.`KREDIT` <> 0 "
            + " AND YEAR(t.`TANGGAL_TRANSAKSI`) = '"+year+"' "
            + " AND MONTH(t.`TANGGAL_TRANSAKSI`) = '"+month+"' "
            + " AND js.`FREKUENSI_SIMPANAN` = '" + JenisSimpanan.FREKUENSI_SIMPANAN_BERULANG + "')"
            + "AND js.`ID_JENIS_SIMPANAN` NOT IN ("
            + " SELECT dt.ID_JENIS_SIMPANAN "
            + "  FROM `aiso_data_tabungan` dt "
            + "  WHERE dt.`ID_ANGGOTA` = '" + member + "'"
            + "    AND dt.STATUS = 0"
            + ")"
            + "";

    return list(0, 0, "", "", sql);

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
          JenisSimpanan entJenisSimpanan = (JenisSimpanan) list.get(ls);
          if (oid == entJenisSimpanan.getOID()) {
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
