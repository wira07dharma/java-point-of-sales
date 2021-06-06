/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import static java.awt.image.DataBuffer.TYPE_DOUBLE;
import java.sql.*;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author Dewa
 */
public class PstKadar extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_KADAR = "pos_kadar";
    public static final int FLD_KADAR_ID = 0;
    public static final int FLD_KODE_KADAR = 1;
    public static final int FLD_KADAR = 2;
    public static final int FLD_KARAT = 3;

    public static String[] fieldNames = {
        "KADAR_ID",
        "KODE_KADAR",
        "KADAR",
        "KARAT"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_DOUBLE,
        TYPE_INT,
    };

    public PstKadar() {
    }

    public PstKadar(int i) throws DBException {
        super(new PstKadar());
    }

    public PstKadar(String sOid) throws DBException {
        super(new PstKadar(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstKadar(long lOid) throws DBException {
        super(new PstKadar(0));
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
        return TBL_POS_KADAR;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstKadar().getClass().getName();
    }

    public static Kadar fetchExc(long oid) throws DBException {
        try {
            Kadar entKadar = new Kadar();
            PstKadar pstKadar = new PstKadar(oid);
            entKadar.setOID(oid);
            entKadar.setKodeKadar(pstKadar.getString(FLD_KODE_KADAR));
            entKadar.setKadar(pstKadar.getdouble(FLD_KADAR));
            entKadar.setKarat(pstKadar.getInt(FLD_KARAT));
            return entKadar;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKadar(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        Kadar entKadar = fetchExc(entity.getOID());
        entity = (Entity) entKadar;
        return entKadar.getOID();
    }

    public static synchronized long updateExc(Kadar entKadar) throws DBException {
        try {
            if (entKadar.getOID() != 0) {
                PstKadar pstKadar = new PstKadar(entKadar.getOID());
                pstKadar.setString(FLD_KODE_KADAR, entKadar.getKodeKadar());
                pstKadar.setDouble(FLD_KADAR, entKadar.getKadar());
                pstKadar.setInt(FLD_KARAT, entKadar.getKarat());
                pstKadar.update();
                return entKadar.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKadar(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((Kadar) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstKadar pstKadar = new PstKadar(oid);
            pstKadar.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKadar(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(Kadar entKadar) throws DBException {
        try {
            PstKadar pstKadar = new PstKadar(0);
            pstKadar.setString(FLD_KODE_KADAR, entKadar.getKodeKadar());
            pstKadar.setDouble(FLD_KADAR,entKadar.getKadar());
            pstKadar.setInt(FLD_KARAT, entKadar.getKarat());
            pstKadar.insert();
            entKadar.setOID(pstKadar.getlong(FLD_KADAR_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKadar(0), DBException.UNKNOWN);
        }
        return entKadar.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((Kadar) entity);
    }

    public static void resultToObject(ResultSet rs, Kadar entKadar) {
        try {
            entKadar.setOID(rs.getLong(PstKadar.fieldNames[PstKadar.FLD_KADAR_ID]));
            entKadar.setKodeKadar(rs.getString(PstKadar.fieldNames[PstKadar.FLD_KODE_KADAR]));
            entKadar.setKadar(rs.getDouble(PstKadar.fieldNames[PstKadar.FLD_KADAR]));
            entKadar.setKarat(rs.getInt(PstKadar.fieldNames[PstKadar.FLD_KARAT]));
        } catch (Exception e) {
        }
    }
    
    
    public static void resultToObjectTanpaOid(ResultSet rs, Kadar entKadar) {
        try {
          
            entKadar.setKadar(rs.getDouble(PstKadar.fieldNames[PstKadar.FLD_KADAR]));
            entKadar.setKarat(rs.getInt(PstKadar.fieldNames[PstKadar.FLD_KARAT]));
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
            String sql = "SELECT * FROM " + TBL_POS_KADAR;
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
                Kadar entKadar = new Kadar();
                resultToObject(rs, entKadar);
                lists.add(entKadar);
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
    
    
    public static Vector listTanpaOid(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT KADAR, KARAT  FROM " + TBL_POS_KADAR;
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
                Kadar entKadar = new Kadar();
                resultToObjectTanpaOid(rs, entKadar);
                lists.add(entKadar);
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

    public static boolean checkOID(long entKadarId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_KADAR + " WHERE "
                    + PstKadar.fieldNames[PstKadar.FLD_KADAR_ID] + " = " + entKadarId;
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
            String sql = "SELECT COUNT(" + PstKadar.fieldNames[PstKadar.FLD_KADAR_ID] + ") FROM " + TBL_POS_KADAR;
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
                    Kadar entKadar = (Kadar) list.get(ls);
                    if (oid == entKadar.getOID()) {
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
                Kadar kadar = PstKadar.fetchExc(oid);
                object.put(PstKadar.fieldNames[PstKadar.FLD_KADAR_ID], kadar.getOID());
                object.put(PstKadar.fieldNames[PstKadar.FLD_KADAR], kadar.getKadar());
                object.put(PstKadar.fieldNames[PstKadar.FLD_KARAT], kadar.getKarat());
                object.put(PstKadar.fieldNames[PstKadar.FLD_KODE_KADAR], kadar.getKodeKadar());
            }catch(Exception exc){}

            return object;
        }
}
