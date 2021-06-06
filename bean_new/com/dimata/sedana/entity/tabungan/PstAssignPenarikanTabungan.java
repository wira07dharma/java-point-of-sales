/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.tabungan;

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
public class PstAssignPenarikanTabungan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_ASSIGN_PENARIKAN_TABUNGAN = "sedana_assign_penarikan_tabungan";
    public static final int FLD_ID_ASSIGN_PENARIKAN_TABUNGAN = 0;
    public static final int FLD_MASTER_TABUNGAN_ID = 1;
    public static final int FLD_ID_TABUNGAN_RANGE_PENARIKAN = 2;

    public static String[] fieldNames = {
        "ID_ASSIGN_PENARIKAN_TABUNGAN",
        "MASTER_TABUNGAN_ID",
        "ID_TABUNGAN_RANGE_PENARIKAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstAssignPenarikanTabungan() {
    }

    public PstAssignPenarikanTabungan(int i) throws DBException {
        super(new PstAssignPenarikanTabungan());
    }

    public PstAssignPenarikanTabungan(String sOid) throws DBException {
        super(new PstAssignPenarikanTabungan(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAssignPenarikanTabungan(long lOid) throws DBException {
        super(new PstAssignPenarikanTabungan(0));
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
        return TBL_ASSIGN_PENARIKAN_TABUNGAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAssignPenarikanTabungan().getClass().getName();
    }

    public static AssignPenarikanTabungan fetchExc(long oid) throws DBException {
        try {
            AssignPenarikanTabungan entAssignPenarikanTabungan = new AssignPenarikanTabungan();
            PstAssignPenarikanTabungan pstAssignPenarikanTabungan = new PstAssignPenarikanTabungan(oid);
            entAssignPenarikanTabungan.setOID(oid);
            entAssignPenarikanTabungan.setMasterTabunganId(pstAssignPenarikanTabungan.getlong(FLD_MASTER_TABUNGAN_ID));
            entAssignPenarikanTabungan.setIdTabunganRangePenarikan(pstAssignPenarikanTabungan.getlong(FLD_ID_TABUNGAN_RANGE_PENARIKAN));
            return entAssignPenarikanTabungan;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignPenarikanTabungan(0), DBException.UNKNOWN);
        }
    }

    public static AssignPenarikanTabungan fetchWhere(String where) throws DBException {
        Vector<AssignPenarikanTabungan> tab = list(0, 0, where, "");
        return (tab.size()>0?tab.get(0):new AssignPenarikanTabungan());
    }

    public long fetchExc(Entity entity) throws Exception {
        AssignPenarikanTabungan entAssignPenarikanTabungan = fetchExc(entity.getOID());
        entity = (Entity) entAssignPenarikanTabungan;
        return entAssignPenarikanTabungan.getOID();
    }

    public static synchronized long updateExc(AssignPenarikanTabungan entAssignPenarikanTabungan) throws DBException {
        try {
            if (entAssignPenarikanTabungan.getOID() != 0) {
                PstAssignPenarikanTabungan pstAssignPenarikanTabungan = new PstAssignPenarikanTabungan(entAssignPenarikanTabungan.getOID());
                pstAssignPenarikanTabungan.setLong(FLD_MASTER_TABUNGAN_ID, entAssignPenarikanTabungan.getMasterTabunganId());
                pstAssignPenarikanTabungan.setLong(FLD_ID_TABUNGAN_RANGE_PENARIKAN, entAssignPenarikanTabungan.getIdTabunganRangePenarikan());
                pstAssignPenarikanTabungan.update();
                return entAssignPenarikanTabungan.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignPenarikanTabungan(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((AssignPenarikanTabungan) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstAssignPenarikanTabungan pstAssignPenarikanTabungan = new PstAssignPenarikanTabungan(oid);
            pstAssignPenarikanTabungan.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignPenarikanTabungan(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(AssignPenarikanTabungan entAssignPenarikanTabungan) throws DBException {
        try {
            PstAssignPenarikanTabungan pstAssignPenarikanTabungan = new PstAssignPenarikanTabungan(0);
            pstAssignPenarikanTabungan.setLong(FLD_MASTER_TABUNGAN_ID, entAssignPenarikanTabungan.getMasterTabunganId());
            pstAssignPenarikanTabungan.setLong(FLD_ID_TABUNGAN_RANGE_PENARIKAN, entAssignPenarikanTabungan.getIdTabunganRangePenarikan());
            pstAssignPenarikanTabungan.insert();
            entAssignPenarikanTabungan.setOID(pstAssignPenarikanTabungan.getlong(FLD_ID_ASSIGN_PENARIKAN_TABUNGAN));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAssignPenarikanTabungan(0), DBException.UNKNOWN);
        }
        return entAssignPenarikanTabungan.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((AssignPenarikanTabungan) entity);
    }

    public static void resultToObject(ResultSet rs, AssignPenarikanTabungan entAssignPenarikanTabungan) {
        try {
            entAssignPenarikanTabungan.setOID(rs.getLong(PstAssignPenarikanTabungan.fieldNames[PstAssignPenarikanTabungan.FLD_ID_ASSIGN_PENARIKAN_TABUNGAN]));
            entAssignPenarikanTabungan.setMasterTabunganId(rs.getLong(PstAssignPenarikanTabungan.fieldNames[PstAssignPenarikanTabungan.FLD_MASTER_TABUNGAN_ID]));
            entAssignPenarikanTabungan.setIdTabunganRangePenarikan(rs.getLong(PstAssignPenarikanTabungan.fieldNames[PstAssignPenarikanTabungan.FLD_ID_TABUNGAN_RANGE_PENARIKAN]));
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
            String sql = "SELECT * FROM " + TBL_ASSIGN_PENARIKAN_TABUNGAN;
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
                AssignPenarikanTabungan entAssignPenarikanTabungan = new AssignPenarikanTabungan();
                resultToObject(rs, entAssignPenarikanTabungan);
                lists.add(entAssignPenarikanTabungan);
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

    public static boolean checkOID(long entAssignPenarikanTabunganId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_ASSIGN_PENARIKAN_TABUNGAN + " WHERE "
                    + PstAssignPenarikanTabungan.fieldNames[PstAssignPenarikanTabungan.FLD_ID_ASSIGN_PENARIKAN_TABUNGAN] + " = " + entAssignPenarikanTabunganId;
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
            String sql = "SELECT COUNT(" + PstAssignPenarikanTabungan.fieldNames[PstAssignPenarikanTabungan.FLD_ID_ASSIGN_PENARIKAN_TABUNGAN] + ") FROM " + TBL_ASSIGN_PENARIKAN_TABUNGAN;
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
                    AssignPenarikanTabungan entAssignPenarikanTabungan = (AssignPenarikanTabungan) list.get(ls);
                    if (oid == entAssignPenarikanTabungan.getOID()) {
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
