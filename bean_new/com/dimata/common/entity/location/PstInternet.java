
/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */
/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/
package com.dimata.common.entity.location;

import java.sql.*;
import java.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;
//import com.dimata.common.entity.contact.*;

//integrasi cashier vs hanoman
//import com.dimata.ObjLink.BOPos.OutletLink;
//import com.dimata.interfaces.BOPos.I_Outlet;
//import com.dimata.common.entity.custom.*;

public class PstInternet extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language  {

    //public static final  String TBL_P2_LOCATION = "LOCATION";
    public static final String TBL_P2_INTERNET = "internetcon";
    public static final int FLD_INTERCON_ID = 0;
    public static final int FLD_IP = 1;
    public static final int FLD_USER_NAME = 2;
    public static final int FLD_PASSWORD = 3;
    public static final int FLD_PORT = 4;
    public static final int FLD_CASH_MASTER_ID=5;
    public static final int FLD_DATABASE_NAME=6;
    public static final String[] fieldNames = {//harus sama dengan nama field di database
        "internet_id",
        "ip",
        "user_name",
        "pasword",
        "port",
        "cash_master_id",
        "database_name"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID ,//generate internet_id otomatis
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING

    };

    public PstInternet() {
    }

    public PstInternet(int i) throws DBException {
        super(new PstInternet());
    }

    public PstInternet(String sOid) throws DBException {
        super(new PstInternet(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstInternet(long lOid) throws DBException {
        super(new PstInternet(0));
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
        return TBL_P2_INTERNET;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstInternet().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        try {
            internetconn internet = (internetconn) ent;
            long oid = ent.getOID();
            PstInternet pstInter = new PstInternet(oid);
            internet.setOID(oid);//method setOID berada pada class Entity

            internet.setIp(pstInter.getString(FLD_IP));
            internet.setUser_name(pstInter.getString(FLD_USER_NAME));
            internet.setPasword(pstInter.getString(FLD_PASSWORD));
            internet.setPort(pstInter.getString(FLD_PORT));
            internet.setCash_master_id(pstInter.getLong(FLD_CASH_MASTER_ID));
            internet.setDatabase_name(pstInter.getString(FLD_DATABASE_NAME));

            return internet.getOID();

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInternet(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((internetconn) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((internetconn) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static internetconn fetchExc(long oid) throws DBException {
        try {
            internetconn internet = new internetconn();
            PstInternet pstInter = new PstInternet(oid);

            internet.setOID(oid);
            internet.setIp(pstInter.getString(FLD_IP));
            internet.setUser_name(pstInter.getString(FLD_USER_NAME));
            internet.setPasword(pstInter.getString(FLD_PASSWORD));
            internet.setPort(pstInter.getString(FLD_PORT));
            internet.setCash_master_id(pstInter.getLong(FLD_CASH_MASTER_ID));
            internet.setDatabase_name(pstInter.getString(FLD_DATABASE_NAME));


            return internet;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInternet(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(internetconn internet) throws DBException {
        try {
            PstInternet pstInter = new PstInternet(0);
            pstInter.setString(FLD_IP, internet.getIp());
            pstInter.setString(FLD_USER_NAME, internet.getUser_name());
            pstInter.setString(FLD_PASSWORD, internet.getPasword());
            pstInter.setString(FLD_PORT, internet.getPort());
            pstInter.setLong(FLD_CASH_MASTER_ID, internet.getCash_master_id());
            pstInter.setString(FLD_DATABASE_NAME,internet.getDatabase_name());


            pstInter.insert();
            internet.setOID(pstInter.getlong(FLD_INTERCON_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInternet(0), DBException.UNKNOWN);
        }
        return internet.getOID();
    }

    public static long updateExc(internetconn internet) throws DBException {
        try {
            if (internet.getOID() != 0) {
                PstInternet pstInter = new PstInternet(internet.getOID());

                pstInter.setString(FLD_IP, internet.getIp());
                pstInter.setString(FLD_USER_NAME, internet.getUser_name());
                pstInter.setString(FLD_PASSWORD, internet.getPasword());
                pstInter.setString(FLD_PORT, internet.getPort());
                pstInter.setLong(FLD_CASH_MASTER_ID, internet.getCash_master_id());
                pstInter.setString(FLD_DATABASE_NAME, internet.getDatabase_name());

                pstInter.update();

                return internet.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocation(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstInternet pstInter = new PstInternet(oid);
            pstInter.delete();

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocation(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_P2_INTERNET;
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
                internetconn internet = new internetconn();
                resultToObject(rs, internet);

                lists.add(internet);
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

    private static void resultToObject(ResultSet rs, internetconn internet) {
        try {
            PstInternet pstInter = new PstInternet();
            internet.setOID(rs.getLong(pstInter.fieldNames[pstInter.FLD_INTERCON_ID]));
            internet.setIp(rs.getString(pstInter.fieldNames[pstInter.FLD_IP]));
            internet.setUser_name(rs.getString(pstInter.fieldNames[pstInter.FLD_USER_NAME]));
            internet.setPasword(rs.getString(pstInter.fieldNames[pstInter.FLD_PASSWORD]));
            internet.setPort(rs.getString(pstInter.fieldNames[pstInter.FLD_PORT]));
            internet.setCash_master_id(rs.getLong(pstInter.fieldNames[pstInter.FLD_CASH_MASTER_ID]));
            internet.setDatabase_name(rs.getString(pstInter.fieldNames[pstInter.FLD_DATABASE_NAME]));

    } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean checkOID(long locationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_P2_INTERNET + " WHERE "
                    + PstInternet.fieldNames[PstInternet.FLD_INTERCON_ID] + " = " + locationId;

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
            String sql = "SELECT COUNT(" + PstInternet.fieldNames[PstInternet.FLD_INTERCON_ID] + ") FROM " + TBL_P2_INTERNET;
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
                    internetconn internet = (internetconn) list.get(ls);
                    if (oid == internet.getOID()) {
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
}
