/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author dimata005
 */
public class PstMatMappKsg extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final String TBL_POS_MAT_KSG = "POS_MAT_LOCATION";

    public static final String TBL_POS_MAT_KSG = "product_ksg";

    //public static final int FLD_MAT_LOC_ID = 0;
    public static final int FLD_MATERIAL_ID = 0;
    public static final int FLD_KSG_ID = 1;

    public static final String[] fieldNames =
            {
                    "PRODUCT_ID",
                    "KSG_ID"
            };

    public static final int[] fieldTypes =
            {
                    TYPE_PK + TYPE_FK + TYPE_LONG,
                    TYPE_PK + TYPE_FK + TYPE_LONG
            };

    public PstMatMappKsg() {
    }

    public PstMatMappKsg(int i) throws DBException {
        super(new PstMatMappKsg());
    }

    public PstMatMappKsg(String sOid) throws DBException {
        super(new PstMatMappKsg(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMatMappKsg(long ksgOid, long productOid) throws DBException {
        super(new PstMatMappKsg(0));
        String sOid = "0";
        /*try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }*/
        if (!locate(ksgOid, productOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_POS_MAT_KSG;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatMappKsg().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatMappKsg matMappKsg = fetchExc(ent.getOID(0), ent.getOID(1)); // fetchExc(ent.getOID());
        ent = (Entity) matMappKsg;
        return matMappKsg.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((MatMappKsg) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((MatMappKsg) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent);
    }

    public static MatMappKsg fetchExc(long ksgOid, long productOid) throws DBException {
        try {
            MatMappKsg matMappKsg = new MatMappKsg();
            PstMatMappKsg pstMatMappKsg = new PstMatMappKsg(ksgOid, productOid);
            matMappKsg.setMaterialId(pstMatMappKsg.getlong(FLD_MATERIAL_ID));
            matMappKsg.setKsgId(pstMatMappKsg.getlong(FLD_KSG_ID));

            return matMappKsg;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatMappKsg(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatMappKsg matMappKsg) throws DBException {
        try {
            System.out.println("sdfasdfdsfad asdasdasdasd ");

            PstMatMappKsg pstMatMappKsg = new PstMatMappKsg(0);

            System.out.println("sdfasdfdsfad ");

            pstMatMappKsg.setLong(FLD_MATERIAL_ID, matMappKsg.getMaterialId());
            pstMatMappKsg.setLong(FLD_KSG_ID, matMappKsg.getKsgId());

            pstMatMappKsg.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatMappKsg.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatMappKsg(0), DBException.UNKNOWN);
        }
        return matMappKsg.getOID();
    }

    public static long updateExc(MatMappKsg matMappKsg) throws DBException {
        try {
            if (matMappKsg.getOID() != 0) {
                PstMatMappKsg pstMatMappKsg = new PstMatMappKsg(matMappKsg.getKsgId(), matMappKsg.getMaterialId());
                //pstMatMappKsg.setLong(FLD_MATERIAL_ID, matMappKsg.getMaterialId());
                // pstMatMappKsg.setLong(FLD_KSG_ID, matMappKsg.getKsgId());
                pstMatMappKsg.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatMappKsg.getUpdateSQL());
                return matMappKsg.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatMappKsg(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long ksgOid, long productOid) throws DBException {
        try {
            PstMatMappKsg pstMatMappKsg = new PstMatMappKsg(ksgOid, productOid);
            pstMatMappKsg.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatMappKsg.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatMappKsg(0), DBException.UNKNOWN);
        }
        return ksgOid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MAT_KSG;
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
                MatMappKsg matMappKsg = new MatMappKsg();
                resultToObject(rs, matMappKsg);
                lists.add(matMappKsg);
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
    
    
    public static Vector listJoinKsg(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MAP."+fieldNames[FLD_MATERIAL_ID]+","
                    + " MAP."+fieldNames[FLD_KSG_ID]
                    + " FROM " + TBL_POS_MAT_KSG + " AS MAP "
                    + " INNER JOIN "+ PstKsg.TBL_MAT_KSG + " AS KSG "
                    + " ON MAP."+fieldNames[FLD_KSG_ID]+ " = KSG." + PstKsg.fieldNames[PstKsg.FLD_KSG_ID];
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
                MatMappKsg matMappKsg = new MatMappKsg();
                resultToObject(rs, matMappKsg);
                lists.add(matMappKsg);
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

    public static void resultToObject(ResultSet rs, MatMappKsg matMappKsg) {
        try {
            matMappKsg.setMaterialId(rs.getLong(PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_MATERIAL_ID]));
            matMappKsg.setKsgId(rs.getLong(PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_KSG_ID]));
        } catch (Exception e) {
            System.out.println("error : " + e.toString());
        }
    }

    public static boolean checkOID(long ksgOid, long productOid) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MAT_KSG + " WHERE " +
                    PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_KSG_ID] + " = " + ksgOid + " AND " +
                    PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_MATERIAL_ID] + " = " + productOid;

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
            String sql = "SELECT COUNT(" + PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_MATERIAL_ID] + ") FROM " + TBL_POS_MAT_KSG;
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
                    MatMappKsg matMappKsg = (MatMappKsg) list.get(ls);
                    if (oid == matMappKsg.getOID())
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


    public static long deleteByKsgId(long ksgId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_MAT_KSG +
                    " WHERE " + fieldNames[FLD_KSG_ID] +
                    " = " + ksgId;

            int a = DBHandler.execUpdate(sql);
            return ksgId;
        } catch (Exception exc) {
            System.out.println(" exception delete by Owner ID " + exc.toString());
        }
        return 0;
    }

    public static long deleteByItemId(long itemId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_MAT_KSG +
                    " WHERE " + fieldNames[FLD_MATERIAL_ID] +
                    " = " + itemId;

            int a = DBHandler.execUpdate(sql);
            return itemId;
        } catch (Exception exc) {
            System.out.println(" exception delete by Owner ID " + exc.toString());
        }
        return 0;
    }
    public static JSONObject fetchJSON(long ksgOid, long productOid){
            JSONObject object = new JSONObject();
            try {
                MatMappKsg matMappKsg = PstMatMappKsg.fetchExc(ksgOid, productOid);
                object.put(PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_KSG_ID], matMappKsg.getOID());
                object.put(PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_MATERIAL_ID], matMappKsg.getMaterialId());
            }catch(Exception exc){}

            return object;
        }
}
