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

public class PstTransaksiPayment extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_SEDANA_TRANSAKSI_PAYMENT = "sedana_transaksi_payment";
  public static final int FLD_TRANSAKSI_PAYMENT_ID = 0;
  public static final int FLD_PAYMENT_SYSTEM_ID = 1;
  public static final int FLD_TRANSAKSI_ID = 2;
  public static final int FLD_JUMLAH = 3;

  public static String[] fieldNames = {
    "TRANSAKSI_PAYMENT_ID",
    "PAYMENT_SYSTEM_ID",
    "TRANSAKSI_ID",
    "JUMLAH"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG + TYPE_FK,
    TYPE_LONG + TYPE_FK,
    TYPE_LONG
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
    return TBL_SEDANA_TRANSAKSI_PAYMENT;
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
      TransaksiPayment entSedanaTransaksiPayment = new TransaksiPayment();
      PstTransaksiPayment pstSedanaTransaksiPayment = new PstTransaksiPayment(oid);
      entSedanaTransaksiPayment.setOID(oid);
      entSedanaTransaksiPayment.setPaymentSystemId(pstSedanaTransaksiPayment.getLong(FLD_PAYMENT_SYSTEM_ID));
      entSedanaTransaksiPayment.setTransaksiId(pstSedanaTransaksiPayment.getLong(FLD_TRANSAKSI_ID));
      entSedanaTransaksiPayment.setJumlah(pstSedanaTransaksiPayment.getdouble(FLD_JUMLAH));
      return entSedanaTransaksiPayment;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTransaksiPayment(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    TransaksiPayment entSedanaTransaksiPayment = fetchExc(entity.getOID());
    entity = (Entity) entSedanaTransaksiPayment;
    return entSedanaTransaksiPayment.getOID();
  }

  public static synchronized long updateExc(TransaksiPayment entSedanaTransaksiPayment) throws DBException {
    try {
      if (entSedanaTransaksiPayment.getOID() != 0) {
        PstTransaksiPayment pstSedanaTransaksiPayment = new PstTransaksiPayment(entSedanaTransaksiPayment.getOID());
        pstSedanaTransaksiPayment.setLong(FLD_PAYMENT_SYSTEM_ID, entSedanaTransaksiPayment.getPaymentSystemId());
        pstSedanaTransaksiPayment.setLong(FLD_TRANSAKSI_ID, entSedanaTransaksiPayment.getTransaksiId());
        pstSedanaTransaksiPayment.setDouble(FLD_JUMLAH, entSedanaTransaksiPayment.getJumlah());
        pstSedanaTransaksiPayment.update();
        return entSedanaTransaksiPayment.getOID();
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
      PstTransaksiPayment pstSedanaTransaksiPayment = new PstTransaksiPayment(oid);
      pstSedanaTransaksiPayment.delete();
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

  public static synchronized long insertExc(TransaksiPayment entSedanaTransaksiPayment) throws DBException {
    try {
      PstTransaksiPayment pstSedanaTransaksiPayment = new PstTransaksiPayment(0);
      pstSedanaTransaksiPayment.setLong(FLD_PAYMENT_SYSTEM_ID, entSedanaTransaksiPayment.getPaymentSystemId());
      pstSedanaTransaksiPayment.setLong(FLD_TRANSAKSI_ID, entSedanaTransaksiPayment.getTransaksiId());
      pstSedanaTransaksiPayment.setDouble(FLD_JUMLAH, entSedanaTransaksiPayment.getJumlah());
      pstSedanaTransaksiPayment.insert();
      entSedanaTransaksiPayment.setOID(pstSedanaTransaksiPayment.getLong(FLD_TRANSAKSI_PAYMENT_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstTransaksiPayment(0), DBException.UNKNOWN);
    }
    return entSedanaTransaksiPayment.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((TransaksiPayment) entity);
  }

  public static void resultToObject(ResultSet rs, TransaksiPayment entSedanaTransaksiPayment) {
    try {
      entSedanaTransaksiPayment.setOID(rs.getLong(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_TRANSAKSI_PAYMENT_ID]));
      entSedanaTransaksiPayment.setPaymentSystemId(rs.getLong(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_PAYMENT_SYSTEM_ID]));
      entSedanaTransaksiPayment.setTransaksiId(rs.getLong(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_TRANSAKSI_ID]));
      entSedanaTransaksiPayment.setJumlah(rs.getDouble(PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_JUMLAH]));
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
      String sql = "SELECT * FROM " + TBL_SEDANA_TRANSAKSI_PAYMENT;
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
        TransaksiPayment entSedanaTransaksiPayment = new TransaksiPayment();
        resultToObject(rs, entSedanaTransaksiPayment);
        lists.add(entSedanaTransaksiPayment);
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

  public static boolean checkOID(long entSedanaTransaksiPaymentId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_SEDANA_TRANSAKSI_PAYMENT + " WHERE "
              + PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_TRANSAKSI_PAYMENT_ID] + " = " + entSedanaTransaksiPaymentId;
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
      String sql = "SELECT COUNT(" + PstTransaksiPayment.fieldNames[PstTransaksiPayment.FLD_TRANSAKSI_PAYMENT_ID] + ") FROM " + TBL_SEDANA_TRANSAKSI_PAYMENT;
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
          TransaksiPayment entSedanaTransaksiPayment = (TransaksiPayment) list.get(ls);
          if (oid == entSedanaTransaksiPayment.getOID()) {
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
