/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.masterdata;

/* package java */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

/**
 *
 * @author PT. Dimata
 */
public class PstCosting extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
     //public static final String TBL_COSTING = "POS_COSTING";
    public static final String TBL_COSTING = "pos_costing";

    public static final int FLD_COSTING_ID = 0;
    public static final int FLD_NAME = 1;
    public static final int FLD_DESCRIPTION = 2;


    public static final String[] fieldNames =
            {
                "COSTING_ID",
                "NAME",
                "DESCRIPTION",
            };

    public static final int[] fieldTypes =
            {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_STRING,
                TYPE_STRING,
            };

    public PstCosting() {
    }

    public PstCosting(int i) throws DBException {
        super(new PstCosting());
    }

    public PstCosting(String sOid) throws DBException {
        super(new PstCosting(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstCosting(long lOid) throws DBException {
        super(new PstCosting(0));
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
        return TBL_COSTING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCosting().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Costing costing = fetchExc(ent.getOID());
        ent = (Entity) costing;
        return costing.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Costing) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Costing) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Costing fetchExc(long oid) throws DBException {
        try {
            Costing costing = new Costing();
            PstCosting pstCosting = new PstCosting(oid);
            costing.setOID(oid);

            costing.setName(pstCosting.getString(FLD_NAME));
            costing.setDescription(pstCosting.getString(FLD_DESCRIPTION));

            return costing;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCosting(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Costing costing) throws DBException {
        try {
            PstCosting pstCosting = new PstCosting(0);

            pstCosting.setString(FLD_NAME, costing.getName());
            pstCosting.setString(FLD_DESCRIPTION, costing.getDescription());

            pstCosting.insert();
            costing.setOID(pstCosting.getlong(FLD_COSTING_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCosting(0), DBException.UNKNOWN);
        }
        return costing.getOID();
    }

    public static long updateExc(Costing costing) throws DBException {
        try {
            if (costing.getOID() != 0) {
                PstCosting pstCosting = new PstCosting(costing.getOID());

                pstCosting.setString(FLD_NAME, costing.getName());
                pstCosting.setString(FLD_DESCRIPTION, costing.getDescription());


                pstCosting.update();
                return costing.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCosting(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstCosting pstCosting = new PstCosting(oid);
            pstCosting.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCosting(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_COSTING;
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
                Costing costing = new Costing();
                resultToObject(rs, costing);
                lists.add(costing);
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

    private static void resultToObject(ResultSet rs, Costing costing) {
        try {
            costing.setOID(rs.getLong(PstCosting.fieldNames[PstCosting.FLD_COSTING_ID]));
            costing.setName(rs.getString(PstCosting.fieldNames[PstCosting.FLD_NAME]));
            costing.setDescription(rs.getString(PstCosting.fieldNames[PstCosting.FLD_DESCRIPTION]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long costingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_COSTING + " WHERE " +
                    PstCosting.fieldNames[PstCosting.FLD_COSTING_ID] + " = " + costingId;

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
            String sql = "SELECT COUNT(" + PstCosting.fieldNames[PstCosting.FLD_COSTING_ID] + ") FROM " + TBL_COSTING;
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
                    Costing costing = (Costing) list.get(ls);
                    if (oid == costing.getOID())
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
                Costing costing = PstCosting.fetchExc(oid);
                object.put(PstCosting.fieldNames[PstCosting.FLD_COSTING_ID], costing.getOID());
                object.put(PstCosting.fieldNames[PstCosting.FLD_DESCRIPTION], costing.getDescription());
                object.put(PstCosting.fieldNames[PstCosting.FLD_NAME], costing.getName());
            }catch(Exception exc){}

            return object;
        }
}
