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

public class PstCashTellerBalance extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CASHTELLERBALANCE = "cash_teller_balance";
    public static final int FLD_CASH_BALANCE_ID = 0;
    public static final int FLD_TELLER_SHIFT_ID = 1;
    public static final int FLD_CURRENCY_ID = 2;
    public static final int FLD_TYPE = 3;
    public static final int FLD_BALANCE_DATE = 4;
    public static final int FLD_BALANCE_VALUE = 5;
    public static final int FLD_SHOULD_VALUE = 6;
    public static final int FLD_PAYMENT_SYSTEM_ID = 7;

    public static String[] fieldNames = {
        "CASH_BALANCE_ID",
        "TELLER_SHIFT_ID",
        "CURRENCY_ID",
        "TYPE",
        "BALANCE_DATE",
        "BALANCE_VALUE",
        "SHOULD_VALUE",
        "PAYMENT_SYSTEM_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG
    };

    public PstCashTellerBalance() {
    }

    public PstCashTellerBalance(int i) throws DBException {
        super(new PstCashTellerBalance());
    }

    public PstCashTellerBalance(String sOid) throws DBException {
        super(new PstCashTellerBalance(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstCashTellerBalance(long lOid) throws DBException {
        super(new PstCashTellerBalance(0));
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
        return TBL_CASHTELLERBALANCE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCashTellerBalance().getClass().getName();
    }

    public static CashTellerBalance fetchExc(long oid) throws DBException {
        try {
            CashTellerBalance entCashTellerBalance = new CashTellerBalance();
            PstCashTellerBalance pstCashTellerBalance = new PstCashTellerBalance(oid);
            entCashTellerBalance.setOID(oid);
            entCashTellerBalance.setTellerShiftId(pstCashTellerBalance.getLong(FLD_TELLER_SHIFT_ID));
            entCashTellerBalance.setCurrencyId(pstCashTellerBalance.getLong(FLD_CURRENCY_ID));
            entCashTellerBalance.setType(pstCashTellerBalance.getInt(FLD_TYPE));
            entCashTellerBalance.setBalanceDate(pstCashTellerBalance.getDate(FLD_BALANCE_DATE));
            entCashTellerBalance.setBalanceValue(pstCashTellerBalance.getdouble(FLD_BALANCE_VALUE));
            entCashTellerBalance.setShouldValue(pstCashTellerBalance.getdouble(FLD_SHOULD_VALUE));
            entCashTellerBalance.setPaymentSystemId(pstCashTellerBalance.getlong(FLD_PAYMENT_SYSTEM_ID));
            return entCashTellerBalance;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashTellerBalance(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        CashTellerBalance entCashTellerBalance = fetchExc(entity.getOID());
        entity = (Entity) entCashTellerBalance;
        return entCashTellerBalance.getOID();
    }

    public static synchronized long updateExc(CashTellerBalance entCashTellerBalance) throws DBException {
        try {
            if (entCashTellerBalance.getOID() != 0) {
                PstCashTellerBalance pstCashTellerBalance = new PstCashTellerBalance(entCashTellerBalance.getOID());
                pstCashTellerBalance.setLong(FLD_TELLER_SHIFT_ID, entCashTellerBalance.getTellerShiftId());
                pstCashTellerBalance.setLong(FLD_CURRENCY_ID, entCashTellerBalance.getCurrencyId());
                pstCashTellerBalance.setInt(FLD_TYPE, entCashTellerBalance.getType());
                pstCashTellerBalance.setDate(FLD_BALANCE_DATE, entCashTellerBalance.getBalanceDate());
                pstCashTellerBalance.setDouble(FLD_BALANCE_VALUE, entCashTellerBalance.getBalanceValue());
                pstCashTellerBalance.setDouble(FLD_SHOULD_VALUE, entCashTellerBalance.getShouldValue());
                pstCashTellerBalance.setLong(FLD_PAYMENT_SYSTEM_ID, entCashTellerBalance.getPaymentSystemId());
                pstCashTellerBalance.update();
                return entCashTellerBalance.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashTellerBalance(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((CashTellerBalance) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstCashTellerBalance pstCashTellerBalance = new PstCashTellerBalance(oid);
            pstCashTellerBalance.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashTellerBalance(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(CashTellerBalance entCashTellerBalance) throws DBException {
        try {
            PstCashTellerBalance pstCashTellerBalance = new PstCashTellerBalance(0);
            pstCashTellerBalance.setLong(FLD_TELLER_SHIFT_ID, entCashTellerBalance.getTellerShiftId());
            pstCashTellerBalance.setLong(FLD_CURRENCY_ID, entCashTellerBalance.getCurrencyId());
            pstCashTellerBalance.setInt(FLD_TYPE, entCashTellerBalance.getType());
            pstCashTellerBalance.setDate(FLD_BALANCE_DATE, entCashTellerBalance.getBalanceDate());
            pstCashTellerBalance.setDouble(FLD_BALANCE_VALUE, entCashTellerBalance.getBalanceValue());
            pstCashTellerBalance.setDouble(FLD_SHOULD_VALUE, entCashTellerBalance.getShouldValue());
            pstCashTellerBalance.setLong(FLD_PAYMENT_SYSTEM_ID, entCashTellerBalance.getPaymentSystemId());
            pstCashTellerBalance.insert();
            entCashTellerBalance.setOID(pstCashTellerBalance.getlong(FLD_CASH_BALANCE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashTellerBalance(0), DBException.UNKNOWN);
        }
        return entCashTellerBalance.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((CashTellerBalance) entity);
    }

    public static void resultToObject(ResultSet rs, CashTellerBalance entCashTellerBalance) {
        try {
            entCashTellerBalance.setOID(rs.getLong(PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_CASH_BALANCE_ID]));
            entCashTellerBalance.setTellerShiftId(rs.getLong(PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_TELLER_SHIFT_ID]));
            entCashTellerBalance.setCurrencyId(rs.getLong(PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_CURRENCY_ID]));
            entCashTellerBalance.setType(rs.getInt(PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_TYPE]));
            entCashTellerBalance.setBalanceDate(rs.getTimestamp(PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_BALANCE_DATE]));
            entCashTellerBalance.setBalanceValue(rs.getDouble(PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_BALANCE_VALUE]));
            entCashTellerBalance.setShouldValue(rs.getDouble(PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_SHOULD_VALUE]));
            entCashTellerBalance.setPaymentSystemId(rs.getLong(PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_PAYMENT_SYSTEM_ID]));
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
            String sql = "SELECT * FROM " + TBL_CASHTELLERBALANCE;
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
                CashTellerBalance entCashTellerBalance = new CashTellerBalance();
                resultToObject(rs, entCashTellerBalance);
                lists.add(entCashTellerBalance);
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

    public static boolean checkOID(long entCashTellerBalanceId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CASHTELLERBALANCE + " WHERE "
                    + PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_CASH_BALANCE_ID] + " = " + entCashTellerBalanceId;
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
            String sql = "SELECT COUNT(" + PstCashTellerBalance.fieldNames[PstCashTellerBalance.FLD_CASH_BALANCE_ID] + ") FROM " + TBL_CASHTELLERBALANCE;
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
                    CashTellerBalance entCashTellerBalance = (CashTellerBalance) list.get(ls);
                    if (oid == entCashTellerBalance.getOID()) {
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
