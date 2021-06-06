/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author PC
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

public class PstRateJualBerlian extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_RATE_JUAL_BERLIAN = "pos_rate_jual_berlian";
    public static final int FLD_RATE_JUAL_BERLIAN_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_NAME = 2;
    public static final int FLD_DESCRIPTION = 3;
    public static final int FLD_RATE = 4;
    public static final int FLD_UPDATE_DATE = 5;
    public static final int FLD_STATUS_AKTIF = 6;

    public static String[] fieldNames = {
        "RATE_JUAL_BERLIAN_ID",
        "CODE",
        "NAME",
        "DESCRIPTION",
        "RATE",
        "UPDATE_DATE",
        "STATUS_AKTIF"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_INT
    };

    public PstRateJualBerlian() {
    }

    public PstRateJualBerlian(int i) throws DBException {
        super(new PstRateJualBerlian());
    }

    public PstRateJualBerlian(String sOid) throws DBException {
        super(new PstRateJualBerlian(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstRateJualBerlian(long lOid) throws DBException {
        super(new PstRateJualBerlian(0));
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
        return TBL_RATE_JUAL_BERLIAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstRateJualBerlian().getClass().getName();
    }

    public static RateJualBerlian fetchExc(long oid) throws DBException {
        try {
            RateJualBerlian entRateJualBerlian = new RateJualBerlian();
            PstRateJualBerlian pstRateJualBerlian = new PstRateJualBerlian(oid);
            entRateJualBerlian.setOID(oid);
            entRateJualBerlian.setCode(pstRateJualBerlian.getString(FLD_CODE));
            entRateJualBerlian.setName(pstRateJualBerlian.getString(FLD_NAME));
            entRateJualBerlian.setDescription(pstRateJualBerlian.getString(FLD_DESCRIPTION));
            entRateJualBerlian.setRate(pstRateJualBerlian.getdouble(FLD_RATE));
            entRateJualBerlian.setUpdateDate(pstRateJualBerlian.getDate(FLD_UPDATE_DATE));
            entRateJualBerlian.setStatusAktif(pstRateJualBerlian.getInt(FLD_STATUS_AKTIF));
            return entRateJualBerlian;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRateJualBerlian(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        RateJualBerlian entRateJualBerlian = fetchExc(entity.getOID());
        entity = (Entity) entRateJualBerlian;
        return entRateJualBerlian.getOID();
    }

    public static synchronized long updateExc(RateJualBerlian entRateJualBerlian) throws DBException {
        try {
            if (entRateJualBerlian.getOID() != 0) {
                PstRateJualBerlian pstRateJualBerlian = new PstRateJualBerlian(entRateJualBerlian.getOID());
                pstRateJualBerlian.setString(FLD_CODE, entRateJualBerlian.getCode());
                pstRateJualBerlian.setString(FLD_NAME, entRateJualBerlian.getName());
                pstRateJualBerlian.setString(FLD_DESCRIPTION, entRateJualBerlian.getDescription());
                pstRateJualBerlian.setDouble(FLD_RATE, entRateJualBerlian.getRate());
                pstRateJualBerlian.setDate(FLD_UPDATE_DATE, entRateJualBerlian.getUpdateDate());
                pstRateJualBerlian.setInt(FLD_STATUS_AKTIF, entRateJualBerlian.getStatusAktif());
                pstRateJualBerlian.update();
                return entRateJualBerlian.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRateJualBerlian(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((RateJualBerlian) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstRateJualBerlian pstRateJualBerlian = new PstRateJualBerlian(oid);
            pstRateJualBerlian.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRateJualBerlian(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(RateJualBerlian entRateJualBerlian) throws DBException {
        try {
            PstRateJualBerlian pstRateJualBerlian = new PstRateJualBerlian(0);
            pstRateJualBerlian.setString(FLD_CODE, entRateJualBerlian.getCode());
            pstRateJualBerlian.setString(FLD_NAME, entRateJualBerlian.getName());
            pstRateJualBerlian.setString(FLD_DESCRIPTION, entRateJualBerlian.getDescription());
            pstRateJualBerlian.setDouble(FLD_RATE, entRateJualBerlian.getRate());
            pstRateJualBerlian.setDate(FLD_UPDATE_DATE, entRateJualBerlian.getUpdateDate());
            pstRateJualBerlian.setInt(FLD_STATUS_AKTIF, entRateJualBerlian.getStatusAktif());
            pstRateJualBerlian.insert();
            entRateJualBerlian.setOID(pstRateJualBerlian.getlong(FLD_RATE_JUAL_BERLIAN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRateJualBerlian(0), DBException.UNKNOWN);
        }
        return entRateJualBerlian.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((RateJualBerlian) entity);
    }

    public static void resultToObject(ResultSet rs, RateJualBerlian entRateJualBerlian) {
        try {
            entRateJualBerlian.setOID(rs.getLong(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE_JUAL_BERLIAN_ID]));
            entRateJualBerlian.setCode(rs.getString(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_CODE]));
            entRateJualBerlian.setName(rs.getString(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_NAME]));
            entRateJualBerlian.setDescription(rs.getString(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_DESCRIPTION]));
            entRateJualBerlian.setRate(rs.getDouble(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE]));
            entRateJualBerlian.setUpdateDate(rs.getTimestamp(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_UPDATE_DATE]));
            entRateJualBerlian.setStatusAktif(rs.getInt(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_STATUS_AKTIF]));
        } catch (Exception e) {
        }
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_RATE_JUAL_BERLIAN;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                RateJualBerlian entRateJualBerlian = new RateJualBerlian();
                resultToObject(rs, entRateJualBerlian);
                lists.add(entRateJualBerlian);
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

    public static boolean checkOID(long entRateJualBerlianId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_RATE_JUAL_BERLIAN + " WHERE "
                    + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE_JUAL_BERLIAN_ID] + " = " + entRateJualBerlianId;
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
            return result;
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE_JUAL_BERLIAN_ID] + ") FROM " + TBL_RATE_JUAL_BERLIAN;
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

    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    RateJualBerlian entRateJualBerlian = (RateJualBerlian) list.get(ls);
                    if (oid == entRateJualBerlian.getOID()) {
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
                    System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                RateJualBerlian rateJualBerlian = PstRateJualBerlian.fetchExc(oid);
                object.put(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE_JUAL_BERLIAN_ID], rateJualBerlian.getOID());
                object.put(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_CODE], rateJualBerlian.getCode());
                object.put(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_DESCRIPTION], rateJualBerlian.getDescription());
                object.put(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_NAME], rateJualBerlian.getName());
                object.put(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE], rateJualBerlian.getRate());
                object.put(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_STATUS_AKTIF], rateJualBerlian.getStatusAktif());
                object.put(PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_UPDATE_DATE], rateJualBerlian.getUpdateDate());
            }catch(Exception exc){}

            return object;
        }
}
