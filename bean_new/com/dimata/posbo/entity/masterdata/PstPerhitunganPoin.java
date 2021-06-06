/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class PstPerhitunganPoin extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PERHITUNGAN_POIN = "pos_perhitungan_poin";
    public static final int FLD_PERHITUNGAN_POIN_ID = 0;
    public static final int FLD_MATERIAL_JENIS_TYPE = 1;
    public static final int FLD_SELL_VALUE = 2;
    public static final int FLD_POIN_REWARD = 3;
    public static final int FLD_UPDATE_DATE = 4;
    public static final int FLD_STATUS_AKTIF = 5;

    public static String[] fieldNames = {
        "PERHITUNGAN_POIN_ID",
        "MATERIAL_JENIS_TYPE",
        "SELL_VALUE",
        "POIN_REWARD",
        "UPDATE_DATE",
        "STATUS_AKTIF"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_INT
    };

    public PstPerhitunganPoin() {
    }

    public PstPerhitunganPoin(int i) throws DBException {
        super(new PstPerhitunganPoin());
    }

    public PstPerhitunganPoin(String sOid) throws DBException {
        super(new PstPerhitunganPoin(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPerhitunganPoin(long lOid) throws DBException {
        super(new PstPerhitunganPoin(0));
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
        return TBL_PERHITUNGAN_POIN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPerhitunganPoin().getClass().getName();
    }

    public static PerhitunganPoin fetchExc(long oid) throws DBException {
        try {
            PerhitunganPoin entPerhitunganPoin = new PerhitunganPoin();
            PstPerhitunganPoin pstPerhitunganPoin = new PstPerhitunganPoin(oid);
            entPerhitunganPoin.setOID(oid);
            entPerhitunganPoin.setMaterialJenisType(pstPerhitunganPoin.getInt(FLD_MATERIAL_JENIS_TYPE));
            entPerhitunganPoin.setSellValue(pstPerhitunganPoin.getdouble(FLD_SELL_VALUE));
            entPerhitunganPoin.setPoinReward(pstPerhitunganPoin.getInt(FLD_POIN_REWARD));
            entPerhitunganPoin.setUpdateDate(pstPerhitunganPoin.getDate(FLD_UPDATE_DATE));
            entPerhitunganPoin.setStatusAktif(pstPerhitunganPoin.getInt(FLD_STATUS_AKTIF));
            return entPerhitunganPoin;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPerhitunganPoin(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        PerhitunganPoin entPerhitunganPoin = fetchExc(entity.getOID());
        entity = (Entity) entPerhitunganPoin;
        return entPerhitunganPoin.getOID();
    }

    public static synchronized long updateExc(PerhitunganPoin entPerhitunganPoin) throws DBException {
        try {
            if (entPerhitunganPoin.getOID() != 0) {
                PstPerhitunganPoin pstPerhitunganPoin = new PstPerhitunganPoin(entPerhitunganPoin.getOID());
                pstPerhitunganPoin.setInt(FLD_MATERIAL_JENIS_TYPE, entPerhitunganPoin.getMaterialJenisType());
                pstPerhitunganPoin.setDouble(FLD_SELL_VALUE, entPerhitunganPoin.getSellValue());
                pstPerhitunganPoin.setInt(FLD_POIN_REWARD, entPerhitunganPoin.getPoinReward());
                pstPerhitunganPoin.setDate(FLD_UPDATE_DATE, entPerhitunganPoin.getUpdateDate());
                pstPerhitunganPoin.setInt(FLD_STATUS_AKTIF, entPerhitunganPoin.getStatusAktif());
                pstPerhitunganPoin.update();
                return entPerhitunganPoin.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPerhitunganPoin(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((PerhitunganPoin) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstPerhitunganPoin pstPerhitunganPoin = new PstPerhitunganPoin(oid);
            pstPerhitunganPoin.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPerhitunganPoin(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(PerhitunganPoin entPerhitunganPoin) throws DBException {
        try {
            PstPerhitunganPoin pstPerhitunganPoin = new PstPerhitunganPoin(0);
            pstPerhitunganPoin.setInt(FLD_MATERIAL_JENIS_TYPE, entPerhitunganPoin.getMaterialJenisType());
            pstPerhitunganPoin.setDouble(FLD_SELL_VALUE, entPerhitunganPoin.getSellValue());
            pstPerhitunganPoin.setInt(FLD_POIN_REWARD, entPerhitunganPoin.getPoinReward());
            pstPerhitunganPoin.setDate(FLD_UPDATE_DATE, entPerhitunganPoin.getUpdateDate());
            pstPerhitunganPoin.setInt(FLD_STATUS_AKTIF, entPerhitunganPoin.getStatusAktif());
            pstPerhitunganPoin.insert();
            entPerhitunganPoin.setOID(pstPerhitunganPoin.getlong(FLD_PERHITUNGAN_POIN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPerhitunganPoin(0), DBException.UNKNOWN);
        }
        return entPerhitunganPoin.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((PerhitunganPoin) entity);
    }

    public static void resultToObject(ResultSet rs, PerhitunganPoin entPerhitunganPoin) {
        try {
            entPerhitunganPoin.setOID(rs.getLong(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_PERHITUNGAN_POIN_ID]));
            entPerhitunganPoin.setMaterialJenisType(rs.getInt(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_MATERIAL_JENIS_TYPE]));
            entPerhitunganPoin.setSellValue(rs.getDouble(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_SELL_VALUE]));
            entPerhitunganPoin.setPoinReward(rs.getInt(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_POIN_REWARD]));
            entPerhitunganPoin.setUpdateDate(rs.getTimestamp(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_UPDATE_DATE]));
            entPerhitunganPoin.setStatusAktif(rs.getInt(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_STATUS_AKTIF]));
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
            String sql = "SELECT * FROM " + TBL_PERHITUNGAN_POIN;
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
                PerhitunganPoin entPerhitunganPoin = new PerhitunganPoin();
                resultToObject(rs, entPerhitunganPoin);
                lists.add(entPerhitunganPoin);
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

    public static boolean checkOID(long entPerhitunganPoinId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PERHITUNGAN_POIN + " WHERE "
                    + PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_PERHITUNGAN_POIN_ID] + " = " + entPerhitunganPoinId;
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
            String sql = "SELECT COUNT(" + PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_PERHITUNGAN_POIN_ID] + ") FROM " + TBL_PERHITUNGAN_POIN;
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
                    PerhitunganPoin entPerhitunganPoin = (PerhitunganPoin) list.get(ls);
                    if (oid == entPerhitunganPoin.getOID()) {
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
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                PerhitunganPoin perhitunganPoin = PstPerhitunganPoin.fetchExc(oid);
                object.put(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_PERHITUNGAN_POIN_ID], perhitunganPoin.getOID());
                object.put(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_MATERIAL_JENIS_TYPE], perhitunganPoin.getMaterialJenisType());
                object.put(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_POIN_REWARD], perhitunganPoin.getPoinReward());
                object.put(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_SELL_VALUE], perhitunganPoin.getSellValue());
                object.put(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_STATUS_AKTIF], perhitunganPoin.getStatusAktif());
                object.put(PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_UPDATE_DATE], perhitunganPoin.getUpdateDate());
            }catch(Exception exc){}

            return object;
        }
}
