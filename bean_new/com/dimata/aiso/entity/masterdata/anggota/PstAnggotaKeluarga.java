/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

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
public class PstAnggotaKeluarga extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_ANGGOTA_KELUARGA = "aiso_anggota_keluarga";
    public static final int FLD_RELASI_ANGGOTA_ID = 0;
    public static final int FLD_CONTACT_ANGGOTA_ID = 1;
    public static final int FLD_CONTACT_KELUARGA_ID = 2;
    public static final int FLD_STATUS_RELASI = 3;
    public static final int FLD_KETERANGAN = 4;

    public static String[] fieldNames = {
        "RELASI_ANGGOTA_ID",
        "CONTACT_ANGGOTA_ID",
        "CONTACT_KELUARGA_ID",
        "STATUS_RELASI",
        "KETERANGAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING
    };

    public PstAnggotaKeluarga() {
    }

    public PstAnggotaKeluarga(int i) throws DBException {
        super(new PstAnggotaKeluarga());
    }

    public PstAnggotaKeluarga(String sOid) throws DBException {
        super(new PstAnggotaKeluarga(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAnggotaKeluarga(long lOid) throws DBException {
        super(new PstAnggotaKeluarga(0));
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
        return TBL_ANGGOTA_KELUARGA;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAnggotaKeluarga().getClass().getName();
    }

    public static AnggotaKeluarga fetchExc(long oid) throws DBException {
        try {
            AnggotaKeluarga entAnggotaKeluarga = new AnggotaKeluarga();
            PstAnggotaKeluarga pstAnggotaKeluarga = new PstAnggotaKeluarga(oid);
            entAnggotaKeluarga.setOID(oid);
            entAnggotaKeluarga.setContactAnggotaId(pstAnggotaKeluarga.getlong(FLD_CONTACT_ANGGOTA_ID));
            entAnggotaKeluarga.setContactKeluargaId(pstAnggotaKeluarga.getlong(FLD_CONTACT_KELUARGA_ID));
            entAnggotaKeluarga.setStatusRelasi(pstAnggotaKeluarga.getInt(FLD_STATUS_RELASI));
            entAnggotaKeluarga.setKeterangan(pstAnggotaKeluarga.getString(FLD_KETERANGAN));
            return entAnggotaKeluarga;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggotaKeluarga(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        AnggotaKeluarga entAnggotaKeluarga = fetchExc(entity.getOID());
        entity = (Entity) entAnggotaKeluarga;
        return entAnggotaKeluarga.getOID();
    }

    public static synchronized long updateExc(AnggotaKeluarga entAnggotaKeluarga) throws DBException {
        try {
            if (entAnggotaKeluarga.getOID() != 0) {
                PstAnggotaKeluarga pstAnggotaKeluarga = new PstAnggotaKeluarga(entAnggotaKeluarga.getOID());
                pstAnggotaKeluarga.setLong(FLD_CONTACT_ANGGOTA_ID, entAnggotaKeluarga.getContactAnggotaId());
                pstAnggotaKeluarga.setLong(FLD_CONTACT_KELUARGA_ID, entAnggotaKeluarga.getContactKeluargaId());
                pstAnggotaKeluarga.setInt(FLD_STATUS_RELASI, entAnggotaKeluarga.getStatusRelasi());
                pstAnggotaKeluarga.setString(FLD_KETERANGAN, entAnggotaKeluarga.getKeterangan());
                pstAnggotaKeluarga.update();
                return entAnggotaKeluarga.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggotaKeluarga(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((AnggotaKeluarga) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstAnggotaKeluarga pstAnggotaKeluarga = new PstAnggotaKeluarga(oid);
            pstAnggotaKeluarga.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggotaKeluarga(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(AnggotaKeluarga entAnggotaKeluarga) throws DBException {
        try {
            PstAnggotaKeluarga pstAnggotaKeluarga = new PstAnggotaKeluarga(0);
            pstAnggotaKeluarga.setLong(FLD_CONTACT_ANGGOTA_ID, entAnggotaKeluarga.getContactAnggotaId());
            pstAnggotaKeluarga.setLong(FLD_CONTACT_KELUARGA_ID, entAnggotaKeluarga.getContactKeluargaId());
            pstAnggotaKeluarga.setInt(FLD_STATUS_RELASI, entAnggotaKeluarga.getStatusRelasi());
            pstAnggotaKeluarga.setString(FLD_KETERANGAN, entAnggotaKeluarga.getKeterangan());
            pstAnggotaKeluarga.insert();
            entAnggotaKeluarga.setOID(pstAnggotaKeluarga.getlong(FLD_RELASI_ANGGOTA_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggotaKeluarga(0), DBException.UNKNOWN);
        }
        return entAnggotaKeluarga.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((AnggotaKeluarga) entity);
    }

    public static void resultToObject(ResultSet rs, AnggotaKeluarga entAnggotaKeluarga) {
        try {
            entAnggotaKeluarga.setOID(rs.getLong(PstAnggotaKeluarga.fieldNames[PstAnggotaKeluarga.FLD_RELASI_ANGGOTA_ID]));
            entAnggotaKeluarga.setContactAnggotaId(rs.getLong(PstAnggotaKeluarga.fieldNames[PstAnggotaKeluarga.FLD_CONTACT_ANGGOTA_ID]));
            entAnggotaKeluarga.setContactKeluargaId(rs.getLong(PstAnggotaKeluarga.fieldNames[PstAnggotaKeluarga.FLD_CONTACT_KELUARGA_ID]));
            entAnggotaKeluarga.setStatusRelasi(rs.getInt(PstAnggotaKeluarga.fieldNames[PstAnggotaKeluarga.FLD_STATUS_RELASI]));
            entAnggotaKeluarga.setKeterangan(rs.getString(PstAnggotaKeluarga.fieldNames[PstAnggotaKeluarga.FLD_KETERANGAN]));
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
            String sql = "SELECT * FROM " + TBL_ANGGOTA_KELUARGA;
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
                AnggotaKeluarga entAnggotaKeluarga = new AnggotaKeluarga();
                resultToObject(rs, entAnggotaKeluarga);
                lists.add(entAnggotaKeluarga);
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

    public static boolean checkOID(long entAnggotaKeluargaId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_ANGGOTA_KELUARGA + " WHERE "
                    + PstAnggotaKeluarga.fieldNames[PstAnggotaKeluarga.FLD_RELASI_ANGGOTA_ID] + " = " + entAnggotaKeluargaId;
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
            String sql = "SELECT COUNT(" + PstAnggotaKeluarga.fieldNames[PstAnggotaKeluarga.FLD_RELASI_ANGGOTA_ID] + ") FROM " + TBL_ANGGOTA_KELUARGA;
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
                    AnggotaKeluarga entAnggotaKeluarga = (AnggotaKeluarga) list.get(ls);
                    if (oid == entAnggotaKeluarga.getOID()) {
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
