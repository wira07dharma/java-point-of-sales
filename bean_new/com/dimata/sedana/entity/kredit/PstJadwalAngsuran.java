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
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.sedana.common.I_Sedana;
import static com.dimata.sedana.entity.kredit.PstAngsuran.TBL_ANGSURAN;
import com.dimata.sedana.entity.tabungan.PstTransaksi;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class PstJadwalAngsuran extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_JADWALANGSURAN = "sedana_jadwal_angsuran";
    public static final int FLD_JADWAL_ANGSURAN_ID = 0;
    public static final int FLD_PINJAMAN_ID = 1;
    public static final int FLD_TANGGAL_ANGSURAN = 2;
    public static final int FLD_JENIS_ANGSURAN = 3;
    public static final int FLD_JUMLAH_ANGSURAN = 4;
    //update dewok 2017-09-26
    public static final int FLD_TRANSAKSI_ID = 5;
    public static final int FLD_PARENT_JADWAL_ANGSURAN_ID = 6;
    public static final int FLD_START_DATE = 7;
    public static final int FLD_END_DATE = 8;
    public static final int FLD_JUMLAH_ANGSURAN_SEHARUSNYA = 9;
    public static final int FLD_SISA = 10;
    public static final int FLD_NO_KWITANSI = 11;
    public static final int FLD_STATUS_CETAK = 12;

    public static String[] fieldNames = {
        "JADWAL_ANGSURAN_ID",
        "PINJAMAN_ID",
        "TANGGAL_ANGSURAN",
        "JENIS_ANGSURAN",
        "JUMLAH_ANGSURAN",
        "TRANSAKSI_ID",
        "PARENT_JADWAL_ANGSURAN_ID",
        "START_DATE",
        "END_DATE",
        "JUMLAH_ANGSURAN_SEHARUSNYA",
        "SISA",
        "NO_KWITANSI",
        "STATUS_CETAK"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT
    };

    public static int JENIS_ANGSURAN_POKOK = I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_POKOK;
    public static int JENIS_ANGSURAN_BUNGA = I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_BUNGA;
    public static int JENIS_ANGSURAN_DENDA = I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_DENDA;

    public PstJadwalAngsuran() {
    }

    public PstJadwalAngsuran(int i) throws DBException {
        super(new PstJadwalAngsuran());
    }

    public PstJadwalAngsuran(String sOid) throws DBException {
        super(new PstJadwalAngsuran(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstJadwalAngsuran(long lOid) throws DBException {
        super(new PstJadwalAngsuran(0));
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
        return TBL_JADWALANGSURAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstJadwalAngsuran().getClass().getName();
    }

    public static JadwalAngsuran fetchExc(long oid) throws DBException {
        try {
            JadwalAngsuran entJadwalAngsuran = new JadwalAngsuran();
            PstJadwalAngsuran pstJadwalAngsuran = new PstJadwalAngsuran(oid);
            entJadwalAngsuran.setOID(oid);
            entJadwalAngsuran.setPinjamanId(pstJadwalAngsuran.getlong(FLD_PINJAMAN_ID));
            entJadwalAngsuran.setTanggalAngsuran(pstJadwalAngsuran.getDate(FLD_TANGGAL_ANGSURAN));
            entJadwalAngsuran.setJenisAngsuran(pstJadwalAngsuran.getInt(FLD_JENIS_ANGSURAN));
            entJadwalAngsuran.setJumlahANgsuran(pstJadwalAngsuran.getdouble(FLD_JUMLAH_ANGSURAN));
            //
            entJadwalAngsuran.setTransaksiId(pstJadwalAngsuran.getlong(FLD_TRANSAKSI_ID));
            entJadwalAngsuran.setParentJadwalAngsuranId(pstJadwalAngsuran.getlong(FLD_PARENT_JADWAL_ANGSURAN_ID));
            entJadwalAngsuran.setStartDate(pstJadwalAngsuran.getDate(FLD_START_DATE));
            entJadwalAngsuran.setEndDate(pstJadwalAngsuran.getDate(FLD_END_DATE));
            entJadwalAngsuran.setJumlahAngsuranSeharusnya(pstJadwalAngsuran.getdouble(FLD_JUMLAH_ANGSURAN_SEHARUSNYA));
            entJadwalAngsuran.setSisa(pstJadwalAngsuran.getdouble(FLD_SISA));
            entJadwalAngsuran.setNoKwitansi(pstJadwalAngsuran.getString(FLD_NO_KWITANSI));
            entJadwalAngsuran.setStatusCetak(FLD_STATUS_CETAK);

            return entJadwalAngsuran;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJadwalAngsuran(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        JadwalAngsuran entJadwalAngsuran = fetchExc(entity.getOID());
        entity = (Entity) entJadwalAngsuran;
        return entJadwalAngsuran.getOID();
    }

    public static synchronized long updateExc(JadwalAngsuran entJadwalAngsuran) throws DBException {
        try {
            if (entJadwalAngsuran.getOID() != 0) {
                PstJadwalAngsuran pstJadwalAngsuran = new PstJadwalAngsuran(entJadwalAngsuran.getOID());
                pstJadwalAngsuran.setLong(FLD_PINJAMAN_ID, entJadwalAngsuran.getPinjamanId());
                pstJadwalAngsuran.setDate(FLD_TANGGAL_ANGSURAN, entJadwalAngsuran.getTanggalAngsuran());
                pstJadwalAngsuran.setInt(FLD_JENIS_ANGSURAN, entJadwalAngsuran.getJenisAngsuran());
                pstJadwalAngsuran.setDouble(FLD_JUMLAH_ANGSURAN, entJadwalAngsuran.getJumlahANgsuran());
                //
                if (entJadwalAngsuran.getTransaksiId() != 0) {
                    pstJadwalAngsuran.setLong(FLD_TRANSAKSI_ID, entJadwalAngsuran.getTransaksiId());
                }
                if (entJadwalAngsuran.getParentJadwalAngsuranId() != 0) {
                    pstJadwalAngsuran.setLong(FLD_PARENT_JADWAL_ANGSURAN_ID, entJadwalAngsuran.getParentJadwalAngsuranId());
                }
                pstJadwalAngsuran.setDate(FLD_START_DATE, entJadwalAngsuran.getStartDate());
                pstJadwalAngsuran.setDate(FLD_END_DATE, entJadwalAngsuran.getEndDate());
                pstJadwalAngsuran.setDouble(FLD_JUMLAH_ANGSURAN_SEHARUSNYA, entJadwalAngsuran.getJumlahAngsuranSeharusnya());
                pstJadwalAngsuran.setDouble(FLD_SISA, entJadwalAngsuran.getSisa());
                pstJadwalAngsuran.setString(FLD_NO_KWITANSI, entJadwalAngsuran.getNoKwitansi());
                pstJadwalAngsuran.setInt(FLD_STATUS_CETAK, entJadwalAngsuran.getStatusCetak());
                pstJadwalAngsuran.update();
                return entJadwalAngsuran.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJadwalAngsuran(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((JadwalAngsuran) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstJadwalAngsuran pstJadwalAngsuran = new PstJadwalAngsuran(oid);
            pstJadwalAngsuran.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJadwalAngsuran(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(JadwalAngsuran entJadwalAngsuran) throws DBException {
        try {
            PstJadwalAngsuran pstJadwalAngsuran = new PstJadwalAngsuran(0);
            pstJadwalAngsuran.setLong(FLD_PINJAMAN_ID, entJadwalAngsuran.getPinjamanId());
            pstJadwalAngsuran.setDate(FLD_TANGGAL_ANGSURAN, entJadwalAngsuran.getTanggalAngsuran());
            pstJadwalAngsuran.setInt(FLD_JENIS_ANGSURAN, entJadwalAngsuran.getJenisAngsuran());
            pstJadwalAngsuran.setDouble(FLD_JUMLAH_ANGSURAN, entJadwalAngsuran.getJumlahANgsuran());
            //
            if (entJadwalAngsuran.getTransaksiId() != 0) {
                pstJadwalAngsuran.setLong(FLD_TRANSAKSI_ID, entJadwalAngsuran.getTransaksiId());
            }
            if (entJadwalAngsuran.getParentJadwalAngsuranId() != 0) {
                pstJadwalAngsuran.setLong(FLD_PARENT_JADWAL_ANGSURAN_ID, entJadwalAngsuran.getParentJadwalAngsuranId());
            }
            pstJadwalAngsuran.setDate(FLD_START_DATE, entJadwalAngsuran.getStartDate());
            pstJadwalAngsuran.setDate(FLD_END_DATE, entJadwalAngsuran.getEndDate());
            pstJadwalAngsuran.setDouble(FLD_JUMLAH_ANGSURAN_SEHARUSNYA, entJadwalAngsuran.getJumlahAngsuranSeharusnya());
            pstJadwalAngsuran.setDouble(FLD_SISA, entJadwalAngsuran.getSisa());
            pstJadwalAngsuran.setString(FLD_NO_KWITANSI, entJadwalAngsuran.getNoKwitansi());
            pstJadwalAngsuran.setInt(FLD_STATUS_CETAK, entJadwalAngsuran.getStatusCetak());
            pstJadwalAngsuran.insert();
            entJadwalAngsuran.setOID(pstJadwalAngsuran.getlong(FLD_JADWAL_ANGSURAN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJadwalAngsuran(0), DBException.UNKNOWN);
        }
        return entJadwalAngsuran.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((JadwalAngsuran) entity);
    }

    public static void resultToObject(ResultSet rs, JadwalAngsuran entJadwalAngsuran) {
        try {
            entJadwalAngsuran.setOID(rs.getLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID]));
            entJadwalAngsuran.setPinjamanId(rs.getLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID]));
            entJadwalAngsuran.setTanggalAngsuran(rs.getDate(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN]));
            entJadwalAngsuran.setJenisAngsuran(rs.getInt(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN]));
            entJadwalAngsuran.setJumlahANgsuran(rs.getDouble(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN]));
            //
            entJadwalAngsuran.setTransaksiId(rs.getLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TRANSAKSI_ID]));
            entJadwalAngsuran.setParentJadwalAngsuranId(rs.getLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PARENT_JADWAL_ANGSURAN_ID]));
            entJadwalAngsuran.setStartDate(rs.getDate(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_START_DATE]));
            entJadwalAngsuran.setEndDate(rs.getDate(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_END_DATE]));
            entJadwalAngsuran.setJumlahAngsuranSeharusnya(rs.getDouble(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN_SEHARUSNYA]));
            entJadwalAngsuran.setSisa(rs.getDouble(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_SISA]));
            entJadwalAngsuran.setNoKwitansi(rs.getString(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_NO_KWITANSI]));
            entJadwalAngsuran.setStatusCetak(rs.getInt(fieldNames[FLD_STATUS_CETAK]));
        } catch (Exception e) {
        }
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        return list(limitStart, recordToGet, whereClause, order, "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order, String group) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_JADWALANGSURAN;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (group != null && group.length() > 0) {
                sql = sql + " GROUP BY " + group;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JadwalAngsuran entJadwalAngsuran = new JadwalAngsuran();
                resultToObject(rs, entJadwalAngsuran);
                lists.add(entJadwalAngsuran);
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

    public static Vector listJoinPinjaman(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_JADWALANGSURAN + " SJA "
                    + " INNER JOIN " + PstPinjaman.TBL_PINJAMAN + " AP "
                    + " ON SJA." + fieldNames[FLD_PINJAMAN_ID]
                    + " = AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID];
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
                JadwalAngsuran entJadwalAngsuran = new JadwalAngsuran();
                resultToObject(rs, entJadwalAngsuran);
                lists.add(entJadwalAngsuran);
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

    public static Vector listDenganBunga(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + fieldNames[FLD_JADWAL_ANGSURAN_ID] + ", "
                    + fieldNames[FLD_PINJAMAN_ID] + ", "
                    + fieldNames[FLD_TANGGAL_ANGSURAN] + ", "
                    + fieldNames[FLD_NO_KWITANSI] + ", "
                    + " SUM(" + fieldNames[FLD_JUMLAH_ANGSURAN] + ") AS ANGSURAN, "
                    + "	SUM(" + fieldNames[FLD_SISA] + ") AS SISA"
                    + " FROM " + TBL_JADWALANGSURAN;
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
            sql += " GROUP BY " + fieldNames[FLD_TANGGAL_ANGSURAN] + " ASC";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector array = new Vector();
                Date tglAngsuran = rs.getDate(fieldNames[FLD_TANGGAL_ANGSURAN]);
                long pinjamanId = rs.getLong(fieldNames[FLD_PINJAMAN_ID]);

                array.add(rs.getLong(fieldNames[FLD_JADWAL_ANGSURAN_ID])); //0
                array.add(pinjamanId); //1
                array.add(tglAngsuran); //2
                array.add(rs.getString(fieldNames[FLD_NO_KWITANSI])); //3
                array.add(rs.getString("ANGSURAN")); //4
                array.add(rs.getString("SISA")); //5

                lists.add(array);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static boolean checkOID(long entJadwalAngsuranId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_JADWALANGSURAN + " WHERE "
                    + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = " + entJadwalAngsuranId;
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

    public static int getCountJoinAngsuran(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + ")"
                    + " FROM " + TBL_JADWALANGSURAN + " AS jadwal"
                    + " LEFT JOIN " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " ON angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = jadwal." + fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + "";
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

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + ") FROM " + TBL_JADWALANGSURAN;
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
                    JadwalAngsuran entJadwalAngsuran = (JadwalAngsuran) list.get(ls);
                    if (oid == entJadwalAngsuran.getOID()) {
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

    public static Vector getSumAngsuranDenganBunga(String whereClause) {
        DBResultSet dbrs = null;
        Vector lists = new Vector();
        try {
            String sql = "SELECT SUM(JA." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS JUMLAH_ANGSURAN"
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS JA "
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JadwalAngsuran ja = new JadwalAngsuran();
                ja.setJumlahANgsuran(rs.getDouble(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN]));
                resultToObject(rs, ja);
                lists.add(ja);
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

    public static double getTotalBungaByPinjamanId(long pinjamanId) {
        double n = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(`" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + "`) "
                    + "FROM sedana_jadwal_angsuran j "
                    + "WHERE JENIS_ANGSURAN = " + JadwalAngsuran.TIPE_ANGSURAN_BUNGA + " "
                    + "AND PINJAMAN_ID = " + pinjamanId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                n = rs.getDouble(1);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PstJadwalAngsuran.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DBException ex) {
            Logger.getLogger(PstJadwalAngsuran.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBResultSet.close(dbrs);
        }

        return n;
    }

    public static int updateIdTransaksiJadwalAngsuranKredit(long idPinjaman, long idTransaksiPencairan) throws DBException {
        int upd = 0;
        try {
            String sql = "UPDATE " + TBL_JADWALANGSURAN
                    + " SET " + fieldNames[FLD_TRANSAKSI_ID] + " = NULL "
                    + " WHERE " + fieldNames[FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + " AND " + fieldNames[FLD_TRANSAKSI_ID] + " = '" + idTransaksiPencairan + "'"
                    + "";
            upd = DBHandler.execUpdate(sql);
            return upd;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return upd;
    }

    public static Vector<JadwalAngsuran> getNoPaymentScheduleByNoKredit(String noKredit) {
        return getNoPaymentScheduleByNoKredit(noKredit, null);
    }

    public static Vector<JadwalAngsuran> getNoPaymentScheduleByNoKredit(String noKredit, String additionalCondition) {
        Vector<JadwalAngsuran> vAngsuran = new Vector<JadwalAngsuran>();
        DBResultSet dbrs = null;
        String sql = "SELECT a.* "
                + "FROM `sedana_jadwal_angsuran` a "
                + "JOIN `aiso_pinjaman` p USING(`PINJAMAN_ID`) "
                + "LEFT JOIN `aiso_angsuran` aa USING (`JADWAL_ANGSURAN_ID`) "
                + "WHERE p.`NO_KREDIT`='" + noKredit + "' AND (a.JUMLAH_ANGSURAN-COALESCE(aa.JUMLAH_ANGSURAN, 0)) > 0 "
                + (additionalCondition != null && !additionalCondition.equals("") ? "AND " + additionalCondition + " " : "")
                + "GROUP BY a.JADWAL_ANGSURAN_ID "
                + "ORDER BY `JENIS_ANGSURAN` ASC, TANGGAL_ANGSURAN ASC ";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JadwalAngsuran angsuran = new JadwalAngsuran();
                resultToObject(rs, angsuran);
                vAngsuran.add(angsuran);
            }
            rs.close();
        } catch (Exception e) {

        }
        return vAngsuran;
    }

    public static Vector getListAngsuranWithBunga(long pinjamanId) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        String sql = "SELECT "
                + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + ", "
                + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + ", "
                + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + ", "
                + " SUM(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS JUMLAH_ANGSURAN, "
                + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TRANSAKSI_ID] + ", "
                + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_SISA] + ", "
                + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN_SEHARUSNYA] + ""
                + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + ""
                + " WHERE " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = " + pinjamanId
                + " AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " IN (" + I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_POKOK + ", " + I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_BUNGA + ")"
                + " GROUP BY " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN];
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JadwalAngsuran angsuran = new JadwalAngsuran();
                angsuran.setOID(rs.getLong("JADWAL_ANGSURAN_ID"));
                angsuran.setPinjamanId(rs.getLong("PINJAMAN_ID"));
                angsuran.setTanggalAngsuran(rs.getDate("TANGGAL_ANGSURAN"));
                angsuran.setJumlahANgsuran(rs.getDouble("JUMLAH_ANGSURAN"));
                angsuran.setTransaksiId(rs.getLong("TRANSAKSI_ID"));
                angsuran.setSisa(rs.getDouble("SISA"));
                angsuran.setJumlahAngsuranSeharusnya(rs.getDouble("JUMLAH_ANGSURAN_SEHARUSNYA"));
                list.add(angsuran);
            }
            rs.close();
        } catch (Exception e) {

        }
        return list;
    }

    public static int getCountAngsuranWithBunga(long pinjamanId) {
        int result = 0;
        DBResultSet dbrs = null;
        String sql = "SELECT COUNT("
                + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + ") AS RESULT "
                + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + ""
                + " WHERE " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = " + pinjamanId
                + " AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " IN (" + I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_POKOK + ", " + I_Sedana.TIPE_DOC_KREDIT_NASABAH_POS_PIUTANG_BUNGA + ")"
                + " GROUP BY " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN];
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result++;
            }
            rs.close();
        } catch (Exception e) {

        }
        return result;
    }

    public static double getJumlahAngsuranWithBunga(long pinjamanId, String tglAngsuran) {
        double jumlah = 0;
        DBResultSet dbrs = null;
        String sql = "SELECT "
                + " SUM(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS JUMLAH_ANGSURAN "
                + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + ""
                + " WHERE " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = " + pinjamanId
                + " AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " = '" + tglAngsuran + "'"
                + " AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " IN ("
                + JadwalAngsuran.TIPE_ANGSURAN_POKOK + ", " + JadwalAngsuran.TIPE_ANGSURAN_BUNGA
                + ") GROUP BY " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN];
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                jumlah = rs.getDouble(1);
            }
            rs.close();
        } catch (Exception e) {
        }
        return jumlah;
    }

    public static Vector getAngsuranWithBunga(long pinjamanId, String tglAngsuran) {
        return getAngsuranWithBunga(pinjamanId, tglAngsuran, "");
    }

    public static Vector getAngsuranWithBunga(long pinjamanId, String tglAngsuran, String order) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        String sql = " SELECT *"
                + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + ""
                + " WHERE " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = " + pinjamanId
                + " AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " IN ("
                + JadwalAngsuran.TIPE_ANGSURAN_POKOK + ", " + JadwalAngsuran.TIPE_ANGSURAN_BUNGA
                + ") AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " = '" + tglAngsuran + "'";
        if (order.length() > 0 && order != null) {
            sql += " ORDER BY " + order;
        }
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JadwalAngsuran angsuran = new JadwalAngsuran();
                resultToObject(rs, angsuran);
                list.add(angsuran);
            }
            rs.close();
        } catch (Exception e) {

        }
        return list;
    }

    public static double getRemainingAngsuranByJadwalAngsuranId(long jadwalAngsuranId) {
        double n = 0;
        DBResultSet dbrs = null;
        String sql = "SELECT (a.`JUMLAH_ANGSURAN` - COALESCE(SUM(p.`JUMLAH_ANGSURAN`), 0)) "
                + "FROM `sedana_jadwal_angsuran` a "
                + "LEFT JOIN `aiso_angsuran` p USING(`JADWAL_ANGSURAN_ID`) "
                + "WHERE JADWAL_ANGSURAN_ID = " + jadwalAngsuranId + " "
                + "GROUP BY `JADWAL_ANGSURAN_ID` ";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                n = rs.getDouble(1);
            }
            rs.close();
        } catch (Exception e) {

        }
        return n;
    }

    public static double getRemainingAngsuranByPinjamanId(long pinjamanId) {
        double n = 0;
        DBResultSet dbrs = null;
        String sql = "SELECT SUM( COALESCE(ja.`JUMLAH_ANGSURAN`,0) - COALESCE(aa.`JUMLAH_ANGSURAN`,0) ) "
                + "FROM `sedana_jadwal_angsuran` ja "
                + "LEFT JOIN `aiso_angsuran` aa USING(`JADWAL_ANGSURAN_ID`) "
                + "WHERE ja.PINJAMAN_ID = '" + pinjamanId + "'"
                + "AND ja.JENIS_ANGSURAN = '4'";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                n = rs.getDouble(1);
            }
            rs.close();
        } catch (Exception e) {

        }
        return n;
    }

    public static float getPaymentPercentageByNoKredit(String noKredit) {
        float d = 0;
        DBResultSet dbrs = null;
        String sql = "SELECT SUM(aa.`JUMLAH_ANGSURAN`)/SUM(a.`JUMLAH_ANGSURAN`)*100 FROM `sedana_jadwal_angsuran` a "
                + "JOIN `aiso_pinjaman` p USING(`PINJAMAN_ID`) "
                + "LEFT JOIN `aiso_angsuran` aa USING (`JADWAL_ANGSURAN_ID`) "
                + "WHERE a.`JENIS_ANGSURAN`=" + JadwalAngsuran.TIPE_ANGSURAN_POKOK + " AND p.`NO_KREDIT`='" + noKredit + "'";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                d = rs.getFloat(1);
            }
            rs.close();
        } catch (Exception e) {

        }
        return d;
    }

    public static Vector<Long> getUnpaidIdJadwal(long pinjamanId, int jenisAngusran) {
        Vector v = new Vector();

        Vector<JadwalAngsuran> as = PstJadwalAngsuran.list(0, 0, fieldNames[FLD_PINJAMAN_ID] + "=" + pinjamanId + " AND " + fieldNames[FLD_JENIS_ANGSURAN] + "=" + jenisAngusran, "");
        for (JadwalAngsuran a : as) {
            if (getRemainingAngsuranByJadwalAngsuranId(a.getOID()) > 0) {
                v.add(a.getOID());
            }
        }
        return v;
    }

    public static ArrayList listAngsuranDibayar(String whereClause, String group) {
        ArrayList listData = new ArrayList();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SJA.* "
                    + " FROM " + TBL_ANGSURAN + " AA"
                    + " INNER JOIN " + PstTransaksi.TBL_TRANSAKSI + " ST"
                    + " ON AA.TRANSAKSI_ID = ST.TRANSAKSI_ID"
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " SJA"
                    + " ON AA.JADWAL_ANGSURAN_ID = SJA.JADWAL_ANGSURAN_ID"
                    + " INNER JOIN " + PstPinjaman.TBL_PINJAMAN + " AP "
                    + " ON ST.PINJAMAN_ID = AP.PINJAMAN_ID ";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (group != null && group.length() > 0) {
                sql += " GROUP BY " + group;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JadwalAngsuran jadwalAngsuran = new JadwalAngsuran();

                PstJadwalAngsuran.resultToObject(rs, jadwalAngsuran);

                listData.add(jadwalAngsuran);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return listData;
    }

    public static JSONObject fetchJSON(long oid) {
        JSONObject object = new JSONObject();
        try {
            JadwalAngsuran jadwalAngsuran = PstJadwalAngsuran.fetchExc(oid);
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID], jadwalAngsuran.getOID());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID], jadwalAngsuran.getPinjamanId());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN], jadwalAngsuran.getTanggalAngsuran());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN], jadwalAngsuran.getJenisAngsuran());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN], jadwalAngsuran.getJumlahANgsuran());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TRANSAKSI_ID], jadwalAngsuran.getTransaksiId());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PARENT_JADWAL_ANGSURAN_ID], jadwalAngsuran.getParentJadwalAngsuranId());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_START_DATE], jadwalAngsuran.getStartDate());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_END_DATE], jadwalAngsuran.getEndDate());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN_SEHARUSNYA], jadwalAngsuran.getJumlahAngsuranSeharusnya());
            object.put(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_SISA], jadwalAngsuran.getSisa());
        } catch (Exception exc) {
        }
        return object;
    }

    public static long syncExc(JSONObject jSONObject) {
        long oid = 0;
        if (jSONObject != null) {
            oid = jSONObject.optLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID], 0);
            if (oid > 0) {
                JadwalAngsuran jadwalAngsuran = new JadwalAngsuran();
                jadwalAngsuran.setOID(jSONObject.optLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID], 0));
                jadwalAngsuran.setPinjamanId(jSONObject.optLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID], 0));
                jadwalAngsuran.setTanggalAngsuran(Formater.formatDate(jSONObject.optString(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN], "0000-00-00"), "yyyy-MM-dd"));
                jadwalAngsuran.setJenisAngsuran(jSONObject.optInt(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN], 0));
                jadwalAngsuran.setJumlahANgsuran(jSONObject.optDouble(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN], 0));
                jadwalAngsuran.setTransaksiId(jSONObject.optLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TRANSAKSI_ID], 0));
                jadwalAngsuran.setParentJadwalAngsuranId(jSONObject.optLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PARENT_JADWAL_ANGSURAN_ID], 0));
                jadwalAngsuran.setStartDate(Formater.formatDate(jSONObject.optString(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_START_DATE], "0000-00-00"), "yyyy-MM-dd"));
                jadwalAngsuran.setEndDate(Formater.formatDate(jSONObject.optString(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_END_DATE], "0000-00-00"), "yyyy-MM-dd"));
                jadwalAngsuran.setJumlahAngsuranSeharusnya(jSONObject.optDouble(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN_SEHARUSNYA], 0));
                jadwalAngsuran.setSisa(jSONObject.optDouble(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_SISA], 0));
                boolean checkOidJadwalAngsuran = PstJadwalAngsuran.checkOID(oid);
                try {
                    if (checkOidJadwalAngsuran) {
                        PstJadwalAngsuran.updateExc(jadwalAngsuran);
                    } else {
                        PstJadwalAngsuran.insertByOid(jadwalAngsuran);
                    }
                } catch (Exception exc) {
                }
            }
        }
        return oid;
    }

    public static long insertByOid(JadwalAngsuran jadwalAngsuran) throws DBException {
        try {
            PstJadwalAngsuran pstJadwalAngsuran = new PstJadwalAngsuran(0);
            pstJadwalAngsuran.setLong(FLD_JADWAL_ANGSURAN_ID, jadwalAngsuran.getOID());
            pstJadwalAngsuran.setLong(FLD_PINJAMAN_ID, jadwalAngsuran.getPinjamanId());
            pstJadwalAngsuran.setDate(FLD_TANGGAL_ANGSURAN, jadwalAngsuran.getTanggalAngsuran());
            pstJadwalAngsuran.setInt(FLD_JENIS_ANGSURAN, jadwalAngsuran.getJenisAngsuran());
            pstJadwalAngsuran.setDouble(FLD_JUMLAH_ANGSURAN, jadwalAngsuran.getJumlahANgsuran());
            pstJadwalAngsuran.setLong(FLD_TRANSAKSI_ID, jadwalAngsuran.getTransaksiId());
            pstJadwalAngsuran.setLong(FLD_PARENT_JADWAL_ANGSURAN_ID, jadwalAngsuran.getParentJadwalAngsuranId());
            pstJadwalAngsuran.setDate(FLD_START_DATE, jadwalAngsuran.getStartDate());
            pstJadwalAngsuran.setDate(FLD_END_DATE, jadwalAngsuran.getEndDate());
            pstJadwalAngsuran.setDouble(FLD_JUMLAH_ANGSURAN_SEHARUSNYA, jadwalAngsuran.getJumlahAngsuranSeharusnya());
            pstJadwalAngsuran.setDouble(FLD_SISA, jadwalAngsuran.getSisa());
            pstJadwalAngsuran.insertByOid(jadwalAngsuran.getOID());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJadwalAngsuran(0), DBException.UNKNOWN);
        }
        return jadwalAngsuran.getOID();
    }

}
