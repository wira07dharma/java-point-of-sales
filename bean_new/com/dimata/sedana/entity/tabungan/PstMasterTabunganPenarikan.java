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
public class PstMasterTabunganPenarikan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MASTER_TABUNGAN_PENARIKAN = "sedana_master_tabungan_penarikan";
    public static final int FLD_ID_TABUNGAN_RANGE_PENARIKAN = 0;
    public static final int FLD_JUDUL_RANGE_PENARIKAN = 1;
    public static final int FLD_TIPE_RANGE = 2;
    public static final int FLD_START_DATE = 3;
    public static final int FLD_END_DATE = 4;
    public static final int FLD_PERIODE_BULAN = 5;

    public static String[] fieldNames = {
        "ID_TABUNGAN_RANGE_PENARIKAN",
        "JUDUL_RANGE_PENARIKAN",
        "TIPE_RANGE",
        "START_DATE",
        "END_DATE",
        "PERIODE_BULAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_INT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT
    };

    public PstMasterTabunganPenarikan() {
    }

    public PstMasterTabunganPenarikan(int i) throws DBException {
        super(new PstMasterTabunganPenarikan());
    }

    public PstMasterTabunganPenarikan(String sOid) throws DBException {
        super(new PstMasterTabunganPenarikan(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMasterTabunganPenarikan(long lOid) throws DBException {
        super(new PstMasterTabunganPenarikan(0));
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
        return TBL_MASTER_TABUNGAN_PENARIKAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMasterTabunganPenarikan().getClass().getName();
    }

    public static MasterTabunganPenarikan fetchExc(long oid) throws DBException {
        MasterTabunganPenarikan entMasterTabunganPenarikan = new MasterTabunganPenarikan();
        try {
            PstMasterTabunganPenarikan pstMasterTabunganPenarikan = new PstMasterTabunganPenarikan(oid);
            entMasterTabunganPenarikan.setOID(oid);
            entMasterTabunganPenarikan.setJudulRangePenarikan(pstMasterTabunganPenarikan.getString(FLD_JUDUL_RANGE_PENARIKAN));
            entMasterTabunganPenarikan.setTipeRange(pstMasterTabunganPenarikan.getInt(FLD_TIPE_RANGE));
            entMasterTabunganPenarikan.setStartDate(pstMasterTabunganPenarikan.getDate(FLD_START_DATE));
            entMasterTabunganPenarikan.setEndDate(pstMasterTabunganPenarikan.getDate(FLD_END_DATE));
            entMasterTabunganPenarikan.setPeriodeBulan(pstMasterTabunganPenarikan.getInt(FLD_PERIODE_BULAN));
        } catch (DBException dbe) {
            System.err.println(dbe);
        } catch (Exception e) {
            System.err.println(e);
        }
        return entMasterTabunganPenarikan;
    }

    public long fetchExc(Entity entity) throws Exception {
        MasterTabunganPenarikan entMasterTabunganPenarikan = fetchExc(entity.getOID());
        entity = (Entity) entMasterTabunganPenarikan;
        return entMasterTabunganPenarikan.getOID();
    }

    public static synchronized long updateExc(MasterTabunganPenarikan entMasterTabunganPenarikan) throws DBException {
        try {
            if (entMasterTabunganPenarikan.getOID() != 0) {
                PstMasterTabunganPenarikan pstMasterTabunganPenarikan = new PstMasterTabunganPenarikan(entMasterTabunganPenarikan.getOID());
                pstMasterTabunganPenarikan.setString(FLD_JUDUL_RANGE_PENARIKAN, entMasterTabunganPenarikan.getJudulRangePenarikan());
                pstMasterTabunganPenarikan.setInt(FLD_TIPE_RANGE, entMasterTabunganPenarikan.getTipeRange());
                pstMasterTabunganPenarikan.setDate(FLD_START_DATE, entMasterTabunganPenarikan.getStartDate());
                pstMasterTabunganPenarikan.setDate(FLD_END_DATE, entMasterTabunganPenarikan.getEndDate());
                pstMasterTabunganPenarikan.setInt(FLD_PERIODE_BULAN, entMasterTabunganPenarikan.getPeriodeBulan());
                pstMasterTabunganPenarikan.update();
                return entMasterTabunganPenarikan.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterTabunganPenarikan(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MasterTabunganPenarikan) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMasterTabunganPenarikan pstMasterTabunganPenarikan = new PstMasterTabunganPenarikan(oid);
            pstMasterTabunganPenarikan.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterTabunganPenarikan(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MasterTabunganPenarikan entMasterTabunganPenarikan) throws DBException {
        try {
            PstMasterTabunganPenarikan pstMasterTabunganPenarikan = new PstMasterTabunganPenarikan(0);
            pstMasterTabunganPenarikan.setString(FLD_JUDUL_RANGE_PENARIKAN, entMasterTabunganPenarikan.getJudulRangePenarikan());
            pstMasterTabunganPenarikan.setInt(FLD_TIPE_RANGE, entMasterTabunganPenarikan.getTipeRange());
            pstMasterTabunganPenarikan.setDate(FLD_START_DATE, entMasterTabunganPenarikan.getStartDate());
            pstMasterTabunganPenarikan.setDate(FLD_END_DATE, entMasterTabunganPenarikan.getEndDate());
            pstMasterTabunganPenarikan.setInt(FLD_PERIODE_BULAN, entMasterTabunganPenarikan.getPeriodeBulan());
            pstMasterTabunganPenarikan.insert();
            entMasterTabunganPenarikan.setOID(pstMasterTabunganPenarikan.getlong(FLD_ID_TABUNGAN_RANGE_PENARIKAN));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterTabunganPenarikan(0), DBException.UNKNOWN);
        }
        return entMasterTabunganPenarikan.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MasterTabunganPenarikan) entity);
    }

    public static void resultToObject(ResultSet rs, MasterTabunganPenarikan entMasterTabunganPenarikan) {
        try {
            entMasterTabunganPenarikan.setOID(rs.getLong(PstMasterTabunganPenarikan.fieldNames[PstMasterTabunganPenarikan.FLD_ID_TABUNGAN_RANGE_PENARIKAN]));
            entMasterTabunganPenarikan.setJudulRangePenarikan(rs.getString(PstMasterTabunganPenarikan.fieldNames[PstMasterTabunganPenarikan.FLD_JUDUL_RANGE_PENARIKAN]));
            entMasterTabunganPenarikan.setTipeRange(rs.getInt(PstMasterTabunganPenarikan.fieldNames[PstMasterTabunganPenarikan.FLD_TIPE_RANGE]));
            entMasterTabunganPenarikan.setStartDate(rs.getDate(PstMasterTabunganPenarikan.fieldNames[PstMasterTabunganPenarikan.FLD_START_DATE]));
            entMasterTabunganPenarikan.setEndDate(rs.getDate(PstMasterTabunganPenarikan.fieldNames[PstMasterTabunganPenarikan.FLD_END_DATE]));
            entMasterTabunganPenarikan.setPeriodeBulan(rs.getInt(PstMasterTabunganPenarikan.fieldNames[PstMasterTabunganPenarikan.FLD_PERIODE_BULAN]));
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
            String sql = "SELECT * FROM " + TBL_MASTER_TABUNGAN_PENARIKAN;
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
                MasterTabunganPenarikan entMasterTabunganPenarikan = new MasterTabunganPenarikan();
                resultToObject(rs, entMasterTabunganPenarikan);
                lists.add(entMasterTabunganPenarikan);
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

    public static boolean checkOID(long entMasterTabunganPenarikanId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MASTER_TABUNGAN_PENARIKAN + " WHERE "
                    + PstMasterTabunganPenarikan.fieldNames[PstMasterTabunganPenarikan.FLD_ID_TABUNGAN_RANGE_PENARIKAN] + " = " + entMasterTabunganPenarikanId;
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
            String sql = "SELECT COUNT(" + PstMasterTabunganPenarikan.fieldNames[PstMasterTabunganPenarikan.FLD_ID_TABUNGAN_RANGE_PENARIKAN] + ") FROM " + TBL_MASTER_TABUNGAN_PENARIKAN;
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
                    MasterTabunganPenarikan entMasterTabunganPenarikan = (MasterTabunganPenarikan) list.get(ls);
                    if (oid == entMasterTabunganPenarikan.getOID()) {
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
