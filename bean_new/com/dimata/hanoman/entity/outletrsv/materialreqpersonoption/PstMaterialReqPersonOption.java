/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.materialreqpersonoption;

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
public class PstMaterialReqPersonOption extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MATERIAL_REQ_PERSON_OPTION = "pos_material_req_person_option";
    public static final int FLD_MATERIAL_REQ_PERSON_OPTION_ID = 0;
    public static final int FLD_MATERIAL_REQ_PERSON_ID = 1;
    public static final int FLD_DEPARTMENT_ID = 2;
    public static final int FLD_SECTION_ID = 3;
    public static final int FLD_POSITION_ID = 4;
    public static final int FLD_COMPETENCY_ID = 5;
    public static final int FLD_LEVEL_ID = 6;
    public static final int FLD_PRIORITY_INDEX = 7;

    public static String[] fieldNames = {
        "MATERIAL_REQ_PERSON_OPTION_ID",
        "MATERIAL_REQ_PERSON_ID",
        "DEPARTMENT_ID",
        "SECTION_ID",
        "POSITION_ID",
        "COMPETENCY_ID",
        "LEVEL_ID",
        "PRIORITY_INDEX"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT
    };

    public PstMaterialReqPersonOption() {
    }

    public PstMaterialReqPersonOption(int i) throws DBException {
        super(new PstMaterialReqPersonOption());
    }

    public PstMaterialReqPersonOption(String sOid) throws DBException {
        super(new PstMaterialReqPersonOption(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMaterialReqPersonOption(long lOid) throws DBException {
        super(new PstMaterialReqPersonOption(0));
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
        return TBL_MATERIAL_REQ_PERSON_OPTION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialReqPersonOption().getClass().getName();
    }

    public static MaterialReqPersonOption fetchExc(long oid) throws DBException {
        try {
            MaterialReqPersonOption entMaterialReqPersonOption = new MaterialReqPersonOption();
            PstMaterialReqPersonOption pstMaterialReqPersonOption = new PstMaterialReqPersonOption(oid);
            entMaterialReqPersonOption.setOID(oid);
            entMaterialReqPersonOption.setMaterialReqPersonId(pstMaterialReqPersonOption.getlong(FLD_MATERIAL_REQ_PERSON_ID));
            entMaterialReqPersonOption.setDepartmentId(pstMaterialReqPersonOption.getlong(FLD_DEPARTMENT_ID));
            entMaterialReqPersonOption.setSectionId(pstMaterialReqPersonOption.getlong(FLD_SECTION_ID));
            entMaterialReqPersonOption.setPositionId(pstMaterialReqPersonOption.getlong(FLD_POSITION_ID));
            entMaterialReqPersonOption.setCompetencyId(pstMaterialReqPersonOption.getlong(FLD_COMPETENCY_ID));
            entMaterialReqPersonOption.setLevelId(pstMaterialReqPersonOption.getlong(FLD_LEVEL_ID));
            entMaterialReqPersonOption.setPriorityIndex(pstMaterialReqPersonOption.getInt(FLD_PRIORITY_INDEX));
            return entMaterialReqPersonOption;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqPersonOption(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MaterialReqPersonOption entMaterialReqPersonOption = fetchExc(entity.getOID());
        entity = (Entity) entMaterialReqPersonOption;
        return entMaterialReqPersonOption.getOID();
    }

    public static synchronized long updateExc(MaterialReqPersonOption entMaterialReqPersonOption) throws DBException {
        try {
            if (entMaterialReqPersonOption.getOID() != 0) {
                PstMaterialReqPersonOption pstMaterialReqPersonOption = new PstMaterialReqPersonOption(entMaterialReqPersonOption.getOID());
                pstMaterialReqPersonOption.setLong(FLD_MATERIAL_REQ_PERSON_ID, entMaterialReqPersonOption.getMaterialReqPersonId());
                pstMaterialReqPersonOption.setLong(FLD_DEPARTMENT_ID, entMaterialReqPersonOption.getDepartmentId());
                pstMaterialReqPersonOption.setLong(FLD_SECTION_ID, entMaterialReqPersonOption.getSectionId());
                pstMaterialReqPersonOption.setLong(FLD_POSITION_ID, entMaterialReqPersonOption.getPositionId());
                pstMaterialReqPersonOption.setLong(FLD_COMPETENCY_ID, entMaterialReqPersonOption.getCompetencyId());
                pstMaterialReqPersonOption.setLong(FLD_LEVEL_ID, entMaterialReqPersonOption.getLevelId());
                pstMaterialReqPersonOption.setInt(FLD_PRIORITY_INDEX, entMaterialReqPersonOption.getPriorityIndex());
                pstMaterialReqPersonOption.update();
                return entMaterialReqPersonOption.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqPersonOption(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MaterialReqPersonOption) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMaterialReqPersonOption pstMaterialReqPersonOption = new PstMaterialReqPersonOption(oid);
            pstMaterialReqPersonOption.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqPersonOption(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MaterialReqPersonOption entMaterialReqPersonOption) throws DBException {
        try {
            PstMaterialReqPersonOption pstMaterialReqPersonOption = new PstMaterialReqPersonOption(0);
            pstMaterialReqPersonOption.setLong(FLD_MATERIAL_REQ_PERSON_ID, entMaterialReqPersonOption.getMaterialReqPersonId());
            pstMaterialReqPersonOption.setLong(FLD_DEPARTMENT_ID, entMaterialReqPersonOption.getDepartmentId());
            pstMaterialReqPersonOption.setLong(FLD_SECTION_ID, entMaterialReqPersonOption.getSectionId());
            pstMaterialReqPersonOption.setLong(FLD_POSITION_ID, entMaterialReqPersonOption.getPositionId());
            pstMaterialReqPersonOption.setLong(FLD_COMPETENCY_ID, entMaterialReqPersonOption.getCompetencyId());
            pstMaterialReqPersonOption.setLong(FLD_LEVEL_ID, entMaterialReqPersonOption.getLevelId());
            pstMaterialReqPersonOption.setInt(FLD_PRIORITY_INDEX, entMaterialReqPersonOption.getPriorityIndex());
            pstMaterialReqPersonOption.insert();
            entMaterialReqPersonOption.setOID(pstMaterialReqPersonOption.getlong(FLD_MATERIAL_REQ_PERSON_OPTION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialReqPersonOption(0), DBException.UNKNOWN);
        }
        return entMaterialReqPersonOption.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MaterialReqPersonOption) entity);
    }

    public static void resultToObject(ResultSet rs, MaterialReqPersonOption entMaterialReqPersonOption) {
        try {
            entMaterialReqPersonOption.setOID(rs.getLong(PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_MATERIAL_REQ_PERSON_OPTION_ID]));
            entMaterialReqPersonOption.setMaterialReqPersonId(rs.getLong(PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_MATERIAL_REQ_PERSON_ID]));
            entMaterialReqPersonOption.setDepartmentId(rs.getLong(PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_DEPARTMENT_ID]));
            entMaterialReqPersonOption.setSectionId(rs.getLong(PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_SECTION_ID]));
            entMaterialReqPersonOption.setPositionId(rs.getLong(PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_POSITION_ID]));
            entMaterialReqPersonOption.setCompetencyId(rs.getLong(PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_COMPETENCY_ID]));
            entMaterialReqPersonOption.setLevelId(rs.getLong(PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_LEVEL_ID]));
            entMaterialReqPersonOption.setPriorityIndex(rs.getInt(PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_PRIORITY_INDEX]));
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
            String sql = "SELECT * FROM " + TBL_MATERIAL_REQ_PERSON_OPTION;
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
              MaterialReqPersonOption entMaterialReqPersonOption = new MaterialReqPersonOption();
              resultToObject(rs, entMaterialReqPersonOption);
              lists.add(entMaterialReqPersonOption);
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

    public static boolean checkOID(long entMaterialReqPersonOptionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_REQ_PERSON_OPTION + " WHERE "
                    + PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_MATERIAL_REQ_PERSON_OPTION_ID] + " = " + entMaterialReqPersonOptionId;
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
            String sql = "SELECT COUNT(" + PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_MATERIAL_REQ_PERSON_OPTION_ID] + ") FROM " + TBL_MATERIAL_REQ_PERSON_OPTION;
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
                    MaterialReqPersonOption entMaterialReqPersonOption = (MaterialReqPersonOption) list.get(ls);
                    if (oid == entMaterialReqPersonOption.getOID()) {
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
