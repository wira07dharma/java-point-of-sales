/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.penjamin;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class PstPersentaseJaminan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PERSENTASE_JAMINAN = "sedana_persentase_jaminan";
    public static final int FLD_ID_PERSENTASE_JAMINAN = 0;
    public static final int FLD_ID_PENJAMIN = 1;
    public static final int FLD_JANGKA_WAKTU = 2;
    public static final int FLD_PERSENTASE_COVERAGE = 3;
    public static final int FLD_PERSENTASE_DIJAMIN = 4;

    public static String[] fieldNames = {
        "ID_PERSENTASE_JAMINAN",
        "ID_PENJAMIN",
        "JANGKA_WAKTU",
        "PERSENTASE_COVERAGE",
        "PERSENTASE_DIJAMIN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public PstPersentaseJaminan() {
    }

    public PstPersentaseJaminan(int i) throws DBException {
        super(new PstPersentaseJaminan());
    }

    public PstPersentaseJaminan(String sOid) throws DBException {
        super(new PstPersentaseJaminan(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPersentaseJaminan(long lOid) throws DBException {
        super(new PstPersentaseJaminan(0));
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
        return TBL_PERSENTASE_JAMINAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPersentaseJaminan().getClass().getName();
    }

    public static PersentaseJaminan fetchExc(long oid) throws DBException {
        try {
            PersentaseJaminan entPersentaseJaminan = new PersentaseJaminan();
            PstPersentaseJaminan pstPersentaseJaminan = new PstPersentaseJaminan(oid);
            entPersentaseJaminan.setOID(oid);
            entPersentaseJaminan.setIdPenjamin(pstPersentaseJaminan.getlong(FLD_ID_PENJAMIN));
            entPersentaseJaminan.setJangkaWaktu(pstPersentaseJaminan.getInt(FLD_JANGKA_WAKTU));
            entPersentaseJaminan.setPersentaseCoverage(pstPersentaseJaminan.getdouble(FLD_PERSENTASE_COVERAGE));
            entPersentaseJaminan.setPersentaseDijamin(pstPersentaseJaminan.getdouble(FLD_PERSENTASE_DIJAMIN));
            return entPersentaseJaminan;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPersentaseJaminan(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        PersentaseJaminan entPersentaseJaminan = fetchExc(entity.getOID());
        entity = (Entity) entPersentaseJaminan;
        return entPersentaseJaminan.getOID();
    }

    public static synchronized long updateExc(PersentaseJaminan entPersentaseJaminan) throws DBException {
        try {
            if (entPersentaseJaminan.getOID() != 0) {
                PstPersentaseJaminan pstPersentaseJaminan = new PstPersentaseJaminan(entPersentaseJaminan.getOID());
                pstPersentaseJaminan.setLong(FLD_ID_PENJAMIN, entPersentaseJaminan.getIdPenjamin());
                pstPersentaseJaminan.setInt(FLD_JANGKA_WAKTU, entPersentaseJaminan.getJangkaWaktu());
                pstPersentaseJaminan.setDouble(FLD_PERSENTASE_COVERAGE, entPersentaseJaminan.getPersentaseCoverage());
                pstPersentaseJaminan.setDouble(FLD_PERSENTASE_DIJAMIN, entPersentaseJaminan.getPersentaseDijamin());
                pstPersentaseJaminan.update();
                return entPersentaseJaminan.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPersentaseJaminan(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((PersentaseJaminan) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstPersentaseJaminan pstPersentaseJaminan = new PstPersentaseJaminan(oid);
            pstPersentaseJaminan.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPersentaseJaminan(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(PersentaseJaminan entPersentaseJaminan) throws DBException {
        try {
            PstPersentaseJaminan pstPersentaseJaminan = new PstPersentaseJaminan(0);
            pstPersentaseJaminan.setLong(FLD_ID_PENJAMIN, entPersentaseJaminan.getIdPenjamin());
            pstPersentaseJaminan.setInt(FLD_JANGKA_WAKTU, entPersentaseJaminan.getJangkaWaktu());
            pstPersentaseJaminan.setDouble(FLD_PERSENTASE_COVERAGE, entPersentaseJaminan.getPersentaseCoverage());
            pstPersentaseJaminan.setDouble(FLD_PERSENTASE_DIJAMIN, entPersentaseJaminan.getPersentaseDijamin());
            pstPersentaseJaminan.insert();
            entPersentaseJaminan.setOID(pstPersentaseJaminan.getlong(FLD_ID_PERSENTASE_JAMINAN));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPersentaseJaminan(0), DBException.UNKNOWN);
        }
        return entPersentaseJaminan.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((PersentaseJaminan) entity);
    }

    public static void resultToObject(ResultSet rs, PersentaseJaminan entPersentaseJaminan) {
        try {
            entPersentaseJaminan.setOID(rs.getLong(PstPersentaseJaminan.fieldNames[PstPersentaseJaminan.FLD_ID_PERSENTASE_JAMINAN]));
            entPersentaseJaminan.setIdPenjamin(rs.getLong(PstPersentaseJaminan.fieldNames[PstPersentaseJaminan.FLD_ID_PENJAMIN]));
            entPersentaseJaminan.setJangkaWaktu(rs.getInt(PstPersentaseJaminan.fieldNames[PstPersentaseJaminan.FLD_JANGKA_WAKTU]));
            entPersentaseJaminan.setPersentaseCoverage(rs.getDouble(PstPersentaseJaminan.fieldNames[PstPersentaseJaminan.FLD_PERSENTASE_COVERAGE]));
            entPersentaseJaminan.setPersentaseDijamin(rs.getDouble(PstPersentaseJaminan.fieldNames[PstPersentaseJaminan.FLD_PERSENTASE_DIJAMIN]));
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
            String sql = "SELECT * FROM " + TBL_PERSENTASE_JAMINAN;
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
                PersentaseJaminan entPersentaseJaminan = new PersentaseJaminan();
                resultToObject(rs, entPersentaseJaminan);
                lists.add(entPersentaseJaminan);
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

    public static boolean checkOID(long entPersentaseJaminanId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PERSENTASE_JAMINAN + " WHERE "
                    + PstPersentaseJaminan.fieldNames[PstPersentaseJaminan.FLD_ID_PERSENTASE_JAMINAN] + " = " + entPersentaseJaminanId;
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
            String sql = "SELECT COUNT(" + PstPersentaseJaminan.fieldNames[PstPersentaseJaminan.FLD_ID_PERSENTASE_JAMINAN] + ") FROM " + TBL_PERSENTASE_JAMINAN;
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
                    PersentaseJaminan entPersentaseJaminan = (PersentaseJaminan) list.get(ls);
                    if (oid == entPersentaseJaminan.getOID()) {
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
