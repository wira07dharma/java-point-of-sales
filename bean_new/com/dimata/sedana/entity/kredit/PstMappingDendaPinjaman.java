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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dimata 007
 */
public class PstMappingDendaPinjaman extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MAPPING_DENDA_PINJAMAN = "sedana_pinjaman_mapping_denda";
    public static final int FLD_ID_MAPPING_DENDA = 0;
    public static final int FLD_NILAI_DENDA = 1;
    public static final int FLD_TIPE_DENDA_BERLAKU = 2;
    public static final int FLD_TIPE_PERHITUNGAN_DENDA = 3;
    public static final int FLD_FREKUENSI_DENDA = 4;
    public static final int FLD_SATUAN_FREKUENSI_DENDA = 5;
    public static final int FLD_PINJAMAN_ID = 6;
    public static final int FLD_DENDA_TOLERANSI = 7;
    public static final int FLD_VARIABEL_DENDA = 8;
    public static final int FLD_JENIS_ANGSURAN = 9;
    public static final int FLD_TIPE_VARIABEL_DENDA = 10;
    public static final int FLD_TIPE_FREKUENSI_DENDA = 11;

    public static String[] fieldNames = {
        "ID_MAPPING_DENDA",
        "NILAI_DENDA",
        "TIPE_DENDA_BERLAKU",
        "TIPE_PERHITUNGAN_DENDA",
        "FREKUENSI_DENDA",
        "SATUAN_FREKUENSI_DENDA",
        "PINJAMAN_ID",
        "DENDA_TOLERANSI",
        "VARIABEL_DENDA",
        "JENIS_ANGSURAN",
        "TIPE_VARIABEL_DENDA",
        "TIPE_FREKUENSI_DENDA"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };

    public PstMappingDendaPinjaman() {
    }

    public PstMappingDendaPinjaman(int i) throws DBException {
        super(new PstMappingDendaPinjaman());
    }

    public PstMappingDendaPinjaman(String sOid) throws DBException {
        super(new PstMappingDendaPinjaman(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMappingDendaPinjaman(long lOid) throws DBException {
        super(new PstMappingDendaPinjaman(0));
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
        return TBL_MAPPING_DENDA_PINJAMAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMappingDendaPinjaman().getClass().getName();
    }

    public static MappingDendaPinjaman fetchExc(long oid) throws DBException {
        try {
            MappingDendaPinjaman entMappingDendaPinjaman = new MappingDendaPinjaman();
            PstMappingDendaPinjaman pstMappingDendaPinjaman = new PstMappingDendaPinjaman(oid);
            entMappingDendaPinjaman.setOID(oid);
            entMappingDendaPinjaman.setNilaiDenda(pstMappingDendaPinjaman.getdouble(FLD_NILAI_DENDA));
            entMappingDendaPinjaman.setTipeDendaBerlaku(pstMappingDendaPinjaman.getInt(FLD_TIPE_DENDA_BERLAKU));
            entMappingDendaPinjaman.setTipePerhitunganDenda(pstMappingDendaPinjaman.getInt(FLD_TIPE_PERHITUNGAN_DENDA));
            entMappingDendaPinjaman.setFrekuensiDenda(pstMappingDendaPinjaman.getInt(FLD_FREKUENSI_DENDA));
            entMappingDendaPinjaman.setSatuanFrekuensiDenda(pstMappingDendaPinjaman.getInt(FLD_SATUAN_FREKUENSI_DENDA));
            entMappingDendaPinjaman.setPinjamanId(pstMappingDendaPinjaman.getlong(FLD_PINJAMAN_ID));
            entMappingDendaPinjaman.setDendaToleransi(pstMappingDendaPinjaman.getInt(FLD_DENDA_TOLERANSI));
            entMappingDendaPinjaman.setVariabelDenda(pstMappingDendaPinjaman.getInt(FLD_VARIABEL_DENDA));
            entMappingDendaPinjaman.setJenisAngsuran(pstMappingDendaPinjaman.getInt(FLD_JENIS_ANGSURAN));
            entMappingDendaPinjaman.setTipeVariabelDenda(pstMappingDendaPinjaman.getInt(FLD_TIPE_VARIABEL_DENDA));
            entMappingDendaPinjaman.setTipeFrekuensiDenda(pstMappingDendaPinjaman.getInt(FLD_TIPE_FREKUENSI_DENDA));
            return entMappingDendaPinjaman;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMappingDendaPinjaman(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MappingDendaPinjaman entMappingDendaPinjaman = fetchExc(entity.getOID());
        entity = (Entity) entMappingDendaPinjaman;
        return entMappingDendaPinjaman.getOID();
    }

    public static synchronized long updateExc(MappingDendaPinjaman entMappingDendaPinjaman) throws DBException {
        try {
            if (entMappingDendaPinjaman.getOID() != 0) {
                PstMappingDendaPinjaman pstMappingDendaPinjaman = new PstMappingDendaPinjaman(entMappingDendaPinjaman.getOID());
                pstMappingDendaPinjaman.setDouble(FLD_NILAI_DENDA, entMappingDendaPinjaman.getNilaiDenda());
                pstMappingDendaPinjaman.setInt(FLD_TIPE_DENDA_BERLAKU, entMappingDendaPinjaman.getTipeDendaBerlaku());
                pstMappingDendaPinjaman.setInt(FLD_TIPE_PERHITUNGAN_DENDA, entMappingDendaPinjaman.getTipePerhitunganDenda());
                pstMappingDendaPinjaman.setInt(FLD_FREKUENSI_DENDA, entMappingDendaPinjaman.getFrekuensiDenda());
                pstMappingDendaPinjaman.setInt(FLD_SATUAN_FREKUENSI_DENDA, entMappingDendaPinjaman.getSatuanFrekuensiDenda());
                pstMappingDendaPinjaman.setLong(FLD_PINJAMAN_ID, entMappingDendaPinjaman.getPinjamanId());
                pstMappingDendaPinjaman.setInt(FLD_DENDA_TOLERANSI, entMappingDendaPinjaman.getDendaToleransi());
                pstMappingDendaPinjaman.setInt(FLD_VARIABEL_DENDA, entMappingDendaPinjaman.getVariabelDenda());
                pstMappingDendaPinjaman.setInt(FLD_JENIS_ANGSURAN, entMappingDendaPinjaman.getJenisAngsuran());
                pstMappingDendaPinjaman.setInt(FLD_TIPE_VARIABEL_DENDA, entMappingDendaPinjaman.getTipeVariabelDenda());
                pstMappingDendaPinjaman.setInt(FLD_TIPE_FREKUENSI_DENDA, entMappingDendaPinjaman.getTipeFrekuensiDenda());
                pstMappingDendaPinjaman.update();
                return entMappingDendaPinjaman.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMappingDendaPinjaman(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MappingDendaPinjaman) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMappingDendaPinjaman pstMappingDendaPinjaman = new PstMappingDendaPinjaman(oid);
            pstMappingDendaPinjaman.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMappingDendaPinjaman(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MappingDendaPinjaman entMappingDendaPinjaman) throws DBException {
        try {
            PstMappingDendaPinjaman pstMappingDendaPinjaman = new PstMappingDendaPinjaman(0);
            pstMappingDendaPinjaman.setDouble(FLD_NILAI_DENDA, entMappingDendaPinjaman.getNilaiDenda());
            pstMappingDendaPinjaman.setInt(FLD_TIPE_DENDA_BERLAKU, entMappingDendaPinjaman.getTipeDendaBerlaku());
            pstMappingDendaPinjaman.setInt(FLD_TIPE_PERHITUNGAN_DENDA, entMappingDendaPinjaman.getTipePerhitunganDenda());
            pstMappingDendaPinjaman.setInt(FLD_FREKUENSI_DENDA, entMappingDendaPinjaman.getFrekuensiDenda());
            pstMappingDendaPinjaman.setInt(FLD_SATUAN_FREKUENSI_DENDA, entMappingDendaPinjaman.getSatuanFrekuensiDenda());
            pstMappingDendaPinjaman.setLong(FLD_PINJAMAN_ID, entMappingDendaPinjaman.getPinjamanId());
            pstMappingDendaPinjaman.setInt(FLD_DENDA_TOLERANSI, entMappingDendaPinjaman.getDendaToleransi());
            pstMappingDendaPinjaman.setInt(FLD_VARIABEL_DENDA, entMappingDendaPinjaman.getVariabelDenda());
            pstMappingDendaPinjaman.setInt(FLD_JENIS_ANGSURAN, entMappingDendaPinjaman.getJenisAngsuran());
            pstMappingDendaPinjaman.setInt(FLD_TIPE_VARIABEL_DENDA, entMappingDendaPinjaman.getTipeVariabelDenda());
            pstMappingDendaPinjaman.setInt(FLD_TIPE_FREKUENSI_DENDA, entMappingDendaPinjaman.getTipeFrekuensiDenda());
            pstMappingDendaPinjaman.insert();
            entMappingDendaPinjaman.setOID(pstMappingDendaPinjaman.getlong(FLD_ID_MAPPING_DENDA));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMappingDendaPinjaman(0), DBException.UNKNOWN);
        }
        return entMappingDendaPinjaman.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MappingDendaPinjaman) entity);
    }

    public static void resultToObject(ResultSet rs, MappingDendaPinjaman entMappingDendaPinjaman) {
        try {
            entMappingDendaPinjaman.setOID(rs.getLong(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_ID_MAPPING_DENDA]));
            entMappingDendaPinjaman.setNilaiDenda(rs.getDouble(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_NILAI_DENDA]));
            entMappingDendaPinjaman.setTipeDendaBerlaku(rs.getInt(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_TIPE_DENDA_BERLAKU]));
            entMappingDendaPinjaman.setTipePerhitunganDenda(rs.getInt(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_TIPE_PERHITUNGAN_DENDA]));
            entMappingDendaPinjaman.setFrekuensiDenda(rs.getInt(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_FREKUENSI_DENDA]));
            entMappingDendaPinjaman.setSatuanFrekuensiDenda(rs.getInt(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_SATUAN_FREKUENSI_DENDA]));
            entMappingDendaPinjaman.setPinjamanId(rs.getLong(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_PINJAMAN_ID]));
            entMappingDendaPinjaman.setDendaToleransi(rs.getInt(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_DENDA_TOLERANSI]));
            entMappingDendaPinjaman.setVariabelDenda(rs.getInt(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_VARIABEL_DENDA]));
            entMappingDendaPinjaman.setJenisAngsuran(rs.getInt(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_JENIS_ANGSURAN]));
            entMappingDendaPinjaman.setTipeVariabelDenda(rs.getInt(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_TIPE_VARIABEL_DENDA]));
            entMappingDendaPinjaman.setTipeFrekuensiDenda(rs.getInt(PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_TIPE_FREKUENSI_DENDA]));
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
            String sql = "SELECT * FROM " + TBL_MAPPING_DENDA_PINJAMAN;
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
                MappingDendaPinjaman entMappingDendaPinjaman = new MappingDendaPinjaman();
                resultToObject(rs, entMappingDendaPinjaman);
                lists.add(entMappingDendaPinjaman);
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

    public static boolean checkOID(long entMappingDendaPinjamanId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAPPING_DENDA_PINJAMAN + " WHERE "
                    + PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_ID_MAPPING_DENDA] + " = " + entMappingDendaPinjamanId;
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
            String sql = "SELECT COUNT(" + PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_ID_MAPPING_DENDA] + ") FROM " + TBL_MAPPING_DENDA_PINJAMAN;
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
                    MappingDendaPinjaman entMappingDendaPinjaman = (MappingDendaPinjaman) list.get(ls);
                    if (oid == entMappingDendaPinjaman.getOID()) {
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
    
    public static MappingDendaPinjaman getSettingDenda(long oidPinjaman, int jenisAngsuran) {
        String where = PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_PINJAMAN_ID] + " = " + oidPinjaman
                + " AND " + PstMappingDendaPinjaman.fieldNames[PstMappingDendaPinjaman.FLD_JENIS_ANGSURAN] + " = " + jenisAngsuran;
        Vector<MappingDendaPinjaman> listMappingDenda = PstMappingDendaPinjaman.list(0, 0, where, "");
        if (listMappingDenda.size() == 1) {
            try {
                return PstMappingDendaPinjaman.fetchExc(listMappingDenda.get(0).getOID());
            } catch (DBException ex) {
                Logger.getLogger(PstMappingDendaPinjaman.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
