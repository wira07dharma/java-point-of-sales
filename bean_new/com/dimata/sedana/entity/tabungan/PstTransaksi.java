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
import com.dimata.sedana.entity.assigncontacttabungan.AssignContactTabungan;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class PstTransaksi extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_TRANSAKSI = "sedana_transaksi";
    public static final int FLD_TRANSAKSI_ID = 0;
    public static final int FLD_TANGGAL_TRANSAKSI = 1;
    public static final int FLD_KODE_BUKTI_TRANSAKSI = 2;
    public static final int FLD_ID_ANGGOTA = 3;
    public static final int FLD_TGL_PRINT_TERAKHIR = 4;
    public static final int FLD_TELLER_SHIFT_ID = 5;
    public static final int FLD_KETERANGAN = 6;
    public static final int FLD_STATUS = 7;
    public static final int FLD_TIPE_ARUS_KAS = 8;
    public static final int FLD_PINJAMAN_ID = 9;
    public static final int FLD_ID_DEPOSITO = 10;
    public static final int FLD_USECASE_TYPE = 11;
    public static final int FLD_TRANSAKSI_PARENT_ID = 12;

    public static String[] fieldNames = {
        "TRANSAKSI_ID",
        "TANGGAL_TRANSAKSI",
        "KODE_BUKTI_TRANSAKSI",
        "ID_ANGGOTA",
        "TGL_PRINT_TERAKHIR",
        "TELLER_SHIFT_ID",
        "KETERANGAN",
        "STATUS",
        "TIPE_ARUS_KAS",
        "PINJAMAN_ID",
        "ID_DEPOSITO",
        "USECASE_TYPE",
        "TRANSAKSI_PARENT_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_ID + TYPE_PK,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG
    };

    public PstTransaksi() {
    }

    public PstTransaksi(int i) throws DBException {
        super(new PstTransaksi());
    }

    public PstTransaksi(String sOid) throws DBException {
        super(new PstTransaksi(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstTransaksi(long lOid) throws DBException {
        super(new PstTransaksi(0));
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
        return TBL_TRANSAKSI;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstTransaksi().getClass().getName();
    }

    public static Transaksi fetchExc(long oid) throws DBException {
        try {
            Transaksi entTransaksi = new Transaksi();
            PstTransaksi pstTransaksi = new PstTransaksi(oid);
            entTransaksi.setOID(oid);
            entTransaksi.setTanggalTransaksi(pstTransaksi.getDate(FLD_TANGGAL_TRANSAKSI));
            entTransaksi.setKodeBuktiTransaksi(pstTransaksi.getString(FLD_KODE_BUKTI_TRANSAKSI));
            entTransaksi.setIdAnggota(pstTransaksi.getlong(FLD_ID_ANGGOTA));
            entTransaksi.setTglPrintTerakhir(pstTransaksi.getDate(FLD_TGL_PRINT_TERAKHIR));
            entTransaksi.setTellerShiftId(pstTransaksi.getlong(FLD_TELLER_SHIFT_ID));
            entTransaksi.setKeterangan(pstTransaksi.getString(FLD_KETERANGAN));
            entTransaksi.setStatus(pstTransaksi.getInt(FLD_STATUS));
            entTransaksi.setTipeArusKas(pstTransaksi.getInt(FLD_TIPE_ARUS_KAS));
            entTransaksi.setPinjamanId(pstTransaksi.getlong(FLD_PINJAMAN_ID));
            entTransaksi.setIdDeposito(pstTransaksi.getlong(FLD_ID_DEPOSITO));
            entTransaksi.setUsecaseType(pstTransaksi.getInt(FLD_USECASE_TYPE));
            entTransaksi.setTransaksiParentId(pstTransaksi.getlong(FLD_TRANSAKSI_PARENT_ID));
            return entTransaksi;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTransaksi(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        Transaksi entTransaksi = fetchExc(entity.getOID());
        entity = (Entity) entTransaksi;
        return entTransaksi.getOID();
    }

    public static synchronized long updateExc(Transaksi entTransaksi) throws DBException {
        try {
            if (entTransaksi.getOID() != 0) {
                PstTransaksi pstTransaksi = new PstTransaksi(entTransaksi.getOID());
                pstTransaksi.setDate(FLD_TANGGAL_TRANSAKSI, entTransaksi.getTanggalTransaksi());
                pstTransaksi.setString(FLD_KODE_BUKTI_TRANSAKSI, entTransaksi.getKodeBuktiTransaksi());
                pstTransaksi.setLong(FLD_ID_ANGGOTA, entTransaksi.getIdAnggota());
                pstTransaksi.setDate(FLD_TGL_PRINT_TERAKHIR, entTransaksi.getTglPrintTerakhir());
                pstTransaksi.setLong(FLD_TELLER_SHIFT_ID, entTransaksi.getTellerShiftId());
                pstTransaksi.setString(FLD_KETERANGAN, entTransaksi.getKeterangan());
                pstTransaksi.setInt(FLD_STATUS, entTransaksi.getStatus());
                pstTransaksi.setInt(FLD_TIPE_ARUS_KAS, entTransaksi.getTipeArusKas());
                if (entTransaksi.getPinjamanId() != 0) {
                    pstTransaksi.setLong(FLD_PINJAMAN_ID, entTransaksi.getPinjamanId());
                }
                if (entTransaksi.getIdDeposito() != 0) {
                    pstTransaksi.setLong(FLD_ID_DEPOSITO, entTransaksi.getIdDeposito());
                }
                pstTransaksi.setInt(FLD_USECASE_TYPE, entTransaksi.getUsecaseType());
                if (entTransaksi.getTransaksiParentId() != 0) {
                    pstTransaksi.setLong(FLD_TRANSAKSI_PARENT_ID, entTransaksi.getTransaksiParentId());
                }
                pstTransaksi.update();
                return entTransaksi.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTransaksi(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static double getSaldoBeforeDate(long memberId, Vector<Long> jenisSimpanan, java.util.Date date) {
        double saldo = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        for (long idJ : jenisSimpanan) {
            long id = PstDataTabungan.getIdSimpanan(memberId, idJ);
            if (id != 0) {
                saldo += PstDetailTransaksi.getSaldoByDate(id, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
            }
        }
        return saldo;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((Transaksi) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstTransaksi pstTransaksi = new PstTransaksi(oid);
            pstTransaksi.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTransaksi(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(Transaksi entTransaksi) throws DBException {
        try {
            PstTransaksi pstTransaksi = new PstTransaksi(0);
            pstTransaksi.setDate(FLD_TANGGAL_TRANSAKSI, entTransaksi.getTanggalTransaksi());
            pstTransaksi.setString(FLD_KODE_BUKTI_TRANSAKSI, entTransaksi.getKodeBuktiTransaksi());
            pstTransaksi.setLong(FLD_ID_ANGGOTA, entTransaksi.getIdAnggota());
            pstTransaksi.setDate(FLD_TGL_PRINT_TERAKHIR, entTransaksi.getTglPrintTerakhir());
            pstTransaksi.setLong(FLD_TELLER_SHIFT_ID, entTransaksi.getTellerShiftId());
            pstTransaksi.setString(FLD_KETERANGAN, entTransaksi.getKeterangan());
            pstTransaksi.setInt(FLD_STATUS, entTransaksi.getStatus());
            pstTransaksi.setInt(FLD_TIPE_ARUS_KAS, entTransaksi.getTipeArusKas());
            if (entTransaksi.getPinjamanId() != 0) {
                pstTransaksi.setLong(FLD_PINJAMAN_ID, entTransaksi.getPinjamanId());
            }
            if (entTransaksi.getIdDeposito() != 0) {
                pstTransaksi.setLong(FLD_ID_DEPOSITO, entTransaksi.getIdDeposito());
            }
            pstTransaksi.setInt(FLD_USECASE_TYPE, entTransaksi.getUsecaseType());
            if (entTransaksi.getTransaksiParentId() != 0) {
                pstTransaksi.setLong(FLD_TRANSAKSI_PARENT_ID, entTransaksi.getTransaksiParentId());
            }
            pstTransaksi.insert();
            entTransaksi.setOID(pstTransaksi.getlong(FLD_TRANSAKSI_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTransaksi(0), DBException.UNKNOWN);
        }
        return entTransaksi.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((Transaksi) entity);
    }

    public static void resultToObject(ResultSet rs, Transaksi entTransaksi) {
        try {
            entTransaksi.setOID(rs.getLong(PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID]));
            entTransaksi.setTanggalTransaksi(Formater.formatDate(rs.getString(PstTransaksi.fieldNames[PstTransaksi.FLD_TANGGAL_TRANSAKSI]), "yyyy-MM-dd HH:mm:ss"));
            entTransaksi.setKodeBuktiTransaksi(rs.getString(PstTransaksi.fieldNames[PstTransaksi.FLD_KODE_BUKTI_TRANSAKSI]));
            entTransaksi.setIdAnggota(rs.getLong(PstTransaksi.fieldNames[PstTransaksi.FLD_ID_ANGGOTA]));
            entTransaksi.setTglPrintTerakhir(rs.getDate(PstTransaksi.fieldNames[PstTransaksi.FLD_TGL_PRINT_TERAKHIR]));
            entTransaksi.setTellerShiftId(rs.getLong(PstTransaksi.fieldNames[PstTransaksi.FLD_TELLER_SHIFT_ID]));
            entTransaksi.setKeterangan(rs.getString(PstTransaksi.fieldNames[PstTransaksi.FLD_KETERANGAN]));
            entTransaksi.setStatus(rs.getInt(PstTransaksi.fieldNames[PstTransaksi.FLD_STATUS]));
            entTransaksi.setTipeArusKas(rs.getInt(PstTransaksi.fieldNames[PstTransaksi.FLD_TIPE_ARUS_KAS]));
            entTransaksi.setPinjamanId(rs.getLong(fieldNames[FLD_PINJAMAN_ID]));
            entTransaksi.setIdDeposito(rs.getLong(fieldNames[FLD_ID_DEPOSITO]));
            entTransaksi.setUsecaseType(rs.getInt(fieldNames[FLD_USECASE_TYPE]));
            entTransaksi.setTransaksiParentId(rs.getLong(fieldNames[FLD_TRANSAKSI_PARENT_ID]));
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
            String sql = "SELECT * FROM " + TBL_TRANSAKSI;
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
                Transaksi entTransaksi = new Transaksi();
                resultToObject(rs, entTransaksi);
                lists.add(entTransaksi);
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

    public static boolean checkOID(long entTransaksiId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_TRANSAKSI + " WHERE "
                    + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + " = " + entTransaksiId;
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
            String sql = "SELECT COUNT(" + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + ") FROM " + TBL_TRANSAKSI;
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
                    Transaksi entTransaksi = (Transaksi) list.get(ls);
                    if (oid == entTransaksi.getOID()) {
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

    public static String generateNoTransaksi() {
        return generateNoTransaksi("");
    }

    public static synchronized String generateNoTransaksi(String prefix) {
        String n = "";
        java.util.Date now = new java.util.Date();
        String year = Formater.formatDate(now, "yyyy");
        String month = Formater.formatDate(now, "MM");
        String day = Formater.formatDate(now, "dd");
        String no = "";
        int num = -1;

        String digit = "0000";
        while (no.equals("") || PstTransaksi.getCount(PstTransaksi.fieldNames[PstTransaksi.FLD_KODE_BUKTI_TRANSAKSI] + "='" + no + "'") > 0) {
            if ((num < 0)) {
                num = getCount(fieldNames[FLD_TANGGAL_TRANSAKSI] + ">='" + Formater.formatDate(now, "yyyy-MM-dd") + "'") + 1;
            } else {
                num++;
            }
            String sNum = String.valueOf(num);
            String newNumber = digit.substring(0, digit.length() - sNum.length()) + sNum;
            n = year + month + day + "-" + newNumber;
            no = prefix + n;
        }
        return no;
    }

    public static String getNoAnggotaByTransaksiId(long transaksiId) {
        DBResultSet dbrs = null;
        String sql = "SELECT DISTINCT NO_TABUNGAN AS NO FROM sedana_transaksi t JOIN `sedana_detail_transaksi` dt USING (TRANSAKSI_ID) JOIN `aiso_data_tabungan` tab USING (ID_SIMPANAN) JOIN `sedana_assign_contact_tabungan` ct USING (ASSIGN_TABUNGAN_ID) WHERE t.`TRANSAKSI_ID` = " + transaksiId;
        String noAnggota = "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                noAnggota = rs.getString(1);
                break;
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return noAnggota;
    }

    public static String getLastTransactionDate(long anggotaId, long assignTabunganId, long jenisSimpananId) {
        DBResultSet dbrs = null;
        String sql = "SELECT DISTINCT ST." + PstTransaksi.fieldNames[PstTransaksi.FLD_TANGGAL_TRANSAKSI]
                + " FROM " + TBL_TRANSAKSI + " ST INNER JOIN " + PstDetailTransaksi.TBL_DETAILTRANSAKSI + " SDT "
                + " ON ST." + PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID] + " = SDT." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID]
                + " INNER JOIN " + PstDataTabungan.TBL_DATA_TABUNGAN + " ADT "
                + " ON SDT." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_ID_SIMPANAN] + " = ADT." + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_SIMPANAN]
                + " WHERE ST." + PstTransaksi.fieldNames[PstTransaksi.FLD_ID_ANGGOTA] + " = " + anggotaId
                + " AND ADT." + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ASSIGN_TABUNGAN_ID] + " = " + assignTabunganId
                + " AND ADT." + PstDataTabungan.fieldNames[PstDataTabungan.FLD_ID_JENIS_SIMPANAN] + " = " + jenisSimpananId
                + " ORDER BY ST." + PstTransaksi.fieldNames[PstTransaksi.FLD_TANGGAL_TRANSAKSI] + " DESC LIMIT 1";
        String tglTransaksi = "-";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                tglTransaksi = rs.getString(1);
                break;
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return tglTransaksi;
    }

    public static String generateKodeTransaksi(String kodeTransaksi, int useCaseType, java.util.Date tglTransaksi) {
        String nomorTransaksi = "Transaksi." + Formater.formatDate(new java.util.Date(), "yyyy-MM-dd/HH:mm:ss");;
        try {
            tglTransaksi = (tglTransaksi == null) ? new java.util.Date() : tglTransaksi;
            String date = Formater.formatDate(tglTransaksi, "yyyy-MM-dd");
            String where = ""
                    + " DATE(" + PstTransaksi.fieldNames[PstTransaksi.FLD_TANGGAL_TRANSAKSI] + ") = '" + date + "'"
                    + " AND " + PstTransaksi.fieldNames[PstTransaksi.FLD_USECASE_TYPE] + " = " + useCaseType + "";
            int count = PstTransaksi.getCount(where);
            count += 1;
            boolean isExist = true;
            String zero = "0000";
            while (isExist) {
                String lastCode = "" + count;
                if (lastCode.length() < zero.length()) {
                    lastCode = zero.substring(zero.length() - (zero.length() - lastCode.length())) + count;
                }
                nomorTransaksi = kodeTransaksi + date.replace("-", "") + "-" + lastCode;
                Vector listTransaksi = PstTransaksi.list(0, 0, "" + PstTransaksi.fieldNames[PstTransaksi.FLD_KODE_BUKTI_TRANSAKSI] + " = '" + nomorTransaksi + "'", "");
                if (listTransaksi.isEmpty()) {
                    isExist = false;
                } else {
                    count += 1;
                }
            }
        } catch (Exception e) {
            System.out.println("==================== WARNING ====================");
            System.out.println("MESSAGE : " + e.getMessage());
            System.out.println("=================================================");
        }
        return nomorTransaksi;
    }

    public static int countTransaksiBungaTabunganPosted(java.util.Date tglTransaksi) {
        DBResultSet dbrs = null;
        String sql = "SELECT "
                + "  t.* "
                + " FROM "
                + "  " + TBL_TRANSAKSI + " t "
                + "  JOIN " + PstDetailTransaksi.TBL_DETAILTRANSAKSI + " dt "
                + "    ON dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID] + " = t." + fieldNames[FLD_TRANSAKSI_ID]
                + " WHERE t." + fieldNames[FLD_USECASE_TYPE] + " = '" + Transaksi.USECASE_TYPE_TABUNGAN_BUNGA_PENCATATAN + "' "
                + "  AND YEAR(t." + fieldNames[FLD_TANGGAL_TRANSAKSI] + ") = '" + Formater.formatDate(tglTransaksi, "yyyy") + "' "
                + "  AND MONTH(t." + fieldNames[FLD_TANGGAL_TRANSAKSI] + ") = '" + Formater.formatDate(tglTransaksi, "MM") + "' "
                + "  AND t." + fieldNames[FLD_STATUS] + " = '" + Transaksi.STATUS_DOC_TRANSAKSI_POSTED + "'"
                + "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            int posted;
            try (ResultSet rs = dbrs.getResultSet()) {
                posted = 0;
                while (rs.next()) {
                    posted = rs.getInt(1);
                }
            }
            return posted;
            
        } catch (DBException | SQLException e) {
            System.err.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
    
    public static ArrayList getTransaksiBungaTabunganClosed(java.util.Date tglTransaksi) {
        DBResultSet dbrs = null;
        String sql = "SELECT "
                + "  t.* "
                + " FROM "
                + "  " + TBL_TRANSAKSI + " t "
                + "  JOIN " + PstDetailTransaksi.TBL_DETAILTRANSAKSI + " dt "
                + "    ON dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID] + " = t." + fieldNames[FLD_TRANSAKSI_ID]
                + " WHERE t." + fieldNames[FLD_USECASE_TYPE] + " = '" + Transaksi.USECASE_TYPE_TABUNGAN_BUNGA_PENCATATAN + "' "
                + "  AND YEAR(t." + fieldNames[FLD_TANGGAL_TRANSAKSI] + ") = '" + Formater.formatDate(tglTransaksi, "yyyy") + "' "
                + "  AND MONTH(t." + fieldNames[FLD_TANGGAL_TRANSAKSI] + ") = '" + Formater.formatDate(tglTransaksi, "MM") + "' "
                + "  AND t." + fieldNames[FLD_STATUS] + " = '" + Transaksi.STATUS_DOC_TRANSAKSI_CLOSED + "'"
                + "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            ArrayList list = new ArrayList();
            while (rs.next()) {
                Transaksi entTransaksi = new Transaksi();
                resultToObject(rs, entTransaksi);
                list.add(entTransaksi);
            }
            rs.close();
            return list;
            
        } catch (DBException | SQLException e) {
            System.err.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new ArrayList();
    }
    
    public static ArrayList getTransaksiBiayaAdminBulanan(long idSimpanan, java.util.Date tglTransaksi) {
        DBResultSet dbrs = null;
        String sql = "SELECT "
                + "  t.* "
                + " FROM "
                + "  " + TBL_TRANSAKSI + " t "
                + "  JOIN " + PstDetailTransaksi.TBL_DETAILTRANSAKSI + " dt "
                + "    ON dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_TRANSAKSI_ID] + " = t." + fieldNames[FLD_TRANSAKSI_ID]
                + " WHERE t." + fieldNames[FLD_USECASE_TYPE] + " = '" + Transaksi.USECASE_TYPE_TABUNGAN_POTONGAN_ADMIN_PENCATATAN + "' "
                + "  AND YEAR(t." + fieldNames[FLD_TANGGAL_TRANSAKSI] + ") = '" + Formater.formatDate(tglTransaksi, "yyyy") + "' "
                + "  AND MONTH(t." + fieldNames[FLD_TANGGAL_TRANSAKSI] + ") = '" + Formater.formatDate(tglTransaksi, "MM") + "' "
                + "  AND dt." + PstDetailTransaksi.fieldNames[PstDetailTransaksi.FLD_ID_SIMPANAN] + " = '" + idSimpanan + "'"
                + "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            ArrayList list = new ArrayList();
            while (rs.next()) {
                Transaksi entTransaksi = new Transaksi();
                resultToObject(rs, entTransaksi);
                list.add(entTransaksi);
            }
            rs.close();
            return list;
            
        } catch (DBException | SQLException e) {
            System.err.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new ArrayList();
    }
    
    public static ArrayList getBungaTabunganPerTanggal() {
        DBResultSet dbrs = null;
        String sql = "SELECT "
                + "  t.TANGGAL_TRANSAKSI, "
                + "  t.TELLER_SHIFT_ID, "
                + "  COUNT(dt.TRANSAKSI_DETAIL_ID) "
                + " FROM "
                + "  sedana_transaksi t "
                + "  JOIN sedana_detail_transaksi dt "
                + "    ON dt.TRANSAKSI_ID = t.TRANSAKSI_ID "
                + " WHERE t.USECASE_TYPE = '" + Transaksi.USECASE_TYPE_TABUNGAN_BUNGA_PENCATATAN + "' "
                + " GROUP BY t.TANGGAL_TRANSAKSI "
                + " ORDER BY t.TANGGAL_TRANSAKSI DESC"
                + " LIMIT 0,12";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            ArrayList list = new ArrayList();
            while (rs.next()) {
                Transaksi t = new Transaksi();
                t.setTanggalTransaksi(rs.getTimestamp(1));
                t.setTellerShiftId(rs.getLong(2));
                t.setStatus(rs.getInt(3));
                list.add(t);
            }
            rs.close();
            return list;
            
        } catch (DBException | SQLException e) {
            System.err.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new ArrayList();
    }
    
    public static ArrayList getDetaiBungaTabunganPerTanggal(String date) {
        DBResultSet dbrs = null;
        String sql = "SELECT "
                + "  act.NO_TABUNGAN, "
                + "  cl.PERSON_NAME, "
                + "  js.JENIS_BUNGA, "
                + "  dt.KREDIT, "
                + "  dt.DETAIL_INFO "
                + " FROM "
                + "  sedana_transaksi t "
                + "  JOIN sedana_detail_transaksi dt "
                + "    ON dt.TRANSAKSI_ID = t.TRANSAKSI_ID "
                + "  JOIN aiso_data_tabungan adt "
                + "    ON adt.ID_SIMPANAN = dt.ID_SIMPANAN "
                + "  JOIN sedana_assign_contact_tabungan act "
                + "    ON act.ASSIGN_TABUNGAN_ID = adt.ASSIGN_TABUNGAN_ID "
                + "  JOIN contact_list cl "
                + "    ON cl.CONTACT_ID = adt.ID_ANGGOTA "
                + "  JOIN aiso_jenis_simpanan js "
                + "    ON js.ID_JENIS_SIMPANAN = adt.ID_JENIS_SIMPANAN "
                + " WHERE t.USECASE_TYPE = '" + Transaksi.USECASE_TYPE_TABUNGAN_BUNGA_PENCATATAN + "' "
                + "  AND t.TANGGAL_TRANSAKSI = '" + date + "' "
                + " ORDER BY act.NO_TABUNGAN"
                + "";
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            ArrayList list = new ArrayList();
            while (rs.next()) {
                ArrayList data = new ArrayList();
                DataTabungan tabungan = new DataTabungan();
                tabungan.setKodeTabungan(rs.getString("NO_TABUNGAN"));
                tabungan.setCatatan(rs.getString("PERSON_NAME"));
                tabungan.setStatus(rs.getInt("JENIS_BUNGA"));
                data.add(tabungan);
                
                DetailTransaksi dt = new DetailTransaksi();
                dt.setKredit(rs.getDouble("KREDIT"));
                dt.setDetailInfo(rs.getString("DETAIL_INFO"));
                data.add(dt);
                
                list.add(data);
            }
            rs.close();
            return list;
            
        } catch (DBException | SQLException e) {
            System.err.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new ArrayList();
    }

}
