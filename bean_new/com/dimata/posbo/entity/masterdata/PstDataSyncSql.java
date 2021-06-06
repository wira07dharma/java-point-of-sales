/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.masterdata;

import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

/* package posbo */
//import com.dimata.posbo.db.DBHandler;
//import com.dimata.posbo.db.DBException;
//import com.dimata.posbo.db.DBLogger;
import com.dimata.posbo.entity.masterdata.*;
import org.json.JSONObject;
public class PstDataSyncSql extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final  String TBL_data_sync_sql = "data_sync_sql";

    public static final  int FLD_DATA_SYNC_ID = 0;
    public static final  int FLD_QUERY = 1;

    public static final  String[] fieldNames = {
        "id_data_sync",
        "query"
    };

     //public static final  String TBL_P2_LOCATION = "LOCATION";

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID ,//generate internet_id otomatis
        TYPE_STRING
    };

    public PstDataSyncSql() {
    }

    public PstDataSyncSql(int i) throws DBException {
        super(new PstDataSyncSql());
    }

    public PstDataSyncSql(String sOid) throws DBException {
        super(new PstDataSyncSql(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstDataSyncSql(long lOid) throws DBException {
        super(new PstDataSyncSql(0));
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
        return TBL_data_sync_sql;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDataSyncSql().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        try {
            DataSyncSql datasyncsql = (DataSyncSql) ent;
            long oid = ent.getOID();
            PstDataSyncSql pstdatasyncsql = new PstDataSyncSql(oid);
            datasyncsql.setOID(oid);//method setOID berada pada class Entity

            datasyncsql.setQuery(dbUrl);
            return datasyncsql.getOID();

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncSql(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((DataSyncSql) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((DataSyncSql) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }



    public static DataSyncSql fetchExc(long oid) throws DBException {
        try {
            DataSyncSql datasyncsql = new DataSyncSql();
            PstDataSyncSql pstdatasyncsql = new PstDataSyncSql(oid);

            datasyncsql.setOID(oid);
            datasyncsql.setQuery(pstdatasyncsql.getString(FLD_QUERY));
            //internet.setUser_name(pstInter.getString(FLD_USER_NAME));
            //internet.setPasword(pstInter.getString(FLD_PASSWORD));
            //internet.setPort(pstInter.getString(FLD_PORT));
            //internet.setCash_master_id(pstInter.getLong(FLD_CASH_MASTER_ID));

            return datasyncsql;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncSql(0), DBException.UNKNOWN);
        }
    }


    public static long insertExc(String sql) throws DBException {
        try {
            PstDataSyncSql pstdatasyncsql = new PstDataSyncSql(0);
            pstdatasyncsql.setString(FLD_QUERY, sql);
            pstdatasyncsql.insert();
            return (pstdatasyncsql.getlong(FLD_DATA_SYNC_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncSql(0), DBException.UNKNOWN);
        }
    }



    public static long insertExc(DataSyncSql datasyncsql) throws DBException {
        try {
            PstDataSyncSql pstdatasyncsql = new PstDataSyncSql(0);
            pstdatasyncsql.setString(FLD_QUERY, datasyncsql.getQuery());
            pstdatasyncsql.insert();
            datasyncsql.setOID(pstdatasyncsql.getlong(FLD_DATA_SYNC_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncSql(0), DBException.UNKNOWN);
        }
        return datasyncsql.getOID();
    }

    public static long updateExc(DataSyncSql datasyncsql) throws DBException {
        try {
            if (datasyncsql.getOID() != 0) {

                 PstDataSyncSql pstdatasyncsql = new PstDataSyncSql(datasyncsql.getId_data_sync());
                pstdatasyncsql.setString(FLD_DATA_SYNC_ID, datasyncsql.getQuery());
                //pstdatasyncsql.setString(FLD_QUERY, datasyncsql.setId_data_sync(datasyncsql.getOID()));
               // pstInter.setString(FLD_USER_NAME, internet.getUser_name());
                //pstInter.setString(FLD_PASSWORD, internet.getPasword());
                //pstInter.setString(FLD_PORT, internet.getPort());
                //pstInter.setLong(FLD_CASH_MASTER_ID, internet.getCash_master_id());
               // pstInter.update();

                return datasyncsql.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncSql(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstDataSyncSql pstdatasyncsql = new PstDataSyncSql(oid);
            pstdatasyncsql.delete();


        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncSql(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static long deleteRecExc(String sql) throws DBException {
        try {
            PstDataSyncSql pstdatasyncsql = new PstDataSyncSql(0);
            pstdatasyncsql.deleteRecords(0, sql);

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDataSyncSql(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_data_sync_sql ;
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
                DataSyncSql datasyncsql = new DataSyncSql();
                resultToObject(rs, datasyncsql);
                lists.add(datasyncsql);
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




    private static void resultToObject(ResultSet rs, DataSyncSql datasyncsql) {
        try {
            PstDataSyncSql pstdatasyncsql = new PstDataSyncSql();
            datasyncsql.setOID(rs.getLong(pstdatasyncsql.fieldNames[pstdatasyncsql.FLD_DATA_SYNC_ID]));
            datasyncsql.setQuery(rs.getString(pstdatasyncsql.fieldNames[pstdatasyncsql.FLD_QUERY]));
            //internet.setUser_name(rs.getString(pstInter.fieldNames[pstInter.FLD_USER_NAME]));
            //internet.setPasword(rs.getString(pstInter.fieldNames[pstInter.FLD_PASSWORD]));
            //internet.setPort(rs.getString(pstInter.fieldNames[pstInter.FLD_PORT]));
            //internet.setCash_master_id(rs.getLong(pstInter.fieldNames[pstInter.FLD_CASH_MASTER_ID]));

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean checkOID(long data_sync_id) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_data_sync_sql + " WHERE "
                    + PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID] + " = " + data_sync_id;

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
            String sql = "SELECT COUNT(" + PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID] + ") FROM " + TBL_data_sync_sql;
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
                    DataSyncSql datasyncsql = (DataSyncSql) list.get(ls);
                    if (oid == datasyncsql.getOID()) {
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
                DataSyncSql dataSyncSql = PstDataSyncSql.fetchExc(oid);
                object.put(PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID], dataSyncSql.getId_data_sync());
                object.put(PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_QUERY], dataSyncSql.getQuery());

            }catch(Exception exc){}

            return object;
        }


}
