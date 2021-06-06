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

public class PstAfiliasi extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_AFILIASI = "aiso_afiliasi";
  public static final int FLD_AFILIASI_ID = 0;
  public static final int FLD_FEE_KOPERASI = 1;
  public static final int FLD_NAME_AFILIASI = 2;
  public static final int FLD_ALAMAT_AFILIASI = 3;

  public static String[] fieldNames = {
    "AFILIASI_ID",
    "FEE_KOPERASI",
    "NAME_AFILIASI",
    "ALAMAT_AFILIASI"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_STRING
  };

  public PstAfiliasi() {
  }

  public PstAfiliasi(int i) throws DBException {
    super(new PstAfiliasi());
  }

  public PstAfiliasi(String sOid) throws DBException {
    super(new PstAfiliasi(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstAfiliasi(long lOid) throws DBException {
    super(new PstAfiliasi(0));
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
    return TBL_AFILIASI;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstAfiliasi().getClass().getName();
  }

  public static Afiliasi fetchExc(long oid) throws DBException {
    try {
      Afiliasi entAfiliasi = new Afiliasi();
      PstAfiliasi pstAfiliasi = new PstAfiliasi(oid);
      entAfiliasi.setOID(oid);
      entAfiliasi.setFeeKoperasi(pstAfiliasi.getlong(FLD_FEE_KOPERASI));
      entAfiliasi.setNameAfiliasi(pstAfiliasi.getString(FLD_NAME_AFILIASI));
      entAfiliasi.setAlamatAfiliasi(pstAfiliasi.getString(FLD_ALAMAT_AFILIASI));
      return entAfiliasi;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAfiliasi(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    Afiliasi entAfiliasi = fetchExc(entity.getOID());
    entity = (Entity) entAfiliasi;
    return entAfiliasi.getOID();
  }

  public static synchronized long updateExc(Afiliasi entAfiliasi) throws DBException {
    try {
      if (entAfiliasi.getOID() != 0) {
        PstAfiliasi pstAfiliasi = new PstAfiliasi(entAfiliasi.getOID());
        pstAfiliasi.setLong(FLD_FEE_KOPERASI, entAfiliasi.getFeeKoperasi());
        pstAfiliasi.setString(FLD_NAME_AFILIASI, entAfiliasi.getNameAfiliasi());
        pstAfiliasi.setString(FLD_ALAMAT_AFILIASI, entAfiliasi.getAlamatAfiliasi());
        pstAfiliasi.update();
        return entAfiliasi.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAfiliasi(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((Afiliasi) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstAfiliasi pstAfiliasi = new PstAfiliasi(oid);
      pstAfiliasi.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAfiliasi(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(Afiliasi entAfiliasi) throws DBException {
    try {
      PstAfiliasi pstAfiliasi = new PstAfiliasi(0);
      pstAfiliasi.setLong(FLD_FEE_KOPERASI, entAfiliasi.getFeeKoperasi());
      pstAfiliasi.setString(FLD_NAME_AFILIASI, entAfiliasi.getNameAfiliasi());
      pstAfiliasi.setString(FLD_ALAMAT_AFILIASI, entAfiliasi.getAlamatAfiliasi());
      pstAfiliasi.insert();
      entAfiliasi.setOID(pstAfiliasi.getLong(FLD_AFILIASI_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAfiliasi(0), DBException.UNKNOWN);
    }
    return entAfiliasi.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((Afiliasi) entity);
  }

  public static void resultToObject(ResultSet rs, Afiliasi entAfiliasi) {
    try {
      entAfiliasi.setOID(rs.getLong(PstAfiliasi.fieldNames[PstAfiliasi.FLD_AFILIASI_ID]));
      entAfiliasi.setFeeKoperasi(rs.getLong(PstAfiliasi.fieldNames[PstAfiliasi.FLD_FEE_KOPERASI]));
      entAfiliasi.setNameAfiliasi(rs.getString(PstAfiliasi.fieldNames[PstAfiliasi.FLD_NAME_AFILIASI]));
      entAfiliasi.setAlamatAfiliasi(rs.getString(PstAfiliasi.fieldNames[PstAfiliasi.FLD_ALAMAT_AFILIASI]));
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
      String sql = "SELECT * FROM " + TBL_AFILIASI;
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
        Afiliasi entAfiliasi = new Afiliasi();
        resultToObject(rs, entAfiliasi);
        lists.add(entAfiliasi);
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

  public static boolean checkOID(long entAfiliasiId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_AFILIASI + " WHERE "
              + PstAfiliasi.fieldNames[PstAfiliasi.FLD_AFILIASI_ID] + " = " + entAfiliasiId;
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
      String sql = "SELECT COUNT(" + PstAfiliasi.fieldNames[PstAfiliasi.FLD_AFILIASI_ID] + ") FROM " + TBL_AFILIASI;
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
          Afiliasi entAfiliasi = (Afiliasi) list.get(ls);
          if (oid == entAfiliasi.getOID()) {
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
