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
public class PstJenisTransaksiMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_JENIS_TRANSAKSI_MAPPING = "sedana_jenis_transaksi_mapping";
    public static final int FLD_JENIS_TRANSAKSI_MAPPING_ID = 0;
    public static final int FLD_JENIS_TRANSAKSI_ID = 1;
    public static final int FLD_ID_JENIS_SIMPANAN = 2;

    public static String[] fieldNames = {
        "JENIS_TRANSAKSI_MAPPING_ID",
        "JENIS_TRANSAKSI_ID",
        "ID_JENIS_SIMPANAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstJenisTransaksiMapping() {
    }

    public PstJenisTransaksiMapping(int i) throws DBException {
        super(new PstJenisTransaksiMapping());
    }

    public PstJenisTransaksiMapping(String sOid) throws DBException {
        super(new PstJenisTransaksiMapping(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstJenisTransaksiMapping(long lOid) throws DBException {
        super(new PstJenisTransaksiMapping(0));
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
        return TBL_JENIS_TRANSAKSI_MAPPING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstJenisTransaksiMapping().getClass().getName();
    }

    public static JenisTransaksiMapping fetchExc(long oid) throws DBException {
        try {
            JenisTransaksiMapping entJenisTransaksiMapping = new JenisTransaksiMapping();
            PstJenisTransaksiMapping pstJenisTransaksiMapping = new PstJenisTransaksiMapping(oid);
            entJenisTransaksiMapping.setOID(oid);
            entJenisTransaksiMapping.setJenisTransaksiId(pstJenisTransaksiMapping.getlong(FLD_JENIS_TRANSAKSI_ID));
            entJenisTransaksiMapping.setIdJenisSimpanan(pstJenisTransaksiMapping.getlong(FLD_ID_JENIS_SIMPANAN));
            return entJenisTransaksiMapping;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJenisTransaksiMapping(0), DBException.UNKNOWN);
        }
    }
    
    public static JenisTransaksiMapping fetchWhere(String where) {
      return fetchWhere(where, "");
    }
    
    public static JenisTransaksiMapping fetchWhere(String where, String order) {
      Vector ms = list(0,0,where, order);
      JenisTransaksiMapping m = ms.size()>0?(JenisTransaksiMapping)ms.get(0):new JenisTransaksiMapping();      
      return m;
    }

    public long fetchExc(Entity entity) throws Exception {
        JenisTransaksiMapping entJenisTransaksiMapping = fetchExc(entity.getOID());
        entity = (Entity) entJenisTransaksiMapping;
        return entJenisTransaksiMapping.getOID();
    }

    public static synchronized long updateExc(JenisTransaksiMapping entJenisTransaksiMapping) throws DBException {
        try {
            if (entJenisTransaksiMapping.getOID() != 0) {
                PstJenisTransaksiMapping pstJenisTransaksiMapping = new PstJenisTransaksiMapping(entJenisTransaksiMapping.getOID());
                pstJenisTransaksiMapping.setLong(FLD_JENIS_TRANSAKSI_ID, entJenisTransaksiMapping.getJenisTransaksiId());
                pstJenisTransaksiMapping.setLong(FLD_ID_JENIS_SIMPANAN, entJenisTransaksiMapping.getIdJenisSimpanan());
                pstJenisTransaksiMapping.update();
                return entJenisTransaksiMapping.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJenisTransaksiMapping(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((JenisTransaksiMapping) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstJenisTransaksiMapping pstJenisTransaksiMapping = new PstJenisTransaksiMapping(oid);
            pstJenisTransaksiMapping.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJenisTransaksiMapping(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(JenisTransaksiMapping entJenisTransaksiMapping) throws DBException {
        try {
            PstJenisTransaksiMapping pstJenisTransaksiMapping = new PstJenisTransaksiMapping(0);
            pstJenisTransaksiMapping.setLong(FLD_JENIS_TRANSAKSI_ID, entJenisTransaksiMapping.getJenisTransaksiId());
            pstJenisTransaksiMapping.setLong(FLD_ID_JENIS_SIMPANAN, entJenisTransaksiMapping.getIdJenisSimpanan());
            pstJenisTransaksiMapping.insert();
            entJenisTransaksiMapping.setOID(pstJenisTransaksiMapping.getlong(FLD_JENIS_TRANSAKSI_MAPPING_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJenisTransaksiMapping(0), DBException.UNKNOWN);
        }
        return entJenisTransaksiMapping.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((JenisTransaksiMapping) entity);
    }

    public static void resultToObject(ResultSet rs, JenisTransaksiMapping entJenisTransaksiMapping) {
        try {
            entJenisTransaksiMapping.setOID(rs.getLong(PstJenisTransaksiMapping.fieldNames[PstJenisTransaksiMapping.FLD_JENIS_TRANSAKSI_MAPPING_ID]));
            entJenisTransaksiMapping.setJenisTransaksiId(rs.getLong(PstJenisTransaksiMapping.fieldNames[PstJenisTransaksiMapping.FLD_JENIS_TRANSAKSI_ID]));
            entJenisTransaksiMapping.setIdJenisSimpanan(rs.getLong(PstJenisTransaksiMapping.fieldNames[PstJenisTransaksiMapping.FLD_ID_JENIS_SIMPANAN]));
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
            String sql = "SELECT * FROM " + TBL_JENIS_TRANSAKSI_MAPPING;
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
                JenisTransaksiMapping entJenisTransaksiMapping = new JenisTransaksiMapping();
                resultToObject(rs, entJenisTransaksiMapping);
                lists.add(entJenisTransaksiMapping);
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

    public static boolean checkOID(long entJenisTransaksiMappingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_JENIS_TRANSAKSI_MAPPING + " WHERE "
                    + PstJenisTransaksiMapping.fieldNames[PstJenisTransaksiMapping.FLD_JENIS_TRANSAKSI_MAPPING_ID] + " = " + entJenisTransaksiMappingId;
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
            String sql = "SELECT COUNT(" + PstJenisTransaksiMapping.fieldNames[PstJenisTransaksiMapping.FLD_JENIS_TRANSAKSI_MAPPING_ID] + ") FROM " + TBL_JENIS_TRANSAKSI_MAPPING;
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
                    JenisTransaksiMapping entJenisTransaksiMapping = (JenisTransaksiMapping) list.get(ls);
                    if (oid == entJenisTransaksiMapping.getOID()) {
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
