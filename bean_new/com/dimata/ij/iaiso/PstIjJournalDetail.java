/*
 * PstJournalDetail.java
 *
 * Created on January 29, 2005, 6:07 AM
 */

package com.dimata.ij.iaiso;

// package java 

import java.sql.*;
import java.util.*;

// package qdep
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;

// package ij
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;




/**
 * @author Administrator
 */
public class PstIjJournalDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_IJ_JOURNAL_DETAIL = "ij_journal_detail";

    public static final int FLD_IJ_JOURNAL_DETAIL_ID = 0;
    public static final int FLD_JOURNAL_MAIN_ID = 1;
    public static final int FLD_ACCOUNT_CHART_ID = 2;
    public static final int FLD_DEBT_VALUE = 3;
    public static final int FLD_CREDIT_VALUE = 4;
    public static final int FLD_CURRENCY_ID = 5;
    public static final int FLD_TRANSACTION_RATE = 6;

    public static final String[] fieldNames = {
            "IJ_JOURNAL_DETAIL_ID",
            "JOURNAL_MAIN_ID",
            "ACCOUNT_CHART_ID",
            "DEBT_VALUE",
            "CREDIT_VALUE",
            "CURRENCY_ID",
            "TRANSACTION_RATE"
    };

    public static final int[] fieldTypes = {
            TYPE_LONG + TYPE_PK + TYPE_ID,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_FLOAT,
            TYPE_FLOAT,
            TYPE_LONG,
            TYPE_FLOAT
    };

    public PstIjJournalDetail() {
    }

    public PstIjJournalDetail(int i) throws DBException {
        super(new PstIjJournalDetail());
    }

    public PstIjJournalDetail(String sOid) throws DBException {
        super(new PstIjJournalDetail());
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstIjJournalDetail(long lOid) throws DBException {
        super(new PstIjJournalDetail());
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
        return TBL_IJ_JOURNAL_DETAIL;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstIjJournalDetail().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        IjJournalDetail ijjournaldetail = fetchExc(ent.getOID());
        ent = (Entity) ijjournaldetail;
        return ijjournaldetail.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((IjJournalDetail) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((IjJournalDetail) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static IjJournalDetail fetchExc(long oid) throws DBException {
        try {
            IjJournalDetail ijjournaldetail = new IjJournalDetail();
            PstIjJournalDetail pstIjJournalDetail = new PstIjJournalDetail(oid);
            ijjournaldetail.setOID(oid);

            ijjournaldetail.setJdetMainOid(pstIjJournalDetail.getlong(FLD_JOURNAL_MAIN_ID));
            ijjournaldetail.setJdetAccChart(pstIjJournalDetail.getlong(FLD_ACCOUNT_CHART_ID));
            ijjournaldetail.setJdetDebet(pstIjJournalDetail.getdouble(FLD_DEBT_VALUE));
            ijjournaldetail.setJdetCredit(pstIjJournalDetail.getdouble(FLD_CREDIT_VALUE));
            ijjournaldetail.setJdetTransCurrency(pstIjJournalDetail.getlong(FLD_CURRENCY_ID));
            ijjournaldetail.setJdetTransRate(pstIjJournalDetail.getdouble(FLD_TRANSACTION_RATE));

            return ijjournaldetail;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjJournalDetail(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(IjJournalDetail ijjournaldetail) throws DBException {
        try {
            PstIjJournalDetail pstIjJournalDetail = new PstIjJournalDetail(0);
            pstIjJournalDetail.setLong(FLD_JOURNAL_MAIN_ID, ijjournaldetail.getJdetMainOid());
            pstIjJournalDetail.setLong(FLD_ACCOUNT_CHART_ID, ijjournaldetail.getJdetAccChart());
            pstIjJournalDetail.setDouble(FLD_DEBT_VALUE, ijjournaldetail.getJdetDebet());
            pstIjJournalDetail.setDouble(FLD_CREDIT_VALUE, ijjournaldetail.getJdetCredit());
            pstIjJournalDetail.setLong(FLD_CURRENCY_ID, ijjournaldetail.getJdetTransCurrency());
            pstIjJournalDetail.setDouble(FLD_TRANSACTION_RATE, ijjournaldetail.getJdetTransRate());

            pstIjJournalDetail.insert();
            ijjournaldetail.setOID(pstIjJournalDetail.getlong(FLD_IJ_JOURNAL_DETAIL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjJournalDetail(0), DBException.UNKNOWN);
        }
        return ijjournaldetail.getOID();
    }

    public static long updateExc(IjJournalDetail ijjournaldetail) throws DBException {
        try {
            if (ijjournaldetail.getOID() != 0) {
                PstIjJournalDetail pstIjJournalDetail = new PstIjJournalDetail(ijjournaldetail.getOID());

                pstIjJournalDetail.setLong(FLD_JOURNAL_MAIN_ID, ijjournaldetail.getJdetMainOid());
                pstIjJournalDetail.setLong(FLD_ACCOUNT_CHART_ID, ijjournaldetail.getJdetAccChart());
                pstIjJournalDetail.setDouble(FLD_DEBT_VALUE, ijjournaldetail.getJdetDebet());
                pstIjJournalDetail.setDouble(FLD_CREDIT_VALUE, ijjournaldetail.getJdetCredit());
                pstIjJournalDetail.setLong(FLD_CURRENCY_ID, ijjournaldetail.getJdetTransCurrency());
                pstIjJournalDetail.setDouble(FLD_TRANSACTION_RATE, ijjournaldetail.getJdetTransRate());

                pstIjJournalDetail.update();
                return ijjournaldetail.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjJournalDetail(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstIjJournalDetail pstIjJournalDetail = new PstIjJournalDetail(oid);
            pstIjJournalDetail.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjJournalDetail(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_IJ_JOURNAL_DETAIL;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
//                        System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                IjJournalDetail ijjournaldetail = new IjJournalDetail();
                resultToObject(rs, ijjournaldetail);
                lists.add(ijjournaldetail);
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

    private static void resultToObject(ResultSet rs, IjJournalDetail ijjournaldetail) {
        try {
            ijjournaldetail.setOID(rs.getLong(PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_IJ_JOURNAL_DETAIL_ID]));
            ijjournaldetail.setJdetMainOid(rs.getLong(PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_JOURNAL_MAIN_ID]));
            ijjournaldetail.setJdetAccChart(rs.getLong(PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_ACCOUNT_CHART_ID]));
            ijjournaldetail.setJdetDebet(rs.getDouble(PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_DEBT_VALUE]));
            ijjournaldetail.setJdetCredit(rs.getDouble(PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_CREDIT_VALUE]));
            ijjournaldetail.setJdetTransCurrency(rs.getLong(PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_CURRENCY_ID]));
            ijjournaldetail.setJdetTransRate(rs.getDouble(PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_TRANSACTION_RATE]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long ijMapAccountId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_IJ_JOURNAL_DETAIL +
                    " WHERE " + PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_IJ_JOURNAL_DETAIL_ID] +
                    " = " + ijMapAccountId;

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
            String sql = "SELECT COUNT(" + PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_IJ_JOURNAL_DETAIL_ID] + ") FROM " + TBL_IJ_JOURNAL_DETAIL;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

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


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    IjJournalDetail ijjournaldetail = (IjJournalDetail) list.get(ls);
                    if (oid == ijjournaldetail.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    /**
     * gadnyana
     * proses penghapusan data ij item journal
     *
     * @param oidMain
     */

    public static boolean deleteItemJournal(long ijJournalMainId) {
        boolean result = false;
        try {
            String sql = "DELETE FROM " + TBL_IJ_JOURNAL_DETAIL +
                    " WHERE " + PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_JOURNAL_MAIN_ID] +
                    " = " + ijJournalMainId;
            DBHandler.execUpdate(sql);
            result = true;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
            result = false;
        }
        return result;
    }

    /**
     * Get OID of IjJournalDetail object for specified 'JournalMainOID'
     *
     * @param lIjJournalMainOid
     * @return
     * @created by Edhy
     */
    public static long getIjJournalDetailOID(long lIjJournalMainOid, long lAccountChart) {
        long lResult = 0;

        String sWhereClause = PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_JOURNAL_MAIN_ID] +
                " = " + lIjJournalMainOid +
                " AND " + PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_ACCOUNT_CHART_ID] +
                " = " + lAccountChart;
        Vector vListOfIjJournalDetail = PstIjJournalDetail.list(0, 0, sWhereClause, "");
        if (vListOfIjJournalDetail != null && vListOfIjJournalDetail.size() > 0) {
            IjJournalDetail objIjJournalDetail = (IjJournalDetail) vListOfIjJournalDetail.get(0);
            lResult = objIjJournalDetail.getOID();
        }

        return lResult;
    }


    /**
     * @param vIjJournalDetail
     * @return
     */
    public static double getTotalOnDebetSide(Vector vIjJournalDetail) {
        double dResult = 0;

        if (vIjJournalDetail != null && vIjJournalDetail.size() > 0) {
            int iIjJournalDetailCount = vIjJournalDetail.size();
            for (int i = 0; i < iIjJournalDetailCount; i++) {
                IjJournalDetail objIjJournalDetail = (IjJournalDetail) vIjJournalDetail.get(i);
                dResult = dResult + objIjJournalDetail.getJdetDebet();
            }
        }

        return dResult;
    }


    /**
     * @param vIjJournalDetail
     * @return
     */
    public static double getTotalOnCreditSide(Vector vIjJournalDetail) {
        double dResult = 0;

        if (vIjJournalDetail != null && vIjJournalDetail.size() > 0) {
            int iIjJournalDetailCount = vIjJournalDetail.size();
            for (int i = 0; i < iIjJournalDetailCount; i++) {
                IjJournalDetail objIjJournalDetail = (IjJournalDetail) vIjJournalDetail.get(i);
                dResult = dResult + objIjJournalDetail.getJdetCredit();
            }
        }

        return dResult;
    }


    /**
     * @param vIjJournalDetail
     * @return
     */
    public static boolean isBalanceDebetAndCredit(Vector vIjJournalDetail) {
        double dDebetBalance = getTotalOnDebetSide(vIjJournalDetail);
        double dCreditBalance = getTotalOnCreditSide(vIjJournalDetail);
        System.out.println("cek balance >>>>>>>");
        System.out.println("====== > debet :" + dDebetBalance);
        System.out.println("====== > kredit :" + dCreditBalance);
        if ((dDebetBalance == dCreditBalance) && (dDebetBalance != 0)) {
            return true;
        }

        return false;
    }

}
