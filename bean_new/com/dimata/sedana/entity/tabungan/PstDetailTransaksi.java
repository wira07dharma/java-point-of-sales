/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.tabungan;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.sedana.entity.assigncontacttabungan.PstAssignContactTabungan;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PstDetailTransaksi extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_DETAILTRANSAKSI = "sedana_detail_transaksi";
    public static final int FLD_TRANSAKSI_DETAIL_ID = 0;
    public static final int FLD_JENIS_TRANSAKSI_ID = 1;
    public static final int FLD_DEBET = 2;
    public static final int FLD_KREDIT = 3;
    public static final int FLD_ID_SIMPANAN = 4;
    public static final int FLD_TGL_PRINT_TERAKHIR = 5;
    public static final int FLD_TRANSAKSI_ID = 6;
    public static final int FLD_DETAIL_INFO = 7;

    public static String[] fieldNames = {
        "TRANSAKSI_DETAIL_ID",
        "JENIS_TRANSAKSI_ID",
        "DEBET",
        "KREDIT",
        "ID_SIMPANAN",
        "TGL_PRINT_TERAKHIR",
        "TRANSAKSI_ID",
        "DETAIL_INFO"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING,};

    public PstDetailTransaksi() {
    }

    public PstDetailTransaksi(int i) throws DBException {
        super(new PstDetailTransaksi());
    }

    public PstDetailTransaksi(String sOid) throws DBException {
        super(new PstDetailTransaksi(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstDetailTransaksi(long lOid) throws DBException {
        super(new PstDetailTransaksi(0));
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
        return TBL_DETAILTRANSAKSI;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDetailTransaksi().getClass().getName();
    }

    public static DetailTransaksi fetchExc(long oid) throws DBException {
        try {
            DetailTransaksi entDetailTransaksi = new DetailTransaksi();
            PstDetailTransaksi pstDetailTransaksi = new PstDetailTransaksi(oid);
            entDetailTransaksi.setOID(oid);
            entDetailTransaksi.setJenisTransaksiId(pstDetailTransaksi.getLong(FLD_JENIS_TRANSAKSI_ID));
            entDetailTransaksi.setTransaksiId(pstDetailTransaksi.getLong(FLD_TRANSAKSI_ID));
            entDetailTransaksi.setDebet(pstDetailTransaksi.getdouble(FLD_DEBET));
            entDetailTransaksi.setKredit(pstDetailTransaksi.getdouble(FLD_KREDIT));
            entDetailTransaksi.setIdSimpanan(pstDetailTransaksi.getLong(FLD_ID_SIMPANAN));
            entDetailTransaksi.setTglPrintTerakhir(pstDetailTransaksi.getDate(FLD_TGL_PRINT_TERAKHIR));
            entDetailTransaksi.setDetailInfo(pstDetailTransaksi.getString(FLD_DETAIL_INFO));
            return entDetailTransaksi;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDetailTransaksi(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        DetailTransaksi entDetailTransaksi = fetchExc(entity.getOID());
        entity = (Entity) entDetailTransaksi;
        return entDetailTransaksi.getOID();
    }

    public static synchronized long updateExc(DetailTransaksi entDetailTransaksi) throws DBException {
        try {
            if (entDetailTransaksi.getOID() != 0) {
                PstDetailTransaksi pstDetailTransaksi = new PstDetailTransaksi(entDetailTransaksi.getOID());
                pstDetailTransaksi.setLong(FLD_JENIS_TRANSAKSI_ID, entDetailTransaksi.getJenisTransaksiId());
                pstDetailTransaksi.setLong(FLD_TRANSAKSI_ID, entDetailTransaksi.getTransaksiId());
                pstDetailTransaksi.setDouble(FLD_DEBET, entDetailTransaksi.getDebet());
                pstDetailTransaksi.setDouble(FLD_KREDIT, entDetailTransaksi.getKredit());
                pstDetailTransaksi.setLong(FLD_ID_SIMPANAN, entDetailTransaksi.getIdSimpanan());
                pstDetailTransaksi.setDate(FLD_TGL_PRINT_TERAKHIR, entDetailTransaksi.getTglPrintTerakhir());
                pstDetailTransaksi.setString(FLD_DETAIL_INFO, entDetailTransaksi.getDetailInfo());
                pstDetailTransaksi.update();
                return entDetailTransaksi.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDetailTransaksi(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((DetailTransaksi) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstDetailTransaksi pstDetailTransaksi = new PstDetailTransaksi(oid);
            pstDetailTransaksi.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDetailTransaksi(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(DetailTransaksi entDetailTransaksi) throws DBException {
        try {
            PstDetailTransaksi pstDetailTransaksi = new PstDetailTransaksi(0);
            pstDetailTransaksi.setLong(FLD_JENIS_TRANSAKSI_ID, entDetailTransaksi.getJenisTransaksiId());
            pstDetailTransaksi.setLong(FLD_TRANSAKSI_ID, entDetailTransaksi.getTransaksiId());
            pstDetailTransaksi.setDouble(FLD_DEBET, entDetailTransaksi.getDebet());
            pstDetailTransaksi.setDouble(FLD_KREDIT, entDetailTransaksi.getKredit());
            if (entDetailTransaksi.getIdSimpanan() != 0) {
                pstDetailTransaksi.setLong(FLD_ID_SIMPANAN, entDetailTransaksi.getIdSimpanan());
            }
            pstDetailTransaksi.setDate(FLD_TGL_PRINT_TERAKHIR, entDetailTransaksi.getTglPrintTerakhir());
            pstDetailTransaksi.setString(FLD_DETAIL_INFO, entDetailTransaksi.getDetailInfo());
            pstDetailTransaksi.insert();
            entDetailTransaksi.setOID(pstDetailTransaksi.getlong(FLD_TRANSAKSI_DETAIL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDetailTransaksi(0), DBException.UNKNOWN);
        }
        return entDetailTransaksi.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((DetailTransaksi) entity);
    }

    public static void resultToObject(ResultSet rs, DetailTransaksi entDetailTransaksi) {
        try {
            entDetailTransaksi.setOID(rs.getLong(PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_DETAIL_ID]));
            entDetailTransaksi.setJenisTransaksiId(rs.getLong(PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_JENIS_TRANSAKSI_ID]));
            entDetailTransaksi.setTransaksiId(rs.getLong(PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID]));
            entDetailTransaksi.setDebet(rs.getDouble(PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_DEBET]));
            entDetailTransaksi.setKredit(rs.getDouble(PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_KREDIT]));
            entDetailTransaksi.setIdSimpanan(rs.getLong(PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_ID_SIMPANAN]));
            entDetailTransaksi.setTglPrintTerakhir(rs.getDate(PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TGL_PRINT_TERAKHIR]));
            entDetailTransaksi.setDetailInfo(rs.getString(PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_DETAIL_INFO]));
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
            String sql = "SELECT * FROM " + TBL_DETAILTRANSAKSI;
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
                DetailTransaksi entDetailTransaksi = new DetailTransaksi();
                resultToObject(rs, entDetailTransaksi);
                lists.add(entDetailTransaksi);
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

    public long getTotalSaldo(long transaksiId) {
        return getTotalSaldo(transaksiId, "");
    }

    public long getTotalSaldo(long transaksiId, String idSimpanan) {
        String whereDataTabungan = "";
        if (!idSimpanan.equals("")) {
            Vector<DataTabungan> dt = PstDataTabungan.list(0, 0, PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_SIMPANAN] + " IN (" + idSimpanan + ")", "");
            for (DataTabungan d : dt) {
                if (!whereDataTabungan.equals("")) {
                    whereDataTabungan += ", ";
                }
                whereDataTabungan += d.getOID();
            }

            if (!whereDataTabungan.equals("")) {
                whereDataTabungan = " AND " + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_SIMPANAN] + " IN (" + whereDataTabungan + ")";
            }
        }

        long total = 0;
        String sql = "SELECT SUM(t.`KREDIT`-t.`DEBET`) total FROM `sedana_detail_transaksi` t WHERE t.`TRANSAKSI_ID`=" + transaksiId + whereDataTabungan;

        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                total = rs.getLong("total");
                break;
            }
        } catch (DBException ex) {
            Logger.getLogger(PstDetailTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PstDetailTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }

        return total;
    }

    public static boolean checkOID(long entDetailTransaksiId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_DETAILTRANSAKSI + " WHERE "
                    + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID] + " = " + entDetailTransaksiId;
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
            String sql = "SELECT COUNT(" + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID] + ") FROM " + TBL_DETAILTRANSAKSI;
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

    public static double getCountDebetKredit(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT"
                    + " SUM(detail." + fieldNames[FLD_DEBET] + ") AS debet,"
                    + " SUM(detail." + fieldNames[FLD_KREDIT] + ") AS kredit,"
                    + " SUM(detail." + fieldNames[FLD_KREDIT] + ") - SUM(detail." + fieldNames[FLD_DEBET] + ") AS saldo"
                    + " FROM " + TBL_DETAILTRANSAKSI + " AS detail "
                    + " INNER JOIN " + PstDataTabungan.TBL_DATA_TABUNGAN + " AS datatab"
                    + " ON datatab." + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_SIMPANAN] + " = detail." + fieldNames[FLD_ID_SIMPANAN]
                    + " INNER JOIN " + PstAssignContactTabungan.TBL_ASSIGNCONTACTTABUNGAN + " AS contab"
                    + " ON contab." + PstAssignContactTabungan.fieldNames[PstAssignContactTabungan.FLD_ASSIGN_TABUNGAN_ID] + " = datatab." + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ASSIGN_TABUNGAN_ID]
                    + " INNER JOIN " + PstJenisSimpanan.TBL_JENISSIMPANAN + " AS jenis "
                    + " ON jenis." + PstJenisSimpanan.fieldNames[PstJenisSimpanan.FLD_ID_JENIS_SIMPANAN] + " = datatab." + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_JENIS_SIMPANAN]
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double debet = 0;
            double kredit = 0;
            double saldo = 0;
            while (rs.next()) {
                debet = rs.getDouble(1);
                kredit = rs.getDouble(2);
            }
            saldo = kredit - debet;
            rs.close();
            return saldo;
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
                    DetailTransaksi entDetailTransaksi = (DetailTransaksi) list.get(ls);
                    if (oid == entDetailTransaksi.getOID()) {
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

    public long getSaldoTabungan(long idSimpanan) {
        long saldo = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return saldo;
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

    public static double getSimpananSaldo(long contactId, long assignContactTabunganId, long jenisSimpananId) {
        double saldo = 0;
        DBResultSet dbrs = null;
        try {
            DataTabungan dtab = PstDataTabungan.fetchWhere(PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_ANGGOTA] + "=" + contactId + " AND " + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ASSIGN_TABUNGAN_ID] + "=" + assignContactTabunganId + " AND " + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_JENIS_SIMPANAN] + "=" + jenisSimpananId);
            if (dtab.getOID() != 0) {
                Calendar now = Calendar.getInstance();
                saldo = getLastSaldoOfTheMonth(dtab.getOID(), now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR));
            }
        } catch (Exception e) {

        } finally {
            DBResultSet.close(dbrs);
        }

        return saldo;
    }

    public static long getSimpananSaldo(long dataTabunganId) {
        long saldo = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + fieldNames[FLD_KREDIT] + "-" + fieldNames[FLD_DEBET] + ") AS val FROM " + TBL_DETAILTRANSAKSI + " "
                    + "WHERE " + fieldNames[FLD_ID_SIMPANAN] + "=" + dataTabunganId + " ";
            ;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                saldo = rs.getLong(1);
                break;
            }
            rs.close();
        } catch (Exception e) {

        } finally {
            DBResultSet.close(dbrs);
        }

        return saldo;
    }

    public static String queryGetTransaksiByJenisSimpanan(Vector<Long> js) {
        String x = "";
        for (Long j : js) {
            if (!x.equals("")) {
                x += ", ";
            }
            x += j;
        }
        String u = "SELECT DISTINCT TRANSAKSI_ID FROM `aiso_jenis_simpanan` j JOIN `aiso_data_tabungan` USING (ID_JENIS_SIMPANAN) JOIN `sedana_detail_transaksi` USING (`ID_SIMPANAN`) WHERE j.`ID_JENIS_SIMPANAN` IN (" + x + ")";
        return u;
    }

    public static String queryGetTransaksiByJenisTransaksi(Vector<Long> js) {
        String x = "";
        for (Long j : js) {
            if (!x.equals("")) {
                x += ", ";
            }
            x += j;
        }
        String u = "SELECT DISTINCT TRANSAKSI_ID FROM `sedana_jenis_transaksi` j JOIN `sedana_detail_transaksi` USING (JENIS_TRANSAKSI_ID) WHERE j.`JENIS_TRANSAKSI_ID` IN (" + x + ")";
        return u;
    }

    public static int countDetailTransaksiByTransaksidAndSimpananId(long idJenisTransaksi, String year, String month, long simpananId) {
        int n = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(0) FROM `sedana_transaksi` t "
                    + "JOIN `sedana_detail_transaksi` d USING(TRANSAKSI_ID) "
                    + "WHERE YEAR(t.`TANGGAL_TRANSAKSI`) = " + year + " AND MONTH(t.`TANGGAL_TRANSAKSI`) = " + month + " "
                    + "AND d.`JENIS_TRANSAKSI_ID` = " + idJenisTransaksi + " "
                    + "AND d.`ID_SIMPANAN` = " + simpananId;
            ;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                n = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return n;
    }

    public static double getLastSaldoOfTheMonth(long idSimpanan, int month, int year) {
        //added by dewok 2019-01-02, parameter tanggal harus ada
        int day = 1;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        day = cal.get(Calendar.DAY_OF_MONTH);

        return getSaldoByDate(idSimpanan, day, month, year);
    }

    public static double getSaldoByDate(long idSimpanan, int day, int month, int year) {
        double n = 0;
        DBResultSet dbrs = null;
        String sql = "SELECT SUM(KREDIT-DEBET) FROM sedana_detail_transaksi dt "
                + "JOIN sedana_transaksi t USING (TRANSAKSI_ID) "
                + "WHERE `ID_SIMPANAN`=" + idSimpanan + " "
                + "AND DATE(TANGGAL_TRANSAKSI) <= '" + year + "-" + month + "-" + day + "'"
                + "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                n = rs.getDouble(1);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (DBException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return n;
    }

    public static HashMap<Integer, Double> getSaldoSummaryPerDayOfTheMonth(long idSimpanan, int month, int year) {
        HashMap<Integer, Double> n = new HashMap<Integer, Double>();
        DBResultSet dbrs = null;
        String sql = "SELECT DAY(TANGGAL_TRANSAKSI), SUM(KREDIT-DEBET) FROM sedana_detail_transaksi dt "
                + "JOIN sedana_transaksi t USING (TRANSAKSI_ID) "
                + "WHERE `ID_SIMPANAN`=" + idSimpanan + " "
                + "AND MONTH(TANGGAL_TRANSAKSI)=" + month + " "
                + "AND YEAR(TANGGAL_TRANSAKSI)=" + year + " "
                + "GROUP BY dt.TRANSAKSI_DETAIL_ID "
                + "ORDER BY t.TANGGAL_TRANSAKSI"
                + "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                n.put(rs.getInt(1), rs.getDouble(2));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (DBException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return n;
    }

    public static List<Double> getListMutasiPerPeriode(long idSimpanan, int month, int year) {
        List<Double> n = new ArrayList();
        DBResultSet dbrs = null;
        String sql = "SELECT DAY(TANGGAL_TRANSAKSI), SUM(KREDIT-DEBET) FROM sedana_detail_transaksi dt "
                + "JOIN sedana_transaksi t USING (TRANSAKSI_ID) "
                + "WHERE `ID_SIMPANAN`=" + idSimpanan + " "
                + "AND MONTH(TANGGAL_TRANSAKSI)=" + month + " "
                + "AND YEAR(TANGGAL_TRANSAKSI)=" + year + " "
                + "GROUP BY dt.TRANSAKSI_DETAIL_ID "
                + "ORDER BY t.TANGGAL_TRANSAKSI"
                + "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                n.add(rs.getDouble(2));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (DBException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return n;
    }

    public static double getSaldoOfTheMonth(long idSimpanan, int month, int year) {
        double n = 0;
        DBResultSet dbrs = null;
        String sql = "SELECT SUM(KREDIT-DEBET) FROM sedana_detail_transaksi dt "
                + "JOIN sedana_transaksi t USING (TRANSAKSI_ID) "
                + "WHERE `ID_SIMPANAN`=" + idSimpanan + " "
                + "AND YEAR(TANGGAL_TRANSAKSI) = '" + year + "' "
                + "AND MONTH(TANGGAL_TRANSAKSI) = '" + month + "' ";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                n = rs.getDouble(1);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (DBException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return n;
    }

    public static double getPreviousBunga(long idSimpanan, long oidJenisTransaksi, int month, int year) {
        double n = 0;
        DBResultSet dbrs = null;
        if (month == 1) {
            month = 12;
            year = year - 1;
        } else {
            month = month - 1;
        }

        //updated by dewok 2019-01-02 pencarian harus lengkap dengan tanggal
        int day = 1;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        day = cal.get(Calendar.DAY_OF_MONTH);

        String sql = "SELECT SUM(KREDIT-DEBET) FROM sedana_detail_transaksi dt "
                + "JOIN sedana_transaksi t USING (TRANSAKSI_ID) "
                + "WHERE `ID_SIMPANAN`=" + idSimpanan + " "
                + "AND `JENIS_TRANSAKSI_ID`=" + oidJenisTransaksi + " "
                + " AND DATE(TANGGAL_TRANSAKSI) <= '" + year + "-" + month + "-" + day + "'"
                + "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                n = rs.getDouble(1);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (DBException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return n;
    }

    public static java.util.Date getLastCalculationDateByTransaksiAndSimpanan(long idJenisTransaksi, int year, int month, long simpananId) {
        java.util.Date dt = null;
        int n = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT t.`TANGGAL_TRANSAKSI` FROM `sedana_transaksi` t "
                    + "JOIN `sedana_detail_transaksi` d USING(TRANSAKSI_ID) "
                    + "WHERE YEAR(t.`TANGGAL_TRANSAKSI`) = " + year + " AND MONTH(t.`TANGGAL_TRANSAKSI`) = " + month + " "
                    + "AND d.`JENIS_TRANSAKSI_ID` = " + idJenisTransaksi + " "
                    + "AND d.`ID_SIMPANAN` = " + simpananId;
            ;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                dt = rs.getDate(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return dt;
    }

    public static double getFirstSaldo(long idSimpanan) {
        double n = 0;
        DBResultSet dbrs = null;
        String sql = "SELECT t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID]
                + " FROM " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                + " JOIN " + PstDetailTransaksi.TBL_DETAILTRANSAKSI + " AS dt "
                + " ON dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID] + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID]
                + " WHERE dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_ID_SIMPANAN] + " = '" + idSimpanan + "'"
                + " ORDER BY t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TANGGAL_TRANSAKSI] + " ASC "
                + " LIMIT 0,1"
                + "";
        long idTransaksi = 0;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                idTransaksi = rs.getLong(1);
            }
            rs.close();
            if (idTransaksi > 0) {
                String whereDetail = PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID] + " = '" + idTransaksi + "'"
                        + " AND " + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_ID_SIMPANAN] + " = '" + idSimpanan + "'";
                Vector<DetailTransaksi> listDetail = PstDetailTransaksi.list(0, 1, whereDetail, PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_DETAIL_ID]);
                if (listDetail.size() > 0) {
                    n = listDetail.get(0).getDebet() + listDetail.get(0).getKredit();
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        } catch (DBException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return n;
    }

    public static Vector getLastTransaksiBungaTabungan(long idSimpanan) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        String sql = "SELECT t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID]
                + " FROM " + PstTransaksi.TBL_TRANSAKSI + " AS t "
                + " JOIN " + PstDetailTransaksi.TBL_DETAILTRANSAKSI + " AS dt "
                + " ON dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID] + " = t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID]
                + " WHERE dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_ID_SIMPANAN] + " = '" + idSimpanan + "'"
                + " ORDER BY t." + PstTransaksi.fieldNames[PstTransaksi.FLD_TANGGAL_TRANSAKSI] + " ASC "
                + " LIMIT 0,1"
                + "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (DBException e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

}
