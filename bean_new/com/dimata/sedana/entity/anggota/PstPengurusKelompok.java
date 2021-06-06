/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.anggota;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class PstPengurusKelompok extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_PENGURUS_KELOMPOK = "pengurus_kelompok_badan_usaha";
  public static final int FLD_ID_PENGURUS = 0;
  public static final int FLD_NAMA_PENGURUS = 1;
  public static final int FLD_JENIS_KELAMIN = 2;
  public static final int FLD_STATUS_KEPEMILIKAN = 3;
  public static final int FLD_JABATAN = 4;
  public static final int FLD_TELEPON = 5;
  public static final int FLD_EMAIL = 6;
  public static final int FLD_ALAMAT = 7;
  public static final int FLD_PROSENTASE_KEPENGURUSAN = 8;
  public static final int FLD_ID_KELOMPOK = 9;

  public static String[] fieldNames = {
    "ID_PENGURUS",
    "NAMA_PENGURUS",
    "JENIS_KELAMIN",
    "STATUS_KEPEMILIKAN",
    "JABATAN",
    "TELEPON",
    "EMAIL",
    "ALAMAT",
    "PROSENTASE_KEPENGURUSAN",
    "ID_KELOMPOK"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_STRING,
    TYPE_INT,
    TYPE_INT,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_FLOAT,
    TYPE_LONG
  };

  public PstPengurusKelompok() {
  }

  public PstPengurusKelompok(int i) throws DBException {
    super(new PstPengurusKelompok());
  }

  public PstPengurusKelompok(String sOid) throws DBException {
    super(new PstPengurusKelompok(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstPengurusKelompok(long lOid) throws DBException {
    super(new PstPengurusKelompok(0));
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
    return TBL_PENGURUS_KELOMPOK;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstPengurusKelompok().getClass().getName();
  }

  public static PengurusKelompok fetchExc(long oid) throws DBException {
    try {
      PengurusKelompok entPengurusKelompok = new PengurusKelompok();
      PstPengurusKelompok pstPengurusKelompok = new PstPengurusKelompok(oid);
      entPengurusKelompok.setOID(oid);
      entPengurusKelompok.setNamaPengurus(pstPengurusKelompok.getString(FLD_NAMA_PENGURUS));
      entPengurusKelompok.setJenisKelamin(pstPengurusKelompok.getInt(FLD_JENIS_KELAMIN));
      entPengurusKelompok.setStatusKepemilikan(pstPengurusKelompok.getInt(FLD_STATUS_KEPEMILIKAN));
      entPengurusKelompok.setJabatan(pstPengurusKelompok.getInt(FLD_JABATAN));
      entPengurusKelompok.setTelepon(pstPengurusKelompok.getString(FLD_TELEPON));
      entPengurusKelompok.setEmail(pstPengurusKelompok.getString(FLD_EMAIL));
      entPengurusKelompok.setAlamat(pstPengurusKelompok.getString(FLD_ALAMAT));
      entPengurusKelompok.setProsentaseKepengurusan(pstPengurusKelompok.getdouble(FLD_PROSENTASE_KEPENGURUSAN));
      entPengurusKelompok.setIdKelompok(pstPengurusKelompok.getlong(FLD_ID_KELOMPOK));
      return entPengurusKelompok;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstPengurusKelompok(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    PengurusKelompok entPengurusKelompok = fetchExc(entity.getOID());
    entity = (Entity) entPengurusKelompok;
    return entPengurusKelompok.getOID();
  }

  public static synchronized long updateExc(PengurusKelompok entPengurusKelompok) throws DBException {
    try {
      if (entPengurusKelompok.getOID() != 0) {
        PstPengurusKelompok pstPengurusKelompok = new PstPengurusKelompok(entPengurusKelompok.getOID());
        pstPengurusKelompok.setString(FLD_NAMA_PENGURUS, entPengurusKelompok.getNamaPengurus());
        pstPengurusKelompok.setInt(FLD_JENIS_KELAMIN, entPengurusKelompok.getJenisKelamin());
        pstPengurusKelompok.setInt(FLD_STATUS_KEPEMILIKAN, entPengurusKelompok.getStatusKepemilikan());
        pstPengurusKelompok.setLong(FLD_JABATAN, entPengurusKelompok.getJabatan());
        pstPengurusKelompok.setString(FLD_TELEPON, entPengurusKelompok.getTelepon());
        pstPengurusKelompok.setString(FLD_EMAIL, entPengurusKelompok.getEmail());
        pstPengurusKelompok.setString(FLD_ALAMAT, entPengurusKelompok.getAlamat());
        pstPengurusKelompok.setDouble(FLD_PROSENTASE_KEPENGURUSAN, entPengurusKelompok.getProsentaseKepengurusan());
        if (entPengurusKelompok.getIdKelompok() > 0) {
          pstPengurusKelompok.setLong(FLD_ID_KELOMPOK, entPengurusKelompok.getIdKelompok());
        }
        pstPengurusKelompok.update();
        return entPengurusKelompok.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstPengurusKelompok(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((PengurusKelompok) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstPengurusKelompok pstPengurusKelompok = new PstPengurusKelompok(oid);
      pstPengurusKelompok.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstPengurusKelompok(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(PengurusKelompok entPengurusKelompok) throws DBException {
    try {
      PstPengurusKelompok pstPengurusKelompok = new PstPengurusKelompok(0);
      pstPengurusKelompok.setString(FLD_NAMA_PENGURUS, entPengurusKelompok.getNamaPengurus());
      pstPengurusKelompok.setInt(FLD_JENIS_KELAMIN, entPengurusKelompok.getJenisKelamin());
      pstPengurusKelompok.setInt(FLD_STATUS_KEPEMILIKAN, entPengurusKelompok.getStatusKepemilikan());
      pstPengurusKelompok.setLong(FLD_JABATAN, entPengurusKelompok.getJabatan());
      pstPengurusKelompok.setString(FLD_TELEPON, entPengurusKelompok.getTelepon());
      pstPengurusKelompok.setString(FLD_EMAIL, entPengurusKelompok.getEmail());
      pstPengurusKelompok.setString(FLD_ALAMAT, entPengurusKelompok.getAlamat());
      pstPengurusKelompok.setDouble(FLD_PROSENTASE_KEPENGURUSAN, entPengurusKelompok.getProsentaseKepengurusan());
      if (entPengurusKelompok.getIdKelompok() > 0) {
        pstPengurusKelompok.setLong(FLD_ID_KELOMPOK, entPengurusKelompok.getIdKelompok());
      }
      pstPengurusKelompok.insert();
      entPengurusKelompok.setOID(pstPengurusKelompok.getlong(FLD_ID_PENGURUS));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstPengurusKelompok(0), DBException.UNKNOWN);
    }
    return entPengurusKelompok.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((PengurusKelompok) entity);
  }

  public static void resultToObject(ResultSet rs, PengurusKelompok entPengurusKelompok) {
    try {
      entPengurusKelompok.setOID(rs.getLong(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_ID_PENGURUS]));
      entPengurusKelompok.setNamaPengurus(rs.getString(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_NAMA_PENGURUS]));
      entPengurusKelompok.setJenisKelamin(rs.getInt(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_JENIS_KELAMIN]));
      entPengurusKelompok.setStatusKepemilikan(rs.getInt(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_STATUS_KEPEMILIKAN]));
      entPengurusKelompok.setJabatan(rs.getLong(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_JABATAN]));
      entPengurusKelompok.setTelepon(rs.getString(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_TELEPON]));
      entPengurusKelompok.setEmail(rs.getString(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_EMAIL]));
      entPengurusKelompok.setAlamat(rs.getString(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_ALAMAT]));
      entPengurusKelompok.setProsentaseKepengurusan(rs.getDouble(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_PROSENTASE_KEPENGURUSAN]));
      entPengurusKelompok.setIdKelompok(rs.getLong(PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_ID_KELOMPOK]));
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
      String sql = "SELECT * FROM " + TBL_PENGURUS_KELOMPOK;
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
        PengurusKelompok entPengurusKelompok = new PengurusKelompok();
        resultToObject(rs, entPengurusKelompok);
        lists.add(entPengurusKelompok);
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

  public static boolean checkOID(long entPengurusKelompokId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_PENGURUS_KELOMPOK + " WHERE "
              + PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_ID_PENGURUS] + " = " + entPengurusKelompokId;
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
      String sql = "SELECT COUNT(" + PstPengurusKelompok.fieldNames[PstPengurusKelompok.FLD_ID_PENGURUS] + ") FROM " + TBL_PENGURUS_KELOMPOK;
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
          PengurusKelompok entPengurusKelompok = (PengurusKelompok) list.get(ls);
          if (oid == entPengurusKelompok.getOID()) {
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
