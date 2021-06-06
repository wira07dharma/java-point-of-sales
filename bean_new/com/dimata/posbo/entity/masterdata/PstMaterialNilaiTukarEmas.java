/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

public class PstMaterialNilaiTukarEmas extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MATERIALNILAITUKAREMAS = "pos_material_nilai_tukar_emas";
    public static final int FLD_NILAI_TUKAR_EMAS_ID = 0;
    public static final int FLD_KADAR_ID = 1;
    //update by dewok 20180420
    public static final int FLD_COLOR_ID = 2;    
    public static final int FLD_LOKAL = 3;
    public static final int FLD_RETURN_LOKAL = 4;
    public static final int FLD_RETURN_LOKAL_RUSAK = 5;
    public static final int FLD_ASING = 6;
    public static final int FLD_RETURN_ASING = 7;
    public static final int FLD_RETURN_ASING_RUSAK = 8;
    public static final int FLD_PESANAN = 9;
    public static final int FLD_TARIF_RETUR = 10;
    public static final int FLD_TARIF_RETUR_DIATAS_SETAHUN = 11;
    
    //update by dewok 20180420

    public static String[] fieldNames = {
        "NILAI_TUKAR_EMAS_ID",
        "KADAR_ID",
        "COLOR_ID",
        "LOKAL",
        "RETURN_LOKAL",
        "RETURN_LOKAL_RUSAK",
        "ASING",
        "RETURN_ASING",
        "RETURN_ASING_RUSAK",
        "PESANAN",
        "TARIF_RETUR",
        "TARIF_RETUR_DIATAS_SETAHUN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public PstMaterialNilaiTukarEmas() {
    }

    public PstMaterialNilaiTukarEmas(int i) throws DBException {
        super(new PstMaterialNilaiTukarEmas());
    }

    public PstMaterialNilaiTukarEmas(String sOid) throws DBException {
        super(new PstMaterialNilaiTukarEmas(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMaterialNilaiTukarEmas(long lOid) throws DBException {
        super(new PstMaterialNilaiTukarEmas(0));
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
        return TBL_MATERIALNILAITUKAREMAS;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialNilaiTukarEmas().getClass().getName();
    }

    public static MaterialNilaiTukarEmas fetchExc(long oid) throws DBException {
        try {
            MaterialNilaiTukarEmas entMaterialNilaiTukarEmas = new MaterialNilaiTukarEmas();
            PstMaterialNilaiTukarEmas pstMaterialNilaiTukarEmas = new PstMaterialNilaiTukarEmas(oid);
            entMaterialNilaiTukarEmas.setOID(oid);
            entMaterialNilaiTukarEmas.setKadarId(pstMaterialNilaiTukarEmas.getlong(FLD_KADAR_ID));
            entMaterialNilaiTukarEmas.setColorId(pstMaterialNilaiTukarEmas.getlong(FLD_COLOR_ID));
            entMaterialNilaiTukarEmas.setLokal(pstMaterialNilaiTukarEmas.getdouble(FLD_LOKAL));
            entMaterialNilaiTukarEmas.setReturnLokal(pstMaterialNilaiTukarEmas.getdouble(FLD_RETURN_LOKAL));
            entMaterialNilaiTukarEmas.setReturnLokalRusak(pstMaterialNilaiTukarEmas.getdouble(FLD_RETURN_LOKAL_RUSAK));
            entMaterialNilaiTukarEmas.setAsing(pstMaterialNilaiTukarEmas.getdouble(FLD_ASING));
            entMaterialNilaiTukarEmas.setReturnAsing(pstMaterialNilaiTukarEmas.getdouble(FLD_RETURN_ASING));
            entMaterialNilaiTukarEmas.setReturnAsingRusak(pstMaterialNilaiTukarEmas.getdouble(FLD_RETURN_ASING_RUSAK));
            entMaterialNilaiTukarEmas.setPesanan(pstMaterialNilaiTukarEmas.getdouble(FLD_PESANAN));
            entMaterialNilaiTukarEmas.setTarifRetur(pstMaterialNilaiTukarEmas.getdouble(FLD_TARIF_RETUR));
            entMaterialNilaiTukarEmas.setTarifReturDiatasSetahun(pstMaterialNilaiTukarEmas.getdouble(FLD_TARIF_RETUR_DIATAS_SETAHUN));
            return entMaterialNilaiTukarEmas;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialNilaiTukarEmas(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MaterialNilaiTukarEmas entMaterialNilaiTukarEmas = fetchExc(entity.getOID());
        entity = (Entity) entMaterialNilaiTukarEmas;
        return entMaterialNilaiTukarEmas.getOID();
    }

    public static synchronized long updateExc(MaterialNilaiTukarEmas entMaterialNilaiTukarEmas) throws DBException {
        try {
            if (entMaterialNilaiTukarEmas.getOID() != 0) {
                PstMaterialNilaiTukarEmas pstMaterialNilaiTukarEmas = new PstMaterialNilaiTukarEmas(entMaterialNilaiTukarEmas.getOID());
                pstMaterialNilaiTukarEmas.setLong(FLD_KADAR_ID, entMaterialNilaiTukarEmas.getKadarId());
                pstMaterialNilaiTukarEmas.setLong(FLD_COLOR_ID, entMaterialNilaiTukarEmas.getColorId());
                pstMaterialNilaiTukarEmas.setDouble(FLD_LOKAL, entMaterialNilaiTukarEmas.getLokal());
                pstMaterialNilaiTukarEmas.setDouble(FLD_RETURN_LOKAL, entMaterialNilaiTukarEmas.getReturnLokal());
                pstMaterialNilaiTukarEmas.setDouble(FLD_RETURN_LOKAL_RUSAK, entMaterialNilaiTukarEmas.getReturnLokalRusak());
                pstMaterialNilaiTukarEmas.setDouble(FLD_ASING, entMaterialNilaiTukarEmas.getAsing());
                pstMaterialNilaiTukarEmas.setDouble(FLD_RETURN_ASING, entMaterialNilaiTukarEmas.getReturnAsing());
                pstMaterialNilaiTukarEmas.setDouble(FLD_RETURN_ASING_RUSAK, entMaterialNilaiTukarEmas.getReturnAsingRusak());
                pstMaterialNilaiTukarEmas.setDouble(FLD_PESANAN, entMaterialNilaiTukarEmas.getPesanan());
                pstMaterialNilaiTukarEmas.setDouble(FLD_TARIF_RETUR, entMaterialNilaiTukarEmas.getTarifRetur());
                pstMaterialNilaiTukarEmas.setDouble(FLD_TARIF_RETUR_DIATAS_SETAHUN, entMaterialNilaiTukarEmas.getTarifReturDiatasSetahun());
                pstMaterialNilaiTukarEmas.update();
                return entMaterialNilaiTukarEmas.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialNilaiTukarEmas(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MaterialNilaiTukarEmas) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMaterialNilaiTukarEmas pstMaterialNilaiTukarEmas = new PstMaterialNilaiTukarEmas(oid);
            pstMaterialNilaiTukarEmas.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialNilaiTukarEmas(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MaterialNilaiTukarEmas entMaterialNilaiTukarEmas) throws DBException {
        try {
            PstMaterialNilaiTukarEmas pstMaterialNilaiTukarEmas = new PstMaterialNilaiTukarEmas(0);
            pstMaterialNilaiTukarEmas.setLong(FLD_KADAR_ID, entMaterialNilaiTukarEmas.getKadarId());
            pstMaterialNilaiTukarEmas.setLong(FLD_COLOR_ID, entMaterialNilaiTukarEmas.getColorId());
            pstMaterialNilaiTukarEmas.setDouble(FLD_LOKAL, entMaterialNilaiTukarEmas.getLokal());
            pstMaterialNilaiTukarEmas.setDouble(FLD_RETURN_LOKAL, entMaterialNilaiTukarEmas.getReturnLokal());
            pstMaterialNilaiTukarEmas.setDouble(FLD_RETURN_LOKAL_RUSAK, entMaterialNilaiTukarEmas.getReturnLokalRusak());
            pstMaterialNilaiTukarEmas.setDouble(FLD_ASING, entMaterialNilaiTukarEmas.getAsing());
            pstMaterialNilaiTukarEmas.setDouble(FLD_RETURN_ASING, entMaterialNilaiTukarEmas.getReturnAsing());
            pstMaterialNilaiTukarEmas.setDouble(FLD_RETURN_ASING_RUSAK, entMaterialNilaiTukarEmas.getReturnAsingRusak());
            pstMaterialNilaiTukarEmas.setDouble(FLD_PESANAN, entMaterialNilaiTukarEmas.getPesanan());
            pstMaterialNilaiTukarEmas.setDouble(FLD_TARIF_RETUR, entMaterialNilaiTukarEmas.getTarifRetur());
            pstMaterialNilaiTukarEmas.setDouble(FLD_TARIF_RETUR_DIATAS_SETAHUN, entMaterialNilaiTukarEmas.getTarifReturDiatasSetahun());
            pstMaterialNilaiTukarEmas.insert();
            entMaterialNilaiTukarEmas.setOID(pstMaterialNilaiTukarEmas.getlong(FLD_NILAI_TUKAR_EMAS_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialNilaiTukarEmas(0), DBException.UNKNOWN);
        }
        return entMaterialNilaiTukarEmas.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MaterialNilaiTukarEmas) entity);
    }

    public static void resultToObject(ResultSet rs, MaterialNilaiTukarEmas entMaterialNilaiTukarEmas) {
        try {
            entMaterialNilaiTukarEmas.setOID(rs.getLong(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_NILAI_TUKAR_EMAS_ID]));
            entMaterialNilaiTukarEmas.setKadarId(rs.getLong(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID]));
            entMaterialNilaiTukarEmas.setColorId(rs.getLong(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_COLOR_ID]));
            entMaterialNilaiTukarEmas.setLokal(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_LOKAL]));
            entMaterialNilaiTukarEmas.setReturnLokal(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_LOKAL]));
            entMaterialNilaiTukarEmas.setReturnLokalRusak(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_LOKAL_RUSAK]));
            entMaterialNilaiTukarEmas.setAsing(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_ASING]));
            entMaterialNilaiTukarEmas.setReturnAsing(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_ASING]));
            entMaterialNilaiTukarEmas.setReturnAsingRusak(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_ASING_RUSAK]));
            entMaterialNilaiTukarEmas.setPesanan(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_PESANAN]));
            entMaterialNilaiTukarEmas.setTarifRetur(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_TARIF_RETUR]));
            entMaterialNilaiTukarEmas.setTarifReturDiatasSetahun(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_TARIF_RETUR_DIATAS_SETAHUN]));
        } catch (Exception e) {
        }
    }
    
    public static void resultToObjectJoin(ResultSet rs, MaterialNilaiTukarEmas entMaterialNilaiTukarEmas) {
        try {
            entMaterialNilaiTukarEmas.setOID(rs.getLong(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_NILAI_TUKAR_EMAS_ID]));
            entMaterialNilaiTukarEmas.setKadarId(rs.getLong(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID]));
            entMaterialNilaiTukarEmas.setColorId(rs.getLong(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_COLOR_ID]));
            entMaterialNilaiTukarEmas.setLokal(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_LOKAL]));
            entMaterialNilaiTukarEmas.setReturnLokal(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_LOKAL]));
            entMaterialNilaiTukarEmas.setReturnLokalRusak(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_LOKAL_RUSAK]));
            entMaterialNilaiTukarEmas.setAsing(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_ASING]));
            entMaterialNilaiTukarEmas.setReturnAsing(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_ASING]));
            entMaterialNilaiTukarEmas.setReturnAsingRusak(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_ASING_RUSAK]));
            entMaterialNilaiTukarEmas.setPesanan(rs.getDouble(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_PESANAN]));
            entMaterialNilaiTukarEmas.setKadarName(rs.getString("KADAR"));
            entMaterialNilaiTukarEmas.setWarnaName(rs.getString("color_name"));
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
            String sql = "SELECT * FROM " + TBL_MATERIALNILAITUKAREMAS;
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
                MaterialNilaiTukarEmas entMaterialNilaiTukarEmas = new MaterialNilaiTukarEmas();
                resultToObject(rs, entMaterialNilaiTukarEmas);
                lists.add(entMaterialNilaiTukarEmas);
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
    
    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
           String sql = " SELECT pos_material_nilai_tukar_emas.*,pos_kadar.KADAR, pos_color.color_name  FROM pos_material_nilai_tukar_emas " +
                        " INNER JOIN pos_kadar ON pos_material_nilai_tukar_emas.KADAR_ID=pos_kadar.KADAR_ID " +
                        " INNER JOIN pos_color ON pos_material_nilai_tukar_emas.WARNA_ID=pos_color.color_id ";
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
                MaterialNilaiTukarEmas entMaterialNilaiTukarEmas = new MaterialNilaiTukarEmas();
                resultToObjectJoin(rs, entMaterialNilaiTukarEmas);
                lists.add(entMaterialNilaiTukarEmas);
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

    public static boolean checkOID(long entMaterialNilaiTukarEmasId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIALNILAITUKAREMAS + " WHERE "
                    + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_NILAI_TUKAR_EMAS_ID] + " = " + entMaterialNilaiTukarEmasId;
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
            String sql = "SELECT COUNT(" + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_NILAI_TUKAR_EMAS_ID] + ") FROM " + TBL_MATERIALNILAITUKAREMAS;
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
                    MaterialNilaiTukarEmas entMaterialNilaiTukarEmas = (MaterialNilaiTukarEmas) list.get(ls);
                    if (oid == entMaterialNilaiTukarEmas.getOID()) {
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
        } else if (start == (vectSize - recordToGet)) {
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
        return cmd;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MaterialNilaiTukarEmas materialNilaiTukarEmas = PstMaterialNilaiTukarEmas.fetchExc(oid);
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_NILAI_TUKAR_EMAS_ID], materialNilaiTukarEmas.getOID());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_ASING], materialNilaiTukarEmas.getAsing());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_COLOR_ID], materialNilaiTukarEmas.getColorId());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID], materialNilaiTukarEmas.getKadarId());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_LOKAL], materialNilaiTukarEmas.getLokal());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_TARIF_RETUR_DIATAS_SETAHUN], materialNilaiTukarEmas.getTarifReturDiatasSetahun());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_PESANAN], materialNilaiTukarEmas.getPesanan());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_ASING], materialNilaiTukarEmas.getReturnAsing());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_ASING_RUSAK], materialNilaiTukarEmas.getReturnAsingRusak());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_LOKAL], materialNilaiTukarEmas.getLokal());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_LOKAL_RUSAK], materialNilaiTukarEmas.getReturnLokalRusak());
                object.put(PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_TARIF_RETUR], materialNilaiTukarEmas.getTarifRetur());
            }catch(Exception exc){}

            return object;
        }
}
