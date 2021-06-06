/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

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
public class PstAgunanMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_AGUNAN_MAPPING = "sedana_mapping_agunan";
    public static final int FLD_AGUNAN_MAPPING_ID = 0;
    public static final int FLD_AGUNAN_ID = 1;
    public static final int FLD_PINJAMAN_ID = 2;
    public static final int FLD_PROSENTASE_PINJAMAN = 3;

    public static String[] fieldNames = {
        "AGUNAN_MAPPING_ID",
        "AGUNAN_ID",
        "PINJAMAN_ID",
        "PROSENTASE_PINJAMAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT
    };

    public PstAgunanMapping() {
    }

    public PstAgunanMapping(int i) throws DBException {
        super(new PstAgunanMapping());
    }

    public PstAgunanMapping(String sOid) throws DBException {
        super(new PstAgunanMapping(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAgunanMapping(long lOid) throws DBException {
        super(new PstAgunanMapping(0));
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
        return TBL_AGUNAN_MAPPING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAgunanMapping().getClass().getName();
    }

    public static AgunanMapping fetchExc(long oid) throws DBException {
        try {
            AgunanMapping entAgunanMapping = new AgunanMapping();
            PstAgunanMapping pstAgunanMapping = new PstAgunanMapping(oid);
            entAgunanMapping.setOID(oid);
            entAgunanMapping.setAgunanId(pstAgunanMapping.getlong(FLD_AGUNAN_ID));
            entAgunanMapping.setPinjamanId(pstAgunanMapping.getlong(FLD_PINJAMAN_ID));
            entAgunanMapping.setProsentasePinjaman(pstAgunanMapping.getdouble(FLD_PROSENTASE_PINJAMAN));
            return entAgunanMapping;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAgunanMapping(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        AgunanMapping entAgunanMapping = fetchExc(entity.getOID());
        entity = (Entity) entAgunanMapping;
        return entAgunanMapping.getOID();
    }

    public static synchronized long updateExc(AgunanMapping entAgunanMapping) throws DBException {
        try {
            if (entAgunanMapping.getOID() != 0) {
                PstAgunanMapping pstAgunanMapping = new PstAgunanMapping(entAgunanMapping.getOID());
                pstAgunanMapping.setLong(FLD_AGUNAN_ID, entAgunanMapping.getAgunanId());
                pstAgunanMapping.setLong(FLD_PINJAMAN_ID, entAgunanMapping.getPinjamanId());
                pstAgunanMapping.setDouble(FLD_PROSENTASE_PINJAMAN, entAgunanMapping.getProsentasePinjaman());
                pstAgunanMapping.update();
                return entAgunanMapping.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAgunanMapping(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((AgunanMapping) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstAgunanMapping pstAgunanMapping = new PstAgunanMapping(oid);
            pstAgunanMapping.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAgunanMapping(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(AgunanMapping entAgunanMapping) throws DBException {
        try {
            PstAgunanMapping pstAgunanMapping = new PstAgunanMapping(0);
            pstAgunanMapping.setLong(FLD_AGUNAN_ID, entAgunanMapping.getAgunanId());
            pstAgunanMapping.setLong(FLD_PINJAMAN_ID, entAgunanMapping.getPinjamanId());
            pstAgunanMapping.setDouble(FLD_PROSENTASE_PINJAMAN, entAgunanMapping.getProsentasePinjaman());
            pstAgunanMapping.insert();
            entAgunanMapping.setOID(pstAgunanMapping.getlong(FLD_AGUNAN_MAPPING_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAgunanMapping(0), DBException.UNKNOWN);
        }
        return entAgunanMapping.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((AgunanMapping) entity);
    }

    public static void resultToObject(ResultSet rs, AgunanMapping entAgunanMapping) {
        try {
            entAgunanMapping.setOID(rs.getLong(PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_AGUNAN_MAPPING_ID]));
            entAgunanMapping.setAgunanId(rs.getLong(PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_AGUNAN_ID]));
            entAgunanMapping.setPinjamanId(rs.getLong(PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_PINJAMAN_ID]));
            entAgunanMapping.setProsentasePinjaman(rs.getDouble(PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_PROSENTASE_PINJAMAN]));
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
            String sql = "SELECT * FROM " + TBL_AGUNAN_MAPPING;
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
                AgunanMapping entAgunanMapping = new AgunanMapping();
                resultToObject(rs, entAgunanMapping);
                lists.add(entAgunanMapping);
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

    public static boolean checkOID(long entAgunanMappingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_AGUNAN_MAPPING + " WHERE "
                    + PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_AGUNAN_MAPPING_ID] + " = " + entAgunanMappingId;
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
            String sql = "SELECT COUNT(" + PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_AGUNAN_MAPPING_ID] + ") FROM " + TBL_AGUNAN_MAPPING;
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
                    AgunanMapping entAgunanMapping = (AgunanMapping) list.get(ls);
                    if (oid == entAgunanMapping.getOID()) {
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
