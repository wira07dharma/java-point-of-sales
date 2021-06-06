/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.session;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import java.sql.ResultSet;
import java.util.Vector;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Dimata 007
 */
public class SessHistory {

    // * Master Bumdesa
    //   - Nasabah
    //   - Status
    public static final int DOC_MASTER_PENDIDIKAN = 4;
    public static final int DOC_MASTER_PEKERJAAN = 6;
    public static final int DOC_MASTER_POSISI = 5;
    //   - Master
    public static final int DOC_MASTER_LOKET = 9;
    public static final int DOC_MASTER_SHIFT = 7;
    public static final int DOC_RELEVANT_DOC_GROUP = 8;
    public static final int DOC_MASTER_JENIS_TRANSAKSI = 12;
    //    - Master Dokumen
    public static final int DOC_JENIS_DOKUMEN = 0;
    public static final int DOC_MASTER_DOKUMEN = 11;
    // * Master Tabungan
    public static final int DOC_JENIS_SIMPANAN = 1;
    public static final int DOC_MASTER_AFILIASI = 2;
    public static final int DOC_MASTER_TABUNGAN = 3;
    // * Master Kredit
    public static final int DOC_MASTER_JENIS_KREDIT = 15;
    public static final int DOC_MASTER_SUMBER_DANA = 16;
    public static final int DOC_MASTER_KOLEKTIBILITAS_PEMBAYARAN = 10;
    public static final int DOC_MASTER_PENJAMIN_KREDIT = 17;
    // * Transaksi
    //   - Tabungan
    public static final int DOC_TRANSAKSI_TABUNGAN = 13;
    //   - Kredit
    public static final int DOC_TRANSAKSI_KREDIT = 14;
    
    public static String[] document = {
        "Master Pendidikan",//4
        "Master Pekerjaan",//6
        "Master Posisi",//5
        "Master Loket",//9
        "Master Shift",//7
        "Master Kelompok Dokumen Terkait",//8
        "Master Jenis Transaksi",//12
        "Master Jenis Dokumen",//0
        "Master Dokumen",//11
        "Master Jenis Item",//1
        "Master Afiliasi",//2
        "Master Tabungan",//3
        "Master Jenis Kredit",//15
        "Master Sumber Dana",//16
        "Master Kolektibilitas Pembayaran",//10
        "Master Penjamin Kredit",//17
        "Transaksi Tabungan",//13
        "Transaksi Kredit",//14
    };

    public void saveHistory(long userID, String userName, String userAction, long documentId, String documentNumber, String documentType, String aplicationName, String url, String allHistory) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(userName);
            logSysHistory.setLogUserAction(userAction);
            logSysHistory.setLogDocumentId(documentId);
            logSysHistory.setLogDocumentNumber(documentNumber);
            logSysHistory.setLogDocumentType(documentType);
            logSysHistory.setLogApplication(aplicationName);
            logSysHistory.setLogOpenUrl(url);
            logSysHistory.setLogDetail(allHistory);
            logSysHistory.setLogUpdateDate(new Date());
            long oidHistory = PstLogSysHistory.insertLog(logSysHistory);
        } catch (Exception e) {
        }
    }

    public static Vector listHistory(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + PstLogSysHistory.TBL_LOG_HISTORY;
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
                LogSysHistory entPinjaman = new LogSysHistory();
                PstLogSysHistory.resultToObject(rs, entPinjaman);
                lists.add(entPinjaman);
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

}
