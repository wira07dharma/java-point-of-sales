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

package com.dimata.common.entity.periode;

import java.sql.*;
import java.util.*; 
import java.util.Date;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.I_PersistentExcSynch;
import com.dimata.qdep.entity.Entity;


public class PstPeriode extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_PersistentExcSynch {

    public static final String TBL_STOCK_PERIODE = "periode";

    public static final int FLD_STOCK_PERIODE_ID = 0;
    public static final int FLD_PERIODE_TYPE = 1;
    public static final int FLD_PERIODE_NAME = 2;
    public static final int FLD_START_DATE = 3;
    public static final int FLD_END_DATE = 4;
    public static final int FLD_STATUS = 5;
    public static final int FLD_LAST_ENTRY = 6;

    public static final String[] fieldNames = {
        "PERIODE_ID",
        "PERIODE_TYPE",
        "PERIODE_NAME",
        "START_DATE",
        "END_DATE",
        "STATUS",
        "LAST_ENTRY"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_DATE
    };

    public static final int MAT_PERIODE_MONTHLY = 0;
    public static final int MAT_PERIODE_THREEMONTH = 1;
    public static final int MAT_PERIODE_FOURMONTH = 2;
    public static final int MAT_PERIODE_SIXMONTH = 3;
    public static final int MAT_PERIODE_ANNUAL = 4;
    public static final int[] matPeriodTypeValues = {1, 3, 4, 6, 12};
    public static final String[] matPeriodTypeNames = {
        "Monthly",
        "Three Month",
        "Four Month",
        "Six Month",
        "Annual"
    };

