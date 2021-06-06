/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author IanRizky
 */
public class PstChainPeriod extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CHAINPERIOD = "pos_chain_period";
    public static final int FLD_CHAIN_PERIOD_ID = 0;
    public static final int FLD_CHAIN_MAIN_ID = 1;
    public static final int FLD_TITLE = 2;
    public static final int FLD_INDEX = 3;
    public static final int FLD_DURATION = 4;

    public static String[] fieldNames = {
        "CHAIN_PERIOD_ID",
        "CHAIN_MAIN_ID",
        "TITLE",
        "IDX",
        "DURATION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG
    };

    public PstChainPeriod() {
    }

    public PstChainPeriod(int i) throws DBException {
        super(new PstChainPeriod());
    }

    public PstChainPeriod(String sOid) throws DBException {
        super(new PstChainPeriod(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstChainPeriod(long lOid) throws DBException {
        super(new PstChainPeriod(0));
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
        return TBL_CHAINPERIOD;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstChainPeriod().getClass().getName();
    }

    public static ChainPeriod fetchExc(long oid) throws DBException {
        try {
            ChainPeriod entChainPeriod = new ChainPeriod();
            PstChainPeriod pstChainPeriod = new PstChainPeriod(oid);
            entChainPeriod.setOID(oid);
            entChainPeriod.setChainMainId(pstChainPeriod.getlong(FLD_CHAIN_MAIN_ID));
            entChainPeriod.setTitle(pstChainPeriod.getString(FLD_TITLE));
            entChainPeriod.setIndex(pstChainPeriod.getInt(FLD_INDEX));
            entChainPeriod.setDuration(pstChainPeriod.getlong(FLD_DURATION));
            return entChainPeriod;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainPeriod(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ChainPeriod entChainPeriod = fetchExc(entity.getOID());
        entity = (Entity) entChainPeriod;
        return entChainPeriod.getOID();
    }

    public static synchronized long updateExc(ChainPeriod entChainPeriod) throws DBException {
        try {
            if (entChainPeriod.getOID() != 0) {
                PstChainPeriod pstChainPeriod = new PstChainPeriod(entChainPeriod.getOID());
                pstChainPeriod.setLong(FLD_CHAIN_MAIN_ID, entChainPeriod.getChainMainId());
                pstChainPeriod.setString(FLD_TITLE, entChainPeriod.getTitle());
                pstChainPeriod.setInt(FLD_INDEX, entChainPeriod.getIndex());
                pstChainPeriod.setLong(FLD_DURATION, entChainPeriod.getDuration());
                pstChainPeriod.update();
                return entChainPeriod.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainPeriod(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ChainPeriod) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstChainPeriod pstChainPeriod = new PstChainPeriod(oid);
            pstChainPeriod.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainPeriod(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ChainPeriod entChainPeriod) throws DBException {
        try {
            PstChainPeriod pstChainPeriod = new PstChainPeriod(0);
            pstChainPeriod.setLong(FLD_CHAIN_MAIN_ID, entChainPeriod.getChainMainId());
            pstChainPeriod.setString(FLD_TITLE, entChainPeriod.getTitle());
            pstChainPeriod.setInt(FLD_INDEX, entChainPeriod.getIndex());
            pstChainPeriod.setLong(FLD_DURATION, entChainPeriod.getDuration());
            pstChainPeriod.insert();
            entChainPeriod.setOID(pstChainPeriod.getlong(FLD_CHAIN_PERIOD_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstChainPeriod(0), DBException.UNKNOWN);
        }
        return entChainPeriod.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ChainPeriod) entity);
    }

    public static void resultToObject(ResultSet rs, ChainPeriod entChainPeriod) {
        try {
            entChainPeriod.setOID(rs.getLong(PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_PERIOD_ID]));
            entChainPeriod.setChainMainId(rs.getLong(PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID]));
            entChainPeriod.setTitle(rs.getString(PstChainPeriod.fieldNames[PstChainPeriod.FLD_TITLE]));
            entChainPeriod.setIndex(rs.getInt(PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX]));
            entChainPeriod.setDuration(rs.getLong(PstChainPeriod.fieldNames[PstChainPeriod.FLD_DURATION]));
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
            String sql = "SELECT * FROM " + TBL_CHAINPERIOD;
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
                ChainPeriod entChainPeriod = new ChainPeriod();
                resultToObject(rs, entChainPeriod);
                lists.add(entChainPeriod);
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

    public static boolean checkOID(long entChainPeriodId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CHAINPERIOD + " WHERE "
                    + PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_PERIOD_ID] + " = " + entChainPeriodId;
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
            String sql = "SELECT COUNT(" + PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_PERIOD_ID] + ") FROM " + TBL_CHAINPERIOD;
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
                    ChainPeriod entChainPeriod = (ChainPeriod) list.get(ls);
                    if (oid == entChainPeriod.getOID()) {
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
        } else if (start == (vectSize - recordToGet)) {
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
        return cmd;
    }
        
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                ChainPeriod  chainPeriod = PstChainPeriod.fetchExc(oid);
                object.put(PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_PERIOD_ID], chainPeriod.getOID());
                object.put(PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID], chainPeriod.getChainMainId());
                object.put(PstChainPeriod.fieldNames[PstChainPeriod.FLD_DURATION], chainPeriod.getDuration());
                object.put(PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX], chainPeriod.getIndex());
                object.put(PstChainPeriod.fieldNames[PstChainPeriod.FLD_TITLE], chainPeriod.getTitle());
                

            }catch(Exception exc){}

            return object;
        }
}
