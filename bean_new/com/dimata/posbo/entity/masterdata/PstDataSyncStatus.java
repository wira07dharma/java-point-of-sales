/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.masterdata;

import java.sql.*;
import java.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;
/**
 *
 * @author user
 */
public class PstDataSyncStatus extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
//public static final  String TBL_P2_LOCATION = "LOCATION";
    public static final String TBL_P2_DATA_SYNC_STATUS = "data_sync_status";
    public static final int FLD_ID_DATA_SYNC_STATUS = 0;
    public static final int FLD_ID_DATA_SYNC = 1;
    public static final int FLD_ID_DBCONNECTION = 2;
    public static final int FLD_STATUS = 3;
    
    
    public static final String[] fieldNames = {//harus sama dengan nama field di database
        "id_data_sync_status",
        "id_data_sync",
        "id_dbconnection",
        "status"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID ,//generate internet_id otomatis
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING
    };

    public PstDataSyncStatus() {
    }

    public PstDataSyncStatus(int i) throws DBException {
        super(new PstDataSyncStatus());
    }

    public PstDataSyncStatus(String sOid) throws DBException {
        super(new PstDataSyncStatus(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstDataSyncStatus(long lOid) throws DBException {
        super(new PstDataSyncStatus(0));
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
        return TBL_P2_DATA_SYNC_STATUS;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDataSyncStatus().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        try {
            DataSyncStatus dataStatus = (DataSyncStatus) ent;
            long oid = ent.getOID();
            PstDataSyncStatus pstDataStatus = new PstDataSyncStatus(oid);
            dataStatus.setOID(oid);//method setOID berada pada class Entity
            dataStatus.setId_data_sync(pstDataStatus.getlong(FLD_ID_DATA_SYNC));
            dataStatus.setId_dbconnection(pstDataStatus.getlong(FLD_ID_DBCONNECTION));
            dataStatus.setStatus(pstDataStatus.getString(FLD_STATUS));

            return dataStatus.getOID();

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncStatus(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((DataSyncStatus) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((DataSyncStatus) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static DataSyncStatus fetchExc(long oid) throws DBException {
        try {
            DataSyncStatus dataStatus = new DataSyncStatus();
            PstDataSyncStatus pstDataStatus = new PstDataSyncStatus(oid);

            dataStatus.setOID(oid);//method setOID berada pada class Entity
            dataStatus.setId_data_sync(pstDataStatus.getlong(FLD_ID_DATA_SYNC));
            dataStatus.setId_dbconnection(pstDataStatus.getlong(FLD_ID_DBCONNECTION));
            dataStatus.setStatus(pstDataStatus.getString(FLD_STATUS));


            return dataStatus;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncStatus(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(DataSyncStatus dataStatus) throws DBException {
        try {
            PstDataSyncStatus pstDataStatus = new PstDataSyncStatus(0);
            pstDataStatus.setLong(FLD_ID_DATA_SYNC, dataStatus.getId_data_sync());
            pstDataStatus.setLong(FLD_ID_DBCONNECTION, dataStatus.getId_dbconnection());
            pstDataStatus.setString(FLD_STATUS, dataStatus.getStatus());
                             
            pstDataStatus.insert();
            dataStatus.setOID(pstDataStatus.getlong(FLD_ID_DATA_SYNC_STATUS));

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncStatus(0), DBException.UNKNOWN);
        }
        return dataStatus.getOID();
    }

    public static long insertExc(long oidDataSync) throws DBException {
        try {
            Vector vecDbConn = PstConnection.listAll();
            for(int i=0; i < vecDbConn.size();i++ ){
                OutletConnection dbConn = (OutletConnection) vecDbConn.get(i);
                PstDataSyncStatus pstDataStatus = new PstDataSyncStatus(0);
                pstDataStatus.setLong(FLD_ID_DATA_SYNC, oidDataSync);
                pstDataStatus.setLong(FLD_ID_DBCONNECTION,dbConn.getOID() );
                pstDataStatus.setString(FLD_STATUS, "0");
                
                pstDataStatus.insert();
                //return (pstdatasyncsql.getlong(FLD_DATA_SYNC_ID));
            }

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncStatus(0), DBException.UNKNOWN);
        }
        //return(pstDataStatus.getlong(FLD_ID_DATA_SYNC_STATUS));
        return 0;
    }
/**
 * insert ke table data sync untuk transfer ke outlet
 * @param oidDataSync
 * @param vecDbConn
 * @return
 * @throws DBException
 * update opie-eyek 20121027
 */
  public static long insertExc(long oidDataSync, Vector vecDbConn) throws DBException {
        try {
            for(int i=0; i < vecDbConn.size();i++ ){
                OutletConnection dbConn = (OutletConnection) vecDbConn.get(i);
                PstDataSyncStatus pstDataStatus = new PstDataSyncStatus(0);
                pstDataStatus.setLong(FLD_ID_DATA_SYNC, oidDataSync);
                pstDataStatus.setLong(FLD_ID_DBCONNECTION,dbConn.getOID() );
                pstDataStatus.setString(FLD_STATUS, "0");

                pstDataStatus.insert();
                //return (pstdatasyncsql.getlong(FLD_DATA_SYNC_ID));
            }

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncStatus(0), DBException.UNKNOWN);
        }
        //return(pstDataStatus.getlong(FLD_ID_DATA_SYNC_STATUS));
        return 0;
    }



    public static long updateExc(DataSyncStatus dataStatus) throws DBException {
        try {
            if (dataStatus.getOID() != 0) {
                PstDataSyncStatus pstDataStatus = new PstDataSyncStatus(dataStatus.getOID());

                pstDataStatus.setLong(FLD_ID_DATA_SYNC, dataStatus.getId_data_sync());
                pstDataStatus.setLong(FLD_ID_DBCONNECTION, dataStatus.getId_dbconnection());
                pstDataStatus.setString(FLD_STATUS, dataStatus.getStatus());

                pstDataStatus.update();

                return dataStatus.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncStatus(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstDataSyncStatus pstDataStatus = new PstDataSyncStatus(oid);
            pstDataStatus.delete();

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncStatus(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static long deleteRecExc(String sql) throws DBException {
        try {
            PstDataSyncStatus pstDataSyncStatus = new PstDataSyncStatus(0);
            pstDataSyncStatus.deleteRecords(0, sql);

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
             throw new DBException(new PstDataSyncStatus(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_P2_DATA_SYNC_STATUS;
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
            System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                DataSyncStatus dataStatus = new DataSyncStatus();
                resultToObject(rs,dataStatus );

                lists.add(dataStatus);
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


    public static Vector listQuery(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT b.*, c.query FROM " + TBL_P2_DATA_SYNC_STATUS + " as b " +
                    " inner join " + PstDataSyncSql.TBL_data_sync_sql + " as c " +
                    " on " + "b." + fieldNames[FLD_ID_DATA_SYNC] + " = " +
                    " c." + PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID] ;
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
            System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                DataSyncStatus dataStatus = new DataSyncStatus();
                resultToObjectWithSQL(rs,dataStatus);

                lists.add(dataStatus);
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



    private static void resultToObject(ResultSet rs, DataSyncStatus dataSyncStatus) {
        try {
            //Pstconnection pstconn = new Pstconnection();
            dataSyncStatus.setOID(rs.getLong(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DATA_SYNC_STATUS]));
            dataSyncStatus.setId_data_sync(rs.getLong(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DATA_SYNC]));
            dataSyncStatus.setId_dbconnection(rs.getLong(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION]));
            dataSyncStatus.setStatus(rs.getString(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_STATUS]));
            
    } catch (Exception e) {
            System.out.println(e);
        }
    }


    private static void resultToObjectWithSQL(ResultSet rs, DataSyncStatus dataSyncStatus) {
        try {
            //Pstconnection pstconn = new Pstconnection();
            dataSyncStatus.setOID(rs.getLong(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DATA_SYNC_STATUS]));
            dataSyncStatus.setId_data_sync(rs.getLong(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DATA_SYNC]));
            dataSyncStatus.setId_dbconnection(rs.getLong(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION]));
            dataSyncStatus.setStatus(rs.getString(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_STATUS]));
            dataSyncStatus.setSql(rs.getString("query"));


    } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean checkOID(long oidDataStatus) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_P2_DATA_SYNC_STATUS + " WHERE "
                    + PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DATA_SYNC_STATUS] + " = " + oidDataStatus;

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
            String sql = "SELECT COUNT(" + PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DATA_SYNC_STATUS] + ") FROM " + TBL_P2_DATA_SYNC_STATUS;
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
                    DataSyncStatus dataStatus = (DataSyncStatus) list.get(ls);
                    if (oid == dataStatus.getOID()) {
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
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
               DataSyncStatus dataSyncStatus = PstDataSyncStatus.fetchExc(oid);
               object.put(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DATA_SYNC_STATUS], dataSyncStatus.getId_data_sync_status());
               object.put(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DATA_SYNC], dataSyncStatus.getId_data_sync());
               object.put(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION], dataSyncStatus.getId_dbconnection());
               object.put(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_STATUS], dataSyncStatus.getStatus());
            }catch(Exception exc){}

            return object;
        }
}