    public static Vector getVectPeriodTypes() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < matPeriodTypeNames.length; i++) {
            result.add(String.valueOf(matPeriodTypeNames[i]));
        }
        return result;
    }

    public static final int FLD_STATUS_CLOSED = 0;
    public static final int FLD_STATUS_PREPARE = 1;
    public static final int FLD_STATUS_RUNNING = 2;
    public static final String[] statusPeriode = {
        "Closed",
        "Prepare Running",
        "Running"
    };

    public static int NO_ERR = 0;
    public static int ERR_START_DATE = 1;
    public static int ERR_DUE_DATE = 2;

    public static String[][] errorText = {
        {" ", "Tanggal awal tidak sesuai", "Tanggal akhir tidak sesuai"},
        {" ", "Start date invalid", "Due date invalid"}
    };

    public static Vector getVectPeriodStatus() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < statusPeriode.length; i++) {
            result.add(String.valueOf(statusPeriode[i]));
        }
        return result;
    }

    public static final int START_DATE = 0;
    public static final int END_DATE = 1;

    public PstPeriode() {
    }

    public PstPeriode(int i) throws DBException {
        super(new PstPeriode());
    }

    public PstPeriode(String sOid) throws DBException {
        super(new PstPeriode(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPeriode(long lOid) throws DBException {
        super(new PstPeriode(0));
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
        return TBL_STOCK_PERIODE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPeriode().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        try {
            Periode periode = (Periode) ent;
            PstPeriode pstPeriode = new PstPeriode(ent.getOID());
            periode.setOID(ent.getOID());

            periode.setPeriodeType(pstPeriode.getInt(FLD_PERIODE_TYPE));
            periode.setPeriodeName(pstPeriode.getString(FLD_PERIODE_NAME));
            periode.setStartDate(pstPeriode.getDate(FLD_START_DATE));
            periode.setEndDate(pstPeriode.getDate(FLD_END_DATE));
            periode.setStatus(pstPeriode.getInt(FLD_STATUS));
            periode.setLastEntry(pstPeriode.getDate(FLD_LAST_ENTRY));

            return ent.getOID();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPeriode(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Periode) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Periode) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Periode fetchExc(long oid) throws DBException {
        try {
            Periode periode = new Periode();
            PstPeriode pstPeriode = new PstPeriode(oid);
            periode.setOID(oid);

            periode.setPeriodeType(pstPeriode.getInt(FLD_PERIODE_TYPE));
            periode.setPeriodeName(pstPeriode.getString(FLD_PERIODE_NAME));
            periode.setStartDate(pstPeriode.getDate(FLD_START_DATE));
            periode.setEndDate(pstPeriode.getDate(FLD_END_DATE));
            periode.setStatus(pstPeriode.getInt(FLD_STATUS));
            periode.setLastEntry(pstPeriode.getDate(FLD_LAST_ENTRY));

            return periode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPeriode(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Periode periode) throws DBException {
        try {
            PstPeriode pstPeriode = new PstPeriode(0);

            pstPeriode.setInt(FLD_PERIODE_TYPE, periode.getPeriodeType());
            pstPeriode.setString(FLD_PERIODE_NAME, periode.getPeriodeName());
            pstPeriode.setDate(FLD_START_DATE, periode.getStartDate());
            pstPeriode.setDate(FLD_END_DATE, periode.getEndDate());
            pstPeriode.setInt(FLD_STATUS, periode.getStatus());
            pstPeriode.setDate(FLD_LAST_ENTRY, periode.getLastEntry());

            pstPeriode.insert();
            periode.setOID(pstPeriode.getlong(FLD_STOCK_PERIODE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPeriode(0), DBException.UNKNOWN);
        }
        return periode.getOID();
    }

    public static long updateExc(Periode periode) throws DBException {
        try {
            if (periode.getOID() != 0) {
                PstPeriode pstPeriode = new PstPeriode(periode.getOID());

                pstPeriode.setInt(FLD_PERIODE_TYPE, periode.getPeriodeType());
                pstPeriode.setString(FLD_PERIODE_NAME, periode.getPeriodeName());
                pstPeriode.setDate(FLD_START_DATE, periode.getStartDate());
                pstPeriode.setDate(FLD_END_DATE, periode.getEndDate());
                pstPeriode.setInt(FLD_STATUS, periode.getStatus());
                pstPeriode.setDate(FLD_LAST_ENTRY, periode.getLastEntry());

                pstPeriode.update();
                return periode.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPeriode(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPeriode pstPeriode = new PstPeriode(oid);
            pstPeriode.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPeriode(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_STOCK_PERIODE;
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
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Periode periode = new Periode();
                resultToObject(rs, periode);
                lists.add(periode);
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

    public static void resultToObject(ResultSet rs, Periode periode) {
        try {
            periode.setOID(rs.getLong(PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID]));
            periode.setPeriodeType(rs.getInt(PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_TYPE]));
            periode.setPeriodeName(rs.getString(PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_NAME]));
            periode.setStartDate(rs.getDate(PstPeriode.fieldNames[PstPeriode.FLD_START_DATE]));
            periode.setEndDate(rs.getDate(PstPeriode.fieldNames[PstPeriode.FLD_END_DATE]));
            periode.setStatus(rs.getInt(PstPeriode.fieldNames[PstPeriode.FLD_STATUS]));
            periode.setLastEntry(rs.getDate(PstPeriode.fieldNames[PstPeriode.FLD_LAST_ENTRY]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long mcdStockPeriodeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_STOCK_PERIODE + " WHERE " +
                    PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] + " = " + mcdStockPeriodeId;

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
            String sql = "SELECT COUNT(" + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] + ") FROM " + TBL_STOCK_PERIODE;
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

    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    Periode periode = (Periode) list.get(ls);
                    if (oid == periode.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0)
            cmd = Command.FIRST;
        else {
            if (start == (vectSize - recordToGet))
                cmd = Command.LAST;
            else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                    }
                }
            }
        }
        return cmd;
    }

    /**
     * this method used to check period
     * if period doesn't exist or the date is valid date to update period ---> create new period
     */
    public static void cekPeriode(Date date, int periodType) {
        long oidPeriode = 0;
        try {
            oidPeriode = cekExistPeriode(PstPeriode.FLD_STATUS_RUNNING);
            if (oidPeriode != 0) {
                Periode periode = new Periode();
                try {
                    periode = PstPeriode.fetchExc(oidPeriode);
                } catch (Exception e) {
                }
                if (cekEndDate(periodType, date, periode.getEndDate())) {
                    insertPeriode(periodType, PstPeriode.FLD_STATUS_PREPARE, date, PstPeriode.FLD_STATUS_PREPARE);
                } else {
                    long oidPrepare = cekExistPeriode(PstPeriode.FLD_STATUS_PREPARE);
                    if (oidPrepare != 0) {
                        Periode prepare = new Periode();
                        try {
                            prepare = PstPeriode.fetchExc(oidPrepare);
                        } catch (Exception e) {
                        }
                        if (cekEndDate(periodType, date, prepare.getStartDate())) {
                            updatePeriode(oidPeriode, PstPeriode.FLD_STATUS_CLOSED);
                            updatePeriode(oidPrepare, PstPeriode.FLD_STATUS_RUNNING);
                        }
                    }
                }
            } else {
                insertPeriode(periodType, PstPeriode.FLD_STATUS_RUNNING, date, PstPeriode.FLD_STATUS_RUNNING);
            }
        } catch (Exception e) {
        }
    }

    /**
     * this method used to check if period exist or not
     * if exist ---> return periodId otherwise return 0
     */
    public static long cekExistPeriode(int status) {
        DBResultSet dbrs = null;
        boolean result = false;
        long oidPeriode = 0;
        try {
            String sql = "SELECT * FROM " + PstPeriode.TBL_STOCK_PERIODE + " WHERE " +
                    PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + " = " + status;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                oidPeriode = rs.getLong(PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID]);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return oidPeriode;
        }
    }

    /**
     * this method used to create new period
     */
    public static long insertPeriode(int periodType, int status, Date date, int typeDate) {
        long oid = 0;
        String monthText[] = {
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
        };

        Periode periode = new Periode();
        periode.setPeriodeType(periodType);
        periode.setStartDate(getDateStartEnd(periodType, START_DATE, date, typeDate));
        periode.setEndDate(getDateStartEnd(periodType, END_DATE, date, typeDate));
        periode.setStatus(status);

        Date startDate = periode.getStartDate();
        Date endDate = periode.getEndDate();
        if (periodType != PstPeriode.MAT_PERIODE_MONTHLY) {
            periode.setPeriodeName("Period " + monthText[startDate.getMonth()] + " " + (1900 + startDate.getYear()) + " - " + monthText[endDate.getMonth()] + " " + (1900 + endDate.getYear()));
        } else {
            periode.setPeriodeName("Period " + monthText[startDate.getMonth()] + " " + (1900 + startDate.getYear()));
        }

        try {
            oid = PstPeriode.insertExc(periode);
        } catch (Exception e) {
        }
        return oid;
    }
    
    public static long getPeriodeNow(Date date){
      DBResultSet dbrs = null;
      boolean result = false;
      long oid = 0;
      try {            
        String sql = "SELECT * FROM " + PstPeriode.TBL_STOCK_PERIODE + " AS P WHERE "
               + "TO_DAYS(P." +     PstPeriode.fieldNames[PstPeriode.FLD_START_DATE] + ") <= TO_DAYS('" + Formater.formatDate(date, "yyyy-MM-dd")
                + "') AND TO_DAYS(P." + PstPeriode.fieldNames[PstPeriode.FLD_END_DATE]+ ") >= TO_DAYS('" + Formater.formatDate(date, "yyyy-MM-dd") + "')";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                oid = rs.getLong(PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID]);
            }
            rs.close();
      } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return oid;
        }
    }
    
    public static long getPeriodeBefore(Date date){
      DBResultSet dbrs = null;
      boolean result = false;
      long oid = 0;
      try {            
        String sql = "SELECT * FROM " + PstPeriode.TBL_STOCK_PERIODE + " AS P WHERE "
                + "P."+PstPeriode.fieldNames[PstPeriode.FLD_START_DATE]+ " < '"+date+"'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                oid = rs.getLong(PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID]);
            }
            rs.close();
      } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return oid;
        }
    }

    /**
     * this method used to get start date or end date depend on typeDate
     */
    public static Date getDateStartEnd(int periodType, int typeDate, Date date, int tpDate) {
        Date result = null;
        Calendar newCalendar = Calendar.getInstance();

        if (typeDate == START_DATE) {
            if (tpDate == PstPeriode.FLD_STATUS_PREPARE) {
                result = new Date(date.getYear(), date.getMonth(), date.getDate() + 1);
            } else {
                result = new Date(date.getYear(), date.getMonth(), 1);
            }
        } else {
            int monthInterval = PstPeriode.matPeriodTypeValues[periodType];
            Date newDate = null;
            if (tpDate == PstPeriode.FLD_STATUS_PREPARE) {
                newDate = new Date(date.getYear(), date.getMonth() + monthInterval, 1);
            } else {
                newDate = new Date(date.getYear(), date.getMonth() + monthInterval - 1, 1);
            }
            newCalendar.setTime(newDate);
            Date currDate = newCalendar.getTime();
            result = new Date(currDate.getYear(), currDate.getMonth(), newCalendar.getActualMaximum(newCalendar.DAY_OF_MONTH));
        }
        return result;
    }

    /**
     * this method used to check if 'date' is equal to 'newDate'
     */
    public static boolean cekEndDate(int type, Date newDate, Date date) {
        boolean bool = false;
        String strNewDate = Formater.formatDate(newDate, "dd MMMM yyyy");
        String strDate = Formater.formatDate(date, "dd MMMM yyyy");
        if (strNewDate.equals(strDate)) {
            bool = true;
        }
        return bool;
    }

    /**
     * this method used to update status of current period into 'close' status
     */
    public static long updatePeriode(long oidPeriode, int type) {
        Periode periode = new Periode();
        long oid = 0;
        try {
            try {
                periode = PstPeriode.fetchExc(oidPeriode);
            } catch (Exception e) {
            }
            periode.setOID(oidPeriode);
            periode.setStatus(type);
            try {
                oid = PstPeriode.updateExc(periode);
            } catch (Exception e) {
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        }
        return oid;
    }


    /**
     * get OID of period object that wrap selectedDate
     * @param selectedDate
     * @return
     * @created by Edhy
     */
    public static long getPeriodIdBySelectedDate(Date selectedDate) {
        long result = 0;
        DBResultSet dbrs = null;
        String strDate = "\"" + Formater.formatDate(selectedDate, "yyyy-MM-dd") + "\"";
        try {
            String sql = "SELECT " + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] +
                    " FROM " + TBL_STOCK_PERIODE +
                    " WHERE " + strDate +
                    " BETWEEN " + PstPeriode.fieldNames[PstPeriode.FLD_START_DATE] +
                    " AND " + PstPeriode.fieldNames[PstPeriode.FLD_END_DATE];
            //System.out.println("\tgetPeriodIdBySelectedDate : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }


    public static void main(String args[]) {
        Date newDate = new Date(102, 1, 28);
        Date startDate = getDateStartEnd(MAT_PERIODE_ANNUAL, START_DATE, newDate, FLD_STATUS_RUNNING);
        Date endDate = getDateStartEnd(MAT_PERIODE_ANNUAL, END_DATE, newDate, FLD_STATUS_RUNNING);
        System.out.println("new start date : " + startDate);
        System.out.println("new end date : " + endDate);
    }


    //updated by : eka
    //date : 1 feb 2004
    /***  function for data synchronization ***/
    public long insertExcSynch(Entity ent) throws Exception {
        return insertExcSynch((Periode) ent);
    }

    public static long insertExcSynch(Periode theObj) throws DBException {
        long newOID = 0;
        long originalOID = theObj.getOID();
        try {
            newOID = insertExc(theObj);
            if (newOID != 0) {  // sukses insert ?
                updateSynchOID(newOID, originalOID);
                return originalOID;
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPeriode(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + TBL_STOCK_PERIODE + " SET " +
                    PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] + " = " + originalOID +
                    " WHERE " + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] +
                    " = " + newOID;

            int Result = DBHandler.execUpdate(sql);

            return originalOID;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    /*** -------------------------- ***/


    /**
     * get object ofo current period
     * @created by Edhy
     */
    public static Periode getCurrentPeriode() {
        DBResultSet dbrs = null;
        Periode period = new Periode();
        try {
            String sql = "SELECT * " +
                    " FROM " + PstPeriode.TBL_STOCK_PERIODE +
                    " WHERE " + PstPeriode.fieldNames[PstPeriode.FLD_STATUS] +
                    " = " + FLD_STATUS_RUNNING;
            System.out.println(" sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                period.setOID(rs.getLong(PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID]));
                period.setPeriodeType(rs.getInt(PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_TYPE]));
                period.setPeriodeName(rs.getString(PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_NAME]));
                period.setStartDate(rs.getDate(PstPeriode.fieldNames[PstPeriode.FLD_START_DATE]));
                period.setEndDate(rs.getDate(PstPeriode.fieldNames[PstPeriode.FLD_END_DATE]));
                period.setStatus(rs.getInt(PstPeriode.fieldNames[PstPeriode.FLD_STATUS]));

                break;
            }
            rs.close();
            return period;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return period;
        }
    }

    public static Periode getPeriode(Date prdStartDate){
        DBResultSet dbrs = null;
        Periode materialPeriode = new Periode();
        try{
            String sql = "SELECT * FROM "+TBL_STOCK_PERIODE+
            " WHERE '"+Formater.formatDate(prdStartDate,"yyyy-MM-dd")+
            "' BETWEEN "+fieldNames[FLD_START_DATE]+" AND "+fieldNames[FLD_END_DATE];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                resultToObject(rs, materialPeriode);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("err getPreviousPeriode : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
        return materialPeriode;
    }


   public static Periode getPeriodeRunning(){
        DBResultSet dbrs = null;
        Periode materialPeriode = new Periode();
        try{
            String sql = "SELECT * FROM "+TBL_STOCK_PERIODE+
            " WHERE "+fieldNames[FLD_STATUS]+"="+FLD_STATUS_RUNNING;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                resultToObject(rs, materialPeriode);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("err getPreviousPeriode : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
        return materialPeriode;
    }

    public static long getByMonthYear(Date startDate) {
        DBResultSet dbrs = null;
        long result = 0;
        try {
            String startMonth = String.valueOf(startDate.getMonth() + 1);
            if (startMonth.length() < 2) startMonth = "0" + startMonth;
            String composeDate = String.valueOf((startDate.getYear() + 1900)) +
            "-" + (startMonth);
            String sql = " SELECT " + fieldNames[FLD_STOCK_PERIODE_ID] +
            " FROM " + TBL_STOCK_PERIODE+
            " WHERE " + fieldNames[FLD_PERIODE_NAME] +
            " = '" + composeDate + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
}
