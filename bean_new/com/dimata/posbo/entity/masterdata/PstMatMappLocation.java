package com.dimata.posbo.entity.masterdata;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.posbo.db.*;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;

import java.util.Vector;
import java.sql.ResultSet;
import org.json.JSONObject;

public class PstMatMappLocation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final String TBL_POS_MAT_LOCATION = "POS_MAT_LOCATION";

    public static final String TBL_POS_MAT_LOCATION = "product_location";

    //public static final int FLD_MAT_LOC_ID = 0;
    public static final int FLD_MATERIAL_ID = 0;
    public static final int FLD_LOCATION_ID = 1;

    public static final String[] fieldNames =
            {
                    "PRODUCT_ID",
                    "LOCATION_ID"
            };

    public static final int[] fieldTypes =
            {
                    TYPE_PK + TYPE_FK + TYPE_LONG,
                    TYPE_PK + TYPE_FK + TYPE_LONG
            };

    public PstMatMappLocation() {
    }

    public PstMatMappLocation(int i) throws DBException {
        super(new PstMatMappLocation());
    }

    public PstMatMappLocation(String sOid) throws DBException {
        super(new PstMatMappLocation(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMatMappLocation(long locationOid, long productOid) throws DBException {
        super(new PstMatMappLocation(0));
        String sOid = "0";
        /*try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }*/
        if (!locate(locationOid, productOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_POS_MAT_LOCATION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatMappLocation().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatMappLocation matMappLocation = fetchExc(ent.getOID(0), ent.getOID(1)); // fetchExc(ent.getOID());
        ent = (Entity) matMappLocation;
        return matMappLocation.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((MatMappLocation) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((MatMappLocation) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent);
    }

    public static MatMappLocation fetchExc(long locationOid, long productOid) throws DBException {
        try {
            MatMappLocation matMappLocation = new MatMappLocation();
            PstMatMappLocation pstMatMappLocation = new PstMatMappLocation(locationOid, productOid);
            matMappLocation.setMaterialId(pstMatMappLocation.getlong(FLD_MATERIAL_ID));
            matMappLocation.setLocationId(pstMatMappLocation.getlong(FLD_LOCATION_ID));

            return matMappLocation;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatMappLocation(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatMappLocation matMappLocation) throws DBException {
        try {
            System.out.println("sdfasdfdsfad asdasdasdasd ");

            PstMatMappLocation pstMatMappLocation = new PstMatMappLocation(0);

            System.out.println("sdfasdfdsfad ");

            pstMatMappLocation.setLong(FLD_MATERIAL_ID, matMappLocation.getMaterialId());
            pstMatMappLocation.setLong(FLD_LOCATION_ID, matMappLocation.getLocationId());

            pstMatMappLocation.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatMappLocation.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatMappLocation(0), DBException.UNKNOWN);
        }
        return matMappLocation.getOID();
    }

    public static long updateExc(MatMappLocation matMappLocation) throws DBException {
        try {
            if (matMappLocation.getOID() != 0) {
                PstMatMappLocation pstMatMappLocation = new PstMatMappLocation(matMappLocation.getLocationId(), matMappLocation.getMaterialId());
                //pstMatMappLocation.setLong(FLD_MATERIAL_ID, matMappLocation.getMaterialId());
                // pstMatMappLocation.setLong(FLD_LOCATION_ID, matMappLocation.getLocationId());
                pstMatMappLocation.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatMappLocation.getUpdateSQL());
                return matMappLocation.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatMappLocation(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long locationOid, long productOid) throws DBException {
        try {
            PstMatMappLocation pstMatMappLocation = new PstMatMappLocation(locationOid, productOid);
            pstMatMappLocation.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatMappLocation.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatMappLocation(0), DBException.UNKNOWN);
        }
        return locationOid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MAT_LOCATION;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

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
                MatMappLocation matMappLocation = new MatMappLocation();
                resultToObject(rs, matMappLocation);
                lists.add(matMappLocation);
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

    public static void resultToObject(ResultSet rs, MatMappLocation matMappLocation) {
        try {
            matMappLocation.setMaterialId(rs.getLong(PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID]));
            matMappLocation.setLocationId(rs.getLong(PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_LOCATION_ID]));
        } catch (Exception e) {
            System.out.println("error : " + e.toString());
        }
    }

    public static boolean checkOID(long locationOid, long productOid) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MAT_LOCATION + " WHERE " +
                    PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_LOCATION_ID] + " = " + locationOid + " AND " +
                    PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID] + " = " + productOid;

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
            String sql = "SELECT COUNT(" + PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID] + ") FROM " + TBL_POS_MAT_LOCATION;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

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
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    MatMappLocation matMappLocation = (MatMappLocation) list.get(ls);
                    if (oid == matMappLocation.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0)
            cmd = Command.FIRST;
        else {
            if (start == (vectSize - recordToGet))
                cmd = Command.LAST;
            else {
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


    public static long deleteByLocationId(long locationId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_MAT_LOCATION +
                    " WHERE " + fieldNames[FLD_LOCATION_ID] +
                    " = " + locationId;

            int a = DBHandler.execUpdate(sql);
            return locationId;
        } catch (Exception exc) {
            System.out.println(" exception delete by Owner ID " + exc.toString());
        }
        return 0;
    }

    public static long deleteByItemId(long itemId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_MAT_LOCATION +
                    " WHERE " + fieldNames[FLD_MATERIAL_ID] +
                    " = " + itemId;

            int a = DBHandler.execUpdate(sql);
            return itemId;
        } catch (Exception exc) {
            System.out.println(" exception delete by Owner ID " + exc.toString());
        }
        return 0;
    }
    public static JSONObject fetchJSON(long locationOid, long productOid){
            JSONObject object = new JSONObject();
            try {
                MatMappLocation matMappLocation  = PstMatMappLocation.fetchExc(locationOid, productOid);
                object.put(PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_LOCATION_ID], matMappLocation.getLocationId());
                object.put(PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID], matMappLocation.getMaterialId());
            }catch(Exception exc){}

            return object;
        }
}
