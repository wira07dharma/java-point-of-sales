/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */
/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/
package com.dimata.posbo.entity.masterdata;

/* package java */
import com.dimata.common.entity.location.PstLocation;
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
//import com.dimata.qdep.entity.*;

/* package  */
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import org.json.JSONObject;

public class PstMappingProductLocationStoreRequest extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_MAT_KSG = "POS_KSG";
    public static final String TBL_MAPPING_PRODUCT = "mapping_product_location_store_request";
    public static final int FLD_POS_LOCATION_MAPPING_STORE_REQUEST_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_LOCATION_ID = 2;
    public static final String[] fieldNames = {
        "POS_LOCATION_MAPPING_STORE_REQUEST_ID",
        "MATERIAL_ID",
        "LOCATION_ID"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG, 
        TYPE_LONG
    };

    public PstMappingProductLocationStoreRequest() {
    }

    public PstMappingProductLocationStoreRequest(int i) throws DBException {
        super(new PstMappingProductLocationStoreRequest());
    }

    public PstMappingProductLocationStoreRequest(String sOid) throws DBException {
        super(new PstMappingProductLocationStoreRequest(0));
        try {
            if (!locate(sOid)) {
                throw new DBException(this, DBException.RECORD_NOT_FOUND);
            } else {
                return;
            }
        } catch (Exception e) {
        }
    }

    public PstMappingProductLocationStoreRequest(long lOid) throws DBException {
        super(new PstMappingProductLocationStoreRequest(0));
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
        return TBL_MAPPING_PRODUCT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstKsg().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MappingProductLocationStoreRequest mappingProductLocationStoreRequest = fetchExc(ent.getOID());
        ent = (Entity) mappingProductLocationStoreRequest;
        return mappingProductLocationStoreRequest.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Ksg) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Ksg) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MappingProductLocationStoreRequest fetchExc(long oid) throws DBException {
        try {
            MappingProductLocationStoreRequest mappingProductLocationStoreRequest = new MappingProductLocationStoreRequest();
            PstMappingProductLocationStoreRequest pstMappingProductLocationStoreRequest = new PstMappingProductLocationStoreRequest(oid);
            mappingProductLocationStoreRequest.setOID(oid);
            mappingProductLocationStoreRequest.setMaterialId(pstMappingProductLocationStoreRequest.getlong(FLD_MATERIAL_ID));
            mappingProductLocationStoreRequest.setLocationId(pstMappingProductLocationStoreRequest.getlong(FLD_LOCATION_ID));

            return mappingProductLocationStoreRequest;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKsg(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MappingProductLocationStoreRequest mappingProductLocationStoreRequest) throws DBException {
        try {
            PstMappingProductLocationStoreRequest pstMappingProductLocationStoreRequest = new PstMappingProductLocationStoreRequest(0);
            pstMappingProductLocationStoreRequest.setLong(FLD_MATERIAL_ID, mappingProductLocationStoreRequest.getMaterialId());
            pstMappingProductLocationStoreRequest.setLong(FLD_LOCATION_ID, mappingProductLocationStoreRequest.getLocationId());

            pstMappingProductLocationStoreRequest.insert();
            mappingProductLocationStoreRequest.setOID(pstMappingProductLocationStoreRequest.getlong(FLD_POS_LOCATION_MAPPING_STORE_REQUEST_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKsg(0), DBException.UNKNOWN);
        }
        return mappingProductLocationStoreRequest.getOID();
    }

    public static long updateExc(MappingProductLocationStoreRequest mappingProductLocationStoreRequest) throws DBException {
        try {
            if (mappingProductLocationStoreRequest.getOID() != 0) {
                PstMappingProductLocationStoreRequest pstMappingProductLocationStoreRequest = new PstMappingProductLocationStoreRequest(mappingProductLocationStoreRequest.getOID());

                pstMappingProductLocationStoreRequest.setLong(FLD_MATERIAL_ID, mappingProductLocationStoreRequest.getMaterialId());
                pstMappingProductLocationStoreRequest.setLong(FLD_LOCATION_ID, mappingProductLocationStoreRequest.getLocationId());

                pstMappingProductLocationStoreRequest.update();
                return mappingProductLocationStoreRequest.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKsg(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMappingProductLocationStoreRequest pstMappingProductLocationStoreRequest = new PstMappingProductLocationStoreRequest(oid);
            pstMappingProductLocationStoreRequest.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKsg(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAPPING_PRODUCT; 
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
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MappingProductLocationStoreRequest mappingProductLocationStoreRequest = new MappingProductLocationStoreRequest();
                resultToObject(rs, mappingProductLocationStoreRequest);
                lists.add(mappingProductLocationStoreRequest);
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
    
    public static Vector listJoinMaterial(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = ""
                + " SELECT mp.*, "
                + " pm."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+","
                + " un."+PstUnit.fieldNames[PstUnit.FLD_CODE]+""
                + " FROM "+TBL_MAPPING_PRODUCT+" mp "
                + " INNER JOIN "+PstMaterial.TBL_MATERIAL+" pm "
                + " ON pm."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" = mp."+fieldNames[FLD_MATERIAL_ID]+""
                + " INNER JOIN "+PstUnit.TBL_P2_UNIT+" un"
                + " ON pm."+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]+"= un."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]+""; 
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
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MappingProductLocationStoreRequest mappingProductLocationStoreRequest = new MappingProductLocationStoreRequest();
                resultToObjectJoinMaterial(rs, mappingProductLocationStoreRequest);
                lists.add(mappingProductLocationStoreRequest);
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



    public static void resultToObject(ResultSet rs, MappingProductLocationStoreRequest mappingProductLocationStoreRequest) {
        try {
            mappingProductLocationStoreRequest.setOID(rs.getLong(fieldNames[FLD_POS_LOCATION_MAPPING_STORE_REQUEST_ID]));
            mappingProductLocationStoreRequest.setMaterialId(rs.getLong(fieldNames[FLD_MATERIAL_ID]));
            mappingProductLocationStoreRequest.setLocationId(rs.getLong(fieldNames[FLD_LOCATION_ID]));

        } catch (Exception e) {
        }
    }
    
    public static void resultToObjectJoinMaterial(ResultSet rs, MappingProductLocationStoreRequest mappingProductLocationStoreRequest) {
        try {
            mappingProductLocationStoreRequest.setOID(rs.getLong(fieldNames[FLD_POS_LOCATION_MAPPING_STORE_REQUEST_ID]));
            mappingProductLocationStoreRequest.setMaterialId(rs.getLong(fieldNames[FLD_MATERIAL_ID]));
            mappingProductLocationStoreRequest.setLocationId(rs.getLong(fieldNames[FLD_LOCATION_ID]));
            mappingProductLocationStoreRequest.setItemName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
            mappingProductLocationStoreRequest.setUnitCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long oid) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAPPING_PRODUCT + " WHERE " +
                   fieldNames[FLD_POS_LOCATION_MAPPING_STORE_REQUEST_ID] + " = " + oid;

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
            String sql = "SELECT COUNT(" + fieldNames[FLD_POS_LOCATION_MAPPING_STORE_REQUEST_ID] + ") FROM " + TBL_MAPPING_PRODUCT;
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
    
    public static int deleteMappingByCompany(long oidCompany) {
            int ud=0;
            
            String sql = ""
                + " DELETE mp FROM "+TBL_MAPPING_PRODUCT+" mp "
                + " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" lc "
                + " ON mp."+fieldNames[FLD_LOCATION_ID]+" = lc."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" "
                + " WHERE lc."+PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID]+" = '"+oidCompany+"'";

            try {
                DBHandler.execUpdate(sql); 
            } catch (Exception e) {
                ud=-1;
            }
            
        return ud;
    }


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    Ksg matKsg = (Ksg) list.get(ls);
                    if (oid == matKsg.getOID()) {
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

    /* This method used to find command where current data */
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
                //System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                    //System.out.println("prev.......................");
                    }
                }
            }
        }

        return cmd;
    }
//    public static JSONObject fetchJSON(long oid){
//            JSONObject object = new JSONObject();
//            try {
//                MappingProductLocationStoreRequest mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oid);
//                object.put(PstMappingProductLocationStoreRequest.fieldNames[PstMappingProductLocationStoreRequest.FLD_POS_LOCATION_MAPPING_STORE_REQUEST_ID], mappingProductLocationStoreRequest.getOID());
//                object.put(PstMappingProductLocationStoreRequest.fieldNames[PstMappingProductLocationStoreRequest.FLD_LOCATION_ID], mappingProductLocationStoreRequest.getLocationId());
//                object.put(PstMappingProductLocationStoreRequest.fieldNames[PstMappingProductLocationStoreRequest.FLD_MATERIAL_ID], mappingProductLocationStoreRequest.getMaterialId());
//            }catch(Exception exc){} 
//
//            return object;
//        }
    
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MappingProductLocationStoreRequest mappingProductLocationStoreRequest = PstMappingProductLocationStoreRequest.fetchExc(oid);
         object.put(PstMappingProductLocationStoreRequest.fieldNames[PstMappingProductLocationStoreRequest.FLD_POS_LOCATION_MAPPING_STORE_REQUEST_ID], mappingProductLocationStoreRequest.getOID());
         object.put(PstMappingProductLocationStoreRequest.fieldNames[PstMappingProductLocationStoreRequest.FLD_LOCATION_ID], mappingProductLocationStoreRequest.getLocationId());
         object.put(PstMappingProductLocationStoreRequest.fieldNames[PstMappingProductLocationStoreRequest.FLD_MATERIAL_ID], mappingProductLocationStoreRequest.getMaterialId());
      }catch(Exception exc){}
      return object;
   }
}
