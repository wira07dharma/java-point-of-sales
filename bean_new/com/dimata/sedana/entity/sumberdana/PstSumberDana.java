/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.sumberdana;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstSumberDana extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_SUMBERDANA = "aiso_sumber_dana";
    public static final int FLD_SUMBER_DANA_ID = 0;
    public static final int FLD_CONTACT_ID = 1;
    public static final int FLD_JENIS_SUMBER_DANA = 2;
    public static final int FLD_KODE_SUMBER_DANA = 3;
    public static final int FLD_JUDUL_SUMBER_DANA = 4;
    public static final int FLD_TARGET_PENDANAAN = 5;
    public static final int FLD_PRIORITAS_PENGGUNAAN = 6;
    public static final int FLD_TOTAL_KETERSEDIAAN_DANA = 7;
    public static final int FLD_BIAYA_BUNGA_KE_KREDITUR = 8;
    public static final int FLD_TIPE_BUNGA_KE_KREDITUR = 9;
    public static final int FLD_TANGGAL_DANA_MASUK = 10;
    public static final int FLD_TANGGAL_LUNAS_KE_KREDITUR = 11;
    public static final int FLD_TANGGAL_DANA_MULAI_TERSEDIA = 12;
    public static final int FLD_TANGGAL_AKHIR_TERSEDIA = 13;
    public static final int FLD_MINIMUM_PINJAMAN_KE_DEBITUR = 14;
    public static final int FLD_MAXIMUM_PINJAMAN_KE_DEBITUR = 15;

    public static String[] fieldNames = {
        "SUMBER_DANA_ID",
        "CONTACT_ID",
        "JENIS_SUMBER_DANA",
        "KODE_SUMBER_DANA",
        "JUDUL_SUMBER_DANA",
        "TARGET_PENDANAAN",
        "PRIORITAS_PENGGUNAAN",
        "TOTAL_KETERSEDIAAN_DANA",
        "BIAYA_BUNGA_KE_KREDITUR",
        "TIPE_BUNGA_KE_KREDITUR",
        "TANGGAL_DANA_MASUK",
        "TANGGAL_LUNAS_KE_KREDITUR",
        "TANGGAL_DANA_MULAI_TERSEDIA",
        "TANGGAL_AKHIR_TERSEDIA",
        "MINIMUM_PINJAMAN_KE_DEBITUR",
        "MAXIMUM_PINJAMAN_KE_DEBITUR"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public static final int HIBAH = 0;
    public static final int KREDIT = 1;
    public static final String[] jenisSumberDana = {
        "Hibah",
        "Kredit"
    };
    
    public static final String[] tipeBunga = {
        "Menetap",
        "Menurun",
        "Anuitas"
    };
    
    public PstSumberDana() {
    }

    public PstSumberDana(int i) throws DBException {
        super(new PstSumberDana());
    }

    public PstSumberDana(String sOid) throws DBException {
        super(new PstSumberDana(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstSumberDana(long lOid) throws DBException {
        super(new PstSumberDana(0));
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
        return TBL_SUMBERDANA;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstSumberDana().getClass().getName();
    }

    public static SumberDana fetchExc(long oid) throws DBException {
        try {
            SumberDana entSumberDana = new SumberDana();
            PstSumberDana pstSumberDana = new PstSumberDana(oid);
            entSumberDana.setOID(oid);
            entSumberDana.setContactId(pstSumberDana.getlong(FLD_CONTACT_ID));
            entSumberDana.setJenisSumberDana(pstSumberDana.getInt(FLD_JENIS_SUMBER_DANA));
            entSumberDana.setKodeSumberDana(pstSumberDana.getString(FLD_KODE_SUMBER_DANA));
            entSumberDana.setJudulSumberDana(pstSumberDana.getString(FLD_JUDUL_SUMBER_DANA));
            entSumberDana.setTargetPendanaan(pstSumberDana.getString(FLD_TARGET_PENDANAAN));
            entSumberDana.setPrioritasPenggunaan(pstSumberDana.getInt(FLD_PRIORITAS_PENGGUNAAN));
            entSumberDana.setTotalKetersediaanDana(pstSumberDana.getdouble(FLD_TOTAL_KETERSEDIAAN_DANA));
            entSumberDana.setBiayaBungaKeKreditur(pstSumberDana.getfloat(FLD_BIAYA_BUNGA_KE_KREDITUR));
            entSumberDana.setTipeBungaKeKreditur(pstSumberDana.getInt(FLD_TIPE_BUNGA_KE_KREDITUR));
            entSumberDana.setTanggalDanaMasuk(pstSumberDana.getDate(FLD_TANGGAL_DANA_MASUK));
            entSumberDana.setTanggalLunasKeKreditur(pstSumberDana.getDate(FLD_TANGGAL_LUNAS_KE_KREDITUR));
            entSumberDana.setTanggalDanaMulaiTersedia(pstSumberDana.getDate(FLD_TANGGAL_DANA_MULAI_TERSEDIA));
            entSumberDana.setTanggalAkhirTersedia(pstSumberDana.getDate(FLD_TANGGAL_AKHIR_TERSEDIA));
            entSumberDana.setMinimumPinjamanKeDebitur(pstSumberDana.getdouble(FLD_MINIMUM_PINJAMAN_KE_DEBITUR));
            entSumberDana.setMaximumPinjamanKeDebitur(pstSumberDana.getdouble(FLD_MAXIMUM_PINJAMAN_KE_DEBITUR));
            return entSumberDana;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSumberDana(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        SumberDana entSumberDana = fetchExc(entity.getOID());
        entity = (Entity) entSumberDana;
        return entSumberDana.getOID();
    }

    public static synchronized long updateExc(SumberDana entSumberDana) throws DBException {
        try {
            if (entSumberDana.getOID() != 0) {
                PstSumberDana pstSumberDana = new PstSumberDana(entSumberDana.getOID());
                pstSumberDana.setLong(FLD_CONTACT_ID, entSumberDana.getContactId());
                pstSumberDana.setInt(FLD_JENIS_SUMBER_DANA, entSumberDana.getJenisSumberDana());
                pstSumberDana.setString(FLD_KODE_SUMBER_DANA, entSumberDana.getKodeSumberDana());
                pstSumberDana.setString(FLD_JUDUL_SUMBER_DANA, entSumberDana.getJudulSumberDana());
                pstSumberDana.setString(FLD_TARGET_PENDANAAN, entSumberDana.getTargetPendanaan());
                pstSumberDana.setInt(FLD_PRIORITAS_PENGGUNAAN, entSumberDana.getPrioritasPenggunaan());
                pstSumberDana.setDouble(FLD_TOTAL_KETERSEDIAAN_DANA, entSumberDana.getTotalKetersediaanDana());
                pstSumberDana.setFloat(FLD_BIAYA_BUNGA_KE_KREDITUR, entSumberDana.getBiayaBungaKeKreditur());
                pstSumberDana.setInt(FLD_TIPE_BUNGA_KE_KREDITUR, entSumberDana.getTipeBungaKeKreditur());
                pstSumberDana.setDate(FLD_TANGGAL_DANA_MASUK, entSumberDana.getTanggalDanaMasuk());
                pstSumberDana.setDate(FLD_TANGGAL_LUNAS_KE_KREDITUR, entSumberDana.getTanggalLunasKeKreditur());
                pstSumberDana.setDate(FLD_TANGGAL_DANA_MULAI_TERSEDIA, entSumberDana.getTanggalDanaMulaiTersedia());
                pstSumberDana.setDate(FLD_TANGGAL_AKHIR_TERSEDIA, entSumberDana.getTanggalAkhirTersedia());
                pstSumberDana.setDouble(FLD_MINIMUM_PINJAMAN_KE_DEBITUR, entSumberDana.getMinimumPinjamanKeDebitur());
                pstSumberDana.setDouble(FLD_MAXIMUM_PINJAMAN_KE_DEBITUR, entSumberDana.getMinimumPinjamanKeDebitur());
                pstSumberDana.update();
                return entSumberDana.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSumberDana(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((SumberDana) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstSumberDana pstSumberDana = new PstSumberDana(oid);
            pstSumberDana.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSumberDana(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(SumberDana entSumberDana) throws DBException {
        try {
            PstSumberDana pstSumberDana = new PstSumberDana(0);
            pstSumberDana.setLong(FLD_CONTACT_ID, entSumberDana.getContactId());
            pstSumberDana.setInt(FLD_JENIS_SUMBER_DANA, entSumberDana.getJenisSumberDana());
            pstSumberDana.setString(FLD_KODE_SUMBER_DANA, entSumberDana.getKodeSumberDana());
            pstSumberDana.setString(FLD_JUDUL_SUMBER_DANA, entSumberDana.getJudulSumberDana());
            pstSumberDana.setString(FLD_TARGET_PENDANAAN, entSumberDana.getTargetPendanaan());
            pstSumberDana.setInt(FLD_PRIORITAS_PENGGUNAAN, entSumberDana.getPrioritasPenggunaan());
            pstSumberDana.setDouble(FLD_TOTAL_KETERSEDIAAN_DANA, entSumberDana.getTotalKetersediaanDana());
            pstSumberDana.setFloat(FLD_BIAYA_BUNGA_KE_KREDITUR, entSumberDana.getBiayaBungaKeKreditur());
            pstSumberDana.setInt(FLD_TIPE_BUNGA_KE_KREDITUR, entSumberDana.getTipeBungaKeKreditur());
            pstSumberDana.setDate(FLD_TANGGAL_DANA_MASUK, entSumberDana.getTanggalDanaMasuk());
            pstSumberDana.setDate(FLD_TANGGAL_LUNAS_KE_KREDITUR, entSumberDana.getTanggalLunasKeKreditur());
            pstSumberDana.setDate(FLD_TANGGAL_DANA_MULAI_TERSEDIA, entSumberDana.getTanggalDanaMulaiTersedia());
            pstSumberDana.setDate(FLD_TANGGAL_AKHIR_TERSEDIA, entSumberDana.getTanggalAkhirTersedia());
            pstSumberDana.setDouble(FLD_MINIMUM_PINJAMAN_KE_DEBITUR, entSumberDana.getMinimumPinjamanKeDebitur());
            pstSumberDana.setDouble(FLD_MAXIMUM_PINJAMAN_KE_DEBITUR, entSumberDana.getMaximumPinjamanKeDebitur());
            pstSumberDana.insert();
            entSumberDana.setOID(pstSumberDana.getlong(FLD_SUMBER_DANA_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSumberDana(0), DBException.UNKNOWN);
        }
        return entSumberDana.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((SumberDana) entity);
    }

    public static void resultToObject(ResultSet rs, SumberDana entSumberDana) {
        try {
            entSumberDana.setOID(rs.getLong(PstSumberDana.fieldNames[PstSumberDana.FLD_SUMBER_DANA_ID]));
            entSumberDana.setContactId(rs.getLong(PstSumberDana.fieldNames[PstSumberDana.FLD_CONTACT_ID]));
            entSumberDana.setJenisSumberDana(rs.getInt(PstSumberDana.fieldNames[PstSumberDana.FLD_JENIS_SUMBER_DANA]));
            entSumberDana.setKodeSumberDana(rs.getString(PstSumberDana.fieldNames[PstSumberDana.FLD_KODE_SUMBER_DANA]));
            entSumberDana.setJudulSumberDana(rs.getString(PstSumberDana.fieldNames[PstSumberDana.FLD_JUDUL_SUMBER_DANA]));
            entSumberDana.setTargetPendanaan(rs.getString(PstSumberDana.fieldNames[PstSumberDana.FLD_TARGET_PENDANAAN]));
            entSumberDana.setPrioritasPenggunaan(rs.getInt(PstSumberDana.fieldNames[PstSumberDana.FLD_PRIORITAS_PENGGUNAAN]));
            entSumberDana.setTotalKetersediaanDana(rs.getDouble(PstSumberDana.fieldNames[PstSumberDana.FLD_TOTAL_KETERSEDIAAN_DANA]));
            entSumberDana.setBiayaBungaKeKreditur(rs.getFloat(PstSumberDana.fieldNames[PstSumberDana.FLD_BIAYA_BUNGA_KE_KREDITUR]));
            entSumberDana.setTipeBungaKeKreditur(rs.getInt(PstSumberDana.fieldNames[PstSumberDana.FLD_TIPE_BUNGA_KE_KREDITUR]));
            entSumberDana.setTanggalDanaMasuk(rs.getDate(PstSumberDana.fieldNames[PstSumberDana.FLD_TANGGAL_DANA_MASUK]));
            entSumberDana.setTanggalLunasKeKreditur(rs.getDate(PstSumberDana.fieldNames[PstSumberDana.FLD_TANGGAL_LUNAS_KE_KREDITUR]));
            entSumberDana.setTanggalDanaMulaiTersedia(rs.getDate(PstSumberDana.fieldNames[PstSumberDana.FLD_TANGGAL_DANA_MULAI_TERSEDIA]));
            entSumberDana.setTanggalAkhirTersedia(rs.getDate(PstSumberDana.fieldNames[PstSumberDana.FLD_TANGGAL_AKHIR_TERSEDIA]));
            entSumberDana.setMinimumPinjamanKeDebitur(rs.getDouble(PstSumberDana.fieldNames[PstSumberDana.FLD_MINIMUM_PINJAMAN_KE_DEBITUR]));
            entSumberDana.setMaximumPinjamanKeDebitur(rs.getDouble(PstSumberDana.fieldNames[PstSumberDana.FLD_MAXIMUM_PINJAMAN_KE_DEBITUR]));
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
            String sql = "SELECT * FROM " + TBL_SUMBERDANA;
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
                SumberDana entSumberDana = new SumberDana();
                resultToObject(rs, entSumberDana);
                lists.add(entSumberDana);
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

    public static boolean checkOID(long entSumberDanaId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_SUMBERDANA + " WHERE "
                    + PstSumberDana.fieldNames[PstSumberDana.FLD_SUMBER_DANA_ID] + " = " + entSumberDanaId;
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
            String sql = "SELECT COUNT(" + PstSumberDana.fieldNames[PstSumberDana.FLD_SUMBER_DANA_ID] + ") FROM " + TBL_SUMBERDANA;
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
                    SumberDana entSumberDana = (SumberDana) list.get(ls);
                    if (oid == entSumberDana.getOID()) {
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
