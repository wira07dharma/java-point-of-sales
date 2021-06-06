/*
 * PstJournalMain.java
 *
 * Created on January 29, 2005, 6:07 AM
 */

package com.dimata.ij.iaiso;

// package java

import java.sql.*;
import java.util.*;

// package qdep
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.qdep.entity.*;

// package common
import com.dimata.common.entity.logger.*;

// package ij
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;




/**
 *
 * @author  Administrator
 * @version
 */
public class PstIjJournalMain extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_IJ_JOURNAL_MAIN = "ij_journal_main";

    public static final int FLD_IJ_JOURNAL_MAIN_ID = 0;
    public static final int FLD_TRANSACTION_DATE = 1;
    public static final int FLD_BOOK_TYPE = 2;
    public static final int FLD_CURRENCY_ID = 3;
    public static final int FLD_STATUS = 4;
    public static final int FLD_NOTE = 5;
    public static final int FLD_PERIOD = 6;
    public static final int FLD_ENTRY_DATE = 7;
    public static final int FLD_USER = 8;
    public static final int FLD_REF_BO_DOC_TYPE = 9;
    public static final int FLD_REF_BO_DOC_OID = 10;
    public static final int FLD_REF_BO_DOC_NUMBER = 11;
    public static final int FLD_REF_AISO_JOURNAL_OID = 12;
    public static final int FLD_REF_BO_SYSTEM = 13;
    public static final int FLD_REF_BO_DOC_LAST_UPDATE = 14;
    public static final int FLD_REF_BO_LOCATION = 15;
    public static final int FLD_REF_BO_TRANSACTION_TYPE = 16;
    public static final int FLD_CONTACT_ID = 17;
    public static final int FLD_REF_SUB_BO_DOC_OID = 18;

    public static final String[] fieldNames = {
        "IJ_JOURNAL_MAIN_ID",
        "TRANSACTION_DATE",
        "BOOK_TYPE",
        "CURRENCY_ID",
        "STATUS",
        "NOTE",
        "PERIOD_ID",
        "ENTRY_DATE",
        "USER_ID",
        "REF_BO_DOC_TYPE",
        "REF_BO_DOC_OID",
        "REF_BO_DOC_NUMBER",
        "REF_AISO_JOURNAL_OID",
        "REF_BO_SYSTEM",
        "REF_BO_DOC_LAST_UPDATE",
        "REF_BO_LOCATION",
        "REF_BO_TRANSACTION_TYPE",
        "CONTACT_ID",
        "REF_SUB_BO_DOC_OID"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstIjJournalMain() {
    }

    public PstIjJournalMain(int i) throws DBException {
        super(new PstIjJournalMain());
    }

    public PstIjJournalMain(String sOid) throws DBException {
        super(new PstIjJournalMain());
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstIjJournalMain(long lOid) throws DBException {
        super(new PstIjJournalMain());
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
        return TBL_IJ_JOURNAL_MAIN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstIjJournalMain().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        IjJournalMain ijjournalmain = fetchExc(ent.getOID());
        ent = (Entity) ijjournalmain;
        return ijjournalmain.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((IjJournalMain) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((IjJournalMain) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static IjJournalMain fetchExc(long oid) throws DBException {
        try {
            IjJournalMain ijjournalmain = new IjJournalMain();
            PstIjJournalMain pstIjJournalMain = new PstIjJournalMain(oid);
            ijjournalmain.setOID(oid);

            ijjournalmain.setJurTransDate(pstIjJournalMain.getDate(FLD_TRANSACTION_DATE));
            ijjournalmain.setJurBookType(pstIjJournalMain.getlong(FLD_BOOK_TYPE));
            ijjournalmain.setJurTransCurrency(pstIjJournalMain.getlong(FLD_CURRENCY_ID));
            ijjournalmain.setJurStatus(pstIjJournalMain.getInt(FLD_STATUS));
            ijjournalmain.setJurDesc(pstIjJournalMain.getString(FLD_NOTE));
            ijjournalmain.setJurPeriod(pstIjJournalMain.getlong(FLD_PERIOD));
            ijjournalmain.setJurEntryDate(pstIjJournalMain.getDate(FLD_ENTRY_DATE));
            ijjournalmain.setJurUser(pstIjJournalMain.getlong(FLD_USER));
            ijjournalmain.setRefBoDocType(pstIjJournalMain.getInt(FLD_REF_BO_DOC_TYPE));
            ijjournalmain.setRefBoDocOid(pstIjJournalMain.getlong(FLD_REF_BO_DOC_OID));
            ijjournalmain.setRefSubBoDocOid(pstIjJournalMain.getlong(FLD_REF_SUB_BO_DOC_OID));
            ijjournalmain.setRefBoDocNumber(pstIjJournalMain.getString(FLD_REF_BO_DOC_NUMBER));
            ijjournalmain.setRefAisoJournalOid(pstIjJournalMain.getlong(FLD_REF_AISO_JOURNAL_OID));
            ijjournalmain.setRefBoSystem(pstIjJournalMain.getInt(FLD_REF_BO_SYSTEM));
            try {
                ijjournalmain.setRefBoDocLastUpdate(pstIjJournalMain.getDate(FLD_REF_BO_DOC_LAST_UPDATE));
            } catch (Exception e) {
                System.out.println("ijjournalmain.setRefBoDocLastUpdate : " + e.toString());
            }
            ijjournalmain.setRefBoLocation(pstIjJournalMain.getlong(FLD_REF_BO_LOCATION));
            ijjournalmain.setRefBoTransacTionType(pstIjJournalMain.getInt(FLD_REF_BO_TRANSACTION_TYPE));
            ijjournalmain.setContactOid(pstIjJournalMain.getlong(FLD_CONTACT_ID));

            return ijjournalmain;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjJournalMain(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(IjJournalMain ijjournalmain) throws DBException {
        try {
            PstIjJournalMain pstIjJournalMain = new PstIjJournalMain(0);

            pstIjJournalMain.setDate(FLD_TRANSACTION_DATE, ijjournalmain.getJurTransDate());
            pstIjJournalMain.setLong(FLD_BOOK_TYPE, ijjournalmain.getJurBookType());
            pstIjJournalMain.setLong(FLD_CURRENCY_ID, ijjournalmain.getJurTransCurrency());
            pstIjJournalMain.setInt(FLD_STATUS, ijjournalmain.getJurStatus());
            pstIjJournalMain.setString(FLD_NOTE, ijjournalmain.getJurDesc());
            pstIjJournalMain.setLong(FLD_PERIOD, ijjournalmain.getJurPeriod());
            pstIjJournalMain.setDate(FLD_ENTRY_DATE, ijjournalmain.getJurEntryDate());
            pstIjJournalMain.setLong(FLD_USER, ijjournalmain.getJurUser());
            pstIjJournalMain.setInt(FLD_REF_BO_DOC_TYPE, ijjournalmain.getRefBoDocType());
            pstIjJournalMain.setLong(FLD_REF_BO_DOC_OID, ijjournalmain.getRefBoDocOid());
            pstIjJournalMain.setLong(FLD_REF_SUB_BO_DOC_OID, ijjournalmain.getRefSubBoDocOid());
            pstIjJournalMain.setString(FLD_REF_BO_DOC_NUMBER, ijjournalmain.getRefBoDocNumber());
            pstIjJournalMain.setLong(FLD_REF_AISO_JOURNAL_OID, ijjournalmain.getRefAisoJournalOid());
            pstIjJournalMain.setInt(FLD_REF_BO_SYSTEM, ijjournalmain.getRefBoSystem());
            pstIjJournalMain.setDate(FLD_REF_BO_DOC_LAST_UPDATE, ijjournalmain.getRefBoDocLastUpdate());
            pstIjJournalMain.setLong(FLD_REF_BO_LOCATION, ijjournalmain.getRefBoLocation());
            pstIjJournalMain.setInt(FLD_REF_BO_TRANSACTION_TYPE, ijjournalmain.getRefBoTransacTionType());
            pstIjJournalMain.setLong(FLD_CONTACT_ID, ijjournalmain.getContactOid());

            pstIjJournalMain.insert();
            ijjournalmain.setOID(pstIjJournalMain.getlong(FLD_IJ_JOURNAL_MAIN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjJournalMain(0), DBException.UNKNOWN);
        }
        return ijjournalmain.getOID();
    }

    public static long updateExc(IjJournalMain ijjournalmain) throws DBException {
        try {
            if (ijjournalmain.getOID() != 0) {
                PstIjJournalMain pstIjJournalMain = new PstIjJournalMain(ijjournalmain.getOID());

                pstIjJournalMain.setDate(FLD_TRANSACTION_DATE, ijjournalmain.getJurTransDate());
                pstIjJournalMain.setLong(FLD_BOOK_TYPE, ijjournalmain.getJurBookType());
                pstIjJournalMain.setLong(FLD_CURRENCY_ID, ijjournalmain.getJurTransCurrency());
                pstIjJournalMain.setInt(FLD_STATUS, ijjournalmain.getJurStatus());
                pstIjJournalMain.setString(FLD_NOTE, ijjournalmain.getJurDesc());
                pstIjJournalMain.setLong(FLD_PERIOD, ijjournalmain.getJurPeriod());
                pstIjJournalMain.setDate(FLD_ENTRY_DATE, ijjournalmain.getJurEntryDate());
                pstIjJournalMain.setLong(FLD_USER, ijjournalmain.getJurUser());
                pstIjJournalMain.setInt(FLD_REF_BO_DOC_TYPE, ijjournalmain.getRefBoDocType());
                pstIjJournalMain.setLong(FLD_REF_BO_DOC_OID, ijjournalmain.getRefBoDocOid());
                pstIjJournalMain.setLong(FLD_REF_SUB_BO_DOC_OID, ijjournalmain.getRefSubBoDocOid());
                pstIjJournalMain.setString(FLD_REF_BO_DOC_NUMBER, ijjournalmain.getRefBoDocNumber());
                pstIjJournalMain.setLong(FLD_REF_AISO_JOURNAL_OID, ijjournalmain.getRefAisoJournalOid());
                pstIjJournalMain.setInt(FLD_REF_BO_SYSTEM, ijjournalmain.getRefBoSystem());
                pstIjJournalMain.setDate(FLD_REF_BO_DOC_LAST_UPDATE, ijjournalmain.getRefBoDocLastUpdate());
                pstIjJournalMain.setLong(FLD_REF_BO_LOCATION, ijjournalmain.getRefBoLocation());
                pstIjJournalMain.setInt(FLD_REF_BO_TRANSACTION_TYPE, ijjournalmain.getRefBoTransacTionType());
                pstIjJournalMain.setLong(FLD_CONTACT_ID, ijjournalmain.getContactOid());

                pstIjJournalMain.update();
                return ijjournalmain.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjJournalMain(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstIjJournalMain pstIjJournalMain = new PstIjJournalMain(oid);
            pstIjJournalMain.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjJournalMain(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_IJ_JOURNAL_MAIN;
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

            /*if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;

             */


//            System.out.println("Sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                IjJournalMain ijjournalmain = new IjJournalMain();
                resultToObject(rs, ijjournalmain);
                lists.add(ijjournalmain);
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

    private static void resultToObject(ResultSet rs, IjJournalMain ijjournalmain) {
        try {
            ijjournalmain.setOID(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID]));
            ijjournalmain.setJurTransDate(rs.getDate(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_TRANSACTION_DATE]));
            ijjournalmain.setJurBookType(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_BOOK_TYPE]));
            ijjournalmain.setJurTransCurrency(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CURRENCY_ID]));
            ijjournalmain.setJurStatus(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_STATUS]));
            ijjournalmain.setJurDesc(rs.getString(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_NOTE]));
            ijjournalmain.setJurPeriod(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_PERIOD]));
            ijjournalmain.setJurEntryDate(rs.getDate(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_ENTRY_DATE]));
            ijjournalmain.setJurUser(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_USER]));
            ijjournalmain.setRefBoDocType(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_TYPE]));
            ijjournalmain.setRefBoDocOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_OID]));
            ijjournalmain.setRefSubBoDocOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_SUB_BO_DOC_OID]));
            ijjournalmain.setRefBoDocNumber(rs.getString(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER]));
            ijjournalmain.setRefAisoJournalOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_AISO_JOURNAL_OID]));
            ijjournalmain.setRefBoSystem(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_SYSTEM]));
            ijjournalmain.setRefBoDocLastUpdate(rs.getDate(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_LAST_UPDATE]));
            ijjournalmain.setRefBoLocation(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_LOCATION]));
            ijjournalmain.setRefBoTransacTionType(rs.getInt(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_TRANSACTION_TYPE]));
            ijjournalmain.setContactOid(rs.getLong(PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_CONTACT_ID]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long ijMapAccountId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_IJ_JOURNAL_MAIN +
                    " WHERE " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID] +
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
            String sql = "SELECT COUNT(" + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_IJ_JOURNAL_MAIN_ID] + ") FROM " + TBL_IJ_JOURNAL_MAIN;
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
                    IjJournalMain ijjournalmain = (IjJournalMain) list.get(ls);
                    if (oid == ijjournalmain.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    /**
     * @param transactionDate
     * @return
     * @created by Edhy
     */
    public static IjJournalMain getIjJournalMain(long oidIjJournalMain) {
        IjJournalMain objIjJournalMain = new IjJournalMain();

        try {
            // pencarian object IjJournalMain
            objIjJournalMain = PstIjJournalMain.fetchExc(oidIjJournalMain);

            // proses ngambil jurnal detail
            String whDetail = PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_JOURNAL_MAIN_ID] + "=" + objIjJournalMain.getOID();
            String ordDetail = PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_DEBT_VALUE] + " DESC" +
                    ", " + PstIjJournalDetail.fieldNames[PstIjJournalDetail.FLD_CURRENCY_ID];
            Vector listJournalDetail = PstIjJournalDetail.list(0, 0, whDetail, ordDetail);

            // proses pencarian sub ledger
            if (listJournalDetail != null && listJournalDetail.size() > 0) {
                int maxJournalDetail = listJournalDetail.size();
                for (int i = 0; i < maxJournalDetail; i++) {
                    IjJournalDetail objIjJournalDetail = (IjJournalDetail) listJournalDetail.get(i);

                    // set data detail ke jurnal main
                    objIjJournalMain.setListOfDetails(listJournalDetail);
                }
            }
        } catch (Exception e) {
            System.out.println(".::ERR => Exception when fetch IjJournalMain : " + e.toString());
        }

        return objIjJournalMain;
    }


    /**
     * Get OID of IjJournalMain object for specified 'RefBoDocOID' and 'RefBoDocNumber'
     * @param lRefBoDocOid
     * @param lRefBoDocNumber
     * @return
     * @created by Edhy
     */
    public static long getIjJournalMainOID(long lRefBoDocOid, String lRefBoDocNumber) {
        long lResult = 0;

        String sWhereClause = PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_OID] +
                " = " + lRefBoDocOid +
                " AND " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER] +
                " = '" + lRefBoDocNumber + "'";

        Vector vListOfIjJournal = PstIjJournalMain.list(0, 0, sWhereClause, "");
        if (vListOfIjJournal != null && vListOfIjJournal.size() > 0) {
            IjJournalMain objIjJournalMain = (IjJournalMain) vListOfIjJournal.get(0);
            lResult = objIjJournalMain.getOID();
        }

        return lResult;
    }

    /**
     * gadnyana
     * get oid journal main berdasarkan oidRef, doc number , description
     * @param lRefBoDocOid
     * @param lRefBoDocNumber
     * @return
     */
    public static long getIjJournalMainOID(long lRefBoDocOid, String lRefBoDocNumber, String refDesc) {
        long lResult = 0;

        String sWhereClause = PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_OID] +
                " = " + lRefBoDocOid +
                " AND " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_REF_BO_DOC_NUMBER] +
                " = '" + lRefBoDocNumber + "'"+
                " AND " + PstIjJournalMain.fieldNames[PstIjJournalMain.FLD_NOTE] +
                " = '" + refDesc + "'";

        Vector vListOfIjJournal = PstIjJournalMain.list(0, 0, sWhereClause, "");
        if (vListOfIjJournal != null && vListOfIjJournal.size() > 0) {
            IjJournalMain objIjJournalMain = (IjJournalMain) vListOfIjJournal.get(0);
            lResult = objIjJournalMain.getOID();
        }

        return lResult;
    }

    /**
     * @param objIjJournalMain
     * @param vectOfObjIjJurDetail
     * @return
     */
    public static long generateIjJournal(IjJournalMain objIjJournalMain, Vector vectOfObjIjJurDetail) {
        long lResult = 0;

        // proses jurnal main
        try {
            long oidJournalMain = 0;
            System.out.println("objIjJournalMain.getRefBoDocOid()objIjJournalMain.getRefBoDocOid() : "+objIjJournalMain.getRefBoDocOid());
            long oidOfIjJournalMain = PstIjJournalMain.getIjJournalMainOID(objIjJournalMain.getRefBoDocOid(), objIjJournalMain.getRefBoDocNumber(), objIjJournalMain.getJurDesc());
            if (oidOfIjJournalMain != 0) {
                objIjJournalMain.setOID(oidOfIjJournalMain);
                oidJournalMain = PstIjJournalMain.updateExc(objIjJournalMain);
                PstIjJournalDetail.deleteItemJournal(oidJournalMain);
                lResult = oidJournalMain;
            } else {
                oidJournalMain = PstIjJournalMain.insertExc(objIjJournalMain);
                lResult = oidJournalMain;
            }

            // proses jurnal detail
            if (vectOfObjIjJurDetail != null && vectOfObjIjJurDetail.size() > 0) {
                int maxDetail = vectOfObjIjJurDetail.size();
                for (int it = 0; it < maxDetail; it++) {
                    IjJournalDetail objDetail = (IjJournalDetail) vectOfObjIjJurDetail.get(it);

                    long oidJournalDetail = 0;
                    long oidOfIjJournalDetail = PstIjJournalDetail.getIjJournalDetailOID(oidJournalMain, objDetail.getJdetAccChart());
                    if (oidOfIjJournalDetail != 0) {

                        // gadnyana,
                        // jika account sama maka total nilai nya di gabung
                        IjJournalDetail objDetailExist = new IjJournalDetail();
                        try {
                            objDetailExist = PstIjJournalDetail.fetchExc(oidOfIjJournalDetail);
                        } catch (Exception e) {
                        }
                        if (objDetail.getJdetDebet() != 0) {
                            objDetail.setJdetDebet(objDetail.getJdetDebet() + objDetailExist.getJdetDebet());
                        } else {
                            objDetail.setJdetCredit(objDetail.getJdetCredit() + objDetailExist.getJdetCredit());
                        }
                        // -------

                        objDetail.setOID(oidOfIjJournalDetail);
                        objDetail.setJdetMainOid(oidJournalMain);
                        oidJournalDetail = PstIjJournalDetail.updateExc(objDetail);
                    } else {
                        objDetail.setJdetMainOid(oidJournalMain);
                        System.out.println("oidJournalDetail============= objDetail.>>>>>>>>>>>>>>>>>>> : "+objDetail.getJdetMainOid());
                        oidJournalDetail = PstIjJournalDetail.insertExc(objDetail);
                        System.out.println("oidJournalDetail============= >>>>>>>>>>>>>>>>>>>>>>>> : "+oidJournalDetail);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(new PstIjJournalMain().getClass().getName() + ".generateIjJournal() exc : " + e.toString());
        }

        return lResult;
    }


    /**
     * check if current object is the same version with object on Back Office
     * @param objIjJournalMain
     * @return
     */
    public boolean isLastVersionObject(IjJournalMain objIjJournalMain) {
        String sLastUpdateDate = Formater.formatDate(objIjJournalMain.getRefBoDocLastUpdate(), "yyyy-MM-dd HH:mm:ss");
        //System.out.println("===>>> objIjJournalMain.getRefBoDocLastUpdate() : "+objIjJournalMain.getRefBoDocLastUpdate());
        //System.out.println("===>>> : "+Formater.formatDate(objIjJournalMain.getRefBoDocLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
        System.out.println("objIjJournalMain.getRefBoDocNumber() " + objIjJournalMain.getRefBoDocNumber());
        System.out.println("objIjJournalMain.getRefBoDocType() " + objIjJournalMain.getRefBoDocType());
        System.out.println("sLastUpdateDate " + sLastUpdateDate);
        String sWhereClause = PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_NUMBER] + " = '" + objIjJournalMain.getRefBoDocNumber() + "'" +
                " AND " + PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_TYPE] + " = " + objIjJournalMain.getRefBoDocType() +
                " AND " + PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_DATE] + " = '" + sLastUpdateDate + "'"+
                " AND " + PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_OID] + " = " + objIjJournalMain.getRefBoDocOid();

        System.out.println("\n\n---- isLastVersionObject : " + sWhereClause + "\n");

        Vector vResult = PstDocLogger.list(0, 0, sWhereClause, "");
        if (vResult != null && vResult.size() > 0) {
            return true;
        }

        return false;
    }

}
