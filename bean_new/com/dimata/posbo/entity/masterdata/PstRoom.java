/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author Dimata 007- Mirahu
 * 20120515
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


public class PstRoom extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    //public static final  String TBL_P2_room = "room";
    public static final String TBL_P2_ROOM = "pos_room";
    public static final int FLD_ROOM_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_CODE = 2;
    public static final int FLD_NAME = 3;
    public static final int FLD_DESCRIPTION = 4;
    public static final int FLD_STATUS = 5;
    public static final int FLD_ROOM_CLASS_ID = 6;
    public static final int FLD_CAPACITY = 7;
    public static final int FLD_CAPACITY_UNIT = 8;
    public static final int FLD_SHOW_INDEX = 9;

    public static final String[] fieldNames = {
        "ROOM_ID",
        "LOCATION_ID",
        "CODE",
        "NAME",
        "DESCRIPTION",
        "STATUS",
        "POS_ROOM_CLASS_ID",
        "CAPACITY",
        "CAPACITY_UNIT",
        "SHOW_INDEX"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT
    };
   

    public PstRoom() {
    }

    public PstRoom(int i) throws DBException {
        super(new PstRoom());
    }

    public PstRoom(String sOid) throws DBException {
        super(new PstRoom(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstRoom(long lOid) throws DBException {
        super(new PstRoom(0));
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
        return TBL_P2_ROOM;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstRoom().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        try {
            Room room = (Room) ent;
            long oid = ent.getOID();
            PstRoom pstroom = new PstRoom(oid);
            room.setOID(oid);

            room.setLocationId(pstroom.getlong(FLD_ROOM_ID));
            room.setCode(pstroom.getString(FLD_CODE));
            room.setName(pstroom.getString(FLD_NAME));
            room.setDescription(pstroom.getString(FLD_DESCRIPTION));
            room.setStatus(pstroom.getInt(FLD_STATUS));
            room.setRoomClassId(pstroom.getlong(FLD_ROOM_CLASS_ID));
            room.setCapacity(pstroom.getInt(FLD_CAPACITY));
            room.setCapacityUnit(pstroom.getString(FLD_CAPACITY_UNIT));
            room.setShowIndex(pstroom.getInt(FLD_SHOW_INDEX));

            return room.getOID();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoom(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Room) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Room) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Room fetchExc(long oid) throws DBException {
        try {
            Room room = new Room();

            PstRoom pstRoom = new PstRoom(oid);
            room.setOID(oid);
            
            room.setLocationId(pstRoom.getlong(FLD_LOCATION_ID));
            room.setCode(pstRoom.getString(FLD_CODE)); 
            room.setName(pstRoom.getString(FLD_NAME));            
            room.setDescription(pstRoom.getString(FLD_DESCRIPTION));
            room.setStatus(pstRoom.getInt(FLD_STATUS));
            room.setRoomClassId(pstRoom.getlong(FLD_ROOM_CLASS_ID));
            room.setCapacity(pstRoom.getInt(FLD_CAPACITY));
            room.setCapacityUnit(pstRoom.getString(FLD_CAPACITY_UNIT));
            room.setShowIndex(pstRoom.getInt(FLD_SHOW_INDEX));

            return room;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoom(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Room room) throws DBException {
        try {
            PstRoom pstRoom = new PstRoom(0);

            pstRoom.setLong(FLD_LOCATION_ID, room.getLocationId());
            pstRoom.setString(FLD_CODE, room.getCode());
            pstRoom.setString(FLD_NAME, room.getName());
            pstRoom.setString(FLD_DESCRIPTION, room.getDescription());
            pstRoom.setInt(FLD_STATUS, room.getStatus());
            pstRoom.setLong(FLD_ROOM_CLASS_ID, room.getRoomClassId());
            pstRoom.setInt(FLD_CAPACITY, room.getCapacity());
            pstRoom.setString(FLD_CAPACITY_UNIT, room.getCapacityUnit());
            pstRoom.setInt(FLD_SHOW_INDEX, room.getShowIndex());
            pstRoom.insert();
            
            room.setOID(pstRoom.getlong(FLD_ROOM_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoom(0), DBException.UNKNOWN);
        }
        return room.getOID();
    }

    public static long updateExc(Room room) throws DBException {
        try {
            if (room.getOID() != 0) {
                PstRoom pstroom = new PstRoom(room.getOID());
                
                pstroom.setLong(FLD_LOCATION_ID, room.getLocationId());
                pstroom.setString(FLD_CODE, room.getCode());
                pstroom.setString(FLD_NAME, room.getName());
                pstroom.setString(FLD_DESCRIPTION, room.getDescription());
                pstroom.setInt(FLD_STATUS, room.getStatus());
                pstroom.setLong(FLD_ROOM_CLASS_ID, room.getRoomClassId());
                pstroom.setInt(FLD_CAPACITY, room.getCapacity());
                pstroom.setString(FLD_CAPACITY_UNIT, room.getCapacityUnit());
                pstroom.setInt(FLD_SHOW_INDEX, room.getShowIndex());
               
                pstroom.update();
                
                return room.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoom(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstRoom pstRoom = new PstRoom(oid);
            pstRoom.delete();
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoom(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_P2_ROOM;
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
                Room room = new Room();
                resultToObject(rs, room);
                lists.add(room);
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

    private static void resultToObject(ResultSet rs, Room room) {
        try {
            room.setOID(rs.getLong(PstRoom.fieldNames[PstRoom.FLD_ROOM_ID]));
            room.setLocationId(rs.getLong(PstRoom.fieldNames[PstRoom.FLD_LOCATION_ID]));
            room.setCode(rs.getString(PstRoom.fieldNames[PstRoom.FLD_CODE]));
            room.setName(rs.getString(PstRoom.fieldNames[PstRoom.FLD_NAME]));
            room.setDescription(rs.getString(PstRoom.fieldNames[PstRoom.FLD_DESCRIPTION]));
            room.setStatus(rs.getInt(PstRoom.fieldNames[PstRoom.FLD_STATUS]));
            room.setRoomClassId(rs.getLong(PstRoom.fieldNames[PstRoom.FLD_ROOM_CLASS_ID]));
            room.setCapacity(rs.getInt(PstRoom.fieldNames[PstRoom.FLD_CAPACITY]));
            room.setCapacityUnit(rs.getString(PstRoom.fieldNames[PstRoom.FLD_CAPACITY_UNIT]));
            room.setShowIndex(rs.getInt(PstRoom.fieldNames[PstRoom.FLD_SHOW_INDEX]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long roomId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_P2_ROOM + " WHERE " +
                    PstRoom.fieldNames[PstRoom.FLD_ROOM_ID] + " = " + roomId;

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
            String sql = "SELECT COUNT(" + PstRoom.fieldNames[PstRoom.FLD_ROOM_ID] + ") FROM " + TBL_P2_ROOM;
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
                    Room room = (Room) list.get(ls);
                    if (oid == room.getOID()) {
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
            String sql = "UPDATE " + PstRoom.TBL_P2_ROOM + " SET " +
                    PstRoom.fieldNames[PstRoom.FLD_ROOM_ID] + " = " + originalOID +
                    " WHERE " + PstRoom.fieldNames[PstRoom.FLD_ROOM_ID] +
                    " = " + newOID;

            int Result = DBHandler.execUpdate(sql);


            return originalOID;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                Room room = PstRoom.fetchExc(oid);
                object.put(PstRoom.fieldNames[PstRoom.FLD_ROOM_ID], room.getOID());
                object.put(PstRoom.fieldNames[PstRoom.FLD_CAPACITY], room.getCapacity());
                object.put(PstRoom.fieldNames[PstRoom.FLD_CAPACITY_UNIT], room.getCapacityUnit());
                object.put(PstRoom.fieldNames[PstRoom.FLD_CODE], room.getCode());
                object.put(PstRoom.fieldNames[PstRoom.FLD_DESCRIPTION], room.getDescription());
                object.put(PstRoom.fieldNames[PstRoom.FLD_LOCATION_ID], room.getLocationId());
                object.put(PstRoom.fieldNames[PstRoom.FLD_NAME], room.getName());
                object.put(PstRoom.fieldNames[PstRoom.FLD_ROOM_CLASS_ID], room.getRoomClassId());
                object.put(PstRoom.fieldNames[PstRoom.FLD_SHOW_INDEX], room.getShowIndex());
                object.put(PstRoom.fieldNames[PstRoom.FLD_STATUS], room.getStatus());
            }catch(Exception exc){}

            return object;
        }    
    
}
