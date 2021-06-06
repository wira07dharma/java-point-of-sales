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

public class PstTransaksiPayment extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_TRANSAKSIPAYMENT = "sedana_transaksi_payment";
  public static final int FLD_TRANSAKSI_PAYMENT_ID = 0;
  public static final int FLD_PAYMENT_SYSTEM_ID = 1;
  public static final int FLD_TRANSAKSI_ID = 2;
  public static final int FLD_JUMLAH = 3;
  public static final int FLD_CARD_NAME = 4;
  public static final int FLD_CARD_NO = 5;
  public static final int FLD_BANK_NAME = 6;
  public static final int FLD_VALIDATE_DATE = 7;

  public static String[] fieldNames = {
    "TRANSAKSI_PAYMENT_ID",
    "PAYMENT_SYSTEM_ID",
    "TRANSAKSI_ID",
    "JUMLAH",
    "CARD_NAME",
    "CARD_NO",
    "BANK_NAME",
    "VALIDATE_DATE"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_DATE
  };

  public PstTransaksiPayment() {
  }

  public PstTransaksiPayment(int i) throws DBException {
    super(new PstTransaksiPayment());
  }

  public PstTransaksiPayment(String sOid) throws DBException {
    super(new PstTransaksiPayment(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstTransaksiPayment(long lOid) throws DBException {
    super(new PstTransaksiPayment(0));
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
    return TBL_TRANSAKSIPAYMENT;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstTransaksiPayment().getClass().getName();
  }

  public static TransaksiPayment fetchExc(long oid) throws DBException {
    try {
      TransaksiPayment entTransaksiPayment = new TransaksiPayment();
      PstTransaksiPayment pstTransaksiPayment = new PstTransaksiPayment(oid);
      entTransaksiPayment.setOID(oid);
      entTransaksiPayment.setPaymentSystemId(pstTransaksiPayment.getLong(FLD_PAYMENT_SYSTEM_ID));
      entTransaksiPayment.setTransaksiId(pstTransaksiPayment.getLong(FLD_TRANSAKSI_ID));
      entTransaksiPayment.setJumlah(pstTransaksiPayment.getdouble(FLD_JUMLAH));
      entTransaksiPayment.setCardName(pstTransaksiPayment.getString(FLD_CARD_NAME));
      entTransaksiPayment.setCardNo(pstTransaksiPayment.getString(FLD_CARD_NO));
      entTransaksiPayment.setBankName(pstTransaksiPayment.getString(FLD_BANK_NAME));
      entTransaksiPayment.setValidateDate(pstTransaksiPayment.getDate(FLD_VALIDATE_DATE));
      return entTransaksiPayment;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTransaksiPayment(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    TransaksiPayment entTransaksiPayment = fetchExc(entity.getOID());
    entity = (Entity) entTransaksiPayment;
    return entTransaksiPayment.getOID();
  }

  public static synchronized long updateExc(TransaksiPayment entTransaksiPayment) throws DBException {
    try {
      if (entTransaksiPayment.getOID() != 0) {
        PstTransaksiPayment pstTransaksiPayment = new PstTransaksiPayment(entTransaksiPayment.getOID());
        pstTransaksiPayment.setLong(FLD_PAYMENT_SYSTEM_ID, entTransaksiPayment.getPaymentSystemId());
        pstTransaksiPayment.setLong(FLD_TRANSAKSI_ID, entTransaksiPayment.getTransaksiId());
        pstTransaksiPayment.setDouble(FLD_JUMLAH, entTransaksiPayment.getJumlah());
        pstTransaksiPayment.setString(FLD_CARD_NAME, entTransaksiPayment.getCardName());
        pstTransaksiPayment.setString(FLD_CARD_NO, entTransaksiPayment.getCardNo());
        pstTransaksiPayment.setString(FLD_BANK_NAME, entTransaksiPayment.getBankName());
        pstTransaksiPayment.setDate(FLD_VALIDATE_DATE, entTransaksiPayment.getValidateDate());
        pstTransaksiPayment.update();
        return entTransaksiPayment.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTransaksiPayment(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((TransaksiPayment) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstTransaksiPayment pstTransaksiPayment = new PstTransaksiPayment(oid);
      pstTransaksiPayment.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTransaksiPayment(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(TransaksiPayment entTransaksiPayment) throws DBException {
    try {
      PstTransaksiPayment pstTransaksiPayment = new PstTransaksiPayment(0);
      pstTransaksiPayment.setLong(FLD_PAYMENT_SYSTEM_ID, entTransaksiPayment.getPaymentSystemId());
      pstTransaksiPayment.setLong(FLD_TRANSAKSI_ID, entTransaksiPayment.getTransaksiId());
      pstTransaksiPayment.setDouble(FLD_JUMLAH, entTransaksiPayment.getJumlah());
      pstTransaksiPayment.setString(FLD_CARD_NAME, entTransaksiPayment.getCardName());
      pstTransaksiPayment.setString(FLD_CARD_NO, entTransaksiPayment.getCardNo());
      pstTransaksiPayment.setString(FLD_BANK_NAME, entTransaksiPayment.getBankName());
      pstTransaksiPayment.setDate(FLD_VALIDATE_DATE, entTransaksiPayment.getValidateDate());
      pstTransaksiPayment.insert();
      entTransaksiPayment.setOID(pstTransaksiPayment.getlong(FLD_TRANSAKSI_PAYMENT_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTransaksiPayment(0), DBException.UNKNOWN);
    }
    return entTransaksiPayment.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((TransaksiPayment) entity);
  }

  public static void resultToObject(ResultSet rs, TransaksiPayment entTransaksiPayment) {
    try {
      entTransaksiPayment.setOID(rs.getLong(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_TRANSAKSI_PAYMENT_ID]));
      entTransaksiPayment.setPaymentSystemId(rs.getLong(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_PAYMENT_SYSTEM_ID]));
      entTransaksiPayment.setTransaksiId(rs.getLong(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_TRANSAKSI_ID]));
      entTransaksiPayment.setJumlah(rs.getDouble(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_JUMLAH]));
      entTransaksiPayment.setCardName(rs.getString(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_CARD_NAME]));
      entTransaksiPayment.setCardNo(rs.getString(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_CARD_NO]));
      entTransaksiPayment.setBankName(rs.getString(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_BANK_NAME]));
      entTransaksiPayment.setValidateDate(rs.getDate(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_VALIDATE_DATE]));
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
      String sql = "SELECT * FROM " + TBL_TRANSAKSIPAYMENT;
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
        TransaksiPayment entTransaksiPayment = new TransaksiPayment();
        resultToObject(rs, entTransaksiPayment);
        lists.add(entTransaksiPayment);
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

  public static boolean checkOID(long entTransaksiPaymentId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_TRANSAKSIPAYMENT + " WHERE "
              + PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_TRANSAKSI_PAYMENT_ID] + " = " + entTransaksiPaymentId;
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
      String sql = "SELECT COUNT(" + PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_TRANSAKSI_PAYMENT_ID] + ") FROM " + TBL_TRANSAKSIPAYMENT;
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
          TransaksiPayment entTransaksiPayment = (TransaksiPayment) list.get(ls);
          if (oid == entTransaksiPayment.getOID()) {
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
