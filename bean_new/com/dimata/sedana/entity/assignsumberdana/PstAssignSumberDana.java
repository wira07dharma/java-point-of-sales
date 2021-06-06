/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.assignsumberdana;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstAssignSumberDana extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_ASSIGNSUMBERDANA = "aiso_assign_sumber_dana_jenis_kredit";
    public static final int FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID = 0;
    public static final int FLD_SUMBER_DANA_ID = 1;
    public static final int FLD_TYPE_KREDIT_ID = 2;
    public static final int FLD_MAX_PRESENTASE_PENGGUNAAN = 3;

    public static String[] fieldNames = {
        "ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID",
        "SUMBER_DANA_ID",
        "TYPE_KREDIT_ID",
        "MAX_PERSENTASE_PENGGUNAAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT
    };

    public PstAssignSumberDana() {
    }

    public PstAssignSumberDana(int i) throws DBException {
        super(new PstAssignSumberDana());
    }

    public PstAssignSumberDana(String sOid) throws DBException {
        super(new PstAssignSumberDana(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAssignSumberDana(long lOid) throws DBException {
        super(new PstAssignSumberDana(0));
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
        return TBL_ASSIGNSUMBERDANA;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAssignSumberDana().getClass().getName();
    }

    public static AssignSumberDana fetchExc(long oid) throws DBException {
        try {
            AssignSumberDana entAssignSumberDana = new AssignSumberDana();
            PstAssignSumberDana pstAssignSumberDana = new PstAssignSumberDana(oid);
            entAssignSumberDana.setOID(oid);
            entAssignSumberDana.setSumberDanaId(pstAssignSumberDana.getlong(FLD_SUMBER_DANA_ID));
            entAssignSumberDana.setTypeKreditId(pstAssignSumberDana.getlong(FLD_TYPE_KREDIT_ID));
            entAssignSumberDana.setMaxPresentasePenggunaan(pstAssignSumberDana.getfloat(FLD_MAX_PRESENTASE_PENGGUNAAN));
            return entAssignSumberDana;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignSumberDana(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        AssignSumberDana entAssignSumberDana = fetchExc(entity.getOID());
        entity = (Entity) entAssignSumberDana;
        return entAssignSumberDana.getOID();
    }

    public static synchronized long updateExc(AssignSumberDana entAssignSumberDana) throws DBException {
        try {
            if (entAssignSumberDana.getOID() != 0) {
                PstAssignSumberDana pstAssignSumberDana = new PstAssignSumberDana(entAssignSumberDana.getOID());
                pstAssignSumberDana.setLong(FLD_SUMBER_DANA_ID, entAssignSumberDana.getSumberDanaId());
                pstAssignSumberDana.setLong(FLD_TYPE_KREDIT_ID, entAssignSumberDana.getTypeKreditId());
                pstAssignSumberDana.setFloat(FLD_MAX_PRESENTASE_PENGGUNAAN, entAssignSumberDana.getMaxPresentasePenggunaan());
                pstAssignSumberDana.update();
                return entAssignSumberDana.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignSumberDana(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((AssignSumberDana) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstAssignSumberDana pstAssignSumberDana = new PstAssignSumberDana(oid);
            pstAssignSumberDana.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignSumberDana(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(AssignSumberDana entAssignSumberDana) throws DBException {
        try {
            PstAssignSumberDana pstAssignSumberDana = new PstAssignSumberDana(0);
            pstAssignSumberDana.setLong(FLD_SUMBER_DANA_ID, entAssignSumberDana.getSumberDanaId());
            pstAssignSumberDana.setLong(FLD_TYPE_KREDIT_ID, entAssignSumberDana.getTypeKreditId());
            pstAssignSumberDana.setFloat(FLD_MAX_PRESENTASE_PENGGUNAAN, entAssignSumberDana.getMaxPresentasePenggunaan());
            pstAssignSumberDana.insert();
            entAssignSumberDana.setOID(pstAssignSumberDana.getlong(FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignSumberDana(0), DBException.UNKNOWN);
        }
        return entAssignSumberDana.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((AssignSumberDana) entity);
    }

    public static void resultToObject(ResultSet rs, AssignSumberDana entAssignSumberDana) {
        try {
            entAssignSumberDana.setOID(rs.getLong(PstAssignSumberDana.fieldNames[PstAssignSumberDana.FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID]));
            entAssignSumberDana.setSumberDanaId(rs.getLong(PstAssignSumberDana.fieldNames[PstAssignSumberDana.FLD_SUMBER_DANA_ID]));
            entAssignSumberDana.setTypeKreditId(rs.getLong(PstAssignSumberDana.fieldNames[PstAssignSumberDana.FLD_TYPE_KREDIT_ID]));
            entAssignSumberDana.setMaxPresentasePenggunaan(rs.getFloat(PstAssignSumberDana.fieldNames[PstAssignSumberDana.FLD_MAX_PRESENTASE_PENGGUNAAN]));
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
            String sql = "SELECT * FROM " + TBL_ASSIGNSUMBERDANA;
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
                AssignSumberDana entAssignSumberDana = new AssignSumberDana();
                resultToObject(rs, entAssignSumberDana);
                lists.add(entAssignSumberDana);
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

    public static boolean checkOID(long entAssignSumberDanaId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_ASSIGNSUMBERDANA + " WHERE "
                    + PstAssignSumberDana.fieldNames[PstAssignSumberDana.FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID] + " = " + entAssignSumberDanaId;
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
            String sql = "SELECT COUNT(" + PstAssignSumberDana.fieldNames[PstAssignSumberDana.FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID] + ") FROM " + TBL_ASSIGNSUMBERDANA;
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
                    AssignSumberDana entAssignSumberDana = (AssignSumberDana) list.get(ls);
                    if (oid == entAssignSumberDana.getOID()) {
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
