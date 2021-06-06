/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.roomclass;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstRoomClass extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_ROOM_CLASS = "pos_room_class";
    public static final int FLD_POS_ROOM_CLASS_ID = 0;
    public static final int FLD_CLASS_NAME = 1;
    public static final int FLD_CLASS_DESC = 2;

    public static String[] fieldNames = {
        "POS_ROOM_CLASS_ID",
        "CLASS_NAME",
        "CLASS_DESC"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING
    };

    public PstRoomClass() {
    }

    public PstRoomClass(int i) throws DBException {
        super(new PstRoomClass());
    }

    public PstRoomClass(String sOid) throws DBException {
        super(new PstRoomClass(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstRoomClass(long lOid) throws DBException {
        super(new PstRoomClass(0));
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
        return TBL_ROOM_CLASS;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstRoomClass().getClass().getName();
    }

    public static RoomClass fetchExc(long oid) throws DBException {
        try {
            RoomClass entRoomClass = new RoomClass();
            PstRoomClass pstRoomClass = new PstRoomClass(oid);
            entRoomClass.setOID(oid);
            entRoomClass.setClassName(pstRoomClass.getString(FLD_CLASS_NAME));
            entRoomClass.setClassDesc(pstRoomClass.getString(FLD_CLASS_DESC));
            return entRoomClass;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoomClass(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        RoomClass entRoomClass = fetchExc(entity.getOID());
        entity = (Entity) entRoomClass;
        return entRoomClass.getOID();
    }

    public static synchronized long updateExc(RoomClass entRoomClass) throws DBException {
        try {
            if (entRoomClass.getOID() != 0) {
                PstRoomClass pstRoomClass = new PstRoomClass(entRoomClass.getOID());
                pstRoomClass.setString(FLD_CLASS_NAME, entRoomClass.getClassName());
                pstRoomClass.setString(FLD_CLASS_DESC, entRoomClass.getClassDesc());
                pstRoomClass.update();
                return entRoomClass.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoomClass(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((RoomClass) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstRoomClass pstRoomClass = new PstRoomClass(oid);
            pstRoomClass.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoomClass(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(RoomClass entRoomClass) throws DBException {
        try {
            PstRoomClass pstRoomClass = new PstRoomClass(0);
            pstRoomClass.setString(FLD_CLASS_NAME, entRoomClass.getClassName());
            pstRoomClass.setString(FLD_CLASS_DESC, entRoomClass.getClassDesc());
            pstRoomClass.insert();
            entRoomClass.setOID(pstRoomClass.getlong(FLD_POS_ROOM_CLASS_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoomClass(0), DBException.UNKNOWN);
        }
        return entRoomClass.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((RoomClass) entity);
    }

    public static void resultToObject(ResultSet rs, RoomClass entRoomClass) {
        try {
            entRoomClass.setOID(rs.getLong(PstRoomClass.fieldNames[PstRoomClass.FLD_POS_ROOM_CLASS_ID]));
            entRoomClass.setClassName(rs.getString(PstRoomClass.fieldNames[PstRoomClass.FLD_CLASS_NAME]));
            entRoomClass.setClassDesc(rs.getString(PstRoomClass.fieldNames[PstRoomClass.FLD_CLASS_DESC]));
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
            String sql = "SELECT * FROM " + TBL_ROOM_CLASS;
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
                RoomClass entRoomClass = new RoomClass();
                resultToObject(rs, entRoomClass);
                lists.add(entRoomClass);
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

    public static boolean checkOID(long entRoomClassId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_ROOM_CLASS + " WHERE "
                    + PstRoomClass.fieldNames[PstRoomClass.FLD_POS_ROOM_CLASS_ID] + " = " + entRoomClassId;
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
            String sql = "SELECT COUNT(" + PstRoomClass.fieldNames[PstRoomClass.FLD_POS_ROOM_CLASS_ID] + ") FROM " + TBL_ROOM_CLASS;
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
                    RoomClass entRoomClass = (RoomClass) list.get(ls);
                    if (oid == entRoomClass.getOID()) {
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
}
