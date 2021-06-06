package com.dimata.posbo.entity.admin;

import com.dimata.common.entity.custom.DataCustom;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.PstSystemProperty;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.entity.masterdata.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * update opie-eyek 20130903
 *
 * @author dimata005
 */
public class PstAppUser extends DBHandler implements I_DBInterface, I_DBType, I_Persintent {

    //public static final String TBL_APP_USER = "APP_USER";
    public static final String TBL_APP_USER = "pos_app_user";
    public static final int FLD_USER_ID = 0;
    public static final int FLD_LOGIN_ID = 1;
    public static final int FLD_PASSWORD = 2;
    public static final int FLD_FULL_NAME = 3;
    public static final int FLD_EMAIL = 4;
    public static final int FLD_DESCRIPTION = 5;
    public static final int FLD_REG_DATE = 6;
    public static final int FLD_UPDATE_DATE = 7;
    public static final int FLD_USER_STATUS = 8;
    public static final int FLD_LAST_LOGIN_DATE = 9;
    public static final int FLD_LAST_LOGIN_IP = 10;
    public static final int FLD_EMPLOYEE_ID = 11;
    public static final int FLD_USER_GROUP_NEW = 12;
    public static final int FLD_START_TIME = 13;
    public static final int FLD_END_TIME = 14;
    public static final int FLD_FINGER_DATA = 15;
    public static final int FLD_COMPANY_ID = 16;
    public static final String[] fieldNames
            = {
                "USER_ID",
                "LOGIN_ID",
                "PASSWORD",
                "FULL_NAME",
                "EMAIL",
                "DESCRIPTION",
                "REG_DATE",
                "UPDATE_DATE",
                "USER_STATUS",
                "LAST_LOGIN_DATE",
                "LAST_LOGIN_IP",
                "EMPLOYEE_ID",
                "USER_GROUP",
                "START_TIME",
                "END_TIME",
                "FINGER_DATA",
                "GEN_ID" // END TIME , TIME IN USE ONLY
            };

    public static int[] fieldTypes
            = {
                TYPE_PK + TYPE_LONG + TYPE_ID, TYPE_STRING, TYPE_STRING, TYPE_STRING, TYPE_STRING, TYPE_STRING,
                TYPE_DATE, TYPE_DATE, TYPE_INT, TYPE_DATE, TYPE_STRING, TYPE_LONG, TYPE_INT, TYPE_DATE, TYPE_DATE, TYPE_STRING,
                TYPE_LONG, TYPE_LONG
            };

    /**
     * Creates new PstAppUser
     */
    public PstAppUser() {
    }

    public PstAppUser(int i) throws DBException {
        super(new PstAppUser());
    }

