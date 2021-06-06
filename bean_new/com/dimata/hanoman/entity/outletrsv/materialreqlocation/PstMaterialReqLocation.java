/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.materialreqlocation;

import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstMaterialReqLocation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MATERIAL_REQ_LOCATION = "pos_material_req_location";
    public static final int FLD_MATERIAL_REQ_LOCATION_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_POS_ROOM_CLASS_ID = 2;
    public static final int FLD_DURATION = 3;
    public static final int FLD_ORDER_INDEX = 4;
    public static final int FLD_IGNORE_PIC = 5;

    public static String[] fieldNames = {
        "MATERIAL_REQ_LOCATION_ID",
        "MATERIAL_ID",
        "POS_ROOM_CLASS_ID",
        "DURATION",
        "ORDER_INDEX",
        "IGNORE_PIC"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT
    };

    public PstMaterialReqLocation() {
    }

    public PstMaterialReqLocation(int i) throws DBException {
        super(new PstMaterialReqLocation());
    }

    public PstMaterialReqLocation(String sOid) throws DBException {
        super(new PstMaterialReqLocation(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMaterialReqLocation(long lOid) throws DBException {
        super(new PstMaterialReqLocation(0));
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
        return TBL_MATERIAL_REQ_LOCATION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialReqLocation().getClass().getName();
    }

    public static MaterialReqLocation fetchExc(long oid) throws DBException {
        try {
            MaterialReqLocation entMaterialReqLocation = new MaterialReqLocation();
            PstMaterialReqLocation pstMaterialReqLocation = new PstMaterialReqLocation(oid);
            entMaterialReqLocation.setOID(oid);
            entMaterialReqLocation.setMaterialId(pstMaterialReqLocation.getlong(FLD_MATERIAL_ID));
            entMaterialReqLocation.setPosRoomClassId(pstMaterialReqLocation.getlong(FLD_POS_ROOM_CLASS_ID));
            entMaterialReqLocation.setDuration(pstMaterialReqLocation.getfloat(FLD_DURATION));
            entMaterialReqLocation.setOrderIndex(pstMaterialReqLocation.getInt(FLD_ORDER_INDEX));
            entMaterialReqLocation.setIgnorePIC(pstMaterialReqLocation.getInt(FLD_IGNORE_PIC));
            return entMaterialReqLocation;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqLocation(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MaterialReqLocation entMaterialReqLocation = fetchExc(entity.getOID());
        entity = (Entity) entMaterialReqLocation;
        return entMaterialReqLocation.getOID();
    }

    public static synchronized long updateExc(MaterialReqLocation entMaterialReqLocation) throws DBException {
        try {
            if (entMaterialReqLocation.getOID() != 0) {
                PstMaterialReqLocation pstMaterialReqLocation = new PstMaterialReqLocation(entMaterialReqLocation.getOID());
                pstMaterialReqLocation.setLong(FLD_MATERIAL_ID, entMaterialReqLocation.getMaterialId());
                pstMaterialReqLocation.setLong(FLD_POS_ROOM_CLASS_ID, entMaterialReqLocation.getPosRoomClassId());
                pstMaterialReqLocation.setDouble(FLD_DURATION, entMaterialReqLocation.getDuration());
                pstMaterialReqLocation.setInt(FLD_ORDER_INDEX, entMaterialReqLocation.getOrderIndex());
                pstMaterialReqLocation.setInt(FLD_IGNORE_PIC, entMaterialReqLocation.getIgnorePIC());
                pstMaterialReqLocation.update();
                return entMaterialReqLocation.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqLocation(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MaterialReqLocation) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMaterialReqLocation pstMaterialReqLocation = new PstMaterialReqLocation(oid);
            pstMaterialReqLocation.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqLocation(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MaterialReqLocation entMaterialReqLocation) throws DBException {
        try {
            PstMaterialReqLocation pstMaterialReqLocation = new PstMaterialReqLocation(0);
            pstMaterialReqLocation.setLong(FLD_MATERIAL_ID, entMaterialReqLocation.getMaterialId());
            pstMaterialReqLocation.setLong(FLD_POS_ROOM_CLASS_ID, entMaterialReqLocation.getPosRoomClassId());
            pstMaterialReqLocation.setDouble(FLD_DURATION, entMaterialReqLocation.getDuration());
            pstMaterialReqLocation.setInt(FLD_ORDER_INDEX, entMaterialReqLocation.getOrderIndex());
            pstMaterialReqLocation.setInt(FLD_IGNORE_PIC, entMaterialReqLocation.getIgnorePIC());
            pstMaterialReqLocation.insert();
            entMaterialReqLocation.setOID(pstMaterialReqLocation.getlong(FLD_MATERIAL_REQ_LOCATION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqLocation(0), DBException.UNKNOWN);
        }
        return entMaterialReqLocation.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MaterialReqLocation) entity);
    }

    public static void resultToObject(ResultSet rs, MaterialReqLocation entMaterialReqLocation) {
        try {
            entMaterialReqLocation.setOID(rs.getLong(PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_MATERIAL_REQ_LOCATION_ID]));
            entMaterialReqLocation.setMaterialId(rs.getLong(PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_MATERIAL_ID]));
            entMaterialReqLocation.setPosRoomClassId(rs.getLong(PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_POS_ROOM_CLASS_ID]));
            entMaterialReqLocation.setDuration(rs.getFloat(PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_DURATION]));
            entMaterialReqLocation.setOrderIndex(rs.getInt(PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_ORDER_INDEX]));
            entMaterialReqLocation.setIgnorePIC(rs.getInt(PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_IGNORE_PIC]));
        } catch (Exception e) {
        }
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }
    
    public static Vector<MaterialReqLocationRoomClass> listRoomClass(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql ="";
            
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

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialReqLocationRoomClass entMaterialReqLocation = new MaterialReqLocationRoomClass();
                resultToObject(rs, entMaterialReqLocation);
                entMaterialReqLocation.setRoomClassName(rs.getString("CLASS_NAME"));
                lists.add(entMaterialReqLocation);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
        
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_REQ_LOCATION;
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
                MaterialReqLocation entMaterialReqLocation = new MaterialReqLocation();
                resultToObject(rs, entMaterialReqLocation);
                lists.add(entMaterialReqLocation);
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
                    MaterialReqLocation materialReqLocation = (MaterialReqLocation) list.get(ls);
                    if (oid == materialReqLocation.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    public static boolean checkOID(long entMaterialReqLocationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_REQ_LOCATION + " WHERE "
                    + PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_MATERIAL_REQ_LOCATION_ID] + " = " + entMaterialReqLocationId;
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
            String sql = "SELECT COUNT(" + PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_MATERIAL_REQ_LOCATION_ID] + ") FROM " + TBL_MATERIAL_REQ_LOCATION;
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
                    MaterialReqLocation entMaterialReqLocation = (MaterialReqLocation) list.get(ls);
                    if (oid == entMaterialReqLocation.getOID()) {
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
