/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author Dimata 007-Mirahu 
 * 20120517
 */

import java.sql.*;
import java.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class PstTableRoom extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    //public static final  String TBL_P2_table = "table";
    public static final String TBL_P2_TABLE = "pos_table";
    public static final int FLD_TABLE_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_ROOM_ID = 2;
    public static final int FLD_TABLE_NUMBER = 3;
    public static final int FLD_DIMENTION_L = 4;
    public static final int FLD_DIMENTION_W = 5;
    public static final int FLD_CAPACITY= 6;
    public static final int FLD_GRID_X = 7;
    public static final int FLD_GRID_Y = 8;
    public static final int FLD_SHAPE = 9;
    public static final int FLD_STATUS = 10;
    public static final int FLD_STATUS_MEJA = 11;
    
    public static final String[] fieldNames = {
        "TABLE_ID",
        "LOCATION_ID",
        "ROOM_ID",
        "TABLE_NUMBER",
        "DIMENTION_L",
        "DIMENTION_W",
        "CAPACITY",
        "GRID_X",
        "GRID_Y",
        "SHAPE",
        "STATUS",
        "TABLE_STATUS"
    };
    //update opie 21-06-2012
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT
    };
   

    
    public static final int TABLE_STATUS_FREE = 0;
    public static final int TABLE_STATUS_HALF = 1;
    public static final int TABLE_STATUS_FULL = 2;
    
    
    public static final int TABLE_CONDITION_CLEAN = 0;
    public static final int TABLE_CONDITION_DIRTY = 1;
    
    public PstTableRoom() {
    }

    public PstTableRoom(int i) throws DBException {
        super(new PstTableRoom());
    }

    public PstTableRoom(String sOid) throws DBException {
        super(new PstTableRoom(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstTableRoom(long lOid) throws DBException {
        super(new PstTableRoom(0));
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
        return TBL_P2_TABLE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstTableRoom().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        try {
            TableRoom tableRoom = (TableRoom) ent;
            long oid = ent.getOID();
            PstTableRoom pstTableRoom = new PstTableRoom(oid);
            tableRoom.setOID(oid);

            tableRoom.setLocationId(pstTableRoom.getlong(FLD_LOCATION_ID));
            tableRoom.setRoomId(pstTableRoom.getlong(FLD_ROOM_ID));
            tableRoom.setTableNumber(pstTableRoom.getString(FLD_TABLE_NUMBER));
            tableRoom.setDimentionL(pstTableRoom.getInt(FLD_DIMENTION_L));
            tableRoom.setDimentionW(pstTableRoom.getInt(FLD_DIMENTION_W));
            tableRoom.setCapacity(pstTableRoom.getInt(FLD_CAPACITY));
            tableRoom.setGridX(pstTableRoom.getInt(FLD_GRID_X));
            tableRoom.setGridY(pstTableRoom.getInt(FLD_GRID_Y));
            tableRoom.setShape(pstTableRoom.getString(FLD_SHAPE));
            tableRoom.setStatus(pstTableRoom.getInt(FLD_STATUS));
            tableRoom.setStatusTable(pstTableRoom.getInt(FLD_STATUS_MEJA));
            
            return tableRoom.getOID();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTableRoom(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((TableRoom) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((TableRoom) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static TableRoom fetchExc(long oid) throws DBException {
        try {
            TableRoom tableRoom = new TableRoom();

            PstTableRoom pstTableRoom = new PstTableRoom(oid);
            tableRoom.setOID(oid);
            
            
            tableRoom.setLocationId(pstTableRoom.getlong(FLD_LOCATION_ID));
            tableRoom.setRoomId(pstTableRoom.getlong(FLD_ROOM_ID));
            tableRoom.setTableNumber(pstTableRoom.getString(FLD_TABLE_NUMBER));
            tableRoom.setDimentionL(pstTableRoom.getInt(FLD_DIMENTION_L));
            tableRoom.setDimentionW(pstTableRoom.getInt(FLD_DIMENTION_W));
            tableRoom.setCapacity(pstTableRoom.getInt(FLD_CAPACITY));
            tableRoom.setGridX(pstTableRoom.getInt(FLD_GRID_X));
            tableRoom.setGridY(pstTableRoom.getInt(FLD_GRID_Y));
            tableRoom.setShape(pstTableRoom.getString(FLD_SHAPE));
            tableRoom.setStatus(pstTableRoom.getInt(FLD_STATUS));
            tableRoom.setStatusTable(pstTableRoom.getInt(FLD_STATUS_MEJA));
            
            return tableRoom;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTableRoom(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(TableRoom tableRoom) throws DBException {
        try {
            PstTableRoom pstTableRoom = new PstTableRoom(0);
            
            pstTableRoom.setLong(FLD_LOCATION_ID, tableRoom.getLocationId());
            pstTableRoom.setLong(FLD_ROOM_ID, tableRoom.getRoomId());
            pstTableRoom.setString(FLD_TABLE_NUMBER, tableRoom.getTableNumber());
            pstTableRoom.setInt(FLD_DIMENTION_L, tableRoom.getDimentionL());
            pstTableRoom.setInt(FLD_DIMENTION_W, tableRoom.getDimentionW());
            pstTableRoom.setInt(FLD_CAPACITY, tableRoom.getCapacity());
            pstTableRoom.setInt(FLD_GRID_X, tableRoom.getGridX());
            pstTableRoom.setInt(FLD_GRID_Y, tableRoom.getGridY());
            pstTableRoom.setString(FLD_SHAPE, tableRoom.getShape());
            pstTableRoom.setInt(FLD_STATUS, tableRoom.getStatus());
            pstTableRoom.setInt(FLD_STATUS_MEJA, tableRoom.getStatusTable());
            
            pstTableRoom.insert();
            
            tableRoom.setOID(pstTableRoom.getlong(FLD_TABLE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTableRoom(0), DBException.UNKNOWN);
        }
        return tableRoom.getOID();
    }

    public static long updateExc(TableRoom tableRoom) throws DBException {
        try {
            if (tableRoom.getOID() != 0) {
                PstTableRoom pstTableRoom = new PstTableRoom(tableRoom.getOID());
                //update opie 22-06-2012 masalah update meja
                pstTableRoom.setLong(FLD_LOCATION_ID, tableRoom.getLocationId());
                pstTableRoom.setLong(FLD_ROOM_ID, tableRoom.getRoomId());
                pstTableRoom.setString(FLD_TABLE_NUMBER, tableRoom.getTableNumber());
                pstTableRoom.setInt(FLD_DIMENTION_L, tableRoom.getDimentionL());
                pstTableRoom.setInt(FLD_DIMENTION_W, tableRoom.getDimentionW());
                pstTableRoom.setInt(FLD_CAPACITY, tableRoom.getCapacity());
                pstTableRoom.setInt(FLD_GRID_X, tableRoom.getGridX());
                pstTableRoom.setInt(FLD_GRID_Y, tableRoom.getGridY());
                pstTableRoom.setInt(FLD_STATUS, tableRoom.getStatus());
                pstTableRoom.setString(FLD_SHAPE, tableRoom.getShape());
                pstTableRoom.setInt(FLD_STATUS_MEJA, tableRoom.getStatusTable());
                
                pstTableRoom.update();
                return tableRoom.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTableRoom(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstTableRoom pstTableRoom = new PstTableRoom(oid);
            pstTableRoom.delete();
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTableRoom(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_P2_TABLE;
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
                TableRoom tableRoom = new TableRoom();
                resultToObject(rs, tableRoom);
                lists.add(tableRoom);
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

    private static void resultToObject(ResultSet rs, TableRoom tableRoom) {
        try {
            
            tableRoom.setOID(rs.getLong(PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_ID]));
            tableRoom.setLocationId(rs.getLong(PstTableRoom.fieldNames[PstTableRoom.FLD_LOCATION_ID]));
            tableRoom.setRoomId(rs.getLong(PstTableRoom.fieldNames[PstTableRoom.FLD_ROOM_ID]));
            tableRoom.setTableNumber(rs.getString(PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_NUMBER]));
            tableRoom.setDimentionL(rs.getInt(PstTableRoom.fieldNames[PstTableRoom.FLD_DIMENTION_L]));
            tableRoom.setDimentionW(rs.getInt(PstTableRoom.fieldNames[PstTableRoom.FLD_DIMENTION_W]));
            tableRoom.setCapacity(rs.getInt(PstTableRoom.fieldNames[PstTableRoom.FLD_CAPACITY]));
            tableRoom.setGridX(rs.getInt(PstTableRoom.fieldNames[PstTableRoom.FLD_GRID_X]));
            tableRoom.setGridY(rs.getInt(PstTableRoom.fieldNames[PstTableRoom.FLD_GRID_Y]));
            tableRoom.setShape(rs.getString(PstTableRoom.fieldNames[PstTableRoom.FLD_SHAPE]));   
            tableRoom.setStatus(rs.getInt(PstTableRoom.fieldNames[PstTableRoom.FLD_STATUS]));
            tableRoom.setStatusTable(rs.getInt(PstTableRoom.fieldNames[PstTableRoom.FLD_STATUS_MEJA]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long tableId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_P2_TABLE + " WHERE " +
                    PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_ID] + " = " + tableId;

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
            String sql = "SELECT COUNT(" + PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_ID] + ") FROM " + TBL_P2_TABLE;
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
    
    
    public static int getSumCapacity(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstTableRoom.fieldNames[PstTableRoom.FLD_CAPACITY] + ") FROM " + TBL_P2_TABLE;
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
                    TableRoom tableroom = (TableRoom) list.get(ls);
                    if (oid == tableroom.getOID()) {
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

    
    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstTableRoom.TBL_P2_TABLE + " SET " +
                    PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_ID] + " = " + originalOID +
                    " WHERE " + PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_ID] +
                    " = " + newOID;

            int Result = DBHandler.execUpdate(sql);


            return originalOID;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    
    
    public static long updateStatusCapacitynCondition(long tableId, int statusCapacity, int statusCondition) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstTableRoom.TBL_P2_TABLE + " SET " +
                    PstTableRoom.fieldNames[PstTableRoom.FLD_STATUS] + " = " + statusCapacity +", "+
                    PstTableRoom.fieldNames[PstTableRoom.FLD_STATUS_MEJA] + " = " + statusCondition +
                    " WHERE " + PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_ID] +
                    " = " + tableId;

            int Result = DBHandler.execUpdate(sql);


            return tableId;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public static long updateStatusConditionTable(long tableId, int statusCondition) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstTableRoom.TBL_P2_TABLE + " SET " +
                    PstTableRoom.fieldNames[PstTableRoom.FLD_STATUS_MEJA] + " = " + statusCondition +
                    " WHERE " + PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_ID] +
                    " = " + tableId;

            int Result = DBHandler.execUpdate(sql);


            return tableId;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    
    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null; 
        try {
            String sql = "SELECT * FROM " + TBL_P2_TABLE;
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
                TableRoom tableRoom = new TableRoom();
                resultToObject(rs, tableRoom);
                lists.add(tableRoom);
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
                TableRoom tableRoom = PstTableRoom.fetchExc(oid);
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_ID], tableRoom.getOID());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_CAPACITY], tableRoom.getCapacity());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_DIMENTION_L], tableRoom.getDimentionL());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_DIMENTION_W], tableRoom.getDimentionW());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_GRID_X], tableRoom.getGridX());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_GRID_Y], tableRoom.getGridY());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_LOCATION_ID], tableRoom.getLocationId());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_ROOM_ID], tableRoom.getRoomId());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_SHAPE], tableRoom.getShape());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_STATUS], tableRoom.getStatus());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_STATUS_MEJA], tableRoom.getStatusTable());
                object.put(PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_NUMBER], tableRoom.getTableNumber());
            }catch(Exception exc){}

            return object;
        }
    
}
