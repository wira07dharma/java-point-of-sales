/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PstCashCreditPaymentDinamis.java
 *
 * Created on May 23, 2003, 2:52 PM
 */

package com.dimata.pos.entity.payment;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

/* package java */

import java.util.Vector;

/* package qdep */
import java.sql.ResultSet;
import java.util.Date;
//import com.dimata.qdep.db.*;
/* package cashier */
import com.dimata.pos.entity.billing.*;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;

public class PstCashCreditPaymentDinamis extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    //public static final String TBL_PAYMENT = "CASH_CREDIT_PAYMENT";
    public static final String TBL_PAYMENT = "cash_credit_payment";

    public static final int FLD_PAYMENT_ID = 0;
    public static final int FLD_CURRENCY_ID = 1;
    public static final int FLD_CREDIT_MAIN_ID = 2;
    public static final int FLD_PAY_TYPE = 3;
    public static final int FLD_RATE = 4;
    public static final int FLD_AMOUNT = 5;
    public static final int FLD_PAY_DATETIME = 6;
    public static final int FLD_PAYMENT_STATUS = 7;

    public static final String[] fieldNames = {
        "CASH_CREDIT_PAYMENT_ID",
        "CURRENCY_ID",
        "CREDIT_PAYMENT_MAIN_ID",
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
    public static final String[] paymentType = {

        "CASH",
        "CREDITCARD",
        "CHEQUE",
        "DEBIT"
    };

    /** Creates new PstCashCreditPaymentDinamis */
    public PstCashCreditPaymentDinamis() {
    }

    public PstCashCreditPaymentDinamis(int i) throws DBException {
        super(new PstCashCreditPaymentDinamis());
    }

    public PstCashCreditPaymentDinamis(String sOid) throws DBException {
        super(new PstCashCreditPaymentDinamis(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstCashCreditPaymentDinamis(long lOid) throws DBException {
        super(new PstCashCreditPaymentDinamis(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
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
        return new PstCashCreditPaymentDinamis().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        CashCreditPaymentsDinamis cashCreditPayment = fetchExc(ent.getOID());
        ent = (Entity) cashCreditPayment;
        return cashCreditPayment.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((CashCreditPaymentsDinamis) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((CashCreditPaymentsDinamis) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static CashCreditPaymentsDinamis fetchExc(long oid) throws DBException {
        try {
            CashCreditPaymentsDinamis cashCreditPayment = new CashCreditPaymentsDinamis();
            PstCashCreditPaymentDinamis pstCashPayment = new PstCashCreditPaymentDinamis(oid);
            cashCreditPayment.setOID(oid);
            cashCreditPayment.setCreditMainId(pstCashPayment.getlong(FLD_CREDIT_MAIN_ID));
            cashCreditPayment.setCurrencyId(pstCashPayment.getlong(FLD_CURRENCY_ID));
            cashCreditPayment.setPaymentType(pstCashPayment.getLong(FLD_PAY_TYPE));
            cashCreditPayment.setRate(pstCashPayment.getdouble(FLD_RATE));
            cashCreditPayment.setAmount(pstCashPayment.getdouble(FLD_AMOUNT));
            cashCreditPayment.setPayDateTime(pstCashPayment.getDate(FLD_PAY_DATETIME));
            cashCreditPayment.setPaymentStatus(pstCashPayment.getInt(FLD_PAYMENT_STATUS));

            return cashCreditPayment;
        } catch (DBException dbe) {
            System.out.println(">>>>>>>>" + dbe);
            throw dbe;
        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>" + e);
            throw new DBException(new PstCashCreditPaymentDinamis(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(CashCreditPaymentsDinamis cashCreditPayment) throws DBException {
        try {
            PstCashCreditPaymentDinamis pstCashPayment = new PstCashCreditPaymentDinamis(0);
            pstCashPayment.setLong(FLD_CREDIT_MAIN_ID, cashCreditPayment.getCreditMainId());
            pstCashPayment.setLong(FLD_CURRENCY_ID, cashCreditPayment.getCurrencyId());
            pstCashPayment.setLong(FLD_PAY_TYPE, cashCreditPayment.getPaymentType());
            pstCashPayment.setDouble(FLD_RATE, cashCreditPayment.getRate());
            pstCashPayment.setDouble(FLD_AMOUNT, cashCreditPayment.getAmount());
            pstCashPayment.setDate(FLD_PAY_DATETIME, cashCreditPayment.getPayDateTime());
            pstCashPayment.setInt(FLD_PAYMENT_STATUS, cashCreditPayment.getPaymentStatus());

            pstCashPayment.insert();
            cashCreditPayment.setOID(pstCashPayment.getlong(FLD_PAYMENT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashCreditPaymentDinamis(0), DBException.UNKNOWN);
        }
        return cashCreditPayment.getOID();
    }

    public static long insertExcByOid(CashCreditPaymentsDinamis cashCreditPayment) throws DBException {
        try {
            PstCashCreditPaymentDinamis pstCashPayment = new PstCashCreditPaymentDinamis(0);
            pstCashPayment.setLong(FLD_CREDIT_MAIN_ID, cashCreditPayment.getCreditMainId());
            pstCashPayment.setLong(FLD_CURRENCY_ID, cashCreditPayment.getCurrencyId());
            pstCashPayment.setLong(FLD_PAY_TYPE, cashCreditPayment.getPaymentType());
            pstCashPayment.setDouble(FLD_RATE, cashCreditPayment.getRate());
            pstCashPayment.setDouble(FLD_AMOUNT, cashCreditPayment.getAmount());
            pstCashPayment.setDate(FLD_PAY_DATETIME, cashCreditPayment.getPayDateTime());
            pstCashPayment.setInt(FLD_PAYMENT_STATUS, cashCreditPayment.getPaymentStatus());

            pstCashPayment.insertByOid(cashCreditPayment.getOID());
            //cashCreditPayment.setOID(pstCashPayment.getlong(FLD_PAYMENT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashCreditPaymentDinamis(0), DBException.UNKNOWN);
        }
        return cashCreditPayment.getOID();
    }


    public static long updateExc(CashCreditPaymentsDinamis cashCreditPayment) throws DBException {
        try {
            if (cashCreditPayment.getOID() != 0) {
                PstCashCreditPaymentDinamis pstCashPayment = new PstCashCreditPaymentDinamis(cashCreditPayment.getOID());
                pstCashPayment.setLong(FLD_CREDIT_MAIN_ID, cashCreditPayment.getCreditMainId());
                pstCashPayment.setLong(FLD_CURRENCY_ID, cashCreditPayment.getCurrencyId());
                pstCashPayment.setLong(FLD_PAY_TYPE, cashCreditPayment.getPaymentType());
                pstCashPayment.setDouble(FLD_RATE, cashCreditPayment.getRate());
                pstCashPayment.setDouble(FLD_AMOUNT, cashCreditPayment.getAmount());
                pstCashPayment.setInt(FLD_PAYMENT_STATUS, cashCreditPayment.getPaymentStatus());
                pstCashPayment.setDate(FLD_PAY_DATETIME, cashCreditPayment.getPayDateTime());
                pstCashPayment.update();
                return cashCreditPayment.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashCreditPaymentDinamis(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstCashCreditPaymentDinamis pstCashPayment = new PstCashCreditPaymentDinamis(oid);
            pstCashPayment.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCashCreditPaymentDinamis(0), DBException.UNKNOWN);
        }
        return oid;
    }


    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PAYMENT;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CashCreditPaymentsDinamis cashCreditPayment = new CashCreditPaymentsDinamis();
                resultToObject(rs, cashCreditPayment);
                lists.add(cashCreditPayment);
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


    public static double getPembayaranKredit2(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT] +
            " * CP." + fieldNames[FLD_RATE] + ")" + " AS AMOUNT_" +
            " FROM " + TBL_PAYMENT + " CP" +
            " INNER JOIN " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN + " CPM" +
            " ON CP." + fieldNames[FLD_CREDIT_MAIN_ID] +
            " = CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID] +
            " LEFT JOIN " + PstCashReturn.TBL_RETURN + " CR" +
            " ON CP." + fieldNames[FLD_CREDIT_MAIN_ID] +
            " = CR." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " + fieldNames[FLD_PAY_TYPE] + " != 7" +
            " AND CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID] +
            " = " + oidCashCashier;

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

     //Bayar Credit
      public static double getSummaryBayarCredit(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT  (SUM(CP." + fieldNames[FLD_AMOUNT] + ")" +
                    "- IF(ISNULL(SUM(CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + "))" +
                    ",0,SUM(CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] + ")))" +
                    " FROM " + TBL_PAYMENT + " CP" +
                    " INNER JOIN " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN + " CPM" +
                    " ON CP." + fieldNames[FLD_CREDIT_MAIN_ID] +
                    " = CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID] +
                    " LEFT JOIN " + PstCashReturn.TBL_RETURN + " CRP" +
                    " ON CP." + fieldNames[FLD_CREDIT_MAIN_ID] +
                    " = CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] +
                    " WHERE CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID]+
                    " = " + oidCashCashier +
                    " AND " + fieldNames[FLD_PAY_TYPE] + "!=7";

            //if (whereClause != null && whereClause.length() > 0)
               // sql = sql + " WHERE " + whereClause;

            System.out.println("sql bayar kredit :" + sql);
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


    public static void resultToObject(ResultSet rs, CashCreditPaymentsDinamis cashCreditPayment) {
        try {
            cashCreditPayment.setOID(rs.getLong(PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_PAYMENT_ID]));
            cashCreditPayment.setCreditMainId(rs.getLong(PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_CREDIT_MAIN_ID]));
            cashCreditPayment.setCurrencyId(rs.getLong(PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_CURRENCY_ID]));
            cashCreditPayment.setPaymentType(rs.getLong(PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_PAY_TYPE]));
            cashCreditPayment.setRate(rs.getDouble(PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_RATE]));
            cashCreditPayment.setAmount(rs.getDouble(PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_AMOUNT]));
            cashCreditPayment.setPaymentStatus(rs.getInt(PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_PAYMENT_STATUS]));
            Date dtRoster = DBHandler.convertDate(rs.getDate(PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_PAY_DATETIME]), rs.getTime(PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_PAY_DATETIME]));
            cashCreditPayment.setPayDateTime(dtRoster);
        } catch (Exception e) {
            System.out.println("err on resultToObject: "+e.toString());
        }
    }

    public static boolean checkOID(long paymentId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PAYMENT +
            " WHERE " + PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_PAYMENT_ID] +
            " = " + paymentId;

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
        }
        return result;
    }


    /**
     * mencari total satu credit payment main
     * @param paymentCreditMainId
     * @return
     */
    public static double getTotalCreditPayment(long paymentCreditMainId) {
        DBResultSet dbrs = null;
        double total = 0;
        try {
            String sql = "SELECT SUM(cp." + PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_AMOUNT] +
                "- IF(ISNULL(rp."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+"),0,rp."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+")) FROM " + TBL_PAYMENT + " as cp "+
                " left JOIN "+ PstCashReturn.TBL_RETURN + " as rp "+
                " on cp."+PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_CREDIT_MAIN_ID]+
                "= rp."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+
                " WHERE " + PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_CREDIT_MAIN_ID] +
                " = " + paymentCreditMainId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println(sql);
            while (rs.next()) {
                total = rs.getDouble(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }


    /**
     * get tota yang sudah terbayar tapi tampa di hitung dengan
     * pembayaran sekarang
     * @param billMainId
     * @param creditPayMainId
     * @return
     */
    public static double getTotalCreditPaymentNotInPayNow(long billMainId, long creditPayMainId) {
        DBResultSet dbrs = null;
        double total = 0;
        try {
            String sql = "SELECT SUM(" + PstCashCreditPaymentDinamis.fieldNames[PstCashCreditPaymentDinamis.FLD_AMOUNT] + ") " +
            " FROM " + TBL_PAYMENT +" AS P "+
            " INNER JOIN "+PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN+" AS CP "+
            " ON P."+fieldNames[FLD_CREDIT_MAIN_ID]+" = CP."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID]+
            " WHERE CP." +PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID]+"="+billMainId+
            " AND CP." +PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CREDIT_PAYMENT_MAIN_ID] +
            " != " + creditPayMainId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                total = rs.getDouble(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }

}
