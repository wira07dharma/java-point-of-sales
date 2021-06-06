/* Created on 	:  [date] [time] AM/PM
*
* @author	 :
* @version	 :
*/

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.entity.payment;

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

public class PstDailyRate extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_DAILY_RATE = "daily_rate";

    public static final int FLD_DAILY_RATE_ID = 0;
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

    public PstDailyRate() {
    }

    public PstDailyRate(int i) throws DBException {
        super(new PstDailyRate());
    }

    public PstDailyRate(String sOid) throws DBException {
        super(new PstDailyRate(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstDailyRate(long lOid) throws DBException {
        super(new PstDailyRate(0));
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
        return TBL_POS_DAILY_RATE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDailyRate().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        DailyRate dailyrate = fetchExc(ent.getOID());
        ent = (Entity) dailyrate;
        return dailyrate.getOID();
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

    public static DailyRate fetchExc(long oid) throws DBException {
        try {
            DailyRate dailyrate = new DailyRate();
            PstDailyRate pstDailyRate = new PstDailyRate(oid);
            dailyrate.setOID(oid);

            dailyrate.setCurrencyTypeId(pstDailyRate.getlong(FLD_CURRENCY_TYPE_ID));
            dailyrate.setSellingRate(pstDailyRate.getdouble(FLD_SELLING_RATE));
            dailyrate.setRosterDate(pstDailyRate.getDate(FLD_ROSTER_DATE));

            return dailyrate;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDailyRate(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(DailyRate dailyrate) throws DBException {
        try {
            PstDailyRate pstDailyRate = new PstDailyRate(0);

            pstDailyRate.setLong(FLD_CURRENCY_TYPE_ID, dailyrate.getCurrencyTypeId());
            pstDailyRate.setDouble(FLD_SELLING_RATE, dailyrate.getSellingRate());
            pstDailyRate.setDate(FLD_ROSTER_DATE, dailyrate.getRosterDate());

            pstDailyRate.insert();
            dailyrate.setOID(pstDailyRate.getlong(FLD_DAILY_RATE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDailyRate(0), DBException.UNKNOWN);
        }
        return dailyrate.getOID();
    }

    public static long updateExc(DailyRate dailyrate) throws DBException {
        try {
            if (dailyrate.getOID() != 0) {
                PstDailyRate pstDailyRate = new PstDailyRate(dailyrate.getOID());

                pstDailyRate.setLong(FLD_CURRENCY_TYPE_ID, dailyrate.getCurrencyTypeId());
                pstDailyRate.setDouble(FLD_SELLING_RATE, dailyrate.getSellingRate());
                pstDailyRate.setDate(FLD_ROSTER_DATE, dailyrate.getRosterDate());

                pstDailyRate.update();
                return dailyrate.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDailyRate(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstDailyRate pstDailyRate = new PstDailyRate(oid);
            pstDailyRate.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDailyRate(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_POS_DAILY_RATE;
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
                DailyRate dailyrate = new DailyRate();
                resultToObject(rs, dailyrate);
                lists.add(dailyrate);
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

    private static void resultToObject(ResultSet rs, DailyRate dailyrate) {
        try {
            dailyrate.setOID(rs.getLong(PstDailyRate.fieldNames[PstDailyRate.FLD_DAILY_RATE_ID]));
            dailyrate.setCurrencyTypeId(rs.getLong(PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]));
            dailyrate.setSellingRate(rs.getDouble(PstDailyRate.fieldNames[PstDailyRate.FLD_SELLING_RATE]));
            Date dtRoster = DBHandler.convertDate(rs.getDate(PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]), rs.getTime(PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]));
            dailyrate.setRosterDate(dtRoster);

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long dailyRateId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_DAILY_RATE + " WHERE " +
                    PstDailyRate.fieldNames[PstDailyRate.FLD_DAILY_RATE_ID] + " = " + dailyRateId;

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
            String sql = "SELECT COUNT(" + PstDailyRate.fieldNames[PstDailyRate.FLD_DAILY_RATE_ID] + ") FROM " + TBL_POS_DAILY_RATE;
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
                    " FROM " + TBL_POS_DAILY_RATE + " AS DY " +
                    " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " AS CURR" +
                    " ON DY." + PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID] +
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
                DailyRate dailyrate = new DailyRate();
                resultToObject(rs, dailyrate);
                lists.add(dailyrate);
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
    * @return
    *alogritmanya :
    * 1. Setelah user menyeting curency harian yang di pergunakan berdasarkan tanggal dan aktifnya.
    * 2. List yang di munculkan di Running Rate ada rate yang berada pada jam server.
    * exp : contoh ada 2 harian rate -> a. yang pertama rate 12000 di set tanggal 11-05-2013 jam 11:00:00
    *                                   b. yang kedua rate 11500 di set tanggal 11-05-2013 jam 13:00:00
    * Maka pada saat di server menunjukan tanggal 11-05-2013 jam 11:30:00 maka yang muncul adalah rate 12000
    * Tapi jika di server menunjukan tanggal 11-05-2013 jam 13:10:00 maka yang muncul rate 11500
    * Query :
    * SELECT * FROM daily_rate WHERE CURRENCY_TYPE_ID=504404272577831676 AND ROSTER_DATE <= (NOW()) ORDER BY ROSTER_DATE DESC LIMIT 0,1;
    */
    public static Vector getCurrentDailyRate() {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            Vector vCurr = PstCurrencyType.list(0, 0, "", PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]);
            if (vCurr != null && vCurr.size() > 0) {
                for (int k = 0; k < vCurr.size(); k++) {
                    CurrencyType currType = (CurrencyType) vCurr.get(k);
                    String where =  fieldNames[FLD_CURRENCY_TYPE_ID] + "=" + currType.getOID()+" AND " + fieldNames[FLD_ROSTER_DATE]+"<=(NOW())";
                    Vector listRate = list(0, 1,where, fieldNames[FLD_ROSTER_DATE] + " DESC ");
                    if (listRate!=null && listRate.size() > 0) {
                        DailyRate dailyrate = (DailyRate) listRate.get(0);
                        lists.add(dailyrate);
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
     * mencari dailyrate yang berlaku di sales
     * @return
     */
     public static double getCurrentDailyRateSales(long oid) {
        double result = 0;
        DBResultSet dbrs = null;
        try {
                    String where =  fieldNames[FLD_CURRENCY_TYPE_ID] + "=" + oid+" AND " + fieldNames[FLD_ROSTER_DATE]+"<=(NOW())";
                    Vector listRate = list(0, 1,where, fieldNames[FLD_ROSTER_DATE] + " DESC ");
                    if (listRate!=null && listRate.size() > 0) {
                        DailyRate dailyrate = (DailyRate) listRate.get(0);
                        result = dailyrate.getSellingRate();
                    }
            return result;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * Fungsi ini digunakan untuk mendapatkan list Daily Rate yanng berstatus RUNNING
     * @param
     * @return HashTable yang berisi list dari Daily Rate berstatus RUNNING
     */
    public static Hashtable getHashCurrentDailyRate() {
        Hashtable objHashtable = new Hashtable();
        Vector vctCurrenctDailyRate = PstDailyRate.getCurrentDailyRate();
        
        for(int i=0; i<vctCurrenctDailyRate.size(); i++) {
            DailyRate objDailyRate = (DailyRate)vctCurrenctDailyRate.get(i);
            objHashtable.put(""+objDailyRate.getCurrencyTypeId(), ""+objDailyRate.getSellingRate());
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
            String sql = "UPDATE " + TBL_POS_DAILY_RATE +
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
