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

public class PstMasterTabungan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_MASTERTABUNGAN = "sedana_master_tabungan";
  public static final int FLD_MASTER_TABUNGAN_ID = 0;
  public static final int FLD_KODE_TABUNGAN = 1;
  public static final int FLD_NAMA_TABUNGAN = 2;
  public static final int FLD_KET = 3;
  public static final int FLD_DENDA = 4;
  public static final int FLD_JENIS_DENDA = 5;
  public static final int FLD_MIN_SALDO_BUNGA = 6;

  public static String[] fieldNames = {
    "MASTER_TABUNGAN_ID",
    "KODE_TABUNGAN",
    "NAMA_TABUNGAN",
    "KET",
    "DENDA",
    "JENIS_DENDA",
    "MIN_SALDO_BUNGA"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_FLOAT,
    TYPE_INT,
    TYPE_FLOAT
  };

  public PstMasterTabungan() {
  }

  public PstMasterTabungan(int i) throws DBException {
    super(new PstMasterTabungan());
  }

  public PstMasterTabungan(String sOid) throws DBException {
    super(new PstMasterTabungan(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstMasterTabungan(long lOid) throws DBException {
    super(new PstMasterTabungan(0));
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
    return TBL_MASTERTABUNGAN;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstMasterTabungan().getClass().getName();
  }

  public static MasterTabungan fetchExc(long oid) {
    MasterTabungan entMasterTabungan = new MasterTabungan();
    try {
      PstMasterTabungan pstMasterTabungan = new PstMasterTabungan(oid);
      entMasterTabungan.setOID(oid);
      entMasterTabungan.setKodeTabungan(pstMasterTabungan.getString(FLD_KODE_TABUNGAN));
      entMasterTabungan.setNamaTabungan(pstMasterTabungan.getString(FLD_NAMA_TABUNGAN));
      entMasterTabungan.setKet(pstMasterTabungan.getString(FLD_KET));
      entMasterTabungan.setDenda(pstMasterTabungan.getdouble(FLD_DENDA));
      entMasterTabungan.setJenisDenda(pstMasterTabungan.getInt(FLD_JENIS_DENDA));
      entMasterTabungan.setMinSaldoBunga(pstMasterTabungan.getInt(FLD_MIN_SALDO_BUNGA));
    } catch (DBException dbe) {
      System.err.println(dbe);
    } catch (Exception e) {
      System.err.println(e);
    }
    return entMasterTabungan;
  }

  public long fetchExc(Entity entity) throws Exception {
    MasterTabungan entMasterTabungan = fetchExc(entity.getOID());
    entity = (Entity) entMasterTabungan;
    return entMasterTabungan.getOID();
  }

  public static synchronized long updateExc(MasterTabungan entMasterTabungan) throws DBException {
    try {
      if (entMasterTabungan.getOID() != 0) {
        PstMasterTabungan pstMasterTabungan = new PstMasterTabungan(entMasterTabungan.getOID());
        pstMasterTabungan.setString(FLD_KODE_TABUNGAN, entMasterTabungan.getKodeTabungan());
        pstMasterTabungan.setString(FLD_NAMA_TABUNGAN, entMasterTabungan.getNamaTabungan());
        pstMasterTabungan.setString(FLD_KET, entMasterTabungan.getKet());
        pstMasterTabungan.setDouble(FLD_DENDA, entMasterTabungan.getDenda());
        pstMasterTabungan.setInt(FLD_JENIS_DENDA, entMasterTabungan.getJenisDenda());
        pstMasterTabungan.setDouble(FLD_MIN_SALDO_BUNGA, entMasterTabungan.getMinSaldoBunga());
        pstMasterTabungan.update();
        return entMasterTabungan.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstMasterTabungan(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((MasterTabungan) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstMasterTabungan pstMasterTabungan = new PstMasterTabungan(oid);
      pstMasterTabungan.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstMasterTabungan(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(MasterTabungan entMasterTabungan) throws DBException {
    try {
      PstMasterTabungan pstMasterTabungan = new PstMasterTabungan(0);
      pstMasterTabungan.setString(FLD_KODE_TABUNGAN, entMasterTabungan.getKodeTabungan());
      pstMasterTabungan.setString(FLD_NAMA_TABUNGAN, entMasterTabungan.getNamaTabungan());
      pstMasterTabungan.setString(FLD_KET, entMasterTabungan.getKet());
      pstMasterTabungan.setDouble(FLD_DENDA, entMasterTabungan.getDenda());
      pstMasterTabungan.setInt(FLD_JENIS_DENDA, entMasterTabungan.getJenisDenda());
      pstMasterTabungan.setDouble(FLD_MIN_SALDO_BUNGA, entMasterTabungan.getMinSaldoBunga());
      pstMasterTabungan.insert();
      entMasterTabungan.setOID(pstMasterTabungan.getlong(FLD_MASTER_TABUNGAN_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstMasterTabungan(0), DBException.UNKNOWN);
    }
    return entMasterTabungan.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((MasterTabungan) entity);
  }

  public static void resultToObject(ResultSet rs, MasterTabungan entMasterTabungan) {
    try {
      entMasterTabungan.setOID(rs.getLong(PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_MASTER_TABUNGAN_ID]));
      entMasterTabungan.setKodeTabungan(rs.getString(PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_KODE_TABUNGAN]));
      entMasterTabungan.setNamaTabungan(rs.getString(PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_NAMA_TABUNGAN]));
      entMasterTabungan.setKet(rs.getString(PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_KET]));
      entMasterTabungan.setDenda(rs.getDouble(PstMasterTabungan.fieldNames[FLD_DENDA]));
      entMasterTabungan.setJenisDenda(rs.getInt(PstMasterTabungan.fieldNames[FLD_JENIS_DENDA]));
      entMasterTabungan.setMinSaldoBunga(rs.getInt(PstMasterTabungan.fieldNames[FLD_MIN_SALDO_BUNGA]));
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
      String sql = "SELECT * FROM " + TBL_MASTERTABUNGAN;
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
        MasterTabungan entMasterTabungan = new MasterTabungan();
        resultToObject(rs, entMasterTabungan);
        lists.add(entMasterTabungan);
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

  public static boolean checkOID(long entMasterTabunganId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_MASTERTABUNGAN + " WHERE "
              + PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_MASTER_TABUNGAN_ID] + " = " + entMasterTabunganId;
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
      String sql = "SELECT COUNT(" + PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_MASTER_TABUNGAN_ID] + ") FROM " + TBL_MASTERTABUNGAN;
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
          MasterTabungan entMasterTabungan = (MasterTabungan) list.get(ls);
          if (oid == entMasterTabungan.getOID()) {
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
