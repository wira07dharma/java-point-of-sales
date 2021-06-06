/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

/**
 *
 * @author Regen
 */
import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstTypeKredit extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_TYPEKREDIT = "aiso_type_kredit";
    public static final int FLD_TYPE_KREDIT_ID = 0;
    public static final int FLD_NAME_KREDIT = 1;
    public static final int FLD_MIN_KREDIT = 2;
    public static final int FLD_MAX_KREDIT = 3;
    public static final int FLD_TIPE_BUNGA = 4;
    public static final int FLD_BUNGA_MIN = 5;
    public static final int FLD_BUNGA_MAX = 6;
    public static final int FLD_BIAYA_ADMIN = 7;
    public static final int FLD_PROVISI = 8;
    public static final int FLD_DENDA = 9;
    public static final int FLD_BERLAKU_MULAI = 10;
    public static final int FLD_BERLAKU_SAMPAI = 11;
    public static final int FLD_JANGKA_WAKTU_MIN = 12;
    public static final int FLD_JANGKA_WAKTU_MAX = 13;
    public static final int FLD_KEGUNAAN = 14;
    public static final int FLD_PERIODE_PEMBAYARAN = 15;
    public static final int FLD_KATEGORI_KREDIT_ID = 16;
    //update dewok 2017-09-26
    public static final int FLD_TIPE_DENDA_BERLAKU = 17;
    public static final int FLD_TIPE_PERHITUNGAN_DENDA = 18;
    public static final int FLD_FREKUENSI_DENDA = 19;
    public static final int FLD_SATUAN_FREKUANSI_DENDA = 20;
    public static final int FLD_TIPE_FREKUENSI_POKOK = 21;

    public static String[] fieldNames = {
        "TYPE_KREDIT_ID",
        "NAME_KREDIT",
        "MIN_KREDIT",
        "MAX_KREDIT",
        "TIPE_BUNGA",
        "BUNGA_MIN",
        "BUNGA_MAX",
        "BIAYA_ADMIN",
        "PROVISI",
        "DENDA",
        "BERLAKU_MULAI",
        "BERLAKU_SAMPAI",
        "JANGKA_WAKTU_MIN",
        "JANGKA_WAKTU_MAX",
        "KEGUNAAN",
        "PERIODE_PEMBAYARAN",
        "KATEGORI_KREDIT_ID",
        "TIPE_DENDA_BERLAKU",
        "TIPE_PERHITUNGAN_DENDA",
        "FREKUENSI_DENDA",
        "SATUAN_FREKUANSI_DENDA",
        "TIPE_FREKUENSI_POKOK",
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
    };

    public PstTypeKredit() {
    }

    public PstTypeKredit(int i) throws DBException {
        super(new PstTypeKredit());
    }

    public PstTypeKredit(String sOid) throws DBException {
        super(new PstTypeKredit(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstTypeKredit(long lOid) throws DBException {
        super(new PstTypeKredit(0));
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
        return TBL_TYPEKREDIT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstTypeKredit().getClass().getName();
    }

    public static JenisKredit fetchExc(long oid) throws DBException {
        try {
            JenisKredit entTypeKredit = new JenisKredit();
            PstTypeKredit pstTypeKredit = new PstTypeKredit(oid);
            entTypeKredit.setOID(oid);
            entTypeKredit.setNamaKredit(pstTypeKredit.getString(FLD_NAME_KREDIT));
            entTypeKredit.setMinKredit(pstTypeKredit.getlong(FLD_MIN_KREDIT));
            entTypeKredit.setMaxKredit(pstTypeKredit.getlong(FLD_MAX_KREDIT));
            entTypeKredit.setTipeBunga(pstTypeKredit.getInt(FLD_TIPE_BUNGA));
            entTypeKredit.setBungaMin(pstTypeKredit.getdouble(FLD_BUNGA_MIN));
            entTypeKredit.setBungaMax(pstTypeKredit.getdouble(FLD_BUNGA_MAX));
            entTypeKredit.setBiayaAdmin(pstTypeKredit.getlong(FLD_BIAYA_ADMIN));
            entTypeKredit.setProvisi(pstTypeKredit.getlong(FLD_PROVISI));
            entTypeKredit.setDenda(pstTypeKredit.getdouble(FLD_DENDA));
            entTypeKredit.setBerlakuMulai(pstTypeKredit.getDate(FLD_BERLAKU_MULAI));
            entTypeKredit.setBerlakuSampai(pstTypeKredit.getDate(FLD_BERLAKU_SAMPAI));
            entTypeKredit.setJangkaWaktuMin(pstTypeKredit.getfloat(FLD_JANGKA_WAKTU_MIN));
            entTypeKredit.setJangkaWaktuMax(pstTypeKredit.getfloat(FLD_JANGKA_WAKTU_MAX));
            entTypeKredit.setKegunaan(pstTypeKredit.getString(FLD_KEGUNAAN));
            entTypeKredit.setPeriodePembayaran(pstTypeKredit.getInt(FLD_PERIODE_PEMBAYARAN));
            entTypeKredit.setKategoriKreditId(pstTypeKredit.getlong(FLD_KATEGORI_KREDIT_ID));
            entTypeKredit.setTipeFrekuensiPokok(pstTypeKredit.getInt(FLD_TIPE_FREKUENSI_POKOK));
            //
            entTypeKredit.setTipeDendaBerlaku(pstTypeKredit.getInt(FLD_TIPE_DENDA_BERLAKU));
            entTypeKredit.setTipePerhitunganDenda(pstTypeKredit.getInt(FLD_TIPE_PERHITUNGAN_DENDA));
            entTypeKredit.setFrekuensiDenda(pstTypeKredit.getInt(FLD_FREKUENSI_DENDA));
            entTypeKredit.setSatuanFrekuensiDenda(pstTypeKredit.getInt(FLD_SATUAN_FREKUANSI_DENDA));
            return entTypeKredit;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTypeKredit(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        JenisKredit entTypeKredit = fetchExc(entity.getOID());
        entity = (Entity) entTypeKredit;
        return entTypeKredit.getOID();
    }

    public static synchronized long updateExc(TypeKredit entTypeKredit) throws DBException {
        try {
            if (entTypeKredit.getOID() != 0) {
                PstTypeKredit pstTypeKredit = new PstTypeKredit(entTypeKredit.getOID());
                pstTypeKredit.setString(FLD_NAME_KREDIT, entTypeKredit.getNameKredit());
                pstTypeKredit.setDouble(FLD_MIN_KREDIT, entTypeKredit.getMinKredit());
                pstTypeKredit.setDouble(FLD_MAX_KREDIT, entTypeKredit.getMaxKredit());
                pstTypeKredit.setInt(FLD_TIPE_BUNGA, entTypeKredit.getTipeBunga());
                pstTypeKredit.setDouble(FLD_BUNGA_MIN, entTypeKredit.getBungaMin());
                pstTypeKredit.setDouble(FLD_BUNGA_MAX, entTypeKredit.getBungaMax());
                pstTypeKredit.setDouble(FLD_BIAYA_ADMIN, entTypeKredit.getBiayaAdmin());
                pstTypeKredit.setDouble(FLD_PROVISI, entTypeKredit.getProvisi());
                pstTypeKredit.setDouble(FLD_DENDA, entTypeKredit.getDenda());
                pstTypeKredit.setDate(FLD_BERLAKU_MULAI, entTypeKredit.getBerlakuMulai());
                pstTypeKredit.setDate(FLD_BERLAKU_SAMPAI, entTypeKredit.getBerlakuSampai());
                pstTypeKredit.setDouble(FLD_JANGKA_WAKTU_MIN, entTypeKredit.getJangkaWaktuMin());
                pstTypeKredit.setDouble(FLD_JANGKA_WAKTU_MAX, entTypeKredit.getJangkaWaktuMax());
                pstTypeKredit.setString(FLD_KEGUNAAN, entTypeKredit.getKegunaan());
                pstTypeKredit.setInt(FLD_PERIODE_PEMBAYARAN, entTypeKredit.getPeriodePembayaran());
                pstTypeKredit.setLong(FLD_KATEGORI_KREDIT_ID, entTypeKredit.getKategoriKreditId());
                pstTypeKredit.setInt(FLD_TIPE_FREKUENSI_POKOK, entTypeKredit.getTipeFrekuensiPokok());
                //
                pstTypeKredit.setInt(FLD_TIPE_DENDA_BERLAKU, entTypeKredit.getTipeDendaBerlaku());
                pstTypeKredit.setInt(FLD_TIPE_PERHITUNGAN_DENDA, entTypeKredit.getTipePerhitunganDenda());
                pstTypeKredit.setInt(FLD_FREKUENSI_DENDA, entTypeKredit.getFrekuensiDenda());
                pstTypeKredit.setInt(FLD_SATUAN_FREKUANSI_DENDA, entTypeKredit.getSatuanFrekuensiDenda());
                pstTypeKredit.update();
                return entTypeKredit.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTypeKredit(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((TypeKredit) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstTypeKredit pstTypeKredit = new PstTypeKredit(oid);
            pstTypeKredit.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTypeKredit(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(TypeKredit entTypeKredit) throws DBException {
        try {
            PstTypeKredit pstTypeKredit = new PstTypeKredit(0);
            pstTypeKredit.setString(FLD_NAME_KREDIT, entTypeKredit.getNameKredit());
            pstTypeKredit.setDouble(FLD_MIN_KREDIT, entTypeKredit.getMinKredit());
            pstTypeKredit.setDouble(FLD_MAX_KREDIT, entTypeKredit.getMaxKredit());
            pstTypeKredit.setInt(FLD_TIPE_BUNGA, entTypeKredit.getTipeBunga());
            pstTypeKredit.setDouble(FLD_BUNGA_MIN, entTypeKredit.getBungaMin());
            pstTypeKredit.setDouble(FLD_BUNGA_MAX, entTypeKredit.getBungaMax());
            pstTypeKredit.setDouble(FLD_BIAYA_ADMIN, entTypeKredit.getBiayaAdmin());
            pstTypeKredit.setDouble(FLD_PROVISI, entTypeKredit.getProvisi());
            pstTypeKredit.setDouble(FLD_DENDA, entTypeKredit.getDenda());
            pstTypeKredit.setDate(FLD_BERLAKU_MULAI, entTypeKredit.getBerlakuMulai());
            pstTypeKredit.setDate(FLD_BERLAKU_SAMPAI, entTypeKredit.getBerlakuSampai());
            pstTypeKredit.setDouble(FLD_JANGKA_WAKTU_MIN, entTypeKredit.getJangkaWaktuMin());
            pstTypeKredit.setDouble(FLD_JANGKA_WAKTU_MAX, entTypeKredit.getJangkaWaktuMax());
            pstTypeKredit.setString(FLD_KEGUNAAN, entTypeKredit.getKegunaan());
            pstTypeKredit.setInt(FLD_PERIODE_PEMBAYARAN, entTypeKredit.getPeriodePembayaran());
            pstTypeKredit.setLong(FLD_KATEGORI_KREDIT_ID, entTypeKredit.getKategoriKreditId());
            pstTypeKredit.setInt(FLD_TIPE_FREKUENSI_POKOK, entTypeKredit.getTipeFrekuensiPokok());
            //
            pstTypeKredit.setInt(FLD_TIPE_DENDA_BERLAKU, entTypeKredit.getTipeDendaBerlaku());
            pstTypeKredit.setInt(FLD_TIPE_PERHITUNGAN_DENDA, entTypeKredit.getTipePerhitunganDenda());
            pstTypeKredit.setInt(FLD_FREKUENSI_DENDA, entTypeKredit.getFrekuensiDenda());
            pstTypeKredit.setInt(FLD_SATUAN_FREKUANSI_DENDA, entTypeKredit.getSatuanFrekuensiDenda());
            pstTypeKredit.insert();
            entTypeKredit.setOID(pstTypeKredit.getlong(FLD_TYPE_KREDIT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTypeKredit(0), DBException.UNKNOWN);
        }
        return entTypeKredit.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((TypeKredit) entity);
    }

    public static void resultToObject(ResultSet rs, TypeKredit entTypeKredit) {
        try {
            entTypeKredit.setOID(rs.getLong(PstTypeKredit.fieldNames[PstTypeKredit.FLD_TYPE_KREDIT_ID]));
            entTypeKredit.setNameKredit(rs.getString(PstTypeKredit.fieldNames[PstTypeKredit.FLD_NAME_KREDIT]));
            entTypeKredit.setMinKredit(rs.getLong(PstTypeKredit.fieldNames[PstTypeKredit.FLD_MIN_KREDIT]));
            entTypeKredit.setMaxKredit(rs.getLong(PstTypeKredit.fieldNames[PstTypeKredit.FLD_MAX_KREDIT]));
            entTypeKredit.setTipeBunga(rs.getInt(PstTypeKredit.fieldNames[PstTypeKredit.FLD_TIPE_BUNGA]));
            entTypeKredit.setBungaMin(rs.getDouble(PstTypeKredit.fieldNames[PstTypeKredit.FLD_BUNGA_MIN]));
            entTypeKredit.setBungaMax(rs.getDouble(PstTypeKredit.fieldNames[PstTypeKredit.FLD_BUNGA_MAX]));
            entTypeKredit.setBiayaAdmin(rs.getLong(PstTypeKredit.fieldNames[PstTypeKredit.FLD_BIAYA_ADMIN]));
            entTypeKredit.setProvisi(rs.getLong(PstTypeKredit.fieldNames[PstTypeKredit.FLD_PROVISI]));
            entTypeKredit.setDenda(rs.getDouble(PstTypeKredit.fieldNames[PstTypeKredit.FLD_DENDA]));
            entTypeKredit.setBerlakuMulai(rs.getDate(PstTypeKredit.fieldNames[PstTypeKredit.FLD_BERLAKU_MULAI]));
            entTypeKredit.setBerlakuSampai(rs.getDate(PstTypeKredit.fieldNames[PstTypeKredit.FLD_BERLAKU_SAMPAI]));
            entTypeKredit.setJangkaWaktuMin(rs.getFloat(PstTypeKredit.fieldNames[PstTypeKredit.FLD_JANGKA_WAKTU_MIN]));
            entTypeKredit.setJangkaWaktuMax(rs.getFloat(PstTypeKredit.fieldNames[PstTypeKredit.FLD_JANGKA_WAKTU_MAX]));
            entTypeKredit.setKegunaan(rs.getString(PstTypeKredit.fieldNames[PstTypeKredit.FLD_KEGUNAAN]));
            entTypeKredit.setPeriodePembayaran(rs.getInt(PstTypeKredit.fieldNames[PstTypeKredit.FLD_PERIODE_PEMBAYARAN]));
            entTypeKredit.setKategoriKreditId(rs.getLong(PstTypeKredit.fieldNames[PstTypeKredit.FLD_KATEGORI_KREDIT_ID]));
            entTypeKredit.setTipeFrekuensiPokok(rs.getInt(PstTypeKredit.fieldNames[PstTypeKredit.FLD_TIPE_FREKUENSI_POKOK]));
            //
            entTypeKredit.setTipeDendaBerlaku(rs.getInt(PstTypeKredit.fieldNames[PstTypeKredit.FLD_TIPE_DENDA_BERLAKU]));
            entTypeKredit.setTipePerhitunganDenda(rs.getInt(PstTypeKredit.fieldNames[PstTypeKredit.FLD_TIPE_PERHITUNGAN_DENDA]));
            entTypeKredit.setFrekuensiDenda(rs.getInt(PstTypeKredit.fieldNames[PstTypeKredit.FLD_FREKUENSI_DENDA]));
            entTypeKredit.setFrekuensiDenda(rs.getInt(PstTypeKredit.fieldNames[PstTypeKredit.FLD_SATUAN_FREKUANSI_DENDA]));
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
            String sql = "SELECT * FROM " + TBL_TYPEKREDIT;
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
                TypeKredit entTypeKredit = new TypeKredit();
                resultToObject(rs, entTypeKredit);
                lists.add(entTypeKredit);
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

    public static boolean checkOID(long entTypeKreditId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_TYPEKREDIT + " WHERE "
                    + PstTypeKredit.fieldNames[PstTypeKredit.FLD_TYPE_KREDIT_ID] + " = " + entTypeKreditId;
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
            String sql = "SELECT COUNT(" + PstTypeKredit.fieldNames[PstTypeKredit.FLD_TYPE_KREDIT_ID] + ") FROM " + TBL_TYPEKREDIT;
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
                    TypeKredit entTypeKredit = (TypeKredit) list.get(ls);
                    if (oid == entTypeKredit.getOID()) {
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
