/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.session;

import com.dimata.aiso.entity.admin.PstAppUser;
import com.dimata.aiso.entity.masterdata.anggota.Anggota;
import com.dimata.aiso.entity.masterdata.anggota.PstAnggota;
import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import com.dimata.common.entity.contact.PstContactClass;
import com.dimata.common.entity.contact.PstContactClassAssign;
import java.util.Vector;
import com.dimata.qdep.db.*;
import com.dimata.sedana.entity.assignsumberdana.AssignSumberDana;
import com.dimata.sedana.entity.assignsumberdana.PstAssignSumberDana;
import com.dimata.sedana.entity.kredit.Angsuran;
import com.dimata.sedana.entity.kredit.JadwalAngsuran;
import com.dimata.sedana.entity.kredit.Pinjaman;
import com.dimata.sedana.entity.kredit.PstAngsuran;
import com.dimata.sedana.entity.kredit.PstJadwalAngsuran;
import com.dimata.sedana.entity.kredit.PstPinjaman;
import com.dimata.sedana.entity.kredit.PstPinjamanSumberDana;
import com.dimata.sedana.entity.kredit.PstTypeKredit;
import com.dimata.sedana.entity.masterdata.BiayaTransaksi;
import com.dimata.sedana.entity.masterdata.PstBiayaTransaksi;
import com.dimata.sedana.entity.masterdata.PstCashTeller;
import com.dimata.sedana.entity.masterdata.PstKolektibilitasPembayaran;
import com.dimata.sedana.entity.masterdata.PstMasterLoket;
import com.dimata.sedana.entity.sumberdana.PstSumberDana;
import com.dimata.sedana.entity.tabungan.PstDetailTransaksi;
import com.dimata.sedana.entity.tabungan.PstJenisTransaksi;
import com.dimata.sedana.entity.tabungan.PstTransaksi;
import com.dimata.sedana.entity.tabungan.Transaksi;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class SessReportKredit {

    public static Vector listPerTahun(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT YEAR(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + ") AS tahun FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN;
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
            int tahun = 0;
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                tahun = rs.getInt("tahun");
                lists.add(tahun);
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

    public static Vector listJoinPinjamanAngsuran(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT pinjaman." + PstPinjaman.fieldNames[PstPinjaman.FLD_ANGGOTA_ID] + ", jadwal.* "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal"
                    + " INNER JOIN " + PstPinjaman.TBL_PINJAMAN + " AS pinjaman "
                    + " ON pinjaman." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID] + " = jadwal." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID]
                    + " INNER JOIN " + PstAnggota.TBL_ANGGOTA + " AS contact "
                    + " ON contact." + PstAnggota.fieldNames[PstAnggota.FLD_ID_ANGGOTA] + " = pinjaman." + PstPinjaman.fieldNames[PstPinjaman.FLD_ANGGOTA_ID]
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
                JadwalAngsuran entJadwalAngsuran = new JadwalAngsuran();
                PstJadwalAngsuran.resultToObject(rs, entJadwalAngsuran);
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

    public static double getSumAngsuranHarusDibayar(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
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

    public static double getSumAngsuranDibayar(long oidJadwalAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") "
                    + " FROM " + PstAngsuran.TBL_ANGSURAN
                    + " WHERE " + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = '" + oidJadwalAngsuran + "'";
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

    public static Vector listJoinTunggakan(long oidPinjaman, int jenisAngsuran, String dateParameter) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT angsuran.*, "
                    + " SUM(angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS total_dibayar, "
                    + " jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " AS jadwal_angsuran"
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " LEFT JOIN " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " ON angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + oidPinjaman + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " <= '" + dateParameter + "' "
                    + " GROUP BY jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN]
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Angsuran entAngsuran = new Angsuran();
                PstAngsuran.resultToObject(rs, entAngsuran);
                entAngsuran.setTotalDibayar(rs.getDouble("total_dibayar"));
                entAngsuran.setTunggakanBulanAwal(rs.getDate("jadwal_angsuran"));
                lists.add(entAngsuran);
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

    public static Vector listJoinJenisTransaksiBiayaKredit(String whereClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT pd.* "
                    + " FROM " + PstBiayaTransaksi.TBL_BIAYA_TRANSAKSI + " AS pd "
                    + " INNER JOIN " + PstJenisTransaksi.TBL_JENISTRANSAKSI + " AS jt "
                    + " ON jt." + PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_JENIS_TRANSAKSI_ID] + " = pd." + PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_ID_JENIS_TRANSAKSI]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                BiayaTransaksi entBiayaTransaksi = new BiayaTransaksi();
                PstBiayaTransaksi.resultToObject(rs, entBiayaTransaksi);
                lists.add(entBiayaTransaksi);
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

    public static Date getTunggakanKredit(long idPinjaman, String tanggalCek, int jenisAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MIN(tb.TANGGAL_ANGSURAN) "
                    + " FROM "
                    + " (SELECT "
                    + " jadwal.`TANGGAL_ANGSURAN`,"
                    + " jadwal.`JADWAL_ANGSURAN_ID`,"
                    + " jadwal.`PINJAMAN_ID`,"
                    + " jadwal.`JUMLAH_ANGSURAN`,"
                    + " angsuran.`JUMLAH_ANGSURAN` AS PEMBAYARAN,"
                    + " jadwal.`JUMLAH_ANGSURAN` AS BALANCE "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " LEFT JOIN " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " ON angsuran.`JADWAL_ANGSURAN_ID` = jadwal.`JADWAL_ANGSURAN_ID` "
                    + " WHERE jadwal.`PINJAMAN_ID` = '" + idPinjaman + "' "
                    + " AND jadwal.`TANGGAL_ANGSURAN` <= '" + tanggalCek + "' "
                    + " AND jadwal.`JENIS_ANGSURAN` = '" + jenisAngsuran + "' "
                    + " AND angsuran.id_angsuran IS NULL "
                    + " UNION ALL"
                    + " SELECT"
                    + " jadwal.`TANGGAL_ANGSURAN`,"
                    + " jadwal.`JADWAL_ANGSURAN_ID`,"
                    + " jadwal.`PINJAMAN_ID`,"
                    + " jadwal.`JUMLAH_ANGSURAN`,"
                    + " SUM(angsuran.`JUMLAH_ANGSURAN`) AS PEMBAYARAN,"
                    + " ("
                    + " jadwal.`JUMLAH_ANGSURAN` - SUM(angsuran.`JUMLAH_ANGSURAN`)"
                    + " ) AS BALANCE "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " LEFT JOIN " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " ON angsuran.`JADWAL_ANGSURAN_ID` = jadwal.`JADWAL_ANGSURAN_ID`"
                    + " WHERE jadwal.`PINJAMAN_ID` = '" + idPinjaman + "' "
                    + " AND jadwal.`TANGGAL_ANGSURAN` <= '" + tanggalCek + "' "
                    + " AND jadwal.`JENIS_ANGSURAN` = '" + jenisAngsuran + "' "
                    + " AND angsuran.id_angsuran IS NOT NULL "
                    + " GROUP BY jadwal.`JADWAL_ANGSURAN_ID`) AS tb "
                    + " WHERE balance != 0 "
                    + " ORDER BY tb.TANGGAL_ANGSURAN "
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Date tanggal = null;
            while (rs.next()) {
                tanggal = rs.getDate(1);
            }
            rs.close();
            return tanggal;
        } catch (Exception e) {
            return null;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static double getTotalAngsuranDibayarByTransaksi(long idTransaksi) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS dibayar"
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " ON jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE angsuran." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TRANSAKSI_ID] + " = '" + idTransaksi + "'"
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

    public static double getTotalAngsuranDibayar(long idPinjaman, int jenisAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS dibayar"
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " ON jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "'";
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

    public static double getTotalAngsuran(long idPinjaman, int jenisAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS total "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "' "
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

    public static double getTotalAngsuranPerTanggalCek(long idPinjaman, int jenisAngsuran, String tglCek) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS jumlah "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " <= '" + tglCek + "' "
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

    public static double getTotalAngsuranPerBulanCek(long idPinjaman, int jenisAngsuran, String tglCek) {
        DBResultSet dbrs = null;
        try {
            String split[] = tglCek.split("-");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.MONTH, Integer.valueOf(split[1]));
            cal.set(Calendar.YEAR, Integer.valueOf(split[0]));
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date d = cal.getTime();
            tglCek = Formater.formatDate(d, "yyyy-MM-dd");

            String sql = "SELECT SUM(jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS jumlah "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "' "
                    + " AND DATE(" + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + ") <= '" + tglCek + "'"
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

    public static Date getTanggalAwalAngsuran(long idPinjaman) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MIN(jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + ") "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "' "
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Date tanggal = null;
            while (rs.next()) {
                tanggal = rs.getDate(1);
            }
            rs.close();
            return tanggal;
        } catch (Exception e) {
            return null;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static Vector listJoinJenisKreditBySumberDana(String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " tk." + PstTypeKredit.fieldNames[PstTypeKredit.FLD_TYPE_KREDIT_ID]
                    + ", tk." + PstTypeKredit.fieldNames[PstTypeKredit.FLD_NAME_KREDIT]
                    + ", sd." + PstSumberDana.fieldNames[PstSumberDana.FLD_SUMBER_DANA_ID]
                    + ", sd." + PstSumberDana.fieldNames[PstSumberDana.FLD_JUDUL_SUMBER_DANA]
                    + ", ass." + PstAssignSumberDana.fieldNames[PstAssignSumberDana.FLD_ASSIGN_SUMBER_DANA_JENIS_KREDIT_ID]
                    + " FROM " + PstTypeKredit.TBL_TYPEKREDIT + " AS tk "
                    + " INNER JOIN " + PstAssignSumberDana.TBL_ASSIGNSUMBERDANA + " AS ass "
                    + " ON ass." + PstAssignSumberDana.fieldNames[PstAssignSumberDana.FLD_TYPE_KREDIT_ID] + " = tk." + PstTypeKredit.fieldNames[PstTypeKredit.FLD_TYPE_KREDIT_ID]
                    + " INNER JOIN " + PstSumberDana.TBL_SUMBERDANA + " AS sd "
                    + " ON sd." + PstSumberDana.fieldNames[PstSumberDana.FLD_SUMBER_DANA_ID] + " = ass." + PstAssignSumberDana.fieldNames[PstAssignSumberDana.FLD_SUMBER_DANA_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                AssignSumberDana asd = new AssignSumberDana();
                PstAssignSumberDana.resultToObject(rs, asd);
                lists.add(asd);
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

    public static Vector listJoinPinjamanKolektibilitas(String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + PstPinjaman.TBL_PINJAMAN + " AS p "
                    + " INNER JOIN " + PstKolektibilitasPembayaran.TBL_KOLEKTIBILITASPEMBAYARAN + " AS kp "
                    + " ON kp." + PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_KOLEKTIBILITAS_ID]
                    + " = p." + PstPinjaman.fieldNames[PstPinjaman.FLD_KODE_KOLEKTIBILITAS]
                    + " INNER JOIN " + PstPinjamanSumberDana.TBL_PINJAMANSUMBERDANA + " AS sd "
                    + " ON sd." + PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_PINJAMAN_ID]
                    + " = p." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID]
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS ja "
                    + " ON ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID]
                    + " = p." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Pinjaman p = new Pinjaman();
                PstPinjaman.resultToObject(rs, p);
                p.setSumberDanaId(rs.getLong(PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_SUMBER_DANA_ID]));
                lists.add(p);
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

    public static Vector listJoinPinjamanRangkumanKolektibilitas(String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT p.* "
                    + " FROM " + PstPinjaman.TBL_PINJAMAN + " AS p "
                    + " INNER JOIN " + PstAnggota.TBL_ANGGOTA + " AS cl "
                    + " ON cl." + PstAnggota.fieldNames[PstAnggota.FLD_ID_ANGGOTA] + " = p." + PstPinjaman.fieldNames[PstPinjaman.FLD_ANGGOTA_ID]
                    + " INNER JOIN " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN + " AS cca "
                    + " ON cca." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] + " = cl." + PstAnggota.fieldNames[PstAnggota.FLD_ID_ANGGOTA]
                    + " INNER JOIN " + PstContactClass.TBL_CONTACT_CLASS + " AS cc "
                    + " ON cc." + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + " = cca." + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]
                    + " INNER JOIN " + PstPinjamanSumberDana.TBL_PINJAMANSUMBERDANA + " AS psd "
                    + " ON psd." + PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_PINJAMAN_ID] + " = p." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID]
                    + " INNER JOIN " + PstSumberDana.TBL_SUMBERDANA + " AS sd "
                    + " ON sd." + PstSumberDana.fieldNames[PstSumberDana.FLD_SUMBER_DANA_ID] + " = psd." + PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_SUMBER_DANA_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Pinjaman p = new Pinjaman();
                PstPinjaman.resultToObject(rs, p);
                lists.add(p);
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

    public static Vector listKreditPerShift(String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                    + " INNER JOIN " + PstPinjaman.TBL_PINJAMAN + " AS p "
                    + " ON p." + PstPinjaman.fieldNames[PstPinjaman.FLD_PINJAMAN_ID] + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_PINJAMAN_ID]
                    + " INNER JOIN " + PstCashTeller.TBL_CASH_TELLER + " AS ct "
                    + " ON ct." + PstCashTeller.fieldNames[PstCashTeller.FLD_TELLER_SHIFT_ID] + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TELLER_SHIFT_ID]
                    + " INNER JOIN " + PstMasterLoket.TBL_SEDANA_MASTER_LOKET + " AS ml "
                    + " ON ml." + PstMasterLoket.fieldNames[PstMasterLoket.FLD_MASTER_LOKET_ID] + " = ct." + PstCashTeller.fieldNames[PstCashTeller.FLD_MASTER_LOKET_ID]
                    //                    + " INNER JOIN " + PstAppUser.TBL_APP_USER + " AS au "
                    //                    + " ON au." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " = ct." + PstCashTeller.fieldNames[PstCashTeller.FLD_APP_USER_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Transaksi t = new Transaksi();
                PstTransaksi.resultToObject(rs, t);
                lists.add(t);
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

    public static double getTotalNilaiTransaksi(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + " SUM(dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_KREDIT] + ") AS kredit,"
                    + " SUM(dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_DEBET] + ") AS debet,"
                    + " SUM(dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_KREDIT] + ") "
                    + " - SUM(dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_DEBET] + ") AS nilai "
                    + " FROM " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                    + " INNER JOIN " + PstDetailTransaksi.TBL_DETAILTRANSAKSI + " AS dt "
                    + " ON dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID]
                    + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double count = 0;
            while (rs.next()) {
                count = rs.getInt(3);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static double getTotalAngsuranPerJadwal(long idPinjaman, int jenisAngsuran, String jadwalAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS total "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " = '" + jadwalAngsuran + "' "
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

    public static double getTotalAngsuranDibayarPerJadwal(long idPinjaman, int jenisAngsuran, String jadwalAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS dibayar"
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " ON jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "'"
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " = '" + jadwalAngsuran + "'";
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

    public static int getCountAngsuranDibayarPerJadwal(long idPinjaman, int jenisAngsuran, String jadwalAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_ID_ANGSURAN] + ")"
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " ON jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "'"
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " = '" + jadwalAngsuran + "'";
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

    public static double getTotalAngsuranDariJadwalPertama(long idPinjaman, int jenisAngsuran, String jadwalAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS total "
                    + " FROM " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "' "
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " <= '" + jadwalAngsuran + "' "
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

    public static double getTotalAngsuranDibayarDariJadwalPertama(long idPinjaman, int jenisAngsuran, String jadwalAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") AS dibayar"
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS angsuran "
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS jadwal "
                    + " ON jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = angsuran." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "'"
                    + " AND jadwal." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " <= '" + jadwalAngsuran + "'";
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

    public static Vector listSisaPinjaman(String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + "  p.*, "
                    + "  c.*, "
                    + "  angsuran.TOTAL_ANGSURAN - angsuran.TOTAL_DIBAYAR AS SISA_ANGSURAN "
                    + " FROM "
                    + "  aiso_pinjaman AS p "
                    + "  JOIN contact_list AS c "
                    + "  ON c.CONTACT_ID = p.ANGGOTA_ID"
                    + "  JOIN "
                    + "    (SELECT "
                    + "      j.PINJAMAN_ID, "
                    + "      j.JENIS_ANGSURAN, "
                    + "      SUM(j.JUMLAH_ANGSURAN) AS TOTAL_ANGSURAN, "
                    + "      COALESCE(SUM(a.JUMLAH_ANGSURAN), 0) AS TOTAL_DIBAYAR "
                    + "    FROM "
                    + "      sedana_jadwal_angsuran AS j "
                    + "      LEFT JOIN aiso_angsuran AS a "
                    + "        ON a.JADWAL_ANGSURAN_ID = j.JADWAL_ANGSURAN_ID "
                    + "    GROUP BY j.PINJAMAN_ID) AS angsuran "
                    + "    ON angsuran.PINJAMAN_ID = p.PINJAMAN_ID"
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector data = new Vector();
                Pinjaman p = new Pinjaman();
                PstPinjaman.resultToObject(rs, p);
                data.add(p);
                Anggota a = new Anggota();
                a.setName(rs.getString(PstAnggota.fieldNames[PstAnggota.FLD_NAME]));
                data.add(a);
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

    public static int getCountAngsuranDibayarTerlambatPerJadwal(long idPinjaman, int jenisAngsuran, String jadwalAngsuran) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + ")"
                    + " FROM " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                    + " INNER JOIN " + PstAngsuran.TBL_ANGSURAN + " AS a "
                    + " ON a." + PstAngsuran.fieldNames[PstAngsuran.FLD_TRANSAKSI_ID] + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID]
                    + " INNER JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS ja "
                    + " ON ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = a." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "'"
                    + " AND ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "'"
                    + " AND ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + " = '" + jadwalAngsuran + "'"
                    + " AND DATE(t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TANGGAL_TRANSAKSI] + ") > '" + jadwalAngsuran + "'";
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

    public static double getTotalAngsuranDibayarPerTanggalCek(long idPinjaman, int jenisAngsuran, String tglCek) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(a." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") "
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS a"
                    + " JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS ja "
                    + " ON ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = a." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " JOIN " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                    + " ON t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + " = a." + PstAngsuran.fieldNames[PstAngsuran.FLD_TRANSAKSI_ID]
                    + " WHERE ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "' "
                    + " AND ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "' "
                    + " AND DATE(t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TANGGAL_TRANSAKSI] + ") <= '" + tglCek + "' "
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

    public static double getTotalAngsuranDibayarPerJadwalCek(long idPinjaman, int jenisAngsuran, String tglCek) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(a." + PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN] + ") "
                    + " FROM " + PstAngsuran.TBL_ANGSURAN + " AS a"
                    + " JOIN " + PstJadwalAngsuran.TBL_JADWALANGSURAN + " AS ja "
                    + " ON ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JADWAL_ANGSURAN_ID] + " = a." + PstAngsuran.fieldNames[PstAngsuran.FLD_JADWAL_ANGSURAN_ID]
                    + " WHERE ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_PINJAMAN_ID] + " = '" + idPinjaman + "' "
                    + " AND ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_JENIS_ANGSURAN] + " = '" + jenisAngsuran + "' "
                    + " AND DATE(ja." + PstJadwalAngsuran.fieldNames[PstJadwalAngsuran.FLD_TANGGAL_ANGSURAN] + ") <= '" + tglCek + "' "
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

    public static ArrayList listRiwayatAngsuranKredit(String nomorKredit) {
        DBResultSet dbrs = null;
        try {
            String sql = ""
                    + "SELECT "
                    + "  p.NO_KREDIT, "
                    + "  cl.PERSON_NAME, "
                    + "  p.JUMLAH_PINJAMAN, "
                    + "  cl.HOME_ADDRESS, "
                    + "  p.JATUH_TEMPO, "
                    + "  p.TGL_LUNAS, "
                    + "  p.SUKU_BUNGA, "
                    + "  p.JANGKA_WAKTU, "
                    + "  k.TIPE_FREKUENSI_POKOK, "
                    + "  t.KODE_BUKTI_TRANSAKSI, "
                    + "  t.TANGGAL_TRANSAKSI, "
                    + "  ROUND( "
                    + "    SUM(all_angsuran.ANGSURAN_POKOK), "
                    + "    2 "
                    + "  ) AS ANGSURAN_POKOK, "
                    + "  ROUND( "
                    + "    SUM(all_angsuran.ANGSURAN_BUNGA), "
                    + "    2 "
                    + "  ) AS ANGSURAN_BUNGA, "
                    + "  t.KETERANGAN "
                    + "FROM "
                    + "  (SELECT "
                    + "    t.TRANSAKSI_ID, "
                    + "    j.JENIS_ANGSURAN, "
                    + "    SUM(a.JUMLAH_ANGSURAN) AS ANGSURAN_POKOK, "
                    + "    0 AS ANGSURAN_BUNGA "
                    + "  FROM "
                    + "    sedana_transaksi t "
                    + "    JOIN aiso_angsuran a "
                    + "      ON a.TRANSAKSI_ID = t.TRANSAKSI_ID "
                    + "    JOIN sedana_jadwal_angsuran j "
                    + "      ON j.JADWAL_ANGSURAN_ID = a.JADWAL_ANGSURAN_ID "
                    + "    JOIN aiso_pinjaman p "
                    + "      ON p.PINJAMAN_ID = t.PINJAMAN_ID "
                    + "  WHERE t.USECASE_TYPE = '" + Transaksi.USECASE_TYPE_KREDIT_ANGSURAN_PAYMENT + "' "
                    + "    AND j.JENIS_ANGSURAN = '" + JadwalAngsuran.TIPE_ANGSURAN_POKOK + "' "
                    + "    AND p.NO_KREDIT = '" + nomorKredit + "' "
                    + "  GROUP BY t.TRANSAKSI_ID "
                    + "  UNION "
                    + "  SELECT "
                    + "    t.TRANSAKSI_ID, "
                    + "    j.JENIS_ANGSURAN, "
                    + "    0 AS ANGSURAN_POKOK, "
                    + "    SUM(a.JUMLAH_ANGSURAN) AS ANGSURAN_BUNGA "
                    + "  FROM "
                    + "    sedana_transaksi t "
                    + "    JOIN aiso_angsuran a "
                    + "      ON a.TRANSAKSI_ID = t.TRANSAKSI_ID "
                    + "    JOIN sedana_jadwal_angsuran j "
                    + "      ON j.JADWAL_ANGSURAN_ID = a.JADWAL_ANGSURAN_ID "
                    + "    JOIN aiso_pinjaman p "
                    + "      ON p.PINJAMAN_ID = t.PINJAMAN_ID "
                    + "  WHERE t.USECASE_TYPE = '" + Transaksi.USECASE_TYPE_KREDIT_ANGSURAN_PAYMENT + "' "
                    + "    AND j.JENIS_ANGSURAN = '" + JadwalAngsuran.TIPE_ANGSURAN_BUNGA + "' "
                    + "    AND p.NO_KREDIT = '" + nomorKredit + "' "
                    + "  GROUP BY t.TRANSAKSI_ID) AS all_angsuran "
                    + "  JOIN sedana_transaksi t "
                    + "    ON t.TRANSAKSI_ID = all_angsuran.TRANSAKSI_ID "
                    + "  JOIN aiso_pinjaman p "
                    + "    ON p.PINJAMAN_ID = t.PINJAMAN_ID "
                    + "  JOIN contact_list cl "
                    + "    ON cl.CONTACT_ID = p.ANGGOTA_ID "
                    + "  JOIN aiso_type_kredit k "
                    + "    ON k.TYPE_KREDIT_ID = p.TIPE_KREDIT_ID "
                    + "GROUP BY t.TRANSAKSI_ID "
                    + "ORDER BY t.TANGGAL_TRANSAKSI";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            ArrayList lists = new ArrayList();
            while (rs.next()) {
                ArrayList data = new ArrayList();
                Pinjaman p = new Pinjaman();
                p.setNoKredit(rs.getString("NO_KREDIT"));
                p.setJumlahPinjaman(rs.getDouble("JUMLAH_PINJAMAN"));
                p.setJatuhTempo(rs.getDate("JATUH_TEMPO"));
                p.setTglLunas(rs.getDate("TGL_LUNAS"));
                p.setSukuBunga(rs.getDouble("SUKU_BUNGA"));
                p.setJangkaWaktu(rs.getInt("JANGKA_WAKTU"));
                p.setJumlahAngsuran(rs.getDouble("ANGSURAN_POKOK"));
                data.add(p);

                Anggota a = new Anggota();
                a.setName(rs.getString("PERSON_NAME"));
                a.setAddressPermanent(rs.getString("HOME_ADDRESS"));
                data.add(a);

                Transaksi t = new Transaksi();
                t.setKodeBuktiTransaksi(rs.getString("KODE_BUKTI_TRANSAKSI"));
                t.setTanggalTransaksi(rs.getTimestamp("TANGGAL_TRANSAKSI"));
                t.setKeterangan(rs.getString("KETERANGAN"));
                data.add(t);
                
                Angsuran angsuran = new Angsuran();
                angsuran.setJumlahAngsuran(rs.getDouble("ANGSURAN_BUNGA"));
                data.add(angsuran);
                
                JenisKredit k = new JenisKredit();
                k.setTipeFrekuensiPokok(rs.getInt("TIPE_FREKUENSI_POKOK"));
                data.add(k);
                
                lists.add(data);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList();
    }
}
