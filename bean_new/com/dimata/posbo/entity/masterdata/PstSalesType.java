/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;


/* package java */

import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

/**
 *
 * @author PT. Dimata
 */
public class PstSalesType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_MASTER_SALES_TYPE = "pos_master_sales_type";
    
    public static final int FLD_TYPE_SALES_ID = 0;
    public static final int FLD_TYPE_SALES_NAME = 1;
    public static final int FLD_TYPE_USE_SALES = 2;


    public static final String[] fieldNames =
            {
                "TYPE_SALES_ID",
                "TYPE_SALES_NAME",
                "TYPE_USE_SALES",
            };

    public static final int[] fieldTypes =
            {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_STRING,
                TYPE_INT,
            };

    
    public static final int TYPE_DINE_IN = 0;
    public static final int TYPE_TAKE_AWAY = 1;
    public static final int TYPE_DELIVERY = 2;
    public static final int TYPE_CATHERING = 3;
    public static final int TYPE_EVENT = 4;

    public static final String nameTypeSales[]= {
        "Dine In", "Take A Way","Delivery", "Cathering", "Event"
    };
    
    
    public PstSalesType() {
    }

    public PstSalesType(int i) throws DBException {
        super(new PstSalesType());
    }

    public PstSalesType(String sOid) throws DBException {
        super(new PstSalesType(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstSalesType(long lOid) throws DBException {
        super(new PstSalesType(0));
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
        return TBL_MASTER_SALES_TYPE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstSalesType().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        SalesType salesType = fetchExc(ent.getOID());
        ent = (Entity) salesType;
        return salesType.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((SalesType) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((SalesType) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static SalesType fetchExc(long oid) throws DBException {
        try {
            SalesType salesType = new SalesType();
            PstSalesType pstSalesType = new PstSalesType(oid);
            salesType.setOID(oid);

            salesType.setName(pstSalesType.getString(FLD_TYPE_SALES_NAME));
            salesType.setTypeSales(pstSalesType.getInt(FLD_TYPE_USE_SALES));

            return salesType;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSalesType(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(SalesType salesType) throws DBException {
        try {
            PstSalesType pstSalesType = new PstSalesType(0);

            pstSalesType.setString(FLD_TYPE_SALES_NAME, salesType.getName());
            pstSalesType.setInt(FLD_TYPE_USE_SALES, salesType.getTypeSales());

            pstSalesType.insert();
            salesType.setOID(pstSalesType.getlong(FLD_TYPE_SALES_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSalesType(0), DBException.UNKNOWN);
        }
        return salesType.getOID();
    }

    public static long updateExc(SalesType salesType) throws DBException {
        try {
            if (salesType.getOID() != 0) {
                PstSalesType pstSalesType = new PstSalesType(salesType.getOID());

                pstSalesType.setString(FLD_TYPE_SALES_NAME, salesType.getName());
                pstSalesType.setInt(FLD_TYPE_USE_SALES, salesType.getTypeSales());

                pstSalesType.update();
                return salesType.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSalesType(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstSalesType pstSalesType = new PstSalesType(oid);
            pstSalesType.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSalesType(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_MASTER_SALES_TYPE;
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
                SalesType salesType = new SalesType();
                resultToObject(rs, salesType);
                lists.add(salesType);
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

    private static void resultToObject(ResultSet rs, SalesType salesType) {
        try {
            salesType.setOID(rs.getLong(PstSalesType.fieldNames[PstSalesType.FLD_TYPE_SALES_ID]));
            salesType.setName(rs.getString(PstSalesType.fieldNames[PstSalesType.FLD_TYPE_SALES_NAME]));
            salesType.setTypeSales(rs.getInt(PstSalesType.fieldNames[PstSalesType.FLD_TYPE_USE_SALES]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long salesTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MASTER_SALES_TYPE + " WHERE " +
                    PstSalesType.fieldNames[PstSalesType.FLD_TYPE_SALES_ID] + " = " + salesTypeId;

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
            String sql = "SELECT COUNT(" + PstSalesType.fieldNames[PstSalesType.FLD_TYPE_SALES_ID] + ") FROM " + TBL_MASTER_SALES_TYPE;
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
                    SalesType salesType = (SalesType) list.get(ls);
                    if (oid == salesType.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }
    
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                SalesType salesType = PstSalesType.fetchExc(oid);
                object.put(PstSalesType.fieldNames[PstSalesType.FLD_TYPE_SALES_ID], salesType.getOID());
                object.put(PstSalesType.fieldNames[PstSalesType.FLD_TYPE_SALES_NAME], salesType.getName());
                object.put(PstSalesType.fieldNames[PstSalesType.FLD_TYPE_USE_SALES], salesType.getTypeSales());
            }catch(Exception exc){}

            return object;
        }

}
