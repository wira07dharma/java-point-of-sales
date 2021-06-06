/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */

/* package java */
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;

/* package garment */
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import org.json.JSONObject;

public class PstWaitingList extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_MAT_MERK = "POS_MERK";
    public static final String TBL_WAITING_LIST = "pos_waiting_list";
    public static final int FLD_CUSTOMER_WAITING_ID = 0;
    public static final int FLD_CUSTOMER_NAME = 1;
    public static final int FLD_NO_TLP = 2;
    public static final int FLD_START_TIME = 3;
    public static final int FLD_END_TIME = 4;
    public static final int FLD_REAL_END_TIME = 5;
    public static final int FLD_STATUS = 6;
    public static final int FLD_STAFF = 7;
    public static final int FLD_PAX = 8;
    public static final String[] fieldNames = {
        "CUSTOMER_WAITING_ID",
        "CUSTOMER_NAME",
        "NO_TLP",
        "START_TIME",
        "END_TIME",
        "REAL_END_TIME",
        "STATUS",
        "STAFF",
        "PAX"
    };
    
     //adding status ditampilkan atau tidak by mirahu 20120511
    public static int USE_NO_SHOW = 0 ;
    public static int USE_SHOW = 1;
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING, 
        TYPE_STRING, 
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT
    };

    public PstWaitingList() {
    }

    public PstWaitingList(int i) throws DBException {
        super(new PstWaitingList());
    }

    public PstWaitingList(String sOid) throws DBException {
        super(new PstWaitingList(0));
        try {
            if (!locate(sOid)) {
                throw new DBException(this, DBException.RECORD_NOT_FOUND);
            } else {
                return;
            }
        } catch (Exception e) {
        }
    }

    public PstWaitingList(long lOid) throws DBException {
        super(new PstWaitingList(0));
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
        return TBL_WAITING_LIST;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstWaitingList().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        WaitingList waitingList = fetchExc(ent.getOID());
        ent = (Entity) waitingList;
        return waitingList.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((WaitingList) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((WaitingList) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static WaitingList fetchExc(long oid) throws DBException {
        try {
            WaitingList waitingList = new WaitingList();
            PstWaitingList PstWaitingList = new PstWaitingList(oid);
            waitingList.setOID(oid);

            waitingList.setCustomerName(PstWaitingList.getString(FLD_CUSTOMER_NAME));
            waitingList.setStartTime(PstWaitingList.getDate(FLD_START_TIME));
            waitingList.setEndTime(PstWaitingList.getDate(FLD_END_TIME));
            waitingList.setStartTime(PstWaitingList.getDate(FLD_START_TIME));
            waitingList.setRealTime(PstWaitingList.getDate(FLD_REAL_END_TIME));
            waitingList.setStatus(PstWaitingList.getInt(FLD_STATUS));
            waitingList.setStaff(PstWaitingList.getString(FLD_STAFF));
            waitingList.setPax(PstWaitingList.getInt(FLD_PAX));
            
            return waitingList;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstWaitingList(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(WaitingList waitingList) throws DBException {
        try {
            PstWaitingList PstWaitingList = new PstWaitingList(0);

            PstWaitingList.setString(FLD_CUSTOMER_NAME, waitingList.getCustomerName());
            PstWaitingList.setString(FLD_NO_TLP, waitingList.getNoTlp());
            PstWaitingList.setDate(FLD_START_TIME, waitingList.getStartTime());
            PstWaitingList.setDate(FLD_END_TIME, waitingList.getEndTime());
            PstWaitingList.setDate(FLD_REAL_END_TIME, waitingList.getRealTime());
            PstWaitingList.setInt(FLD_STATUS, waitingList.getStatus());
            PstWaitingList.setString(FLD_STAFF, waitingList.getStaff());
            PstWaitingList.setInt(FLD_PAX, waitingList.getPax());
            PstWaitingList.insert();
            //long oidDataSync=PstDataSyncSql.insertExc(PstWaitingList.getInsertSQL());
            //PstDataSyncStatus.insertExc(oidDataSync);
            waitingList.setOID(PstWaitingList.getlong(FLD_CUSTOMER_WAITING_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstWaitingList(0), DBException.UNKNOWN);
        }
        return waitingList.getOID();
    }

    public static long updateExc(WaitingList waitingList) throws DBException {
        try {
            if (waitingList.getOID() != 0) {
                PstWaitingList PstWaitingList = new PstWaitingList(waitingList.getOID());

                PstWaitingList.setString(FLD_CUSTOMER_NAME, waitingList.getCustomerName());
                PstWaitingList.setString(FLD_NO_TLP, waitingList.getNoTlp());
                PstWaitingList.setDate(FLD_START_TIME, waitingList.getStartTime());
                PstWaitingList.setDate(FLD_END_TIME, waitingList.getEndTime());
                PstWaitingList.setDate(FLD_REAL_END_TIME, waitingList.getRealTime());
                PstWaitingList.setInt(FLD_STATUS, waitingList.getStatus());
                PstWaitingList.setString(FLD_STAFF, waitingList.getStaff());
                PstWaitingList.setInt(FLD_PAX, waitingList.getPax());
                PstWaitingList.update();
                
                //long oidDataSync=PstDataSyncSql.insertExc(PstWaitingList.getUpdateSQL());
                //PstDataSyncStatus.insertExc(oidDataSync);
                return waitingList.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstWaitingList(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstWaitingList PstWaitingList = new PstWaitingList(oid);
            PstWaitingList.delete();

            long oidDataSync = PstDataSyncSql.insertExc(PstWaitingList.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstWaitingList(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_WAITING_LIST;
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
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                WaitingList waitingList = new WaitingList();
                resultToObject(rs, waitingList);
                lists.add(waitingList);
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
     * adnyana
     * untuk mencari list merk
     * @return hashtable
     */
    public static Hashtable getListAccountMerk() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_WAITING_LIST;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                WaitingList waitingList = new WaitingList();
                resultToObject(rs, waitingList);
                lists.put(waitingList.getCustomerName().toUpperCase(), waitingList);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }

    public static void resultToObject(ResultSet rs, WaitingList waitingList) {
        try {
            
            waitingList.setOID(rs.getLong(PstWaitingList.fieldNames[PstWaitingList.FLD_CUSTOMER_WAITING_ID]));
            waitingList.setCustomerName(rs.getString(PstWaitingList.fieldNames[PstWaitingList.FLD_CUSTOMER_NAME]));
            waitingList.setNoTlp(rs.getString(PstWaitingList.fieldNames[PstWaitingList.FLD_NO_TLP]));
            waitingList.setStartTime(rs.getTimestamp(PstWaitingList.fieldNames[PstWaitingList.FLD_START_TIME]));
            waitingList.setEndTime(rs.getTimestamp(PstWaitingList.fieldNames[PstWaitingList.FLD_END_TIME]));
            waitingList.setRealTime(rs.getTimestamp(PstWaitingList.fieldNames[PstWaitingList.FLD_REAL_END_TIME]));
            waitingList.setStatus(rs.getInt(PstWaitingList.fieldNames[PstWaitingList.FLD_STATUS]));
            waitingList.setStaff(rs.getString(PstWaitingList.fieldNames[PstWaitingList.FLD_STAFF]));
            waitingList.setPax(rs.getInt(PstWaitingList.fieldNames[PstWaitingList.FLD_PAX]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long customerID) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_WAITING_LIST + " WHERE " +
                    PstWaitingList.fieldNames[PstWaitingList.FLD_CUSTOMER_WAITING_ID] + " = " + customerID;

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
            String sql = "SELECT COUNT(" + PstWaitingList.fieldNames[PstWaitingList.FLD_CUSTOMER_WAITING_ID] + ") FROM " + TBL_WAITING_LIST;
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


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    WaitingList waitingList = (WaitingList) list.get(ls);
                    if (oid == waitingList.getOID()) {
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

    /* This method used to find command where current data */
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
                //System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                    //System.out.println("prev.......................");
                    }
                }
            }
        }

        return cmd;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                WaitingList waitingList = PstWaitingList.fetchExc(oid);
                object.put(PstWaitingList.fieldNames[PstWaitingList.FLD_CUSTOMER_WAITING_ID], waitingList.getOID());
                object.put(PstWaitingList.fieldNames[PstWaitingList.FLD_CUSTOMER_NAME], waitingList.getCustomerName());
                object.put(PstWaitingList.fieldNames[PstWaitingList.FLD_END_TIME], waitingList.getEndTime());
                object.put(PstWaitingList.fieldNames[PstWaitingList.FLD_NO_TLP], waitingList.getNoTlp());
                object.put(PstWaitingList.fieldNames[PstWaitingList.FLD_PAX], waitingList.getPax());
                object.put(PstWaitingList.fieldNames[PstWaitingList.FLD_REAL_END_TIME], waitingList.getRealTime());
                object.put(PstWaitingList.fieldNames[PstWaitingList.FLD_STAFF], waitingList.getStaff());
                object.put(PstWaitingList.fieldNames[PstWaitingList.FLD_START_TIME], waitingList.getStartTime());
                object.put(PstWaitingList.fieldNames[PstWaitingList.FLD_STATUS], waitingList.getStatus());
            }catch(Exception exc){}

            return object;
        }
}
