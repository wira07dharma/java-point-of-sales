/* Created on 	:  [date] [time] AM/PM
 *
 * @author	:
 * @version	:
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.ij.entity.mapping;

// package java

import java.sql.*;
import java.util.*;

// package qdep
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;

// package ij
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.ij.entity.mapping.*;

public class PstIjLocationMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_IJ_MAPPING_LOCATION = "ij_mapping_location";

    public static final int FLD_IJ_MAP_LOCATION_ID = 0;
    public static final int FLD_TRANSACTION_TYPE = 1;
    public static final int FLD_SALES_TYPE = 2;
    public static final int FLD_CURRENCY = 3;
    public static final int FLD_LOCATION = 4;
    public static final int FLD_PROD_DEPARTMENT = 5;
    public static final int FLD_ACCOUNT = 6;
    public static final int FLD_BO_SYSTEM = 7;
    public static final int FLD_PRICE_TYPE = 8;

    public static final String[] fieldNames = {
        "IJ_MAP_LOCATION_ID",
        "TRANSACTION_TYPE",
        "SALES_TYPE",
        "CURRENCY",
        "LOCATION",
        "PROD_DEPARTMENT",
        "ACCOUNT",
        "BO_SYSTEM",
        "PRICE_TYPE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG
    };

    public PstIjLocationMapping() {
    }

    public PstIjLocationMapping(int i) throws DBException {
        super(new PstIjLocationMapping());
    }

    public PstIjLocationMapping(String sOid) throws DBException {
        super(new PstIjLocationMapping(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstIjLocationMapping(long lOid) throws DBException {
        super(new PstIjLocationMapping(0));
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
        return TBL_IJ_MAPPING_LOCATION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstIjLocationMapping().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        IjLocationMapping ijlocationmapping = fetchExc(ent.getOID());
        ent = (Entity) ijlocationmapping;
        return ijlocationmapping.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((IjLocationMapping) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((IjLocationMapping) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static IjLocationMapping fetchExc(long oid) throws DBException {
        try {
            IjLocationMapping ijlocationmapping = new IjLocationMapping();
            PstIjLocationMapping pstIjLocationMapping = new PstIjLocationMapping(oid);
            ijlocationmapping.setOID(oid);

            ijlocationmapping.setTransactionType(pstIjLocationMapping.getInt(FLD_TRANSACTION_TYPE));
            ijlocationmapping.setSalesType(pstIjLocationMapping.getInt(FLD_SALES_TYPE));
            ijlocationmapping.setCurrency(pstIjLocationMapping.getlong(FLD_CURRENCY));
            ijlocationmapping.setLocation(pstIjLocationMapping.getlong(FLD_LOCATION));
            ijlocationmapping.setProdDepartment(pstIjLocationMapping.getlong(FLD_PROD_DEPARTMENT));
            ijlocationmapping.setAccount(pstIjLocationMapping.getlong(FLD_ACCOUNT));
            ijlocationmapping.setBoSystem(pstIjLocationMapping.getInt(FLD_BO_SYSTEM));
            ijlocationmapping.setPriceType(pstIjLocationMapping.getlong(FLD_PRICE_TYPE));

            return ijlocationmapping;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjLocationMapping(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(IjLocationMapping ijlocationmapping) throws DBException {
        try {
            PstIjLocationMapping pstIjLocationMapping = new PstIjLocationMapping(0);

            pstIjLocationMapping.setInt(FLD_TRANSACTION_TYPE, ijlocationmapping.getTransactionType());
            pstIjLocationMapping.setInt(FLD_SALES_TYPE, ijlocationmapping.getSalesType());
            pstIjLocationMapping.setLong(FLD_CURRENCY, ijlocationmapping.getCurrency());
            pstIjLocationMapping.setLong(FLD_LOCATION, ijlocationmapping.getLocation());
            pstIjLocationMapping.setLong(FLD_PROD_DEPARTMENT, ijlocationmapping.getProdDepartment());
            pstIjLocationMapping.setLong(FLD_ACCOUNT, ijlocationmapping.getAccount());
            pstIjLocationMapping.setInt(FLD_BO_SYSTEM, ijlocationmapping.getBoSystem());
            pstIjLocationMapping.setLong(FLD_PRICE_TYPE, ijlocationmapping.getPriceType());

            pstIjLocationMapping.insert();
            ijlocationmapping.setOID(pstIjLocationMapping.getlong(FLD_IJ_MAP_LOCATION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjLocationMapping(0), DBException.UNKNOWN);
        }
        return ijlocationmapping.getOID();
    }

    public static long updateExc(IjLocationMapping ijlocationmapping) throws DBException {
        try {
            if (ijlocationmapping.getOID() != 0) {
                PstIjLocationMapping pstIjLocationMapping = new PstIjLocationMapping(ijlocationmapping.getOID());

                pstIjLocationMapping.setInt(FLD_TRANSACTION_TYPE, ijlocationmapping.getTransactionType());
                pstIjLocationMapping.setInt(FLD_SALES_TYPE, ijlocationmapping.getSalesType());
                pstIjLocationMapping.setLong(FLD_CURRENCY, ijlocationmapping.getCurrency());
                pstIjLocationMapping.setLong(FLD_LOCATION, ijlocationmapping.getLocation());
                pstIjLocationMapping.setLong(FLD_PROD_DEPARTMENT, ijlocationmapping.getProdDepartment());
                pstIjLocationMapping.setLong(FLD_ACCOUNT, ijlocationmapping.getAccount());
                pstIjLocationMapping.setInt(FLD_BO_SYSTEM, ijlocationmapping.getBoSystem());
                pstIjLocationMapping.setLong(FLD_PRICE_TYPE, ijlocationmapping.getPriceType());

                pstIjLocationMapping.update();
                return ijlocationmapping.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjLocationMapping(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstIjLocationMapping pstIjLocationMapping = new PstIjLocationMapping(oid);
            pstIjLocationMapping.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstIjLocationMapping(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_IJ_MAPPING_LOCATION;
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
                    break;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                IjLocationMapping ijlocationmapping = new IjLocationMapping();
                resultToObject(rs, ijlocationmapping);
                lists.add(ijlocationmapping);
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

    private static void resultToObject(ResultSet rs, IjLocationMapping ijlocationmapping) {
        try {
            ijlocationmapping.setOID(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_IJ_MAP_LOCATION_ID]));
            ijlocationmapping.setTransactionType(rs.getInt(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_TRANSACTION_TYPE]));
            ijlocationmapping.setSalesType(rs.getInt(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_SALES_TYPE]));
            ijlocationmapping.setCurrency(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_CURRENCY]));
            ijlocationmapping.setLocation(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_LOCATION]));
            ijlocationmapping.setProdDepartment(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_PROD_DEPARTMENT]));
            ijlocationmapping.setAccount(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_ACCOUNT]));
            ijlocationmapping.setBoSystem(rs.getInt(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_BO_SYSTEM]));
            ijlocationmapping.setPriceType(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_PRICE_TYPE]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long ijMapLocationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_IJ_MAPPING_LOCATION + " WHERE " +
                    PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_IJ_MAP_LOCATION_ID] + " = " + ijMapLocationId;

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
            String sql = "SELECT COUNT(" + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_IJ_MAP_LOCATION_ID] + ") FROM " + TBL_IJ_MAPPING_LOCATION;
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
                    IjLocationMapping ijlocationmapping = (IjLocationMapping) list.get(ls);
                    if (oid == ijlocationmapping.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    /**
     * @param transactionType
     * @param transactionCurrency
     * @return
     * @created by Edhy
     */
  public static IjLocationMapping getObjIjLocationMapping(int transType, int saleType, long transCurrency, long transLoc, long transProdDept) {
        DBResultSet dbrs = null;
        IjLocationMapping objIjLocationMapping = new IjLocationMapping();
        try {
            String sql = "SELECT " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_IJ_MAP_LOCATION_ID] +
                    ", " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_TRANSACTION_TYPE] +
                    ", " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_SALES_TYPE] +
                    ", " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_CURRENCY] +
                    ", " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_LOCATION] +
                    ", " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_PROD_DEPARTMENT] +
                    ", " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_ACCOUNT] +
                    ", " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_BO_SYSTEM] +
                    ", " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_PRICE_TYPE] +
                    " FROM " + TBL_IJ_MAPPING_LOCATION +
                    " WHERE " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_TRANSACTION_TYPE] +
                    " = " + transType +
                    " AND " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_LOCATION] +
                    " = " + transLoc;

            if (transCurrency != 0) {
                sql = sql + " AND " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_CURRENCY] +
                    " = " + transCurrency;
            }

            if (saleType > -1) {
                sql = sql + " AND " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_SALES_TYPE] +
                        " = " + saleType;
            }

            if (transProdDept > 0) {
                sql = sql + " AND " + PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_PROD_DEPARTMENT] +
                        " = " + transProdDept;
            }

            System.out.println("-----------------------------> sql getObjIjLocationMapping : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                objIjLocationMapping.setOID(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_IJ_MAP_LOCATION_ID]));
                objIjLocationMapping.setTransactionType(rs.getInt(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_TRANSACTION_TYPE]));
                objIjLocationMapping.setSalesType(rs.getInt(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_SALES_TYPE]));
                objIjLocationMapping.setCurrency(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_CURRENCY]));
                objIjLocationMapping.setLocation(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_LOCATION]));
                objIjLocationMapping.setProdDepartment(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_PROD_DEPARTMENT]));
                objIjLocationMapping.setAccount(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_ACCOUNT]));
                objIjLocationMapping.setBoSystem(rs.getInt(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_BO_SYSTEM]));
                objIjLocationMapping.setPriceType(rs.getLong(PstIjLocationMapping.fieldNames[PstIjLocationMapping.FLD_PRICE_TYPE]));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return objIjLocationMapping;

    }
}
