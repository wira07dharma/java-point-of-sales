/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.hanoman.entity.masterdata.PstMasterGroup;
import com.dimata.hanoman.entity.masterdata.PstMasterType;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author dimata005
 */
public class PstMaterialMappingType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MATERIAL_TYPE_MAPPING = "pos_material_type_mapping";
    public static final int FLD_MATERIAL_TYPE_MAPPING_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_TYPE_ID = 2;
    public static final int FLD_NOTE = 3;

    public static String[] fieldNames = {
        "MATERIAL_TYPE_MAPPING_ID",
        "MATERIAL_ID",
        "TYPE_ID",
        "NOTE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING
    };

    public PstMaterialMappingType() {
    }

    public PstMaterialMappingType(int i) throws DBException {
        super(new PstMaterialMappingType());
    }

    public PstMaterialMappingType(String sOid) throws DBException {
        super(new PstMaterialMappingType(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMaterialMappingType(long lOid) throws DBException {
        super(new PstMaterialMappingType(0));
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
        return TBL_MATERIAL_TYPE_MAPPING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialMappingType().getClass().getName();
    }

    public static MaterialTypeMapping fetchExc(long oid) throws DBException {
        try {
            MaterialTypeMapping entMaterialTypeMapping = new MaterialTypeMapping();
            PstMaterialMappingType pstMaterialTypeMapping = new PstMaterialMappingType(oid);
            entMaterialTypeMapping.setOID(oid);
            entMaterialTypeMapping.setMaterialId(pstMaterialTypeMapping.getlong(FLD_MATERIAL_ID));
            entMaterialTypeMapping.setTypeId(pstMaterialTypeMapping.getlong(FLD_TYPE_ID));
            entMaterialTypeMapping.setNote(pstMaterialTypeMapping.getString(FLD_NOTE));
            return entMaterialTypeMapping;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialMappingType(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MaterialTypeMapping entMaterialTypeMapping = fetchExc(entity.getOID());
        entity = (Entity) entMaterialTypeMapping;
        return entMaterialTypeMapping.getOID();
    }

    public static synchronized long updateExc(MaterialTypeMapping entMaterialTypeMapping) throws DBException {
        try {
            if (entMaterialTypeMapping.getOID() != 0) {
                PstMaterialMappingType pstMaterialTypeMapping = new PstMaterialMappingType(entMaterialTypeMapping.getOID());
                pstMaterialTypeMapping.setLong(FLD_MATERIAL_ID, entMaterialTypeMapping.getMaterialId());
                pstMaterialTypeMapping.setLong(FLD_TYPE_ID, entMaterialTypeMapping.getTypeId());
                pstMaterialTypeMapping.setString(FLD_NOTE, entMaterialTypeMapping.getNote());
                pstMaterialTypeMapping.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMaterialTypeMapping.getUpdateSQL());
                return entMaterialTypeMapping.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialMappingType(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MaterialTypeMapping) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMaterialMappingType pstMaterialTypeMapping = new PstMaterialMappingType(oid);
            pstMaterialTypeMapping.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMaterialTypeMapping.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialMappingType(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MaterialTypeMapping entMaterialTypeMapping) throws DBException {
        try {
            PstMaterialMappingType pstMaterialTypeMapping = new PstMaterialMappingType(0);
            pstMaterialTypeMapping.setLong(FLD_MATERIAL_ID, entMaterialTypeMapping.getMaterialId());
            pstMaterialTypeMapping.setLong(FLD_TYPE_ID, entMaterialTypeMapping.getTypeId());
            pstMaterialTypeMapping.setString(FLD_NOTE, entMaterialTypeMapping.getNote());
            pstMaterialTypeMapping.insert();
            //kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMaterialTypeMapping.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialMappingType(0), DBException.UNKNOWN);
        }
        return entMaterialTypeMapping.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MaterialTypeMapping) entity);
    }

    public static void resultToObject(ResultSet rs, MaterialTypeMapping entMaterialTypeMapping) {
        try {
            entMaterialTypeMapping.setOID(rs.getLong(PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_TYPE_MAPPING_ID]));
            entMaterialTypeMapping.setMaterialId(rs.getLong(PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID]));
            entMaterialTypeMapping.setTypeId(rs.getLong(PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_TYPE_ID]));
            entMaterialTypeMapping.setNote(rs.getString(PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_NOTE]));
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
            String sql = "SELECT * FROM " + TBL_MATERIAL_TYPE_MAPPING;
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
                MaterialTypeMapping entMaterialTypeMapping = new MaterialTypeMapping();
                resultToObject(rs, entMaterialTypeMapping);
                lists.add(entMaterialTypeMapping);
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

    public static boolean checkOID(long entMaterialTypeMappingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_TYPE_MAPPING + " WHERE "
                    + PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_TYPE_MAPPING_ID] + " = " + entMaterialTypeMappingId;
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
            String sql = "SELECT COUNT(" + PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_TYPE_MAPPING_ID] + ") FROM " + TBL_MATERIAL_TYPE_MAPPING;
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
                    MaterialTypeMapping entMaterialTypeMapping = (MaterialTypeMapping) list.get(ls);
                    if (oid == entMaterialTypeMapping.getOID()) {
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
    
    public static long getSelectedTypeId(int group, long materialId){
        
        DBResultSet dbrs = null;
        long result = 0;
        try {
            String sql = "SELECT map."+fieldNames[FLD_TYPE_ID]+" FROM " + TBL_MATERIAL_TYPE_MAPPING + " AS map"
                    + " INNER JOIN " + PstMasterType.TBL_MASTER_TYPE + " AS ty "
                    + " ON map."+fieldNames[FLD_TYPE_ID] + " = ty."+PstMasterType.fieldNames[PstMasterType.FLD_MASTER_TYPE_ID]
                    + " INNER JOIN " + PstMasterGroup.TBL_MASTER_GROUP + " AS gr "
                    + " ON ty."+PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+ " = gr."+PstMasterGroup.fieldNames[PstMasterGroup.FLD_TYPE_GROUP]
                    + " WHERE gr."+PstMasterGroup.fieldNames[PstMasterGroup.FLD_TYPE_GROUP]+ " = " + group 
                    + " AND map."+fieldNames[FLD_MATERIAL_ID]+" = " + materialId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(fieldNames[FLD_TYPE_ID]);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MaterialTypeMapping materialTypeMapping = PstMaterialMappingType.fetchExc(oid);
                object.put(PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_TYPE_MAPPING_ID], materialTypeMapping.getOID());
                object.put(PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID], materialTypeMapping.getMaterialId());
                object.put(PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_NOTE], materialTypeMapping.getNote());
                object.put(PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_TYPE_ID], materialTypeMapping.getTypeId());
            }catch(Exception exc){}

            return object;
        }
}
