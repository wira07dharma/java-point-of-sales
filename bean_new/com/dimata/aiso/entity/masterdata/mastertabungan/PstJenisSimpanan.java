package com.dimata.aiso.entity.masterdata.mastertabungan;

import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.*;

/**
 *
 * @author dede nuharta
 */
public class PstJenisSimpanan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  /**
   * keteranan: fungsi ini adalah inisialisasi database dengan tabel bernama
   * tb_anggota
   */
  public static final String TBL_AISO_JENIS_SIMPANAN = "aiso_jenis_simpanan";

  public static final int FLD_ID_JENIS_SIMPANAN = 0;
  public static final int FLD_NAMA_SIMPANAN = 1;
  public static final int FLD_SETORAN_MIN = 2;
  public static final int FLD_BUNGA = 3;
  public static final int FLD_PROVISI = 4;
  public static final int FLD_BIAYA_ADMIN = 5;
  public static final int FLD_DESC_JENIS_SIMPANAN = 6;
  public static final int FLD_FREKUENSI_SIMPANAN = 7;
  public static final int FLD_FREKUENSI_PENARIKAN = 8;
  public static final int FLD_BASIS_HARI_BUNGA = 9;
  public static final int FLD_JENIS_BUNGA = 10;

  public static final String[] fieldNames = {
    "ID_JENIS_SIMPANAN",
    "NAMA_SIMPANAN",
    "SETORAN_MIN",
    "BUNGA",
    "PROVISI",
    "BIAYA_ADMIN",
    "DESC_JENIS_SIMPANAN",
    "FREKUENSI_SIMPANAN",
    "FREKUENSI_PENARIKAN",
    "BASIS_HARI_BUNGA",
    "JENIS_BUNGA"
  };

  /**
   * menginisialisasikan tipe data untuk database..
   */
  public static final int[] fieldTypes = {
    TYPE_LONG + TYPE_ID + TYPE_PK,
    TYPE_STRING,
    TYPE_FLOAT,
    TYPE_FLOAT,
    TYPE_FLOAT,
    TYPE_FLOAT,
    TYPE_STRING,
    TYPE_INT,
    TYPE_INT,
    TYPE_FLOAT,
    TYPE_INT,
  };

  public PstJenisSimpanan() {
  }

  /**
   * fungsi yang otomatis akan dipanggil setiap kali melakukan instasiasi
   * terhadap suatu kelas
   *
   * @param i
   * @throws DBException
   */
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
    return TBL_AISO_JENIS_SIMPANAN;
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

  public long fetchExc(Entity ent) throws Exception {
    JenisSimpanan jenisSimpanan = fetchExc(ent.getOID());
    ent = (Entity) jenisSimpanan;
    return jenisSimpanan.getOID();
  }

  public long insertExc(Entity ent) throws Exception {
    return insertExc((JenisSimpanan) ent);
  }

  public long updateExc(Entity ent) throws Exception {
    return updateExc((JenisSimpanan) ent);
  }

  public long deleteExc(Entity ent) throws Exception {
    if (ent == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(ent.getOID());
  }

  public static JenisSimpanan fetchExc(long oid) throws DBException {
    try {
      JenisSimpanan jenisSimpanan = new JenisSimpanan();
      PstJenisSimpanan pstJenisSimpanan = new PstJenisSimpanan(oid);
      jenisSimpanan.setOID(oid);
      jenisSimpanan.setNamaSimpanan(pstJenisSimpanan.getString(FLD_NAMA_SIMPANAN));
      jenisSimpanan.setSetoranMin(pstJenisSimpanan.getdouble(FLD_SETORAN_MIN));
      jenisSimpanan.setBunga(pstJenisSimpanan.getdouble(FLD_BUNGA));
      jenisSimpanan.setProvisi(pstJenisSimpanan.getdouble(FLD_PROVISI));
      jenisSimpanan.setBiayaAdmin(pstJenisSimpanan.getdouble(FLD_BIAYA_ADMIN));
      jenisSimpanan.setDescJenisSimpanan(pstJenisSimpanan.getString(FLD_DESC_JENIS_SIMPANAN));
      jenisSimpanan.setFrekuensiSimpanan(pstJenisSimpanan.getInt(FLD_FREKUENSI_SIMPANAN));
      jenisSimpanan.setFrekuensiPenarikan(pstJenisSimpanan.getInt(FLD_FREKUENSI_PENARIKAN));
      jenisSimpanan.setBasisHariBunga(pstJenisSimpanan.getdouble(FLD_BASIS_HARI_BUNGA));
      jenisSimpanan.setJenisBunga(pstJenisSimpanan.getInt(FLD_JENIS_BUNGA));
      return jenisSimpanan;

    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstJenisSimpanan(0), DBException.UNKNOWN);
    }
  }

  public static long insertExc(JenisSimpanan jenisSimpanan) throws DBException {

    try {
      PstJenisSimpanan pstJenisSimpanan = new PstJenisSimpanan(0);

      pstJenisSimpanan.setString(FLD_NAMA_SIMPANAN, jenisSimpanan.getNamaSimpanan());
      pstJenisSimpanan.setFloat(FLD_SETORAN_MIN, jenisSimpanan.getSetoranMin());
      pstJenisSimpanan.setFloat(FLD_BUNGA, jenisSimpanan.getBunga());
      pstJenisSimpanan.setFloat(FLD_PROVISI, jenisSimpanan.getProvisi());
      pstJenisSimpanan.setFloat(FLD_BIAYA_ADMIN, jenisSimpanan.getBiayaAdmin());
      pstJenisSimpanan.setString(FLD_DESC_JENIS_SIMPANAN, jenisSimpanan.getDescJenisSimpanan());
      pstJenisSimpanan.setInt(FLD_FREKUENSI_SIMPANAN, jenisSimpanan.getFrekuensiSimpanan());
      pstJenisSimpanan.setInt(FLD_FREKUENSI_PENARIKAN, jenisSimpanan.getFrekuensiPenarikan());
      pstJenisSimpanan.setFloat(FLD_BASIS_HARI_BUNGA, jenisSimpanan.getBasisHariBunga());
      pstJenisSimpanan.setInteger(FLD_JENIS_BUNGA, jenisSimpanan.getJenisBunga());

      pstJenisSimpanan.insert();
      jenisSimpanan.setOID(pstJenisSimpanan.getlong(FLD_ID_JENIS_SIMPANAN));

    } catch (DBException dbe) {

      throw dbe;

    } catch (Exception e) {

      throw new DBException(new PstJenisSimpanan(0), DBException.UNKNOWN);

    }

    return jenisSimpanan.getOID();

  }

  public static long updateExc(JenisSimpanan jenisSimpanan) throws DBException {
    try {
      if (jenisSimpanan.getOID() != 0) {
        PstJenisSimpanan pstJenisSimpanan = new PstJenisSimpanan(jenisSimpanan.getOID());
        pstJenisSimpanan.setString(FLD_NAMA_SIMPANAN, jenisSimpanan.getNamaSimpanan());
        pstJenisSimpanan.setFloat(FLD_SETORAN_MIN, jenisSimpanan.getSetoranMin());
        pstJenisSimpanan.setFloat(FLD_BUNGA, jenisSimpanan.getBunga());
        pstJenisSimpanan.setFloat(FLD_PROVISI, jenisSimpanan.getProvisi());
        pstJenisSimpanan.setFloat(FLD_BIAYA_ADMIN, jenisSimpanan.getBiayaAdmin());
        pstJenisSimpanan.setString(FLD_DESC_JENIS_SIMPANAN, jenisSimpanan.getDescJenisSimpanan());
        pstJenisSimpanan.setInt(FLD_FREKUENSI_SIMPANAN, jenisSimpanan.getFrekuensiSimpanan());
        pstJenisSimpanan.setInt(FLD_FREKUENSI_PENARIKAN, jenisSimpanan.getFrekuensiPenarikan());
        pstJenisSimpanan.setFloat(FLD_BASIS_HARI_BUNGA, jenisSimpanan.getBasisHariBunga());
        pstJenisSimpanan.setInteger(FLD_JENIS_BUNGA, jenisSimpanan.getJenisBunga());
        pstJenisSimpanan.update();
        return jenisSimpanan.getOID();
      }

    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstJenisSimpanan(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public static long deleteExc(long oid) throws DBException {
    try {
      /**
       * Mendefinisikan objek sekalligus menginisialisasi nilai objek.
       */
      PstJenisSimpanan pstJenisSimpanan = new PstJenisSimpanan(oid);
      pstJenisSimpanan.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstJenisSimpanan(0), DBException.UNKNOWN);
    }
    return oid;
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
      sql = "SELECT * FROM " + TBL_AISO_JENIS_SIMPANAN + "";

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

  public static Vector listJenisSetoran(int year, int month, long member) {
    String sql = "SELECT * FROM `aiso_jenis_simpanan` js "
            + "WHERE js.`ID_JENIS_SIMPANAN` NOT IN ( "
            + "SELECT js.`ID_JENIS_SIMPANAN` "
            + "FROM `aiso_jenis_simpanan` js "
            + "INNER JOIN `aiso_data_tabungan` dt "
            + "ON js.`ID_JENIS_SIMPANAN` = dt.`ID_JENIS_SIMPANAN` "
            + "WHERE js.`FREKUENSI_SIMPANAN` = 1 "
            + "AND dt.`ID_ANGGOTA` = " + member + ") "
            + "AND js.`ID_JENIS_SIMPANAN` NOT IN ( "
            + "SELECT js.`ID_JENIS_SIMPANAN` "
            + "FROM `aiso_jenis_simpanan` js "
            + "INNER JOIN `aiso_data_tabungan` dt "
            + "ON js.`ID_JENIS_SIMPANAN` = dt.`ID_JENIS_SIMPANAN` "
            + "WHERE ( YEAR(dt.`TANGGAL`) = " + year + " "
            + "AND MONTH(dt.`TANGGAL`) = " + month + " "
            + "AND js.`FREKUENSI_SIMPANAN` = 2 "
            + "AND dt.`ID_ANGGOTA` = " + member + " ) )";

    return list(0, 0, "", "", sql);

  }

  public static void resultToObject(ResultSet rs, JenisSimpanan jenisSimpanan) {
    try {

      jenisSimpanan.setOID(rs.getLong(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_ID_JENIS_SIMPANAN]));

      jenisSimpanan.setNamaSimpanan(rs.getString(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_NAMA_SIMPANAN]));

      jenisSimpanan.setSetoranMin(rs.getFloat(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_SETORAN_MIN]));

      jenisSimpanan.setBunga(rs.getFloat(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_BUNGA]));

      jenisSimpanan.setProvisi(rs.getFloat(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_PROVISI]));

      jenisSimpanan.setBiayaAdmin(rs.getFloat(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_BIAYA_ADMIN]));

      jenisSimpanan.setDescJenisSimpanan(rs.getString(PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_DESC_JENIS_SIMPANAN]));
      jenisSimpanan.setFrekuensiSimpanan(rs.getInt(PstJenisSimpanan.fieldNames[FLD_FREKUENSI_SIMPANAN]));
      jenisSimpanan.setFrekuensiPenarikan(rs.getInt(PstJenisSimpanan.fieldNames[FLD_FREKUENSI_PENARIKAN]));
      jenisSimpanan.setBasisHariBunga(rs.getDouble(PstJenisSimpanan.fieldNames[FLD_BASIS_HARI_BUNGA]));
      jenisSimpanan.setJenisBunga(rs.getInt(PstJenisSimpanan.fieldNames[FLD_JENIS_BUNGA]));

    } catch (Exception e) {
      System.out.println("Exception" + e);
    }
  }

  public static boolean checkOID(long osId) {

    DBResultSet dbrs = null;

    boolean result = false;

    try {

      String sql = "SELECT * FROM " + TBL_AISO_JENIS_SIMPANAN + " WHERE "
              + PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_ID_JENIS_SIMPANAN] + " = " + osId;

      dbrs = DBHandler.execQueryResult(sql);

      ResultSet rs = (ResultSet) dbrs.getResultSet();

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
      String sql = "SELECT COUNT(" + PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_NAMA_SIMPANAN] + ") FROM " + TBL_AISO_JENIS_SIMPANAN;
      if (whereClause != null && whereClause.length() > 0) {
        sql = sql + " WHERE " + whereClause;
      }

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = (ResultSet) dbrs.getResultSet();
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
          JenisSimpanan jenisSimpanan = (JenisSimpanan) list.get(ls);
          if (oid == jenisSimpanan.getOID()) {
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
