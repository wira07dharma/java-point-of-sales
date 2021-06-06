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
import java.sql.*;
import com.dimata.sedana.common.I_Sedana;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
//import com.dimata.common.db.*;
import com.dimata.common.entity.contact.*;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstJenisTransaksi extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Sedana {

    public static final String TBL_JENISTRANSAKSI = "sedana_jenis_transaksi";
    public static final int FLD_JENIS_TRANSAKSI_ID = 0;
    public static final int FLD_JENIS_TRANSAKSI = 1;
    public static final int FLD_AFLIASI_ID = 2;
    public static final int FLD_TIPE_ARUS_KAS = 3;
    public static final int FLD_TYPE_PROSEDUR = 4;
    public static final int FLD_PROSEDURE_UNTUK = 5;
    public static final int FLD_PROSENTASE_PERHITUNGAN = 6;
    public static final int FLD_TYPE_TRANSAKSI = 7;
    public static final int FLD_VALUE_STANDAR_TRANSAKSI = 8;
    public static final int FLD_STATUS_AKTIF = 9;
    public static final int FLD_TIPE_DOC = 10;
    public static final int FLD_INPUT_OPTION = 11;

    public static String[] fieldNames = {
        "JENIS_TRANSAKSI_ID",
        "JENIS_TRANSAKSI",
        "AFLIASI_ID",
        "TIPE_ARUS_KAS",
        "TYPE_PROSEDUR",
        "PROSEDURE_UNTUK",
        "PROSENTASE_PERHITUNGAN",
        "TYPE_TRANSAKSI",
        "VALUE_STANDAR_TRANSAKSI",
        "STATUS_AKTIF",
        "TIPE_DOC",
        "INPUT_OPTION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };

    public PstJenisTransaksi() {
    }

    public PstJenisTransaksi(int i) throws DBException {
        super(new PstJenisTransaksi());
    }

    public PstJenisTransaksi(String sOid) throws DBException {
        super(new PstJenisTransaksi(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstJenisTransaksi(long lOid) throws DBException {
        super(new PstJenisTransaksi(0));
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
        return TBL_JENISTRANSAKSI;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstJenisTransaksi().getClass().getName();
    }

    public static JenisTransaksi fetchWhere(String Where) throws DBException {
      Vector<JenisTransaksi> j = list(0, 1, Where, "");
      return (j.size()>0) ? j.get(0) : new JenisTransaksi();
    }

    public static JenisTransaksi fetchExc(long oid) {
        JenisTransaksi entJenisTransaksi = new JenisTransaksi();
        try {
            PstJenisTransaksi pstJenisTransaksi = new PstJenisTransaksi(oid);
            entJenisTransaksi.setOID(oid);
            entJenisTransaksi.setJenisTransaksi(pstJenisTransaksi.getString(FLD_JENIS_TRANSAKSI));
            entJenisTransaksi.setAfliasiId(pstJenisTransaksi.getlong(FLD_AFLIASI_ID));
            entJenisTransaksi.setTipeArusKas(pstJenisTransaksi.getInt(FLD_TIPE_ARUS_KAS));
            entJenisTransaksi.setTypeProsedur(pstJenisTransaksi.getInt(FLD_TYPE_PROSEDUR));
            entJenisTransaksi.setProsedureUntuk(pstJenisTransaksi.getInt(FLD_PROSEDURE_UNTUK));
            entJenisTransaksi.setProsentasePerhitungan(pstJenisTransaksi.getdouble(FLD_PROSENTASE_PERHITUNGAN));
            entJenisTransaksi.setTypeTransaksi(pstJenisTransaksi.getInt(FLD_TYPE_TRANSAKSI));
            entJenisTransaksi.setValueStandarTransaksi(pstJenisTransaksi.getdouble(FLD_VALUE_STANDAR_TRANSAKSI));
            entJenisTransaksi.setStatusAktif(pstJenisTransaksi.getInt(FLD_STATUS_AKTIF));
            entJenisTransaksi.setTipeDoc(pstJenisTransaksi.getInt(FLD_TIPE_DOC));
            entJenisTransaksi.setInputOption(pstJenisTransaksi.getInt(FLD_INPUT_OPTION));
        } catch (DBException dbe) {
            System.err.println(dbe);
        } catch (Exception e) {
            System.err.println(e);
        }
        return entJenisTransaksi;
    }

    public long fetchExc(Entity entity) throws Exception {
        JenisTransaksi entJenisTransaksi = fetchExc(entity.getOID());
        entity = (Entity) entJenisTransaksi;
        return entJenisTransaksi.getOID();
    }

    public static synchronized long updateExc(JenisTransaksi entJenisTransaksi) throws DBException {
        try {
            if (entJenisTransaksi.getOID() != 0) {
                PstJenisTransaksi pstJenisTransaksi = new PstJenisTransaksi(entJenisTransaksi.getOID());
                pstJenisTransaksi.setString(FLD_JENIS_TRANSAKSI, entJenisTransaksi.getJenisTransaksi());
                if (entJenisTransaksi.getAfliasiId() != 0) {
                    pstJenisTransaksi.setLong(FLD_AFLIASI_ID, entJenisTransaksi.getAfliasiId());
                }
                pstJenisTransaksi.setInt(FLD_TIPE_ARUS_KAS, entJenisTransaksi.getTipeArusKas());
                pstJenisTransaksi.setInt(FLD_TYPE_PROSEDUR, entJenisTransaksi.getTypeProsedur());
                pstJenisTransaksi.setInt(FLD_PROSEDURE_UNTUK, entJenisTransaksi.getProsedureUntuk());
                pstJenisTransaksi.setDouble(FLD_PROSENTASE_PERHITUNGAN, entJenisTransaksi.getProsentasePerhitungan());
                pstJenisTransaksi.setInt(FLD_TYPE_TRANSAKSI, entJenisTransaksi.getTypeTransaksi());
                pstJenisTransaksi.setDouble(FLD_VALUE_STANDAR_TRANSAKSI, entJenisTransaksi.getValueStandarTransaksi());
                pstJenisTransaksi.setInt(FLD_STATUS_AKTIF, entJenisTransaksi.getStatusAktif());
                pstJenisTransaksi.setInt(FLD_TIPE_DOC, entJenisTransaksi.getTipeDoc());
                pstJenisTransaksi.setInt(FLD_INPUT_OPTION, entJenisTransaksi.getInputOption());
                pstJenisTransaksi.update();
                return entJenisTransaksi.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJenisTransaksi(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((JenisTransaksi) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstJenisTransaksi pstJenisTransaksi = new PstJenisTransaksi(oid);
            pstJenisTransaksi.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJenisTransaksi(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(JenisTransaksi entJenisTransaksi) throws DBException {
        try {
            PstJenisTransaksi pstJenisTransaksi = new PstJenisTransaksi(0);
            pstJenisTransaksi.setString(FLD_JENIS_TRANSAKSI, entJenisTransaksi.getJenisTransaksi());
            if (entJenisTransaksi.getAfliasiId() != 0) {
                pstJenisTransaksi.setLong(FLD_AFLIASI_ID, entJenisTransaksi.getAfliasiId());
            }
            pstJenisTransaksi.setInt(FLD_TIPE_ARUS_KAS, entJenisTransaksi.getTipeArusKas());
            pstJenisTransaksi.setInt(FLD_TYPE_PROSEDUR, entJenisTransaksi.getTypeProsedur());
            pstJenisTransaksi.setInt(FLD_PROSEDURE_UNTUK, entJenisTransaksi.getProsedureUntuk());
            pstJenisTransaksi.setDouble(FLD_PROSENTASE_PERHITUNGAN, entJenisTransaksi.getProsentasePerhitungan());
            pstJenisTransaksi.setInt(FLD_TYPE_TRANSAKSI, entJenisTransaksi.getTypeTransaksi());
            pstJenisTransaksi.setDouble(FLD_VALUE_STANDAR_TRANSAKSI, entJenisTransaksi.getValueStandarTransaksi());
            pstJenisTransaksi.setInt(FLD_STATUS_AKTIF, entJenisTransaksi.getStatusAktif());
            pstJenisTransaksi.setInt(FLD_TIPE_DOC, entJenisTransaksi.getTipeDoc());
            pstJenisTransaksi.setInt(FLD_INPUT_OPTION, entJenisTransaksi.getInputOption());
            pstJenisTransaksi.insert();
            entJenisTransaksi.setOID(pstJenisTransaksi.getlong(FLD_JENIS_TRANSAKSI_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstJenisTransaksi(0), DBException.UNKNOWN);
        }
        return entJenisTransaksi.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((JenisTransaksi) entity);
    }

    public static void resultToObject(ResultSet rs, JenisTransaksi entJenisTransaksi) {
        try {
            entJenisTransaksi.setOID(rs.getLong(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_JENIS_TRANSAKSI_ID]));
            entJenisTransaksi.setJenisTransaksi(rs.getString(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_JENIS_TRANSAKSI]));
            entJenisTransaksi.setAfliasiId(rs.getLong(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_AFLIASI_ID]));
            entJenisTransaksi.setTipeArusKas(rs.getInt(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_TIPE_ARUS_KAS]));
            entJenisTransaksi.setTypeProsedur(rs.getInt(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_TYPE_PROSEDUR]));
            entJenisTransaksi.setProsedureUntuk(rs.getInt(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_PROSEDURE_UNTUK]));
            entJenisTransaksi.setProsentasePerhitungan(rs.getDouble(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_PROSENTASE_PERHITUNGAN]));
            entJenisTransaksi.setTypeTransaksi(rs.getInt(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_TYPE_TRANSAKSI]));
            entJenisTransaksi.setValueStandarTransaksi(rs.getDouble(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_VALUE_STANDAR_TRANSAKSI]));
            entJenisTransaksi.setStatusAktif(rs.getInt(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_STATUS_AKTIF]));
            entJenisTransaksi.setTipeDoc(rs.getInt(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_TIPE_DOC]));
            entJenisTransaksi.setInputOption(rs.getInt(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_INPUT_OPTION]));
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
            String sql = "SELECT * FROM " + TBL_JENISTRANSAKSI;
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
                JenisTransaksi entJenisTransaksi = new JenisTransaksi();
                resultToObject(rs, entJenisTransaksi);
                lists.add(entJenisTransaksi);
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

    public static boolean checkOID(long entJenisTransaksiId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_JENISTRANSAKSI + " WHERE "
                    + PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_JENIS_TRANSAKSI_ID] + " = " + entJenisTransaksiId;
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
            String sql = "SELECT COUNT(" + PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_JENIS_TRANSAKSI_ID] + ") FROM " + TBL_JENISTRANSAKSI;
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
                    JenisTransaksi entJenisTransaksi = (JenisTransaksi) list.get(ls);
                    if (oid == entJenisTransaksi.getOID()) {
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
    
    public static long getIdJenisTransaksiByNamaJenisTransaksi(String nama) {
      long p = 0;
      DBResultSet dbrs = null;
      String sql = "SELECT JENIS_TRANSAKSI_ID FROM `sedana_jenis_transaksi` WHERE jenis_transaksi='"+nama+"'";
      try {
          dbrs = DBHandler.execQueryResult(sql);
          ResultSet rs = dbrs.getResultSet();
          if (rs.next()) {
              p = rs.getLong(1);
          }
          rs.close();
      } catch (DBException e) {
          System.err.println(e);
      } catch (SQLException e) {
          System.err.println(e);
      } finally {
          DBResultSet.close(dbrs);
      }

      return p;
    }

    public static long getIdJenisTransaksiByPinjamanId(long pinajamanId) {
        return getIdJenisTransaksiByPinjamanId(pinajamanId, 0);
    }

    public static long getIdJenisTransaksiByPinjamanId(long pinajamanId, int tipeDoc) {
        long p = 0;
        DBResultSet dbrs = null;
        String sql = "SELECT DISTINCT t.`JENIS_TRANSAKSI_ID` "
                + "FROM `sedana_jenis_transaksi` t "
                + "JOIN `sedana_pinjaman_detail` d ON t.`JENIS_TRANSAKSI_ID`=d.`ID_JENIS_TRANSAKSI` "
                + "JOIN `aiso_pinjaman` p ON d.`ID_PINJAMAN` = p.`PINJAMAN_ID` "
                + "WHERE p.`PINJAMAN_ID` = " + pinajamanId + " "
                + (tipeDoc != 0 ? "AND t.`TIPE_DOC`=" + tipeDoc : "");
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                p = rs.getLong(1);
                break;
            }
            rs.close();
        } catch (DBException e) {
            System.err.println(e);
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return p;
    }
}
