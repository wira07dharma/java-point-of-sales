/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.entity.payment;

/**
 *
 * @author PT. Dimata
 */

/* package java */

import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;

public class PstQuickDailyRate extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    public static final String TBL_POS_QUICK_DAILY_RATE = "quick_daily_rate";

    public static final int FLD_QUICK_DAILY_RATE_ID = 0;
    public static final int FLD_CURRENCY_TYPE_ID = 1;
    public static final int FLD_SELLING_RATE = 2;
    public static final int FLD_ROSTER_DATE = 3;

    public static final String[] fieldNames = {
            "DAILY_RATE_ID",
            "CURRENCY_TYPE_ID",
            "SELLING_RATE",
            "ROSTER_DATE"
    };

    public static final int[] fieldTypes = {
            TYPE_LONG + TYPE_PK + TYPE_ID,
            TYPE_LONG,
            TYPE_FLOAT,
            TYPE_DATE
    };

    public PstQuickDailyRate() {
    }

    public PstQuickDailyRate(int i) throws DBException {
        super(new PstQuickDailyRate());
    }

    public PstQuickDailyRate(String sOid) throws DBException {
        super(new PstQuickDailyRate(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstQuickDailyRate(long lOid) throws DBException {
        super(new PstQuickDailyRate(0));
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
        return TBL_POS_QUICK_DAILY_RATE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstQuickDailyRate().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        QuickDailyRate quickdailyrate = fetchExc(ent.getOID());
        ent = (Entity) quickdailyrate;
        return quickdailyrate.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((DailyRate) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((DailyRate) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static QuickDailyRate fetchExc(long oid) throws DBException {
        try {
            QuickDailyRate quickdailyrate = new QuickDailyRate();
            PstQuickDailyRate pstQuickDailyRate = new PstQuickDailyRate(oid);
            quickdailyrate.setOID(oid);

            quickdailyrate.setCurrencyTypeId(pstQuickDailyRate.getlong(FLD_CURRENCY_TYPE_ID));
            quickdailyrate.setSellingRate(pstQuickDailyRate.getdouble(FLD_SELLING_RATE));
            quickdailyrate.setRosterDate(pstQuickDailyRate.getDate(FLD_ROSTER_DATE));

            return quickdailyrate;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstQuickDailyRate(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(QuickDailyRate quickdailyrate) throws DBException {
        try {
            PstQuickDailyRate pstQuickDailyRate = new PstQuickDailyRate(0);

            pstQuickDailyRate.setLong(FLD_CURRENCY_TYPE_ID, quickdailyrate.getCurrencyTypeId());
            pstQuickDailyRate.setDouble(FLD_SELLING_RATE, quickdailyrate.getSellingRate());
            pstQuickDailyRate.setDate(FLD_ROSTER_DATE, quickdailyrate.getRosterDate());

            pstQuickDailyRate.insert();
            quickdailyrate.setOID(pstQuickDailyRate.getlong(FLD_QUICK_DAILY_RATE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstQuickDailyRate(0), DBException.UNKNOWN);
        }
        return quickdailyrate.getOID();
    }

    public static long updateExc(QuickDailyRate quickdailyrate) throws DBException {
        try {
            if (quickdailyrate.getOID() != 0) {
                PstQuickDailyRate pstQuickDailyRate = new PstQuickDailyRate(quickdailyrate.getOID());

                pstQuickDailyRate.setLong(FLD_CURRENCY_TYPE_ID, quickdailyrate.getCurrencyTypeId());
                pstQuickDailyRate.setDouble(FLD_SELLING_RATE, quickdailyrate.getSellingRate());
                pstQuickDailyRate.setDate(FLD_ROSTER_DATE, quickdailyrate.getRosterDate());

                pstQuickDailyRate.update();
                return quickdailyrate.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstQuickDailyRate(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstQuickDailyRate pstQuickDailyRate = new PstQuickDailyRate(oid);
            pstQuickDailyRate.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstQuickDailyRate(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_POS_QUICK_DAILY_RATE;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                QuickDailyRate quickdailyrate = new QuickDailyRate();
                resultToObject(rs, quickdailyrate);
                lists.add(quickdailyrate);
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

    private static void resultToObject(ResultSet rs, QuickDailyRate quickdailyrate) {
        try {
            quickdailyrate.setOID(rs.getLong(PstQuickDailyRate.fieldNames[PstQuickDailyRate.FLD_QUICK_DAILY_RATE_ID]));
            quickdailyrate.setCurrencyTypeId(rs.getLong(PstQuickDailyRate.fieldNames[PstQuickDailyRate.FLD_CURRENCY_TYPE_ID]));
            quickdailyrate.setSellingRate(rs.getDouble(PstQuickDailyRate.fieldNames[PstQuickDailyRate.FLD_SELLING_RATE]));
            Date dtRoster = DBHandler.convertDate(rs.getDate(PstQuickDailyRate.fieldNames[PstQuickDailyRate.FLD_ROSTER_DATE]), rs.getTime(PstDailyRate.fieldNames[PstQuickDailyRate.FLD_ROSTER_DATE]));
            quickdailyrate.setRosterDate(dtRoster);

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long quickdailyRateId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_QUICK_DAILY_RATE + " WHERE " +
                    PstQuickDailyRate.fieldNames[PstQuickDailyRate.FLD_QUICK_DAILY_RATE_ID] + " = " + quickdailyRateId;

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

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstQuickDailyRate.fieldNames[PstQuickDailyRate.FLD_QUICK_DAILY_RATE_ID] + ") FROM " + TBL_POS_QUICK_DAILY_RATE;
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
                    DailyRate dailyrate = (DailyRate) list.get(ls);
                    if (oid == dailyrate.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    /**
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
    public static Vector listDailyRate(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DY.*" +
                    " FROM " + TBL_POS_QUICK_DAILY_RATE + " AS DY " +
                    " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " AS CURR" +
                    " ON DY." + PstQuickDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID] +
                    " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                QuickDailyRate quickdailyrate = new QuickDailyRate();
                resultToObject(rs, quickdailyrate);
                lists.add(quickdailyrate);
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

    public static Vector getCurrentDailyRate() {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            Vector vCurr = PstCurrencyType.list(0, 0, "", PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]);
            if (vCurr != null && vCurr.size() > 0) {
                for (int k = 0; k < vCurr.size(); k++) {
                    CurrencyType currType = (CurrencyType) vCurr.get(k);
                    Vector listRate = list(0, 0, fieldNames[FLD_CURRENCY_TYPE_ID] + "=" + currType.getOID(), fieldNames[FLD_ROSTER_DATE] + " DESC ");
                    if (listRate!=null && listRate.size() > 0) {
                        QuickDailyRate quickdailyrate = (QuickDailyRate) listRate.get(0);
                        lists.add(quickdailyrate);
                    }
                }
            }
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }


    /**
     * Fungsi ini digunakan untuk mendapatkan list Daily Rate yanng berstatus RUNNING
     * @param
     * @return HashTable yang berisi list dari Daily Rate berstatus RUNNING
     */
    public static Hashtable getHashCurrentDailyRate() {
        Hashtable objHashtable = new Hashtable();
        Vector vctCurrenctDailyRate = PstQuickDailyRate.getCurrentDailyRate();

        for(int i=0; i<vctCurrenctDailyRate.size(); i++) {
            QuickDailyRate objQuickDailyRate = (QuickDailyRate)vctCurrenctDailyRate.get(i);
            objHashtable.put(""+objQuickDailyRate.getCurrencyTypeId(), ""+objQuickDailyRate.getSellingRate());
        }

        return objHashtable;
    }


    /**
     * update oid lama dengan oid baru
     *
     * @param newOID
     * @param originalOID
     * @return
     * @throws DBException
     */
    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + TBL_POS_QUICK_DAILY_RATE +
                    " SET " + PstDailyRate.fieldNames[PstDailyRate.FLD_DAILY_RATE_ID] +
                    " = " + originalOID +
                    " WHERE " + PstDailyRate.fieldNames[PstDailyRate.FLD_DAILY_RATE_ID] +
                    " = " + newOID;
//            System.out.println(new PstDailyRate().getClass().getName() + ".updateSynchOID() sql : " + sql);
            DBHandler.execUpdate(sql);
            return originalOID;
        }
        catch (Exception e) {
            System.out.println(new PstDailyRate().getClass().getName() + ".updateSynchOID() exp : " + e.toString());
            return 0;
        }
        finally {
            DBResultSet.close(dbrs);
        }
    }

}
