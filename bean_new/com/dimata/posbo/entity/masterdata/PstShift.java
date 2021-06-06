package com.dimata.posbo.entity.masterdata;

/* package java */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class PstShift extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final String TBL_SHIFT = "POS_SHIFT";
    public static final String TBL_SHIFT = "pos_shift";

    public static final int FLD_SHIFT_ID = 0;
    public static final int FLD_NAME = 1;
    public static final int FLD_REMARK = 2;
    public static final int FLD_START_TIME = 3;
    public static final int FLD_END_TIME = 4;

    public static final String[] fieldNames =
            {
                "SHIFT_ID",
                "NAME",
                "REMARK",
                "START_TIME", // START TIME , TIME IN USE ONLY
                "END_TIME" // END TIME , TIME IN USE ONLY
            };

    public static final int[] fieldTypes =
            {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_DATE,
                TYPE_DATE
            };

    public PstShift() {
    }

    public PstShift(int i) throws DBException {
        super(new PstShift());
    }

    public PstShift(String sOid) throws DBException {
        super(new PstShift(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstShift(long lOid) throws DBException {
        super(new PstShift(0));
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
        return TBL_SHIFT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstShift().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Shift matShift = fetchExc(ent.getOID());
        ent = (Entity) matShift;
        return matShift.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Shift) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Shift) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Shift fetchExc(long oid) throws DBException {
        try {
            Shift matShift = new Shift();
            PstShift pstShift = new PstShift(oid);
            matShift.setOID(oid);

            matShift.setName(pstShift.getString(FLD_NAME));
            matShift.setRemark(pstShift.getString(FLD_REMARK));
            matShift.setStartTime(pstShift.getDate(FLD_START_TIME));
            matShift.setEndTime(pstShift.getDate(FLD_END_TIME));

            return matShift;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstShift(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Shift matShift) throws DBException {
        try {
            PstShift pstShift = new PstShift(0);

            pstShift.setString(FLD_NAME, matShift.getName());
            pstShift.setString(FLD_REMARK, matShift.getRemark());
            pstShift.setDate(FLD_START_TIME, matShift.getStartTime());
            pstShift.setDate(FLD_END_TIME, matShift.getEndTime());

            pstShift.insert();
            matShift.setOID(pstShift.getlong(FLD_SHIFT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstShift(0), DBException.UNKNOWN);
        }
        return matShift.getOID();
    }

    public static long updateExc(Shift matShift) throws DBException {
        try {
            if (matShift.getOID() != 0) {
                PstShift pstShift = new PstShift(matShift.getOID());

                pstShift.setString(FLD_REMARK, matShift.getRemark());
                pstShift.setString(FLD_NAME, matShift.getName());
                pstShift.setDate(FLD_START_TIME, matShift.getStartTime());
                pstShift.setDate(FLD_END_TIME, matShift.getEndTime());

                pstShift.update();
                return matShift.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstShift(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstShift pstShift = new PstShift(oid);
            pstShift.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstShift(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_SHIFT;
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
                Shift matShift = new Shift();
                resultToObject(rs, matShift);
                lists.add(matShift);
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
    
    
    public static Vector listDua(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_SHIFT;
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
                Shift matShift = new Shift();
                resultToObjectDua(rs, matShift);
                lists.add(matShift);
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

    private static void resultToObject(ResultSet rs, Shift matShift) {
        try {
            matShift.setOID(rs.getLong(PstShift.fieldNames[PstShift.FLD_SHIFT_ID]));
            matShift.setName(rs.getString(PstShift.fieldNames[PstShift.FLD_NAME]));
            matShift.setRemark(rs.getString(PstShift.fieldNames[PstShift.FLD_REMARK]));
            matShift.setStartTime(DBHandler.convertDate(rs.getDate(PstShift.fieldNames[PstShift.FLD_START_TIME]),rs.getTime(PstShift.fieldNames[PstShift.FLD_START_TIME])));
            matShift.setEndTime(DBHandler.convertDate(rs.getDate(PstShift.fieldNames[PstShift.FLD_END_TIME]),rs.getTime(PstShift.fieldNames[PstShift.FLD_END_TIME])));
        } catch (Exception e) {
        }
    }
    
    private static void resultToObjectDua(ResultSet rs, Shift matShift) {
        try {
            matShift.setOID(rs.getLong(PstShift.fieldNames[PstShift.FLD_SHIFT_ID]));
            matShift.setName(rs.getString(PstShift.fieldNames[PstShift.FLD_NAME]));
            matShift.setRemark(rs.getString(PstShift.fieldNames[PstShift.FLD_REMARK]));
            matShift.setStartTime(DBHandler.convertDate(rs.getDate(PstShift.fieldNames[PstShift.FLD_START_TIME]),rs.getTime(PstShift.fieldNames[PstShift.FLD_START_TIME])));
            matShift.setEndTime(DBHandler.convertDate(rs.getDate(PstShift.fieldNames[PstShift.FLD_END_TIME]),rs.getTime(PstShift.fieldNames[PstShift.FLD_END_TIME])));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long shiftId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_SHIFT + " WHERE " +
                    PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + " = " + shiftId;

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
            String sql = "SELECT COUNT(" + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + ") FROM " + TBL_SHIFT;
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
                    Shift matShift = (Shift) list.get(ls);
                    if (oid == matShift.getOID())
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
                Shift shift = PstShift.fetchExc(oid);
                object.put(PstShift.fieldNames[PstShift.FLD_SHIFT_ID], shift.getOID());
                object.put(PstShift.fieldNames[PstShift.FLD_END_TIME], shift.getEndTime());
                object.put(PstShift.fieldNames[PstShift.FLD_NAME], shift.getName());
                object.put(PstShift.fieldNames[PstShift.FLD_REMARK], shift.getRemark());
                object.put(PstShift.fieldNames[PstShift.FLD_START_TIME], shift.getStartTime());
            }catch(Exception exc){}

            return object;
        }
}
