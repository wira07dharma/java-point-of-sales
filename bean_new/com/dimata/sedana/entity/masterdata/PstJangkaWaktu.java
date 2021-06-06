/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

public class PstJangkaWaktu extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_JANGKA_WAKTU = "sedana_jangka_waktu";
    public static final int FLD_JANGKA_WAKTU_ID = 0;
    public static final int FLD_JANGKA_WAKTU = 1;

    public static String[] fieldNames = {
        "JANGKA_WAKTU_ID",
        "JANGKA_WAKTU"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_INT
    };

    public PstJangkaWaktu() {
    }

    public PstJangkaWaktu(int i) throws DBException {
        super(new PstJangkaWaktu());
    }

    public PstJangkaWaktu(String sOid) throws DBException {
        super(new PstJangkaWaktu(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstJangkaWaktu(long lOid) throws DBException {
        super(new PstJangkaWaktu(0));
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
        return TBL_JANGKA_WAKTU;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstJangkaWaktu().getClass().getName();
    }

    public static JangkaWaktu fetchExc(long oid) throws DBException {
        try {
            JangkaWaktu entJangkaWaktu = new JangkaWaktu();
            PstJangkaWaktu pstJangkaWaktu = new PstJangkaWaktu(oid);
            entJangkaWaktu.setOID(oid);
            entJangkaWaktu.setJangkaWaktu(pstJangkaWaktu.getInt(FLD_JANGKA_WAKTU));
            return entJangkaWaktu;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJangkaWaktu(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        JangkaWaktu entJangkaWaktu = fetchExc(entity.getOID());
        entity = (Entity) entJangkaWaktu;
        return entJangkaWaktu.getOID();
    }

    public static synchronized long updateExc(JangkaWaktu entJangkaWaktu) throws DBException {
        try {
            if (entJangkaWaktu.getOID() != 0) {
                PstJangkaWaktu pstJangkaWaktu = new PstJangkaWaktu(entJangkaWaktu.getOID());
                pstJangkaWaktu.setInt(FLD_JANGKA_WAKTU, entJangkaWaktu.getJangkaWaktu());
                pstJangkaWaktu.update();
                return entJangkaWaktu.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJangkaWaktu(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((JangkaWaktu) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstJangkaWaktu pstJangkaWaktu = new PstJangkaWaktu(oid);
            pstJangkaWaktu.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJangkaWaktu(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(JangkaWaktu entJangkaWaktu) throws DBException {
        try {
            PstJangkaWaktu pstJangkaWaktu = new PstJangkaWaktu(0);
            pstJangkaWaktu.setInt(FLD_JANGKA_WAKTU, entJangkaWaktu.getJangkaWaktu());
            pstJangkaWaktu.insert();
            entJangkaWaktu.setOID(pstJangkaWaktu.getlong(FLD_JANGKA_WAKTU_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJangkaWaktu(0), DBException.UNKNOWN);
        }
        return entJangkaWaktu.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((JangkaWaktu) entity);
    }

    public static void resultToObject(ResultSet rs, JangkaWaktu entJangkaWaktu) {
        try {
            entJangkaWaktu.setOID(rs.getLong(PstJangkaWaktu.fieldNames[PstJangkaWaktu.FLD_JANGKA_WAKTU_ID]));
            entJangkaWaktu.setJangkaWaktu(rs.getInt(PstJangkaWaktu.fieldNames[PstJangkaWaktu.FLD_JANGKA_WAKTU]));
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
            String sql = "SELECT * FROM " + TBL_JANGKA_WAKTU;
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
                JangkaWaktu entJangkaWaktu = new JangkaWaktu();
                resultToObject(rs, entJangkaWaktu);
                lists.add(entJangkaWaktu);
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

    public static boolean checkOID(long entJangkaWaktuId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_JANGKA_WAKTU + " WHERE "
                    + PstJangkaWaktu.fieldNames[PstJangkaWaktu.FLD_JANGKA_WAKTU_ID] + " = " + entJangkaWaktuId;
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
            String sql = "SELECT COUNT(" + PstJangkaWaktu.fieldNames[PstJangkaWaktu.FLD_JANGKA_WAKTU_ID] + ") FROM " + TBL_JANGKA_WAKTU;
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
                    JangkaWaktu entJangkaWaktu = (JangkaWaktu) list.get(ls);
                    if (oid == entJangkaWaktu.getOID()) {
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

    public static JSONObject fetchJSON(long oid) {
        JSONObject object = new JSONObject();
        try {
            JangkaWaktu entJangkaWaktu = PstJangkaWaktu.fetchExc(oid);
            object.put(PstJangkaWaktu.fieldNames[PstJangkaWaktu.FLD_JANGKA_WAKTU_ID], entJangkaWaktu.getOID());
            object.put(PstJangkaWaktu.fieldNames[PstJangkaWaktu.FLD_JANGKA_WAKTU], entJangkaWaktu.getJangkaWaktu());
        } catch (Exception exc) {
        }

        return object;
    }

    public static long syncExc(JSONObject jSONObject) {
        long oid = 0;
        if (jSONObject != null) {
            oid = jSONObject.optLong(PstJangkaWaktu.fieldNames[PstJangkaWaktu.FLD_JANGKA_WAKTU_ID], 0);
            if (oid > 0) {
                JangkaWaktu entJangkaWaktu = new JangkaWaktu();
                entJangkaWaktu.setOID(jSONObject.optLong(PstJangkaWaktu.fieldNames[PstJangkaWaktu.FLD_JANGKA_WAKTU_ID], 0));
                entJangkaWaktu.setJangkaWaktu(jSONObject.optInt(PstJangkaWaktu.fieldNames[PstJangkaWaktu.FLD_JANGKA_WAKTU], 0));

                boolean chekOidMaterial = PstJangkaWaktu.checkOID(oid);
                try {
                    if (chekOidMaterial) {
                        PstJangkaWaktu.updateExc(entJangkaWaktu);
                    } else {
                        PstJangkaWaktu.insertByOid(entJangkaWaktu);
                    }
                } catch (Exception exc) {
                }
            }
        }
        return oid;
    }

    public static long insertByOid(JangkaWaktu enJangkaWaktu) throws DBException {
        try {
            PstJangkaWaktu pstJangkaWaktu = new PstJangkaWaktu(0);
            pstJangkaWaktu.setInt(FLD_JANGKA_WAKTU, enJangkaWaktu.getJangkaWaktu());
            pstJangkaWaktu.insertByOid(enJangkaWaktu.getOID());
        } catch (DBException dbe) {
            throw dbe;

        } catch (Exception e) {
            throw new DBException(new PstJangkaWaktu(0), DBException.UNKNOWN);
        }
        return enJangkaWaktu.getOID();
    }
}
