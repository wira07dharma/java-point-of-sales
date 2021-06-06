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
 * @author dimata005
 */
public class PstMasterGroupMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MASTER_GROUP_MAPPING = "pos_master_group_mapping";
    public static final int FLD_MASTER_GROUP_MAPPING_ID = 0;
    public static final int FLD_GROUP_ID  = 1;
    public static final int FLD_MODUL = 2;

    public static String[] fieldNames = {
        "MASTER_GROUP_MAPPING_ID",
        "GROUP_ID",
        "MODUL"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_INT
    };
    
    
    
    public static final int MODUL_MATERIAL = 1;
    public static final int MODUL_RECEIVING = 0;
    
    public static final String[] modul = {"Receiving","Material"};

    public PstMasterGroupMapping() {
    }

    public PstMasterGroupMapping(int i) throws DBException {
        super(new PstMasterGroupMapping());
    }

    public PstMasterGroupMapping(String sOid) throws DBException {
        super(new PstMasterGroupMapping(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMasterGroupMapping(long lOid) throws DBException {
        super(new PstMasterGroupMapping(0));
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
        return TBL_MASTER_GROUP_MAPPING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMasterGroupMapping().getClass().getName();
    }

    public static MasterGroupMapping fetchExc(long oid) throws DBException {
        try {
            MasterGroupMapping entMasterGroupMapping = new MasterGroupMapping();
            PstMasterGroupMapping pstMasterTypeMapping = new PstMasterGroupMapping(oid);
            entMasterGroupMapping.setOID(oid);
            entMasterGroupMapping.setGroupId(pstMasterTypeMapping.getLong(FLD_GROUP_ID));
            entMasterGroupMapping.setModul(pstMasterTypeMapping.getInt(FLD_GROUP_ID));
            return entMasterGroupMapping;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterGroupMapping(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MasterGroupMapping entMasterGroupMapping = fetchExc(entity.getOID());
        entity = (Entity) entMasterGroupMapping;
        return entMasterGroupMapping.getOID();
    }

    public static synchronized long updateExc(MasterGroupMapping entMasterGroupMapping) throws DBException {
        try {
            if (entMasterGroupMapping.getOID() != 0) {
                PstMasterGroupMapping pstMasterTypeMapping = new PstMasterGroupMapping(entMasterGroupMapping.getOID());
                pstMasterTypeMapping.setLong(FLD_GROUP_ID, entMasterGroupMapping.getGroupId());
                pstMasterTypeMapping.setInt(FLD_MODUL, entMasterGroupMapping.getModul());
                pstMasterTypeMapping.update();
                return entMasterGroupMapping.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterGroupMapping(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MasterGroupMapping) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMasterGroupMapping pstMasterTypeMapping = new PstMasterGroupMapping(oid);
            pstMasterTypeMapping.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterGroupMapping(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MasterGroupMapping entMasterGroupMapping) throws DBException {
        try {
            PstMasterGroupMapping pstMasterTypeMapping = new PstMasterGroupMapping(0);
            pstMasterTypeMapping.setLong(FLD_GROUP_ID, entMasterGroupMapping.getGroupId());
            pstMasterTypeMapping.setInt(FLD_MODUL, entMasterGroupMapping.getModul());
            pstMasterTypeMapping.insert();
            entMasterGroupMapping.setOID(pstMasterTypeMapping.getlong(FLD_MASTER_GROUP_MAPPING_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterGroupMapping(0), DBException.UNKNOWN);
        }
        return entMasterGroupMapping.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MasterGroupMapping) entity);
    }

    public static void resultToObject(ResultSet rs, MasterGroupMapping entMasterGroupMapping) {
        try {
            entMasterGroupMapping.setOID(rs.getLong(PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MASTER_GROUP_MAPPING_ID]));
            entMasterGroupMapping.setGroupId(rs.getLong(PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_GROUP_ID]));
            entMasterGroupMapping.setModul(rs.getInt(PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL]));
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
            String sql = "SELECT * FROM " + TBL_MASTER_GROUP_MAPPING;
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
                MasterGroupMapping entMasterGroupMapping = new MasterGroupMapping();
                resultToObject(rs, entMasterGroupMapping);
                lists.add(entMasterGroupMapping);
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

    public static boolean checkOID(long entMasterGroupMappingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MASTER_GROUP_MAPPING + " WHERE "
                    + PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MASTER_GROUP_MAPPING_ID] + " = " + entMasterGroupMappingId;
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
            String sql = "SELECT COUNT(" + PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MASTER_GROUP_MAPPING_ID] + ") FROM " + TBL_MASTER_GROUP_MAPPING;
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
                    MasterGroupMapping entMasterGroupMapping = (MasterGroupMapping) list.get(ls);
                    if (oid == entMasterGroupMapping.getOID()) {
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
                MasterGroupMapping masterGroupMapping = PstMasterGroupMapping.fetchExc(oid);
                object.put(PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MASTER_GROUP_MAPPING_ID], masterGroupMapping.getOID());
                object.put(PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_GROUP_ID], masterGroupMapping.getGroupId());
                object.put(PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL], masterGroupMapping.getModul());
            }catch(Exception exc){}

            return object;
        }
}