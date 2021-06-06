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
public class PstAgunan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_AGUNAN = "sedana_master_agunan";
    public static final int FLD_AGUNAN_ID = 0;
    public static final int FLD_KODE_JENIS_AGUNAN = 1;
    public static final int FLD_CONTACT_ID = 2;
    public static final int FLD_KODE_KAB_LOKASI_AGUNAN = 3;
    public static final int FLD_ALAMAT_AGUNAN = 4;
    public static final int FLD_NILAI_AGUNAN_NJOP = 5;
    public static final int FLD_BUKTI_KEPEMILIKAN = 6;
    public static final int FLD_TIPE_AGUNAN = 7;
    public static final int FLD_NOTE_TIPE_AGUNAN = 8;
    public static final int FLD_NILAI_EKONOMIS = 9;
    public static final int FLD_NILAI_JAMINAN_AGUNAN = 10;

    public static String[] fieldNames = {
        "AGUNAN_ID",
        "KODE_JENIS_AGUNAN",
        "CONTACT_ID",
        "KODE_KAB_LOKASI_AGUNAN",
        "ALAMAT_AGUNAN",
        "NILAI_AGUNAN_NJOP",
        "BUKTI_KEPEMILIKAN",
        "TIPE_AGUNAN",
        "NOTE_TIPE_AGUNAN",
        "NILAI_EKONOMIS",
        "NILAI_JAMINAN_AGUNAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public PstAgunan() {
    }

    public PstAgunan(int i) throws DBException {
        super(new PstAgunan());
    }

    public PstAgunan(String sOid) throws DBException {
        super(new PstAgunan(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAgunan(long lOid) throws DBException {
        super(new PstAgunan(0));
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
        return TBL_AGUNAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAgunan().getClass().getName();
    }

    public static Agunan fetchExc(long oid) throws DBException {
        try {
            Agunan entAgunan = new Agunan();
            PstAgunan pstAgunan = new PstAgunan(oid);
            entAgunan.setOID(oid);
            entAgunan.setKodeJenisAgunan(pstAgunan.getString(FLD_KODE_JENIS_AGUNAN));
            entAgunan.setContactId(pstAgunan.getlong(FLD_CONTACT_ID));
            entAgunan.setKodeKabLokasiAgunan(pstAgunan.getlong(FLD_KODE_KAB_LOKASI_AGUNAN));
            entAgunan.setAlamatAgunan(pstAgunan.getString(FLD_ALAMAT_AGUNAN));
            entAgunan.setNilaiAgunanNjop(pstAgunan.getdouble(FLD_NILAI_AGUNAN_NJOP));
            entAgunan.setBuktiKepemilikan(pstAgunan.getString(FLD_BUKTI_KEPEMILIKAN));
            entAgunan.setTipeAgunan(pstAgunan.getInt(FLD_TIPE_AGUNAN));
            entAgunan.setNoteTipeAgunan(pstAgunan.getString(FLD_NOTE_TIPE_AGUNAN));
            entAgunan.setNilaiEkonomis(pstAgunan.getdouble(FLD_NILAI_EKONOMIS));
            entAgunan.setNilaiJaminanAgunan(pstAgunan.getdouble(FLD_NILAI_JAMINAN_AGUNAN));
            return entAgunan;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAgunan(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        Agunan entAgunan = fetchExc(entity.getOID());
        entity = (Entity) entAgunan;
        return entAgunan.getOID();
    }

    public static synchronized long updateExc(Agunan entAgunan) throws DBException {
        try {
            if (entAgunan.getOID() != 0) {
                PstAgunan pstAgunan = new PstAgunan(entAgunan.getOID());
                pstAgunan.setString(FLD_KODE_JENIS_AGUNAN, entAgunan.getKodeJenisAgunan());
                pstAgunan.setLong(FLD_CONTACT_ID, entAgunan.getContactId());
                pstAgunan.setLong(FLD_KODE_KAB_LOKASI_AGUNAN, entAgunan.getKodeKabLokasiAgunan());
                pstAgunan.setString(FLD_ALAMAT_AGUNAN, entAgunan.getAlamatAgunan());
                pstAgunan.setDouble(FLD_NILAI_AGUNAN_NJOP, entAgunan.getNilaiAgunanNjop());
                pstAgunan.setString(FLD_BUKTI_KEPEMILIKAN, entAgunan.getBuktiKepemilikan());
                pstAgunan.setInt(FLD_TIPE_AGUNAN, entAgunan.getTipeAgunan());
                pstAgunan.setString(FLD_NOTE_TIPE_AGUNAN, entAgunan.getNoteTipeAgunan());
                pstAgunan.setDouble(FLD_NILAI_EKONOMIS, entAgunan.getNilaiEkonomis());
                pstAgunan.setDouble(FLD_NILAI_JAMINAN_AGUNAN, entAgunan.getNilaiJaminanAgunan());
                pstAgunan.update();
                return entAgunan.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAgunan(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((Agunan) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstAgunan pstAgunan = new PstAgunan(oid);
            pstAgunan.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAgunan(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(Agunan entAgunan) throws DBException {
        try {
            PstAgunan pstAgunan = new PstAgunan(0);
            pstAgunan.setString(FLD_KODE_JENIS_AGUNAN, entAgunan.getKodeJenisAgunan());
            pstAgunan.setLong(FLD_CONTACT_ID, entAgunan.getContactId());
            pstAgunan.setLong(FLD_KODE_KAB_LOKASI_AGUNAN, entAgunan.getKodeKabLokasiAgunan());
            pstAgunan.setString(FLD_ALAMAT_AGUNAN, entAgunan.getAlamatAgunan());
            pstAgunan.setDouble(FLD_NILAI_AGUNAN_NJOP, entAgunan.getNilaiAgunanNjop());
            pstAgunan.setString(FLD_BUKTI_KEPEMILIKAN, entAgunan.getBuktiKepemilikan());
            pstAgunan.setInt(FLD_TIPE_AGUNAN, entAgunan.getTipeAgunan());
            pstAgunan.setString(FLD_NOTE_TIPE_AGUNAN, entAgunan.getNoteTipeAgunan());
            pstAgunan.setDouble(FLD_NILAI_EKONOMIS, entAgunan.getNilaiEkonomis());
            pstAgunan.setDouble(FLD_NILAI_JAMINAN_AGUNAN, entAgunan.getNilaiJaminanAgunan());
            pstAgunan.insert();
            entAgunan.setOID(pstAgunan.getlong(FLD_AGUNAN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAgunan(0), DBException.UNKNOWN);
        }
        return entAgunan.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((Agunan) entity);
    }

    public static void resultToObject(ResultSet rs, Agunan entAgunan) {
        try {
            entAgunan.setOID(rs.getLong(PstAgunan.fieldNames[PstAgunan.FLD_AGUNAN_ID]));
            entAgunan.setKodeJenisAgunan(rs.getString(PstAgunan.fieldNames[PstAgunan.FLD_KODE_JENIS_AGUNAN]));
            entAgunan.setContactId(rs.getLong(PstAgunan.fieldNames[PstAgunan.FLD_CONTACT_ID]));
            entAgunan.setKodeKabLokasiAgunan(rs.getLong(PstAgunan.fieldNames[PstAgunan.FLD_KODE_KAB_LOKASI_AGUNAN]));
            entAgunan.setAlamatAgunan(rs.getString(PstAgunan.fieldNames[PstAgunan.FLD_ALAMAT_AGUNAN]));
            entAgunan.setNilaiAgunanNjop(rs.getDouble(PstAgunan.fieldNames[PstAgunan.FLD_NILAI_AGUNAN_NJOP]));
            entAgunan.setBuktiKepemilikan(rs.getString(PstAgunan.fieldNames[PstAgunan.FLD_BUKTI_KEPEMILIKAN]));
            entAgunan.setTipeAgunan(rs.getInt(PstAgunan.fieldNames[PstAgunan.FLD_TIPE_AGUNAN]));
            entAgunan.setNoteTipeAgunan(rs.getString(PstAgunan.fieldNames[PstAgunan.FLD_NOTE_TIPE_AGUNAN]));
            entAgunan.setNilaiEkonomis(rs.getDouble(PstAgunan.fieldNames[PstAgunan.FLD_NILAI_EKONOMIS]));
            entAgunan.setNilaiJaminanAgunan(rs.getDouble(PstAgunan.fieldNames[PstAgunan.FLD_NILAI_JAMINAN_AGUNAN]));
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
            String sql = "SELECT * FROM " + TBL_AGUNAN;
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
                Agunan entAgunan = new Agunan();
                resultToObject(rs, entAgunan);
                lists.add(entAgunan);
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

    public static Vector listJoinMapping(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AGUNAN + " AS agunan "
                    + " INNER JOIN " + PstAgunanMapping.TBL_AGUNAN_MAPPING + " AS map "
                    + " ON map." + PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_AGUNAN_ID] + " = agunan." + fieldNames[FLD_AGUNAN_ID]
                    + "";
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
                Agunan entAgunan = new Agunan();
                resultToObject(rs, entAgunan);
                entAgunan.setAgunanMappingId(rs.getLong(PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_AGUNAN_MAPPING_ID]));
                lists.add(entAgunan);
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

    public static boolean checkOID(long entAgunanId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_AGUNAN + " WHERE "
                    + PstAgunan.fieldNames[PstAgunan.FLD_AGUNAN_ID] + " = " + entAgunanId;
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
            String sql = "SELECT COUNT(" + PstAgunan.fieldNames[PstAgunan.FLD_AGUNAN_ID] + ") FROM " + TBL_AGUNAN;
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
                    Agunan entAgunan = (Agunan) list.get(ls);
                    if (oid == entAgunan.getOID()) {
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
