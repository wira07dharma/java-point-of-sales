/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.materialreqfacility;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstMaterialReqFacility extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MATERIAL_REQ_FACILITY = "pos_material_req_facility";
    public static final int FLD_MATERIAL_REQ_FACILITY_ID = 0;
    public static final int FLD_MATERIAL_REQ_LOCATION_ID = 1;
    public static final int FLD_AKTIVA_ID = 2;
    public static final int FLD_MATERIAL_ID = 3;
    public static final int FLD_NUMBER = 4;
    public static final int FLD_NOTE = 5;
    public static final int FLD_ORDER_INDEX = 6;
    public static final int FLD_DURATION = 7;

    public static String[] fieldNames = {
        "MATERIAL_REQ_FACILITY_ID",
        "MATERIAL_REQ_LOCATION_ID",
        "AKTIVA_ID",
        "MATERIAL_ID",
        "NUMBER",
        "NOTE",
        "ORDER_INDEX",
        "DURATION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT
    };

    public PstMaterialReqFacility() {
    }

    public PstMaterialReqFacility(int i) throws DBException {
        super(new PstMaterialReqFacility());
    }

    public PstMaterialReqFacility(String sOid) throws DBException {
        super(new PstMaterialReqFacility(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMaterialReqFacility(long lOid) throws DBException {
        super(new PstMaterialReqFacility(0));
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
        return TBL_MATERIAL_REQ_FACILITY;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialReqFacility().getClass().getName();
    }

    public static MaterialReqFacility fetchExc(long oid) throws DBException {
        try {
            MaterialReqFacility entMaterialReqFacility = new MaterialReqFacility();
            PstMaterialReqFacility pstMaterialReqFacility = new PstMaterialReqFacility(oid);
            entMaterialReqFacility.setOID(oid);
            entMaterialReqFacility.setMaterialReqLocationId(pstMaterialReqFacility.getlong(FLD_MATERIAL_REQ_LOCATION_ID));
            entMaterialReqFacility.setAktivaId(pstMaterialReqFacility.getlong(FLD_AKTIVA_ID));
            entMaterialReqFacility.setMaterialId(pstMaterialReqFacility.getlong(FLD_MATERIAL_ID));
            entMaterialReqFacility.setNumber(pstMaterialReqFacility.getfloat(FLD_NUMBER));
            entMaterialReqFacility.setNote(pstMaterialReqFacility.getString(FLD_NOTE));
            entMaterialReqFacility.setOrderIndex(pstMaterialReqFacility.getInt(FLD_ORDER_INDEX));
            entMaterialReqFacility.setDuration(pstMaterialReqFacility.getfloat(FLD_DURATION));
            return entMaterialReqFacility;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqFacility(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MaterialReqFacility entMaterialReqFacility = fetchExc(entity.getOID());
        entity = (Entity) entMaterialReqFacility;
        return entMaterialReqFacility.getOID();
    }

    public static synchronized long updateExc(MaterialReqFacility entMaterialReqFacility) throws DBException {
        try {
            if (entMaterialReqFacility.getOID() != 0) {
                PstMaterialReqFacility pstMaterialReqFacility = new PstMaterialReqFacility(entMaterialReqFacility.getOID());
                pstMaterialReqFacility.setLong(FLD_MATERIAL_REQ_LOCATION_ID, entMaterialReqFacility.getMaterialReqLocationId());
                pstMaterialReqFacility.setLong(FLD_AKTIVA_ID, entMaterialReqFacility.getAktivaId());
                pstMaterialReqFacility.setLong(FLD_MATERIAL_ID, entMaterialReqFacility.getMaterialId());
                pstMaterialReqFacility.setDouble(FLD_NUMBER, entMaterialReqFacility.getNumber());
                pstMaterialReqFacility.setString(FLD_NOTE, entMaterialReqFacility.getNote());
                pstMaterialReqFacility.setInt(FLD_ORDER_INDEX, entMaterialReqFacility.getOrderIndex());
                pstMaterialReqFacility.setDouble(FLD_DURATION, entMaterialReqFacility.getDuration());
                pstMaterialReqFacility.update();
                return entMaterialReqFacility.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqFacility(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MaterialReqFacility) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMaterialReqFacility pstMaterialReqFacility = new PstMaterialReqFacility(oid);
            pstMaterialReqFacility.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqFacility(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MaterialReqFacility entMaterialReqFacility) throws DBException {
        try {
            PstMaterialReqFacility pstMaterialReqFacility = new PstMaterialReqFacility(0);
            pstMaterialReqFacility.setLong(FLD_MATERIAL_REQ_LOCATION_ID, entMaterialReqFacility.getMaterialReqLocationId());
            pstMaterialReqFacility.setLong(FLD_AKTIVA_ID, entMaterialReqFacility.getAktivaId());
            pstMaterialReqFacility.setLong(FLD_MATERIAL_ID, entMaterialReqFacility.getMaterialId());
            pstMaterialReqFacility.setDouble(FLD_NUMBER, entMaterialReqFacility.getNumber());
            pstMaterialReqFacility.setString(FLD_NOTE, entMaterialReqFacility.getNote());
            pstMaterialReqFacility.setInt(FLD_ORDER_INDEX, entMaterialReqFacility.getOrderIndex());
            pstMaterialReqFacility.setDouble(FLD_DURATION, entMaterialReqFacility.getDuration());
            pstMaterialReqFacility.insert();
            entMaterialReqFacility.setOID(pstMaterialReqFacility.getlong(FLD_MATERIAL_REQ_FACILITY_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqFacility(0), DBException.UNKNOWN);
        }
        return entMaterialReqFacility.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MaterialReqFacility) entity);
    }

    public static void resultToObject(ResultSet rs, MaterialReqFacility entMaterialReqFacility) {
        try {
            entMaterialReqFacility.setOID(rs.getLong(PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_MATERIAL_REQ_FACILITY_ID]));
            entMaterialReqFacility.setMaterialReqLocationId(rs.getLong(PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_MATERIAL_REQ_LOCATION_ID]));
            entMaterialReqFacility.setAktivaId(rs.getLong(PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_AKTIVA_ID]));
            entMaterialReqFacility.setMaterialId(rs.getLong(PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_MATERIAL_ID]));
            entMaterialReqFacility.setNumber(rs.getFloat(PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_NUMBER]));
            entMaterialReqFacility.setNote(rs.getString(PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_NOTE]));
            entMaterialReqFacility.setOrderIndex(rs.getInt(PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_ORDER_INDEX]));
            entMaterialReqFacility.setDuration(rs.getFloat(PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_DURATION]));
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
            String sql = "SELECT * FROM " + TBL_MATERIAL_REQ_FACILITY;
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
                MaterialReqFacility entMaterialReqFacility = new MaterialReqFacility();
                resultToObject(rs, entMaterialReqFacility);
                lists.add(entMaterialReqFacility);
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

    public static boolean checkOID(long entMaterialReqFacilityId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_REQ_FACILITY + " WHERE "
                    + PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_MATERIAL_REQ_FACILITY_ID] + " = " + entMaterialReqFacilityId;
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
            String sql = "SELECT COUNT(" + PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_MATERIAL_REQ_FACILITY_ID] + ") FROM " + TBL_MATERIAL_REQ_FACILITY;
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
                    MaterialReqFacility entMaterialReqFacility = (MaterialReqFacility) list.get(ls);
                    if (oid == entMaterialReqFacility.getOID()) {
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
