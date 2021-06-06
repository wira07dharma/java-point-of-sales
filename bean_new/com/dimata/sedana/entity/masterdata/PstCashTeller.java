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
import com.dimata.qdep.entity.*;
import com.dimata.posbo.db.*;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.util.Vector;

public class PstCashTeller extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_CASH_TELLER = "cash_teller";
  public static final int FLD_TELLER_SHIFT_ID = 0;
  public static final int FLD_MASTER_LOKET_ID = 1;
  public static final int FLD_APP_USER_ID = 2;
  public static final int FLD_OPEN_DATE = 3;
  public static final int FLD_SPV_OPEN_ID = 4;
  public static final int FLD_SPV_OPEN_NAME = 5;
  public static final int FLD_SPV_CLOSE_ID = 6;
  public static final int FLD_SPV_CLOSE_NAME = 7;
  public static final int FLD_SHIFT_ID = 8;
  public static final int FLD_CLOSE_DATE = 9;
  public static final int FLD_STATUS = 10;

  public static String[] fieldNames = {
    "TELLER_SHIFT_ID",
    "MASTER_LOKET_ID",
    "APP_USER_ID",
    "OPEN_DATE",
    "SPV_OPEN_ID",
    "SPV_OPEN_NAME",
    "SPV_CLOSE_ID",
    "SPV_CLOSE_NAME",
    "SHIFT_ID",
    "CLOSE_DATE",
    "STATUS"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_DATE,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_LONG,
    TYPE_DATE,
    TYPE_INT
  };

  public PstCashTeller() {
  }

  public PstCashTeller(int i) throws DBException {
    super(new PstCashTeller());
  }

  public PstCashTeller(String sOid) throws DBException {
    super(new PstCashTeller(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstCashTeller(long lOid) throws DBException {
    super(new PstCashTeller(0));
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
    return TBL_CASH_TELLER;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstCashTeller().getClass().getName();
  }

  public static CashTeller fetchExc(long oid) throws DBException {
    try {
      CashTeller entCashTeller = new CashTeller();
      PstCashTeller pstCashTeller = new PstCashTeller(oid);
      entCashTeller.setOID(oid);
      entCashTeller.setMasterLoketId(pstCashTeller.getlong(FLD_MASTER_LOKET_ID));
      entCashTeller.setAppUserId(pstCashTeller.getlong(FLD_APP_USER_ID));
      entCashTeller.setOpenDate(pstCashTeller.getDate(FLD_OPEN_DATE));
      entCashTeller.setSpvOpenId(pstCashTeller.getlong(FLD_SPV_OPEN_ID));
      entCashTeller.setSpvOpenName(pstCashTeller.getString(FLD_SPV_OPEN_NAME));
      entCashTeller.setSpvCloseId(pstCashTeller.getlong(FLD_SPV_CLOSE_ID));
      entCashTeller.setSpvCloseName(pstCashTeller.getString(FLD_SPV_CLOSE_NAME));
      entCashTeller.setShiftId(pstCashTeller.getlong(FLD_SHIFT_ID));
      entCashTeller.setCloseDate(pstCashTeller.getDate(FLD_CLOSE_DATE));
      entCashTeller.setStatus(pstCashTeller.getInt(FLD_STATUS));
      return entCashTeller;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstCashTeller(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    CashTeller entCashTeller = fetchExc(entity.getOID());
    entity = (Entity) entCashTeller;
    return entCashTeller.getOID();
  }

  public static synchronized long updateExc(CashTeller entCashTeller) throws DBException {
    try {
      if (entCashTeller.getOID() != 0) {
        PstCashTeller pstCashTeller = new PstCashTeller(entCashTeller.getOID());
        pstCashTeller.setLong(FLD_MASTER_LOKET_ID, entCashTeller.getMasterLoketId());
        pstCashTeller.setLong(FLD_APP_USER_ID, entCashTeller.getAppUserId());
        pstCashTeller.setDate(FLD_OPEN_DATE, entCashTeller.getOpenDate());
        pstCashTeller.setLong(FLD_SPV_OPEN_ID, entCashTeller.getSpvOpenId());
        pstCashTeller.setString(FLD_SPV_OPEN_NAME, entCashTeller.getSpvOpenName());
        pstCashTeller.setLong(FLD_SPV_CLOSE_ID, entCashTeller.getSpvCloseId());
        pstCashTeller.setString(FLD_SPV_CLOSE_NAME, entCashTeller.getSpvCloseName());
        pstCashTeller.setLong(FLD_SHIFT_ID, entCashTeller.getShiftId());
        pstCashTeller.setDate(FLD_CLOSE_DATE, entCashTeller.getCloseDate());
        pstCashTeller.setInt(FLD_STATUS, entCashTeller.getStatus());
        pstCashTeller.update();
        return entCashTeller.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstCashTeller(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((CashTeller) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstCashTeller pstCashTeller = new PstCashTeller(oid);
      pstCashTeller.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstCashTeller(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(CashTeller entCashTeller) throws DBException {
    try {
      PstCashTeller pstCashTeller = new PstCashTeller(0);
      pstCashTeller.setLong(FLD_MASTER_LOKET_ID, entCashTeller.getMasterLoketId());
      pstCashTeller.setLong(FLD_APP_USER_ID, entCashTeller.getAppUserId());
      pstCashTeller.setDate(FLD_OPEN_DATE, new java.util.Date());
      pstCashTeller.setLong(FLD_SPV_OPEN_ID, entCashTeller.getSpvOpenId());
      pstCashTeller.setString(FLD_SPV_OPEN_NAME, entCashTeller.getSpvOpenName());
      pstCashTeller.setLong(FLD_SPV_CLOSE_ID, entCashTeller.getSpvCloseId());
      pstCashTeller.setString(FLD_SPV_CLOSE_NAME, entCashTeller.getSpvCloseName());
      pstCashTeller.setLong(FLD_SHIFT_ID, entCashTeller.getShiftId());
      pstCashTeller.setDate(FLD_CLOSE_DATE, entCashTeller.getCloseDate());
      pstCashTeller.setInt(FLD_STATUS, entCashTeller.getStatus());
      pstCashTeller.insert();
      entCashTeller.setOID(pstCashTeller.getlong(FLD_TELLER_SHIFT_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstCashTeller(0), DBException.UNKNOWN);
    }
    return entCashTeller.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((CashTeller) entity);
  }

  public static void resultToObject(ResultSet rs, CashTeller entCashTeller) {
    try {
      entCashTeller.setOID(rs.getLong(PstCashTeller.fieldNames[PstCashTeller.FLD_TELLER_SHIFT_ID]));
      entCashTeller.setMasterLoketId(rs.getLong(PstCashTeller.fieldNames[PstCashTeller.FLD_MASTER_LOKET_ID]));
      entCashTeller.setAppUserId(rs.getLong(PstCashTeller.fieldNames[PstCashTeller.FLD_APP_USER_ID]));
      rs.getString(PstCashTeller.fieldNames[PstCashTeller.FLD_OPEN_DATE]);
      entCashTeller.setOpenDate(Formater.formatDate(rs.getString(PstCashTeller.fieldNames[PstCashTeller.FLD_OPEN_DATE]), "yyyy-MM-dd HH:mm:ss"));
      entCashTeller.setSpvOpenId(rs.getLong(PstCashTeller.fieldNames[PstCashTeller.FLD_SPV_OPEN_ID]));
      entCashTeller.setSpvOpenName(rs.getString(PstCashTeller.fieldNames[PstCashTeller.FLD_SPV_OPEN_NAME]));
      entCashTeller.setSpvCloseId(rs.getLong(PstCashTeller.fieldNames[PstCashTeller.FLD_SPV_CLOSE_ID]));
      entCashTeller.setSpvCloseName(rs.getString(PstCashTeller.fieldNames[PstCashTeller.FLD_SPV_CLOSE_NAME]));
      entCashTeller.setShiftId(rs.getLong(PstCashTeller.fieldNames[PstCashTeller.FLD_SHIFT_ID]));
      entCashTeller.setCloseDate(Formater.formatDate(rs.getString(PstCashTeller.fieldNames[PstCashTeller.FLD_CLOSE_DATE]), "yyyy-MM-dd HH:mm:ss"));
      entCashTeller.setStatus(rs.getInt(PstCashTeller.fieldNames[PstCashTeller.FLD_STATUS]));
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
      String sql = "SELECT * FROM " + TBL_CASH_TELLER;
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
        CashTeller entCashTeller = new CashTeller();
        resultToObject(rs, entCashTeller);
        lists.add(entCashTeller);
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

  public static boolean checkOID(long entCashTellerId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_CASH_TELLER + " WHERE "
              + PstCashTeller.fieldNames[PstCashTeller.FLD_TELLER_SHIFT_ID] + " = " + entCashTellerId;
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
      String sql = "SELECT COUNT(" + PstCashTeller.fieldNames[PstCashTeller.FLD_TELLER_SHIFT_ID] + ") FROM " + TBL_CASH_TELLER;
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
          CashTeller entCashTeller = (CashTeller) list.get(ls);
          if (oid == entCashTeller.getOID()) {
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
  
  public static long getActiveTellerShiftIdByUserId(long userOID) {
    String sql = "SELECT `TELLER_SHIFT_ID` FROM `cash_teller` WHERE STATUS = 2 AND (SPV_CLOSE_ID = 0 OR `SPV_CLOSE_ID` IS NULL) AND APP_USER_ID = "+userOID+" GROUP BY `TELLER_SHIFT_ID`";
    DBResultSet dbrs = null;
    long ID = 0;
    try {
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      if (rs.next()) {
        ID = rs.getLong(1);
      }
    } catch (Exception e) {
    } finally {
      DBResultSet.close(dbrs);
    }

    return ID;
  }

  public static String getTellerShiftQuery() {
    return getTellerShiftQuery("");
  }

  public static String getTellerShiftQuery(String Where) {
    return "SELECT"
            + " c.`TELLER_SHIFT_ID`,"
            + " c.`APP_USER_ID`,"
            + " c.`OPEN_DATE`,"
            + " c.`SPV_OPEN_NAME`,"
            + " c.`SPV_CLOSE_NAME`,"
            + " c.`CLOSE_DATE`,"
            + " c.`STATUS`,"
            + " user.`login_id`,"
            + " user.`full_name`,"
            // >> updated by dewok 20190121, data yg muncul lebih spesifik
            //+ " opening.`BALANCE_VALUE` AS `OPENING_VALUE`,"
            //+ " closing.`BALANCE_VALUE` AS `CLOSING_VALUE`,"
            //+ " SUM(trx.`KREDIT`-trx.`DEBET`) AS `COMPUTED_VALUE`"
            // <<
            // >> added by dewok 20190121
            + " IFNULL(opening.`BALANCE_VALUE`,0) AS `OPENING_VALUE`, "
            + " IFNULL(SUM(trx.`DEBET`),0) AS DEBET, "
            + " IFNULL(SUM(trx.`KREDIT`),0) AS KREDIT, "
            + " IFNULL(SUM(trx.`DEBET` - trx.`KREDIT`),0) AS `COMPUTED_VALUE`, "
            + " IFNULL(opening.`BALANCE_VALUE`,0) + IFNULL(SUM(trx.`DEBET` - trx.`KREDIT`),0) AS `COMPUTED_CLOSING_VALUE`, "
            + " IFNULL(SUM(closing.`BALANCE_VALUE`),0) AS `CLOSING_VALUE`, "
            + " IFNULL(opening.`BALANCE_VALUE`,0) + IFNULL(SUM(trx.`DEBET` - trx.`KREDIT`),0) - IFNULL(SUM(closing.`BALANCE_VALUE`),0) AS SELISIH "
            // <<
            + " FROM `cash_teller` AS `c`"
            + " JOIN `aiso_app_user` AS `user`"
            + " ON c.`APP_USER_ID` = user.`user_id`"
            + " LEFT JOIN cash_teller_balance AS `opening`"
            + " ON opening.`TELLER_SHIFT_ID` = c.`TELLER_SHIFT_ID`"
            + " AND opening.`TYPE` = 0"
            + " LEFT JOIN cash_teller_balance AS `closing`"
            + " ON closing.`TELLER_SHIFT_ID` = c.`TELLER_SHIFT_ID`"
            + " AND closing.`TYPE` = 1"
            + " LEFT JOIN ("
            + " SELECT"
            + " s.`TELLER_SHIFT_ID`,"
            + " d.`DEBET`,"
            + " d.`KREDIT`"
            + " FROM sedana_transaksi s"
            + " JOIN sedana_detail_transaksi d USING (transaksi_id)"
            + " UNION"
            + " SELECT "
            + " t.TELLER_SHIFT_ID,"
            + " 0 AS DEBET,"
            + " a.`JUMLAH_ANGSURAN` AS KREDIT "
            + " FROM "
            + " aiso_angsuran AS a "
            + " INNER JOIN sedana_transaksi AS t "
            + " ON t.`TRANSAKSI_ID` = a.`TRANSAKSI_ID`) AS trx"
            + " ON c.`TELLER_SHIFT_ID` = trx.`TELLER_SHIFT_ID`" + (Where.equals("") ? "" : " WHERE " + Where + " ") + ""
            + " GROUP BY c.`TELLER_SHIFT_ID`"
            + " ORDER BY c.`STATUS` asc, c.`OPEN_DATE` DESC";
  }

  public static Vector<TellerShift> getTellerShift() {
    Vector<TellerShift> d = new Vector<TellerShift>();

    DBResultSet dbrs = null;
    try {
      String sql = getTellerShiftQuery();
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      while (rs.next()) {
        TellerShift ts = new TellerShift();
        ts.setOpenDate(Formater.formatDate(rs.getString("OPEN_DATE"), "yyyy-MM-dd HH:mm:ss"));
        ts.setCloseDate(Formater.formatDate(rs.getString("CLOSE_DATE"), "yyyy-MM-dd HH:mm:ss"));
        ts.setClosingValue(rs.getDouble("CLOSING_VALUE"));
        ts.setComputedValue(rs.getDouble("COMPUTED_VALUE"));
        ts.setOpeningValue(rs.getDouble("OPENING_VALUE"));
        ts.setStatusVal(rs.getInt("STATUS"));
        ts.setName(rs.getString("full_name"));
        ts.setUsername(rs.getString("login_id"));
        ts.setStatus((ts.getStatusVal() > 2) ? "CLOSED" : "OPEN");
        ts.setSpvOpenName(rs.getString("SPV_OPEN_NAME"));
        ts.setSpvCloseName(rs.getString("SPV_CLOSE_NAME"));
        ts.setOID(rs.getLong("TELLER_SHIFT_ID"));
        ts.setAppUserId(rs.getLong("APP_USER_ID"));
        d.add(ts);
      }
      rs.close();
    } catch (Exception e) {
    } finally {
      DBResultSet.close(dbrs);
    }

    return d;
  }

  public static double getComputedBalanceByTellerShiftId(long tellerShiftId) {
    String sql = "SELECT SUM(d.`KREDIT`-d.`DEBET`) AS `saldo` FROM sedana_transaksi s JOIN sedana_detail_transaksi d USING (transaksi_id) WHERE s.`TELLER_SHIFT_ID` = " + tellerShiftId + " GROUP BY s.`TELLER_SHIFT_ID`";
    DBResultSet dbrs = null;
    double balance = 0;
    try {
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      while (rs.next()) {
        balance = rs.getDouble("saldo");
        break;
      }
    } catch (Exception e) {
    } finally {
      DBResultSet.close(dbrs);
    }

    return balance;
  }

  public static TellerShift getTellerShiftById(long tellerShiftId) {
    TellerShift ts = new TellerShift();

    DBResultSet dbrs = null;
    try {
      String sql = getTellerShiftQuery("c.`TELLER_SHIFT_ID` = " + tellerShiftId);
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      while (rs.next()) {
        ts.setOpenDate(Formater.formatDate(rs.getString("OPEN_DATE"), "yyyy-MM-dd HH:mm:ss"));
        ts.setCloseDate(Formater.formatDate(rs.getString("CLOSE_DATE"), "yyyy-MM-dd HH:mm:ss"));
        ts.setClosingValue(rs.getDouble("CLOSING_VALUE"));
        ts.setComputedValue(rs.getDouble("COMPUTED_VALUE"));
        ts.setOpeningValue(rs.getDouble("OPENING_VALUE"));
        ts.setStatusVal(rs.getInt("STATUS"));
        ts.setName(rs.getString("full_name"));
        ts.setUsername(rs.getString("login_id"));
        ts.setStatus((ts.getStatusVal() > 2) ? "CLOSED" : "OPEN");
        ts.setSpvOpenName(rs.getString("SPV_OPEN_NAME"));
        ts.setSpvCloseName(rs.getString("SPV_CLOSE_NAME"));
        ts.setOID(rs.getLong("TELLER_SHIFT_ID"));
        break;
      }
      rs.close();
    } catch (Exception e) {
    } finally {
      DBResultSet.close(dbrs);
    }

    return ts;
  }
  
  public static boolean isClosingTime(long oidUser) {
    boolean r = false;
    String sql = "select COUNT(0) from `cash_teller` c JOIN `sedana_shift` s ON s.`SHIFT_ID`=c.`SHIFT_ID` AND TIME(NOW())>=TIME(s.`END_TIME`) where c.`CLOSE_DATE` IS NULL AND c.`APP_USER_ID` = "+oidUser+" AND DATE(NOW())>=DATE(c.`OPEN_DATE`)";
    DBResultSet dbrs = null;
    try {
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      if (rs.next()) {
        r = rs.getInt(1)>0;
      }
      rs.close();
    } catch (Exception e) {
      System.err.println("isClosingTime: "+e);
    } finally {
      DBResultSet.close(dbrs);
    }
    return r;
  }

}
