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

public class PstKsg extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_MAT_KSG = "POS_KSG";
    public static final String TBL_MAT_KSG = "pos_ksg";
    public static final int FLD_KSG_ID = 0;
    public static final int FLD_NAME = 1;
    public static final int FLD_CODE = 2;
    public static final int FLD_LOCATION_ID = 3;
    
    public static final String[] fieldNames = {
        "KSG_ID",
        "NAME",
        "CODE",
        "LOCATION_ID"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING, 
        TYPE_STRING,
        TYPE_LONG
    };

    public PstKsg() {
    }

    public PstKsg(int i) throws DBException {
        super(new PstKsg());
    }

    public PstKsg(String sOid) throws DBException {
        super(new PstKsg(0));
        try {
            if (!locate(sOid)) {
                throw new DBException(this, DBException.RECORD_NOT_FOUND);
            } else {
                return;
            }
        } catch (Exception e) {
        }
    }

    public PstKsg(long lOid) throws DBException {
        super(new PstKsg(0));
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
        return TBL_MAT_KSG;
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
        Ksg matKsg = fetchExc(ent.getOID());
        ent = (Entity) matKsg;
        return matKsg.getOID();
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

    public static Ksg fetchExc(long oid) throws DBException {
        try {
            Ksg matKsg = new Ksg();
            PstKsg pstKsg = new PstKsg(oid);
            matKsg.setOID(oid);

            matKsg.setName(pstKsg.getString(FLD_NAME));
            matKsg.setCode(pstKsg.getString(FLD_CODE));
            matKsg.setLocationId(pstKsg.getlong(FLD_LOCATION_ID));

            return matKsg;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKsg(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Ksg matKsg) throws DBException {
        try {
            PstKsg pstKsg = new PstKsg(0);

            pstKsg.setString(FLD_NAME, matKsg.getName());
            pstKsg.setString(FLD_CODE, matKsg.getCode());
            pstKsg.setLong(FLD_LOCATION_ID, matKsg.getLocationId());

            pstKsg.insert();
            matKsg.setOID(pstKsg.getlong(FLD_KSG_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKsg(0), DBException.UNKNOWN);
        }
        return matKsg.getOID();
    }

    public static long updateExc(Ksg matKsg) throws DBException {
        try {
            if (matKsg.getOID() != 0) {
                PstKsg pstKsg = new PstKsg(matKsg.getOID());

                pstKsg.setString(FLD_NAME, matKsg.getName());
                pstKsg.setString(FLD_CODE, matKsg.getCode());
                pstKsg.setLong(FLD_LOCATION_ID, matKsg.getLocationId());

                pstKsg.update();
                return matKsg.getOID();

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
            PstKsg pstKsg = new PstKsg(oid);
            pstKsg.delete();
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
            String sql = "SELECT * FROM " + TBL_MAT_KSG; 
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
                Ksg matKsg = new Ksg();
                resultToObject(rs, matKsg);
                lists.add(matKsg);
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

    /**
     * adnyana
     * untuk mencari list merk
     * @return hashtable
     */
    public static Hashtable getListAccountKsg() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_KSG;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Ksg matKsg = new Ksg();
                resultToObject(rs, matKsg);
                lists.put(matKsg.getName().toUpperCase(), matKsg);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }

    public static void resultToObject(ResultSet rs, Ksg matKsg) {
        try {
            matKsg.setOID(rs.getLong(PstKsg.fieldNames[PstKsg.FLD_KSG_ID]));
            matKsg.setName(rs.getString(PstKsg.fieldNames[PstKsg.FLD_NAME]));
            matKsg.setCode(rs.getString(PstKsg.fieldNames[PstKsg.FLD_CODE]));
            matKsg.setLocationId(rs.getLong(PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long merkId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_KSG + " WHERE " +
                    PstKsg.fieldNames[PstKsg.FLD_KSG_ID] + " = " + merkId;

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
            String sql = "SELECT COUNT(" + PstKsg.fieldNames[PstKsg.FLD_KSG_ID] + ") FROM " + TBL_MAT_KSG;
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
    
    public static Vector listJoinLocation(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_KSG + " AS KSG "
                    + " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS LOC "
                    + " ON LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = "
                    + " KSG." + fieldNames[FLD_LOCATION_ID]
                    + "";
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
                Ksg matKsg = new Ksg();
                resultToObject(rs, matKsg);
                lists.add(matKsg);
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
    
    public static Ksg fetchByCodeAndLocation(String code, long locationId) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        Ksg ksg = new Ksg();
        try {
            String sql = "SELECT * FROM " + TBL_MAT_KSG +
                    " WHERE " + fieldNames[FLD_CODE] +
                    " = '" + code + "' AND "+fieldNames[FLD_LOCATION_ID]+
                    " = '" + locationId + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, ksg);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return ksg;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                Ksg ksg = PstKsg.fetchExc(oid);
                object.put(PstKsg.fieldNames[PstKsg.FLD_KSG_ID], ksg.getOID());
                object.put(PstKsg.fieldNames[PstKsg.FLD_CODE], ksg.getCode());
                object.put(PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID], ksg.getLocationId());
                object.put(PstKsg.fieldNames[PstKsg.FLD_NAME], ksg.getName());
            }catch(Exception exc){}

            return object;
        }
}
