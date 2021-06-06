/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.rsvcustpackschedule;

import com.dimata.posbo.entity.masterdata.PstRoom;
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
public class PstResvCustomPackSchedule extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_RESV_CUSTOM_PACK_SCHEDULE = "rsv_custome_package_schedule";
    public static final int FLD_CUSTOM_SCHEDULE_ID = 0;
    public static final int FLD_CUSTOME_PACK_BILLING_ID = 1;
    public static final int FLD_ROOM_ID = 2;
    public static final int FLD_TABLE_ID = 3;
    public static final int FLD_START_DATE = 4;
    public static final int FLD_END_DATE = 5;
    public static final int FLD_STATUS = 6;
    public static final int FLD_QUANTITY = 7;

    public static String[] fieldNames = {
        "CUSTOM_SCHEDULE_ID",
        "CUSTOME_PACK_BILLING_ID",
        "ROOM_ID",
        "TABLE_ID",
        "START_DATE",
        "END_DATE",
        "STATUS",
        "QUANTITY"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_FLOAT
    };

    public PstResvCustomPackSchedule() {
    }

    public PstResvCustomPackSchedule(int i) throws DBException {
        super(new PstResvCustomPackSchedule());
    }

    public PstResvCustomPackSchedule(String sOid) throws DBException {
        super(new PstResvCustomPackSchedule(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstResvCustomPackSchedule(long lOid) throws DBException {
        super(new PstResvCustomPackSchedule(0));
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
        return TBL_RESV_CUSTOM_PACK_SCHEDULE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstResvCustomPackSchedule().getClass().getName();
    }

    public static ResvCustomPackSchedule fetchExc(long oid) throws DBException {
        try {
            ResvCustomPackSchedule entResvCustomPackSchedule = new ResvCustomPackSchedule();
            PstResvCustomPackSchedule pstResvCustomPackSchedule = new PstResvCustomPackSchedule(oid);
            entResvCustomPackSchedule.setOID(oid);
            entResvCustomPackSchedule.setCustomePackBillingId(pstResvCustomPackSchedule.getlong(FLD_CUSTOME_PACK_BILLING_ID));
            entResvCustomPackSchedule.setRoomId(pstResvCustomPackSchedule.getlong(FLD_ROOM_ID));
            entResvCustomPackSchedule.setTableId(pstResvCustomPackSchedule.getlong(FLD_TABLE_ID));
            entResvCustomPackSchedule.setStartDate(pstResvCustomPackSchedule.getDate(FLD_START_DATE));
            entResvCustomPackSchedule.setEndDate(pstResvCustomPackSchedule.getDate(FLD_END_DATE));
            entResvCustomPackSchedule.setStatus(pstResvCustomPackSchedule.getInt(FLD_STATUS));
            entResvCustomPackSchedule.setQuantity(pstResvCustomPackSchedule.getdouble(FLD_QUANTITY));
            return entResvCustomPackSchedule;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackSchedule(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ResvCustomPackSchedule entResvCustomPackSchedule = fetchExc(entity.getOID());
        entity = (Entity) entResvCustomPackSchedule;
        return entResvCustomPackSchedule.getOID();
    }

    public static synchronized long updateExc(ResvCustomPackSchedule entResvCustomPackSchedule) throws DBException {
        try {
            if (entResvCustomPackSchedule.getOID() != 0) {
                PstResvCustomPackSchedule pstResvCustomPackSchedule = new PstResvCustomPackSchedule(entResvCustomPackSchedule.getOID());
                pstResvCustomPackSchedule.setLong(FLD_CUSTOME_PACK_BILLING_ID, entResvCustomPackSchedule.getCustomePackBillingId());
                pstResvCustomPackSchedule.setLong(FLD_ROOM_ID, entResvCustomPackSchedule.getRoomId());
                pstResvCustomPackSchedule.setLong(FLD_TABLE_ID, entResvCustomPackSchedule.getTableId());
                pstResvCustomPackSchedule.setDate(FLD_START_DATE, entResvCustomPackSchedule.getStartDate());
                pstResvCustomPackSchedule.setDate(FLD_END_DATE, entResvCustomPackSchedule.getEndDate());
                pstResvCustomPackSchedule.setInt(FLD_STATUS, entResvCustomPackSchedule.getStatus());
                pstResvCustomPackSchedule.setDouble(FLD_QUANTITY, entResvCustomPackSchedule.getQuantity());
                pstResvCustomPackSchedule.update();
                return entResvCustomPackSchedule.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackSchedule(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ResvCustomPackSchedule) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstResvCustomPackSchedule pstResvCustomPackSchedule = new PstResvCustomPackSchedule(oid);
            pstResvCustomPackSchedule.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackSchedule(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ResvCustomPackSchedule entResvCustomPackSchedule) throws DBException {
        try {
            PstResvCustomPackSchedule pstResvCustomPackSchedule = new PstResvCustomPackSchedule(0);
            pstResvCustomPackSchedule.setLong(FLD_CUSTOME_PACK_BILLING_ID, entResvCustomPackSchedule.getCustomePackBillingId());
            pstResvCustomPackSchedule.setLong(FLD_ROOM_ID, entResvCustomPackSchedule.getRoomId());
            pstResvCustomPackSchedule.setLong(FLD_TABLE_ID, entResvCustomPackSchedule.getTableId());
            pstResvCustomPackSchedule.setDate(FLD_START_DATE, entResvCustomPackSchedule.getStartDate());
            pstResvCustomPackSchedule.setDate(FLD_END_DATE, entResvCustomPackSchedule.getEndDate());
            pstResvCustomPackSchedule.setInt(FLD_STATUS, entResvCustomPackSchedule.getStatus());
            pstResvCustomPackSchedule.setDouble(FLD_QUANTITY, entResvCustomPackSchedule.getQuantity());
            pstResvCustomPackSchedule.insert();
            entResvCustomPackSchedule.setOID(pstResvCustomPackSchedule.getlong(FLD_CUSTOM_SCHEDULE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackSchedule(0), DBException.UNKNOWN);
        }
        return entResvCustomPackSchedule.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ResvCustomPackSchedule) entity);
    }

    public static void resultToObject(ResultSet rs, ResvCustomPackSchedule entResvCustomPackSchedule) {
        try {
            entResvCustomPackSchedule.setOID(rs.getLong(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_CUSTOM_SCHEDULE_ID]));
            entResvCustomPackSchedule.setCustomePackBillingId(rs.getLong(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_CUSTOME_PACK_BILLING_ID]));
            entResvCustomPackSchedule.setRoomId(rs.getLong(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_ROOM_ID]));
            entResvCustomPackSchedule.setTableId(rs.getLong(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_TABLE_ID]));
            entResvCustomPackSchedule.setStartDate(rs.getTimestamp(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_START_DATE]));
            entResvCustomPackSchedule.setEndDate(rs.getTimestamp(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_END_DATE]));
            entResvCustomPackSchedule.setStatus(rs.getInt(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_STATUS]));
            entResvCustomPackSchedule.setQuantity(rs.getDouble(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_QUANTITY]));
            
            entResvCustomPackSchedule.setRoomName(rs.getString(PstRoom.fieldNames[PstRoom.FLD_NAME]));
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
            String sql = "SELECT * FROM " + TBL_RESV_CUSTOM_PACK_SCHEDULE;
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
                ResvCustomPackSchedule entResvCustomPackSchedule = new ResvCustomPackSchedule();
                resultToObject(rs, entResvCustomPackSchedule);
                lists.add(entResvCustomPackSchedule);
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

    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT packschedule.*, room.NAME"
                    + " FROM " + TBL_RESV_CUSTOM_PACK_SCHEDULE + " AS packschedule"
                    + " INNER JOIN pos_room AS room"
                    + " ON room.ROOM_ID = packschedule.ROOM_ID"
                    + "";
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
                ResvCustomPackSchedule entResvCustomPackSchedule = new ResvCustomPackSchedule();
                resultToObject(rs, entResvCustomPackSchedule);
                lists.add(entResvCustomPackSchedule);
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

    public static boolean checkOID(long entResvCustomPackScheduleId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_RESV_CUSTOM_PACK_SCHEDULE + " WHERE "
                    + PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_CUSTOM_SCHEDULE_ID] + " = " + entResvCustomPackScheduleId;
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
            String sql = "SELECT COUNT(" + PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_CUSTOM_SCHEDULE_ID] + ") FROM " + TBL_RESV_CUSTOM_PACK_SCHEDULE;
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
                    ResvCustomPackSchedule entResvCustomPackSchedule = (ResvCustomPackSchedule) list.get(ls);
                    if (oid == entResvCustomPackSchedule.getOID()) {
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
