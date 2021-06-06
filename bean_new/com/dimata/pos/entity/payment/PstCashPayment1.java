/*
 * PstCashPayment.java
 *
 * Created on May 23, 2003, 2:52 PM
 */
package com.dimata.pos.entity.payment;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

import com.dimata.common.entity.payment.*;
/* package java */
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* package qdep */
import java.sql.ResultSet;

/* package cashier */
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.util.Formater;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.entity.search.SrcSaleReport;

public class PstCashPayment1 extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    //public static final String TBL_PAYMENT = "CASH_PAYMENT";

    public static final String TBL_PAYMENT = "cash_payment";
    public static final int FLD_PAYMENT_ID = 0;
    public static final int FLD_CURRENCY_ID = 1;
    public static final int FLD_BILL_MAIN_ID = 2;
    public static final int FLD_PAY_TYPE = 3;
    public static final int FLD_RATE = 4;
    public static final int FLD_AMOUNT = 5;
    public static final int FLD_PAY_DATETIME = 6;
    public static final int FLD_PAYMENT_STATUS = 7;
    public static final String[] fieldNames = {
        "CASH_PAYMENT_ID",
        "CURRENCY_ID",
        "CASH_BILL_MAIN_ID",
        "PAY_TYPE",
        "RATE",
        "AMOUNT",
        "PAY_DATETIME",
        "PAYMENT_STATUS"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_INT
    };
    //diskon type
    public static final int DISKON_PROSEN = 0;
    public static final int DISKON_RUPIAH = 1;
    public static final int UPDATE_STATUS_NONE = 0;
    public static final int UPDATE_STATUS_INSERTED = 1;
    public static final int UPDATE_STATUS_UPDATED = 2;
    public static final int UPDATE_STATUS_DELETED = 3;
    public static final String[] diskonType = {
        "%",
        "Rp"
    };
    //payment type
    public static final int CASH = 0;
    public static final int CARD = 1;
    public static final int CHEQUE = 2;
    public static final int DEBIT = 3;
    public static final int RETURN = 4;
    public static final int DP = 5;
    public static final int VOUCHER = 6;
    public static final int DISCOUNT = 7;
    public static final String[] paymentType = {
        "CASH",
        "CREDIT CARD",
        "CHEQUE",
        "DEBIT CARD",
        "RETURN",
        "DP",
        "VOUCHER",
        "DISCOUNT"
    };

    /** Creates new PstCashPayment */
    public PstCashPayment1() {
    }

    public PstCashPayment1(int i) throws DBException {
        super(new PstCashPayment1());
    }

    public PstCashPayment1(String sOid) throws DBException {
        super(new PstCashPayment1(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstCashPayment1(long lOid) throws DBException {
        super(new PstCashPayment1(0));
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
        return TBL_PAYMENT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCashPayment1().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        CashPayments1 cashPayment1 = fetchExc(ent.getOID());
        ent = (Entity) cashPayment1;
        return cashPayment1.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((CashPayments1) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((CashPayments1) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static CashPayments1 fetchExc(long oid) throws DBException {
        try {
            CashPayments1 cashPayment1 = new CashPayments1();
            PstCashPayment1 pstCashPayment1 = new PstCashPayment1(oid);
            cashPayment1.setOID(oid);
            cashPayment1.setBillMainId(pstCashPayment1.getlong(FLD_BILL_MAIN_ID));
            cashPayment1.setCurrencyId(pstCashPayment1.getlong(FLD_CURRENCY_ID));
            cashPayment1.setPaymentType(pstCashPayment1.getlong(FLD_PAY_TYPE));
            cashPayment1.setRate(pstCashPayment1.getdouble(FLD_RATE));
            cashPayment1.setAmount(pstCashPayment1.getdouble(FLD_AMOUNT));
            cashPayment1.setPayDateTime(pstCashPayment1.getDate(FLD_PAY_DATETIME));
            cashPayment1.setPaymentStatus(pstCashPayment1.getInt(FLD_PAYMENT_STATUS));

            return cashPayment1;
        } catch (DBException dbe) {
            System.out.println(dbe.toString());
            throw dbe;
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new DBException(new PstCashPayment1(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(CashPayments1 cashPayment1) throws DBException {
        try {
            PstCashPayment1 pstCashPayment1 = new PstCashPayment1(0);
            pstCashPayment1.setLong(FLD_BILL_MAIN_ID, cashPayment1.getBillMainId());
            pstCashPayment1.setLong(FLD_CURRENCY_ID, cashPayment1.getCurrencyId());
            pstCashPayment1.setLong(FLD_PAY_TYPE, cashPayment1.getPaymentType());
            pstCashPayment1.setDouble(FLD_RATE, cashPayment1.getRate());
            pstCashPayment1.setDouble(FLD_AMOUNT, cashPayment1.getAmount());
            pstCashPayment1.setDate(FLD_PAY_DATETIME, cashPayment1.getPayDateTime());
            pstCashPayment1.setInt(FLD_PAYMENT_STATUS, cashPayment1.getPaymentStatus());

            pstCashPayment1.insert();
            cashPayment1.setOID(pstCashPayment1.getlong(FLD_PAYMENT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashPayment1(0), DBException.UNKNOWN);
        }
        return cashPayment1.getOID();
    }

    public static long insertExcByOid(CashPayments1 cashPayment1) throws DBException {
        try {
            PstCashPayment1 pstCashPayment1 = new PstCashPayment1(0);
            pstCashPayment1.setLong(FLD_BILL_MAIN_ID, cashPayment1.getBillMainId());
            pstCashPayment1.setLong(FLD_CURRENCY_ID, cashPayment1.getCurrencyId());
            pstCashPayment1.setLong(FLD_PAY_TYPE, cashPayment1.getPaymentType());
            pstCashPayment1.setDouble(FLD_RATE, cashPayment1.getRate());
            pstCashPayment1.setDouble(FLD_AMOUNT, cashPayment1.getAmount());
            pstCashPayment1.setDate(FLD_PAY_DATETIME, cashPayment1.getPayDateTime());
            pstCashPayment1.setInt(FLD_PAYMENT_STATUS, cashPayment1.getPaymentStatus());

            pstCashPayment1.insertByOid(cashPayment1.getOID());
            //cashPayment.setOID(pstCashPayment.getlong(FLD_PAYMENT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashPayment1(0), DBException.UNKNOWN);
        }
        return cashPayment1.getOID();
    }

    public static long updateExc(CashPayments1 cashPayment1) throws DBException {
        try {
            if (cashPayment1.getOID() != 0) {
                PstCashPayment1 pstCashPayment1 = new PstCashPayment1(cashPayment1.getOID());
                pstCashPayment1.setLong(FLD_BILL_MAIN_ID, cashPayment1.getBillMainId());
                pstCashPayment1.setLong(FLD_CURRENCY_ID, cashPayment1.getCurrencyId());
                pstCashPayment1.setLong(FLD_PAY_TYPE, cashPayment1.getPaymentType());
                pstCashPayment1.setDouble(FLD_RATE, cashPayment1.getRate());
                pstCashPayment1.setDouble(FLD_AMOUNT, cashPayment1.getAmount());
                pstCashPayment1.setDate(FLD_PAY_DATETIME, cashPayment1.getPayDateTime());
                pstCashPayment1.setInt(FLD_PAYMENT_STATUS, cashPayment1.getPaymentStatus());

                pstCashPayment1.update();
                return cashPayment1.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashPayment1(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstCashPayment1 pstCashPayment1 = new PstCashPayment1(oid);
            pstCashPayment1.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashPayment1(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static int getCount(String whereClause) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[FLD_PAYMENT_ID] + ") AS CNT FROM " + TBL_PAYMENT;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PAYMENT;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CashPayments1 cashPayment1 = new CashPayments1();
                resultToObject(rs, cashPayment1);
                lists.add(cashPayment1);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static double getPembayaranCash(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT]
                    + "* CP." + fieldNames[FLD_RATE] + ")"
                    + " FROM " + TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE " + fieldNames[FLD_PAY_TYPE] + " != 5"
                    + " AND " + fieldNames[FLD_PAY_TYPE] + " !=4"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " =0"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]
                    + " = " + oidCashCashier;

            /*if (whereClause != null && whereClause.length() > 0)
            sql = sql + " WHERE " + whereClause;*/

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

    public static double getPembayaranKredit(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT]
                    + "* CP." + fieldNames[FLD_RATE] + ")"
                    + " FROM " + TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE " + fieldNames[FLD_PAY_TYPE] + " != 5"
                    + " AND " + fieldNames[FLD_PAY_TYPE] + " !=4"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " =0"
                    + //" AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=1" +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=1"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]
                    + " = " + oidCashCashier;

            /*if (whereClause != null && whereClause.length() > 0)
            sql = sql + " WHERE " + whereClause;*/

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

    //List Cash Per Detail Pembayaran
    public static Vector listDetailBayar(int limitStart, int recordToGet, String whereClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + fieldNames[FLD_PAY_TYPE]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
                    + " , CP." + fieldNames[FLD_RATE]
                    + " , CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " , SUM(CP." + fieldNames[FLD_AMOUNT] + ")" + "AS SUM_AMOUNT" + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT]
                    + " FROM " + TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT"
                    + " ON CP." + fieldNames[FLD_CURRENCY_ID]
                    + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " WHERE " + whereClause
                    + " AND " + fieldNames[FLD_PAY_TYPE] + "!=5"
                    + " AND " + fieldNames[FLD_PAY_TYPE] + "!=4"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2"
                    + " GROUP BY " + fieldNames[FLD_PAY_TYPE]
                    + ", CP." + fieldNames[FLD_CURRENCY_ID]
                    + ", CP." + fieldNames[FLD_RATE];

            //if(whereClause != null && whereClause.length() > 0)
            //sql = sql + " WHERE " + whereClause;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                CashPayments1 cashPayment1 = new CashPayments1();
                CurrencyType currencyType = new CurrencyType();
                BillMain billMain = new BillMain();
                //CashReturn cashReturn = new CashReturn();

                cashPayment1.setPaymentType(rs.getInt(1));
                cashPayment1.setRate(rs.getInt(4));
                cashPayment1.setAmount(rs.getDouble("SUM_AMOUNT" + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT]));
                //cashPayment.setAmount(rs.getDouble(6));
                temp.add(cashPayment1);

                currencyType.setOID(rs.getLong(2));
                currencyType.setCode(rs.getString(3));
                temp.add(currencyType);

                billMain.setOID(rs.getLong(5));
                temp.add(billMain);
                lists.add(temp);
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

    public static Vector listDetailPaymentWithReturn(int limitStart, int recordToGet, String whereClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + fieldNames[FLD_PAY_TYPE]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
                    + " , CP." + fieldNames[FLD_RATE]
                    + " , SUM(CP." + fieldNames[FLD_AMOUNT] + ")" + "AS SUM_PAYMENT_" + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT]
                    + " , SUM(CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")" + "AS SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]
                    + " FROM " + TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT"
                    + " ON CP." + fieldNames[FLD_CURRENCY_ID]
                    + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " INNER JOIN " + PstCashReturn.TBL_RETURN + " CRP"
                    + " ON CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE " + whereClause
                    + //" AND " + fieldNames[FLD_PAY_TYPE] + "!=5" +
                    //" AND " + fieldNames[FLD_PAY_TYPE] + "!=4" +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0"
                    + //Add Transaction type and transaction status
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0"
                    + //" AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
                    " GROUP BY " + fieldNames[FLD_PAY_TYPE]
                    + ", CP." + fieldNames[FLD_CURRENCY_ID]
                    + ", CP." + fieldNames[FLD_RATE];

            //if(whereClause != null && whereClause.length() > 0)
            //sql = sql + " WHERE " + whereClause;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }
            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                CashPayments1 cashPayment1 = new CashPayments1();
                CurrencyType currencyType = new CurrencyType();
                CashReturn cashReturn = new CashReturn();

                cashPayment1.setPaymentType(rs.getInt(1));
                cashPayment1.setRate(rs.getInt(4));
                cashPayment1.setAmount(rs.getDouble("SUM_PAYMENT_" + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT]));
                //cashPayment.setAmount(rs.getDouble(5));
                temp.add(cashPayment1);

                currencyType.setOID(rs.getLong(2));
                currencyType.setCode(rs.getString(3));
                temp.add(currencyType);


                cashReturn.setAmount(rs.getDouble("SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]));
                temp.add(cashReturn);
                lists.add(temp);
                //temp.add(new Double(rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")));
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

    public static double getSumListDetailBayar(String whereClause) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT]
                    + "* CP." + fieldNames[FLD_RATE] + ")"
                    + " FROM " + TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT"
                    + " ON CP." + fieldNames[FLD_CURRENCY_ID]
                    + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " WHERE " + whereClause
                    + " AND " + fieldNames[FLD_PAY_TYPE] + "!=5"
                    + " AND " + fieldNames[FLD_PAY_TYPE] + "!=4"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2"
                    + //" GROUP BY " + fieldNames[FLD_PAY_TYPE] +
                    //", CP." + fieldNames[FLD_CURRENCY_ID];
                    " GROUP BY CP." + fieldNames[FLD_CURRENCY_ID]
                    + ", CP." + fieldNames[FLD_RATE];

            /*if (whereClause != null && whereClause.length() > 0)
            sql = sql + " WHERE " + whereClause;*/

            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

    /**
     * Ari_wiweka 20130727
     * Menampilkan list cash payment di Multiple Payment
     * @param rs
     * @param cashCashier
     */
    public static Vector listCashPayment(int limitStart, int recordToGet, String whereClause, String orderClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_ID] + " ,CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NAME] + " , CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE] + " , CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID] + ", CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] + ", CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE] + ", CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT]
                    + " FROM  " + TBL_PAYMENT + " AS CP "
                    + " LEFT JOIN " + PstCashCreditCard.TBL_CREDIT_CARD + " AS CC "
                    + " ON CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] + " = CP." + fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]
                    + " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " AS CT"
                    + " ON CP." + fieldNames[PstCashPayment1.FLD_CURRENCY_ID] + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }
            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector temp = new Vector();
                CashPayments1 cashPayment1 = new CashPayments1();
                CashCreditCard cashCreditCard = new CashCreditCard();
                CurrencyType currencyType = new CurrencyType();

                cashPayment1.setPaymentType(rs.getLong(PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]));
                cashPayment1.setCurrencyId(rs.getLong(PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]));
                cashPayment1.setRate(rs.getDouble(PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]));
                cashPayment1.setAmount(rs.getDouble(PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT]));
                temp.add(cashPayment1);

                cashCreditCard.setCcName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_ID]));
                cashCreditCard.setCcName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NAME]));
                temp.add(cashCreditCard);

                currencyType.setCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                temp.add(currencyType);

                lists.add(temp);
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

    /**
     * Ari_wiweka 20130728
     * sum total payment
     */
    public static double getSumPayment(String whereClause) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
             String sql = " SELECT SUM(CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT]+") "
                    + " FROM  " + TBL_PAYMENT + " AS CP "
                    + " LEFT JOIN " + PstCashCreditCard.TBL_CREDIT_CARD + " AS CC "
                    + " ON CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] + " = CP." + fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]
                    + " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " AS CT"
                    + " ON CP." + fieldNames[PstCashPayment1.FLD_CURRENCY_ID] + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

    /**
     * Ari_wiweka 20130727
     * Count untuk list cah payment
     * @param rs
     * @param cashPayment1
     */
    public static int getCountCashPayment(String whereClause) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT COUNT(" + fieldNames[PstCashPayment1.FLD_PAYMENT_ID] + ")"
                    + " FROM  " + TBL_PAYMENT + " AS CP "
                    + " LEFT JOIN " + PstCashCreditCard.TBL_CREDIT_CARD + "AS CC "
                    + " ON CC." + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] + " = CP." + fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]
                    + " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " AS CT"
                    + " ON CP." + fieldNames[PstCashPayment1.FLD_CURRENCY_ID] + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static void resultToObject(ResultSet rs, CashPayments1 cashPayment1) {
        try {
            cashPayment1.setOID(rs.getLong(PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAYMENT_ID]));
            cashPayment1.setBillMainId(rs.getLong(PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID]));
            cashPayment1.setCurrencyId(rs.getLong(PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]));
            cashPayment1.setPaymentType(rs.getLong(PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]));
            cashPayment1.setRate(rs.getDouble(PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]));
            cashPayment1.setAmount(rs.getDouble(PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT]));
            cashPayment1.setPaymentStatus(rs.getInt(PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAYMENT_STATUS]));
            Date dtRoster = DBHandler.convertDate(rs.getDate(PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_DATETIME]), rs.getTime(PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_DATETIME]));
            cashPayment1.setPayDateTime(dtRoster);
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long paymentId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PAYMENT
                    + " WHERE " + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAYMENT_ID]
                    + " = " + paymentId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static long deleteBillPayments(long oidMain) {
        long oid = 0;
        DBResultSet dbrs = null;
        try {
            String sql = " DELETE FROM " + TBL_PAYMENT
                    + " WHERE " + fieldNames[FLD_BILL_MAIN_ID] + "='" + oidMain + "'";
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return oidMain;
    }
    /**
     * overloading method getListPayment(lLocationOid, dStartDate, dEndDate, iDocType, iSaleReportType)
     */
    SrcSaleReport srcSaleReport = new SrcSaleReport();

    public Vector getListPayment(SrcSaleReport srcSaleReport, long lLocationOid, Date dStartDate, Date dEndDate, int iDocType, int iSaleReportType) {
        this.srcSaleReport = srcSaleReport;
        Vector result = getListPayment(lLocationOid, dStartDate, dEndDate, iDocType, iSaleReportType);
        return result;
    }

    /**
     * @param lLocationOid
     * @param dStartDate
     * @param dEndDate
     * @param iSaleReportType
     * @return Vector of (vector of object cashPayment - per payType)
     */
    public Vector getListPayment(long lLocationOid, Date dStartDate, Date dEndDate, int iDocType, int iSaleReportType) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        String sStartDate = "";
        String sEndDate = "";
        if (dStartDate != null && dEndDate != null) {
            sStartDate = Formater.formatDate(dStartDate, "yyyy-MM-dd") + " 00:00:00";
            sEndDate = Formater.formatDate(dEndDate, "yyyy-MM-dd") + " 23:59:59";
        }

        try {
            String sSQLPayment = "SELECT P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]
                    + ", SUM(P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT] + ") "
                    + " FROM " + PstCashPayment1.TBL_PAYMENT + " AS P "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
                    + " ON P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + " BETWEEN \"" + sStartDate + "\""
                    + " AND \"" + sEndDate + "\"";
            if (lLocationOid != 0) {
                sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                        + " = " + lLocationOid;
            }
            sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + " = " + iDocType
                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + " = " + iSaleReportType
                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + " != " + PstBillMain.TRANS_STATUS_DELETED;
            if (srcSaleReport.getCurrencyOid() != 0) {
                sSQLPayment += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + srcSaleReport.getCurrencyOid();
            }
            sSQLPayment += " GROUP BY P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE];

            String sSQLChange = "SELECT 0 AS " + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]
                    + ", - SUM(C." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")"
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS M "
                    + " INNER JOIN " + PstCashReturn.TBL_RETURN + " AS C "
                    + " ON M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = C." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " WHERE M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + " BETWEEN \"" + sStartDate + "\""
                    + " AND \"" + sEndDate + "\"";
            if (lLocationOid != 0) {
                sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                        + " = " + lLocationOid;
            }
            sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + " = " + iDocType
                    + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + " = " + iSaleReportType
                    + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + " != " + PstBillMain.TRANS_STATUS_DELETED;
            if (srcSaleReport.getCurrencyOid() != 0) {
                sSQLChange += " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + srcSaleReport.getCurrencyOid();
            }
            sSQLChange += " GROUP BY C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];

            String sql = sSQLPayment + " UNION " + sSQLChange;
            //System.out.println("sql on PstCashPayment.getListPayment(#,#,#,#,#) : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Hashtable hashPayment = new Hashtable();
            while (rs.next()) {
                CashPayments1 objCashPayments1 = new CashPayments1();
                objCashPayments1.setPaymentType(rs.getInt(1));
                objCashPayments1.setCurrencyId(rs.getLong(2));
                objCashPayments1.setRate(rs.getDouble(3));
                objCashPayments1.setAmount(rs.getDouble(4));

                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                if (objCashPayments1.getAmount() != 0) {
                    String strHashKey = "" + objCashPayments1.getPaymentType() + objCashPayments1.getCurrencyId() + objCashPayments1.getRate();
                    if (hashPayment.containsKey(strHashKey)) {
                        CashPayments1 objCashPayments1onHash = (CashPayments1) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPayments1onHash.getAmount();
                        double dAdditionalAmount = objCashPayments1.getAmount();
                        objCashPayments1onHash.setAmount(dOriginalAmount + dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPayments1onHash);
                    } else {
                        hashPayment.put(strHashKey, objCashPayments1);
                    }
                }
            }


            Vector vCashPayment = new Vector(1, 1);
            Vector vCardPayment = new Vector(1, 1);
            Vector vChequePayment = new Vector(1, 1);
            Vector vDebitPayment = new Vector(1, 1);
            Vector vReturnPayment = new Vector(1, 1);
            for (Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements();) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments1 objCashPaymentResult = (CashPayments1) hashPayment.get(strKey);
                int iPayType = Integer.parseInt(strKey.substring(0, 1));
                switch (iPayType) {
                    case PstCashPayment1.CASH:
                        vCashPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.CARD:
                        vCardPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.CHEQUE:
                        vChequePayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.DEBIT:
                        vDebitPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.RETURN:
                        vReturnPayment.add(objCashPaymentResult);
                        break;
                }
            }

            result.add(vCashPayment);
            result.add(vCardPayment);
            result.add(vChequePayment);
            result.add(vDebitPayment);
            result.add(vReturnPayment);
        } catch (Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    public Vector getListPayment(SrcSaleReport srcSaleReport, int iDocType, int iSaleReportType) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        String sStartDate = "";
        String sEndDate = "";
        if (srcSaleReport.getDateFrom() != null && srcSaleReport.getDateTo() != null) {
            sStartDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd") + " 00:00:00";
            sEndDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd") + " 23:59:59";
        }

        try {
            String sSQLPayment = "SELECT P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]
                    + ", SUM(P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT] + ")" +//*P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+") "+
                    "/P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]
                    + " FROM " + PstCashPayment1.TBL_PAYMENT + " AS P "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
                    + " ON P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + " BETWEEN \"" + sStartDate + "\""
                    + " AND \"" + sEndDate + "\"";
            if (srcSaleReport.getLocationId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                        + " = " + srcSaleReport.getLocationId();
            }
            if (srcSaleReport.getShiftId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]
                        + " = " + srcSaleReport.getShiftId();
            }
            sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + " = " + iDocType
                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + " = " + iSaleReportType
                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + " != " + PstBillMain.TRANS_STATUS_DELETED;
            if (srcSaleReport.getCurrencyOid() != 0) {
                sSQLPayment += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + srcSaleReport.getCurrencyOid();
            }
            sSQLPayment += " GROUP BY P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE];

            String sSQLChange = "SELECT 0 AS " + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]
                    + ", - SUM(C." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")"
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS M "
                    + " INNER JOIN " + PstCashReturn.TBL_RETURN + " AS C "
                    + " ON M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = C." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " WHERE M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + " BETWEEN \"" + sStartDate + "\""
                    + " AND \"" + sEndDate + "\"";
            if (srcSaleReport.getLocationId() != 0) {
                sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                        + " = " + srcSaleReport.getLocationId();
            }
            if (srcSaleReport.getShiftId() != 0) {
                sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]
                        + " = " + srcSaleReport.getShiftId();
            }
            sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + " = " + iDocType
                    + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + " = " + iSaleReportType
                    + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + " != " + PstBillMain.TRANS_STATUS_DELETED;
            if (srcSaleReport.getCurrencyOid() != 0) {
                sSQLChange += " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + srcSaleReport.getCurrencyOid();
            }
            sSQLChange += " GROUP BY C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];

            String sql = sSQLPayment + " UNION " + sSQLChange;

            //System.out.println("sql on PstCashPayment.getListPayment() : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Hashtable hashPayment = new Hashtable();
            while (rs.next()) {
                CashPayments1 objCashPayments1 = new CashPayments1();
                objCashPayments1.setPaymentType(rs.getInt(1));
                objCashPayments1.setCurrencyId(rs.getLong(2));
                objCashPayments1.setRate(rs.getDouble(3));
                objCashPayments1.setAmount(rs.getDouble(4));

                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                if (objCashPayments1.getAmount() != 0) {
                    String strHashKey = "" + objCashPayments1.getPaymentType() + objCashPayments1.getCurrencyId() + objCashPayments1.getRate();
                    if (hashPayment.containsKey(strHashKey)) {
                        CashPayments1 objCashPayments1onHash = (CashPayments1) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPayments1onHash.getAmount();
                        double dAdditionalAmount = objCashPayments1.getAmount();
                        objCashPayments1onHash.setAmount(dOriginalAmount + dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPayments1onHash);
                    } else {
                        hashPayment.put(strHashKey, objCashPayments1);
                    }
                }
            }


            Vector vCashPayment = new Vector(1, 1);
            Vector vCardPayment = new Vector(1, 1);
            Vector vChequePayment = new Vector(1, 1);
            Vector vDebitPayment = new Vector(1, 1);
            Vector vReturnPayment = new Vector(1, 1);
            for (Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements();) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments1 objCashPaymentResult = (CashPayments1) hashPayment.get(strKey);
                int iPayType = Integer.parseInt(strKey.substring(0, 1));
                switch (iPayType) {
                    case PstCashPayment1.CASH:
                        vCashPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.CARD:
                        vCardPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.CHEQUE:
                        vChequePayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.DEBIT:
                        vDebitPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.RETURN:
                        vReturnPayment.add(objCashPaymentResult);
                        break;
                }
            }

            result.add(vCashPayment);
            result.add(vCardPayment);
            result.add(vChequePayment);
            result.add(vDebitPayment);
            result.add(vReturnPayment);
        } catch (Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    /**
     * @return Vector of (vector of object cashPayment - per payType)
     */
    public Vector getListPayment(long lCashBillMainOid) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        try {
            String sSQLPayment = "SELECT P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]
                    + ", SUM(P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT] + ") "
                    + " FROM " + PstCashPayment1.TBL_PAYMENT + " AS P "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
                    + " ON P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = " + lCashBillMainOid
                    + " GROUP BY P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE];

            String sSQLChange = "SELECT 0 AS " + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]
                    + ", - SUM(C." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")"
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS M "
                    + " INNER JOIN " + PstCashReturn.TBL_RETURN + " AS C "
                    + " ON M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = C." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " WHERE M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = " + lCashBillMainOid
                    + " GROUP BY C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];

            String sql = sSQLPayment + " UNION " + sSQLChange;
            //System.out.println("sql on PstCashPayment.getListPayment() : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Hashtable hashPayment = new Hashtable();
            while (rs.next()) {
                CashPayments1 objCashPayments1 = new CashPayments1();
                objCashPayments1.setPaymentType(rs.getInt(1));
                objCashPayments1.setCurrencyId(rs.getLong(2));
                objCashPayments1.setRate(rs.getDouble(3));
                objCashPayments1.setAmount(rs.getDouble(4));

                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                if (objCashPayments1.getAmount() != 0) {
                    String strHashKey = "" + objCashPayments1.getPaymentType() + objCashPayments1.getCurrencyId() + objCashPayments1.getRate();
                    if (hashPayment.containsKey(strHashKey)) {
                        CashPayments1 objCashPayments1onHash = (CashPayments1) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPayments1onHash.getAmount();
                        double dAdditionalAmount = objCashPayments1.getAmount();
                        objCashPayments1onHash.setAmount(dOriginalAmount + dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPayments1onHash);
                    } else {
                        hashPayment.put(strHashKey, objCashPayments1);
                    }
                }
            }


            Vector vCashPayment = new Vector(1, 1);
            Vector vCardPayment = new Vector(1, 1);
            Vector vChequePayment = new Vector(1, 1);
            Vector vDebitPayment = new Vector(1, 1);
            Vector vReturnPayment = new Vector(1, 1);
            for (Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements();) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments1 objCashPaymentResult = (CashPayments1) hashPayment.get(strKey);
                int iPayType = Integer.parseInt(strKey.substring(0, 1));
                switch (iPayType) {
                    case PstCashPayment1.CASH:
                        vCashPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.CARD:
                        vCardPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.CHEQUE:
                        vChequePayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.DEBIT:
                        vDebitPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment1.RETURN:
                        vReturnPayment.add(objCashPaymentResult);
                        break;
                }
            }

            result.add(vCashPayment);
            result.add(vCardPayment);
            result.add(vChequePayment);
            result.add(vDebitPayment);
            result.add(vReturnPayment);
        } catch (Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    //Pembayaran untuk closing
    public static double getPembayaranKotorClosing(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT]
                    + "* CP." + fieldNames[FLD_RATE] + ")"
                    + " FROM " + TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE " + " CP." + fieldNames[FLD_PAY_TYPE] + "!= 5"
                    + " AND CP." + fieldNames[FLD_PAY_TYPE] + "!=4"
                    + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0"
                    + //" AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
                    " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0"
                    + " AND CBM." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]
                    + " = " + oidCashCashier;

            //if (whereClause != null && whereClause.length() > 0)
            // sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

    public static double getPembayaranKreditClosing(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT]
                    + "* CP." + fieldNames[FLD_RATE] + ")"
                    + " FROM " + TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE " + " CP." + fieldNames[FLD_PAY_TYPE] + "!= 5"
                    + " AND CP." + fieldNames[FLD_PAY_TYPE] + "!=4"
                    + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0"
                    + //" AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
                    " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0"
                    + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=1"
                    + " AND CBM." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]
                    + " = " + oidCashCashier;

            //if (whereClause != null && whereClause.length() > 0)
            // sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

    /**
     * getListPaymentDinamis by mirahu 20120416
     * @param srcSaleReport
     * @param iDocType
     * @param iSaleReportType
     * @return
     */
    public Vector getListPaymentDinamis(SrcSaleReport srcSaleReport, int iDocType, int iSaleReportType) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        String sStartDate = "";
        String sEndDate = "";
        if (srcSaleReport.getDateFrom() != null && srcSaleReport.getDateTo() != null) {
            sStartDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd") + " 00:00:00";
            sEndDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd") + " 23:59:59";
        }

        try {
            String sSQLPayment = "SELECT P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]
                    + ", SUM(P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT] + ")" +//*P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+") "+
                    "/P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]
                    + ", PS.PAYMENT_TYPE"
                    + " FROM " + PstCashPayment1.TBL_PAYMENT + " AS P "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
                    + " ON P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN payment_system AS PS ON PS.PAYMENT_SYSTEM_ID=P.PAY_TYPE "
                    + " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + " BETWEEN \"" + sStartDate + "\""
                    + " AND \"" + sEndDate + "\"";
            if (srcSaleReport.getLocationId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                        + " = " + srcSaleReport.getLocationId();
            }
            if (srcSaleReport.getShiftId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]
                        + " = " + srcSaleReport.getShiftId();
            }
            sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + " = " + iDocType
                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + " = " + iSaleReportType
                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + " != " + PstBillMain.TRANS_STATUS_DELETED;
            if (srcSaleReport.getCurrencyOid() != 0) {
                sSQLPayment += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + srcSaleReport.getCurrencyOid();
            }
            sSQLPayment += " GROUP BY P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE];

            String sSQLChange = "SELECT 504404313199282645 AS " + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]
                    + ", - SUM(C." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")"
                    + ", 0 "
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS M "
                    + " INNER JOIN " + PstCashReturn.TBL_RETURN + " AS C "
                    + " ON M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = C." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " WHERE M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + " BETWEEN \"" + sStartDate + "\""
                    + " AND \"" + sEndDate + "\"";
            if (srcSaleReport.getLocationId() != 0) {
                sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                        + " = " + srcSaleReport.getLocationId();
            }
            if (srcSaleReport.getShiftId() != 0) {
                sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]
                        + " = " + srcSaleReport.getShiftId();
            }
            sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + " = " + iDocType
                    + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + " = " + iSaleReportType
                    + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + " != " + PstBillMain.TRANS_STATUS_DELETED;
            if (srcSaleReport.getCurrencyOid() != 0) {
                sSQLChange += " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + srcSaleReport.getCurrencyOid();
            }
            sSQLChange += " GROUP BY C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];

            String sql = sSQLPayment + " UNION " + sSQLChange;

            //System.out.println("sql on PstCashPayment.getListPayment() : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Hashtable hashPayment = new Hashtable();
            Hashtable hashUangKembalian = new Hashtable();
            while (rs.next()) {
                CashPayments1 objCashPayments1 = new CashPayments1();
                objCashPayments1.setPaymentType(rs.getLong(1));
                objCashPayments1.setCurrencyId(rs.getLong(2));
                objCashPayments1.setRate(rs.getDouble(3));
                objCashPayments1.setAmount(rs.getDouble(4));
                objCashPayments1.setTypeSistemPayment(rs.getInt("PAYMENT_TYPE"));
                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                
                if (objCashPayments1.getAmount() > 0) {
                    String strHashKey = "" + objCashPayments1.getPaymentType() + objCashPayments1.getCurrencyId() + objCashPayments1.getRate();
                    if (hashPayment.containsKey(strHashKey)) {
                        CashPayments1 objCashPayments1onHash = (CashPayments1) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPayments1onHash.getAmount();
                        double dAdditionalAmount = objCashPayments1.getAmount();
                        objCashPayments1onHash.setAmount(dOriginalAmount + dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPayments1onHash);
                    } else {
                        hashPayment.put(strHashKey, objCashPayments1);
                    }
                }else{
                    String strHashKeyKembalian = "uang_kembalian";
                    if (hashUangKembalian.containsKey(strHashKeyKembalian)) {
                        CashPayments1 objCashPayments1onHash = (CashPayments1) hashUangKembalian.get(strHashKeyKembalian);
                        double dOriginalAmount = objCashPayments1onHash.getAmount();
                        double dAdditionalAmount = objCashPayments1.getAmount();
                        objCashPayments1onHash.setAmount(dOriginalAmount + dAdditionalAmount);
                        hashUangKembalian.put(strHashKeyKembalian, objCashPayments1onHash);
                    } else {
                        hashUangKembalian.put(strHashKeyKembalian, objCashPayments1);
                    }
                }
            }


            Vector vCashPayment = new Vector(1, 1);
            for (Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements();) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments1 objCashPaymentResult = (CashPayments1) hashPayment.get(strKey);
                vCashPayment.add(objCashPaymentResult);
                if(objCashPaymentResult.getTypeSistemPayment()==1){
                    objCashPaymentResult = (CashPayments1) hashUangKembalian.get("uang_kembalian");
                    vCashPayment.add(objCashPaymentResult);
                }
            }

            result.add(vCashPayment);
        } catch (Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    public Vector getListPaymentDinamis(long lLocationOid, Date dStartDate, Date dEndDate, int iDocType, int iSaleReportType) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        String sStartDate = "";
        String sEndDate = "";
        if (srcSaleReport.getDateFrom() != null && srcSaleReport.getDateTo() != null) {
            sStartDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd") + " 00:00:00";
            sEndDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd") + " 23:59:59";
        }

        try {
            String sSQLPayment = "SELECT P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]
                    + ", SUM(P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_AMOUNT] + ")" +//*P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+") "+
                    "/P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE]
                    + " FROM " + PstCashPayment1.TBL_PAYMENT + " AS P "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
                    + " ON P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + " BETWEEN \"" + sStartDate + "\""
                    + " AND \"" + sEndDate + "\"";
            if (srcSaleReport.getLocationId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                        + " = " + srcSaleReport.getLocationId();
            }
            if (srcSaleReport.getShiftId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]
                        + " = " + srcSaleReport.getShiftId();
            }
            sSQLPayment = sSQLPayment + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + " = " + iDocType
                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + " = " + iSaleReportType
                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + " != " + PstBillMain.TRANS_STATUS_DELETED;
            if (srcSaleReport.getCurrencyOid() != 0) {
                sSQLPayment += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + srcSaleReport.getCurrencyOid();
            }
            sSQLPayment += " GROUP BY P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_RATE];

            String sSQLChange = "SELECT 0 AS " + PstCashPayment1.fieldNames[PstCashPayment1.FLD_PAY_TYPE]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]
                    + ", - SUM(C." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")"
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS M "
                    + " INNER JOIN " + PstCashReturn.TBL_RETURN + " AS C "
                    + " ON M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = C." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " WHERE M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + " BETWEEN \"" + sStartDate + "\""
                    + " AND \"" + sEndDate + "\"";
            if (srcSaleReport.getLocationId() != 0) {
                sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                        + " = " + srcSaleReport.getLocationId();
            }
            if (srcSaleReport.getShiftId() != 0) {
                sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]
                        + " = " + srcSaleReport.getShiftId();
            }
            sSQLChange = sSQLChange + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + " = " + iDocType
                    + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + " = " + iSaleReportType
                    + " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + " != " + PstBillMain.TRANS_STATUS_DELETED;
            if (srcSaleReport.getCurrencyOid() != 0) {
                sSQLChange += " AND M." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + srcSaleReport.getCurrencyOid();
            }
            sSQLChange += " GROUP BY C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];

            String sql = sSQLPayment + " UNION " + sSQLChange;

            //System.out.println("sql on PstCashPayment.getListPayment() : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Hashtable hashPayment = new Hashtable();
            while (rs.next()) {
                CashPayments1 objCashPayments1 = new CashPayments1();
                objCashPayments1.setPaymentType(rs.getLong(1));
                objCashPayments1.setCurrencyId(rs.getLong(2));
                objCashPayments1.setRate(rs.getDouble(3));
                objCashPayments1.setAmount(rs.getDouble(4));

                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                if (objCashPayments1.getAmount() != 0) {
                    String strHashKey = "" + objCashPayments1.getPaymentType() + objCashPayments1.getCurrencyId() + objCashPayments1.getRate();
                    if (hashPayment.containsKey(strHashKey)) {
                        CashPayments1 objCashPayments1onHash = (CashPayments1) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPayments1onHash.getAmount();
                        double dAdditionalAmount = objCashPayments1.getAmount();
                        objCashPayments1onHash.setAmount(dOriginalAmount + dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPayments1onHash);
                    } else {
                        hashPayment.put(strHashKey, objCashPayments1);
                    }
                }
            }


            Vector vCashPayment = new Vector(1, 1);
            for (Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements();) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments1 objCashPaymentResult = (CashPayments1) hashPayment.get(strKey);
                vCashPayment.add(objCashPaymentResult);
            }

            result.add(vCashPayment);
        } catch (Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    public Vector getListPaymentDinamis(long lCashBillMainOid) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sSQLPayment = "SELECT P." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]
                    + ", SUM(P." + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] + ")" +//*P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+") "+
                    "/P." + PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]
                    + " FROM " + PstCashPayment.TBL_PAYMENT + " AS P "
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM "
                    + " ON P." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                    + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = " + lCashBillMainOid;

            sSQLPayment += " GROUP BY P." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]
                    + ", P." + PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]
                    + ", P." + PstCashPayment.fieldNames[PstCashPayment.FLD_RATE];

            String sSQLChange = "SELECT 0 AS " + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]
                    + ", - SUM(C." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")"
                    + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS M "
                    + " INNER JOIN " + PstCashReturn.TBL_RETURN + " AS C "
                    + " ON M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = C." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " WHERE M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " = " + lCashBillMainOid;

            sSQLChange += " GROUP BY C." + PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]
                    + ", C." + PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];

            String sql = sSQLPayment + " UNION " + sSQLChange;

            System.out.println("sql on PstCashPayment.getListPayment() : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Hashtable hashPayment = new Hashtable();
            while (rs.next()) {
                CashPayments1 objCashPaymentsDinamis = new CashPayments1();
                objCashPaymentsDinamis.setPaymentType(rs.getLong(1));
                objCashPaymentsDinamis.setCurrencyId(rs.getLong(2));
                objCashPaymentsDinamis.setRate(rs.getDouble(3));
                objCashPaymentsDinamis.setAmount(rs.getDouble(4));

                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                if (objCashPaymentsDinamis.getAmount() != 0) {
                    String strHashKey = "" + objCashPaymentsDinamis.getPaymentType() + objCashPaymentsDinamis.getCurrencyId() + objCashPaymentsDinamis.getRate();
                    if (hashPayment.containsKey(strHashKey)) {
                        CashPayments objCashPaymentsonHash = (CashPayments) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPaymentsonHash.getAmount();
                        double dAdditionalAmount = objCashPaymentsDinamis.getAmount();
                        objCashPaymentsonHash.setAmount(dOriginalAmount + dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPaymentsonHash);
                    } else {
                        hashPayment.put(strHashKey, objCashPaymentsDinamis);
                    }
                }
            }


            Vector vCashPayment = new Vector(1, 1);
            for (Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements();) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments1 objCashPaymentResult = (CashPayments1) hashPayment.get(strKey);
                vCashPayment.add(objCashPaymentResult);
            }
            result.add(vCashPayment);
        } catch (Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    //
    public static Vector listDetailPaymentDinamisWithReturn(int limitStart, int recordToGet, String whereClause) {
        return listDetailPaymentDinamisWithReturn( limitStart,  recordToGet,  whereClause, new Vector()) ;
    }
    /**
     *payment dinamis
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @return
     */
    public static Vector listDetailPaymentDinamisWithReturn(int limitStart, int recordToGet, String whereClause, Vector vPaymentTransactionReturn) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + fieldNames[FLD_PAY_TYPE]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
                    + " , CP." + fieldNames[FLD_RATE]
                    + " , SUM(CP." + fieldNames[FLD_AMOUNT] + ")" + "AS SUM_PAYMENT_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]
                    + " , SUM(CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")" + "AS SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]
                    + " FROM " + TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT"
                    + " ON CP." + fieldNames[FLD_CURRENCY_ID]
                    + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " INNER JOIN " + PstCashReturn.TBL_RETURN + " CRP"
                    + " ON CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE " + whereClause
                    + //" AND " + fieldNames[FLD_PAY_TYPE] + "!=5" +
                    //" AND " + fieldNames[FLD_PAY_TYPE] + "!=4" +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0"
                    + //Add Transaction type and transaction status
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0"
                    + //" AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
                    " GROUP BY " + fieldNames[FLD_PAY_TYPE]
                    + ", CP." + fieldNames[FLD_CURRENCY_ID]
                    + ", CP." + fieldNames[FLD_RATE];

            //if(whereClause != null && whereClause.length() > 0)
            //sql = sql + " WHERE " + whereClause;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }
            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Hashtable hashPayment = new Hashtable();
            while (rs.next()) {
                Vector temp = new Vector();
                CashPayments1 objCashPaymentsDinamis = new CashPayments1();
                CurrencyType currencyType = new CurrencyType();
                CashReturn cashReturn = new CashReturn();
                
                double paymentTransactionReturn=0;
                double paymentReturnTransactionReturn=0;
                if(vPaymentTransactionReturn.size()>0){
                    for(int i=0; i<vPaymentTransactionReturn.size(); i++) {
                        Vector tempPaymentReturnTransaction = (Vector)vPaymentTransactionReturn.get(i);
                        CashPayments1 cashPayment = (CashPayments1) tempPaymentReturnTransaction.get(0);
                        CashReturn cashTransactionReturn = (CashReturn)tempPaymentReturnTransaction.get(2);
                        if(cashPayment.getPaymentType()==rs.getLong(1)){
                            paymentTransactionReturn=cashPayment.getAmount();
                            paymentReturnTransactionReturn=cashTransactionReturn.getAmount();
                        }
                    }
                }
                
                objCashPaymentsDinamis.setPaymentType(rs.getLong(1));
                objCashPaymentsDinamis.setRate(rs.getInt(4));
                double dPaymentAmount = rs.getDouble("SUM_PAYMENT_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]) - paymentTransactionReturn;
                objCashPaymentsDinamis.setAmount(dPaymentAmount);
                temp.add(objCashPaymentsDinamis);

                currencyType.setOID(rs.getLong(2));
                currencyType.setCode(rs.getString(3));
                temp.add(currencyType);

                double dReturnPaymentAmount = rs.getDouble("SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]) - paymentReturnTransactionReturn;
                cashReturn.setAmount(dReturnPaymentAmount);
                temp.add(cashReturn);
                
                lists.add(temp);
                //temp.add(new Double(rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")));
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
    
    
    public static Vector listDetailPaymentDinamisWithReturnTransaction(int limitStart, int recordToGet, String whereClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + fieldNames[FLD_PAY_TYPE]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
                    + " , CP." + fieldNames[FLD_RATE]
                    + " , SUM(CP." + fieldNames[FLD_AMOUNT] + ")" + "AS SUM_PAYMENT_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]
                    + " , SUM(CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")" + "AS SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]
                    + " FROM " + TBL_PAYMENT + " CP"
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM"
                    + " ON CP." + fieldNames[FLD_BILL_MAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT"
                    + " ON CP." + fieldNames[FLD_CURRENCY_ID]
                    + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " LEFT JOIN " + PstCashReturn.TBL_RETURN + " CRP"
                    + " ON CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]
                    + " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " WHERE " + whereClause
                    + //" AND " + fieldNames[FLD_PAY_TYPE] + "!=5" +
                    //" AND " + fieldNames[FLD_PAY_TYPE] + "!=4" +
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=1"
                    + //Add Transaction type and transaction status
                    " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0"
                    + //" AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
                    " GROUP BY " + fieldNames[FLD_PAY_TYPE]
                    + ", CP." + fieldNames[FLD_CURRENCY_ID]
                    + ", CP." + fieldNames[FLD_RATE];

            //if(whereClause != null && whereClause.length() > 0)
            //sql = sql + " WHERE " + whereClause;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }
            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Hashtable hashPayment = new Hashtable();
            while (rs.next()) {
                Vector temp = new Vector();
                CashPayments1 objCashPaymentsDinamis = new CashPayments1();
                CurrencyType currencyType = new CurrencyType();
                CashReturn cashReturn = new CashReturn();

                objCashPaymentsDinamis.setPaymentType(rs.getLong(1));
                objCashPaymentsDinamis.setRate(rs.getInt(4));
                objCashPaymentsDinamis.setAmount(rs.getDouble("SUM_PAYMENT_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));
                temp.add(objCashPaymentsDinamis);

                currencyType.setOID(rs.getLong(2));
                currencyType.setCode(rs.getString(3));
                temp.add(currencyType);


                cashReturn.setAmount(rs.getDouble("SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]));
                temp.add(cashReturn);
                lists.add(temp);
                //temp.add(new Double(rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")));
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
    

    /**
     * Ari_wiweka20130729
     * untuk SUM Credit card, cheque, cash , dan debit card yang di inputkan dari multiple payment
     */
    public static double getSumSystemPayment(String whereClause) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP."+fieldNames[PstCashPayment1.FLD_AMOUNT]+")  "
                    + " FROM "+TBL_PAYMENT+" AS CP "
                    + " LEFT JOIN "+PstPaymentSystem.TBL_P2_PAYMENT_SYSTEM+" AS PS"
                    + " ON CP."+fieldNames[PstCashPayment1.FLD_PAY_TYPE]+" = "
                    + " PS."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
}

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }
}