    public PstAppUser(String sOid) throws DBException {
        super(new PstAppUser(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAppUser(long lOid) throws DBException {
        super(new PstAppUser(0));
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
        return TBL_APP_USER;
    }

    public String getPersistentName() {
        return new PstAppUser().getClass().getName();
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public long delete(Entity ent) {
        return delete((AppUser) ent);
    }

    public long insert(Entity ent) {
        try {
            return PstAppUser.insert((AppUser) ent);
        } catch (Exception e) {
            System.out.println(" EXC " + e);
            return 0;
        }
    }

    public long update(Entity ent) {
        return update((AppUser) ent);
    }

    public long fetch(Entity ent) {
        AppUser entObj = PstAppUser.fetch(ent.getOID());
        ent = (Entity) entObj;
        return ent.getOID();
    }

    public static AppUser fetch(long oid) {
        AppUser entObj = new AppUser();
        try {
            PstAppUser pstObj = new PstAppUser(oid);
            entObj.setOID(oid);
            entObj.setLoginId(pstObj.getString(FLD_LOGIN_ID));
            entObj.setPassword(pstObj.getString(FLD_PASSWORD));
            entObj.setFullName(pstObj.getString(FLD_FULL_NAME));
            entObj.setEmail(pstObj.getString(FLD_EMAIL));
            entObj.setDescription(pstObj.getString(FLD_DESCRIPTION));
            entObj.setRegDate(pstObj.getDate(FLD_REG_DATE));
            entObj.setUpdateDate(pstObj.getDate(FLD_UPDATE_DATE));
            entObj.setUserStatus(pstObj.getInt(FLD_USER_STATUS));
            entObj.setLastLoginDate(pstObj.getDate(FLD_LAST_LOGIN_DATE));
            entObj.setLastLoginIp(pstObj.getString(FLD_LAST_LOGIN_IP));
            entObj.setEmployeeId(pstObj.getlong(FLD_EMPLOYEE_ID));
            entObj.setUserGroupNew(pstObj.getInt(FLD_USER_GROUP_NEW));
            entObj.setStartTime(pstObj.getDate(FLD_START_TIME));
            entObj.setEndTime(pstObj.getDate(FLD_END_TIME));
            entObj.setFingerData(pstObj.getString(FLD_FINGER_DATA));
            entObj.setCompanyId(pstObj.getlong(FLD_COMPANY_ID));
        } catch (DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

    public static long insert(AppUser entObj) throws DBException, DBException {
        // try{
        PstAppUser pstObj = new PstAppUser(0);

        pstObj.setString(FLD_LOGIN_ID, entObj.getLoginId());
        pstObj.setString(FLD_PASSWORD, entObj.getPassword());
        pstObj.setString(FLD_FULL_NAME, entObj.getFullName());
        pstObj.setString(FLD_EMAIL, entObj.getEmail());
        pstObj.setString(FLD_DESCRIPTION, entObj.getDescription());
        pstObj.setDate(FLD_REG_DATE, new Date());//entObj.getRegDate());
        pstObj.setDate(FLD_UPDATE_DATE, new Date());//entObj.getUpdateDate());
        pstObj.setInt(FLD_USER_STATUS, entObj.getUserStatus());
        pstObj.setLong(FLD_EMPLOYEE_ID, entObj.getEmployeeId());
        //pstObj.setDate(FLD_LAST_LOGIN_DATE, entObj.getLastLoginDate());
        //pstObj.setString(FLD_LAST_LOGIN_IP, entObj.getLastLoginIp());
        pstObj.setInt(FLD_USER_GROUP_NEW, entObj.getUserGroupNew());
        pstObj.setDate(FLD_START_TIME, entObj.getStartTime());
        pstObj.setDate(FLD_END_TIME, entObj.getEndTime());
        pstObj.setLong(FLD_COMPANY_ID, entObj.getCompanyId());

        pstObj.insert();
        long oidDataSync = PstDataSyncSql.insertExc(pstObj.getInsertSQL());
        PstDataSyncStatus.insertExc(oidDataSync);
        entObj.setOID(pstObj.getlong(FLD_USER_ID));
        //kebutuhan untuk service transfer katalog
        PstDataCustom.insertDataForSyncAllLocation(pstObj.getInsertSQL());
        return entObj.getOID();
        /*  }
        catch(DBException e) {
            System.out.println(e);
        }
        return 0;
         */
    }

    public static long update(AppUser entObj) {
        if ((entObj != null) && (entObj.getOID() != 0)) {
            try {
                PstAppUser pstObj = new PstAppUser(entObj.getOID());

                pstObj.setString(FLD_LOGIN_ID, entObj.getLoginId());
                pstObj.setString(FLD_PASSWORD, entObj.getPassword());
                pstObj.setString(FLD_FULL_NAME, entObj.getFullName());
                pstObj.setString(FLD_EMAIL, entObj.getEmail());
                pstObj.setString(FLD_DESCRIPTION, entObj.getDescription());
                //pstObj.setDate(FLD_REG_DATE, entObj.getRegDate());
                pstObj.setDate(FLD_UPDATE_DATE, new Date()); //entObj.getUpdateDate());
                pstObj.setInt(FLD_USER_STATUS, entObj.getUserStatus());
                pstObj.setDate(FLD_LAST_LOGIN_DATE, entObj.getLastLoginDate());
                pstObj.setString(FLD_LAST_LOGIN_IP, entObj.getLastLoginIp());
                pstObj.setLong(FLD_EMPLOYEE_ID, entObj.getEmployeeId());
                pstObj.setInt(FLD_USER_GROUP_NEW, entObj.getUserGroupNew());
                pstObj.setDate(FLD_START_TIME, entObj.getStartTime());
                pstObj.setDate(FLD_END_TIME, entObj.getEndTime());
                pstObj.setLong(FLD_COMPANY_ID, entObj.getCompanyId());
                pstObj.update();

                long oidDataSync = PstDataSyncSql.insertExc(pstObj.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
                //kebutuhan untuk service transfer katalog
                PstDataCustom.insertDataForSyncAllLocation(pstObj.getUpdateSQL());
                return entObj.getOID();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }

    public static long delete(long oid) {
        try {
            PstAppUser pstObj = new PstAppUser(oid);
            pstObj.delete();
            long oidDataSync = PstDataSyncSql.insertExc(pstObj.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstObj.getDeleteSQL());
            return oid;

        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            int count = 0;
            String sql = " SELECT COUNT(" + fieldNames[FLD_USER_ID] + ") AS NRCOUNT"
                    + " FROM " + TBL_APP_USER;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception exc) {
            System.out.println("getCount " + exc);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }

    }

    public static Vector listPartObj(int start, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + fieldNames[FLD_USER_ID]
                    + ", " + fieldNames[FLD_LOGIN_ID]
                    + ", " + fieldNames[FLD_FULL_NAME]
                    + ", " + fieldNames[FLD_EMAIL]
                    + ", " + fieldNames[FLD_USER_STATUS]
                    + ", " + fieldNames[FLD_EMPLOYEE_ID]
                    + ", " + fieldNames[FLD_USER_GROUP_NEW]
                    + ", " + fieldNames[FLD_FINGER_DATA]
                    + " FROM " + TBL_APP_USER;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    }

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
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

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                AppUser appUser = new AppUser();
                resultToObject(rs, appUser);
                lists.add(appUser);
            }
            return lists; 

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static Vector listUser(int start, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT"
                    + " * "
                    + " FROM "
                    + ""+PstAppUser.TBL_APP_USER+" AS ap"
                    + " INNER JOIN "+PstDataCustom.TBL_DATA_CUSTOM+" AS dc"
                    + " ON dc."+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]
                    + " = ap."+fieldNames[FLD_USER_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
                sql += " GROUP BY ap."+fieldNames[FLD_USER_ID];
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    }

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
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
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector data = new Vector();
                AppUser appUser = new AppUser();
                DataCustom dc = new DataCustom();
                resultToObject(rs, appUser);
                data.add(appUser);
                
                PstDataCustom.resultToObject(rs, dc);
                data.add(dc);
                lists.add(data);
            }
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    private static void resultToObject(ResultSet rs, AppUser entObj) {
        try {
            entObj.setOID(rs.getLong(fieldNames[FLD_USER_ID]));
            entObj.setLoginId(rs.getString(fieldNames[FLD_LOGIN_ID]));
            entObj.setFullName(rs.getString(fieldNames[FLD_FULL_NAME]));
            entObj.setEmail(rs.getString(fieldNames[FLD_EMAIL]));
            entObj.setUserStatus(rs.getInt(fieldNames[FLD_USER_STATUS]));
            entObj.setEmployeeId(rs.getLong(fieldNames[FLD_EMPLOYEE_ID]));
            entObj.setUserGroupNew(rs.getInt(fieldNames[FLD_USER_GROUP_NEW]));
            entObj.setFingerData(rs.getString(fieldNames[FLD_FINGER_DATA]));
            entObj.setStartTime(DBHandler.convertDate(rs.getDate(PstAppUser.fieldNames[PstAppUser.FLD_START_TIME]), rs.getTime(PstAppUser.fieldNames[PstAppUser.FLD_START_TIME])));
            entObj.setEndTime(DBHandler.convertDate(rs.getDate(PstAppUser.fieldNames[PstAppUser.FLD_END_TIME]), rs.getTime(PstAppUser.fieldNames[PstAppUser.FLD_END_TIME])));
            entObj.setCompanyId(rs.getLong(fieldNames[FLD_COMPANY_ID]));

        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static Vector listFullObj(int start, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + fieldNames[FLD_USER_ID]
                    + " FROM " + TBL_APP_USER;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            if ((start == 0) && (recordToGet == 0)) {
                sql = sql + "";  //nothing to do
            } else {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                AppUser appUser = new AppUser();
                appUser = PstAppUser.fetch(rs.getLong(fieldNames[FLD_USER_ID]));
                lists.add(appUser);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static Vector getUserPrivObj(long userOID) {
        if (userOID == 0) {
            return new Vector(1, 1);
        }
        DBResultSet dbrs = null;
        try {
            String sql
                    = "SELECT DISTINCT(PO." + PstAppPrivilegeObj.fieldNames[PstAppPrivilegeObj.FLD_CODE] + ") AS CODE FROM " + PstAppPrivilegeObj.TBL_APP_PRIVILEGE_OBJ + " AS PO "
                    + " INNER JOIN " + PstGroupPriv.TBL_GROUP_PRIV + " AS GP ON GP." + PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID] + "=PO." + PstAppPrivilegeObj.fieldNames[PstAppPrivilegeObj.FLD_PRIV_ID]
                    + " INNER JOIN " + PstUserGroup.TBL_USER_GROUP + " AS UG ON UG." + PstUserGroup.fieldNames[PstUserGroup.FLD_GROUP_ID] + "=GP." + PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID]
                    + " WHERE UG." + PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID] + "='" + userOID + "'";

            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector privObjs = new Vector(10, 2);
            while (rs.next()) {
                privObjs.add(new Integer(rs.getInt("CODE")));
            }
            rs.close();
            return privObjs;

        } catch (Exception e) {
            System.out.println("getUserPrivObj " + e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return new Vector(1, 1);
    }

    public static long updateUserStatus(long userOID, int status) {
        if (userOID == 0) {
            return 0;
        }

        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + TBL_APP_USER + " SET " + fieldNames[FLD_USER_STATUS] + "='" + status + "'"
                    + " WHERE " + fieldNames[FLD_USER_ID] + "='" + userOID + "'";

            //System.out.println("sql Update User : "+sql);
            DBHandler.execUpdate(sql);
            return userOID;
        } catch (Exception e) {
            System.out.println("updateUserStatus " + e);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static AppUser getByLoginIDAndPassword(String loginID, String password) {
        if ((loginID == null) || (loginID.length() < 1) || (password == null) || (password.length() < 1)) {
            return null;
        }

        try {

            String whereClause = " " + fieldNames[FLD_LOGIN_ID] + "='" + loginID.trim() + "' AND "
                    + fieldNames[FLD_PASSWORD] + "='" + password.trim() + "'";

            Vector appUsers = listFullObj(0, 0, whereClause, "");

            if ((appUsers == null) || (appUsers.size() != 1)) {
                return new AppUser();
            }

            return (AppUser) appUsers.get(0);

        } catch (Exception e) {
            System.out.println("getByLoginIDAndPassword " + e);
            return null;
        }
    }

    public static boolean checkOID(long userId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_APP_USER + " WHERE "
                    + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " = " + userId;

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

    //This method use to get kasir names according code kasir
    //by mirahu
    //20120315
    public static String getNameCashier(long codeCashier) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + " FROM " + TBL_APP_USER
                    + " WHERE " + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " = '" + codeCashier + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            String name = "";
            while (rs.next()) {
                name = rs.getString(1);
            }

            rs.close();
            return name;
        } catch (Exception e) {
            return "";
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static Vector getSalesUserByLocation(long oid) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql
                    = "SELECT"
                    + " ap.`USER_ID`,"
                    + " ap.`FULL_NAME`,"
                    + " mg.`GROUP_USER_ID`,"
                    + " dc.`data_value`"
                    + " FROM "
                    + " `pos_app_user` AS ap"
                    + " INNER JOIN `data_custom` AS dc"
                    + " ON dc.`owner_id` = ap.`USER_ID`"
                    + " INNER JOIN `mapping_user_group` AS mg"
                    + " ON mg.`USER_ID` = ap.`USER_ID`"
                    + " WHERE mg.`GROUP_USER_ID` = 6"
                    + " AND dc.`data_value` = " + oid
                    + "GROUP BY ap.`USER_ID`";

            System.out.println("getSalesUserByLocation : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector data = new Vector();
                AppUser appUser = new AppUser();
                MappingUserGroup mg = new MappingUserGroup();
                DataCustom dc = new DataCustom();

                appUser.setOID(rs.getLong(1));
                appUser.setFullName(rs.getString(2));
                data.add(appUser);

                mg.setGroupUserId(rs.getLong(3));
                data.add(mg);

                dc.setDataValue(rs.getString(4));
                data.add(dc);
                lists.add(data);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    //Untuk prochain POS
    public static final int GROUP_CASHIER = 0;
    public static final int GROUP_SUPERVISOR = 1;
    public static final int GROUP_STORE = 2;
    public static final int GROUP_WAREHOUSE = 3;
    public static final int GROUP_ADMINISTRATOR = 4;

    /**
     * Ari wiweka 20130713 untuk privillage sales admin
     */
    public static final int GROUP_SALES_ADMIN = 5;

    public static final String UserGroupNewKey[] = {"Cashier", "Cashier Supervisor", "Store", "Warehouse", "Administrator", "Sales Admin"};
    public static final int UserGroupNewValue[] = {0, 1, 2, 3, 4, 5};

    public static final String GroupUsingKey[] = {"Cashier", "Cashier Supervisor", "Store", "Warehouse", "Administrator", "Sales", "Delivery Courier"};
    public static final String GroupUsingValue[] = {"1", "2", "3", "4", "5", "6", "7"};

    public static Vector listUserGroupNewTypeKey() {
        Vector result = new Vector(1, 1);
        for (int s = 0; s < UserGroupNewKey.length; s++) {
            result.add(UserGroupNewKey[s]);
        }
        return result;
    }

    public static Vector listUserGroupTypeValue() {
        Vector result = new Vector(1, 1);
        for (int s = 0; s < UserGroupNewValue.length; s++) {
            result.add("" + UserGroupNewValue[s]);
        }
        return result;
    }

    //--------------- add by edhy ---------------------
    public static long updateByLoggedIn(AppUser entObj) {
        if ((entObj != null) && (entObj.getOID() != 0)) {
            try {
                PstAppUser pstObj = new PstAppUser(entObj.getOID());
                pstObj.setString(FLD_FULL_NAME, entObj.getFullName());
                pstObj.setString(FLD_EMAIL, entObj.getEmail());
                pstObj.setString(FLD_DESCRIPTION, entObj.getDescription());
                pstObj.setDate(FLD_UPDATE_DATE, new Date());
                pstObj.setInt(FLD_USER_STATUS, entObj.getUserStatus());
                pstObj.setDate(FLD_LAST_LOGIN_DATE, entObj.getLastLoginDate());
                pstObj.setString(FLD_LAST_LOGIN_IP, entObj.getLastLoginIp());
                pstObj.setLong(FLD_EMPLOYEE_ID, entObj.getEmployeeId());
                pstObj.setInt(FLD_USER_GROUP_NEW, entObj.getUserGroupNew());
                pstObj.setDate(FLD_START_TIME, entObj.getStartTime());
                pstObj.setDate(FLD_END_TIME, entObj.getEndTime());
                pstObj.update();
                System.out.println("------------> update user data by LoggedIn process");
                return entObj.getOID();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }

    //--------------- add by edhy ---------------------
    //--add by koyo------------------------------------
    public static long updateUserFingerData(AppUser entObj) {
        if ((entObj != null) && (entObj.getOID() != 0)) {
            try {
                PstAppUser pstObj = new PstAppUser(entObj.getOID());
                pstObj.setString(FLD_FINGER_DATA, entObj.getFingerData());
                pstObj.update();
                return entObj.getOID();
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        return 0;
    }

    public static JSONObject fetchJSON(long oid) {
        JSONObject object = new JSONObject();
        try {
            AppUser appUser = PstAppUser.fetch(oid);
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_USER_ID], appUser.getOID());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID], appUser.getLoginId());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD], appUser.getPassword());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME], appUser.getFullName());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_EMAIL], appUser.getEmail());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_DESCRIPTION], appUser.getDescription());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_REG_DATE], appUser.getRegDate());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_UPDATE_DATE], appUser.getUpdateDate());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS], appUser.getUserStatus());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_LAST_LOGIN_DATE], appUser.getLastLoginDate());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_LAST_LOGIN_IP], appUser.getLastLoginIp());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_EMPLOYEE_ID], appUser.getEmployeeId());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW], appUser.getUserGroupNew());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_START_TIME], appUser.getStartTime());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_END_TIME], appUser.getEndTime());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_FINGER_DATA], appUser.getFingerData());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_COMPANY_ID], appUser.getCompanyId());
        } catch (Exception exc) {
        }
        return object;
    }
    
    public static JSONObject jsonData(AppUser ap) {
        JSONObject object = new JSONObject();
        try {
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_USER_ID], ap.getOID());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID], ap.getLoginId());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD], ap.getPassword());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME], ap.getFullName());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_EMAIL], ap.getEmail().equals("") ? "-" : ap.getEmail());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_DESCRIPTION], ap.getDescription().equals("") ? "-" : ap.getDescription());
            object.put(PstAppUser.fieldNames[PstAppUser.FLD_EMPLOYEE_ID], ap.getEmployeeId());
        } catch (Exception exc) {
        }
        return object;
    }
    
    public static JSONArray checkUserGroup(String[] groupPemakai){
        JSONArray result = new JSONArray();
        try {
            String groupToSedanaRaw = PstSystemProperty.getValueByName("GROUP_TO_SEDANA");
            String[] groupToSedanaProcessed = groupToSedanaRaw.split("#");
            String groupSedana = "";
            for(int i = 0; i < groupToSedanaProcessed.length; i++){
                if(i != 0){
                    groupSedana += ", ";
                }
                String group = groupToSedanaProcessed[i];
                groupSedana += "'" + group + "'";
            }
           
            Vector<AppGroup> listGroupToSedana = PstAppGroup.list(0, 0, PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_NAME] + " IN (" + groupSedana + ")", "");
            if(groupPemakai != null){
                for(int i = 0; i < groupPemakai.length; i++){
                    long id = Long.parseLong(groupPemakai[i]);
                    for(AppGroup ag : listGroupToSedana){
                        JSONObject temp = new JSONObject();
                        if(id == ag.getOID()){
                            try {
                                temp.put("USER_GROUP_NAME", "" + ag.getGroupName()); 
                            } catch (Exception e) {
                                System.out.println("Kenapa ya? : " + e.getMessage());
                            }
                            result.put(temp);
                        }
                    }        
                }
            }
            
        } catch (Exception e) {
        }
        return result;
    }
    
    
    public static Vector listUserManagement(int start, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " ap."+PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]
                    + ", ap."+PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]
                    + ", ap."+PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS]
                    + ", ap."+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]
                    + ", loc."+PstLocation.fieldNames[PstLocation.FLD_NAME]
                    + " FROM "
                    + ""+PstAppUser.TBL_APP_USER + " AS ap"
                    + " INNER JOIN "+ PstDataCustom.TBL_DATA_CUSTOM + " AS dc"
                    + " ON dc."+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]
                    + " = ap."+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]
                    + " INNER JOIN "+ PstLocation.TBL_P2_LOCATION + " AS loc"
                    + " ON loc."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
                    + " = dc."+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_VALUE]
                    + "" ;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql = sql + " GROUP BY ap."+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID];
            
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    }
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
                    }
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (start == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    }
            }
            System.out.println("SQL User Management : "+ sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector data = new Vector();
                AppUser ap = new AppUser();
                Location loc = new Location();
                
                ap.setLoginId(rs.getString(""+PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]+""));
                ap.setFullName(rs.getString(""+PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]+""));
                ap.setUserStatus(rs.getInt(""+PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS]+""));
                ap.setOID(rs.getLong(PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]));
                data.add(ap);
                
                loc.setName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
                data.add(loc);
                
                lists.add(data);
            }
            return lists; 

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    
    public static int countUserManagement(String whereClause) {
        DBResultSet dbrs = null;
        try {
            int count = 0;
            String sql = " SELECT COUNT( ap." + fieldNames[FLD_USER_ID] + ") AS NRCOUNT"
                    + " FROM "
                    + ""+PstAppUser.TBL_APP_USER + " AS ap"
                    + " INNER JOIN "+ PstDataCustom.TBL_DATA_CUSTOM + " AS dc"
                    + " ON dc."+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]
                    + " = ap."+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]
                    + " INNER JOIN "+ PstLocation.TBL_P2_LOCATION + " AS loc"
                    + " ON loc."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
                    + " = dc."+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_VALUE]
                    + "" ;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
//            sql = sql + " GROUP BY ap."+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID];

            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception exc) {
            System.out.println("getCount " + exc);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }

    }
}






