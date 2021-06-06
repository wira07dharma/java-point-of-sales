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
import com.dimata.sedana.entity.assigncontacttabungan.AssignContactTabungan;
import com.dimata.sedana.entity.assigncontacttabungan.PstAssignContactTabungan;
import com.dimata.sedana.entity.tabungan.DataTabungan;
import com.dimata.sedana.entity.tabungan.PstDataTabungan;
import com.dimata.util.Command;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dimata 007
 */
public class PstTingkatanBunga extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_TINGKATAN_BUNGA = "sedana_tingkatan_bunga";
  public static final int FLD_ID_TINGKATAN_BUNGA = 0;
  public static final int FLD_ID_JENIS_SIMPANAN = 1;
  public static final int FLD_NOMINAL_SALDO_MIN = 2;
  public static final int FLD_PERSENTASE_BUNGA = 3;

  public static String[] fieldNames = {
    "ID_TINGKATAN_BUNGA",
    "ID_JENIS_SIMPANAN",
    "NOMINAL_SALDO_MIN",
    "PERSENTASE_BUNGA"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_FLOAT,
    TYPE_FLOAT
  };

  public PstTingkatanBunga() {
  }

  public PstTingkatanBunga(int i) throws DBException {
    super(new PstTingkatanBunga());
  }

  public PstTingkatanBunga(String sOid) throws DBException {
    super(new PstTingkatanBunga(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstTingkatanBunga(long lOid) throws DBException {
    super(new PstTingkatanBunga(0));
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
    return TBL_TINGKATAN_BUNGA;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstTingkatanBunga().getClass().getName();
  }
  
  public static double getBungaBySaldoAndNoTabungan(double saldo, String noTabungan) {
    double bunga = 0;
    try {
      Vector<AssignContactTabungan> act = PstAssignContactTabungan.list(0, 0, PstAssignContactTabungan.fieldNames[PstAssignContactTabungan.FLD_NO_TABUNGAN]+"='"+noTabungan+"'", "");
      bunga = getBungaBySaldoAndAssignContactTab(saldo, act.get(0).getOID());
    } catch(Exception e) {
      e.printStackTrace();
    }
    return bunga;
  }
  
  public static double getBungaBySaldoAndAssignContactTab(double saldo, long idAssignContactTabungan) {
    double bunga = 0;
    try {
      Vector<DataTabungan> dt = PstDataTabungan.list(0, 0, PstDataTabungan.fieldNames[PstDataTabungan.FLD_ASSIGN_TABUNGAN_ID]+"='"+idAssignContactTabungan+"'", "");
      bunga = getBungaBySaldo(saldo, dt.get(0).getOID());
    } catch(Exception e) {
      e.printStackTrace();
    }
    return bunga;
  }
  
  public static double getBungaBySaldo(double saldo, long idDataTabungan/* A.K.A ID_SIMPANAN */) {
    double bunga = 0;
    try {
      DataTabungan dataTabungan = PstDataTabungan.fetchExc(idDataTabungan);
      Vector<TingkatanBunga> list = list(0, 0, fieldNames[FLD_NOMINAL_SALDO_MIN]+"<="+saldo+" AND "+fieldNames[FLD_ID_JENIS_SIMPANAN]+"="+dataTabungan.getIdJenisSimpanan(), fieldNames[FLD_NOMINAL_SALDO_MIN]+" DESC");
      if (!list.isEmpty()) {
        bunga = list.get(0).getPersentaseBunga();
      }
    } catch (DBException ex) {
      Logger.getLogger(PstTingkatanBunga.class.getName()).log(Level.SEVERE, null, ex);
    }
    return bunga;
  }

  public static TingkatanBunga fetchExc(long oid) throws DBException {
    try {
      TingkatanBunga entTingkatanBunga = new TingkatanBunga();
      PstTingkatanBunga pstTingkatanBunga = new PstTingkatanBunga(oid);
      entTingkatanBunga.setOID(oid);
      entTingkatanBunga.setIdJenisSimpanan(pstTingkatanBunga.getlong(FLD_ID_JENIS_SIMPANAN));
      entTingkatanBunga.setNominalSaldoMin(pstTingkatanBunga.getdouble(FLD_NOMINAL_SALDO_MIN));
      entTingkatanBunga.setPersentaseBunga(pstTingkatanBunga.getdouble(FLD_PERSENTASE_BUNGA));
      return entTingkatanBunga;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTingkatanBunga(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    TingkatanBunga entTingkatanBunga = fetchExc(entity.getOID());
    entity = (Entity) entTingkatanBunga;
    return entTingkatanBunga.getOID();
  }

  public static synchronized long updateExc(TingkatanBunga entTingkatanBunga) throws DBException {
    try {
      if (entTingkatanBunga.getOID() != 0) {
        PstTingkatanBunga pstTingkatanBunga = new PstTingkatanBunga(entTingkatanBunga.getOID());
        pstTingkatanBunga.setLong(FLD_ID_JENIS_SIMPANAN, entTingkatanBunga.getIdJenisSimpanan());
        pstTingkatanBunga.setDouble(FLD_NOMINAL_SALDO_MIN, entTingkatanBunga.getNominalSaldoMin());
        pstTingkatanBunga.setDouble(FLD_PERSENTASE_BUNGA, entTingkatanBunga.getPersentaseBunga());
        pstTingkatanBunga.update();
        return entTingkatanBunga.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTingkatanBunga(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((TingkatanBunga) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstTingkatanBunga pstTingkatanBunga = new PstTingkatanBunga(oid);
      pstTingkatanBunga.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTingkatanBunga(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(TingkatanBunga entTingkatanBunga) throws DBException {
    try {
      PstTingkatanBunga pstTingkatanBunga = new PstTingkatanBunga(0);
      pstTingkatanBunga.setLong(FLD_ID_JENIS_SIMPANAN, entTingkatanBunga.getIdJenisSimpanan());
      pstTingkatanBunga.setDouble(FLD_NOMINAL_SALDO_MIN, entTingkatanBunga.getNominalSaldoMin());
      pstTingkatanBunga.setDouble(FLD_PERSENTASE_BUNGA, entTingkatanBunga.getPersentaseBunga());
      pstTingkatanBunga.insert();
      entTingkatanBunga.setOID(pstTingkatanBunga.getlong(FLD_ID_TINGKATAN_BUNGA));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTingkatanBunga(0), DBException.UNKNOWN);
    }
    return entTingkatanBunga.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((TingkatanBunga) entity);
  }

  public static void resultToObject(ResultSet rs, TingkatanBunga entTingkatanBunga) {
    try {
      entTingkatanBunga.setOID(rs.getLong(PstTingkatanBunga.fieldNames[PstTingkatanBunga.FLD_ID_TINGKATAN_BUNGA]));
      entTingkatanBunga.setIdJenisSimpanan(rs.getLong(PstTingkatanBunga.fieldNames[PstTingkatanBunga.FLD_ID_JENIS_SIMPANAN]));
      entTingkatanBunga.setNominalSaldoMin(rs.getDouble(PstTingkatanBunga.fieldNames[PstTingkatanBunga.FLD_NOMINAL_SALDO_MIN]));
      entTingkatanBunga.setPersentaseBunga(rs.getDouble(PstTingkatanBunga.fieldNames[PstTingkatanBunga.FLD_PERSENTASE_BUNGA]));
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
      String sql = "SELECT * FROM " + TBL_TINGKATAN_BUNGA;
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
        TingkatanBunga entTingkatanBunga = new TingkatanBunga();
        resultToObject(rs, entTingkatanBunga);
        lists.add(entTingkatanBunga);
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

  public static boolean checkOID(long entTingkatanBungaId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_TINGKATAN_BUNGA + " WHERE "
              + PstTingkatanBunga.fieldNames[PstTingkatanBunga.FLD_ID_TINGKATAN_BUNGA] + " = " + entTingkatanBungaId;
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
      String sql = "SELECT COUNT(" + PstTingkatanBunga.fieldNames[PstTingkatanBunga.FLD_ID_TINGKATAN_BUNGA] + ") FROM " + TBL_TINGKATAN_BUNGA;
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
          TingkatanBunga entTingkatanBunga = (TingkatanBunga) list.get(ls);
          if (oid == entTingkatanBunga.getOID()) {
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
