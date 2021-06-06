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

public class PstDataTabungan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_DATA_TABUNGAN = "aiso_data_tabungan";
  public static final int FLD_ID_SIMPANAN = 0;
  public static final int FLD_ID_ANGGOTA = 1;
  public static final int FLD_TANGGAL = 2;
  public static final int FLD_STATUS = 3;
  public static final int FLD_KODE_TABUNGAN = 4;
  public static final int FLD_CONTACT_ID_AHLI_WARIS = 5;
  public static final int FLD_TANGGAL_TUTUP = 6;
  public static final int FLD_ALASAN_TUTUP = 7;
  public static final int FLD_CATATAN = 8;
  public static final int FLD_ASSIGN_TABUNGAN_ID = 9;
  public static final int FLD_ID_JENIS_SIMPANAN = 10;
  public static final int FLD_ID_ALOKASI_BUNGA = 11;

  public static String[] fieldNames = {
    "ID_SIMPANAN",
    "ID_ANGGOTA",
    "TANGGAL",
    "STATUS",
    "KODE_TABUNGAN",
    "CONTACT_ID_AHLI_WARIS",
    "TANGGAL_TUTUP",
    "ALASAN_TUTUP",
    "CATATAN",
    "ASSIGN_TABUNGAN_ID",
    "ID_JENIS_SIMPANAN",
    "ID_ALOKASI_BUNGA"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_DATE,
    TYPE_INT,
    TYPE_STRING,
    TYPE_LONG,
    TYPE_DATE,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG
  };

  public PstDataTabungan() {
  }

  public PstDataTabungan(int i) throws DBException {
    super(new PstDataTabungan());
  }

  public PstDataTabungan(String sOid) throws DBException {
    super(new PstDataTabungan(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstDataTabungan(long lOid) throws DBException {
    super(new PstDataTabungan(0));
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
    return TBL_DATA_TABUNGAN;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstDataTabungan().getClass().getName();
  }

  public static DataTabungan fetchExc(long oid) throws DBException {
    try {
      DataTabungan entDataTabungan = new DataTabungan();
      PstDataTabungan pstDataTabungan = new PstDataTabungan(oid);
      entDataTabungan.setOID(oid);
      entDataTabungan.setIdAnggota(pstDataTabungan.getlong(FLD_ID_ANGGOTA));
      entDataTabungan.setTanggal(pstDataTabungan.getDate(FLD_TANGGAL));
      entDataTabungan.setStatus(pstDataTabungan.getInt(FLD_STATUS));
      entDataTabungan.setKodeTabungan(pstDataTabungan.getString(FLD_KODE_TABUNGAN));
      entDataTabungan.setContactIdAhliWaris(pstDataTabungan.getlong(FLD_CONTACT_ID_AHLI_WARIS));
      entDataTabungan.setTanggalTutup(pstDataTabungan.getDate(FLD_TANGGAL_TUTUP));
      entDataTabungan.setAlasanTutup(pstDataTabungan.getString(FLD_ALASAN_TUTUP));
      entDataTabungan.setCatatan(pstDataTabungan.getString(FLD_CATATAN));
      entDataTabungan.setAssignTabunganId(pstDataTabungan.getlong(FLD_ASSIGN_TABUNGAN_ID));
      entDataTabungan.setIdJenisSimpanan(pstDataTabungan.getlong(FLD_ID_JENIS_SIMPANAN));
      entDataTabungan.setIdAlokasiBunga(pstDataTabungan.getlong(FLD_ID_ALOKASI_BUNGA));
      return entDataTabungan;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstDataTabungan(0), DBException.UNKNOWN);
    }
  }

  public static DataTabungan fetchWhere(String where) throws DBException {
    Vector<DataTabungan> tab = PstDataTabungan.list(0, 0, where, "");
    return (tab.size() > 0 ? tab.get(0) : new DataTabungan());
  }

  public long fetchExc(Entity entity) throws Exception {
    DataTabungan entDataTabungan = fetchExc(entity.getOID());
    entity = (Entity) entDataTabungan;
    return entDataTabungan.getOID();
  }

  public static synchronized long updateExc(DataTabungan entDataTabungan) throws DBException {
    try {
      if (entDataTabungan.getOID() != 0) {
        PstDataTabungan pstDataTabungan = new PstDataTabungan(entDataTabungan.getOID());
        pstDataTabungan.setLong(FLD_ID_ANGGOTA, entDataTabungan.getIdAnggota());
        pstDataTabungan.setDate(FLD_TANGGAL, entDataTabungan.getTanggal());
        pstDataTabungan.setInt(FLD_STATUS, entDataTabungan.getStatus());
        pstDataTabungan.setString(FLD_KODE_TABUNGAN, entDataTabungan.getKodeTabungan());
        pstDataTabungan.setLong(FLD_CONTACT_ID_AHLI_WARIS, entDataTabungan.getContactIdAhliWaris());
        pstDataTabungan.setDate(FLD_TANGGAL_TUTUP, entDataTabungan.getTanggalTutup());
        pstDataTabungan.setString(FLD_ALASAN_TUTUP, entDataTabungan.getAlasanTutup());
        pstDataTabungan.setString(FLD_CATATAN, entDataTabungan.getCatatan());
        pstDataTabungan.setLong(FLD_ASSIGN_TABUNGAN_ID, entDataTabungan.getAssignTabunganId());
        pstDataTabungan.setLong(FLD_ID_JENIS_SIMPANAN, entDataTabungan.getIdJenisSimpanan());
        pstDataTabungan.setLong(FLD_ID_ALOKASI_BUNGA, entDataTabungan.getIdAlokasiBunga());
        pstDataTabungan.update();
        return entDataTabungan.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstDataTabungan(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((DataTabungan) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstDataTabungan pstDataTabungan = new PstDataTabungan(oid);
      pstDataTabungan.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstDataTabungan(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(DataTabungan entDataTabungan) throws DBException {
    try {
      PstDataTabungan pstDataTabungan = new PstDataTabungan(0);
      pstDataTabungan.setLong(FLD_ID_ANGGOTA, entDataTabungan.getIdAnggota());
      pstDataTabungan.setDate(FLD_TANGGAL, entDataTabungan.getTanggal());
      pstDataTabungan.setInt(FLD_STATUS, entDataTabungan.getStatus());
      pstDataTabungan.setString(FLD_KODE_TABUNGAN, entDataTabungan.getKodeTabungan());
      pstDataTabungan.setDate(FLD_TANGGAL_TUTUP, entDataTabungan.getTanggalTutup());
      pstDataTabungan.setString(FLD_ALASAN_TUTUP, entDataTabungan.getAlasanTutup());
      pstDataTabungan.setString(FLD_CATATAN, entDataTabungan.getCatatan());
      pstDataTabungan.setLong(FLD_ASSIGN_TABUNGAN_ID, entDataTabungan.getAssignTabunganId());
      pstDataTabungan.setLong(FLD_ID_JENIS_SIMPANAN, entDataTabungan.getIdJenisSimpanan());
      pstDataTabungan.setLong(FLD_ID_ALOKASI_BUNGA, entDataTabungan.getIdAlokasiBunga());
      if (entDataTabungan.getContactIdAhliWaris() > 0) {
        pstDataTabungan.setLong(FLD_CONTACT_ID_AHLI_WARIS, entDataTabungan.getContactIdAhliWaris());
      }
      pstDataTabungan.insert();
      entDataTabungan.setOID(pstDataTabungan.getlong(FLD_ID_SIMPANAN));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstDataTabungan(0), DBException.UNKNOWN);
    }
    return entDataTabungan.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((DataTabungan) entity);
  }

  public static void resultToObject(ResultSet rs, DataTabungan entDataTabungan) {
    try {
      entDataTabungan.setOID(rs.getLong(PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_SIMPANAN]));
      entDataTabungan.setIdAnggota(rs.getLong(PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_ANGGOTA]));
      entDataTabungan.setTanggal(rs.getTimestamp(PstDataTabungan.fieldNames[PstDataTabungan.FLD_TANGGAL]));
      entDataTabungan.setStatus(rs.getInt(PstDataTabungan.fieldNames[PstDataTabungan.FLD_STATUS]));
      entDataTabungan.setKodeTabungan(rs.getString(PstDataTabungan.fieldNames[PstDataTabungan.FLD_KODE_TABUNGAN]));
      entDataTabungan.setContactIdAhliWaris(rs.getLong(PstDataTabungan.fieldNames[PstDataTabungan.FLD_CONTACT_ID_AHLI_WARIS]));
      entDataTabungan.setTanggalTutup(rs.getTimestamp(PstDataTabungan.fieldNames[PstDataTabungan.FLD_TANGGAL_TUTUP]));
      entDataTabungan.setAlasanTutup(rs.getString(PstDataTabungan.fieldNames[PstDataTabungan.FLD_ALASAN_TUTUP]));
      entDataTabungan.setCatatan(rs.getString(PstDataTabungan.fieldNames[PstDataTabungan.FLD_CATATAN]));
      entDataTabungan.setAssignTabunganId(rs.getLong(PstDataTabungan.fieldNames[PstDataTabungan.FLD_ASSIGN_TABUNGAN_ID]));
      entDataTabungan.setIdJenisSimpanan(rs.getLong(PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_JENIS_SIMPANAN]));
      entDataTabungan.setIdAlokasiBunga(rs.getLong(PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_ALOKASI_BUNGA]));
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
      String sql = "SELECT * FROM " + TBL_DATA_TABUNGAN;
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
        DataTabungan entDataTabungan = new DataTabungan();
        resultToObject(rs, entDataTabungan);
        lists.add(entDataTabungan);
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

  public static boolean checkOID(long entDataTabunganId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_DATA_TABUNGAN + " WHERE "
              + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_SIMPANAN] + " = " + entDataTabunganId;
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
      String sql = "SELECT COUNT(" + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_SIMPANAN] + ") FROM " + TBL_DATA_TABUNGAN;
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
          DataTabungan entDataTabungan = (DataTabungan) list.get(ls);
          if (oid == entDataTabungan.getOID()) {
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
  
  public static long getIdSimpanan(long memberId, long idJenisSimpanan) {
    long id = 0;
    Vector<DataTabungan> d = list(0, 1, fieldNames[FLD_ID_ANGGOTA]+"="+memberId+" AND "+fieldNames[FLD_ID_JENIS_SIMPANAN]+"="+idJenisSimpanan, "");
    if(d.size()>0) {
      id = d.get(0).getOID();
    }
    return id;
  }
  
  public static long getIdSimpananByAssignSimpanan(long idAssignTabungan, long idJenisSimpanan) {
    long id = 0;
    Vector<DataTabungan> d = list(0, 1, fieldNames[FLD_ASSIGN_TABUNGAN_ID]+"="+idAssignTabungan+" AND "+fieldNames[FLD_ID_JENIS_SIMPANAN]+"="+idJenisSimpanan, "");
    if(d.size()>0) {
      id = d.get(0).getOID();
    }
    return id;
  }

  public static String queryGetSimpananByJenisSimpanan(Vector<Long> js) {
    String x = "";
    for (Long j : js) {
      if (!x.equals("")) {
        x += ", ";
      }
      x += j;
    }
    String u = "SELECT DISTINCT " + fieldNames[FLD_ID_SIMPANAN] + " FROM `" + TBL_DATA_TABUNGAN + "` j JOIN `aiso_jenis_simpanan` USING (ID_JENIS_SIMPANAN) WHERE j.`ID_JENIS_SIMPANAN` IN (" + x + ")";
    return u;
  }

}
