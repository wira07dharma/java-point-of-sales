package com.dimata.posbo.entity.masterdata;

/* package java */

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class PstSales extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_SALES = "POS_SALES_PERSON";
    public static final String TBL_SALES = "pos_sales_person";

    public static final int FLD_SALES_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_NAME = 2;
    public static final int FLD_REMARK = 3;
    public static final int FLD_COMMISION = 4;
    //adding login id & password by mirahu 20120514
    public static final int FLD_LOGIN_ID = 5;
    public static final int FLD_PASSWORD = 6;
    //add opie-eyek 20131113 untuk location sales
    public static final int FLD_ASSIGN_LOCATION_WAREHOUSE = 7;
    public static final int FLD_STANDARD_CURRENCY_ID=8;
    //added by dewok 2017
    public static final int FLD_EMPLOYEE_ID=9;
    //added by dewok 2018-02-22
    public static final int FLD_STATUS=10;
    public static final int FLD_POSITION_ID=11;

    public static final String[] fieldNames =
            {
                "SALES_ID",
                "SALES_CODE",
                "SALES_NAME",
                "REMARK",
                "COMMISION",
                //adding login id & password by mirahu 20120514
                "LOGIN_ID",
                "PASSWORD",
                "ASSIGN_LOCATION_WAREHOUSE",
                "STANDARD_CURRENCY_ID",
                "EMPLOYEE_ID",
                "STATUS",
                "POSITION_ID"
            };

    public static final int[] fieldTypes =
            {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_FLOAT,
                //adding login id & password by mirahu 20120514
                TYPE_STRING,
                TYPE_STRING,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_LONG
            };

    public PstSales() {
    }

    public PstSales(int i) throws DBException {
        super(new PstSales());
    }

    public PstSales(String sOid) throws DBException {
        super(new PstSales(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstSales(long lOid) throws DBException {
        super(new PstSales(0));
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
        return TBL_SALES;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstSales().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Sales matSales = fetchExc(ent.getOID());
        ent = (Entity) matSales;
        return matSales.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Sales) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Sales) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Sales fetchExc(long oid) throws DBException {
        try {
            Sales matSales = new Sales();
            PstSales pstSales = new PstSales(oid);
            matSales.setOID(oid);

            matSales.setCode(pstSales.getString(FLD_CODE));
            matSales.setName(pstSales.getString(FLD_NAME));
            matSales.setRemark(pstSales.getString(FLD_REMARK));
            matSales.setCommision(pstSales.getdouble(FLD_COMMISION));
            //adding login id & password by mirahu 20120514
            matSales.setLoginId(pstSales.getString(FLD_LOGIN_ID));
            matSales.setPassword(pstSales.getString(FLD_PASSWORD));
            matSales.setLocationId(pstSales.getlong(FLD_ASSIGN_LOCATION_WAREHOUSE));
            matSales.setDefaultCurrencyId(pstSales.getlong(FLD_STANDARD_CURRENCY_ID));
            //added by dewok 2017
            matSales.setEmployeeId(pstSales.getlong(FLD_EMPLOYEE_ID));
            matSales.setStatus(pstSales.getInt(FLD_STATUS));
            matSales.setPositionId(pstSales.getlong(FLD_POSITION_ID));
            
            return matSales;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSales(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Sales matSales) throws DBException {
        try {
            PstSales pstSales = new PstSales(0);

            pstSales.setString(FLD_CODE, matSales.getCode());
            pstSales.setString(FLD_NAME, matSales.getName());
            pstSales.setString(FLD_REMARK, matSales.getRemark());
            pstSales.setDouble(FLD_COMMISION, matSales.getCommision());
            //adding login id & password by mirahu 20120514
            pstSales.setString(FLD_LOGIN_ID, matSales.getLoginId());
            pstSales.setString(FLD_PASSWORD, matSales.getPassword());
            pstSales.setLong(FLD_ASSIGN_LOCATION_WAREHOUSE,matSales.getLocationId());
            pstSales.setLong(FLD_STANDARD_CURRENCY_ID, matSales.getDefaultCurrencyId());
            //added by dewok 2017
            pstSales.setLong(FLD_EMPLOYEE_ID, matSales.getEmployeeId());
            pstSales.setInt(FLD_STATUS, matSales.getStatus());
            pstSales.setLong(FLD_POSITION_ID, matSales.getPositionId());
            
            pstSales.insert();
            //update opie-eyek 20130815
            long oidDataSync=PstDataSyncSql.insertExc(pstSales.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            matSales.setOID(pstSales.getlong(FLD_SALES_ID));
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstSales.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSales(0), DBException.UNKNOWN);
        }
        return matSales.getOID();
    }

    public static long updateExc(Sales matSales) throws DBException {
        try {
            if (matSales.getOID() != 0) {
                PstSales pstSales = new PstSales(matSales.getOID());

                pstSales.setString(FLD_CODE, matSales.getCode());
                pstSales.setString(FLD_NAME, matSales.getName());
                pstSales.setString(FLD_REMARK, matSales.getRemark());
                pstSales.setDouble(FLD_COMMISION, matSales.getCommision());
                //adding login id & password by mirahu 20120514
                pstSales.setString(FLD_LOGIN_ID, matSales.getLoginId());
                pstSales.setString(FLD_PASSWORD, matSales.getPassword());
                pstSales.setLong(FLD_ASSIGN_LOCATION_WAREHOUSE, matSales.getLocationId());
                pstSales.setLong(FLD_STANDARD_CURRENCY_ID, matSales.getDefaultCurrencyId());
                //added by dewok 2017
                pstSales.setLong(FLD_EMPLOYEE_ID, matSales.getEmployeeId());
                pstSales.setInt(FLD_STATUS, matSales.getStatus());
                pstSales.setLong(FLD_POSITION_ID, matSales.getPositionId());
                
                pstSales.update();
                //update opie-eyek 20130815
                long oidDataSync = PstDataSyncSql.insertExc(pstSales.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstSales.getUpdateSQL());
                return matSales.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSales(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstSales pstSales = new PstSales(oid);
            pstSales.delete();
            //update opie-eyek 20130815
            long oidDataSync = PstDataSyncSql.insertExc(pstSales.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstSales.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSales(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_SALES;
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
                Sales matSales = new Sales();
                resultToObject(rs, matSales);
                lists.add(matSales);
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
	
	public static Vector listServices(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_SALES + " SALES "
					+ "LEFT JOIN "+PstLocation.TBL_P2_LOCATION+" LOC ON "
					+ "SALES."+fieldNames[FLD_ASSIGN_LOCATION_WAREHOUSE]+"=LOC."
					+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
					+ " LEFT JOIN "+PstEmployee.TBL_HR_EMPLOYEE+" EMP ON "
					+ "SALES."+fieldNames[FLD_EMPLOYEE_ID]+"=EMP."
					+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
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
				Vector vect = new Vector(1, 1);
                Sales matSales = new Sales();
				Location loc = new Location();
				Employee emp = new Employee();
                resultToObject(rs, matSales);
				vect.add(matSales);
				
				loc.setName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
				vect.add(loc);
				
				emp.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
				vect.add(emp);
				
                lists.add(vect);
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

    private static void resultToObject(ResultSet rs, Sales matSales) {
        try {
            matSales.setOID(rs.getLong(PstSales.fieldNames[PstSales.FLD_SALES_ID]));
            matSales.setCode(rs.getString(PstSales.fieldNames[PstSales.FLD_CODE]));
            matSales.setName(rs.getString(PstSales.fieldNames[PstSales.FLD_NAME]));
            matSales.setRemark(rs.getString(PstSales.fieldNames[PstSales.FLD_REMARK]));
            matSales.setCommision(rs.getDouble(PstSales.fieldNames[PstSales.FLD_COMMISION]));
            //adding login id & password by mirahu 20120514
            matSales.setLoginId(rs.getString(PstSales.fieldNames[PstSales.FLD_LOGIN_ID]));
            matSales.setPassword(rs.getString(PstSales.fieldNames[PstSales.FLD_PASSWORD]));
            //adding location assign and currency standar by opie-eyek 20131128
            matSales.setLocationId(rs.getLong(PstSales.fieldNames[PstSales.FLD_ASSIGN_LOCATION_WAREHOUSE]));
            matSales.setDefaultCurrencyId(rs.getLong(PstSales.fieldNames[PstSales.FLD_STANDARD_CURRENCY_ID]));
            //added by dewok 2017
            matSales.setEmployeeId(rs.getLong(PstSales.fieldNames[PstSales.FLD_EMPLOYEE_ID]));
            matSales.setStatus(rs.getInt(PstSales.fieldNames[PstSales.FLD_STATUS]));
            matSales.setPositionId(rs.getLong(PstSales.fieldNames[PstSales.FLD_POSITION_ID]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long shiftId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_SALES + " WHERE " +
                    PstSales.fieldNames[PstSales.FLD_SALES_ID] + " = " + shiftId;

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

            String sql = "SELECT COUNT(" + PstSales.fieldNames[PstSales.FLD_SALES_ID] + ") FROM " + TBL_SALES;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            // DBHandler.execQueryResult("COMMIT");
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
                    Sales matSales = (Sales) list.get(ls);
                    if (oid == matSales.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    //This method use to get sales names according code sales
    public static String getNameSales(String codeSales) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstSales.fieldNames[PstSales.FLD_NAME] + " FROM " + TBL_SALES +
                    " WHERE " + PstSales.fieldNames[PstSales.FLD_CODE] + " = '" + codeSales + "'";

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

    //This method use to get sales names according code sales
    public static Sales getObjectSales(String codeSales) {
        DBResultSet dbrs = null;
        Sales sales = new Sales();
        try {
            String sql = "SELECT * FROM " + TBL_SALES +
                    " WHERE " + PstSales.fieldNames[PstSales.FLD_CODE] + " = '" + codeSales + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            resultToObject(rs, sales);
            rs.close();
        } catch (Exception e) {
            return sales;
        } finally {
            DBResultSet.close(dbrs);
        }
        return sales;
    }

    /**
     * Ari_wiweka 20130827
     * untuk cek login sales
     */
    public static Sales getByLoginIDAndPassword(String loginID, String password) {
        if((loginID==null) || (loginID.length()<1) || (password==null) || (password.length()<1))
        return null;

        try{

            String whereClause = " "+fieldNames[FLD_LOGIN_ID]+"='"+loginID.trim()+"' AND "
            +fieldNames[FLD_PASSWORD] +"='"+password.trim()+"'";

            Vector appUsers = list(0,0, whereClause, "");

            if( (appUsers==null) || (appUsers.size()!=1))
                return new Sales();

            return (Sales)  appUsers.get(0);

        } catch(Exception e) {
            System.out.println("getByLoginIDAndPassword " +e);
            return null;
        }
    }
    
    //added by dewok 20180315
    public static Vector listJoinEmployee(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_SALES + " AS sp"
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS emp"
                    + " ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                    + " = sp." + fieldNames[FLD_EMPLOYEE_ID]
                    + "";
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
                Sales matSales = new Sales();
                resultToObject(rs, matSales);
                lists.add(matSales);
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
    
  
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
               Sales sales = PstSales.fetchExc(oid);
               object.put(PstSales.fieldNames[PstSales.FLD_SALES_ID], ""+sales.getOID());
               object.put(PstSales.fieldNames[PstSales.FLD_ASSIGN_LOCATION_WAREHOUSE], ""+sales.getLocationId());
               object.put(PstSales.fieldNames[PstSales.FLD_CODE], ""+sales.getCode());
               object.put(PstSales.fieldNames[PstSales.FLD_COMMISION], ""+sales.getCommision());
               object.put(PstSales.fieldNames[PstSales.FLD_EMPLOYEE_ID], ""+sales.getEmployeeId());
               object.put(PstSales.fieldNames[PstSales.FLD_LOGIN_ID], ""+sales.getLoginId());
               object.put(PstSales.fieldNames[PstSales.FLD_NAME], ""+sales.getName());
               object.put(PstSales.fieldNames[PstSales.FLD_PASSWORD], ""+sales.getPassword());
               object.put(PstSales.fieldNames[PstSales.FLD_REMARK], ""+sales.getRemark());
               object.put(PstSales.fieldNames[PstSales.FLD_STANDARD_CURRENCY_ID], ""+sales.getDefaultCurrencyId());
               object.put(PstSales.fieldNames[PstSales.FLD_STATUS], ""+sales.getStatus());
            }catch(Exception exc){}

            return object;
        }

}
