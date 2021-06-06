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

public class PstEmasLantakan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_EMASLANTAKAN = "pos_material_emas_lantakan";
    public static final int FLD_EMAS_LANTAKAN_ID = 0;
    public static final int FLD_HARGA_BELI = 1;
    public static final int FLD_HARGA_JUAL = 2;
    public static final int FLD_HARGA_TENGAH = 3;
    public static final int FLD_START_DATE = 4;
    public static final int FLD_END_DATE = 5;
    public static final int FLD_STATUS_AKTIF = 6;

    public static String[] fieldNames = {
        "EMAS_LANTAKAN_ID",
        "HARGA_BELI",
        "HARGA_JUAL",
        "HARGA_TENGAH",
        "START_DATE",
        "END_DATE",
        "STATUS_AKTIF"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT
    };

    public PstEmasLantakan() {
    }

    public PstEmasLantakan(int i) throws DBException {
        super(new PstEmasLantakan());
    }

    public PstEmasLantakan(String sOid) throws DBException {
        super(new PstEmasLantakan(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstEmasLantakan(long lOid) throws DBException {
        super(new PstEmasLantakan(0));
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
        return TBL_EMASLANTAKAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstEmasLantakan().getClass().getName();
    }

    public static EmasLantakan fetchExc(long oid) throws DBException {
        try {
            EmasLantakan entEmasLantakan = new EmasLantakan();
            PstEmasLantakan pstEmasLantakan = new PstEmasLantakan(oid);
            entEmasLantakan.setOID(oid);
            entEmasLantakan.setHargaBeli(pstEmasLantakan.getdouble(FLD_HARGA_BELI));
            entEmasLantakan.setHargaJual(pstEmasLantakan.getdouble(FLD_HARGA_JUAL));
            entEmasLantakan.setHargaTengah(pstEmasLantakan.getdouble(FLD_HARGA_TENGAH));
            entEmasLantakan.setStartDate(pstEmasLantakan.getDate(FLD_START_DATE));
            entEmasLantakan.setEndDate(pstEmasLantakan.getDate(FLD_END_DATE));
            entEmasLantakan.setStatusAktif(pstEmasLantakan.getInt(FLD_STATUS_AKTIF));
            return entEmasLantakan;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmasLantakan(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        EmasLantakan entEmasLantakan = fetchExc(entity.getOID());
        entity = (Entity) entEmasLantakan;
        return entEmasLantakan.getOID();
    }

    public static synchronized long updateExc(EmasLantakan entEmasLantakan) throws DBException {
        try {
            if (entEmasLantakan.getOID() != 0) {
                PstEmasLantakan pstEmasLantakan = new PstEmasLantakan(entEmasLantakan.getOID());
                pstEmasLantakan.setDouble(FLD_HARGA_BELI, entEmasLantakan.getHargaBeli());
                pstEmasLantakan.setDouble(FLD_HARGA_JUAL, entEmasLantakan.getHargaJual());
                pstEmasLantakan.setDouble(FLD_HARGA_TENGAH, entEmasLantakan.getHargaTengah());
                pstEmasLantakan.setDate(FLD_START_DATE, entEmasLantakan.getStartDate());
                pstEmasLantakan.setDate(FLD_END_DATE, entEmasLantakan.getEndDate());
                pstEmasLantakan.setInt(FLD_STATUS_AKTIF, entEmasLantakan.getStatusAktif());
                pstEmasLantakan.update();
                return entEmasLantakan.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmasLantakan(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((EmasLantakan) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstEmasLantakan pstEmasLantakan = new PstEmasLantakan(oid);
            pstEmasLantakan.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmasLantakan(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(EmasLantakan entEmasLantakan) throws DBException {
        try {
            PstEmasLantakan pstEmasLantakan = new PstEmasLantakan(0);
            pstEmasLantakan.setDouble(FLD_HARGA_BELI, entEmasLantakan.getHargaBeli());
            pstEmasLantakan.setDouble(FLD_HARGA_JUAL, entEmasLantakan.getHargaJual());
            pstEmasLantakan.setDouble(FLD_HARGA_TENGAH, entEmasLantakan.getHargaTengah());
            pstEmasLantakan.setDate(FLD_START_DATE, entEmasLantakan.getStartDate());
            pstEmasLantakan.setDate(FLD_END_DATE, entEmasLantakan.getEndDate());
            pstEmasLantakan.setInt(FLD_STATUS_AKTIF, entEmasLantakan.getStatusAktif());
            pstEmasLantakan.insert();
            entEmasLantakan.setOID(pstEmasLantakan.getlong(FLD_EMAS_LANTAKAN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmasLantakan(0), DBException.UNKNOWN);
        }
        return entEmasLantakan.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((EmasLantakan) entity);
    }

    public static void resultToObject(ResultSet rs, EmasLantakan entEmasLantakan) {
        try {
            entEmasLantakan.setOID(rs.getLong(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_EMAS_LANTAKAN_ID]));
            entEmasLantakan.setHargaBeli(rs.getDouble(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_BELI]));
            entEmasLantakan.setHargaJual(rs.getDouble(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_JUAL]));
            entEmasLantakan.setHargaTengah(rs.getDouble(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_TENGAH]));
            entEmasLantakan.setStartDate(rs.getDate(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_START_DATE]));
            entEmasLantakan.setEndDate(rs.getDate(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_END_DATE]));
            entEmasLantakan.setStatusAktif(rs.getInt(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_STATUS_AKTIF]));
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
            String sql = "SELECT * FROM " + TBL_EMASLANTAKAN;
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
                EmasLantakan entEmasLantakan = new EmasLantakan();
                resultToObject(rs, entEmasLantakan);
                lists.add(entEmasLantakan);
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

    public static boolean checkOID(long entEmasLantakanId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_EMASLANTAKAN + " WHERE "
                    + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_EMAS_LANTAKAN_ID] + " = " + entEmasLantakanId;
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
            String sql = "SELECT COUNT(" + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_EMAS_LANTAKAN_ID] + ") FROM " + TBL_EMASLANTAKAN;
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
                    EmasLantakan entEmasLantakan = (EmasLantakan) list.get(ls);
                    if (oid == entEmasLantakan.getOID()) {
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
    
    public static EmasLantakan getEmasLantakan() {
        Vector listEmasLantakan = new Vector();
        listEmasLantakan = list(0, 1, fieldNames[FLD_STATUS_AKTIF] + " = 1", null);
        if (!listEmasLantakan.isEmpty()) {
            return (EmasLantakan) listEmasLantakan.get(0);
        } else {
            return new EmasLantakan();
        }
    }
    
    public static int updateStatusEmasLantakan(long newEmasLantakanId) {
        int upd = 0;
        try {
            String sql = "UPDATE " + TBL_EMASLANTAKAN 
                    + " SET " + fieldNames[FLD_STATUS_AKTIF] + " = 0"
                    + " WHERE " + fieldNames[FLD_EMAS_LANTAKAN_ID] + " <> " + newEmasLantakanId;
            upd = DBHandler.execUpdate(sql);
            return upd;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return upd;        
    }
  
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                EmasLantakan emasLantakan = PstEmasLantakan.fetchExc(oid);
                object.put(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_EMAS_LANTAKAN_ID], emasLantakan.getOID());
                object.put(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_END_DATE], emasLantakan.getEndDate());
                object.put(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_BELI], emasLantakan.getHargaBeli());
                object.put(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_JUAL], emasLantakan.getHargaJual());
                object.put(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_TENGAH], emasLantakan.getHargaTengah());
                object.put(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_START_DATE], emasLantakan.getStartDate());
                object.put(PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_STATUS_AKTIF], emasLantakan.getStatusAktif());
            }catch(Exception exc){}

            return object;
        }
}
