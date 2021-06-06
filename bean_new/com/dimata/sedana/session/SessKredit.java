/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.session;

import com.dimata.aiso.entity.masterdata.anggota.PstAnggota;
import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import com.dimata.aiso.entity.masterdata.mastertabungan.PstJenisKredit;
import java.util.Vector;
import com.dimata.qdep.db.*;
import com.dimata.sedana.entity.assigncontacttabungan.PstAssignContactTabungan;
import com.dimata.sedana.entity.assigntabungan.PstAssignTabungan;
import com.dimata.sedana.entity.kredit.*;
import com.dimata.sedana.entity.masterdata.*;
import com.dimata.sedana.entity.tabungan.*;
import com.dimata.util.Formater;
import java.sql.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Dimata 007
 */
public class SessKredit {

    public static Vector listTabunganBebas(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + PstJenisSimpanan.TBL_JENISSIMPANAN + " AS js "
                    + " INNER JOIN " + PstAssignTabungan.TBL_ASSIGNTABUNGAN + " AS asstab "
                    + " ON asstab." + PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_ID_JENIS_SIMPANAN] + " = js." + PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_ID_JENIS_SIMPANAN]
                    + " INNER JOIN " + PstMasterTabungan.TBL_MASTERTABUNGAN + " AS mastab "
                    + " ON mastab." + PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_MASTER_TABUNGAN_ID] + " = asstab." + PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_MASTER_TABUNGAN]
                    + " INNER JOIN " + PstAssignContactTabungan.TBL_ASSIGNCONTACTTABUNGAN + " AS asscontab "
                    + " ON `asscontab`." + PstAssignContactTabungan.fieldNames[PstAssignContactTabungan.FLD_MASTER_TABUNGAN_ID] + " = asstab." + PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_MASTER_TABUNGAN]
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
                JenisSimpanan jenisSimpanan = new JenisSimpanan();
                PstJenisSimpanan.resultToObject(rs, jenisSimpanan);
                lists.add(jenisSimpanan);
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

    public static Vector checkJadwalTerlambat(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT jadwal.*, pinjaman.* "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " INNER JOIN " + PstPinjaman.TBL_PINJAMAN + " AS pinjaman "
                    + " ON pinjaman." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID] + " = jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID]
                    + " AND pinjaman." + PstPinjaman.fieldNames[PstPinjaman.FLD_STATUS_PINJAMAN] + " = '" + Pinjaman.STATUS_DOC_CAIR + "'"
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
                Vector data = new Vector();
                JadwalAngsuran jadwalAngsuran = new JadwalAngsuran();
                PstJadwalAngsuran.resultToObject(rs, jadwalAngsuran);
                data.add(jadwalAngsuran);
                Pinjaman pinjaman = new Pinjaman();
                PstPinjaman.resultToObject(rs, pinjaman);
                data.add(pinjaman);
                lists.add(data);
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

    public static Vector getJadwalSudahDibayar(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT angsuran.*"
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " ON angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID]
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
                Angsuran angsuran = new Angsuran();
                PstAngsuran.resultToObject(rs, angsuran);
                lists.add(angsuran);
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

    public static Vector getBiayaKredit(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT pd.* "
                    + " FROM " + PstBiayaTransaksi.TBL_BIAYA_TRANSAKSI + " AS pd "
                    + " INNER JOIN " + PstJenisTransaksi.TBL_JENISTRANSAKSI + " AS jt "
                    + " ON jt." + PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_JENIS_TRANSAKSI_ID]
                    + " = pd." + PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_ID_JENIS_TRANSAKSI]
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
                BiayaTransaksi biayaTransaksi = new BiayaTransaksi();
                PstBiayaTransaksi.resultToObject(rs, biayaTransaksi);
                lists.add(biayaTransaksi);
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

    public static double getTotalBayarSebelumTempo(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ")"
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " ON jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " INNER JOIN " + PstTransaksi.TBL_TRANSAKSI + " AS transaksi "
                    + " ON transaksi." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + " = angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_TRANSAKSI_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double count = 0;
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static int getCountJadwalDenda(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(DISTINCT(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + ")) FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN;
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

    public static int getCountListTransaksiKredit(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + ") "
                    + " FROM " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                    + " INNER JOIN " + PstPinjaman.TBL_PINJAMAN + " AS p "
                    + " ON p." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID] + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_PINJAMAN_ID]
                    + " INNER JOIN " + PstAnggota.TBL_ANGGOTA + " AS a "
                    + " ON a." + PstAnggota.fieldNames[PstAnggota.FLD_ID_ANGGOTA] + " = p." + PstPinjaman.fieldNames[PstPinjaman.FLD_ANGGOTA_ID]
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

    public static Vector getListTransaksiKredit(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                    + " INNER JOIN " + PstPinjaman.TBL_PINJAMAN + " AS p "
                    + " ON p." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID] + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_PINJAMAN_ID]
                    + " INNER JOIN " + PstAnggota.TBL_ANGGOTA + " AS a "
                    + " ON a." + PstAnggota.fieldNames[PstAnggota.FLD_ID_ANGGOTA] + " = p." + PstPinjaman.fieldNames[PstPinjaman.FLD_ANGGOTA_ID]
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
                Transaksi transaksi = new Transaksi();
                PstTransaksi.resultToObject(rs, transaksi);
                lists.add(transaksi);
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

    public static Vector getTransaksiPencairanKredit(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + PstDetailTransaksi.TBL_DETAILTRANSAKSI + " AS dt "
                    + " INNER JOIN " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                    + " ON t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + " = dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID]
                    + " INNER JOIN " + PstJenisTransaksi.TBL_JENISTRANSAKSI + " AS jt "
                    + " ON jt." + PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_JENIS_TRANSAKSI_ID] + " = dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_JENIS_TRANSAKSI_ID]
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
                DetailTransaksi detailTransaksi = new DetailTransaksi();
                PstDetailTransaksi.resultToObject(rs, detailTransaksi);
                lists.add(detailTransaksi);
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

    public static Vector getListTransaksiAngsuran(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                    + " INNER JOIN " + PstPinjaman.TBL_PINJAMAN + " AS p "
                    + " ON p." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID] + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_PINJAMAN_ID]
                    + " INNER JOIN " + PstAngsuran.TBL_ANGSURAN + " AS a "
                    + " ON a." + PstAngsuran.fieldNames[PstAngsuran.FLD_TRANSAKSI_ID] + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID]
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS ja "
                    + " ON ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = a." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
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
                Transaksi transaksi = new Transaksi();
                PstTransaksi.resultToObject(rs, transaksi);
                lists.add(transaksi);
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

    public static Vector getListSisaJadwalBunga(long idPinjaman, int jenisAngsuran) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN
                    + " WHERE " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + " AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "'"
                    + " AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " NOT IN "
                    + " ("
                    + " SELECT lunas.id FROM ("
                    + " SELECT "
                    + " jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " AS id, "
                    + " jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + " AS jumlah, "
                    + " SUM(angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS bayar "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " INNER JOIN " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " ON angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + ""
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "'"
                    + " GROUP BY angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID] + ""
                    + " HAVING bayar >= jumlah ) AS lunas "
                    + ")"
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JadwalAngsuran jadwalAngsuran = new JadwalAngsuran();
                PstJadwalAngsuran.resultToObject(rs, jadwalAngsuran);
                lists.add(jadwalAngsuran);
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

    public static Vector getListJadwalPrioritas(long idPinjaman, String year, String month) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * ,"
                    + " SUM(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS denda "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN
                    + " WHERE " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + " AND ("
                    + "" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + JadwalAngsuran.TIPE_ANGSURAN_DENDA + "'"
                    + " OR ("
                    + " YEAR(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + ") = '" + year + "'"
                    + " AND MONTH(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + ") = '" + month + "'"
                    + ")"
                    + ")"
                    + " GROUP BY " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + ", "
                    + "" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN]
                    + " ORDER BY " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " DESC "
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JadwalAngsuran jadwalAngsuran = new JadwalAngsuran();
                PstJadwalAngsuran.resultToObject(rs, jadwalAngsuran);
                lists.add(jadwalAngsuran);
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

    public static Vector getListDenda(String startDate, String endDate, int jenisAngsuran) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String start[] = startDate.split("-");
            String end[] = startDate.split("-");
            String sql = "SELECT "
                    + "" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID]
                    + "," + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN]
                    + ", SUM(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN]
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN
                    + " WHERE " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = " + jenisAngsuran
                    + " AND ("
                    + "(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_START_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "')"
                    + " OR "
                    + "(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_END_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "')"
                    + " OR "
                    + "('" + startDate + "') BETWEEN " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_START_DATE] + " AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_END_DATE]
                    + " OR "
                    + "('" + endDate + "') BETWEEN " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_START_DATE] + " AND " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_END_DATE]
                    + " OR ( "
                    + "      MONTH(TANGGAL_ANGSURAN) BETWEEN '" + start[1] + "' AND '" + end[1] + "'"
                    + "      AND YEAR(TANGGAL_ANGSURAN) BETWEEN '" + end[0] + "' AND '" + end[0] + "'"
                    + "      AND START_DATE IS NULL "
                    + "      AND END_DATE IS NULL "
                    + "    )"
                    + ")"
                    + " GROUP BY " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID]
                    + "," + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN]
                    + " ORDER BY " + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN]
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JadwalAngsuran jadwalAngsuran = new JadwalAngsuran();
                jadwalAngsuran.setPinjamanId(rs.getLong(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID]));
                jadwalAngsuran.setTanggalAngsuran(rs.getDate(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN]));
                jadwalAngsuran.setJumlahANgsuran(rs.getDouble(PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN]));
                lists.add(jadwalAngsuran);
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

    public static double getTotalAngsuran(long idPinjaman) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS total "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "' "
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double count = 0;
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static double getTotalAngsuranDibayar(long idPinjaman) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS dibayar"
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " ON jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double count = 0;
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static Vector getListAngsuranByPinjaman(long idPinjaman) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + PstAngsuran.TBL_ANGSURAN + " AS ang "
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jad "
                    + " ON jad." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = ang." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE jad." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                lists.add(null);
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

    public static String getRumusDenda(MappingDendaPinjaman mdp) {
        String tipePerhitungan[] = {"Sisa", "Full"};
        String akumulasi = true ? "Seluruh" : "";
        String perjadwal = true ? "" : JenisKredit.TIPE_VARIABEL_DENDA_TITLE[JenisKredit.TIPE_VARIABEL_DENDA_PER_JADWAL];

        String rumus = "" + mdp.getNilaiDenda() + "% dari";
        rumus += " " + tipePerhitungan[mdp.getTipePerhitunganDenda()];
        rumus += " " + akumulasi;
        rumus += " " + JenisKredit.VARIABEL_DENDA_TITLE[mdp.getVariabelDenda()];
        rumus += " " + perjadwal;

        return rumus;
    }

    public static int hapusIdSimpananWajib(long idPinjaman) throws DBException {
        int upd = 0;
        try {
            String sql = "UPDATE " + PstPinjaman.TBL_PINJAMAN
                    + " SET " + PstPinjaman.fieldNames[PstPinjaman.FLD_WAJIB_ID_JENIS_SIMPANAN] + " = NULL "
                    + " WHERE " + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + "";
            upd = DBHandler.execUpdate(sql);
            return upd;
        } catch (Exception e) {

        }
        return upd;
    }

    public static int hapusIdSimpananPencairan(long idPinjaman) throws DBException {
        int upd = 0;
        try {
            String sql = "UPDATE " + PstPinjaman.TBL_PINJAMAN
                    + " SET " + PstPinjaman.fieldNames[PstPinjaman.FLD_ASSIGN_TABUNGAN_ID] + " = NULL "
                    + ", " + PstPinjaman.fieldNames[PstPinjaman.FLD_ID_JENIS_SIMPANAN] + " = NULL "
                    + " WHERE " + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + "";
            upd = DBHandler.execUpdate(sql);
            return upd;
        } catch (Exception e) {

        }
        return upd;
    }
    
    public void updateKolektibilitasKredit(long idPinjaman) {
        try {
            Pinjaman p = PstPinjaman.fetchExc(idPinjaman);
            if (p.getStatusPinjaman() == Pinjaman.STATUS_DOC_CAIR || p.getStatusPinjaman() == Pinjaman.STATUS_DOC_PENANGANAN_MACET) {
                //cari semua jadwal angsuran
                Vector<JadwalAngsuran> listJadwal = PstJadwalAngsuran.list(0, 0, PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = " + p.getOID(), PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN]);
                for (JadwalAngsuran ja : listJadwal) {
                    try {
                        java.util.Date tglJatuhTempo = ja.getTanggalAngsuran();
                        java.util.Date tglAngsuranTerakhir = null;
                        //cari tgl angsuran terakhir
                        String where = PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + " IN ("
                                + " SELECT " + PstAngsuran.fieldNames[PstAngsuran.FLD_TRANSAKSI_ID]
                                + " FROM " + PstAngsuran.TBL_ANGSURAN
                                + " WHERE " + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = '" + ja.getOID() + "'"
                                + ")";
                        Vector<Transaksi> listTransaksi = PstTransaksi.list(0, 1, where, PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + " DESC ");
                        for (Transaksi t : listTransaksi) {
                            tglAngsuranTerakhir = t.getTanggalTransaksi();
                        }
                        
                        if (tglAngsuranTerakhir != null) {
                            if (tglAngsuranTerakhir.after(tglJatuhTempo)) {
                                //cek selisih hari bayar
                                
                            }
                        }
                        
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static Vector updateKolektibilitasKredit(Vector<Pinjaman> listPinjamanNunggak) {
        java.util.Date now = new java.util.Date();
        String dateCheck = Formater.formatDate(now, "yyy-MM-dd");
        return updateKolektibilitasKredit(listPinjamanNunggak, dateCheck, true);
    }
    
    public static Vector updateKolektibilitasKredit(Vector<Pinjaman> listPinjamanNunggak, String dateCheck, boolean update) {
        java.util.Date now = Formater.formatDate(dateCheck, "yyyy-MM-dd");
        //String dateCheck = Formater.formatDate(now, "yyy-MM-dd");
        Vector result = new Vector();
        for (int i = 0; i < listPinjamanNunggak.size(); i++) {
            try {
                Pinjaman p = (Pinjaman) listPinjamanNunggak.get(i);
                long hariTunggakanPokok = 0;
                long hariTunggakanBunga = 0;
                //jika status sudah lunas, default kolektibilitas jadi lancar
                if (listPinjamanNunggak.get(i).getStatusPinjaman() != Pinjaman.STATUS_DOC_LUNAS
                        && listPinjamanNunggak.get(i).getStatusPinjaman() != Pinjaman.STATUS_DOC_PELUNASAN_DINI
                        && listPinjamanNunggak.get(i).getStatusPinjaman() != Pinjaman.STATUS_DOC_PELUNASAN_MACET) {
                    //cari tanggal tunggakan
                    java.util.Date tglAwalTunggakanPokok = SessReportKredit.getTunggakanKredit(p.getOID(), dateCheck, JadwalAngsuran.TIPE_ANGSURAN_POKOK);
                    java.util.Date tglAwalTunggakanBunga = SessReportKredit.getTunggakanKredit(p.getOID(), dateCheck, JadwalAngsuran.TIPE_ANGSURAN_BUNGA);
                    if (tglAwalTunggakanPokok != null) {
                        java.util.Date jatuhTempoAwal = tglAwalTunggakanPokok;
                        long diff = now.getTime() - jatuhTempoAwal.getTime();
                        hariTunggakanPokok = TimeUnit.MILLISECONDS.toDays(diff);
                    }
                    if (tglAwalTunggakanBunga != null) {
                        java.util.Date jatuhTempoAwal = tglAwalTunggakanBunga;
                        long diff = now.getTime() - jatuhTempoAwal.getTime();
                        hariTunggakanBunga = TimeUnit.MILLISECONDS.toDays(diff);
                    }
                }

                //update kolektibilitas
                JenisKredit kredit = PstJenisKredit.fetch(p.getTipeKreditId());

                String whereKolektibilitas = PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_TIPEKREIDT] + " = " + kredit.getTipeFrekuensiPokokLegacy();
                String order = PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_TINGKAT_KOLEKTIBILITAS] + " ASC ";
                Vector<KolektibilitasPembayaranDetails> cekKolektibilitas = PstKolektibilitasPembayaranDetails.getJoin(0, 0, whereKolektibilitas, order);
                for (int j = 0; j < cekKolektibilitas.size(); j++) {
                    try {
                        long idKolektibilitas = cekKolektibilitas.get(j).getKolektibilitasId();
                        int maxHariPokok = cekKolektibilitas.get(j).getMaxHariTunggakanPokok();
                        int maxHariBunga = cekKolektibilitas.get(j).getMaxHariJumlahTunggakanBunga();
                        if (hariTunggakanPokok <= maxHariPokok && hariTunggakanBunga <= maxHariBunga) {
                            p.setKodeKolektibilitas(idKolektibilitas);
                            if (update) {
                                PstPinjaman.updateExc(p);
                            }
                            break;
                        }
                        if (j == (cekKolektibilitas.size() - 1)) {
                            p.setKodeKolektibilitas(idKolektibilitas);
                            if (update) {
                                PstPinjaman.updateExc(p);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                result.add(p);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    public static Vector orderHistoryKolektibilitas(Vector<Pinjaman> listPinjamanNunggak, String order) {
        Vector orderedList = new Vector();
        DBResultSet dbrs = null;
        
        try {
            String sql = "";
            for (Pinjaman p : listPinjamanNunggak) {
                KolektibilitasPembayaran k = PstKolektibilitasPembayaran.fetchExc(p.getKodeKolektibilitas());
                sql += (sql.length() > 0) ? " UNION " : "";
                sql += "SELECT " + p.getOID() + " AS " + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID]
                        + ", " + p.getKodeKolektibilitas() + " AS " + PstPinjaman.fieldNames[PstPinjaman.FLD_KODE_KOLEKTIBILITAS]
                        + ", " + p.getTipeKreditId()+ " AS " + PstPinjaman.fieldNames[PstPinjaman.FLD_TIPE_KREDIT_ID]
                        + ", " + k.getTingkatKolektibilitas() + " AS " + PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_TINGKAT_KOLEKTIBILITAS]
                        + "";
            }
            sql = "SELECT * FROM (" + sql + ") AS data_list"
                    + " JOIN aiso_pinjaman AS p "
                    + "    ON p.PINJAMAN_ID = data_list.PINJAMAN_ID "
                    + " JOIN sedana_kolektibilitas_pembayaran AS kp "
                    + "    ON kp.KOLEKTIBILITAS_ID = data_list.KODE_KOLEKTIBILITAS "
                    + " JOIN sedana_pinjaman_sumber_dana sd "
                    + "    ON sd.PINJAMAN_ID = p.PINJAMAN_ID"
                    + "";
            sql += " ORDER BY " + order;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Pinjaman p = new Pinjaman();
                p = PstPinjaman.fetchExc(rs.getLong(PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID]));
                p.setKodeKolektibilitas(rs.getLong(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_KOLEKTIBILITAS_ID]));
                p.setSumberDanaId(rs.getLong("SUMBER_DANA_ID"));
                orderedList.add(p);
            }
            rs.close();
            return orderedList;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return orderedList;
    }
}
