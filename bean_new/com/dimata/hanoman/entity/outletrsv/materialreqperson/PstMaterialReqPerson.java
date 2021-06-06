/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.materialreqperson;

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
public class PstMaterialReqPerson extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MATERIAL_REQ_PERSON = "pos_material_req_person";
    public static final int FLD_MATERIAL_REQ_PERSON_ID = 0;
    public static final int FLD_MATERIAL_REQ_LOCATION_ID = 1;
    public static final int FLD_NUMBER_OF_PERSON = 2;
    public static final int FLD_JOBDESC = 3;
    public static final int FLD_JOB_WEIGHT = 4;

    public static String[] fieldNames = {
        "MATERIAL_REQ_PERSON_ID",
        "MATERIAL_REQ_LOCATION_ID",
        "NUMBER_OF_PERSON",
        "JOBDESC",
        "JOB_WEIGHT"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_FLOAT
    };

    public PstMaterialReqPerson() {
    }

    public PstMaterialReqPerson(int i) throws DBException {
        super(new PstMaterialReqPerson());
    }

    public PstMaterialReqPerson(String sOid) throws DBException {
        super(new PstMaterialReqPerson(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMaterialReqPerson(long lOid) throws DBException {
        super(new PstMaterialReqPerson(0));
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
        return TBL_MATERIAL_REQ_PERSON;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialReqPerson().getClass().getName();
    }

    public static MaterialReqPerson fetchExc(long oid) throws DBException {
        try {
            MaterialReqPerson entMaterialReqPerson = new MaterialReqPerson();
            PstMaterialReqPerson pstMaterialReqPerson = new PstMaterialReqPerson(oid);
            entMaterialReqPerson.setOID(oid);
            entMaterialReqPerson.setMaterialReqLocationId(pstMaterialReqPerson.getlong(FLD_MATERIAL_REQ_LOCATION_ID));
            entMaterialReqPerson.setNumberOfPerson(pstMaterialReqPerson.getInt(FLD_NUMBER_OF_PERSON));
            entMaterialReqPerson.setJobdesc(pstMaterialReqPerson.getString(FLD_JOBDESC));
            entMaterialReqPerson.setJobWeight(pstMaterialReqPerson.getfloat(FLD_JOB_WEIGHT));
            return entMaterialReqPerson;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqPerson(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MaterialReqPerson entMaterialReqPerson = fetchExc(entity.getOID());
        entity = (Entity) entMaterialReqPerson;
        return entMaterialReqPerson.getOID();
    }

    public static synchronized long updateExc(MaterialReqPerson entMaterialReqPerson) throws DBException {
        try {
            if (entMaterialReqPerson.getOID() != 0) {
                PstMaterialReqPerson pstMaterialReqPerson = new PstMaterialReqPerson(entMaterialReqPerson.getOID());
                pstMaterialReqPerson.setLong(FLD_MATERIAL_REQ_LOCATION_ID, entMaterialReqPerson.getMaterialReqLocationId());
                pstMaterialReqPerson.setInt(FLD_NUMBER_OF_PERSON, entMaterialReqPerson.getNumberOfPerson());
                pstMaterialReqPerson.setString(FLD_JOBDESC, entMaterialReqPerson.getJobdesc());
                pstMaterialReqPerson.setDouble(FLD_JOB_WEIGHT, entMaterialReqPerson.getJobWeight());
                pstMaterialReqPerson.update();
                return entMaterialReqPerson.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqPerson(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MaterialReqPerson) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMaterialReqPerson pstMaterialReqPerson = new PstMaterialReqPerson(oid);
            pstMaterialReqPerson.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqPerson(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MaterialReqPerson entMaterialReqPerson) throws DBException {
        try {
            PstMaterialReqPerson pstMaterialReqPerson = new PstMaterialReqPerson(0);
            pstMaterialReqPerson.setLong(FLD_MATERIAL_REQ_LOCATION_ID, entMaterialReqPerson.getMaterialReqLocationId());
            pstMaterialReqPerson.setInt(FLD_NUMBER_OF_PERSON, entMaterialReqPerson.getNumberOfPerson());
            pstMaterialReqPerson.setString(FLD_JOBDESC, entMaterialReqPerson.getJobdesc());
            pstMaterialReqPerson.setDouble(FLD_JOB_WEIGHT, entMaterialReqPerson.getJobWeight());
            pstMaterialReqPerson.insert();
            entMaterialReqPerson.setOID(pstMaterialReqPerson.getlong(FLD_MATERIAL_REQ_PERSON_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqPerson(0), DBException.UNKNOWN);
        }
        return entMaterialReqPerson.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MaterialReqPerson) entity);
    }

    public static void resultToObject(ResultSet rs, MaterialReqPerson entMaterialReqPerson) {
        try {
            entMaterialReqPerson.setOID(rs.getLong(PstMaterialReqPerson.fieldNames[PstMaterialReqPerson.FLD_MATERIAL_REQ_PERSON_ID]));
            entMaterialReqPerson.setMaterialReqLocationId(rs.getLong(PstMaterialReqPerson.fieldNames[PstMaterialReqPerson.FLD_MATERIAL_REQ_LOCATION_ID]));
            entMaterialReqPerson.setNumberOfPerson(rs.getInt(PstMaterialReqPerson.fieldNames[PstMaterialReqPerson.FLD_NUMBER_OF_PERSON]));
            entMaterialReqPerson.setJobdesc(rs.getString(PstMaterialReqPerson.fieldNames[PstMaterialReqPerson.FLD_JOBDESC]));
            entMaterialReqPerson.setJobWeight(rs.getFloat(PstMaterialReqPerson.fieldNames[PstMaterialReqPerson.FLD_JOB_WEIGHT]));
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
            String sql = "SELECT * FROM " + TBL_MATERIAL_REQ_PERSON;
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
                MaterialReqPerson entMaterialReqPerson = new MaterialReqPerson();
                resultToObject(rs, entMaterialReqPerson);
                lists.add(entMaterialReqPerson);
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

    public static boolean checkOID(long entMaterialReqPersonId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_REQ_PERSON + " WHERE "
                    + PstMaterialReqPerson.fieldNames[PstMaterialReqPerson.FLD_MATERIAL_REQ_PERSON_ID] + " = " + entMaterialReqPersonId;
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
            String sql = "SELECT COUNT(" + PstMaterialReqPerson.fieldNames[PstMaterialReqPerson.FLD_MATERIAL_REQ_PERSON_ID] + ") FROM " + TBL_MATERIAL_REQ_PERSON;
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
                    MaterialReqPerson entMaterialReqPerson = (MaterialReqPerson) list.get(ls);
                    if (oid == entMaterialReqPerson.getOID()) {
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
